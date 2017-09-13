package top.smartsport.www.fragment.information;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import top.smartsport.www.bean.Carousel;
import top.smartsport.www.bean.Data;
import top.smartsport.www.bean.HDZXInfo;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * deprecation:活动操作工具类[FIXME 临时中转处理,后期对分包结构可以做优化]
 * author:AnYB
 * time:2017/9/24
 */
public class InformationOperateUtils {
    private static final String NET_ERROR = "网络异常，请刷新重试";

    /**
     * 获取活动资讯数据
     * @param page  页码
     * @param callBack  回调
     */
    public static void requestActivityInformation(int page,final ActivityInformationAPICallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", RegInfo.newInstance().getApp_key());
            json.put("state", RegInfo.newInstance().getSeed_secret());
            json.put("access_token", TokenInfo.newInstance().getAccess_token());
            json.put("action", "getNews");
            json.put("type", "2");
            json.put("page", page);
        } catch (JSONException e) {
        }
        X.Post(RegInfo.newInstance().getSource_url(), json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                if (callBack != null){
                    callBack.onError(message);
                    callBack.onFinished();
                }
            }
            @Override
            public void onSuccess(NetEntity entity) {
                try {
                    if (entity != null){
                        Data data = entity.toObj(Data.class);
                        List<Carousel> bannerResources = data.toListcarousel(Carousel.class);
                        List<HDZXInfo> informationResources = data.toListnews(HDZXInfo.class);
                        if (callBack != null){
                            callBack.onSuccess(bannerResources,informationResources);
                            callBack.onFinished();
                        }
                    }
                }catch (Exception e){
                    if (callBack != null){
                        callBack.onError(NET_ERROR);
                        callBack.onFinished();
                    }
                }
            }
        });
    }

    /**
     * 活动资讯接口回调
     */
    public interface ActivityInformationAPICallBack{
        void onSuccess(List<Carousel> bannerResources, List<HDZXInfo> informationResources);
        void onError(String errorMsg);
        void onFinished();
    }

}
