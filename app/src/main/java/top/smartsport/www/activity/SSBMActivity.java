package top.smartsport.www.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.utils.ImageUtil;
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



    @Override
    protected void initView() {

        id = (String) getObj(SSBMActivity.TAG);

        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();

        getData();

    }

    @Event(value = {R.id.ssbm_rl_dui,R.id.ssbm_pay})
    private void getEvent(View view){
        switch (view.getId()){
            case R.id.ssbm_rl_dui://修改球队
                goActivity(ChangeQDActivity.class);
                break;
            case R.id.ssbm_pay:
                goActivity(OrderCMActivity.class);//去支付
                break;
        }
    }

    /**
     * 赛事报名接口
     * */
    private void getData(){
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","matchApply");
            json.put("match_id",id);
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
                BMmatch bMmatch=data.toMatch(BMmatch.class);
                BMvideo bMvideo = data.toVideo(BMvideo.class);
                BMmyteam bMmyteam = data.toMyteam(BMmyteam.class);
                ImageLoader.getInstance().displayImage(bMmatch.getCover(), ssbm_img_pic, ImageUtil.getOptions());
                ssbm_text_title.setText(bMmatch.getName());
                ssbm_text_time.setText(bMmatch.getStart_time());
                ssbm_text_adress.setText(bMmatch.getCounty());
                ssbm_text_sell_price.setText(bMmatch.getSell_price());
                ssbm_text_price.setText(bMmatch.getPrice());
                ssbm_text_sell_prices.setText(bMmatch.getSell_price());
                ssbm_text_prices.setText(bMmatch.getPrice());
                ssbm_text_baoming_qiudui.setText(bMmyteam.getTeam_name());
                ssbm_text_people_name.setText(bMmyteam.getCoath_name());
                ssbm_text_people_num.setText(bMmyteam.getMembers());
                ssbm_text_people_phone.setText(bMmyteam.getCoath_mobile());
                ssbm_text_dingzhi_video.setText(bMvideo.getName());
            }
        });
    }
}
