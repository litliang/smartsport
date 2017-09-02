package top.smartsport.www.activity;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;


/**
 * Created by Aaron on 2017/7/12.
 * 完善个人资料
 */
@ContentView(R.layout.activity_wszl)
public class WSZLActivity extends BaseActivity {
    @ViewInject(R.id.wszl_text_name)
    private EditText wszl_text_name;//姓名
    private String name;
    @ViewInject(R.id.wszl_text_height)
    private TextView wszl_text_height;//身高
    private String height;
    @ViewInject(R.id.wszl_text_weight)
    private TextView wszl_text_weight;//体重
    private String weight;
    @ViewInject(R.id.wszl_text_sex)
    private TextView wszl_text_sex;//性别
    private String sex;
    @ViewInject(R.id.wszl_text_age)
    private TextView wszl_text_age;//年龄
    private String age;
    @ViewInject(R.id.wszl_text_habit)
    private TextView wszl_text_habit;//惯用脚
    private String habit;

    private static final List<String> options1Items = new ArrayList<>();

    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;


    @Override
    protected void initView() {
        setRightText("跳过");
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();
        getHW();
    }

    @Override
    protected void doRight() {
        goActivity(LoginActivity.class);
    }

    @Event(value = {R.id.wszl_rel_age, R.id.wszl_rel_habit, R.id.wszl_rel_height,
            R.id.wszl_rel_sex, R.id.wszl_rel_weight,R.id.wszl_btn_sure})
    private void getEvent(View v) {
        switch (v.getId()) {
            case R.id.wszl_rel_age:
                getDate();
                break;
            case R.id.wszl_rel_habit:
                getHabit();
                break;
            case R.id.wszl_rel_height:
                getHeight();
                break;
            case R.id.wszl_rel_sex:
                getSex();
                break;
            case R.id.wszl_rel_weight:
                getWeight();
                break;
            case R.id.wszl_btn_sure:
                wszl();
                break;
        }
    }

    /**
     * 选择日期
     */
    public String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void getDate() {
        TimePickerView pvTime = new TimePickerView.Builder(WSZLActivity.this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date2, View v) {//选中事件回调
                String time = getTime(date2);
                wszl_text_age.setText(time);
            }
        })
                .setType(TimePickerView.Type.YEAR_MONTH_DAY)//默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentSize(18)//滚轮文字大小
                .setTitleSize(18)//标题文字大小
                .setTitleText("选择出生日期")//标题文字
                .setOutSideCancelable(true)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTextColorCenter(Color.BLACK)//设置选中项的颜色
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(getResources().getColor(R.color.theme_color))//确定按钮文字颜色
                .setCancelColor(getResources().getColor(R.color.theme_color))//取消按钮文字颜色
