package com.example.jbtang.agi.device;

import android.util.Log;

import com.example.jbtang.agi.core.CellInfo;
import com.example.jbtang.agi.core.Global;
import com.example.jbtang.agi.core.Status;
import com.example.jbtang.agi.messages.GenProtocolTraceMsg;
import com.example.jbtang.agi.messages.GetFrequentlyUsedMsg;
import com.example.jbtang.agi.service.OrientationFinding;
import com.example.jbtang.agi.ui.FindSTMSIActivity;
import com.example.jbtang.agi.ui.OrientationFindingActivity;

/**
 * Created by jbtang on 11/1/2015.
 */
public class MonitorDevice extends Device {

    public static final String DEVICE_NAME_PREFIX = "AGI";
    public static final int DATA_PORT = 3333;
    public static final int MESSAGE_PORT = 3334;

    private CellInfo cellInfo;
    private boolean isReadyToMonitor;
    private Status.BoardType type;
    private Status.DeviceWorkingStatus workingStatus;

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
    }

    public boolean isReady() {
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

}
