/*
    Android Asynchronous Http Client
    Copyright (c) 2011 James Smith <james@loopj.com>
    http://loopj.com

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
 */

package app.base.task;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import app.base.framework.Init;
import org.apache.http.HttpEntity;

/**
 * 发放线程的类
 * @author Administrator
 *
 */
public class AsyncClient {
	public static AsyncClient client;
	public static Map<Callback, Handler> handlerPool = new HashMap<Callback, Handler>();

	public static AsyncClient getInstane() {
		if (client == null) {
			client = new AsyncClient();
		}
		return client;
	}

	// private static final int DEFAULT_MAX_CONNECTIONS = 10;

	/** 锟斤拷锟斤拷锟斤拷锟斤拷叱坛锟� */
	private ThreadPoolExecutor threadPool;
	/** 锟斤拷锟斤拷锟組ap锟斤拷key为锟斤拷锟斤拷锟斤拷锟斤拷锟揭筹拷锟� */
	private Map<Context, List<WeakReference<Future<?>>>> requestMap;

	/**
	 * Creates a new AsyncClient.
	 */
	public AsyncClient() {

		threadPool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
		requestMap = new ConcurrentHashMap<Context, List<WeakReference<Future<?>>>>();

	}

	/**
	 * Overrides the threadpool implementation used when queuing/pooling
	 * requests. By default, Executors.newCachedThreadPool() is used.
	 * 
	 * @param threadPool
	 *            an instance of {@link ThreadPoolExecutor} to use for
	 *            queuing/pooling requests.
	 */
	public void setThreadPool(ThreadPoolExecutor threadPool) {
		// this.threadPool = threadPool;
	}

	/**
	 * Cancels any pending (or potentially active) requests associated with the
	 * passed Context.
	 * <p>
	 * <b>Note:</b>
	 * 锟斤拷锟斤拷锟斤拷锟街伙拷锟接帮拷斓斤拷锟斤拷玫锟斤拷歉锟絚ontext锟斤拷锟斤拷锟斤拷锟节诧拷锟斤拷锟斤拷要锟斤拷时锟斤拷锟斤拷茫锟斤拷锟斤拷锟
	 * � onstop ondestroy
	 * 
	 * @param context
	 *            the android Context instance associated to the request.
	 * @param mayInterruptIfRunning
	 *            specifies if active requests should be cancelled along with
	 *            pending requests.
	 */
	public void cancelRequests(Context context, boolean mayInterruptIfRunning) {
		List<WeakReference<Future<?>>> requestList = requestMap.get(context);
		if (requestList != null) {
			for (WeakReference<Future<?>> requestRef : requestList) {
				Future<?> request = requestRef.get();
				if (request != null) {
					request.cancel(mayInterruptIfRunning);
				}
			}
		}
		requestMap.remove(context);
	}

	/**
	 * 取锟斤拷锟斤拷锟叫碉拷锟斤拷锟斤拷
	 * */
	public void cancelAll() {
		Collection<List<WeakReference<Future<?>>>> requestList = requestMap
				.values();
		if (requestList != null) {
			Iterator<List<WeakReference<Future<?>>>> requestList2 = requestList
					.iterator();
			if (requestList2.hasNext()) {
				List<WeakReference<Future<?>>> list = requestList2.next();
				if (list != null) {
					for (WeakReference<Future<?>> requestRef : list) {
						Future<?> request = requestRef.get();
						if (request != null) {
							request.cancel(true);
						}
					}
				}
			}
		}
		try {
			threadPool.shutdownNow();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		threadPool = null;
		requestMap.clear();
		requestMap = null;
	}

	public static Set<HttpEntity> httpEntities = new HashSet<HttpEntity>();

	public void sendReq(BackRunnable runningunit, UICallback uicallback) {

		
		AsyncRequest asyncHttpRequest = new AsyncRequest(runningunit,
				uicallback);
		//java api
		Future<?> request = threadPool.submit(asyncHttpRequest);

		if (uicallback != null && uicallback.getActivity() != null) {
			Context context = uicallback.getActivity();
			// Add request to request map
			List<WeakReference<Future<?>>> requestList = requestMap
					.get(context);
			if (requestList == null) {
				requestList = new LinkedList<WeakReference<Future<?>>>();
				requestMap.put(context, requestList);
			}
			requestList.add(new WeakReference<Future<?>>(request));
			// TODO: Remove dead weakrefs from requestLists?
		}
	}

	public static boolean request(BackRunnable param, UICallback uicallback) {
		Init.asyncClient.sendReq(param, uicallback);
		return false;
	}
	//houtai
	public class AsyncRequest implements Runnable {

		private Activity context;
		BackRunnable backRunnable;
		UICallback uicallback;

		public AsyncRequest(BackRunnable backRunnable, UICallback uicallback) {
			this.backRunnable = backRunnable;
			this.uicallback = uicallback;
			if (uicallback != null) {
				if (this.uicallback.getBackRunnable() == null) {
					this.uicallback.setBackRunnable(backRunnable);
				}
			}
		}

		@Override
		public void run() {
			threadRun();
		}

		int id;

		public void threadRun() {
			Exception ex = null;

			try {

				backRunnable.onStart();
				backRunnable.run();
				// response = post(toFormat);
			} catch (Exception e) {
				ex = e;
				
				if (uicallback == null) {
					uicallback = new UICallback();
				}
				uicallback.setError(true);
			} finally {
				if (ex != null) {
					backRunnable.onFailure(-1, ex, null);
				} else {
					backRunnable.onSuccess(getId(), "");
					backRunnable.onFinish();
				}
				if (uicallback != null) {
					Handler handler = new Handler(Looper.getMainLooper(),
							uicallback);
					handlerPool.put(uicallback, handler);
					handler.sendEmptyMessage(0);

				}

			}
		}

		public int getId() {
			return id;
		}

	}
}
