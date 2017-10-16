package cn.yanweijia.slimming.fragment.analyze;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.yanweijia.slimming.R;
import cn.yanweijia.slimming.databinding.FragmentAnalyzeBinding;
import cn.yanweijia.slimming.utils.ChartUtils;

/**
 * @author weijia
 * @date 2017.09.24
 */
public class AnalyzeFragment extends Fragment {
    //this view is used to save view stack
    private View rootView;
    private static final String TAG = "AnalyzeFragment";
    private FragmentAnalyzeBinding binding;

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
        showChart();

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

    private void showChart() {
        List<ArrayList<Entry>> values = new ArrayList<>();
        ArrayList<Entry> values1 = new ArrayList<>();
        ArrayList<Entry> values2 = new ArrayList<>();

        values1.add(new Entry(4, 10));
        values1.add(new Entry(6, 15));
        values1.add(new Entry(9, 20));
        values1.add(new Entry(12, 5));
        values1.add(new Entry(15, 30));

        values2.add(new Entry(3, 110));
        values2.add(new Entry(6, 115));
        values2.add(new Entry(9, 130));
        values2.add(new Entry(12, 85));
        values2.add(new Entry(15, 90));
        values.add(values1);
        values.add(values2);
        List<String> titles = new ArrayList<>();
        titles.add("数据1");
        titles.add("数据2");
        List<Integer> colors = new ArrayList<>();
        colors.add(Color.BLACK);
        colors.add(Color.BLUE);
        ChartUtils.drawLineChart(getActivity(), binding.lineChart, "", "description", null, values, titles, colors);
    }
}