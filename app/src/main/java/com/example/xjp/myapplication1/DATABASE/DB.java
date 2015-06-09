package com.example.xjp.myapplication1.DATABASE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by XJP on 15/6/9.
 */
public class DB extends SQLiteOpenHelper {
    public DB(Context context) {
        super(context, "userdb", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE userfavorite(_id INTEGER PRIMARY KEY AUTOINCREMENT,username TEXT,star TEXT," +
                "ScenicName TEXT,ScenicPrice TEXT,ScenicAddress TEXT,ScenicClass Text,date TEXT)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
    }
}
