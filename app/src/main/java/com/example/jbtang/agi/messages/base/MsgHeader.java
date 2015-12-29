package com.example.jbtang.agi.messages.base;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.type.U16;
import com.example.jbtang.agi.core.type.U32;
import com.example.jbtang.agi.core.type.U8;

/**
 * Message head
 * Created by jbtang on 9/29/2015.
 */
public class MsgHeader {
    private U32 reserved;
    private byte srcID;
    private byte destID;
    private U16 msgType;
    private U16 transactionID;
    private U16 msgLen;

    public static final int byteArrayLen = U32.byteArrayLen + 2 * U8.byteArrayLen + 3 * U16.byteArrayLen;

    public long getReserved() {
        return reserved.getBase();
    }

    public void setReserved(long reserved) {
        this.reserved = new U32(reserved);
    }

    public byte getSrcID() {
        return srcID;
    }

    public void setSrcID(byte srcID) {
        this.srcID = srcID;
    }

    public byte getDestID() {
        return destID;
    }

    public void setDestID(byte destID) {
        this.destID = destID;
    }

    public int getMsgType() {
        return msgType.getBase();
    }

    public void setMsgType(int msgType) {
        this.msgType = new U16(msgType);
    }

    public int getTransactionID() {
        return transactionID.getBase();
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = new U16(transactionID);
    }

    public int getMsgLen() {
        return msgLen.getBase();
    }

    public void setMsgLen(int msgLen) {
        this.msgLen = new U16(msgLen);
    }

    public MsgHeader() {
        reserved = new U32();
        srcID = 0;
        destID = 0;
        msgType = new U16();
        transactionID = new U16();
        msgLen = new U16();
    }

    public MsgHeader(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        reserved = new U32(MsgSendHelper.getSubByteArray(bytes, pos, U32.byteArrayLen));
        pos += U32.byteArrayLen;
        srcID = bytes[pos++];
        destID = bytes[pos++];
        msgType = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        transactionID = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        msgLen = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
    }

    public byte[] getBytes() {
        return new MsgSendHelper.AppendByteArray()
                .append(reserved.getBytes())
                .append(srcID)
                .append(destID)
                .append(msgType.getBytes())
                .append(transactionID.getBytes())
                .append(msgLen.getBytes())
                .toByteArray();
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgHeader.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message header!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) reserved.getBase();
        result = prime * result + srcID;
        result = prime * result + destID;
        result = prime * result + msgType.getBase();
        result = prime * result + transactionID.getBase();
        result = prime * result + msgLen.getBase();
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
        final MsgHeader other = (MsgHeader) o;
        return reserved.equals(other.reserved)
                && srcID == other.srcID
                && destID == other.destID
                && msgType.equals(other.msgType)
                && transactionID.equals(other.transactionID)
                && msgLen.equals(other.msgLen);
    }
}
