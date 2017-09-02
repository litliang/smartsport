package top.smartsport.www.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.adapter.QXZXAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.fragment.MYQXKTFragment;
import top.smartsport.www.fragment.MYZXJAFragment;
import top.smartsport.www.fragment.QXFragment;
import top.smartsport.www.fragment.SCBSFragment;
import top.smartsport.www.fragment.SCJLFragment;
import top.smartsport.www.fragment.SCQXKTFragment;
import top.smartsport.www.fragment.SCZXFragment;
import top.smartsport.www.fragment.SCZXJAFragment;
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
    public static QXFragment newInstance() {
        QXFragment fragment = new QXFragment();
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
        listFM.add(SCQXKTFragment.newInstance());
        listFM.add(SCZXJAFragment.newInstance());
        listFM.add(SCBSFragment.newInstance());
        listFM.add(SCZXFragment.newInstance());
        listFM.add(SCJLFragment.newInstance());
        qxzxAdapter = new QXZXAdapter(this,fragmentManager,tabTitle,listFM);
        qx_viewpager.setAdapter(qxzxAdapter);
        qx_tab.setViewPager(qx_viewpager);

    }
}
