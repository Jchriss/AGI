package com.example.jbtang.agi.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jbtang.agi.R;
import com.example.jbtang.agi.core.Global;
import com.example.jbtang.agi.core.Status;
import com.example.jbtang.agi.dao.devices.DeviceDAO;
import com.example.jbtang.agi.dao.devices.DeviceDBManager;
import com.example.jbtang.agi.device.Device;
import com.example.jbtang.agi.device.DeviceManager;
import com.example.jbtang.agi.device.MonitorDevice;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DeviceConfigurationActivity extends AppCompatActivity {

    private static final String TAG = "DeviceConfiguration";
    private static final String IP_SPLITTER = ".";

    private List<MonitorDevice> devices;
    private DeviceDBManager mgr;

    private List<MonitorDevice> getDevices() {
        List<MonitorDevice> devices = new ArrayList<>();
        List<DeviceDAO> deviceDAOs = mgr.listDB();
        for (DeviceDAO dao : deviceDAOs) {
            devices.add(new MonitorDevice(dao.name, dao.ip, dao.type));
        }
        return devices;
    }

    /**
     * for ListView
     */
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

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.device_configuration_list_item, null);
                holder = new ViewHolder();
                holder.imageView = (ImageView) convertView.findViewById(R.id.device_configuration_item_icon);
                holder.title = (TextView) convertView.findViewById(R.id.device_configuration_item_title);
                holder.detailBtn = (Button) convertView.findViewById(R.id.device_configuration_detail_btn);
                holder.switchBtn = (Switch) convertView.findViewById(R.id.device_configuration_item_btn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Status.PingResult pingResult = devices.get(position).getPingStatus();
            if (pingResult == Status.PingResult.SUCCEED) {
                holder.imageView.setBackgroundColor(Color.GREEN);
            } else {
                holder.imageView.setBackgroundColor(Color.RED);
            }

            holder.title.setText(devices.get(position).getName());

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
                    Toast.makeText(DeviceConfigurationActivity.this, "Detail", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(DeviceConfigurationActivity.this, "Switch", Toast.LENGTH_SHORT).show();
                    switchBtnHandler(mPosition, buttonView);
                    break;
                default:
                    break;
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

    private void deleteDevice(int position) {
        if (devices.get(position).isConnected()) {
            try {
                devices.get(position).disconnect();
            } catch (Exception e) {
                //TODO
            }
        }
        mgr.deleteByName(devices.get(position).getName());
        MonitorDevice device = devices.remove(position);
        device.release();
        refreshDeviceListView();
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

    private void switchBtnHandler(int position, CompoundButton buttonView) {
        if (buttonView.isChecked()) {
            try {
                devices.get(position).connect();
                if (!devices.get(position).isConnected()) {
                    buttonView.setChecked(false);
                }
            } catch (Exception e) {
                buttonView.setChecked(false);
            }
        } else {
            try {
                devices.get(position).disconnect();
            } catch (Exception e) {
                //TODO
            }
        }
    }

    public final class ViewHolder {
        public ImageView imageView;
        public TextView title;
        public Button detailBtn;
        public Switch switchBtn;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_configuration);
        mgr = new DeviceDBManager(this);
        devices = getDevices();
        ListView listView = (ListView) findViewById(R.id.device_configuration_listView);
        MyAdapter adapter = new MyAdapter(this);
        listView.setAdapter(adapter);
        Global.ThreadPool.scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                refreshDeviceListView();
            }
        }, 1, 3, TimeUnit.SECONDS);
    }

    private void refreshDeviceListView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ListView listView = (ListView) findViewById(R.id.device_configuration_listView);
                ((MyAdapter) listView.getAdapter()).notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_device_configuration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.menu_device_configuration_add_device:
                showAddDeviceDialog();
                return true;
            case R.id.menu_device_configuration_save:
                saveToNextStep();
                return true;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mgr.closeDB();
        System.exit(0);
    }

    private void showAddDeviceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View view = inflater.inflate(R.layout.device_configuration_add_device, null);
        final EditText ip = (EditText) view.findViewById(R.id.device_configure_ip);
        final RadioButton fdd = (RadioButton) view.findViewById(R.id.device_configure_hint_board_type_fdd);

        builder.setTitle(R.string.menu_device_configuration).setIcon(android.R.drawable.ic_dialog_info)
                .setView(view)
                .setNegativeButton(R.string.menu_device_configuration_dialog_cancel, null)
                .setPositiveButton(R.string.menu_device_configuration_dialog_add, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        addDevice(ip, fdd);
                    }
                })
                .show();
    }

    private void addDevice(EditText ip, RadioButton fdd) {
        if (validateInput(ip.getText().toString())) {
            Status.BoardType type;
            if (fdd.isChecked()) {
                type = Status.BoardType.FDD;
            } else {
                type = Status.BoardType.TDD;
            }
            String iptext = ip.getText().toString();
            String name = MonitorDevice.DEVICE_NAME_PREFIX + iptext.substring(iptext.lastIndexOf(IP_SPLITTER) + 1);
            MonitorDevice device = new MonitorDevice(name, ip.getText().toString(), type);
            addDeviceToDB(device);
            addDeviceToListView(device);
        }
    }

    private void addDeviceToDB(MonitorDevice device) {
        DeviceDAO deviceDAO = new DeviceDAO(
                device.getName(),
                device.getIP(),
                device.getDataPort(),
                device.getMessagePort(),
                device.getType());
        mgr.add(Collections.singletonList(deviceDAO));
    }

    private void addDeviceToListView(MonitorDevice device) {
        devices.add(device);
        refreshDeviceListView();
    }

    private boolean validateInput(String ip) {
        try {
            validateIP(ip);
        } catch (IllegalArgumentException e) {
            new AlertDialog.Builder(this)
                    .setTitle("非法输入")
                    .setMessage(e.getMessage())
                    .setPositiveButton("确定", null)
                    .show();
            return false;
        }
        return true;
    }

    private void validateIP(String ip) throws IllegalArgumentException {
        String regex = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)$";
        if (!ip.matches(regex)) {
            throw new IllegalArgumentException("IP地址格式错误！");
        }
        if (mgr.isExistsByIP(ip)) {
            throw new IllegalArgumentException("该IP地址已有板卡使用！");
        }
    }

    private void saveToNextStep() {
        for (MonitorDevice device : devices) {
            if (device.isConnected()) {
                DeviceManager.getInstance().add(device);
            }
        }
        Intent intent = new Intent(this, SystemConfigurationActivity.class);
        startActivity(intent);
    }
}
