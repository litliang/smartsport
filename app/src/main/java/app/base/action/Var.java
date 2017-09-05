package app.base.action;

/**
 * Created by minhua on 2015/11/1.
 */
public class Var extends Epr {


    public String pre;
    public String after;
    public String sign;

    public Var(String raw) {
        super(raw);
        String text = raw;
        text = text.replace("$", "");
        if (text.toString().contains(":")) {
            String[] ns = text.toString().split(":");
            if (ns[1].contains("%s")) {
                int mark = ns[1].indexOf("%s");
                if (ns[1].endsWith("%s")) {
                    after = "";

                } else {
                    after = ns[1].substring(mark+ 2) ;
                }
                if (ns[1].startsWith("%s")) {
                    pre = "";
                } else {
                    pre = ns[1].substring(0, mark);
                }
            }
            set(pre, ns[0], after);
        } else {
            set("",  text, "");
        }
    }

    public void set(String pre, String epr, String after) {
        this.pre = pre;
        this.after = after;
        this.sign = epr;
    }

    @Override
    public Object innerrun() {
        return pre+sign+after;
    }
}