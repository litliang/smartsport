package top.smartsport.www.fragment;

import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.adapter.HDZXAdapter;
import top.smartsport.www.base.BaseV4Fragment;
import top.smartsport.www.bean.Carousel;
import top.smartsport.www.bean.HDZXInfo;
import top.smartsport.www.fragment.viewutils.InformationOperateUtils;
import top.smartsport.www.adapter.ZXBannerAdapter;
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

    private boolean isRequesting;//是否正在请求
    private int mCurrentPage;//当前页码

    @Override
    protected void initView() {
        mBanner = new Banner(getContext());
        int bannerHeight = 550;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,bannerHeight);
        mBanner.setLayoutParams(params);
        mBannerAdapter = new ZXBannerAdapter();
        mBanner.setAdapter(mBannerAdapter);
        ListView listView = mPullToRefreshView.getRefreshableView();
        mInformationAdapter = new HDZXAdapter(getContext());
        listView.setAdapter(mInformationAdapter);
        listView.addHeaderView(mBanner);
        getData(true);
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (isRequesting) return;
                getData(true);
            }
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                getData(false);
            }
        });
    }

    private void getData(final boolean isRefresh) {
        if (isRequesting) return;
        isRequesting = true;
        if (isRefresh) {
            mCurrentPage = 1;
        } else {
            mCurrentPage++;
        }
        InformationOperateUtils.requestActivityInformation(mCurrentPage, new InformationOperateUtils.ActivityInformationAPICallBack() {
            @Override
            public void onSuccess(List<Carousel> bannerResources, List<HDZXInfo> informationResources) {
                //refresh information
                if (informationResources != null && informationResources.size() > 0){
                    if (isRefresh) {
                        mInformationAdapter.clear();
                    }
                    mInformationAdapter.addAll(informationResources);
                }
                //refresh banner
                if (bannerResources != null){
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
                isRequesting = false;
                mPullToRefreshView.onRefreshComplete();
            }
        });
    }

}
