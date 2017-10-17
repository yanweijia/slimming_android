package cn.yanweijia.slimming;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import cn.yanweijia.slimming.databinding.ActivityRunBinding;
import cn.yanweijia.slimming.entity.LocationRecordPoint;
import cn.yanweijia.slimming.entity.RunRecord;


/**
 * activity to show run record
 *
 * @author weijia
 */
public class ShowRunRecordActivity extends AppCompatActivity {
    private static final String TAG = "ShowRunRecordActivity";
    private ActivityRunBinding binding;
    private AMap aMap;
    private RunRecord runRecord = null;
    private List<LatLng> list;  //路线点

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_run);
        binding.map.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = binding.map.getMap();
        }
        runRecord = (RunRecord) getIntent().getSerializableExtra("runRecord");
        getLatLngs();
        initViews();
    }

    /**
     * initial views
     */
    private void initViews() {
        binding.stopPanel.setVisibility(View.GONE);
        binding.runPanel.setVisibility(View.GONE);
        binding.sharePanel.setVisibility(View.VISIBLE);

//        MyLocationStyle myLocationStyle;
//        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔,毫秒。
//        myLocationStyle.showMyLocation(true);
//        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);  //设置默认定位按钮是否显示，非必需设置。
//        aMap.setMyLocationEnabled(true);// 显示定位蓝点


        aMap.setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
            @Override
            public void onMapLoaded() {
                aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
                if (list != null && list.size() > 1)
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(list.get((list.size() - 1) / 2)));
            }
        });
        aMap.addPolyline(new PolylineOptions().addAll(list).width(10).geodesic(true).color(Color.GREEN));

        if (list != null && list.size() > 2) {
            aMap.addMarker(new MarkerOptions()
                    .position(list.get(0))
                    .title("起点")
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.startpoint))));
            aMap.addMarker(new MarkerOptions()
                    .position(list.get(list.size() - 1))
                    .title("终点")
                    .icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.endpoint))));
        }
        binding.distance.setText(new BigDecimal(Double.toString(runRecord.getDistance().floatValue() / 1000.0f)).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        binding.kcalorie.setText(runRecord.getCalorie().toString());
        binding.pace.setText(runRecord.getPace().toString());
        long seconds = (runRecord.getEndtime().getTime() - runRecord.getStarttime().getTime()) / 1000;
        String time = (seconds / 3600) + ":" + (seconds / 60 % 60) + ":" + (seconds % 60);
        binding.time.setText(time);


        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });
    }

    private void getLatLngs() {
        list = new ArrayList<>();
        if (runRecord == null)
            return;
        List<LocationRecordPoint> locationRecordPoints = runRecord.getRoad();
        for (LocationRecordPoint point : locationRecordPoints) {
            LatLng latLng = new LatLng(point.getLat(), point.getLng());
            list.add(latLng);
        }
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
                    startActivity(intent);
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
