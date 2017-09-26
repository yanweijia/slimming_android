package cn.yanweijia.slimming.diet;

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
public class DietFragment extends Fragment {
    //this view is used to save view stack
    private View rootView;
    private static final String TAG = "DietFragment";

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
     * @see cn.yanweijia.slimming.sport.SportFragment#getPersistentView(LayoutInflater, ViewGroup, Bundle, int)
     */
    public View getPersistentView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, int layout) {
        Log.d(TAG, "getPersistentView: rootView==null? " + String.valueOf(rootView == null));
        if (rootView == null) {
            rootView = inflater.inflate(layout, container, false);
            Log.d(TAG, "getPersistentView:  rootView is null, initial rootView");
            initViews();
            initDatas();
        } else {
            Log.d(TAG, "getPersistentView: removeRootView and add again");
            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if (viewGroup != null)
                viewGroup.removeView(rootView);
        }
        initViews();
        return rootView;
    }

}
