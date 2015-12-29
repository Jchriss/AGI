package com.example.jbtang.agi.dao.configuration;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jbtang on 11/5/2015.
 */
public class ConfigurationDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "configuration";

    public ConfigurationDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(name TEXT PRIMARY KEY, triggerType INTEGER, triggerInterval INTEGER, filterInterval INTEGER," +
                " silenceCheckTimer INTEGER, receivingAntennaNum INTEGER, totalTriggerCount INTEGER, targetPhoneNum TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //TODO
    }
}
