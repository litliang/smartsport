package top.smartsport.www.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.activity.BSDetailActivity;
import top.smartsport.www.adapter.BSssAdapter;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.BSssInfo;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/13.
 * 比赛
 */
@ContentView(R.layout.fragment_scss)
public class SCBSV4Fragment extends BaseV4Fragment {
    private int page;
    @ViewInject(R.id.ptrlv)
    private PullToRefreshListView ptrlv;
    @ViewInject(R.id.mykcempty)
    private ViewGroup empty;

    private BSssAdapter bSssAdapter;
    private List<BSssInfo> bSssInfoList;

    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    public static SCBSV4Fragment newInstance() {
        SCBSV4Fragment fragment = new SCBSV4Fragment();
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

        bSssAdapter = new BSssAdapter(getActivity());
        ptrlv = (PullToRefreshListView) root.findViewById(R.id.ptrlv);
        ptrlv.getRefreshableView().setAdapter(bSssAdapter);
        ptrlv.setOnRefreshListener(new top.smartsport.www.listview_pulltorefresh.PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(top.smartsport.www.listview_pulltorefresh.PullToRefreshBase<ListView> refreshView) {
                getData(true);
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getHeaderLoadingLayout().setLastUpdatedLabel(label);
            }

            @Override
            public void onPullUpToRefresh(top.smartsport.www.listview_pulltorefresh.PullToRefreshBase<ListView> refreshView) {
                getData(false);
                String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getFooterLoadingLayout().setLastUpdatedLabel(label);
            }
        });
        ptrlv.getRefreshableView().setDivider(new ColorDrawable(Color.parseColor("#F2F2F2")));
        ptrlv.getRefreshableView().setDividerHeight(20);
        ptrlv.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                BSssInfo info = bSssAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString(BSDetailActivity.TAG, info.getId());
                bundle.putString("states", info.getStatus());
                toActivity(BSDetailActivity.class, bundle);
            }
        });
        getData(true);

        ptrlv.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                BSssInfo info = bSssAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString(BSDetailActivity.TAG, info.getId());
                bundle.putString("states", info.getStatus());
                toActivity(BSDetailActivity.class, bundle);
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
//        getData(true);
    }

    /**
     * 获取数据接口
     */
    private void getData(final boolean refresh) {
        if (refresh) {
            page = 1;
        } else {
            page++;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "getMyCollection");
            json.put("page", page);
            json.put("type", 3);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                showToast(message);
                if (refresh) {
                    ptrlv.onPullDownRefreshComplete();

                } else {
                    ptrlv.onPullUpRefreshComplete();
                }
            }

            @Override
            public void onSuccess(NetEntity entity) {
                if (refresh) {
                    ptrlv.onPullDownRefreshComplete();

                } else {
                    ptrlv.onPullUpRefreshComplete();
                }
                String data = entity.getData().toString();
                bSssInfoList = top.smartsport.www.utils.JsonUtil.jsonToEntityList(app.base.JsonUtil.findJsonLink("matches", data).toString(), BSssInfo.class);
                if (refresh) {
                    bSssAdapter.clear();
                    if (bSssInfoList.size() > 0) {
                        empty.setVisibility(View.GONE);
                    } else {
                        empty.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (bSssInfoList.size() == 0) {
                        return;
                    }
                }
                bSssAdapter.addAll(bSssInfoList);

            }
        });

    }


}
