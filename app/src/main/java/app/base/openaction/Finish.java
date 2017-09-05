package app.base.openaction;

import android.app.Activity;
import android.view.View;

import java.util.Objects;

import app.base.action.Action;
import app.base.action.EprRunable;
import app.base.action.Task;

/**
 * Created by admin on 2017/9/3.
 */

public class Finish extends Task {


    @Override
    public Object run(View view, Object... params) {
        ((Activity) view.getContext()).finish();
        return null;
    }
}
