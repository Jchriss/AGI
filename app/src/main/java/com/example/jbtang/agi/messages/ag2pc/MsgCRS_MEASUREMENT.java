package com.example.jbtang.agi.messages.ag2pc;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.type.U16;

/**
 * Created by jbtang on 10/5/2015.
 */
public class MsgCRS_MEASUREMENT {
    private short ms16CRS_RSSI;
    private short ms16CRS_RP;
    private short ms16CRS_RQ;
    private U16 mu16Padding;

    public static final int byteArrayLen = 3 * 2 + U16.byteArrayLen;


    public short getMs16CRS_RSSI() {
        return ms16CRS_RSSI;
    }

    public void setMs16CRS_RSSI(short ms16CRS_RSSI) {
        this.ms16CRS_RSSI = ms16CRS_RSSI;
    }

    public short getMs16CRS_RP() {
        return ms16CRS_RP;
    }

    public void setMs16CRS_RP(short ms16CRS_RP) {
        this.ms16CRS_RP = ms16CRS_RP;
    }

    public short getMs16CRS_RQ() {
        return ms16CRS_RQ;
    }

    public void setMs16CRS_RQ(short ms16CRS_RQ) {
        this.ms16CRS_RQ = ms16CRS_RQ;
    }

    public int getMu16Padding() {
        return mu16Padding.getBase();
    }

    public void setMu16Padding(int mu16Padding) {
        this.mu16Padding = new U16(mu16Padding);
    }

    public MsgCRS_MEASUREMENT() {
        ms16CRS_RSSI = 0;
        ms16CRS_RP = 0;
        ms16CRS_RQ = 0;
        mu16Padding = new U16();
    }

    public MsgCRS_MEASUREMENT(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        ms16CRS_RSSI = MsgSendHelper.byteArrayToShort(MsgSendHelper.getSubByteArray(bytes, pos, 2));
        pos += 2;
        ms16CRS_RP = MsgSendHelper.byteArrayToShort(MsgSendHelper.getSubByteArray(bytes, pos, 2));
        pos += 2;
        ms16CRS_RQ = MsgSendHelper.byteArrayToShort(MsgSendHelper.getSubByteArray(bytes, pos, 2));
        pos += 2;
        mu16Padding = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
    }

    public byte[] getBytes() {
        return new MsgSendHelper.AppendByteArray()
                .append(MsgSendHelper.shortToByteArray(ms16CRS_RSSI))
                .append(MsgSendHelper.shortToByteArray(ms16CRS_RP))
                .append(MsgSendHelper.shortToByteArray(ms16CRS_RQ))
                .append(mu16Padding.getBytes())
                .toByteArray();
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgCRS_MEASUREMENT.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message CRS_MEASUREMENT!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ms16CRS_RSSI;
        result = prime * result + ms16CRS_RP;
        result = prime * result + ms16CRS_RQ;
        result = prime * result + mu16Padding.getBase();
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
        final MsgCRS_MEASUREMENT other = (MsgCRS_MEASUREMENT) o;
        return ms16CRS_RSSI == other.ms16CRS_RSSI
                && ms16CRS_RP == other.ms16CRS_RP
                && ms16CRS_RQ == other.ms16CRS_RQ
                && mu16Padding.equals(other.mu16Padding);
    }
}
