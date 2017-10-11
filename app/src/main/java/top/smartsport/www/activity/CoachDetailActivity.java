package top.smartsport.www.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import app.base.MapConf;
import top.smartsport.www.R;
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
    private boolean isAll = true;
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
    private WebView introduce;
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
    @ViewInject(R.id.fl_loading)
    private FrameLayout fl_loading;
    private TrainningAdapter trainingAdapter;
    private CoachAdapter coachAdapter;
    private String tmpIntro = "";
    private String allIntro;
    private boolean isCurrentScStatus = true;

    public void initView() {
        Coaches coach = (Coaches) getIntent().getSerializableExtra("data");
        if (coach == null) {
            id = getIntent().getStringExtra("id");
        } else
            id = coach.getId();
//            id = coach.getCoach_id();

        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();
        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();
        back();
        fav();
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
                if (tmpIntro.equals(allIntro)) {
                    introduce.loadData(allIntro.substring(0, 300), "text/html;charset=UTF-8", null);
                } else {
                    introduce.loadData(allIntro, "text/html;charset=UTF-8", null);
                    seeMore.setVisibility(View.GONE);
                }
                introduce.invalidate();
                ((ViewGroup) introduce.getParent()).invalidate();
                ((ViewGroup) introduce.getParent()).requestLayout();


                isAll = !isAll;
            }
        });
        lvTraining.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getBaseContext(), ActivityTrainingDetails.class).putExtra("id", ((CoachInfoCourse) parent.getItemAtPosition(position)).getId()));
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
                fl_loading.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(NetEntity entity) {
                fl_loading.setVisibility(View.GONE);
                String data = entity.getData().toString();
                String collect_status = app.base.JsonUtil.findJsonLink("detail-collect_status", entity.getData().toString()).toString();

                MapConf.build().with(CoachDetailActivity.this)
                        .pair("detail-collect_status->ivRight_text", "0:mipmap.fav_undo;1:mipmap.fav_done").source(entity.getData().toString(), CoachDetailActivity.this).toView();
                setFaved(!collect_status.equals("0"));
                try {
                    CoachInfoDetail details = JsonUtil.jsonToEntity(app.base.JsonUtil.findJsonLink("detail", data).toString(), CoachInfoDetail.class);
                    List<CoachInfoCourse> course = JsonUtil.jsonToEntityList(app.base.JsonUtil.findJsonLink("course", data).toString(), CoachInfoCourse.class);
                    List<Coaches> others = JsonUtil.jsonToEntityList(app.base.JsonUtil.findJsonLink("other_coach", data).toString(), Coaches.class);
                    ImageLoader.getInstance().displayImage(details.getHeader_url(), ivTop, ImageUtil.getOptions(), ImageUtil.getImageLoadingListener(true));
                    tvName.setText(details.getName());
                    tvTeam.setText(details.getTeam_name());
                    allIntro = details.getIntroduce();
                    if (details.getIntroduce().length() > 57) {
                        tmpIntro = details.getIntroduce().substring(0, 57) + "...";
                    } else {
                        tmpIntro = details.getIntroduce();
                        seeMore.setVisibility(View.GONE);
                    }
                    introduce.loadData(tmpIntro, "text/html;charset=UTF-8", null);
                    trainingAdapter.setData(course);
                    coachAdapter.setData(others);
                    setSharetitle(details.getName());
                    setSharetxt(tmpIntro);
                    setShareurl(details.getHeader_url());
                    isCurrentScStatus = getFaved();
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
        Intent intent = new Intent(this, CoachDetailActivity.class);
        intent.putExtra("data", coachAdapter.getItem(position));
        startActivity(intent);
    }

    @Override
    public void favImpl(View view, boolean unfav) {
        fav.run(view, unfav + "", 5, id);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        boolean scStatus = getFaved();
        if(isCurrentScStatus != scStatus)
            setResult(RESULT_OK);
    }

}
