package top.smartsport.www.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import app.base.MapAdapter;
import app.base.MapContent;

import top.smartsport.www.activity.ActivityTrainingDetails;
import top.smartsport.www.activity.CoachDetailActivity;
import top.smartsport.www.bean.Carousel;
import top.smartsport.www.bean.HDZXInfo;
import top.smartsport.www.bean.SSXWInfo;
import top.smartsport.www.fragment.viewutils.InformationOperateUtils;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import intf.FunCallback;
import intf.JsonUtil;
import intf.MapBuilder;

import org.xutils.view.annotation.ContentView;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.NetEntity;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * Created by Aaron on 2017/7/24.
 * 青训--青训课堂
 */
@ContentView(R.layout.fragment_qxkt)
public class QXKTV4Fragment extends BaseV4Fragment {
    @ViewInject(R.id.pullrefreshlistview)
    PullToRefreshListView pullrefreshlistview;
    private int mCurrentPage;

    public static QXKTV4Fragment newInstance() {
        QXKTV4Fragment fragment = new QXKTV4Fragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;

    }


    MapAdapter mapadapter;

    @Override
    protected void initView() {
        pullrefreshlistview = (PullToRefreshListView) root.findViewById(R.id.pullrefreshlistview);
        pullrefreshlistview.setMode(PullToRefreshBase.Mode.BOTH);
        MapAdapter.AdaptInfo adaptinfo = new MapAdapter.AdaptInfo();
        adaptinfo.addListviewItemLayoutId(R.layout.qingxun_qingxunkecheng);
        adaptinfo.addViewIds(new Integer[]{R.id.image, R.id.title, R.id.date, R.id.address, R.id.u16, R.id.price, R.id.coach_head, R.id.coach_name, R.id.haishengjigeminge, R.id.woyaobaoming});
        adaptinfo.addObjectFields(new String[]{"cover_url", "title", "start_time", "address", "level", "sell_price", "coach_header", "coach_name", "surplus", "status"});
        mapadapter = new MapAdapter(getContext(), adaptinfo) {
            @Override
            protected boolean findAndBindView(View convertView, int pos, Object item, String name, Object value) {
                if (name.equals("level")) {
                    value = "U" + value;
                } else if (name.equals("sell_price")) {
                    value = "￥" + value.toString().replace(".00", "") + "/年";
                } else if (name.equals("surplus")) {

                    if (value.toString().equals("0")) {
                        convertView.findViewById(R.id.haishengjigeminge).setVisibility(View.GONE);
                        Drawable drawable = context.getResources().getDrawable(R.mipmap.yibaoman, null);
                        convertView.findViewById(R.id.woyaobaoming).setBackground(drawable);
                        ((TextView) convertView.findViewById(R.id.woyaobaoming)).setTextColor(getResources().getColor(R.color.text_hint, null));

                    } else {
                        convertView.findViewById(R.id.haishengjigeminge).setVisibility(View.VISIBLE);
                        Drawable drawable = context.getResources().getDrawable(R.drawable.shape_bg_round_corner_green, null);
                        convertView.findViewById(R.id.woyaobaoming).setBackground(drawable);
                        ((TextView) convertView.findViewById(R.id.woyaobaoming)).setTextColor(getResources().getColor(R.color.theme_color, null));
                    }
                    value = "还剩" + value + "个名额";
                    //1报名中2进行中 3已结束 4已报满5已报名
                } else if (name.equals("status")) {

                    int val = Integer.valueOf(value.toString());
                    switch (val) {
                        case 1:
                            value = "报名中";
                            break;

                        case 2:
                            value = "进行中";
                            break;
                        case 3:
                            value = "已结束";
                            break;
                        case 4:
                            value = "已报满";
                            break;
                        case 5:
                            value = "已报名";
                            break;
                    }
                }
                super.findAndBindView(convertView, pos, item, name, value);

                return true;
            }
            @Override
            protected void getViewInDetail(final Object item, int position, View convertView) {
                super.getViewInDetail(item, position, convertView);
                convertView.findViewById(R.id.coach_name).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        view.getContext().startActivity(new Intent(view.getContext(),CoachDetailActivity.class).putExtra("id",((Map)item).get("id").toString()));
                    }
                });
                convertView.findViewById(R.id.coach_head).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.getContext().startActivity(new Intent(view.getContext(),CoachDetailActivity.class).putExtra("id",((Map)item).get("id").toString()));

                    }
                });

            }
        };
        pullrefreshlistview.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
        pullrefreshlistview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                pullrefreshlistview.setMode(PullToRefreshBase.Mode.BOTH);
                reload(mapadapter, true);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                reload(mapadapter, false);

            }
        });
        reload(mapadapter, true);

    }

    private void reload(final MapAdapter mapadapter, final boolean isRefresh) {
        if (isRefresh) {
            mCurrentPage = 1;
        } else {
            mCurrentPage++;
        }
        BaseActivity.callHttp(MapBuilder.build().add("action", "getRecommendCourses").add("page", mCurrentPage).get(), new FunCallback() {

            @Override
            public void onCallback(Object result, List object) {

            }

            @Override
            public void onFailure(Object result, List object) {

            }

            @Override
            public void onSuccess(Object result, List object) {
                try {

                    String data = ((NetEntity) result).getData().toString();
                    Object sdata = JsonUtil.findJsonLink("courses", data);
                    if (!(sdata == null || sdata.toString().equals("") || sdata.toString().equals("null"))) {
                        List list = (List) JsonUtil.extractJsonRightValue(sdata);

                        if (isRefresh) {
                            mapadapter.setItemDataSrc(new MapContent(list));
                            pullrefreshlistview.getRefreshableView().setAdapter(mapadapter);
                        } else {
                            List lt = ((List) mapadapter.getItemDataSrc().getContent());
                            lt.addAll(list);
                            mapadapter.setItemDataSrc(new MapContent(lt));
                        }
                        mapadapter.notifyDataSetChanged();
                    } else {
                        if (!isRefresh) {
                            showToast("已经到底了");
                            pullrefreshlistview.onRefreshComplete();
                            pullrefreshlistview.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
                            pullrefreshlistview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        }
                    }
                    if (isRefresh) {
                        pullrefreshlistview.onRefreshComplete();
                    } else {
                        pullrefreshlistview.onRefreshComplete();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }


}
