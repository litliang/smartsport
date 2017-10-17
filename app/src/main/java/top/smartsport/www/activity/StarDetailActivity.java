package top.smartsport.www.activity;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import app.base.widget.ImageView;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.StarDetail;
import top.smartsport.www.utils.ImageUtil;

@ContentView(R.layout.star_details)
public class StarDetailActivity extends BaseActivity {

    @ViewInject(R.id.iv_top_pic)
    private ImageView iv_top_pic;
    @ViewInject(R.id.tv_name)
    private TextView tv_name;
    @ViewInject(R.id.tv_team)
    private TextView tv_team;
    @ViewInject(R.id.tv_introduce_star)
    private WebView tv_introduce_star;
    @ViewInject(R.id.tv_week)
    private TextView tv_week;
    @ViewInject(R.id.fl_loading)
    private FrameLayout fl_loading;

    @Override
    protected void initView() {
        share();
        final String data = getIntent().getStringExtra("id");
        fl_loading.setVisibility(View.VISIBLE);
        BaseActivity.callHttp(MapBuilder.build().add("action", "getRecommendPlayers").add("id",getIntent().getStringExtra("id")).get(), getView(R.id.content), new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {
                fl_loading.setVisibility(View.GONE);
                Gson gson = new Gson();
                String data = ((NetEntity)result).getData().toString();
                String coverUrl = null;
                try {
                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray players = jsonObject.optJSONArray("players");
                    if(players != null && players.length() > 0) {
                        JSONObject playersItem = players.optJSONObject(0);
                        StarDetail starDetail = top.smartsport.www.utils.JsonUtil.jsonToEntity(playersItem.toString(), StarDetail.class);
                        if(starDetail != null) {
                            tv_name.setText(starDetail.getName());
                            tv_team.setText(starDetail.getTeam_name());
                            String introduce = starDetail.getIntroduce() ;
                            introduce = introduce.replace("<img", "<img style='max-width:100%;height:auto;'");
                            tv_introduce_star.loadData(introduce, "text/html;charset=UTF-8", null);
                            tv_week.setText(starDetail.getStage());
                            coverUrl = starDetail.getCover_url();
                            ImageLoader.getInstance().displayImage(coverUrl, iv_top_pic, ImageUtil.getOptions(), ImageUtil.getImageLoadingListener(true));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                RelativeLayout l = new RelativeLayout(getBaseContext());


//                MapConf.with(getBaseContext()).pair("players[0]-name->tv_name").pair("players[0]-team_name->tv_team").pair("players[0]-cover_url->iv_top_pic").pair("players[0]-introduce->tv_introduce_star").pair("players[0]-stage->tv_week").source(((NetEntity)result).getData().toString(),StarDetailActivity.this).toView();
                String title = !TextUtils.isEmpty(getTextString(R.id.tv_name)) ? getTextString(R.id.tv_name) : "球星名称";
                String shareText = !TextUtils.isEmpty(((WebView)getView(R.id.tv_introduce_star)).getTitle()) ? ((WebView)getView(R.id.tv_introduce_star)).getTitle() : "球星介绍。图文介绍。";
                String shareUrl = coverUrl;
                setSharetitle(title);
                setSharetxt(shareText);
                setShareurl(shareUrl);
            }
            @Override
            public void onFailure(Object result, List object) {
                fl_loading.setVisibility(View.GONE);
            }
            @Override
            public void onCallback(Object result, List object) {
            }
        });

    }
}
