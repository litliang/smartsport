package top.smartsport.www.activity;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import intf.MapBuilder;
import top.smartsport.www.O;
import top.smartsport.www.R;
import top.smartsport.www.adapter.BSSZAdapter;
import top.smartsport.www.adapter.BSZTAdapter;
import top.smartsport.www.adapter.SSJBAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseApplication;
import top.smartsport.www.bean.BSSZInfo;
import top.smartsport.www.bean.BSZTInfo;
import top.smartsport.www.bean.City;
import top.smartsport.www.bean.Province;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.SSJBInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.utils.SPUtils;
import top.smartsport.www.widget.ListViewForScrollView;
import top.smartsport.www.widget.MyGridView;

/**
 * Created by Aaron on 2017/7/25.
 * 比赛--筛选
 */
@ContentView(R.layout.activity_bschoice)
public class BSChoiceActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    @ViewInject(R.id.bs_choice_gv_type)
    private MyGridView bs_choice_gv_type;
    @ViewInject(R.id.bs_choice_gv_state)
    private MyGridView bs_choice_gv_state;
    @ViewInject(R.id.bs_choice_gv_rule)
    private MyGridView bs_choice_gv_rule;

    private SSJBAdapter ssjbAdapter;
    private List<SSJBInfo> ssjbInfoList;

    private BSSZAdapter bsszAdapter;
    private List<BSSZInfo> bsszInfoList;

    private BSZTAdapter bsztAdapter;
    private List<BSZTInfo> bsztInfoList;

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
    private int currentLevelIndex, currentStatusIndex, currentSaizhiIndex;


    @ViewInject(R.id.action_bar)
    private View actionbar;
    @Override
    public View getTopBar() {
        return actionbar;
    }



    @Override
    protected void initView() {
        back();
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
                checkState(i);

            }
        });

        bs_choice_gv_rule.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkType(i);
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

        getSSJB();
        getBSSZ();
        getBSZT();
        initListView();
        findViewById(R.id.chongzhi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLevel(0);
                checkType(0);
                checkState(0);
                SPUtils.put(getApplicationContext(), "getCounties-city", "");
                SPUtils.put(getApplicationContext(), "getCounties-county", "");
            }
        });
        findViewById(R.id.queding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.put(getApplicationContext(), "ss_currentLevelIndex", currentLevelIndex);
                SPUtils.put(getApplicationContext(), "ss_currentStatusIndex", currentStatusIndex);
                SPUtils.put(getApplicationContext(), "ss_currentSaizhiIndex", currentSaizhiIndex);
                setResult(RESULT_OK);
                finish();
            }
        });
        currentLevelIndex = (Integer) SPUtils.get(BaseApplication.getApplication(), "ss_currentLevelIndex", 0);
        currentStatusIndex = (Integer) SPUtils.get(BaseApplication.getApplication(), "ss_currentStatusIndex", 0);
        currentSaizhiIndex = (Integer) SPUtils.get(BaseApplication.getApplication(), "ss_currentSaizhiIndex", 0);
        if(currentLevelIndex >= ssjbAdapter.getCount()) {
            currentLevelIndex = 0;
        }
        if(currentStatusIndex >= bsztAdapter.getCount()) {
            currentStatusIndex = 0;
        }
        if(currentSaizhiIndex >= bsszAdapter.getCount()) {
            currentSaizhiIndex = 0;
        }
        checkLevel(currentLevelIndex);
        checkState(currentStatusIndex);
        checkType(currentSaizhiIndex);
        SPUtils.put(getApplicationContext(), "getCounties-city", "");
        SPUtils.put(getApplicationContext(), "getCounties-county", "");
    }

    private void checkLevel(int i) {
        currentLevelIndex = i;
        ssjbAdapter.setSeclection(i);
        ssjbAdapter.notifyDataSetChanged();
        String level = ssjbAdapter.getItem(i).getName().replace("U", "");
        if (level.startsWith("全部")) {
            level = "0";
        }
        SPUtils.put(getApplicationContext(), "level", level);
    }

    private void checkState(int i) {
        currentStatusIndex = i;
        bsztAdapter.setSeclection(i);
        bsztAdapter.notifyDataSetChanged();
        Map m = MapBuilder.build().add("全部比赛", 0 + "").add("报名中", 1 + "").add("进行中", 2 + "").add("已结束", 3 + "").get();
        String state = m.get(bsztAdapter.getItem(i).getName()).toString();

        if (state.equals("全部")) {
            state = "0";
        }
        SPUtils.put(getApplicationContext(), "status", state);
    }

    private void checkType(int i) {
        currentSaizhiIndex = i;
        bsszAdapter.setSeclection(i);
        bsszAdapter.notifyDataSetChanged();
        String type = bsszAdapter.getItem(i).getName().replace("人制", "");
        if (type.startsWith("全部")) {
            type = "0";
        }
        SPUtils.put(getApplicationContext(), "type", type);
    }

    /**
     * 赛事级别
     */
    private void getSSJB() {
        ssjbInfoList = new ArrayList<>();
        ssjbInfoList = O.getSSJB();
        ssjbAdapter = new SSJBAdapter(this);
        ssjbAdapter.addAll(ssjbInfoList);
        bs_choice_gv_type.setAdapter(ssjbAdapter);

    }

    /**
     * 比赛状态
     */
    private void getBSZT() {
        bsztInfoList = new ArrayList<>();
        bsztInfoList = O.getBSZT();
        bsztAdapter = new BSZTAdapter(this);
        bsztAdapter.addAll(bsztInfoList);
        bs_choice_gv_state.setAdapter(bsztAdapter);
    }

    /**
     * 比赛赛制
     */
    private void getBSSZ() {
        bsszInfoList = new ArrayList<>();
        bsszInfoList = O.getBSSZ();
        bsszAdapter = new BSSZAdapter(this);
        bsszAdapter.addAll(bsszInfoList);
        bs_choice_gv_rule.setAdapter(bsszAdapter);
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
                SPUtils.put(getApplicationContext(), "getCounties-city", p.getArea_id());

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
                SPUtils.put(getApplicationContext(), "getCounties-county", c.getArea_id());

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            LogUtil.d("---------------onKeyDown-------------------");
            setResult(RESULT_OK, new Intent());
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
