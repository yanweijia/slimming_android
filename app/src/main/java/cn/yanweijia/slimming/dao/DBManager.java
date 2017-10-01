package cn.yanweijia.slimming.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.yanweijia.slimming.entity.User;

/**
 * Created by weijia on 30/09/2017.
 *
 * @author weijia
 */

public class DBManager {

    private static final String TAG = "DBManager";

    private static DBHelper dbHelper;
    private static SQLiteDatabase db;
    private static ObjectMapper objectMapper;

    /**
     * initial db connection
     * <p>call this function before calling others.</p>
     *
     * @param context
     */
    public static void initSQLiteDB(Context context) {
        if (dbHelper == null)
            dbHelper = new DBHelper(context, DBSentence.DB_NAME, null, DBSentence.DB_VERSION);
        if (db == null)
            db = dbHelper.getWritableDatabase();
        if (objectMapper == null)
            objectMapper = new ObjectMapper();
    }

    /**
     * close db conncection
     */
    public static void closeSQLiteDB() {
        if (db != null)
            db.close();
        if (dbHelper != null)
            dbHelper.close();
    }

    /**
     * Query user from db
     *
     * @return
     */
    public static User getUser() {
        //TODO: queryUser
        Cursor cursor = db.rawQuery(DBSentence.GET_USER, null);
        if (cursor.moveToNext()) {
            JSONObject jsonObject = new JSONObject();
            for (int i = 0; i < cursor.getColumnCount(); i++) {
                try {
                    jsonObject.put(cursor.getColumnName(i), cursor.getString(i));
                } catch (JSONException e) {
                    Log.e(TAG, "getUser: Error:" + jsonObject.toString(), e);
                    e.printStackTrace();
                    return null;
                }
            }
            User user = null;
            try {
                user = objectMapper.readValue(jsonObject.toString(),User.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return user;

        } else
            return null;
    }

    /**
     * Clean user table, then save user to db
     * <p>you should call init() before calling this function</p>
     *
     * @param user
     * @return
     */
    public static boolean saveUser(User user) {
        try {
            db.execSQL(DBSentence.CLEAN_TABLE_USER);
            db.execSQL(DBSentence.SAVE_USER,
                    new Object[]{user.getId(),
                            user.getUsername(),
                            user.getPassword(),
                            user.getPhone(),
                            user.getEmail(),
                            user.getName(),
                            user.getBirthday(),
                            user.getGender(),
                            user.getHeight(),
                            user.getWeight(),
                            user.getStatus(),
                            user.getRegTime(),
                            user.getRegIp(),
                            user.getLastLogin()});
        } catch (Exception e) {
            try {
                Log.e(TAG, "saveUser: Error:" + e.getMessage() + " User Bean:" + objectMapper.writeValueAsString(user), e);
            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
            }
            return false;
        }
        return true;
    }
}
