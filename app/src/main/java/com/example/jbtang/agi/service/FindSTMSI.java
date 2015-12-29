package com.example.jbtang.agi.service;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Pair;

import com.example.jbtang.agi.core.Global;
import com.example.jbtang.agi.core.Status;
import com.example.jbtang.agi.core.type.U32;
import com.example.jbtang.agi.core.type.U8;
import com.example.jbtang.agi.device.DeviceManager;
import com.example.jbtang.agi.device.MonitorDevice;
import com.example.jbtang.agi.messages.MessageDispatcher;
import com.example.jbtang.agi.messages.ag2pc.MsgL2P_AG_CELL_CAPTURE_IND;
import com.example.jbtang.agi.messages.ag2pc.MsgL2P_AG_UE_CAPTURE_IND;
import com.example.jbtang.agi.messages.base.MsgTypes;
import com.example.jbtang.agi.trigger.SMSTrigger;
import com.example.jbtang.agi.trigger.Trigger;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by jbtang on 11/1/2015.
 */
public class FindSTMSI {
    private static final String TAG = "FindSTMSI";
    private static final FindSTMSI instance = new FindSTMSI();
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");

    private Map<String, Integer> sTMSI2Count;
    private List<CountSortedInfo> countSortedInfoList;
    private List<TimeSortedInfo> timeSortedInfoList;
    private myHandler handler;
    private Trigger trigger;

    public List<CountSortedInfo> getCountSortedInfoList() {
        countSortedInfoList.clear();
        Set<Map.Entry<String, Integer>> sortedStmsi = getSortedSTMSI();
        for (Map.Entry<String, Integer> entry : sortedStmsi) {
            CountSortedInfo info = new CountSortedInfo();
            info.stmsi = entry.getKey();
            info.count = String.valueOf(entry.getValue());
            countSortedInfoList.add(info);
        }
        return countSortedInfoList;
    }

    public List<TimeSortedInfo> getTimeSortedInfoList() {
        return timeSortedInfoList;
    }

    private FindSTMSI() {
        sTMSI2Count = new HashMap<>();
        countSortedInfoList = new ArrayList<>();
        timeSortedInfoList = new ArrayList<>();
        handler = new myHandler(this);
        trigger = SMSTrigger.getInstance();
    }

    public static FindSTMSI getInstance() {
        return instance;
    }

    static class myHandler extends Handler {
        private final WeakReference<FindSTMSI> mOuter;

        public myHandler(FindSTMSI findSTMSI) {
            mOuter = new WeakReference<>(findSTMSI);
        }

        @Override
        public void handleMessage(Message msg) {
            Global.GlobalMsg globalMsg = (Global.GlobalMsg) msg.obj;
            switch (msg.what) {
                case MsgTypes.L2P_AG_UE_CAPTURE_IND_MSG_TYPE:
                    mOuter.get().resolveUECaptureMsg(globalMsg);
                    break;
                case MsgTypes.L2P_AG_CELL_CAPTURE_IND_MSG_TYPE:
                    mOuter.get().resolveCellCaptureMsg(globalMsg);
                default:
                    break;
            }
        }
    }

    public void start(Activity activity) {
        MessageDispatcher.getInstance().RegisterHandler(handler);
        sTMSI2Count.clear();
        countSortedInfoList.clear();
        timeSortedInfoList.clear();
        trigger.start(activity, Status.Service.FINDSTMIS);
    }

    public void stop() {
        trigger.stop();
    }

    private void resolveCellCaptureMsg(Global.GlobalMsg globalMsg) {
        MsgL2P_AG_CELL_CAPTURE_IND msg = new MsgL2P_AG_CELL_CAPTURE_IND(globalMsg.getBytes());
        Status.DeviceWorkingStatus status = msg.getMu16Rsrp() == 0 ? Status.DeviceWorkingStatus.ABNORMAL : Status.DeviceWorkingStatus.NORMAL;
        Float rsrp = msg.getMu16Rsrp() * 0.125F;
        DeviceManager.getInstance().getDevice(globalMsg.getDeviceName()).setWorkingStatus(status);
        DeviceManager.getInstance().getDevice(globalMsg.getDeviceName()).getCellInfo().rsrp = rsrp;
        Log.d(TAG, String.format("==========status : %s, rsrp : %f ============", status.name(), rsrp));
    }

