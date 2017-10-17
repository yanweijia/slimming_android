package cn.yanweijia.slimming.fragment.analyze;

import android.app.DatePickerDialog;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.yanweijia.slimming.R;
import cn.yanweijia.slimming.databinding.FragmentAnalyzeBinding;
import cn.yanweijia.slimming.entity.BloodGlucose;
import cn.yanweijia.slimming.entity.BloodPressure;
import cn.yanweijia.slimming.entity.HeartRate;
import cn.yanweijia.slimming.entity.RunRecordUpload;
import cn.yanweijia.slimming.entity.UserWeight;
import cn.yanweijia.slimming.utils.ChartUtils;
import cn.yanweijia.slimming.utils.RequestUtils;

/**
 * @author weijia
 * @date 2017.09.24
 */
public class AnalyzeFragment extends Fragment {
    //this view is used to save view stack
    private View rootView;
    private static final String TAG = "AnalyzeFragment";
    private FragmentAnalyzeBinding binding;
    private Date startTime, endTime;
    private ObjectMapper objectMapper;  //convert jsonString to java bean
    private Handler handler;
    private IAxisValueFormatter formatter;
    private static final int DRAW_WEIGHT = 1;
    private static final int DRAW_BLOOD_PRESSURE = 2;
    private static final int DRAW_BLOOD_GLUCOSE = 3;
    private static final int DRAW_HEART_RATE = 4;
    private static final int DRAW_RUN_RECORD = 5;


    public AnalyzeFragment() {
        Log.d(TAG, "AnalyzeFragment: Constructor");
    }

