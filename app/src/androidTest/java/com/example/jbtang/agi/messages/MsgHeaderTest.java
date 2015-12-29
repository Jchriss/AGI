package com.example.jbtang.agi.messages;

import android.test.InstrumentationTestCase;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.messages.base.MsgHeader;

/**
 * Created by jbtang on 10/2/2015.
 */
public class MsgHeaderTest extends InstrumentationTestCase {
    public void test() {
        //test min
        MsgHeader msgHead = new MsgHeader();
        assertEquals(msgHead.getDestID(), 0);
        assertEquals(msgHead.getMsgLen(), 0);
        assertEquals(msgHead.getMsgType(), 0);
        assertEquals(msgHead.getReserved(), 0L);
        assertEquals(msgHead.getSrcID(), 0);
        assertEquals(msgHead.getTransactionID(), 0);
        assertTrue(MsgSendHelper.byteArrayEquals(msgHead.getBytes(),
                new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}));

        //test max
        msgHead.setDestID((byte) 0xFF);
        msgHead.setMsgLen(0xFFFF);
        msgHead.setMsgType(0xFFFF);
        msgHead.setReserved(0xFFFFFFFFL);
        msgHead.setSrcID((byte) 0xFF);
        msgHead.setTransactionID(0xFFFF);
        assertTrue(MsgSendHelper.byteArrayEquals(msgHead.getBytes(),
                new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF}));

        msgHead = new MsgHeader(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF});
        assertEquals(msgHead.getTransactionID(), 0xFFFF);
        assertEquals(msgHead.getSrcID(), (byte) 0xFF);
        assertEquals(msgHead.getReserved(), 0xFFFFFFFFL);
        assertEquals(msgHead.getMsgType(), 0xFFFF);
        assertEquals(msgHead.getDestID(), (byte) 0xFF);
        assertEquals(msgHead.getMsgLen(), 0xFFFF);

        //test sequence
        msgHead.setReserved(0x12345678L);
        msgHead.setSrcID((byte) 0x90);
        msgHead.setDestID((byte) 0x09);
        msgHead.setMsgType(0x8765);
        msgHead.setTransactionID(0x4321);
        msgHead.setMsgLen(0x1234);
        assertTrue(MsgSendHelper.byteArrayEquals(msgHead.getBytes(),
                new byte[]{(byte) 0x78, (byte) 0x56, (byte) 0x34, (byte) 0x12, (byte) 0x90, (byte) 0x09,
                        (byte) 0x65, (byte) 0x87, (byte) 0x21, (byte) 0x43, (byte) 0x34, (byte) 0x12}));

        msgHead = new MsgHeader(new byte[]{(byte) 0x78, (byte) 0x56, (byte) 0x34, (byte) 0x12, (byte) 0x90, (byte) 0x09,
                (byte) 0x65, (byte) 0x87, (byte) 0x21, (byte) 0x43, (byte) 0x34, (byte) 0x12});
        assertEquals(msgHead.getReserved(), 0x12345678L);
        assertEquals(msgHead.getSrcID(), (byte)0x90);
        assertEquals(msgHead.getDestID(), (byte)0x09);
        assertEquals(msgHead.getMsgLen(), 0x1234);
        assertEquals(msgHead.getMsgType(), 0x8765);
        assertEquals(msgHead.getTransactionID(), 0x4321);
    }
}
