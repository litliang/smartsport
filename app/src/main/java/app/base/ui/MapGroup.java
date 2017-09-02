package app.base.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import app.base.JsonUtil;
import app.base.MapAdapter;

public class MapGroup extends FrameLayout implements AdaptView {
	Context context;

	public MapGroup(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	@Override
	public void init() {
		int leng = mapAdapter.linearInfo.itemsid.size();
		for (int i = 0; i < leng; i++) {
			View view = findViewById(mapAdapter.linearInfo.itemsid.get(i));
			if (view == null)
				return;
			int idx = mapAdapter.linearInfo.itemsid
					.indexOf(mapAdapter.linearInfo.itemsid.get(i));
			Object item = JsonUtil.findJsonLink(
					mapAdapter.linearInfo.itemsname.get(idx), mapAdapter
							.getItemDataSrc().getContent());
			mapAdapter.getView(item, view, i, this);
		}
	}

	MapAdapter mapAdapter = new MapAdapter(context);

	@Override
	public MapAdapter getAdapter() {
		// TODO Auto-generated method stub
		return mapAdapter;
	}

	@Override
	public void setAdapter(MapAdapter mapAdapter) {
		// TODO Auto-generated method stub
		this.mapAdapter = mapAdapter;
		init();
	}

}