    /**
     * new instance
     *
     * @return
     */
    public static AnalyzeFragment newInstance() {
        AnalyzeFragment fragment = new AnalyzeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        Log.d(TAG, "newInstance: ");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: rootView==null?  " + String.valueOf(rootView == null));
        return getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_analyze);
    }

    /**
     * use rootView to initial views
     *
     * @author weijia
     */
    private void initViews() {
        handler = new AnalyzeFragmentHandler();
        objectMapper = new ObjectMapper();
        endTime = new Date();
        startTime = new Date(endTime.getTime() - 7 * 24 * 60 * 60 * 1000L);
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        binding.startTime.setText(simpleDateFormat.format(startTime));
        binding.endTime.setText(simpleDateFormat.format(endTime));
        try {
            startTime = simpleDateFormat.parse(simpleDateFormat.format(startTime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //x轴单位对应显示数据
        formatter = new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                Date date = new Date((long) value);
                return new SimpleDateFormat("MM.dd").format(date);
            }
        };


        drawChart();
        binding.setStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, -7);
                new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                calendar.set(Calendar.HOUR_OF_DAY, 0);
                                calendar.set(Calendar.MINUTE, 0);
                                calendar.set(Calendar.SECOND, 0);
                                startTime = calendar.getTime();
                                binding.startTime.setText(simpleDateFormat.format(startTime));
                                drawChart();
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();

            }
        });
        binding.setEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, month);
                                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                calendar.set(Calendar.HOUR_OF_DAY, 23);
                                calendar.set(Calendar.MINUTE, 59);
                                calendar.set(Calendar.SECOND, 59);
                                endTime = calendar.getTime();
                                binding.endTime.setText(simpleDateFormat.format(endTime));
                                drawChart();
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();

            }
        });
        Log.d(TAG, "initViews: complete!");
    }

    /**
     * initial datas (after initViews)
     *
     * @author weijia
     */
    private void initDatas() {

        Log.d(TAG, "initDatas: complete!");
    }

    /**
     * get saved view stack <br/>
     *
     * @return rootView
     * @author weijia
     * @see cn.yanweijia.slimming.fragment.sport.SportFragment#getPersistentView(LayoutInflater, ViewGroup, Bundle, int)
     */
    public View getPersistentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, int layout) {
        Log.d(TAG, "getPersistentView: rootView==null? " + String.valueOf(rootView == null));
        if (rootView == null) {
            binding = DataBindingUtil.inflate(inflater, layout, container, false);
            rootView = binding.getRoot();
            Log.d(TAG, "getPersistentView:  rootView is null, initial rootView");
            initViews();
            initDatas();
        } else {
            Log.d(TAG, "getPersistentView: removeRootView and add again");
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if (viewGroup != null)
                viewGroup.removeView(rootView);
        }
        return rootView;
    }


    /**
     * draw charts
     */
    private void drawChart() {
        drawRunRecordChart();
        drawHeartRateChart();
        drawBloodPressureChart();
        drawBloodGlucoseChart();
        drawWeightChart();
    }

    private void drawRunRecordChart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonResult = RequestUtils.listRunRecord(startTime, endTime);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("data", jsonResult);
                msg.setData(bundle);
                msg.what = DRAW_RUN_RECORD;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void drawHeartRateChart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonResult = RequestUtils.listHeartRate(startTime, endTime);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("data", jsonResult);
                msg.setData(bundle);
                msg.what = DRAW_HEART_RATE;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void drawBloodPressureChart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonResult = RequestUtils.listBloodPressure(startTime, endTime);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("data", jsonResult);
                msg.setData(bundle);
                msg.what = DRAW_BLOOD_PRESSURE;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void drawBloodGlucoseChart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonResult = RequestUtils.listBloodGlucose(startTime, endTime);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("data", jsonResult);
                msg.setData(bundle);
                msg.what = DRAW_BLOOD_GLUCOSE;
                handler.sendMessage(msg);
            }
        }).start();
    }

    private void drawWeightChart() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonResult = RequestUtils.listWeight(startTime, endTime);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("data", jsonResult);
                msg.setData(bundle);
                msg.what = DRAW_WEIGHT;
                handler.sendMessage(msg);
            }
        }).start();
    }


    private void drawRunRecordChart(String jsonResult) {
        List<ArrayList<Entry>> values = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        ArrayList<Entry> runRecordValues = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(jsonResult);
            if (json.getBoolean("success")) {
                String jsonPressure = json.getString("runRecord");
                List<RunRecordUpload> list = null;
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, RunRecordUpload.class);
                list = objectMapper.readValue(jsonPressure, javaType);
                for (RunRecordUpload runRecord : list) {
                    float x = runRecord.getStarttime().getTime();
                    runRecordValues.add(new Entry(x, runRecord.getDistance().floatValue()));
                }
            } else {
                Log.d(TAG, "drawRunRecordChart: downloadFail:" + json.getString("message"));
            }
        } catch (Exception e) {
            Log.e(TAG, "drawRunRecordChart: ", e);
        }
        values.add(runRecordValues);
        titles.add(getString(R.string.run));
        colors.add(Color.BLACK);
        ChartUtils.drawLineChart(getActivity(), binding.runRecordChart, "", getString(R.string.run), null, values, titles, colors, formatter);

    }

    private void drawHeartRateChart(String jsonResult) {
        List<ArrayList<Entry>> values = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        ArrayList<Entry> heartRateValues = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(jsonResult);
            if (json.getBoolean("success")) {
                String jsonPressure = json.getString("heartRate");
                List<HeartRate> list = null;
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, HeartRate.class);
                list = objectMapper.readValue(jsonPressure, javaType);
                for (HeartRate heartRate : list) {
                    float x = heartRate.getTime().getTime();
                    heartRateValues.add(new Entry(x, heartRate.getRate().floatValue()));
                }
            } else {
                Log.d(TAG, "drawHeartRateChart: downloadFail:" + json.getString("message"));
            }
        } catch (Exception e) {
            Log.e(TAG, "drawHeartRateChart: ", e);
        }
        values.add(heartRateValues);
        titles.add(getString(R.string.heart_rate));
        colors.add(Color.BLACK);
        ChartUtils.drawLineChart(getActivity(), binding.heartRateChart, "", getString(R.string.heart_rate), null, values, titles, colors, formatter);

    }

    private void drawBloodPressureChart(String jsonResult) {
        List<ArrayList<Entry>> values = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        ArrayList<Entry> systolicValues = new ArrayList<>();
        ArrayList<Entry> diastolicValues = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(jsonResult);
            if (json.getBoolean("success")) {
                String jsonPressure = json.getString("bloodPressure");
                List<BloodPressure> list = null;
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, BloodPressure.class);
                list = objectMapper.readValue(jsonPressure, javaType);
                for (BloodPressure bloodPressure : list) {
                    float x = bloodPressure.getTime().getTime();
                    systolicValues.add(new Entry(x, bloodPressure.getSystolicPressure().floatValue()));
                    diastolicValues.add(new Entry(x, bloodPressure.getDiastolicPressure().floatValue()));
                }
            } else {
                Log.d(TAG, "drawBloodPressureChart: downloadFail:" + json.getString("message"));
            }
        } catch (Exception e) {
            Log.e(TAG, "drawBloodPressureChart: ", e);
        }
        values.add(systolicValues);
        values.add(diastolicValues);
        titles.add(getString(R.string.systolic_blood_pressure_hint));
        titles.add(getString(R.string.diastolic_blood_pressure_hint));
        colors.add(Color.BLACK);
        colors.add(Color.BLUE);
        ChartUtils.drawLineChart(getActivity(), binding.bloodPressureChart, "", getString(R.string.blood_persure), null, values, titles, colors, formatter);

    }

    private void drawBloodGlucoseChart(String jsonResult) {
        List<ArrayList<Entry>> values = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        ArrayList<Entry> bloodGlucoseValues = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(jsonResult);
            if (json.getBoolean("success")) {
                String jsonPressure = json.getString("bloodGlucose");
                List<BloodGlucose> list = null;
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, BloodGlucose.class);
                list = objectMapper.readValue(jsonPressure, javaType);
                for (BloodGlucose bloodGlucose : list) {
                    float x = bloodGlucose.getTime().getTime();
                    bloodGlucoseValues.add(new Entry(x, bloodGlucose.getGlucose().floatValue()));
                }
            } else {
                Log.d(TAG, "drawBloodGlucoseChart: downloadFail:" + json.getString("message"));
            }
        } catch (Exception e) {
            Log.e(TAG, "drawBloodGlucoseChart: ", e);
        }
        values.add(bloodGlucoseValues);
        titles.add(getString(R.string.blood_glucose_hint));
        colors.add(Color.BLACK);
        ChartUtils.drawLineChart(getActivity(), binding.bloodGlucoseChart, "", getString(R.string.blood_glucose_hint), null, values, titles, colors, formatter);

    }

    private void drawWeightChart(String jsonResult) {
        List<ArrayList<Entry>> values = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        List<Integer> colors = new ArrayList<>();

        ArrayList<Entry> weightValues = new ArrayList<>();
        try {
            JSONObject json = new JSONObject(jsonResult);
            if (json.getBoolean("success")) {
                String jsonPressure = json.getString("userWeight");
                List<UserWeight> list = null;
                JavaType javaType = objectMapper.getTypeFactory().constructParametricType(ArrayList.class, UserWeight.class);
                list = objectMapper.readValue(jsonPressure, javaType);
                for (UserWeight userWeight : list) {
                    float x = userWeight.getTime().getTime();
                    weightValues.add(new Entry(x, userWeight.getWeight().floatValue()));
                }
            } else {
                Log.d(TAG, "drawWeightChart: downloadFail:" + json.getString("message"));
            }
        } catch (Exception e) {
            Log.e(TAG, "drawWeightChart: ", e);
        }
        values.add(weightValues);
        titles.add(getString(R.string.weight));
        colors.add(Color.BLACK);
        ChartUtils.drawLineChart(getActivity(), binding.weightChart, "", getString(R.string.weight), null, values, titles, colors, formatter);

    }

    class AnalyzeFragmentHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String jsonResult = msg.getData().getString("data");
            switch (msg.what) {
                case DRAW_WEIGHT:
                    drawWeightChart(jsonResult);
                    break;
                case DRAW_BLOOD_PRESSURE:
                    drawBloodPressureChart(jsonResult);
                    break;
                case DRAW_BLOOD_GLUCOSE:
                    drawBloodGlucoseChart(jsonResult);
                    break;
                case DRAW_HEART_RATE:
                    drawHeartRateChart(jsonResult);
                    break;
                case DRAW_RUN_RECORD:
                    drawRunRecordChart(jsonResult);
                    break;
                default:
            }
        }
    }
}