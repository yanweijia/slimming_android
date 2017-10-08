package cn.yanweijia.slimming.fragment.health;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.yanweijia.slimming.BR;
import cn.yanweijia.slimming.HeartRateActivity;
import cn.yanweijia.slimming.R;
import cn.yanweijia.slimming.custom.adapter.ListAdapter;
import cn.yanweijia.slimming.databinding.FragmentHealthBinding;

/**
 * @author weijia
 * @date 2017.09.24
 */
public class HealthFragment extends Fragment {
    //this view is used to save view stack
    private View rootView;
    private static final String TAG = "HealthFragment";
    private FragmentHealthBinding binding;
    private List<Map<String, Object>> list;


    public HealthFragment() {
        Log.d(TAG, "HealthFragment: Constructor");
    }

    /**
     * new instance
     *
     * @return
     */
    public static HealthFragment newInstance() {
        HealthFragment fragment = new HealthFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        Log.d(TAG, "newInstance: ");
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: rootView==null?  " + String.valueOf(rootView == null));
        return getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_health);
    }

    /**
     * use rootView to initial views
     *
     * @author weijia
     */
    private void initViews() {

        list = new ArrayList<>();
        int[] images = {R.drawable.heart_rate, R.drawable.blood_persure, R.drawable.blood_sugar, R.drawable.weight};
        int[] titles = {R.string.heart_rate, R.string.blood_persure, R.string.blood_sugar, R.string.weight};
        for (int i = 0; i < images.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", images[i]);
            map.put("title", getString(titles[i]));
            list.add(map);
        }
        ListAdapter<Map<String, Object>> adapter = new ListAdapter<>(getActivity(), list, R.layout.fragment_health_gridview_item, BR.map);
        binding.setAdapter(adapter);
        binding.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //listener
                int drawableId = (int) list.get(position).get("image");
                Log.d(TAG, "onItemClick: " + list.get(position).get("title"));
                switch (drawableId) {
                    case R.drawable.heart_rate:
                        startActivity(new Intent(getActivity(), HeartRateActivity.class)); //TODO:replace it
                        break;
                    case R.drawable.blood_persure:
                        startActivity(new Intent(getActivity(), null));
                        break;
                    case R.drawable.blood_sugar:
                        startActivity(new Intent(getActivity(), null));
                        break;
                    case R.drawable.weight:
                        startActivity(new Intent(getActivity(), null));
                        break;
                    default:
                        Log.d(TAG, "onItemClick: Err!" + position);
                }
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

}
