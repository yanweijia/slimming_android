package cn.yanweijia.slimming;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.yanweijia.slimming.databinding.ActivityRunBinding;

public class RunActivity extends AppCompatActivity {
    private static final String TAG = "RunActivity";
    private ActivityRunBinding binding;
    private AMap aMap;
    private ObjectMapper objectMapper;
    private List<AMapLocation> locationList;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener;

    //跑步状态记录
    private boolean isRunning = false;
    private Date startTime = null;
    private Date endTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_run);
        binding.map.onCreate(savedInstanceState);
        objectMapper = new ObjectMapper();
        locationList = new ArrayList<>();
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
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔,毫秒。
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);  //设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 显示定位蓝点
//        aMap.getUiSettings().setZoomControlsEnabled(false);     //取消缩放按钮

        //location change listener
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
//                onLocationChange(location);
            }
        });
        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                Location location = aMap.getMyLocation();
                LatLng latLng = null;
                if (null != location)
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latLng));
                aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
            }
        });

        //定位api
        mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
                onLocationChange(aMapLocation);
            }
        };
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化AMapLocationClientOption对象
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭定位缓存机制
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(mLocationListener);


        binding.startRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:启动定位
                isRunning = true;
                startTime = new Date();
                mLocationClient.startLocation();
                binding.runPanel.setVisibility(View.GONE);
                binding.sharePanel.setVisibility(View.GONE);
                binding.stopPanel.setVisibility(View.VISIBLE);
            }
        });
        binding.stopRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isRunning = false;
                endTime = new Date();
                mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
                //TODO:判断list不为空,再继续
                binding.sharePanel.setVisibility(View.VISIBLE);
                binding.stopPanel.setVisibility(View.GONE);
                binding.runPanel.setVisibility(View.GONE);
            }
        });
        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:截图并分享截图
            }
        });
    }

    /**
     * compare location , then recoard time and new location
     *
     * @param aMapLocation
     */
    private void onLocationChange(AMapLocation aMapLocation) {
        if (null == aMapLocation) {
            Log.d(TAG, "onLocationChange: Error ! location is null");
            return;
        }
        //定位失败
        if (aMapLocation.getErrorCode() != 0) {
            Log.e("AmapError", "location Error, ErrCode:"
                    + aMapLocation.getErrorCode() + ", errInfo:"
                    + aMapLocation.getErrorInfo());
            return;
        }
        String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(aMapLocation.getTime()));
        Log.d(TAG, "onLocationChange: " + time + ":" + aMapLocation.getLatitude() + "," + aMapLocation.getLongitude());
        if (!isRunning) {
            return;
        }
        if (locationList.isEmpty()) {
            locationList.add(aMapLocation);
            return;
        }
        AMapLocation lastEffectiveLocation = locationList.get(locationList.size() - 1);
        if (lastEffectiveLocation.getLatitude() != aMapLocation.getLatitude() || lastEffectiveLocation.getLongitude() != aMapLocation.getLongitude()) {
            locationList.add(aMapLocation);
        }
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
        if (mLocationClient.isStarted())
            mLocationClient.stopLocation();
        mLocationClient.onDestroy();
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
