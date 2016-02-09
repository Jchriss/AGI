package com.example.jbtang.agi.ui;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.graphics.Color;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jbtang.agi.R;
import com.example.jbtang.agi.core.Global;
import com.example.jbtang.agi.core.Status;
import com.example.jbtang.agi.dao.devices.DeviceDAO;
import com.example.jbtang.agi.dao.devices.DeviceDBManager;
import com.example.jbtang.agi.device.DeviceManager;
import com.example.jbtang.agi.device.MonitorDevice;
import com.example.jbtang.agi.external.MonitorApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    private Button connectBtn;
    private Button disconnectBtn;
    private TextView deviceStatusText;
    private TextView deviceColorOne;
    private TextView deviceColorTwo;
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

        connectBtn = (Button) findViewById(R.id.main_menu_device_connnect);
        disconnectBtn = (Button) findViewById(R.id.main_menu_device_disconnect);
        connectBtn.setOnClickListener(new conBtnOnclickListener());
        disconnectBtn.setOnClickListener(new disconBtnOnclickListener());

        deviceColorOne = (TextView) findViewById(R.id.main_menu_device_background_one);
        deviceColorTwo = (TextView) findViewById(R.id.main_menu_device_background_two);
        deviceStatusText = (TextView) findViewById(R.id.main_menu_device_status_text);

        dmgr = new DeviceDBManager(this);
        devices = getDevices();

        broadcastReceiver = new myBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MonitorApplication.BROAD_FROM_MAIN_MENU_ACTIVITY);
        filter.addAction(MonitorApplication.BROAD_FROM_CONFIGURATION_ACTIVITY);
        registerReceiver(broadcastReceiver, filter);

        Global.ThreadPool.scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                refreshDeviceStatusBar();
            }
        }, 1, 3, TimeUnit.SECONDS);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    private static final String TAG = "DeviceConfiguration";
    private static final String IP_SPLITTER = ".";

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
    class conBtnOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            for(MonitorDevice device:devices){
                try {
                    device.connect();
                    DeviceManager.getInstance().add(device);
                } catch (Exception e) {
                }
            }
        }
    }
    class disconBtnOnclickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            for(MonitorDevice device:devices){
                try {
                    DeviceManager.getInstance().remove(device.getName());
                    device.disconnect();
                } catch (Exception e) {
                }
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

     private class myBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            if(intent.getAction().equals(MonitorApplication.BROAD_FROM_MAIN_MENU_ACTIVITY)) {
                final int colorOne = intent.getIntExtra("colorOne", 0xFFFF0000);
                final int colorTwo = intent.getIntExtra("colorTwo", 0xFFFF0000);
                final String statusText = intent.getStringExtra("statusText");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        deviceColorOne.setBackgroundColor(colorOne);
                        deviceColorTwo.setBackgroundColor(colorTwo);
                        deviceStatusText.setText(statusText);
                    }
                });
            }
            else if(intent.getAction().equals(MonitorApplication.BROAD_FROM_CONFIGURATION_ACTIVITY)){
                String flag = intent.getStringExtra("flag");
                switch (flag)
                {
                    case ConfigurationActivity.ADD_DEVICE_FLAG:
                        String name = intent.getStringExtra("name");
                        String ip = intent.getStringExtra("ip");
                        Status.BoardType type = (Status.BoardType) intent.getSerializableExtra("type");
                        devices.add(new MonitorDevice(name, ip, type));
                        break;
                    case ConfigurationActivity.DELETE_DEVICE_FLAG:
                        String temDevice = intent.getStringExtra("name");
                        MonitorDevice device = DeviceManager.getInstance().getDevice(temDevice);
                        if(device != null)
                            device.release();
                        for(MonitorDevice temdevice:devices){
                            if(temdevice.getName().equals(temDevice)) {
                                temdevice.release();
                                devices.remove(temdevice);
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void refreshDeviceStatusBar(){
        int colorOne = Color.RED;
        int colorTwo = Color.RED;
        String statusText = "未就绪";
        int i = 0;
        for(MonitorDevice device:devices) {
            i++;
            if(device.isConnected()){
                if(i == 1)
                    colorOne = Color.GREEN;
                else if(i == 2)
                    colorTwo = Color.GREEN;
                statusText = "已连接";

            }
            else {
                if (device.getPingStatus() == Status.PingResult.SUCCEED) {
                    if (i == 1)
                        colorOne = Color.YELLOW;
                    else if (i == 2)
                        colorTwo = Color.YELLOW;
                    if (statusText != "已连接")
                        statusText = "已就绪";
                }
            }
            //Log.d("changeDevice","devicename:"+device.getName()+" hashcode:"+device.hashCode());
        }
        Intent intent = new Intent();
        intent.putExtra("colorOne",colorOne);
        intent.putExtra("colorTwo",colorTwo);
        intent.putExtra("statusText",statusText);
        intent.setAction(MonitorApplication.BROAD_FROM_MAIN_MENU_ACTIVITY);
        sendBroadcast(intent);
    }

}

