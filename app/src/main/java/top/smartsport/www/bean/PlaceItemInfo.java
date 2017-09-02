package top.smartsport.www.bean;


import java.io.Serializable;

import top.smartsport.www.utils.PinYinUtil;

/**
 * @author：suntongfu
 * @time: 2016/7/29 09:33
 * @describe:
 */
public class PlaceItemInfo implements Comparable<PlaceItemInfo>,Serializable {
    public   String mPlaceId;
    public   String mName;
    public  String mPinyin;
    public PlaceItemInfo(String placeName, String mPlaceId ) {
        mName = placeName;
        this.mPlaceId=mPlaceId;
        mPinyin = PinYinUtil.toPinyin(placeName);
    }

    @Override
    public int compareTo(PlaceItemInfo another) {
        return mPinyin.compareTo(another.mPinyin);
    }


}