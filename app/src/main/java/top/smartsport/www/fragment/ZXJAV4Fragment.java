package top.smartsport.www.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;

import java.util.List;
import java.util.Map;

import app.base.DialogUtil;
import app.base.JsonUtil;
import app.base.MapAdapter;
import app.base.MapContent;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.activity.ActivityOnLineVideo;
import top.smartsport.www.activity.MyHYActivity;
import top.smartsport.www.base.BaseApplication;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.utils.SPUtils;
import top.smartsport.www.widget.MyGridView;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/24.
 * 青训--在线案例
 */
@ContentView(R.layout.fragment_zxja)
public class ZXJAV4Fragment extends BaseV4Fragment {
    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;
    private PullToRefreshScrollView pullToRefreshScrollView;

    public static ZXJAV4Fragment newInstance() {
        ZXJAV4Fragment fragment = new ZXJAV4Fragment();
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

        pullToRefreshScrollView = (PullToRefreshScrollView) root.findViewById(R.id.pullrefreshlistview);
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getData();

            }
        });
        getData();

    }


    private void getData() {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "getNewOnlineCourses");

            int levelIndex = (Integer) SPUtils.get(BaseApplication.getApplication(), "kc_currentLevelIndex", 0); // 课程级别
            int statusIndex = (Integer) SPUtils.get(BaseApplication.getApplication(), "kc_currentLaiYuanIndex", 0); // 课程来源
            int typeIndex = (Integer) SPUtils.get(BaseApplication.getApplication(), "kc_currentLeiBieIndex", 0); // 课程类别

            LogUtil.d("----------levelIndex---------->" + (levelIndex + 0));
            LogUtil.d("----------statusIndex---------->" + (statusIndex + 0));
            LogUtil.d("----------typeIndex---------->" + (typeIndex));

            json.put("level", "" + (levelIndex + 1));// 课程级别
            json.put("status", "" + (statusIndex + 1));// 课程来源
            json.put("type", "" + (typeIndex + 1));// 课程类别
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
                String name = "begin";
                int gridid = R.id.rumenjigrid;
                setGrid(entity, name, gridid,R.id.titlerumenji);
                setGrid(entity, "primary", R.id.chujigrid,R.id.titlechuji);
                setGrid(entity, "middle", R.id.zhongjigrid,R.id.titlezhongji);
                setGrid(entity, "senior", R.id.gaojigrid,R.id.titlegaoji);
                pullToRefreshScrollView.onRefreshComplete();
            }

            @Override
            public void onError(Throwable throwable, boolean b) {

                super.onError(throwable, b);
                pullToRefreshScrollView.onRefreshComplete();
            }
        });

    }

    private void setGrid(NetEntity entity, String name, int gridid, int titleji) {
        MapAdapter.AdaptInfo adaptinfo = new MapAdapter.AdaptInfo();
        adaptinfo.addListviewItemLayoutId(R.layout.qingxun_zaixianjiaoan);
        adaptinfo.addObjectFields(new String[]{"cover", "hours", "title"});
        adaptinfo.addViewIds(new Integer[]{R.id.shipin, R.id.duration, R.id.kechengmingzi, R.id.xueqi_hours});

        MapAdapter mapadapter = new MapAdapter(getContext(), adaptinfo) {
            @Override
            protected boolean findAndBindView(View convertView, int pos, Object item, String name, Object value) {
                return super.findAndBindView(convertView, pos, item, name, value);
            }

            @Override
            protected void getViewInDetail(Object item, int position, View convertView) {
                super.getViewInDetail(item, position, convertView);
                if (!((TextView) convertView.findViewById(R.id.duration)).getText().toString().endsWith("小时")) {
                    ((TextView) convertView.findViewById(R.id.duration)).append("小时");
                }

                String time = "第" + map.get(((Map) item).get("term").toString()) + "学期 | 共" + ((Map) item).get("hours").toString() + "个课时";
                ((TextView) convertView.findViewById(R.id.xueqi_hours)).setText(time);
            }
        };
        String type = JsonUtil.findJsonLink(name, entity.getData().toString()).toString();
        if (type.equals("")) {
            ((MyGridView) root.findViewById(gridid)).setVisibility(View.GONE);
            root.findViewById(titleji).setVisibility(View.GONE  );
            return;
        }
        List list = (List) JsonUtil.extractJsonRightValue(type);

        mapadapter.setItemDataSrc(new MapContent(list));
        ((MyGridView) root.findViewById(gridid)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String isvip = (String) SPUtils.get(view.getContext(), "is_vip", "");

                if (!isvip.equals("1")) {
                    showDialog((Activity) view.getContext());
                } else {
                    startActivity(new Intent(getActivity(), ActivityOnLineVideo.class).putExtra("id", ((Map) adapterView.getItemAtPosition(i)).get("id").toString()));
                }
//                startActivity(new Intent(getActivity(), ActivityOnLineVideo.class).putExtra("id", ((Map) adapterView.getItemAtPosition(i)).get("id").toString()));
                ;
            }
        });
        ((MyGridView) root.findViewById(gridid)).setAdapter(mapadapter);
    }

    public Dialog dialog;

    public void showDialog(final Activity ay) {

        DialogUtil.DialogInfo dialogInfo = new DialogUtil.DialogInfo(ay);
        dialogInfo.aty = ay;
        dialogInfo.title = "会员权限";
        dialogInfo.message = new SpannableStringBuilder("观看在线教案需要成为会员");

        dialogInfo.positiveButtonText = "取消";
        dialogInfo.positiveButtonClickListener = DialogUtil.getNewCancelOption(ay);
        dialogInfo.negativeButtonText = "开启会员";
        dialogInfo.negativeButtonClickListener = new DialogInterface.OnClickListener() {


            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                dialog.cancel();
                ay.startActivity(new Intent(ay, MyHYActivity.class));
            }
        };

        dialog = DialogUtil.showChoiceDialog(dialogInfo, true);
        dialog.show();
    }

    Map map = MapBuilder.build().add("1", "一").add("2", "二").add("3", "三").add("4", "四").add("5", "五").add("6", "六").add("7", "七").add("8", "八").add("9", "九").get();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0:
                getData();
                break;
            default:
                break;
        }
    }
}
