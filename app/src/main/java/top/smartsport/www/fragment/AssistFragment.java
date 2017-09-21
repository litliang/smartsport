package top.smartsport.www.fragment;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import app.base.MapAdapter;
import app.base.MapContent;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.ScorerInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshBase;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;

/**
 * Created by bajieaichirou on 2017/9/21 0021.
 * 数据分析之助攻榜
 */
@ContentView(R.layout.fragment_scorer)
public class AssistFragment extends BaseV4Fragment {
    @ViewInject(R.id.scorer_list)
    PullToRefreshListView mList;

    @ViewInject(R.id.empty)
    RelativeLayout mEmptyLayout;

    private Activity mThis;
    private int page = 1;
    //    private ScorerAdapter scorerAdapter;
    private List<ScorerInfo> list = new ArrayList<>();
    private MapAdapter mapadapter;
    private RegInfo regInfo;
    private TokenInfo tokenInfo;
    private String client_id, state, url, access_token;


    @Override
    protected void initView() {
        mThis = getActivity();
        list= new ArrayList<>();
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        url = regInfo.getSource_url();
        state = regInfo.getSeed_secret();
        access_token = tokenInfo.getAccess_token();
        MapAdapter.AdaptInfo adaptinfo = new MapAdapter.AdaptInfo();
        adaptinfo.addListviewItemLayoutId(R.layout.adapter_scorer_item);
        adaptinfo.addViewIds(new Integer[]{/*R.id.item_number_tv, */R.id.scorer_item_iv, R.id.scorer_item_username_tv, R.id.scorer_item_name_tv, R.id.scorer_item_score_tv});
        adaptinfo.addObjectFields(new String[]{/*"position", */"header", "member_name", "team_name", "assists"});
        mapadapter = new MapAdapter(getContext(), adaptinfo) {
            @Override
            protected boolean findAndBindView(View convertView, int pos, Object item, String name, Object value) {
                ((TextView) convertView.findViewById(R.id.scorer_item_number_tv)).setText(pos + "");
                if (name.equals("header")) {
                    Glide.with(context).load(value.toString()).into((ImageView) convertView.findViewById(R.id.scorer_item_iv));
                }
                super.findAndBindView(convertView, pos, item, name, value);
                return true;
            }
        };
        reload(mapadapter);
        mList.getFooterLoadingLayout().setVisibility(View.GONE);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                reload(mapadapter);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                reload(mapadapter);
            }
        });
    }

    /**
     * 获取数据
     * */
    private void reload(final MapAdapter mapadapter) {
        BaseActivity.callHttp(MapBuilder.build()
                .add("client_id",client_id)
                .add("state",state)
                .add("access_token",access_token)
                .add("action","viewMatchAnalysis")
                .add("match_id","1")
                .add("type","3")//1积分榜2射手榜3助攻榜
                .get(), new FunCallback() {

            @Override
            public void onCallback(Object result, List object) {

            }

            @Override
            public void onFailure(Object result, List object) {
                if (page == 1){
                    mList.onPullDownRefreshComplete();
                }else{
                    mList.onPullUpRefreshComplete();
                }
            }

            @Override
            public void onSuccess(Object result, List object) {
                if (page ==1) {
                    mList.onPullDownRefreshComplete();
                    list = new ArrayList();
                }else {
                    mList.onPullUpRefreshComplete();
                }
                String data = ((NetEntity)result).getData().toString();
                if (data.equals("null") || data == null || data.toString().equals("")){
                    mEmptyLayout.setVisibility(View.VISIBLE);
                }else{
                    List list = (List) intf.JsonUtil.extractJsonRightValue(data);
                    if (page ==1) {
                        if (list.size()>0){
                            mapadapter.setItemDataSrc(new MapContent(list));
                            mList.getRefreshableView().setAdapter(mapadapter);
                        }else{
                            mEmptyLayout.setVisibility(View.GONE);
                        }
                    }else{
                        mEmptyLayout.setVisibility(View.GONE);
                        List lt = ((List) mapadapter.getItemDataSrc().getContent());
                        lt.addAll(list);
                        mapadapter.setItemDataSrc(new MapContent(lt));
                    }
                    mapadapter.notifyDataSetChanged();
                }
            }

        });
    }
}
