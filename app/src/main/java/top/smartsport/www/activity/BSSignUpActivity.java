package top.smartsport.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoLinearLayout;

import org.xutils.view.annotation.ContentView;

import java.util.List;

import app.base.MapConf;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;

/**
 * Created by bajieaichirou on 17/8/17.
 * 比赛报名
 */
@ContentView(R.layout.activity_bs_sign_up)
public class BSSignUpActivity extends BaseActivity implements View.OnClickListener {

    ImageView mIv;
    TextView mTitleTv, mTitleHintTv, mPriceTv,
            mOldPriceTv, mTotalPriceTv,  mRefundTv,
            mDisclaimerTv, mBuyVideoPriceTv, mBuyVideoContentTv,
             mTeamNameEdt, mMemberNameTv, mContactTv, mPhoneTv;
    AutoLinearLayout mTeamLayout, mMemberLayout, mContactLayout, mPhoneLayout;
    Button mPayBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        initUI();
        setTitle(getResources().getString(R.string.bs_sign_up_title));
    }

    private void initUI() {
        mIv = (ImageView) findViewById(R.id.bs_sign_up_iv);
        mTitleTv = (TextView) findViewById(R.id.bs_sign_up_title_tv);
        mTitleHintTv = (TextView) findViewById(R.id.bs_sign_up_hint_tv);
        mPriceTv = (TextView) findViewById(R.id.bs_sign_up_price_tv);
        mOldPriceTv = (TextView) findViewById(R.id.bs_sign_up_old_price_tv);
        mTotalPriceTv = (TextView) findViewById(R.id.bs_sign_up_total_price_tv);
        mTeamNameEdt = (TextView) findViewById(R.id.bs_sign_up_team_tv);
        mMemberNameTv = (TextView) findViewById(R.id.bs_sign_up_member_tv);
        mContactTv = (TextView) findViewById(R.id.bs_sign_up_contact_tv);
        mPhoneTv = (TextView) findViewById(R.id.bs_sign_up_phone_tv);
        mRefundTv = (TextView) findViewById(R.id.bs_sign_up_refund_tv);
        mDisclaimerTv = (TextView) findViewById(R.id.bs_sign_up_disclaimer_tv);
        mBuyVideoPriceTv = (TextView) findViewById(R.id.bs_buy_video_price_tv);
        mBuyVideoContentTv = (TextView) findViewById(R.id.bs_buy_video_content);
        mTeamLayout = (AutoLinearLayout) findViewById(R.id.bs_sign_up_team_layout);
        mMemberLayout = (AutoLinearLayout) findViewById(R.id.bs_sign_up_member_layout);
        mContactLayout = (AutoLinearLayout) findViewById(R.id.bs_sign_up_contact_layout);
        mPhoneLayout = (AutoLinearLayout) findViewById(R.id.bs_sign_up_phone_layout);
        mPayBtn = (Button) findViewById(R.id.bs_sign_up_pay_btn);

        mMemberLayout.setOnClickListener(this);
        mContactLayout.setOnClickListener(this);
        mPhoneLayout.setOnClickListener(this);

        mPayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mPayBtn.setEnabled(false);
                BaseActivity.callHttp(MapBuilder.build().add("action", "matchApplyPay").add("total", "").add("match_id", "").add("team_id", "").add("members", "").add("coach_name", "").add("coach_mobile", "").get(), new FunCallback() {
                    @Override
                    public void onSuccess(Object result, List object) {

                    }

                    @Override
                    public void onFailure(Object result, List object) {
                        mPayBtn.setEnabled(true);
                    }

                    @Override
                    public void onCallback(Object result, List object) {
                        String root = ((NetEntity) result).getData().toString();
                        startActivity(new Intent(getBaseContext(), ActivityOrderConfirm.class).putExtra("data", root));
//                MapConf.with(BSSignUpActivity.this).pair("->").source()
                    }
                });
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up_member_layout:
                break;
            case R.id.sign_up_contact_layout:
                break;
            case R.id.sign_up_phone_layout:
                break;
        }
    }
}
