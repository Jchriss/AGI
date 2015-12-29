package com.example.jbtang.agi.messages.ag2pc;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.type.U32;
import com.example.jbtang.agi.core.type.U8;
import com.example.jbtang.agi.messages.base.MsgHeader;

/**
 * Created by jbtang on 10/5/2015.
 */
public class MsgL2P_AG_UE_CAPTURE_IND {
    private MsgHeader header;
    private U8[] TimeStampH; // size 4
    private U32 TimeStampL;
    private MsgAG_UE_CAPTURE_INFO_STRU mstUECaptureInfo;

    public static final int byteArrayLen = MsgHeader.byteArrayLen + 4 * U8.byteArrayLen + U32.byteArrayLen + MsgAG_UE_CAPTURE_INFO_STRU.byteArrayLen;

    public MsgHeader getHeader() {
        return header;
    }

    public void setHeader(MsgHeader header) {
        this.header = header;
    }

    public U8[] getTimeStampH() {
        return TimeStampH;
    }

    public void setTimeStampH(U8[] timeStampH) {
        TimeStampH = timeStampH;
    }

    public long getTimeStampL() {
        return TimeStampL.getBase();
    }

    public void setTimeStampL(long timeStampL) {
        TimeStampL = new U32(timeStampL);
    }

    public MsgAG_UE_CAPTURE_INFO_STRU getMstUECaptureInfo() {
        return mstUECaptureInfo;
    }

    public void setMstUECaptureInfo(MsgAG_UE_CAPTURE_INFO_STRU mstUECaptureInfo) {
        this.mstUECaptureInfo = mstUECaptureInfo;
    }

    public MsgL2P_AG_UE_CAPTURE_IND() {
        header = new MsgHeader();
        TimeStampH = new U8[4]; // size 4
        TimeStampL = new U32();
        mstUECaptureInfo = new MsgAG_UE_CAPTURE_INFO_STRU();
    }

    public MsgL2P_AG_UE_CAPTURE_IND(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        header = new MsgHeader(MsgSendHelper.getSubByteArray(bytes, pos, MsgHeader.byteArrayLen));
        pos += MsgHeader.byteArrayLen;
        TimeStampH = new U8[]{
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen))
        }; // size 4
        TimeStampL = new U32(MsgSendHelper.getSubByteArray(bytes, pos, U32.byteArrayLen));
        pos += U32.byteArrayLen;
        mstUECaptureInfo = new MsgAG_UE_CAPTURE_INFO_STRU(MsgSendHelper.getSubByteArray(bytes, pos, MsgAG_UE_CAPTURE_INFO_STRU.byteArrayLen));
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgL2P_AG_UE_CAPTURE_IND.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message L2P_AG_UE_CAPTURE_IND!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + header.hashCode();
        for (U8 t : TimeStampH) {
            result = prime * result + t.getBase();
        }
        result = prime * result + (int) TimeStampL.getBase();
        result = prime * result + mstUECaptureInfo.hashCode();
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
        final MsgL2P_AG_UE_CAPTURE_IND other = (MsgL2P_AG_UE_CAPTURE_IND) o;
        for (int i = 0; i < TimeStampH.length; i++) {
            if (!TimeStampH[i].equals(other.TimeStampH[i])) {
                return false;
            }
        }
        return header.equals(other.header)
                && TimeStampL.equals(other.TimeStampL)
                && mstUECaptureInfo.equals(other.mstUECaptureInfo);
    }
}
