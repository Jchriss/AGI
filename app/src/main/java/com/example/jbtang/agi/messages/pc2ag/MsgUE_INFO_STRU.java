package com.example.jbtang.agi.messages.pc2ag;

import com.example.jbtang.agi.core.MsgSendHelper;

/**
 * define message ue info
 * Created by jbtang on 10/4/2015.
 */
public class MsgUE_INFO_STRU {
    private byte mu8CellInfoFlag;
    private byte mu8UEIDTYPE;
    private byte mu8UEIDLength;
    private byte mu8UeCategory;
    private MsgCELL_INFO_STRU mstCellInfo;
    private MsgUEID_DATA_STRU muUeIdData;

    public static final int byteArrayLen = 4 + MsgCELL_INFO_STRU.byteArrayLen + MsgUEID_DATA_STRU.byteArrayLen;

    public byte getMu8CellInfoFlag() {
        return mu8CellInfoFlag;
    }

    public void setMu8CellInfoFlag(byte mu8CellInfoFlag) {
        this.mu8CellInfoFlag = mu8CellInfoFlag;
    }

    public byte getMu8UEIDTYPE() {
        return mu8UEIDTYPE;
    }

    public void setMu8UEIDTYPE(byte mu8UEIDTYPE) {
        this.mu8UEIDTYPE = mu8UEIDTYPE;
    }

    public byte getMu8UEIDLength() {
        return mu8UEIDLength;
    }

    public void setMu8UEIDLength(byte mu8UEIDLength) {
        this.mu8UEIDLength = mu8UEIDLength;
    }

    public byte getMu8UeCategory() {
        return mu8UeCategory;
    }

    public void setMu8UeCategory(byte mu8UeCategory) {
        this.mu8UeCategory = mu8UeCategory;
    }

    public MsgCELL_INFO_STRU getMstCellInfo() {
        return mstCellInfo;
    }

    public void setMstCellInfo(MsgCELL_INFO_STRU mstCellInfo) {
        this.mstCellInfo = mstCellInfo;
    }

    public MsgUEID_DATA_STRU getMuUeIdData() {
        return muUeIdData;
    }

    public void setMuUeIdData(MsgUEID_DATA_STRU muUeIdData) {
        this.muUeIdData = muUeIdData;
    }

    public MsgUE_INFO_STRU() {
        this.mu8CellInfoFlag = 0;
        this.mu8UEIDTYPE = 0;
        this.mu8UEIDLength = 0;
        this.mu8UeCategory = 0;
        this.mstCellInfo = new MsgCELL_INFO_STRU();
        this.muUeIdData = new MsgUEID_DATA_STRU();
    }

    public MsgUE_INFO_STRU(byte[] bytes) {
        validate(bytes);
        this.mu8CellInfoFlag = bytes[0];
        this.mu8UEIDTYPE = bytes[1];
        this.mu8UEIDLength = bytes[2];
        this.mu8UeCategory = bytes[3];
        int pos = 4;
        this.mstCellInfo = new MsgCELL_INFO_STRU(MsgSendHelper.getSubByteArray(bytes, pos, MsgUE_INFO_STRU.byteArrayLen));
        pos += MsgCELL_INFO_STRU.byteArrayLen;
        this.muUeIdData = new MsgUEID_DATA_STRU(mu8UEIDTYPE, MsgSendHelper.getSubByteArray(bytes, pos, MsgUEID_DATA_STRU.byteArrayLen));
    }

    public byte[] getBytes() {
        return new MsgSendHelper.AppendByteArray()
                .append(mu8CellInfoFlag)
                .append(mu8UEIDTYPE)
                .append(mu8UEIDLength)
                .append(mu8UeCategory)
                .append(mstCellInfo.getBytes())
                .append(muUeIdData.getBytes())
                .toByteArray();
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgUE_INFO_STRU.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message UE_INFO_STRU!");
        }
    }
}
