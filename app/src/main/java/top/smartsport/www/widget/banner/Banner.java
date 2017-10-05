package top.smartsport.www.widget.banner;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import top.smartsport.www.R;
import top.smartsport.www.utils.ScreenUtils;

public class Banner extends LinearLayout {

    private AutoFlingPager mPager;
    private AutoFlingPagerAdapter<?> mPagerAdapter;
    private GalleryIndicator         mPagerIndicator;
    private TextView mIndicatorTitle;
    private RelativeLayout mIndicatorLayout;

    private int singleCircleWidth;

    public Banner(Context context) {
        super(context);
        initViews(context);
    }

    public Banner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    @Override
    public void setOnClickListener(OnClickListener clickListener) {
        mPager.setOnClickListener(clickListener);
    }

    public void setAdapter(AutoFlingPagerAdapter<?> adapter) {
        mPagerAdapter = adapter;
        mPager.setAdapter(mPagerAdapter);

        mPagerAdapter.registerDataSetObserver(
                new DataSetObserver() {
            @Override
            public void onChanged() {
                mPagerIndicator.setCount(mPagerAdapter.getRealCount());
                mIndicatorTitle.setText(mPagerAdapter.getTitle(mPager.getCurrentItem()));
            }
        });
    }

    public void setDuration(int duration) {
        mPager.setDuration(duration);
    }

    public void stop() {
        mPager.stop();
    }

    public void start() {
        mPager.start();
    }

    public void setTitleVisibility(int visibility) {
        mIndicatorTitle.setVisibility(visibility);
        ViewGroup.LayoutParams layoutParams = mPagerIndicator.getLayoutParams();
        if (visibility == View.VISIBLE) {
            int widthDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, mIndicatorTitle.getContext().getResources()
                    .getDisplayMetrics());
            layoutParams.width = widthDp;
        } else {
            layoutParams.width = LayoutParams.MATCH_PARENT;
        }
    }

    public void changeIndicatorStyle(int count, int height, int color) {
        if (count <= 0) return;
        int widthDp = (count - 1) * ScreenUtils.dip2px(getContext(), 10) + singleCircleWidth * count;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(widthDp, RelativeLayout.LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.setMargins(0,0,ScreenUtils.dip2px(getContext(),12), ScreenUtils.dip2px(getContext(),19));
		mPagerIndicator.setLayoutParams(params);
        if (height != -1) {
            int heightDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, height, mIndicatorTitle.getContext().getResources()
                    .getDisplayMetrics());
            ViewGroup.LayoutParams layoutParams = mIndicatorLayout.getLayoutParams();
            layoutParams.height = heightDp;
        }
        if (color != -1) {
            mIndicatorLayout.setBackgroundColor(color);
        }
    }

    private void initViews(Context context) {
        LayoutInflater.from(getContext()).inflate(R.layout.ad_banner, this, true);
        mPager = (AutoFlingPager) findViewById(R.id.pager_adbanner);
        mPager.setOnPageChangeListener(createOnPagerChangeListener());
        mPager.setDuration(850);
        mPagerIndicator = (GalleryIndicator) findViewById(R.id.indicator);
        mIndicatorTitle = (TextView) findViewById(R.id.tv_title);
        mIndicatorLayout = (RelativeLayout) findViewById(R.id.indicator_layout);
        try {
            Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.point_nomal);
            singleCircleWidth = bitmap.getWidth();
        }catch (Exception e){
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mPager.getParent() != null) {
            mPager.getParent().requestDisallowInterceptTouchEvent(true);
            if (mPager.getParent().getParent() != null) {
                mPager.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                if (mPager.getParent().getParent().getParent() != null) {
                    mPager.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                    if (mPager.getParent().getParent().getParent().getParent() != null) {
                        mPager.getParent().getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                        if (mPager.getParent().getParent().getParent().getParent().getParent() != null) {
                            mPager.getParent().getParent().getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                            if (mPager.getParent().getParent().getParent().getParent().getParent().getParent() != null) {
                                mPager.getParent().getParent().getParent().getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                            }
                        }
                    }
                }
            }
        }
        return super.onTouchEvent(event);
    }

    private ViewPager.OnPageChangeListener createOnPagerChangeListener() {
        return new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if (mPagerAdapter != null) {
                    int currentPosition = position % mPagerAdapter.getRealCount();
                    mPagerIndicator.setSeletion(currentPosition);
                    mIndicatorTitle.setText(mPagerAdapter.getTitle(currentPosition));
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (android.os.Build.VERSION.SDK_INT <= 10) // 2.3版本及以下需一直调用此方法以解决无法滑动的问题
                    mPager.getParent().requestDisallowInterceptTouchEvent(true);
            }

            // 滑动时候，父控件不拦截事件！解决banner 不好滑动的问题。
            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_DRAGGING) {
                    if (mPager.getParent() != null) {
                        mPager.getParent().requestDisallowInterceptTouchEvent(true);
                        if (mPager.getParent().getParent() != null) {
                            mPager.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                            if (mPager.getParent().getParent().getParent() != null) {
                                mPager.getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                                if (mPager.getParent().getParent().getParent().getParent() != null) {
                                    mPager.getParent().getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                                    if (mPager.getParent().getParent().getParent().getParent().getParent() != null) {
                                        mPager.getParent().getParent().getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                                        if (mPager.getParent().getParent().getParent().getParent().getParent().getParent() != null) {
                                            mPager.getParent().getParent().getParent().getParent().getParent().getParent().requestDisallowInterceptTouchEvent(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        };
    }

}
