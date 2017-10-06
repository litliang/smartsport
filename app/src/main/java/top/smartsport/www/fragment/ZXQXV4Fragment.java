package top.smartsport.www.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.base.MapConf;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.activity.ActivityTrainingDetails;
import top.smartsport.www.activity.AllJLActivity;
import top.smartsport.www.activity.AllKechengActivity;
import top.smartsport.www.activity.CoachDetailActivity;
import top.smartsport.www.activity.ConsultDetailActivity;
import top.smartsport.www.activity.StarDetailActivity;
import top.smartsport.www.activity.WQQXActivity;
import top.smartsport.www.adapter.CoachesAdapter;
import top.smartsport.www.adapter.CoursesAdapter;
import top.smartsport.www.adapter.NewsAdapter;
import top.smartsport.www.adapter.NewsHotAdapter;
import top.smartsport.www.adapter.PlayersAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.Carousel;
import top.smartsport.www.bean.Coaches;
import top.smartsport.www.bean.Courses;
import top.smartsport.www.bean.Data;
import top.smartsport.www.bean.HotNews;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.News;
import top.smartsport.www.bean.Players;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.listener.OnClickThrottleListener;
import top.smartsport.www.widget.Banner;
import top.smartsport.www.widget.HorizontalListView;
import top.smartsport.www.widget.MyListView;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/24.
 * 资讯--青训资讯
 */
@ContentView(R.layout.fragment_zxqx)
public class ZXQXV4Fragment extends BaseV4Fragment {
    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    List<Carousel> carousels = new ArrayList<>();
    List<Coaches> coaches = new ArrayList<>();
    List<Courses> courses = new ArrayList<>();
    List<News> newses = new ArrayList<>();
    List<Players> playerses = new ArrayList<>();
    List<HotNews> hotNewses = new ArrayList<>();


    private Banner banner;//轮播图

    private CoursesAdapter coursesAdapter;//推荐课程
    @ViewInject(R.id.fm_grid_tjkc)
    private HorizontalListView fm_grid_tjkc;

    private NewsAdapter newsAdapter;//青训资讯
    @ViewInject(R.id.fm_list_qxzx)
    private MyListView fm_list_qxzx;

    private CoachesAdapter coachesAdapter;//推荐教练
    @ViewInject(R.id.fm_grid_tjjl)
    private HorizontalListView fm_grid_tjjl;

    private PlayersAdapter playersAdapter;//今日球星
    @ViewInject(R.id.listview)
    private HorizontalListView listview;

    private NewsHotAdapter newsHotAdapter;
    @ViewInject(R.id.fm_list_top_news)
    private MyListView fm_list_top_news;

    @ViewInject(R.id.scrollView)
    private PullToRefreshScrollView scrollView;
    private ViewPager viewpager;

    @ViewInject(R.id.fm_text_qbkc)
    private TextView fm_text_qbkc;
    @ViewInject(R.id.fm_text_)
    private TextView fm_text_;

