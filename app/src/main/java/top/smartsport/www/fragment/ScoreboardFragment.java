package top.smartsport.www.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import app.base.JsonUtil;
import app.base.MapAdapter;
import app.base.MapConf;
import app.base.MapContent;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.H;
import top.smartsport.www.R;
import top.smartsport.www.adapter.ScoreboardAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.ScoreboardInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshBase;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by bajieaichirou on 17/9/8.
 * 数据分析之积分榜
 */
@ContentView(R.layout.fragment_scoreboard)
public class ScoreboardFragment extends BaseV4Fragment{

    @ViewInject(R.id.scoreboard_list)
    PullToRefreshListView  mList;

    @ViewInject(R.id.empty)
    RelativeLayout mEmptyLayout;

    private Activity mThis;
    private int page = 1;
//    private ScoreboardAdapter scoreboardAdapter;
    private MapAdapter mapadapter;
    private List<ScoreboardInfo> list = new ArrayList<>();

    private RegInfo regInfo;
    private TokenInfo tokenInfo;
    private String client_id, state, url, access_token;

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_scoreboard, container, false);
//    }

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
        adaptinfo.addListviewItemLayoutId(R.layout.adapter_scoreboard_item);
        adaptinfo.addViewIds(new Integer[]{R.id.item_number_tv, R.id.item_name_tv, R.id.item_result_tv, R.id.item_scored_tv, R.id.item_card_tv, R.id.item_score_tv});
        adaptinfo.addObjectFields(new String[]{"position", "name", "result", "scored", "card", "score"});
        mapadapter = new MapAdapter(getContext(), adaptinfo) {
            @Override
            protected boolean findAndBindView(View convertView, int pos, Object item, String name, Object value) {
                if (name.equals("card")) {
                    String temp = String.valueOf(value);
                    SpannableStringBuilder builder = new SpannableStringBuilder(temp);
                    int start_index = temp.indexOf("红");
                    int start_index1 = temp.indexOf("黄");
                    builder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF3B30")), start_index, start_index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.setSpan(new ForegroundColorSpan(Color.parseColor("#FFD700")), start_index1, start_index1 + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ((TextView) convertView.findViewById(R.id.item_card_tv)).setText(builder);
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
                .add("type","1")//1积分榜2射手榜3助攻榜
                .get(), new FunCallback<NetEntity, String, NetEntity>() {

            @Override
            public void onSuccess(NetEntity result, List<Object> object) {
            }

            @Override
            public void onFailure(String result, List<Object> object) {

            }

            @Override
            public void onCallback(NetEntity result, List<Object> object) {
                if (page ==1) {
                    mList.onPullDownRefreshComplete();
                    list = new ArrayList();
                }else {
                    mList.onPullUpRefreshComplete();
                }
                String data = result.getData().toString();
                Log.e("smile", "ScoreBoardFragment --------- data = " + data);
                if (data == null){
                    mEmptyLayout.setVisibility(View.VISIBLE);
                }else{
                    List list = (List) intf.JsonUtil.extractJsonRightValue(intf.JsonUtil.findJsonLink("courses", data));
                    if (page==1){
                        if (!(list.size()>0)){
                            mEmptyLayout.setVisibility(View.GONE);
                            return;
                        }
                    }else {
                        mEmptyLayout.setVisibility(View.GONE);
                    }
                    list.addAll(list);
                    mapadapter.setItemDataSrc(new MapContent(mList));
                    mList.getRefreshableView().setAdapter(mapadapter);
                    mapadapter.notifyDataSetChanged();
                }
            }
        });
    }
//    private void getData(final boolean refresh) {
//        JSONObject json = new JSONObject();
//        try {
//            json.put("client_id",client_id);
//            json.put("state",state);
//            json.put("access_token",access_token);
//            json.put("action","viewMatchAnalysis");
//            json.put("match_id","1");
//            json.put("type","1");//1积分榜2射手榜3助攻榜
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        X.Post(url, json, new MyCallBack<String>() {
//            @Override
//            protected void onFailure(String message) {
//                showToast(message);
//                if (refresh){
//                    mList.onPullUpRefreshComplete();
//                }else {
//                    mList.onPullDownRefreshComplete();
//                }
//            }
//
//            @Override
//            public void onSuccess(NetEntity entity) {
//                if (refresh){
//                    mList.onPullUpRefreshComplete();
//                }else {
//                    mList.onPullDownRefreshComplete();
//                }
//                String data = entity.getData().toString();
//                Log.e("smile", "ScoreBoardFragment --------- data = " + data);
////                list = (List) JsonUtil.extractJsonRightValue(entity.getData().toString());
////                scoreboardAdapter.setData(list);
////                MapConf mc = MapConf.with(getContext())
////                        .pair("position->item_number_tv")
////                        .pair("team_name->item_name_tv")
////                        .pair("member_name->item_card_tv")
////                        .pair("goal->item_score_tv")
////                        .source(R.layout.adapter_scoreboard_item);
////                MapConf.with(getContext()).conf(mc).source(data, mList.getRefreshableView()).toView();
//            }
//
//            @Override
//            public void onError(Throwable throwable, boolean b) {
//                super.onError(throwable, b);
//            }
//        });
//    }
}
