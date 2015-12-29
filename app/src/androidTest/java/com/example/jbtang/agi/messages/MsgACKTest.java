package com.example.jbtang.agi.messages;

import android.test.InstrumentationTestCase;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.messages.base.MsgACK;
import com.example.jbtang.agi.messages.base.MsgHeader;

/**
 * Created by jbtang on 10/2/2015.
 */
public class MsgACKTest extends InstrumentationTestCase {
    public void test() {
        //test min
        MsgACK msgACK = new MsgACK();
        assertEquals(msgACK.getHeader(), new MsgHeader());
        assertEquals(msgACK.getCause(), 0);
        assertTrue(MsgSendHelper.byteArrayEquals(msgACK.getBytes(),
                new byte[]{0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00}));

        //test max
        MsgHeader msgHead = new MsgHeader();
        msgHead.setDestID((byte) 0xFF);
        msgHead.setMsgLen(0xFFFF);
        msgHead.setMsgType(0xFFFF);
        msgHead.setReserved(0xFFFFFFFFL);
        msgHead.setSrcID((byte) 0xFF);
        msgHead.setTransactionID(0xFFFF);
        msgACK.setHeader(msgHead);
        msgACK.setCause(0xFF);
        assertTrue(MsgSendHelper.byteArrayEquals(msgACK.getBytes(),
                new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                        (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x00, 0x00, 0x00}));

        msgACK = new MsgACK(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF,
                (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x00, 0x00, 0x00});
        assertEquals(msgACK.getCause(), 0xFF);
        assertEquals(msgACK.getHeader(), msgHead);

        //test sequence
        msgHead = new MsgHeader();
        msgACK.setHeader(msgHead);
        msgACK.setCause(0x12);
        assertTrue(MsgSendHelper.byteArrayEquals(msgACK.getBytes(),
                new MsgSendHelper.AppendByteArray().append(msgHead.getBytes()).append(new byte[]{0x12}).append(new byte[MsgACK.alignByteCount]).toByteArray()));
    }
}
