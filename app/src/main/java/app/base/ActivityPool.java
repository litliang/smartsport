/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.base;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;

/**
 * activity池
 * 对activity做一些简单操作
 * @author Administrator
 */
public class ActivityPool {

	public Map<String, Activity> activityMap = new HashMap<String, Activity>();
	private static ActivityPool instance;

	protected ActivityPool() {
	}

	public static Activity currAty;

	public static ActivityPool getInstance() {
		if (null == instance) {
			instance = new ActivityPool();
		}
		return instance;
	}

	public void closeActivity(Class activiyClass) {
	Activity activity =getActivity(activiyClass); 	
	if(activity!=null){
		activity.finish();
		}
	}
	public void addActivity(Activity activity) {
		addActivity(activity.getClass().getName(), activity);
	}

	public void addActivity(String atysign, Activity activity) {
		activityMap.put(atysign, activity);
		currAty = activity;

	}

	public Activity getActivity(Class atysign) {
		if(atysign==null){
			return null;
		}
		return activityMap.get(atysign.getName());

	}

	public Activity getActivity(String atysign) {
		return activityMap.get(atysign);

	}

}
