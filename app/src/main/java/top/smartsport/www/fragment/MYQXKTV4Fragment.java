package top.smartsport.www.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.base.MapAdapter;
import app.base.MapContent;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.activity.ActivityTrainingDetails;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshBase;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;
import top.smartsport.www.utils.StringUtil;

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
    private List<Boolean> showStatus = new ArrayList<>();
    private List list;

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
                } else if(name.equals("status")){
                    if (value.toString().equals("1") || value.toString().equals("2")){
                        convertView.findViewById(R.id.ll_tome).setVisibility(View.GONE);
                        value = "进行中的青训";
                    }else {
                        value = "已结束的青训";
                        convertView.findViewById(R.id.ll_tome).setVisibility(View.VISIBLE);
                    }
                    boolean blStatus = showStatus.get(pos);
                    if(blStatus) {
                        convertView.findViewById(R.id.ll_head).setVisibility(View.VISIBLE);
                    } else {
                        convertView.findViewById(R.id.ll_head).setVisibility(View.GONE);
                    }
                }
                int status = convertView.findViewById(R.id.ll_tome).getVisibility();
                if(status == 0) {
                    ViewGroup nl = (ViewGroup) convertView.findViewById(R.id.ll_nl);
                    nl.removeAllViews();
                    Object obj = list.get(pos);
                    Map<String, Object> map = (Map<String, Object>) obj;
                    Object evaluates = (Object) map.get("evaluates");

                    String enduranceValue = "0";
                    String explosiveness = "0";
                    String technology = "0";
                    String cooperation = "0";
                    String speed = "0";
                    String content = "";
                    if(evaluates != null && !StringUtil.isEmpty(evaluates.toString()))  {
                        Map<String, Object> enduranceMap = (Map<String, Object>) evaluates;
                        enduranceValue = (String) enduranceMap.get("endurance");
                        explosiveness = (String) enduranceMap.get("explosiveness");
                        technology = (String) enduranceMap.get("technology");
                        cooperation = (String) enduranceMap.get("cooperation");
                        speed = (String) enduranceMap.get("speed");
                        content = (String) enduranceMap.get("content");
                    }

                    if(!StringUtil.isEmpty(content)) {
                        ((TextView) convertView.findViewById(R.id.content)).setText(content);
                    } else {
                        ((TextView) convertView.findViewById(R.id.content)).setText("");
                    }
                    int nlScore = 0;
                    if(!StringUtil.isEmpty(enduranceValue) && StringUtil.isNumeric(enduranceValue)) {
                        nlScore = Integer.valueOf(enduranceValue).intValue();
                    }
                    int bflScore = 0;
                    if(!StringUtil.isEmpty(explosiveness) && StringUtil.isNumeric(explosiveness)) {
                        bflScore = Integer.valueOf(explosiveness).intValue();
                    }
                    int jsScore = 0;
                    if(!StringUtil.isEmpty(technology) && StringUtil.isNumeric(technology)) {
                        jsScore = Integer.valueOf(technology).intValue();
                    }
                    int hzScore = 0;
                    if(!StringUtil.isEmpty(cooperation) && StringUtil.isNumeric(cooperation)) {
                        hzScore = Integer.valueOf(cooperation).intValue();
                    }
                    int sdScore = 0;
                    if(!StringUtil.isEmpty(speed) && StringUtil.isNumeric(speed)) {
                        sdScore = Integer.valueOf(speed).intValue();
                    }

                    for (int i =0;i<5;i++){ // 耐力
                        ImageView iv = new ImageView(convertView.getContext());
                        if(nlScore > 0) {
                            if(i <= (nlScore - 1)) {
                                iv.setImageResource(R.mipmap.pj_yes);
                            } else {
                                iv.setImageResource(R.mipmap.pj_no);
                            }
                        } else {
                            iv.setImageResource(R.mipmap.pj_no);
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0,0,30,0);
                        iv.setLayoutParams(params);
                        nl.addView(iv);
                    }
                    ViewGroup bfl = (ViewGroup) convertView.findViewById(R.id.ll_bfl);
                    bfl.removeAllViews();
                    for (int i =0;i<5;i++){ // 爆发力
                        ImageView iv = new ImageView(convertView.getContext());
                        if(bflScore > 0) {
                            if(i <= (bflScore - 1)) {
                                iv.setImageResource(R.mipmap.pj_yes);
                            } else {
                                iv.setImageResource(R.mipmap.pj_no);
                            }
                        } else {
                            iv.setImageResource(R.mipmap.pj_no);
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0,0,30,0);
                        iv.setLayoutParams(params);
                        bfl.addView(iv);
                    }
                    ViewGroup js = (ViewGroup) convertView.findViewById(R.id.ll_js);
                    js.removeAllViews();
                    for (int i =0;i<5;i++){ // 技术
                        ImageView iv = new ImageView(convertView.getContext());
                        if(jsScore > 0) {
                            if(i <= (jsScore - 1)) {
                                iv.setImageResource(R.mipmap.pj_yes);
                            } else {
                                iv.setImageResource(R.mipmap.pj_no);
                            }
                        } else {
                            iv.setImageResource(R.mipmap.pj_no);
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0,0,30,0);
                        iv.setLayoutParams(params);
                        js.addView(iv);
                    }
                    ViewGroup hz = (ViewGroup) convertView.findViewById(R.id.ll_hz);
                    hz.removeAllViews();
                    for (int i =0;i<5;i++){ // 合作
                        ImageView iv = new ImageView(convertView.getContext());
                        if(hzScore > 0) {
                            if(i <= (hzScore - 1)) {
                                iv.setImageResource(R.mipmap.pj_yes);
                            } else {
                                iv.setImageResource(R.mipmap.pj_no);
                            }
                        } else {
                            iv.setImageResource(R.mipmap.pj_no);
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0,0,30,0);
                        iv.setLayoutParams(params);
                        hz.addView(iv);
                    }
                    ViewGroup sd = (ViewGroup) convertView.findViewById(R.id.ll_sd);
                    sd.removeAllViews();
                    for (int i =0;i<5;i++){ // 速度
                        ImageView iv = new ImageView(convertView.getContext());
                        if(sdScore > 0) {
                            if(i <= (sdScore - 1)) {
                                iv.setImageResource(R.mipmap.pj_yes);
                            } else {
                                iv.setImageResource(R.mipmap.pj_no);
                            }
                        } else {
                            iv.setImageResource(R.mipmap.pj_no);
                        }
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.setMargins(0,0,30,0);
                        iv.setLayoutParams(params);
                        sd.addView(iv);
                    }
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
        pullrefreshlistview.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map map = (Map) adapterView.getItemAtPosition(i);
                startActivity(new Intent(view.getContext(),ActivityTrainingDetails.class).putExtra("id", map.get("id").toString()));
            }
        });
    }

    private void reload(final MapAdapter mapadapter) {
        BaseActivity.callHttp(MapBuilder.build().add("action", "getMyCourse").add("pay_status", 1).add("product_type", 1).add("page",page).get(), new FunCallback() {

            @Override
            public void onCallback(Object result, List object) {

            }

            @Override
            public void onFailure(Object result, List object) {
                if (page == 1){
                    pullrefreshlistview.onPullDownRefreshComplete();
                }else{
                    pullrefreshlistview.onPullUpRefreshComplete();
                }
            }

            @Override
            public void onSuccess(Object result, List object) {
                String data = ((NetEntity)result).getData().toString();
//                List list = (List) JsonUtil.extractJsonRightValue(data);
                if(page == 1) {
                    showStatus.clear();
                }
                list = (List) intf.JsonUtil.extractJsonRightValue(intf.JsonUtil.findJsonLink("playing", data)); //进行中
                List listWatting = (List) intf.JsonUtil.extractJsonRightValue(intf.JsonUtil.findJsonLink("watting", data)); //报名中
                List listOver = (List) intf.JsonUtil.extractJsonRightValue(intf.JsonUtil.findJsonLink("over", data)); // 已结束
                if(list == null) {
                    list = new ArrayList();
                } else {
                    for(int i=0; i<list.size(); i++) {
                        if(page == 1 && i == 0) {
                            showStatus.add(true);
                        } else {
                            showStatus.add(false);
                        }
                    }
                }
                if(listWatting != null && listWatting.size() > 0) {
                    for(int i=0; i<listWatting.size(); i++) {
                        if(list.size() == 0 && i == 0) {
                            showStatus.add(true);
                        } else {
                            showStatus.add(false);
                        }
                    }
                    list.addAll(listWatting);
                }
                if(listOver != null && listOver.size() > 0) {
                    list.addAll(listOver);
                    for(int i=0; i<listOver.size(); i++) {
                        if(page == 1 && i == 0) {
                            showStatus.add(true);
                        } else {
                            showStatus.add(false);
                        }
                    }
                }
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
