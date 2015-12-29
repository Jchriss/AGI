package com.example.jbtang.agi.core.type;

import android.test.InstrumentationTestCase;

/**
 * Created by jbtang on 10/1/2015.
 */
public class U8Test extends InstrumentationTestCase {
    public void test() {
        //0x00-0x7F
        U8 test = new U8(0x00);
        assertEquals(test.getBase(), 0x00);
        assertEquals(test.getBytes().length, 1);
        assertEquals(test.getBytes()[0], 0x00);
        test = new U8(new byte[]{0x00});
        assertEquals(test.getBase(), 0x00);
        assertEquals(test.getBytes().length, 1);
        assertEquals(test.getBytes()[0], 0x00);

        test = new U8(0x7F);
        assertEquals(test.getBase(), 0x7F);
        assertEquals(test.getBytes().length, 1);
        assertEquals(test.getBytes()[0], 0x7F);
        test = new U8(new byte[]{0x7F});
        assertEquals(test.getBase(), 0x7F);
        assertEquals(test.getBytes().length, 1);
        assertEquals(test.getBytes()[0], 0x7F);

        //0x80-0xFF
        test = new U8(0x80);
        assertEquals(test.getBase(), 0x80);
        assertEquals(test.getBytes().length, 1);
        byte b = test.getBytes()[0];
        assertEquals((b & 0x0F0) >> 4, 0x8);
        assertEquals(b & 0x0F, 0);
        test = new U8(new byte[]{(byte) 0x80});
        assertEquals(test.getBase(), 0x80);
        assertEquals(test.getBytes().length, 1);
        b = test.getBytes()[0];
        assertEquals((b & 0x0F0) >> 4, 0x8);
        assertEquals(b & 0x0F, 0);

        test = new U8(0xFF);
        assertEquals(test.getBase(), 0xFF);
        assertEquals(test.getBytes().length, 1);
        b = test.getBytes()[0];
        assertEquals((b & 0xF0) >> 4, 0xF);
        assertEquals(b & 0x0F, 0x0F);
        test = new U8(new byte[]{(byte) 0xFF});
        assertEquals(test.getBase(), 0xFF);
        assertEquals(test.getBytes().length, 1);
        b = test.getBytes()[0];
        assertEquals((b & 0xF0) >> 4, 0xF);
        assertEquals(b & 0x0F, 0x0F);
    }
}
