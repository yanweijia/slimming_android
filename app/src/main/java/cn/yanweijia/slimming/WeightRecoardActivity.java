package cn.yanweijia.slimming;

import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;

import cn.yanweijia.slimming.databinding.ActivityWeightRecoardBinding;
import cn.yanweijia.slimming.entity.UserWeight;
import cn.yanweijia.slimming.utils.RequestUtils;

public class WeightRecoardActivity extends AppCompatActivity {
    private static final String TAG = "WeightRecoardActivity";
    private ActivityWeightRecoardBinding binding;
    private Handler handler;
    private static final int UPLOAD_FAIL = 0;
    private static final int UPLOAD_SUCCESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weight_recoard);
        handler = new WeightRecoardActivityHandler();
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String weightStr = binding.textview.getText().toString();
                if (StringUtils.isTrimEmpty(weightStr)) {
                    Toast.makeText(WeightRecoardActivity.this, R.string.illegal_input, Toast.LENGTH_SHORT).show();
                    binding.textview.requestFocus();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        UserWeight userWeight = new UserWeight();
                        userWeight.setWeight(new BigDecimal(weightStr));
                        userWeight.setMethod("手机输入");
                        userWeight.setTime(new Date());
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        try {
                            String jsonResult = RequestUtils.uploadWeight(userWeight);
                            JSONObject json = new JSONObject(jsonResult);
                            if (json.getBoolean("success"))
                                msg.what = UPLOAD_SUCCESS;
                            else
                                msg.what = UPLOAD_FAIL;
                            bundle.putString("message", json.getString("message"));
                        } catch (Exception e) {
                            Log.e(TAG, "onClick: ", e);
                            msg.what = UPLOAD_FAIL;
                            bundle.putString("message", e.getMessage());
                        }
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    }
                }).start();
            }
        });
    }

    /**
     * custom handler
     */
    class WeightRecoardActivityHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPLOAD_FAIL:
                    Toast.makeText(WeightRecoardActivity.this, getString(R.string.upload_fail) + msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
                    break;
                case UPLOAD_SUCCESS:
                    binding.button.setEnabled(false);
                    Toast.makeText(WeightRecoardActivity.this, getString(R.string.upload_success), Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    }
}
