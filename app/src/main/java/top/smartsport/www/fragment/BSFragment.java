package top.smartsport.www.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.activity.BSChoiceActivity;
import top.smartsport.www.adapter.SSZBAdapter;
import top.smartsport.www.base.BaseFragment;
import top.smartsport.www.widget.PagerSlidingTabStrip;

/**
 * Created by Aaron on 2017/7/13.
 * 比赛
 */
@ContentView(R.layout.fragment_ss)
public class BSFragment extends BaseFragment {
    public static final String TAG = "SSFragment";

    @ViewInject(R.id.ss_tab)
    private PagerSlidingTabStrip ss_tab;
    @ViewInject(R.id.ss_viewpager)
    private ViewPager ss_viewpager;
    private String[] tabTitle = {"比赛","直播"};
    private SSZBAdapter bszbAdapter;//比赛,直播adapter
    private FragmentManager fragmentManager;

    private List<Fragment> listFM;

    public static BSFragment newInstance() {
        BSFragment fragment = new BSFragment();
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
        listFM.add(BSSSFragment.newInstance());
        listFM.add(BSZBFragment.newInstance());
        bszbAdapter = new SSZBAdapter(getActivity(),fragmentManager,tabTitle,listFM);
        ss_viewpager.setAdapter(bszbAdapter);
        ss_tab.setViewPager(ss_viewpager);
    }

    @Event(value = {R.id.bs_ll_choice})
    private void getEvent(View view){
        switch (view.getId()){
            case R.id.bs_ll_choice:
                toActivity(BSChoiceActivity.class);
                break;
        }
    }


}
