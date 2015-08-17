package com.lvyerose.eventbusdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.lvyerose.eventbusdemo.message.BaseMsgBean;

import de.greenrobot.event.EventBus;

@ContentView(R.layout.activity_two)
public class TwoActivity extends AppCompatActivity {
    private BaseMsgBean mMsg;
    @ViewInject(R.id.two_content_tv)
    TextView content_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.two_content_tv})
    public void onClicks(View view){
        switch (view.getId()){
            case R.id.two_content_tv :
                break;
        }
    }

    public void onEventMainThread(BaseMsgBean msg){
        mMsg = msg;
        if(msg != null){
            content_tv.setText(msg.getContent());
        }else{
            Toast.makeText(this, "接收参数为null", Toast.LENGTH_SHORT).show();
        }
    }

}
