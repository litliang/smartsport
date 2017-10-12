package top.smartsport.www.activity;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import java.io.File;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.utils.GetFileSizeUtil;
import top.smartsport.www.utils.GlideCatchUtil;

/**
 * Created by Aaron on 2017/7/18.
 * 设置
 */
@ContentView(R.layout.activity_set)
public class SetActivity extends BaseActivity {

    private Context mContext;

    @Override
    protected void initView() {
        mContext = SetActivity.this;
        findViewById(R.id.rate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMarket(view.getContext(), getPackageName(), "http://sj.qq.com/myapp/detail.htm?apkName=top.smartsport.www");

            }
        });
//        ((TextView) findViewById(R.id.set_qctphc)).setText(GlideCatchUtil.getInstance().getCacheSize());
        ((TextView) findViewById(R.id.set_qctphc)).setText(showMemorySize());
    }

    private String showMemorySize() {
        String bytes = "0";
        // 获得文件夹的大小
        File file = mContext.getCacheDir();
        GetFileSizeUtil getFileSizeUtil = GetFileSizeUtil.getInstance();
        try {
            // 得到文件夹的大小
            long size = getFileSizeUtil.getFileSize(file);

            if (size > 0) {
                bytes = getFileSizeUtil.FormetFileSize(size);
            } else {
                bytes = "0M";
            }
            LogUtil.d("------------showMemorySize()---------->" + bytes);
            return bytes;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0M";
    }

    private void getCachDir() {
        new GetFileSizeUtil().clearCacheFolder(mContext.getCacheDir(),
                System.currentTimeMillis());
        Toast.makeText(mContext, "清除缓存成功", Toast.LENGTH_SHORT).show();
        showMemorySize();
    }

    @Event(value = {R.id.set_rl_tsxx_set, R.id.set_rl_about, R.id.hc, R.id.rl_qctphc})
    private void getEvent(View v) {
        switch (v.getId()) {
            case R.id.rate:
                break;
            case R.id.set_rl_about://关于我们
                goActivity(ActivityAbout.class);
                break;
            case R.id.set_rl_tsxx_set://推送消息设置
                goActivity(TSXXSetActivity.class);
                break;
            case R.id.hc://推送消息设置
                goActivity(HelpCenterActivity.class);
                break;
            case R.id.rl_qctphc://推送消息设置
//                GlideCatchUtil.getInstance().cleanCatchDisk();
//                GlideCatchUtil.getInstance().clearCacheDiskSelf();
                GlideCatchUtil.getInstance().clearCacheMemory();
                getCachDir();
                ((TextView) findViewById(R.id.set_qctphc)).setText("0M");
                break;
        }
    }

    public static void goToMarket(Context context, String packageName, String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent it = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(it);
        } catch (ActivityNotFoundException e) {
            Uri uri = Uri.parse("market://details?id=" + packageName);
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

            context.startActivity(goToMarket);
        }

    }
}

