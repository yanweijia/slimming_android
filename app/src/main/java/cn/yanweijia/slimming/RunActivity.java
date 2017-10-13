package cn.yanweijia.slimming;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.MyLocationStyle;

import cn.yanweijia.slimming.databinding.ActivityRunBinding;

public class RunActivity extends AppCompatActivity {
    private static final String TAG = "RunActivity";
    private ActivityRunBinding binding;
    private AMap aMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_run);
        binding.map.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = binding.map.getMap();
        }
        initViews();
        initDatas();
        Log.d(TAG, "onCreate: ");
    }


    /**
     * initial View
     */
    private void initViews() {
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔,毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);  //设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 显示定位蓝点
    }

    /**
     * initial data
     */
    private void initDatas() {

    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        super.onDestroy();
        binding.map.onDestroy();
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume: ");
        super.onResume();
        binding.map.onResume();
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
        binding.map.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "onSaveInstanceState: ");
        super.onSaveInstanceState(outState);
        binding.map.onSaveInstanceState(outState);
    }
}
