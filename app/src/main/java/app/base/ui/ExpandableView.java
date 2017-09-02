package app.base.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class ExpandableView extends LinearLayout {
	int extid;
	int topid;
	public ExpandableView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		int id = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/com.benlai.shopping", "extptid", -1);
		setExtId(id);
		int topid = attrs.getAttributeResourceValue("http://schemas.android.com/apk/res/com.benlai.shopping", "topbarid", -1);
		setTopId(topid);
		
		
	}

	private void setTopId(int topbar) {
		// TODO Auto-generated method stub
		this.topid = topbar;
	}

	public void setExtId(int id) {
		this.extid = id;
	}
	public void expand(){
		View view = findViewById(extid);
		if(view!=null){
			view.setVisibility(View.VISIBLE);
		}
	}
	public void collapse(){
		View view = findViewById(extid);
		if(view!=null){
			view.setVisibility(View.GONE);
		}
	}
	
	public boolean toggle(){
		View view = findViewById(extid);
		if(view!=null){
			if(view.getVisibility()==View.GONE){
				view.setVisibility(View.VISIBLE);
				return true;
			}else{
				view.setVisibility(View.GONE);
				return false;
			}
		}
		return false;
	}
	
	
	
}