//                        .setTitleBgColor(0xFF666666)//标题背景颜色 Night mode
//                        .setBgColor(0xFF333333)//滚轮背景颜色 Night mode
//                        .setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR) + 20)//默认是1900-2100年
//                        .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
//                        .setRangDate(startDate,endDate)//起始终止年月日设定
//                        .setLabel("年","月","日","时","分","秒")
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
//                        .isDialog(true)//是否显示为对话框样式
                .build();
        pvTime.setDate(Calendar.getInstance());//注：根据需求来决定是否使用该方法（一般是精确到秒的情况），此项可以在弹出选择器的时候重新设置当前时间，避免在初始化之后由于时间已经设定，导致选中时间与当前时间不匹配的问题。
        pvTime.show();

    }


    /**
     * 性别
     */
    private void getSex() {
        options1Items.clear();
        options1Items.add("男");
        options1Items.add("女");
        options1Items.add("保密");

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(WSZLActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String s = options1Items.get(options1);
                wszl_text_sex.setText(s);

            }
        })
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(18)//滚轮文字大小
                .setTitleSize(18)//标题文字大小
                .setTitleText("选择性别")//标题文字
                .build();
        pvOptions.setPicker(options1Items);
        pvOptions.show();
    }
    /**
     * 选择身高
     * */
    private List<String> heightLIST= new ArrayList<>();
    private void getHeight(){
//        options1Items.clear();
//        options1Items.add("164cm");
//        options1Items.add("165cm");
//        options1Items.add("166cm");
//        options1Items.add("167cm");
//        options1Items.add("168cm");
//        options1Items.add("169cm");
//        options1Items.add("170cm");
//        options1Items.add("171cm");
//        options1Items.add("172cm");
//        options1Items.add("173cm");
//        options1Items.add("174cm");
//        options1Items.add("175cm");

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(WSZLActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String s = heightLIST.get(options1);
                wszl_text_height.setText(s);

            }
        })
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(18)//滚轮文字大小
                .setTitleSize(18)//标题文字大小
                .setTitleText("选择身高")//标题文字
                .build();
        pvOptions.setPicker(heightLIST);
        pvOptions.show();
    }

    /**
     * 选择体重
     * */
    private List<String> weightLIST= new ArrayList<>();
    private void getWeight(){
//        options1Items.clear();
//        options1Items.add("60kg");
//        options1Items.add("61kg");
//        options1Items.add("62kg");
//        options1Items.add("63kg");
//        options1Items.add("64kg");
//        options1Items.add("65kg");
//        options1Items.add("66kg");
//        options1Items.add("67kg");
//        options1Items.add("68kg");
//        options1Items.add("69kg");
//        options1Items.add("70kg");
//        options1Items.add("71kg");
//        options1Items.add("72kg");
//        options1Items.add("73kg");
//        options1Items.add("74kg");

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(WSZLActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String s = weightLIST.get(options1);
                wszl_text_weight.setText(s);

            }
        })
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(18)//滚轮文字大小
                .setTitleSize(18)//标题文字大小
                .setTitleText("选择体重")//标题文字
                .build();
        pvOptions.setPicker(weightLIST);
        pvOptions.show();
    }

    /**
     * 惯用脚
     * */
    private void getHabit(){
        options1Items.clear();
        options1Items.add("左脚");
        options1Items.add("右脚");
        options1Items.add("左右均衡");
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(WSZLActivity.this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String s = options1Items.get(options1);
                wszl_text_habit.setText(s);

            }
        })
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setContentTextSize(18)//滚轮文字大小
                .setTitleSize(18)//标题文字大小
                .setTitleText("选择惯用脚")//标题文字
                .build();
        pvOptions.setPicker(options1Items);
        pvOptions.show();
    }

    /**
     * 获取身高体重
     * */

    private void getHW(){
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","getHW");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                showToast(message);
            }

            @Override
            public void onSuccess(NetEntity entity) {
                JsonElement jsonElement = entity.getData();
                try {
                    JSONObject json =  new JSONObject(jsonElement.toString());
                    JSONArray height = json.optJSONArray("height");
                    JSONArray weight = json.optJSONArray("weight");
                    for(int i =0;i<height.length();i++){
                        heightLIST.add(height.get(i)+"");
                    }

                    for(int i =0;i<weight.length();i++){
                        weightLIST.add(weight.get(i)+"");
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    /**
     * 用户信息完善确定
     * */
    private void wszl(){
        name = wszl_text_name.getText().toString().trim();
        height = wszl_text_height.getText().toString().trim();
        weight = wszl_text_weight.getText().toString().trim();
        sex = wszl_text_sex.getText().toString().trim();
        age = wszl_text_age.getText().toString().trim();
        habit = wszl_text_habit.getText().toString().trim();

        if(sex.equals("保密"))
            sex ="0";
        if(sex.equals("男"))
            sex="1";
        if(sex.equals("女"))
            sex ="2";
        if(habit.equals("右脚"))
            habit="1";
        if(habit.equals("左脚"))
            habit="2";
        if(habit.equals("左右均衡"))
            habit="3";

        if(name.isEmpty()){
            showToast("请输入姓名");
            return;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","saveBaseUserInfo");
            json.put("truename",name);
            json.put("birthday",age);
            json.put("sex",sex);
            json.put("height",height);
            json.put("weight",weight);
            json.put("leg",habit);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                showToast(message);
            }

            @Override
            public void onSuccess(NetEntity entity) {
                showToast(entity.getMessage());
                goActivity(LoginActivity.class);
                finish();
            }
        });
    }


}
