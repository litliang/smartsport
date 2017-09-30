package top.smartsport.www.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import app.base.MapAdapter;
import app.base.MapConf;
import app.base.MapContent;
import intf.FunCallback;
import intf.JsonUtil;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.utils.SPUtils;

/**
 * Created by Aaron on 2017/7/18.
 * 我的视频
 */
@ContentView(R.layout.activity_mysp)
public class MySPActivity extends BaseActivity {

    @ViewInject(R.id.pullrefreshlistview)
    PullToRefreshListView pullrefreshlistview;
    private int mCurrentPage;

    @Override
    protected void initView() {
        pullrefreshlistview = (PullToRefreshListView) findViewById(R.id.pullrefreshlistview);
        pullrefreshlistview.setMode(PullToRefreshBase.Mode.BOTH);

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
        BaseActivity.callHttp(MapBuilder.build().add("action", "getMyVideo").add("page", mCurrentPage).get(), new FunCallback() {

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
                    Object sdata = JsonUtil.findJsonLink("video", data);
                    if (!(sdata == null || sdata.toString().equals("") || sdata.toString().equals("null"))) {
                        List list = (List) JsonUtil.extractJsonRightValue(sdata);

                        if (isRefresh) {
                            loadConfig(list);
                            getView(R.id.mykcempty).setVisibility(View.GONE);

//                            ((MapAdapter) pullrefreshlistview.getRefreshableView().getAdapter()).setItemDataSrc(new MapContent(list));
                        } else {
                            List lt = ((List) ((MapAdapter) pullrefreshlistview.getRefreshableView().getAdapter()).getItemDataSrc().getContent());
                            lt.addAll(list);
                            ((MapAdapter) pullrefreshlistview.getRefreshableView().getAdapter()).setItemDataSrc(new MapContent(lt));
                            ((MapAdapter) pullrefreshlistview.getRefreshableView().getAdapter()).notifyDataSetChanged();
                        }
                    } else {
                        if (!isRefresh) {
                            showToast("已经到底了");
                            pullrefreshlistview.onRefreshComplete();
                            pullrefreshlistview.setMode(PullToRefreshBase.Mode.PULL_DOWN_TO_REFRESH);
                            pullrefreshlistview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                        } else {
                            if (mCurrentPage == 1) { // 返回数据为空
                                List lt = new ArrayList();
                                getView(R.id.mykcempty).setVisibility(View.VISIBLE);

                                ((MapAdapter) pullrefreshlistview.getRefreshableView().getAdapter())
                                        .setItemDataSrc(new MapContent(lt));
                                pullrefreshlistview.getRefreshableView().setAdapter(((MapAdapter) pullrefreshlistview.getRefreshableView().getAdapter()));
                                ((MapAdapter) pullrefreshlistview.getRefreshableView().getAdapter()).notifyDataSetChanged();
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

    //[{"match":{"id":null,"name":null,"start_time":"1970-01-01 08:00:00","address":null,"level":"U","cover_url":"http://admin.smartsport.top/data/upload/default/logo.png"},"video":{"id":1,"title":"定制视频","sell_price":"199.00","pay_status":1,"video_url":"http://soccer.baibaobike.com/apps/admin/_static/file/upload/2017/08/01/video1.mp4","cover_url":"http://admin.smartsport.top/data/upload/2017/0915/15/59bb8370df471.jpg"}},{"match":{"id":"1","name":"14年青少年足球杯","start_time":"2017-09-19 12:22:56","address":"鸟巢","level":"U13","cover_url":"http://admin.smartsport.top/data/upload/2017/0915/12/59bb558f8ca74.jpg"},"video":{"id":1,"title":"定制视频","sell_price":"199.00","pay_status":1,"video_url":"http://soccer.baibaobike.com/apps/admin/_static/file/upload/2017/08/01/video1.mp4","cover_url":"http://admin.smartsport.top/data/upload/2017/0915/15/59bb8370df471.jpg"}}]
    private void loadConfig(List list) {

        MapConf m = MapConf.with(getBaseContext()).pair("match-level->level").pair("match-address->address").pair("match-start_time->date").pair("match-name->title").pair("match-cover_url->image").pair("video-pay_status->pay_status", "1:已支付;0:未支付", "showPayStatus()").pair("video-sell_price:¥%s->adapter_price").pair("video-title->video_name").pair("video-cover_url->adapter_bszb_img").source(R.layout.adapter_myvideo);
        MapConf.with(MySPActivity.this).conf(m).source(list, pullrefreshlistview.getRefreshableView()).toView();
        pullrefreshlistview.getRefreshableView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*parent.getItemAtPosition(position).toString()*/
                startActivity(new Intent(getBaseContext(), BSVideoActivity.class).putExtra("videoid", "1"));
            }
        });

    }

}
