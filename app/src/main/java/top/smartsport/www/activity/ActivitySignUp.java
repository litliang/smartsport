package top.smartsport.www.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.AutoLinearLayout;

import org.xutils.view.annotation.ContentView;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import app.base.MapConf;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.widget.FloatOnKeyboardLayout;

/**
 * Created by bajieaichirou on 17/8/17.
 */
@ContentView(R.layout.activity_sign_up)
public class ActivitySignUp extends BaseActivity implements View.OnClickListener{

    ImageView mIv;
    TextView mTitleTv, mTitleHintTv, mPriceTv,
            mOldPriceTv, mTotalPriceTv, mMemberNameTv,
            mContactTv, mPhoneTv, mRefundTv, mDisclaimerTv;
    AutoLinearLayout mMemberLayout, mContactLayout, mPhoneLayout;
    Button mPayBtn;
    private String enbale = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        initUI();
    }

    private void initUI() {
        back();

        final Map parammap = (Map) getIntent().getSerializableExtra("data");
        enbale = (String) parammap.get("enable");
        enbale = enbale == null?"":enbale;
        mIv = (ImageView) findViewById(R.id.sign_up_iv);
        mTitleTv = (TextView) findViewById(R.id.sign_up_title_tv);
        mPriceTv = (TextView) findViewById(R.id.sign_up_price_tv);
        mOldPriceTv = (TextView) findViewById(R.id.sign_up_old_price_tv);
        mTotalPriceTv = (TextView) findViewById(R.id.sign_up_total_price_tv);
        mMemberNameTv = (TextView) findViewById(R.id.sign_up_member_tv);
        mContactTv = (TextView) findViewById(R.id.sign_up_contact_tv);
        mPhoneTv = (TextView) findViewById(R.id.sign_up_phone_tv);
        mRefundTv = (TextView) findViewById(R.id.sign_up_refund_tv);
        mDisclaimerTv = (TextView) findViewById(R.id.sign_up_disclaimer_tv);
        mMemberLayout = (AutoLinearLayout) findViewById(R.id.sign_up_member_layout);
        mContactLayout = (AutoLinearLayout) findViewById(R.id.sign_up_contact_layout);
        mPhoneLayout = (AutoLinearLayout) findViewById(R.id.sign_up_phone_layout);
        mPayBtn = (Button) findViewById(R.id.sign_up_pay_btn);

        mMemberLayout.setOnClickListener(this);
        mContactLayout.setOnClickListener(this);
        mPhoneLayout.setOnClickListener(this);
        mRefundTv.setOnClickListener(this);
        mDisclaimerTv.setOnClickListener(this);
        FloatOnKeyboardLayout floatOnKeyboardLayout = (FloatOnKeyboardLayout) findViewById(R.id.float_on_keyboard_layout);
        floatOnKeyboardLayout.setView(findViewById(R.id.sign_up_phone_layout));
        mPayBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mPayBtn.setEnabled(false);
//                MapConf.with(ActivitySignUp.this).pair("total","sign_up_price_tv:￥%s/年").pair("match_id",":").pair("team_id",":").pair("members",":").pair("coach_name","").pair("coach_mobile","").toMap();
//                BaseActivity.callHttp(MapBuilder.build().add("action", "matchApplyPay").add("total", "0.01").add("match_id", "").add("team_id", "").add("members", "").add("coach_name", "").add("coach_mobile", "").get(), new FunCallback() {
//
//                    @Override
//                    public void onSuccess(Object result, List object) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Object result, List object) {
//                        mPayBtn.setEnabled(true);
//                    }
//
//                    @Override
//                    public void onCallback(Object result, List object) {
//                        String root = ((NetEntity) result).getData().toString();
//                        startActivity(new Intent(getBaseContext(), ActivityOrderConfirm.class).putExtra("data", root));
////                MapConf.with(BSSignUpActivity.this).pair("->").source()
//                    }
//                });

                final Map map = MapConf.with(getBaseContext()).pair("total->sign_up_total_price_tv").pair("player->sign_up_member_tv").pair("contact->sign_up_contact_tv").pair("contact_mobile->sign_up_phone_tv").toMap(ActivitySignUp.this);
                callHttp(MapBuilder.withMap(map).add("action", "qxCourseApplyPay").add("qx_course_id", parammap.get("qx_course_id").toString()).get(), new FunCallback() {
                    @Override
                    public void onSuccess(Object result, List object) {
                        Map p = MapBuilder.build().add("type","1").add("product_id",parammap.get("qx_course_id").toString()).add("total",map.get("total").toString()).get();
                        startActivity(new Intent(getBaseContext(), ActivityOrderConfirm.class).putExtra("data", (Serializable) p));

                    }

                    @Override
                    public void onFailure(Object result, List object) {
                        Toast.makeText(ActivitySignUp.this,result.toString(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCallback(Object result, List object) {
                        mPayBtn.setEnabled(true);
                    }
                });
            }
        });

        MapConf.with(getBaseContext()).pair("details_title_iv->sign_up_iv").pair("details_title_tv->sign_up_title_tv")
                .pair("details_time_tv->sign_up_hint_date_tv")
                .pair("details_training_ground_tv->sign_up_hint_location_tv")
                .pair("details_amount_tv->sign_up_price_tv","","replace(/年)").pair("details_amount_tv->sign_up_total_price_tv","","replace(/年)")
                .source(parammap,this).toView();
        getTextView(R.id.sign_up_total_price_tv).setText(getTextString(R.id.sign_up_price_tv).replace("/年",""));
        if(enbale.equals("true")){
//            getView(R.id.sign_up_phone_tv).setEnabled(false);
//            getView(R.id.sign_up_contact_tv).setEnabled(false);


//
//            getView(R.id.sign_up_phone_iv).setEnabled(false);
//            getView(R.id.sign_up_contact_iv).setEnabled(false);
            getView(R.id.sign_up_phone_iv).setVisibility(View.GONE);
            getView(R.id.sign_up_contact_iv).setVisibility(View.GONE);

//            getView(R.id.sign_up_member_iv).setEnabled(false);
            getView(R.id.sign_up_member_iv).setVisibility(View.GONE);

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent_temp;
        switch (v.getId()){
            case R.id.sign_up_member_layout:
                break;
            case R.id.sign_up_contact_layout:
                break;
            case R.id.sign_up_phone_layout:
                break;
            case R.id.sign_up_refund_tv:
                //退款
                intent_temp = new Intent(this, AboutServiceActivity.class);
                intent_temp.putExtra("type", "refund");
                startActivity(intent_temp);
                break;
            case R.id.sign_up_disclaimer_tv:
                //免责
                intent_temp = new Intent(this, AboutServiceActivity.class);
                intent_temp.putExtra("type", "disclaimer");
                startActivity(intent_temp);
                break;
        }
    }
}
