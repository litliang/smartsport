package top.smartsport.www.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.base.MapAdapter;
import app.base.MapContent;
import intf.JsonUtil;
import top.smartsport.www.R;
import top.smartsport.www.adapter.PICAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.BSDetail;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.PicInfo;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.widget.MyGridView;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

/**
 * Created by Aaron on 2017/7/25.
 * 比赛详情
 */
@ContentView(R.layout.activity_bsdetail)
public class BSDetailActivity extends BaseActivity {
    public static final String TAG = BSDetailActivity.class.getName();

    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    @ViewInject(R.id.adapter_bsss_img)
    private ImageView adapter_bsss_img;//图片
    @ViewInject(R.id.adapter_bsss_state)
    private TextView adapter_bsss_state;//状态
    @ViewInject(R.id.adapter_bsss_date)
    private TextView adapter_bsss_date;//时间
    @ViewInject(R.id.adapter_bsss_title)
    private TextView adapter_bsss_title;//标题
    @ViewInject(R.id.adapter_bsss_address)
    private TextView adapter_bsss_address;//地址
    @ViewInject(R.id.adapter_bsss_syu)
    private TextView adapter_bsss_syu;//还剩多少名额
    @ViewInject(R.id.adapter_bsss_pay)
    private TextView adapter_bsss_pay;//报名金额
    @ViewInject(R.id.adapter_bsss_level)
    private TextView adapter_bsss_level;//比赛等级
    @ViewInject(R.id.adapter_bsss_people)
    private TextView adapter_bsss_people;//多少人

    private String id;
    private String states;

    @ViewInject(R.id.pic_gridView)
    private MyGridView pic_gridView;
    private PICAdapter picAdapter;

    @ViewInject(R.id.bs_detail_baoming)
    private Button bs_detail_baoming;

    @ViewInject(R.id.bs_detail_ll__listView)
    private LinearLayout bs_detail_ll__listView;

    @ViewInject(R.id.bs_detail_content)
    private WebView bs_detail_content;

    @ViewInject(R.id.bs_detail_ll_video)
    private LinearLayout bs_detail_ll_video;

    @ViewInject(R.id.bs_detail_video)
    private ListView bs_detail_video;

    @ViewInject(R.id.action_bar)
    private View actionbar;

    @Override
    public View getTopBar() {
        return actionbar;
    }

    @Override
    protected void initView() {
        back();
        fav();
        share();
        ((TextView) actionbar.findViewById(R.id.tvTitle)).setText("比赛");
        id = (String) getObj(BSDetailActivity.TAG);
        states = (String) getObj("states");
        if (null != state) {
            if (states.equals("报名中")) {
                bs_detail_baoming.setVisibility(View.VISIBLE);//报名显示
                bs_detail_ll__listView.setVisibility(View.GONE); //正在比赛列表隐藏
                bs_detail_ll_video.setVisibility(View.GONE);//赛事视频隐藏
                adapter_bsss_state.setBackgroundResource(R.drawable.shape_bg_button);
                bs_detail_baoming.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Bundle bundle = new Bundle();
                        bundle.putString(SSBMActivity.TAG, id);
                        goActivity(SSBMActivity.class, bundle);
                    }
                });
            }
            if (states.equals("进行中")) {
                bs_detail_baoming.setVisibility(View.GONE);//报名隐藏
                bs_detail_ll__listView.setVisibility(View.VISIBLE); //正在比赛列表显示
                bs_detail_ll_video.setVisibility(View.VISIBLE);//赛事视频隐藏
                adapter_bsss_state.setBackgroundResource(R.drawable.shape_bg_button_blue);
            }
            if (states.equals("已结束")) {
                bs_detail_baoming.setVisibility(View.GONE);//报名隐藏
                bs_detail_ll__listView.setVisibility(View.GONE); //正在比赛列表隐藏
                bs_detail_ll_video.setVisibility(View.VISIBLE);//赛事视频隐藏
                adapter_bsss_state.setBackgroundResource(R.drawable.shape_bg_button_gray);
            }

        }

        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();

        picAdapter = new PICAdapter(this);
        pic_gridView.setAdapter(picAdapter);

        findViewById(R.id.lookuppics).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("pics", (Serializable) picInfoList);
                goActivity(BSPictureActivity.class, bundle);
            }
        });
        getDetail();
    }



    @Event(value = {R.id.rl_kc, R.id.rl_sc, R.id.rl_sp})
    private void getEvent(View v) {
        switch (v.getId()) {
            case R.id.rl_kc://参赛球队
                Bundle bundle = new Bundle();
                bundle.putString("matchid", id);
                goActivity(CSQDActivity.class, bundle);
                break;
            case R.id.rl_sc://数据分析
                goActivity(SJFXActivity.class);
                break;
            case R.id.rl_sp://赛程表
                goActivity(KCBActivity.class);
                break;
        }
    }

    /**
     * 获取比赛详情
     */
    private List<PicInfo> picInfoList = new ArrayList<>();

    private void getDetail() {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "getMatchDetail");
            json.put("id", id);
            //‘view_img’ : 1,  //选填 为1时显示全部赛事图片，不填默认显示4张
            //‘view_video’ : 1,  //选填 为1时显示全部赛事视频，不填默认显示6部
