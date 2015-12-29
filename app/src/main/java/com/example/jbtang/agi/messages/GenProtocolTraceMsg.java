package com.example.jbtang.agi.messages;

import com.example.jbtang.agi.core.Global;
import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.type.U16;
import com.example.jbtang.agi.messages.base.MsgHeader;
import com.example.jbtang.agi.messages.pc2ag.MsgCELL_INFO_STRU;
import com.example.jbtang.agi.messages.pc2ag.MsgPC_AG_PROTOCOL_TRACE_REQ;
import com.example.jbtang.agi.messages.pc2ag.MsgPLMNID_STRU;
import com.example.jbtang.agi.messages.pc2ag.MsgPROTOCOL_TRACE_CELL_INFO_STRU;
import com.example.jbtang.agi.messages.pc2ag.MsgPROTOCOL_TRACE_UE_INFO_STRU;
import com.example.jbtang.agi.messages.pc2ag.MsgUEID_DATA_STRU;
import com.example.jbtang.agi.messages.pc2ag.MsgUE_INFO_STRU;
import com.example.jbtang.agi.messages.pc2ag.MsgUSIM_INFO_STRU;

/**
 * to generate protocol trace message
 * Created by jbtang on 10/4/2015.
 */
public class GenProtocolTraceMsg {
    private static final U16 mcc = new U16(460);
    private static final U16 mnc = new U16(0);

    public static byte[] gen(byte mode, int earfcn, int pci, byte[] stmsi) {
        return new MsgSendHelper.AppendByteArray()
                .append(genPC_AG_PROTOCOL_TRACE_REQ(mode, stmsi).getBytes())
                .append(genMsgCELL_INFO_STRU(earfcn, pci).getBytes())
                .toByteArray();
    }

    private static MsgPC_AG_PROTOCOL_TRACE_REQ genPC_AG_PROTOCOL_TRACE_REQ(byte mode, byte[] stmsi) {
        MsgPC_AG_PROTOCOL_TRACE_REQ ret = new MsgPC_AG_PROTOCOL_TRACE_REQ();
        ret.setHeader(genMsgHeader());
        ret.setMstUeInfo(genMsgPROTOCOL_TRACE_UE_INFO_STRU(mode, stmsi));
        ret.setMstCellInfo(genPROTOCOL_TRACE_CELL_INFO_STRU());
        return ret;
    }

    private static MsgCELL_INFO_STRU genMsgCELL_INFO_STRU(int earfcn, int pci) {
        MsgCELL_INFO_STRU ret = new MsgCELL_INFO_STRU();
        ret.setU16EARFCN(earfcn);
        ret.setU16PCI(pci);
        return ret;
    }

    private static MsgHeader genMsgHeader() {
        MsgHeader header = new MsgHeader();
        header.setSrcID((byte) 4);
        header.setDestID((byte) 2);
        header.setMsgType(0x400a);
        header.setMsgLen((MsgPC_AG_PROTOCOL_TRACE_REQ.byteArrayLen - MsgHeader.byteArrayLen + MsgCELL_INFO_STRU.byteArrayLen) / 4);
        return header;
    }

    private static MsgPROTOCOL_TRACE_UE_INFO_STRU genMsgPROTOCOL_TRACE_UE_INFO_STRU(byte mode, byte[] stmsi) {
        MsgPROTOCOL_TRACE_UE_INFO_STRU ret = new MsgPROTOCOL_TRACE_UE_INFO_STRU();
        ret.setMastUEInfoList(new MsgUE_INFO_STRU[]{genMsgUE_INFO_STRU()});
        ret.setMastUsimInfoList(new MsgUSIM_INFO_STRU[]{genMsgUSIM_INFO_STRU()});
        ret.setMu8UESelectMode(mode);
        ret.setMu8TraceUENum((byte) 1);
        ret.setMu8UEIDListCount((byte) 1);
        ret.setMu32UeSilenceCheckTimer(Global.Configuration.silenceCheckTimer);

        if (mode == 0) {
            ret.getMastUEInfoList()[0].setMu8UEIDTYPE(MsgUEID_DATA_STRU.IDType.GUTI);
            ret.getMastUEInfoList()[0].setMu8UEIDLength((byte) 10);
            ret.getMastUEInfoList()[0].setMuUeIdData(genMsgUEID_DATA_STRU(stmsi));
        }

        ret.getMastUsimInfoList()[0].setMu8KLengthIndex((byte) 2);
        return ret;
    }

