package com.example.jbtang.agi.messages.pc2ag;

import com.example.jbtang.agi.core.MsgSendHelper;

/**
 * define PLMN id message
 * Created by jbtang on 10/3/2015.
 */
public class MsgPLMNID_STRU {
    private byte[] mau8AucMcc; //size = 3
    private byte mau8Pading1;
    private byte[] mau8AucMnc; //size = 2
    private byte[] mau8Pading2; //size = 2

    public static final int byteArrayLen = 3 + 1 + 2 + 2;

    public byte[] getMau8AucMcc() {
        return mau8AucMcc;
    }

    public void setMau8AucMcc(byte[] mau8AucMcc) {
        this.mau8AucMcc = mau8AucMcc;
    }

    public byte getMau8Pading1() {
        return mau8Pading1;
    }

    public void setMau8Pading1(byte mau8Pading1) {
        this.mau8Pading1 = mau8Pading1;
    }

    public byte[] getMau8AucMnc() {
        return mau8AucMnc;
    }

    public void setMau8AucMnc(byte[] mau8AucMnc) {
        this.mau8AucMnc = mau8AucMnc;
    }

    public byte[] getMau8Pading2() {
        return mau8Pading2;
    }

    public void setMau8Pading2(byte[] mau8Pading2) {
        this.mau8Pading2 = mau8Pading2;
    }

    public MsgPLMNID_STRU() {
        this.mau8AucMcc = new byte[3];
        this.mau8AucMnc = new byte[2];
        this.mau8Pading1 = 0;
        this.mau8Pading2 = new byte[2];
    }

    public MsgPLMNID_STRU(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        this.mau8AucMcc = MsgSendHelper.getSubByteArray(bytes, 0, 3);
        this.mau8Pading1 = bytes[3];
        pos += 4;
        this.mau8AucMnc = MsgSendHelper.getSubByteArray(bytes, pos, 2);
        pos += 2;
        this.mau8Pading2 = MsgSendHelper.getSubByteArray(bytes, pos, 2);
    }

    public byte[] getBytes() {
        return new MsgSendHelper.AppendByteArray()
                .append(mau8AucMcc)
                .append(new byte[]{mau8Pading1})
                .append(mau8AucMnc)
                .append(mau8Pading2)
                .toByteArray();
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgPLMNID_STRU.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message PLMNID_STRU!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        for (byte b : mau8AucMcc) {
            result = prime * result + b;
        }
        result = prime * result + mau8Pading1;
        for (byte b : mau8AucMnc) {
            result = prime * result + b;
        }
        for (byte b : mau8Pading2) {
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
        final MsgPLMNID_STRU other = (MsgPLMNID_STRU) o;
        return MsgSendHelper.byteArrayEquals(mau8AucMcc, other.getMau8AucMcc())
                && MsgSendHelper.byteArrayEquals(mau8AucMnc, other.getMau8AucMnc())
                && (mau8Pading1 == other.getMau8Pading1())
                && MsgSendHelper.byteArrayEquals(mau8Pading2, other.getMau8Pading2());
    }
}
