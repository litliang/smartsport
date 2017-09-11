package top.smartsport.www.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/24.
 * 我的课程--在线案例
 */
@ContentView(R.layout.fragment_kc_zxja)
public class MYZXJAV4Fragment extends BaseV4Fragment {
    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;
    @ViewInject(R.id.pullrefreshlistview)
    PullToRefreshListView pullrefreshlistview;
    @ViewInject(R.id.mykcempty)
    ViewGroup empty;
    private int page;

    public static MYZXJAV4Fragment newInstance() {
        MYZXJAV4Fragment fragment = new MYZXJAV4Fragment();
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
        pullrefreshlistview.getRefreshableView().setDivider(new ColorDrawable(Color.parseColor("#d6d6d9")));
        pullrefreshlistview.getRefreshableView().setDividerHeight(20);
        pullrefreshlistview.setOnRefreshListener(new top.smartsport.www.listview_pulltorefresh.PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(top.smartsport.www.listview_pulltorefresh.PullToRefreshBase<ListView> refreshView) {
                page =1 ;
                getData(true);
            }

            @Override
            public void onPullUpToRefresh(top.smartsport.www.listview_pulltorefresh.PullToRefreshBase<ListView> refreshView) {
                page ++ ;
                getData(false);
            }
        });
        getData(true);
    }


    private void getData(final boolean refresh) {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "getMyOrders");
            json.put("product_type", 2);
            json.put("page", page);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                showToast(message);
                if (refresh){

                    pullrefreshlistview.onPullUpRefreshComplete();
                }else {
                    pullrefreshlistview.onPullDownRefreshComplete();
                }
            }

            @Override
            public void onSuccess(NetEntity entity) {
                if (refresh){
                    pullrefreshlistview.onPullUpRefreshComplete();
                }else {
                    pullrefreshlistview.onPullDownRefreshComplete();
                }
//                MapAdapter.AdaptInfo adaptinfo = new MapAdapter.AdaptInfo();
//                adaptinfo.addListviewItemLayoutId(R.layout.adapter_bsdetail_shipin);
//                adaptinfo.addViewIds(new Integer[]{R.id.news_name,R.id.news_img,R.id.news_date,R.id.news_hint});
//                adaptinfo.addObjectFields(new String[]{"name"});
//                MapAdapter mapAdapter = new MapAdapter(getBaseContext(), adaptinfo);
//                mapAdapter.setItemDataSrc(new MapContent(JsonUtil.extractJsonRightValue(bsDetail.getMatch_video().toString())));
//                bs_detail_video.setAdapter(mapAdapter);
//                bs_detail_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                        Bundle b = new Bundle();
//                        b.putString("fileurl", ((Map) adapterView.getItemAtPosition(i)).get("fileurl").toString());
//                        b.putString("name", ((Map) adapterView.getItemAtPosition(i)).get("name").toString());
//
//                        goActivity(BSVideoActivity.class, b);
//                    }
//                });
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
            }
        });
    }
}
