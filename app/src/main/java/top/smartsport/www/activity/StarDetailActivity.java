package top.smartsport.www.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.view.annotation.ContentView;

import app.base.JsonUtil;
import app.base.MapConf;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;

@ContentView(R.layout.star_details)
public class StarDetailActivity extends BaseActivity {


    @Override
    protected void initView() {
        String data = getIntent().getStringExtra("data");
        MapConf.with(getBaseContext()).pair("name->tv_name").pair("team_name->tv_team").pair("cover_url->iv_top_pic").pair("introduce->tv_introduce_star").pair("stage->tv_week").source(data,this).match();
    }
}
