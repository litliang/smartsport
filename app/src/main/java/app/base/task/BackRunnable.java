package app.base.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import app.base.Logs;

/**
 * 后台任务   extends Task
 * @author Administrator
 *
 */
public abstract class BackRunnable extends Task {

	public List<CallbackRunnable> clbcks = new ArrayList<CallbackRunnable>(0);
	public Object object;
	public boolean error;
	public Activity activity;
	
	

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		return super.clone();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	public BackRunnable() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BackRunnable(Task backRunnable) {
		super(backRunnable);
		// TODO Auto-generated constructor stub
	}

	public abstract void run() throws Exception;

	public void onSuccess(int reqid, String reqRoot) {
		Logs.d("===== onSuccess roaded [reqid: " + reqid + " reqRoot: "
				+ reqRoot + "]");
	}

	public void onStart() {

		Logs.d("===== onStart roaded");
	}

	public void onFinish() {
		Logs.d("===== onFinish roaded");

	}

	public void onFailure(int statusCode, Throwable error, Map map) {
	}

	public Object getObject() {
		return object;
	}

	public BackRunnable setObject(Object object) {
		this.object = object;
		return this;
	}
}
