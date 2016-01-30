package com.example.jbtang.agi.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jbtang.agi.R;
import com.example.jbtang.agi.core.Global;
import com.example.jbtang.agi.core.Status;
import com.example.jbtang.agi.device.DeviceManager;
import com.example.jbtang.agi.external.MonitorApplication;
import com.example.jbtang.agi.service.OrientationFinding;
import com.example.jbtang.agi.utils.BarChartView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by jbtang on 11/7/2015.
 */
public class OrientationFindingActivity extends AppCompatActivity {
    private static final int RSRP_LIST_MAX_SIZE = 4;
    private boolean startToFind;

    private TextView myStmsiTextView;
    private TextView targetStmsiTextView;
    private Button startButton;
    private Button stopButton;
    private ListView resultListView;
    private LinearLayout resultGraphLayout;
    private myHandler handler;
    private List<OrientationFinding.OrientationInfo> orientationInfoList;
    private TextView cellConfirmColor;
    private TextView cellRsrpColor;
    private TextView pciNum;

    public static List<String> options = Arrays.asList("", "一", "二", "三", "四");
    private BarChartView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation_finding);

        startToFind = false;
        OrientationFinding.getInstance().targetStmsi = Global.TARGET_STMSI;
        orientationInfoList = new ArrayList<>();
        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_orientation_find, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_find_stmsi_save) {
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {
        //myStmsiTextView = (TextView) findViewById(R.id.orientation_find_my_stmsi);
        targetStmsiTextView = (TextView) findViewById(R.id.orientation_find_target_stmsi);
        startButton = (Button) findViewById(R.id.orientation_find_start);
        stopButton = (Button) findViewById(R.id.orientation_find_stop);
        cellConfirmColor = (TextView)findViewById(R.id.orientation_confirm_background);
        cellRsrpColor = (TextView)findViewById(R.id.orientation_rsrp_background);
        pciNum = (TextView)findViewById(R.id.orientation_pci_num);

        resultListView = (ListView) findViewById(R.id.orientation_find_result_list);
        resultListView.setAdapter(new MyAdapter(this));

        resultGraphLayout = (LinearLayout) findViewById(R.id.orientation_find_layout_result_graph);

        targetStmsiTextView.setText(OrientationFinding.getInstance().targetStmsi);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrientationFinding.getInstance().start(OrientationFindingActivity.this);
                startToFind = true;
                orientationInfoList.clear();
                refreshBarChart();
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrientationFinding.getInstance().stop();
                startToFind = false;
            }
        });

        IntentFilter filter = new IntentFilter(MonitorApplication.BROAD_TO_ORIENTATION_ACTIVITY);
        registerReceiver(receiver, filter);

        OrientationFinding.getInstance().setOutHandler(new myHandler(this));
    }

    private void refresh(String type) {
        final String  temtype = type;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(temtype == "all") {
                    ((MyAdapter) resultListView.getAdapter()).notifyDataSetChanged();
                    resultListView.setSelection(orientationInfoList.size() - 1);
                    refreshBarChart();
                }
                refreshCellStatusBar();
            }
        });
    }

    private void refreshBarChart() {
        resultGraphLayout.removeAllViews();
        view = new BarChartView(OrientationFindingActivity.this);

        int from = orientationInfoList.size() < RSRP_LIST_MAX_SIZE ? 0 : orientationInfoList.size() - RSRP_LIST_MAX_SIZE;
        int to = orientationInfoList.size();
        int[] pucchList = new int[RSRP_LIST_MAX_SIZE];
        int[] puschList = new int[RSRP_LIST_MAX_SIZE];

        int rsrpIndex = RSRP_LIST_MAX_SIZE - 1;
        for (; to > from; to--, rsrpIndex--) {
            pucchList[rsrpIndex] = orientationInfoList.get(to - 1).getStandardPucch();
            puschList[rsrpIndex] = orientationInfoList.get(to - 1).getStandardPusch();
        }
        for (; to > from; to--, rsrpIndex--) {
            pucchList[rsrpIndex] = 0;
            puschList[rsrpIndex] = 0;
        }
        view.initData(pucchList, puschList, options, "功率图");
        resultGraphLayout.addView(view.getBarChartView());
    }
    private void refreshCellStatusBar(){
        if(DeviceManager.getInstance().getDevices().get(0).getWorkingStatus()== Status.DeviceWorkingStatus.NORMAL){
            Float rsrp = DeviceManager.getInstance().getDevices().get(0).getCellInfo().rsrp;
            cellConfirmColor.setBackgroundColor(Color.GREEN);
            if(rsrp >= -90){
                cellRsrpColor.setBackgroundColor(Color.GREEN);
            }else if(rsrp < -90 && rsrp >= -100){
                cellRsrpColor.setBackgroundColor(Color.YELLOW);
            }else if(rsrp < -100 && rsrp >= -110){
                cellRsrpColor.setBackgroundColor(Color.MAGENTA);
            }else if(rsrp <-110){
                cellRsrpColor.setBackgroundColor(Color.RED);
            }
        }else{
            cellConfirmColor.setBackgroundColor(Color.RED);
            cellRsrpColor.setBackgroundColor(Color.RED);
        }
        pciNum.setText(DeviceManager.getInstance().getDevices().get(0).getCellInfo().pci.toString());
    }
    static class myHandler extends Handler {
        private final WeakReference<OrientationFindingActivity> mOuter;

        public myHandler(OrientationFindingActivity activity) {
            mOuter = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if(msg.obj != null) {
                OrientationFinding.OrientationInfo info = (OrientationFinding.OrientationInfo) msg.obj;
                mOuter.get().orientationInfoList.add(info);
                mOuter.get().refresh("all");
            }else{
                mOuter.get().refresh("");
            }

        }
    }

    /**
     * for ListView
     */
    private final class ViewHolder {
        public TextView pucch;
        public TextView pusch;
        public TextView time;
    }

    private class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return orientationInfoList.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.orientation_finding_list_item, null);
                holder = new ViewHolder();
                holder.pucch = (TextView) convertView.findViewById(R.id.orientation_find_list_item_pucch);
                holder.pusch = (TextView) convertView.findViewById(R.id.orientation_find_list_item_pusch);
                holder.time = (TextView) convertView.findViewById(R.id.orientation_find_list_item_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.pucch.setText(String.format("%.2f", orientationInfoList.get(position).PUCCHRsrp));
            holder.pusch.setText(String.format("%.2f", orientationInfoList.get(position).PUSCHRsrp));
            holder.time.setText(orientationInfoList.get(position).timeStamp);
            return convertView;
        }
    }


    private MyBroadcastReceiver receiver = new MyBroadcastReceiver();

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("")) {
                return;
            }
            //refreshView(intent);
        }
    }

//    private void refreshView(Intent intent) {
//
//        int flag = intent.getFlags();
//        Bundle bundle = intent.getExtras();
//        switch (flag) {
//            case MonitorApplication.STMSI:
//                String stmsi = bundle.getString("msg");
//                myStmsiTextView.setText(stmsi);
//                break;
//        }
//    }
}
