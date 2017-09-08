package top.smartsport.www.actions;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

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

/**
 * Created by admin on 2017/9/4.
 */

public class Showchoicebox extends Task {
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
        String title = params[1].toString();
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
                ((TextView) ((Activity) view.getContext()).findViewById(changeid)).setText(result.toString() + finalUnit);
            }
        });
        return null;
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
