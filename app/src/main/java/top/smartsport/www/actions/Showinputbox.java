package top.smartsport.www.actions;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.lecloud.xutils.util.LogUtils;

import java.util.List;

import app.base.DialogUtil;
import app.base.RRes;
import app.base.action.Task;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.utils.StringUtil;

/**
 * Created by admin on 2017/9/4.
 */

public class Showinputbox extends Task {
    Dialog dialog;

    /***
     *
     * @param view
     * @param params
     * 0  dialog 内容回设的接受textviewid
     * 1  dialog 标题
     * @return
     */
    @Override
    public Object run(final View view, Object... params) {
        final int changeid = RRes.get("R.id." + params[0].toString()).getAndroidValue();
        final String title = params[1].toString();
        final String findId = params[0].toString();
        return showDialog((Activity) view.getContext(), ((TextView) ((Activity) view.getContext()).findViewById(changeid)).getText().toString(), title, new FunCallback() {

            @Override
            public void onSuccess(Object result, List object) {

            }

            @Override
            public void onFailure(Object result, List object) {

            }

            @Override
            public void onCallback(Object result, List object) {
                LogUtils.d("-------result-----" + result.toString());
                LogUtils.d("-------title-----" + title);
                if(!StringUtil.isEmpty(title) && !StringUtil.isEmpty(findId)) {
                    String type = "";
                    String value = result.toString();
                    if (findId.equals("username")) {
                        type = "username";
                    } else if (findId.equals("truename")) {
                        type = "truename";
                    } else if (findId.equals("account_jz")) {
                        type = "address";
                    } else if (findId.equals("account_jlb")) {
//                        type = "";
                    }
                    if(!StringUtil.isEmpty(type)) {
                        saveAccount(type, value);
                    }
                }
                ((TextView) ((Activity) view.getContext()).findViewById(changeid)).setText(result.toString());
            }
        });
    }

    private void saveAccount(String type, String value) {
        BaseActivity.callHttp(MapBuilder.build().add("action", "saveBaseUserInfo").add(type, value).
                        add("type", "modify").get(), new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {
            }

            @Override
            public void onFailure(Object result, List object) {
            }

            @Override
            public void onCallback(Object result, List object) {
            }
        });
    }


    public Dialog showDialog(Activity ay, String txt, String title, final FunCallback fb) {
        final DialogUtil.DialogInfo dialogInfo = new DialogUtil.DialogInfo(ay);
        dialogInfo.aty = ay;
        dialogInfo.title = title;
        dialogInfo.view = LayoutInflater.from(ay).inflate(R.layout.layout_inputbox, null);
        ((TextView) dialogInfo.view.findViewById(R.id.edittext)).setText(txt);
        final FunCallback finalFb = fb;
        dialogInfo.view.findViewById(R.id.queding).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String text = ((TextView) dialogInfo.view.findViewById(R.id.edittext)).getText().toString();
                fb.onCallback(text, null);
                dialog.dismiss();
                dialog.cancel();
            }
        });
        dialog = DialogUtil.showNeutralDialog(dialogInfo,true);
        return dialog;
    }

}
