package top.smartsport.www.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import top.smartsport.www.R;

public class Banner {
    private Context context;
    private View root;
    @ViewInject(R.id.vpBanner)
    private ViewPager vpBanner;
    @ViewInject(R.id.tvBanner)
    private TextView tvBanner;
    @ViewInject(R.id.llDot)
    private LinearLayout llDot;
    private View[] dotArray;
    private boolean isChanged = true;
    private int pagePosition = 1;
    private Timer mTimer;
    private SwitchImageTask siTask;
    private MultiItemClickListener listener;
    private BannerAdapter adapter;

    public Banner(View root, Context context, MultiItemClickListener listener) {
        this.root = root.findViewById(R.id.rlBanner);
        x.view().inject(this, this.root);
        this.context = context;
        this.listener = listener;
    }

    public interface MultiItemClickListener {
        void onMultiItemClick(BannerData data);
    }

    public void init(final List<? extends BannerData> list) {
        if (adapter == null && list.size() > 0) {
            adapter = new BannerAdapter(context, list);
            adapter.setOnMultiItemClickListener(listener);
            vpBanner.setAdapter(adapter);
            if (adapter.getCount() > 1) {
                dotArray = new View[adapter.getCount() - 2];
                for (int i = 0; i < adapter.getCount() - 2; i++) {
                    View dot = LayoutInflater.from(context).inflate(R.layout.layout_dot, llDot, false);
                    llDot.addView(dot);
                    dotArray[i] = dot;
                }
                vpBanner.addOnPageChangeListener(new OnPageChangeListener() {

                    @Override
                    public void onPageSelected(int position) {
                        isChanged = true;
                        if (position > adapter.getCount() - 2) {
                            pagePosition = 1;
                        } else if (position < 1) {
                            pagePosition = adapter.getCount() - 2;
                        } else {
                            pagePosition = position;
                        }
                        for (int i = 0; i < dotArray.length; i++) {
                            dotArray[i].setBackgroundResource(pagePosition == i + 1 ? R.drawable.dot_b : R.drawable.dot_w);
                        }
                        int index = list.size() == adapter.getCount() ? position : position == 0 ? list.size() - 1 : position > list.size() ? 0 : position - 1;
//                        tvBanner.setText(list.get(index).getTitle());
                    }

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {
                        if (ViewPager.SCROLL_STATE_IDLE == state) {
                            if (isChanged) {
                                vpBanner.setCurrentItem(pagePosition, false);
                                isChanged = false;
                            }
                        }
                    }
                });
                vpBanner.setCurrentItem(1);
                startAutoPage();
            } else {
//                tvBanner.setText(list.get(0).getTitle());
            }
        }
    }

    private void startAutoPage() {
        endAutoPage();
        mTimer = new Timer();
        siTask = new SwitchImageTask();
        mTimer.schedule(siTask, 3000, 3000);
    }

    private void endAutoPage() {
        if (mTimer != null) {
            mTimer.cancel();
            siTask.cancel();
            mTimer = null;
            siTask = null;
        }
    }

    private class SwitchImageTask extends TimerTask {
        @Override
        public void run() {
            vpBanner.post(new Runnable() {
                @Override
                public void run() {
                    if (adapter != null && adapter.getCount() > 1) {
                        vpBanner.setCurrentItem(pagePosition++, true);
                    }
                }
            });
        }
    }

    public static interface BannerData {


        String getImgurl();

    }

    private class BannerAdapter extends PagerAdapter {

        private Context context;
        private LayoutInflater inflater;
        private List<? extends BannerData> list;
        private View[] views;
        private MultiItemClickListener listener;

        public BannerAdapter(Context context, List<? extends BannerData> list) {
            if (list == null) {
                throw new IllegalAccessError("参数不可为空");
            }
            this.context = context;
            this.list = list;
            views = new View[list.size() > 1 ? list.size() + 2 : list.size()];
            inflater = LayoutInflater.from(context);
        }

        public void setOnMultiItemClickListener(MultiItemClickListener listener) {
            this.listener = listener;
        }

        @Override
        public int getCount() {
            return views.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (views[position] == null) {
                if (views.length > 1) {
                    int index = list.size() == views.length ? position : position == 0 ? list.size() - 1 : position > list.size() ? 0 : position - 1;
                    views[position] = initView(container, list.get(index));
                    switch (position) {
                        case 0:
                            views[getCount() - 2] = initView(container, list.get(index));
                            break;
                        case 1:
                            views[getCount() - 1] = initView(container, list.get(index));
                            break;
                    }
                } else {
                    views[position] = initView(container, list.get(position));
                }
            }
            container.addView(views[position]);
            return views[position];
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views[position]);
        }

        private View initView(ViewGroup container, final BannerData bannerData) {
            View view = inflater.inflate(R.layout.adapter_banner, container, false);
            ImageView iv = (ImageView) view.findViewById(R.id.ivBanner);
            ImageLoader.getInstance().displayImage(bannerData.getImgurl(), iv);
            iv.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(context, WebActivity.class);
//                    intent.putExtra(WebActivity.TITLE, bannerData.getTitle());
//                    intent.putExtra(WebActivity.ID, bannerData.getID());
//                    context.startActivity(intent);
                    if(null!=bannerData){
                        listener.onMultiItemClick(bannerData);
                    }
                }
            });
            return view;
        }
    }


}
