package top.smartsport.www.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

public class MySwipeRefreshLayout extends SwipeRefreshLayout {
	private View view;

	public MySwipeRefreshLayout(Context context) {
		super(context);
	}

	public MySwipeRefreshLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setViewGroup(View view) {
		this.view = view;
	}

	@Override
	public boolean canChildScrollUp() {
		if (view != null && view instanceof AbsListView) {
			final AbsListView absListView = (AbsListView) view;
			return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0
					|| absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
		}
		return super.canChildScrollUp();
	}
}
