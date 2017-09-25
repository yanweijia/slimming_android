package cn.yanweijia.slimming.me;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.yanweijia.slimming.R;

/**
 * @author weijia
 * @date 2017.09.24
 */
public class MeFragment extends Fragment {
    public MeFragment() {

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
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        Bundle bundle = getArguments();
        //TextView textView = (TextView) view.findViewById(R.id.textView);
        return view;
    }
}
