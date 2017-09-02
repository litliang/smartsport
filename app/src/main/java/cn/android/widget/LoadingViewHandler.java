package cn.android.widget;


import android.app.ProgressDialog;
import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import top.smartsport.www.R;

public class LoadingViewHandler {
	private static ProgressDialog dlg;
	public static ProgressDialog creteProgressDialog(Context context,
			String text) {
		//final ProgressDialog dlg = new ProgressDialog(context);
		dlg = new ProgressDialog(context);
		dlg.show();
		dlg.setContentView(R.layout.loading);

		LinearLayout root = (LinearLayout) dlg.findViewById(R.id.loading_layout);
		root.setGravity(android.view.Gravity.CENTER);

		LoadingView mLoadView = new LoadingView(context);
		mLoadView.setDrawableResId(R.mipmap.loading);
		root.addView(mLoadView);
		TextView alert = new TextView(context);
		alert.setText(text);
		root.addView(alert);
		return dlg;
	}
	
	public static void dismiss() {
		dlg.dismiss();
	}
}
