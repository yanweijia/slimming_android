package cn.yanweijia.slimming;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.RegexUtils;
import com.blankj.utilcode.util.StringUtils;

import org.json.JSONObject;

import cn.yanweijia.slimming.dao.DBManager;
import cn.yanweijia.slimming.databinding.ActivityChangePasswordBinding;
import cn.yanweijia.slimming.utils.RequestUtils;

/**
 * change password
 *
 * @author weijia
 */
public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityChangePasswordBinding binding;
    private String oldPw, newPw, reNewPw;
    private static final String TAG = "ChangePasswordActivity";

    private static final int CHANGE_PASSWORD_SUCCESS = 1;
    private static final int CHANGE_PASSWORD_FAIL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);

        binding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPw = binding.oldPassword.getText().toString();
                newPw = binding.newPassword.getText().toString();
                reNewPw = binding.reNewPassword.getText().toString();
                if (newPw.length() < 6 || newPw.length() > 20) {
                    Toast.makeText(ChangePasswordActivity.this, R.string.illegal_password, Toast.LENGTH_SHORT).show();
                    binding.newPassword.requestFocus();
                    return;
                }
                if (!StringUtils.equals(newPw, reNewPw)) {
                    Toast.makeText(ChangePasswordActivity.this, R.string.password_not_same, Toast.LENGTH_SHORT).show();
                    binding.reNewPassword.requestFocus();
                    return;
                }
                oldPw = EncryptUtils.encryptMD5ToString(oldPw);
                newPw = EncryptUtils.encryptMD5ToString(newPw);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int userid = DBManager.getUser().getId();
                        String jsonResult = RequestUtils.changePassword(userid, oldPw, newPw);
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(jsonResult);
                            //TODO:change

                        } catch (Exception e) {
                            Log.e(TAG, "run: ", e);
                        }
                    }
                }).start();
            }
        });

    }

    /**
     * custom handler
     */
    class ChangePasswordHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CHANGE_PASSWORD_SUCCESS:
                    Toast.makeText(ChangePasswordActivity.this,R.string.change_password_success,Toast.LENGTH_SHORT).show();
                    //TODO:logout and finish(), open LoginActivity

                    break;
                case CHANGE_PASSWORD_FAIL:
                    Toast.makeText(ChangePasswordActivity.this,R.string.change_password_fail,Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Log.d(TAG, "handleMessage: " + getString(R.string.unknow_exception) + msg.toString());
            }
        }
    }
}
