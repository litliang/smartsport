package app.base.task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.os.Handler.Callback;
import android.os.Message;
import app.base.ActivityPool;

/**
 * 老的UI回调
 * @author Administrator
 *
 */
public class UICallback extends CallbackRunnable implements Callback {

	public List<CallbackRunnable> cbrunnables = new ArrayList<CallbackRunnable>(
			0);
	private Activity aty;
	public int id;
	public BackRunnable backRunnable;
	public boolean backThrowError;
	public Exception exception;

	public Activity getAty() {
		return aty;
	}

	public void setAty(Activity aty) {
		this.aty = aty;
	}

	public boolean isError() {
		return backThrowError;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public UICallback addAllCallbackRunnable(
			Collection<CallbackRunnable> callbackRunnables) {
		cbrunnables.addAll(callbackRunnables);
		return this;
	}

	public UICallback addCallbackRunnable(CallbackRunnable callbackRunnable) {
		cbrunnables.add(callbackRunnable);
		return this;
	}

	public void setError(boolean error) {
		this.backThrowError = error;
	}

	public UICallback setActivity(Activity aty) {
		if (aty == null) {
			return this;
		}
		super.setActivity(aty);
		this.aty = aty;
		return this;
	}

	public BackRunnable getBackRunnable() {
		return backRunnable;
	}

	public void setBackRunnable(BackRunnable backRunnable) {
		this.backRunnable = backRunnable;
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		boolean isSuccess = false;
		try {
			isSuccess = callbackRunwithError(msg, backThrowError, activity,
					exception);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AsyncClient.handlerPool.remove(this);
		return isSuccess;
	}

	public boolean callbackRunwithError(Message msg, boolean error,
			final Activity aty, Exception e) throws Exception {
		if (this.cbrunnables.size() == 0) {
			if (error) {
				runErrorReport(msg, error, aty, e);
			} else
				run(msg, error, aty);
		} else {
			for (int i = 0; i < cbrunnables.size(); i++) {

				Activity ctx = cbrunnables.get(i).getActivity() != null ? cbrunnables
						.get(i).getActivity() : aty;
				ctx = ctx != null ? ctx : ActivityPool.getInstance()
						.getActivity(cbrunnables.get(i).atycz);
				if (error) {
					cbrunnables.get(i).runErrorReport(msg, error, ctx, e);
				} else
					cbrunnables.get(i).run(msg, error, ctx);

			}
		}
		setError(false);
		if (getContinueCompt() != null) {
			getContinueCompt().run();
		}
		return false;

	}

	@Override
	public boolean run(Message msg, boolean error, Activity aty) {
		return error;
		// TODO Auto-generated method stub

	};

	@Override
	public boolean runErrorReport(Message msg, boolean error, Activity aty,
			Exception ex) {
		return error;
		// TODO Auto-generated method stub
	};

	Compt continueCompt;

	public Compt getContinueCompt() {
		return continueCompt;
	}

	public void setContinueCompt(Compt postCompt) {
		this.continueCompt = postCompt;
	}

}
