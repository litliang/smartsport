package app.base.task;

import android.app.Activity;

/**
 * ui线程0任务  基类
 * @author Administrator
 *
 */
public abstract class PreHandler extends Task{
	public Activity aty;
	public void preRun() throws Exception{
		run(aty);
	};
	
	public Activity getPreAty() {
		return aty;
	}
	public PreHandler setPreAty(Activity aty) {
		this.aty = aty;
		return this;
	}
	public abstract void run(final Activity aty) throws Exception ;
	public String getPreHandleActivityIdty(){
		return null;
		
	};
}
