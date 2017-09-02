package top.smartsport.www.utils;

import android.view.View;

import org.xutils.x;

public abstract class ViewHolder {
    protected View root;

    public ViewHolder(View root) {
        this.root = root;
        x.view().inject(this, root);
    }
}
