package com.example.jbtang.agi.ui;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.example.jbtang.agi.R;
import com.example.jbtang.agi.core.Global;
import com.example.jbtang.agi.core.Status;
import com.example.jbtang.agi.dao.configuration.ConfigurationDAO;
import com.example.jbtang.agi.dao.configuration.ConfigurationDBManager;

import java.util.regex.Pattern;

public class SystemConfigurationActivity extends AppCompatActivity {

    private RadioButton triggerSMS;
    private RadioButton triggerPhone;
    private EditText triggerInterval;
    private EditText filterInterval;
    private EditText receivingAntennaNum;
    private EditText totalTriggerCount;
    private EditText targetPhoneNum;
    private ConfigurationDBManager mgr;

    private static final int DEFAULT_TRIGGER_INTERVAL_SMS_MIN = 10;
    private static final int DEFAULT_TRIGGER_INTERVAL_SMS_MAX = 30;
    private static final int DEFAULT_TRIGGER_INTERVAL_PHONE_MIN = 2;
    private static final int DEFAULT_TRIGGER_INTERVAL_PHONE_MAX = 10;
    private static final int DEFAULT_SMS_FILTER_INTERVAL_MIN = 5;
    private static final int DEFAULT_SMS_FILTER_INTERVAL_MAX = 10;
    private static final int DEFAULT_SILENCECHECKTIME = 80;
    private static final int DEFAULT_RECEIVINGANTENNANUM = 2;
    private static final int DEFAULT_TOTAL_TRIGGER_COUNT = 30;

