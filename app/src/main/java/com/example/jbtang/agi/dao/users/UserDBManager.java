package com.example.jbtang.agi.dao.users;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * manage api for user database
 * Created by jbtang on 10/2/2015.
 */
public class UserDBManager {
    private UserDBHelper helper;
    private SQLiteDatabase db;

    public UserDBManager(Context context) {
        helper = new UserDBHelper(context);
        db = helper.getWritableDatabase();
    }

    public void add(List<User> users) {
        db.beginTransaction();
        try {
            for (User user : users) {
                db.execSQL("INSERT INTO " + UserDBHelper.TABLE_NAME + " VALUES(?, ?)",
                        new Object[]{user.name, user.password});
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    private void updateByField(String field, String key, String value) {
        ContentValues cv = new ContentValues();
        cv.put(field, value);
        db.update(UserDBHelper.TABLE_NAME, cv, "name = ?", new String[]{key});
    }

    public void updateName(String from, String to) {
        updateByField("name", from, to);
    }

    public void updatePwd(String name, String newPwd) {
        updateByField("password", name, newPwd);
    }

    private int deleteByField(String field, String value) {
        return db.delete(UserDBHelper.TABLE_NAME, field + "=?", new String[]{value});
    }

    public int deleteByName(String name) {
        return deleteByField("name", name);
    }

    public String getPwdByName(String name) {
        Cursor c = db.rawQuery("SELECT password FROM " + UserDBHelper.TABLE_NAME + " where name = ? ", new String[]{name});
        String pwd = null;
        if (c.moveToNext()) {
            pwd = c.getString(c.getColumnIndex("password"));
        }
        c.close();
        return pwd;
    }

    public boolean isExistsByField(String field, String value) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ").append(UserDBHelper.TABLE_NAME).append(" WHERE ")
                .append(field).append(" =?");

        return isExistsBySQL(sql.toString(), new String[]{field, value});
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

    public List<User> listDB() {
        String sql = "SELECT name, password FROM " + UserDBHelper.TABLE_NAME;

        final Cursor c = db.rawQuery(sql, new String[]{});
        List<User> users = new ArrayList<>();
        while (c.moveToNext()) {
            String name = c.getString(c.getColumnIndex("name"));
            String pwd = c.getString(c.getColumnIndex("password"));
            User user = new User(name, pwd);
            users.add(user);
        }
        c.close();
        return users;
    }

    public void closeDB() {
        db.close();
    }

    public void dropTable() {
        String sql = "DROP TABLE " + UserDBHelper.TABLE_NAME;
        db.execSQL(sql);
    }

    public void createTable() {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + UserDBHelper.TABLE_NAME +
                "(name TEXT PRIMARY KEY, password TEXT)");
    }
}
