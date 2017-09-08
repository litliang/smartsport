package top.smartsport.www.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.fragment.ScoreboardFragment;
import top.smartsport.www.fragment.ScorerFragment;

/**
 * Created by bajieaichirou on 17/9/7.
 * 数据分析
 */
@ContentView(R.layout.activity_data_analysis)
public class ActivityDataAnalysis extends BaseActivity implements View.OnClickListener{

    private RelativeLayout mScoreboardLayout, mScorerLayout, mAssistLayout;
    private TextView mScoreboardTv, mScorerTv, mAssistTv;
    private View mScoreboardView, mScorerView, mAssistView;

    private ScoreboardFragment scoreboardFragment;
    private ScorerFragment scorerFragment;



    @Override
    protected void initView() {
        initUI();
        setTitle(getString(R.string.data_analysis));
        selectTab(0);
    }

    private void initUI() {
        mScoreboardLayout = (RelativeLayout) findViewById(R.id.tab_scoreboard);
        mScorerLayout = (RelativeLayout) findViewById(R.id.tab_scorer);
        mAssistLayout = (RelativeLayout) findViewById(R.id.tab_assist);
        mScoreboardTv = (TextView) findViewById(R.id.tab_scoreboard_txt);
        mScorerTv = (TextView) findViewById(R.id.tab_scorer_txt);
        mAssistTv = (TextView) findViewById(R.id.tab_assist_txt);
        mScoreboardView = (View) findViewById(R.id.tab_scoreboard_line);
        mScorerView = (View) findViewById(R.id.tab_scorer_line);
        mAssistView = (View) findViewById(R.id.tab_assist_line);
        mScoreboardLayout.setOnClickListener(this);
        mScorerLayout.setOnClickListener(this);
        mAssistLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_scoreboard:
                selectTab(0);
                break;
            case R.id.tab_scorer:
                selectTab(1);
                break;
            case R.id.tab_assist:
                selectTab(2);
                break;
        }
    }

    private void selectTab(int i) {
        resetImgs();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        hideFragments(transaction);
        switch (i) {
            case 0:
                mScoreboardView.setVisibility(View.VISIBLE);
                mScoreboardTv.setTextColor(getResources().getColor(R.color.theme_color));
                if (scoreboardFragment == null) {
                    scoreboardFragment = new ScoreboardFragment();
                    transaction.add(R.id.tab_content, scoreboardFragment);
                } else {
                    transaction.show(scoreboardFragment);
                }
                break;
            case 1:
                mScorerView.setVisibility(View.VISIBLE);
                mScorerTv.setTextColor(getResources().getColor(R.color.theme_color));
                scorerFragment = new ScorerFragment();
                transaction.add(R.id.tab_content, scorerFragment);
                break;
            case 2:
                mAssistView.setVisibility(View.VISIBLE);
                mAssistTv.setTextColor(getResources().getColor(R.color.theme_color));
                scorerFragment = new ScorerFragment();
                transaction.add(R.id.tab_content, scorerFragment);
                break;
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        if (scoreboardFragment != null) {
            transaction.hide(scoreboardFragment);
        }
        if (scorerFragment != null) {
            transaction.hide(scorerFragment);
        }
    }

    private void resetImgs() {
        mScoreboardView.setVisibility(View.INVISIBLE);
        mScoreboardTv.setTextColor(getResources().getColor(R.color.black));
        mScorerView.setVisibility(View.INVISIBLE);
        mScorerTv.setTextColor(getResources().getColor(R.color.black));
        mAssistView.setVisibility(View.INVISIBLE);
        mAssistTv.setTextColor(getResources().getColor(R.color.black));
    }


    @Override
    protected void onResume() {
        super.onResume();
//        laManager.dispatchResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        laManager.dispatchPause(true);
    }

}
