package com.example.jbtang.agi.core.type;

import android.test.InstrumentationTestCase;

/**
 * Created by jbtang on 10/1/2015.
 */
public class U16Test extends InstrumentationTestCase {
    public void test() {
        //0x0000-0x7FFF
        U16 test = new U16(0x0000);
        assertEquals(test.getBase(), 0x0000);
        assertEquals(test.getBytes().length, 2);
        assertEquals(test.getBytes()[0], 0x00);
        assertEquals(test.getBytes()[1], 0x00);
        test = new U16(new byte[]{0x00, 0x00});
        assertEquals(test.getBase(), 0x0000);
        assertEquals(test.getBytes().length, 2);
        assertEquals(test.getBytes()[0], 0x00);
        assertEquals(test.getBytes()[1], 0x00);

        test = new U16(0x7FFF);
        assertEquals(test.getBase(), 0x7FFF);
        assertEquals(test.getBytes().length, 2);
        assertEquals(test.getBytes()[0], (byte) 0xFF);
        assertEquals(test.getBytes()[1], 0x7F);
        test = new U16(new byte[]{(byte) 0xFF, 0x7F});
        assertEquals(test.getBase(), 0x7FFF);
        assertEquals(test.getBytes().length, 2);
        assertEquals(test.getBytes()[0], (byte) 0xFF);
        assertEquals(test.getBytes()[1], 0x7F);

        //0x8000-0xFFFF
        test = new U16(0x8000);
        assertEquals(test.getBase(), 0x8000);
        assertEquals(test.getBytes().length, 2);
        byte b0 = test.getBytes()[0];
        byte b1 = test.getBytes()[1];
        assertEquals(b0, 0x00);
        assertEquals(b1, (byte) 0x80);
        test = new U16(new byte[]{0x00, (byte) 0x80});
        assertEquals(test.getBase(), 0x8000);
        assertEquals(test.getBytes().length, 2);
        b0 = test.getBytes()[0];
        b1 = test.getBytes()[1];
        assertEquals(b0, 0x00);
        assertEquals(b1, (byte) 0x80);

        test = new U16(0xFFFF);
        assertEquals(test.getBase(), 0xFFFF);
        assertEquals(test.getBytes().length, 2);
        b0 = test.getBytes()[0];
        b1 = test.getBytes()[1];
        assertEquals(b0, (byte) 0xFF);
        assertEquals(b1, (byte) 0xFF);
        test = new U16(new byte[]{(byte) 0xFF, (byte) 0xFF});
        assertEquals(test.getBase(), 0xFFFF);
        assertEquals(test.getBytes().length, 2);
        b0 = test.getBytes()[0];
        b1 = test.getBytes()[1];
        assertEquals(b0, (byte) 0xFF);
        assertEquals(b1, (byte) 0xFF);

        //test sequence
        test = new U16(0x1234);
        assertEquals(test.getBase(), 0x1234);
        assertEquals(test.getBytes().length, 2);
        b0 = test.getBytes()[0];
        b1 = test.getBytes()[1];
        assertEquals(b0, (byte) 0x34);
        assertEquals(b1, (byte) 0x12);
        test = new U16(new byte[]{0x34, 0x12});
        assertEquals(test.getBase(), 0x1234);
        assertEquals(test.getBytes().length, 2);
        b0 = test.getBytes()[0];
        b1 = test.getBytes()[1];
        assertEquals(b0, (byte) 0x34);
        assertEquals(b1, (byte) 0x12);
    }
}
