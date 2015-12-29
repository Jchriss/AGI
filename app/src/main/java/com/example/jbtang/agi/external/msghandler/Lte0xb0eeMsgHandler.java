package com.example.jbtang.agi.external.msghandler;

import android.util.Log;

import com.example.jbtang.agi.external.MonitorApplication;

import io.fmaster.LteMessage.Lte0xb0eeMessage;
import io.fmaster.Message;
import io.fmaster.MessageHandler;

/**
 * Created by jbtang on 11/24/2015.
 */
public class Lte0xb0eeMsgHandler implements MessageHandler {

    @Override
    public void handle(Message message) {

        Lte0xb0eeMessage msg = (Lte0xb0eeMessage) message;

        MonitorApplication.stmsi = new StringBuilder().append(padLeft(String.format("%X", msg.getMmeCode()), "0", 2))
                .append(padLeft(String.format("%X", msg.getmTmsi3()), "0", 2))
                .append(padLeft(String.format("%X", msg.getmTmsi2()), "0", 2))
                .append(padLeft(String.format("%X", msg.getmTmsi1()), "0", 2))
                .append(padLeft(String.format("%X", msg.getmTmsi0()), "0", 2))
                .toString();

        Log.d("STMSI", MonitorApplication.stmsi);
        //send broad
        MonitorApplication.data_ServCell.setTime(msg.getTime());
        MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_ORIENTATION_ACTIVITY, MonitorApplication.STMSI, "msg", MonitorApplication.stmsi);

    }

    private static String padLeft(String src, String pad, int len) {
        StringBuilder builder = new StringBuilder();
        len -= src.length();
        while (len-- > 0) {
            builder.append(pad);
        }
        builder.append(src);
        return builder.toString();
    }
}

