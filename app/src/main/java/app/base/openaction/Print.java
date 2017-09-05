package app.base.openaction;

import android.view.View;
import android.widget.Toast;

import app.base.action.EprRunable;
import app.base.action.Task;

/**
 * Created by admin on 2017/9/3.
 */

public class Print extends Task {


    @Override
    public Object run(View view, Object... params) {
        Toast.makeText(view.getContext(),("-----   "+(params.length>0?params[0].toString():"")),Toast.LENGTH_LONG).show();
        return null;
    }
}
