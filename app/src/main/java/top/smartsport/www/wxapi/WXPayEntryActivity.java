package top.smartsport.www.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import top.smartsport.www.R;
import top.smartsport.www.activity.ActivityOrderConfirm;
import top.smartsport.www.activity.ActivitySignUp;
import top.smartsport.www.activity.BSSignUpActivity;
import top.smartsport.www.activity.OrderCMActivity;
import top.smartsport.www.activity.SSBMActivity;
import top.smartsport.www.utils.ActivityStack;

/**
 * Created by admin on 2017/9/1.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {


    private IWXAPI api;
    private static final String APP_ID = "wx5939ba19b940fea1";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    /**
     * 得到支付结果回调
     */
    @Override
    public void onResp(BaseResp resp) {
        ActivityStack.getInstance().finishActivity(ActivityOrderConfirm.class);
        ActivityStack.getInstance().finishActivity(ActivitySignUp.class);
        ActivityStack.getInstance().finishActivity(BSSignUpActivity.class);
        ActivityStack.getInstance().finishActivity(OrderCMActivity.class);
        ActivityStack.getInstance().finishActivity(SSBMActivity.class);
        finish();
    }
}
