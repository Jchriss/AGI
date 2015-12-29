package com.example.jbtang.agi.core.type;

import android.test.InstrumentationTestCase;

/**
 * Created by jbtang on 10/1/2015.
 */
public class U32Test extends InstrumentationTestCase {
    public void test() {
        //0x00000000-0x7FFFFFFF
        U32 test = new U32(0x00000000L);
        assertEquals(test.getBase(), 0x00000000L);
        assertEquals(test.getBytes().length, 4);
        assertEquals(test.getBytes()[0], 0x00);
        assertEquals(test.getBytes()[1], 0x00);
        assertEquals(test.getBytes()[2], 0x00);
        assertEquals(test.getBytes()[3], 0x00);
        test = new U32(new byte[]{0x00, 0x00, 0x00, 0x00});
        assertEquals(test.getBase(), 0x00000000L);
        assertEquals(test.getBytes().length, 4);
        assertEquals(test.getBytes()[0], 0x00);
        assertEquals(test.getBytes()[1], 0x00);
        assertEquals(test.getBytes()[2], 0x00);
        assertEquals(test.getBytes()[3], 0x00);

        test = new U32(0x7FFFFFFFL);
        assertEquals(test.getBase(), 0x7FFFFFFFL);
        assertEquals(test.getBytes().length, 4);
        assertEquals(test.getBytes()[0], (byte) 0xFF);
        assertEquals(test.getBytes()[1], (byte) 0xFF);
        assertEquals(test.getBytes()[2], (byte) 0xFF);
        assertEquals(test.getBytes()[3], 0x7F);
        test = new U32(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, 0x7F});
        assertEquals(test.getBase(), 0x7FFFFFFFL);
        assertEquals(test.getBytes().length, 4);
        assertEquals(test.getBytes()[0], (byte) 0xFF);
        assertEquals(test.getBytes()[1], (byte) 0xFF);
        assertEquals(test.getBytes()[2], (byte) 0xFF);
        assertEquals(test.getBytes()[3], 0x7F);

        //0x8000-0xFFFF
        test = new U32(0x80000000L);
        assertEquals(test.getBase(), 0x80000000L);
        assertEquals(test.getBytes().length, 4);
        assertEquals(test.getBytes()[0], 0x00);
        assertEquals(test.getBytes()[1], 0x00);
        assertEquals(test.getBytes()[2], 0x00);
        assertEquals(test.getBytes()[3], (byte) 0x80);
        test = new U32(new byte[]{0x00, 0x00, 0x00, (byte) 0x80});
        assertEquals(test.getBase(), 0x80000000L);
        assertEquals(test.getBytes().length, 4);
        assertEquals(test.getBytes()[0], 0x00);
        assertEquals(test.getBytes()[1], 0x00);
        assertEquals(test.getBytes()[2], 0x00);
        assertEquals(test.getBytes()[3], (byte) 0x80);

        test = new U32(0xFFFFFFFFL);
        assertEquals(test.getBase(), 0xFFFFFFFFL);
        assertEquals(test.getBytes().length, 4);
        assertEquals(test.getBytes()[0], (byte) 0xFF);
        assertEquals(test.getBytes()[1], (byte) 0xFF);
        assertEquals(test.getBytes()[2], (byte) 0xFF);
        assertEquals(test.getBytes()[3], (byte) 0xFF);
        test = new U32(new byte[]{(byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF});
        assertEquals(test.getBase(), 0xFFFFFFFFL);
        assertEquals(test.getBytes().length, 4);
        assertEquals(test.getBytes()[0], (byte) 0xFF);
        assertEquals(test.getBytes()[1], (byte) 0xFF);
        assertEquals(test.getBytes()[2], (byte) 0xFF);
        assertEquals(test.getBytes()[3], (byte) 0xFF);

        //test sequence
        test = new U32(0x12345678L);
        assertEquals(test.getBase(), 0x12345678L);
        assertEquals(test.getBytes().length, 4);
        assertEquals(test.getBytes()[0], (byte) 0x78);
        assertEquals(test.getBytes()[1], (byte) 0x56);
        assertEquals(test.getBytes()[2], (byte) 0x34);
        assertEquals(test.getBytes()[3], (byte) 0x12);
        test = new U32(new byte[]{(byte) 0x78, (byte) 0x56, (byte) 0x34, (byte) 0x12});
        assertEquals(test.getBase(), 0x12345678L);
        assertEquals(test.getBytes().length, 4);
        assertEquals(test.getBytes()[0], (byte) 0x78);
        assertEquals(test.getBytes()[1], (byte) 0x56);
        assertEquals(test.getBytes()[2], (byte) 0x34);
        assertEquals(test.getBytes()[3], (byte) 0x12);
    }
}
