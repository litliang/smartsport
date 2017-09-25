package top.smartsport.www.activity;


import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import intf.MapBuilder;
import top.smartsport.www.O;
import top.smartsport.www.R;
import top.smartsport.www.adapter.KCZTAdapter;
import top.smartsport.www.adapter.QXJBAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseApplication;
import top.smartsport.www.bean.City;
import top.smartsport.www.bean.KCZTInfo;
import top.smartsport.www.bean.Province;
import top.smartsport.www.bean.QXJBInfo;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.utils.SPUtils;
import top.smartsport.www.widget.ListViewForScrollView;
import top.smartsport.www.widget.MyGridView;

/**
 * Created by Aaron on 2017/7/25.
 * 青训--筛选
 */
@ContentView(R.layout.activity_qxchoice)
public class QXChoiceActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @ViewInject(R.id.bs_choice_gv_type)
    private MyGridView bs_choice_gv_type;
    @ViewInject(R.id.bs_choice_gv_state)
    private MyGridView bs_choice_gv_state;

    private QXJBAdapter qxjbAdapter;
    private List<QXJBInfo> qxjbInfoList;

    private KCZTAdapter kcztAdapter;
    private List<KCZTInfo> kcztInfoList;

    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;
    /**
     * listview竖向滑动的阈值
     */
    private static final int THRESHOLD_Y_LIST_VIEW = 20;
    @ViewInject(R.id.mScrollView)
    private ListViewForScrollView mScrollView;
    private boolean isSvToBottom = false;
    private float mLastX;
    private float mLastY;
    @ViewInject(R.id.lv1)
    private ListView lv1;
    private ArrayAdapter<Province> adapter1;
    @ViewInject(R.id.lv2)
    private ListView lv2;
    private ArrayAdapter<City> adapter2;

    private Province province;
    private int indexOfProvince = 0;
    private int indexOfCity = 0;
    private boolean hasAll = true;
    private Integer currentLevelIndex, currentStatusIndex;

    @Override
    protected void initView() {
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();

        bs_choice_gv_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkLevel(i);
            }
        });
        bs_choice_gv_state.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkStatus(i);
            }
        });

        mScrollView.setScrollToBottomListener(new ListViewForScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollToBottom() {
                isSvToBottom = true;
            }

            @Override
            public void onNotScrollToBottom() {
                isSvToBottom = false;
            }
        });

        // ListView滑动冲突解决
        lv1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    mLastY = event.getY();
                }
                if (action == MotionEvent.ACTION_MOVE) {
                    int top = lv1.getChildAt(0).getTop();
                    float nowY = event.getY();
                    if (!isSvToBottom) {
                        // 允许scrollview拦截点击事件, scrollView滑动
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                    } else if (top == 0 && nowY - mLastY > THRESHOLD_Y_LIST_VIEW) {
                        // 允许scrollview拦截点击事件, scrollView滑动
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                    } else {
                        // 不允许scrollview拦截点击事件， listView滑动
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                    }
                }
                return false;
            }
        });
        lv2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                if (action == MotionEvent.ACTION_DOWN) {
                    mLastY = event.getY();
                }
                if (action == MotionEvent.ACTION_MOVE) {
                    int top = lv2.getChildAt(0).getTop();
                    float nowY = event.getY();
                    if (!isSvToBottom) {
                        // 允许scrollview拦截点击事件, scrollView滑动
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                    } else if (top == 0 && nowY - mLastY > THRESHOLD_Y_LIST_VIEW) {
                        // 允许scrollview拦截点击事件, scrollView滑动
                        mScrollView.requestDisallowInterceptTouchEvent(false);
                    } else {
                        // 不允许scrollview拦截点击事件， listView滑动
                        mScrollView.requestDisallowInterceptTouchEvent(true);
                    }
                }
                return false;
            }
        });

        getQXJB();
        getKCZT();
        initListView();
        findViewById(R.id.chongzhi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLevel(0);
                checkStatus(0);
                SPUtils.put(getApplicationContext(), "qx-getCounties-city", null);
                SPUtils.put(getApplicationContext(), "qx-getCounties-county", null);
            }
        });
        findViewById(R.id.queding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.put(getApplicationContext(), "qx_currentLevelIndex", currentLevelIndex + "");
                SPUtils.put(getApplicationContext(), "qx_currentStatusIndex", currentStatusIndex + "");
                setResult(RESULT_OK);
                finish();
            }
        });
        String level = (String) SPUtils.get(BaseApplication.getApplication(), "qx_currentLevelIndex", null);
        ;
        String status = (String) SPUtils.get(BaseApplication.getApplication(), "qx_currentStatusIndex", null);
        if (level != null&&!level.equals("null")) {
            currentLevelIndex = Integer.parseInt(level);
        }else{
            currentLevelIndex = 0;
        }
        if (status != null&&!status.equals("null")) {
            currentStatusIndex = Integer.parseInt(status);
        }else {
            currentStatusIndex = 0;
        }
        if (currentLevelIndex >= qxjbAdapter.getCount()) {
            currentLevelIndex = 0;
        }
        if (currentStatusIndex >= kcztAdapter.getCount()) {
            currentStatusIndex = 0;
        }

        checkLevel(currentLevelIndex);
        checkStatus(currentStatusIndex);
        SPUtils.put(getApplicationContext(), "qx-getCounties-city", null);
        SPUtils.put(getApplicationContext(), "qx-getCounties-county", null);
    }

    private void checkLevel(int i) {
        if(i==0){
            currentLevelIndex = null;
        }else
        currentLevelIndex = i;
        qxjbAdapter.setSeclection(i);
        qxjbAdapter.notifyDataSetChanged();
        String level = qxjbAdapter.getItem(i).getName().replace("U", "");
        if (level.startsWith("全部")) {
            level = null;
        }
        SPUtils.put(getApplicationContext(), "qx-level", level);
    }

    private void checkStatus(int i) {
        if(i==0){
            currentStatusIndex = null;
        }else
        currentStatusIndex = i;
        kcztAdapter.setSeclection(i);
        kcztAdapter.notifyDataSetChanged();
        Map m = MapBuilder.build().add("全部课程", null)
                .add("报名中", 1 + "")
                .add("已报满", 2 + "").get();
        String state = (String) m.get(kcztAdapter.getItem(i).getName());

        if (state!=null&&state.equals("全部")) {
            state = null;
        }
        SPUtils.put(getApplicationContext(), "qx-status", state);
    }

    /**
     * 青训级别
     */
    private void getQXJB() {
        qxjbInfoList = new ArrayList<>();
        qxjbInfoList = O.getQXJB();
        qxjbAdapter = new QXJBAdapter(this);
        qxjbAdapter.addAll(qxjbInfoList);
        bs_choice_gv_type.setAdapter(qxjbAdapter);
    }

    /**
     * 课程状态
     */
    private void getKCZT() {
        kcztInfoList = new ArrayList<>();
        kcztInfoList = O.getKCZT();
        kcztAdapter = new KCZTAdapter(this);
        kcztAdapter.addAll(kcztInfoList);
        bs_choice_gv_state.setAdapter(kcztAdapter);
    }

    private void initListView() {
        List<Province> areas = new ArrayList<>();
        areas.addAll(O.getAreas());

        adapter1 = new ArrayAdapter<>(this, R.layout.adapter_province);
        adapter1.addAll(areas);
        lv1.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        lv1.setAdapter(adapter1);
        lv1.setOnItemClickListener(this);
//        lv1.setItemChecked(indexOfProvince, true);
//        lv1.setSelection(indexOfProvince);
        Province p = province == null ? adapter1.getItem(0) : province;

        adapter2 = new ArrayAdapter<>(this, R.layout.adapter_city);
        adapter2.addAll(p.getCity());
        lv2.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
        lv2.setAdapter(adapter2);
        lv2.setOnItemClickListener(this);
//        lv2.setItemChecked(indexOfCity, true);
//        lv2.setSelection(indexOfCity);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.lv1:
                Province p = adapter1.getItem(position);
                SPUtils.put(getApplicationContext(), "qx-getCounties-city", p.getArea_id() + "");

                if (handler != null) {
                    Message msg = new Message();
                    msg.what = 10110;
                    msg.obj = p.getTitle();
                    handler.sendMessage(msg);
                }
                adapter2.clear();
                adapter2.addAll(p.getCity());

                lv2.setSelection(lv2.getTop());
                Log.i("___v1", "v1");
                break;
            case R.id.lv2:
                City c = adapter2.getItem(position);
                SPUtils.put(getApplicationContext(), "qx-getCounties-county", c.getArea_id() + "");

                if (handler != null) {
                    Message msg = new Message();
                    msg.what = 10111;
//                    if(hasAll&&selectedD.getArea_id().equals(c.getArea_id())){
//                        msg.obj = selectedD.getTitle();
//                    }
                    msg.obj = c.getTitle();

                    handler.sendMessage(msg);
                    Log.i("___v2", "v2");
                }
                break;
        }
    }

    String areaProvince;
    String areaCity;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 10110:
                    areaProvince = msg.obj.toString();
                    Log.i("___areaProvince", "v" + areaProvince);
                    break;
                case 10111:
                    areaCity = msg.obj.toString();
                    Log.i("___areaCity", "v" + areaCity);
                    break;
            }
        }
    };
}
