package top.smartsport.www.adapter;

/**
 * Created by Aaron on 2017/7/24.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

public class QXZXAdapter extends FragmentPagerAdapter {

    private String[] title;
    private List<Fragment> list;
    private Context context;

    public QXZXAdapter(FragmentManager fm) {
        super(fm);
    }

    public QXZXAdapter(Context context,FragmentManager fm, String[] title, List<Fragment> list) {
        super(fm);
        this.title = title;
        this.list = list;
        this.context =context;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return title.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return title[position];
    }

}