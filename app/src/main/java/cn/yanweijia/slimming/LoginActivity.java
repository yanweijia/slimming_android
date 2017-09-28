package cn.yanweijia.slimming;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * @author weijia
 * @date 2017.09.27
 * login
 */
public class LoginActivity extends Activity {
    private static final String TAG = "LoginActivity";
    private EditText editText_username, editText_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //bind views
        Button btn_signin = (Button) findViewById(R.id.sign_in_button);
        Button btn_signon = (Button) findViewById(R.id.sign_on_button);
        editText_password = (EditText) findViewById(R.id.textview_login_password);
        editText_username = (EditText) findViewById(R.id.textview_login_username);


        //sign in action
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = editText_username.getText().toString();
                String password = editText_password.getText().toString();
                Log.d(TAG, "Btn_sign_in onClick: Username:" + username + " , Password:" + password);
                if(!isUserNameValid(username)){
                    //TODO: check username
                    Toast.makeText(LoginActivity.this,R.string.illegal_username,Toast.LENGTH_SHORT).show();
                }
                if(!isPasswordValid(password)){
                    //TODO: check password
                    Toast.makeText(LoginActivity.this,R.string.illegal_password,Toast.LENGTH_SHORT).show();
                }
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
    //TODO: 自定义Handler,处理异步信息
}

