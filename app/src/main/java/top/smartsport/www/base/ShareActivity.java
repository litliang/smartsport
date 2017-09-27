package top.smartsport.www.base;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import app.base.MapAdapter;
import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.qqmodel.QQ;
import cn.jiguang.share.qqmodel.QZone;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatFavorite;
import cn.jiguang.share.wechat.WechatMoments;
import cn.jiguang.share.weibo.SinaWeibo;
import top.smartsport.www.R;
import top.smartsport.www.adapter.ShareAdapter;
import top.smartsport.www.bean.ShareItem;

/**
 * Created by admin on 2017/8/24.
 */

public class ShareActivity extends BaseActivity {
    public static ShareParams shareparams;
    private List<ShareItem> listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_share);
        MapAdapter.AdaptInfo adaptInfo = new MapAdapter.AdaptInfo();
        adaptInfo.listviewItemLayoutId = R.layout.layout_imagebtn;
        adaptInfo.objectFieldList = Arrays.asList(new String[]{"name"});
        adaptInfo.viewIdList = Arrays.asList(new Integer[]{R.id.image});
        listItem = new ArrayList();
        listItem.add(new ShareItem("微信收藏", R.mipmap.collection, WechatFavorite.Name));
        listItem.add(new ShareItem("微信", R.mipmap.wechat, Wechat.Name));
        listItem.add(new ShareItem("QQ", R.mipmap.qq, QQ.Name));
        listItem.add(new ShareItem("QQ空间", R.mipmap.qzone, QZone.Name));
        listItem.add(new ShareItem("微博", R.mipmap.weibo, SinaWeibo.Name));
        listItem.add(new ShareItem("微信朋友圈", R.mipmap.friends_circle, WechatMoments.Name));
        ShareAdapter adapter = new ShareAdapter(getApplicationContext(), listItem);

        ((RelativeLayout) findViewById(R.id.rl_bg)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        mapAdapter.setItemDataSrc(new MapContent(list));
        ((GridView) findViewById(R.id.grid)).setAdapter(adapter);
        ((GridView) findViewById(R.id.grid)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                JShareInterface.share(listItem.get(i).getType(), shareparams, new PlatActionListener() {

                    @Override
                    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                    }

                    @Override
                    public void onError(Platform platform, int i, int i1, Throwable throwable) {
                    }

                    @Override
                    public void onCancel(Platform platform, int i) {
                    }
                });
                finish();
            }
        });
    }

    @Override
    protected void initView() {

    }
}
