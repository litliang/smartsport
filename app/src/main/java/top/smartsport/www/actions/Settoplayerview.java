package top.smartsport.www.actions;

import android.view.View;
import android.widget.TextView;

import com.dl7.player.media.IjkPlayerView;

import app.base.openaction.MapConfTask;

/**
 * Created by admin on 2017/9/3.
 */

public class Settoplayerview extends MapConfTask {

    @Override
    public void run(View view, Object item, String name, Object value, Object casevalue, View convertView, Object... objects) {
        ((IjkPlayerView)view).init()                // 初始化，必须先调用
//                .setTitle("这是个标题")    // 设置标题，全屏时显示
                .setSkipTip(1000 * 60 * 1)    // 设置跳转提示
//                .enableOrientation()    // 使能重力翻转
                .setVideoPath(value.toString())    // 设置视频Url，单个视频源可用这个
//                .setVideoSource(null, url, url, url, null)    // 设置视频Url，多个视频源用这个
                .setMediaQuality(IjkPlayerView.MEDIA_QUALITY_HIGH)    // 指定初始视频源
                .enableDanmaku()      // 使能弹幕功能
//                .setDanmakuSource(getResources().openRawResource(R.raw.comments))	// 添加弹幕资源，必须在enableDanmaku()后调用
                .start();    // 启动播放

    }
}
