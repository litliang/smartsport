package top.smartsport.www.bean;

/**
 * Created by Aaron on 2017/8/4.
 */

public class Schedule {
    /**
     *  "id": "9",
     "round": "第1轮",
     "home_team": "人大附小队",
     "away_team": "上海田林第三小学",
     "home_logo": "http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png",
     "away_logo": "http://soccer.baibaobike.com/data/upload/2017/0802/18/5981a4b46578c.png",
     "cate_name": "小组赛",
     "group_name": "B",
     "start_time": "2017-07-06"
     * */
    private String id;
    private String round;
    private String home_team;
    private String away_team;
    private String home_logo;
    private String away_logo;
    private String cate_name;
    private String group_name;
    private String start_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getHome_team() {
        return home_team;
    }

    public void setHome_team(String home_team) {
        this.home_team = home_team;
    }

    public String getAway_team() {
        return away_team;
    }

    public void setAway_team(String away_team) {
        this.away_team = away_team;
    }

    public String getHome_logo() {
        return home_logo;
    }

    public void setHome_logo(String home_logo) {
        this.home_logo = home_logo;
    }

    public String getAway_logo() {
        return away_logo;
    }

    public void setAway_logo(String away_logo) {
        this.away_logo = away_logo;
    }

    public String getCate_name() {
        return cate_name;
    }

    public void setCate_name(String cate_name) {
        this.cate_name = cate_name;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }
}
