package app.base.ui;

import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import app.base.DipUtil;

public class UISeriesLayout extends LinearLayout {
	List<String> arrays;

	public UISeriesLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		String oriention = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/android", "orientation");
		if ((oriention == null || oriention.equals(""))
				|| oriention.toLowerCase().equals("0")) {
			this.setOrientation(LinearLayout.HORIZONTAL);
		} else if (oriention.toLowerCase().equals("1")) {
			this.setOrientation(LinearLayout.VERTICAL);
		}
		String seq = attrs
				.getAttributeValue(
						"http://schemas.android.com/apk/res/com.benlai.shopping",
						"seq");
		if (seq != null && !seq.isEmpty()) {
			String[] array = seq.split(",");
			arrays = Arrays.asList(array);
		}
	}

	int i;

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		// TODO Auto-generated method stub
		LayoutParams lym = super.generateLayoutParams(attrs);
		if (arrays == null) {
			return lym;
		}
		String len = arrays.get(i++).trim();
		int pixels;
		if (!len.toLowerCase().equals("x")) {
			if (len.endsWith("dip")) {
				len = len.replaceAll("dip", "");
				pixels = DipUtil.fromDip(Float.parseFloat(len));
				getPixels(lym, pixels);
			} else if (len.endsWith("px")) {
				len = len.replaceAll("px", "");
				pixels = Integer.parseInt(len);
				getPixels(lym, pixels);
			}
				lym.weight = Float.parseFloat(len);
				if (this.getOrientation() == LinearLayout.HORIZONTAL) {
					lym.width = 0;
					lym.height = LayoutParams.FILL_PARENT;
				} else if (this.getOrientation() == LinearLayout.VERTICAL) {
					lym.height = 0;
					lym.width = LayoutParams.FILL_PARENT;
				}
			
		}
		return lym;
	}

	void getPixels(LayoutParams lym, int pixels) {
		if (lym.width == 0) {
			lym.width = pixels;
		} else {
			lym.height = pixels;
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		i = 0;
		super.onLayout(changed, l, t, r, b);

	}

}