    //OrientationFinding
    private static MsgUEID_DATA_STRU genMsgUEID_DATA_STRU(byte[] stmsi) {
        int ii = 0;
        int j = 1;
        for (int i = 0; i < stmsi.length; i++) {
            if (stmsi[i] >= '0' && stmsi[i] <= '9') {
                stmsi[i] = (byte) (stmsi[i] - 48);
            } else if (stmsi[i] >= 'a' && stmsi[i] <= 'f') {
                stmsi[i] = (byte) (stmsi[i] - 'a' + 10);
            } else if (stmsi[i] >= 'A' && stmsi[i] <= 'F') {
                stmsi[i] = (byte) (stmsi[i] - 'A' + 10);
            }
        }
        byte[] TypeGUTI = new byte[MsgUEID_DATA_STRU.byteArrayLen];
        for (int num = 5; num < 10; num++) {
            byte tempValue = (byte) (stmsi[ii] << 4);
            tempValue &= 0xf0;
            tempValue |= stmsi[j];

            TypeGUTI[num] = tempValue;
            ii += 2;
            j += 2;
        }
        TypeGUTI[0] = (byte) (mnc.getBase() / 10);
        TypeGUTI[1] = (byte) (mnc.getBase() % 10 | 0xf0);
        TypeGUTI[2] = (byte) (mcc.getBase() % 10);
        TypeGUTI[3] = (byte) (mcc.getBase() % 100 / 10);
        TypeGUTI[4] = (byte) (mcc.getBase() / 100);
        return new MsgUEID_DATA_STRU(MsgUEID_DATA_STRU.IDType.GUTI, TypeGUTI);
    }

    private static MsgUE_INFO_STRU genMsgUE_INFO_STRU() {
        MsgUE_INFO_STRU ret = new MsgUE_INFO_STRU();
        ret.setMu8UEIDLength((byte) 8);
        ret.setMu8UeCategory((byte) 1);
        return ret;
    }

    private static MsgUSIM_INFO_STRU genMsgUSIM_INFO_STRU() {
        MsgUSIM_INFO_STRU ret = new MsgUSIM_INFO_STRU();
        ret.setMu8KLengthIndex((byte) 1);
        return ret;
    }

    private static MsgPROTOCOL_TRACE_CELL_INFO_STRU genPROTOCOL_TRACE_CELL_INFO_STRU() {
        MsgPROTOCOL_TRACE_CELL_INFO_STRU ret = new MsgPROTOCOL_TRACE_CELL_INFO_STRU();
        ret.setMu16CellNumber(1);
        ret.setMu32PlmnNum(1L);
        ret.setMu16ProtolLayerSelect(0x0200);
        ret.setMu16AverageFrameNum(1);
        ret.setMu16statisticalInfoReportPeriod(1);
        ret.setMu16CtrlInfoReportPeriod(1);
        ret.setPlmnIdList(genMsgPLMNID_STRUList());
        ret.setMu16CellNumber(1);
        ret.setMu16ProtolLayerSelect(515);
        ret.setMu16L1MeasSelect(8193);
        return ret;
    }

    private static MsgPLMNID_STRU[] genMsgPLMNID_STRUList() {
        MsgPLMNID_STRU[] ret = new MsgPLMNID_STRU[MsgPROTOCOL_TRACE_CELL_INFO_STRU.plmnIdListSize];
        for (int i = 0; i < MsgPROTOCOL_TRACE_CELL_INFO_STRU.plmnIdListSize; i++) {
            MsgPLMNID_STRU tmp = new MsgPLMNID_STRU();
            tmp.setMau8AucMcc(new byte[]{4, 6, 0});
            ret[i] = tmp;
        }
        return ret;
    }
}
