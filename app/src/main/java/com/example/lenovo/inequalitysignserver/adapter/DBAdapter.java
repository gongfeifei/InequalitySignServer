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
    public static final String KEY_UNAME = "shop_id";
    public static final String KEY_PWD = "shop_pwd";
    public static final String KEY_SIMG = "shop_img_small";
    public static final String KEY_BIMG = "shop_img_big";
    public static final String KEY_NAME = "shop_name";
    public static final String KEY_TYPE = "shop_type";
    public static final String KEY_ADDRESS = "shop_address";
    public static final String KEY_TEL = "shop_tel";
    public static final String KEY_CITY = "shop_city";
    public static final String KEY_DESCRI = "shop_description";

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

        newValues.put(KEY_UNAME, account.shop_id);
        newValues.put(KEY_PWD, account.shop_pwd);
        newValues.put(KEY_SIMG, account.shop_img_small);
        newValues.put(KEY_BIMG, account.shop_img_big);
        newValues.put(KEY_NAME, account.shop_name);
        newValues.put(KEY_TYPE, account.shop_type);
        newValues.put(KEY_ADDRESS, account.shop_address);
        newValues.put(KEY_TEL, account.shop_tel);
        newValues.put(KEY_CITY, account.shop_city);
        newValues.put(KEY_DESCRI, account.shop_description);

        return db.insert(DB_TABLE, null, newValues);
    }

    public Account[] queryOneData(String name) {
        Cursor results =  db.query(DB_TABLE, new String[] { KEY_ID, KEY_UNAME, KEY_PWD,
                KEY_SIMG, KEY_BIMG, KEY_NAME, KEY_TYPE, KEY_ADDRESS, KEY_TEL, KEY_CITY,
                KEY_DESCRI}, KEY_UNAME + "=" + name, null, null, null, null);
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
            accounts[i].id = cursor.getInt(0);
            accounts[i].shop_id = cursor.getString(cursor.getColumnIndex(KEY_UNAME));
            accounts[i].shop_pwd = cursor.getString(cursor.getColumnIndex(KEY_PWD));
            accounts[i].shop_img_small = cursor.getBlob(cursor.getColumnIndex(KEY_SIMG));
            accounts[i].shop_img_big = cursor.getString(cursor.getColumnIndex(KEY_BIMG));
            accounts[i].shop_name = cursor.getString(cursor.getColumnIndex(KEY_NAME));
            accounts[i].shop_type = cursor.getString(cursor.getColumnIndex(KEY_TYPE));
            accounts[i].shop_address = cursor.getString(cursor.getColumnIndex(KEY_ADDRESS));
            accounts[i].shop_tel = cursor.getString(cursor.getColumnIndex(KEY_TEL));
            accounts[i].shop_city = cursor.getString(cursor.getColumnIndex(KEY_CITY));
            accounts[i].shop_description = cursor.getString(cursor.getColumnIndex(KEY_DESCRI));

            cursor.moveToNext();
        }
        return accounts;
    }

    public long updateOneData(String name , Account account){
        ContentValues updateValues = new ContentValues();
        updateValues.put(KEY_UNAME, account.shop_id);
        updateValues.put(KEY_PWD, account.shop_pwd);
        updateValues.put(KEY_SIMG, account.shop_img_small);
        updateValues.put(KEY_BIMG, account.shop_img_big);
        updateValues.put(KEY_NAME, account.shop_name);
        updateValues.put(KEY_TYPE, account.shop_type);
        updateValues.put(KEY_ADDRESS, account.shop_address);
        updateValues.put(KEY_TEL, account.shop_tel);
        updateValues.put(KEY_CITY, account.shop_city);
        updateValues.put(KEY_DESCRI, account.shop_description);

        return db.update(DB_TABLE, updateValues,  KEY_UNAME + "=" + name, null);
    }
    /**
     * 静态Helper类，用于建立、更新和打开数据库
     */
    private class DBOpenHelper extends SQLiteOpenHelper{
        public DBOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        private static final String DB_CREATE = "create table " + DB_TABLE + " ("+ KEY_ID
                + " integer primary key autoincrement, " + KEY_UNAME + " text not null, "
                + KEY_PWD + " text not null, " + KEY_SIMG + " text not null, " + KEY_BIMG
                + " text not null, " + KEY_NAME + " text not null, " + KEY_TYPE
                + " text, " + KEY_ADDRESS + " text not null, " + KEY_TEL
                + " text not null, " + KEY_CITY + " text not null, " + KEY_DESCRI
                + " text not null);";
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
