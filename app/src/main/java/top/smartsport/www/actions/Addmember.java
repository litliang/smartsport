package top.smartsport.www.actions;

import android.view.View;

import app.base.openaction.MapConfTask;
import top.smartsport.www.widget.TeamMemberView;

/**
 * Created by admin on 2017/9/11.
 */

public class Addmember extends MapConfTask {
    @Override
    public void run(View view, Object item, String name, Object value, Object casevalue, View convertView, Object... objects) {
        if (value.toString().split("-")[0].equals("name")) {
            ((TeamMemberView) view).setName(value.toString().split("-")[1]);
        }else if (value.toString().split("-")[0].equals("position")){
            ((TeamMemberView) view).setLocation(value.toString().split("-")[1]);
        }else if (value.toString().split("-")[0].equals("number")){
            ((TeamMemberView) view).setNumber(value.toString().split("-")[1]);
        }
    }
}
