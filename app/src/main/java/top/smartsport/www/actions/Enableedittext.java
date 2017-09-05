package top.smartsport.www.actions;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;

import app.base.RRes;
import app.base.action.Task;

/**
 * Created by admin on 2017/9/3.
 */

public class Enableedittext extends Task {
    boolean init = false;

    @Override
    public Object run(View view, Object... params) {
        ((Activity) view.getContext()).findViewById(RRes.get("R.id." + params[0]).getAndroidValue()).setEnabled(init = !init);
        if (init) {
            ((Activity) view.getContext()).findViewById(RRes.get("R.id." + params[0]).getAndroidValue()).requestFocus();
            ((Activity) view.getContext()).findViewById(RRes.get("R.id." + params[0]).getAndroidValue()).requestFocusFromTouch();
        }
        return null;
    }
}
