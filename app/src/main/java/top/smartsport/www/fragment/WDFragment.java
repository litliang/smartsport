package top.smartsport.www.fragment;

import android.os.Bundle;
import android.view.View;

import android.widget.TextView;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import top.smartsport.www.R;
import top.smartsport.www.activity.*;
import top.smartsport.www.base.BaseFragment;
import top.smartsport.www.utils.SPUtils;

/**
 * Created by Aaron on 2017/7/13.
 * 我的
 */
@ContentView(R.layout.fragment_wd)
public class WDFragment extends BaseFragment {
    public static final String TAG = "WDFragment";
    public static WDFragment newInstance() {
        WDFragment fragment = new WDFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    protected void initView() {
        String phone = (String) SPUtils.get(getContext(),"USER","");
        if(phone!=null&&!phone.equals("")){
            ((TextView)root.findViewById(R.id.status)).setText("已登录");
            ((TextView)root.findViewById(R.id.phone)).setText(phone);

        }
    }

    @Event(value = {R.id.ll_account_set,R.id.rl_kc,R.id.rl_sc,R.id.rl_sp,
            R.id.rl_qd,R.id.rl_yl,R.id.rl_hy,R.id.rl_dd,R.id.wd_set,R.id.wd_msg})
    private void getEvent(View view){
        switch (view.getId()){
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
