package top.smartsport.www.base;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.base.MapAdapter;
import app.base.MapContent;
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
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.wxapi.WXEntryActivity;

/**
 * Created by admin on 2017/8/24.
 */

public class ShareActivity extends BaseActivity {
    public static ShareParams shareparams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_share);
        MapAdapter.AdaptInfo adaptInfo = new MapAdapter.AdaptInfo();
        adaptInfo.listviewItemLayoutId = R.layout.layout_imagebtn;
        adaptInfo.objectFieldList = Arrays.asList(new String[]{"name"});
        adaptInfo.viewIdList = Arrays.asList(new Integer[]{R.id.image});
        MapAdapter mapAdapter = new MapAdapter(getBaseContext(), adaptInfo);
        List list = new ArrayList() {
            {
                add(MapBuilder.build().add("name", R.mipmap.collection).add("type", WechatFavorite.Name).get());
                add(MapBuilder.build().add("name", R.mipmap.wechat).add("type", Wechat.Name).get());
                add(MapBuilder.build().add("name", R.mipmap.qq).add("type", QQ.Name).get());
                add(MapBuilder.build().add("name", R.mipmap.qzone).add("type", QZone.Name).get());
                add(MapBuilder.build().add("name", R.mipmap.weibo).add("type", SinaWeibo.Name).get());
                add(MapBuilder.build().add("name", R.mipmap.friends_circle).add("type",WechatMoments.Name).get());
            }
        };

        mapAdapter.setItemDataSrc(new MapContent(list));
        ((GridView) findViewById(R.id.grid)).setAdapter(mapAdapter);
        ((GridView) findViewById(R.id.grid)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                JShareInterface.share(((Map)adapterView.getItemAtPosition(i)).get("type").toString(),shareparams,new PlatActionListener(){

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
