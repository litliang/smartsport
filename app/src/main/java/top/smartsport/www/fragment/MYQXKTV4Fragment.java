package top.smartsport.www.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import app.base.MapAdapter;
import app.base.MapContent;
import intf.FunCallback;
import intf.JsonUtil;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshBase;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;

/**
 * Created by Aaron on 2017/7/24.
 * 我的课程--青训课堂
 */
@ContentView(R.layout.fragment_myqxkt)
public class MYQXKTV4Fragment extends BaseV4Fragment {
    @ViewInject(R.id.pullrefreshlistview)
    PullToRefreshListView pullrefreshlistview;
    @ViewInject(R.id.mykcempty)
    ViewGroup empty;
    private int page =1;

    public static MYQXKTV4Fragment newInstance() {
        MYQXKTV4Fragment fragment = new MYQXKTV4Fragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;

    }

    MapAdapter mapadapter;

    @Override
    protected void initView() {
        pullrefreshlistview.getRefreshableView().setDivider(new ColorDrawable(Color.parseColor("#d6d6d9")));
        pullrefreshlistview.getRefreshableView().setDividerHeight(20);
        final MapAdapter.AdaptInfo adaptinfo = new MapAdapter.AdaptInfo();
        adaptinfo.addListviewItemLayoutId(R.layout.sc_kecheng);
        adaptinfo.addViewIds(new Integer[]{R.id.tv_head,R.id.image, R.id.title, R.id.date, R.id.address, R.id.u16, R.id.price, R.id.coach_head, R.id.coach_name});
        adaptinfo.addObjectFields(new String[]{"status","cover_url", "title", "start_time", "address", "level", "pay_total", "coach_header", "coach_name"});
        mapadapter = new MapAdapter(getContext(), adaptinfo) {
            @Override
            protected boolean findAndBindView(View convertView, int pos, Object item, String name, Object value) {
                if (name.equals("level")) {
                    value = "U" + value;
                } else if (name.equals("pay_total")) {
                    value = "￥" + value.toString().replace(".00", "") + "/年";
                } else if (name.equals("surplus")) {
                    if (value.toString().equals("0")) {
                        convertView.findViewById(R.id.haishengjigeminge).setVisibility(View.GONE);
                        Drawable drawable = context.getResources().getDrawable(R.mipmap.yibaoman, null);
                        convertView.findViewById(R.id.woyaobaoming).setBackground(drawable);
                        ((TextView) convertView.findViewById(R.id.woyaobaoming)).setTextColor(getResources().getColor(R.color.text_hint,null));

                    } else {
                        convertView.findViewById(R.id.haishengjigeminge).setVisibility(View.VISIBLE);
                        Drawable drawable = context.getResources().getDrawable(R.drawable.shape_bg_round_corner_green, null);
                        convertView.findViewById(R.id.woyaobaoming).setBackground(drawable);
                        ((TextView) convertView.findViewById(R.id.woyaobaoming)).setTextColor(getResources().getColor(R.color.theme_color,null));
                    }
                    value = "还剩" + value + "个名额";
                }else if(name.equals("status")){
                    if (pos ==0) {
                        convertView.findViewById(R.id.tv_head).setVisibility(View.VISIBLE);
                    }
                    if (value.toString().equals("1")){
                        ((TextView) convertView.findViewById(R.id.tv_head)).setText("进行中的青训");
                    }else {
                        value = "已结束的青训";
                        convertView.findViewById(R.id.ll_tome).setVisibility(View.VISIBLE);
                    }
                }
                ViewGroup nl = (ViewGroup) convertView.findViewById(R.id.ll_nl);
                nl.removeAllViews();
                for (int i =0;i<5;i++){
                    ImageView iv = new ImageView(convertView.getContext());
                    iv.setImageResource(R.mipmap.pj_yes);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,30,0);
                    iv.setLayoutParams(params);
                    nl.addView(iv);
                }
                ViewGroup bfl = (ViewGroup) convertView.findViewById(R.id.ll_bfl);
                bfl.removeAllViews();
                for (int i =0;i<5;i++){
                    ImageView iv = new ImageView(convertView.getContext());
                    iv.setImageResource(R.mipmap.pj_yes);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,30,0);
                    iv.setLayoutParams(params);
                    bfl.addView(iv);
                }
                ViewGroup js = (ViewGroup) convertView.findViewById(R.id.ll_js);
                js.removeAllViews();
                for (int i =0;i<5;i++){
                    ImageView iv = new ImageView(convertView.getContext());
                    iv.setImageResource(R.mipmap.pj_yes);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,30,0);
                    iv.setLayoutParams(params);
                    js.addView(iv);
                }
                ViewGroup hz = (ViewGroup) convertView.findViewById(R.id.ll_hz);
                hz.removeAllViews();
                for (int i =0;i<5;i++){
                    ImageView iv = new ImageView(convertView.getContext());
                    iv.setImageResource(R.mipmap.pj_yes);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,30,0);
                    iv.setLayoutParams(params);
                    hz.addView(iv);
                }
                ViewGroup sd = (ViewGroup) convertView.findViewById(R.id.ll_sd);
                sd.removeAllViews();
                for (int i =0;i<5;i++){
                    ImageView iv = new ImageView(convertView.getContext());
                    iv.setImageResource(R.mipmap.pj_yes);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(0,0,30,0);
                    iv.setLayoutParams(params);
                    sd.addView(iv);
                }
                super.findAndBindView(convertView, pos, item, name, value);

                return true;
            }
        };
        reload(mapadapter);
        pullrefreshlistview.getFooterLoadingLayout().setVisibility(View.GONE);
        pullrefreshlistview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page =1;
                reload(mapadapter);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page ++;
                reload(mapadapter);
            }
        });

    }

    private void reload(final MapAdapter mapadapter) {
        BaseActivity.callHttp(MapBuilder.build().add("action", "getMyOrders").add("pay_status", 1).add("product_type", 1).add("page",page).get(), new FunCallback<NetEntity, String, NetEntity>() {

            @Override
            public void onSuccess(NetEntity result, List<Object> object) {

            }

            @Override
            public void onFailure(String result, List<Object> object) {
                if (page == 1){
                    pullrefreshlistview.onPullDownRefreshComplete();
                }else{
                    pullrefreshlistview.onPullUpRefreshComplete();
                }
            }

            @Override
            public void onCallback(NetEntity result, List<Object> object) {
                String data = result.getData().toString();
                List list = (List) JsonUtil.extractJsonRightValue(data);
                if (page == 1){
                    pullrefreshlistview.onPullDownRefreshComplete();
                    if (list!=null && list.size()>0){
                        empty.setVisibility(View.GONE);
                    }else {
                        empty.setVisibility(View.VISIBLE);
                    }
                }else{
                    pullrefreshlistview.onPullUpRefreshComplete();
                    if (list!=null && list.size()>0){
                        empty.setVisibility(View.GONE);
                    }else {
                        return;
                    }
                }
                mapadapter.setItemDataSrc(new MapContent(list));
                pullrefreshlistview.getRefreshableView().setAdapter(mapadapter);
                mapadapter.notifyDataSetChanged();


            }
        });
    }


}
