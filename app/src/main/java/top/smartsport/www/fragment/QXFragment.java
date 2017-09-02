package top.smartsport.www.fragment;

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
import top.smartsport.www.activity.QXChoiceActivity;
import top.smartsport.www.adapter.QXZXAdapter;
import top.smartsport.www.base.BaseFragment;
import top.smartsport.www.widget.PagerSlidingTabStrip;

/**
 * Created by Aaron on 2017/7/13.
 * 青训
 */
@ContentView(R.layout.fragment_qx)
public class QXFragment extends BaseFragment {
    public static final String TAG = "QXFragment";
    @ViewInject(R.id.qx_tab)
    private PagerSlidingTabStrip qx_tab;
    @ViewInject(R.id.qx_viewpager)
    private ViewPager qx_viewpager;
    private String[] tabTitle = {"青训课程","在线教案"};
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
        fragmentManager = getFragmentManager();
        addFragment();
    }

    private void addFragment(){
        listFM = new ArrayList<>();
        listFM.add(QXKTFragment.newInstance());
        listFM.add(ZXJAFragment.newInstance());
        qxzxAdapter = new QXZXAdapter(getActivity(),fragmentManager,tabTitle,listFM);
        qx_viewpager.setAdapter(qxzxAdapter);
        qx_tab.setViewPager(qx_viewpager);
        root.findViewById(R.id.bs_ll_choice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toActivity(QXChoiceActivity.class);
            }
        });
    }
}
