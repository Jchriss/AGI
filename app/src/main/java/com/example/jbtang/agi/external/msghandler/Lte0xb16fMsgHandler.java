package com.example.jbtang.agi.external.msghandler;

import com.example.jbtang.agi.external.MonitorApplication;

import io.fmaster.Message;
import io.fmaster.MessageHandler;
import io.fmaster.LteMessage.Lte0xb16fMessage;

public class Lte0xb16fMsgHandler implements MessageHandler {

    @Override
    public void handle(Message message) {
        Lte0xb16fMessage msg = (Lte0xb16fMessage) message;

        Float txPwr = msg.getPucchTxPwr();
        if (txPwr != null) {
            MonitorApplication.data_PwrInfo.setPUCCH_TxPwr(txPwr);
        }
    }

}
