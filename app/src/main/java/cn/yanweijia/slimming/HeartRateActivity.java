package cn.yanweijia.slimming;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import cn.yanweijia.slimming.databinding.ActivityHeartRateBinding;
import cn.yanweijia.slimming.entity.HeartRate;
import cn.yanweijia.slimming.utils.RequestUtils;
import cn.yanweijia.slimming.utils.YUV420SPUtils;


/**
 * 程序的主入口
 */
public class HeartRateActivity extends Activity {
    private static final String TAG = "HeartRateActivity";
    private static final int MEASURE_COMPLETE = 1;
    private static final int UPLOAD_COMPLETE = 2;
    private static final int UPLOAD_FAIL = 3;
    private static double flag = 1;
    private static boolean complete = false;
    private ActivityHeartRateBinding binding;
    private static final AtomicBoolean processing = new AtomicBoolean(false);
    //Android手机预览控件
    private SurfaceView preview = null;
    //预览设置信息
    private static SurfaceHolder previewHolder = null;
    //Android手机相机句柄
    private static Camera camera = null;
    private static WakeLock wakeLock = null;
    private static int averageIndex = 0;
    private static final int averageArraySize = 5;
    private static final int[] averageArray = new int[averageArraySize];
    private static int heartBeat = 0;
    private HeartRateActivityHandler handler = new HeartRateActivityHandler();

    /**
     * 类型枚举
     */
    public static enum TYPE {
        GREEN, RED
    }

    ;

    //设置默认类型
    private static TYPE currentType = TYPE.GREEN;

    //获取当前类型
    public static TYPE getCurrent() {
        return currentType;
    }

