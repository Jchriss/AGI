package com.example.jbtang.agi.messages.ag2pc;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.messages.base.MsgHeader;

/**
 * Created by jbtang on 10/5/2015.
 */
public class MsgL1_PHY_COMMEAS_IND {
    private MsgHeader header;
    private MsgL1_PHY_COMMEAS_IND_HEADER_STRU mstL1PHYComentIndHeader;

    public static final int byteArrayLen = MsgHeader.byteArrayLen + MsgL1_PHY_COMMEAS_IND_HEADER_STRU.byteArrayLen;


    public MsgL1_PHY_COMMEAS_IND_HEADER_STRU getMstL1PHYComentIndHeader() {
        return mstL1PHYComentIndHeader;
    }

    public void setMstL1PHYComentIndHeader(MsgL1_PHY_COMMEAS_IND_HEADER_STRU mstL1PHYComentIndHeader) {
        this.mstL1PHYComentIndHeader = mstL1PHYComentIndHeader;
    }

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }

    public MsgL1_PHY_COMMEAS_IND() {
        this.header = new MsgHeader();
        this.mstL1PHYComentIndHeader = new MsgL1_PHY_COMMEAS_IND_HEADER_STRU();
    }

    public MsgL1_PHY_COMMEAS_IND(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        this.header = new MsgHeader(MsgSendHelper.getSubByteArray(bytes, pos, MsgHeader.byteArrayLen));
        pos += MsgHeader.byteArrayLen;
        this.mstL1PHYComentIndHeader = new MsgL1_PHY_COMMEAS_IND_HEADER_STRU(MsgSendHelper.getSubByteArray(bytes, pos, MsgL1_PHY_COMMEAS_IND_HEADER_STRU.byteArrayLen));
    }

    public byte[] getBytes() {
        return new MsgSendHelper.AppendByteArray()
                .append(header.getBytes())
                .append(mstL1PHYComentIndHeader.getBytes())
                .toByteArray();
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgL1_PHY_COMMEAS_IND.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message L1_PHY_COMMEAS_IND!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + header.hashCode();
        result = prime * result + mstL1PHYComentIndHeader.hashCode();
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
        final MsgL1_PHY_COMMEAS_IND other = (MsgL1_PHY_COMMEAS_IND) o;
        return header.equals(other.header)
                && mstL1PHYComentIndHeader.equals(other.mstL1PHYComentIndHeader);
    }
}
