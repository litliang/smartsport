package app.base.task;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import android.app.Activity;
import app.base.ActivityPool;
import app.base.MapAdapter.AdaptInfo;
import app.base.MapAdapter.ContainerInfo;

/** 
 * 包装了 PreHandler BackRunnable UICallback
 * @author Administrator
 *
 */
public class Compt {
	public PreHandler uiPreHandler;
	public BackRunnable backrunnable;
	public UICallback uiCallback;
	Activity preActivity;
	Activity callbackActivity;
	public String strPreActivity;
	public String strCallbackActivity;
	Listener listener;
	public Compt postCompt;

	public Compt getPostCompt() {
		return postCompt;
	}

	public Compt setPostCompt(Compt postCompt) {
		this.postCompt = postCompt;
		return this;
	}

	public Compt() {
		super();
	}

	public Map<String, BackRunnable> backs = new HashMap<String, BackRunnable>();
	public Map<String, CallbackRunnable> callbacks = new HashMap<String, CallbackRunnable>();

	public Map<BackRunnable, CallbackRunnable> taskMap = new TreeMap<BackRunnable, CallbackRunnable>(
			new Comparator<BackRunnable>() {

				@Override
				public int compare(BackRunnable object1, BackRunnable object2) {
					// TODO Auto-generated method stub
					return object1.seq - object2.seq;
				}

			});
	int seq = -1;


	BackRunnable runnable;
	CallbackRunnable callbackRunnable;

	public CallbackRunnable getCallbackRunnable() {
		return callbackRunnable;
	}

	public void setCallbackRunnable(CallbackRunnable callbackRunnable) {
		this.callbackRunnable = callbackRunnable;
	}

	public BackRunnable getBackrunnable() {
		return backrunnable;
	}

	public Compt putTask(BackRunnable runnable, CallbackRunnable uicallback) {
		this.runnable = runnable;
		this.callbackRunnable = uicallback;
		runnable.setCompt(this);
		uicallback.setBackRunnable(runnable);
		backs.put(runnable.getId(), runnable);
		callbacks.put(uicallback.getId(), uicallback);
		runnable.clbcks.add(uicallback);
		uicallback.bkRunnables.add(runnable);
		uicallback.setRunnableActivity();
		uicallback.setCompt(this);
		return this;
	}

	public void rerun() {
		run(1, 2);
	}

	public Compt putTask(String tag, UICallback uicallback) {
		putTask(backs.get(tag), uicallback);
		return this;
	}

	public Compt putTask(BackRunnable runnable, String tag) {
		putTask(runnable, callbacks.get(tag));
		return this;
	}

	public Task getTask(int tag, boolean isBack) {
		return isBack ? backs.get(tag + "") : callbacks.get(tag + "");
	}

	public void init() {
		strPreActivity = getPreHandleActivityIdty();
		strCallbackActivity = getCallbackActivityIdty();
		if (strPreActivity != null) {
			setPreActivity(ActivityPool.getInstance().getActivity(
					strPreActivity));
		}
		if (getCallbackActivity() == null && strCallbackActivity != null) {

			setCallbackActivity(ActivityPool.getInstance().getActivity(
					strCallbackActivity));
		}
		if (uiPreHandler == null) {
			setPreHandler(createUIPreHandlerInstance());
		}
		if (backrunnable == null) {
			setBackrunnable(createBackRunnableInstance());
		}

		if (uiCallback == null) {
			setUiCallback(createUICallbackInstance());
		}
		if (getUiPreHandler() != null) {
			if (getUiPreHandler().aty == null) {
				getUiPreHandler().setPreAty(getPreActivity());
			}
			getUiPreHandler().setCompt(this);
			// tasks.put(0, getUiPreHandler());
		}
		// tasks.put(1, getBackrunnable());
		if (getUiCallback() != null) {
			if (getUiCallback().getActivity() == null) {
				getUiCallback().setActivity(getCallbackActivity());
			}
			getUiCallback().setCompt(this);
			// tasks.put(2, getUiCallback());
		}
		if (getBackRunnable() != null) {
			getBackRunnable().setCompt(this);
		}

	}

	public PreHandler createUIPreHandlerInstance() {
		return null;
	};

