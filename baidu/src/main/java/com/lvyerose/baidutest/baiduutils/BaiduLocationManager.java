package com.lvyerose.baidutest.baiduutils;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;


/**
 * 功能：提供定位服务
 * 
 * @author lvyerose
 * 使用方法： 
 * 		BaiduLocationManager.init(getApplicationContext());
 *      BaiduLocationManager manager = BaiduLocationManager.getInstance();
 *      //回调重写方法中获取经纬度
 *      manager.setCallBack(new LocationListenerCallBack());
 *      //开始定位
 *      manager.startLocation();
 *
 *
 *      //获取结果后
 *      manager.stopLocation();
 *
 * 
 */
public class BaiduLocationManager {
	private static BaiduLocationManager manager;
	private LocationClient mLocationClient;
	private MyLocationListener mMyLocationListener;
	private static Context context;
	public LocationListenerCallBack callBack;
	private LocationStrAddCallBack addStrCallBack;

	public static void init(Context context) {
		BaiduLocationManager.context = context;
	}

	public static BaiduLocationManager getInstance() {
		if (manager == null) {
			manager = new BaiduLocationManager();
		}
		return manager;
	}

	public void startLocation() {
		if (mLocationClient != null) {
			mLocationClient.start();
		}
	}

	public void stopLocation() {
		if (mLocationClient != null) {
            mLocationClient.stop();
		}
	}

	private BaiduLocationManager() {
		mLocationClient = new LocationClient(context);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		initLocation();
	}

	/**
	 * 初始化参数
	 */
	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("gcj02");// 返回的定位结果是百度经纬度，默认值gcj02
		int span = 1000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
	}

	public class MyLocationListener implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (callBack != null) {
				callBack.locationListenerCallBack(location.getLongitude(),
						location.getLatitude());
			}
			if(addStrCallBack !=null){
				addStrCallBack.locationStrAdd(location.getProvince(),
						location.getCity(),location.getDistrict()
						,location.getStreet()+location.getStreetNumber());
			}
		}

	}

	//设置经纬度回调  在start定位之前调用
	public void setCallBack(LocationListenerCallBack callBack) {
		this.callBack = callBack;
	}
	//设置地理位置回调  在start定位之前调用
	public void setLocationAddCallBack(LocationStrAddCallBack addStrCallBack) {
		this.addStrCallBack = addStrCallBack;
	}

	//获取经纬度回调
	public interface LocationListenerCallBack {
		void locationListenerCallBack(double longitude, double latitude);
	}
	//获取地理位置回调
	public interface LocationStrAddCallBack {
		void locationStrAdd(String province, String city, String district, String street);
	}

}
