package top.smartsport.www.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.adapter.JLAdapter;
import top.smartsport.www.adapter.QYAdapter;
import top.smartsport.www.adapter.ZJAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.JLInfo;
import top.smartsport.www.bean.Members;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.QYInfo;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.bean.ZJInfo;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.StringUtil;
import top.smartsport.www.widget.MyListView;

/**
 * Created by Aaron on 2017/8/1.
 * 球队主页
 */
@ContentView(R.layout.activity_qdhome)
public class QDHomeActivity extends BaseActivity{
    public static String TAG = QDHomeActivity.class.getName();
    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    private String team_id;

    private QYAdapter qyAdapter;
    private ZJAdapter zjAdapter;
    private JLAdapter jlAdapter;

    @ViewInject(R.id.wd_logo)
    private ImageView wd_logo;
    @ViewInject(R.id.tv_qd_name)
    private TextView tv_qd_name;
    @ViewInject(R.id.tv_qd_description)
    private TextView tv_qd_description;
    @ViewInject(R.id.qd_home_listView_jiaolian)
    private MyListView qd_home_listView_jiaolian;
    @ViewInject(R.id.qd_home_listView_zhujiao)
    private MyListView qd_home_listView_zhujiao;
    @ViewInject(R.id.qd_home_listView_qiuyuan)
    private MyListView qd_home_listView_qiuyuan;
    @ViewInject(R.id.tv_jl)
    private TextView tv_jl;
    @ViewInject(R.id.tv_zj)
    private TextView tv_zj;
    @ViewInject(R.id.tv_qy)
    private TextView tv_qy;
    @ViewInject(R.id.tv_ranking)
    private TextView tv_ranking; // 排名
    @ViewInject(R.id.tv_integral)
    private TextView tv_integral; // 积分
    @ViewInject(R.id.tv_win)
    private TextView tv_win; // 胜
    @ViewInject(R.id.tv_draw)
    private TextView tv_draw; // 平
    @ViewInject(R.id.tv_loss)
    private TextView tv_loss; // 负


    @Override
    protected void initView() {
        team_id = (String) getObj(QDHomeActivity.TAG);
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();

        qyAdapter = new QYAdapter(this);
        zjAdapter = new ZJAdapter(this);
        jlAdapter = new JLAdapter(this);

        qd_home_listView_jiaolian.setAdapter(jlAdapter);
        qd_home_listView_qiuyuan.setAdapter(qyAdapter);
        qd_home_listView_zhujiao.setAdapter(zjAdapter);

        getData();
    }

