package com.demo.bms.appdemo.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.demo.bms.appdemo.bean.DailyNews;
import com.demo.bms.appdemo.support.Constants;
import com.google.gson.GsonBuilder;

import java.util.List;

/**
 * Created by bms on 17-3-6.
 */

public final class DailyNewsDataSource {
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = {
            DBHelper.COLUMN_ID,
            DBHelper.COLUMN_DATE,
            DBHelper.COLUMN_CONTENT
    };

    public DailyNewsDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public List<DailyNews> insertDailyNewsList(String date, String context) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_DATE, date);
        values.put(DBHelper.COLUMN_CONTENT, context);

        long insertId = database.insert(DBHelper.TABLE_NAME, null, values);
        Cursor cursor = database.query(DBHelper.TABLE_NAME, allColumns,
                DBHelper.COLUMN_ID + " = " + insertId, null, null, null, null);
        cursor.moveToFirst();
        List<DailyNews> newsList = cursorToNewsList(cursor);
        cursor.close();
        return newsList;
    }

    public void updateNewsList(String date, String content) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_DATE, date);
        values.put(DBHelper.COLUMN_CONTENT, content);

    }

    public List<DailyNews> newsOfTheDay(String date) {
        Cursor cursor = database.query(DBHelper.TABLE_NAME,
                allColumns, DBHelper.COLUMN_DATE + " = " + date, null, null, null, null);

        cursor.moveToFirst();
        List<DailyNews> newsList = cursorToNewsList(cursor);
        cursor.close();
        return newsList;
    }






    private List<DailyNews> cursorToNewsList(Cursor cursor) {
        if (cursor != null && cursor.getCount() > 0) {
            return new GsonBuilder().create().fromJson(cursor.getString(2), Constants.Types.newsListType);
        } else {
            return null;
        }
    }

}
