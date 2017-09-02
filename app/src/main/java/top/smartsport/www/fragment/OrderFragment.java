package top.smartsport.www.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import top.smartsport.www.R;
import top.smartsport.www.activity.MyOrderActivity;
import top.smartsport.www.activity.OrderDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    private static final String PAY_STATUS = "pay_status";
    //    @BindView(R.id.id_listview)
    ListView mlistview;
    private View mView;
    private List<PayOrder> mList;
    private Context mContext;
    private int status;

    public static OrderFragment newInstance(int status) {
        Bundle args = new Bundle();
        args.putInt(PAY_STATUS, status);
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            status = bundle.getInt(PAY_STATUS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_list, container, false);
//        ButterKnife.bind(this,mView);
        initView(mView);
        return mView;
    }

    private void initView(View view) {
        mList = new ArrayList<>();
        mlistview = (ListView) view.findViewById(R.id.id_listview);
        mContext = getActivity();
        PayOrder order1 = new PayOrder("比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题", "2017-05-28", "上海市虹口体育馆", "¥1999/年", 0);
        PayOrder order2 = new PayOrder("比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题", "2017-05-28", "上海市虹口体育馆", "¥1999/年", 1);
        PayOrder order3 = new PayOrder("比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题，比赛活动标题", "2017-05-28", "上海市虹口体育馆", "¥1999/年", 2);
        switch (status) {
            case 0:
                mList.add(order1);
                mList.add(order2);
                mList.add(order3);
                break;
            case 1:
                mList.add(order1);
                mList.add(order2);
                break;
            case 2:
                mList.add(order3);
                break;
        }
        MyAdapter adapter = new MyAdapter();
        mlistview.setAdapter(adapter);

    }

    class MyAdapter extends BaseAdapter {
//        private List<PayOrder> list;
//        public MyAdapter() {
//            list = new ArrayList<PayOrder>();
//            switch (status){
//                case 0:
//                    list = mList;
//                    break;
//                case 1:
//                    for (PayOrder po : mList) {
//                        if (po.status == 0 || po.status == 1) {
//                            list.add(po);
//                        }
//                    }
//                    break;
//                case 2:
//                    for (PayOrder po : mList) {
//                        if (po.status == 2) {
//                            list.add(po);
//                        }
//                    }
//                    break;
//            }
//
//        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return mList.get(arg0);
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
            switch (mList.get(position).status) {
                case 0:
                    txt = "去支付";
                    break;
                case 1:
                    txt = "审核中";
                    break;
                case 2:
                    txt = "已完成";
                    break;
            }
            holder.tv_Status.setBackgroundResource(mList.get(position).status == 0 ? R.drawable.shape_go_pay : R.drawable.shape_wait_pay);
            holder.tv_Status.setTextColor(Color.parseColor(mList.get(position).status == 0 ? "#3bb862" : "#e5e5e5"));
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

    private class PayOrder {
        public String title;
        public String date;
        public String address;
        public String price;
        public int status;


        public PayOrder(String title, String date, String address, String price, int status) {
            this.title = title;
            this.date = date;
            this.address = address;
            this.price = price;
            this.status = status;
        }
    }
}
