package app.base.ui;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

public class AniLayout extends RelativeLayout {
	public static class LayoutInfo {

		public LayoutInfo(int l, int r) {
			super();
			this.l = l;
			this.r = r;
		}

		public LayoutInfo() {
			super();
			// TODO Auto-generated constructor stub
		}

		public int l = -1;
		public int r = -1;

		public int t = -1;
		public int b = -1;
		public int rl = -1;
		public int rr = -1;

		public int rt = -1;
		public int rb = -1;
		public int getL() {
			return l;
		}

		
		public int getRl() {
			return rl;
		}

		public LayoutInfo setRl(int rl) {
			this.rl = rl;return this;
		}

		public int getRr() {
			return rr;
		}

		public LayoutInfo setRr(int rr) {
			this.rr = rr;return this;
		}

		public int getRt() {
			return rt;
		}

		public LayoutInfo setRt(int rt) {
			this.rt = rt;return this;
		}

		public int getRb() {
			return rb;
		}

		public LayoutInfo setRb(int rb) {
			this.rb = rb;return this;
		}

		public LayoutInfo setLeft(int l) {
			this.l = l;
			return this;
		}

		public int getR() {
			return r;
		}

		public LayoutInfo setRight(int r) {
			this.r = r;
			return this;
		}

		public int getT() {
			return t;
		}

		public LayoutInfo setTop(int t) {
			this.t = t;
			return this;
		}

		public int getB() {
			return b;
		}

		public LayoutInfo setBottom(int b) {
			this.b = b;
			return this;
		}

	}

	public Map<View, LayoutInfo> view_layoutInfo = new HashMap<View, LayoutInfo>();

	public boolean inAni() {
		return view_layoutInfo.size() > 0;
	}

	public void addAniStateInfo(int id, LayoutInfo layoutInfo) {
		view_layoutInfo.put(findViewById(id), layoutInfo);
	}
	public void addAniStateInfo(View view, LayoutInfo layoutInfo) {
		view_layoutInfo.put(view, layoutInfo);
	}
	public void clearAniState() {
		view_layoutInfo.clear();
	}

	public AniLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		// TODO Auto-generated method stub
		super.onLayout(changed, l, t, r, b);
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			View view = getChildAt(i);
			LayoutInfo layoutInfo = view_layoutInfo.get(view);
			if (layoutInfo != null) {
				if (layoutInfo.l != -1) {
					view.setLeft(layoutInfo.l);
				}
				if (layoutInfo.r != -1) {
					view.setRight(layoutInfo.r);
				}
				if (layoutInfo.t != -1) {
					view.setTop(layoutInfo.t);
				}
				if (layoutInfo.b != -1) {
					view.setBottom(layoutInfo.b);
				}
			}
		}
	}

}
