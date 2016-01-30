package com.example.jbtang.agi.external;

import io.fmaster.ChartStyle;
import io.fmaster.GSMNCellListMessage;
import io.fmaster.GSMServCellMessage;
import io.fmaster.LTEPwrInfoMessage;
import io.fmaster.LTEServCellMessage;
import io.fmaster.LineChartType;
import io.fmaster.LTENcellListMessage;

import android.app.Application;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;

public class MonitorApplication extends Application {


    public static String IMEI;


    public static LineChartType lineChartType = LineChartType.None;

    public static ChartStyle RSSIChartStyle = new ChartStyle(Color.RED, Color.RED);
    public static ChartStyle RSRPChartStyle = new ChartStyle(Color.GREEN, Color.GREEN);
    public static ChartStyle RSRQChartStyle = new ChartStyle(Color.BLUE, Color.BLUE);
    public static ChartStyle CINAChartStyle = new ChartStyle(Color.argb(255, 0, 230, 205), Color.argb(255, 0, 230, 205));
    public static ChartStyle RSRP_1ChartStyle = new ChartStyle(Color.YELLOW, Color.YELLOW);
    public static ChartStyle RSRP_2ChartStyle = new ChartStyle(Color.argb(255, 254, 0, 251), Color.argb(255, 254, 0, 251));
    public static ChartStyle PUSCH_TxPwrChartStyle = new ChartStyle(Color.WHITE, Color.WHITE);


    public static ChartStyle GSM_RxlevChartStyle = new ChartStyle(Color.RED, Color.RED);
    public static ChartStyle GSM_RxqualChartStyle = new ChartStyle(Color.GREEN, Color.GREEN);
    public static ChartStyle GSM_TxpowerChartStyle = new ChartStyle(Color.argb(255, 0, 230, 205), Color.argb(255, 0, 230, 205));

    public static final String BROAD_TO_MAIN_ACTIVITY = "com.example.jbtang.agi.ui.TestCellMonitorActivity";
    public static final String BROAD_TO_ORIENTATION_ACTIVITY = "com.example.jbtang.agi.ui.OrientationFindingActivity";
    public static final String BROAD_FROM_MAIN_MENU_ACTIVITY = "com.example.jbtang.agi.ui.MainMenuActivity";
    public static final String BROAD_FROM_CONFIGURATION_ACTIVITY = "com.example.jbtang.agi.ui.ConfigurationActivity";

    //static data for view display
    public static LTEServCellMessage data_ServCell = new LTEServCellMessage();
    public static LTEPwrInfoMessage data_PwrInfo = new LTEPwrInfoMessage();
    public static LTENcellListMessage data_NcellList = new LTENcellListMessage();
    public static String stmsi;


    //static data for gsm
    public static GSMServCellMessage data_gsm_ServCell = new GSMServCellMessage();
    public static GSMNCellListMessage data_gsm_NcellList = new GSMNCellListMessage();
    public static final byte msOutputPower[][] = {
            {39, 39, 39, 37, 35, 33, 31, 29, 27, 25, 23, 21, 19, 17, 15, 13, 11, 9, 7, 5, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
            {30, 28, 26, 24, 22, 20, 18, 16, 14, 12, 10, 8, 6, 4, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 36, 34, 32},
            {30, 28, 26, 24, 22, 20, 18, 16, 14, 12, 10, 8, 6, 4, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 33, 32}
    };

    public static final int SERVER_CELL_FLAG = 16;
    public static final int POWER_INFO_FLAG = 17;
    public static final int N_CELL_FLAG = 18;
    public static final int STMSI = 19;


    public static final int GSM_SERVER_CELL_FLAG = 21;
    public static final int GSM_N_CELL_FLAG = 22;


    public static com.example.jbtang.agi.external.service.MonitorService MonitorService;


    private static MonitorApplication mInstance = null;


    public static MonitorApplication getInstance() {
        return mInstance;
    }


    public static void sendBroad(String action, int flag, String key, Parcelable par) {
        Intent intent = new Intent();
        intent.setFlags(flag);
        intent.setAction(action);
        Bundle bundle = new Bundle();
        bundle.putParcelable(key, par);
        intent.putExtras(bundle);
        getInstance().sendBroadcast(intent);
    }

    public static void sendBroad(String action, int flag, String key, int par) {
        Intent intent = new Intent();
        intent.setFlags(flag);
        intent.setAction(action);
        Bundle bundle = new Bundle();
        bundle.putInt(key, par);
        intent.putExtras(bundle);
        getInstance().sendBroadcast(intent);
    }

    public static void sendBroad(String action, int flag, String key, String par) {
        Intent intent = new Intent();
        intent.setFlags(flag);
        intent.setAction(action);
        Bundle bundle = new Bundle();
        bundle.putString(key, par);
        intent.putExtras(bundle);
        getInstance().sendBroadcast(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }
}