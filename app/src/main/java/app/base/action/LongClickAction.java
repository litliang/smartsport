package app.base.action;

import android.view.View;

/**
 * Created by admin on 2017/9/3.
 */

public class LongClickAction implements View.OnLongClickListener {
    Actions actions;
    @Override
    public boolean onLongClick(View view) {
        if (actions == null) {
            actions = Actions.withView(view).parse(view.getTag().toString());
        }
        actions.action();
        return false;
    }
}
