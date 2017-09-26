package top.smartsport.www.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.O;
import top.smartsport.www.R;
import top.smartsport.www.adapter.KCJBAdapter;
import top.smartsport.www.adapter.KCLBAdapter;
import top.smartsport.www.adapter.KCLYAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseApplication;
import top.smartsport.www.bean.KCJBInfo;
import top.smartsport.www.bean.KCLBInfo;
import top.smartsport.www.bean.KCLYInfo;
import top.smartsport.www.bean.Province;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.utils.SPUtils;
import top.smartsport.www.widget.ListViewForScrollView;
import top.smartsport.www.widget.MyGridView;

/**
 *  课程  筛选页面
 */
@ContentView(R.layout.activity_zxchoice)
public class ZXChoiceActivity extends BaseActivity {
    @ViewInject(R.id.zx_choice_kc_jb)
    private MyGridView zx_choice_kc_jb;
    @ViewInject(R.id.zx_choice_kc_ly)
    private MyGridView zx_choice_kc_ly;
    @ViewInject(R.id.zx_choice_kc_lb)
    private MyGridView zx_choice_kc_lb;

    private KCJBAdapter kcjbAdapter;
    private List<KCJBInfo> kcjbInfoList;

    private KCLYAdapter kclyAdapter;
    private List<KCLYInfo> kclyInfoList;

    private KCLBAdapter kclbAdapter;
    private List<KCLBInfo> kclbInfoList;

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

    private Province province;
    private int indexOfProvince = 0;
    private int indexOfCity = 0;
    private boolean hasAll = true;
    private int currentLevelIndex, currentLaiYuanIndex, currentLeiBieIndex;
    private String levelId, lyId, lbId;

    @Override
    protected void initView() {
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();

        zx_choice_kc_jb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkJB(i);
            }
        });
        zx_choice_kc_ly.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkLY(i);

            }
        });

        zx_choice_kc_lb.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                checkLB(i);
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

        getKCJB();
        getKCLY();
        getKCLB();
        findViewById(R.id.chongzhi).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkJB(0);
                checkLY(0);
                checkLB(0);
            }
        });
        findViewById(R.id.queding).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SPUtils.put(getApplicationContext(), "kc_currentLevelId", levelId);
                SPUtils.put(getApplicationContext(), "kc_currentLaiYuanId", lyId);
                SPUtils.put(getApplicationContext(), "kc_currentLeiBieId", lbId);

                SPUtils.put(getApplicationContext(), "kc_currentLevelIndex", currentLevelIndex);
                SPUtils.put(getApplicationContext(), "kc_currentLaiYuanIndex", currentLaiYuanIndex);
                SPUtils.put(getApplicationContext(), "kc_currentLeiBieIndex", currentLeiBieIndex);
                setResult(RESULT_OK, new Intent());
                finish();
            }
        });
        currentLevelIndex = (Integer) SPUtils.get(BaseApplication.getApplication(), "kc_currentLevelIndex", 0);
        currentLaiYuanIndex = (Integer) SPUtils.get(BaseApplication.getApplication(), "kc_currentLaiYuanIndex", 0);
        currentLeiBieIndex = (Integer) SPUtils.get(BaseApplication.getApplication(), "kc_currentLeiBieIndex", 0);
        if(currentLevelIndex >= kcjbAdapter.getCount()) {
            currentLevelIndex = 0;
        }
        if(currentLaiYuanIndex >= kclyAdapter.getCount()) {
            currentLaiYuanIndex = 0;
        }
        if(currentLeiBieIndex >= kclbAdapter.getCount()) {
            currentLeiBieIndex = 0;
        }
        checkJB(currentLevelIndex);
        checkLY(currentLaiYuanIndex);
        checkLB(currentLeiBieIndex);
    }

    private void checkJB(int i) {
        currentLevelIndex = i;
        kcjbAdapter.setSeclection(i);
        kcjbAdapter.notifyDataSetChanged();
        String level = kcjbAdapter.getItem(i).getName();
        LogUtil.d("---------等级------" + level);
        levelId = kcjbAdapter.getItem(i).getId();
        SPUtils.put(getApplicationContext(), "kc_jb", level);
    }

    private void checkLY(int i) {
        currentLaiYuanIndex = i;
        kclyAdapter.setSeclection(i);
        kclyAdapter.notifyDataSetChanged();
        String state = kclyAdapter.getItem(i).getName();
        LogUtil.d("---------来源------" + state);
        lyId = kclyAdapter.getItem(i).getId();
        SPUtils.put(getApplicationContext(), "kc_ly", state);
    }

    private void checkLB(int i) {
        currentLeiBieIndex = i;
        kclbAdapter.setSeclection(i);
        kclbAdapter.notifyDataSetChanged();
        String type = kclbAdapter.getItem(i).getName();
        LogUtil.d("---------类别------" + type);
        lbId = kclbAdapter.getItem(i).getId();
        SPUtils.put(getApplicationContext(), "kc_lb", type);
    }

    /**
     * 课程级别
     */
    private void getKCJB() {
        kcjbInfoList = new ArrayList<>();
        kcjbInfoList = O.getKCJB();
        kcjbAdapter = new KCJBAdapter(this);
        kcjbAdapter.addAll(kcjbInfoList);
        zx_choice_kc_jb.setAdapter(kcjbAdapter);
    }

    /**
     * 课程来源
     */
    private void getKCLY() {
        kclyInfoList = new ArrayList<>();
        kclyInfoList = O.getKCLY();
        kclyAdapter = new KCLYAdapter(this);
        kclyAdapter.addAll(kclyInfoList);
        zx_choice_kc_ly.setAdapter(kclyAdapter);
    }

    /**
     * 课程类别
     */
    private void getKCLB() {
        kclbInfoList = new ArrayList<>();
        kclbInfoList = O.getKCLB();
        kclbAdapter = new KCLBAdapter(this);
        kclbAdapter.addAll(kclbInfoList);
        zx_choice_kc_lb.setAdapter(kclbAdapter);
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
            setResult(RESULT_OK, new Intent());
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
