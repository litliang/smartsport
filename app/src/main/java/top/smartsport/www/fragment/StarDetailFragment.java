package top.smartsport.www.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import top.smartsport.www.R;

public class StarDetailFragment extends Fragment {
    private View mView;
    private Context mContext;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.star_details, container, false);
        return mView;
    }

}
