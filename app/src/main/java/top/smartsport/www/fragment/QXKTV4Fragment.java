package top.smartsport.www.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.base.MapAdapter;
import app.base.MapContent;
import intf.FunCallback;
import intf.JsonUtil;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.activity.ActivityTrainingDetails;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.utils.SPUtils;

/**
 * Created by Aaron on 2017/7/24.
 * 青训--青训课堂
 */
@ContentView(R.layout.fragment_qxkt)
public class QXKTV4Fragment extends BaseV4Fragment {
    @ViewInject(R.id.pullrefreshlistview)
    PullToRefreshListView pullrefreshlistview;
    @ViewInject(R.id.rl_empty)
    RelativeLayout rl_empty;
    private int mCurrentPage;

    @Override
    public void onStart() {
        super.onStart();
        initView();
    }

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
                    // 报名中 进行中 已结束
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

                    }
                }
                super.findAndBindView(convertView, pos, item, name, value);

                return true;
            }

            @Override
            protected void getViewInDetail(final Object item, int position, View convertView) {
                super.getViewInDetail(item, position, convertView);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Map map = (Map) item;
                        startActivity(new Intent(view.getContext(), ActivityTrainingDetails.class).putExtra("id", map.get("id").toString()));
                    }
                });
                convertView.findViewById(R.id.coach_name).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


//                        view.getContext().startActivity(new Intent(view.getContext(), CoachDetailActivity.class).putExtra("id", ((Map) item).get("coach_id").toString()));
                    }
                });
                convertView.findViewById(R.id.coach_head).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        view.getContext().startActivity(new Intent(view.getContext(), CoachDetailActivity.class).putExtra("id", ((Map) item).get("coach_id").toString()));

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
                reload(true);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                reload(false);

            }
        });
        reload(true);

    }

    private void reload(final boolean isRefresh) {
        if (isRefresh) {
            mCurrentPage = 1;
        } else {
            mCurrentPage++;
        }
        String level =  (String) SPUtils.get(getActivity(), "qx-level", "0");
        String status  =  (String)  SPUtils.get(getActivity(), "qx-status", "0");
        // TODO 预留：刷新课程列表数据
//        LogUtil.d("-------level------------>" + level);
//        LogUtil.d("-------status------------>" + status);
        String city =  SPUtils.get(getContext(), "qx-getCounties-city", null)+"";
        String county = SPUtils.get(getContext(), "qx-getCounties-county", null)+"";

        BaseActivity.callHttp(MapBuilder.build().add("action", "getRecommendCourses").add("page", mCurrentPage).add("level",level).add("status",status).add("city",city).add("county",county).get(), new FunCallback() {

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
                            rl_empty.setVisibility(View.GONE);
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
                        } else {
                            if(mCurrentPage == 1) { // 返回数据为空
                                rl_empty.setVisibility(View.VISIBLE);
                                List lt = new ArrayList();
                                mapadapter.setItemDataSrc(new MapContent(lt));
                                pullrefreshlistview.getRefreshableView().setAdapter(mapadapter);
                                mapadapter.notifyDataSetChanged();
                            }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                reload(true);
                break;
            default:
                break;
        }
    }

}
