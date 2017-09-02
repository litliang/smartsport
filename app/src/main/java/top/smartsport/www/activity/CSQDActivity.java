package top.smartsport.www.activity;

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

import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.adapter.CSQDAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.CSQDInfo;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshBase;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/25.
 * 参赛球队
 */
@ContentView(R.layout.activity_csqd)
public class CSQDActivity extends BaseActivity {
    private int page;
    @ViewInject(R.id.ptrlv)
    private PullToRefreshListView ptrlv;
    @ViewInject(R.id.action_bar)
    private View actionbar;

    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    private List<CSQDInfo> csqdInfoList;

    private CSQDAdapter csqdAdapter;
    String id;

    @Override
    public View getTopBar() {
        return actionbar;
    }

    @Override
    protected void initView() {
        back();
        setTitle("参赛球队");
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();

        csqdAdapter =new CSQDAdapter(this);
        ptrlv.getRefreshableView().setAdapter(csqdAdapter);
        ptrlv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                getData(true);
                String label = DateUtils.formatDateTime(CSQDActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getHeaderLoadingLayout().setLastUpdatedLabel(label);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData(false);
                String label = DateUtils.formatDateTime(CSQDActivity.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getFooterLoadingLayout().setLastUpdatedLabel(label);
            }
        });
        ptrlv.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CSQDInfo csqdInfo = csqdAdapter.getItem(position-1);
                Bundle bundle = new Bundle();
                bundle.putString(QDHomeActivity.TAG,csqdInfo.getTeam_id());
                goActivity(QDHomeActivity.class,bundle);
            }
        });
        id = getIntent().getStringExtra("matchid");
        getData(true);

    }

    /**
     * 获取参赛球队的接口
     * */
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
            json.put("action","getApplyTeam");
            json.put("page",page);
            json.put("match_id",id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                showToast(message);
                if(refresh){
                    ptrlv.onPullDownRefreshComplete();
                }else{
                    ptrlv.onPullUpRefreshComplete();
                }
            }

            @Override
            public void onSuccess(NetEntity entity) {
                if(refresh){
                    ptrlv.onPullDownRefreshComplete();
                }else{
                    ptrlv.onPullUpRefreshComplete();
                }

                csqdInfoList = entity.toList(CSQDInfo.class);
                if(csqdInfoList.size()==0){
                    findViewById(R.id.empty).setVisibility(View.VISIBLE);
                }else{
                    findViewById(R.id.empty).setVisibility(View.GONE);
                }
                if(refresh){
                    csqdAdapter.clear();
                } else {
                    if(csqdInfoList.size()==0){
                        return;
                    }
                }
                csqdAdapter.addAll(csqdInfoList);

            }
        });
    }


}
