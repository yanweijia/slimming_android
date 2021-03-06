package cn.yanweijia.slimming.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import cn.yanweijia.slimming.entity.BloodGlucose;
import cn.yanweijia.slimming.entity.BloodPressure;
import cn.yanweijia.slimming.entity.FoodCategory;
import cn.yanweijia.slimming.entity.HeartRate;
import cn.yanweijia.slimming.entity.User;
import cn.yanweijia.slimming.entity.UserWeight;

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
     * query food category
     *
     * @param categoryid
     * @return
     */
    public static FoodCategory getFoodCategory(int categoryid) {
        try {
            Cursor cursor = db.rawQuery(DBSentence.GET_FOOD_CATEGORY.replace(DBSentence.REPLACEMENT_FOODCATEGORY_ID, String.valueOf(categoryid)), null);
            JSONObject jsonObject = new JSONObject();
            if (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    jsonObject.put(cursor.getColumnName(i), cursor.getString(i));
                }
                FoodCategory foodCategory = objectMapper.readValue(jsonObject.toString(), FoodCategory.class);
                cursor.close();
                return foodCategory;
            } else {
                Log.d(TAG, "getFoodCategory: cannot find anything");
                return null;
            }
        } catch (Exception e) {
            Log.e(TAG, "getFoodCategory: ", e);
            return null;
        }
    }

    /**
     * list food categorys.
     *
     * @return
     */
    public static List<FoodCategory> listFoodCategories() {
        List<FoodCategory> list = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery(DBSentence.LIST_FOOD_CATEGORY, null);
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
            Log.e(TAG, "listFoodCategories: Error:", e);
            e.printStackTrace();
        }
        return list;
    }

    /**
     * save user weight data
     *
     * @param list
     * @return success flag
     */
    public static boolean saveUserWeight(List<UserWeight> list) {
        if (list == null || list.size() == 0)
            return false;
        try {
            for (int i = 0; i < list.size(); i++) {
                UserWeight userWeight = list.get(i);
                db.execSQL(DBSentence.SAVE_USER_WEIGHT,
                        new Object[]{userWeight.getId(),
                                userWeight.getUserId(),
                                userWeight.getWeight(),
                                userWeight.getTime() == null ? null : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(userWeight.getTime()),
                                userWeight.getMethod()});
            }
        } catch (Exception e) {
            try {
                Log.e(TAG, "saveUserWeight: Error:" + e.getMessage() + " User Bean:" + objectMapper.writeValueAsString(list), e);
            } catch (JsonProcessingException e1) {
                Log.e(TAG, "saveUserWeight: ", e1);
            }
            return false;
        }
        return true;
    }

    /**
     * save heart rate data
     *
     * @param list
     * @return success flag
     */
    public static boolean saveHeartRate(List<HeartRate> list) {
        if (list == null || list.size() == 0)
            return false;
        try {
            for (int i = 0; i < list.size(); i++) {
                HeartRate heartRate = list.get(i);
                db.execSQL(DBSentence.SAVE_HEART_RATE,
                        new Object[]{heartRate.getId(),
                                heartRate.getUserId(),
                                heartRate.getRate(),
                                heartRate.getTime() == null ? null : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(heartRate.getTime()),
                                heartRate.getMethod()});
            }
        } catch (Exception e) {
            try {
                Log.e(TAG, "saveHeartRate: Error:" + e.getMessage() + " User Bean:" + objectMapper.writeValueAsString(list), e);
            } catch (JsonProcessingException e1) {
                Log.e(TAG, "saveHeartRate: ", e1);
            }
            return false;
        }
        return true;
    }


    /**
     * save blood pressure data
     *
     * @param list
     * @return success flag
     */
    public static boolean saveBloodPressure(List<BloodPressure> list) {
        if (list == null || list.size() == 0)
            return false;
        try {
            for (int i = 0; i < list.size(); i++) {
                BloodPressure bloodPressure = list.get(i);
                db.execSQL(DBSentence.SAVE_BLOOD_PRESSURE,
                        new Object[]{bloodPressure.getId(),
                                bloodPressure.getUserId(),
                                bloodPressure.getDiastolicPressure(),
                                bloodPressure.getSystolicPressure(),
                                bloodPressure.getTime() == null ? null : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(bloodPressure.getTime()),
                                bloodPressure.getMethod()});
            }
        } catch (Exception e) {
            try {
                Log.e(TAG, "saveBloodPressure: Error:" + e.getMessage() + " User Bean:" + objectMapper.writeValueAsString(list), e);
            } catch (JsonProcessingException e1) {
                Log.e(TAG, "saveBloodPressure: ", e1);
            }
            return false;
        }
        return true;
    }

    /**
     * save blood glucose data
     *
     * @param list
     * @return success flag
     */
    public static boolean saveBloodGlucose(List<BloodGlucose> list) {
        if (list == null || list.size() == 0)
            return false;
        try {
            for (int i = 0; i < list.size(); i++) {
                BloodGlucose bloodGlucose = list.get(i);
                db.execSQL(DBSentence.SAVE_BLOOD_GLUCOSE,
                        new Object[]{bloodGlucose.getId(),
                                bloodGlucose.getUserId(),
                                bloodGlucose.getGlucose(),
                                bloodGlucose.getTime() == null ? null : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(bloodGlucose.getTime()),
                                bloodGlucose.getMethod()});
            }
        } catch (Exception e) {
            try {
                Log.e(TAG, "saveBloodGlucose: Error:" + e.getMessage() + " User Bean:" + objectMapper.writeValueAsString(list), e);
            } catch (JsonProcessingException e1) {
                Log.e(TAG, "saveBloodGlucose: ", e1);
            }
            return false;
        }
        return true;
    }

    /**
     * list all blood glucose data
     *
     * @return
     */
    public static List<BloodGlucose> listBloodGlucose() {
        List<BloodGlucose> list = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery(DBSentence.LIST_BLOOD_GLUCOSE, null);
            JSONObject json = new JSONObject();
            BloodGlucose bloodGlucose = null;
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    json.put(cursor.getColumnName(i), cursor.getString(i));
                    ;
                }
                bloodGlucose = objectMapper.readValue(json.toString(), BloodGlucose.class);
                list.add(bloodGlucose);
            }
        } catch (Exception e) {
            Log.e(TAG, "listBloodGlucose: Error:", e);
        }
        return list;
    }

    /**
     * list all blood pressure data
     *
     * @return
     */
    public static List<BloodPressure> listBloodPressure() {
        List<BloodPressure> list = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery(DBSentence.LIST_BLOOD_PRESSURE, null);
            JSONObject json = new JSONObject();
            BloodPressure bloodPressure = null;
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    json.put(cursor.getColumnName(i), cursor.getString(i));
                    ;
                }
                bloodPressure = objectMapper.readValue(json.toString(), BloodPressure.class);
                list.add(bloodPressure);
            }
        } catch (Exception e) {
            Log.e(TAG, "listBloodPressure: Error:", e);
        }
        return list;
    }

    /**
     * list all heart rate data
     *
     * @return
     */
    public static List<HeartRate> listHeartRate() {
        List<HeartRate> list = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery(DBSentence.LIST_HEART_RATE, null);
            JSONObject json = new JSONObject();
            HeartRate heartRate = null;
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    json.put(cursor.getColumnName(i), cursor.getString(i));
                    ;
                }
                heartRate = objectMapper.readValue(json.toString(), HeartRate.class);
                list.add(heartRate);
            }
        } catch (Exception e) {
            Log.e(TAG, "listHeartRate: Error:", e);
        }
        return list;
    }

    /**
     * list all user weight data
     *
     * @return
     */
    public static List<UserWeight> listWeight() {
        List<UserWeight> list = new ArrayList<>();
        try {
            Cursor cursor = db.rawQuery(DBSentence.LIST_USER_WEIGHT, null);
            JSONObject json = new JSONObject();
            UserWeight userWeight = null;
            while (cursor.moveToNext()) {
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    json.put(cursor.getColumnName(i), cursor.getString(i));
                    ;
                }
                userWeight = objectMapper.readValue(json.toString(), UserWeight.class);
                list.add(userWeight);
            }
        } catch (Exception e) {
            Log.e(TAG, "listWeight: Error:", e);
        }
        return list;
    }
}
