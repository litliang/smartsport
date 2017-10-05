package top.smartsport.www.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import app.base.MapConf;
import intf.FunCallback;
import intf.JsonUtil;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.adapter.SCCoatchAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.Coaches;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshBase;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;

/**
 * Created by Aaron on 2017/7/24.
 * 教练--全部教练
 */
@ContentView(R.layout.fragment_all_jl)
public class AllJLActivity extends BaseActivity {
    public static final String TAG = "SCJLV4Fragment";
    @ViewInject(R.id.pullrefreshlistview)
    PullToRefreshListView pullrefreshlistview;
    @ViewInject(R.id.mykcempty)
    ViewGroup empty;
    private int page =1;
    private SCCoatchAdapter mAdapter;
    private List mList;
    private List<Coaches> list;



    @Override
    protected void initView() {
        back();
        mAdapter = new SCCoatchAdapter();
        pullrefreshlistview.getRefreshableView().setAdapter(mAdapter);
        pullrefreshlistview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page =1;
                reload(true);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page ++;
                reload(false);
            }
        });
        reload(true);
        pullrefreshlistview.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Gson gson = new Gson();
                    String json = gson.toJson(mList.get(i));
                Coaches coache = top.smartsport.www.utils.JsonUtil.jsonToEntity(json,Coaches.class);
                startActivity(new Intent(getBaseContext(), CoachDetailActivity.class).putExtra("data", coache));
            }
        });
    }

    private void reload(final boolean refresh) {
        BaseActivity.callHttp(MapBuilder.build().add("action", "getRecommendCoaches").add("page", page).add("type", 5).get(), new FunCallback() {
            @Override
            public void onCallback(Object result, List object) {

            }

            @Override
            public void onFailure(Object result, List object) {
                if (refresh){
                    pullrefreshlistview.onPullDownRefreshComplete();
                }else {
                    pullrefreshlistview.onPullUpRefreshComplete();
                }
                empty.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(Object result, List object) {
                if (refresh){
                    pullrefreshlistview.onPullDownRefreshComplete();
                    mList = new ArrayList();
                }else {
                    pullrefreshlistview.onPullUpRefreshComplete();
                }
                String data = ((NetEntity)result).getData().toString();
                list = (List<Coaches>) app.base.JsonUtil.extractJsonRightValue(JsonUtil.findJsonLink("coaches",data).toString());
                if (list.size() > 0){
                    empty.setVisibility(View.GONE);
                }
                mList.addAll(list);
                mAdapter.setData(mList);
                MapConf mc = MapConf.with(getBaseContext())
                        .pair("header_url->iv_head_icon")
                        .pair("name->tv_coach_name")
                        .pair("team_name->tv_team")
                        .pair("collect_status->iv_collect", "0:mipmap.icon_collect;1:mipmap.collect_checked")
                        .source(R.layout.coach_list);
                MapConf.with(getBaseContext()).conf(mc).source(mList, pullrefreshlistview.getRefreshableView()).toView();

            }

        });
    }



}
