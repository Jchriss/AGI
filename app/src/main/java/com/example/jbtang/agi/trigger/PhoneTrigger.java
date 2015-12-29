package com.example.jbtang.agi.trigger;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import com.example.jbtang.agi.core.Global;
import com.example.jbtang.agi.core.Status;

/**
 * Created by jbtang on 12/4/2015.
 */
public class PhoneTrigger implements Trigger {

    private static final PhoneTrigger instance = new PhoneTrigger();

    private PhoneTrigger() {
    }

    public static PhoneTrigger getInstance() {
        return instance;
    }


    @Override
    public void start(Activity activity, Status.Service service) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + Global.Configuration.targetPhoneNum));
        activity.startActivity(intent);
    }

    @Override
    public void stop() {

    }
}