    private void resolveUECaptureMsg(Global.GlobalMsg globalMsg) {
        MsgL2P_AG_UE_CAPTURE_IND msg = new MsgL2P_AG_UE_CAPTURE_IND(globalMsg.getBytes());
        String stmsi = "";
        byte mec = 0;
        int mu8EstCause = 0;
        if ((msg.getMstUECaptureInfo().getMu8UEIDTypeFlg() & 0x20) == 0x20) {
            mec = msg.getMstUECaptureInfo().getMau8GUTIDATA()[5].getBytes()[0];
            byte[] stmsiBytes = new byte[4];
            stmsiBytes[3] = msg.getMstUECaptureInfo().getMau8GUTIDATA()[6].getBytes()[0];
            stmsiBytes[2] = msg.getMstUECaptureInfo().getMau8GUTIDATA()[7].getBytes()[0];
            stmsiBytes[1] = msg.getMstUECaptureInfo().getMau8GUTIDATA()[8].getBytes()[0];
            stmsiBytes[0] = msg.getMstUECaptureInfo().getMau8GUTIDATA()[9].getBytes()[0];
            mu8EstCause = msg.getMstUECaptureInfo().getMu8Pading1();
            stmsi = new StringBuilder().append(padLeft(String.format("%X", mec), "0", 2))
                    .append(padLeft(String.format("%X", stmsiBytes[0]), "0", 2))
                    .append(padLeft(String.format("%X", stmsiBytes[1]), "0", 2))
                    .append(padLeft(String.format("%X", stmsiBytes[2]), "0", 2))
                    .append(padLeft(String.format("%X", stmsiBytes[3]), "0", 2))
                    .toString();
        }
        if (!(mu8EstCause == 0x02)) {
            return;
        }
        Log.d(TAG, String.format("---------Find STMSI :  %s -----------", stmsi));
        int count = sTMSI2Count.containsKey(stmsi) ? sTMSI2Count.get(stmsi) : 0;
        sTMSI2Count.put(stmsi, ++count);

        TimeSortedInfo info = new TimeSortedInfo();
        info.stmsi = stmsi;
        info.time = DATE_FORMAT.format(new Date());
        info.board = globalMsg.getDeviceName();
        info.pci = String.valueOf(DeviceManager.getInstance().getDevice(info.board).getCellInfo().pci);
        timeSortedInfoList.add(info);

        for (Map.Entry<String, Integer> entry : sTMSI2Count.entrySet()) {
            Log.d(TAG, String.format("===================%s : %d =================", entry.getKey(), entry.getValue()));
        }
    }

    private Set<Map.Entry<String, Integer>> getSortedSTMSI() {
        Set<Map.Entry<String, Integer>> sortedSTMSI = new TreeSet<>(
                new Comparator<Map.Entry<String, Integer>>() {
                    public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                        Integer d1 = o1.getValue();
                        Integer d2 = o2.getValue();
                        int r = d2.compareTo(d1);
                        if (r != 0) {
                            return r;
                        } else {
                            return o2.getKey().compareTo(o1.getKey());
                        }
                    }
                }
        );
        sortedSTMSI.addAll(sTMSI2Count.entrySet());
        return sortedSTMSI;
    }

    private String padLeft(String src, String pad, int len) {
        StringBuilder builder = new StringBuilder();
        len -= src.length();
        while (len-- > 0) {
            builder.append(pad);
        }
        builder.append(src);
        return builder.toString();
    }

    public static class CountSortedInfo {
        public String stmsi;
        public String count;
    }

    public static class TimeSortedInfo {
        public String stmsi;
        public String time;
        public String pci;
        public String board;
    }
}