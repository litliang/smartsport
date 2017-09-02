package top.smartsport.www.fragment;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.adapter.CoachesAdapter;
import top.smartsport.www.adapter.CoursesAdapter;
import top.smartsport.www.adapter.NewsAdapter;
import top.smartsport.www.adapter.NewsHotAdapter;
import top.smartsport.www.adapter.PlayersAdapter;
import top.smartsport.www.base.BaseFragment;
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
import top.smartsport.www.widget.Banner;
import top.smartsport.www.widget.HorizontalListView;
import top.smartsport.www.widget.MyGridView;
import top.smartsport.www.widget.MyListView;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/24.
 * 资讯--青训资讯
 */
@ContentView(R.layout.fragment_zxqx)
public class ZXQXFragment extends BaseFragment {
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
    @ViewInject(R.id.gridview)
    private GridView gridview;

    private NewsHotAdapter newsHotAdapter;
    @ViewInject(R.id.fm_list_top_news)
    private MyListView fm_list_top_news;

    @ViewInject(R.id.scrollView)
    private ScrollView scrollView;
    private ViewPager viewpager;

    public static ZXQXFragment newInstance() {
        ZXQXFragment fragment = new ZXQXFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView() {
        gridview.setFocusable(false);
//        thtc_gridView = (GridView) scrollView.findViewById(R.id.thtc_layout).findViewById(R.id.gridview);
//
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();


        coursesAdapter = new CoursesAdapter(getActivity());
        fm_grid_tjkc.setAdapter(coursesAdapter);
        fm_grid_tjkc.setViewPager(viewpager);
        newsAdapter= new NewsAdapter(getActivity());
        fm_list_qxzx.setAdapter(newsAdapter);

        coachesAdapter = new CoachesAdapter(getActivity());
        fm_grid_tjjl.setAdapter(coachesAdapter);
        fm_grid_tjjl.setViewPager(viewpager);
        playersAdapter = new PlayersAdapter(getActivity());
        gridview.setAdapter(playersAdapter);

        newsHotAdapter = new NewsHotAdapter(getActivity());
        fm_list_top_news.setAdapter(newsAdapter);

        getData();

    }

    private void initBanner(List<Carousel> list) {
        banner = new Banner(root, getActivity(), new Banner.MultiItemClickListener() {
            @Override
            public void onMultiItemClick(Banner.BannerData data) {

            }

        });

        banner.init(list);


    }

    /**
     * 接口
     * */
    private void getData(){
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","getQxNews");
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
                coursesAdapter.addAll(courses);
                newsAdapter.addAll(newses);
                coachesAdapter.addAll(coaches);
                playersAdapter.addAll(playerses);
                newsHotAdapter.addAll(hotNewses);

                DisplayMetrics dm = new DisplayMetrics();//获取分辨率
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        (dm.widthPixels-50)*playerses.size(), LinearLayout.LayoutParams.MATCH_PARENT);
                params.rightMargin=30;
                gridview.setLayoutParams(params);
                gridview.setColumnWidth(dm.widthPixels-80);
                gridview.setHorizontalSpacing(20);
                gridview.setStretchMode(gridview.STRETCH_SPACING_UNIFORM);
                gridview.setNumColumns(playerses.size());
            }
        });
    }

    public ZXQXFragment setViewpager(ViewPager viewpager) {
        this.viewpager = viewpager;
        return this;
    }
}
