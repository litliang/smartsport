package top.smartsport.www.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import top.smartsport.www.R;
import top.smartsport.www.adapter.ScoreboardAdapter;
import top.smartsport.www.bean.ScoreboardInfo;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshBase;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;

/**
 * Created by bajieaichirou on 17/9/8.
 * 数据分析之积分榜
 */

public class ScoreboardFragment extends Fragment {

    @ViewInject(R.id.scoreboard_list)
    PullToRefreshListView  mList;

    private Activity mThis;
    private int page = 1;
    private ScoreboardAdapter scoreboardAdapter;
    private List<ScoreboardInfo> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scoreboard, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mThis = getActivity();
        mList = (PullToRefreshListView) view.findViewById(R.id.scoreboard_list);
        initData();
        getData(true);
    }

    private void initData() {
        scoreboardAdapter = new ScoreboardAdapter(mThis);

        mList.getRefreshableView().setAdapter(scoreboardAdapter);
        mList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                getData(true);
                String label = DateUtils.formatDateTime(mThis, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getHeaderLoadingLayout().setLastUpdatedLabel(label);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData(false);
                String label = DateUtils.formatDateTime(mThis, System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                // Update the LastUpdatedLabel
                refreshView.getFooterLoadingLayout().setLastUpdatedLabel(label);
            }
        });
    }

    /**
     * 获取数据
     * */
    private void getData(final boolean refresh){
        //TODO
//        if(refresh){
//            page = 1;
//        }else {
//            page++;
//        }
        ScoreboardInfo info;
        Random random = new Random();

        for (int i = 0; i < 10; i ++){
            info = new ScoreboardInfo();
            info.setCard(random.nextInt(5)%(5) + 1 + "红" + (random.nextInt(5)%(5) + 1) + "黄" );
            info.setPosition(i + 1 + "");
            info.setResult("3/2/0");
            list.add(info);
        }
        mList.onPullDownRefreshComplete();
        mList.onPullUpRefreshComplete();
        scoreboardAdapter.clear();
        scoreboardAdapter.addAll(list);
    }
}
