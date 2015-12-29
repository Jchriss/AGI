package com.example.jbtang.agi.messages;

import android.test.InstrumentationTestCase;
import android.util.Log;

import com.example.jbtang.agi.core.MsgSendHelper;

/**
 * Created by jbtang on 10/11/2015.
 */
public class GenProtocolTraceMsgTest extends InstrumentationTestCase {
    public void test() {
        String stmsi = "06CC0B16ED";
        byte[] bytes = GenProtocolTraceMsg.gen((byte) 0, 37900, 228, stmsi.getBytes());
        Log.i("Gen cell search", MsgSendHelper.convertBytesToString(bytes));
    }
}
