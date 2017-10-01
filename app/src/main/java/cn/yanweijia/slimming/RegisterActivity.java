package cn.yanweijia.slimming;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class RegisterActivity extends AppCompatActivity {
    private static final String TAG = "RegisterActivity";
    /**
     * register success
     */
    private static final int REGISTER_SUCCESS = 1;
    /**
     * register fail
     */
    private static final int REGISTER_FAIL = 2;

    private String username;
    private String password;    //encrypted (MD5)
    private EditText editText_username,editText_password;

    private RegisterActivityHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    //customer Handler,handle asynchronous message..
    class RegisterActivityHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REGISTER_SUCCESS:
                    break;
                case REGISTER_FAIL:
                    break;
                default:
                    Log.d(TAG, "handleMessage: mysterious msg.what");
            }
        }
    }
}
