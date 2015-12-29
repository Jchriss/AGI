package com.example.jbtang.agi.external.msghandler;

import com.example.jbtang.agi.external.MonitorApplication;

import io.fmaster.Message;
import io.fmaster.MessageHandler;
import io.fmaster.LteMessage.Lte0xb16eMessage;

public class Lte0xb16eMsgHandler implements MessageHandler {

    @Override
    public void handle(Message message) {
        Lte0xb16eMessage msg = (Lte0xb16eMessage) message;

        Float txPwr = msg.getPuschTxPwr();
        if (txPwr != null) {
            MonitorApplication.data_PwrInfo.setPUSCH_TxPwr(txPwr);
        }
    }
}
