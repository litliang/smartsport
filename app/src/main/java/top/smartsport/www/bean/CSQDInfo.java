package top.smartsport.www.bean;

/**
 * Created by Aaron on 2017/7/25.
 */

public class CSQDInfo {
    /***  "match_id": "1",
     "team_id": "14",
     "team_name": "上海田林第三小学",
     "logo": "http://soccer.baibaobike.com/data/upload/2017/0720/17/59707b814cbb0.png",
     "description": null,
     "integral": 0
     *
     * */

    private String match_id;
    private String team_id;
    private String team_name;
    private String logo;
    private String description;
    private String integral;

    public String getMatch_id() {
        return match_id;
    }

    public String getTeam_id() {
        return team_id;
    }

    public String getTeam_name() {
        return team_name;
    }

    public String getLogo() {
        return logo;
    }

    public String getDescription() {
        return description;
    }

    public String getIntegral() {
        return integral;
    }
}
