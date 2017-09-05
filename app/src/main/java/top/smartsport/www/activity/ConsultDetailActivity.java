package top.smartsport.www.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.view.annotation.ContentView;

import top.smartsport.www.R;
import top.smartsport.www.adapter.ConsultAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.widget.MyListView;
@ContentView(R.layout.consult_layout)
public class ConsultDetailActivity extends BaseActivity {
    private View mView;
    private MyListView lvConsult;


    @Override
    protected void initView() {
        back();

        lvConsult = (MyListView) findViewById(R.id.lv_consult);
        ConsultAdapter adapter = new ConsultAdapter();
        lvConsult.setAdapter(adapter);
        TextView title = (TextView) findViewById(R.id.tv_title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
