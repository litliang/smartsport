package intf;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import top.smartsport.www.bean.Obj;

/**
 * Created by admin on 2017/3/2.
 */
public class BundleBuilder {

    public Bundle map = new Bundle();
    public static BundleBuilder build(){
        return new BundleBuilder();
    }

    public BundleBuilder add(String k, Object v){
        map.putString(k,v+"");
        return this;
    }

    public BundleBuilder initMap(Map m){
        for(Object k:m.keySet()){
            String v = m.get(k.toString()).toString();
            map.putString(k.toString(),v+"");
        }
        return this;
    }

    public Bundle get(){
        return map;
    }

}
