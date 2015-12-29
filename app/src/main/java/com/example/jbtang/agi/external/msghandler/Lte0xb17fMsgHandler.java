package com.example.jbtang.agi.external.msghandler;

import com.example.jbtang.agi.external.MonitorApplication;

import io.fmaster.Message;
import io.fmaster.MessageHandler;
import io.fmaster.LteMessage.Lte0xb17fMessage;

public class Lte0xb17fMsgHandler implements MessageHandler {

    @Override
    public void handle(Message message) {

        Lte0xb17fMessage msg = (Lte0xb17fMessage) message;
        if (msg.getEARFCN() == 0) {
            return;
        }
        boolean send = false;
        if (MonitorApplication.data_ServCell.getPCI() != msg.getPci()) {
            MonitorApplication.data_ServCell.reset();
            MonitorApplication.data_PwrInfo.reset();
            MonitorApplication.data_NcellList.reset();

            send = true;
        }

        MonitorApplication.data_ServCell.setEARFCN(msg.getEARFCN());
        MonitorApplication.data_ServCell.setPCI(msg.getPci());
        //send broad
        //if (msg.getTime() - MonitorApplication.data_ServCell.getTime() >= ServCellMessage.UPDATE_INTERVAL) {
        MonitorApplication.data_ServCell.setTime(msg.getTime());
        MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.SERVER_CELL_FLAG, "msg", MonitorApplication.data_ServCell);
        //}

        if (send)
            MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.N_CELL_FLAG, "msg", MonitorApplication.data_NcellList);
        if (msg.getMs_rsrp() < 0)
            MonitorApplication.data_PwrInfo.setRSRP(msg.getMs_rsrp());
        if (msg.getMs_rsrq() < 0)
            MonitorApplication.data_PwrInfo.setRSRQ(msg.getMs_rsrq());
        if (msg.getMsrssi() < 0)
            MonitorApplication.data_PwrInfo.setRSSI(msg.getMsrssi());

        //Log.d("RSSI", String.format("0xb17f RSSI:%.2f", msg.getMsrssi()));
        //send broad
        //if (msg.getTime() - MonitorApplication.data_PwrInfo.getTime() >= PwrInfoMessage.UPDATE_INTERVAL) {
        MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.POWER_INFO_FLAG, "msg", MonitorApplication.data_PwrInfo);
        //}


    }

}
