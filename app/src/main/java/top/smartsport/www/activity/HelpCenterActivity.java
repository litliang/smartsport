package top.smartsport.www.activity;

import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;

/**
 * Created by Aaron on 2017/7/18.
 * 设置
 */
@ContentView(R.layout.activity_helpcenter)
public class HelpCenterActivity extends BaseActivity{

    @Override
    protected void initView() {

    }

    @Event(value = {R.id.set_rl_tsxx_set,R.id.set_rl_about,R.id.hc})
    private void getEvent(View v){
        switch (v.getId()){
            case R.id.set_rl_about://关于我们
                goActivity(ActivityAbout.class);
                break;
            case R.id.set_rl_tsxx_set://推送消息设置
                goActivity(TSXXSetActivity.class);
                break;
            case R.id.hc://推送消息设置
                goActivity(HelpCenterActivity.class);
                break;

        }
    }
}
