package com.example.jbtang.agi.messages.ag2pc;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.type.U16;
import com.example.jbtang.agi.core.type.U8;

/**
 * Created by jbtang on 10/5/2015.
 */
public class MsgAG_UE_CAPTURE_INFO_STRU {
    private U16 mu16EARFCN;
    private U16 mu16PCI;
    private U8 mu8UEIDTypeFlg;
    private U8 mu8ImsiDigitCnt;
    private U8[] mau8IMSI; // size = 21
    private U8[] mau8GUTIDATA; // size = 10
    private U8[] mau8IMEI; // size = 15
    private U16 mu16CRNTIDATA;
    private U8 mu8PRIDDATA;
    private U8 mu8Pading1;

    public static final int byteArrayLen = 3 * U16.byteArrayLen + (2 + 21 + 10 + 15 + 2) * U8.byteArrayLen;


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

    public int getMu8UEIDTypeFlg() {
        return mu8UEIDTypeFlg.getBase();
    }

    public void setMu8UEIDTypeFlg(int mu8UEIDTypeFlg) {
        this.mu8UEIDTypeFlg = new U8(mu8UEIDTypeFlg);
    }

    public int getMu8ImsiDigitCnt() {
        return mu8ImsiDigitCnt.getBase();
    }

    public void setMu8ImsiDigitCnt(int mu8ImsiDigitCnt) {
        this.mu8ImsiDigitCnt = new U8(mu8ImsiDigitCnt);
    }

    public U8[] getMau8IMSI() {
        return mau8IMSI;
    }

    public void setMau8IMSI(U8[] mau8IMSI) {
        this.mau8IMSI = mau8IMSI;
    }

    public U8[] getMau8GUTIDATA() {
        return mau8GUTIDATA;
    }

    public void setMau8GUTIDATA(U8[] mau8GUTIDATA) {
        this.mau8GUTIDATA = mau8GUTIDATA;
    }

    public U8[] getMau8IMEI() {
        return mau8IMEI;
    }

    public void setMau8IMEI(U8[] mau8IMEI) {
        this.mau8IMEI = mau8IMEI;
    }

    public int getMu16CRNTIDATA() {
        return mu16CRNTIDATA.getBase();
    }

    public void setMu16CRNTIDATA(int mu16CRNTIDATA) {
        this.mu16CRNTIDATA = new U16(mu16CRNTIDATA);
    }

    public int getMu8PRIDDATA() {
        return mu8PRIDDATA.getBase();
    }

    public void setMu8PRIDDATA(int mu8PRIDDATA) {
        this.mu8PRIDDATA = new U8(mu8PRIDDATA);
    }

    public int getMu8Pading1() {
        return mu8Pading1.getBase();
    }

    public void setMu8Pading1(int mu8Pading1) {
        this.mu8Pading1 = new U8(mu8Pading1);
    }

    public MsgAG_UE_CAPTURE_INFO_STRU() {
        mu16EARFCN = new U16();
        mu16PCI = new U16();
        mu8UEIDTypeFlg = new U8();
        mu8ImsiDigitCnt = new U8();
        mau8IMSI = new U8[21]; // size = 21
        mau8GUTIDATA = new U8[10]; // size = 10
        mau8IMEI = new U8[15]; // size = 15
        mu16CRNTIDATA = new U16();
        mu8PRIDDATA = new U8();
        mu8Pading1 = new U8();
    }

    public MsgAG_UE_CAPTURE_INFO_STRU(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        mu16EARFCN = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mu16PCI = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mu8UEIDTypeFlg = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        mu8ImsiDigitCnt = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        mau8IMSI = new U8[21]; // size = 21
        for (int i = 0; i < mau8IMSI.length; i++) {
            mau8IMSI[i] = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
            pos += U8.byteArrayLen;
        }
        mau8GUTIDATA = new U8[10]; // size = 10
        for (int i = 0; i < mau8GUTIDATA.length; i++) {
            mau8GUTIDATA[i] = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
            pos += U8.byteArrayLen;
        }
        mau8IMEI = new U8[15]; // size = 15
        for (int i = 0; i < mau8IMEI.length; i++) {
            mau8IMEI[i] = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
            pos += U8.byteArrayLen;
        }
        mu16CRNTIDATA = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mu8PRIDDATA = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        mu8Pading1 = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
    }

    public byte[] getBytes() {
        MsgSendHelper.AppendByteArray ret = new MsgSendHelper.AppendByteArray();
        ret.append(mu16EARFCN.getBytes())
                .append(mu16PCI.getBytes())
                .append(mu8UEIDTypeFlg.getBytes())
                .append(mu8ImsiDigitCnt.getBytes());
        for (U8 t : mau8IMSI) {
            ret.append(t.getBytes());
        }
        for (U8 t : mau8GUTIDATA) {
            ret.append(t.getBytes());
        }
        for (U8 t : mau8IMEI) {
            ret.append(t.getBytes());
        }
        ret.append(mu16CRNTIDATA.getBytes())
                .append(mu8PRIDDATA.getBytes())
                .append(mu8Pading1.getBytes());
        return ret.toByteArray();
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgAG_UE_CAPTURE_INFO_STRU.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message AG_UE_CAPTURE_INFO_STRU!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mu16EARFCN.getBase();
        result = prime * result + mu16PCI.getBase();
        result = prime * result + mu8UEIDTypeFlg.getBase();
        result = prime * result + mu8ImsiDigitCnt.getBase();
        for (U8 t : mau8IMSI) {
            result = prime * result + t.getBase();
        }
        for (U8 t : mau8GUTIDATA) {
            result = prime * result + t.getBase();
        }
        for (U8 t : mau8IMEI) {
            result = prime * result + t.getBase();
        }
        result = prime * result + mu16CRNTIDATA.getBase();
        result = prime * result + mu8PRIDDATA.getBase();
        result = prime * result + mu8Pading1.getBase();
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
        final MsgAG_UE_CAPTURE_INFO_STRU other = (MsgAG_UE_CAPTURE_INFO_STRU) o;
        for (int i = 0; i < mau8IMSI.length; i++) {
            if (!mau8IMSI[i].equals(other.mau8IMSI[i])) {
                return false;
            }
        }
        for (int i = 0; i < mau8GUTIDATA.length; i++) {
            if (!mau8GUTIDATA[i].equals(other.mau8GUTIDATA[i])) {
                return false;
            }
        }
        for (int i = 0; i < mau8IMEI.length; i++) {
            if (!mau8IMEI[i].equals(other.mau8IMEI[i])) {
                return false;
            }
        }
        return mu16EARFCN.equals(other.mu16EARFCN)
                && mu16PCI.equals(other.mu16PCI)
                && mu8UEIDTypeFlg.equals(other.mu8UEIDTypeFlg)
                && mu8ImsiDigitCnt.equals(other.mu8ImsiDigitCnt)
                && mu16CRNTIDATA.equals(other.mu16CRNTIDATA)
                && mu8PRIDDATA.equals(other.mu8PRIDDATA)
                && mu8Pading1.equals(other.mu8Pading1);
    }
}
