package top.smartsport.www.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import android.widget.TextView;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.activity.ChoiceCityActivity;
import top.smartsport.www.adapter.ZXALLAdapter;
import top.smartsport.www.base.BaseApplication;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.utils.SPUtils;
import top.smartsport.www.widget.PagerSlidingTabStrip;

/**
 * Created by Aaron on 2017/7/13.
 * 资讯
 */
@ContentView(R.layout.fragment_zx)
public class ZXV4Fragment extends BaseV4Fragment {
    public static final String TAG = "ZXV4Fragment";
    @ViewInject(R.id.zx_tab)
    private PagerSlidingTabStrip zx_tab;
    @ViewInject(R.id.zx_viewpager)
    private ViewPager zx_viewpager;
    private String[] tabTitle = {"青训资讯","活动资讯","赛事新闻"};
    private ZXALLAdapter zxallAdapter;//比赛,直播adapter
    private FragmentManager fragmentManager;
    private List<Fragment> listFM;
    private String cityId;

    public static ZXV4Fragment newInstance() {
        ZXV4Fragment fragment = new ZXV4Fragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initView() {
        fragmentManager = getFragmentManager();
        addFragment();
    }

    @Event(value = {R.id.zx_ll_location})
    private void getEvent(View v){
        switch (v.getId()){
            case R.id.zx_ll_location:
//                toActivity(ChoiceCityActivity.class);
                startActivityForResult(new Intent(getActivity(), ChoiceCityActivity.class), 101);
                break;
        }
    }

    private void addFragment(){
        listFM = new ArrayList<>();
        listFM.add(ZXQXV4Fragment.newInstance().setViewpager(zx_viewpager));
        listFM.add(new ZXHDV4Fragment());
        listFM.add(new ZXSSV4Fragment());
        zxallAdapter = new ZXALLAdapter(getActivity(),fragmentManager,tabTitle,listFM);
        zx_viewpager.setAdapter(zxallAdapter);
        zx_tab.setViewPager(zx_viewpager);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(root==null){
            return;
        }
        String city = (String) SPUtils.get(BaseApplication.getApplication(),"city","");
        if(city!=null&&!city.trim().equals("")){
            ((TextView)root.findViewById(R.id.zx_location)).setText(city);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (listFM != null) {
            for (Fragment fragment : listFM) {
                if(fragment==null)
                    continue;
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

}
