package top.smartsport.www.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.common.util.LogUtil;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import app.base.action.ViewInflater;
import top.smartsport.www.R;
import top.smartsport.www.activity.ConsultDetailActivity;
import top.smartsport.www.adapter.HDZXAdapter;
import top.smartsport.www.adapter.ZXBannerAdapter;
import top.smartsport.www.base.BaseApplication;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.Carousel;
import top.smartsport.www.bean.HDZXInfo;
import top.smartsport.www.bean.SSXWInfo;
import top.smartsport.www.fragment.viewutils.InformationOperateUtils;
import top.smartsport.www.utils.SPUtils;
import top.smartsport.www.widget.banner.Banner;

/**
 * deprecation:活动资讯
 * author:AnYB
 * time:2017/9/24
 */
@ContentView(R.layout.fragment_zxhd)
public class ZXHDV4Fragment extends BaseV4Fragment {
    @ViewInject(R.id.pull_to_refresh_list_view)
    private PullToRefreshListView mPullToRefreshView;//刷新控件
    private Banner mBanner;//轮播图控件

    private HDZXAdapter mInformationAdapter;//资讯列表适配器
    private ZXBannerAdapter mBannerAdapter;//Banner 适配器
    private int mCurrentPage;//当前页码
    private String cityId;


    @Override
    public void onStart() {
        super.onStart();
        Context context = getContext();
        cityId = (String) SPUtils.get(BaseApplication.getApplication(),"cityId","");
        View headerView = new ViewInflater(getContext()).inflate(R.layout.head_information,null);
        ((TextView)headerView.findViewById(R.id.title_name_tv)).setText("活动资讯");
        mBanner = (Banner) headerView.findViewById(R.id.banner);

        mBannerAdapter = new ZXBannerAdapter();
        mBanner.setAdapter(mBannerAdapter);
        ListView listView = mPullToRefreshView.getRefreshableView();
        mInformationAdapter = new HDZXAdapter(getContext());
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
        ((ListView)mPullToRefreshView.getRefreshableView()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getActivity(), ConsultDetailActivity.class).putExtra("id", ((HDZXInfo) adapterView.getItemAtPosition(i)).getId() + ""));

            }
        });


    }

    @Override
    protected void initView() {

    }

    private void getData(final boolean isRefresh) {
        if (isRefresh) {
            mCurrentPage = 1;
        } else {
            mCurrentPage++;
        }
        InformationOperateUtils.requestActivityInformation(mCurrentPage,"2", cityId, new InformationOperateUtils.ActivityInformationAPICallBack() {
            @Override
            public void onSuccessTypeTwo(List<Carousel> bannerResources, List<HDZXInfo> informationResources) {
                //refresh information
                if (informationResources != null && informationResources.size() > 0){
                    if (isRefresh) {
                        mInformationAdapter.clear();
                    }
                    mInformationAdapter.addAll(informationResources);
                }else {
                    if(isRefresh) {
                        mInformationAdapter.clear();
                    } else {
                        showToast("已经到底了");
                        mPullToRefreshView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
                    }
                }
                //refresh banner
                if (bannerResources != null){
                    mBannerAdapter.setData(bannerResources);
                    mBanner.changeIndicatorStyle(bannerResources.size(), 35, Color.TRANSPARENT);
                    mBanner.start();
                }
            }
            @Override
            public void onSuccessTypeThree(List<Carousel> bannerResources, List<SSXWInfo> informationResources) {

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 101:
                cityId = (String) SPUtils.get(BaseApplication.getApplication(),"cityId","");
                LogUtil.d("----------cityId---------->" + cityId);
                getData(true);
                break;
            default:
                break;
        }
    }

}