    private static final Pattern PHONE_NUMBER = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_configuration);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_system_configuration, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_system_configuration_save) {
            save();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mgr.closeDB();
    }

    private void init() {
        triggerSMS = (RadioButton) findViewById(R.id.system_configure_trigger_sms);
        triggerPhone = (RadioButton) findViewById(R.id.system_configure_trigger_phone);
        triggerInterval = (EditText) findViewById(R.id.system_configure_trigger_interval);
        filterInterval = (EditText) findViewById(R.id.system_configure_filter_threshold);
        receivingAntennaNum = (EditText) findViewById(R.id.system_configure_receiving_antenna_count);
        totalTriggerCount = (EditText) findViewById(R.id.system_configure_trigger_max);
        targetPhoneNum = (EditText) findViewById(R.id.system_configure_target);
        mgr = new ConfigurationDBManager(this);
        initDefaultValue();
    }

    private void initDefaultValue() {
        final ConfigurationDAO dao = mgr.getConfiguration(Global.UserInfo.user_name);

        triggerSMS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (dao != null) {
                        triggerInterval.setText(String.valueOf(dao.triggerInterval));
                    } else {
                        String text = String.format("%d~%d", DEFAULT_TRIGGER_INTERVAL_SMS_MIN, DEFAULT_TRIGGER_INTERVAL_SMS_MAX);
                        triggerInterval.setText("");
                        triggerInterval.setHint(text);
                    }
                }
            }
        });

        triggerPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (dao != null) {
                        triggerInterval.setText(String.valueOf(dao.triggerInterval));
                    } else {
                        String text = String.format("%d~%d", DEFAULT_TRIGGER_INTERVAL_PHONE_MIN, DEFAULT_TRIGGER_INTERVAL_PHONE_MAX);
                        triggerInterval.setText("");
                        triggerInterval.setHint(text);
                    }
                }
            }
        });


        if (dao != null) {
            switch (dao.type) {
                case SMS:
                    triggerSMS.setChecked(true);
                    break;
                case PHONE:
                    triggerPhone.setChecked(true);
            }
        }

        String text = String.format("%d~%d", DEFAULT_SMS_FILTER_INTERVAL_MIN, DEFAULT_SMS_FILTER_INTERVAL_MAX);
        if (dao == null) {
            filterInterval.setHint(text);
        } else {
            filterInterval.setText(String.valueOf(dao.filterInterval));
        }

        receivingAntennaNum.setText(String.valueOf(dao == null ? DEFAULT_RECEIVINGANTENNANUM : dao.receivingAntennaNum));
        totalTriggerCount.setText(String.valueOf(dao == null ? DEFAULT_TOTAL_TRIGGER_COUNT : dao.totalTriggerCount));
        targetPhoneNum.setText(dao == null ? "" : dao.targetPhoneNum);
    }

    private void save() {
        if (validate()) {
            saveToCache();
            saveToDAO();
            Intent intent = new Intent(this, CellMonitorActivity.class);
            startActivity(intent);
        }
    }

    private boolean validate() {
        try {
            validateNull();
            validatePhoneNumber();
            validateTriggerInterval();
            validateFilterInterval();
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

    private void validateTriggerInterval() throws IllegalArgumentException {
        Integer interval = Integer.parseInt(triggerInterval.getText().toString());
        if (triggerSMS.isChecked() && !(interval >= DEFAULT_TRIGGER_INTERVAL_SMS_MIN && interval <= DEFAULT_TRIGGER_INTERVAL_SMS_MAX)) {
            throw new IllegalArgumentException(String.format("触发间隔配置错误,需在%d~%d中(含)",
                    DEFAULT_TRIGGER_INTERVAL_SMS_MIN, DEFAULT_TRIGGER_INTERVAL_SMS_MAX));
        }
        if (triggerPhone.isChecked() && !(interval >= DEFAULT_TRIGGER_INTERVAL_PHONE_MIN && interval <= DEFAULT_TRIGGER_INTERVAL_PHONE_MAX)) {
            throw new IllegalArgumentException(String.format("触发间隔配置错误,需在%d~%d中(含)",
                    DEFAULT_TRIGGER_INTERVAL_PHONE_MIN, DEFAULT_TRIGGER_INTERVAL_PHONE_MAX));
        }
    }

    private void validateNull() throws IllegalArgumentException {
        if (triggerInterval.getText().toString().isEmpty()
                || !(triggerSMS.isChecked() || triggerPhone.isChecked())
                || filterInterval.getText().toString().isEmpty()
                || receivingAntennaNum.getText().toString().isEmpty()
                || totalTriggerCount.getText().toString().isEmpty()
                || targetPhoneNum.getText().toString().isEmpty()) {
            throw new IllegalArgumentException("参数不可为空!");
        }
    }

    private void validatePhoneNumber() throws IllegalArgumentException {
        if (!PHONE_NUMBER.matcher(targetPhoneNum.getText().toString()).matches()) {
            throw new IllegalArgumentException("手机号码不合法!");
        }
    }

    private void validateFilterInterval() throws IllegalArgumentException {
        Integer interval = Integer.parseInt(filterInterval.getText().toString());
        if (!(interval >= DEFAULT_SMS_FILTER_INTERVAL_MIN && interval <= DEFAULT_SMS_FILTER_INTERVAL_MAX)) {
            throw new IllegalArgumentException(String.format("过滤门限配置错误,需在%d~%d中(含)",
                    DEFAULT_SMS_FILTER_INTERVAL_MIN, DEFAULT_SMS_FILTER_INTERVAL_MAX));
        }
    }

    private void saveToCache() {
        Global.Configuration.name = Global.UserInfo.user_name;
        Global.Configuration.type = triggerSMS.isChecked() ? Status.TriggerType.SMS : Status.TriggerType.PHONE;
        Global.Configuration.triggerInterval = Integer.parseInt(triggerInterval.getText().toString());
        Global.Configuration.filterInterval = Integer.parseInt(filterInterval.getText().toString());
        Global.Configuration.silenceCheckTimer = DEFAULT_SILENCECHECKTIME;
        Global.Configuration.receivingAntennaNum = Integer.parseInt(receivingAntennaNum.getText().toString());
        Global.Configuration.triggerTotalCount = Integer.parseInt(totalTriggerCount.getText().toString());
        Global.Configuration.targetPhoneNum = targetPhoneNum.getText().toString();
    }

    private void saveToDAO() {
        ConfigurationDAO dao = new ConfigurationDAO(Global.Configuration.name, Global.Configuration.type, Global.Configuration.triggerInterval,
                Global.Configuration.filterInterval, Global.Configuration.silenceCheckTimer, Global.Configuration.receivingAntennaNum,
                Global.Configuration.triggerTotalCount, Global.Configuration.targetPhoneNum);
        mgr.insertOrUpdate(dao);
    }
}
