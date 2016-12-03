package com.example.lenovo.inequalitysignserver.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lenovo.inequalitysignserver.entity.Account;

/**
 * Created by lenovo on 2016/11/30.
 */
public class DBAdapter {
    private static final String DB_NAME = "account.db";
    private static final String DB_TABLE = "accountinfo";
    private static final int DB_VERSION = 1;

    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "name";
    public static final String KEY_PWD = "pwd";

    private SQLiteDatabase db;
    private final Context context;
    private DBOpenHelper dbOpenHelper;

    public DBAdapter(Context context) {
        this.context = context;
    }

    public void close() {
        if (db != null) {
            db.close();
            db = null;
        }
    }
    public void open() throws SQLiteException {
        dbOpenHelper = new DBOpenHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db = dbOpenHelper.getWritableDatabase();
        }
        catch (SQLiteException ex) {
            db = dbOpenHelper.getReadableDatabase();
        }
    }

    public long insert(Account account) {
        ContentValues newValues = new ContentValues();

        newValues.put(KEY_NAME, account.Name);
        newValues.put(KEY_PWD, account.Pwd);

        return db.insert(DB_TABLE, null, newValues);
    }

    public Account[] queryOneData(String name) {
        Cursor results =  db.query(DB_TABLE, new String[] { KEY_ID, KEY_NAME, KEY_PWD},
                KEY_NAME + "=" + name, null, null, null, null);
        return ConvertToAccount(results);
    }

    private Account[] ConvertToAccount(Cursor cursor){
        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()){
            return null;
        }
        Account[] accounts = new Account[resultCounts];
        for (int i = 0 ; i<resultCounts; i++){
            accounts[i] = new Account();
            accounts[i].ID = cursor.getInt(0);
            accounts[i].Name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            accounts[i].Pwd = cursor.getString(cursor.getColumnIndex(KEY_PWD));

            cursor.moveToNext();
        }
        return accounts;
    }

    public long updateOneData(String name , Account account){
        ContentValues updateValues = new ContentValues();
        updateValues.put(KEY_NAME, account.Name);
        updateValues.put(KEY_PWD, account.Pwd);

        return db.update(DB_TABLE, updateValues,  KEY_NAME + "=" + name, null);
    }
    /**
     * 静态Helper类，用于建立、更新和打开数据库
     */
    private class DBOpenHelper extends SQLiteOpenHelper{
        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        private static final String DB_CREATE = "create table" + DB_TABLE + "("+ KEY_ID
                + " integer primary key autoincrement, " + KEY_NAME+ " text not null unique, "
                + KEY_PWD+ " text not null);";
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }

}
