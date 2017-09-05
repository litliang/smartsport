package app.base.action;

import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import app.base.framework.Init;

/**
 * Created by minhua on 2015/10/23.
 */

public class Action extends Epr {

    public static Action parseAction(Action action) throws Exception {
        String raw = action.raw;
        String parameters = action.parameters;
        String taskname = action.taskname;
        Class clazz = action.viewContext;
        if (raw.endsWith(";")) {
            raw = raw.substring(0, raw.length() - 1);
        }

        parameters = raw.substring(raw.indexOf("(") + 1, raw.length() - 1);
        taskname = raw.substring(0, raw.indexOf("("));

        String strclz = null;
        strclz = "app.base.openaction." + taskname.toUpperCase().substring(0, 1) + taskname.substring(1).toLowerCase();

        try {
            clazz = (Class<Action>) Class.<Action>forName(strclz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (clazz == null) {
            strclz = Epr.getCtx().getPackageName() + ".actions." + taskname.toUpperCase().substring(0, 1) + taskname.substring(1).toLowerCase();

            try {
                clazz = (Class<Action>) Class.<Action>forName(strclz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        action.raw = raw;
        action.parameters = parameters;
        action.taskname = taskname;
        action.viewContext = clazz;
        return action;
    }


    public List<Object> params = new ArrayList<Object>();

    public Action addParams(List objects) {
        params.addAll(objects);
        return this;
    }
    public String taskname;


    public Action(String raw) {
        super(raw);
        try {
            Action.parseAction(this);
            Action.parseParams(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List getParams() {
        return params;
    }


    public Object innerrun() {

        if (viewContext != null) {

            Object aresult;
            List<Object> rt = new ArrayList<Object>();
            for (Epr epr : preparedParams) {
                if (epr == null) {
                    continue;
                }

                aresult = epr.innerrun();
                if (aresult != null) {
                    rt.add(aresult.toString());
                } else {
                    rt.add("");
                }

            }
            rt.addAll(params);
            try {
                if (runnable == null) {
                    runnable = viewContext.newInstance();
                }
                return runnable.run(getEventView(), rt.toArray());
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    String parameters;
    Class<Task> viewContext;


    public static Action parseParams(Action action) {
        String param = action.parameters;
        List<Epr> ps = new ArrayList<Epr>();
        String[] params = param.split(",");
        for (String p : params) {
            if (p == null || p.equals("")) {
                continue;
            }
            ps.add(app.base.action.Epr.parseParam(p, action.getEventView()));
        }
        action.preparedParams = ps;
        return action;
    }

    List<Epr> preparedParams;

}
