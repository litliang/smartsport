package top.smartsport.www.actions;

import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.O;
import top.smartsport.www.bean.BSSZInfo;
import top.smartsport.www.bean.BSZTInfo;
import top.smartsport.www.bean.KCJBInfo;
import top.smartsport.www.bean.KCLBInfo;
import top.smartsport.www.bean.KCLYInfo;
import top.smartsport.www.bean.KCZTInfo;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.QXJBInfo;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.SSJBInfo;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by gst-pc on 2017/9/23.
 */

public class DataInfo {

    // 青训筛选
    public static void getCourseChoice(RegInfo regInfo, String client_id, String state, String access_token) {
        final List<QXJBInfo> qxjbInfoList = new ArrayList<>();
        final List<KCZTInfo> kcztInfoList = new ArrayList<>();
        String url = regInfo.getSource_url();
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "getQxCourseFilter");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {

            }

            @Override
            public void onSuccess(NetEntity entity) {
                JsonElement jsonElement = entity.getData();
                try {
                    JSONObject json = new JSONObject(jsonElement.toString());
                    JSONArray levelList = json.optJSONArray("level");
                    JSONArray statusList = json.optJSONArray("status");
                    for (int i = 0; i < levelList.length(); i++) {
                        JSONObject obj = (JSONObject) levelList.get(i);
                        String id = obj.optString("id");
                        String name = obj.optString("name");
                        QXJBInfo info = new QXJBInfo();
                        info.setId(id);
                        info.setName(name);
                        qxjbInfoList.add(info);
                    }
                    for (int i = 0; i < statusList.length(); i++) {
                        JSONObject obj = (JSONObject) statusList.get(i);
                        String id = obj.optString("id");
                        String name = obj.optString("name");
                        KCZTInfo info = new KCZTInfo();
                        info.setId(id);
                        info.setName(name);
                        kcztInfoList.add(info);
                    }
                    O.setQXJB(qxjbInfoList);
                    O.setKCZT(kcztInfoList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 在线教案筛选
    public static void getTeachingPlanChoice(RegInfo regInfo, String client_id, String state, String access_token) {
        final List<KCJBInfo> kcjbInfoList = new ArrayList<>();
        final List<KCLYInfo> kclyInfoList = new ArrayList<>();
        final List<KCLBInfo> kclbInfoList = new ArrayList<>();
        String url = regInfo.getSource_url();
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "getOnlineCourseFilter");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {

            }

            @Override
            public void onSuccess(NetEntity entity) {
                JsonElement jsonElement = entity.getData();
                try {
                    JSONObject json = new JSONObject(jsonElement.toString());
                    JSONArray levelList = json.optJSONArray("level");
                    JSONArray sourceList = json.optJSONArray("source");
                    JSONArray categoryList = json.optJSONArray("category");
                    for (int i = 0; i < levelList.length(); i++) {
                        JSONObject obj = (JSONObject) levelList.get(i);
                        String id = obj.optString("id");
                        String name = obj.optString("name");
                        KCJBInfo info = new KCJBInfo();
                        info.setId(id);
                        info.setName(name);
                        kcjbInfoList.add(info);

                    }
                    for (int i = 0; i < sourceList.length(); i++) {
                        JSONObject obj = (JSONObject) sourceList.get(i);
                        String id = obj.optString("id");
                        String name = obj.optString("name");
                        KCLYInfo info = new KCLYInfo();
                        info.setId(id);
                        info.setName(name);
                        kclyInfoList.add(info);
                    }
//
                    for (int i = 0; i < categoryList.length(); i++) {
                        JSONObject obj = (JSONObject) categoryList.get(i);
                        String id = obj.optString("id");
                        String name = obj.optString("name");
                        KCLBInfo info = new KCLBInfo();
                        info.setId(id);
                        info.setName(name);
                        kclbInfoList.add(info);
                    }
                    O.setKCJB(kcjbInfoList);
                    O.setKCLY(kclyInfoList);
                    O.setKCLB(kclbInfoList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 获取筛选条件
     */
    public static void getChoice(RegInfo regInfo, String client_id, String state, String access_token) {
        final List<SSJBInfo> ssjbInfoList = new ArrayList<>();
        final List<BSZTInfo> bsztInfoList = new ArrayList<>();
        final List<BSSZInfo> bsszInfoList = new ArrayList<>();
        String url = regInfo.getSource_url();
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "getMatchFilter");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {

            }

            @Override
            public void onSuccess(NetEntity entity) {
                JsonElement jsonElement = entity.getData();
                try {
                    JSONObject json = new JSONObject(jsonElement.toString());
                    JSONArray levelList = json.optJSONArray("level");
                    JSONArray statusList = json.optJSONArray("status");
                    JSONArray typeList = json.optJSONArray("type");
                    for (int i = 0; i < levelList.length(); i++) {
                        JSONObject obj = (JSONObject) levelList.get(i);
                        String id = obj.optString("id");
                        String name = obj.optString("name");
                        SSJBInfo info = new SSJBInfo();
                        info.setId(id);
                        info.setName(name);
                        ssjbInfoList.add(info);

                    }
                    for (int i = 0; i < statusList.length(); i++) {
                        JSONObject obj = (JSONObject) statusList.get(i);
                        String id = obj.optString("id");
                        String name = obj.optString("name");
                        BSZTInfo info = new BSZTInfo();
                        info.setId(id);
                        info.setName(name);
                        bsztInfoList.add(info);

                    }

                    for (int i = 0; i < typeList.length(); i++) {
                        JSONObject obj = (JSONObject) typeList.get(i);
                        String id = obj.optString("id");
                        String name = obj.optString("name");
                        BSSZInfo info = new BSSZInfo();
                        info.setId(id);
                        info.setName(name);
                        bsszInfoList.add(info);

                    }
                    O.setSSJB(ssjbInfoList);
                    O.setBSZT(bsztInfoList);
                    O.setBSSZ(bsszInfoList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
