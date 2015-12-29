package com.example.jbtang.agi.external.msghandler;

import com.example.jbtang.agi.external.MonitorApplication;

import io.fmaster.Message;
import io.fmaster.MessageHandler;
import io.fmaster.LteMessage.Lte0xb193Message;

public class Lte0xb193MsgHandler implements MessageHandler {

    @Override
    public void handle(Message message) {
        Lte0xb193Message msg = (Lte0xb193Message) message;
        Float rssi = msg.GetRSSI(MonitorApplication.data_ServCell.getPCI());
        Float sinr = msg.GetSINR(MonitorApplication.data_ServCell.getPCI());

        if (rssi != null || sinr != null) {
            //Log.d("RSSI", String.format("0xb193 RSSI:%.2f", rssi));
            if (rssi != null) {
                MonitorApplication.data_PwrInfo.setRSSI(rssi.floatValue());
            }
            if (sinr != null) {
                MonitorApplication.data_PwrInfo.setSINR(sinr.floatValue());
            }

            MonitorApplication.data_PwrInfo.setTime(msg.getTime());
            MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.POWER_INFO_FLAG, "msg", MonitorApplication.data_PwrInfo);
        }
    }

}
