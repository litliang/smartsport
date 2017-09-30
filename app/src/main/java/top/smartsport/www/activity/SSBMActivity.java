package top.smartsport.www.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.BMmatch;
import top.smartsport.www.bean.BMmyteam;
import top.smartsport.www.bean.BMvideo;
import top.smartsport.www.bean.Data;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.SSBMOrder;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.StringUtil;
import top.smartsport.www.widget.FloatOnKeyboardLayout;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/27.
 * 赛事报名
 */
@ContentView(R.layout.activity_ssbm)
public class SSBMActivity extends BaseActivity {
    public static final String TAG = SSBMActivity.class.getName();

    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    private String id;
    private String packageid;

    @ViewInject(R.id.ssbm_img_pic)
    private ImageView ssbm_img_pic;
    @ViewInject(R.id.ssbm_text_title)
    private TextView ssbm_text_title;
    @ViewInject(R.id.ssbm_text_time)
    private TextView ssbm_text_time;
    @ViewInject(R.id.ssbm_text_adress)
    private TextView ssbm_text_adress;
    @ViewInject(R.id.ssbm_text_price)
    private TextView ssbm_text_price;
    @ViewInject(R.id.ssbm_text_prices)
    private TextView ssbm_text_prices;
    @ViewInject(R.id.ssbm_text_sell_price)
    private TextView ssbm_text_sell_price;
    @ViewInject(R.id.ssbm_text_sell_prices)
    private TextView ssbm_text_sell_prices;
    @ViewInject(R.id.ssbm_text_dingzhi_video)
    private TextView ssbm_text_dingzhi_video;
    @ViewInject(R.id.ssbm_text_baoming_qiudui)
    private TextView ssbm_text_baoming_qiudui;
    @ViewInject(R.id.ssbm_text_people_num)
    private TextView ssbm_text_people_num;
    @ViewInject(R.id.ssbm_text_people_name)
    private TextView ssbm_text_people_name;
    @ViewInject(R.id.ssbm_text_people_phone)
    private TextView ssbm_text_people_phone;
    @ViewInject(R.id.ssbm_refund_tv)
    private TextView refund_tv;
    @ViewInject(R.id.ssbm_disclaimer_tv)
    private TextView disclaimer_tv;

    private Context mContext;
    private BMmatch bMmatch;
    private BMvideo bMvideo;
    private BMmyteam bMmyteam;
    private String total, teamId;
    private String team_name;

