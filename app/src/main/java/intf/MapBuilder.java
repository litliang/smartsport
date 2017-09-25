package intf;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2017/3/2.
 */
public class MapBuilder {
    public static MapBuilder withMap(Map map) {
        return build().setMap(map);
    }

    private MapBuilder setMap(Map map) {
        this.map = map;
        return this;
    }

    public Map map = new HashMap();
    public static MapBuilder build(){
        return new MapBuilder();
    }
    public MapBuilder add(String k, Object v){
        if(v==null||v.toString().equals("null")){

        }else {
            map.put(k, v + "");
        }
        return this;
    }

    public Map get(){
        return map;
    }

}
