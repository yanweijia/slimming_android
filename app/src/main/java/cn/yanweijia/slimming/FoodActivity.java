package cn.yanweijia.slimming;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.yanweijia.slimming.custom.adapter.ListAdapter;
import cn.yanweijia.slimming.dao.DBManager;
import cn.yanweijia.slimming.databinding.ActivityFoodBinding;
import cn.yanweijia.slimming.entity.Food;
import cn.yanweijia.slimming.entity.FoodCategory;
import cn.yanweijia.slimming.entity.FoodMeasurement;
import cn.yanweijia.slimming.utils.RequestUtils;

/**
 * display food information
 *
 * @author weijia
 */
public class FoodActivity extends AppCompatActivity {
    private ActivityFoodBinding binding;
    private ObjectMapper objectMapper;
    private static final String TAG = "FoodActivity";
    private int foodId = 1;
    private FoodActivityHandler myHandler;
    private List<FoodMeasurement> list;


    private static final int GET_INFO_SUCCESS = 1;
    private static final int GET_INFO_FAIL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_food);
        objectMapper = new ObjectMapper();
        myHandler = new FoodActivityHandler();
        DBManager.initSQLiteDB(this);
        list = new ArrayList<>();

        Intent intent = getIntent();
        intent.putExtra("foodid", 1);
        foodId = intent.getIntExtra("foodid", 1);
        initDatas();
    }

    /**
     * initial food information
     */
    private void initDatas() {
        binding.setTitle("标题");
        binding.setFoodCategory("食物分类");
        binding.setFood(new Food());
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonResult = RequestUtils.getFoodInfoById(foodId);
                Message msg = new Message();
                Bundle bundle = new Bundle();
                try {
                    JSONObject jsonObject = new JSONObject(jsonResult);
                    if (jsonObject.getBoolean("success")) {
                        msg.what = GET_INFO_SUCCESS;
                        bundle.putString("message", jsonObject.getString("message"));
                        Food food = objectMapper.readValue(jsonObject.getString("food"), Food.class);
                        binding.setFood(food);
                        FoodCategory foodCategory = DBManager.getFoodCategory(food.getCategory());
                        binding.setFoodCategory(foodCategory == null ? "unknow" : foodCategory.getName());
                        binding.setTitle(food.getName().substring(0, (food.getName().indexOf(',') > 0 ? food.getName().indexOf(',') : food.getName().length())));
                    } else {
                        msg.what = GET_INFO_FAIL;
                        bundle.putString("message", jsonObject.getString("message"));
                    }
                } catch (Exception e) {
                    msg.what = GET_INFO_FAIL;
                    Log.e(TAG, "run: ", e);
                }
                msg.setData(bundle);
                myHandler.sendMessage(msg);
            }
        }).start();

    }

    private void getFoodMeasurement() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonResult = RequestUtils.getFoodMeasurements(foodId);
                try {
                    JSONObject json = new JSONObject(jsonResult);
                    if (json.getBoolean("success")) {
                        JSONArray jsonArray = json.getJSONArray("foodMeasurement");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            FoodMeasurement foodMeasurement = objectMapper.readValue(jsonArray.getString(i),FoodMeasurement.class);
                            list.add(foodMeasurement);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, "run: ", e);
                }

                ListAdapter<FoodMeasurement> adapter = new ListAdapter<>(FoodActivity.this, list, R.layout.food_measurement_item, BR.foodMeasurement);
                binding.setAdapter(adapter);
            }
        }).start();

    }


    class FoodActivityHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GET_INFO_SUCCESS:
                    getFoodMeasurement();
                    break;
                case GET_INFO_FAIL:
                    Toast.makeText(FoodActivity.this, R.string.load_food_fail, Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(FoodActivity.this, R.string.unknow_exception, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "handleMessage: Unknow exception");
            }
        }
    }
}
