package cn.yanweijia.slimming.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import cn.yanweijia.slimming.entity.User;

/**
 * Created by weijia on 29/09/2017.
 *
 * @author weijia
 */

public class RequestUtils {
    private static ObjectMapper objectMapper;
    private static final String TAG = "RequestUtils";
    public static final String BASE_URL = "http://server.yanweijia.cn:8080/slimming";
    public static final String URL_FOOD_IMAGE = BASE_URL + "/images/food/food$ID$.png"; //replace $ID$ to food id
    public static final String REPLACEMENT_FOODID = "$ID$";
    private static final String URL_LOGIN = BASE_URL + "/api/guest/login";   //post:  username=&password=
    private static final String URL_GET_USER_INFO = BASE_URL + "/api/user/getUserInfo"; //get:  ?id=1
    private static final String URL_REGISTER = BASE_URL + "/api/guest/register";
    private static final String URL_UPDATE_USER_INFO = BASE_URL + "/api/user/updateUserInfo"; //post with json
    private static final String URL_CHANGE_PASSWORD = BASE_URL + "/api/user/changePassword";
    private static final String URL_RECOMMEND_FOOD = BASE_URL + "/api/food/recommend"; //recommend food
    private static final String URL_GET_FOOD_BY_CATEGORY = BASE_URL + "/api/food/listFoodByCategory";// get food by category id.  ex: ?categoryId=1
    private static final String URL_GET_FOOD_BY_NAME = BASE_URL + "/api/food/listFoodByName"; //get food by food name, ex:  ?name=米饭  remember to encode id using
    private static final String URL_GET_FOOD_INFO_BY_ID = BASE_URL + "/api/food/getFoodInfo";//get food info by id ?foodId=1
    private static final String URL_GET_FOOD_MEASUREMENT = BASE_URL + "/api/food/listFoodMeasurementByFoodID";//get food measurements by food id. ex: ?foodId=100

    static {
        objectMapper = new ObjectMapper();
    }


    /**
     * get food measurements by food id;
     *
     * @param foodId
     * @return
     * @author weijia
     */
    public static String getFoodMeasurements(int foodId) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("foodId", String.valueOf(foodId));
            return HttpUtils.sendGet(URL_GET_FOOD_MEASUREMENT, params);
        } catch (Exception e) {
            Log.e(TAG, "getFoodMeasurements: ", e);
            return "{\"success\":false,\"message\":\"" + e.getMessage() + "\"}";
        }
    }


    /**
     * get food info by food id
     *
     * @param foodId
     * @return
     * @author weijia
     */
    public static String getFoodInfoById(int foodId) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("foodId", String.valueOf(foodId));
            return HttpUtils.sendGet(URL_GET_FOOD_INFO_BY_ID, params);
        } catch (Exception e) {
            Log.e(TAG, "getFoodInfoById: ", e);
            return "{\"success\":false,\"message\":\"" + e.getMessage() + "\"}";
        }
    }

    /**
     * get foods by food name
     *
     * @param name
     * @return
     */
    public static String getFoodByName(String name) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("name", name);
            return HttpUtils.sendGet(URL_GET_FOOD_BY_NAME, params);
        } catch (Exception e) {
            Log.e(TAG, "getFoodByName: ", e);
            return "{\"success\":false,\"message\":\"" + e.getMessage() + "\"}";
        }
    }


    /**
     * get foods by category
     *
     * @param categoryid
     * @return
     */
    public static String getFoodByCategory(int categoryid) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("categoryId", String.valueOf(categoryid));
            return HttpUtils.sendGet(URL_GET_FOOD_BY_CATEGORY, params);
        } catch (Exception e) {
            Log.e(TAG, "getFoodByCategory: ", e);
            return "{\"success\":false,\"message\":\"" + e.getMessage() + "\"}";
        }
    }

    /**
     * download food image by food id
     *
     * @param foodid
     * @return
     */
    public static Bitmap downloadFoodImage(int foodid) {
        String urlStr = URL_FOOD_IMAGE.replace("$ID$", "" + foodid);
        try {
            URL url = new URL(urlStr);
            Bitmap bitmap = BitmapFactory.decodeStream(url.openStream());
            return bitmap;
        } catch (Exception e) {
            Log.e(TAG, "downloadFoodImage: " + urlStr, e);
            return null;
        }
    }

    /**
     * recommend food
     *
     * @param userid user id
     * @return
     */
    public static String recommendFood(int userid) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("userid", String.valueOf(userid));
            return HttpUtils.sendGet(URL_RECOMMEND_FOOD, params);
        } catch (Exception e) {
            Log.e(TAG, "recommendFood: ", e);
            return "{\"success\":false,\"message\":\"" + e.getMessage() + "\"}";
        }
    }

    /**
     * change password
     *
     * @param userid
     * @param oldPw  old password (md5 encrypted)
     * @param newPw  new password (md5 encrypted)
     * @return
     */
    public static String changePassword(int userid, String oldPw, String newPw) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("id", String.valueOf(userid));
            params.put("oldPw", oldPw);
            params.put("newPw", newPw);
            return HttpUtils.sendPost(URL_CHANGE_PASSWORD, params);
        } catch (Exception e) {
            Log.e(TAG, "changePassword: ", e);
            return "{\"success\":false,\"message\":\"" + e.getMessage() + "\"}";
        }
    }

    /**
     * update user info <br/>
     * <p>
     * 记得请求的时候 contentType设置为:
     * <strong style='color:green'>
     * application/json;charset=UTF-8
     * </strong>
     * </p>
     *
     * @param user
     * @return
     */
    public static String updateUserInfo(User user) {
        try {
            return HttpUtils.sendPostWithJSON(URL_UPDATE_USER_INFO, objectMapper.writeValueAsString(user));
        } catch (JsonProcessingException e) {
            Log.e(TAG, "updateUserInfo: ", e);
            return "{\"success\":false,\"message\":\"" + e.getMessage() + "\"}";
        }
    }

    /**
     * register new user
     *
     * @param username
     * @param password
     * @return
     * @author weijia
     */
    public static String register(String username, String password) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);
            Log.d(TAG, "register: " + objectMapper.writeValueAsString(params));
            return HttpUtils.sendPost(URL_REGISTER, params);
        } catch (Exception e) {
            Log.e(TAG, "register: ", e);
            return "{\"success\":false,\"message\":\"" + e.getMessage() + "\"}";
        }
    }


    /**
     * get user info
     *
     * @param id user id
     * @return json string
     */
    public static String getUserInfo(int id) {
        Map<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(id));
        Log.d(TAG, "getUserInfo: id:" + id);
        return HttpUtils.sendGet(URL_GET_USER_INFO, params);
    }


    /**
     * login action
     *
     * @param username username
     * @param password origin password ,encrypted (MD5)
     * @return json string
     */
    public static String login(String username, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        Log.d(TAG, "login: username:" + username + " password(encrypted):" + password);
        return HttpUtils.sendPost(URL_LOGIN, params);
    }
}
