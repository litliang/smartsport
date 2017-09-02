package intf;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/3/2.
 */
public class MapBuilder {
    public Map map = new HashMap();
    public static MapBuilder build(){
        return new MapBuilder();
    }
    public MapBuilder add(String k, Object v){
        map.put(k,v+"");
        return this;
    }

    public Map get(){
        return map;
    }

}
