package top.smartsport.www.fragment;

import android.os.Bundle;
import android.view.View;

import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import java.util.List;

import app.base.MapConf;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.activity.*;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.utils.SPUtils;

/**
 * Created by Aaron on 2017/7/13.
 * 我的
 */
@ContentView(R.layout.fragment_wd)
public class WDV4Fragment extends BaseV4Fragment {
    public static final String TAG = "WDV4Fragment";

    public static WDV4Fragment newInstance() {
        WDV4Fragment fragment = new WDV4Fragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void refresh() {
        super.refresh();
    }

    @Override
    protected void initView() {
        initUserInfo();
    }

    public void initUserInfo() {
        BaseActivity.callHttp(MapBuilder.build().add("action", "getUserInfo").get(), new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {

            }

            @Override
            public void onFailure(Object result, List object) {

            }

            @Override
            public void onCallback(Object result, List object) {
                String data = ((NetEntity)result).getData().toString();

                SPUtils.put(getContext(), "getUserInfo", data);
                //MapConf.withView(getContext()).pair("username->status").pair("height:身高：%s cm->height").pair("weight:体重：%s kg->weight").pair("leg:惯用脚：%s ->leg").source(app.base.JsonUtil.extractJsonRightValue(data), root).match();
                MapConf.with(getContext()).addPair("username", R.id.status).addPair("height", R.id.height).addPair("weight", R.id.weight).addPair("leg", R.id.leg).addTackle(new MapConf.Tackle() {


                    @Override
                    public void tackleBefore(Object item, Object value, String name, View convertView, View theView) {

                    }

                    @Override
                    public void tackleAfter(Object item, Object value, String name, View convertView, View theView) {
                        if (name.equals("height")) {
                            ((TextView) theView).setText("身高：" + value);
                        } else if (name.equals("weight")) {
                            ((TextView) theView).setText("体重：" + value);
                        } else if (name.equals("leg")) {
                            String leg = "";
                            if (value.toString().equals("1")) {
                                leg = "左脚";
                            } else if (value.toString().equals("2")) {
                                leg = "右脚";
                            } else if (value.toString().equals("3")) {
                                leg = "左右脚";
                            }
                            ((TextView) theView).setText("惯用脚：" + leg);
                        }
                    }
                }).source(app.base.JsonUtil.extractJsonRightValue(data), root).match();

            }
        });
        String phone = (String) SPUtils.get(getContext(), "USER", "");
        if (phone != null && !phone.equals("")) {
            ((TextView) root.findViewById(R.id.status)).setText("已登录");
            ((TextView) root.findViewById(R.id.phone)).setText(phone);

        }
    }

    @Event(value = {R.id.ll_account_set, R.id.rl_kc, R.id.rl_sc, R.id.rl_sp,
            R.id.rl_qd, R.id.rl_yl, R.id.rl_hy, R.id.rl_dd, R.id.wd_set, R.id.wd_msg})
    private void getEvent(View view) {
        switch (view.getId()) {
            case R.id.ll_account_set://账户设置
                toActivity(AccountSetActivity.class);
                break;
            case R.id.rl_kc://我的课程
                toActivity(MyKCActivity.class);
                break;
            case R.id.rl_sc://我的收藏
                toActivity(MySCActivity.class);
                break;
            case R.id.rl_sp://我的视频
                toActivity(MySPActivity.class);
                break;
            case R.id.rl_qd://我的球队
                toActivity(WDQDActivity.class);
                break;
            case R.id.rl_yl://娱乐
                toActivity(MyYLActivity.class);
                break;
            case R.id.rl_hy://我的会员
                toActivity(MyHYActivity.class);
                break;
            case R.id.rl_dd://我的订单
                toActivity(MyOrderActivity.class);
                break;
            case R.id.wd_set://设置
                toActivity(SetActivity.class);
                break;
            case R.id.wd_msg://消息
                toActivity(MessageActivity.class);
                break;
        }
    }
}
