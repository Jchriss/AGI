package com.example.jbtang.agi.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jbtang.agi.R;
import com.example.jbtang.agi.core.Global;
import com.example.jbtang.agi.core.Status;
import com.example.jbtang.agi.device.DeviceManager;
import com.example.jbtang.agi.messages.base.MsgTypes;
import com.example.jbtang.agi.service.FindSTMSI;

import org.w3c.dom.Text;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by jbtang on 11/1/2015.
 */
public class FindSTMSIActivity extends AppCompatActivity {
    private boolean startToFind;

    private List<FindSTMSI.CountSortedInfo> countSortedInfoList;

    private Button startButton;
    private Button stopButton;
    private TextView triggeredCount;
    private TextView targetPhone;
    private ListView count;
    private EditText targetSTMSI;
    private myHandler handler;
    private TextView cellConfirmColor;
    private TextView cellRsrpColor;
    private TextView pciNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_stmsi);

        countSortedInfoList = new ArrayList<>();
        startToFind = false;
        init();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_find_stmsi, menu);
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
            saveToNext();
        }

        return super.onOptionsItemSelected(item);
    }

    private void init() {
        startButton = (Button) findViewById(R.id.find_stmsi_start_button);
        stopButton = (Button) findViewById(R.id.find_stmsi_stop_button);
        triggeredCount = (TextView) findViewById(R.id.find_stmsi_triggered_count);
        targetPhone = (TextView) findViewById(R.id.find_stmsi_target_phone_num);
        targetSTMSI = (EditText) findViewById(R.id.find_stmsi_target_stmsi);
        cellConfirmColor = (TextView)findViewById(R.id.find_stmsi_confirm_background);
        cellRsrpColor = (TextView)findViewById(R.id.find_stmsi_rsrp_background);
        pciNum = (TextView)findViewById(R.id.find_stmsi_pci_num);

        count = (ListView) findViewById(R.id.find_stmsi_count_listView);
        CountAdapter countAdapter = new CountAdapter(this);
        count.setAdapter(countAdapter);

        count.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                targetSTMSI.setText(countSortedInfoList.get(position).stmsi);
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                targetPhone.setText(Global.Configuration.targetPhoneNum);
                triggeredCount.setText("0");
                FindSTMSI.getInstance().start(FindSTMSIActivity.this);
                startToFind = true;
            }
        });
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindSTMSI.getInstance().stop();
                startToFind = false;
            }
        });

        handler = new myHandler(this);
        Global.ThreadPool.scheduledThreadPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (startToFind) {
                    handler.sendMessage(new Message());
                }
            }
        }, 1, 3, TimeUnit.SECONDS);
    }

    private void refresh() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                countSortedInfoList.clear();
                countSortedInfoList.addAll(FindSTMSI.getInstance().getCountSortedInfoList());
                ((CountAdapter) count.getAdapter()).notifyDataSetChanged();
                if (DeviceManager.getInstance().getDevices().get(0).getWorkingStatus() == Status.DeviceWorkingStatus.NORMAL) {
                    Float rsrp = DeviceManager.getInstance().getDevices().get(0).getCellInfo().rsrp;
                    cellConfirmColor.setBackgroundColor(Color.GREEN);
                    if (rsrp >= -90) {
                        cellRsrpColor.setBackgroundColor(Color.GREEN);
                    } else if (rsrp < -90 && rsrp >= -100) {
                        cellRsrpColor.setBackgroundColor(Color.YELLOW);
                    } else if (rsrp < -100 && rsrp >= -110) {
                        cellRsrpColor.setBackgroundColor(Color.MAGENTA);
                    } else if (rsrp < -110) {
                        cellRsrpColor.setBackgroundColor(Color.RED);
                    }
                } else {
                    cellConfirmColor.setBackgroundColor(Color.RED);
                    cellRsrpColor.setBackgroundColor(Color.RED);
                }
                pciNum.setText(DeviceManager.getInstance().getDevices().get(0).getCellInfo().pci.toString());
            }
        });
    }
    static class myHandler extends Handler {
        private final WeakReference<FindSTMSIActivity> mOuter;

        public myHandler(FindSTMSIActivity activity) {
            mOuter = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            mOuter.get().refresh();
        }

    }

    /**
     * for count ListView
     */
    private final class CountViewHolder {
        public TextView stmsi;
        public TextView count;
        public TextView time;
        public TextView pci;
        public TextView earfcn;
    }

    private class CountAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        public CountAdapter(Context context) {
            this.mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return countSortedInfoList.size();
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
            final CountViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.find_stmsi_count_list_item, null);
                holder = new CountViewHolder();
                holder.stmsi = (TextView) convertView.findViewById(R.id.find_stmsi_count_list_item_stmsi);
                holder.count = (TextView) convertView.findViewById(R.id.find_stmsi_count_list_item_count);
                holder.time = (TextView) convertView.findViewById(R.id.find_stmsi_count_list_item_time);
                holder.pci = (TextView) convertView.findViewById(R.id.find_stmsi_count_list_item_pci);
                holder.earfcn = (TextView) convertView.findViewById(R.id.find_stmsi_count_list_item_earfcn);
                convertView.setTag(holder);
            } else {
                holder = (CountViewHolder) convertView.getTag();
            }

            holder.stmsi.setText(countSortedInfoList.get(position).stmsi);
            holder.count.setText(countSortedInfoList.get(position).count);
            holder.time.setText(countSortedInfoList.get(position).time);
            holder.pci.setText(countSortedInfoList.get(position).pci);
            holder.earfcn.setText(countSortedInfoList.get(position).earfcn);
            return convertView;
        }
    }

    /**
     * for count ListView
     */

    private void saveToNext() {
        String stmsi = targetSTMSI.getText().toString();
        if (validateSTMSI(stmsi)) {
            Intent intent = new Intent(this, MainMenuActivity.class);
            Global.TARGET_STMSI = stmsi;
            intent.putExtra(Global.TARGET_STMSI, stmsi);
            startActivity(intent);
        }
    }

    private boolean validateSTMSI(String stmsi) {
        String regex = "[a-zA-Z\\d]{10}$";
        if (!stmsi.matches(regex)) {
            new AlertDialog.Builder(this)
                    .setTitle("非法STMSI")
                    .setMessage("STMSI需为10位字母数字组合!")
                    .setPositiveButton("确定", null)
                    .show();
            return false;
        }
        return true;
    }
}
