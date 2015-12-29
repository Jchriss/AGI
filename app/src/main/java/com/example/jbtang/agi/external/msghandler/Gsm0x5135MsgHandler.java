package com.example.jbtang.agi.external.msghandler;

import com.example.jbtang.agi.external.MonitorApplication;

import io.fmaster.GSMServCellMessage;
import io.fmaster.Message;
import io.fmaster.MessageHandler;

import io.fmaster.Enumeration.BAND_TYPE;
import io.fmaster.GsmMessage.Gsm0x5135Message;


public class Gsm0x5135MsgHandler implements MessageHandler {

    @Override
    public void handle(Message message) {
        Gsm0x5135Message msg = (Gsm0x5135Message) message;

        if (MonitorApplication.data_gsm_ServCell.getBand() != BAND_TYPE.GSM_BAND_Cur_Setting
                && (msg.getPwrLevel() >= 0 && msg.getPwrLevel() <= 31))         /*calculate MS output power */ {
            if (MonitorApplication.data_gsm_ServCell.getBand() == BAND_TYPE.GSM_BAND_PCS) {
                MonitorApplication.data_gsm_ServCell.setTxpwr(MonitorApplication.msOutputPower[2][msg.getPwrLevel()]);
            } else if (MonitorApplication.data_gsm_ServCell.getBand() == BAND_TYPE.GSM_BAND_DCS) {
                MonitorApplication.data_gsm_ServCell.setTxpwr(MonitorApplication.msOutputPower[1][msg.getPwrLevel()]);

            } else {
                if (msg.getPwrLevel() < 0x14) {
                    MonitorApplication.data_gsm_ServCell.setTxpwr(MonitorApplication.msOutputPower[0][msg.getPwrLevel()]);
                } else {
                    MonitorApplication.data_gsm_ServCell.setTxpwr(Byte.MAX_VALUE);
                }
            }

        } else {
            MonitorApplication.data_gsm_ServCell.setTxpwr(Byte.MAX_VALUE);
        }
        if (msg.getTime() - MonitorApplication.data_gsm_ServCell.getTime() > GSMServCellMessage.UPDATE_INTERVAL) {
            MonitorApplication.data_gsm_ServCell.setTime(msg.getTime());
            MonitorApplication.sendBroad(MonitorApplication.BROAD_TO_MAIN_ACTIVITY, MonitorApplication.GSM_SERVER_CELL_FLAG, "msg", MonitorApplication.data_gsm_ServCell);
        }
    }
}
