package app.base.task;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Message;
import app.base.ActivityPool;

/**
 * 新的回调任务  可以放多个CallbackRunnable到compt
 * @author Administrator
 *
 */
public abstract class CallbackRunnable extends Task {
	public List<BackRunnable> bkRunnables = new ArrayList<BackRunnable>(0);

	public abstract boolean run(Message msg, boolean error, Activity aty) throws Exception;

	public BackRunnable backRunnable;

	public BackRunnable getBackRunnable() {
		return backRunnable;
	}

	public void setBackRunnable(BackRunnable backRunnable) {
		this.backRunnable = backRunnable;
	}

	public boolean error;
	protected Activity activity;

	public CallbackRunnable setActivity(Activity activity) {
		this.activity = activity;
		return this;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setRunnableActivity() {
		// TODO Auto-generated method stub
		if (atycz != null) {
			setRunnableActivity(ActivityPool.getInstance().getActivity(atycz));
		}
	}

	Class atycz;

	public CallbackRunnable setActivityCz(Class activity) {
		// TODO Auto-generated method stub
		atycz = activity;
		return this;
	}

	public CallbackRunnable setRunnableActivity(Activity activity) {
		this.activity = activity;
		return this;
	}

	public boolean runErrorReport(Message msg, boolean error, Activity aty,
			Exception ex) {
		return false;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

}
