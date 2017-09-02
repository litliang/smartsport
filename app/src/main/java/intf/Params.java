package intf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by admin on 2017/3/1.
 */

public class Params {
    public Map<String,String> left_right_x_nick_portrait = new TreeMap<String,String>();
    private String side_a_users_m_1;
    private String side_a_users_m_2;
    private String side_a_users_m_3;
    private String side_b_users_m_1;
    private String side_b_users_m_2;
    private String side_b_users_m_3;

    public Map<String, String> getLeft_right_x_nick_portrait() {
        return left_right_x_nick_portrait;
    }

    public void setLeft_right_x_nick_portrait(Map<String, String> left_right_x_nick_portrait) {
        this.left_right_x_nick_portrait = left_right_x_nick_portrait;
    }

    static Params params = new Params();

    public static Params getInstanceParam() {
        return params;
    }
    String videoPath;

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    String club_id;
    String user_id;
    String game_id;
    String code;
    String game_type;
    String youku_uri;
    String time;
    String game_name = "";

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getClub_id() {
        return club_id;
    }

    public void setClub_id(String club_id) {
        this.club_id = club_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getYouku_uri() {
        return youku_uri;
    }

    public void setYouku_uri(String youku_uri) {
        this.youku_uri = youku_uri;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSide_a__add_scores() {
        return side_a__add_scores;
    }

    public void setSide_a__add_scores(String side_a__add_scores) {
        this.side_a__add_scores = side_a__add_scores;
    }

    public String getSide_a__result() {
        return side_a__result;
    }

    public void setSide_a__result(String side_a__result) {
        this.side_a__result = side_a__result;
    }

    public String getSide_a__flagrant_fouls() {
        return side_a__flagrant_fouls;
    }

    public void setSide_a__flagrant_fouls(String side_a__flagrant_fouls) {
        this.side_a__flagrant_fouls = side_a__flagrant_fouls;
    }

    public String getSide_a__abstained() {
        return side_a__abstained;
    }

    public void setSide_a__abstained(String side_a__abstained) {
        this.side_a__abstained = side_a__abstained;
    }

    public String getSide_a__picture() {
        return side_a__picture;
    }

    public void setSide_a__picture(String side_a__picture) {
        this.side_a__picture = side_a__picture;
    }

    public String getSide_b__add_scores() {
        return side_b__add_scores;
    }

    public void setSide_b__add_scores(String side_b__add_scores) {
        this.side_b__add_scores = side_b__add_scores;
    }

    public String getSide_b__result() {
        return side_b__result;
    }

    public void setSide_b__result(String side_b__result) {
        this.side_b__result = side_b__result;
    }

    public String getSide_b__picture() {
        return side_b__picture;
    }

    public void setSide_b__picture(String side_b__picture) {
        this.side_b__picture = side_b__picture;
    }



    public String getSide_b__abstained() {
        return side_b__abstained;
    }

    public void setSide_b__abstained(String side_b__abstained) {
        this.side_b__abstained = side_b__abstained;
    }

    public String getSide_b__flagrant_fouls() {
        return side_b__flagrant_fouls;
    }

    public void setSide_b__flagrant_fouls(String side_b__flagrant_fouls) {
        this.side_b__flagrant_fouls = side_b__flagrant_fouls;
    }

    List<String> side_a__users = new ArrayList<String>();
    String side_a__add_scores;
    String side_a__result;
    String side_a__goals = "0";
    String side_a__pannas = "0";
    String side_a__fouls;
    String side_a__flagrant_fouls;
    String side_a__panna_ko = "0";
    String side_a__abstained;
    String side_a__picture;

    List<String> side_b__users= new ArrayList<String>();
    String side_b__add_scores;
    String side_b__result;
    String side_b__goals = "0";
    String side_b__pannas = "0";
    String side_b__fouls;
    String side_b__flagrant_fouls;
    String side_b__panna_ko = "0";
    String side_b__abstained;
    String side_b__picture;

    String side_a_users_1;
    String side_a_users_2;
    String side_a_users_3;

    public String getSide_a_users_1() {
        return side_a_users_1;
    }

    public String getSide_a_users_m_1() {
        return side_a_users_m_1;
    }

    public String getSide_a_users_m_2() {
        return side_a_users_m_2;
    }

    public String getSide_a_users_m_3() {
        return side_a_users_m_3;
    }

    public String getSide_b_users_m_1() {
        return side_b_users_m_1;
    }

    public String getSide_b_users_m_2() {
        return side_b_users_m_2;
    }

    public String getSide_b_users_m_3() {
        return side_b_users_m_3;
    }

    public void setSide_a_users_1(String side_a_users_1) {
        this.side_a_users_1 = side_a_users_1;
    }

    public String getSide_a_users_2() {
        return side_a_users_2;
    }

    public void setSide_a_users_2(String side_a_users_2) {
        this.side_a_users_2 = side_a_users_2;
    }

    public String getSide_a_users_3() {
        return side_a_users_3;
    }

    public void setSide_a_users_3(String side_a_users_3) {
        this.side_a_users_3 = side_a_users_3;
    }

    String side_b_users_1;
    String side_b_users_2;
    String side_b_users_3;


    public String getSide_b_users_1() {
        return side_b_users_1;
    }

    public void setSide_b_users_1(String side_b_users_1) {
        this.side_b_users_1 = side_b_users_1;
    }

    public String getSide_b_users_2() {
        return side_b_users_2;
    }

    public void setSide_b_users_2(String side_b_users_2) {
        this.side_b_users_2 = side_b_users_2;
    }

    public String getSide_b_users_3() {
        return side_b_users_3;
    }

    public void setSide_b_users_3(String side_b_users_3) {
        this.side_b_users_3 = side_b_users_3;
    }


    String side_a_users;
    String side_b_users;

    public List<String> getSide_a__users() {
        return side_a__users;
    }

    public void setSide_a__users(List<String> side_a__users) {
        this.side_a__users = side_a__users;
    }

    public List<String> getSide_b__users() {
        return side_b__users;
    }

    public void setSide_b__users(List<String> side_b__users) {
        this.side_b__users = side_b__users;
    }

//    "'club_id':'俱乐部ID','"俱乐部ID+
//
//            "user_id':'当前裁判ID','" +
//
//            "game_id':'赛事ID','" +
//
//            "code':'气场二维码','" +
//
//            "game_type':'0','" +
//
//            "youku_uri':'优酷视频地址','" +
//
//            "time':'比赛时间(格式 2016-01-01 17:00)','" +
//
//            "side_a':{'" +
//
//            "  users':[*],'" +
//
//            "  add_scores':'增加的积分','" +
//
//            "  result':'结果(胜者为 1 败者 -1 平局为0)','" +
//
//            "  goals':'进球数','" +
//
//            "  pannas':'穿裆数','" +
//
//            "  fouls':'犯规数','" +
//
//            "  flagrant_fouls':'恶意犯规数','" +
//
//            "  panna_ko':'是否穿裆KT(0 否 1 是)','" +
//
//            "  abstained':'是否放弃比赛(0 否 1 是)','" +
//
//            "  picture':'比赛图片'" +
//
//            "},'" +

    public String getGame_type() {
        return game_type;
    }

    public void setGame_type(String game_type) {
        this.game_type = game_type;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getGoals1() {
        return side_a__goals;
    }
    public void setGoals1(String goals1) {
        this.side_a__goals = goals1;
    }

    public String getGoals2() {
        return side_b__goals;
    }

    public void setGoals2(String goals2) {
        this.side_b__goals = goals2;
    }


    public String getPannas1() {
        return side_a__pannas;
    }

    public void setPannas1(String pannas1) {
        this.side_a__pannas = pannas1;
    }

    public String getPannas2() {
        return side_b__pannas;
    }

    public void setPannas2(String pannas2) {
        this.side_b__pannas = pannas2;
    }

    public String getPanna_ko1() {
        return side_a__panna_ko;
    }

    public void setPanna_ko1(String panna_ko1) {
        this.side_a__panna_ko = panna_ko1;
    }

    public String getPanna_ko2() {
        return side_b__panna_ko;
    }

    public void setPanna_ko2(String panna_ko2) {
        this.side_b__panna_ko = panna_ko2;
    }

    public void setBattle_id(String battle_id) {
    }

    public void setSide_a_users_m_1(String side_a_users_m_1) {
        this.side_a_users_m_1 = side_a_users_m_1;
    }

    public void setSide_a_users_m_2(String side_a_users_m_2) {
        this.side_a_users_m_2 = side_a_users_m_2;
    }

    public void setSide_a_users_m_3(String side_a_users_m_3) {
        this.side_a_users_m_3 = side_a_users_m_3;
    }

    public void setSide_b_users_m_1(String side_b_users_m_1) {
        this.side_b_users_m_1 = side_b_users_m_1;
    }

    public void setSide_b_users_m_2(String side_b_users_m_2) {
        this.side_b_users_m_2 = side_b_users_m_2;
    }

    public void setSide_b_users_m_3(String side_b_users_m_3) {
        this.side_b_users_m_3 = side_b_users_m_3;
    }
}
