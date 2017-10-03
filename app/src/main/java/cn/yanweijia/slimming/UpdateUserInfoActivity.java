package cn.yanweijia.slimming;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.RegexUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.yanweijia.slimming.dao.DBManager;
import cn.yanweijia.slimming.databinding.ActivityUpdateUserInfoBinding;
import cn.yanweijia.slimming.entity.User;
import cn.yanweijia.slimming.fragment.me.MeFragment;
import cn.yanweijia.slimming.utils.RequestUtils;

public class UpdateUserInfoActivity extends AppCompatActivity {
    private ActivityUpdateUserInfoBinding binding;
    private ObjectMapper objectMapper;
    private static final String TAG = "UpdateUserInfoActivity";
    private UpdateUserInfoHandler myHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_update_user_info);
        objectMapper = new ObjectMapper();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update_user_info);
        DBManager.initSQLiteDB(this);
        binding.setUser(DBManager.getUser());

        myHandler = new UpdateUserInfoHandler();

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user_old = binding.getUser();
                final User user = new User();
                user.setId(user_old.getId());
                user.setUsername(user_old.getUsername());
                user.setPassword(user_old.getPassword());
                String phone = binding.phone.getText().toString();
                if (!RegexUtils.isMobileSimple(phone)) {
                    Toast.makeText(UpdateUserInfoActivity.this, R.string.illegal_phone, Toast.LENGTH_SHORT).show();
                    binding.phone.requestFocus();
                    return;
                }
                String email = binding.email.getText().toString();
                if (!RegexUtils.isEmail(email)) {
                    Toast.makeText(UpdateUserInfoActivity.this, R.string.illegal_email, Toast.LENGTH_SHORT).show();
                    binding.email.requestFocus();
                    return;
                }
                String name = binding.name.getText().toString();
                if (name.length() < 2 || name.length() > 20) {
                    Toast.makeText(UpdateUserInfoActivity.this, R.string.illegal_name, Toast.LENGTH_SHORT).show();
                    binding.name.requestFocus();
                    return;
                }
                String birthday_str = binding.birthday.getText().toString();
                Date birthday;
                try {
                    birthday = new SimpleDateFormat("yyyy-MM-dd").parse(birthday_str);
                } catch (ParseException e) {
                    Toast.makeText(UpdateUserInfoActivity.this, R.string.illegal_birthday, Toast.LENGTH_SHORT).show();
                    binding.birthday.requestFocus();
                    e.printStackTrace();
                    return;
                }
                String gender = binding.gender.getText().toString();
                if (!(gender.equals("男") || gender.equals("女") || gender.equals("未知"))) {
                    Toast.makeText(UpdateUserInfoActivity.this, R.string.illegal_gender, Toast.LENGTH_SHORT).show();
                    binding.gender.requestFocus();
                    return;
                }
                String weight_str = binding.weight.getText().toString();
                double weight;
                try {
                    weight = Double.parseDouble(weight_str);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(UpdateUserInfoActivity.this, R.string.illegal_weight, Toast.LENGTH_SHORT).show();
                    binding.weight.requestFocus();
                    return;
                }
                String height_str = binding.height.getText().toString();
                double height;
                try {
                    height = Double.parseDouble(height_str);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(UpdateUserInfoActivity.this, R.string.illegal_height, Toast.LENGTH_SHORT).show();
                    binding.height.requestFocus();
                    return;
                }
                user.setPhone(phone);
                user.setEmail(email);
                user.setName(name);
                user.setBirthday(birthday);
                user.setGender(gender);
                user.setWeight(new BigDecimal(weight));
                user.setHeight(new BigDecimal(height));
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        try {
                            String resultJson = RequestUtils.updateUserInfo(user);
                            JSONObject jsonObject = new JSONObject(resultJson);
                            if(jsonObject.getBoolean("success")) {
                                msg.what=MeFragment.UPDATE_USERINFO_SUCCESS;
                            }else{
                                msg.what=MeFragment.UPDATE_USERINFO_FAIL;
                                bundle.putString("message",jsonObject.getString("message"));
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "run: ", e);
                            msg.what=MeFragment.UPDATE_USERINFO_FAIL;
                            bundle.putString("message",e.getMessage());
                        }
                        msg.setData(bundle);
                        myHandler.sendMessage(msg);
                    }
                }).start();
            }
        });
    }

    /**
     * custome handler
     *
     * @author weijia
     */
    class UpdateUserInfoHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MeFragment.UPDATE_USERINFO_SUCCESS:
                    setResult(MeFragment.UPDATE_USERINFO_SUCCESS);
                    Toast.makeText(UpdateUserInfoActivity.this,R.string.update_userinfo_success,Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "handleMessage: UPDATE_USERINFO_SUCCESS");
                    finish();
                    break;
                case MeFragment.UPDATE_USERINFO_FAIL:
                    Toast.makeText(UpdateUserInfoActivity.this,getString(R.string.update_userinfo_fail)+msg.getData().getString("message"),Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(UpdateUserInfoActivity.this, R.string.unknow_exception, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "handleMessage: " + msg.toString());
            }
        }
    }


}
