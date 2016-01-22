package com.example.jbtang.agi.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jbtang.agi.R;
import com.example.jbtang.agi.core.CellInfo;
import com.example.jbtang.agi.core.Status;
import com.example.jbtang.agi.device.DeviceManager;
import com.example.jbtang.agi.device.MonitorDevice;
import com.example.jbtang.agi.external.MonitorApplication;
import com.example.jbtang.agi.external.service.MonitorService;
import com.example.jbtang.agi.service.CellMonitor;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.fmaster.GSMNCellListMessage;
import io.fmaster.GSMServCellMessage;
import io.fmaster.LTENcellInfo;
import io.fmaster.LTENcellListMessage;
import io.fmaster.LTEPwrInfoMessage;
import io.fmaster.LTEServCellMessage;

public class CellMonitorActivity extends AppCompatActivity {

    private static final String TAG = "CellMonitorActivity";
    private EditText manualEARFCN;
    private EditText manualPCI;
    private CheckBox manualChoose;
    private ListView listView;
    private ListView confirmListView;
    private List<CellInfo> cellInfoList;
    private Set<CellInfo> monitorCellSet;
    private List<CellInfo> monitorCellList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cell_monitor);

        initData();
        startService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cell_monitor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_device_configuration_save) {
            saveToNext();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private MonitorService mBoundService;

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

            mBoundService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBoundService = ((MonitorService.LocalBinder) service).getService();
            MonitorApplication.MonitorService = mBoundService;
        }
    };

    private void startService() {
        MonitorApplication.IMEI = getIMEI(this);
        Intent startIntent = new Intent(this, MonitorService.class);
        startService(startIntent);
        //Intent intent=new Intent(this,MonitorService.class);
        bindService(startIntent, connection, 0);
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        unbindService(connection);
        super.onDestroy();
    }

    public static String getIMEI(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
        return imei != null ? imei : "";
    }

    private final MyBroadcastReceiver receiver = new MyBroadcastReceiver();

    class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals("")) {
                return;
            }
            refreshView(intent);
        }
    }

    private void refreshView(Intent intent) {

        int flag = intent.getFlags();
        Bundle bundle = intent.getExtras();
        switch (flag) {
            case MonitorApplication.SERVER_CELL_FLAG:
                LTEServCellMessage mServCellMessage = bundle.getParcelable("msg");
                SCELL_refresh(mServCellMessage);
                break;
            case MonitorApplication.POWER_INFO_FLAG:
                LTEPwrInfoMessage mPwrInfoMessage = bundle.getParcelable("msg");
                PWR_INFO_refresh(mPwrInfoMessage);
                break;
            case MonitorApplication.N_CELL_FLAG:
                LTENcellListMessage mNcellListMessage = bundle.getParcelable("msg");
                NCell_refresh(mNcellListMessage);
                break;
            case MonitorApplication.STMSI:
                String stmsi = bundle.getString("msg");
                STMSI_refresh(stmsi);
                break;
            case MonitorApplication.GSM_SERVER_CELL_FLAG:
                GSMServCellMessage mGSMServCellMessage = bundle.getParcelable("msg");
                GSM_SCELL_refresh(mGSMServCellMessage);
                break;
            case MonitorApplication.GSM_N_CELL_FLAG:
                GSMNCellListMessage mgsmNcellListMessage = bundle.getParcelable("msg");
                GSM_NCell_refresh(mgsmNcellListMessage);
                break;
            default:
                break;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((MyAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    private void SCELL_refresh(LTEServCellMessage mServCellMessage) {
        if (mServCellMessage != null) {
            CellInfo info = new CellInfo();
            info.earfcn = mServCellMessage.getEARFCN();
            info.pci = mServCellMessage.getPCI();
            info.tai = mServCellMessage.getTAC();

            if (cellInfoList.isEmpty()) {
                cellInfoList.add(info);
            } else {
                cellInfoList.set(0, info);
            }
        }
    }

    private void PWR_INFO_refresh(LTEPwrInfoMessage mPwrInfoMessage) {
        if (mPwrInfoMessage != null) {
            if (!cellInfoList.isEmpty()) {
                cellInfoList.get(0).sinr = mPwrInfoMessage.getSINR();
                cellInfoList.get(0).rsrp = mPwrInfoMessage.getRSRP();
            }
        }
    }

    private void STMSI_refresh(String stmsi) {
        if (stmsi != null && !stmsi.isEmpty()) {
            //stmsiTextView.setText("");
            //stmsiTextView.append("STMSI: " + stmsi);
        }
    }

    private void NCell_refresh(LTENcellListMessage mNcellListMessage) {
        if (mNcellListMessage != null) {
            List<CellInfo> ret = new ArrayList<>();
            for (LTENcellInfo ncellInfo : mNcellListMessage.getNcells()) {
                CellInfo info = new CellInfo();
                info.earfcn = ncellInfo.getEARFCN();
                info.pci = (short) ncellInfo.getPCI();
                info.rsrp = ncellInfo.getRSRP();
                ret.add(info);
            }
            cellInfoList = new ArrayList<>(ret);
            /*if (ret.size() > 1) {
                cellInfoList = cellInfoList.subList(0, 1);
                //cellInfoList.addAll(ret.subList(1, ret.size()));
            }*/
        }
    }

    private void GSM_NCell_refresh(GSMNCellListMessage mNcellListMessage) {
        if (mNcellListMessage != null) {
            //gsmServeCell.setText("");
            //gsmServeCell.append("GSM Ncell info: count " + mNcellListMessage.getNcells().size());
        }

    }

    private void GSM_SCELL_refresh(GSMServCellMessage mServCellMessage) {
        if (mServCellMessage != null) {
            Integer EARFCN = mServCellMessage.getEARFCN();
            //gsmNcell.setText("");
            //gsmNcell.append("GSM serve cell, " + EARFCN);
        }
    }

    public MonitorApplication MyApp;

    public void initData() {
        MyApp = (MonitorApplication) getApplication();
        IntentFilter filter = new IntentFilter(MonitorApplication.BROAD_TO_MAIN_ACTIVITY);
        registerReceiver(receiver, filter);
        manualEARFCN = (EditText) findViewById(R.id.cell_monitor_manual_earfcn);
        manualPCI = (EditText) findViewById(R.id.cell_moitor_manual_pci);
        manualChoose = (CheckBox) findViewById(R.id.cell_moitor_manual_choose);

        cellInfoList = new ArrayList<>();

        listView = (ListView) findViewById(R.id.cell_monitor_listView);
        MyAdapter adapter = new MyAdapter(this);
        listView.setAdapter(adapter);

        monitorCellList = new ArrayList<>();
        confirmListView = (ListView) findViewById(R.id.cell_monitor_confirm_listView);
        MyConfirmAdapter confirmAdapter = new MyConfirmAdapter(this);
        confirmListView.setAdapter(confirmAdapter);

        monitorCellSet = new HashSet<>();
        manualChoose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!validateManualEarfcn()) {
                    buttonView.setChecked(false);
                    return;
                }

                CellInfo info = new CellInfo();
                info.earfcn = Integer.parseInt(manualEARFCN.getText().toString());
                info.pci = Short.parseShort(manualPCI.getText().toString());
                if (isChecked) {
                    monitorCellSet.add(info);
                } else {
                    monitorCellSet.remove(info);
                }
                monitorCellList = new ArrayList<>(monitorCellSet);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((MyConfirmAdapter) confirmListView.getAdapter()).notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private boolean validateManualEarfcn() {
        if (manualEARFCN.getText().toString().isEmpty() || manualPCI.getText().toString().isEmpty()) {
            new AlertDialog.Builder(this)
                    .setTitle("非法输入")
                    .setMessage("请确认'EARFCN'及'PCI'是否输入正确！")
                    .setPositiveButton("确定", null)
                    .show();
            return false;
        }
        return true;
    }

    /**
     * for ListView
     */
    public final class ViewHolder {
        public TextView earfcn;
        public TextView pci;
        public TextView tai;
        public TextView sinr;
        public TextView rsrp;
        public CheckBox choose;
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return cellInfoList.size();
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
                convertView = mInflater.inflate(R.layout.cell_monitor_list_item, null);
                holder = new ViewHolder();
                holder.earfcn = (TextView) convertView.findViewById(R.id.cell_monitor_item_earfcn);
                holder.pci = (TextView) convertView.findViewById(R.id.cell_monitor_item_pci);
                holder.tai = (TextView) convertView.findViewById(R.id.cell_monitor_item_tai);
                holder.sinr = (TextView) convertView.findViewById(R.id.cell_monitor_item_sinr);
                holder.rsrp = (TextView) convertView.findViewById(R.id.cell_monitor_item_rsrp);
                holder.choose = (CheckBox) convertView.findViewById(R.id.cell_monitor_item_choose);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final Integer earfcn = cellInfoList.get(position).earfcn;
            if (earfcn == Integer.MAX_VALUE) {
                holder.earfcn.setText(CellInfo.NULL_VALUE);
            } else {
                holder.earfcn.setText("" + earfcn);
            }

            final Short pci = cellInfoList.get(position).pci;
            if (pci == Short.MAX_VALUE) {
                holder.pci.setText(CellInfo.NULL_VALUE);
            } else {
                holder.pci.setText("" + pci);
            }

            final Short tai = cellInfoList.get(position).tai;
            if (tai == Short.MAX_VALUE) {
                holder.tai.setText(CellInfo.NULL_VALUE);
            } else {
                holder.tai.setText("" + tai);
            }

            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumIntegerDigits(3);
            nf.setMaximumFractionDigits(2);

            final Float sinr = cellInfoList.get(position).sinr;
            if (!Float.isNaN(sinr)) {
                holder.sinr.setText(nf.format(sinr));
            } else {
                holder.sinr.setText(CellInfo.NULL_VALUE);
            }

            final Float rsrp = cellInfoList.get(position).rsrp;
            if (!Float.isNaN(rsrp)) {
                holder.rsrp.setText(nf.format(rsrp));
            } else {
                holder.rsrp.setText(CellInfo.NULL_VALUE);
            }

            holder.choose.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    CellInfo info = new CellInfo();
                    info.earfcn = earfcn;
                    info.pci = pci;
                    info.tai = tai;
                    info.sinr = sinr;
                    info.rsrp = rsrp;

                    if (isChecked) {
                        monitorCellSet.add(info);
                    } else {
                        monitorCellSet.remove(info);
                    }
                    monitorCellList = new ArrayList<>(monitorCellSet);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((MyConfirmAdapter) confirmListView.getAdapter()).notifyDataSetChanged();
                        }
                    });
                }
            });
            return convertView;
        }
    }

    private void saveToNext() {
        Map<Status.BoardType, List<MonitorDevice>> deviceMap = distributeMonitorDevices();
        Map<Status.BoardType, List<CellInfo>> cellInfoMap = distributeCellInfo();
        if (validate(deviceMap, cellInfoMap)) {
            distributeToMonitor(deviceMap, cellInfoMap);

            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        }
    }

    private boolean validate(Map<Status.BoardType, List<MonitorDevice>> deviceMap,
                             Map<Status.BoardType, List<CellInfo>> cellInfoMap) {
        for (Status.BoardType type : Status.BoardType.values()) {
            if (deviceMap.get(type).size() < cellInfoMap.get(type).size()) {
                new AlertDialog.Builder(this)
                        .setTitle("非法配置")
                        .setMessage(String.format("%s 模式缺少可用板卡!", type.name()))
                        .setPositiveButton("确定", null)
                        .show();
                return false;
            }
        }
        return true;
    }

    private void distributeToMonitor(Map<Status.BoardType, List<MonitorDevice>> deviceMap,
                                     Map<Status.BoardType, List<CellInfo>> cellInfoMap) {
        for (Status.BoardType type : Status.BoardType.values()) {
            List<CellInfo> cellInfoList = cellInfoMap.get(type);
            for (int index = 0; index < cellInfoList.size(); index++) {
                CellMonitor.getInstance().prepareMonitor(deviceMap.get(type).get(index), cellInfoList.get(index));
            }
        }
    }

    private Map<Status.BoardType, List<MonitorDevice>> distributeMonitorDevices() {
        Map<Status.BoardType, List<MonitorDevice>> ret = new HashMap<>();
        for (Status.BoardType type : Status.BoardType.values()) {
            ret.put(type, new ArrayList<MonitorDevice>());
        }
        for (MonitorDevice device : DeviceManager.getInstance().getDevices()) {
            ret.get(device.getType()).add(device);
        }
        return ret;
    }

    private Map<Status.BoardType, List<CellInfo>> distributeCellInfo() {
        Map<Status.BoardType, List<CellInfo>> ret = new HashMap<>();
        for (Status.BoardType type : Status.BoardType.values()) {
            ret.put(type, new ArrayList<CellInfo>());
        }
        for (CellInfo info : monitorCellSet) {
            ret.get(info.toBoardType()).add(info);
        }
        return ret;
    }

    /**
     * for ListView
     */
    public final class ConfirmViewHolder {
        public TextView earfcn;
        public TextView pci;
        public TextView tai;
        public TextView board;
    }

    public class MyConfirmAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyConfirmAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return monitorCellList.size();
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
            final ConfirmViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.cell_monitor_confirm_list_item, null);
                holder = new ConfirmViewHolder();
                holder.earfcn = (TextView) convertView.findViewById(R.id.cell_monitor_confirm_item_earfcn);
                holder.pci = (TextView) convertView.findViewById(R.id.cell_monitor_confirm_item_pci);
                holder.tai = (TextView) convertView.findViewById(R.id.cell_monitor_confirm_item_tai);
                holder.board = (TextView) convertView.findViewById(R.id.cell_monitor_confirm_item_board);
                convertView.setTag(holder);
            } else {
                holder = (ConfirmViewHolder) convertView.getTag();
            }

            final Integer earfcn = monitorCellList.get(position).earfcn;
            if (earfcn == Integer.MAX_VALUE) {
                holder.earfcn.setText(CellInfo.NULL_VALUE);
            } else {
                holder.earfcn.setText("" + earfcn);
            }

            final Short pci = monitorCellList.get(position).pci;
            if (pci == Short.MAX_VALUE) {
                holder.pci.setText(CellInfo.NULL_VALUE);
            } else {
                holder.pci.setText("" + pci);
            }

            final Short tai = monitorCellList.get(position).tai;
            if (tai == Short.MAX_VALUE) {
                holder.tai.setText(CellInfo.NULL_VALUE);
            } else {
                holder.tai.setText("" + tai);
            }

            return convertView;
        }
    }
}
