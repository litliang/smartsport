package top.smartsport.www.bean;

/**
 * 分享Item
 */
public class ShareItem {

    private int picId;
    private String name;
    private String type;

    public int getPicId() {
        return picId;
    }

    public void setPicId(int picId) {
        this.picId = picId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ShareItem(String name, int picId, String type) {
        this.name = name;
        this.picId = picId;
        this.type = type;
    }

}
