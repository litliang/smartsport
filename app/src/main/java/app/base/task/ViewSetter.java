package app.base.task;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import app.base.MapAdapter;
import app.base.MapAdapter.AdaptInfo;
import app.base.ui.FlowLayout;

/**
 * UI线程0那个任务
 * @author Administrator
 *
 */
public class ViewSetter extends PreHandler {
	private Object adapterId;

	public ViewSetter setAdapterId(Object adapterId) {
		this.adapterId = adapterId;
		return this;
	}

	AdaptInfo adaptInfo;

	public ViewSetter setAdaptInfo(AdaptInfo adaptInfo) {
		this.adaptInfo = adaptInfo;
		return this;
	}

	@Override
	public void run(Activity aty) throws Exception {
		// TODO Auto-generated method stub
		if (adaptInfo == null || adapterId == null)
			return;
		MapAdapter mapAdapter = new MapAdapter(aty, adaptInfo);
		View view;
		if (adapterId instanceof Integer) {
			view = aty.findViewById((Integer) adapterId);
		} else {
			view = (View) adapterId;
		}
		if (view instanceof AdapterView) {
			((AdapterView) view).setAdapter(mapAdapter);
		} else if (view instanceof FlowLayout) {
			((FlowLayout) view).setAdapter(mapAdapter);
		}
	}

}
