package top.smartsport.www.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.base.JsonUtil;
import app.base.MapConf;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.adapter.AdapterTrainingDetails;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.TrainingClassBean;
import top.smartsport.www.widget.HorizontalListView;

/**
 * Created by bajieaichirou on 2017/8/21 0021.
 * 青训详情
 */
@ContentView(R.layout.activity_qingxun_details)
public class ActivityTrainingDetails extends BaseActivity {
    TextView mImg;
    ImageView mDetailsIv, mIconImg;
    TextView mDetailsTitleTv, mDateTv, mAddressTv, mAmountTv,
            mNameTv, mLevelTv, mSchoolNameTv, mStudentTv, mTimeTv,
            mGroundTv, mIntroductionTv, mQuotaTv;
    HorizontalListView mHorizontaList;
    Button mSignUpBtn;

    TrainingClassBean classBean;
    AdapterTrainingDetails mClassAdapter;

    List<TrainingClassBean> classList = new ArrayList<>();

    @Override
    protected void initView() {
        initUI();
    }

    private void initUI() {
        mDetailsIv = (ImageView) findViewById(R.id.details_title_iv);
        mDetailsTitleTv = (TextView) findViewById(R.id.details_title_tv);
        mDateTv = (TextView) findViewById(R.id.details_date_tv);
        mAddressTv = (TextView) findViewById(R.id.details_address_tv);
        mImg = (TextView) findViewById(R.id.details_img);
        mAmountTv = (TextView) findViewById(R.id.details_amount_tv);
        mIconImg = (ImageView) findViewById(R.id.details_icon_iv);
        mNameTv = (TextView) findViewById(R.id.details_name_tv);
        mLevelTv = (TextView) findViewById(R.id.details_level_tv);
        mSchoolNameTv = (TextView) findViewById(R.id.details_school_name_tv);
        mStudentTv = (TextView) findViewById(R.id.details_student_tv);
        mTimeTv = (TextView) findViewById(R.id.details_time_tv);
        mGroundTv = (TextView) findViewById(R.id.details_training_ground_tv);
        mIntroductionTv = (TextView) findViewById(R.id.details_introduction_tv);
        mQuotaTv = (TextView) findViewById(R.id.details_quota_tv);
        mHorizontaList = (HorizontalListView) findViewById(R.id.details_class_listview);
        mSignUpBtn = (Button) findViewById(R.id.details_sign_up_btn);
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), ActivitySignUp.class));
            }
        });
        for (int i = 0; i < 5; i++) {
            classBean = new TrainingClassBean();
            classBean.setClassTitle("青训瑜伽" + i);
            classBean.setClassPrice("￥" + i + 1000 + i * 3);
            classList.add(classBean);
        }
        mClassAdapter = new AdapterTrainingDetails(this, classList);
        mHorizontaList.setAdapter(mClassAdapter);


        callHttp(MapBuilder.build().add("action", "getQxCourseDetail").add("id", getIntent().getSerializableExtra("id").toString()).get(), new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {

            }

            @Override
            public void onFailure(Object result, List object) {

            }

            @Override
            public void onCallback(Object result, List object) {
                String data = ((NetEntity)result).getData().toString();
                String detail =  JsonUtil.findJsonLink("detail",data).toString();
                String other_course =  JsonUtil.findJsonLink("other_course",data).toString();
                MapConf.build().with(getBaseContext())
                        .pair("title->details_title_tv")
                        .pair("start_time->details_date_tv")
                        .pair("address->details_address_tv")
                        .pair("level:U%s->details_img")
                        .pair("surplus:还剩%s个名额->details_quota_tv")
                        .pair("status->")
                        .pair("sell_price:￥%s/年->details_amount_tv")
                        .pair("coach_name->details_name_tv")
                        .pair("cover_url->details_title_iv")
                        .pair("coach_header->details_icon_iv")
                        .pair("coach_team->details_school_name_tv")
                        .pair("schedules->details_time_tv")
                        .pair("recruit_students->details_student_tv")
                        .pair("content->details_introduction_tv")
                        .pair("sell_price:我要报名(￥%s/年)->details_sign_up_btn").pair("other_course->details_class_listview",MapConf.build().pair("cover_url->class_iv").pair("title->class_title_tv").pair("sell_price:￥%s/年->class_price_tv")).source(detail,getWindow().getDecorView()).match();
                MapConf.build().with(getBaseContext()).pair("other_course->details_class_listview",MapConf.build().with(getBaseContext()).pair("cover_url->class_iv").pair("title->class_title_tv").pair("sell_price:￥%s/年->class_price_tv").source(R.layout.adapter_class_item)).source(data,getWindow().getDecorView()).match();
            }
        });
    }

}
