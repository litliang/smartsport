package app.base.framework;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.TreeSet;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.text.SpannableStringBuilder;
import android.util.Log;
import app.base.ActivityPool;
import app.base.Cache;
import app.base.DialogUtil;
import app.base.DialogUtil.DialogInfo;
import app.base.Logs;
import app.base.ui.highlight.TextHighLightDecorator;


/**
 * 错误处理类
 */
public class CrashHandler implements UncaughtExceptionHandler {
	/** Debug Log tag */
	public static final String TAG = "CrashHandler";
	/**
	 * */
	public static final boolean DEBUG = true;
	//系统默认的UncaughtException处理类 
	private UncaughtExceptionHandler mDefaultHandler;
	 //CrashHandler实例  
	private static CrashHandler INSTANCE;
	//程序的Context对象
	private Context mContext;
//用来保存配置信息
	private Properties mDeviceCrashInfo = new Properties();
	private static final String VERSION_NAME = "versionName";
	private static final String VERSION_CODE = "versionCode";
	private static final String STACK_TRACE = "STACK_TRACE";
	private static final String CRASH_REPORTER_EXTENSION = ".cr";
	private boolean debug = true;

	public CrashHandler() {
	}

	public static CrashHandler getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new CrashHandler();
		}
		return INSTANCE;
	}

	/**
	 * 
	 * @param ctx
	 */
	public void init(Context ctx) {
		mContext = ctx;
		//获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 
	 * @param ex
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return true;
		}
		final String msg = ex.getLocalizedMessage();
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				// Toast.LENGTH_LONG)
				// .show();
				Looper.loop();
			}

		}.start();
		collectCrashDeviceInfo(mContext);
		String crashFileName = saveCrashInfoToFile(ex);
		sendCrashReportsToServer(mContext);
		return true;
	}

	/**
	 */
	public void sendPreviousReportsToServer() {
		sendCrashReportsToServer(mContext);
	}

	/**
	 * 
	 * @param ctx
	 */
	private void sendCrashReportsToServer(Context ctx) {
		String[] crFiles = getCrashReportFiles(ctx);
		if (crFiles != null && crFiles.length > 0) {
			TreeSet<String> sortedFiles = new TreeSet<String>();
			sortedFiles.addAll(Arrays.asList(crFiles));

			for (String fileName : sortedFiles) {
				File cr = new File(ctx.getFilesDir(), fileName);
				postReport(cr);
				cr.delete();
			}
		}
	}

	private void postReport(File file) {
	}

	/**
	 * 
	 * @param ctx
	 * @return
	 */
	private String[] getCrashReportFiles(Context ctx) {
		File filesDir = ctx.getFilesDir();
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(CRASH_REPORTER_EXTENSION);
			}
		};
		return filesDir.list(filter);
	}

	/**
	 * 
	 * @param ex
	 * @return
	 */
	private String saveCrashInfoToFile(Throwable ex) {
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		ex.printStackTrace(printWriter);

		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}

		String result = info.toString();
		printWriter.close();
		mDeviceCrashInfo.put(STACK_TRACE, result);

		try {
			long timestamp = System.currentTimeMillis();
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				File mk = new File("/sdcard/Jibo/Error/");
				if (!mk.exists()) {
					mk.mkdirs();
				}
				String fileName = "/sdcard/Jibo/Error/" + timestamp + ".log";
				File ErrorLog = new File(fileName);
				if (!ErrorLog.exists()) {
					ErrorLog.createNewFile();
				}
				FileWriter fw = new FileWriter(ErrorLog, true);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write((new Date(timestamp)).toLocaleString() + "\r\naaaa"
						+ ex.getLocalizedMessage() + "\r\nbbbbbb:"
						+ mDeviceCrashInfo.getProperty(STACK_TRACE));
				bw.close();
				fw.close();
				return fileName;
			}
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing report file...", e);
		}
		return null;
	}

	/**
	 * 
	 * @param ctx
	 */
	public void collectCrashDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				mDeviceCrashInfo.put(VERSION_NAME,
						pi.versionName == null ? "not set" : pi.versionName);
				mDeviceCrashInfo.put(VERSION_CODE, pi.versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "Error while collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();//getDeclaredFields()只能获取自己声明的各种字段，包括public，protected，private
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mDeviceCrashInfo.put(field.getName(), field.get(null));
				if (DEBUG) {
					Log.d(TAG, field.getName() + " : " + field.get(null));
				}
			} catch (Exception e) {
				Log.e(TAG, "Error while collect crash info", e);
			}
		}
	}

	@SuppressWarnings("static-access")
	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		if (debug) {
			String errorMsg = "界面： "
					+ ActivityPool.currAty.getClass().getSimpleName() + "\n";
			errorMsg += "原因：" + arg1.getMessage() + "\n" + "\n\n";
			try {
				StackTraceElement[] eles = arg1.getStackTrace();
				for (int i = 0; i < eles.length; i++) {
					errorMsg += i + 1 + "：" + eles[i].toString() + "\n";
				}
				Cache.getInstance().putValue(Cache.getInstance().KEY_error,
						errorMsg);

			} catch (Exception e) {
				// TODO Auto-generated catch block

			}
		}
		System.exit(0);
	}

	public static void errorDialog() {
		final String out = Cache.getInstance().getValue(Cache.KEY_error)
				.toString();
		Logs.i("---------- out " + out);
		if (out.equals("")) {
			return;
		}
		SpannableStringBuilder spannable = new TextHighLightDecorator(
				Color.YELLOW).setMatcher(Init.bigContext.getPackageName())
				.getDecorated(out, out, out);
		DialogInfo dialogInfo = new DialogInfo(ActivityPool.currAty);
		dialogInfo.aty = ActivityPool.currAty;
		dialogInfo.message = spannable;
		dialogInfo.negativeButtonText = "发送";
		dialogInfo.negativeButtonClickListener = new DialogUtil.DialogClicker() {

			@Override
			public void onClick(DialogInterface dialog, int arg1) {
				// TODO Auto-generated method stub
				if (out != null && !out.equals("")) {
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.putExtra(Intent.EXTRA_TEXT, new String(out));
					intent.setType("text/plain");
					Init.bigContext.startActivity(Intent.createChooser(intent,
							"分享").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
				}
				super.onClick(dialog, arg1);

			}
		};
		DialogUtil.showChoiceDialog(dialogInfo, true);
		Cache.getInstance().putValue(Cache.KEY_error, "");
	}
}