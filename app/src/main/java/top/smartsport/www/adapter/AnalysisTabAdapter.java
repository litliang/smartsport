package top.smartsport.www.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import top.smartsport.www.R;

/**
 * Created by bajieaichirou on 17/9/9.
 */
public class AnalysisTabAdapter extends FragmentPagerAdapter {

    private List<Fragment> list_fragment;
    private String[] tab_titles;

    private TextView mTxt;
    private View mLineView;

    private Context mContext;
    private boolean isShowImg;

    public AnalysisTabAdapter(FragmentManager fm, Context context, List<Fragment> list_fragment, String[] title, boolean isShow) {
        super(fm);
        this.list_fragment = list_fragment;
        this.tab_titles = title;
        this.mContext = context;
        this.isShowImg = isShow;
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.adapter_analysis_tab_item, null);
        mLineView = (View) v.findViewById(R.id.tab_line);
        mTxt = (TextView) v.findViewById(R.id.tab_txt);
        mTxt.setText(tab_titles[position]);
        if(isShowImg){
            mLineView.setVisibility(View.VISIBLE);
        }else{
            mLineView.setVisibility(View.INVISIBLE);
        }
        return v;
    }

    @Override
    public Fragment getItem(int position) {
        return list_fragment.get(position);
    }

    @Override
    public int getCount() {
        return tab_titles.length;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tab_titles[position];
    }



}
