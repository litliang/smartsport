package top.smartsport.www.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ScrollingTabContainerView;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.zhy.autolayout.AutoLayoutActivity;

import org.xutils.view.annotation.ContentView;

import top.smartsport.www.R;
import top.smartsport.www.adapter.CoachAdapter;
import top.smartsport.www.adapter.TrainningAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.widget.MyListView;

@ContentView(R.layout.coach_details)
public class CoachDetailActivity extends BaseActivity {
    private View mView;
    private Context mContext;
    private MyListView lvTraining;
    private RecyclerView rcCoach;
    private boolean isAll;


    public void initView() {

        lvTraining = (MyListView) findViewById(R.id.lv_training);
        rcCoach = (RecyclerView) findViewById(R.id.rc_coach);
        TrainningAdapter trainingAdapter = new TrainningAdapter();
        lvTraining.setAdapter(trainingAdapter);
        LinearLayoutManager linearLayoutManagerHorizontal = new LinearLayoutManager(this);
        linearLayoutManagerHorizontal.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcCoach.setLayoutManager(linearLayoutManagerHorizontal);
        CoachAdapter coachAdapter = new CoachAdapter();
        rcCoach.setAdapter(coachAdapter);
        TextView tvName = (TextView) findViewById(R.id.tv_name);
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                OperatHelper.getInstance().toActivity(view.getContext(),ConsultDetailActivity.class);
            }
        });
        final TextView introduce = (TextView) findViewById(R.id.tv_introduce);
        TextView seeMore = (TextView) findViewById(R.id.tv_see_more);
        seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                OperatHelper.getInstance().seeMore(view.getContext(),isAll,introduce);
                isAll = !isAll;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        ((ScrollView) findViewById(R.id.scroll)).scrollTo(0, 0);
    }
}