    @Override
    protected void initView() {
        mContext = SSBMActivity.this;
        id = (String) getObj(SSBMActivity.TAG);

        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();
        FloatOnKeyboardLayout floatOnKeyboardLayout = (FloatOnKeyboardLayout) findViewById(R.id.float_on_keyboard_layout);
        floatOnKeyboardLayout.setView(findViewById(R.id.inputphone));
        getData();
        findViewById(R.id.buycustomvideo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getBaseContext(), ActivityBuyCustomVedio.class), CHANGE_CUSTOM_VEDIO);
            }
        });
    }


    @Event(value = {R.id.ssbm_rl_dui, R.id.ssbm_pay, R.id.ssbm_refund_tv, R.id.ssbm_disclaimer_tv})
    private void getEvent(View view) {
        Intent intent_temp;
        switch (view.getId()) {
            case R.id.ssbm_rl_dui://修改球队
                startActivityForResult(new Intent(getBaseContext(), ChangeQDActivity.class), CHANGE_QD);
                break;
            case R.id.ssbm_pay:
//                goActivity(OrderCMActivity.class);//去支付
                judgeValue();
                break;
            case R.id.ssbm_refund_tv:
                //退款
                intent_temp = new Intent(this, AboutServiceActivity.class);
                intent_temp.putExtra("type", "refund");
                startActivity(intent_temp);
                break;
            case R.id.ssbm_disclaimer_tv:
                //免责
                intent_temp = new Intent(this, AboutServiceActivity.class);
                intent_temp.putExtra("type", "disclaimer");
                startActivity(intent_temp);
                break;
        }
    }

    /**
     * 赛事报名接口
     */
    private void getData() {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "matchApply");
            json.put("match_id", id);
            json.put("team_id", teamId);
            if (packageid != null) {
                json.put("package_id", packageid);
            }
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
                Data data = entity.toObj(Data.class);
                bMmatch = data.toMatch(BMmatch.class);
                bMvideo = data.toVideo(BMvideo.class);
                bMmyteam = data.toMyteam(BMmyteam.class);
                ImageLoader.getInstance().displayImage(bMmatch.getCover(), ssbm_img_pic, ImageUtil.getOptions(), ImageUtil.getImageLoadingListener(true));
                ssbm_text_title.setText(bMmatch.getName());
                ssbm_text_time.setText(bMmatch.getStart_time());
                ssbm_text_adress.setText(bMmatch.getCounty());
                ssbm_text_sell_price.setText("¥" + bMmatch.getSell_price());
                ssbm_text_price.setText("¥" + bMmatch.getPrice());
                ssbm_text_price.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); //中间横线
                ssbm_text_sell_prices.setText("¥" + bMvideo.getSell_price());
                // 总金额（两数相加）
                setprice();
                ssbm_text_baoming_qiudui.setText(bMmyteam.getTeam_name());
                ssbm_text_people_name.setText(bMmyteam.getCoath_name());
                ssbm_text_people_num.setText(bMmyteam.getMembers());
                ssbm_text_people_phone.setText(bMmyteam.getCoath_mobile());
                ssbm_text_dingzhi_video.setText(bMvideo.getName());
                teamId = bMmyteam.getId();
            }
        });
    }

    private void setprice() {
        float money1 = Float.parseFloat(ssbm_text_sell_price.getText().toString().replace("¥", ""));
        float money2 = Float.parseFloat(ssbm_text_sell_prices.getText().toString().replace("¥", ""));
        float totalValue = money1 + money2;
        total = StringUtil.strToDouble("" + totalValue);
        ssbm_text_prices.setText("¥" + total);
    }

    // 判断联系人、联系电话是否为空
    private void judgeValue() {
        String peoplePame = ssbm_text_people_name.getText().toString();
        if (StringUtil.isEmpty(peoplePame)) {
            Toast.makeText(mContext, "联系人不能为空哦！", Toast.LENGTH_LONG).show();
            return;
        }
        String phone = ssbm_text_people_phone.getText().toString();
        boolean isPhone = StringUtil.checkMobile(mContext, phone);
        if (isPhone) {
            // TODO 创建订单
            getDetail(peoplePame, phone);
        }
    }

    private void getDetail(String peoplePame, String phone) {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "matchApplyPay");
            json.put("total", total);
            json.put("match_id", id);
            json.put("team_id", teamId);
            json.put("members", bMmyteam != null ? bMmyteam.getMembers() : "11");
            json.put("coach_name", peoplePame);
            json.put("coach_mobile", phone);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(NetEntity entity) {
                SSBMOrder bmOrder = entity.toObj(SSBMOrder.class);
                Bundle bundle = new Bundle();
//                {"total":"200.00","type":2,"product_id":"4"}
                bundle.putString("total", bmOrder.getTotal());
                bundle.putString("type", bmOrder.getType());
                bundle.putString("product_id", bmOrder.getProduct_id());
                goActivity(OrderCMActivity.class, bundle);
            }
        });
    }

    public static final int CHANGE_QD = 0;
    public static final int CHANGE_CUSTOM_VEDIO = 999;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CHANGE_QD:
                if (data != null) {
                    if (data.getStringExtra("team_id") == null || data.getStringExtra("team_id").equals("")) {

                    } else {
                        teamId = data.getStringExtra("team_id");
                    }
                    if (data.getStringExtra("team_name") == null || data.getStringExtra("team_name").equals("")) {

                    } else {
                        team_name = data.getStringExtra("team_name");
                        ssbm_text_baoming_qiudui.setText(team_name);
                    }
                }
                break;
            case CHANGE_CUSTOM_VEDIO:
                if (data != null && data.getStringExtra("sell_price") != null) {
                    String sell_price = (String) data.getStringExtra("sell_price");
                    getTextView(R.id.ssbm_text_sell_prices).setText(sell_price);
                    setprice();
                    getTextView(R.id.ssbm_text_dingzhi_video).setText((String) data.getStringExtra("title"));
                    packageid = (String) data.getStringExtra("id");
                }
                break;
            default:
                break;
        }
    }
}
