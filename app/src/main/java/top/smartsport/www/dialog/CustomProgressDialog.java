package top.smartsport.www.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import top.smartsport.www.R;

/**
 * 自定义加载dialog
 */
public class CustomProgressDialog extends Dialog {
    private Context context;
    private String progressText;
    private LoadingCancelCallBack loadingCancel;

    public CustomProgressDialog(Context context) {
        super(context, R.style.dialog_theme);
        this.context = context;
    }

    public CustomProgressDialog(Context context, String progressText) {
        super(context, R.style.dialog_theme);
        this.context = context;
        this.progressText = progressText;
    }


    public void setLoadingCancel(LoadingCancelCallBack loadingCancel) {
        this.loadingCancel = loadingCancel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_progress);
        TextView title = (TextView) findViewById(R.id.custom_imageview_progress_title);
        title.setText(progressText == null ? "加载中..." : progressText);
    }

    public void setMessage(String message) {
        TextView title = (TextView) findViewById(R.id.custom_imageview_progress_title);
        title.setText(message);
    }

    @Override
    public void show() {
        if (!isShowing() && context != null) {
            super.show();
        }
    }


    @Override
    public void dismiss() {
        if (loadingCancel != null && isShowing()) {
            loadingCancel.loadCancel();
        }
        super.dismiss();
    }

    public interface LoadingCancelCallBack {
        void loadCancel();
    }

}

