package top.smartsport.www.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.smartsport.www.R;
import top.smartsport.www.adapter.ConsultAdapter;
import top.smartsport.www.widget.MyListView;

public class ConsultDetailFragment extends Fragment {
    private View mView;
    private Context mContext;
    private MyListView lvConsult;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.consult_layout, null, false);
        initView();
        return mView;
    }

    private void initView() {
        mContext = getActivity();
        lvConsult = (MyListView) mView.findViewById(R.id.lv_consult);
        ConsultAdapter adapter = new ConsultAdapter();
        lvConsult.setAdapter(adapter);
    }

}
