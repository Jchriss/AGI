package com.example.jbtang.agi.external.service;

import io.fmaster.QClient.DataClient;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.example.jbtang.agi.external.MonitorApplication;

public class MonitorService extends Service {

    private static final String TAG = "Monitor Service";
    private MessageDispatcher msgDispatcher = new MessageDispatcher();
    private final IBinder mBinder = new LocalBinder();
    private NotificationManager mNotificationManager;

    private int NOTIFICATION_ID = 0xf7f7;
    private DataClient _DataClient;

    public boolean TestToggle() {
        return msgDispatcher.TestToggle = !msgDispatcher.TestToggle;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Service is Starting...");

        if (openDevice()) {
            //工作线程
            Log.d(TAG, "Service has Started!");
        } else {
            Log.e(TAG, "Service start error!");
        }
    }


    private boolean openDevice() {

        if (_DataClient != null) {
            _DataClient.SetStopFlag();
            _DataClient = null;
        }

        Log.d(TAG,"open device ...");
        _DataClient = new DataClient();

        if (_DataClient.Open(MonitorApplication.IMEI)) {
            _DataClient.SetMessageHandler(msgDispatcher);
            _DataClient.setName("DataClient");
            _DataClient.start();

            return true;
        } else {
            Log.e(TAG, "打开设备失败!");
            return false;
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand() executed");
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.

        if (_DataClient != null && _DataClient.GetStopFlag()) {
            openDevice();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        //close device
        if (mNotificationManager != null)
            mNotificationManager.cancel(NOTIFICATION_ID);//消除对应ID的通知

        if (_DataClient != null)
            _DataClient.Close();

        super.onDestroy();
        Log.d(TAG, "onDestroy() executed");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    /**
     * Class for clients to access.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with
     * IPC.
     */
    public class LocalBinder extends Binder {

        public MonitorService getService() {
            return MonitorService.this;
        }
    }
}  