	public BackRunnable createBackRunnableInstance() {
		return null;
	};

	public UICallback createUICallbackInstance() {
		return null;
	};

	public String getPreHandleActivityIdty() {
		return null;
	}

	public String getCallbackActivityIdty() {
		return null;
	}

	public Activity getPreActivity() {
		return preActivity;
	}

	public void setPreActivity(Activity preActivity) {
		this.preActivity = preActivity;
	}

	public Activity getCallbackActivity() {
		return callbackActivity;
	}

	public Compt setCallbackActivity(Activity callbackActivity) {
		this.callbackActivity = callbackActivity;
		return this;
	}

	public PreHandler getUiPreHandler() {
		return uiPreHandler;
	}

	public Compt setPreHandler(PreHandler uiPreHandler) {
		this.uiPreHandler = uiPreHandler;
		return this;
	}

	public BackRunnable getBackRunnable() {
		return backrunnable;
	}

	public void setBackrunnable(BackRunnable backrunnable) {
		this.backrunnable = backrunnable;
	}

	public UICallback getUiCallback() {
		return uiCallback;
	}

	public void setUiCallback(UICallback uiCallback) {
		this.uiCallback = uiCallback;
		if(getPostCompt()!=null){
			this.uiCallback.setContinueCompt(getPostCompt());
		}
	}

	public Map<String, Object> backRunnable_Object = new HashMap<String, Object>();

	boolean firstFlag;

	public void run() {
		run(new Integer[0]);
	}

	public void run(Integer... sequence) {//0,1,2 

		try {
			if (sequence == null) {
				sequence = new Integer[0];
			}
			List values = Arrays.asList(sequence);
			init();
			// }
			if (getUiPreHandler() != null
					&& (values.size() == 0 || values.contains(new Integer(0)))) {

				try {
					getUiPreHandler().preRun();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//old uiCallback
			
			if (getUiCallback() != null && getBackRunnable() != null
					&& backs.size() == 0) {
				setBackrunnable(getBackRunnable());
				setUiCallback(getUiCallback());

				innerRun(sequence);
				return;
			}
			//new BackRunnable
			BackRunnable back;
			CallbackRunnable callbk;
			for (BackRunnable runnable : backs.values()) {
				back = runnable;
				if (back.clbcks.size() > 1) {
					for (CallbackRunnable uicallback : back.clbcks) {
						callbk = uicallback;
						setBackrunnable(back);
						setUiCallback(null);
						innerRun(sequence);
					}
				} else if (back.clbcks.size() == 1) {

					CallbackRunnable callback = runnable.clbcks.get(0);
					List<BackRunnable> backRunnables = callback.bkRunnables;
					for (BackRunnable rnnble : backRunnables) {
						back = rnnble;
						callbk = callback;
						setBackrunnable(back);
						setUiCallback(null);
						innerRun(sequence);

					}
				} else {
					setBackrunnable(back);
					setUiCallback(null);
					innerRun(sequence);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void innerRun(Integer... sequence) {
		try {
			if (sequence == null) {
				sequence = new Integer[0];
			}
			List values = Arrays.asList(sequence);
			// if (!firstFlag) {
			// firstFlag = true;

			UICallback callback;
			if (getUiCallback() != null
					&& (values.size() == 0 || values.contains(new Integer(2)))) {
				callback = getUiCallback();
			} else {
				callback = new UICallback()
						.addAllCallbackRunnable(this.callbacks.values());
			}
			callback.setRunnableActivity();
			if (callback.getActivity() == null) {
				callback.setActivity(callback.getAty());
			}

			callback.setCompt(this);
			BackRunnable backrunnable;
			if (getBackRunnable() != null
					&& (values.size() == 0 || (values.contains(new Integer(1))))) {
				backrunnable = getBackRunnable();

			} else {
				backrunnable = new BackRunnable() {

					@Override
					public void run() throws Exception {
					}

				};
				if (getBackRunnable() != null) {
					backrunnable.setObject(getBackRunnable().object);
				}
			}
			if (getBackRunnable() != null) {
				AsyncClient.request(backrunnable, callback);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean finished;

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

}
