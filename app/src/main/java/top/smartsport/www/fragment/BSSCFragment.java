package top.smartsport.www.fragment;

import android.os.Bundle;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.xutils.view.annotation.ContentView;

import java.util.Map;

import app.base.MapConf;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseFragment;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;

/*
* 0 = {HashMap$HashMapEntry@5928} "away_logo" -> "http://admin.smartsport.top/data/upload/2017/0802/18/5981a4b46578c.png"
1 = {HashMap$HashMapEntry@5929} "end_time" -> "2017-07-14"
2 = {HashMap$HashMapEntry@5930} "start_time" -> "2017-07-14"
3 = {HashMap$HashMapEntry@5931} "away_team" -> "21"
4 = {HashMap$HashMapEntry@5932} "match_id" -> "1"
5 = {HashMap$HashMapEntry@5933} "home_team" -> "4"
6 = {HashMap$HashMapEntry@5934} "away_score" -> "2"
7 = {HashMap$HashMapEntry@5935} "home_name" -> "我们是最棒的球队"
8 = {HashMap$HashMapEntry@5936} "status" -> "0"
9 = {HashMap$HashMapEntry@5937} "id" -> "1"
10 = {HashMap$HashMapEntry@5938} "away_name" -> "上海中国中学"
11 = {HashMap$HashMapEntry@5939} "home_score" -> "3"
12 = {HashMap$HashMapEntry@5940} "round" -> "1"
13 = {HashMap$HashMapEntry@5941} "home_logo" -> "http://admin.smartsport.top/data/upload/2017/0802/18/5981a4b46578c.png"*/

/**
 * Created by Aaron on 2017/7/24.
 * 比赛--比赛赛程
 */
@ContentView(R.layout.fragment_bssc)
public class BSSCFragment extends BaseV4Fragment {

    Map data;

    public BSSCFragment setData(Map data) {
        this.data = data;
        return this;
    }

    public Map getData() {
        return data;
    }

    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;
    private PullToRefreshScrollView pullToRefreshScrollView;

    public static BSSCFragment newInstance() {
        BSSCFragment fragment = new BSSCFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    protected void initView() {
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();

//        pullToRefreshScrollView = (PullToRefreshScrollView) root.findViewById(R.id.pullrefreshlistview);
//        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
//            @Override
//            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
//                loadData();
//
//            }
//        });

       ;
    }


}
