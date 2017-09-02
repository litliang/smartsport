package top.smartsport.www.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.xutils.x;

import top.smartsport.www.dialog.CustomProgressDialog;

/**
 * Created by Aaron on 2017/6/30.
 */

public abstract class BaseFragment extends Fragment {
    public CustomProgressDialog pd;
    public View root;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initProgressDialog();
        root = x.view().inject(this, inflater, container);
        initView();
        return root;
    }

    /**
     * 初始化参数
     */
    protected abstract void initView();

    private void initProgressDialog() {
        pd = new CustomProgressDialog(getActivity());
        pd.setCancelable(false);
    }

    /**
     * 从Fragment跳转到Activity
     */
    public void toActivity(Class<?> clazz) {
        toActivity(clazz, null);
    }

    public void toActivity(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(getActivity(), clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public static void toActivity(Class<?> clazz, Bundle bundle, Context ctx) {
        Intent intent = new Intent(ctx, clazz);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        ctx.startActivity(intent);
    }

    public void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context ctx, String text) {
        Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
    }

}

