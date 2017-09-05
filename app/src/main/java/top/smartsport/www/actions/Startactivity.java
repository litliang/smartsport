package top.smartsport.www.actions;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import java.util.Map;

import app.base.action.Task;
import app.base.framework.Init;

/**
 * Created by admin on 2017/9/3.
 */

public class Startactivity extends Task {
    /***
     *
     * @param eventview
     * @param params
     * 1 activity
     * 2 adapterview
     * 3 eventview
     * 4 position
     * @return
     */
    @Override
    public Object run(View eventview, Object... params) {
        try {
            AdapterView adapterView = (AdapterView) params[1];
            Integer pos = (Integer) params[3];
            Map m = (Map) adapterView.getItemAtPosition(pos);
            Class clz = Class.forName(eventview.getContext().getPackageName() + ".activity." + params[0].toString());
            eventview.getContext().startActivity(new Intent(eventview.getContext(), clz).putExtra("id",m.get("id").toString()));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
