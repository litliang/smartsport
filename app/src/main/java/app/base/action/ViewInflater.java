package app.base.action;

import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import app.base.framework.Init;
import app.base.ui.AdaptView;
import dalvik.system.DexFile;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;

public class ViewInflater extends LayoutInflater {
    Context newContext;
    private boolean scale;

    public static final String packageName = "app.base.ui.";
    public static final Map<String, String> uiNames = new TreeMap<String, String>();

    static {
        uiNames.put("WebView", "android.webkit.");
    }

    Set<String> layoutlog = new TreeSet<String>();

    @Override
    protected View onCreateView(View parent, String name, AttributeSet attrs) throws ClassNotFoundException {
        if (parent.getTag() != null) {
            if (!layoutlog.contains(parent.toString())) {
                layoutlog.add(parent.toString());
                parent.setOnClickListener(new ClickAction());
            }

        }
        return super.onCreateView(parent, name, attrs);
    }

    static {
        scan();
    }

    public static void scan() {
        String path;
        try {
            path = Init.bigContext.getPackageManager().getApplicationInfo(
                    Init.bigContext.getPackageName(), 0).sourceDir;

            DexFile dexfile = new DexFile(path);
            Enumeration entries = dexfile.entries();
            while (entries.hasMoreElements()) {
                String name = (String) entries.nextElement();
                if (name.startsWith(packageName)) {

                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public ViewInflater(LayoutInflater original, Context newContext) {
        super(original, newContext);
        this.newContext = newContext;
        this.scale = true;
        // TODO Auto-generated constructor stub

    }

    public static ViewInflater from(Context newContext) {
        return new ViewInflater(newContext);
    }

    public ViewInflater(Context newContext) {
        this(newContext, true);
    }

    public ViewInflater(Context newContext, boolean scale) {
        this(LayoutInflater.from(newContext), newContext);
        this.scale = scale;

    }

    @Override
    public LayoutInflater cloneInContext(Context arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    public static ViewInflater fromAty(Context ctx) {
        return new ViewInflater(ctx, true);
    }

    public static ViewInflater fromAty(Context ctx, boolean scale) {
        return new ViewInflater(ctx, scale);
    }

    @Override
    protected View onCreateView(String name, AttributeSet attrs)
            throws ClassNotFoundException {
        // TODO Auto-generated method stub
        View view = null;
        try {

            if (uiNames.containsKey(name)) {
                try {
                    view = (View) Class.forName(uiNames.get(name) + name)
                            .getConstructor(Context.class, AttributeSet.class)
                            .newInstance(newContext, attrs);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            } else if (name.equals("View") || name.contains(".")) {
                view = super.onCreateView(name, attrs);
            } else {
                view = super.createView(name, "android.widget.", attrs);
            }
            if (view.getTag() != null) {
                if (view instanceof AdapterView) {
                    ((AdapterView) view).setOnItemClickListener(new ItemClickAction());
                } else {
                    view.setOnClickListener(new ClickAction());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;

    }
}
