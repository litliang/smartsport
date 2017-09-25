package top.smartsport.www.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.utils.pay.PayUtil;

/**
 * Created by Aaron on 2017/7/28.
 * 比赛--订单确认
 */
@ContentView(R.layout.activity_order_cm)
public class OrderCMActivity extends BaseActivity {

    @ViewInject(R.id.tv_money)
    private TextView tv_money;
    @ViewInject(R.id.tv_time)
    private TextView tv_time;
    @ViewInject(R.id.iv_sel_zhifubao)
    private ImageView iv_sel_zhifubao;
    @ViewInject(R.id.iv_sel_weixin)
    private ImageView iv_sel_weixin;
    @ViewInject(R.id.iv_sel_xianxia)
    private ImageView iv_sel_xianxia;

    private ImageView[] ivPay;

    private int type; // 1: 支付宝支付；  2：微信支付；  3：线下支付

    @Override
    protected void initView() {
        type = 1;
        Bundle bundel = getIntent().getExtras();
        tv_money.setText("¥" + bundel.getString("total"));
        ivPay = new ImageView[3];
        ivPay[0] = iv_sel_zhifubao;
        ivPay[1] = iv_sel_weixin;
        ivPay[2] = iv_sel_xianxia;
        String type = bundel.getString("type");
        selPayType(Integer.valueOf(type).intValue());
        id = bundel.getString("product_id");
    }

    String id;

    @Event(value = {R.id.rl_zhifubao, R.id.rl_weixin, R.id.rl_xianxia})
    private void getEvent(View view) {
        String action = "";
        switch (view.getId()) {
            case R.id.rl_zhifubao://支付宝支付
                type = 1;
                selPayType(1);
                action = "aliPay";
                break;
            case R.id.rl_weixin://微信宝支付
                type = 2;
                selPayType(2);
                action = "wxPay";
                break;
            case R.id.rl_xianxia://线下宝支付
                type = 3;
                selPayType(3);
                break;
        }
        PayUtil.pay(this, action, "2", tv_money.getText().toString(), id);
    }

    private void selPayType(int index) {
        for (int i = 0; i < 3; i++) {
            if (index == (i + 1)) {
                ivPay[i].setImageResource(R.mipmap.check_down);
            } else {
                ivPay[i].setImageResource(R.mipmap.check_up);
            }
        }
    }

}
