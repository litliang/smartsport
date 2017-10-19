package top.smartsport.www.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import app.base.MapConf;
import app.base.widget.ImageView;
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
import top.smartsport.www.swipe.SwipeMenu;
import top.smartsport.www.swipe.SwipeMenuCreator;
import top.smartsport.www.swipe.SwipeMenuItem;
import top.smartsport.www.swipe.SwipeMenuListView;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.JsonUtil;
import top.smartsport.www.utils.StringUtil;
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

    @ViewInject(R.id.sl_view)
    private ScrollView sl_view;
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
    private SwipeMenuListView lvComment;
    @ViewInject(R.id.fl_loading)
    private FrameLayout fl_loading;
    private ConsultAdapter adapterNews;
    private CommentAdapter adapterComment;
    private String data;
    private boolean isCurrentScStatus = true;
    private  List<ZXInfoComment> coments;

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

        sl_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lvComment.dispatchTouchEvent(event); //分发事件(让listview滚动)
                return false; //scrollview自我滚动，true时不滚动
            }
        });

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

//        lvComment.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                getCommentStatus(position);
//                return true;
//            }
//        });

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                menu.addMenuItem(deleteItem);
            }
        };
        lvComment.setMenuCreator(creator);

        lvComment.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        getCommentStatus(position);
                        break;
                }
            }
        });
    }

    private void getCommentStatus(int pos) {
        //删除状态: 0不可删除 1可删除
        ZXInfoComment zxComment = coments.get(pos);
        String delStatus = zxComment.getDel_status();
        if(!StringUtil.isEmpty(delStatus) && delStatus.equals("1")) { // 可删除
//            showDefineDialog(pos, zxComment.getId());
            delComment(pos, zxComment.getId());
        } else {
            Toast.makeText(ConsultDetailActivity.this, "只能删自己的评论哦", Toast.LENGTH_SHORT).show();
        }
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
                setSharetxt(details.getDescription());
                setShareurl(details.getCover_url());
                List<ZXInfoNews> news = JsonUtil.jsonToEntityList(intf.JsonUtil.findJsonLink("other_news", data).toString(), ZXInfoNews.class);
                coments = new ArrayList<ZXInfoComment>();
                coments = JsonUtil.jsonToEntityList(app.base.JsonUtil.findJsonLink("comments", data).toString(), ZXInfoComment.class);
                ImageLoader.getInstance().displayImage(details.getCover_url(), ivTop, ImageUtil.getOptions(), ImageUtil.getImageLoadingListener(true));
                tvTitle.setText(details.getTitle());
                tvTime.setText(details.getCtime());
                tvAction.setText(details.getCate_name());
                readCount.setText("阅读 " + details.getHits());
                tvContent.loadData(details.getBody(), "text/html;charset=UTF-8", null);
                if (news != null) {
                    adapterNews.setData(news);
                }
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
        boolean scStatus = getFaved();
        if(isCurrentScStatus != scStatus)
            setResult(RESULT_OK);
    }

    // 显示自定义Dialog
    private void showDefineDialog(final int pos, final String id) {
        final AlertDialog dialog = new AlertDialog.Builder(ConsultDetailActivity.this).create();
        dialog.show();
        Window window = dialog.getWindow();
        int resId = ConsultDetailActivity.this.getResources().getColor(R.color.transparent);
        window.setBackgroundDrawable(new ColorDrawable(resId));
        // *** 主要就是在这里实现这种效果的.
        window.setContentView(R.layout.define_dialog);

        RelativeLayout rlCancel = (RelativeLayout) window
                .findViewById(R.id.rl_left);
        RelativeLayout rlBoundPhone = (RelativeLayout) window
                .findViewById(R.id.rl_right);
        TextView tvTitle = (TextView) window.findViewById(R.id.tv_dialog_title);
        TextView tvContent = (TextView) window
                .findViewById(R.id.tv_dialog_content);
        TextView tvLeft = (TextView) window.findViewById(R.id.tv_left);
        TextView tvRight = (TextView) window.findViewById(R.id.tv_right);

        tvTitle.setText("删除评论");
        tvTitle.setVisibility(View.VISIBLE);
        tvContent.setText("您是否要删除评论？");
        tvContent.setTextSize(16.0f);
        tvLeft.setText("删除");
        tvRight.setText("取消");
        rlCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                delComment(pos, id);
            }
        });

        rlBoundPhone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    // 删除评论
    private void delComment(final int pos, String id) {
        JSONObject json = new JSONObject();
        try {
            json.put("client_id", client_id);
            json.put("state", state);
            json.put("access_token", access_token);
            json.put("action", "delComment");
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
                coments.remove(pos);
                if (coments != null) {
                    adapterComment.setData(coments);
                }
            }
        });

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

}
