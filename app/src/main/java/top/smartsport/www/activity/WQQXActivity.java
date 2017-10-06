package top.smartsport.www.activity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.base.MapConf;
import intf.FunCallback;
import intf.JsonUtil;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.adapter.SCCoatchAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.Coaches;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.StarDetail;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshBase;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;

/**
 * Created by Aaron on 2017/7/24.
 * 球星-往期球星
 */
@ContentView(R.layout.fragment_all_jl)
public class WQQXActivity extends BaseActivity {
    @ViewInject(R.id.pullrefreshlistview)
    PullToRefreshListView pullrefreshlistview;
    @ViewInject(R.id.mykcempty)
    ViewGroup empty;
    private int page =1;
    private SCCoatchAdapter mAdapter;
    private List mList;
    private List<StarDetail> list;

    @Override
    protected void initView() {
        back();
        ((TextView)findViewById(R.id.empty)).setText("还没有任何往期球星");
        mAdapter = new SCCoatchAdapter(false);
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
                startActivity(new Intent(getBaseContext(), StarDetailActivity.class).putExtra("id", ((Map)adapterView.getItemAtPosition(i)).get("id").toString()));
            }
        });
    }

    private void reload(final boolean refresh) {
        BaseActivity.callHttp(MapBuilder.build().add("action", "getRecommendPlayers").add("page", page).get(), WQQXActivity.this.findViewById(R.id.content), new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {
                if (refresh){
                    pullrefreshlistview.onPullDownRefreshComplete();
                    mList = new ArrayList();
                }else {
                    pullrefreshlistview.onPullUpRefreshComplete();
                }
                findViewById(R.id.mykcempty).setVisibility(View.GONE);
                String data = ((NetEntity)result).getData().toString();
                list = (List<StarDetail>) app.base.JsonUtil.extractJsonRightValue(JsonUtil.findJsonLink("players",data).toString());
                if (list.size() > 0){
                    empty.setVisibility(View.GONE);
                }
                mList.addAll(list);
                mAdapter.setData(mList);
                MapConf mc = MapConf.with(getBaseContext()).pairs("name->players_name","team_name->players_dis","cover_url->players_img","stage:%s\n期->players_num").source(R.layout.wqqx_adapter);
//                MapConf.with(getBaseContext()).conf(mc).source(intf.JsonUtil.findJsonLink("players",((NetEntity)result).getData().toString()).toString(),pullrefreshlistview.getRefreshableView()).toView();
                MapConf.with(getBaseContext()).conf(mc).source(mList,pullrefreshlistview.getRefreshableView()).toView();
            }

            @Override
            public void onFailure(Object result, List object) {
                if (refresh) {
                    pullrefreshlistview.onPullDownRefreshComplete();
                } else {
                    pullrefreshlistview.onPullUpRefreshComplete();
                }
            }

            @Override
            public void onCallback(Object result, List object) {

            }
        });
    }


}
