package top.smartsport.www.activity;

import android.text.TextUtils;
import android.util.Log;

import org.xutils.view.annotation.ContentView;

import java.util.List;

import app.base.MapConf;
import app.base.widget.ImageView;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;

@ContentView(R.layout.star_details)
public class StarDetailActivity extends BaseActivity {


    @Override
    protected void initView() {
        share();
        final String data = getIntent().getStringExtra("id");
        BaseActivity.callHttp(MapBuilder.build().add("action", "getRecommendPlayers").add("id",getIntent().getStringExtra("id")).get(), this, new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {


                MapConf.with(getBaseContext()).pair("players[0]-name->tv_name").pair("players[0]-team_name->tv_team").pair("players[0]-cover_url->iv_top_pic").pair("players[0]-introduce->tv_introduce_star").pair("players[0]-stage->tv_week").source(((NetEntity)result).getData().toString(),StarDetailActivity.this).toView();
                String title = !TextUtils.isEmpty(getTextString(R.id.tv_name)) ? getTextString(R.id.tv_name) : "球星名称";
                String shareText = !TextUtils.isEmpty(getTextString(R.id.tv_introduce_star)) ? getTextString(R.id.tv_introduce_star) : "球星介绍。图文介绍。";
                String shareUrl = !TextUtils.isEmpty(((ImageView)getView(R.id.iv_top_pic)).getUrl()) ? ((ImageView)getView(R.id.iv_top_pic)).getUrl() : "--";
                setSharetitle(title);
                setSharetxt(shareText);
                setShareurl(shareUrl);
            }
            @Override
            public void onFailure(Object result, List object) {
            }
            @Override
            public void onCallback(Object result, List object) {
            }
        });

    }
}
