package top.smartsport.www.actions;

import android.view.View;

import app.base.openaction.AdapterViewTask;
import top.smartsport.www.widget.TeamMemberView;

/**
 * Created by admin on 2017/9/11.
 */

public class Addmember extends AdapterViewTask {
    @Override
    public void adaptitem(View view,Object item, String name, Object value, Object casevalue, View convertView, Object... objects) {
        if (casevalue.toString().split("-")[0].equals("name")) {
            ((TeamMemberView) view).setNumber(casevalue.toString().split("-")[1]);
        }else if (casevalue.toString().split("-")[0].equals("position")){
            ((TeamMemberView) view).setLocation(casevalue.toString().split("-")[1]);
        }else if (casevalue.toString().split("-")[0].equals("number")){
            ((TeamMemberView) view).setNumber(casevalue.toString().split("-")[1]);
        }
    }
}
