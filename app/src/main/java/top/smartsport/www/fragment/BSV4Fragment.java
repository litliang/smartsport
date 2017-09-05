package top.smartsport.www.fragment;

import android.content.Intent;
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
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.widget.PagerSlidingTabStrip;

/**
 * Created by Aaron on 2017/7/13.
 * 比赛
 */
@ContentView(R.layout.fragment_ss)
public class BSV4Fragment extends BaseV4Fragment {
    public static final String TAG = "SSFragment";

    @ViewInject(R.id.ss_tab)
    private PagerSlidingTabStrip ss_tab;
    @ViewInject(R.id.ss_viewpager)
    private ViewPager ss_viewpager;
    private String[] tabTitle = {"比赛","直播"};
    private SSZBAdapter bszbAdapter;//比赛,直播adapter
    private FragmentManager fragmentManager;

    private List<Fragment> listFM;

    public static BSV4Fragment newInstance() {
        BSV4Fragment fragment = new BSV4Fragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected void initView() {
        fragmentManager = getFragmentManager();
        addFragment();
    }

    @Override
    public void refresh() {
        super.refresh();
        ((BaseV4Fragment)listFM.get(ss_viewpager.getCurrentItem())).refresh();
    }

    private void addFragment(){
        listFM = new ArrayList<>();
        listFM.add(BSSSV4Fragment.newInstance());
        listFM.add(BSZBV4Fragment.newInstance());
        bszbAdapter = new SSZBAdapter(getActivity(),fragmentManager,tabTitle,listFM);
        ss_viewpager.setAdapter(bszbAdapter);
        ss_tab.setViewPager(ss_viewpager);
    }

    @Event(value = {R.id.bs_ll_choice})
    private void getEvent(View view){
        switch (view.getId()){
            case R.id.bs_ll_choice:
                startActivityForResult(new Intent(getContext(),BSChoiceActivity.class),0);
                break;
        }
    }


}
