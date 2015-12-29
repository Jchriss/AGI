package com.example.jbtang.agi.messages.pc2ag;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.type.U32;

/**
 * define message protocol trace ue info
 * Created by jbtang on 10/4/2015.
 */
public class MsgPROTOCOL_TRACE_UE_INFO_STRU {
    private byte mu8UESelectMode;
    private byte mu8TraceUENum;
    private byte mu8UEIDListCount;
    private byte mu8KeyGetMode;
    private U32 mu32UeSilenceCheckTimer;
    private MsgUE_INFO_STRU[] mastUEInfoList; //size = 1
    private MsgUSIM_INFO_STRU[] mastUsimInfoList;// size = 1

    public static final int byteArrayLen = 4 + U32.byteArrayLen + MsgUE_INFO_STRU.byteArrayLen + MsgUSIM_INFO_STRU.byteArrayLen;

    public byte getMu8UESelectMode() {
        return mu8UESelectMode;
    }

    public void setMu8UESelectMode(byte mu8UESelectMode) {
        this.mu8UESelectMode = mu8UESelectMode;
    }

    public byte getMu8TraceUENum() {
        return mu8TraceUENum;
    }

    public void setMu8TraceUENum(byte mu8TraceUENum) {
        this.mu8TraceUENum = mu8TraceUENum;
    }

    public byte getMu8UEIDListCount() {
        return mu8UEIDListCount;
    }

    public void setMu8UEIDListCount(byte mu8UEIDListCount) {
        this.mu8UEIDListCount = mu8UEIDListCount;
    }

    public byte getMu8KeyGetMode() {
        return mu8KeyGetMode;
    }

    public void setMu8KeyGetMode(byte mu8KeyGetMode) {
        this.mu8KeyGetMode = mu8KeyGetMode;
    }

    public long getMu32UeSilenceCheckTimer() {
        return mu32UeSilenceCheckTimer.getBase();
    }

    public void setMu32UeSilenceCheckTimer(long mu32UeSilenceCheckTimer) {
        this.mu32UeSilenceCheckTimer = new U32(mu32UeSilenceCheckTimer);
    }

    public MsgUE_INFO_STRU[] getMastUEInfoList() {
        return mastUEInfoList;
    }

    public void setMastUEInfoList(MsgUE_INFO_STRU[] mastUEInfoList) {
        this.mastUEInfoList = mastUEInfoList;
    }

    public MsgUSIM_INFO_STRU[] getMastUsimInfoList() {
        return mastUsimInfoList;
    }

    public void setMastUsimInfoList(MsgUSIM_INFO_STRU[] mastUsimInfoList) {
        this.mastUsimInfoList = mastUsimInfoList;
    }

    public MsgPROTOCOL_TRACE_UE_INFO_STRU() {
        mu8UESelectMode = 0;
        mu8TraceUENum = 0;
        mu8UEIDListCount = 0;
        mu8KeyGetMode = 0;
        mu32UeSilenceCheckTimer = new U32();
        mastUEInfoList = new MsgUE_INFO_STRU[1];
        mastUsimInfoList = new MsgUSIM_INFO_STRU[1];
    }

    public MsgPROTOCOL_TRACE_UE_INFO_STRU(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        mu8UESelectMode = bytes[pos++];
        mu8TraceUENum = bytes[pos++];
        mu8UEIDListCount = bytes[pos++];
        mu8KeyGetMode = bytes[pos++];
        mu32UeSilenceCheckTimer = new U32(MsgSendHelper.getSubByteArray(bytes, pos, U32.byteArrayLen));
        pos += U32.byteArrayLen;
        mastUEInfoList = new MsgUE_INFO_STRU[]{new MsgUE_INFO_STRU(MsgSendHelper.getSubByteArray(bytes, pos, MsgUE_INFO_STRU.byteArrayLen))};
        pos += MsgUE_INFO_STRU.byteArrayLen;
        mastUsimInfoList = new MsgUSIM_INFO_STRU[]{new MsgUSIM_INFO_STRU(MsgSendHelper.getSubByteArray(bytes, pos, MsgUSIM_INFO_STRU.byteArrayLen))};
    }

    public byte[] getBytes() {
        return new MsgSendHelper.AppendByteArray()
                .append(mu8UESelectMode)
                .append(mu8TraceUENum)
                .append(mu8UEIDListCount)
                .append(mu8KeyGetMode)
                .append(mu32UeSilenceCheckTimer.getBytes())
                .append(mastUEInfoList[0].getBytes())
                .append(mastUsimInfoList[0].getBytes())
                .toByteArray();
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgPROTOCOL_TRACE_UE_INFO_STRU.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message PROTOCOL_TRACE_UE_INFO_STRU!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + mu8UESelectMode;
        result = prime * result + mu8TraceUENum;
        result = prime * result + mu8UEIDListCount;
        result = prime * result + mu8KeyGetMode;
        result = prime * result + (int) mu32UeSilenceCheckTimer.getBase();
        result = prime * result + mastUEInfoList[0].hashCode();
        result = prime * result + mastUsimInfoList[0].hashCode();
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
        final MsgPROTOCOL_TRACE_UE_INFO_STRU other = (MsgPROTOCOL_TRACE_UE_INFO_STRU) o;
        return mu8UESelectMode == other.mu8UESelectMode
                && mu8TraceUENum == other.mu8TraceUENum
                && mu8UEIDListCount == other.mu8UEIDListCount
                && mu8KeyGetMode == other.mu8KeyGetMode
                && mu32UeSilenceCheckTimer.equals(other.mu32UeSilenceCheckTimer)
                && mastUEInfoList[0].equals(other.mastUEInfoList[0])
                && mastUsimInfoList[0].equals(other.mastUsimInfoList[0]);
    }
}
