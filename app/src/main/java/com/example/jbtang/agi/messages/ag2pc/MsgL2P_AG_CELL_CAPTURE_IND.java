package com.example.jbtang.agi.messages.ag2pc;

import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.type.U16;
import com.example.jbtang.agi.core.type.U32;
import com.example.jbtang.agi.core.type.U8;

/**
 * define message cell capture ind
 * Created by jbtang on 10/5/2015.
 */
public class MsgL2P_AG_CELL_CAPTURE_IND {
    private U8[] TimeStampH;//size 4
    private U32 TimeStampL;
    private U16 mu16CellStatus;
    private U16 mu16PCI;
    private U16 mu16EARFCN;
    private U16 mu16TAC;
    private U32 mu32CellID;
    private short mu16Rsrp;
    private short mu16Rsrq;
    private U8 mu8Dlbandwidth;
    private U8 mu8PhichDuration;
    private U8 mu8PhichResource;
    private U8 mu8SpecialSubframePatterns;
    private U8 mu8UplinkDownlinkConfiguration;
    private U8[] mau8Pading;//size 3

    public static final int byteArrayLen = (4 + 5 + 3) * U8.byteArrayLen + 4 * U16.byteArrayLen + 2 * 2 + 2 * U32.byteArrayLen;


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

    public int getMu16CellStatus() {
        return mu16CellStatus.getBase();
    }

    public void setMu16CellStatus(int mu16CellStatus) {
        this.mu16CellStatus = new U16(mu16CellStatus);
    }

    public int getMu16PCI() {
        return mu16PCI.getBase();
    }

    public void setMu16PCI(int mu16PCI) {
        this.mu16PCI = new U16(mu16PCI);
    }

    public int getMu16EARFCN() {
        return mu16EARFCN.getBase();
    }

    public void setMu16EARFCN(int mu16EARFCN) {
        this.mu16EARFCN = new U16(mu16EARFCN);
    }

    public int getMu16TAC() {
        return mu16TAC.getBase();
    }

    public void setMu16TAC(int mu16TAC) {
        this.mu16TAC = new U16(mu16TAC);
    }

    public long getMu32CellID() {
        return mu32CellID.getBase();
    }

    public void setMu32CellID(long mu32CellID) {
        this.mu32CellID = new U32(mu32CellID);
    }

    public short getMu16Rsrp() {
        return mu16Rsrp;
    }

    public void setMu16Rsrp(short mu16Rsrp) {
        this.mu16Rsrp = mu16Rsrp;
    }

    public short getMu16Rsrq() {
        return mu16Rsrq;
    }

    public void setMu16Rsrq(short mu16Rsrq) {
        this.mu16Rsrq = mu16Rsrq;
    }

    public int getMu8Dlbandwidth() {
        return mu8Dlbandwidth.getBase();
    }

    public void setMu8Dlbandwidth(int mu8Dlbandwidth) {
        this.mu8Dlbandwidth = new U8(mu8Dlbandwidth);
    }

    public int getMu8PhichDuration() {
        return mu8PhichDuration.getBase();
    }

    public void setMu8PhichDuration(int mu8PhichDuration) {
        this.mu8PhichDuration = new U8(mu8PhichDuration);
    }

    public int getMu8PhichResource() {
        return mu8PhichResource.getBase();
    }

    public void setMu8PhichResource(int mu8PhichResource) {
        this.mu8PhichResource = new U8(mu8PhichResource);
    }

    public int getMu8SpecialSubframePatterns() {
        return mu8SpecialSubframePatterns.getBase();
    }

    public void setMu8SpecialSubframePatterns(int mu8SpecialSubframePatterns) {
        this.mu8SpecialSubframePatterns = new U8(mu8SpecialSubframePatterns);
    }

    public int getMu8UplinkDownlinkConfiguration() {
        return mu8UplinkDownlinkConfiguration.getBase();
    }

    public void setMu8UplinkDownlinkConfiguration(int mu8UplinkDownlinkConfiguration) {
        this.mu8UplinkDownlinkConfiguration = new U8(mu8UplinkDownlinkConfiguration);
    }

    public U8[] getMau8Pading() {
        return mau8Pading;
    }

    public void setMau8Pading(U8[] mau8Pading) {
        this.mau8Pading = mau8Pading;
    }

    public MsgL2P_AG_CELL_CAPTURE_IND() {
        TimeStampH = new U8[4];
        TimeStampL = new U32();
        mu16CellStatus = new U16();
        mu16PCI = new U16();
        mu16EARFCN = new U16();
        mu16TAC = new U16();
        mu32CellID = new U32();
        mu16Rsrp = 0;
        mu16Rsrq = 0;
        mu8Dlbandwidth = new U8();
        mu8PhichDuration = new U8();
        mu8PhichResource = new U8();
        mu8SpecialSubframePatterns = new U8();
        mu8UplinkDownlinkConfiguration = new U8();
        mau8Pading = new U8[3];
    }

