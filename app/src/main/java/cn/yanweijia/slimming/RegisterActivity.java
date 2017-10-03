package cn.yanweijia.slimming;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;

import org.json.JSONObject;

import cn.yanweijia.slimming.utils.RequestUtils;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    /**
     * register success
     */
    public static final int REGISTER_SUCCESS = 1;
    /**
     * register fail
     */
    public static final int REGISTER_FAIL = 2;

    private String username;
    private String password;    //encrypted (MD5)
    private EditText editText_username, editText_password, editText_repassword;
    private Button btn_register;
    private RegisterActivityHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        myHandler = new RegisterActivityHandler();

        //bind views
        editText_username = (EditText) findViewById(R.id.edittext_register_username);
        editText_password = (EditText) findViewById(R.id.edittext_register_password);
        editText_repassword = (EditText) findViewById(R.id.edittext_register_repassword);
        btn_register = (Button) findViewById(R.id.btn_register);


        //register action
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = editText_username.getText().toString();
                password = editText_password.getText().toString();
                String repassword = editText_repassword.getText().toString();
                if (!RegexUtils.isUsername(username)) {
                    Toast.makeText(RegisterActivity.this, R.string.illegal_username, Toast.LENGTH_SHORT).show();
                    editText_username.requestFocus();
                    return;
                }
                if (!isPasswordValid(password)) {
                    Toast.makeText(RegisterActivity.this, R.string.illegal_password, Toast.LENGTH_SHORT).show();
                    editText_password.requestFocus();
                    return;
                }
                if (!StringUtils.equals(password, repassword)) {
                    Toast.makeText(RegisterActivity.this, R.string.password_not_same, Toast.LENGTH_SHORT).show();
                    editText_repassword.requestFocus();
                    return;
                }
                password = EncryptUtils.encryptMD5ToString(password);
                register();
            }
        });

    }

    /**
     * register new account
     */
    private void register() {
        //register
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonResult = RequestUtils.register(username, password);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                try {
                    JSONObject jsonObject = new JSONObject(jsonResult);
                    msg.what = jsonObject.getBoolean("success") ? REGISTER_SUCCESS : REGISTER_FAIL;
                    bundle.putString("message", jsonObject.getString("message"));
                } catch (Exception e) {
                    msg.what = REGISTER_FAIL;
                    bundle.putString("message", e.getMessage());
                    Log.e(TAG, "run: ", e);
                }
                msg.setData(bundle);
                myHandler.sendMessage(msg);
            }
        }).start();
    }

    /**
     * valid password
     *
     * @param password
     * @return
     * @author weijia
     */
    private boolean isPasswordValid(String password) {
        return password.length() >= 6 && password.length() <= 20;
    }


    //customer Handler,handle asynchronous message..
    class RegisterActivityHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REGISTER_SUCCESS:
                    Toast.makeText(RegisterActivity.this, R.string.register_success, Toast.LENGTH_SHORT).show();
                    //set result and finish this activity,then login
                    Intent intent = new Intent();
                    intent.putExtra("username", username);
                    intent.putExtra("password", password);
                    setResult(REGISTER_SUCCESS, intent);
                    Log.d(TAG, "handleMessage: register success,username:" + username + " password:" + password);
                    finish();
                    break;
                case REGISTER_FAIL:
                    String message = msg.getData().getString("message");
                    Toast.makeText(RegisterActivity.this, getString(R.string.register_fail) + message, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "handleMessage: register fail,msg:" + message);
                    setResult(REGISTER_FAIL);
                    break;
                default:
                    setResult(REGISTER_FAIL);
                    Log.d(TAG, "handleMessage: mysterious msg.what");
            }
        }
    }
}
