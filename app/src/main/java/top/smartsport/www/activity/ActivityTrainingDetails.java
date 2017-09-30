package top.smartsport.www.activity;

import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;

import java.io.Serializable;
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
import top.smartsport.www.utils.StringUtil;
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
            mGroundTv, mQuotaTv;
    HorizontalListView mHorizontaList;
    Button mSignUpBtn;
    WebView mIntroductionTv;
    TrainingClassBean classBean;
    AdapterTrainingDetails mClassAdapter;

    List<TrainingClassBean> classList = new ArrayList<>();

    String id;
    String data;
    private int position;

    @Override
    protected void initView() {
        initUI();
    }

    private void initUI() {
        back();
        fav();

        position = getIntent().getIntExtra("position", -1);
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
        mIntroductionTv = (WebView) findViewById(R.id.details_introduction_tv);
        mQuotaTv = (TextView) findViewById(R.id.details_quota_tv);
        mHorizontaList = (HorizontalListView) findViewById(R.id.details_class_listview);
        mSignUpBtn = (Button) findViewById(R.id.details_sign_up_btn);

//        for (int mPos = 0; mPos < 5; mPos++) {
//            classBean = new TrainingClassBean();
//            classBean.setClassTitle("青训瑜伽" + mPos);
//            classBean.setClassPrice("￥" + mPos + 1000 + mPos * 3);
//            classList.add(classBean);
//        }

        mClassAdapter = new AdapterTrainingDetails(this, classList);
        mHorizontaList.setAdapter(mClassAdapter);
        mHorizontaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getBaseContext(), ActivityTrainingDetails.class).putExtra("id", ((Map) parent.getItemAtPosition(position)).get("id").toString()));
            }
        });

        callHttp(MapBuilder.build().add("action", "getQxCourseDetail").add("id", id = getIntent().getSerializableExtra("id").toString()).get(), new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {
                data = ((NetEntity) result).getData().toString();
                String detail = JsonUtil.findJsonLink("detail", data).toString();

                final String coachid = JsonUtil.findJsonLink("detail-coach_id", data).toString();
                final String order_status = JsonUtil.findJsonLink("detail-order_status", data).toString();

                MapConf.build().with(ActivityTrainingDetails.this)
                        .pair("collect_status->ivRight_text", "0:mipmap.fav_undo;1:mipmap.fav_done")
                        .pair("title->details_title_tv")
                        .pair("start_time->details_date_tv")
                        .pair("address->details_address_tv")
                        .pair("level:U%s->details_img")
                        .pair("surplus:还剩%s个名额->details_quota_tv")
                        .pair("sell_price:￥%s/年->details_amount_tv")
                        .pair("coach_name->details_name_tv")
                        .pair("cover_url->details_title_iv", "", "scaleToWidth()")
                        .pair("coach_header->details_icon_iv")
                        .pair("school->details_school_name_tv")
                        .pair("schedules->details_time_tv")
                        .pair("recruit_students->details_student_tv")
                        .pair("content->details_introduction_tv")
                        .pair("sell_price:我要报名(￥%s/年)->details_sign_up_btn")
                        .pair("other_course->details_class_listview", MapConf.with(ActivityTrainingDetails.this).pair("cover_url->class_iv").pair("title->class_title_tv").pair("sell_price:￥%s/年->class_price_tv"))
                        .source(detail, getWindow().getDecorView()).toView();
                String status = JsonUtil.findJsonLink("detail-status", data).toString();
                if (!StringUtil.isEmpty(status)) {
                    // 1报名中2进行中 3已结束 4已报满5已报名
                    if (status.equals("1")) { // 报名中
                        final String sellPrice = JsonUtil.findJsonLink("detail-sell_price", data).toString();
                        mSignUpBtn.setText("我要报名(￥" + sellPrice + "/年)");
                        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                Map map = MapConf.with(getBaseContext()).toMap(ActivityTrainingDetails.this);
                                map.put("qx_course_id", id);
                                startActivity(new Intent(getBaseContext(), ActivitySignUp.class).putExtra("data", (Serializable) map));
                            }
                        });
                    } else if (status.equals("2")) { // 进行中
                        mSignUpBtn.setText("进行中");
                    } else if (status.equals("3")) { // 已结束
                        mSignUpBtn.setText("已结束");
                    } else if (status.equals("4")) { // 已报满
                        mSignUpBtn.setText("已报满");
                    } else if (status.equals("5")) { // 已报名
                        mSignUpBtn.setText("已报名");
                    }
//                    if(order_status==null||order_status.equals("")){
//
//                    }else if(order_status.equals("1")){
//                        mSignUpBtn.setText("已报名");
//                    }else if(order_status.equals("0")){
//                        mSignUpBtn.setText("已报名");
//                    }
                } else {
                    mSignUpBtn.setVisibility(View.GONE);
                }
                MapConf.with(ActivityTrainingDetails.this).pair("other_course->details_class_listview", MapConf.with(ActivityTrainingDetails.this).pair("cover_url->class_iv").pair("title->class_title_tv").pair("sell_price:￥%s/年->class_price_tv").source(R.layout.adapter_class_item)).source(data, getWindow().getDecorView()).toView();
                setShareurl(((app.base.widget.ImageView) getImageView(R.id.details_title_iv)).getUrl());
                setSharetitle(getTextView(R.id.details_title_tv).getText().toString());
                setSharetxt(((WebView) getView(R.id.details_introduction_tv)).getTitle());
                getView(R.id.details_icon_iv).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getBaseContext(), CoachDetailActivity.class).putExtra("id", coachid));
                    }
                });
            }

            @Override
            public void onFailure(Object result, List object) {

            }

            @Override
            public void onCallback(Object result, List object) {
            }
        });

//        findViewById(R.id.details_collect_iv).setOnClickListener(new View.OnClickListener() {
//
//            Boolean unfavored = null;
//
//            @Override
//            public void onClick(View view) {
//                if(unfavored ==null) {
//                    unfavored = JsonUtil.findJsonLink("detail-collect_status", data).toString().equals("0");
//                }
//
//
//
//                favImpl(view, unfavored);
//                unfavored = !unfavored;
//            }
//        });

    }

    @Override
    public void favImpl(View view, boolean unfav) {
        fav.run(view, unfav + "", 1, id, "mipmap.fav_done", "mipmap.fav_undo");

    }
}
