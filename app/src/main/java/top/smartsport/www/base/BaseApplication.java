package top.smartsport.www.base;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.lecloud.sdk.api.stats.IAppStats;
import com.lecloud.sdk.api.stats.ICdeSetting;
import com.lecloud.sdk.config.LeCloudPlayerConfig;
import com.lecloud.sdk.listener.OnInitCmfListener;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zhy.autolayout.config.AutoLayoutConifg;

import org.xutils.x;

import java.util.LinkedHashMap;
import java.util.List;

import app.base.RRes;
import app.base.SPrefUtil;
import app.base.framework.CrashHandler;
import app.base.framework.Init;
import cn.jiguang.analytics.android.api.JAnalyticsInterface;
import cn.jpush.android.api.JPushInterface;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.SerialUtil;

/**
 * Created by Aaron on 2017/6/30.
 */

public class BaseApplication extends Init {
    private static BaseApplication application;
    public static IWXAPI mWxApi;

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        application = this;
        SPrefUtil.iniContext(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(getApplicationContext());
        JPushInterface.initCrashHandler(this);

        JAnalyticsInterface.setDebugMode(true);
        JAnalyticsInterface.init(getApplicationContext());
        JAnalyticsInterface.initCrashHandler(this);
        cn.jiguang.share.android.api.JShareInterface.setDebugMode(true);
        cn.jiguang.share.android.api.JShareInterface.init(getApplicationContext());
        SerialUtil.initCtx(application);
        //ImageLoader初始化
        ImageUtil.initImageLoader(this);
        //AutoLayout初始化
        AutoLayoutConifg.getInstance().useDeviceSize().init(this);

        Bugly.init(getApplicationContext(), "7797887a18", true);

        mWxApi = WXAPIFactory.createWXAPI(getBaseContext(), "wx5939ba19b940fea1", true);
        mWxApi.registerApp("wx5939ba19b940fea1");

        x.Ext.init(this); //初始化xUtils
        x.Ext.setDebug(true);//设置是否输出debug
        RRes.initR(getBaseContext());

//        CrashHandler.getInstance().init(this);

        /**
         * 乐视直播
         * */

        String processName = getProcessName(this, android.os.Process.myPid());
        if (getApplicationInfo().packageName.equals(processName)) {
            //TODO CrashHandler是一个抓取崩溃log的工具类（可选）
//            LeakCanary.install(this);
//            CrashReport.initCrashReport(getApplicationContext(), "900059604", true);
            try {
                PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                final LinkedHashMap<String, String> parameters = new LinkedHashMap<>();
                parameters.put(ICdeSetting.HOST_TYPE, LeCloudPlayerConfig.HOST_DEFAULT + "");
                parameters.put(ICdeSetting.LOG_OUTPUT_TYPE, LeCloudPlayerConfig.LOG_LOGCAT + "");
                parameters.put(ICdeSetting.USE_CDE_PORT, false + "");
                parameters.put(ICdeSetting.SCHEME_TYPE, LeCloudPlayerConfig.SCHEME_HTTP + "");
                parameters.put(IAppStats.APP_VERSION_NAME, packageInfo.versionName);
                parameters.put(IAppStats.APP_VERSION_CODE, packageInfo.versionCode + "");
                parameters.put(IAppStats.APP_PACKAGE_NAME, getPackageName());
                parameters.put(IAppStats.APP_NAME, "bcloud_android");
//                parameters.put(IAppStats.APP_CHANNEL, "说明一下app渠道");
//                parameters.put(IAppStats.APP_CATEGORY, "视频");
                LeCloudPlayerConfig.setmInitCmfListener(new OnInitCmfListener() {

                    @Override
                    public void onCdeStartSuccess() {
                        //cde启动成功,可以开始播放
                        cdeInitSuccess = true;
                        Log.d("huahua", "onCdeStartSuccess: ");
                    }

                    @Override
                    public void onCdeStartFail() {
                        //cde启动失败,不能正常播放;如果使用remote版本则可能是remote下载失败;
                        //如果使用普通版本,则可能是so文件加载失败导致
                        cdeInitSuccess = false;
                        Log.d("huahua", "onCdeStartFail: ");
                    }

                    @Override
                    public void onCmfCoreInitSuccess() {
                        //不包含cde的播放框架需要处理
                    }

                    @Override
                    public void onCmfCoreInitFail() {
                        //不包含cde的播放框架需要处理
                    }

                    @Override
                    public void onCmfDisconnected() {
                        //cde服务断开,会导致播放失败,重启一次服务
                        try {
                            cdeInitSuccess = false;
                            LeCloudPlayerConfig.init(getApplicationContext(), parameters);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                LeCloudPlayerConfig.init(getApplicationContext(), parameters);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    /**
     * 乐视直播
     */
    public static boolean cdeInitSuccess;

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps != null) {
            for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
                if (procInfo.pid == pid) {
                    return procInfo.processName;
                }
            }
        }
        return null;
    }

    /**
     *
     * */
    public static BaseApplication getApplication() {
        return application;
    }

    public static String getDeviceID() {
        TelephonyManager tm = (TelephonyManager) getApplication().getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static String getVersion() {
        PackageManager packageManager = getApplication().getPackageManager();
        PackageInfo packInfo;
        try {
            packInfo = packageManager.getPackageInfo(getApplication().getPackageName(), 0);
            String version = packInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }
}
