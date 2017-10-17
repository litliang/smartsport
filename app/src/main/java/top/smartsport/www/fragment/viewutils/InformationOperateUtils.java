package top.smartsport.www.fragment.viewutils;

import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import top.smartsport.www.bean.Carousel;
import top.smartsport.www.bean.Data;
import top.smartsport.www.bean.HDZXInfo;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.SSXWInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.utils.StringUtil;
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
    public static void requestActivityInformation(int page,final String type, String cityId, final ActivityInformationAPICallBack callBack) {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", RegInfo.newInstance().getApp_key());
            json.put("state", RegInfo.newInstance().getSeed_secret());
            json.put("access_token", TokenInfo.newInstance().getAccess_token());
            json.put("action", "getNews");
            json.put("type", type);
            json.put("page", page);
            if(!StringUtil.isEmpty(cityId)) {
                json.put("city", cityId);
            }
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
                        if (TextUtils.equals("2",type)){
                            List<HDZXInfo> informationResources = data.toListnews(HDZXInfo.class);
                            if (callBack != null){
                                callBack.onSuccessTypeTwo(bannerResources,informationResources);
                                callBack.onFinished();
                            }
                        }else if(TextUtils.equals("3",type)){
                            List<SSXWInfo> informationResources = data.toListnews(SSXWInfo.class);
                            if (callBack != null){
                                callBack.onSuccessTypeThree(bannerResources,informationResources);
                                callBack.onFinished();
                            }
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
        void onSuccessTypeTwo(List<Carousel> bannerResources, List<HDZXInfo> informationResources);
        void onSuccessTypeThree(List<Carousel> bannerResources, List<SSXWInfo> informationResources);
        void onError(String errorMsg);
        void onFinished();
    }

}
