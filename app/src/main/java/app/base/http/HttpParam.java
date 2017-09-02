package app.base.http;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


import android.app.Activity;

public class HttpParam {
	public String activityABId;
	public String activitytBId;

	public HttpParam() {

	}
	public HttpParam(Activity... atyabc) {
		super();
		activityABId = transfertoId(atyabc);
		activitytBId = atyabc[atyabc.length - 1].getClass().getName();
	}

	public HttpParam(String... atyabc) {
		super();
		activityABId = transfertoId(atyabc);
		activitytBId = atyabc[atyabc.length - 1];
	}

	public static String transfertoId(Activity... abc) {
		String id = null;
		for (int i = 0; i < abc.length; i++) {
			id += "@" + abc[i].getClass().getName();
		}
		return id.substring(1);
	}

	public static String transfertoId(String... abc) {
		String id = null;
		for (int i = 0; i < abc.length; i++) {
			id += "@" + abc[i];
		}
		return id.substring(1);
	}

	public static HttpParam getHttpParam(String... activityB) {
		return activityABId_instance.get(transfertoId(activityB));
	}

	public static HttpParam getHttpParam(String activityA) {
		return activityABId_instance.get(transfertoId(activityA));
	}

	public static HttpParam getHttpParam(Activity activityA) {
		return activityABId_instance.get(transfertoId(activityA));
	}

	static Map<String, HttpParam> activityABId_instance = new TreeMap<String, HttpParam>() {

	};

	
	public HttpParam clearParams(){
		this.params.clear();
		return this;
	}
	public String getActivityABId() {
		return activityABId;
	}

	public void setActivityABId(String activityABId) {
		this.activityABId = activityABId;
	}

	public static HttpParam instance;

	public static HttpParam regenerate(String... activityB) {
		HttpParam httpParam = new HttpParam(activityB);
		activityABId_instance.put(httpParam.getActivityABId(), httpParam);
		
		return httpParam;
	}

	public static HttpParam regenerate() {
		return new HttpParam();
	}

	public static HttpParam regenerate(Activity activityA) {
		HttpParam httpParam = new HttpParam(activityA);
		activityABId_instance.put(httpParam.getActivityABId(), httpParam);
		return httpParam;
	}

	public static HttpParam regenerate(Activity... activityB) {
		HttpParam httpParam = new HttpParam(activityB);
		activityABId_instance.put(httpParam.getActivityABId(), httpParam);
		return httpParam;
	}

	public static HttpParam getIntance() {
		return instance;

	}

	public Map<String, String> params = new HashMap<String, String>();

	public String url;

	public String getUrl() {
		return url;
	}

	public HttpParam setUrl(String url) {
		this.url = url;
		return this;
	}

	public HttpParam addParam(String param, Object paramvalue) {
		if (param == null || param.toString().trim().equals("appid")
//				|| paramvalue.toString().trim().equals("")
				) {
			new Exception("http's param invalid").printStackTrace();
		}
		if(paramvalue==null){
			paramvalue = "";
		}
		params.put(param, paramvalue.toString());
		return this;
	}

	public String toUrl() {
		String toaddpart = "";
		for (Object param : this.params.keySet()) {
			toaddpart += "&" + param.toString() + "="
					+ this.params.get(param).toString();
		}
		if (params.size() != 0) {
			toaddpart = toaddpart.substring(1);

			if (url.contains("?")) {
				toaddpart = "&" + toaddpart;
			} else {
				toaddpart = "?" + toaddpart;
			}
		}
		String result = url + toaddpart;
		return result;
	}

}
