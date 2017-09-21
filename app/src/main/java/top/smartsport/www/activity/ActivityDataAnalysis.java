package top.smartsport.www.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.adapter.AnalysisTabAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.fragment.AssistFragment;
import top.smartsport.www.fragment.ScoreboardFragment;
import top.smartsport.www.fragment.ScorerFragment;

/**
 * Created by bajieaichirou on 17/9/7.
 * 数据分析
 */
@ContentView(R.layout.activity_data_analysis)
public class ActivityDataAnalysis extends BaseActivity{

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private ScoreboardFragment scoreboardFragment;
    private ScorerFragment scorerFragment;
    private AssistFragment assistFragment;
    private AnalysisTabAdapter mTabAdapter;

    private List<Fragment> list_fragment;
    private String[] title;
    public static final Integer TAB_COUNT = 3;


    @Override
    protected void initView() {
        initUI();
        setTitle(getString(R.string.data_analysis));
    }

    private void initUI() {
        mTabLayout = (TabLayout) findViewById(R.id.main_tab);
        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        scoreboardFragment = new ScoreboardFragment();
        list_fragment = new ArrayList<>();
        list_fragment.add(scoreboardFragment);
        scorerFragment = new ScorerFragment();
        list_fragment.add(scorerFragment);
        assistFragment = new AssistFragment();
        list_fragment.add(assistFragment);

        title = getResources().getStringArray(R.array.analysis_tab_value);
        for(int i = 0; i < TAB_COUNT; i ++){
            mTabLayout.addTab(mTabLayout.newTab().setText(title[i]));
        }
        mTabAdapter = new AnalysisTabAdapter(this.getSupportFragmentManager(), this, list_fragment, title, true);
        mViewPager.setAdapter(mTabAdapter);
        mViewPager.setOffscreenPageLimit(TAB_COUNT);
        mTabLayout.setupWithViewPager(mViewPager);

        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(mTabAdapter.getTabView(i));
                if (i == 0){
                    tab.getCustomView().setSelected(true);
                    tab.getCustomView().findViewById(R.id.tab_line).setVisibility(View.VISIBLE);
                }else{
                    tab.getCustomView().setSelected(false);
                    tab.getCustomView().findViewById(R.id.tab_line).setVisibility(View.INVISIBLE);
                }
            }
        }

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchImg(true, tab, tab.getPosition());
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switchImg(false, tab, tab.getPosition());
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void switchImg(boolean selectedFlag, TabLayout.Tab tab, int position){
        switch (position){
            case 0:
                setLineView(selectedFlag, tab);
                break;
            case 1:
                setLineView(selectedFlag, tab);
                break;
            case 2:
                setLineView(selectedFlag, tab);
                break;
            default:
                break;
        }
    }

    private void setLineView(boolean flag, TabLayout.Tab tab){
        if (flag){
            tab.getCustomView().findViewById(R.id.tab_line).setVisibility(View.VISIBLE);
        }else{
            tab.getCustomView().findViewById(R.id.tab_line).setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
