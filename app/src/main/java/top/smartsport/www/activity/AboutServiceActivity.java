package top.smartsport.www.activity;

import android.widget.TextView;

import org.xutils.view.annotation.ContentView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;

/**
 * Created by bajieaichirou on 2017/9/12 0012.
 * 服务协议
 */
@ContentView(R.layout.activity_service)
public class AboutServiceActivity extends BaseActivity {
    private TextView mServiceTv;
    private String type;
    @Override
    protected void initView() {
        mServiceTv = (TextView) findViewById(R.id.about_service_txt);
        type = getIntent().getStringExtra("type");

        try {
            InputStream inputStream = null;
            if (type.equals("service")){
                setTitle(getResources().getString(R.string.about_service_agreement));
                inputStream = getResources().openRawResource(R.raw.service);
            }else if (type.equals("refund")){
                setTitle(getResources().getString(R.string.sign_up_refund));
                inputStream = getResources().openRawResource(R.raw.refund);
            }else if (type.equals("disclaimer")){
                setTitle(getResources().getString(R.string.sign_up_disclaimer));
                inputStream = getResources().openRawResource(R.raw.disclaimer);
            }
            if (inputStream != null){
                InputStreamReader isr = new InputStreamReader(inputStream, "gbk");
                StringBuffer sb = new StringBuffer("");
                String line;
                BufferedReader reader = new BufferedReader(isr);
                try {
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        sb.append("\n");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mServiceTv.setText(sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
