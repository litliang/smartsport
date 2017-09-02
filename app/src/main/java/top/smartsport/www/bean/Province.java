package top.smartsport.www.bean;


import java.util.List;

/**
 * Created by Aaron on 2017/7/26.
 */

public class Province extends Obj {
    protected List<City> county;

    public List<City> getCity() {
        return county;
    }
}
