package top.smartsport.www.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.adapter.QXZXAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.fragment.QXV4Fragment;
import top.smartsport.www.fragment.SCBSV4Fragment;
import top.smartsport.www.fragment.SCJLV4Fragment;
import top.smartsport.www.fragment.SCQXKTV4Fragment;
import top.smartsport.www.fragment.SCZXV4Fragment;
import top.smartsport.www.fragment.SCZXJAV4Fragment;
import top.smartsport.www.widget.PagerSlidingTabStrip;

/**
 * Created by Aaron on 2017/7/18.
 * 我的收藏
 */
@ContentView(R.layout.activity_mysc)
public class MySCActivity extends BaseActivity {

    @ViewInject(R.id.qx_tab)
    private PagerSlidingTabStrip qx_tab;
    @ViewInject(R.id.qx_viewpager)
    private ViewPager qx_viewpager;
    private String[] tabTitle = {"青训课程","在线教案","比赛","资讯","教练"};
    private QXZXAdapter qxzxAdapter;//比赛,直播adapter
    private FragmentManager fragmentManager;

    private List<Fragment> listFM;
    public static QXV4Fragment newInstance() {
        QXV4Fragment fragment = new QXV4Fragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;

    }
    @Override
    protected void initView() {
        fragmentManager = getSupportFragmentManager();
        addFragment();
        back();
    }

    private void addFragment(){
        listFM = new ArrayList<>();
        listFM.add(SCQXKTV4Fragment.newInstance());
        listFM.add(SCZXJAV4Fragment.newInstance());
        listFM.add(SCBSV4Fragment.newInstance());
        listFM.add(SCZXV4Fragment.newInstance());
        listFM.add(SCJLV4Fragment.newInstance());
        qxzxAdapter = new QXZXAdapter(this,fragmentManager,tabTitle,listFM);
        qx_viewpager.setAdapter(qxzxAdapter);
        qx_tab.setViewPager(qx_viewpager);


    }
}
