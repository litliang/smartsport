package top.smartsport.www.actions;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import app.base.RRes;
import app.base.action.Task;
import app.base.openaction.MapConfTask;

/**
 * Created by admin on 2017/9/3.
 */

public class Getprice extends MapConfTask {

    @Override
    public void run(View view, Object item, String name, Object value, Object casevalue, View convertView, Object... objects) {
        ((TextView)view).setText(value.toString().replace("￥","").replace(".00","").replace("/年",""));
    }
}
