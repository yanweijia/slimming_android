package cn.yanweijia.slimming.utils;

import android.util.Log;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by weijia on 29/09/2017.
 *
 * @author weijia
 */

public class RequestUtils {
    private static final String TAG = "RequestUtils";
    private static final String BASE_URL = "http://server.yanweijia.cn:8080/slimming";
    private static final String URL_LOGIN = BASE_URL + "/api/guest/login";   //post:  username=&password=
    private static final String URL_GET_USER_INFO = BASE_URL + "/api/user/getUserInfo"; //get:  ?id=1
    private static final String URL_REGISTER = BASE_URL + "/api/guest/register";

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
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
