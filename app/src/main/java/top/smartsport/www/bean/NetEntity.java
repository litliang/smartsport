package top.smartsport.www.bean;

import com.google.gson.JsonElement;

import org.xutils.http.annotation.HttpResponse;

import java.util.List;

import top.smartsport.www.utils.JsonUtil;
import top.smartsport.www.xutils3.JsonResponseParser;

/**
 * Created by Aaron on 2017/7/2.
 */

@HttpResponse(parser = JsonResponseParser.class)
public class NetEntity {
    private String info;
    private String status;
    private String errno;
    private JsonElement data;
    private JsonElement hotCity;
    private String code;
    private String errmsg;
    private String message;
    private String current;//当前页数
    private String total;//总页数


    public JsonElement getData() {
        return data;
    }

    public <T> T toObj(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntity(data.toString(), clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> List<T> toList(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntityList(data.getAsJsonArray(), clazz);
        } catch (Exception e) {
            return null;
        }
    }
    public <T> List<T> toListCity(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntityList(hotCity.getAsJsonArray(), clazz);
        } catch (Exception e) {
            return null;
        }
    }


    public String getInfo() {
        return info;
    }

    public String getStatus() {
        return status;
    }

    public JsonElement getHotCity() {
        return hotCity;
    }

    public String getCode() {
        return code;
    }

    public String getErrno() {
        return errno;
    }

    public void setErrno(String errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public String getMessage() {
        return message;
    }
}


