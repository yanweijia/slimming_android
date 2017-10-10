package cn.yanweijia.slimming.fragment.diet;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.blankj.utilcode.util.KeyboardUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.yanweijia.slimming.FoodListActivity;
import cn.yanweijia.slimming.R;
import cn.yanweijia.slimming.dao.DBManager;
import cn.yanweijia.slimming.databinding.FragmentDietBinding;
import cn.yanweijia.slimming.entity.Food;
import cn.yanweijia.slimming.utils.RequestUtils;

/**
 * @author weijia
 * @date 2017.09.24
 */
public class DietFragment extends Fragment {
    //this view is used to save view stack
    private View rootView;
    private static final String TAG = "DietFragment";
    private ObjectMapper objectMapper;
    private FragmentDietBinding binding;
    private DietFragmentHandler myHandler;
    private Bitmap recommendFoodImage = null;

    public static final int GET_FOOD_BY_CATEGORY = 1;
    public static final int GET_FOOD_BY_NAME = 2;

    /**
     * recommend food sign
     */
    private static final int RECOMMEND_FOOD = 1;

    public DietFragment() {
        Log.d(TAG, "DietFragment: Constructor");
    }

    /**
     * new instance
     *
     * @return
     */
    public static DietFragment newInstance() {
        DietFragment fragment = new DietFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        Log.d(TAG, "newInstance: ");
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: rootView==null? " + String.valueOf(rootView == null));
        return getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_diet);
    }


    /**
     * use rootView to initial views
     *
     * @author weijia
     */
    private void initViews() {
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTextSubmit: key:" + query);
                Intent intent = new Intent(getActivity(), FoodListActivity.class);
                intent.putExtra("method", GET_FOOD_BY_NAME);
                intent.putExtra("name", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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
        myHandler = new DietFragmentHandler();
        objectMapper = new ObjectMapper();
        //data source
        List<HashMap<String, Object>> list = new ArrayList<>();
        int[] images = {R.drawable.category1, R.drawable.category2, R.drawable.category3, R.drawable.category4, R.drawable.category5, R.drawable.category6, R.drawable.category7, R.drawable.category8, R.drawable.category9, R.drawable.category10, R.drawable.category11};
        int[] titles = {R.string.category1, R.string.category2, R.string.category3, R.string.category4, R.string.category5, R.string.category6, R.string.category7, R.string.category8, R.string.category9, R.string.category10, R.string.category11};
        String[] categoryids = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11"};
        for (int i = 0; i < 10; i++) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("image", images[i]);
            map.put("title", getString(titles[i]));
            map.put("categoryid", categoryids[i]);
            list.add(map);
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(getActivity(),
                list,//data source
                R.layout.category_item,
                new String[]{"image", "title", "categoryid"},
                new int[]{R.id.circleImageView, R.id.categorytitle, R.id.categoryid});
        binding.gridview.setAdapter(simpleAdapter);
        binding.gridview.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, Object> item = (HashMap<String, Object>) parent.getItemAtPosition(position);
                int categoryid = Integer.parseInt((String) item.get("categoryid"));
                Log.d(TAG, "onItemClick: position=" + position + ", categoryid:" + categoryid);
                //transfer params
                Intent intent = new Intent(getActivity(), FoodListActivity.class);
                intent.putExtra("method", GET_FOOD_BY_CATEGORY);
                intent.putExtra("categoryid", categoryid);
                startActivity(intent);
            }
        });
        final Food recommendFood = new Food();
        recommendFood.setName(getString(R.string.food_recommend));
        recommendFood.setCalorie(new BigDecimal(0f));
        recommendFood.setComment(getString(R.string.food_intro));
        binding.setRecommendFood(recommendFood);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonResult = RequestUtils.recommendFood(DBManager.getUser().getId());
                JSONObject jsonObject;
                Message msg = new Message();
                msg.what = 0;
                try {
                    jsonObject = new JSONObject(jsonResult);
                    if (jsonObject.getBoolean("success")) {
                        msg.what = RECOMMEND_FOOD;
                        Food food = objectMapper.readValue(jsonObject.getString("food"), Food.class);
                        binding.setRecommendFood(food);
                        //download food image
                        recommendFoodImage = RequestUtils.downloadFoodImage(food.getFoodId());
                    }
                } catch (Exception e) {
                    Log.e(TAG, "run: ", e);
                }
                myHandler.sendMessage(msg);
            }
        }).start();

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

    @Override
    public void onResume() {
        super.onResume();
        //hide soft keyboard
        KeyboardUtils.hideSoftInput(getActivity());
    }

    class DietFragmentHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case RECOMMEND_FOOD:
                    //update food image
                    binding.foodImage.setImageBitmap(recommendFoodImage);
                    break;
                default:
            }
        }
    }

}
