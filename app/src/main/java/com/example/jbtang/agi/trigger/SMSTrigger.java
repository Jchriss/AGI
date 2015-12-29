package com.example.jbtang.agi.trigger;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;

import com.example.jbtang.agi.R;
import com.example.jbtang.agi.core.Global;
import com.example.jbtang.agi.core.Status;
import com.example.jbtang.agi.device.DeviceManager;
import com.example.jbtang.agi.device.MonitorDevice;

import java.lang.reflect.Field;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by jbtang on 12/6/2015.
 */
public class SMSTrigger implements Trigger {
    private static final String TAG = "smsTrigger";
    private static final SMSTrigger instance = new SMSTrigger();

    private boolean start;

    private SMSTrigger() {
        start = false;
    }

    public static SMSTrigger getInstance() {
        return instance;
    }

    private Activity currentActivity;
    private Runnable task;
    private Future future;

    @Override
    public void start(Activity activity, Status.Service service) {
        if (!start) {
            currentActivity = activity;
            switch (service) {
                case FINDSTMIS:
                    task = new FindSTMSITask();
                    break;
                case ORIENTATION:
                    task = new OrientationFindingTask();
                    break;
                default:
                    throw new IllegalArgumentException("Illegal service: " + service.name());
            }
            future = Global.ThreadPool.scheduledThreadPool.scheduleAtFixedRate(task, 1, Global.Configuration.triggerInterval, TimeUnit.SECONDS);
            start = true;
        }
    }

    @Override
    public void stop() {
        if (start) {
            for (MonitorDevice device : DeviceManager.getInstance().getDevices()) {
                device.stopMonitor();
            }
            if (task != null && future != null) {
                future.cancel(true);
            }
            task = null;
            future = null;
            start = false;
        }
    }

    class FindSTMSITask implements Runnable {
        private int count;
        private TextView countTextView;

        public FindSTMSITask() {
            this.count = 0;
            this.countTextView = (TextView) currentActivity.findViewById(R.id.find_stmsi_triggered_count);
        }

        @Override
        public void run() {
            if (count++ == Global.Configuration.triggerTotalCount) {
                future.cancel(true);
            }
            send();
            for (MonitorDevice device : DeviceManager.getInstance().getDevices()) {
                device.startMonitor(Status.Service.FINDSTMIS);
            }
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    countTextView.setText(String.valueOf(count));
                }
            });

            Global.ThreadPool.cachedThreadPool.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(Global.Configuration.filterInterval * 1000);
                    } catch (InterruptedException e) {
                        Log.e(TAG, "run error");
                    }

                    for (MonitorDevice device : DeviceManager.getInstance().getDevices()) {
                        device.stopMonitor();
                    }
                }

            });
        }
    }

    class OrientationFindingTask implements Runnable {
        private boolean start;

        public OrientationFindingTask() {
            this.start = false;
        }

        @Override
        public void run() {
            if (!start) {
                for (MonitorDevice device : DeviceManager.getInstance().getDevices()) {
                    device.startMonitor(Status.Service.ORIENTATION);
                }
                start = true;
            }
            send();
        }
    }

    private void send() {
        String SENT = "sms_sent";
        String DELIVERED = "sms_delivered";

        PendingIntent sentPI = PendingIntent.getActivity(currentActivity, 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getActivity(currentActivity, 0, new Intent(DELIVERED), 0);

        currentActivity.registerReceiver(new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Log.i("====>", "Activity.RESULT_OK");
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Log.i("====>", "RESULT_ERROR_GENERIC_FAILURE");
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Log.i("====>", "RESULT_ERROR_NO_SERVICE");
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Log.i("====>", "RESULT_ERROR_NULL_PDU");
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Log.i("====>", "RESULT_ERROR_RADIO_OFF");
                        break;
                }
            }
        }, new IntentFilter(SENT));

        currentActivity.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Log.i("====>", "RESULT_OK");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i("=====>", "RESULT_CANCELED");
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager smsm = SmsManager.getDefault();
        smsm.sendTextMessage(Global.Configuration.targetPhoneNum, null, "Hi", sentPI, deliveredPI);
    }

}
