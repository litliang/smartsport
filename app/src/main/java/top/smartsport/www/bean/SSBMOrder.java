package top.smartsport.www.bean;

/**
 * Created by gst-pc on 2017/9/23.
 */

public class SSBMOrder {
    //{"total":"200.00","type":2,"product_id":"4"}
    private String total;
    private String type;
    private String product_id;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
}
