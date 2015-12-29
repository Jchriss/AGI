package com.example.jbtang.agi.external.msghandler;


import com.example.jbtang.agi.external.MonitorApplication;

import io.fmaster.GSMServCellMessage;
import io.fmaster.Message;
import io.fmaster.MessageHandler;
import io.fmaster.GsmMessage.Gsm0x5134Message;

public class Gsm0x5134MsgHandler implements MessageHandler {

    @Override
    public void handle(Message message) {
        Gsm0x5134Message msg = (Gsm0x5134Message) message;
        int MCC = (msg.getmccPart1() & 0x0F) * 100 + (msg.getmccPart1() >> 4) * 10 + (msg.getmccPart2() & 0x0F);
        int MNC = (msg.getmncTemp() >> 4) + (msg.getmncTemp() & 0xF) * 10;
        if (MCC == 0) {
            return;
        }
        MonitorApplication.data_gsm_ServCell.setEARFCN(msg.getbcchArfcn());
        MonitorApplication.data_gsm_ServCell.setCI(msg.getcellId() & 0xFFFF);
        MonitorApplication.data_gsm_ServCell.setLAC(msg.getlac() & 0xFFFF);


        MonitorApplication.data_gsm_ServCell.setMCC((short) MCC);
        MonitorApplication.data_gsm_ServCell.setMNC((short) MNC);

        if (msg.getncc() != Byte.MAX_VALUE && msg.getbcc() != Byte.MAX_VALUE) {
            MonitorApplication.data_gsm_ServCell.setBSIC((short) (msg.getncc() * 10 + msg.getbcc()));
        }

        //send broad
        if (msg.getTime() - MonitorApplication.data_gsm_ServCell.getTime() > GSMServCellMessage.UPDATE_INTERVAL) {
            MonitorApplication.data_gsm_ServCell.setTime(msg.getTime());
            MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.GSM_SERVER_CELL_FLAG, "msg", MonitorApplication.data_gsm_ServCell);
        }
    }
}
