package top.smartsport.www.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.actions.Fav;
import top.smartsport.www.adapter.CoachAdapter;
import top.smartsport.www.adapter.TrainningAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.CoachInfoCourse;
import top.smartsport.www.bean.CoachInfoDetail;
import top.smartsport.www.bean.Coaches;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.listener.OnRecyclerViewItemListener;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.JsonUtil;
import top.smartsport.www.widget.MyListView;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

@ContentView(R.layout.coach_details)
public class CoachDetailActivity extends BaseActivity implements OnRecyclerViewItemListener {
    private boolean isAll;
    private RegInfo regInfo;
    private TokenInfo tokenInfo;
    private String client_id;
    private String state;
    private String url;
    private String access_token;
    private String id;
    @ViewInject(R.id.lv_training)
    private MyListView lvTraining;
    @ViewInject(R.id.rc_coach)
    private RecyclerView rcCoach;
    @ViewInject(R.id.tv_introduce)
    private TextView introduce;
    @ViewInject(R.id.tv_see_more)
    private TextView seeMore;
    @ViewInject(R.id.iv_top_pic)
    private ImageView ivTop;
    @ViewInject(R.id.tv_name)
    private TextView tvName;
    @ViewInject(R.id.tv_team)
    private TextView tvTeam;
    @ViewInject(R.id.tv_introduce)
    private TextView tvIntroduce;
    private TrainningAdapter trainingAdapter;
    private CoachAdapter coachAdapter;


    public void initView() {
        Coaches coach = (Coaches) getIntent().getSerializableExtra("data");
        id = coach.getId();
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();
        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();
        back();
        trainingAdapter = new TrainningAdapter();
        lvTraining.setAdapter(trainingAdapter);
        LinearLayoutManager linearLayoutManagerHorizontal = new LinearLayoutManager(this);
        linearLayoutManagerHorizontal.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcCoach.setLayoutManager(linearLayoutManagerHorizontal);
        coachAdapter = new CoachAdapter();
        rcCoach.setAdapter(coachAdapter);
        coachAdapter.setOnRecyclerViewItemListener(this);
        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAll) {
                    introduce.setMaxLines(4);
                    introduce.setEllipsize(TextUtils.TruncateAt.END);
                } else {
                    introduce.setMaxLines(Integer.MAX_VALUE);
                    introduce.setEllipsize(null);
                }
                isAll = !isAll;
            }
        });
        getData();
    }

    private void getData() {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "getCoachDetail");
            json.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                showToast(message);
            }

            @Override
            public void onSuccess(NetEntity entity) {
                String data = entity.getData().toString();
                CoachInfoDetail details =  JsonUtil.jsonToEntity(app.base.JsonUtil.findJsonLink("detail",data).toString(),CoachInfoDetail.class);
                List<CoachInfoCourse> course =  JsonUtil.jsonToEntityList(app.base.JsonUtil.findJsonLink("course",data).toString(), CoachInfoCourse.class);
                List<Coaches> others =  JsonUtil.jsonToEntityList(app.base.JsonUtil.findJsonLink("other_coach",data).toString(), Coaches.class);
                ImageLoader.getInstance().displayImage(details.getHeader_url(), ivTop, ImageUtil.getOptions(), ImageUtil.getImageLoadingListener(true));
                tvName.setText(details.getName());
                tvTeam.setText(details.getTeam_name());
                introduce.setText(Html.fromHtml(details.getIntroduce()));
                trainingAdapter.setData(course);
                coachAdapter.setData(others);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((ScrollView) findViewById(R.id.scroll)).scrollTo(0, 0);
    }

    @Override
    public void onItemClickListener(View view, int position) {
        Intent intent = new Intent(this,CoachDetailActivity.class);
        intent.putExtra("data",coachAdapter.getItem(position));
        startActivity(intent);
    }

    @Override
    public void favImpl(View view, boolean unfav) {
        fav.run(view,unfav+"",5,id);
    }
}
