package top.smartsport.www.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import app.base.MapAdapter;
import app.base.MapConf;
import app.base.MapContent;
import intf.FunCallback;
import intf.JsonUtil;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.adapter.CoachAdapter;
import top.smartsport.www.adapter.SCCoatchAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.News;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshBase;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;

/**
 * Created by Aaron on 2017/7/24.
 * 教练--收藏教练
 */
@ContentView(R.layout.fragment_scjl)
public class SCJLV4Fragment extends BaseV4Fragment {
    public static final String TAG = "SCJLV4Fragment";
    @ViewInject(R.id.pullrefreshlistview)
    PullToRefreshListView pullrefreshlistview;
    @ViewInject(R.id.mykcempty)
    ViewGroup empty;
    private int page =1;
    private SCCoatchAdapter mAdapter;
    private List mList;

    public static SCJLV4Fragment newInstance() {
        SCJLV4Fragment fragment = new SCJLV4Fragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;

    }


    @Override
    protected void initView() {
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

    }

    private void reload(final boolean refresh) {
        BaseActivity.callHttp(MapBuilder.build().add("action", "getMyCollection").add("page", page).add("type", 5).get(), new FunCallback<NetEntity, String, NetEntity>() {

            @Override
            public void onSuccess(NetEntity result, List<Object> object) {
                if (refresh){
                    pullrefreshlistview.onPullDownRefreshComplete();
                    mList = new ArrayList();
                }else {
                    pullrefreshlistview.onPullUpRefreshComplete();
                }
                String data = result.getData().toString();
                String newDate  = data.replace("null","");
                List list = (List) app.base.JsonUtil.extractJsonRightValue(JsonUtil.findJsonLink("coaches",newDate).toString());
                if (list.size() > 0){
                    empty.setVisibility(View.GONE);
                }
                mList.addAll(list);
                mAdapter.setData(mList);
//                MapConf mc = MapConf.with(getActivity())
//                        .pair("header_url->iv_head_icon")
//                        .pair("coach_name->tv_coach_name")
//                        .pair("team_name->tv_team")
//                        .source(R.layout.coach_list);
//                MapConf.with(getActivity()).conf(mc).source(mList, pullrefreshlistview.getRefreshableView()).match();

            }

            @Override
            public void onFailure(String result, List<Object> object) {
                if (refresh){
                    pullrefreshlistview.onPullDownRefreshComplete();
                }else {
                    pullrefreshlistview.onPullUpRefreshComplete();
                }
                empty.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCallback(NetEntity result, List<Object> object) {

            }
        });
    }



}
