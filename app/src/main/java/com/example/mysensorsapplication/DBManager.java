package com.example.mysensorsapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper  {

    private static final String dbname = "SensorInfo.db";
    public DBManager(Context context) {
        super(context,dbname,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String querry = "CREATE TABLE " + "Wifi_table" + " (" +
                "id" + " INTEGER PRIMARY KEY," +
                "wifi_name" + " TEXT," + "created_at TEXT)";
        sqLiteDatabase.execSQL(querry);
        String gpsquerry = "CREATE TABLE " + "gps_table" + " (" +
                "id" + " INTEGER PRIMARY KEY," +
                "location" + " TEXT," + "created_at TEXT)";
        sqLiteDatabase.execSQL(gpsquerry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Wifi_table");
        onCreate(sqLiteDatabase);
    }

    public String addWifiRecord(String record,String time_stamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("wifi_name",record);
        cv.put("created_at",time_stamp);

        long res = db.insert("Wifi_table",null,cv);

        if(res == -1)
            return "failed";
        else
            return "successful insertion";
    }

    public String addGpsRecord(String record,String time_stamp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("location",record);
        cv.put("created_at",time_stamp);

        long res = db.insert("gps_table",null,cv);

        if(res == -1)
            return "failed";
        else
            return "successful insertion";
    }

    public Cursor getWifiData() {
         SQLiteDatabase database = this.getWritableDatabase();
         Cursor cursor = database.rawQuery("select * from Wifi_table",null);
         return cursor;
    }

    public Cursor getGpsData() {
        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("select * from gps_table",null);
        return cursor;
    }
}
