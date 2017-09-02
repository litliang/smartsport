package top.smartsport.www.fragment;

import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.activity.BSDetailActivity;
import top.smartsport.www.activity.LeTvPlayActivity;
import top.smartsport.www.activity.ZBDetailActivity;
import top.smartsport.www.adapter.BSssAdapter;
import top.smartsport.www.adapter.BSzbAdapter;
import top.smartsport.www.base.BaseFragment;
import top.smartsport.www.bean.BSssInfo;
import top.smartsport.www.bean.BSzbInfo;
import top.smartsport.www.bean.Carousel;
import top.smartsport.www.bean.Data;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.bean.ZBRows;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;
import top.smartsport.www.widget.Banner;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/24.
 * 比赛－－直播
 */
@ContentView(R.layout.fragment_zb)
public class BSZBFragment extends BaseFragment{
    private int page;
    @ViewInject(R.id.ptrlv)
    private PullToRefreshListView ptrlv;
    @ViewInject(R.id.tvHint)
    private TextView tvHint;

    private BSzbAdapter bSzbAdapter;
    private List<BSzbInfo> bSzbInfoList;

    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;



    public static BSZBFragment newInstance() {
        BSZBFragment fragment = new BSZBFragment();
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

        bSzbAdapter = new BSzbAdapter(getActivity());
        ptrlv.getRefreshableView().setAdapter(bSzbAdapter);
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

            }
        });
        getData(true);
        ptrlv.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                BSzbInfo info = bSzbAdapter.getItem(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable(ZBDetailActivity.TAG,info);
                toActivity(ZBDetailActivity.class,bundle);
//                toActivity(LeTvPlayActivity.class);
            }
        });


    }




//
    /**
     * 直播接口列表
     * */

    private void getData(final boolean refresh){
        bSzbInfoList = new ArrayList<>();
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
            json.put("action","getLetvLiveList");
//            json.put("page",page);

//‘activity_id’ : ’’,     //直播活动ID（选填）
//‘activity_name’ : ’value’,   //直播活动名称（选填）
//‘activity_status’ : ’value’,   //直播活动状态（选填）0:未开始1:已开始 3:已结束
//‘offset’ : ’value’,   //从第几条数据开始查询，默认0（选填）
//‘fetch_size’ : ’value’   //一次返回多少条数据，默认为10，最多不能超过100条（选填）
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
                }Data data = entity.toObj(Data.class);
                bSzbInfoList = data.toRows(BSzbInfo.class);
                if(refresh){
                    bSzbAdapter.clear();
                } else {
                    if(bSzbInfoList.size()==0){
                        return;
                    }
                }
                bSzbAdapter.addAll(bSzbInfoList);
            }
        });

    }
}
