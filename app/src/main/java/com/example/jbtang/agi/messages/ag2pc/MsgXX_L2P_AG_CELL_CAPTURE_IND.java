package com.example.jbtang.agi.messages.ag2pc;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.messages.base.MsgHeader;

/**
 * Created by jbtang on 10/5/2015.
 */
public class MsgXX_L2P_AG_CELL_CAPTURE_IND {
    private MsgHeader header;
    private MsgL2P_AG_CELL_CAPTURE_IND mstCellCaptureHeader;

    public static final int byteArrayLen = MsgHeader.byteArrayLen + MsgL2P_AG_CELL_CAPTURE_IND.byteArrayLen;

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }

    public MsgL2P_AG_CELL_CAPTURE_IND getMstCellCaptureHeader() {
        return mstCellCaptureHeader;
    }

    public void setMstCellCaptureHeader(MsgL2P_AG_CELL_CAPTURE_IND mstCellCaptureHeader) {
        this.mstCellCaptureHeader = mstCellCaptureHeader;
    }

    public MsgXX_L2P_AG_CELL_CAPTURE_IND() {
        this.header = new MsgHeader();
        this.mstCellCaptureHeader = new MsgL2P_AG_CELL_CAPTURE_IND();
    }

    public MsgXX_L2P_AG_CELL_CAPTURE_IND(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        this.header = new MsgHeader(MsgSendHelper.getSubByteArray(bytes, pos, MsgHeader.byteArrayLen));
        pos += MsgHeader.byteArrayLen;
        this.mstCellCaptureHeader = new MsgL2P_AG_CELL_CAPTURE_IND(MsgSendHelper.getSubByteArray(bytes, pos, MsgL2P_AG_CELL_CAPTURE_IND.byteArrayLen));
    }

    private void validate(byte[] bytes) {
        if (bytes.length <MsgXX_L2P_AG_CELL_CAPTURE_IND.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message XX_L2P_AG_CELL_CAPTURE_IND!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + header.hashCode();
        result = prime * result + mstCellCaptureHeader.hashCode();
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
        final MsgXX_L2P_AG_CELL_CAPTURE_IND other = (MsgXX_L2P_AG_CELL_CAPTURE_IND) o;
        return header.equals(other.header) && mstCellCaptureHeader.equals(other.mstCellCaptureHeader);
    }
}
