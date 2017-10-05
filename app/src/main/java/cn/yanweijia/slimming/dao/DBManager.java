package cn.yanweijia.slimming.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.yanweijia.slimming.entity.FoodCategory;
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
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.CAMEL_CASE_TO_LOWER_CASE_WITH_UNDERSCORES);
        }
    }

    /**
     * close db conncection
     */
    public static void closeSQLiteDB() {
        if (db != null) {
            db.close();
            db = null;
        }
        if (dbHelper != null) {
            dbHelper.close();
            dbHelper = null;
        }

    }

    /**
     * Query user from db
     *
     * @return
     */
    public static User getUser() {
        //queryUser,check if exist error
        try {
            Cursor cursor = db.rawQuery(DBSentence.GET_USER, null);
            JSONObject jsonObject = new JSONObject();
            User user = null;
            if (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    jsonObject.put(cursor.getColumnName(i), cursor.getString(i));
                }
                user = objectMapper.readValue(jsonObject.toString(), User.class);
            }
            cursor.close();
            return user;
        } catch (Exception e) {
            Log.e(TAG, "getUser: Error:", e);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Clean user table, then save user to db
     * <p>you should call init() before calling this function</p>
     *
     * @param user
     * @return
     */
    public static boolean saveUser(User user) {
        if (user == null)
            return false;
        try {
            removeAllUser();
            db.execSQL(DBSentence.SAVE_USER,
                    new Object[]{user.getId(),
                            user.getUsername(),
                            user.getPassword(),
                            user.getPhone(),
                            user.getEmail(),
                            user.getName(),
                            user.getBirthday() == null ? null : new SimpleDateFormat("yyyy-MM-dd").format(user.getBirthday()),
                            user.getGender(),
                            user.getHeight(),
                            user.getWeight(),
                            user.getStatus(),
                            user.getRegTime() == null ? null : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(user.getRegTime()),
                            user.getRegIp(),
                            user.getLastLogin() == null ? null : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(user.getLastLogin())});
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

    /**
     * remove all users
     */
    public static void removeAllUser() {
        db.execSQL(DBSentence.CLEAN_TABLE_USER);
    }

    /**
     * query food category.
     * @return
     */
    public static List<FoodCategory> getFoodCategory(){
        List<FoodCategory> list = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery(DBSentence.GET_FOOD_CATEGORY, null);
            JSONObject jsonObject = new JSONObject();
            FoodCategory foodCategory = null;
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    jsonObject.put(cursor.getColumnName(i), cursor.getString(i));
                }
                foodCategory = objectMapper.readValue(jsonObject.toString(), FoodCategory.class);
                list.add(foodCategory);
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "getFoodCategory: Error:", e);
            e.printStackTrace();
        }
        return list;
    }
}
