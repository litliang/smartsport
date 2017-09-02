package top.smartsport.www.fragment;

import android.os.Build;
import android.os.Bundle;
import android.print.PageRange;
import android.support.annotation.RequiresApi;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
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
import top.smartsport.www.adapter.BannerListAdapter;
import top.smartsport.www.adapter.SSXWAdapter;
import top.smartsport.www.banner.BannerView;
import top.smartsport.www.base.BaseFragment;
import top.smartsport.www.bean.Carousel;
import top.smartsport.www.bean.Data;
import top.smartsport.www.bean.HDZXInfo;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.SSXWInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.widget.Banner;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/24.
 * 资讯--赛事新闻
 */
@ContentView(R.layout.fragment_zxss)
public class ZXSSFragment extends BaseFragment{
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
    private int num = 0;

    private SSXWAdapter ssxwAdapter;
    List<SSXWInfo> ssxwInfos = new ArrayList<>();
    public static ZXSSFragment newInstance() {
        ZXSSFragment fragment = new ZXSSFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView() {
        mScrollView.scrollTo(0,0);

        ptrlv.setFocusable(false);
//        proceeds_listview = ptrlv.getRefreshableView();

        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();

        ssxwAdapter = new SSXWAdapter(getActivity());
        ptrlv.setAdapter(ssxwAdapter);
        getData(true);


    }
    private void initBanner(List<Carousel> list) {
        num++;
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
            json.put("type","3");//赛事新闻
            json.put("page",page);
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
                Log.i("________3",entity.getData().toString());

                Data data = entity.toObj(Data.class);

                carousels = data.toListcarousel(Carousel.class);

                ssxwInfos= data.toListnews(SSXWInfo.class);

                ssxwAdapter.addAll(ssxwInfos);
                initBanner(carousels);

            }
        });

    }

}
