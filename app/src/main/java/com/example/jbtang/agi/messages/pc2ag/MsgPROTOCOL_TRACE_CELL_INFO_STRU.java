package com.example.jbtang.agi.messages.pc2ag;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.type.U16;
import com.example.jbtang.agi.core.type.U32;
import com.example.jbtang.agi.core.type.U8;

/**
 * define protocol trace info message
 * Created by jbtang on 10/3/2015.
 */
public class MsgPROTOCOL_TRACE_CELL_INFO_STRU {
    private U32 mu32PlmnNum;
    public static final int plmnIdListSize = 4;
    private MsgPLMNID_STRU[] PlmnIdList;
    private U8 mu8RATType;
    private U8 mu8CellSelectMode;
    private U8 mu8CellReSelectMode;
    private U8 mu8CellReselectionFlag;
    private U16 mu16ProtolLayerSelect;
    private U16 mu16L1MeasSelect;
    private U8 mu8PhyTrackInfoSelect;
    private U8 mu8PhyMeasRptPeriod;
    private U16 mu16AverageFrameNum;
    private U16 mu16statisticalInfoReportPeriod;
    private U16 mu16CtrlInfoReportPeriod;
    private U16 mu16CellNumber;
    private U16 mu16FreqBandNum;
    private U16 mu16RadioParaGetFlag;
    private byte ms8AgcPara;
    private U8 mu8Padding;

    public static final int byteArrayLen = U32.byteArrayLen + plmnIdListSize * MsgPLMNID_STRU.byteArrayLen + 8 * U16.byteArrayLen + 7 * U8.byteArrayLen + 1;

    public long getMu32PlmnNum() {
        return mu32PlmnNum.getBase();
    }

    public void setMu32PlmnNum(long mu32PlmnNum) {
        this.mu32PlmnNum = new U32(mu32PlmnNum);
    }

    public MsgPLMNID_STRU[] getPlmnIdList() {
        return PlmnIdList;
    }

    public void setPlmnIdList(MsgPLMNID_STRU[] plmnIdList) {
        PlmnIdList = plmnIdList;
    }

    public int getMu8RATType() {
        return mu8RATType.getBase();
    }

    public void setMu8RATType(int mu8RATType) {
        this.mu8RATType = new U8(mu8RATType);
    }

    public int getMu8CellSelectMode() {
        return mu8CellSelectMode.getBase();
    }

    public void setMu8CellSelectMode(int mu8CellSelectMode) {
        this.mu8CellSelectMode = new U8(mu8CellSelectMode);
    }

    public int getMu8CellReSelectMode() {
        return mu8CellReSelectMode.getBase();
    }

    public void setMu8CellReSelectMode(int mu8CellReSelectMode) {
        this.mu8CellReSelectMode = new U8(mu8CellReSelectMode);
    }

    public int getMu8CellReselectionFlag() {
        return mu8CellReselectionFlag.getBase();
    }

    public void setMu8CellReselectionFlag(int mu8CellReselectionFlag) {
        this.mu8CellReselectionFlag = new U8(mu8CellReselectionFlag);
    }

    public int getMu16ProtolLayerSelect() {
        return mu16ProtolLayerSelect.getBase();
    }

    public void setMu16ProtolLayerSelect(int mu16ProtolLayerSelect) {
        this.mu16ProtolLayerSelect = new U16(mu16ProtolLayerSelect);
    }

    public int getMu16L1MeasSelect() {
        return mu16L1MeasSelect.getBase();
    }

    public void setMu16L1MeasSelect(int mu16L1MeasSelect) {
        this.mu16L1MeasSelect = new U16(mu16L1MeasSelect);
    }

    public int getMu8PhyTrackInfoSelect() {
        return mu8PhyTrackInfoSelect.getBase();
    }

    public void setMu8PhyTrackInfoSelect(int mu8PhyTrackInfoSelect) {
        this.mu8PhyTrackInfoSelect = new U8(mu8PhyTrackInfoSelect);
    }

    public int getMu8PhyMeasRptPeriod() {
        return mu8PhyMeasRptPeriod.getBase();
    }

    public void setMu8PhyMeasRptPeriod(int mu8PhyMeasRptPeriod) {
        this.mu8PhyMeasRptPeriod = new U8(mu8PhyMeasRptPeriod);
    }

    public int getMu16AverageFrameNum() {
        return mu16AverageFrameNum.getBase();
    }

