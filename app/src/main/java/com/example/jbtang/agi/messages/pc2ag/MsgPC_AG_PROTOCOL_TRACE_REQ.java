package com.example.jbtang.agi.messages.pc2ag;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.messages.base.MsgHeader;

/**
 * define protocol trace request message
 * Created by jbtang on 10/3/2015.
 */
public class MsgPC_AG_PROTOCOL_TRACE_REQ {
    private MsgHeader header;
    private MsgPROTOCOL_TRACE_UE_INFO_STRU mstUeInfo;
    private MsgPROTOCOL_TRACE_CELL_INFO_STRU mstCellInfo;

    public static final int byteArrayLen = MsgHeader.byteArrayLen + MsgPROTOCOL_TRACE_UE_INFO_STRU.byteArrayLen + MsgPROTOCOL_TRACE_CELL_INFO_STRU.byteArrayLen;


    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }

    public MsgPROTOCOL_TRACE_UE_INFO_STRU getMstUeInfo() {
        return mstUeInfo;
    }

    public void setMstUeInfo(MsgPROTOCOL_TRACE_UE_INFO_STRU mstUeInfo) {
        this.mstUeInfo = mstUeInfo;
    }

    public MsgPROTOCOL_TRACE_CELL_INFO_STRU getMstCellInfo() {
        return mstCellInfo;
    }

    public void setMstCellInfo(MsgPROTOCOL_TRACE_CELL_INFO_STRU mstCellInfo) {
        this.mstCellInfo = mstCellInfo;
    }

    public MsgPC_AG_PROTOCOL_TRACE_REQ() {
        this.header = new MsgHeader();
        this.mstUeInfo = new MsgPROTOCOL_TRACE_UE_INFO_STRU();
        this.mstCellInfo = new MsgPROTOCOL_TRACE_CELL_INFO_STRU();
    }

    public MsgPC_AG_PROTOCOL_TRACE_REQ(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        this.header = new MsgHeader(MsgSendHelper.getSubByteArray(bytes, pos, MsgHeader.byteArrayLen));
        pos += MsgHeader.byteArrayLen;
        this.mstUeInfo = new MsgPROTOCOL_TRACE_UE_INFO_STRU(MsgSendHelper.getSubByteArray(bytes, pos, MsgPROTOCOL_TRACE_UE_INFO_STRU.byteArrayLen));
        pos += MsgPROTOCOL_TRACE_UE_INFO_STRU.byteArrayLen;
        this.mstCellInfo = new MsgPROTOCOL_TRACE_CELL_INFO_STRU(MsgSendHelper.getSubByteArray(bytes, pos, MsgPROTOCOL_TRACE_CELL_INFO_STRU.byteArrayLen));
    }

    public byte[] getBytes() {
        return new MsgSendHelper.AppendByteArray()
                .append(header.getBytes())
                .append(mstUeInfo.getBytes())
                .append(mstCellInfo.getBytes())
                .toByteArray();
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgPC_AG_PROTOCOL_TRACE_REQ.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message PC_AG_PROTOCOL_TRACE_REQ!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + header.hashCode();
        result = prime * result + mstUeInfo.hashCode();
        result = prime * result + mstCellInfo.hashCode();
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
        final MsgPC_AG_PROTOCOL_TRACE_REQ other = (MsgPC_AG_PROTOCOL_TRACE_REQ) o;
        return header.equals(other.header)
                && mstUeInfo.equals(other.mstUeInfo)
                && mstCellInfo.equals(other.mstCellInfo);
    }
}
