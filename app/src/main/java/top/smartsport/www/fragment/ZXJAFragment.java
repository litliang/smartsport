package top.smartsport.www.fragment;

import android.os.Bundle;

import app.base.JsonUtil;
import app.base.MapAdapter;
import app.base.MapContent;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseFragment;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.widget.MyGridView;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by Aaron on 2017/7/24.
 * 青训--在线案例
 */
@ContentView(R.layout.fragment_zxja)
public class ZXJAFragment extends BaseFragment {
    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    public static ZXJAFragment newInstance() {
        ZXJAFragment fragment = new ZXJAFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;

    }
    @Override
    protected void initView() {
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();
        getData();
    }


    private void getData(){
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","getOnlineCourses");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                showToast(message);
            }

            @Override
            public void onSuccess(NetEntity entity) {
                MapAdapter.AdaptInfo adaptinfo = new MapAdapter.AdaptInfo();
                adaptinfo.addListviewItemLayoutId(R.layout.qingxun_zaixianjiaoan);
                adaptinfo.addObjectFields(new String[]{"cover","hours","video_title"});
                adaptinfo.addViewIds(new Integer[]{R.id.shipin,R.id.duration,R.id.kechengmingzi});

                MapAdapter mapadapter = new MapAdapter(getContext(),adaptinfo);
                JsonUtil.extractJsonRightValue(entity.getData().getAsString());

                List<TreeMap> list = new ArrayList<TreeMap>();
                list.add(new TreeMap());
                mapadapter.setItemDataSrc(new MapContent(list));
                ((MyGridView)root.findViewById(R.id.rumenjigrid)).setAdapter(mapadapter);
            }

            @Override
            public void onError(Throwable throwable, boolean b) {
                super.onError(throwable, b);
            }
        });

    }
}