    public void setMu16AverageFrameNum(int mu16AverageFrameNum) {
        this.mu16AverageFrameNum = new U16(mu16AverageFrameNum);
    }

    public int getMu16statisticalInfoReportPeriod() {
        return mu16statisticalInfoReportPeriod.getBase();
    }

    public void setMu16statisticalInfoReportPeriod(int mu16statisticalInfoReportPeriod) {
        this.mu16statisticalInfoReportPeriod = new U16(mu16statisticalInfoReportPeriod);
    }

    public int getMu16CtrlInfoReportPeriod() {
        return mu16CtrlInfoReportPeriod.getBase();
    }

    public void setMu16CtrlInfoReportPeriod(int mu16CtrlInfoReportPeriod) {
        this.mu16CtrlInfoReportPeriod = new U16(mu16CtrlInfoReportPeriod);
    }

    public int getMu16CellNumber() {
        return mu16CellNumber.getBase();
    }

    public void setMu16CellNumber(int mu16CellNumber) {
        this.mu16CellNumber = new U16(mu16CellNumber);
    }

    public int getMu16FreqBandNum() {
        return mu16FreqBandNum.getBase();
    }

    public void setMu16FreqBandNum(int mu16FreqBandNum) {
        this.mu16FreqBandNum = new U16(mu16FreqBandNum);
    }

    public int getMu16RadioParaGetFlag() {
        return mu16RadioParaGetFlag.getBase();
    }

    public void setMu16RadioParaGetFlag(int mu16RadioParaGetFlag) {
        this.mu16RadioParaGetFlag = new U16(mu16RadioParaGetFlag);
    }

    public byte getMs8AgcPara() {
        return ms8AgcPara;
    }

    public void setMs8AgcPara(byte ms8AgcPara) {
        this.ms8AgcPara = ms8AgcPara;
    }

    public int getMu8Padding() {
        return mu8Padding.getBase();
    }

    public void setMu8Padding(int mu8Padding) {
        this.mu8Padding = new U8(mu8Padding);
    }

    public MsgPROTOCOL_TRACE_CELL_INFO_STRU() {
        this.mu32PlmnNum = new U32();
        this.PlmnIdList = new MsgPLMNID_STRU[plmnIdListSize];
        this.mu8RATType = new U8();
        this.mu8CellSelectMode = new U8();
        this.mu8CellReSelectMode = new U8();
        this.mu8CellReselectionFlag = new U8();
        this.mu16ProtolLayerSelect = new U16();
        this.mu16L1MeasSelect = new U16();
        this.mu8PhyTrackInfoSelect = new U8();
        this.mu8PhyMeasRptPeriod = new U8();
        this.mu16AverageFrameNum = new U16();
        this.mu16statisticalInfoReportPeriod = new U16();
        this.mu16CtrlInfoReportPeriod = new U16();
        this.mu16CellNumber = new U16();
        this.mu16FreqBandNum = new U16();
        this.mu16RadioParaGetFlag = new U16();
        this.ms8AgcPara = 0;
        this.mu8Padding = new U8();
    }

    public MsgPROTOCOL_TRACE_CELL_INFO_STRU(byte[] bytes) {
        validate(bytes);
        int pos = 4;
        this.PlmnIdList = new MsgPLMNID_STRU[plmnIdListSize];
        for (int i = 0; i < plmnIdListSize; i++) {
            MsgPLMNID_STRU plmnid_stru = new MsgPLMNID_STRU(
                    MsgSendHelper.getSubByteArray(bytes, pos, MsgPLMNID_STRU.byteArrayLen));
            this.PlmnIdList[i] = plmnid_stru;
            pos += MsgPLMNID_STRU.byteArrayLen;
        }
        this.mu8RATType = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        this.mu8CellSelectMode = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        this.mu8CellReSelectMode = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        this.mu8CellReselectionFlag = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        this.mu16ProtolLayerSelect = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        this.mu16L1MeasSelect = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        this.mu8PhyTrackInfoSelect = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        this.mu8PhyMeasRptPeriod = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
        pos += U8.byteArrayLen;
        this.mu16AverageFrameNum = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        this.mu16statisticalInfoReportPeriod = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        this.mu16CtrlInfoReportPeriod = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        this.mu16CellNumber = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        this.mu16FreqBandNum = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        this.mu16RadioParaGetFlag = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        this.ms8AgcPara = 0;
        pos += 1;
        this.mu8Padding = new U8(MsgSendHelper.getSubByteArray(bytes, pos, U8.byteArrayLen));
    }

