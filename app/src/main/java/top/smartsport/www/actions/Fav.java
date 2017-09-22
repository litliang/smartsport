package top.smartsport.www.actions;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import app.base.MapConf;
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
        isinit = Boolean.parseBoolean(params[0].toString());
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
                    Toast.makeText(view.getContext(), "已收藏", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Object result, List object) {

                }

                @Override
                public void onCallback(Object result, List object) {
                    allow = true;
                }
            });
        } else {


            BaseActivity.callHttp(MapBuilder.build().add("action", "cancelCollect").add("type", type + "").add("id", id).get(), new FunCallback() {
                @Override
                public void onSuccess(Object result, List object) {
                    favIcon(view, false);
                    Toast.makeText(view.getContext(), "已为您取消", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure(Object result, List object) {

                }

                @Override
                public void onCallback(Object result, List object) {

                    allow = true;
                }
            });

        }
    }

    public void favIcon(final View view, final boolean fav) {
        new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {

                int doresv = 0;
                int undoresv = 0;
                if (dores != null) {
                    doresv = RRes.get("R." + dores).getAndroidValue();
                    undoresv = RRes.get("R." + undores).getAndroidValue();
                    if (view instanceof ImageView) {

                        if (fav) {
                            ((ImageView) view).setImageResource(doresv);
                        } else {
                            ((ImageView) view).setImageResource(undoresv);
                        }
                    } else {
                        if (fav) {
                            view.setBackgroundResource(doresv);
                        } else {
                            view.setBackgroundResource(undoresv);

                        }
                    }
                    view.invalidate();
                    return false;
                } else {
                    if (view instanceof ImageView) {
                        if (fav) {
                            ((ImageView) view).setImageResource(R.mipmap.fav_done);
                        } else {
                            ((ImageView) view).setImageResource(R.mipmap.fav_undo);
                        }
                    } else {
                        if (fav) {
                            view.setBackgroundResource(R.mipmap.fav_done);
                        } else {
                            view.setBackgroundResource(R.mipmap.fav_undo);
                        }
                    }
                    view.invalidate();
                }

                return false;
            }
        }).sendEmptyMessage(0);
    }
}
