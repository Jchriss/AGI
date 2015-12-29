package com.example.jbtang.agi.core;

import android.test.InstrumentationTestCase;

/**
 * Created by jbtang on 10/5/2015.
 */
public class MsgSendHelperTest extends InstrumentationTestCase {
    public void test() {
        assertEquals(MsgSendHelper.byteArrayToShort(new byte[]{0x00, 0x00}), (short) 0);
        assertEquals(MsgSendHelper.byteArrayToShort(new byte[]{(byte) 0xFF, (byte) 0xFF}), (short) -1);
        assertEquals(MsgSendHelper.byteArrayToShort(new byte[]{(byte) 0x00, (byte) 0x80}), (short) -32768);
        assertEquals(MsgSendHelper.byteArrayToShort(new byte[]{(byte) 0xFF, (byte) 0x7F}), (short) 32767);

        assertTrue(MsgSendHelper.byteArrayEquals(MsgSendHelper.shortToByteArray((short) 0), new byte[]{0x00, 0x00}));
        assertTrue(MsgSendHelper.byteArrayEquals(MsgSendHelper.shortToByteArray((short) -1), new byte[]{(byte) 0xFF, (byte) 0xFF}));
        assertTrue(MsgSendHelper.byteArrayEquals(MsgSendHelper.shortToByteArray((short) -32768), new byte[]{(byte) 0x00, (byte) 0x80}));
        assertTrue(MsgSendHelper.byteArrayEquals(MsgSendHelper.shortToByteArray((short) 32767), new byte[]{(byte) 0xFF, (byte) 0x7F}));
    }
}
