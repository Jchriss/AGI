package com.example.jbtang.agi.messages.base;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.type.U8;

/**
 * define ACK message
 * Created by jbtang on 10/2/2015.
 */
public class MsgACK {
    private MsgHeader header;
    private U8 cause;
    public static final int alignByteCount = 4 - U8.byteArrayLen;

    public static final int byteArrayLen = MsgHeader.byteArrayLen + U8.byteArrayLen + alignByteCount;

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }

    public int getCause() {
        return cause.getBase();
    }

    public void setCause(int cause) {
        this.cause = new U8(cause);
    }

    public MsgACK() {
        this.header = new MsgHeader();
        this.cause = new U8();
    }

    public MsgACK(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        this.header = new MsgHeader(MsgSendHelper.getSubByteArray(bytes, pos, MsgHeader.byteArrayLen));
        pos += MsgHeader.byteArrayLen;
        this.cause = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
    }

    public byte[] getBytes() {
        return new MsgSendHelper.AppendByteArray()
                .append(header.getBytes())
                .append(cause.getBytes())
                .append(new byte[alignByteCount])
                .toByteArray();
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgACK.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message ACK!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + header.hashCode();
        result = prime * result + cause.getBase();
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
        final MsgACK other = (MsgACK) o;
        return header.equals(other.header) && cause.equals(other.cause);
    }
}
