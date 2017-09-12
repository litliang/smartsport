package top.smartsport.www.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import top.smartsport.www.R;

/**
 * Created by bajieaichirou on 17/3/4.
 * 选择拍照 相册
 */
public class ActivityChooseIcon extends Activity implements View.OnClickListener{

    private TextView mCameraTxt, mPictureTxt, mCancelTxt;

    private Intent intent;
    private final int CODE_CHOOSE_ICON = 3;
    private final String KEY_CHOOSE_ICON_TYPE = "choose_type";
    private final String KEY_CHOOSE_TYPE_CAMERA = "choose_type_camera";
    private final String KEY_CHOOSE_TYPE_PICTURE = "choose_type_picture";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_choose_icon);
        initUI();
    }

    private void initUI() {
        mCameraTxt = (TextView) findViewById(R.id.choose_camera);
        mPictureTxt = (TextView) findViewById(R.id.choose_picture);
        mCancelTxt = (TextView) findViewById(R.id.choose_cancel);

        mCameraTxt.setOnClickListener(this);
        mPictureTxt.setOnClickListener(this);
        mCancelTxt.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        intent = new Intent();
        switch (v.getId()){
            case R.id.choose_camera:
                intent.putExtra(KEY_CHOOSE_ICON_TYPE, KEY_CHOOSE_TYPE_CAMERA);
                setResult(CODE_CHOOSE_ICON, intent);
                this.finish();
                break;
            case R.id.choose_picture:
                intent.putExtra(KEY_CHOOSE_ICON_TYPE, KEY_CHOOSE_TYPE_PICTURE);
                setResult(CODE_CHOOSE_ICON, intent);
                this.finish();
                break;
            case R.id.choose_cancel:
                intent.putExtra(KEY_CHOOSE_ICON_TYPE, "");
                setResult(CODE_CHOOSE_ICON, intent);
                this.finish();
                break;
        }
    }
}
