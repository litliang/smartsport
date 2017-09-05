package app.base.action;


import android.content.Context;
import android.view.View;

/**
 * Created by minhua on 2015/11/1.
 */
public class Epr {
    public static Context getCtx() {
        return ctx;
    }

    public static void setCtx(Context context) {
        ctx = context;
    }

    public static Context ctx;
    public View eventView;

    public View getEventView() {
        return eventView;
    }

    public Epr setEventView(View eventView) {
        this.eventView = eventView;
        return this;
    }


    public String raw;

    public Epr(String raw) {
        this.raw = raw;
    }


    public Task runnable;

    public Task getRunnable() {
        return runnable;
    }

    public Task setRunnable(Task runnable) {
        this.runnable = runnable;
        return runnable;
    }

    public static Epr parseParam(String raw, View enventView) {
        String text = raw;
        boolean methodprior = text.contains("(") && text.contains(")");
        boolean varprior = text.trim().startsWith("$");

        if (methodprior) {
            return new Action(raw).setEventView(enventView);
        } else if(varprior) {
            return new Var(raw).setEventView(enventView);
        }else{
            return new Var(raw).setEventView(enventView);
        }

    }

    public Object innerrun(){
        return null;
    };

}
