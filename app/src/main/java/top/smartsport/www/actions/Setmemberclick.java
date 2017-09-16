package top.smartsport.www.actions;

import android.view.View;

import java.util.Map;

import app.base.action.Task;
import app.base.openaction.MapConfTask;

/**
 * Created by admin on 2017/9/15.
 */

public class Setmemberclick extends MapConfTask {
    @Override
    public void run(View view, Object item, String name, Object value, Object casevalue, View convertView, Object... objects) {
        Map coach = (Map) value;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
