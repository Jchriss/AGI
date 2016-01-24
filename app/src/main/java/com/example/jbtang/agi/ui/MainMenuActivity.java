package com.example.jbtang.agi.ui;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jbtang.agi.R;
import com.example.jbtang.agi.dao.devices.DeviceDAO;
import com.example.jbtang.agi.dao.devices.DeviceDBManager;
import com.example.jbtang.agi.device.DeviceManager;
import com.example.jbtang.agi.device.MonitorDevice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by xiang on 2016/1/11.
 */
public class MainMenuActivity extends AppCompatActivity {
    private GridView gridView;
    private ArrayList<HashMap<String,Object>> itemList;
    private SimpleAdapter simpleAdapter;
    private String texts[];
    private int images[];
    private long exitTime = 0;
    private TextView deviceBackgroundOne;
    private TextView deviceBackgroundTwo;
    private List<MonitorDevice> devices;
    private DeviceDBManager dmgr;
    private myBroadcastReceiver broadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        init();
    }
    public void init(){
        images = new int[]{R.drawable.cell_monitor,
                R.drawable.find_stmsi,
                R.drawable.orientation,
                R.drawable.interference,
                R.drawable.configuration,
                R.drawable.cellphone_info};
        texts = new String[]{this.getString(R.string.title_main_menu_cell_monitor),
                this.getString(R.string.title_main_menu_find_STMSI),
                this.getString(R.string.title_main_menu_orientation),
                this.getString(R.string.title_main_menu_interference_detection),
                this.getString(R.string.title_main_menu_configuration),
                this.getString(R.string.title_main_menu_cellPhone_info)};
        itemList = new ArrayList<HashMap<String, Object>>();
        for(int i = 0; i < 6; i++){
            HashMap<String,Object> map = new HashMap<String,Object>();
            map.put("itemImage",images[i]);
            map.put("itemText",texts[i]);
            itemList.add(map);
        }
        simpleAdapter = new SimpleAdapter(this,
                itemList,
                R.layout.main_menu_item,
                new String[]{"itemImage","itemText"},
                new int[]{R.id.main_menu_item_image,R.id.main_menu_item_name});
        gridView = (GridView) findViewById(R.id.main_menu_gridView);
        gridView.setAdapter(simpleAdapter);
        gridView.setOnItemClickListener(new ItemClickListener());

        dmgr = new DeviceDBManager(this);
        devices = getDevices();
        for (MonitorDevice device : devices) {
                DeviceManager.getInstance().add(device);
        }
        ListView listView = (ListView) findViewById(R.id.main_menu_device_configuration_listView);
        MyAdapter adapter = new MyAdapter(this);
        listView.setAdapter(adapter);

        broadcastReceiver = new myBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("DeviceChanges");
        registerReceiver(broadcastReceiver, filter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            dmgr.closeDB();
            finish();
            System.exit(0);
        }
    }
    class ItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            HashMap<String,Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
            String itemText = (String) item.get("itemText");
            Object object = item.get("itemImage");
            //Toast.makeText(MainMenuActivity.this,itemText,Toast.LENGTH_LONG).show();

            switch (images[position]){
                case R.drawable.cell_monitor:
                    startActivity(new Intent(MainMenuActivity.this, CellMonitorActivity.class));
                    break;
                case R.drawable.find_stmsi:
                    startActivity(new Intent(MainMenuActivity.this, FindSTMSIActivity.class));
                    break;
                case R.drawable.orientation:
                    startActivity(new Intent(MainMenuActivity.this, OrientationFindingActivity.class));
                    break;
                case R.drawable.interference:
                    break;
                case R.drawable.configuration:
                    startActivity(new Intent(MainMenuActivity.this, ConfigurationActivity.class));
                    break;
                case R.drawable.cellphone_info:
                    break;
            }
        }
    }

    private List<MonitorDevice> getDevices() {
        List<MonitorDevice> devices = new ArrayList<>();
        List<DeviceDAO> deviceDAOs = dmgr.listDB();
        for (DeviceDAO dao : deviceDAOs) {
            devices.add(new MonitorDevice(dao.name, dao.ip, dao.type));
        }
        return devices;
    }

    public class MyAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public MyAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return devices.size();
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
        public final class ViewHolder {
            public TextView title;
            public Button detailBtn;
            public Switch switchBtn;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.device_configuration_list_item, null);
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.device_configuration_item_title);
                holder.detailBtn = (Button) convertView.findViewById(R.id.device_configuration_detail_btn);
                holder.switchBtn = (Switch) convertView.findViewById(R.id.device_configuration_item_btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String temDevice = devices.get(position).getName();
            Log.d("device","device: "+temDevice+"position: " + position);
            holder.title.setText(temDevice);
            holder.switchBtn.setChecked(DeviceManager.getInstance().getDevice(temDevice).isConnected());

            MyListener switchListener;
            switchListener = new MyListener(position, SelectedBtn.SWITCH);
            holder.switchBtn.setTag(position);
            holder.switchBtn.setOnClickListener(switchListener);
            holder.switchBtn.setOnCheckedChangeListener(switchListener);
            MyListener detailListener;
            detailListener = new MyListener(position, SelectedBtn.DETAIL);
            holder.detailBtn.setTag(position);
            holder.detailBtn.setOnClickListener(detailListener);
            return convertView;
        }
    }
    public enum SelectedBtn {
        DETAIL,
        SWITCH
    }
    private class MyListener implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        int mPosition;
        SelectedBtn selectedBtn;

        public MyListener(int inPosition, SelectedBtn selectedBtn) {
            this.mPosition = inPosition;
            this.selectedBtn = selectedBtn;
        }

        @Override
        public void onClick(View v) {
            switch (selectedBtn) {
                case DETAIL:
                    Toast.makeText(MainMenuActivity.this, "Detail", Toast.LENGTH_SHORT).show();
                    showDetailInfo(mPosition);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (selectedBtn) {
                case SWITCH:
                    Toast.makeText(MainMenuActivity.this, "Switch", Toast.LENGTH_SHORT).show();
                    switchBtnHandler(mPosition, buttonView);
                    break;
                default:
                    break;
            }
        }
    }
    private void switchBtnHandler(int position, CompoundButton buttonView) {
        String temDevice = devices.get(position).getName();
        if (buttonView.isChecked()) {
            try {
                DeviceManager.getInstance().getDevice(temDevice).connect();
                if (!DeviceManager.getInstance().getDevice(temDevice).isConnected()) {
                    buttonView.setChecked(false);
                }
            } catch (Exception e) {
                buttonView.setChecked(false);
            }
        } else {
            try {
                DeviceManager.getInstance().getDevice(temDevice).disconnect();
            } catch (Exception e) {
                //TODO
            }
        }
    }
    private void showDetailInfo(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.device_configuration_show_detail, null);
        builder.setTitle(R.string.title_device_configuration_show_detail)
                .setView(view)
                .setPositiveButton(R.string.page_device_configure_show_detail_ok, null)
                .setNegativeButton(R.string.page_device_configure_show_detail_delete, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        confirmDelete(position);
                    }
                })
                .show();
        initialShowDetailInfoDialog(view, devices.get(position));
    }
    private void initialShowDetailInfoDialog(View view, MonitorDevice device) {
        TextView name = (TextView) view.findViewById(R.id.device_configuration_show_name);
        TextView ip = (TextView) view.findViewById(R.id.device_configuration_show_ip);
        TextView dataPort = (TextView) view.findViewById(R.id.device_configuration_show_dataPort);
        TextView messagePort = (TextView) view.findViewById(R.id.device_configuration_show_messagePort);
        TextView type = (TextView) view.findViewById(R.id.device_configuration_show_type);

        name.setText("设备名称 : " + device.getName());
        ip.setText("IP地址 : " + device.getIP());
        dataPort.setText("数据端口 : " + String.valueOf(device.getDataPort()));
        messagePort.setText("消息端口 : " + String.valueOf(device.getMessagePort()));
        type.setText("板卡类型 : " + device.getType().name());
    }
    private void confirmDelete(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_device_configuration_confirm_delete)
                .setNegativeButton(R.string.page_device_configure_confirm_delete_cancel, null)
                .setPositiveButton(R.string.page_device_configure_confirm_delete_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDevice(position);
                    }
                })
                .show();
    }
    private void deleteDevice(int position) {
        String temDevice = devices.get(position).getName();
        if (DeviceManager.getInstance().getDevice(temDevice).isConnected()) {
            try {
                DeviceManager.getInstance().getDevice(temDevice).disconnect();
            } catch (Exception e) {
                //TODO
            }
        }
        dmgr.deleteByName(devices.get(position).getName());
        devices.remove(position);
        DeviceManager.getInstance().remove(temDevice);
        ListView listView = (ListView) findViewById(R.id.main_menu_device_configuration_listView);
        ((MyAdapter) listView.getAdapter()).notifyDataSetChanged();
    }
     private class myBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
        String flag = intent.getStringExtra("action");
            switch (flag)
            {
                case "addDevice":
                case "deleteDevice":
                case "changeDeviceStatus":
                    devices = DeviceManager.getInstance().getDevices();
                    ListView listView = (ListView) findViewById(R.id.main_menu_device_configuration_listView);
                    ((MyAdapter) listView.getAdapter()).notifyDataSetChanged();
                    break;
            }
        }
    };
}

