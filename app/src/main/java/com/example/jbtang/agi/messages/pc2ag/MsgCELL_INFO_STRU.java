package com.example.jbtang.agi.messages.pc2ag;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.type.U16;

/**
 * define cell info message
 * Created by jbtang on 10/4/2015.
 */
public class MsgCELL_INFO_STRU {
    private U16 u16EARFCN;
    private U16 u16PCI;

    public static final int byteArrayLen = U16.byteArrayLen * 2;

    public int getU16EARFCN() {
        return u16EARFCN.getBase();
    }

    public void setU16EARFCN(int u16EARFCN) {
        this.u16EARFCN = new U16(u16EARFCN);
    }

    public int getU16PCI() {
        return u16PCI.getBase();
    }

    public void setU16PCI(int u16PCI) {
        this.u16PCI = new U16(u16PCI);
    }

    public MsgCELL_INFO_STRU() {
        this.u16EARFCN = new U16();
        this.u16PCI = new U16();
    }

    public MsgCELL_INFO_STRU(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        this.u16EARFCN = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        this.u16PCI = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
    }

    public byte[] getBytes() {
        return new MsgSendHelper.AppendByteArray()
                .append(u16EARFCN.getBytes())
                .append(u16PCI.getBytes())
                .toByteArray();
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgCELL_INFO_STRU.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message CELL_INFO_STRU!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + u16EARFCN.getBase();
        result = prime * result + u16PCI.getBase();
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
        final MsgCELL_INFO_STRU other = (MsgCELL_INFO_STRU) o;
        return u16EARFCN.equals(other.u16EARFCN) && u16PCI.equals(other.u16PCI);
    }
}
