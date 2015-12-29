package com.example.jbtang.agi.messages.ag2pc;

import com.example.jbtang.agi.core.MsgSendHelper;

/**
 * Created by jbtang on 10/5/2015.
 */
public class MsgCRS_RSRPQI_INFO {
    private MsgCRS_MEASUREMENT mstCrs0RsrpqiInfo;
    private MsgCRS_MEASUREMENT mstCrs1RsrpqiInfo;

    public static final int byteArrayLen = 2 * MsgCRS_MEASUREMENT.byteArrayLen;

    public MsgCRS_MEASUREMENT getMstCrs0RsrpqiInfo() {
        return mstCrs0RsrpqiInfo;
    }

    public void setMstCrs0RsrpqiInfo(MsgCRS_MEASUREMENT mstCrs0RsrpqiInfo) {
        this.mstCrs0RsrpqiInfo = mstCrs0RsrpqiInfo;
    }

    public MsgCRS_MEASUREMENT getMstCrs1RsrpqiInfo() {
        return mstCrs1RsrpqiInfo;
    }

    public void setMstCrs1RsrpqiInfo(MsgCRS_MEASUREMENT mstCrs1RsrpqiInfo) {
        this.mstCrs1RsrpqiInfo = mstCrs1RsrpqiInfo;
    }

    public MsgCRS_RSRPQI_INFO() {
        this.mstCrs0RsrpqiInfo = new MsgCRS_MEASUREMENT();
        this.mstCrs1RsrpqiInfo = new MsgCRS_MEASUREMENT();
    }

    public MsgCRS_RSRPQI_INFO(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        this.mstCrs0RsrpqiInfo = new MsgCRS_MEASUREMENT(MsgSendHelper.getSubByteArray(bytes, pos, MsgCRS_MEASUREMENT.byteArrayLen));
        pos += MsgCRS_MEASUREMENT.byteArrayLen;
        this.mstCrs1RsrpqiInfo = new MsgCRS_MEASUREMENT(MsgSendHelper.getSubByteArray(bytes, pos, MsgCRS_MEASUREMENT.byteArrayLen));
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgCRS_RSRPQI_INFO.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message CRS_RSRPQI_INFO!");
        }
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mstCrs0RsrpqiInfo.hashCode();
        result = prime * result + mstCrs1RsrpqiInfo.hashCode();
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
        final MsgCRS_RSRPQI_INFO other = (MsgCRS_RSRPQI_INFO) o;
        return mstCrs1RsrpqiInfo.equals(other.mstCrs1RsrpqiInfo)
                && mstCrs0RsrpqiInfo.equals(other.mstCrs0RsrpqiInfo);
    }
}
