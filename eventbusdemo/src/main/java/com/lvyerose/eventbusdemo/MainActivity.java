package com.lvyerose.eventbusdemo;

import android.content.Intent;
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

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @ViewInject(R.id.first_content_tv)
    TextView first_content_tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtils.inject(this);
//        EventBus.getDefault().register(this);
    }

    @OnClick({R.id.skip_pass_btn})
    public void onClicks(View view){
        switch (view.getId()){
            case R.id.skip_pass_btn :
                startActivity(new Intent(this, TwoActivity.class));
                break;
        }
    }

    public void onEvent(BaseMsgBean msg){
        if(msg != null){
            first_content_tv.setText(msg.getContent());
        }else{
            Toast.makeText(this, "接收参数为null", Toast.LENGTH_SHORT).show();
        }
    }

    public void postMsg(){
        EventBus.getDefault().post(new BaseMsgBean(1, "message-1", "content-1"));
    }




}
