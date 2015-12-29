package com.example.jbtang.agi.messages.ag2pc;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.type.U8;
import com.example.jbtang.agi.messages.base.MsgHeader;

/**
 * Created by jbtang on 10/5/2015.
 */
public class MsgL1_PROTOCOL_DATA {
    private MsgHeader header;
    private MsgL1_PROTOCOL_DATA_HEADER_STRU mstL1ProtocolDataHeader;
    private U8 mu8CFINum;
    private U8 mu8UeNum;
    private U8[] mau8Padding; // size = 2

    public static final int byteArrayLen = MsgHeader.byteArrayLen + MsgL1_PROTOCOL_DATA_HEADER_STRU.byteArrayLen + (2 + 2) * U8.byteArrayLen;

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }

    public MsgL1_PROTOCOL_DATA_HEADER_STRU getMstL1ProtocolDataHeader() {
        return mstL1ProtocolDataHeader;
    }

    public void setMstL1ProtocolDataHeader(MsgL1_PROTOCOL_DATA_HEADER_STRU mstL1ProtocolDataHeader) {
        this.mstL1ProtocolDataHeader = mstL1ProtocolDataHeader;
    }

    public int getMu8CFINum() {
        return mu8CFINum.getBase();
    }

    public void setMu8CFINum(int mu8CFINum) {
        this.mu8CFINum = new U8(mu8CFINum);
    }

    public int getMu8UeNum() {
        return mu8UeNum.getBase();
    }

    public void setMu8UeNum(int mu8UeNum) {
        this.mu8UeNum = new U8(mu8UeNum);
    }

    public U8[] getMau8Padding() {
        return mau8Padding;
    }

    public void setMau8Padding(U8[] mau8Padding) {
        this.mau8Padding = mau8Padding;
    }

    public MsgL1_PROTOCOL_DATA() {
        header = new MsgHeader();
        mstL1ProtocolDataHeader = new MsgL1_PROTOCOL_DATA_HEADER_STRU();
        mu8CFINum = new U8();
        mu8UeNum = new U8();
        mau8Padding = new U8[2]; // size = 2
    }

    public MsgL1_PROTOCOL_DATA(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        header = new MsgHeader(MsgSendHelper.getSubByteArray(bytes, pos, MsgHeader.byteArrayLen));
        pos += MsgHeader.byteArrayLen;
        mstL1ProtocolDataHeader = new MsgL1_PROTOCOL_DATA_HEADER_STRU(MsgSendHelper.getSubByteArray(bytes, pos, MsgL1_PROTOCOL_DATA_HEADER_STRU.byteArrayLen));
        pos += MsgL1_PROTOCOL_DATA_HEADER_STRU.byteArrayLen;
        mu8CFINum = new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen));
        mu8UeNum = new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen));
        mau8Padding = new U8[]{
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen))
        }; // size = 2
    }

    public byte[] getBytes() {
        MsgSendHelper.AppendByteArray ret = new MsgSendHelper.AppendByteArray();
        ret.append(header.getBytes())
                .append(mstL1ProtocolDataHeader.getBytes())
                .append(mu8CFINum.getBytes())
                .append(mu8UeNum.getBytes());
        for (U8 t : mau8Padding) {
            ret.append(t.getBytes());
        }
        return ret.toByteArray();
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgL1_PROTOCOL_DATA.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message L1_PROTOCOL_DATA!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + header.hashCode();
        result = prime * result + mstL1ProtocolDataHeader.hashCode();
        result = prime * result + mu8CFINum.getBase();
        result = prime * result + mu8UeNum.getBase();
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
        final MsgL1_PROTOCOL_DATA other = (MsgL1_PROTOCOL_DATA) o;
        for (int i = 0; i < mau8Padding.length; i++) {
            if (!mau8Padding[i].equals(other.mau8Padding[i])) {
                return false;
            }
        }
        return header.equals(other.header)
                && mstL1ProtocolDataHeader.equals(other.mstL1ProtocolDataHeader)
                && mu8CFINum.equals(other.mu8CFINum)
                && mu8UeNum.equals(other.mu8UeNum);
    }
}
