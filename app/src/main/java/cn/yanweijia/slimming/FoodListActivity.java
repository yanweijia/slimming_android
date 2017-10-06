package cn.yanweijia.slimming;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.yanweijia.slimming.custom.adapter.ListAdapter;
import cn.yanweijia.slimming.dao.DBManager;
import cn.yanweijia.slimming.databinding.ActivityFoodListBinding;
import cn.yanweijia.slimming.entity.Food;
import cn.yanweijia.slimming.entity.FoodCategory;
import cn.yanweijia.slimming.fragment.diet.DietFragment;
import cn.yanweijia.slimming.utils.RequestUtils;

/**
 * food list <br/>
 * <p>
 * Created by weijia on 2017.10.05
 *
 * @author weijia
 */
public class FoodListActivity extends AppCompatActivity {
    private ActivityFoodListBinding binding;
    private List<Food> list;
    private static final String TAG = "FoodListActivity";
    private FoodListActivityHandler myHandler;
    private ObjectMapper objectMapper;
    /**
     * load data successful complete!
     */
    private static final int LOAD_SUCCESS = 1;
    /**
     * load data fail
     */
    private static final int LOAD_FAIL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_food_list);
        myHandler = new FoodListActivityHandler();
        objectMapper = new ObjectMapper();
        Intent intent = getIntent();
        list = new ArrayList<>();
        //GET_FOOD_BY_CATEGORY
        if (intent.getIntExtra("method", DietFragment.GET_FOOD_BY_CATEGORY) == DietFragment.GET_FOOD_BY_CATEGORY) {
            final int categoryid = intent.getIntExtra("categoryid", 1);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    FoodCategory foodCategory = DBManager.getFoodCategory(categoryid);
                    binding.setTitle(foodCategory.getName() == null ? "unknow" : foodCategory.getName());
                    //获取分类的食物信息
                    String jsonResult = RequestUtils.getFoodByCategory(categoryid);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonResult);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("foods");
                            list.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Food food = objectMapper.readValue(jsonArray.getString(i), Food.class);
                                list.add(food);
                            }
                            myHandler.sendEmptyMessage(LOAD_SUCCESS);
                        } else
                            myHandler.sendEmptyMessage(LOAD_FAIL);
                    } catch (Exception e) {
                        Log.e(TAG, "run: ", e);
                        myHandler.sendEmptyMessage(LOAD_FAIL);
                    }
                }
            }).start();
        } else {// GET_FOOD_BY_NAME
            final String foodName = intent.getStringExtra("name");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    binding.setTitle(foodName);
                    //联网搜索食物信息
                    String jsonResult = RequestUtils.getFoodByName(foodName);
                    try {
                        JSONObject jsonObject = new JSONObject(jsonResult);
                        if (jsonObject.getBoolean("success")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("foods");
                            list.clear();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Food food = objectMapper.readValue(jsonArray.getString(i), Food.class);
                                list.add(food);
                            }
                            myHandler.sendEmptyMessage(LOAD_SUCCESS);
                        } else
                            myHandler.sendEmptyMessage(LOAD_FAIL);
                    } catch (Exception e) {
                        Log.e(TAG, "run: ", e);
                        myHandler.sendEmptyMessage(LOAD_FAIL);
                    }
                }
            }).start();
        }

        binding.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: ");
                // --将null换成对应activity的class--
                Intent intent = new Intent(FoodListActivity.this, FoodActivity.class);
                intent.putExtra("foodid", list.get(position).getFoodId());
                startActivity(intent);
            }
        });
    }

    class FoodListActivityHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_SUCCESS:
                    //reduce list size, should be mult page
//                    if(list.size()>30)
//                        list = list.subList(0,29);
                    ListAdapter<Food> adapter = new ListAdapter<>(FoodListActivity.this, list, R.layout.food_item, BR.foodbean);
                    binding.setAdapter(adapter);
                    break;
                case LOAD_FAIL:
                    break;
                default:
            }
        }
    }

}
