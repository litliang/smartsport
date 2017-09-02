package top.smartsport.www;

/**
 * Created by fengshuai on 16/7/18.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import top.smartsport.www.base.BaseApplication;
import top.smartsport.www.bean.BSSZInfo;
import top.smartsport.www.bean.BSZTInfo;
import top.smartsport.www.bean.BSssInfo;
import top.smartsport.www.bean.ComCity;
import top.smartsport.www.bean.HotCity;
import top.smartsport.www.bean.Province;
import top.smartsport.www.bean.SSJBInfo;
import top.smartsport.www.utils.JsonUtil;

public final class O {

    private static final String DEFAULT = "DEFAULT";
    private static final String LIST = "LIST";
    private static List<HotCity> hot_areas;
    private static List<ComCity> com_areas;

    private O() {
    }

    private static List<Province> areas;
    private static Map<String, String> aMap;
    private static Map<String, String> idMap;
    private static Map<String,String> adept;
    private static Map<String,String> iddept;
    private static Map<String,String> choseCity;
    private static List<SSJBInfo> ssjbInfoList;
    private static List<BSZTInfo> bsztInfoList;
    private static List<BSSZInfo> bsszInfoList;


    public static void setHotAreas(List<HotCity> hot_areas) {
        O.hot_areas = hot_areas;
        O.saveList(hot_areas, HotCity.class);
    }

    public static void setComAreas(List<ComCity> com_areas) {
        O.com_areas = com_areas;
        O.saveList(com_areas, ComCity.class);
    }

    public static void setSSJB(List<SSJBInfo> ssjbInfoList){
        O.ssjbInfoList = ssjbInfoList;
        O.saveList(ssjbInfoList,SSJBInfo.class);
    }

    public static List<SSJBInfo> getSSJB() {
        if (ssjbInfoList == null) {
            ssjbInfoList = O.getList(SSJBInfo.class);
            if (ssjbInfoList == null) {
                ssjbInfoList = new ArrayList<SSJBInfo>();
            }
        }
        return ssjbInfoList;
    }

    public static void setBSZT(List<BSZTInfo> bsztInfoList){
        O.bsztInfoList = bsztInfoList;
        O.saveList(bsztInfoList, BSZTInfo.class);

    }
    public static List<BSZTInfo> getBSZT() {
        if (bsztInfoList == null) {
            bsztInfoList = O.getList(BSZTInfo.class);
            if (bsztInfoList == null) {
                bsztInfoList = new ArrayList<BSZTInfo>();
            }
        }
        return bsztInfoList;
    }
    public static void setBSSZ(List<BSSZInfo> bsszInfoList){
        O.bsszInfoList = bsszInfoList;
        O.saveList(bsszInfoList,BSSZInfo.class);
    }

    public static List<BSSZInfo> getBSSZ() {
        if (bsszInfoList == null) {
            bsszInfoList = O.getList(BSSZInfo.class);
            if (bsszInfoList == null) {
                bsszInfoList = new ArrayList<BSSZInfo>();
            }
        }
        return bsszInfoList;
    }

    public static <T> void save(T t, Class<T> clazz) {
        save(t, DEFAULT, clazz);
    }

    public static <T> void save(T t, String key, Class<T> clazz) {
        SharedPreferences preferences = BaseApplication.getApplication().getSharedPreferences(clazz.getName(), Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        if (t == null) {
            editor.remove(key);
        } else {
            editor.putString(key, JsonUtil.entityToJson(t));
        }
        editor.commit();
    }

    public static <T> void saveList(List<T> list, Class<T> clazz) {
        saveList(list, DEFAULT, clazz);
    }

    public static <T> void saveList(List<T> list, String key, Class<T> clazz) {
        SharedPreferences preferences = BaseApplication.getApplication().getSharedPreferences(clazz.getName() + LIST, Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        if (list == null) {
            editor.remove(key);
        } else {
            editor.putString(key, JsonUtil.entityToJson(list));
        }
        editor.commit();
    }

    public static <T> T get(Class<T> clazz) {
        return get(clazz, DEFAULT);
    }

    public static <T> T get(Class<T> clazz, String key) {
        if (clazz == null) {
            return null;
        }
        SharedPreferences preferences = BaseApplication.getApplication().getSharedPreferences(clazz.getName(), Context.MODE_PRIVATE);
        if (!preferences.contains(key)) {
            return null;
        }
        String json = preferences.getString(key, "");
        try {
            return JsonUtil.jsonToEntity(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> List<T> getList(Class<T> clazz) {
        return getList(clazz, DEFAULT);
    }

    public static <T> List<T> getList(Class<T> clazz, String key) {
        if (clazz == null) {
            return null;
        }
        SharedPreferences preferences = BaseApplication.getApplication().getSharedPreferences(clazz.getName() + LIST, Context.MODE_PRIVATE);
        if (!preferences.contains(key)) {
            return null;
        }
        String json = preferences.getString(key, "");
        try {
            return JsonUtil.jsonToEntityList(json, clazz);
        } catch (Exception e) {
            return null;
        }
    }



    public static String getChoseCityID(String name){
        if (choseCity == null) {

            initChoseCityMap();
        }
        return choseCity.get(name);
    }



    private static void initChoseCityMap() {
        List<ComCity> list = getComAreas();
        choseCity =new HashMap<String, String>();
        for (ComCity p : list) {
            choseCity.put(p.getTitle(), p.getArea_id());

        }
    }


    public static List<ComCity> getComAreas() {
        if (com_areas == null) {
            com_areas = O.getList(ComCity.class);
            if (com_areas == null) {
                com_areas = new ArrayList<ComCity>();
            }
        }
        return com_areas;
    }

    public static List<HotCity> getHotAreas(){
        if (hot_areas == null) {
            hot_areas = O.getList(HotCity.class);
            if (hot_areas == null) {
                hot_areas = new ArrayList<HotCity>();
            }
        }
        return hot_areas;
    }

    public static List<Province> getAreas() {
        if (areas == null) {
            areas = O.getList(Province.class);
            if (areas == null) {
                areas = new ArrayList<Province>();
            }
        }
        return areas;
    }
    public static void setAreas(List<Province> areas) {
        O.areas = areas;
        O.saveList(areas, Province.class);
    }
}
