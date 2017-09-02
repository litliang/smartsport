package top.smartsport.www.xutils3;

import android.util.Log;

import org.xutils.common.Callback;

import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.utils.FormatUtil;
import top.smartsport.www.utils.JsonUtil;


/**
 *   @describe 回调方法
 */
public abstract class MyCacheCallBack<ResultType> implements Callback.CacheCallback<ResultType>{

    @Override
    public void onSuccess(ResultType resultType) {
        Log.i("_____", FormatUtil.formatJson(resultType.toString()));
        NetEntity entity = JsonUtil.jsonToEntity(resultType.toString(), NetEntity.class);
        if ("0".equals(entity.getErrno())) {
            onSuccess(entity);
        } else {

                onFailure(entity.getInfo());
            }

    }

    @Override
    public void onError(Throwable throwable, boolean b) {

    }

    @Override
    public void onCancelled(CancelledException e) {

    }

    @Override
    public void onFinished() {

    }

    protected abstract void onFailure(String message);

    public abstract void onSuccess(NetEntity entity);


}
