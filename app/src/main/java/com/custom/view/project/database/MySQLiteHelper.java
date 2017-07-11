package com.custom.view.project.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dsd on 2017/7/11.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CustomViewProject.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_NEWS = "create table news("
        + "id integer primary key autoincrement, " +
        "title text, " +
        "content text, " +
        "publishdate integer, " +
        "commentcount integer)";

    public MySQLiteHelper(Context context){
        this(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public MySQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);

    }


    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NEWS);
        // MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(context);
        // SQLiteDatabase sqLiteDatabase = mySQLiteHelper.getWritableDatabase();
    }


    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
