package intf;

import android.app.Activity;
import android.util.Log;

import intf.json.JsonToMapUtils;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;



/**
 * Created by admin on 2017/3/2.
 */
public class QueryBuilder {
    String url;
    public Map map = new HashMap();
    public RequestParams p = new RequestParams();

    public QueryBuilder setTokenUrl(String url) {
        p.getQueryStringParams().clear();
        return this;
    }

    public static QueryBuilder build(String url) {
        return build(url, true);
    }

    public static QueryBuilder build(String url, boolean addtoken) {
        QueryBuilder qb = new QueryBuilder();
        qb.url = url;
        qb.p = new RequestParams(url);
        if (addtoken) {
        }
        return qb;
    }

    public QueryBuilder add(String k, Object v) {

        p.addQueryStringParameter(k, "" + v);
        map.put(k, "" + v);

        return this;
    }

    public QueryBuilder appendUrl(String url) {
        this.url += url;
        return this;
    }

    public QueryBuilder addByteArray(String k, byte[] v) {

        p.addParameter(k, v);

        return this;
    }

    public RequestParams get() {
        return p;
    }

    public Map getMap() {
        return map;
    }

    public String getJson() {
        return JsonToMapUtils.mapToJson(map);
    }

    ;
    public Callback callback;

    public void addCallback(Callback callback) {
        this.callback = callback;
    }

    public RequestParams get(final Callback callback) {
        if (callback == null) {
            throw new RuntimeException("addCallback must be invoked before to");
        }
        final RequestParams rp = get();
        callback.onDebug(rp);
        x.http().get(rp, new org.xutils.common.Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                callback.onError(ex, isOnCallback);
                if (!isNetworkConnected()) {
//                    Toast.makeText(, "网络出错，请稍后再试", Toast.LENGTH_LONG).show();
                    return;
                }
//                String str = MapBuilder.build().add("型号", Build.BRAND + "-" + Build.MODEL).add("exception", ex.toString()).add("uri", rp.getUri()).add("params", rp.toString()).add("nickname", KTApplication.getUserLogin().nickname).add("user_id", KTApplication.getUserLogin().user_id).get().toString();
//                MobclickAgent.reportError(KTApplication.getContext(), str);
//                if (true) {
//                    Toast.makeText(KTApplication.getInstance(), str, Toast.LENGTH_LONG).show();
//                }
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

        return rp;
    }

    public static boolean isNetworkConnected() {
//        ConnectivityManager cm = (ConnectivityManager) KTApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo ni = cm.getActiveNetworkInfo();
//        return ni != null && ni.isConnectedOrConnecting();
        return true;
    }

    public static interface Callback {

        public void onSuccess(String result);

        public void onError(Throwable ex, boolean isOnCallback);

        void onDebug(RequestParams rp);
    }

    public abstract static class EnhancedCallback implements Callback {
        List<String> list;


        public EnhancedCallback(String expr) {
            if (expr == null || expr.length() == 0) {
                return;
            }
            this.list = Arrays.asList(expr.split(";"));
        }

        public EnhancedCallback() {

        }

        public void onActivityCallback(String namelink, Object object) {
            if (baseactivity != null) {
//                baseactivity.rt = object;
//                baseactivity.onDataLoad(namelink, object);
            }
            onSuccessWithObject(namelink, object);
        }

        ;

        public abstract void onSuccessWithObject(String namelink, Object object);


        public void onSuccess(String str) {
            String namelink;
            Object msg = JsonUtil.findJsonLink("msg", str);
            str = str.replaceAll("null", "--");
            if (JsonUtil.findJsonLink("response", str).toString().contains("error")) {


                if (msg == null || msg.equals("")) {
                    msg = "error  " + rp.getMethod().toString() + ": " + rp.toString();

                }
                if (baseactivity != null) {
//                    Toast.makeText(baseactivity, msg.toString(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (list == null) {
                namelink = "";
                Object rt = JsonUtil.extractJsonRightValue(str);
                onActivityCallback("", rt);
            } else {
                for (String astr : list) {
                    String group = null;
                    namelink = astr;
                    String expr = null;
                    if (astr.contains("[")) {
                        int i1 = astr.indexOf("[");
                        int i2 = astr.indexOf("]", i1 + 1);
                        expr = astr.substring(i1 + 1, i2);
                        String[] exproptArr = expr.split(":");
                        if (exproptArr[0].contains("group")) {
                            group = exproptArr[1];
                        }
                        namelink = astr.substring(0, i1);
                    }
//            }
                    Object rt = JsonUtil.extractJsonRightValue(JsonUtil.findJsonLink(namelink, str));
                    if (expr == null) {
                        onActivityCallback(astr, rt);
                        return;
                    }
                    Map<String, ArrayList> cats = null;
                    if (rt instanceof List) {
                        if (group != null) {
                            List<Map> listrt = (List<Map>) rt;
                            cats = new TreeMap<String, ArrayList>();
                            for (Map map : listrt) {
                                if (!map.containsKey(group)) {
                                    throw new RuntimeException("no this '" + group + "' name for group on SkillInfo Type Result");
                                }
                                String key = map.get(group).toString();
                                if (!cats.containsKey(key)) {
                                    cats.put(key, new ArrayList<Map>());
                                }
                                cats.get(key).add(map);

                            }
                        }
                    }
                    onActivityCallback(astr, cats);
                }

            }
        }

    }

    public static Activity baseactivity;

    public QueryBuilder add(Activity baseactivity) {
        this.baseactivity = baseactivity;
        return this;
    }

    static RequestParams rp;

    public void post(final Callback callback) {
        if (callback == null) {
            throw new RuntimeException("addCallback must be invoked before to");
        }
        rp = get();
        Log.e("tag", rp.getUri() + "  " + rp.getQueryStringParams() + " " + rp.getBodyParams() + " " + rp.getFileParams());
        callback.onDebug(rp);
        x.http().post(get(), new org.xutils.common.Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
//                if (KTApplication.toastDebug == KTApplication.toastDebugResult) {
//                    Toast.makeText(KTApplication.getInstance(), rp + "\n" + result, Toast.LENGTH_LONG).show();
//                }
                String strresult = (String) JsonUtil.findJsonLink("response", result);

                if (strresult != null && strresult.equals("error")) {
                    String msg = (String) JsonUtil.findJsonLink("msg", result);
                    if (msg != null && !msg.equals("")) {
//                        Toast.makeText(KTApplication.getInstance(), msg, Toast.LENGTH_SHORT).show();
                    }
                }
                callback.onSuccess(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

//                String str = MapBuilder.build().add("型号", Build.BRAND + "-" + Build.MODEL).add("exception", ex.toString()).add("uri", rp.getUri()).add("params", rp.toString()).add("nickname", KTApplication.getUserLogin().nickname).add("user_id", KTApplication.getUserLogin().user_id).get().toString();
//                MobclickAgent.reportError(KTApplication.getContext(), str);
//                if (KTApplication.toastDebug == KTApplication.toastDebugError) {
//                    Toast.makeText(KTApplication.getInstance(), str, Toast.LENGTH_LONG).show();
//                }
                callback.onError(ex, isOnCallback);
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
