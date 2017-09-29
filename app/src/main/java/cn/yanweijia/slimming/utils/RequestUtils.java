package cn.yanweijia.slimming.utils;

import android.util.Log;

import com.blankj.utilcode.util.EncodeUtils;
import com.blankj.utilcode.util.EncryptUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by weijia on 29/09/2017.
 *
 * @author weijia
 */

public class RequestUtils {
    private static final String TAG = "RequestUtils";
    private static final String BASE_URL = "http://server.yanweijia.cn:8080";
    private static final String URL_LOGIN = BASE_URL + "/slimming/api/guest/login";

    //TODO: HttpClient 需要使用同一个HttpClient

    /**
     * login action
     *
     * @param username username
     * @param password origin password ,not encrypted
     * @return
     */
    public static String login(String username, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", EncryptUtils.encryptMD5ToString(password));
        Log.d(TAG, "login: username:" + username + " password(encrypted):" + EncryptUtils.encryptMD5ToString(password));
        return HttpUtils.sendPost(URL_LOGIN, params);
    }
}
