package com.example.jbtang.agi.messages.base;

/**
 * define all message types
 * Created by jbtang on 10/3/2015.
 */
public class MsgTypes {
    public static final int AG_PC_PROTOCOL_TRACE_REL_ACK_MSG_TYPE = 0x480d;
    public static final int L1_AG_PROTOCOL_TRACE_REL_ACK_MSG_TYPE = 0x6010;
    public static final int AG_PC_GET_AGT_INFO_REQ_ACK_MSG_TYPE = 0x4801;
    public static final int L2P_AG_CELL_CAPTURE_IND_MSG_TYPE = 0x700f;
    public static final int L2P_CELL_CAPTURE_IND_MSG_TYPE = L2P_AG_CELL_CAPTURE_IND_MSG_TYPE;
    public static final int L2P_AG_UE_CAPTURE_IND_MSG_TYPE = 0x7012;
    public static final int L2P_UE_CAPTURE_IND_MSG_TYPE = L2P_AG_UE_CAPTURE_IND_MSG_TYPE;
    public static final int L2P_AG_UE_RELEASE_IND_MSG_TYPE = 0x7013;
    public static final int L1_AG_PHY_COMMEAS_IND_MSG_TYPE = 0x6013;
    public static final int L1_PHY_COMMEAS_IND_MSG_TYPE = L1_AG_PHY_COMMEAS_IND_MSG_TYPE;
    public static final int L1_AG_PROTOCOL_DATA_MSG_TYPE = 0x600f;
    public static final int L1_PROTOCOL_DATA_MSG_TYPE = L1_AG_PROTOCOL_DATA_MSG_TYPE;
    public static final int PC_AG_PROTOCOL_TRACE_REQ_MSG_TYPE = 0x400a;
    public static final int AG_PC_PROTOCOL_TRACE_REQ_ACK_MSG_TYPE = 0x480b;
}
