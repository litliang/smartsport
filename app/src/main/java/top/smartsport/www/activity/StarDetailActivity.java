package top.smartsport.www.activity;

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
                setSharetitle(getTextString(R.id.tv_name));
                setSharetxt(getTextString(R.id.tv_introduce_star));
                setShareurl(((ImageView)getView(R.id.iv_top_pic)).getUrl());
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
