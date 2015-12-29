package com.example.jbtang.agi.dao.devices;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.jbtang.agi.core.Status;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jbtang on 10/8/2015.
 */
public class DeviceDBManager {
    private DeviceDBHelper helper;
    private SQLiteDatabase db;

    public DeviceDBManager(Context context) {
        helper = new DeviceDBHelper(context);
        db = helper.getWritableDatabase();
        db.execSQL("CREATE TABLE IF NOT EXISTS " + DeviceDBHelper.TABLE_NAME +
                "(name TEXT PRIMARY KEY, ip TEXT, dataPort INTEGER, messagePort INTEGER, type INTEGER)");
    }

    public void add(List<DeviceDAO> deviceDAOs) {
        db.beginTransaction();
        try {
            for (DeviceDAO deviceDAO : deviceDAOs) {
                insertOrUpdate(deviceDAO);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void insertOrUpdate(DeviceDAO deviceDAO) {
        if (!isExistsByName(deviceDAO.name)) {
            insert(deviceDAO);
        } else {
            update(deviceDAO);
        }
    }

    private void insert(DeviceDAO deviceDAO) {
        db.execSQL("INSERT INTO " + DeviceDBHelper.TABLE_NAME + " VALUES(?, ?, ?, ?, ?)",
                new Object[]{deviceDAO.name, deviceDAO.ip, deviceDAO.dataPort, deviceDAO.messagePort, deviceDAO.type.ordinal()});
    }

    public void update(DeviceDAO deviceDAO) {
        ContentValues cv = new ContentValues();
        cv.put("ip", deviceDAO.ip);
        cv.put("dataPort", deviceDAO.dataPort);
        cv.put("messagePort", deviceDAO.messagePort);
        cv.put("type", deviceDAO.type.ordinal());
        db.update(DeviceDBHelper.TABLE_NAME, cv, "name = ?", new String[]{deviceDAO.name});
    }

    public boolean isExistsByName(String name) {
        return isExistsByField("name", name);
    }

    public boolean isExistsByIP(String ip) {
        return isExistsByField("ip", ip);
    }

    private boolean isExistsByField(String field, String value) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ").append(DeviceDBHelper.TABLE_NAME).append(" WHERE ")
                .append(field).append(" =?");

        return isExistsBySQL(sql.toString(), new String[]{value});
    }

    private boolean isExistsBySQL(String sql, String[] selectionArgs) {
        boolean result = false;

        final Cursor c = db.rawQuery(sql, selectionArgs);
        try {
            if (c.moveToFirst()) {
                result = (c.getInt(0) > 0);
            }
        } finally {
            c.close();
        }
        return result;
    }

    private int deleteByField(String field, String value) {
        return db.delete(DeviceDBHelper.TABLE_NAME, field + "=?", new String[]{value});
    }

    public int deleteByName(String name) {
        return deleteByField("name", name);
    }

    public List<DeviceDAO> listDB() {
        String sql = "SELECT name, ip, dataPort, messagePort, type FROM " + DeviceDBHelper.TABLE_NAME;

        final Cursor c = db.rawQuery(sql, new String[]{});
        List<DeviceDAO> deviceDAOs = new ArrayList<>();
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex("name"));
            String ip = c.getString(c.getColumnIndex("ip"));
            int dataPort = c.getInt(c.getColumnIndex("dataPort"));
            int messagePort = c.getInt(c.getColumnIndex("messagePort"));
            int type = c.getInt(c.getColumnIndex("type"));
            DeviceDAO device = new DeviceDAO(name, ip, dataPort, messagePort, Status.BoardType.values()[type]);
            deviceDAOs.add(device);
        }
        c.close();
        return deviceDAOs;
    }

    public void closeDB() {
        db.close();
    }

    public void dropTable() {
        String sql = "DROP TABLE " + DeviceDBHelper.TABLE_NAME;
        db.execSQL(sql);
    }
}
