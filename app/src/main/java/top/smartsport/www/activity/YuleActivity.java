package top.smartsport.www.activity;

import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.xutils.view.annotation.ContentView;

import java.util.List;

import app.base.JsonUtil;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;

/**
 * Created by admin on 2017/9/14.
 */
@ContentView(R.layout.activity_yule)
public class YuleActivity extends BaseActivity {
    @Override
    protected void initView() {
        back();
        callHttp(MapBuilder.build().add("action", "game").get(), new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {
                String url = (String) JsonUtil.findJsonLink("url",((NetEntity)result).getData().toString());
                WebView wb = ((WebView)findViewById(R.id.detail_content));
                wb.loadUrl(url);
                WebSettings webSettings = wb.getSettings();
                wb.getSettings().setJavaScriptEnabled(true);
                wb.getSettings().setDomStorageEnabled(true);

                wb.setWebViewClient(new WebViewClient(){
                    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
                        //handler.cancel(); 默认的处理方式，WebView变成空白页
//                        //接受证书
                        handler.proceed();
                        //handleMessage(Message msg); 其他处理
                    }
                });
            }

            @Override
            public void onFailure(Object result, List object) {

            }

            @Override
            public void onCallback(Object result, List object) {

            }
        });
    }
}
