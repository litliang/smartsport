package top.smartsport.www.actions;

import android.app.Dialog;
import android.view.View;

import app.base.RRes;
import app.base.action.Action;
import app.base.action.Task;
import app.base.openaction.MapConfTask;

/**
 * Created by admin on 2017/9/15.
 */

public class Addmemberhttp extends Task {

    @Override
    public Object run(View view, Object... objects) {  Dialog dialog = (Dialog) objects[0];
        int id = RRes.get("R."+objects[1].toString()).getAndroidValue();
        final Action action = Action.parseAction(objects[2].toString());

        dialog.findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action.innerrun();
            }
        });
        return null;
    }
}
