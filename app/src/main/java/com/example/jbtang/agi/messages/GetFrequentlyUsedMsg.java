package com.example.jbtang.agi.messages;

/**
 * Created by jbtang on 10/25/2015.
 */
public class GetFrequentlyUsedMsg {
    public static final byte[] getDeviceStateMsg = new byte[]{0, 0, 0, 0, 0, 0, 0x01, 0x40, 0, 0, 0, 0};
    public static final byte[] protocalTraceRelMsg = new byte[]{0, 0, 0, 0, 4, 2, 0x0c, 0x40, 1, 0, 0, 0};
}
