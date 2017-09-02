package app.base.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import app.base.DipUtil;
import app.base.MapAdapter;

public class FlowLayout extends AniLayout {
	List<View> set = new ArrayList<View>(7);
	int marginHorizontal;
	int marginVertical;

	public void addCollection(Collection<View> all) {
		for (View view : all) {
			addView(view);
		}
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		marginHorizontal = DipUtil.fromDip(context, 16);
		marginVertical = DipUtil.fromDip(context, 8);
	}

	int y;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
		int x = 0;

		int row = 0;
		if (mAdapter != null) {
			y = 0;
			for (int index = 0; index < mAdapter.getCount(); index++) {
				View child = null;

				if (set.size() - 1 >= index) {
					child = set.get(index);
					child = mAdapter.getView(index, child, null);
				} else {
					child = mAdapter.getView(index, null, null);
					set.add(child);
					addView(child);
				}
				if (child.getVisibility() != View.GONE) {
					child.measure(MeasureSpec.UNSPECIFIED,
							MeasureSpec.UNSPECIFIED);
					int width = child.getMeasuredWidth() + marginHorizontal;
					int height = child.getMeasuredHeight() + marginVertical;
					x += width;
					y = row * height + height;
					if (x > maxWidth) {
						x = width;
						row++;
						y = row * height + height;
					}
				}
			}
			setMeasuredDimension(maxWidth, y);
			return;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void clear() {
		set.clear();
		removeAllViews();
		invalidate();
		mAdapter.clearDataSrc();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		final int childCount = getChildCount();
		int maxWidth = r - l;
		int x = 0;
		int y = 0;
		int row = 0;

		for (int i = 0; i < childCount; i++) {
			final View child = this.getChildAt(i);
			if (child.getVisibility() != View.GONE) {
				int width = child.getMeasuredWidth() + marginHorizontal;
				int height = child.getMeasuredHeight() + marginVertical;
				x += width;
				y = row * height + height;
				if (x > maxWidth) {
					x = width;
					row++;
					y = row * height + height;
				}
				child.layout(x - width, y - height, x, y);

			}
		}

	}

	public MapAdapter getAdapter() {
		// TODO Auto-generated method stub
		return mAdapter;
	}

	
	MapAdapter mAdapter = new MapAdapter(this.getContext());

	public void setAdapter(MapAdapter adapter) {
		// TODO Auto-generated method stub
		mAdapter = adapter;

		requestLayout();
	}
}