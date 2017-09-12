package top.smartsport.www.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import app.base.JsonUtil;
import app.base.MapConf;
import app.base.action.ViewInflater;
import intf.MapBuilder;
import intf.QXFunCallback;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;

import top.smartsport.www.widget.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Aaron on 2017/7/25.
 * 课程表
 */
@ContentView(R.layout.fragment_scb)
public class KCBActivity extends BaseActivity {
    public static final String TAG = "QXV4Fragment";
    @ViewInject(R.id.qx_tab)
    private PagerSlidingTabStrip qx_tab;
    @ViewInject(R.id.qx_viewpager)
    private ViewPager qx_viewpager;
    private ViewPagerAdapter qxzxAdapter;//比赛,直播adapter
    private FragmentManager fragmentManager;

    private List<Fragment> listFM;

    int count;

    //[{"round":"1","list":[{"id":"1","match_id":"1","round":"1","home_team":"4","away_team":"21","home_score":"3","away_score":"2","start_time":"2017-07-14","end_time":"2017-07-14","status":"0","home_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","away_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","home_name":"我们是最棒的球队","away_name":"上海中国中学"},{"id":"9","match_id":"1","round":"1","home_team":"1","away_team":"14","home_score":null,"away_score":null,"start_time":"2017-07-06","end_time":"2017-08-31","status":"进行中","home_logo":"http://soccer.baibaobike.com/data/upload/default/logo.png","away_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","home_name":"人大附小队","away_name":"上海田林第三小学"},{"id":"12","match_id":"1","round":"1","home_team":"4","away_team":"14","home_score":null,"away_score":null,"start_time":"2017-08-16","end_time":"2017-08-18","status":"0","home_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","away_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","home_name":"我们是最棒的球队","away_name":"上海田林第三小学"}]},{"round":"3","list":[{"id":"8","match_id":"1","round":"3","home_team":"4","away_team":"1","home_score":null,"away_score":null,"start_time":"2017-07-12","end_time":"2017-08-27","status":"2","home_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","away_logo":"http://soccer.baibaobike.com/data/upload/default/logo.png","home_name":"我们是最棒的球队","away_name":"人大附小队"},{"id":"11","match_id":"1","round":"3","home_team":"1","away_team":"2","home_score":null,"away_score":null,"start_time":"2017-08-04","end_time":"2017-08-31","status":"进行中","home_logo":"http://soccer.baibaobike.com/data/upload/default/logo.png","away_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","home_name":"人大附小队","away_name":"中国少年球队"}]},{"round":"4","list":[{"id":"6","match_id":"1","round":"4","home_team":"14","away_team":"1","home_score":"2","away_score":"1","start_time":"2017-07-18","end_time":"2017-07-28","status":"0","home_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","away_logo":"http://soccer.baibaobike.com/data/upload/default/logo.png","home_name":"上海田林第三小学","away_name":"人大附小队"},{"id":"10","match_id":"1","round":"4","home_team":"3","away_team":"21","home_score":null,"away_score":null,"start_time":"2017-07-21","end_time":"2017-08-31","status":"进行中","home_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","away_logo":"http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png","home_name":"嘻哈快乐球队2","away_name":"上海中国中学"}]}]
    @Override
    protected void initView() {
        back();
        callHttp(MapBuilder.build().add("match_id", getIntent().getStringExtra("match_id")).add("action", "getSchedule").get(), new QXFunCallback() {
            @Override
            public void onSuccess(NetEntity result, List<Object> object) {
                List list = new ArrayList();
                if (!result.getData().toString().equals("null")) {

                    list = (List) JsonUtil.extractJsonRightValue(result.getData().toString());

                }
                count = list.size();

                List<String> titles = new ArrayList<String>();
                Map m = MapBuilder.build().add("1", "一").add("2", "二").add("3", "三").add("4", "四").add("5", "五").add("6", "六").add("7", "七").add("8", "九").add("9", "十").get();
                listFM = new ArrayList<>();
                Map<Integer, Object> views = new HashMap<Integer, Object>();
                for (int i = 0; i < count; i++) {
                    views.put(i, ((Map) list.get(i)).get("list"));
                    titles.add("第" + m.get(i + 1 + "").toString() + "场");
                }


                qxzxAdapter = new ViewPagerAdapter(views, titles);
                qx_viewpager.setAdapter(qxzxAdapter);


                qx_tab.setViewPager(qx_viewpager);


                if (count > 0) {
                    findViewById(R.id.empty).setVisibility(View.GONE);
                    qx_tab.setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.empty).setVisibility(View.VISIBLE);
                    qx_tab.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(String result, List<Object> object) {

            }

            @Override
            public void onCallback(NetEntity result, List<Object> object) {

            }
        });
    }

    private class ViewPagerAdapter extends PagerAdapter {

        private final List<String> titles;

        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        public Map<Integer, Object> viewMaps;
        Map<Integer, View> views = new TreeMap<Integer, View>();

        public ViewPagerAdapter(Map<Integer, Object> viewMpas, List<String> titles) {
            this.viewMaps = viewMpas;
            this.titles = titles;
        }

        // 获取要滑动的控件的数量，在这里我们以滑动的广告栏为例，那么这里就应该是展示的广告图片的ImageView数量
        @Override
        public int getCount() {
            return viewMaps.size();
        }

        // 来判断显示的是否是同一张图片，这里我们将两个参数相比较返回即可
        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        // PagerAdapter只缓存三张要显示的图片，如果滑动的图片超出了缓存的范围，就会调用这个方法，将图片销毁
        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView(views.get(position));
        }

        // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            if (view.getChildCount() - 1 == position) {
                return view.getChildAt(position);
            }
            View v = new ViewInflater(KCBActivity.this).inflate(R.layout.fragment_bssc, null);
            view.addView(v);

            MapConf mc = MapConf.with(view.getContext()).pair("home_name->home_name").pair("home_score->home_score").pair("home_logo->home_logo").pair("away_name->away_name").pair("away_score->away_score").pair("away_logo->away_logo").pair("start_time->start_time").source(R.layout.scb_item);
            MapConf.with(view.getContext()).conf(mc).source(viewMaps.get(position), v.findViewById(R.id.lv)).toView();
            views.put(position, v);
            return v;
        }
    }
}
