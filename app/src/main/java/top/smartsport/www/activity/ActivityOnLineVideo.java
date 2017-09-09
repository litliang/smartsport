package top.smartsport.www.activity;

import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;

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
        getData(true);
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
    }

    private void getData(final boolean refresh) {
        for (int i = 0; i < 5; i ++){
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
