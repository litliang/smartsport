package top.smartsport.www.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.adapter.HDZXAdapter;
import top.smartsport.www.base.BaseFragment;
import top.smartsport.www.bean.Carousel;
import top.smartsport.www.bean.Data;
import top.smartsport.www.bean.HDZXInfo;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.widget.Banner;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/24.
 * 资讯--活动资讯
 */
@ContentView(R.layout.fragment_zxhd)
public class ZXHDFragment extends BaseFragment{

    @ViewInject(R.id.mScrollView)
    private ScrollView mScrollView;
    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    private int page;
    @ViewInject(R.id.ptrlv)
    private ListView ptrlv;

    private Banner banner;//轮播图
    List<Carousel> carousels = new ArrayList<>();
    private int flag = 0;

    private HDZXAdapter hdzxAdapter;
    List<HDZXInfo> hdzxInfos = new ArrayList<>();

    public static ZXHDFragment newInstance() {
        ZXHDFragment fragment = new ZXHDFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView() {
        mScrollView.scrollTo(0,0);

        ptrlv.setFocusable(false);
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();


        hdzxAdapter = new HDZXAdapter(getActivity());
        ptrlv.setAdapter(hdzxAdapter);
//        ptrlv.setOnRefreshListener(this);
//        ptrlv.setMode(PullToRefreshBase.Mode.PULL_UP_TO_REFRESH);
        getData(true);
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
//                BSzbInfo info = bSzbAdapter.getItem(position-1);
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(ZBDetailActivity.TAG,info);
//                toActivity(ZBDetailActivity.class,bundle);
//                toActivity(LeTvPlayActivity.class);
            }
        });

    }

    private void initBanner(List<Carousel> list) {
        flag++;
        banner = new Banner(root, getActivity(), new Banner.MultiItemClickListener() {
            @Override
            public void onMultiItemClick(Banner.BannerData data) {

            }

        });

        banner.init(list);


    }
    private void getData(final boolean refresh){
        if(refresh){
            page = 1;
        }else {
            page++;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","getNews");
            json.put("type","2");//活动资讯
            json.put("page",page);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
//                ptrlv.onRefreshComplete();
                showToast(message);
            }

            @Override
            public void onSuccess(NetEntity entity) {
//                ptrlv.onRefreshComplete();
                Data data = entity.toObj(Data.class);

                carousels = data.toListcarousel(Carousel.class);

                hdzxInfos = data.toListnews(HDZXInfo.class);
                hdzxAdapter.addAll(hdzxInfos);

                initBanner(carousels);

//
            }
        });

    }






}
