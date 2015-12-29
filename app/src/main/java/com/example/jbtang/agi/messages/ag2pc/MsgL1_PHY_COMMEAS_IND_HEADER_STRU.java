package com.example.jbtang.agi.messages.ag2pc;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.type.U16;
import com.example.jbtang.agi.core.type.U32;
import com.example.jbtang.agi.core.type.U8;

/**
 * Created by jbtang on 10/5/2015.
 */
public class MsgL1_PHY_COMMEAS_IND_HEADER_STRU {
    private U8[] mau8TimeStampH; // size = 4
    private U32 mu32TimeStampL;
    private U16 mu16EARFCN;
    private U16 mu16PCI;
    private U32 mu32MeasSelect;

    public static final int byteArrayLen = 4 * U8.byteArrayLen + 2 * U32.byteArrayLen + 2 * U16.byteArrayLen;

    public U8[] getMau8TimeStampH() {
        return mau8TimeStampH;
    }

    public void setMau8TimeStampH(U8[] mau8TimeStampH) {
        this.mau8TimeStampH = mau8TimeStampH;
    }

    public long getMu32TimeStampL() {
        return mu32TimeStampL.getBase();
    }

    public void setMu32TimeStampL(long mu32TimeStampL) {
        this.mu32TimeStampL = new U32(mu32TimeStampL);
    }

    public int getMu16EARFCN() {
        return mu16EARFCN.getBase();
    }

    public void setMu16EARFCN(int mu16EARFCN) {
        this.mu16EARFCN = new U16(mu16EARFCN);
    }

    public int getMu16PCI() {
        return mu16PCI.getBase();
    }

    public void setMu16PCI(int mu16PCI) {
        this.mu16PCI = new U16(mu16PCI);
    }

    public long getMu32MeasSelect() {
        return mu32MeasSelect.getBase();
    }

    public void setMu32MeasSelect(long mu32MeasSelect) {
        this.mu32MeasSelect = new U32(mu32MeasSelect);
    }

    public MsgL1_PHY_COMMEAS_IND_HEADER_STRU() {
        mau8TimeStampH = new U8[4]; // size = 4
        mu32TimeStampL = new U32();
        mu16EARFCN = new U16();
        mu16PCI = new U16();
        mu32MeasSelect = new U32();
    }

    public MsgL1_PHY_COMMEAS_IND_HEADER_STRU(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        mau8TimeStampH = new U8[]{
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen))
        }; // size = 4
        mu32TimeStampL = new U32(MsgSendHelper.getSubByteArray(bytes, pos, U32.byteArrayLen));
        pos += U32.byteArrayLen;
        mu16EARFCN = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mu16PCI = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mu32MeasSelect = new U32(MsgSendHelper.getSubByteArray(bytes, pos, U32.byteArrayLen));
    }

    public byte[] getBytes() {
        MsgSendHelper.AppendByteArray ret = new MsgSendHelper.AppendByteArray();
        for (U8 t : mau8TimeStampH) {
            ret.append(t.getBytes());
        }
        return ret.append(mu32TimeStampL.getBytes())
                .append(mu16EARFCN.getBytes())
                .append(mu16PCI.getBytes())
                .append(mu32MeasSelect.getBytes())
                .toByteArray();
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgL1_PHY_COMMEAS_IND_HEADER_STRU.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message L1_PHY_COMMEAS_IND_HEADER_STRU!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        for (U8 t : mau8TimeStampH) {
            result = prime * result + t.getBase();
        }
        result = prime * result + (int) mu32TimeStampL.getBase();
        result = prime * result + mu16EARFCN.getBase();
        result = prime * result + mu16PCI.getBase();
        result = prime * result + (int) mu32MeasSelect.getBase();
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
        final MsgL1_PHY_COMMEAS_IND_HEADER_STRU other = (MsgL1_PHY_COMMEAS_IND_HEADER_STRU) o;
        for (int i = 0; i < mau8TimeStampH.length; i++) {
            if (!mau8TimeStampH[i].equals(other.mau8TimeStampH[i])) {
                return false;
            }
        }
        return mu32TimeStampL.equals(other.mu32TimeStampL)
                && mu16EARFCN.equals(other.mu16EARFCN)
                && mu16PCI.equals(other.mu16PCI)
                && mu32MeasSelect.equals(other.mu32MeasSelect);
    }
}
