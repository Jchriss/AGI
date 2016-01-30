package com.example.jbtang.agi.service;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.jbtang.agi.core.Global;
import com.example.jbtang.agi.core.MsgSendHelper;
import com.example.jbtang.agi.core.Status;
import com.example.jbtang.agi.device.DeviceManager;
import com.example.jbtang.agi.device.MonitorDevice;
import com.example.jbtang.agi.messages.MessageDispatcher;
import com.example.jbtang.agi.messages.ag2pc.MsgCRS_RSRPQI_INFO;
import com.example.jbtang.agi.messages.ag2pc.MsgL1_PHY_COMMEAS_IND;
import com.example.jbtang.agi.messages.ag2pc.MsgL1_PROTOCOL_DATA;
import com.example.jbtang.agi.messages.ag2pc.MsgL1_UL_UE_MEAS;
import com.example.jbtang.agi.messages.ag2pc.MsgL2P_AG_CELL_CAPTURE_IND;
import com.example.jbtang.agi.messages.base.MsgTypes;
import com.example.jbtang.agi.trigger.SMSTrigger;
import com.example.jbtang.agi.trigger.Trigger;
import com.example.jbtang.agi.ui.OrientationFindingActivity;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by jbtang on 11/7/2015.
 */
public class OrientationFinding {
    private static final String TAG = "OrientationFinding";
    private static final Integer UPLINK = 0;
    private static final Integer MAX_RSRP = -45;
    private static final Integer MIN_RSRP = -115;
    private static final double SINR_THRESHOLD = -10.0D;
    private static OrientationFinding instance = new OrientationFinding();
    public static final int PUCCH = 7;
    public static final int PUSCH = 5;
    private static final int COUNT_INTERVAL = 2;
    private static final int RESULT_QUEUE_MAX_LEN = 25;

    private myHandler handler;
    private Trigger trigger;
    private Queue<UEInfo> ueInfoQueue;
    private RsrpResult result;
    private List<Float> cellRSRPList;
    private Future future;
    //private boolean needToCount;
    private Handler outHandler;
    private Task task;

    public String targetStmsi;

    public void setOutHandler(Handler handler) {
        this.outHandler = handler;
    }

    static class UEInfo {
        public int chType;
        public double rsrp;
        public int sinr;
    }

    public static class OrientationInfo {
        public double PUSCHRsrp;
        public double PUCCHRsrp;
        public Float CellRsrp;
        public String timeStamp;

        private int standardPusch = Integer.MAX_VALUE;
        private int standardPucch = Integer.MAX_VALUE;

        public int getStandardPusch() {
            if (standardPusch == Integer.MAX_VALUE) {
                standardPusch = format(PUSCHRsrp);
            }
            return standardPusch;
        }

        public int getStandardPucch() {
            if (standardPucch == Integer.MAX_VALUE) {
                standardPucch = format(PUCCHRsrp);
            }
            return standardPucch;
        }

        private static final int STANDARDED_MIN_RSRP = 10;
        private static final int STANDARDED_MAX_RSRP = 100;
        private static final double MIN_RSRP = -115D;
        private static final double MAX_RSRP = -55D;

        private static int format(double rsrp) {
            if (Double.isNaN(rsrp)) {
                return STANDARDED_MIN_RSRP;
            }
            if (rsrp > MAX_RSRP) {
                return STANDARDED_MAX_RSRP;
            }
            return STANDARDED_MIN_RSRP + (int) ((rsrp - MIN_RSRP) / (MAX_RSRP - MIN_RSRP) * (STANDARDED_MAX_RSRP - STANDARDED_MIN_RSRP));
        }
    }

    private OrientationFinding() {
        handler = new myHandler(this);
        result = new RsrpResult(RESULT_QUEUE_MAX_LEN);
        cellRSRPList = new ArrayList<>();
        trigger = SMSTrigger.getInstance();
        //needToCount = false;
        ueInfoQueue = new LinkedList<>();
    }

    public static OrientationFinding getInstance() {
        return instance;
    }

    static class myHandler extends Handler {
        private final WeakReference<OrientationFinding> mOuter;

        public myHandler(OrientationFinding orientationFinding) {
            mOuter = new WeakReference<>(orientationFinding);
        }

