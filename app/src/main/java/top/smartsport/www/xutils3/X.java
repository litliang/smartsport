package top.smartsport.www.xutils3;

import android.os.Build;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

import top.smartsport.www.base.BaseApplication;

/**
 *   @describe 网络请求
 */
public class X {
    /**
     * 发送get请求
     * @param <T>
     *
     */
    public static <T> Callback.Cancelable Get(String url, Map<String,String> map, Callback.ProgressCallback<T> callback){
        RequestParams params=new RequestParams(url);
        if(null!=map){
            for(Map.Entry<String, String> entry : map.entrySet()){
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    /**
     * 发送post请求
     * @param <T>
     */
    public static <T> Callback.Cancelable Post(String url, Map<String,Object> map, Callback.ProgressCallback<T> callback){
        RequestParams params=new RequestParams(url);
//        params.setHeader("User-Agent", String.format("%s/%s (Linux; Android %s; %s Build/%s)", "pinxiango", MyApp.getVersion(), Build.VERSION.RELEASE, Build.MANUFACTURER, Build.ID));
        params.addHeader("application/x-www-form-urlencoded","charset=UTF-8");
        if(null!=map){
            for(Map.Entry<String, Object> entry : map.entrySet()){
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }

    /**
     * 发送post请求
     * @param <T>
     */
    public static <T> Callback.Cancelable Post(String url, Map<String,Object> map, Callback.CacheCallback<T> callback){
        RequestParams params=new RequestParams(url);
        params.addHeader("application/x-www-form-urlencoded","charset=UTF-8");
        params.setCacheMaxAge(1000 * 60);//缓存60秒

        if(null!=map){
            for(Map.Entry<String, Object> entry : map.entrySet()){
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        Callback.Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }


    /**
     * 发送post请求
     * @param <T>
     */
    public static <T> Callback.Cancelable Post(String url, JSONObject json, Callback.ProgressCallback<T> callback){
        RequestParams params=new RequestParams(url);
        params.addHeader("application/json","charset=UTF-8");
        params.addBodyParameter("",json.toString());
        Callback.Cancelable cancelable = x.http().post(params, callback);
        return cancelable;
    }

    /**
     * 上传文件
     * @param <T>
     */
    public static <T> Callback.Cancelable UpLoadFile(String url, Map<String,Object> map, Callback.ProgressCallback<T> callback){
        RequestParams params=new RequestParams(url);
        if(null!=map){
            for(Map.Entry<String, Object> entry : map.entrySet()){
                params.addParameter(entry.getKey(), entry.getValue());
            }
        }
        params.setMultipart(true);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }

    /**
     * 下载文件
     * @param <T>
     */
    public static <T> Callback.Cancelable DownLoadFile(String url, String filepath, Callback.ProgressCallback<T> callback){
        RequestParams params=new RequestParams(url);
        //设置断点续传
        params.setAutoResume(true);
        params.setSaveFilePath(filepath);
        Callback.Cancelable cancelable = x.http().get(params, callback);
        return cancelable;
    }


}
