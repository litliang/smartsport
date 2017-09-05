package top.smartsport.www.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.adapter.QXZXAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.fragment.MYQXKTV4Fragment;
import top.smartsport.www.fragment.MYZXJAV4Fragment;
import top.smartsport.www.fragment.QXV4Fragment;
import top.smartsport.www.widget.PagerSlidingTabStrip;

/**
 * Created by Aaron on 2017/7/18.
 * 我的课程
 */
@ContentView(R.layout.activity_mykc)
public class MyKCActivity extends BaseActivity {
    @ViewInject(R.id.qx_tab)
    private PagerSlidingTabStrip qx_tab;
    @ViewInject(R.id.qx_viewpager)
    private ViewPager qx_viewpager;
    private String[] tabTitle = {"青训课程","在线教案"};
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
        listFM.add(MYQXKTV4Fragment.newInstance());
        listFM.add(MYZXJAV4Fragment.newInstance());
        qxzxAdapter = new QXZXAdapter(this,fragmentManager,tabTitle,listFM);
        qx_viewpager.setAdapter(qxzxAdapter);
        qx_tab.setViewPager(qx_viewpager);

    }
}