    public byte[] getBytes() {
        MsgSendHelper.AppendByteArray ret = new MsgSendHelper.AppendByteArray();
        ret.append(mu32PlmnNum.getBytes());
        for (MsgPLMNID_STRU plmnid_stru : PlmnIdList) {
            ret.append(plmnid_stru.getBytes());
        }
        ret.append(mu8RATType.getBytes())
                .append(mu8CellSelectMode.getBytes())
                .append(mu8CellReSelectMode.getBytes())
                .append(mu8CellReselectionFlag.getBytes())
                .append(mu16ProtolLayerSelect.getBytes())
                .append(mu16L1MeasSelect.getBytes())
                .append(mu8PhyTrackInfoSelect.getBytes())
                .append(mu8PhyMeasRptPeriod.getBytes())
                .append(mu16AverageFrameNum.getBytes())
                .append(mu16statisticalInfoReportPeriod.getBytes())
                .append(mu16CtrlInfoReportPeriod.getBytes())
                .append(mu16CellNumber.getBytes())
                .append(mu16FreqBandNum.getBytes())
                .append(mu16RadioParaGetFlag.getBytes())
                .append(new byte[]{ms8AgcPara})
                .append(mu8Padding.getBytes());
        return ret.toByteArray();
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgPROTOCOL_TRACE_CELL_INFO_STRU.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message PROTOCOL_TRACE_CELL_INFO!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) mu32PlmnNum.getBase();
        for (MsgPLMNID_STRU plmnid_stru : PlmnIdList) {
            result = prime * result + plmnid_stru.hashCode();
        }
        result = prime * result + mu8RATType.getBase();
        result = prime * result + mu8CellSelectMode.getBase();
        result = prime * result + mu8CellReSelectMode.getBase();
        result = prime * result + mu8CellReselectionFlag.getBase();
        result = prime * result + mu16ProtolLayerSelect.getBase();
        result = prime * result + mu16L1MeasSelect.getBase();
        result = prime * result + mu8PhyTrackInfoSelect.getBase();
        result = prime * result + mu8PhyMeasRptPeriod.getBase();
        result = prime * result + mu16AverageFrameNum.getBase();
        result = prime * result + mu16statisticalInfoReportPeriod.getBase();
        result = prime * result + mu16CtrlInfoReportPeriod.getBase();
        result = prime * result + mu16CellNumber.getBase();
        result = prime * result + mu16FreqBandNum.getBase();
        result = prime * result + mu16RadioParaGetFlag.getBase();
        result = prime * result + ms8AgcPara;
        result = prime * result + mu8Padding.getBase();
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
        final MsgPROTOCOL_TRACE_CELL_INFO_STRU other = (MsgPROTOCOL_TRACE_CELL_INFO_STRU) o;
        if (!mu32PlmnNum.equals(other.mu32PlmnNum)) {
            return false;
        }
        for (int i = 0; i < plmnIdListSize; i++) {
            if (!PlmnIdList[i].equals(other.PlmnIdList[i])) {
                return false;
            }
        }
        return mu8RATType.equals(other.mu8RATType)
                && mu8CellSelectMode.equals(other.mu8CellSelectMode)
                && mu8CellReSelectMode.equals(other.mu8CellReSelectMode)
                && mu8CellReselectionFlag.equals(other.mu8CellReselectionFlag)
                && mu16ProtolLayerSelect.equals(other.mu16ProtolLayerSelect)
                && mu16L1MeasSelect.equals(other.mu16L1MeasSelect)
                && mu8PhyTrackInfoSelect.equals(other.mu8PhyTrackInfoSelect)
                && mu8PhyMeasRptPeriod.equals(other.mu8PhyMeasRptPeriod)
                && mu16AverageFrameNum.equals(other.mu16AverageFrameNum)
                && mu16statisticalInfoReportPeriod.equals(other.mu16statisticalInfoReportPeriod)
                && mu16CtrlInfoReportPeriod.equals(other.mu16CtrlInfoReportPeriod)
                && mu16CellNumber.equals(other.mu16CellNumber)
                && mu16FreqBandNum.equals(other.mu16FreqBandNum)
                && mu16RadioParaGetFlag.equals(other.mu16RadioParaGetFlag)
                && ms8AgcPara == other.ms8AgcPara
                && mu8Padding.equals(other.mu8Padding);
    }
}
