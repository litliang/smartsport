package top.smartsport.www.bean;

import java.io.Serializable;

import top.smartsport.www.widget.Banner;

/**
 * Created by Aaron on 2017/8/9.
 */

public class Carousel implements Serializable, Banner.BannerData{
    private String id;
    private String imgurl;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

}
