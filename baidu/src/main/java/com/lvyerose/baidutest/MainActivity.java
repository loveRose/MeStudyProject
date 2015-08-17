package com.lvyerose.baidutest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lvyerose.baidutest.baiduutils.BaiduLocationManager;
import com.lvyerose.baidutest.beans.LocationBean;

import java.util.ArrayList;

/**
 * author: lvyeRose
 * objective: 百度地图
 * 定位 -- 模拟上传定位信息给服务器并获取服务器返回的周围坐标集
 * 进行添加覆盖物组
 * 并给每个覆盖物添加对应点击事件展示对应信息
 *
 * mailbox: lvyerose@163.com
 * time: 15/7/29 11:42
 */
public class MainActivity extends AppCompatActivity {
    @ViewInject(R.id.bmapView)
    MapView mMapView;
    BaiduMap mBaiduMap;
    private double latitude;
    private double longitude;
    ArrayList<LocationBean> listBeans = new ArrayList<>();  //存储位置实体   由服务器返回
    private InfoWindow mInfoWindow; //信息弹窗



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);
        mBaiduMap = mMapView.getMap();
        setMap();

    }

    /**
     * 设置地图默认中心 可以由定位获得
     * 并且以此中心进行放大比例
     */
    public void setMap(){
        BaiduLocationManager.init(getApplicationContext());
        final BaiduLocationManager manager = BaiduLocationManager.getInstance();
        //回调重写方法中获取经纬度
        manager.setCallBack(new BaiduLocationManager.LocationListenerCallBack() {
            @Override
            public void locationListenerCallBack(double longitude, double latitude) {
                MainActivity.this.longitude = longitude;
                MainActivity.this.latitude = latitude;
                //设置放大倍数
                LatLng ll = new LatLng(latitude,
                        longitude);
                float f = mBaiduMap.getMaxZoomLevel();//19.0
                //float m = mBaiduMap.getMinZoomLevel();//3.0
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, f - 3);
                mBaiduMap.animateMapStatus(u);

                //默念获取服务器参数的所有点  并进行添加
                getData(latitude, longitude);
                addMarker(listBeans);
                setMarkerLinsetener();

                //关闭定位
                manager.stopLocation();

            }
        });
        //开始定位
        manager.startLocation();

    }

    //模拟获取数据集
    public void getData(double latitude , double longitude){
        for(int i=0;i<10;i++){
            LocationBean bean = new LocationBean(latitude+i/1000.00 , longitude+i/1000.00 , "第几个？-- 第"+ i +"个");
            listBeans.add(bean);
        }
    }
    //获取数据后进行添加覆盖物
    public void addMarker(ArrayList<LocationBean> list){
        if(list == null || list.size() ==0){
            return ;
        }
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        for(int i=0;i<list.size();i++){
            Bundle bundle = new Bundle();
            bundle.putCharSequence("info" , list.get(i).getInfo()); //添加对应数据的额外信息
            LatLng point = new LatLng(listBeans.get(i).getLatitude(),listBeans.get(i).getLongitude());
            OverlayOptions option = new MarkerOptions()
                    .position(point)
                    .icon(bitmap)
                    .extraInfo(bundle);
           mBaiduMap.addOverlay(option);
        }
    }

    public void setMarkerLinsetener(){
        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener(){

            @Override
            public boolean onMarkerClick(Marker marker) {           //统一marker点击事件
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_item , null);   //加载点击的弹窗view视图
                TextView tv = (TextView) view.findViewById(R.id.marker_pop_tv);     //对view视图中的控件进行实例化  方便后面对其进行赋值等操作
                view.setOnClickListener(new View.OnClickListener() {        //弹出的整个窗体被点击  这里进行关闭窗体
                    @Override
                    public void onClick(View v) {
                        mBaiduMap.hideInfoWindow();
                    }
                });
                String content = marker.getExtraInfo().getString("info"); //取出对应存储信息
                tv.setText(content + "\n" + "点击我就消失！\n\n");
                LatLng ll = marker.getPosition();       //pop加载的位置由当前点击的marker位置决定
                mInfoWindow = new InfoWindow(view, ll, -47);
                mBaiduMap.showInfoWindow(mInfoWindow);   //展现view
                return false;
            }
        });
    }

    @OnClick(R.id.to_native_btn)
    public void onClickToNavigation(View view){
        Intent intent = new Intent(this , NavigationActivity.class);
        intent.putExtra("longitude" ,this.longitude );
        intent.putExtra("latitude" ,this.latitude );
        startActivity(intent);
    }



}