    public MsgL2P_AG_CELL_CAPTURE_IND(byte[] bytes) {
        validate(bytes);
        int pos = 0;
        TimeStampH = new U8[]{
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen))
        };
        TimeStampL = new U32(MsgSendHelper.getSubByteArray(bytes, pos, U32.byteArrayLen));
        pos += U32.byteArrayLen;
        mu16CellStatus = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mu16PCI = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mu16EARFCN = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mu16TAC = new U16(MsgSendHelper.getSubByteArray(bytes, pos, U16.byteArrayLen));
        pos += U16.byteArrayLen;
        mu32CellID = new U32(MsgSendHelper.getSubByteArray(bytes, pos, U32.byteArrayLen));
        pos += U32.byteArrayLen;
        mu16Rsrp = MsgSendHelper.byteArrayToShort(MsgSendHelper.getSubByteArray(bytes, pos, 2));
        pos += 2;
        mu16Rsrq = MsgSendHelper.byteArrayToShort(MsgSendHelper.getSubByteArray(bytes, pos, 2));
        pos += 2;
        mu8Dlbandwidth = new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen));
        mu8PhichDuration = new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen));
        mu8PhichResource = new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen));
        mu8SpecialSubframePatterns = new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen));
        mu8UplinkDownlinkConfiguration = new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen));
        mau8Pading = new U8[]{
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
                new U8(MsgSendHelper.getSubByteArray(bytes, pos++, U8.byteArrayLen)),
        };
    }

    public byte[] getBytes() {
        MsgSendHelper.AppendByteArray ret = new MsgSendHelper.AppendByteArray();
        for (U8 t : TimeStampH) {
            ret.append(t.getBytes());
        }
        ret.append(TimeStampL.getBytes())
                .append(mu16CellStatus.getBytes())
                .append(mu16PCI.getBytes())
                .append(mu16EARFCN.getBytes())
                .append(mu16TAC.getBytes())
                .append(mu32CellID.getBytes())
                .append(MsgSendHelper.shortToByteArray(mu16Rsrp))
                .append(MsgSendHelper.shortToByteArray(mu16Rsrq))
                .append(mu8Dlbandwidth.getBytes())
                .append(mu8PhichDuration.getBytes())
                .append(mu8PhichResource.getBytes())
                .append(mu8SpecialSubframePatterns.getBytes())
                .append(mu8UplinkDownlinkConfiguration.getBytes());
        for (U8 t : mau8Pading) {
            ret.append(t.getBytes());
        }
        return ret.toByteArray();
    }

    private void validate(byte[] bytes) {
        if (bytes.length < MsgL2P_AG_CELL_CAPTURE_IND.byteArrayLen) {
            throw new IllegalArgumentException("byte array is not message L2P_AG_CELL_CAPTURE_IND!");
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        for (U8 t : TimeStampH) {
            result = prime * result + t.getBase();
        }
        result = prime * result + (int) TimeStampL.getBase();
        result = prime * result + mu16CellStatus.getBase();
        result = prime * result + mu16PCI.getBase();
        result = prime * result + mu16EARFCN.getBase();
        result = prime * result + mu16TAC.getBase();
        result = prime * result + (int) mu32CellID.getBase();
        result = prime * result + mu16Rsrp;
        result = prime * result + mu16Rsrq;
        result = prime * result + mu8Dlbandwidth.getBase();
        result = prime * result + mu8PhichDuration.getBase();
        result = prime * result + mu8PhichResource.getBase();
        result = prime * result + mu8SpecialSubframePatterns.getBase();
        result = prime * result + mu8UplinkDownlinkConfiguration.getBase();
        for (U8 t : mau8Pading) {
            result = prime * result + t.getBase();
        }
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
        final MsgL2P_AG_CELL_CAPTURE_IND other = (MsgL2P_AG_CELL_CAPTURE_IND) o;
        for (int i = 0; i < TimeStampH.length; i++) {
            if (!TimeStampH[i].equals(other.TimeStampH[i])) {
                return false;
            }
        }
        for (int i = 0; i < mau8Pading.length; i++) {
            if (!mau8Pading[i].equals(other.mau8Pading[i])) {
                return false;
            }
        }
        return TimeStampL.equals(other.TimeStampL)
                && mu16CellStatus.equals(other.mu16CellStatus)
                && mu16PCI.equals(other.mu16PCI)
                && mu16EARFCN.equals(other.mu16EARFCN)
                && mu16TAC.equals(other.mu16TAC)
                && mu32CellID.equals(other.mu32CellID)
                && mu16Rsrp == other.mu16Rsrp
                && mu16Rsrq == other.mu16Rsrq
                && mu8Dlbandwidth.equals(other.mu8Dlbandwidth)
                && mu8PhichDuration.equals(other.mu8PhichDuration)
                && mu8PhichResource.equals(other.mu8PhichResource)
                && mu8SpecialSubframePatterns.equals(other.mu8SpecialSubframePatterns)
                && mu8UplinkDownlinkConfiguration.equals(other.mu8UplinkDownlinkConfiguration);
    }
}
