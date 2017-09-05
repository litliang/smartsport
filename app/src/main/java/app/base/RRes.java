package app.base;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

import android.content.Context;

/**
 * @author chenliang
 */
public class RRes {
	public static final String[] initType = new String[] {  "layout" ,"id","string","drawable","color"};
	public static Class r;
	public static Map<String, TypeAttrs> resType = new TreeMap<String, TypeAttrs>(
			ComparatorRepo.stringKey);
	public static final String styleable = "styleable";

	public static Map<Integer, String> getAttrValue_itsname() {
		return attrValue_itsname;
	}

	public static void setAttrValue_itsname(
			Map<Integer, String> attrValue_itsname) {
		RRes.attrValue_itsname = attrValue_itsname;
	}

	public static Map<String, String> resReg = new TreeMap<String, String>(
			ComparatorRepo.stringKey);

	static {
		// init_Theme(Environment.getExternalStorageDirectory().getAbsolutePath()
		// + "/Jibo/testText.txt");
		// test
	}
	public static Map<Integer, String> attrValue_itsname = new TreeMap<Integer, String>(
			ComparatorRepo.intKey);

	public static class ResObject {
		public String name;
		public String simplename;
		public int androidValue;

		public ResObject(Integer value) {
			this.androidValue = value;
		}

		public int getAndroidValue() {
			return androidValue;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getSimplename() {
			return simplename;
		}

		public void setSimplename(String simplename) {
			this.simplename = simplename;
		}

	}

	public static class TypeAttrs {
		public Class type;
		public Map<String, ResObject> attrMaps = new TreeMap<String, ResObject>(
				ComparatorRepo.stringKey);

	}

	// public static void clearTheme() {
	// resReg.clear();
	// }
	//
	// public static void init_Theme(String abspath) {
	// clearTheme();
	// Properties props = new Properties();
	// InputStream is = null;
	// try {
	// is = new FileInputStream(abspath);
	// props.load(is);
	// loadContent(props);
	// } catch (IOException e) {
	// e.printStackTrace();
	// } finally {
	// try {
	// is.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	//
	// private static void loadContent(Properties prop) {
	// Object one;
	// String key_this;
	// Enumeration enu = prop.propertyNames();
	// while (enu.hasMoreElements()) {
	// one = enu.nextElement();
	// key_this = one == null ? "" : one.toString();
	// resReg.put(key_this, prop.getProperty(key_this));
	// }
	// }

	public static void initR(Context context) {
		if (r == null) {
			try {
				r = Class.forName(context.getPackageName() + ".R");
			} catch (ClassNotFoundException ex) {
				ex.printStackTrace();
			}
			Class type;
			// Field[] fields = r.getFields();
			// for (Field f : fields) {
			//
			// type = getSubClass(f.getRaw());
			// Field[] tfs = type.getFields();
			// for (Field tf : tfs) {
			// getAttr(f.getRaw(), tf.getRaw());
			// }
			// }
			for (String initAttr : initType) {
				type = getSubClass(initAttr);
				if (type == null)
					continue;
				Field[] tfs = type.getFields();
				for (Field tf : tfs) {
					getAttr(initAttr, tf.getName());
				}
			}
		}
	}

	private static Class getR() {
		return r;
	}

	private static Class getSubClass(String childName) {
		Class clz = getR();
		for (Class clazz : clz.getClasses()) {
			if (clazz.getName().endsWith(childName)) {
				return clazz;
			}
		}
		return null;
	}

	private static ResObject generateAttrValue(String attr,
			TypeAttrs typeAttrs, String type) {
		Field field;
		Integer componentObj = null;
		ResObject returnResult = null;
		try {
			field = typeAttrs.type.getField(attr);
			componentObj = (Integer) field.get(typeAttrs.type);
			typeAttrs.attrMaps.put(attr, new ResObject(componentObj));

			// returnResult = typeAttrs.attrMaps.get(attr);
			// returnResult.setName(r.getRaw() + "." + type + "." + attr);
			// returnResult.setSimplename("R" + "." + type + "." + attr);
			// Logs.e("=== value name "+componentObj+" "+attr);
			attrValue_itsname.put(componentObj, type + "/" + attr);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return returnResult;
	}

	public static ResObject get(String str_R_Type_Attr) {
		int commaIdx = str_R_Type_Attr.lastIndexOf(".");
		return (ResObject) getAttr(str_R_Type_Attr.substring(2, commaIdx),
				str_R_Type_Attr.substring(commaIdx + 1));
	}

	public static ResObject getAttr(String type, String attr) {
		ResObject componentObj = null;
		TypeAttrs typeAttrs = resType.get(type);
		if (typeAttrs == null) {
			TypeAttrs varTypeAttrs = new TypeAttrs();
			varTypeAttrs.type = getSubClass(type);
			resType.put(type, varTypeAttrs);
			typeAttrs = varTypeAttrs;
		} else {

			componentObj = typeAttrs.attrMaps.get(attr);
			if (componentObj != null) {
				return componentObj;
			}
		}
		return generateAttrValue(attr, typeAttrs, type);
	}
}
