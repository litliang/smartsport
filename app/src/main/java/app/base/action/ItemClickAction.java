package app.base.action;

import android.view.View;
import android.widget.AdapterView;

/**
 * Created by admin on 2017/9/3.
 */

public class ItemClickAction implements AdapterView.OnItemClickListener {
    Actions actions;
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (actions == null) {
            actions = Actions.withView(view).addParams(adapterView,view,new Integer(i)).parse(adapterView.getTag().toString());
        }
        actions.action();
    }
}
