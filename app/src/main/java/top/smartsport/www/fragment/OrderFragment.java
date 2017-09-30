package top.smartsport.www.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.base.JsonUtil;
import app.base.MapConf;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.activity.ActivityOrderConfirm;
import top.smartsport.www.activity.ActivitySignUp;
import top.smartsport.www.activity.ActivityTrainingDetails;
import top.smartsport.www.activity.BSDetailActivity;
import top.smartsport.www.activity.BSSignUpActivity;
import top.smartsport.www.activity.BuyVipActivity;
import top.smartsport.www.activity.MyHYActivity;
import top.smartsport.www.activity.MyOrderActivity;
import top.smartsport.www.activity.OrderDetailsActivity;
import top.smartsport.www.activity.SSBMActivity;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.BSDetail;
import top.smartsport.www.bean.BSssInfo;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import top.smartsport.www.utils.pay.PayUtil;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

@ContentView(R.layout.fragment_list)
public class OrderFragment extends BaseV4Fragment {
    private static final String PAY_STATUS = "pay_status";
    @ViewInject(R.id.id_listview)
    PullToRefreshListView mlistview;
    @ViewInject(R.id.mykcempty)
    ViewGroup empty;
    private View mView;
    private List<Object> mList;
    private Context mContext;
    private int status;
    private int page = 1;
    private RegInfo regInfo;
    private TokenInfo tokenInfo;
    private String client_id;
    private String state;
    private String url;
    private String access_token;


    public static OrderFragment newInstance(int status) {
        Bundle args = new Bundle();
        args.putInt(PAY_STATUS, status);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            status = bundle.getInt(PAY_STATUS);
        }
        mList = new ArrayList<>();
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();
        mlistview.setOnRefreshListener(new com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2<ListView>() {

            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mlistview.setMode(PullToRefreshListView.Mode.BOTH);
                page = 1;
                getData(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                getData(false);
            }
        });
        getData(true);
    }
//


    private void getData(final boolean refresh) {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "getMyOrders");
            json.put("page", page);
            if (status != 2) {
                json.put("pay_status", status);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                showToast(message);
                mlistview.onRefreshComplete();
            }

            @Override
            public void onSuccess(NetEntity entity) {
                String data = entity.getData().toString();
                mList = (List) JsonUtil.extractJsonRightValue(entity.getData().toString());
                if (refresh) {
                    if (mList != null && mList.size() > 0) {
                        empty.setVisibility(View.GONE);
                    } else {
                        empty.setVisibility(View.VISIBLE);
                    }
                }
                mlistview.onRefreshComplete();

//                adapter.setData(mList);
                MapConf mc = MapConf.with(getContext())
                        .pair("cover_url->iv_pic")
                        .pair("title->tv_title")
                        .pair("end_time->tv_date")
                        .pair("address->tv_address")
                        .pair("pay_total:￥%s->tv_price")
                        .pair("pay_status->pay_status", "0:未支付;1:已支付", "showPayStatus()")
                        .source(R.layout.list_item);
                MapConf.with(getContext()).conf(mc).source(data, mlistview.getRefreshableView()).toView();
                mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Map m = (Map) parent.getItemAtPosition(position);
                        //1青训报名2赛事报名3会员购买
                        String pid = m.get("id").toString();
//0 = {HashMap$HashMapEntry@5710} "address" -> "上海市闵行区虹桥足球场"
//                        1 = {HashMap$HashMapEntry@5711} "title" -> "如何当好守门员"
//                        2 = {HashMap$HashMapEntry@5712} "status" -> "1"
//                        3 = {HashMap$HashMapEntry@5713} "id" -> "1"
//                        4 = {HashMap$HashMapEntry@5714} "cover_url" -> "http://admin.smartsport.top/data/upload/2017/0915/13/59bb5e6b0405a.jpg"
//                        5 = {HashMap$HashMapEntry@5715} "start_time" -> "2017-10-15"
//                        6 = {HashMap$HashMapEntry@5716} "pay_status" -> "1"
//                        7 = {HashMap$HashMapEntry@5717} "pay_total" -> "0.01"
//                        8 = {HashMap$HashMapEntry@5718} "level" -> "12"
//                        9 = {HashMap$HashMapEntry@5719} "product_type" -> "1"
                        if (((TextView) view.findViewById(R.id.pay_status)).getText().toString().equals("未支付")) {
                            Map p = MapBuilder.build().add("type", m.get("product_type")).add("product_id", pid).add("total", m.get("pay_total").toString()).get();
                            startActivity(new Intent(getContext(), ActivityOrderConfirm.class).putExtra("data", (Serializable) p));

                        } else if (m.get("product_type").toString().equals("1")) {
// 1报名中2进行中 3已结束 4已报满5已报名
                            if(m.get("status").toString().equals("1")){
                            Map map = MapBuilder.build().add("qx_course_id", pid).add("details_title_tv", m.get("title")).add("details_time_tv", m.get("start_time")).add("details_training_ground_tv", "").add("details_amount_tv", m.get("pay_total")).get();

                            startActivity(new Intent(getContext(), ActivitySignUp.class).putExtra("data", (Serializable) map));

                            }else{
                                startActivity(new Intent(view.getContext(),ActivityTrainingDetails.class).putExtra("id", m.get("id").toString()));

                            }


                        } else if (m.get("product_type").toString().equals("2")) {
                            if(m.get("status").toString().equals("1")) {
                                Bundle bundle = new Bundle();
                            bundle.putString(SSBMActivity.TAG, pid);
                            ((MyOrderActivity) getActivity()).goActivity(SSBMActivity.class, bundle);

                            }else{

                                Bundle bundle = new Bundle();
                                bundle.putString(BSDetailActivity.TAG, m.get("id").toString());
                                bundle.putString("states", m.get("status").toString());
                                toActivity(BSDetailActivity.class, bundle);
                            }

                        } else if (m.get("product_type").toString().equals("3")) {
                            startActivity(new Intent(getContext(), MyHYActivity.class));

                        }
                    }
                });
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getData(true);
    }
}
