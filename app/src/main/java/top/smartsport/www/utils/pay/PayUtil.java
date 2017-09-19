package top.smartsport.www.utils.pay;

import intf.JsonUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.TreeMap;

import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

//我的押金-充值
public class PayUtil {
    public static int REQUEST_SCAN_AFTER_RECHARGE = 111;
    private static IWXAPI mWxApi;


    String item = "";
    String type;
    String action = "";

    private static PayUtil payutil = new PayUtil();

    private static PayUtil getInstance(){
        if(payutil == null){
payutil = new PayUtil();
        }return payutil;
    }

    private PayUtil() {

    }

    public String wxappid;
    public   void initWXPay(String appid){

    }

    public static void recharge(final Activity ay, final String action,String type,String total) {
        final IWXAPI mWxApi = WXAPIFactory.createWXAPI(ay, "wx5939ba19b940fea1", true);
        mWxApi.registerApp("wx5939ba19b940fea1");

        String client_id;
         String state;
         String url;
         String access_token;

        RegInfo regInfo = RegInfo.newInstance();
        TokenInfo tokenInfo = TokenInfo.newInstance();
        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();

        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", action);
            json.put("total", total);
            json.put("type", type);//1 押金 2 充值
        } catch (JSONException e) {
            e.printStackTrace();
        }

        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {

            }

            @Override
            public void onSuccess(final NetEntity entity) {
//                Toast.makeText(ay, entity.getData().toString(), Toast.LENGTH_SHORT).show();


                if (action.equals("aliPay")) {
                    final String order = JsonUtil.findJsonLink("order", ((NetEntity) entity).getData().toString()).toString();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            PayTask payTask = new PayTask(ay);
                            final String result = payTask.pay(order, true);
                            final Map<String, String> map = new TreeMap<String, String>();
                            String[] rtparams = result.split("&");
                            for (String s : rtparams) {
                                String[] p = s.split("=");
                                map.put(p[0], p[1]);
                            }
                            new Handler(Looper.getMainLooper(), new Handler.Callback() {
                                @Override
                                public boolean handleMessage(Message message) {
                                    PayResult payResult = new PayResult(map);

                                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                                    String resultInfo = payResult.getResult();

                                    String resultStatus = payResult.getResultStatus();

                                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                                    if (resultStatus.contains("9000")) {
                                        Toast.makeText(ay, "支付成功",
                                                Toast.LENGTH_SHORT).show();
                                        ay.setResult(REQUEST_SCAN_AFTER_RECHARGE, new Intent().putExtra("callback", "true"));
                                        ay.finish();
                                    } else {
                                        // 判断resultStatus 为非“9000”则代表可能支付失败
                                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                                        if (resultStatus.contains("8000")) {
                                            Toast.makeText(ay, "支付结果确认中",
                                                    Toast.LENGTH_SHORT).show();

                                        } else {
                                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                            Toast.makeText(ay, "支付已取消",
                                                    Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                    return false;
                                }
                            }).sendEmptyMessage(0);

                        }
                    }.start();
                } else if (action.equals("wxPay")) {
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            if (mWxApi != null) {
                                PayReq req = new PayReq();

                                try {
                                    JSONObject jsonObject = new JSONObject(((NetEntity) entity).getData().toString());
                                    req.appId = jsonObject.getString("appid");
                                    req.partnerId = jsonObject.getString("partnerid");// 微信支付分配的商户号
                                    req.prepayId = jsonObject.getString("prepayid");// 预支付订单号，app服务器调用“统一下单”接口获取
                                    req.nonceStr = jsonObject.getString("noncestr");// 随机字符串，不长于32位，服务器小哥会给咱生成
                                    req.timeStamp = jsonObject.getString("timestamp");// 时间戳，app服务器小哥给出
                                    req.packageValue = jsonObject.getString("package");// 固定值Sign=WXPay，可以直接写死，服务器返回的也是这个固定值
                                    req.sign = jsonObject.getString("sign");// 签名，服务器小哥给出，他会根据：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3指导得到这个

                                    mWxApi.sendReq(req);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }.start();

                }
            }
        });
    }
}
