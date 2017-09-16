package top.smartsport.www.actions;

import android.view.View;
import android.widget.Toast;

import java.util.List;

import app.base.RRes;
import app.base.action.Task;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;

/**
 * Created by admin on 2017/9/3.
 */

public class Fav extends Task {

    Boolean isinit = null;
    boolean allow = true;
    String dores;
    String undores;

    @Override
    public Object run(View view, Object... params) {
        if (isinit == null) {
            isinit = Boolean.parseBoolean(params[0].toString());
        }
        if (params.length == 5) {
            dores = params[3].toString();
            undores = params[4].toString();
        }
        String type = params[1].toString();
        String id = params[2].toString();
        if (allow) {
            favImpl(view, isinit, type, id);
            allow = false;
        }

        return null;
    }


    public void favImpl(final View view, final boolean unfav, String type, String id) {
        if (unfav) {
            BaseActivity.callHttp(MapBuilder.build().add("action", "collect").add("type", type + "").add("source_id", id).get(), new FunCallback() {
                @Override
                public void onSuccess(Object result, List object) {
                    favIcon(view, true);
                    isinit = !isinit;

                }

                @Override
                public void onFailure(Object result, List object) {

                }

                @Override
                public void onCallback(Object result, List object) {
                    String toast = "";
                    if(result instanceof String){
                        toast = result.toString();
                    }else if(result instanceof NetEntity){
                        toast = ((NetEntity)result).getData().toString();
                    }else{
                        toast = result.toString();
                    }
                    Toast.makeText(view.getContext(), toast, Toast.LENGTH_SHORT).show();
                    allow = true;
                }
            });
        } else {


            BaseActivity.callHttp(MapBuilder.build().add("action", "cancelCollect").add("type", type + "").add("id", id).get(), new FunCallback() {
                @Override
                public void onSuccess(Object result, List object) {
                    favIcon(view, false);
                    isinit = !isinit;
                }

                @Override
                public void onFailure(Object result, List object) {

                }

                @Override
                public void onCallback(Object result, List object) {
                    Toast.makeText(view.getContext(), result.toString(), Toast.LENGTH_SHORT).show();

                    allow = true;
                }
            });

        }
    }

    public void favIcon(View view, boolean fav) {
        int doresv;
        int undoresv;
        if (dores != null) {
            doresv = RRes.get("R." + dores).getAndroidValue();
            undoresv = RRes.get("R." + undores).getAndroidValue();
            if (fav) {
                view.setBackground(view.getContext().getResources().getDrawable(doresv, null));
            } else {
                view.setBackground(view.getContext().getResources().getDrawable(undoresv, null));
            }
            return;
        }
        if (fav) {
            view.setBackground(view.getContext().getResources().getDrawable(R.mipmap.fav_done, null));
        } else {
            view.setBackground(view.getContext().getResources().getDrawable(R.mipmap.fav_undo, null));
        }
    }
}
