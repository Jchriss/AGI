package com.example.jbtang.agi.messages.pc2ag;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.type.U16;

/**
 * define message usim info
 * Created by jbtang on 10/4/2015.
 */
public class MsgUSIM_INFO_STRU {
    private byte[] mau8IMSI;
    private U16 mau16SimAC;
    private byte mu8KLengthIndex;
    private byte mu8Pading;
    private byte[] maU8KDATA;

    public static final int byteArrayLen = 24 + U16.byteArrayLen + 2 + 32;


    public byte[] getMau8IMSI() {
        return mau8IMSI;
    }

    public void setMau8IMSI(byte[] mau8IMSI) {
        this.mau8IMSI = mau8IMSI;
    }

    public int getMau16SimAC() {
        return mau16SimAC.getBase();
    }

    public void setMau16SimAC(int mau16SimAC) {
        this.mau16SimAC = new U16(mau16SimAC);
    }

    public byte getMu8KLengthIndex() {
        return mu8KLengthIndex;
    }

    public void setMu8KLengthIndex(byte mu8KLengthIndex) {
        this.mu8KLengthIndex = mu8KLengthIndex;
    }

    public byte getMu8Pading() {
        return mu8Pading;
    }

    public void setMu8Pading(byte mu8Pading) {
        this.mu8Pading = mu8Pading;
    }

    public byte[] getMaU8KDATA() {
        return maU8KDATA;
    }

    public void setMaU8KDATA(byte[] maU8KDATA) {
        this.maU8KDATA = maU8KDATA;
    }

    public MsgUSIM_INFO_STRU() {
        this.mau8IMSI = new byte[24];
        this.mau16SimAC = new U16();
        this.mu8KLengthIndex = 0;
        this.mu8Pading = 0;
        this.maU8KDATA = new byte[32];
    }

    public MsgUSIM_INFO_STRU(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        this.mau8IMSI = MsgSendHelper.getSubByteArray(bytes, pos, 24);
        pos += 24;
        this.mau16SimAC = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        this.mu8KLengthIndex = bytes[pos++];
        this.mu8Pading = bytes[pos++];
        this.maU8KDATA = MsgSendHelper.getSubByteArray(bytes, pos, 32);
    }

    public byte[] getBytes() {
        return new MsgSendHelper.AppendByteArray()
                .append(mau8IMSI)
                .append(mau16SimAC.getBytes())
                .append(mu8KLengthIndex)
                .append(mu8Pading)
                .append(maU8KDATA)
                .toByteArray();
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgUSIM_INFO_STRU.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message USIM_INFO_STRU!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        for (byte b : mau8IMSI) {
            result = prime * result + b;
        }
        result = prime * result + mau16SimAC.getBase();
        result = prime * result + mu8KLengthIndex;
        result = prime * result + mu8Pading;
        for (byte b : maU8KDATA) {
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
        final MsgUSIM_INFO_STRU other = (MsgUSIM_INFO_STRU) o;
        return MsgSendHelper.byteArrayEquals(mau8IMSI, other.mau8IMSI)
                && mau16SimAC.equals(other.mau16SimAC)
                && mu8KLengthIndex == other.mu8KLengthIndex
                && mu8Pading == other.mu8Pading
                && MsgSendHelper.byteArrayEquals(maU8KDATA, other.maU8KDATA);
    }
}
