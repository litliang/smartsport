package top.smartsport.www.actions;

import android.view.View;

import app.base.openaction.MapConfTask;
import top.smartsport.www.widget.TeamMemberView;

/**
 * Created by admin on 2017/9/11.
 */

public class Visible extends MapConfTask {
    @Override
    public void run(View view, Object item, String name, Object value, Object casevalue, View convertView, Object... objects) {
        if (value==null||value.toString().equals("null")) {

        }else if (value.toString().equals("0")){
            view.setVisibility(View.VISIBLE);
        }else if (value.toString().equals("1")){
            view.setVisibility(View.GONE);
        }
    }
}
