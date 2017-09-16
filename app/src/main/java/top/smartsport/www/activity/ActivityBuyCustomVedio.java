package top.smartsport.www.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;

/**
 * Created by bajieaichirou on 17/9/16.
 */
@ContentView(R.layout.activity_buy_custom_vedio)
public class ActivityBuyCustomVedio extends BaseActivity implements View.OnClickListener{
    private LinearLayout mBuyAll, mBuyBll, mBuyCll, mBuyNoll;
    private ImageView mBuyAIv, mBuyBIv, mBuyCIv, mBuyNoIv;
    private TextView mPayTv;

    @Override
    protected void initView() {
        initUI();
    }

    private void initUI() {
        mBuyAll = (LinearLayout) findViewById(R.id.buy_a_ll);
        mBuyBll = (LinearLayout) findViewById(R.id.buy_b_ll);
        mBuyCll = (LinearLayout) findViewById(R.id.buy_c_ll);
        mBuyNoll = (LinearLayout) findViewById(R.id.buy_no_ll);

        mBuyAIv = (ImageView) findViewById(R.id.buy_a_iv);
        mBuyBIv = (ImageView) findViewById(R.id.buy_b_iv);
        mBuyCIv = (ImageView) findViewById(R.id.buy_c_iv);
        mBuyNoIv = (ImageView) findViewById(R.id.buy_no_iv);

        mPayTv = (TextView) findViewById(R.id.buy_pay);

        mBuyAll.setOnClickListener(this);
        mBuyBll.setOnClickListener(this);
        mBuyCll.setOnClickListener(this);
        mBuyNoll.setOnClickListener(this);
        mPayTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.buy_a_ll:
                mBuyAIv.setImageResource(R.mipmap.radio_checked);
                mBuyBIv.setImageResource(R.mipmap.radio_uncheck);
                mBuyCIv.setImageResource(R.mipmap.radio_uncheck);
                mBuyNoIv.setImageResource(R.mipmap.radio_uncheck);
                break;
            case R.id.buy_b_ll:
                mBuyAIv.setImageResource(R.mipmap.radio_uncheck);
                mBuyBIv.setImageResource(R.mipmap.radio_checked);
                mBuyCIv.setImageResource(R.mipmap.radio_uncheck);
                mBuyNoIv.setImageResource(R.mipmap.radio_uncheck);
                break;
            case R.id.buy_c_ll:
                mBuyAIv.setImageResource(R.mipmap.radio_uncheck);
                mBuyBIv.setImageResource(R.mipmap.radio_uncheck);
                mBuyCIv.setImageResource(R.mipmap.radio_checked);
                mBuyNoIv.setImageResource(R.mipmap.radio_uncheck);
                break;
            case R.id.buy_no_ll:
                mBuyAIv.setImageResource(R.mipmap.radio_uncheck);
                mBuyBIv.setImageResource(R.mipmap.radio_uncheck);
                mBuyCIv.setImageResource(R.mipmap.radio_uncheck);
                mBuyNoIv.setImageResource(R.mipmap.radio_checked);
                break;
            case R.id.buy_pay:
                //TODO 支付
                break;

        }

    }
}