        @Override
        public void handleMessage(Message msg) {
            Global.GlobalMsg globalMsg = (Global.GlobalMsg) msg.obj;
            switch (msg.what) {
                case MsgTypes.L1_AG_PROTOCOL_DATA_MSG_TYPE:
                    mOuter.get().resolveProtocolDataMsg(globalMsg);
                    Log.e(TAG, "L1_AG_PROTOCOL_DATA_MSG_TYPE!!!!!!!!!!!!!!!!!!!!!!!");
                    break;
                case MsgTypes.L1_PHY_COMMEAS_IND_MSG_TYPE:
                    mOuter.get().resolvePhyCommeasIndMsg(globalMsg);
                    Log.e(TAG, "L1_PHY_COMMEAS_IND_MSG_TYPE captured!!!!!!!!!!!!!!!!!!!!!!!");
                    break;
                case MsgTypes.L2P_AG_UE_CAPTURE_IND_MSG_TYPE:
                    Log.e(TAG, "L2P_AG_UE_CAPTURE_IND_MSG_TYPE captured!!!!!!!!!!!!!!!!!!!!!!!");
                    mOuter.get().startCount();
                    break;
                case MsgTypes.L2P_AG_UE_RELEASE_IND_MSG_TYPE:
                    Log.e(TAG, "L2P_AG_UE_RELEASE_IND_MSG_TYPE released!!!!!!!!!!!!!!!!!!!!!!!!");
                    mOuter.get().stopCount();
                    break;
                case MsgTypes.L2P_AG_CELL_CAPTURE_IND_MSG_TYPE:
                    mOuter.get().resolveCellCaptureMsg(globalMsg);
                    Log.e(TAG, "L2P_AG_CELL_CAPTURE_IND_MSG_TYPE captured!!!!!!!!!!!!!!!!!!!!!!!");
                    break;
                default:
                    break;
            }
        }
    }

    private void startCount() {
        //needToCount = true;
    }

    private void stopCount() {
        //needToCount = false;
        ueInfoQueue.clear();
        result.clear();
        cellRSRPList.clear();
    }

    public void start(Activity activity) {
        MessageDispatcher.getInstance().RegisterHandler(handler);

        Log.d(TAG, String.format("================== Orientation find stmsi : %s ====================", Global.TARGET_STMSI));
        Log.d(TAG, MsgSendHelper.convertBytesToString(Global.TARGET_STMSI.getBytes()));
        trigger.start(activity, Status.Service.ORIENTATION);

        task = new Task();
        future = Global.ThreadPool.scheduledThreadPool.scheduleAtFixedRate(task, COUNT_INTERVAL, COUNT_INTERVAL, TimeUnit.SECONDS);
    }

    public void stop() {
        trigger.stop();
        if (future != null) {
            future.cancel(true);
        }
        task = null;
        future = null;
        stopCount();
    }

    private void resolveProtocolDataMsg(Global.GlobalMsg globalMsg) {
        /*if (!needToCount) {
            return;
        }*/

        UEInfo ueInfo = new UEInfo();

        MsgL1_PROTOCOL_DATA msg = new MsgL1_PROTOCOL_DATA(globalMsg.getBytes());
        if (msg.getMstL1ProtocolDataHeader().getMu8Direction() == UPLINK) {
            MsgL1_UL_UE_MEAS ul_ue_meas = new MsgL1_UL_UE_MEAS(
                    MsgSendHelper.getSubByteArray(globalMsg.getBytes(), MsgL1_PROTOCOL_DATA.byteArrayLen + 100, MsgL1_UL_UE_MEAS.byteArrayLen));
            ueInfo.chType = ul_ue_meas.getMuChType();
            ueInfo.rsrp = ul_ue_meas.getMs16Power() * 0.125;
            ueInfo.sinr = (int) (ul_ue_meas.getMs8Sinr() * 0.5);
            Log.d(TAG, String.format("**************Type = %d, RSRP = %f, SINR = %d ************ ", ueInfo.chType, ueInfo.rsrp, ueInfo.sinr));
            ueInfoQueue.add(ueInfo);
        }
    }

    private void resolvePhyCommeasIndMsg(Global.GlobalMsg globalMsg) {
        /*if (!needToCount) {
            return;
        }*/

        MsgL1_PHY_COMMEAS_IND msg = new MsgL1_PHY_COMMEAS_IND(globalMsg.getBytes());
        if (isCRSChType(msg.getMstL1PHYComentIndHeader().getMu32MeasSelect())) {
            MsgCRS_RSRPQI_INFO crs_rsrpqi_info = new MsgCRS_RSRPQI_INFO(
                    MsgSendHelper.getSubByteArray(globalMsg.getBytes(), MsgL1_PHY_COMMEAS_IND.byteArrayLen, MsgCRS_RSRPQI_INFO.byteArrayLen));
            cellRSRPList.add(crs_rsrpqi_info.getMstCrs0RsrpqiInfo().getMs16CRS_RP() * 0.125F);
        }
    }
    private void resolveCellCaptureMsg(Global.GlobalMsg globalMsg) {
        MsgL2P_AG_CELL_CAPTURE_IND msg = new MsgL2P_AG_CELL_CAPTURE_IND(globalMsg.getBytes());
        Status.DeviceWorkingStatus status = msg.getMu16Rsrp() == 0 ? Status.DeviceWorkingStatus.ABNORMAL : Status.DeviceWorkingStatus.NORMAL;
        Float rsrp = msg.getMu16Rsrp() * 1.0F;
        int pci = msg.getMu16PCI();
        DeviceManager.getInstance().getDevice(globalMsg.getDeviceName()).setWorkingStatus(status);
        DeviceManager.getInstance().getDevice(globalMsg.getDeviceName()).getCellInfo().rsrp = rsrp;
        Log.e(TAG, String.format("==========status : %s, rsrp : %f ============", status.name(), rsrp) + "PCI:" + pci);
    }
    private boolean isCRSChType(long type) {
        return (type & 0x2000) == 0x2000;
    }

