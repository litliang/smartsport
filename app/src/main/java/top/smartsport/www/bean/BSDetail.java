package top.smartsport.www.bean;

import com.google.gson.JsonElement;

import java.util.List;

import top.smartsport.www.utils.JsonUtil;

/**
 * Created by Aaron on 2017/7/25.
 */

public class BSDetail {
    /** {
     "status": true,
     "errno": "0",
     "data": {
     "id": "1",
     "name": "第14届青年足球杯",
     "cover": "http://soccer.baibaobike.com/data/upload/2017/0726/09/5977f1c176b2a.jpg",
     "description": "好玩",
     "start_time": "2017-07-25",
     "address": "上海体育馆",
     "county": "徐汇区",
     "level": "U5",
     "type": "7人制",
     "price": "299.00",    //原价
     "sell_price": "199.00",  //售价
     "surplus": 0,       //剩余名额
     "quota": "8",       //限额
     "apply_num": "8",   //报名个数
     "status": "进行中",
     "match_imgs": [
     {
     "fileurl": "http://soccer.baibaobike.com/apps/admin/_static/file/upload/2017/07/26/20170726093737275.jpg"
     },
     ...
     ],
     "match_video": [
     {
     "id": "8",
     "name": "第一场",
     "fileurl": "http://soccer.baibaobike.com/apps/admin/_static/file/upload/2017/08/01/video1.mp4"
     },
     ...
     ] ,
     "schedule": [        //只有比赛状态为进行中的才有
     {
     "id": "6",
     "home_team": "上海田林第三小学",
     "away_team": "人大附小队",
     "home_logo": "http://soccer.baibaobike.com/data/upload/2017/0720/17/59707b814cbb0.png",
     "away_logo": "http://soccer.baibaobike.com/data/upload/2017/0720/17/5970776aa0cdf.jpg",
     "cate_name": "小组赛",
     "group_name": "A组",
     "live_url": "http://www.aiqiyi.com/live/250.html",
     "start_time": "2017-07-18"
     },
     ...
     ]
     }
     }
     * */
    private String id;
    private String name;
    private String description;
    private String start_time;
    private String end_time;
    private String address;
    private String price;
    private String county;
    private String level;
    private String type;
    private String sell_price;
    private String surplus;
    private String cover;
    private String quota;
    private String apply_num;
    private String status;
    private JsonElement match_imgs;
    private JsonElement match_video;
    private JsonElement schedule;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getAddress() {
        return address;
    }

    public String getPrice() {
        return price;
    }

    public String getCounty() {
        return county;
    }

    public String getLevel() {
        return level;
    }

    public String getType() {
        return type;
    }

    public String getSell_price() {
        return sell_price;
    }

    public String getSurplus() {
        return surplus;
    }

    public String getCover() {
        return cover;
    }

    public String getQuota() {
        return quota;
    }

    public String getApply_num() {
        return apply_num;
    }

    public String getStatus() {
        return status;
    }

    public JsonElement getMatch_imgs() {
        return match_imgs;
    }

    public JsonElement getMatch_video() {
        return match_video;
    }

    public JsonElement getSchedule() {
        return schedule;
    }

    public <T> List<T> toList(Class<T> clazz) {
        try {
            return JsonUtil.jsonToEntityList(match_imgs.getAsJsonArray(), clazz);
        } catch (Exception e) {
            return null;
        }
    }
}
