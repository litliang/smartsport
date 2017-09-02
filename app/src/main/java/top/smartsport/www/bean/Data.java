package top.smartsport.www.bean;

import com.google.gson.JsonElement;

import java.util.List;

import top.smartsport.www.utils.JsonUtil;

/**
 * Created by Aaron on 2017/7/21.
 */

public class Data {
    private JsonElement hotCity;
    private JsonElement cityList;
    private JsonElement match;
    private JsonElement video;
    private JsonElement myteam;
    private JsonElement members;
    private JsonElement rows;
    private JsonElement schedule;
    private JsonElement carousel;
    private JsonElement coaches;
    private JsonElement courses;
    private JsonElement news;
    private JsonElement players;
    private JsonElement hot;

    public <T> List<T> toHot(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntityList(hot.getAsJsonArray(), clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> List<T> toListcarousel(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntityList(carousel.getAsJsonArray(), clazz);
        } catch (Exception e) {
            return null;
        }
    }
    public <T> List<T> toListcoaches(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntityList(coaches.getAsJsonArray(), clazz);
        } catch (Exception e) {
            return null;
        }
    }
    public <T> List<T> toListcourses(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntityList(courses.getAsJsonArray(), clazz);
        } catch (Exception e) {
            return null;
        }
    }
    public <T> List<T> toListnews(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntityList(news.getAsJsonArray(), clazz);
        } catch (Exception e) {
            return null;
        }
    }
    public <T> List<T> toListplayers(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntityList(players.getAsJsonArray(), clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> T toSchedule(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntity(schedule.toString(), clazz);
        } catch (Exception e) {
            return null;
        }
    }
    public <T> T toMatch(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntity(match.toString(), clazz);
        } catch (Exception e) {
            return null;
        }
    }
    public <T> T toVideo(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntity(video.toString(), clazz);
        } catch (Exception e) {
            return null;
        }
    }
    public <T> T toMyteam(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntity(myteam.toString(), clazz);
        } catch (Exception e) {
            return null;
        }
    }



    public JsonElement getHotCity() {
        return hotCity;
    }

    public JsonElement getCityList() {
        return cityList;
    }

    public <T> List<T> toListCity(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntityList(cityList.getAsJsonArray(), clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> List<T> toHotCity(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntityList(hotCity.getAsJsonArray(), clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> List<T> toMembers(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntityList(members.getAsJsonArray(), clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> List<T> toRows(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntityList(rows.getAsJsonArray(), clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public JsonElement getMembers() {
        return members;
    }

    public void setMembers(JsonElement members) {
        this.members = members;
    }
}
