package top.smartsport.www.actions;

import android.view.View;

import app.base.action.Task;
import top.smartsport.www.R;

/**
 * Created by admin on 2017/9/3.
 */

public class Fav extends Task {

    boolean isinit = false;

    @Override
    public Object run(View view, Object... params) {
        if (!isinit) {
            view.setBackground(view.getContext().getResources().getDrawable(R.mipmap.collect_checked, null));
        } else {
            view.setBackground(view.getContext().getResources().getDrawable(R.mipmap.collect_uncheck, null));
        }
        isinit = !isinit;
        return null;
    }
}
