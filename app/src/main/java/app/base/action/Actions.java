package app.base.action;

import android.app.Application;
import android.content.Context;
import android.icu.text.PluralFormat;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;


public class Actions {

    String callflow = "get()--print()-back();back();";
    List<Action> actions = new ArrayList<Action>();
    private View view;

    public Actions() {
        this.actionGroup = actionGroup;
//        parseActions(actionGroup);

    }

    String actionGroup;

    public Actions setView(View view) {
        this.view = view;
        return this;
    }

    public static Actions withView(View view) {
        return new Actions().setView(view).setApplicationContext(view.getContext().getApplicationContext());
    }

    List<Object> params = new ArrayList<Object>();

    Context application;

    public Actions setApplicationContext(Context application) {
        this.application = application;
        return this;
    }

    public Actions setActionGroup(String actionGroup) {
        this.actionGroup = actionGroup;
        return this;
    }

    public Actions parse(String actionGroup) {
        Actions actions = this;
        this.setActionGroup(actionGroup);
        if (!actionGroup.contains("-")) {
            String[] g = actionGroup.split(";");
            for (String s : g) {
                if (s == null || s.trim().equals("")) {
                    continue;
                }
                actions.actions.add((Action) new Action(s).addParams(objects).setEventView(view));
            }
        }
        return actions;
    }

    public void action() {
        for (Action action : actions) {
            action.innerrun();
        }
    }

    List objects = new ArrayList();

    public Actions addParams(AdapterView<?> adapterView, View view, Integer integer) {
        objects.add(adapterView);
        objects.add(view);
        objects.add(integer);
        return this;
    }
}
