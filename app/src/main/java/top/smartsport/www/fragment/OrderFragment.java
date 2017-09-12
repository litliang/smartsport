package top.smartsport.www.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import app.base.JsonUtil;
import app.base.MapConf;
import top.smartsport.www.R;
import top.smartsport.www.activity.OrderDetailsActivity;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshBase;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

@ContentView(R.layout.fragment_list)
public class OrderFragment extends BaseV4Fragment implements PullToRefreshBase.OnRefreshListener<ListView> {
    private static final String PAY_STATUS = "pay_status";
    @ViewInject(R.id.id_listview)
    PullToRefreshListView mlistview;
    @ViewInject(R.id.mykcempty)
    ViewGroup empty;
    private View mView;
    private List<Object> mList;
    private Context mContext;
    private int status;
    private int page =1;
    private RegInfo regInfo;
    private TokenInfo tokenInfo;
    private String client_id;
    private String state;
    private String url;
    private String access_token;
    private MyAdapter adapter;


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
        mList= new ArrayList<>();
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();
        mlistview.setOnRefreshListener(this);
        adapter = new MyAdapter(mList);
        mlistview.getRefreshableView().setAdapter(adapter);
        getData(true);
    }
//

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        page = 1;
        getData(true);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        page ++;
        getData(false);
    }

    private void getData(final boolean refresh) {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","getMyOrders");
            json.put("page",page);
            if (status!=2){
                json.put("pay_status",status);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                showToast(message);
                if (refresh){
                    mlistview.onPullUpRefreshComplete();
                }else {
                    mlistview.onPullDownRefreshComplete();
                }
            }

            @Override
            public void onSuccess(NetEntity entity) {
                String data = entity.getData().toString();
                mList = (List) JsonUtil.extractJsonRightValue(entity.getData().toString());
                if (refresh){
                    mlistview.onPullUpRefreshComplete();
                    if (mList !=null &&mList.size()>0){
                        empty.setVisibility(View.GONE);
                    }else {
                        empty.setVisibility(View.VISIBLE);
                    }
                }else {
                    mlistview.onPullDownRefreshComplete();
                }

                adapter.setData(mList);
                MapConf mc = MapConf.with(getContext())
                        .pair("cover_url->iv_pic")
                        .pair("title->tv_title")
                        .pair("end_time->tv_date")
                        .pair("address->tv_address")
                        .pair("pay_total:￥%s->tv_price")
                        .pair("pay_status->pay_status","0:未支付;1:已支付","showPayStatus()")
                        .source(R.layout.list_item);
                MapConf.with(getContext()).conf(mc).source(data, mlistview.getRefreshableView()).toView();
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
            }
        });
    }

    class MyAdapter extends BaseAdapter {
        private List<Object> list;

        public MyAdapter(List<Object> list) {
            this.list = list;
        }
        public void setData(List<Object> l){
            if (page==1){
                this.list = l;
            }else {
                list = new ArrayList<>();
                list.addAll(l);
                notifyDataSetChanged();
            }
        }
        @Override
        public int getCount() {
            return list!=null?list.size():0;
        }

        @Override
        public Object getItem(int arg0) {
            return list.get(arg0);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);
                holder.ivPic = (ImageView) convertView.findViewById(R.id.iv_pic);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
                holder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
                holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
                holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
                holder.tv_Status = (TextView) convertView.findViewById(R.id.pay_status);
                convertView.setTag(holder);
                //对于listview，注意添加这一行，即可在item上使用高度
                AutoUtils.autoSize(convertView);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String txt = "";
          if( JsonUtil.findJsonLink("pay_status",list.get(position)).toString().equals("0")) {
              txt = "去支付";
              holder.tv_Status.setBackgroundResource(R.drawable.shape_go_pay );
              holder.tv_Status.setTextColor(Color.parseColor("#3bb862"));
            }else {
              txt = "已完成";
              holder.tv_Status.setBackgroundResource(R.drawable.shape_wait_pay);
              holder.tv_Status.setTextColor(Color.parseColor("#e5e5e5"));
          }
            holder.tv_Status.setText(txt);
            holder.tv_Status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getContext(), OrderDetailsActivity.class);
                    startActivity(intent);
                }
            });
            return convertView;
        }

    }

    static class ViewHolder {
        TextView tvTitle;
        TextView tvDate;
        TextView tvAddress;
        TextView tvPrice;
        TextView tv_Status;
        ImageView ivPic;
    }

}
