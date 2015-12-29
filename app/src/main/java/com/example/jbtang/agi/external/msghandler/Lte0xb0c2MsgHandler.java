package com.example.jbtang.agi.external.msghandler;

import com.example.jbtang.agi.external.MonitorApplication;

import io.fmaster.Message;
import io.fmaster.MessageHandler;
import io.fmaster.LteMessage.Lte0xb0c2Message;

public class Lte0xb0c2MsgHandler implements MessageHandler {

    @Override
    public void handle(Message message) {

        Lte0xb0c2Message msg = (Lte0xb0c2Message) message;

        if (msg.getCellid() > 0) {
            MonitorApplication.data_ServCell.setCellId(msg.getCellid());
        }
        MonitorApplication.data_ServCell.setTAC(msg.getTac());
        MonitorApplication.data_ServCell.setMCC(msg.getMcc());
        MonitorApplication.data_ServCell.setMNC(msg.getMnc());
        MonitorApplication.data_ServCell.setBand(msg.getDl_bandwidth());
        MonitorApplication.data_ServCell.setFreBand(msg.getFreqBI());
        //send broad
        MonitorApplication.data_ServCell.setTime(msg.getTime());
        MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.SERVER_CELL_FLAG, "msg", MonitorApplication.data_ServCell);

    }
}
