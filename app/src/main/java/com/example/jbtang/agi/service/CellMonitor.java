package com.example.jbtang.agi.service;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.jbtang.agi.core.CellInfo;
import com.example.jbtang.agi.device.DeviceManager;
import com.example.jbtang.agi.device.MonitorDevice;
import com.example.jbtang.agi.external.MonitorApplication;
import com.example.jbtang.agi.external.service.MonitorService;

/**
 * Created by jbtang on 10/28/2015.
 */
public class CellMonitor {
    private static final String TAG = "Cell monitor";
    private static final CellMonitor instance = new CellMonitor();

    private CellMonitor() {

    }

    public static CellMonitor getInstance() {
        return instance;
    }

    public void prepareMonitor(MonitorDevice device, CellInfo cellInfo) {
        device.setCellInfo(cellInfo);
        device.setIsReadyToMonitor(true);
    }


}
