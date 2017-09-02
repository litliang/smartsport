package app.base.framework;

import android.app.Application;
import android.content.Context;
import app.base.DipUtil;
import app.base.SPrefUtil;
import app.base.task.AsyncClient;

/***
 * android 入口全局类
 * @author Administrator
 *
 */
public class Init extends Application {
	// start: Enviroment market
	public static AsyncClient asyncClient;
	public static Context bigContext;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		asyncClient = AsyncClient.getInstane();
		
		bigContext = this;

		
		SPrefUtil.iniContext(this);
		
		DipUtil.initCtx(this);
	}

	


}
