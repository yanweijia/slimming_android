package cn.yanweijia.slimming.utils;

import android.util.Log;

import com.blankj.utilcode.util.EncodeUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by weijia on 28/09/2017.
 *
 * @author weijia
 */
public class HttpUtils {

    private static final String TAG = "HttpUtils";

    //httpClient
    private static HttpClient httpClient;

    static {
        httpClient = new DefaultHttpClient();
    }


    /**
     * send post request, Content-Type:application/json;charset=UTF-8 <br/>
     *
     * @param url  request url
     * @param json json string
     * @return
     * @author weijia
     */
    public static synchronized String sendPostWithJSON(String url, String json) {
        HttpPost httpPost = new HttpPost(url);
        httpPost.setHeader("Content-Type", "application/json;charset=UTF-8");
        try {
            StringEntity stringEntity = new StringEntity(json, "UTF-8");
            stringEntity.setContentEncoding("UTF-8");
            stringEntity.setContentType("application/json;charset=UTF-8");  // important !!!
            httpPost.setEntity(stringEntity);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "sendPostWithJSON: Error,json=" + json, e);
            return "{\"success\":false,\"message\":\"请求异常:" + e.getMessage() + "\"}";
        }
        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
        } catch (Exception e) {
            Log.e(TAG, "sendPostWithJSON: ", e);
            return "{\"success\":false,\"message\":\"请求异常:" + e.getMessage() + "\"}";
        }
        String temp;
        try {
            HttpEntity entity = response.getEntity();
            temp = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            Log.e(TAG, "sendPostWithJSON: ", e);
            return "{\"success\":false,\"message\":\"请求异常:" + e.getMessage() + "\"}";
        }
        Log.d(TAG, "sendPostWithJSON: url:" + url + " ,jsonBody:" + json + " ,content:" + temp);
        return temp;
    }

    /**
     * send Post request
     *
     * @param url    url
     * @param params params,Nullable
     * @return result html content
     */
    public static synchronized String sendPost(String url, Map<String, String> params) {

        // get method
        HttpPost httpPost = new HttpPost(url);

        // set header
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");

        //set postParams
        List<NameValuePair> postParams = new ArrayList<>();
        for (Map.Entry<String, String> set : params.entrySet()) {
            postParams.add(new BasicNameValuePair(set.getKey(), set.getValue()));
        }
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(postParams));
        } catch (Exception e) {
            Log.e(TAG, "sendPost: ", e);
            return "{\"success\":false,\"message\":\"请求异常:" + e.getMessage() + "\"}";
        }

        //response
        HttpResponse response;
        try {
            response = httpClient.execute(httpPost);
        } catch (Exception e) {
            Log.e(TAG, "sendPost: ", e);
            return "{\"success\":false,\"message\":\"请求异常:" + e.getMessage() + "\"}";
        }

        //get response into String
        String temp;
        try {
            HttpEntity entity = response.getEntity();
            temp = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            Log.e(TAG, "sendPost: ", e);
            return "{\"success\":false,\"message\":\"请求异常:" + e.getMessage() + "\"}";
        }
        Log.d(TAG, "sendPost: url:" + url + " ,content: " + temp);
        return temp;
    }


    /**
     * send get request
     *
     * @param url    url
     * @param params params,Nullable
     * @return result html content
     * @author weijia
     */
    public static synchronized String sendGet(String url, Map<String, String> params) {

        StringBuilder params_str = new StringBuilder("");

        if (params != null) {
            for (Map.Entry<String, String> set : params.entrySet()) {
                params_str.append("&").append(EncodeUtils.urlEncode(set.getKey(), "UTF-8")).append("=").append(EncodeUtils.urlEncode(set.getValue(), "UTF-8"));
            }
        }

        if (!url.contains("?"))
            url += "?" + params_str.substring(1);
        else
            url += params_str;

        // get method
        HttpGet httpGet = new HttpGet(url);

        //response
        HttpResponse response;
        try {
            response = httpClient.execute(httpGet);
        } catch (Exception e) {
            Log.e(TAG, "sendGet: " + e.getMessage(), e);
            return "{\"success\":false,\"message\":\"请求异常:" + e.getMessage() + "\"}";
        }

        //get response into String
        String temp;
        try {
            HttpEntity entity = response.getEntity();
            temp = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e) {
            Log.e(TAG, "sendGet: " + e.getMessage(), e);
            return "{\"success\":false,\"message\":\"请求异常:" + e.getMessage() + "\"}";
        }
        Log.d(TAG, "sendGet: " + url + " content: " + temp);
        return temp;
    }
}
