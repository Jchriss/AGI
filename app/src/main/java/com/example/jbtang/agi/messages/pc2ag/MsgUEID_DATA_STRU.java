package com.example.jbtang.agi.messages.pc2ag;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.type.U16;

/**
 * define message ue id data: like c++/c# union structure, total byte array length is 21 + 3(align)
 * Created by jbtang on 10/4/2015.
 */
public class MsgUEID_DATA_STRU {
    //TODO: confirm type number
    public static class IDType {
        public static final byte IMSI = 0;
        public static final byte GUTI = 1;
        public static final byte IMEI = 2;
        public static final byte CRNTI = 3;
        public static final byte PRID = 4;
        public static final byte MTMSI = 5;
    }

    private byte[] bytes;
    private U16[] cnrtis;
    private byte type;

    public static final int byteArrayLen = 24;

    public MsgUEID_DATA_STRU() {
        this.type = IDType.IMSI;
        this.bytes = new byte[MsgUEID_DATA_STRU.byteArrayLen];
    }

    public MsgUEID_DATA_STRU(byte type, byte[] bytes) {
        this.type = type;
        validate(bytes);
        this.bytes = bytes;
        if (type == IDType.CRNTI) {
            this.cnrtis = new U16[MsgUEID_DATA_STRU.byteArrayLen];
            cnrtis[0] = new U16(MsgSendHelper.getSubByteArray(bytes, 0, U16.byteArrayLen));
        }
    }

    public byte[] getBytes() {
        return bytes;
    }

    public U16[] getCNRTIs() {
        if (type != IDType.CRNTI) {
            throw new IllegalArgumentException("byte array is not CNRTI type!");
        }
        return cnrtis;
    }

    private void validate(byte[] bytes) {
        if (bytes.length < byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message UEID_DATA_STRU!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + type;
        for (byte b : bytes) {
            result = prime * result + b;
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        final MsgUEID_DATA_STRU other = (MsgUEID_DATA_STRU) o;
        return type == other.type && MsgSendHelper.byteArrayEquals(bytes, other.bytes);
    }
}
