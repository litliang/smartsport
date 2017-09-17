package top.smartsport.www.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.letvcloud.cmf.utils.DeviceUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import app.base.action.ViewInflater;
import top.smartsport.www.R;
import top.smartsport.www.activity.ConsultDetailActivity;
import top.smartsport.www.adapter.HDZXAdapter;
import top.smartsport.www.adapter.SSXWAdapter;
import top.smartsport.www.adapter.ZXBannerAdapter;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.Carousel;
import top.smartsport.www.bean.HDZXInfo;
import top.smartsport.www.bean.SSXWInfo;
import top.smartsport.www.fragment.viewutils.InformationOperateUtils;
import top.smartsport.www.utils.ScreenUtils;
import top.smartsport.www.widget.banner.Banner;

/**
 * deprecation:赛事新闻
 * author:AnYB
 * time:2017/9/24
 */
@ContentView(R.layout.fragment_zxss)
public class ZXSSV4Fragment extends BaseV4Fragment {
    @ViewInject(R.id.pull_to_refresh_list_view)
    private PullToRefreshListView mPullToRefreshView;//刷新控件
    private Banner mBanner;//轮播图控件

    private SSXWAdapter mInformationAdapter;//资讯列表适配器
    private ZXBannerAdapter mBannerAdapter;//Banner 适配器
    private int mCurrentPage;//当前页码

    @Override
    protected void initView() {
        Context context = getContext();
        View headerView = new ViewInflater(getContext()).inflate(R.layout.head_information, null);
        ((TextView) headerView.findViewById(R.id.title_name_tv)).setText("赛事新闻");
        mBanner = (Banner) headerView.findViewById(R.id.banner);
        mBannerAdapter = new ZXBannerAdapter();
        mBanner.setAdapter(mBannerAdapter);
        ListView listView = mPullToRefreshView.getRefreshableView();
        mInformationAdapter = new SSXWAdapter(getContext());
        listView.setAdapter(mInformationAdapter);
        listView.addHeaderView(headerView);
        getData(true);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mPullToRefreshView.setMode(PullToRefreshBase.Mode.BOTH);
                getData(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData(false);
            }
        });
        ((ListView) mPullToRefreshView.getRefreshableView()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), ConsultDetailActivity.class).putExtra("id", ((SSXWInfo) adapterView.getItemAtPosition(i)).getId() + ""));

            }
        });
    }

    private void getData(final boolean isRefresh) {
        if (isRefresh) {
            mCurrentPage = 1;
        } else {
            mCurrentPage++;
        }
        InformationOperateUtils.requestActivityInformation(mCurrentPage, "3", new InformationOperateUtils.ActivityInformationAPICallBack() {
            @Override
            public void onSuccessTypeTwo(List<Carousel> bannerResources, List<HDZXInfo> informationResources) {
            }

            @Override
            public void onSuccessTypeThree(List<Carousel> bannerResources, List<SSXWInfo> informationResources) {
                //refresh information
                if (informationResources != null && informationResources.size() > 0) {
                    if (isRefresh) {
                        mInformationAdapter.clear();
                    }
                    mInformationAdapter.addAll(informationResources);
                } else {
                    showToast("已经到底了");
                    mPullToRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                }
                //refresh banner
                if (bannerResources != null) {
                    mBannerAdapter.setData(bannerResources);
                    mBanner.changeIndicatorStyle(bannerResources.size(), 35, Color.TRANSPARENT);
                    mBanner.start();
                }
            }

            @Override
            public void onError(String errorMsg) {
                showToast(errorMsg);
            }

            @Override
            public void onFinished() {
                mPullToRefreshView.onRefreshComplete();
            }
        });
    }




    /*@ViewInject(R.id.mScrollView)
    private PullToRefreshScrollView mScrollView;
    private RegInfo regInfo;
    private TokenInfo tokenInfo;

    private String client_id;
    private String state;
    private String url;
    private String access_token;

    private int page;
    @ViewInject(R.id.ptrlv)
    private ListView ptrlv;

    private Banner banner;//轮播图
    List<Carousel> carousels = new ArrayList<>();
    private int num = 0;

    private SSXWAdapter ssxwAdapter;
    List<SSXWInfo> ssxwInfos = new ArrayList<>();
    public static ZXSSV4Fragment newInstance() {
        ZXSSV4Fragment fragment = new ZXSSV4Fragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void initView() {
        mScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                getData(true);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });
        mScrollView.scrollTo(0,0);
        ptrlv.setFocusable(false);
//        proceeds_listview = ptrlv.getRefreshableView();

        regInfo = RegInfo.newInstance();
        tokenInfo = TokenInfo.newInstance();

        client_id = regInfo.getApp_key();
        state = regInfo.getSeed_secret();
        url = regInfo.getSource_url();
        access_token = tokenInfo.getAccess_token();

        ssxwAdapter = new SSXWAdapter(getActivity());
        ptrlv.setAdapter(ssxwAdapter);
        ptrlv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), ConsultDetailActivity.class).putExtra("id", ((SSXWInfo) adapterView.getItemAtPosition(i)).getId() + ""));
            }
        });
        getData(true);


    }
    private void initBanner(List<Carousel> list) {
        num++;
        banner = new Banner(root, getActivity(), new Banner.MultiItemClickListener() {
            @Override
            public void onMultiItemClick(Banner.BannerData data) {

            }

        });

        banner.init(list);


    }
    private void getData(final boolean refresh){
        if(refresh){
            page = 1;
        }else {
            page++;
        }
        JSONObject json = new JSONObject();
        try {
            json.put("client_id",client_id);
            json.put("state",state);
            json.put("access_token",access_token);
            json.put("action","getNews");
            json.put("type","3");//赛事新闻
            json.put("page",page);
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
                Log.i("________3",entity.getData().toString());

                Data data = entity.toObj(Data.class);

                carousels = data.toListcarousel(Carousel.class);

                ssxwInfos= data.toListnews(SSXWInfo.class);
                ssxwAdapter.clear();
                ssxwAdapter.addAll(ssxwInfos);
                initBanner(carousels);
                mScrollView.onRefreshComplete();

            }
        });

    }*/

}
