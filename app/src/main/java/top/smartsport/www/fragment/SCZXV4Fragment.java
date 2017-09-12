package top.smartsport.www.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.adapter.NewsAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.News;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshBase;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;

/**
 * Created by Aaron on 2017/7/24.
 * 资讯--收藏
 */
@ContentView(R.layout.fragment_sczx)
public class SCZXV4Fragment extends BaseV4Fragment {
    public static final String TAG = "SCZXV4Fragment";
    @ViewInject(R.id.pullrefreshlistview)
    PullToRefreshListView pullrefreshlistview;
    @ViewInject(R.id.mykcempty)
    ViewGroup empty;
    private NewsAdapter newsAdapter;
    private int page =1;
    private List newses;

    public static SCZXV4Fragment newInstance() {
        SCZXV4Fragment fragment = new SCZXV4Fragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;

    }


    @Override
    protected void initView() {
        newsAdapter = new NewsAdapter(getActivity());
        pullrefreshlistview.getRefreshableView().setAdapter(newsAdapter);
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
        BaseActivity.callHttp(MapBuilder.build().add("action", "getMyCollection").add("page", page).add("type", 4).get(), new FunCallback<NetEntity, String, NetEntity>() {

            @Override
            public void onSuccess(NetEntity result, List<Object> object) {

            }

            @Override
            public void onFailure(String result, List<Object> object) {
                if (refresh){
                    pullrefreshlistview.onPullDownRefreshComplete();
                }else {
                    pullrefreshlistview.onPullUpRefreshComplete();
                }
            }

            @Override
            public void onCallback(NetEntity result, List<Object> object) {
                String data = result.getData().toString();
                newses = top.smartsport.www.utils.JsonUtil.jsonToEntityList(app.base.JsonUtil.findJsonLink("news",data).toString(), News.class);
                if (refresh){
                    pullrefreshlistview.onPullDownRefreshComplete();
                    if(newses !=null && newses.size()> 0){
                        empty.setVisibility(View.GONE);
                    }else {
                        empty.setVisibility(View.VISIBLE);
                    }
                }else {
                    pullrefreshlistview.onPullUpRefreshComplete();
                }
                empty.setVisibility(View.GONE);
                newsAdapter.addAll(newses);
            }
        });
    }


}
