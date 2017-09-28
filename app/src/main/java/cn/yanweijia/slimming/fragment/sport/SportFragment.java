package cn.yanweijia.slimming.fragment.sport;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yanweijia.slimming.R;

/**
 * @author weijia
 * @date 2017.09.24
 */
public class SportFragment extends Fragment {
    // this view is used to save view stack
    private View rootView;
    private static final String TAG = "SportFragment";

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
     * @author weijia
     */
    private void initViews(){


        Log.d(TAG, "initViews: complete!");
    }

    /**
     * initial datas (after initViews)
     * @author weijia
     */
    private void initDatas(){

        Log.d(TAG, "initDatas: complete!");
    }


    /**
     * get saved view stack <br/>
     * @author weijia
     * @ref <a href="http://blog.csdn.net/harvic880925/article/details/45013501">
     *      Fragment详解之六——如何监听fragment中的回退事件与怎样保存fragment状态
     *      </a>
     *
     * @param inflater           LayoutInflater
     * @param container          ViewGroup
     * @param savedInstanceState Bundle
     * @param layout             layout resource id
     * @return rootView
     */
    public View getPersistentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, int layout) {
        Log.d(TAG, "getPersistentView: rootView==null? "  + String.valueOf(rootView == null));
        if (rootView == null) {
            // Inflate the layout for this fragment
            rootView = inflater.inflate(layout, container, false);
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
            if(viewGroup != null)
                viewGroup.removeView(rootView);
        }
        return rootView;
    }


}
