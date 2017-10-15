package top.smartsport.www.activity;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import cn.jpush.android.api.JPushInterface;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.BaseApplication;
import top.smartsport.www.utils.SPUtils;

/**
 * Created by Aaron on 2017/7/19.
 * 推送消息设置
 */
@ContentView(R.layout.activity_tsxx_set)
public class TSXXSetActivity  extends BaseActivity{

    @ViewInject(R.id.rl_receive_msg)
    private RelativeLayout rl_receive_msg;
    @ViewInject(R.id.tv_receive_status)
    private TextView tv_receive_status;
    private boolean receiveStatus;

    @Override
    protected void initView() {
        receiveStatus = (boolean) SPUtils.get(BaseApplication.getApplication(),"receive_status", false);
        if(receiveStatus) {
            tv_receive_status.setText("已打开");
        } else {
            tv_receive_status.setText("已关闭");
        }
    }

    @Event(value = {R.id.rl_receive_msg})
    private void getEvent(View v) {
        switch (v.getId()) {
            case R.id.rl_receive_msg:
                SPUtils.put(getApplicationContext(), "receive_status", !receiveStatus);
                if(!receiveStatus) {
                    tv_receive_status.setText("已打开");
                    JPushInterface.resumePush(getApplicationContext());
                } else {
                    tv_receive_status.setText("已关闭");
                    JPushInterface.stopPush(getApplicationContext());
                }
                break;
        }
    }

}
