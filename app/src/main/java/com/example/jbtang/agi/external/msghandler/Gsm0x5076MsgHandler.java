package com.example.jbtang.agi.external.msghandler;

import com.example.jbtang.agi.external.MonitorApplication;

import io.fmaster.GSMServCellMessage;
import io.fmaster.Message;
import io.fmaster.MessageHandler;

import io.fmaster.Enumeration.BAND_TYPE;
import io.fmaster.GsmMessage.Gsm0x5076Message;


public class Gsm0x5076MsgHandler implements MessageHandler {

    @Override
    public void handle(Message message) {
        Gsm0x5076Message msg = (Gsm0x5076Message) message;

        if (MonitorApplication.data_gsm_ServCell.getBand() != BAND_TYPE.GSM_BAND_Cur_Setting
                && (msg.gettxLev() >= 0 && msg.gettxLev() <= 31))         /*calculate MS output power */ {
            if (MonitorApplication.data_gsm_ServCell.getBand() == BAND_TYPE.GSM_BAND_PCS) {
                MonitorApplication.data_gsm_ServCell.setTxpwr(MonitorApplication.msOutputPower[2][msg.gettxLev()]);
            } else if (MonitorApplication.data_gsm_ServCell.getBand() == BAND_TYPE.GSM_BAND_DCS) {
                MonitorApplication.data_gsm_ServCell.setTxpwr(MonitorApplication.msOutputPower[1][msg.gettxLev()]);

            } else {
                if (msg.gettxLev() < 0x14) {
                    MonitorApplication.data_gsm_ServCell.setTxpwr(MonitorApplication.msOutputPower[0][msg.gettxLev()]);
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