    class Task implements Runnable {
        @Override
        public void run() {
            result.log();

            Message msg = new Message();
            if (/*needToCount &&*/ !ueInfoQueue.isEmpty()) {
                msg.obj = getOrientationInfo();
            }else{
                msg.obj = null;
            }

            outHandler.sendMessage(msg);
        }
    }

    private OrientationInfo getOrientationInfo() {
        result.consumeQueue(ueInfoQueue.size());
        OrientationInfo info = new OrientationInfo();
        info.timeStamp = new SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(new Date());
        info.PUCCHRsrp = result.average(PUCCH);
        info.PUSCHRsrp = result.average(PUSCH);
        info.CellRsrp = getCellRsrp();
        DeviceManager.getInstance().getDevices().get(0).getCellInfo().rsrp = info.CellRsrp;
        Log.e(TAG, String.format("============ PUCCH = %f , PUSCH = %f, CELL = %f, Time = %s. ==============",
                info.PUCCHRsrp, info.PUSCHRsrp, info.CellRsrp, info.timeStamp));
        return info;
    }

    private Float getCellRsrp() {
        Float ret = 0F;
        for (int index = 0; index < cellRSRPList.size(); index++) {
            ret += cellRSRPList.get(index);
        }
        ret /= cellRSRPList.size();
        cellRSRPList.clear();
        return ret;
    }

    class RsrpResult {
        public static final double INVALID_RSRP = Double.NaN;

        private Map<Integer, Queue<UEInfo>> results;
        private int maxLen;

        public RsrpResult(int maxLen) {
            results = new HashMap<>();
            this.maxLen = maxLen;
        }

        public void consumeQueue(int count) {
            while (count-- > 0) {
                UEInfo info = ueInfoQueue.poll();
                add(info);
            }
        }

        private void add(UEInfo info) {
            if (validate(info)) {
                int type = info.chType;
                add(info, type);
            }
        }

        private void add(UEInfo info, int type) {
            if (!results.containsKey(type)) {
                results.put(type, new LinkedList<UEInfo>());
            }
            Queue<UEInfo> queue = results.get(type);
            if (queue.size() == maxLen) {
                queue.poll();
            }
            queue.add(info);
        }

        public double average(int type) {
            if (!results.containsKey(type)) {
                return INVALID_RSRP;
            }

            List<Double> rsrpList = new ArrayList<>();
            for (UEInfo info : results.get(type)) {
                rsrpList.add(info.rsrp);
            }

            double ret;
            Collections.sort(rsrpList);
            if (rsrpList.size() >= 10) {
                int from = 3;
                int to = rsrpList.size() - 2;
                ret = getAverage(rsrpList.subList(from, to));
            } else if (rsrpList.size() >= 5) {
                int from = 1;
                int to = rsrpList.size() - 1;
                ret = getAverage(rsrpList.subList(from, to));
            } else if (rsrpList.size() > 0) {
                ret = getAverage(rsrpList);
            } else {
                ret = INVALID_RSRP;
            }

            return ret;
        }

        public void clear() {
            results.clear();
        }

        private double getAverage(List<Double> list) {
            Double ret = 0.0;
            for (Double item : list) {
                ret += item;
            }
            return ret / list.size();
        }

        private boolean validate(UEInfo info) {
            if (info == null) {
                return false;
            }
            if (info.rsrp < MIN_RSRP || info.rsrp > MAX_RSRP) {
                return false;
            }
            return true;
        }

        public void log() {
            Log.d(TAG, "==============================================================");
            for (Map.Entry<Integer, Queue<UEInfo>> entry : results.entrySet()) {
                StringBuilder builder = new StringBuilder();
                builder.append(entry.getKey()).append(" : ");
                for (UEInfo info : entry.getValue()) {
                    builder.append(info.rsrp).append(", ");
                }
                Log.d(TAG, builder.toString());
            }
            Log.d(TAG, "==============================================================");
        }
    }
}
