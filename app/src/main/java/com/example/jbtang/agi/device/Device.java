package com.example.jbtang.agi.device;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.jbtang.agi.core.Global;
import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.Status;
import com.example.jbtang.agi.core.TCPClient;
import com.example.jbtang.agi.messages.GetFrequentlyUsedMsg;
import com.example.jbtang.agi.messages.MessageDispatcher;
import com.example.jbtang.agi.messages.base.MsgHeader;
import com.example.jbtang.agi.messages.base.MsgTypes;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

/**
 * Base device class
 * Created by jbtang on 9/29/2015.
 */
public class Device {
    private String name;
    private TCPClient client;
    private String IP;
    private int messagePort;
    private int dataPort;
    protected Status.DeviceStatus status;
    private InputStream ackIn;
    private InputStream dataIn;

    private static final int Data_RECEIVE_BUFFER_SIZE = 10240;
    private static final int Message_RECEIVE_BUFFER_SIZE = 256;
    protected static final String TAG = "Device";

    public String getName() {
        return name;
    }

    public int getMessagePort() {
        return messagePort;
    }

    public int getDataPort() {
        return dataPort;
    }

    public String getIP() {
        return IP;
    }

    public boolean isConnected() {
        return status != Status.DeviceStatus.DISCONNECTED;
    }

    public Device(String name, String IP, int dataPort, int messagePort) {
        this.name = name;
        this.IP = IP;
        this.dataPort = dataPort;
        this.messagePort = messagePort;
        this.status = Status.DeviceStatus.DISCONNECTED;
    }

    public void connect() throws Exception {
        if (status != Status.DeviceStatus.DISCONNECTED) {
            return;
        }
        if (client == null) {
            client = new TCPClient(IP, dataPort, messagePort, new myHandler(this));
            Global.ThreadPool.cachedThreadPool.execute(client);
            Thread.sleep(200);
            messageReceive();
            dataReceive();
        }
        if (status == Status.DeviceStatus.DISCONNECTED) {
            send(GetFrequentlyUsedMsg.getDeviceStateMsg);
        }
        Thread.sleep(100);
    }

    public void disconnect() throws Exception {
        if (client == null || status == Status.DeviceStatus.DISCONNECTED) {
            return;
        }
        if (status == Status.DeviceStatus.WORKING) {
            send(GetFrequentlyUsedMsg.protocalTraceRelMsg);
            Thread.sleep(200);
        }
        dispose();
    }

    public void send(byte[] bytes) {
        if (client != null) {
            Message msg = new Message();
            msg.what = Status.HandlerMsgStatus.IN_TO_TCPCLIENT.ordinal();
            msg.obj = bytes;
            client.send(msg);
        }
    }

    static class myHandler extends Handler {
        private final WeakReference<Device> mOuter;

        public myHandler(Device device) {
            mOuter = new WeakReference<>(device);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Status.HandlerMsgStatus.TCPCLIENT_FAILED.ordinal()) {
                Log.e(TAG, "TCPClient exception.");
                mOuter.get().dispose();
            }
        }
    }

    private void messageReceive() throws IOException {
        ackIn = client.getMessageStream();
        if (ackIn == null) {
            return;
        }
        Global.ThreadPool.cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] buffer = new byte[Message_RECEIVE_BUFFER_SIZE];
                    while (ackIn.read(buffer) != -1) {
                        MsgHeader header = new MsgHeader(MsgSendHelper.getSubByteArray(buffer, 0, MsgHeader.byteArrayLen));
                        changeStatus(header.getMsgType());
                        Log.d(TAG, "Status : -----------" + status.name());
                        Log.d(TAG, String.format("Message type: %x", header.getMsgType()));
                        Log.d(TAG, "Receive from message port : " + convertByteToString(buffer));
                        if (header.getMsgType() == 0xffff) {
                            Log.d(TAG, "strange ACK!");
                            throw new IOException();
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Message receive exception.", e);
                    dispose();
                }
            }
        });
    }

    private void dataReceive() throws IOException {
        dataIn = client.getDataStream();
        if (dataIn == null) {
            return;
        }
        Global.ThreadPool.cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] buffer = new byte[Data_RECEIVE_BUFFER_SIZE];
                    while (dataIn.read(buffer) != -1) {
                        MsgHeader header = new MsgHeader(MsgSendHelper.getSubByteArray(buffer, 0, MsgHeader.byteArrayLen));
                        changeStatus(header.getMsgType());
                        Log.d(TAG, "Status : -----------" + status.name());
                        Log.d(TAG, String.format("Message type: %x", header.getMsgType()));
                        Log.d(TAG, "Receive from data port : " + convertByteToString(buffer));
                        if (header.getMsgType() != 0x8002) {
                            MessageDispatcher.getInstance().Dispatch(new Global.GlobalMsg(name, header.getMsgType(), buffer));
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Data receive exception.", e);
                    dispose();
                }
            }
        });
    }

    private void changeStatus(int msgType) {
        switch (status) {
            case DISCONNECTED:
                status = Status.DeviceStatus.IDLE;
                break;
            case IDLE:
                if (msgType == MsgTypes.AG_PC_PROTOCOL_TRACE_REQ_ACK_MSG_TYPE ||
                        msgType == MsgTypes.L2P_AG_UE_CAPTURE_IND_MSG_TYPE ||
                        msgType == MsgTypes.L1_AG_PHY_COMMEAS_IND_MSG_TYPE) {
                    status = Status.DeviceStatus.WORKING;
                }
                break;
            case WORKING:
                if (msgType == MsgTypes.AG_PC_PROTOCOL_TRACE_REL_ACK_MSG_TYPE ||
                        msgType == MsgTypes.L1_AG_PROTOCOL_TRACE_REL_ACK_MSG_TYPE) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {

                    }
                    status = Status.DeviceStatus.IDLE;
                }
                break;
            default:
                break;
        }
    }

    public void dispose() {
        //closeReceiveThread();
        closeACKInputStream();
        closeDataInputStream();
        closeTCPClient();
        status = Status.DeviceStatus.DISCONNECTED;
    }

    /*private void closeReceiveThread() {
        if (messageRevThread.isAlive()) {
            messageRevThread.interrupt();
        }
        messageRevThread = null;
        if (dataRevThread.isAlive()) {
            dataRevThread.interrupt();
        }
        dataRevThread = null;
    }*/

    private void closeACKInputStream() {
        if (ackIn != null) {
            try {
                ackIn.close();
                ackIn = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeDataInputStream() {
        if (dataIn != null) {
            try {
                dataIn.close();
                dataIn = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeTCPClient() {
        if (client != null) {
            client.dispose();
            client = null;
        }
    }

    private String convertByteToString(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.valueOf(b)).append(", ");
        }
        return builder.toString();
    }

}
