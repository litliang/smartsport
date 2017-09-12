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
 */
@ContentView(R.layout.activity_service)
public class AboutServiceActivity extends BaseActivity {
    private TextView mServiceTv;
    @Override
    protected void initView() {
        mServiceTv = (TextView) findViewById(R.id.about_service_txt);

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.service);
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
