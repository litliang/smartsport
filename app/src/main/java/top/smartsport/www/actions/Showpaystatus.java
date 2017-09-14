package top.smartsport.www.actions;

import android.view.View;
import android.widget.TextView;

import app.base.openaction.MapConfTask;
import top.smartsport.www.R;

/**
 * Created by admin on 2017/9/11.
 */

public class Showpaystatus extends MapConfTask {
    @Override
    public void run(View view, Object item, String name, Object value, Object casevalue, View convertView, Object... objects) {
        if (casevalue.toString().equals("0")) {
            ((TextView)view).setTextColor(view.getContext().getResources().getColor(R.color.theme_green,null));
            ((TextView)view).setBackgroundResource(R.drawable.shape_bg_hotcity_green);
        } else if (casevalue.toString().equals("1")) {
            ((TextView)view).setTextColor(view.getContext().getResources().getColor(R.color.text_gray,null));
            ((TextView)view).setBackgroundResource(R.drawable.shape_bg_hotcity);
        }
    }
}
