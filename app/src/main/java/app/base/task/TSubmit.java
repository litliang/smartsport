package app.base.task;

import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import android.content.Context;
import android.os.Message;
import app.base.JsonUtil;

/**
 * 判断数据返回是否正确
 * 返回0：正确
 * @author Administrator
 *
 */
public abstract class TSubmit extends CallbackRunnable {
	public Map<String, String> respCmp = new TreeMap<String, String>();

	@Override
	public boolean run(Message msg, boolean error, Activity aty) {
		boolean success = true;
		for (String str : respCmp.keySet()) {
			String exp = JsonUtil.findJsonLink(str,
					this.getBackRunnable().object.toString()).toString();

			if (!exp.equals(respCmp.get(str))) {
				success = false;
				ifUnSuccess(aty);
			}

		}
		if (success) {
			ifSuccess(aty);
		}
		after(success, this.getBackRunnable().object.toString(), aty);
		return true;
	}

	public abstract void after(boolean success, String string, Activity aty);

	public void ifSuccess(Context aty) {

	}

	public void ifUnSuccess(Context aty) {

	}

	String data;
	String resp;

	public TSubmit(String data, String resp, Context activity) {
		super();
		respCmp.put(data, resp);
		if (activity instanceof Activity) {
			setActivity(((Activity) activity));
		}
	}

}
