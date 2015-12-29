package com.example.jbtang.agi.messages.ag2pc;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.type.U16;
import com.example.jbtang.agi.core.type.U8;

/**
 * Created by jbtang on 11/7/2015.
 */
public class MsgL1_UL_UE_MEAS {
    private U8 mu8RNTIType;
    private U8 muChType;
    private U16 mu16UeIndValue;
    private Short ms16Power;
    private U16 mu16Ta;
    private byte ms8Sinr;
    private U8 mu8PucchFormat;
    private U8[] mau8Padding; // size = 2

    public static final int byteArrayLen = (4 + 2) * U8.byteArrayLen + 3 * U16.byteArrayLen;

    public int getMu8RNTIType() {
        return mu8RNTIType.getBase();
    }

    public void setMu8RNTIType(int mu8RNTIType) {
        this.mu8RNTIType = new U8(mu8RNTIType);
    }

    public int getMuChType() {
        return muChType.getBase();
    }

    public void setMuChType(int muChType) {
        this.muChType = new U8(muChType);
    }

    public int getMu16UeIndValue() {
        return mu16UeIndValue.getBase();
    }

    public void setMu16UeIndValue(int mu16UeIndValue) {
        this.mu16UeIndValue = new U16(mu16UeIndValue);
    }

    public Short getMs16Power() {
        return ms16Power;
    }

    public void setMs16Power(Short ms16Power) {
        this.ms16Power = ms16Power;
    }

    public int getMu16Ta() {
        return mu16Ta.getBase();
    }

    public void setMu16Ta(int mu16Ta) {
        this.mu16Ta = new U16(mu16Ta);
    }

    public byte getMs8Sinr() {
        return ms8Sinr;
    }

    public void setMs8Sinr(byte ms8Sinr) {
        this.ms8Sinr = ms8Sinr;
    }

    public int getMu8PucchFormat() {
        return mu8PucchFormat.getBase();
    }

    public void setMu8PucchFormat(int mu8PucchFormat) {
        this.mu8PucchFormat = new U8(mu8PucchFormat);
    }

    public U8[] getMau8Padding() {
        return mau8Padding;
    }

    public void setMau8Padding(U8[] mau8Padding) {
        this.mau8Padding = mau8Padding;
    }

    public MsgL1_UL_UE_MEAS() {
        mu8RNTIType = new U8();
        muChType = new U8();
        mu16UeIndValue = new U16();
        ms16Power = 0;
        mu16Ta = new U16();
        ms8Sinr = 0;
        mu8PucchFormat = new U8();
        mau8Padding = new U8[2]; // size = 2
    }

    public MsgL1_UL_UE_MEAS(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        mu8RNTIType = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        muChType = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        mu16UeIndValue = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        ms16Power = MsgSendHelper.byteArrayToShort(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mu16Ta = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        ms8Sinr = bytes[pos];
        pos += U8.byteArrayLen;
        mu8PucchFormat = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        mau8Padding = new U8[]{
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen))
        }; // size = 2
    }

    public byte[] getBytes() {
        MsgSendHelper.AppendByteArray ret = new MsgSendHelper.AppendByteArray();
        ret.append(mu8RNTIType.getBytes())
                .append(muChType.getBytes())
                .append(mu16UeIndValue.getBytes())
                .append(MsgSendHelper.shortToByteArray(ms16Power))
                .append(mu16Ta.getBytes())
                .append(ms8Sinr)
                .append(mu8PucchFormat.getBytes());
        for (U8 t : mau8Padding) {
            ret.append(t.getBytes());
        }
        return ret.toByteArray();
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgL1_UL_UE_MEAS.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message L1_UL_UE_MEAS!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mu8RNTIType.getBase();
        result = prime * result + muChType.getBase();
        result = prime * result + mu16UeIndValue.getBase();
        result = prime * result + ms16Power;
        result = prime * result + mu16Ta.getBase();
        result = prime * result + ms8Sinr;
        result = prime * result + mu8PucchFormat.getBase();
        for (U8 t : mau8Padding) {
            result = prime * result + t.getBase();
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
        final MsgL1_UL_UE_MEAS other = (MsgL1_UL_UE_MEAS) o;
        for (int i = 0; i < mau8Padding.length; i++) {
            if (!mau8Padding[i].equals(other.mau8Padding[i])) {
                return false;
            }
        }
        return mu8RNTIType.equals(other.mu8RNTIType)
                && muChType.equals(other.muChType)
                && mu16UeIndValue.equals(other.mu16UeIndValue)
                && (ms16Power == other.ms16Power)
                && mu16Ta.equals(other.mu16Ta)
                && (ms8Sinr == other.ms8Sinr)
                && mu8PucchFormat.equals(other.mu8PucchFormat);
    }
}
