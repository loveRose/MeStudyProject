package com.lvyerose.baidutest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.navisdk.adapter.BNOuterTTSPlayerCallback;
import com.baidu.navisdk.adapter.BNRoutePlanNode;
import com.baidu.navisdk.adapter.BaiduNaviManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * author: lvyeRose
 * objective:
 * mailbox: lvyerose@163.com
 * time: 15/8/6 20:43
 */
@ContentView(R.layout.activity_navigate)
public class NavigationActivity extends Activity {
    public static final String TAG = "NaviSDkDemo";
    private static final String APP_FOLDER_NAME = "Bai";
    @ViewInject(R.id.wgsNaviBtn)
    private Button mWgsNaviBtn;
    @ViewInject(R.id.gcjNaviBtn)
    private Button mGcjNaviBtn;
    @ViewInject(R.id.bdmcNaviBtn)
    private Button mBdmcNaviBtn;
    private String mSDCardPath = null;
    private double latitude;
    private double longitude;
    public static final String ROUTE_PLAN_NODE = "routePlanNode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
        longitude = getIntent().getDoubleExtra("longitude" , 0.00);
        latitude = getIntent().getDoubleExtra("latitude" , 0.00);
        Toast.makeText(this , latitude+"-----" + longitude  , Toast.LENGTH_SHORT).show();
        initListener();
        if ( initDirs() ) {
            initNavi();
        }
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
//        super.onCreate(savedInstanceState, persistentState);
////
//
//    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initListener() {
        if ( mWgsNaviBtn != null ) {
            mWgsNaviBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if ( BaiduNaviManager.isNaviInited() ) {
                        routeplanToNavi(BNRoutePlanNode.CoordinateType.WGS84);
                    }
                }
            });
        }
        if ( mGcjNaviBtn != null ) {
            mGcjNaviBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if ( BaiduNaviManager.isNaviInited() ) {
                        routeplanToNavi(BNRoutePlanNode.CoordinateType.GCJ02);
                    }
                }
            });
        }
        if ( mBdmcNaviBtn != null ) {
            mBdmcNaviBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    if ( BaiduNaviManager.isNaviInited() ) {
                        routeplanToNavi(BNRoutePlanNode.CoordinateType.BD09_MC);
                    }
                }
            });
        }
    }

    private boolean initDirs() {
        mSDCardPath = getSdcardDir();
        if ( mSDCardPath == null ) {
            return false;
        }
        File f = new File(mSDCardPath, APP_FOLDER_NAME);
        if ( !f.exists() ) {
            try {
                f.mkdir();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    String authinfo = null;

    private void initNavi() {
        BaiduNaviManager.getInstance().setNativeLibraryPath(mSDCardPath + "/BaiduNaviSDK_SO");
        BaiduNaviManager.getInstance().init(this, mSDCardPath, APP_FOLDER_NAME,
                new BaiduNaviManager.NaviInitListener() {
                    @Override
                    public void onAuthResult(int status, String msg) {
                        if (0 == status) {
                            authinfo = "key校验成功!";
                        } else {
                            authinfo = "key校验失败, " + msg;
                        }
                        NavigationActivity.this.runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
//                                Toast.makeText(NavigationActivity.this, authinfo, Toast.LENGTH_LONG).show();
                            }
                        });

                    }
                    public void initSuccess() {
                        Toast.makeText(NavigationActivity.this, "百度导航引擎初始化成功", Toast.LENGTH_SHORT).show();
                    }

                    public void initStart() {
                        Toast.makeText(NavigationActivity.this, "百度导航引擎初始化开始", Toast.LENGTH_SHORT).show();
                    }

                    public void initFailed() {
                        Toast.makeText(NavigationActivity.this, "百度导航引擎初始化失败", Toast.LENGTH_SHORT).show();
                    }
                }, null /*mTTSCallback*/);
    }


    private String getSdcardDir() {
        if (Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory().toString();
        }
        return null;
    }

    private void routeplanToNavi(BNRoutePlanNode.CoordinateType coType) {
        BNRoutePlanNode sNode = null;
        BNRoutePlanNode eNode = null;
        switch(coType) {
            case GCJ02: {
                sNode = new BNRoutePlanNode(longitude ,latitude,
                        "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.39750, 39.90882,
                        "北京天安门", null, coType);
                break;
            }
            case WGS84: {
                sNode = new BNRoutePlanNode(116.300821,40.050969,
                        "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(116.397491,39.908749,
                        "北京天安门", null, coType);
                break;
            }
            case BD09_MC: {
                sNode = new BNRoutePlanNode(12947471,4846474,
                        "百度大厦", null, coType);
                eNode = new BNRoutePlanNode(12958160,4825947,
                        "北京天安门", null, coType);
                break;
            }
        }
        if (sNode != null && eNode != null) {
            List<BNRoutePlanNode> list = new ArrayList<BNRoutePlanNode>();
            list.add(sNode);
            list.add(eNode);
            BaiduNaviManager.getInstance().launchNavigator(this, list, 1, true, new DemoRoutePlanListener(sNode));
        }
    }

    public class DemoRoutePlanListener implements BaiduNaviManager.RoutePlanListener {

        private BNRoutePlanNode mBNRoutePlanNode = null;
        public DemoRoutePlanListener(BNRoutePlanNode node){
            mBNRoutePlanNode = node;
        }

        @Override
        public void onJumpToNavigator() {
            Intent intent = new Intent(NavigationActivity.this, BNDemoGuideActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable(ROUTE_PLAN_NODE, (BNRoutePlanNode) mBNRoutePlanNode);
            intent.putExtras(bundle);
            startActivity(intent);
        }
        @Override
        public void onRoutePlanFailed() {
            // TODO Auto-generated method stub

        }
    }

    private BNOuterTTSPlayerCallback mTTSCallback = new BNOuterTTSPlayerCallback() {

        @Override
        public void stopTTS() {
            // TODO Auto-generated method stub

        }

        @Override
        public void resumeTTS() {
            // TODO Auto-generated method stub

        }

        @Override
        public void releaseTTSPlayer() {
            // TODO Auto-generated method stub

        }

        @Override
        public int playTTSText(String speech, int bPreempt) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public void phoneHangUp() {
            // TODO Auto-generated method stub

        }

        @Override
        public void phoneCalling() {
            // TODO Auto-generated method stub

        }

        @Override
        public void pauseTTS() {
            // TODO Auto-generated method stub

        }

        @Override
        public void initTTSPlayer() {
            // TODO Auto-generated method stub

        }

        @Override
        public int getTTSState() {
            // TODO Auto-generated method stub
            return 0;
        }
    };
}
