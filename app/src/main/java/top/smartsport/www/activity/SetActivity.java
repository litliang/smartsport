package top.smartsport.www.activity;

import android.view.View;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import java.io.File;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.utils.GlideCatchConfig;
import top.smartsport.www.utils.GlideCatchUtil;

/**
 * Created by Aaron on 2017/7/18.
 * 设置
 */
@ContentView(R.layout.activity_set)
public class SetActivity extends BaseActivity {

    @Override
    protected void initView() {
        ((TextView) findViewById(R.id.set_qctphc)).setText(GlideCatchUtil.getInstance().getCacheSize());
    }

    @Event(value = {R.id.set_rl_tsxx_set, R.id.set_rl_about, R.id.hc, R.id.set_qctphc})
    private void getEvent(View v) {
        switch (v.getId()) {
            case R.id.set_rl_about://关于我们
                goActivity(ActivityAbout.class);
                break;
            case R.id.set_rl_tsxx_set://推送消息设置
                goActivity(TSXXSetActivity.class);
                break;
            case R.id.hc://推送消息设置
                goActivity(HelpCenterActivity.class);
                break;
            case R.id.set_qctphc://推送消息设置
                GlideCatchUtil.getInstance().cleanCatchDisk();
                GlideCatchUtil.getInstance().clearCacheDiskSelf();
                GlideCatchUtil.getInstance().clearCacheMemory();

                ((TextView) findViewById(R.id.set_qctphc)).setText(GlideCatchUtil.getInstance().getCacheSize());
                break;
        }
    }
}

