package top.smartsport.www.bean;

import java.io.Serializable;

/**
 * Created by Aaron on 2017/7/24.
 */

public class BSzbInfo implements Serializable{
    /**
     * "activityCategory": "001",
     "activityId": "A20170731000007e",
     "activityName": "测试直播",
     "activityStatus": 1,
     "belongUserId": "916743",
     "coverImgUrl": "",
     "createTime": "20170731182839",
     "description": "欢迎观看测试直播的精彩直播",
     "endTime": "20500101000000",
     "startTime": "20170731182839"
     * */
    private String activityCategory;
    private String activityId;
    private String activityName;
    private String activityStatus;
    private String belongUserId;
    private String coverImgUrl;
    private String createTime;
    private String description;
    private String endTime;
    private String startTime;

    public String getActivityCategory() {
        return activityCategory;
    }

    public void setActivityCategory(String activityCategory) {
        this.activityCategory = activityCategory;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityStatus() {
        return activityStatus;
    }

    public void setActivityStatus(String activityStatus) {
        this.activityStatus = activityStatus;
    }

    public String getBelongUserId() {
        return belongUserId;
    }

    public void setBelongUserId(String belongUserId) {
        this.belongUserId = belongUserId;
    }

    public String getCoverImgUrl() {
        return coverImgUrl;
    }

    public void setCoverImgUrl(String coverImgUrl) {
        this.coverImgUrl = coverImgUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }
}
