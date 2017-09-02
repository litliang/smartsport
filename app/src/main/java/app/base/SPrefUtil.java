/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package app.base;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

/**
 * sharedprefence 封装类
 * @author chenliang
 */
public class SPrefUtil {
	public String copyName = "default";

	public String getCopyName() {
		return copyName;
	}

	public void setCopyName(String copyName) {
		this.copyName = copyName;
	}

	static Map<String, Class> key_type = new HashMap<String, Class>();
	static Map<String, SharedPreferences> preferences = new HashMap<String, SharedPreferences>();
	private static Context context;

	public static SharedPreferences onOpen(Context context, String spname) {
		if (!preferences.containsKey(spname)) {
			preferences.put(spname, context.getSharedPreferences(spname, 0));
		}
		return preferences.get(spname);
	}

	public void putValue(String key, Object value) {
		putValue(context, this.copyName, key, value);
	}

	public static void putValue(Context context, String spname, String key,
			Object value) {
		SharedPreferences.Editor spedit = onOpen(context, spname).edit();
		if (value == null) {
			spedit.putString(key, null);
		} else if (value instanceof Boolean) {
			spedit.putBoolean(key, (Boolean) value);
		} else if (value instanceof Integer) {
			spedit.putInt(key, (Integer) value);
		} else if (value instanceof String) {
			spedit.putString(key, (String) value);
		} else if (value instanceof Float) {
			spedit.putFloat(key, (Float) value);
		} else if (value instanceof Long) {
			spedit.putLong(key, (Long) value);
		}
		key_type.put(key, value.getClass());
		spedit.commit();
	}

	public static Context getContext() {
		return context;
	}

	public static void iniContext(Application context) {
		SPrefUtil.context = context.getApplicationContext();
	}

	public Object getValue(String key) {
		return getValue(this.getCopyName(), key, key_type.containsKey(key)?key_type.get(key):String.class);
	}
	
	public static Object getValue(String spname, String key, Class clazz) {
		if (clazz == null) {
			new Exception("clazz is null").printStackTrace();
		}
		SharedPreferences sp = onOpen(context, spname);
		if (clazz == Boolean.class) {
			return sp.getBoolean(key, false);
		} else if (clazz == Integer.class) {
			return sp.getInt(key, -1);
		} else if (clazz == String.class) {
			return sp.getString(key, "");
		} else if (clazz == Float.class) {
			return sp.getFloat(key, -1);
		} else if (clazz == Long.class) {
			return sp.getLong(key, -1);
		}
		return "";
	}

	public static void clear(String spname) {
		if (preferences.containsKey(spname)) {
			SharedPreferences sp = preferences.remove(spname);
			sp.edit().clear();
			sp.edit().commit();
		}
	}
}
