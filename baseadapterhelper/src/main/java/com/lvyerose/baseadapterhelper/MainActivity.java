package com.lvyerose.baseadapterhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.lvyerose.baseadapterhelper.base.BaseAdapterHelper;
import com.lvyerose.baseadapterhelper.base.QuickAdapter;
import com.lvyerose.baseadapterhelper.bean.MeDatas;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private QuickAdapter<MeDatas> adapter;
    private List<MeDatas> list;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.listview);
        getDatas();
        adapter = new QuickAdapter<MeDatas>(this , R.layout.item_adapter , list ) {
            @Override
            protected void convert(BaseAdapterHelper helper, MeDatas item) {
                helper.setText(R.id.item_title , item.getTitle())
                        .setText(R.id.item_content , item.getContent());
            }
        };
        listView.setAdapter(adapter);

    }

    public void getDatas(){
        list = new ArrayList<>();
        for(int i=0;i<10;i++){
            list.add(new MeDatas("好屌的标题"+i , "有了标题就要有内容，这也是好屌的内容"+i ,""));
        }

    }
}
