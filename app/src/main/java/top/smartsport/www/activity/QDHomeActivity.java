package top.smartsport.www.activity;

import android.view.View;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.adapter.JLAdapter;
import top.smartsport.www.adapter.QYAdapter;
import top.smartsport.www.adapter.ZJAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.Data;
import top.smartsport.www.bean.JLInfo;
import top.smartsport.www.bean.Members;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.QYInfo;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.bean.ZJInfo;
import top.smartsport.www.widget.MyListView;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

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

    @ViewInject(R.id.qd_home_listView_jiaolian)
    private MyListView qd_home_listView_jiaolian;
    @ViewInject(R.id.qd_home_listView_zhujiao)
    private MyListView qd_home_listView_zhujiao;
    @ViewInject(R.id.qd_home_listView_qiuyuan)
    private MyListView qd_home_listView_qiuyuan;


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
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","getMyTeamDetail");
            json.put("team_id",team_id);
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
                Data data = entity.toObj(Data.class);
                membersList = data.toMembers(Members.class);
                for(int i = 0;i<membersList.size();i++){
                    Members members = membersList.get(i);
                    if(members.getType().equals("0")){//球员
                        QYInfo qyInfo = new QYInfo();
                        qyInfo.setId(members.getId());
                        qyInfo.setName(members.getName());
                        qyInfo.setNumber(members.getNumber());
                        qyInfo.setPosition(members.getPosition());
                        qyInfo.setType(members.getType());
                        qyInfoList.add(qyInfo);
                    }
                    if(members.getType().equals("1")){//助教
                        ZJInfo zjInfo = new ZJInfo();
                        zjInfo.setId(members.getId());
                        zjInfo.setName(members.getName());
                        zjInfo.setPosition(members.getPosition());
                        zjInfo.setNumber(members.getNumber());
                        zjInfo.setType(members.getType());
                        zjInfoList.add(zjInfo);
                    }

                    if(members.getType().equals("2")){
                        JLInfo jlInfo = new JLInfo();
                        jlInfo.setId(members.getId());
                        jlInfo.setName(members.getName());
                        jlInfo.setPosition(members.getPosition());
                        jlInfo.setNumber(members.getNumber());
                        jlInfo.setType(members.getType());
                        jlInfoList.add(jlInfo);
                    }
                }
                qyAdapter.addAll(qyInfoList);
                zjAdapter.addAll(zjInfoList);
                jlAdapter.addAll(jlInfoList);

            }
        });
    }
}
