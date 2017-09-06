package app.base;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by admin on 2017/8/16.
 */
public class MapConf {


    public Map<String, MapConf> confs = new TreeMap<String, MapConf>();


    private MapConf() {
    }

    public static MapConf build() {
        return new MapConf();
    }

    private List<String> fieldnames = new ArrayList<String>();

    private Context context;
    private List<Integer> viewsid = new ArrayList<Integer>();

    public static MapConf with(Context context) {
        MapConf conf = build();
        conf.context = context;
        return conf;
    }

    public MapConf addPair(Integer viewid, String fieldname) {
        fieldnames.add(fieldname);
        viewsid.add(viewid);
        return this;
    }

    public MapConf addPair(String fieldname, Integer viewid) {
        fieldnames.add(fieldname);
        viewsid.add(viewid);
        return this;
    }

    public Object item;
    private View convertView;

    public MapConf source(Object item, android.support.v4.app.Fragment convertView) {
        this.item = item;
        this.convertView = convertView.getView();
        return this;
    }

    public MapConf source(Object item, Fragment convertView) {
        this.item = item;
        this.convertView = convertView.getView();
        return this;
    }

    public MapConf source(Object item, Activity convertView) {
        this.item = item;
        this.convertView = convertView.getWindow().getDecorView();
        return this;
    }

    public MapConf source(Object item, View convertView) {
        this.item = item;
        this.convertView = convertView;
        return this;
    }

    int viewlayoutid;

    public int getViewlayoutid() {
        return viewlayoutid;
    }

    public MapConf source(int viewlayout) {
        viewlayoutid = viewlayout;
        return this;
    }

    public MapConf source(int viewlayout,MapConf conf) {
        viewlayoutid = viewlayout;

        confs.put(viewlayoutid+"", conf);
        return this;
    }

    public void match() {
        if (item instanceof String) {
            item = JsonUtil.extractJsonRightValue(((String) item));
        }
        if (fieldnames.size() == 0 && viewsid.size() == 0) {
            link();
            return;
        }
        String name;
        Object value;
        if (item instanceof Map) {
            Map<String, Object> items = (Map<String, Object>) item;
            for (int i = 0; i < this.fieldnames.size(); i++) {
                name = this.fieldnames.get(i);
                String n;
                if (name.contains(":")) {
                    n = name.split(":")[0];
                } else {
                    n = name;
                }
                if (items.containsKey(n)) {
                    value = items.get(n);
                    if (value != null) {
                        findAndBindView(convertView, item, name, value, i);
                    }
                }


            }
        }
    }

    private void link() {
        setView(item, item, "", convertView, convertView);
    }

    protected boolean findAndBindView(View convertView, Object item,
                                      String name, Object value, int fieldpos) {
        if (value == null) {
            throw new IllegalArgumentException(
                    "check the 'value' data:ensure it is not null.thanq");
        }
        int theViewId = fieldpos;
        int viewid = this.viewsid.get(theViewId);
        if (viewid == -1) {
            return false;
        } else {
            View theView = convertView.findViewById(viewid);
            return setView(item, value, name, convertView, theView);
        }

    }

    @Deprecated
    public MapConf fields(String... fields) {
        fieldnames.clear();
        fieldnames.addAll(Arrays.asList(fields));
        return this;
    }

    @Deprecated
    public MapConf viewids(String... viewids) {
        viewsid.clear();
        for (String s : viewids) {
            viewsid.add(RRes.get("R.id." + s).getAndroidValue());
        }
        return this;
    }

    public MapConf pair(String p, MapConf conf) {

        String[] pair = p.split("->");
        confs.put(pair[0], conf);
        return pair(p);
    }

    public MapConf pair(String p) {
        String[] pair = p.split("->");
        if (pair.length == 1) {
            fieldnames.add(pair[0]);
            viewsid.add(-1);
        } else if (pair.length > 1) {
            fieldnames.add(pair[0]);
            viewsid.add(RRes.get("R.id." + pair[1]).getAndroidValue());
        }

        return this;
    }


    public MapConf pairs(String... pairs) {
        for (String p : pairs) {
            String[] pair = p.split("->");
            if (pair.length == 1) {
                fieldnames.add(pair[0]);
                viewsid.add(-1);
            } else if (pair.length > 1) {
                fieldnames.add(pair[0]);
                viewsid.add(RRes.get("R.id." + pair[1]).getAndroidValue());
            }
        }

        return this;
    }

    public MapConf conf(MapConf mc) {
        confs.put("",mc);
        return this;
    }

    public static abstract class Tackle {
        public abstract void tackleBefore(Object item, Object value, String name,
                                          View convertView, View theView);

        public abstract void tackleAfter(Object item, Object value, String name,
                                         View convertView, View theView);
    }

    public MapConf addTackle(Tackle tackle) {
        this.tackle = tackle;
        return this;
    }

    Tackle tackle;

    protected boolean setView(Object item, Object value, String name,
                              View convertView, View theView) {
        if (theView == null) {
            return false;
        }

        if (tackle != null) {
            tackle.tackleBefore(item, value, name, convertView, theView);
        }
        if(value!=null&&value.toString().toLowerCase().equals("null")){
            value = "";
        }
        if (name.toString().contains(":")) {
            String[] ns = name.toString().split(":");
            if (!(ns[1].contains("(") && ns[1].contains(")") && ns[1].contains("#"))) {
                if (ns[1].contains("%s")) {
                    value = ns[1].replace("%s", value.toString());
                }
            }
        }
        theView.setVisibility(View.VISIBLE);
        StyleBox styleBox = null;
        if (theView instanceof AdapterView) {
            MapConf conf = confs.get(name);
            if (conf != null) {
                ((AdapterView) theView).setAdapter(new MapAdapter(context));
                if (((AdapterView) theView).getAdapter() instanceof MapAdapter) {
                    MapAdapter mapadapter = (MapAdapter) ((AdapterView) theView).getAdapter();
                    mapadapter.setItemDataSrc(new MapContent(value));
                    mapadapter.setItemLayout(conf.getViewlayoutid());
                    mapadapter.setMapConf(conf);
                    mapadapter.notifyDataSetChanged();
                }
            }
        } else if (theView instanceof ImageView) {
            if (value == null || value.toString().equals("-1")) {
                return false;
            }
            if (value instanceof Integer) {
                ((ImageView) theView).setImageResource(Integer.parseInt(value
                        .toString()));
            } else if (value.getClass() == BitmapDrawable.class) {
                ((ImageView) theView).setImageDrawable((BitmapDrawable) value);
            } else if (value instanceof Drawable) {
                ((ImageView) theView).setImageDrawable((Drawable) value);
            } else if (value instanceof String) {

                Glide.with(context).load(value.toString()).into((ImageView) theView);
            }

        } else if (theView instanceof CheckBox) {
            if (value != null && (value.toString().trim().toLowerCase().equals("true") || value.toString().trim().toLowerCase().equals("false"))) {
                ((CheckBox) theView).setChecked(Boolean.parseBoolean(value.toString()));
                ((CheckBox) theView).setVisibility(View.VISIBLE);
            }

        } else if (theView instanceof TextView) {
            ((TextView) theView)
                    .setText(value instanceof SpannableStringBuilder ? (SpannableStringBuilder) value
                            : value.toString());

        }
        if (tackle != null) {
            tackle.tackleAfter(item, value, name, convertView, theView);
        }
        return false;
    }
}
