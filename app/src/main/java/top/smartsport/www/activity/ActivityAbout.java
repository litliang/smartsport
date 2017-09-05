package top.smartsport.www.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhy.autolayout.AutoRelativeLayout;

import org.xutils.view.annotation.ContentView;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;

/**
 * Created by bajieaichirou on 17/8/27.
 */
@ContentView(R.layout.activity_about)
public class ActivityAbout extends BaseActivity implements View.OnClickListener{

    private ImageView mIconIv;
    private TextView mVersionTv, mServicePhoneTv, mEmailTv;
    private AutoRelativeLayout mAgreementLayout;

    @Override
    protected void initView() {
        initUI();
    }

    private void initUI() {
        mIconIv = (ImageView) findViewById(R.id.about_icon_iv);
        mVersionTv = (TextView) findViewById(R.id.about_app_version);
        mServicePhoneTv = (TextView) findViewById(R.id.about_service_phone);
        mEmailTv = (TextView) findViewById(R.id.about_email_address);
        mAgreementLayout = (AutoRelativeLayout) findViewById(R.id.about_agreement_layout);
        mAgreementLayout.setOnClickListener(this);
        mServicePhoneTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.about_agreement_layout:
                //TODO 服务协议
                break;
            case R.id.about_service_phone:
                //TODO 服务协议
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + ((TextView)v).getText().toString()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
        }
    }
}
