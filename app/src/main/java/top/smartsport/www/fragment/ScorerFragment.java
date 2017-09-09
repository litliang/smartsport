package top.smartsport.www.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import top.smartsport.www.R;
import top.smartsport.www.adapter.ScorerAdapter;
import top.smartsport.www.bean.ScorerInfo;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshBase;
import top.smartsport.www.listview_pulltorefresh.PullToRefreshListView;

/**
 * Created by bajieaichirou on 17/9/8.
 * 数据分析之射手榜,积分榜
 */
public class ScorerFragment extends Fragment {

    PullToRefreshListView mList;

    private Activity mThis;
    private int page = 1;
    private ScorerAdapter scorerAdapter;
    private List<ScorerInfo> list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_scorer, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mThis = getActivity();
        mList = (PullToRefreshListView) view.findViewById(R.id.scorer_list);
        initData();
        getData(true);
    }

    private void initData() {
        scorerAdapter = new ScorerAdapter(mThis);

        mList.getRefreshableView().setAdapter(scorerAdapter);
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
        ScorerInfo info;
        Random random = new Random();

        for (int i = 0; i < 10; i ++){
            info = new ScorerInfo();
            info.setPosition(i + 1 + "");
            list.add(info);
        }
        mList.onPullDownRefreshComplete();
        mList.onPullUpRefreshComplete();
        scorerAdapter.clear();
        scorerAdapter.addAll(list);
    }
}
