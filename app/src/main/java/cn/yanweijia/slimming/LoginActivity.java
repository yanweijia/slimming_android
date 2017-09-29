package cn.yanweijia.slimming;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.json.JSONStringer;

import java.util.Map;

import cn.yanweijia.slimming.utils.HttpUtils;
import cn.yanweijia.slimming.utils.RequestUtils;


/**
 * @author weijia
 * @date 2017.09.27
 * login
 */
public class LoginActivity extends Activity {

    /**
     * login success
     */
    private static final int LOGIN_SUCCESS = 1;
    /**
     * login fail
     */
    private static final int LOGIN_FAIL = 2;


    private static final String TAG = "LoginActivity";


    private EditText editText_username, editText_password;
    private String username;
    private String password;    //encrypted(MD5)
    private MyHandler myHandler;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gson = new Gson();
        myHandler = new MyHandler();

        //bind views
        Button btn_signin = (Button) findViewById(R.id.sign_in_button);
        Button btn_signon = (Button) findViewById(R.id.sign_on_button);
        editText_password = (EditText) findViewById(R.id.textview_login_password);
        editText_username = (EditText) findViewById(R.id.textview_login_username);


        //sign in action
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = editText_username.getText().toString();
                password = editText_password.getText().toString();
                if (!isUserNameValid(username) || !RegexUtils.isUsername(username)) {
                    //check username
                    Toast.makeText(LoginActivity.this, R.string.illegal_username, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isPasswordValid(password)) {
                    //check password
                    Toast.makeText(LoginActivity.this, R.string.illegal_password, Toast.LENGTH_SHORT).show();
                    return;
                }
                password = EncryptUtils.encryptMD5ToString(password);
                login();
            }
        });

        //register
        btn_signon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                //finish();
            }
        });
        Log.d(TAG, "onCreate: Complete");
        autoLogin();
    }

    /**
     * @param username String
     * @return valid or not
     * @author weijia
     */
    private boolean isUserNameValid(String username) {
        return !(username.contains(" ") || username.contains("&"));
    }

    /**
     * @param password String
     * @return valid or not
     * @author weijia
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    /**
     * if username/password exists in sqlite DB,try to auto login <br/>
     * @author weijia
     */
    private void autoLogin(){
        //TODO:check sqlite DB,and auto login
    }

    /**
     * login Thread <br/>
     * date 2017.09.29
     *
     * @author weijia
     */
    private void login() {
        //login
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonResult = RequestUtils.login(username, password);
                Map<String, Object> map = gson.fromJson(jsonResult, Map.class);
                Message msg = new Message();
                msg.what = ((boolean) map.get("success") == true) ? LOGIN_SUCCESS : LOGIN_FAIL;
                Bundle bundle = new Bundle();
                bundle.putString("message", String.valueOf(map.get("message")));
                msg.setData(bundle);
                myHandler.sendMessage(msg);
            }
        }).start();
    }

    //customer Handler,handle asynchronous message..
    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOGIN_SUCCESS:
                    Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "handleMessage: LOGIN_SUCCESS " + msg.getData().getString("message"));
                    //open main frame
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    //TODO: save username and password
                    finish();
                    break;
                case LOGIN_FAIL:
                    Toast.makeText(LoginActivity.this, getString(R.string.login_fail) + msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "handleMessage: LOGIN_FAIL " + msg.getData().getString("message"));
                    break;
                default:
                    Log.d(TAG, "handleMessage: mysterious msg.what");
            }
        }
    }
}

