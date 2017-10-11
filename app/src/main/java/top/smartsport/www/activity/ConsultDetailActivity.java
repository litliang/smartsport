package top.smartsport.www.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import app.base.MapConf;
import intf.FunCallback;
import intf.MapBuilder;
import top.smartsport.www.R;
import top.smartsport.www.actions.Showinputbox;
import top.smartsport.www.adapter.CommentAdapter;
import top.smartsport.www.adapter.ConsultAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.NetEntity;
import top.smartsport.www.bean.RegInfo;
import top.smartsport.www.bean.TokenInfo;
import top.smartsport.www.bean.ZXInfoComment;
import top.smartsport.www.bean.ZXInfoDetail;
import top.smartsport.www.bean.ZXInfoNews;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.JsonUtil;
import top.smartsport.www.widget.MyListView;
import top.smartsport.www.xutils3.MyCallBack;
import top.smartsport.www.xutils3.X;

@ContentView(R.layout.consult_layout)
public class ConsultDetailActivity extends BaseActivity {
    public static final String TAG = ConsultDetailActivity.class.getName();
    private View mView;

    private RegInfo regInfo;
    private TokenInfo tokenInfo;
    private String client_id;
    private String state;
    private String url;
    private String access_token;
    private String id;

    @ViewInject(R.id.iv_top_pic)
    private ImageView ivTop;
    @ViewInject(R.id.tv_title)
    private TextView tvTitle;
    @ViewInject(R.id.tv_time)
    private TextView tvTime;
    @ViewInject(R.id.tv_action)
    private TextView tvAction;
    @ViewInject(R.id.iv_head_icon1)
    private ImageView ivHeadPic;
    @ViewInject(R.id.tv_name)
    private TextView tvAuth;
    @ViewInject(R.id.read_count)
    private TextView readCount;
    @ViewInject(R.id.tv_content1)
    private WebView tvContent;
    @ViewInject(R.id.lv_consult)
    private MyListView lvConsult;
    @ViewInject(R.id.lv_comment)
    private MyListView lvComment;
    @ViewInject(R.id.fl_loading)
    private FrameLayout fl_loading;
    private ConsultAdapter adapterNews;
    private CommentAdapter adapterComment;
    private String data;
    private boolean isCurrentScStatus = true;

    @Override
    protected void initView() {
        ivTop.setFocusable(true);
        ivTop.setFocusableInTouchMode(true);
        ivTop.requestFocus();
        id = getIntent().getStringExtra("id");
        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();
        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();
        back();
        fav();
        adapterNews = new ConsultAdapter();
        lvConsult.setAdapter(adapterNews);
        adapterComment = new CommentAdapter();
        lvComment.setAdapter(adapterComment);
        findViewById(R.id.send_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new Showinputbox().showDialog((Activity) view.getContext(), "", "评论", new FunCallback() {
                    @Override
                    public void onSuccess(Object result, List object) {

                    }

                    @Override
                    public void onFailure(Object result, List object) {

                    }

                    @Override
                    public void onCallback(Object result, List object) {

                        BaseActivity.callHttp(MapBuilder.build().add("action", "comment").add("type", "1").add("content", result.toString()).add("obj_id", id).get(), new FunCallback() {
                            @Override
                            public void onSuccess(Object result, List object) {
                                getData();
                            }

                            @Override
                            public void onFailure(Object result, List object) {

                            }

                            @Override
                            public void onCallback(Object result, List object) {

                            }
                        });
                    }
                });
            }
        });
        getData();
        ((ListView) lvConsult).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getBaseContext(), ConsultDetailActivity.class).putExtra("id", ((ZXInfoNews) adapterView.getItemAtPosition(i)).getId() + ""));

            }
        });
    }

    @Override
    public void favImpl(View view, boolean unfav) {

        fav.run(view, unfav + "", 4, id);
    }

    private void getData() {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "getNewsDetail");
            json.put("id", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        X.Post(url, json, new MyCallBack<String>() {
            @Override
            protected void onFailure(String message) {
                showToast(message);
                fl_loading.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(NetEntity entity) {
                fl_loading.setVisibility(View.GONE);
                data = entity.getData().toString();
                String collect_status = app.base.JsonUtil.findJsonLink("detail-collect_status", data).toString();


                MapConf.build().with(ConsultDetailActivity.this)
                        .pair("detail-collect_status->ivRight_text", "0:mipmap.fav_undo;1:mipmap.fav_done")
                        .pair("detail-author->tv_name").source(data, ConsultDetailActivity.this).toView();
                setFaved(!collect_status.equals("0"));
                ZXInfoDetail details = JsonUtil.jsonToEntity(app.base.JsonUtil.findJsonLink("detail", data).toString(), ZXInfoDetail.class);
                setSharetitle(details.getTitle());
                setSharetxt(details.getBody());
                setShareurl(details.getCover_url());
                List<ZXInfoNews> news = JsonUtil.jsonToEntityList(intf.JsonUtil.findJsonLink("other_news", data).toString(), ZXInfoNews.class);
                List<ZXInfoComment> coments = JsonUtil.jsonToEntityList(app.base.JsonUtil.findJsonLink("comments", data).toString(), ZXInfoComment.class);
                ImageLoader.getInstance().displayImage(details.getCover_url(), ivTop, ImageUtil.getOptions(), ImageUtil.getImageLoadingListener(true));
                tvTitle.setText(details.getTitle());
                tvTime.setText(details.getCtime());
                tvAction.setText(details.getCate_name());
                readCount.setText("阅读 " + details.getHits());
                tvContent.loadData(details.getBody(), "text/html;charset=UTF-8", null);
                if (news != null) {
                    adapterNews.setData(news);
                }
                LogUtil.d("--------getData---------->" + getFaved());
                isCurrentScStatus = getFaved();
                if (coments != null) {
                    adapterComment.setData(coments);
                }

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.d("------onDestroy--tofav---------->" + getFaved());
        LogUtil.d("------onDestroy--isCurrentScStatus---------->" + isCurrentScStatus);
        boolean scStatus = getFaved();
        if(isCurrentScStatus != scStatus)
            setResult(RESULT_OK);
    }
}
