package top.smartsport.www.bean;

/**
 * Created by Aaron on 2017/7/26.
 */

public class Obj {
    private String area_id;
    private String title;

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public String toString() {
        return title;
    }
}
