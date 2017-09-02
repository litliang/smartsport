package top.smartsport.www.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.activity.BSChoiceActivity;
import top.smartsport.www.activity.BSDetailActivity;
import top.smartsport.www.activity.BSDetailBMActivity;
import top.smartsport.www.adapter.BSssAdapter;
import top.smartsport.www.base.BaseApplication;
import top.smartsport.www.base.BaseFragment;
import top.smartsport.www.bean.BSssInfo;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;
import top.smartsport.www.utils.SPUtils;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/24.
 * 比赛--赛事
 */
@ContentView(R.layout.fragment_bs)
public class BSSSFragment extends BaseFragment {

    private int page;
    @ViewInject(R.id.ptrlv)
    private PullToRefreshListView ptrlv;
    @ViewInject(R.id.tvHint)
    private TextView tvHint;

    private BSssAdapter bSssAdapter;
    private List<BSssInfo> bSssInfoList;

    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    public static BSSSFragment newInstance() {
        BSSSFragment fragment = new BSSSFragment();
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

        ptrlv.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                BSssInfo info = bSssAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putString(BSDetailActivity.TAG, info.getId());
                bundle.putString("states", info.getStatus());
                if(info.getStatus().equals("报名中")){
                    toActivity(BSDetailBMActivity.class, bundle);
                }else {
                    toActivity(BSDetailActivity.class, bundle);
                }
            }
        });
        getData(true);


    }

    @Override
    public void onStart() {
        super.onStart();
        getData(true);
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
            json.put("action", "getMatchList");
            json.put("page", page);

            String type = (String) SPUtils.get(BaseApplication.getApplication(), "type", null);

            String status = (String) SPUtils.get(BaseApplication.getApplication(), "status", null);
            String level = (String) SPUtils.get(BaseApplication.getApplication(), "level", null);

            String city = (String) SPUtils.get(BaseApplication.getApplication(), "getCounties-city", null);
            String county = (String) SPUtils.get(BaseApplication.getApplication(), "getCounties-county", null);

            if (type != null && !type.trim().equals("")) {
                json.put("type", type);//选填 赛事赛制
            }
            if (status != null && !status.trim().equals("")) {
                json.put("status", status);//比赛状态 1报名中2进行中
            }
            if (level != null && !level.trim().equals("")) {
                json.put("level", level);//比赛级别
            }
            if (city != null && !city.trim().equals("")) {
                json.put("city", city);//城市id
            }
            if (county != null && !county.trim().equals("")) {
                json.put("county", county);//区县id
            }
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
                bSssInfoList = entity.toList(BSssInfo.class);
                if (refresh) {
                    bSssAdapter.clear();
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
