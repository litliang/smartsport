package cn.android.gallery;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import cn.android.service.ImageLoader;
import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.PicInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.zhy.view.ZoomImageView;

/**
 * 相册展示应用程序
 *
 * @Description GoodsGalleryActivity
 * @File GoodsGalleryActivity.java
 * @Package cn.android.gallery
 * @Author WanTianwen
 * @Blog http://blog.csdn.net/WanTianwen
 * @Date 2014-04-13
 * @Version V1.0
 */
public class GoodsGalleryActivity extends BaseActivity implements
        AdapterView.OnItemSelectedListener, ViewSwitcher.ViewFactory {
    private TextView titlebarTitle;
    private ImageView navigationBackButton;
    private ViewPager galleyImages;
    private int position;
    private float startX;
    private FrameLayout switcherFrame;
    private ImageSwitcher imageSwitcher;
    public ImageLoader imageLoader;
    private ArrayList<PicInfo> goodsGalleryList;
    private LinearLayout commonError;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goods_gallery);
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = (int) (display.getWidth()); //设置宽度
        getWindow().setAttributes(lp);


        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);
        goodsGalleryList = (ArrayList<PicInfo>) intent.getSerializableExtra("urls");

        commonError = (LinearLayout) findViewById(R.id.common_error);

        titlebarTitle = (TextView) findViewById(R.id.titlebar_title);
        //mTitlebarTitle.setText("1/" + 5);
        navigationBackButton = (ImageView) findViewById(R.id.navigation_back_button);
        navigationBackButton.setOnClickListener(new NavigationBackButtonListener(this));

        imageSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
        imageSwitcher.setFactory(this);
        imageSwitcher.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
        imageSwitcher.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));
        switcherFrame = (FrameLayout) findViewById(R.id.switcher_frame);
//        switcherFrame.setOnTouchListener(new MSwitcherOnTouchListener(this));

        galleyImages = (ViewPager) findViewById(R.id.goods_gallery);
        initViewPager();
        galleyImages.setCurrentItem(position);


    }

    @Override
    protected void initView() {

    }

    public void initViewPager() {
        final List list = new ArrayList<>();
        for (PicInfo p : goodsGalleryList) {
            View rel = LayoutInflater.from(this).inflate(R.layout.image_control, null);
            ZoomImageView iv1 = (ZoomImageView) rel.findViewById(R.id.image_zoom);
            list.add(rel);

        }

        imageLoader = new ImageLoader(this);
        galleyImages.setAdapter(new PagerAdapter() {

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                final View rel = (View) list.get(position);
                final ZoomImageView imageView = (ZoomImageView) ((View) rel).findViewById(R.id.image_zoom);
                container.addView(rel);
                imageLoader.DisplayImage(goodsGalleryList.get(position).getFileurl(), imageView, new ImageLoader.BitmapListener(){
                        @Override
                    public void onBitmapLoaded(Bitmap b) {
                        super.onBitmapLoaded(b);
                        rel.findViewById(R.id.prg).setVisibility(View.GONE);
                            imageView.getLayoutParams().height = ((View)imageView.getParent()).getHeight();
                        imageView.autoscale(b.getWidth(),getWindowManager().getDefaultDisplay().getWidth());
                    }
                });

                return rel;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                container.removeView((View) list.get(position));
            }

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return list.size();
            }
        });
        galleyImages.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                titlebarTitle.setText((position+1)+"/"+goodsGalleryList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @SuppressWarnings("rawtypes")
    public void onItemSelected(AdapterView parent, View v, int position, long id) {
        position = position;
        try {
            GetImageTask task = new GetImageTask(this);
            task.execute();

//            imageSwitcher.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            commonError.setVisibility(View.VISIBLE);
//            imageSwitcher.setVisibility(View.GONE);
        }
    }

    @SuppressWarnings("rawtypes")
    public void onNothingSelected(AdapterView parent) {
    }

    public View makeView() {
        ImageView i = new ImageView(this);
        i.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return i;
    }

    private class NavigationBackButtonListener implements OnClickListener {
        private Activity activity;

        public NavigationBackButtonListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public void onClick(View v) {
            activity.finish();
        }
    }

    private class MSwitcherOnTouchListener implements OnTouchListener {
        private Activity activity;

        public MSwitcherOnTouchListener(Activity activity) {
            this.activity = activity;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                //手指按下
                case MotionEvent.ACTION_DOWN:
                    //记录起始坐标
                    startX = event.getX();
                    break;
                //手指抬起
                case MotionEvent.ACTION_UP:
                    if (event.getX() != startX) {
                        if (event.getX() < startX && position < goodsGalleryList.size() - 1) {
                            position++;
                        }
                        if (event.getX() > startX && position > 0) {
                            position--;
                        }
                    } else {
                        activity.finish();//单击关闭该窗口
                    }
                    break;
            }
            return true;
        }

    }

    @SuppressWarnings("unused")
    private OnTouchListener mSwitcherOnTouchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                //手指按下
                case MotionEvent.ACTION_DOWN:
                    //记录起始坐标
                    startX = event.getX();
                    break;
                //手指抬起
                case MotionEvent.ACTION_UP:
                    if (event.getX() < startX && position < goodsGalleryList.size()) {
                        position++;
                    }
                    if (event.getX() > startX && position > 0) {
                        position--;
                    }
                    break;
            }
            return true;
        }
    };


    class GetImageTask extends AsyncTask<String, Integer, Bitmap> {
        private Activity mActivity;

        public GetImageTask(Activity activity) {
            mActivity = activity;
        }

        /**
         * 处理后台执行的任务，在后台线程执行
         */
        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap bitmap;
            try {
                URL picUrl = new URL(params[0]);
                HttpURLConnection urlConn;
                urlConn = (HttpURLConnection) picUrl.openConnection();
                urlConn.setConnectTimeout(5000);
                urlConn.setReadTimeout(5000);
                InputStream is = urlConn.getInputStream();
                bitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (Exception e) {
                return null;
            }
            return bitmap;
        }

        /**
         * 在调用publishProgress之后被调用，在UI线程执行
         */
        protected void onProgressUpdate(Integer... progress) {
            //mProgressBar.setProgress(progress[0]);// 更新进度条的进度
        }

        /**
         * 后台任务执行完之后被调用，在UI线程执行
         */
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                Drawable drawable = new BitmapDrawable(result);
                imageSwitcher.setImageDrawable(drawable);
            } else {
                //获取图片网络失败
            }
//            LoadingViewHandler.dismiss();
        }

        /**
         * 在 doInBackground(Params...)之前被调用，在UI线程执行
         */
        protected void onPreExecute() {
//            LoadingViewHandler.creteProgressDialog(mActivity, "");
        }

        /**
         * 在UI线程执行
         */
        protected void onCancelled() {
        }

    }

}
