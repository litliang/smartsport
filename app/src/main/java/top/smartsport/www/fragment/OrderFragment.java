package top.smartsport.www.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import app.base.JsonUtil;
import app.base.MapConf;
import top.smartsport.www.R;
import top.smartsport.www.activity.OrderDetailsActivity;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

@ContentView(R.layout.fragment_list)
public class OrderFragment extends BaseV4Fragment {
    private static final String PAY_STATUS = "pay_status";
    @ViewInject(R.id.id_listview)
    PullToRefreshListView mlistview;
    @ViewInject(R.id.mykcempty)
    ViewGroup empty;
    private View mView;
    private List<Object> mList;
    private Context mContext;
    private int status;
    private int page = 1;
    private RegInfo regInfo;
    private TokenInfo tokenInfo;
    private String client_id;
    private String state;
    private String url;
    private String access_token;


    public static OrderFragment newInstance(int status) {
        Bundle args = new Bundle();
        args.putInt(PAY_STATUS, status);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            status = bundle.getInt(PAY_STATUS);
        }
        mList = new ArrayList<>();
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();
        mlistview.setOnRefreshListener(new com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mlistview.setMode(PullToRefreshListView.Mode.BOTH);
                page = 1;
                getData(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                getData(false);
            }
        });
        getData(true);
    }
//


    private void getData(final boolean refresh) {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "getMyOrders");
            json.put("page", page);
            if (status != 2) {
                json.put("pay_status", status);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                showToast(message);
                mlistview.onRefreshComplete();
            }

            @Override
            public void onSuccess(NetEntity entity) {
                String data = entity.getData().toString();
                mList = (List) JsonUtil.extractJsonRightValue(entity.getData().toString());
                if (refresh) {
                    if (mList != null && mList.size() > 0) {
                        empty.setVisibility(View.GONE);
                    } else {
                        empty.setVisibility(View.VISIBLE);
                    }
                }
                mlistview.onRefreshComplete();

//                adapter.setData(mList);
                MapConf mc = MapConf.with(getContext())
                        .pair("cover_url->iv_pic")
                        .pair("title->tv_title")
                        .pair("end_time->tv_date")
                        .pair("address->tv_address")
                        .pair("pay_total:￥%s->tv_price")
                        .pair("pay_status->pay_status", "0:未支付;1:已支付", "showPayStatus()")
                        .source(R.layout.list_item);
                MapConf.with(getContext()).conf(mc).source(data, mlistview.getRefreshableView()).toView();
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
            }
        });
    }
}
