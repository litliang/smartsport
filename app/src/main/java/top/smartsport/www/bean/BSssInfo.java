package top.smartsport.www.bean;

/**
 * Created by Aaron on 2017/7/24.
 */

public class BSssInfo {
    /**
     * "id":"62",
     "name":"\u5357\u7ad9\u8db3\u7403\u8d5b",
     "description":"\r\n\t\u8db3\u7403\u8c61\u5f81\u7740\u4eba\u7c7b\u7684\u7cbe\u795e\uff0c\u4e0d\u8bba\u662f\u4e00\u79cd\u5b58\u5728\u7cbe\u795e\uff0c\u8fd8\u662f\u4e00\u79cd\u8ffd\u6c42\u7684\u7cbe\u795e\u3002\u5b83\u9700\u8981\u52c7\u6562\u3001\u575a\u97e7\u3001\u987d\u5f3a\u3001\u679c\u6562\u3001\u81ea\u4fe1\u4e0e\u610f\u5fd7\uff0c\u4e5f\u9700\u8981\u673a\u8b66\u3001\u667a\u5de7\uff0c\u9002\u5e94\u4e0e\u7075\u611f\uff1b\u9700\u8981\u51b2\u950b\u9677\u9635\u7684\u4e2a\u4eba\u82f1\u96c4\uff0c\u4e5f\u9700\u8981\u5929\u8863\u65e0\u7f1d\u7684\u6574\u4f53\u534f\u4f5c\uff1b\u9700\u8981\u5b9e\u529b\uff0c",
     "cover":"http:\/\/soccer.baibaobike.com\/data\/upload\/2017\/0724\/14\/59758dbe3feec.jpg",
     "address":"\u4e0a\u6d77\u5357\u7ad9",
     "level":"U19",
     "type":"5\u4eba",
     "sell_price":"299.00",
     "start_time":"2017-08-05",
     "end_time":"2017-08-31",
     "surplus":16,
     "status":"\u62a5\u540d\u4e2d"
     *
     * */

    private String id;
    private String name;
    private String description;
    private String cover;
    private String address;
    private String level;
    private String type;
    private String sell_price;
    private String start_time;
    private String end_time;
    private String surplus;
    private String status;
    private String county;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getCover() {
        return cover;
    }

    public String getAddress() {
        return address;
    }

    public String getLevel() {
        return level;
    }

    public String getSell_price() {
        return sell_price;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getSurplus() {
        return surplus;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
