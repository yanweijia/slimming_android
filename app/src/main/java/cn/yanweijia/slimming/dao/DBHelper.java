package cn.yanweijia.slimming.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by weijia on 30/09/2017.
 *
 * @author weijia
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //create database for the first time.
        db.execSQL(DBSentence.CREATE_TABLE_USER);
        db.execSQL(DBSentence.CREATE_TABLE_FOOD_CATEGORY);
        db.execSQL(DBSentence.INIT_FOOD_CATEGORY);
        db.execSQL(DBSentence.CREATE_TABLE_USER_WEIGHT);
        db.execSQL(DBSentence.CREATE_TABLE_HEART_RATE);
        db.execSQL(DBSentence.CREATE_TABLE_BLOOD_PRESSURE);
        db.execSQL(DBSentence.CREATE_TABLE_BLOOD_GLUCOSE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
