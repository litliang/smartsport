package app.base.framework;

import android.app.Application;
import android.content.Context;
import app.base.DipUtil;
import app.base.MapConf;
import app.base.SPrefUtil;
import app.base.action.Epr;
import app.base.task.AsyncClient;
import top.smartsport.www.R;

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

		MapConf.initDefaultInmg(R.mipmap.default_img);
		SPrefUtil.iniContext(this);
		
		DipUtil.initCtx(this);
		Epr.setCtx(this);
	}

	


}
