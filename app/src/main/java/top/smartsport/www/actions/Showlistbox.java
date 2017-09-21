package top.smartsport.www.actions;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.lecloud.xutils.util.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import app.base.DialogUtil;
import app.base.MapAdapter;
import app.base.MapContent;
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

public class Showlistbox extends Task {
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
        final String findId = params[0].toString();
        LogUtils.d("-------params[0].toString()-----" + params[0].toString());
        final String title = params[1].toString();
        String array = params[2].toString();
        String unit = "";
        if (params.length > 3) {
            unit = params[3].toString();
        }
        final String finalUnit = unit;
        showDialog((Activity) view.getContext(), title, array, new FunCallback() {

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
                    if (findId.equals("account_age")) {
                        type = "age";
                    } else if (findId.equals("account_sex")) { // 传的是参数（0：女， 1：男）
                        type = "sex";
                        if(!StringUtil.isEmpty(value) && value.equals("女")) {
                            value = "0";
                        } else {
                            value = "1";
                        }
                    } else if (findId.equals("account_height")) {
                        type = "height";
                    } else if (findId.equals("account_weight")) {
                        type = "weight";
                    } else if (findId.equals("account_habit")) { // 传的是参数: 1左脚  2右脚  3左右均衡
                        type = "leg";
                        if(!StringUtil.isEmpty(value) && value.equals("右脚")) {
                            value = "2";
                        } else if(!StringUtil.isEmpty(value) && value.equals("左脚")) {
                            value = "1";
                        } else {
                            value = "3";
                        }
                    } else if (findId.equals("account_ql")) {
                        type = "soccer_age";
                    }
                    if(!StringUtil.isEmpty(type)) {
                        saveAccount(type, value);
                    }
                }
                ((TextView) ((Activity) view.getContext()).findViewById(changeid)).setText(result.toString() + finalUnit);
            }
        });
        return null;
    }

    private void saveAccount(String type, String value) {
        BaseActivity.callHttp(MapBuilder.build().add("action", "saveBaseUserInfo").add(type, value).add("type", "modify").get(), new FunCallback() {
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

    public void showDialog(Activity ay, String title, String tosplitlist, FunCallback fb) {
        String[] split = tosplitlist.split("->");
        List list = new ArrayList();
        if (split.length == 2) {
            int begin = Integer.parseInt(split[0]);
            int end = Integer.parseInt(split[1]);

            for (int i = begin; i < end + 1; i++) {
                list.add(i + "");
            }
        } else {
            list = Arrays.asList(tosplitlist.split("-"));
        }

        DialogUtil.DialogInfo dialogInfo = new DialogUtil.DialogInfo(ay);
        dialogInfo.aty = ay;
        dialogInfo.title = title;
        dialogInfo.view = new ListView(ay);

        List ms = new ArrayList();
        for (Object o : list) {
            ms.add(MapBuilder.build().add("name", o.toString()).get());
        }
        MapAdapter.AdaptInfo adaptinfo = new MapAdapter.AdaptInfo();
        adaptinfo.addListviewItemLayoutId(R.layout.string_item);
        adaptinfo.addViewIds(new Integer[]{R.id.name});
        adaptinfo.addObjectFields(new String[]{"name"});
        MapAdapter mapadapter = new MapAdapter(ay, adaptinfo);
        ((ListView) dialogInfo.view).setAdapter(mapadapter);
        ((ListView) dialogInfo.view).setDivider(null);

        mapadapter.setItemDataSrc(new MapContent(ms));
        mapadapter.notifyDataSetChanged();
        final FunCallback finalFb = fb;
        ((ListView) dialogInfo.view).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                finalFb.onCallback(((Map) adapterView.getItemAtPosition(i)).get("name"), null);
                dialog.dismiss();

                dialog.cancel();

            }
        });
        dialog = DialogUtil.showNeutralDialog(dialogInfo, true);
    }

}
