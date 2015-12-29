package com.example.jbtang.agi.external.service;

import android.util.Log;
import android.util.SparseArray;

import com.example.jbtang.agi.external.msghandler.*;

import io.fmaster.Message;
import io.fmaster.MessageHandler;

public class MessageDispatcher implements MessageHandler {

    public static final String TAG = "MessageHandler";

    public SparseArray<MessageHandler> msgHandlers = new SparseArray<>();

    private void initilMsgMap() {
        msgHandlers.put(Message.MESSAGE_0XB0C2, new Lte0xb0c2MsgHandler());
        msgHandlers.put(Message.MESSAGE_0XB16E, new Lte0xb16eMsgHandler());
        msgHandlers.put(Message.MESSAGE_0XB16F, new Lte0xb16fMsgHandler());
        msgHandlers.put(Message.MESSAGE_0XB179, new Lte0xb179MsgHandler());
        msgHandlers.put(Message.MESSAGE_0XB17F, new Lte0xb17fMsgHandler());
        msgHandlers.put(Message.MESSAGE_OXB192, new Lte0xb192MsgHandler());
        msgHandlers.put(Message.MESSAGE_OXB193, new Lte0xb193MsgHandler());
        msgHandlers.put(Message.MESSAGE_0XB0EE, new Lte0xb0eeMsgHandler());


        msgHandlers.put(Message.MESSAGE_0X5134, new Gsm0x5134MsgHandler());
        msgHandlers.put(Message.MESSAGE_0X5135, new Gsm0x5135MsgHandler());
        msgHandlers.put(Message.MESSAGE_0X5137, new Gsm0x5137MsgHandler());
        msgHandlers.put(Message.MESSAGE_0X513A, new Gsm0x513AMsgHandler());
        msgHandlers.put(Message.MESSAGE_0X5071, new Gsm0x5071MsgHandler());
        msgHandlers.put(Message.MESSAGE_0X5076, new Gsm0x5076MsgHandler());
        msgHandlers.put(Message.MESSAGE_0X51FC, new Gsm0x51FCMsgHandler());
    }

    public MessageDispatcher() {
        initilMsgMap();
    }

    public boolean TestToggle = true;

    @Override
    public void handle(Message message) {

        if (message == null || !TestToggle) {
            return;
        }
        if(message.getType()==Message.MESSAGE_0XB0EE) {
            Log.d(TAG, "GUTI msg...........");
        }
        MessageHandler h = msgHandlers.get(message.getType());
        if (h != null) {
            h.handle(message);
            //Log.d(TAG, String.format("Message: %s",  message.toString()));
        } else {
            Log.d(TAG, String.format("unknow message type: 0x%4x", message.getType()));
        }
    }

}
