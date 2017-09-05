package top.smartsport.www.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.smartsport.www.R;
import top.smartsport.www.adapter.CoachAdapter;
import top.smartsport.www.adapter.TrainningAdapter;
import top.smartsport.www.widget.MyListView;

public class CoachDetailFragment extends Fragment {
    private View mView;
    private Context mContext;
    private MyListView lvTraining;
    private RecyclerView rcCoach;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.coach_details, null, false);
        initView();
        return mView;
    }

    private void initView() {
        mContext = getActivity();
        lvTraining = (MyListView) mView.findViewById(R.id.lv_training);
        rcCoach = (RecyclerView) mView.findViewById(R.id.rc_coach);
        TrainningAdapter trainingAdapter = new TrainningAdapter();
        lvTraining.setAdapter(trainingAdapter);
        LinearLayoutManager linearLayoutManagerHorizontal = new LinearLayoutManager(mContext);
        linearLayoutManagerHorizontal.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcCoach.setLayoutManager(linearLayoutManagerHorizontal);
        CoachAdapter coachAdapter = new CoachAdapter();
        rcCoach.setAdapter(coachAdapter);
    }

}
