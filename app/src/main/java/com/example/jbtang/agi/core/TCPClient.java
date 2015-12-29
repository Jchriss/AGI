package com.example.jbtang.agi.core;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by jbtang on 9/28/2015.
 * TCP connection client.
 */
public class TCPClient implements Runnable {
    /**
     * handle data sent from TCPClient
     */
    private Handler outHandler;

    /**
     * handle data sent into TCPClient, and the data will be sent to network
     */
    private Handler inHandler;

    /**
     * message: send control bytes to board and receive ACK
     * data: receive data info
     */
    private Socket messageSocket;
    private Socket dataSocket;
    private OutputStream out;
    private String IP;
    private int messagePort;
    private int dataPort;

    private static final int TIME_OUT = 5000;
    private static final String TAG = "TCPClient";

    public TCPClient(String IP, int dataPort, int messagePort, Handler outHandler) {
        this.IP = IP;
        this.dataPort = dataPort;
        this.messagePort = messagePort;
        this.outHandler = outHandler;
    }

    @Override
    public void run() {
        try {
            init();
            sendMsgToNetwork();
        } catch (SocketTimeoutException e) {
            outHandlerMsgSend("Socket connection time out!", outHandler, Status.HandlerMsgStatus.TCPCLIENT_FAILED);
            Log.e(TAG, "Socket connection time out, IP address is " + IP, e);
            dispose();
        } catch (IOException e) {
            outHandlerMsgSend("TCPClient exception!", outHandler, Status.HandlerMsgStatus.TCPCLIENT_FAILED);
            Log.e(TAG, "TCPClient exception, IP address is " + IP, e);
            dispose();
        }
    }

    public void send(Message msg) {
        try {
            inHandler.sendMessage(msg);
        } catch (Exception e) {
            Log.e(TAG, "Send message failed,IP address is " + IP, e);
        }
    }

    /**
     * initial socket and restart data receive thread
     */
    private void init() throws IOException {
        if (messageSocket == null) {
            InetSocketAddress address = new InetSocketAddress(IP, messagePort);
            messageSocket = new Socket();
            messageSocket.connect(address, TIME_OUT);
            out = messageSocket.getOutputStream();
        }
        if (dataSocket == null) {
            InetSocketAddress address = new InetSocketAddress(IP, dataPort);
            dataSocket = new Socket();
            dataSocket.connect(address, TIME_OUT);
        }
    }

    public InputStream getMessageStream() throws IOException {
        if (messageSocket != null) {
            return messageSocket.getInputStream();
        }
        return null;
    }

    public InputStream getDataStream() throws IOException {
        if (dataSocket != null) {
            return dataSocket.getInputStream();
        }
        return null;
    }

    private void sendMsgToNetwork() {
        Looper.prepare();
        inHandler = new myHandler(this);
        Looper.loop();
    }

    static class myHandler extends Handler {
        private final WeakReference<TCPClient> mOuter;

        public myHandler(TCPClient client) {
            mOuter = new WeakReference<>(client);
        }

        /**
         * if socket is shut down and 'broken pipe' exception is gotten, close old resource first and initial socket
         */
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == Status.HandlerMsgStatus.IN_TO_TCPCLIENT.ordinal()) {
                try {
                    mOuter.get().init();
                    Log.d(TAG, "Message send: " + MsgSendHelper.convertBytesToString((byte[]) msg.obj));
                    mOuter.get().out.write((byte[]) msg.obj);
                } catch (IOException e) {
                    Log.e(TAG, "Message send exception.", e);
                    mOuter.get().dispose();
                }
            }
        }
    }

    /**
     * send message to upper layer
     */
    private void outHandlerMsgSend(Object obj, Handler handler, Status.HandlerMsgStatus status) {
        Message msg = new Message();
        msg.what = status.ordinal();
        msg.obj = obj;
        handler.sendMessage(msg);
    }

    public void dispose() {
        closeOutputStream();
        closeMessageSocket();
        closeDataSocket();
    }

    private void closeOutputStream() {
        if (out != null) {
            try {
                out.close();
                out = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeMessageSocket() {
        if (messageSocket != null) {
            try {
                messageSocket.close();
                messageSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void closeDataSocket() {
        if (dataSocket != null) {
            try {
                dataSocket.close();
                dataSocket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
