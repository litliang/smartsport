package top.smartsport.www.bean;

import java.io.Serializable;

/**
 * Created by Aaron on 2017/8/9.
 */

public class Courses implements Serializable{
    private String cover_url;
    private String id;
    private String sell_price;
    private String title;

    public String getCover_url() {
        return cover_url;
    }

    public void setCover_url(String cover_url) {
        this.cover_url = cover_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSell_price() {
        return sell_price;
    }

    public void setSell_price(String sell_price) {
        this.sell_price = sell_price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