//            json.put("view_img","");
//            json.put("view_video","");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {

            }

            @Override
            public void onSuccess(NetEntity entity) {
                BSDetail bsDetail = entity.toObj(BSDetail.class);
                ImageLoader.getInstance().displayImage(bsDetail.getCover(), adapter_bsss_img, ImageUtil.getOptions());
                adapter_bsss_state.setText(states);
                adapter_bsss_title.setText(bsDetail.getName());
                adapter_bsss_date.setText(bsDetail.getStart_time() + "至" + bsDetail.getEnd_time());
                adapter_bsss_address.setText(bsDetail.getAddress());
                adapter_bsss_level.setText(bsDetail.getLevel());
                adapter_bsss_people.setText(bsDetail.getType());
                adapter_bsss_syu.setText("还剩" + bsDetail.getSurplus() + "个名额");
                adapter_bsss_pay.setText(bsDetail.getSell_price().replace(".00", ""));
                if (bsDetail.getDescription().trim().equals("")) {
                    findViewById(R.id.saishijianjie).setVisibility(View.GONE);
                }
                bs_detail_content.loadData(bsDetail.getDescription(), "text/html;charset=UTF-8", null);
                bs_detail_baoming.setText("我要报名(￥" + bsDetail.getSell_price().replace(".00", "") + ")");
                picInfoList = bsDetail.toList(PicInfo.class);
                ((TextView) findViewById(R.id.text_apply)).setText(bsDetail.getApply_num() + "/" + bsDetail.getQuota());
//                if (bsDetail.getApply_num().equals("0")) {
//                    findViewById(R.id.rl_kc).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            showToast("当前没有参赛球队");
//                        }
//                    });
//                }
                picAdapter.addAll(picInfoList);
                if (picAdapter.getCount() == 0) {
                    findViewById(R.id.pictitle).setVisibility(View.GONE);
                }
                MapAdapter.AdaptInfo adaptinfo = new MapAdapter.AdaptInfo();
                adaptinfo.addListviewItemLayoutId(R.layout.adapter_bsdetail_shipin);
                adaptinfo.addViewIds(new Integer[]{R.id.news_name});
                adaptinfo.addObjectFields(new String[]{"name"});
                MapAdapter mapAdapter = new MapAdapter(getBaseContext(), adaptinfo);
                mapAdapter.setItemDataSrc(new MapContent(JsonUtil.extractJsonRightValue(bsDetail.getMatch_video().toString())));
                bs_detail_video.setAdapter(mapAdapter);
                bs_detail_video.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Bundle b = new Bundle();
                        b.putString("fileurl", ((Map) adapterView.getItemAtPosition(i)).get("fileurl").toString());
                        goActivity(BSVideoActivity.class, b);
                    }
                });
                if (bs_detail_video.getCount() == 0) {
                    bs_detail_ll_video.setVisibility(View.GONE);
                }
            }
        });
    }
}