    //心跳下标值
    private static int beatsIndex = 0;
    //心跳数组的大小
    private static final int beatsArraySize = 5;
    //心跳数组
    private static final int[] beatsArray = new int[beatsArraySize];
    //心跳脉冲
    private static double beats = 0;
    //开始时间
    private static long startTime = 0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_heart_rate);
        flag = 1;
        complete = false;
        processing.set(false);
        heartBeat = 0;
        initConfig();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!complete) {
                }
                handler.sendEmptyMessage(MEASURE_COMPLETE);
            }
        }).start();
    }

    /**
     * 初始化配置
     */
    private void initConfig() {
        //获取SurfaceView控件
        preview = binding.idPreview;
        previewHolder = preview.getHolder();
        previewHolder.addCallback(surfaceCallback);

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK, "DoNotDimScreen");
        binding.confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HeartRate heartRate = new HeartRate();
                        heartRate.setRate(heartBeat);
                        heartRate.setTime(new Date());
                        heartRate.setMethod("手机相机测量");
                        Message msg = new Message();
                        Bundle bundle = new Bundle();
                        try {
                            String jsonResult = RequestUtils.uploadHeartRate(heartRate);
                            JSONObject json = new JSONObject(jsonResult);
                            if (json.getBoolean("success")) {
                                msg.what = UPLOAD_COMPLETE;
                            } else {
                                msg.what = UPLOAD_FAIL;
                            }
                            bundle.putString("message", json.getString("message"));
                        } catch (Exception e) {
                            Log.e(TAG, "run: ", e);
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

    //	曲线
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!complete) {
            wakeLock.acquire();
            camera = Camera.open();
            startTime = System.currentTimeMillis();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (!complete)
            stopCamera();
    }

    /**
     * stop camera
     */
    private static void stopCamera() {
        wakeLock.release();
        camera.setPreviewCallback(null);
        camera.stopPreview();
        camera.release();
        camera = null;
    }

    /**
     * 相机预览方法
     * 这个方法中实现动态更新界面UI的功能，
     * 通过获取手机摄像头的参数来实时动态计算平均像素值、脉冲数，从而实时动态计算心率值。
     */
    private static PreviewCallback previewCallback = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera cam) {
            if (data == null) {
                throw new NullPointerException();
            }
            Camera.Size size = cam.getParameters().getPreviewSize();
            if (size == null) {
                throw new NullPointerException();
            }
            if (!processing.compareAndSet(false, true)) {
                return;
            }
            int width = size.width;
            int height = size.height;

            //图像处理
            int imgAvg = YUV420SPUtils.decodeYUV420SPtoRedAvg(data.clone(), height, width);
            Log.i(TAG, "onPreviewFrame: 平均像素值是:" + imgAvg);

            if (imgAvg == 0 || imgAvg == 255) {
                processing.set(false);
                return;
            }
            //计算平均值
            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            //计算平均值
            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;
            TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED;
                if (newType != currentType) {
                    beats++;
                    flag = 0;
                    Log.d(TAG, "onPreviewFrame: 脉冲数:" + beats);
                }
            } else if (imgAvg > rollingAverage) {
                newType = TYPE.GREEN;
            }

            if (averageIndex == averageArraySize) {
                averageIndex = 0;
            }
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            if (newType != currentType) {
                currentType = newType;
            }

            //获取系统结束时间（ms）
            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if (totalTimeInSecs >= 2) {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180 || imgAvg < 100) {
                    //获取系统开始时间（ms）
                    startTime = System.currentTimeMillis();
                    //beats心跳总数
                    beats = 0;
                    processing.set(false);
                    return;
                }

                if (beatsIndex == beatsArraySize) {
                    beatsIndex = 0;
                }
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;

                int beatsArrayAvg = 0;
                int beatsArrayCount = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCount++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCount);
                Log.d(TAG, "onPreviewFrame: your HeartRate is:");

                //获取系统时间（ms）
                startTime = System.currentTimeMillis();
                beats = 0;

                Log.i(TAG, "onPreviewFrame: your HeartRate is:" + beatsAvg +
                        "  值:" + String.valueOf(beatsArray.length) +
                        ",beatsIndex:" + beatsIndex +
                        ",beatsArrayAvg:" + beatsArrayAvg +
                        ",beatsArrayCount" + beatsArrayCount);
                //判断结束条件
                if (beatsIndex == beatsArray.length) {
                    complete = true;
                    Log.i(TAG, "onPreviewFrame: 测量结束,心率为:" + beatsAvg);
                    heartBeat = beatsAvg;
                    stopCamera();
                }
            }
            processing.set(false);
        }
    };


    /**
     * 预览回调接口
     */
    private static SurfaceHolder.Callback surfaceCallback = new SurfaceHolder.Callback() {
        //创建时调用
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                if (camera != null) {
                    camera.setPreviewDisplay(previewHolder);
                    camera.setPreviewCallback(previewCallback);
                }
            } catch (Throwable t) {
                Log.e("PrevDemo-surfacCallback", "Exception in setPreviewDisplay()", t);
            }
        }

        //当预览改变的时候回调此方法
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            if (camera != null) {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                Camera.Size size = getSmallestPreviewSize(width, height, parameters);
                if (size != null) {
                    parameters.setPreviewSize(size.width, size.height);
                }
                camera.setParameters(parameters);
                camera.startPreview();
            }
        }

        //销毁的时候调用
        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

    /**
     * 获取相机最小的预览尺寸
     */
    private static Camera.Size getSmallestPreviewSize(int width, int height, Camera.Parameters parameters) {
        Camera.Size result = null;
        for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
            if (size.width <= width && size.height <= height) {
                if (result == null) {
                    result = size;
                } else {
                    int resultArea = result.width * result.height;
                    int newArea = size.width * size.height;
                    if (newArea < resultArea) {
                        result = size;
                    }
                }
            }
        }
        return result;
    }

    class HeartRateActivityHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MEASURE_COMPLETE:
                    Toast.makeText(HeartRateActivity.this, "心率:" + heartBeat, Toast.LENGTH_LONG).show();
                    //TODO:save heart rate
                    binding.heartrate.setText(heartBeat + "bmp");
                    binding.confirm.setEnabled(true);
                    break;

                case UPLOAD_COMPLETE:
                    binding.confirm.setEnabled(false);
                    Toast.makeText(HeartRateActivity.this, R.string.upload_success, Toast.LENGTH_SHORT).show();
                    finish();
                    break;

                case UPLOAD_FAIL:
                    Toast.makeText(HeartRateActivity.this, "fail:" + msg.getData().getString("message"), Toast.LENGTH_LONG).show();
                    break;
                default:
            }
        }
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    private void AskForPermission() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle(getString(R.string.need_permission));
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        builder.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                intent.setData(Uri.parse("package:" + getPackageName())); // 根据包名打开对应的设置界面
//                startActivity(intent);
//            }
//        });
//        builder.create().show();
//    }
}