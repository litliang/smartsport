package top.smartsport.www.fragment;

import android.content.Intent;
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
import top.smartsport.www.activity.ZXChoiceActivity;
import top.smartsport.www.adapter.QXZXAdapter;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.widget.PagerSlidingTabStrip;

/**
 * Created by Aaron on 2017/7/13.
 * 青训
 */
@ContentView(R.layout.fragment_qx)
public class QXV4Fragment extends BaseV4Fragment {
    public static final String TAG = "QXV4Fragment";
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
        fragmentManager = getFragmentManager();
        addFragment();
    }

    private void addFragment(){
        listFM = new ArrayList<>();
        listFM.add(QXKTV4Fragment.newInstance());
        listFM.add(ZXJAV4Fragment.newInstance());
        qxzxAdapter = new QXZXAdapter(getActivity(),fragmentManager,tabTitle,listFM);
        qx_viewpager.setAdapter(qxzxAdapter);
        qx_tab.setViewPager(qx_viewpager);
        root.findViewById(R.id.bs_ll_choice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int item = qx_viewpager.getCurrentItem();
                if(item == 0) { // 青训课程
                    startActivityForResult(new Intent(getActivity(),QXChoiceActivity.class),0);
                } else if(item == 1) { // 在线教案
                    startActivityForResult(new Intent(getActivity(),ZXChoiceActivity.class),0);
                }
//                startActivityForResult(new Intent(getContext(),ZXChoiceActivity.class), Activity.RESULT_FIRST_USER);
            }
        });
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
