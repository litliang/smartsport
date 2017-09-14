package top.smartsport.www.activity;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dl7.player.media.IjkPlayerView;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;

import app.base.MapConf;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.adapter.OnLineVideoAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.OnLineVideoInfo;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshBase;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;
import top.smartsport.www.widget.MyListView;

/**
 * Created by bajieaichirou on 17/9/9.
 */

@ContentView(R.layout.activity_online_video)
public class ActivityOnLineVideo extends BaseActivity {

    private ImageView mIv;
    private TextView mNameTv, mGradeTv, mCountryTv,
            mSportTv, mCountTv;
    private MyListView mListView;

    private OnLineVideoAdapter onLineVideoAdapter;
    private OnLineVideoInfo info;
    private List<OnLineVideoInfo> list = new ArrayList<>();

    private IjkPlayerView mPlayerView;

    @Override
    protected void initView() {
        initUI();
        fav();
//        getData(true);
    }

    String id;
    public void favImpl(final View view, boolean unfav) {
        fav.run(view, unfav+"", 2, getIntent().getStringExtra("id"));
    }



    private void initUI() {
        id = getIntent().getStringExtra("id");
        mNameTv = (TextView) findViewById(R.id.online_name_tv);
        mGradeTv = (TextView) findViewById(R.id.online_grade_tv);
        mCountryTv = (TextView) findViewById(R.id.online_country_tv);
        mSportTv = (TextView) findViewById(R.id.online_sport_tv);
        mCountTv = (TextView) findViewById(R.id.online_watched_time_tv);



        callHttp(MapBuilder.build().add("action", "getVideoDetail").add("video_id", id).get(), new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {
                mListView = (MyListView) findViewById(R.id.online_list);

                onLineVideoAdapter = new OnLineVideoAdapter(getBaseContext());
                mListView.setAdapter(onLineVideoAdapter);
                MapConf listconf = MapConf.with(getBaseContext()).pair("name->online_name_tv").pair("fileurl->player_view","","setToPlayerView").source(R.layout.adapter_bsdetail_shipin);
                MapConf.with(getBaseContext()).pair("name->online_name_tv").pair("fileurl->online_iv").pair("other->online_list",listconf)
                        .source(((NetEntity)result).getData().toString(),getWindow().getDecorView()).toView();
                ((ScrollView)findViewById(R.id.scrollview)).getChildAt(0).scrollTo(0,0);
                mPlayerView = (IjkPlayerView) findViewById(R.id.player_view);
            }

            @Override
            public void onFailure(Object result, List object) {

            }

            @Override
            public void onCallback(Object result, List object) {

            }
        });
//        MapConf.build().with(getBaseContext())
//                .pair("title->details_title_tv")
//                .pair("start_time->details_date_tv")
//                .pair("address->details_address_tv")
//                .pair("level:U%s->details_img")
//                .pair("surplus:还剩%s个名额->details_quota_tv")
//                .pair("status->")
//                .pair("sell_price:￥%s/年->details_amount_tv")
//                .pair("coach_name->details_name_tv")
//                .pair("cover_url->details_title_iv")
//                .pair("coach_header->details_icon_iv")
//                .pair("coach_team->details_school_name_tv")
//                .pair("schedules->details_time_tv")
//                .pair("recruit_students->details_student_tv")
//                .pair("content->details_introduction_tv")
//                .pair("sell_price:我要报名(￥%s/年)->details_sign_up_btn").pair("other_course->details_class_listview", MapConf.build().pair("cover_url->class_iv").pair("title->class_title_tv").pair("sell_price:￥%s/年->class_price_tv")).source(detail, getWindow().getDecorView()).toView();
//        MapConf.build().with(getBaseContext()).pair("other_course->details_class_listview", MapConf.build().with(getBaseContext()).pair("cover_url->class_iv").pair("title->class_title_tv").pair("sell_price:￥%s/年->class_price_tv").source(R.layout.adapter_class_item)).source(data, getWindow().getDecorView()).toView();

    }

    private void getData(final boolean refresh) {
        for (int i = 0; i < 5; i++) {
            info = new OnLineVideoInfo();
            info.setViedoName("在线视频 " + i);
            list.add(info);
        }
        onLineVideoAdapter.clear();
        onLineVideoAdapter.addAll(list);
    }
}
