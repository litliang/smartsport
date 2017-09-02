package top.smartsport.www.activity;

import android.content.Intent;
import android.view.View;

import org.xutils.view.annotation.ContentView;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;

/**
 * Created by Aaron on 2017/7/28.
 * 我的球队
 */
@ContentView(R.layout.activity_wdqd)
public class WDQDActivity extends BaseActivity {


    @Override
    protected void initView() {
        back();
        findViewById(R.id.addteam).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplication(),AddMemberActivity.class));
            }
        });
    }
}
