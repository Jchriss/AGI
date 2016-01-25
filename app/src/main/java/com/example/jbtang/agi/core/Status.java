package com.example.jbtang.agi.core;

/**
 * Created by jbtang on 9/28/2015.
 */
public class Status {
    public enum HandlerMsgStatus {
        IN_TO_TCPCLIENT,
        ACK_OUT_FROM_TCPCLIENT,
        DATA_OUT_FROM_TCPCLIENT,
        TCPCLIENT_FAILED
    }

    public enum DeviceStatus {
        IDLE,
        WORKING,
        DISCONNECTED
    }

    public enum DeviceWorkingStatus{
        NORMAL,
        ABNORMAL
    }

    public enum Service {
        FINDSTMIS,
        ORIENTATION
    }

    public enum BoardType {
        FDD,
        TDD
    }

    public enum TriggerType{
        SMS,
        PHONE
    }

    public enum SMSResult{
        OK,
        Failed
    }

    public enum PingResult{
        SUCCEED,
        FAILED
    }
}
