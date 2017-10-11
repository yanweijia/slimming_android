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

import java.util.Date;

import cn.yanweijia.slimming.databinding.ActivityBloodGlucoseBinding;
import cn.yanweijia.slimming.entity.BloodGlucose;
import cn.yanweijia.slimming.utils.RequestUtils;

public class BloodGlucoseActivity extends AppCompatActivity {
    private static final String TAG = "BloodGlucoseActivity";
    private ActivityBloodGlucoseBinding binding;
    private Handler handler;
    private static final int UPLOAD_FAIL = 0;
    private static final int UPLOAD_SUCCESS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_blood_glucose);
        handler = new BloodGlucoseActivityHandler();
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String glucoseStr = binding.textview.getText().toString();
                if (StringUtils.isTrimEmpty(glucoseStr)) {
                    Toast.makeText(BloodGlucoseActivity.this, R.string.illegal_input, Toast.LENGTH_SHORT).show();
                    binding.textview.requestFocus();
                    return;
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BloodGlucose bloodGlucose = new BloodGlucose();
                        bloodGlucose.setGlucose(Integer.parseInt(glucoseStr));
                        bloodGlucose.setMethod("手机输入");
                        bloodGlucose.setTime(new Date());
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        try {
                            String jsonResult = RequestUtils.uploadBloodGlucose(bloodGlucose);
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
    class BloodGlucoseActivityHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPLOAD_FAIL:
                    Toast.makeText(BloodGlucoseActivity.this, getString(R.string.upload_fail) + msg.getData().getString("message"), Toast.LENGTH_SHORT).show();
                    break;
                case UPLOAD_SUCCESS:
                    binding.button.setEnabled(false);
                    Toast.makeText(BloodGlucoseActivity.this, getString(R.string.upload_success), Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    }
}
