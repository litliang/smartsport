package app.base;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2017/8/16.
 */
public class VDMapper {


    public app.base.VDMapper build() {
        return new VDMapper();
    }

    private List<String> fieldnames = new ArrayList<String>();

    private Context context;
    private List<Integer> viewsid = new ArrayList<Integer>();

    public VDMapper with(Context context) {
        this.context = context;
        return this;
    }

    public VDMapper addPair(String fieldname,Integer viewid){
        fieldnames.add(fieldname);
        viewsid.add(viewid);
        return this;
    }

    public void treatMap(Object item, View convertView) {
        String name;
        Object value;
        Map<String, Object> items = (Map<String, Object>) item;
        for (int i = 0; i < this.fieldnames.size(); i++) {
            name = this.fieldnames.get(i);
            if (items.containsKey(name)) {
                value = items.get(name);
                if (value != null) {
                    findAndBindView(convertView, item, name, value);
                }
            }
        }
    }

    protected boolean findAndBindView(View convertView, Object item,
                                      String name, Object value) {
        if (value == null) {
            throw new IllegalArgumentException(
                    "check the 'value' data:ensure it is not null.thanq");
        }
        int theViewId = this.fieldnames.indexOf(name);
        View theView = convertView.findViewById(this.viewsid.get(theViewId));
        return setView(item, value, convertView, theView);

    }

    protected boolean setView( Object item, Object value,
                              View convertView, View theView) {
        if (theView == null) {
            return false;
        }
        theView.setVisibility(View.VISIBLE);
        StyleBox styleBox = null;

        if (theView instanceof ImageView) {
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
            return true;
        } else if (theView instanceof CheckBox) {
            if (value != null && (value.toString().trim().toLowerCase().equals("true") || value.toString().trim().toLowerCase().equals("false"))) {
                ((CheckBox) theView).setChecked(Boolean.parseBoolean(value.toString()));
                ((CheckBox) theView).setVisibility(View.VISIBLE);
            }
            return true;
        } else if (theView instanceof TextView) {
            ((TextView) theView)
                    .setText(value instanceof SpannableStringBuilder ? (SpannableStringBuilder) value
                            : value.toString());
            return true;
        }
        return false;
    }
}
