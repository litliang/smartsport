package top.smartsport.www.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
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
import top.smartsport.www.base.BaseFragment;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshBase;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;

/**
 * Created by Aaron on 2017/7/24.
 * 青训--青训课堂
 */
@ContentView(R.layout.fragment_myqxkt)
public class MYQXKTFragment extends BaseFragment {
    @ViewInject(R.id.pullrefreshlistview)
    PullToRefreshListView pullrefreshlistview;

    public static MYQXKTFragment newInstance() {
        MYQXKTFragment fragment = new MYQXKTFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;

    }

    MapAdapter mapadapter;

    @Override
    protected void initView() {
        pullrefreshlistview = (PullToRefreshListView) root.findViewById(R.id.pullrefreshlistview);
        MapAdapter.AdaptInfo adaptinfo = new MapAdapter.AdaptInfo();
        adaptinfo.addListviewItemLayoutId(R.layout.qingxun_qingxunkecheng);
        adaptinfo.addViewIds(new Integer[]{R.id.image, R.id.title, R.id.date, R.id.address, R.id.u16, R.id.price, R.id.coach_head, R.id.coach_name, R.id.haishengjigeminge});
        adaptinfo.addObjectFields(new String[]{"cover_url", "title", "start_time", "address", "level", "sell_price", "coach_header", "coach_name", "surplus"});
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
                        ((TextView) convertView.findViewById(R.id.woyaobaoming)).setTextColor(getResources().getColor(R.color.text_hint,null));

                    } else {
                        convertView.findViewById(R.id.haishengjigeminge).setVisibility(View.VISIBLE);
                        Drawable drawable = context.getResources().getDrawable(R.drawable.shape_bg_round_corner_green, null);
                        convertView.findViewById(R.id.woyaobaoming).setBackground(drawable);
                        ((TextView) convertView.findViewById(R.id.woyaobaoming)).setTextColor(getResources().getColor(R.color.theme_color,null));
                    }
                    value = "还剩" + value + "个名额";
                }
                super.findAndBindView(convertView, pos, item, name, value);

                return true;
            }
        };
//        reload(mapadapter);
        pullrefreshlistview.getFooterLoadingLayout().setVisibility(View.GONE);
        pullrefreshlistview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                reload(mapadapter);

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

    }

    private void reload(final MapAdapter mapadapter) {
        BaseActivity.callHttp(MapBuilder.build().add("action", "getRecommendCourses").get(), new FunCallback<NetEntity, String, NetEntity>() {

            @Override
            public void onSuccess(NetEntity result, List<Object> object) {

            }

            @Override
            public void onFailure(String result, List<Object> object) {

            }

            @Override
            public void onCallback(NetEntity result, List<Object> object) {
                pullrefreshlistview.onPullDownRefreshComplete();
                String data = result.getData().toString();
                List list = (List) JsonUtil.extractJsonRightValue(JsonUtil.findJsonLink("courses", data));
                mapadapter.setItemDataSrc(new MapContent(list));
                pullrefreshlistview.getRefreshableView().setAdapter(mapadapter);
                mapadapter.notifyDataSetChanged();


            }
        });
    }


}
