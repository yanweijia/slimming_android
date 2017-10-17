package cn.yanweijia.slimming;

import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.yanweijia.slimming.dao.DBManager;
import cn.yanweijia.slimming.databinding.ActivityRunBinding;
import cn.yanweijia.slimming.entity.LocationRecordPoint;
import cn.yanweijia.slimming.entity.RunRecord;
import cn.yanweijia.slimming.entity.User;
import cn.yanweijia.slimming.utils.RequestUtils;

public class RunActivity extends Activity {
    private static final String TAG = "RunActivity";
    private ActivityRunBinding binding;
    private AMap aMap;
    private List<LocationRecordPoint> locationList;
    private RunRecord runRecord = null;
    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明定位回调监听器
    private AMapLocationListener mLocationListener;

    //跑步状态记录
    private boolean isRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_run);
        binding.map.onCreate(savedInstanceState);
        locationList = new ArrayList<>();
        runRecord = new RunRecord();
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
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();        //初始化AMapLocationClientOption对象
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setInterval(1000);        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setNeedAddress(true);        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setMockEnable(true);        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setHttpTimeOut(20000);        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setLocationCacheEnable(false);        //关闭定位缓存机制
        mLocationClient.setLocationOption(mLocationOption);        //给定位客户端对象设置定位参数
        mLocationClient.setLocationListener(mLocationListener);


        binding.startRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRun();
            }
        });
        binding.stopRun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopRun();
            }
        });
        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                share();

            }
        });
    }

    /**
     * start run
     */
    private void startRun() {
        //启动定位
        locationList.clear();
        isRunning = true;
        removeAllMarker();
        runRecord.setStarttime(new Date());
        mLocationClient.startLocation();
        binding.runPanel.setVisibility(View.GONE);
        binding.sharePanel.setVisibility(View.GONE);
        binding.stopPanel.setVisibility(View.VISIBLE);
    }

    /**
     * stop run
     */
    private void stopRun() {
        isRunning = false;
        runRecord.setEndtime(new Date());
        mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁
        //判断list不为空,再继续,无记录点则返回开始跑步layout
        if (locationList.size() < 2) {
            binding.sharePanel.setVisibility(View.GONE);
            binding.stopPanel.setVisibility(View.GONE);
            binding.runPanel.setVisibility(View.VISIBLE);
            return;
        }
        binding.sharePanel.setVisibility(View.VISIBLE);
        binding.stopPanel.setVisibility(View.GONE);
        binding.runPanel.setVisibility(View.GONE);
        //上传运动结果,更新share界面
        runRecord.setId(null);
        runRecord.setRoad(locationList);    //路径点
        BigDecimal distance = new BigDecimal(Double.toString(calcDistance()));
        runRecord.setDistance(distance);
        User user = DBManager.getUser();
        user = user == null ? new User() : user;
        runRecord.setUserId(user.getId());
        BigDecimal userWeight = user.getWeight();
        if (null == userWeight || userWeight.intValue() == 0) {
            userWeight = new BigDecimal("60");
        }
        runRecord.setCalorie(userWeight.multiply(distance.divide(new BigDecimal("1000"), 3, BigDecimal.ROUND_HALF_UP)).multiply(new BigDecimal("1.036")).setScale(2, BigDecimal.ROUND_HALF_UP));
        BigDecimal speed = distance.divide(new BigDecimal("1000"), 10, BigDecimal.ROUND_HALF_UP).divide(new BigDecimal(Double.toString((runRecord.getEndtime().getTime() - runRecord.getStarttime().getTime()) / 1000.0f / 60.0f)), 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
        BigDecimal pace = new BigDecimal("0.0");
        if (distance.doubleValue() != 0) {
            pace = new BigDecimal(Double.toString((runRecord.getEndtime().getTime() - runRecord.getStarttime().getTime()) / 1000.0f / 60f)).divide(distance.divide(new BigDecimal("1000"), 10, BigDecimal.ROUND_HALF_UP), 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
        }
        runRecord.setPace(pace);
        runRecord.setSpeed(speed);
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonResult = RequestUtils.uploadRunRecord(runRecord);
                Log.d(TAG, "run: JSONResult:" + jsonResult);
            }
        }).start();
        binding.distance.setText(new BigDecimal(Double.toString(distance.floatValue() / 1000.0f)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        binding.kcalorie.setText(runRecord.getCalorie().toString());
        binding.pace.setText(runRecord.getPace().toString());
        long seconds = (runRecord.getEndtime().getTime() - runRecord.getStarttime().getTime()) / 1000;
        String time = (seconds / 3600) + ":" + (seconds / 60 % 60) + ":" + (seconds % 60);
        binding.time.setText(time);
        LocationRecordPoint startPoint, endPoint;
        startPoint = locationList.get(0);
        endPoint = locationList.get(locationList.size() - 1);
        aMap.addMarker(new MarkerOptions()
                .position(new LatLng(endPoint.getLat(), endPoint.getLng()))
                .title("终点")
                .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.endpoint))));
    }

    /**
     * share result
     */
    private void share() {
        //截图并分享截图
        aMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
            @Override
            public void onMapScreenShot(Bitmap bitmap) {
                saveScreenShotAndShare(bitmap, binding.container, binding.map, binding.sharePanel);
            }

            @Override
            public void onMapScreenShot(Bitmap bitmap, int i) {

            }
        });
    }

    /**
     * remove all markers
     */
    private void removeAllMarker() {
        List<Marker> markers = aMap.getMapScreenMarkers();
        for (Marker marker : markers) {
            marker.remove();
        }

    }

    /**
     * calculate run distance,(meter)
     *
     * @return
     */
    private float calcDistance() {
        float distance = 0.0f;
        if (locationList.size() < 2)
            return distance;
        LatLng lastLatLng, nowLatLng;
        LocationRecordPoint firstLocation = locationList.get(0);
        lastLatLng = new LatLng(firstLocation.getLat(), firstLocation.getLng());
        for (int i = 1; i < locationList.size() - 1; i++) {
            LocationRecordPoint point = locationList.get(i);
            nowLatLng = new LatLng(point.getLat(), point.getLng());
            distance += AMapUtils.calculateLineDistance(lastLatLng, nowLatLng);
            lastLatLng = nowLatLng;
        }
        return distance;
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
        //begin location
        if (locationList.isEmpty()) {
            locationList.add(new LocationRecordPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude(), aMapLocation.getTime()));
            //标记起点
            aMap.addMarker(new MarkerOptions()
                    .position(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude()))
                    .title("起点")
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.startpoint))));
            return;
        }
        LocationRecordPoint lastEffectiveLocation = locationList.get(locationList.size() - 1);
        if (lastEffectiveLocation.getLat() == aMapLocation.getLatitude() && lastEffectiveLocation.getLng() == aMapLocation.getLongitude()) {
            return;
        }
        locationList.add(new LocationRecordPoint(aMapLocation.getLatitude(), aMapLocation.getLongitude(), aMapLocation.getTime()));

        LocationRecordPoint oldPoint = locationList.get(locationList.size() - 2),
                newPoint = locationList.get(locationList.size() - 1);
        LatLng oldLatLng = new LatLng(oldPoint.getLat(), oldPoint.getLng()),
                newLatLng = new LatLng(newPoint.getLat(), newPoint.getLng());
        aMap.addPolyline(new PolylineOptions().add(oldLatLng, newLatLng).width(10).geodesic(true).color(Color.GREEN));
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


    /**
     * 组装地图截图和其他View截图，并且将截图存储在本地sdcard，需要注意的是目前提供的方法限定为MapView与其他View在同一个ViewGroup下
     *
     * @param bitmap        地图截图回调返回的结果
     * @param viewContainer MapView和其他要截图的View所在的父容器ViewGroup
     * @param mapView       MapView控件
     * @param views         其他想要在截图中显示的控件
     * @return 存放位置
     */
    public void saveScreenShotAndShare(final Bitmap bitmap, final ViewGroup viewContainer, final MapView mapView, final View... views) {
        final String name = "screenshot.png";
        final String screenShotPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + name;
        new Thread() {
            public void run() {

                Bitmap screenShotBitmap = getMapAndViewScreenShot(bitmap, viewContainer, mapView, views);
                if (Environment.getExternalStorageState().
                        equals(Environment.MEDIA_MOUNTED)) {

                    File file = new File(screenShotPath);
                    try {
                        FileOutputStream outputStream = new FileOutputStream(file);
                        screenShotBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

                        //根据自己需求，如果外边对bitmp还有别的需求就不要recycle的
                        screenShotBitmap.recycle();
                        bitmap.recycle();
                        outputStream.close();
                    } catch (Exception e) {
                        Log.e(TAG, "run: ", e);
                    }
                    //这种方式是直接分享
//                    Intent intent = new Intent(Intent.ACTION_SEND);
//                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(screenShotPath));
//                    intent.setType("image/png");
//                    startActivity(Intent.createChooser(intent, getString(R.string.share)));
                    //这种方式是用系统图库打开刚才的照片
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "image/png");
                    RunActivity.this.startActivity(intent);
                }
            }

        }.start();
    }

    /**
     * 组装地图截图和其他View截图，需要注意的是目前提供的方法限定为MapView与其他View在同一个ViewGroup下
     *
     * @param bitmap        地图截图回调返回的结果
     * @param viewContainer MapView和其他要截图的View所在的父容器ViewGroup
     * @param mapView       MapView控件
     * @param views         其他想要在截图中显示的控件
     */
    public Bitmap getMapAndViewScreenShot(Bitmap bitmap, ViewGroup viewContainer, MapView mapView, View... views) {
        int width = viewContainer.getWidth();
        int height = viewContainer.getHeight();
        final Bitmap screenBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(screenBitmap);
        canvas.drawBitmap(bitmap, mapView.getLeft(), mapView.getTop(), null);
        for (View view : views) {
            view.setDrawingCacheEnabled(true);
            canvas.drawBitmap(view.getDrawingCache(), view.getLeft(), view.getTop(), null);
        }

        return screenBitmap;
    }


}
