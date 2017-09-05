package app.base.openaction;

import android.view.View;
import android.widget.Toast;

import app.base.RRes;
import app.base.action.Task;
import app.base.framework.Init;
import top.smartsport.www.activity.MainActivity;
import top.smartsport.www.utils.ActivityStack;

/**
 * Created by admin on 2017/9/3.
 */

public class Clickactivityid extends Task {


    @Override
    public Object run(View view, Object... params) {
        try {
            Class clz = Class.forName(Init.bigContext.getPackageName()+".activity."+params[0].toString());
        ActivityStack.getInstance().findActivityByClass(clz).findViewById(RRes.get("R.id."+params[1].toString()).getAndroidValue()).performClick();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
}
