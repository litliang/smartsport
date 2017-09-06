package top.smartsport.www.activity;

import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import top.smartsport.www.R;
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
    private TextView tvContent;
    @ViewInject(R.id.lv_consult)
    private MyListView lvConsult;
    @ViewInject(R.id.lv_comment)
    private MyListView lvComment;
    private ConsultAdapter adapterNews;
    private CommentAdapter adapterComment;
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

        adapterNews = new ConsultAdapter();
        lvConsult.setAdapter(adapterNews);
        adapterComment = new CommentAdapter();
        lvComment.setAdapter(adapterComment);
        getData();
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
            }

            @Override
            public void onSuccess(NetEntity entity) {
                String data = entity.getData().toString();
                ZXInfoDetail details =  JsonUtil.jsonToEntity(app.base.JsonUtil.findJsonLink("detail",data).toString(),ZXInfoDetail.class);
                List<ZXInfoNews> news =  JsonUtil.jsonToEntityList(app.base.JsonUtil.findJsonLink("other_news",data).toString(), ZXInfoNews.class);
                List<ZXInfoComment> coments =  JsonUtil.jsonToEntityList(app.base.JsonUtil.findJsonLink("comments",data).toString(), ZXInfoComment.class);
                ImageLoader.getInstance().displayImage(details.getCover_url(), ivTop, ImageUtil.getOptions());
                tvTitle.setText(details.getTitle());
                tvTime.setText(details.getCtime());
                tvAction.setText(details.getCate_name());
                readCount.setText("阅读 "+details.getHits());
                tvContent.setText(Html.fromHtml(details.getBody()));
                adapterNews.setData(news);
                adapterComment.setData(coments);
            }
        });

    }
}
