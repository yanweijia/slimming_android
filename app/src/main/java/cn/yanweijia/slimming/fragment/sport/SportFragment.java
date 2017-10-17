package cn.yanweijia.slimming.fragment.sport;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.yanweijia.slimming.BR;
import cn.yanweijia.slimming.R;
import cn.yanweijia.slimming.RunActivity;
import cn.yanweijia.slimming.ShowRunRecordActivity;
import cn.yanweijia.slimming.custom.adapter.ListAdapter;
import cn.yanweijia.slimming.databinding.FragmentSportBinding;
import cn.yanweijia.slimming.entity.RunRecord;
import cn.yanweijia.slimming.entity.RunRecordUpload;
import cn.yanweijia.slimming.utils.EntityConverter;
import cn.yanweijia.slimming.utils.RequestUtils;

/**
 * @author weijia
 * @date 2017.09.24
 */
public class SportFragment extends Fragment {
    // this view is used to save view stack
    private FragmentSportBinding binding;
    private View rootView;
    private static final String TAG = "SportFragment";
    private Handler handler;
    private ObjectMapper objectMapper;
    private List<RunRecord> list = new ArrayList<>(); //run record adapter

    private static final int REFRESH_RUN_RECORD_SUCCESS = 1;
    private static final int REFRESH_RUN_RECORD_FAIL = 2;

    public SportFragment() {
        Log.d(TAG, "SportFragment: Constructor");
    }

    /**
     * new instance
     *
     * @return
     */
    public static SportFragment newInstance() {
        SportFragment fragment = new SportFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        Log.d(TAG, "newInstance: ");
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: rootView==null? " + String.valueOf(rootView == null));
        return getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_sport);
    }


    /**
     * use rootView to initial views
     *
     * @author weijia
     */
    private void initViews() {
        objectMapper = new ObjectMapper();
        handler = new SportFragmentHandler();
        binding.run.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), RunActivity.class));
            }
        });

        binding.swipeRefreshlayout.setRefreshing(false);
        // set swipe circle progress bar's color, default color is WHITE.
        binding.swipeRefreshlayout.setProgressBackgroundColorSchemeResource(android.R.color.white);
        // set swipe color theme
        binding.swipeRefreshlayout.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimary, R.color.colorPrimaryDark);
        // add refresh listener
        binding.swipeRefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // begin refreshing
                binding.swipeRefreshlayout.setRefreshing(true);

                // main thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "SwipeRefreshLayout run: refreshing new data");
                        refreshRunRecord();
                    }
                }).start();
            }
        });
        binding.gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: ");
                Intent intent = new Intent(getActivity(), ShowRunRecordActivity.class);
                intent.putExtra("runRecord", list.get(position));
                startActivity(intent);
            }
        });

        refreshRunRecord();
        Log.d(TAG, "initViews: complete!");
    }

    /**
     * refresh run record
     */
    private void refreshRunRecord() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String jsonResult = RequestUtils.listRunRecord(new Date(0L), new Date(99999999999999L));
                    JSONObject jsonObject = new JSONObject(jsonResult);
                    String message = jsonObject.getString("message");
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    if (jsonObject.getBoolean("success")) {
                        String runRecordJSON = jsonObject.getString("runRecord");
                        msg.what = REFRESH_RUN_RECORD_SUCCESS;
                        bundle.putString("runRecord", runRecordJSON);
                    } else {
                        msg.what = REFRESH_RUN_RECORD_FAIL;
                        Log.d(TAG, "run: RunRecord update fail:" + message);
                        Toast.makeText(getActivity(), getString(R.string.refresh_runrecord_complete) + message, Toast.LENGTH_SHORT).show();
                    }
                    bundle.putString("message", message);
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "refreshRunRecord() : Error!", e);
                } finally {
                    // refresh complete;
                    //binding.swipeRefreshlayout.setRefreshing(false);
                }
            }
        }).start();
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
     * @param inflater           LayoutInflater
     * @param container          ViewGroup
     * @param savedInstanceState Bundle
     * @param layout             layout resource id
     * @return rootView
     * @author weijia
     * @ref <a href="http://blog.csdn.net/harvic880925/article/details/45013501">
     * Fragment详解之六——如何监听fragment中的回退事件与怎样保存fragment状态
     * </a>
     */
    public View getPersistentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, int layout) {
        Log.d(TAG, "getPersistentView: rootView==null? " + String.valueOf(rootView == null));
        if (rootView == null) {
            // Inflate the layout for this fragment
            binding = DataBindingUtil.inflate(inflater, layout, container, false);
            rootView = binding.getRoot();
            //init mapView
            Log.d(TAG, "getPersistentView:  rootView is null, initial rootView");
            initViews();
            initDatas();
        } else {
            // Do not inflate the layout again.
            // The returned View of onCreateView will be added into the fragment.
            // However it is not allowed to be added twice even if the parent is same.
            // So we must remove rootView from the existing parent view group
            // (it will be added back).
            Log.d(TAG, "getPersistentView: removeRootView and add again");
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if (viewGroup != null)
                viewGroup.removeView(rootView);
        }
        return rootView;
    }

    class SportFragmentHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_RUN_RECORD_SUCCESS:
                    binding.swipeRefreshlayout.setRefreshing(false);
                    try {
                        list.clear();
                        String recordJsonArray = msg.getData().getString("runRecord");
                        JSONArray jsonArray = new JSONArray(recordJsonArray);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            RunRecordUpload runRecordUpload = objectMapper.readValue(jsonArray.getString(i), RunRecordUpload.class);
                            list.add(0, EntityConverter.convertRunRecordUploadFormatToNormal(runRecordUpload));//倒序插入,最近一次的跑步记录在最前面
                        }
                        ListAdapter<RunRecord> adapter = new ListAdapter<>(getActivity(), list, R.layout.run_record_item, BR.runRecord);
                        binding.setAdapter(adapter);
                        Log.d(TAG, "run: run record updated");
                        Toast.makeText(getActivity(), R.string.refresh_runrecord_complete, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e(TAG, "handleMessage: ", e);
                        Toast.makeText(getActivity(), getString(R.string.refresh_runrecord_fail) + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case REFRESH_RUN_RECORD_FAIL:
                    binding.swipeRefreshlayout.setRefreshing(false);

                    break;
                default:
            }
        }
    }

}
