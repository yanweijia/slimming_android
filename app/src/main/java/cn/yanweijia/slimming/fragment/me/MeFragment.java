package cn.yanweijia.slimming.fragment.me;

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
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import cn.yanweijia.slimming.ChangePasswordActivity;
import cn.yanweijia.slimming.LoginActivity;
import cn.yanweijia.slimming.R;
import cn.yanweijia.slimming.UpdateUserInfoActivity;
import cn.yanweijia.slimming.dao.DBManager;
import cn.yanweijia.slimming.databinding.FragmentMeBinding;
import cn.yanweijia.slimming.entity.User;
import cn.yanweijia.slimming.utils.RequestUtils;

/**
 * @author weijia
 * @date 2017.09.24
 */
public class MeFragment extends Fragment {
    //this view is used to save view stack
    private View rootView;
    private static final String TAG = "MeFragment";
    private FragmentMeBinding binding;
    private MeFragmentHandler myHandler;
    private ObjectMapper objectMapper;

    /**
     * refresh user information success
     */
    private static final int REFRESH_USERINFO_SUCCESS = 1;
    /**
     * refresh user information fail
     */
    private static final int REFRESH_USERINFO_FAIL = 2;
    /**
     * request to update user information
     */
    private static final int UPDATE_USERINFO_REQUEST = 3;
    /**
     * update user info successs
     */
    public static final int UPDATE_USERINFO_SUCCESS = 4;
    /**
     * update user info fail
     */
    public static final int UPDATE_USERINFO_FAIL = 5;

    /**
     * request to change user's password
     */
    private static final int CHANGE_PASSWORD_REQUEST = 6;
    /**
     * change password success
     */
    public static final int CHANGE_PASSWORD_SUCCESS = 7;
    /**
     * change password fail
     */
    public static final int CHANGE_PASSWORD_FAIL = 8;

    public MeFragment() {
        DBManager.initSQLiteDB(getActivity());
        Log.d(TAG, "MeFragment: Constructor");
    }

    /**
     * new instance
     *
     * @return
     */
    public static MeFragment newInstance() {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        Log.d(TAG, "newInstance: ");
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: rootView==null?  " + String.valueOf(rootView == null));
        DBManager.initSQLiteDB(getActivity());
        return getPersistentView(inflater, container, savedInstanceState, R.layout.fragment_me);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //refresh user information
        switch (requestCode) {
            case UPDATE_USERINFO_REQUEST:
                switch (resultCode) {
                    case UPDATE_USERINFO_SUCCESS:
                        binding.swipeRefreshlayout.post(new Runnable() {
                            @Override
                            public void run() {
                                binding.swipeRefreshlayout.setRefreshing(true);
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        refreshUserInfo();
                                    }
                                }).start();
                            }
                        });
                        break;
                    default:
                }
                break;
            case CHANGE_PASSWORD_REQUEST:
                switch (resultCode){
                    case CHANGE_PASSWORD_SUCCESS:
                        DBManager.removeAllUser();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                        break;
                    case CHANGE_PASSWORD_FAIL:
                        break;
                }
                break;
            default:
                Log.d(TAG, "onActivityResult: requestCode:" + requestCode);
        }

    }

    /**
     * use rootView to initial views
     *
     * @author weijia
     */
    private void initViews() {
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DBManager.removeAllUser();
                Toast.makeText(getActivity(), R.string.logout_success, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });
        binding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), ChangePasswordActivity.class), CHANGE_PASSWORD_REQUEST);
            }
        });
        myHandler = new MeFragmentHandler();
        binding.circleImageViewMeHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //request update user information
                startActivityForResult(new Intent(getActivity(), UpdateUserInfoActivity.class), UPDATE_USERINFO_REQUEST);
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
                        refreshUserInfo();
                    }
                }).start();
            }
        });

        Log.d(TAG, "initViews: complete!");
    }

    private void refreshUserInfo() {
        try {
            String jsonResult = RequestUtils.getUserInfo(DBManager.getUser().getId());
            JSONObject jsonObject = new JSONObject(jsonResult);
            String message = jsonObject.getString("message");
            Message msg = new Message();
            Bundle bundle = new Bundle();
            if (jsonObject.getBoolean("success")) {
                String userJson = jsonObject.getString("User");
                msg.what = REFRESH_USERINFO_SUCCESS;
                bundle.putString("user", userJson);
            } else {
                msg.what = REFRESH_USERINFO_FAIL;
                Log.d(TAG, "run: User update fail:" + message);
                Toast.makeText(getActivity(), getString(R.string.refresh_userinfo_fail) + message, Toast.LENGTH_SHORT).show();
            }
            bundle.putString("message", message);
            msg.setData(bundle);
            myHandler.sendMessage(msg);
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(TAG, "refreshUserInfo() : Error!", e);
        } finally {
            // refresh complete;
            binding.swipeRefreshlayout.setRefreshing(false);
        }
    }

    /**
     * initial datas (after initViews)
     *
     * @author weijia
     */
    private void initDatas() {
        objectMapper = new ObjectMapper();
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
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_me, container, false);
            binding.setUser(DBManager.getUser());
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

    class MeFragmentHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_USERINFO_SUCCESS:
                    try {
                        String userJson = msg.getData().getString("user");
                        User user = objectMapper.readValue(userJson, User.class);
                        binding.setUser(user);
                        DBManager.saveUser(user);
                        Log.d(TAG, "run: User updated");
                        Toast.makeText(getActivity(), R.string.refresh_userinfo_complete, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Log.e(TAG, "handleMessage: ", e);
                        Toast.makeText(getActivity(), getString(R.string.refresh_userinfo_fail) + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    break;
                case REFRESH_USERINFO_FAIL:
                    String message = msg.getData().getString("message");
                    Toast.makeText(getActivity(), getString(R.string.refresh_userinfo_fail) + ":" + message, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "handleMessage: REFRESH_USERINFO_FAIL" + message);
                    break;
                default:
            }
        }
    }
}
