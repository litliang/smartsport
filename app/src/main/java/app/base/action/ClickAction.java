package app.base.action;

import android.view.View;

/**
 * Created by admin on 2017/9/3.
 */

public class ClickAction implements View.OnClickListener {
    Actions actions;

    @Override
    public void onClick(View view) {
        if (actions == null) {
            actions = Actions.withView(view).parse(view.getTag().toString());
        }
        actions.action();
    }
}
