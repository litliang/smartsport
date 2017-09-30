package top.smartsport.www.activity;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;

import java.util.List;
import java.util.Map;

import app.base.JsonUtil;
import app.base.MapConf;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseApplication;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.utils.ActivityStack;
import top.smartsport.www.utils.pay.PayUtil;

/**
 * Created by bajieaichirou on 17/8/17.
 * 确认订单
 */
@ContentView(R.layout.activity_order_confirm)
public class ActivityOrderConfirm extends BaseActivity implements View.OnClickListener {

    TextView mAmountTv, mTimeLeftTv;
    AutoRelativeLayout mAliLayout, mWechatLayout;
    ImageView mAliIv, mWechatIv;
    Button mPayBtn;
    private Handler mHandler;

    @Override
    protected void initView() {
        initUI();
    }

    Map map;

    private void initUI() {

        map = (Map) getIntent().getSerializableExtra("data");
        String total = (String) map.get("total");
        mAmountTv = (TextView) findViewById(R.id.confirm_pay_amount_tv);
        mTimeLeftTv = (TextView) findViewById(R.id.confirm_time_left_tv);
        mAliLayout = (AutoRelativeLayout) findViewById(R.id.confirm_alipay_layout);
        mWechatLayout = (AutoRelativeLayout) findViewById(R.id.confirm_wechatpay_layout);
        mAliIv = (ImageView) findViewById(R.id.confirm_ali_iv);
        mWechatIv = (ImageView) findViewById(R.id.confirm_wechat_iv);
        mPayBtn = (Button) findViewById(R.id.confirm_pay_btn);
        // 默认选择支付方式 支付宝
        mAliIv.setImageResource(R.mipmap.radio_checked);
        mWechatIv.setImageResource(R.mipmap.radio_uncheck);

        mAliLayout.setOnClickListener(this);
        mWechatLayout.setOnClickListener(this);
        mPayBtn.setOnClickListener(this);

        MapConf.with(this).pair("total->confirm_pay_amount_tv").source(getIntent().getStringExtra("data"), this);
        mAmountTv.setText("￥" + total);
    }

    int i = 3;


    String payway = "aliPay";

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm_alipay_layout:
                mAliIv.setImageResource(R.mipmap.radio_checked);
                mWechatIv.setImageResource(R.mipmap.radio_uncheck);
                payway = "aliPay";
                break;
            case R.id.confirm_wechatpay_layout:
                mAliIv.setImageResource(R.mipmap.radio_uncheck);
                mWechatIv.setImageResource(R.mipmap.radio_checked);
                payway = "wxPay";
                break;
            case R.id.confirm_pay_btn:

                String total = (String) map.get("total");
                String type = (String) map.get("type");
                String prd_id = (String) map.get("product_id");

                PayUtil.pay(this,payway,type,total,prd_id);
//                callPay(this, total, payway, prd_id, type);
                break;
        }
    }

    public static void callPay(final Activity aty, String price, final String payway, String prdid, String type) {
        callHttp(MapBuilder.build().add("action", payway).add("total", price).add("type", type).add("product_id", prdid + "").get(), new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {
                if (payway.toLowerCase().equals("alipay")) {
                    final String order = JsonUtil.findJsonLink("order", ((NetEntity) result).getData().toString()).toString();
                    new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            PayTask payTask = new PayTask(aty);
                            String result = payTask.pay(order, true);
                            new Handler(Looper.getMainLooper(), new Handler.Callback() {
                                @Override
                                public boolean handleMessage(Message message) {
                                    ActivityStack.getInstance().finishActivity(ActivityOrderConfirm.class);
                                    ActivityStack.getInstance().finishActivity(ActivitySignUp.class);
                                    ActivityStack.getInstance().finishActivity(BSSignUpActivity.class);
                                    return false;
                                }
                            }).sendEmptyMessage(0);

                        }
                    }.start();
                } else {
                    if (BaseApplication.mWxApi != null) {
                        PayReq req = new PayReq();

                        try {
                            JSONObject jsonObject = new JSONObject(((NetEntity) result).getData().toString());
                            req.appId = jsonObject.getString("appid");
                            req.partnerId = jsonObject.getString("partnerid");// 微信支付分配的商户号
                            req.prepayId = jsonObject.getString("prepayid");// 预支付订单号，app服务器调用“统一下单”接口获取
                            req.nonceStr = jsonObject.getString("noncestr");// 随机字符串，不长于32位，服务器小哥会给咱生成
                            req.timeStamp = jsonObject.getString("timestamp");// 时间戳，app服务器小哥给出
                            req.packageValue = jsonObject.getString("package");// 固定值Sign=WXPay，可以直接写死，服务器返回的也是这个固定值
                            req.sign = jsonObject.getString("sign");// 签名，服务器小哥给出，他会根据：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3指导得到这个
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        BaseApplication.mWxApi.sendReq(req);
                    }
                }
            }

            @Override
            public void onFailure(Object result, List object) {
                Toast.makeText(aty, result.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCallback(Object result, List object) {



            }
        });
    }

    /*支付宝支付结果码*/
    private static final String PAY_OK = "9000";// 支付成功
    private static final String PAY_WAIT_CONFIRM = "8000";// 交易待确认
    private static final String PAY_NET_ERR = "6002";// 网络出错
    private static final String PAY_CANCLE = "6001";// 交易取消
    private static final String PAY_FAILED = "4000";// 交易失败


}
