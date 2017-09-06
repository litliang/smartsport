package top.smartsport.www.bean;

/**
 * 评论
 */

public class ZXInfoComment {
    /**
     *  "id": "18",
     "uid": "60",
     "content": "葱花",
     "username": "只想做一枚安静的美男子",
     "header_url": "http://admin.smartsport.top/data/upload/2017/0831/21/59a80a22c5cc6.jpeg",
     "comment_time": "09-05 15:35"
     */
    private String id;
    private String uid;
    private String content;
    private String username;
    private String header_url;
    private String comment_time;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeader_url() {
        return header_url;
    }

    public void setHeader_url(String header_url) {
        this.header_url = header_url;
    }

    public String getComment_time() {
        return comment_time;
    }

    public void setComment_time(String comment_time) {
        this.comment_time = comment_time;
    }
}
