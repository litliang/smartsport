package app.base;

import org.json.JSONArray;

import android.view.View;
import android.widget.AbsListView;
import android.widget.CheckBox;
import android.widget.TextView;
import app.base.ui.FlowLayout;
/***
 * 本地缓存
 * @author Administrator
 *
 */
public class Cache extends SPrefUtil {
	private static Cache cache = new Cache();
	public static final String KEY_error = "error";
	static {
		cache.setCopyName("cache");
	}

	public static Cache getInstance() {
		return cache;
	}
/**
 * 依据view的不同类型进行相关操作
 * @param name
 * @param view
 * @return
 */
	public boolean loadDefaultView(String name, View view) {
		Object vl = getValue(name);
		if (view instanceof CheckBox) {
			CheckBox tview = (CheckBox) view;
			if (!vl.toString().isEmpty()) {
				tview.setChecked(vl.toString().equals("true"));
				return true;
			}
		} else if (view instanceof TextView) {
			TextView tview = (TextView) view;
			if (!vl.toString().isEmpty()) {
				tview.setText(vl.toString());
				return true;
			}
		} else if (view instanceof AbsListView) {
			AbsListView abslistview = ((AbsListView) view);
			if (abslistview.getAdapter() == null) {
				return false;
			} else {
				if (!vl.toString().isEmpty()) {

					try {
						((MapAdapter) abslistview.getAdapter()).clearDataSrc();
						if (((MapAdapter) abslistview.getAdapter())
								.isCacheoutofdate()) {
							putValue(name, "");
							return false;
						}
						((MapAdapter) abslistview.getAdapter())
								.setItemDataSrc(new MapContent(new JSONArray(vl
										.toString())));
						((MapAdapter) abslistview.getAdapter())
								.notifyDataSetChanged();

					} catch (Exception e) {
						e.printStackTrace();

						return false;
					}
					return true;
				}
			}
		} else if (view instanceof FlowLayout) {
			FlowLayout abslistview = ((FlowLayout) view);
			if (abslistview.getAdapter() == null) {
				return false;
			} else {
				if (!vl.toString().isEmpty()) {

					try {
						((MapAdapter) abslistview.getAdapter()).clearDataSrc();
						if (((MapAdapter) abslistview.getAdapter())
								.isCacheoutofdate()) {
							putValue(name, "");
							return false;
						}
						((MapAdapter) abslistview.getAdapter())
								.setItemDataSrc(new MapContent(new JSONArray(vl
										.toString())));

						view.invalidate();

						return true;
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}

				}
			}
		}
		return false;

	}

	public void saveView(String name, View view) {
		String txt = "";
		if (view instanceof CheckBox) {
			txt = ((CheckBox) view).isChecked() ? "true" : "false";
		} else if (view instanceof TextView) {
			txt = ((TextView) view).getText().toString();
		} else if (view instanceof AbsListView) {
			AbsListView abslistview = ((AbsListView) view);
			if (abslistview.getAdapter() == null
					|| ((MapAdapter) abslistview.getAdapter()).isEmpty()) {

			} else {
				try {
					txt = ((MapAdapter) abslistview.getAdapter())
							.getItemDataSrc().getContent().toString();
				} catch (Exception e) {
					e.printStackTrace();

				}

			}
		} else if (view instanceof FlowLayout) {
			FlowLayout abslistview = ((FlowLayout) view);
			if (abslistview.getAdapter() == null
					|| ((MapAdapter) abslistview.getAdapter()).isEmpty()) {

			} else {
				try {
					txt = ((MapAdapter) abslistview.getAdapter())
							.getItemDataSrc().getContent().toString();
				} catch (Exception e) {
					e.printStackTrace();

				}

			}
		}
		putValue(name, txt);
	}

	public void clearCache(String name) {
		putValue(name, "");
	}
}
