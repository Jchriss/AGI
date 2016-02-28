package com.example.jbtang.agi.device;

import android.os.AsyncTask;
import android.util.Log;

import com.example.jbtang.agi.core.CellInfo;
import com.example.jbtang.agi.core.Global;
import com.example.jbtang.agi.core.Status;
import com.example.jbtang.agi.messages.GenProtocolTraceMsg;
import com.example.jbtang.agi.messages.GetFrequentlyUsedMsg;
import com.example.jbtang.agi.service.OrientationFinding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

/**
 * Created by jbtang on 11/1/2015.
 */
public class MonitorDevice extends Device {

    public static final String DEVICE_NAME_PREFIX = "AGI";
    public static final int DATA_PORT = 3333;
    public static final int MESSAGE_PORT = 3334;

    private static final String REBOOT_USERNAME = "root";
    private static final String REBOOT_PASSWORD = "13M1877";
    private static final int REBOOT_PORT = 22;
    private static final String REBOOT_CMD = "reboot";

    private CellInfo cellInfo;
    private boolean isReadyToMonitor;
    private Status.BoardType type;
    private Status.DeviceWorkingStatus workingStatus;
    private Status.PingResult pingStatus;
    private AsyncTask task;

    public Status.PingResult getPingStatus() {
        return pingStatus;
    }

    public Status.DeviceWorkingStatus getWorkingStatus() {
        return workingStatus;
    }

    public void setWorkingStatus(Status.DeviceWorkingStatus workingStatus) {
        this.workingStatus = workingStatus;
    }

    public Status.BoardType getType() {
        return type;
    }

    public CellInfo getCellInfo() {
        return cellInfo;
    }

    public void setCellInfo(CellInfo cellInfo) {
        this.cellInfo = cellInfo;
    }

    public MonitorDevice(String name, String IP, Status.BoardType type) {
        super(name, IP, DATA_PORT, MESSAGE_PORT);
        this.isReadyToMonitor = false;
        this.type = type;
        this.workingStatus = Status.DeviceWorkingStatus.ABNORMAL;
        this.pingStatus = Status.PingResult.FAILED;
        this.task = new NetPing().execute();
    }

    public boolean isReady() {
        if (pingStatus == Status.PingResult.FAILED) {
            return false;
        }
        if (status == Status.DeviceStatus.DISCONNECTED) {
            return false;
        }
        if (!isReadyToMonitor) {
            return false;
        }
        if (status == Status.DeviceStatus.WORKING) {
            Log.d(TAG, "------------------------ Restart ---------------------");
            send(GetFrequentlyUsedMsg.protocalTraceRelMsg);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {

            }
        }
        return status == Status.DeviceStatus.IDLE;
    }

    public void startMonitor(Status.Service service) {
        if (!isReady()) {
            return;
        }
        if (!validateCellInfo()) {
            return;
        }
        switch (service) {
            case FINDSTMIS:
                send(GenProtocolTraceMsg.gen((byte) 2, cellInfo.earfcn, cellInfo.pci, new byte[]{}));
                break;
            case ORIENTATION:
                byte[] stmsi = OrientationFinding.getInstance().targetStmsi.getBytes();
                if (!validateSTMSI(stmsi)) {
                    return;
                }
                send(GenProtocolTraceMsg.gen((byte) 0, cellInfo.earfcn, cellInfo.pci, stmsi));
                break;
            default:
                break;
        }
    }

    private boolean validateCellInfo() {
        if (cellInfo == null || cellInfo.earfcn <= 0 || cellInfo.pci <= 0) {
            Log.e(TAG, "Invalid Cell Info!");
            return false;
        }
        return true;
    }

    private boolean validateSTMSI(byte[] bytes) {
        if (bytes.length != 10) {
            Log.e(TAG, "Invalid stmsi!");
            return false;
        }
        return true;
    }

    public void stopMonitor() {
        if (status != Status.DeviceStatus.WORKING) {
            return;
        }
        send(GetFrequentlyUsedMsg.protocalTraceRelMsg);
    }

    public void setIsReadyToMonitor(boolean isReadyToMonitor) {
        this.isReadyToMonitor = isReadyToMonitor;
    }

    private void ping() {
        pingStatus = Status.PingResult.FAILED;
        Process p;
        try {
            p = Runtime.getRuntime().exec("ping -c 1 -w 1000 " + IP);
            int status = p.waitFor();
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line);
            }
            Log.i(TAG, "Return ============" + builder.toString());
            if (status == 0) {
                pingStatus = Status.PingResult.SUCCEED;
            } else {
                pingStatus = Status.PingResult.FAILED;
            }
            Log.i(TAG, "++++++++++++++++++ status: " + status + "pingstatus: " + pingStatus);
            //Log.d("changeDevice", "DeviceMonitor  devicename:" + this.getName() + " hashcode:" + this.hashCode());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private class NetPing extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            while (!task.isCancelled()) {
                ping();
                try {
                    Thread.sleep(3000);
                } catch (Exception e) {

                }
            }
            return "";
        }
    }

    public void reboot()  {
        Global.ThreadPool.cachedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                Session session = null;

                try {
                    conn = new Connection(getIP());
                    conn.connect();
                    boolean isAuthenticated = conn.authenticateWithPassword(REBOOT_USERNAME, REBOOT_PASSWORD);

                    if (!isAuthenticated) {
                        Log.e(TAG, "Authentication failed.");
                    }

                    session = conn.openSession();
                    session.execCommand(REBOOT_CMD);

                    Log.e(TAG, "Here is some information about the remote host:");

                    InputStream stdout = new StreamGobbler(session.getStdout());

                    BufferedReader br = new BufferedReader(new InputStreamReader(stdout));

                    while (true) {
                        String line = br.readLine();
                        if (line == null)
                            break;
                        Log.e(TAG, line);
                    }
                    Log.e(TAG, "ExitCode: " + session.getExitStatus());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    session.close();
                    conn.close();
                }
            }
        });
    }

    public void release() {
        try {
            disconnect();
            if (!task.isCancelled()) {
                task.cancel(true);
            }
        } catch (Exception e) {
            Log.e(TAG, String.format("Failed to release device[%s].", IP));
            e.printStackTrace();
        }

    }
}
