package top.smartsport.www.activity;

import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;

import app.base.MapConf;
import top.smartsport.www.R;
import top.smartsport.www.adapter.OnLineVideoAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.OnLineVideoInfo;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshBase;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;

/**
 * Created by bajieaichirou on 17/9/9.
 */

@ContentView(R.layout.activity_online_video)
public class ActivityOnLineVideo extends BaseActivity {

    private ImageView mIv;
    private TextView mNameTv, mGradeTv, mCountryTv,
            mSportTv, mCountTv;
    private PullToRefreshListView mListView;

    private OnLineVideoAdapter onLineVideoAdapter;
    private OnLineVideoInfo info;
    private List<OnLineVideoInfo> list = new ArrayList<>();

    @Override
    protected void initView() {
        setTitle(getResources().getString(R.string.online_video_title));
        initUI();
        fav();
        getData(true);
    }

    public void favImpl(final View view, boolean unfav) {
        fav.run(view, unfav+"", 2, getIntent().getStringExtra("id"));
    }

    private void initUI() {
        mIv = (ImageView) findViewById(R.id.online_iv);
        mNameTv = (TextView) findViewById(R.id.online_name_tv);
        mGradeTv = (TextView) findViewById(R.id.online_grade_tv);
        mCountryTv = (TextView) findViewById(R.id.online_country_tv);
        mSportTv = (TextView) findViewById(R.id.online_sport_tv);
        mCountTv = (TextView) findViewById(R.id.online_watched_time_tv);
        mListView = (PullToRefreshListView) findViewById(R.id.online_list);

        onLineVideoAdapter = new OnLineVideoAdapter(this);
        mListView.getRefreshableView().setAdapter(onLineVideoAdapter);
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                getData(true);
                String label = DateUtils.formatDateTime(ActivityOnLineVideo.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getHeaderLoadingLayout().setLastUpdatedLabel(label);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData(false);
                String label = DateUtils.formatDateTime(ActivityOnLineVideo.this, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getFooterLoadingLayout().setLastUpdatedLabel(label);
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
        mListView.onPullDownRefreshComplete();
        mListView.onPullUpRefreshComplete();
        onLineVideoAdapter.clear();
        onLineVideoAdapter.addAll(list);
    }
}
