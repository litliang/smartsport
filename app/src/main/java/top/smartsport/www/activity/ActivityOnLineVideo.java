package top.smartsport.www.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.dl7.player.media.IjkPlayerView;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.base.MapConf;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.adapter.OnLineVideoAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseComptActivity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.OnLineVideoInfo;
import top.smartsport.www.widget.MyListView;

/**
 * Created by bajieaichirou on 17/9/9.
 */

@ContentView(R.layout.activity_online_video)
public class ActivityOnLineVideo extends BaseComptActivity {

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
        back();
        fav();
        initUI();
//        getData(true);
    }

    String id;

    public void favImpl(final View view, boolean unfav) {
        fav.run(view, unfav + "", 2, getIntent().getStringExtra("id"));
    }


    private void initUI() {

        id = getIntent().getStringExtra("id");
        mNameTv = (TextView) findViewById(R.id.online_name_tv);
        mGradeTv = (TextView) findViewById(R.id.online_grade_tv);
        mCountryTv = (TextView) findViewById(R.id.online_country_tv);
        mSportTv = (TextView) findViewById(R.id.online_sport_tv);
        mCountTv = (TextView) findViewById(R.id.online_watched_time_tv);
        mListView = (MyListView) findViewById(R.id.online_list);

        onLineVideoAdapter = new OnLineVideoAdapter(getBaseContext());
        mListView.setAdapter(onLineVideoAdapter);


        callHttp(MapBuilder.build().add("action", "getVideoDetail").add("video_id", id).get(), new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {

                MapConf listconf = MapConf.with(getBaseContext()).pair("name->news_name").pair("fileurl->news_img").pair("ctime->news_date").source(R.layout.adapter_bsdetail_shipin);
                MapConf.with(getBaseContext()).pair("detail-name->online_name_tv").pair("detail-fileurl->player_view", "", "setToPlayerView()").pair("other->online_list", listconf)
                        .source(((NetEntity) result).getData().toString(), getWindow().getDecorView()).toView();
                ((ScrollView) findViewById(R.id.scrollview)).getChildAt(0).scrollTo(0, 0);

            }

            @Override
            public void onFailure(Object result, List object) {

            }

            @Override
            public void onCallback(Object result, List object) {

            }
        });

        ((MyListView) findViewById(R.id.online_list)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getBaseContext(), ActivityOnLineVideo.class).putExtra("id", ((Map)adapterView.getItemAtPosition(i)).get("id").toString()));
            }
        });
    }

}