    /**
     * 获取球队
     * */
    private List<Members> membersList = new ArrayList<>();
    private List<JLInfo> jlInfoList = new ArrayList<>();
    private List<ZJInfo> zjInfoList = new ArrayList<>();
    private List<QYInfo> qyInfoList = new ArrayList<>();
    private void getData(){

        callHttp(MapBuilder.build().add("action", "getTeamHomePage").add("team_id", team_id).get(), new FunCallback() {
            @Override
            public void onSuccess(Object result, List object) {
                String data = ((NetEntity)result).getData().toString();
                try {
                    JSONObject dataObject = new JSONObject(data);
                    JSONObject base = dataObject.optJSONObject("base");
                    if(base != null) {
                        String logUrl = base.optString("logo");
                        if(!StringUtil.isEmpty(logUrl)) {
                            ImageLoader.getInstance().displayImage(logUrl,
                                    wd_logo, ImageUtil.getOptions_avater());
                        }
                        String name = base.optString("name");
                        if(!StringUtil.isEmpty(name)) {
                            tv_qd_name.setText(name);
                        } else {
                            tv_qd_name.setText("");
                        }
                        String des = base.optString("description");
                        if(!StringUtil.isEmpty(des)) {
                            tv_qd_description.setText(des);
                        } else {
                            tv_qd_description.setText("");
                        }
                    }
                    JSONObject param = dataObject.optJSONObject("param");
                    if(param != null) {
                        tv_ranking.setText("排名: " + param.optString("ranking"));
                        tv_integral.setText("积分: " + param.optString("integral"));
                        tv_win.setText("胜: " + param.optString("win"));
                        tv_draw.setText("平: " + param.optString("draw"));
                        tv_loss.setText("负: " + param.optString("loss"));
                    } else {
                        tv_ranking.setText("排名: 0");
                        tv_integral.setText("积分: 0");
                        tv_win.setText("胜: 0");
                        tv_draw.setText("平: 0");
                        tv_loss.setText("负: 0");
                    }
                    JSONObject members = dataObject.optJSONObject("members");
                    JSONArray coach = members.optJSONArray("coach"); // 教练
                    if(coach != null && coach.length() > 0) {
                        for (int i=0; i<coach.length(); i++) {
                            JSONObject coachItem = coach.optJSONObject(i);
                            JLInfo jlInfo = new JLInfo();
                            jlInfo.setId(coachItem.optString("id"));
                            jlInfo.setName(coachItem.optString("name"));
                            jlInfo.setTitle(coachItem.optString("title"));
                            jlInfo.setType(coachItem.optString("type"));
                            jlInfo.setNumber(coachItem.optString("number"));
                            jlInfo.setPosition(coachItem.optString("position"));
                            jlInfo.setHeader(coachItem.optString("header"));
                            jlInfo.setHeader_url(coachItem.optString("header_url"));
                            jlInfoList.add(jlInfo);
                        }
                    }

                    JSONArray assists = members.optJSONArray("assists"); // 助练
                    if(assists != null && assists.length() > 0) {
                        for (int i=0; i<assists.length(); i++) {
                            JSONObject assistsItem = assists.optJSONObject(i);
                            ZJInfo zjInfo = new ZJInfo();
                            zjInfo.setId(assistsItem.optString("id"));
                            zjInfo.setName(assistsItem.optString("name"));
                            zjInfo.setTitle(assistsItem.optString("title"));
                            zjInfo.setType(assistsItem.optString("type"));
                            zjInfo.setNumber(assistsItem.optString("number"));
                            zjInfo.setPosition(assistsItem.optString("position"));
                            zjInfo.setHeader(assistsItem.optString("header"));
                            zjInfo.setHeader_url(assistsItem.optString("header_url"));
                            zjInfoList.add(zjInfo);
                        }
                    }

                    JSONArray player = members.optJSONArray("player"); // 球员
                    if(player != null && player.length() > 0) {
                        for (int i=0; i<player.length(); i++) {
                            JSONObject playerItem = player.optJSONObject(i);
                            QYInfo qyInfo = new QYInfo();
                            qyInfo.setId(playerItem.optString("id"));
                            qyInfo.setName(playerItem.optString("name"));
                            qyInfo.setTitle(playerItem.optString("title"));
                            qyInfo.setType(playerItem.optString("type"));
                            qyInfo.setNumber(playerItem.optString("number"));
                            qyInfo.setPosition(playerItem.optString("position"));
                            qyInfo.setHeader(playerItem.optString("header"));
                            qyInfo.setHeader_url(playerItem.optString("header_url"));
                            qyInfo.setTeam_name(playerItem.optString("team_name"));
                            qyInfoList.add(qyInfo);
                        }
                    }
                    if(jlInfoList == null || jlInfoList.size() == 0) {
                        tv_jl.setVisibility(View.GONE);
                    }
                    jlAdapter.addAll(jlInfoList);
                    if(zjInfoList == null || zjInfoList.size() == 0) {
                        tv_zj.setVisibility(View.GONE);
                    }
                    zjAdapter.addAll(zjInfoList);
                    if(qyInfoList == null || qyInfoList.size() == 0) {
                        tv_qy.setVisibility(View.GONE);
                    }
                    qyAdapter.addAll(qyInfoList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Object result, List object) {

            }

            @Override
            public void onCallback(Object result, List object) {

            }
        });
    }
}
