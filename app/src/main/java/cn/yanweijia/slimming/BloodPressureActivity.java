package cn.yanweijia.slimming;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.StringUtils;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Date;

import cn.yanweijia.slimming.databinding.ActivityBloodPressureBinding;
import cn.yanweijia.slimming.entity.BloodPressure;
import cn.yanweijia.slimming.utils.RequestUtils;

public class BloodPressureActivity extends AppCompatActivity {
    private static final String TAG = "BloodPressureActivity";
    private ActivityBloodPressureBinding binding;
    private Handler handler;
    private static final int UPLOAD_FAIL = 0;
    private static final int UPLOAD_SUCCESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_blood_pressure);
        handler = new BloodPressureActivityHandler();
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String systolicStr = binding.systolicPressure.getText().toString();
                final String diastolicStr = binding.diastolicPressure.getText().toString();
                if (StringUtils.isTrimEmpty(systolicStr)) {
                    Toast.makeText(BloodPressureActivity.this, R.string.illegal_input, Toast.LENGTH_SHORT).show();
                    binding.systolicPressure.requestFocus();
                    return;
                }
                if (StringUtils.isTrimEmpty(diastolicStr)) {
                    Toast.makeText(BloodPressureActivity.this, R.string.illegal_input, Toast.LENGTH_SHORT).show();
                    binding.diastolicPressure.requestFocus();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BloodPressure bloodPressure = new BloodPressure();
                        bloodPressure.setMethod("用户手机输入");
                        bloodPressure.setDiastolicPressure(new BigDecimal(diastolicStr));
                        bloodPressure.setSystolicPressure(new BigDecimal(systolicStr));
                        bloodPressure.setTime(new Date());
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        msg.what = UPLOAD_FAIL;
                        try {
                            String jsonResult = RequestUtils.uploadBloodPressure(bloodPressure);
                            JSONObject json = new JSONObject(jsonResult);
                            if (json.getBoolean("success")) {
                                msg.what = UPLOAD_SUCCESS;
                            } else {
                                msg.what = UPLOAD_FAIL;
                            }
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
    class BloodPressureActivityHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPLOAD_SUCCESS:
                    binding.button.setEnabled(false);
                    Toast.makeText(BloodPressureActivity.this, R.string.upload_success, Toast.LENGTH_SHORT).show();
                    break;
                case UPLOAD_FAIL:
                    Toast.makeText(BloodPressureActivity.this, getString(R.string.upload_fail) + msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    }

}
