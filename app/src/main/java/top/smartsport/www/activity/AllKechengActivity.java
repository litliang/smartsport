package top.smartsport.www.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;
import java.util.Map;

import app.base.MapAdapter;
import app.base.MapContent;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.Coaches;
import top.smartsport.www.bean.NetEntity;

/**
 * deprecation:全部课程
 * author:AnYB
 * time:2017/9/24
 */
@ContentView(R.layout.activity_all_kecheng)
public class AllKechengActivity extends BaseActivity {
    @ViewInject(R.id.pullrefreshlistview)
    PullToRefreshListView pullrefreshlistview;
    MapAdapter mapadapter;

    @Override
    protected void initView() {
        MapAdapter.AdaptInfo adaptinfo = new MapAdapter.AdaptInfo();
        adaptinfo.addListviewItemLayoutId(R.layout.qingxun_qingxunkecheng);
        adaptinfo.addViewIds(new Integer[]{R.id.image, R.id.title, R.id.date, R.id.address, R.id.u16, R.id.price, R.id.coach_head, R.id.coach_name, R.id.haishengjigeminge, R.id.woyaobaoming});
        adaptinfo.addObjectFields(new String[]{"cover_url", "title", "start_time", "address", "level", "sell_price", "coach_header", "coach_name", "surplus","status"});
        mapadapter = new MapAdapter(this, adaptinfo) {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
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
                        ((TextView) convertView.findViewById(R.id.woyaobaoming)).setTextColor(getResources().getColor(R.color.text_hint,null));

                    } else {
                        convertView.findViewById(R.id.haishengjigeminge).setVisibility(View.VISIBLE);
                        Drawable drawable = context.getResources().getDrawable(R.drawable.shape_bg_round_corner_green, null);
                        convertView.findViewById(R.id.woyaobaoming).setBackground(drawable);
                        ((TextView) convertView.findViewById(R.id.woyaobaoming)).setTextColor(getResources().getColor(R.color.theme_color,null));
                    }
                    value = "还剩" + value + "个名额";
                }else if(name.equals("status")){

                    int val = Integer.valueOf(value.toString());
                    switch (val){
                        case 1:value = "报名中";
                            break;

                        case 2:value = "进行中";
                            break;
                        case 3:value = "已结束";
                            break;
                        case 4:value = "已报满";
                            break;
                        case 5:value = "已报名";
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
        pullrefreshlistview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        pullrefreshlistview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                getData();
            }
        });
        getData();
        pullrefreshlistview.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Map map = (Map) adapterView.getItemAtPosition(i);
                startActivity(new Intent(view.getContext(),ActivityTrainingDetails.class).putExtra("id", map.get("id").toString()));
            }
        });
    }


    private void getData(){
        BaseActivity.callHttp(MapBuilder.build().add("action", "getRecommendCourses").get(), new FunCallback<NetEntity, String, NetEntity>() {
            @Override
            public void onSuccess(NetEntity result, List<Object> object) {
            }
            @Override
            public void onFailure(String result, List<Object> object) {
            }
            @Override
            public void onCallback(NetEntity result, List<Object> object) {
                pullrefreshlistview.onRefreshComplete();
                String data = result.getData().toString();
                List list = (List) intf.JsonUtil.extractJsonRightValue(intf.JsonUtil.findJsonLink("courses", data));
                mapadapter.setItemDataSrc(new MapContent(list));
                pullrefreshlistview.getRefreshableView().setAdapter(mapadapter);
                mapadapter.notifyDataSetChanged();
            }
        });
    }


}