    public static ZXQXV4Fragment newInstance() {
        ZXQXV4Fragment fragment = new ZXQXV4Fragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView() {

        scrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

        fm_grid_tjkc.setOnTouchListener(HorizontalListView.hlv);
        fm_grid_tjjl.setOnTouchListener(HorizontalListView.hlv);
        listview.setOnTouchListener(HorizontalListView.hlv);

        listview.setFocusable(false);
//        thtc_gridView = (GridView) scrollView.findViewById(R.id.thtc_layout).findViewById(R.id.gridview);
//
        fm_text_qbkc.setOnClickListener(new OnClickThrottleListener() {
            @Override
            protected void onThrottleClick(View v) {
                startActivity(new Intent(getContext(), AllKechengActivity.class));
            }
        });
        fm_text_.setOnClickListener(new OnClickThrottleListener() {
            @Override
            protected void onThrottleClick(View v) {
                startActivity(new Intent(getContext(), AllJLActivity.class));
            }
        });
        root.findViewById(R.id.wqqx).setOnClickListener(new OnClickThrottleListener() {
            @Override
            protected void onThrottleClick(View v) {
                startActivity(new Intent(getContext(), WQQXActivity.class));
            }
        });

        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();


        coursesAdapter = new CoursesAdapter(getActivity());
        fm_grid_tjkc.setAdapter(coursesAdapter);
        fm_grid_tjkc.setViewPager(viewpager);
        fm_grid_tjkc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), ActivityTrainingDetails.class).putExtra("id", ((Courses) adapterView.getItemAtPosition(i)).getId()));

            }
        });
        newsAdapter = new NewsAdapter(getActivity());
        fm_list_qxzx.setAdapter(newsAdapter);
        fm_list_qxzx.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), ConsultDetailActivity.class).putExtra("id", ((News) adapterView.getItemAtPosition(i)).getId() + ""));

            }
        });
        coachesAdapter = new CoachesAdapter(getActivity());
        fm_grid_tjjl.setAdapter(coachesAdapter);
        fm_grid_tjjl.setViewPager(viewpager);
        fm_grid_tjjl.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Coaches coache = (Coaches) adapterView.getItemAtPosition(i);
                startActivity(new Intent(getActivity(), CoachDetailActivity.class).putExtra("data", coache));

            }
        });
        playersAdapter = new PlayersAdapter(getActivity());
        listview.setAdapter(playersAdapter);

        newsHotAdapter = new NewsHotAdapter(getActivity());
        fm_list_top_news.setAdapter(newsHotAdapter);
        fm_list_top_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), ConsultDetailActivity.class).putExtra("id", ((HotNews) adapterView.getItemAtPosition(i)).getId() + ""));
            }
        });
        listview.setViewPager(viewpager);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), StarDetailActivity.class).putExtra("id", ((Map)adapterView.getItemAtPosition(i)).get("id").toString()));
            }
        });
        getData();
        BaseActivity.callHttp(MapBuilder.build().add("action", "getRecommendPlayers").get(), root, new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {
                MapConf mc = MapConf.with(getContext()).pairs("name->players_name","team_name->players_dis","cover_url->players_img","stage:%s\n期->players_num").source(R.layout.adapter_players);
                MapConf.with(getContext()).pair("players->listview",mc).source(((NetEntity)result).getData().toString(),getView()).toView();
            }

            @Override
            public void onFailure(Object result, List object) {

            }

            @Override
            public void onCallback(Object result, List object) {

            }
        });
    }

    private void initBanner(List<Carousel> list) {
        banner = new Banner(root, getActivity(), new Banner.MultiItemClickListener() {
            @Override
            public void onMultiItemClick(Banner.BannerData data) {
                startActivity(new Intent(getContext(),ConsultDetailActivity.class).putExtra("id",data.getId()+""));
            }

        });

        banner.init(list);


    }

    /**
     * 接口
     */
    private void getData() {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "getQxNews");
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

                Data data = entity.toObj(Data.class);
                carousels = data.toListcarousel(Carousel.class);
                coaches = data.toListcoaches(Coaches.class);
                courses = data.toListcourses(Courses.class);
                newses = data.toListnews(News.class);
                playerses = data.toListplayers(Players.class);
                hotNewses = data.toHot(HotNews.class);
                initBanner(carousels);
                coursesAdapter.clear();
                newsAdapter.clear();
                coachesAdapter.clear();
                playersAdapter.clear();
                newsHotAdapter.clear();
                coursesAdapter.addAll(courses);
                newsAdapter.addAll(newses);
                coachesAdapter.addAll(coaches);
//                playersAdapter.addAll(playerses);
                newsHotAdapter.addAll(hotNewses);
                scrollView.onRefreshComplete();


            }
        });
    }

    public ZXQXV4Fragment setViewpager(ViewPager viewpager) {
        this.viewpager = viewpager;
        return this;
    }
}
