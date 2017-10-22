package top.smartsport.www.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import top.smartsport.www.R;

/**
 * Created by Aaron on 2017/7/2.
 */

public class ImageUtil {

    private static DisplayImageOptions options;
    private static DisplayImageOptions options_avater;
    private static DisplayImageOptions options_avater_doc;
    private static DisplayImageOptions options_avater_doc2;
    private static DisplayImageOptions options_gallery;
    private static DisplayImageOptions options2;
    private static AbsListView.OnScrollListener pauseScrollListener;

    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "imageloader_yjqzx/Cache");

        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        // config.memoryCacheExtraOptions(480, 800); // max width, max height
        config.threadPoolSize(3);// 线程池内加载的数量
        config.threadPriority(Thread.NORM_PRIORITY - 2); // 降低线程的优先级保证主UI线程不受太大影响
        config.denyCacheImageMultipleSizesInMemory();
        config.memoryCache(new WeakMemoryCache());
        // 建议内存设在5-10M,可以有比较好的表现
        // config.memoryCacheSize(20 * 1024 * 1024);
        // config.diskCacheSize(50 * 1024 * 1024);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.diskCacheFileCount(100); // 缓存的文件数量
        config.diskCache(new UnlimitedDiskCache(cacheDir));
        config.defaultDisplayImageOptions(getOptions());
        config.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000));
        config.memoryCacheExtraOptions(512, 512);
        ImageLoader.getInstance().init(config.build());

        com.nostra13.universalimageloader.utils.L.writeDebugLogs(false);
        com.nostra13.universalimageloader.utils.L.writeLogs(false);
    }

    private static ImageLoadingListener imageLoadingListener;

    static {
        options = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.default_img).showImageForEmptyUri(R.mipmap.default_img)
                .showImageOnFail(R.mipmap.default_img).cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置为RGB565比起默认的ARGB_8888要节省大量的内存
                .delayBeforeLoading(100)// 载入图片前稍做延时可以提高整体滑动的流畅度
                .build();

        options_avater = new DisplayImageOptions.Builder().showImageOnLoading(R.mipmap.default_head_image).showImageForEmptyUri(R.mipmap.default_head_image)
                .showImageOnFail(R.mipmap.default_head_image).cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置为RGB565比起默认的ARGB_8888要节省大量的内存
                .delayBeforeLoading(100)// 载入图片前稍做延时可以提高整体滑动的流畅度
                .displayer(new RoundedBitmapDisplayer(200)).build();

        options_gallery = new DisplayImageOptions.Builder().showImageForEmptyUri(R.mipmap.default_img).showImageOnFail(R.mipmap.default_img)
                .showImageOnLoading(R.mipmap.default_img).cacheInMemory(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.EXACTLY).build();

        options2 = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)// 设置为RGB565比起默认的ARGB_8888要节省大量的内存
                .displayer(new RoundedBitmapDisplayer(10)).build();

        pauseScrollListener = new PauseOnScrollListener(ImageLoader.getInstance(), true, true);

    }

    public static Bitmap scaleImage(Bitmap bm, int newWidth, int newHeight) {
        if (bm == null) {
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        if (width <= 0 || height <= 0) {
            return bm;
        }
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbm;
        try {
            newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                    true);
        } catch (Exception e) {
            return bm;
        }
        if (bm != null & !bm.isRecycled()) {
            bm.recycle();
            bm = null;
        }
        return newbm;
    }


    public static ImageLoadingListener getImageLoadingListener() {
        return getImageLoadingListener(false);
    }

    public static ImageLoadingListener getImageLoadingListener(final boolean palignwidth) {

        return new ImageLoadingListener() {
            boolean alignviewwidth;

            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String s, final View view, final Bitmap bitmap) {
                if(bitmap == null || bitmap.isRecycled()){
                    return;
                }
                this.alignviewwidth = palignwidth;
                if (alignviewwidth) {
                    int height = view.getWidth() * bitmap.getHeight() / bitmap.getWidth();
                    view.getLayoutParams().height = height;
                    if(height==0||view.getWidth()==0){
                        return;
                    }
                    view.measure(view.getWidth(), height);
                    view.invalidate();
                    view.requestLayout();
                    new Handler(Looper.getMainLooper(), new Handler.Callback() {

                        @Override
                        public boolean handleMessage(Message message) {
                            ((ImageView) view).setImageBitmap(scaleImage(bitmap, view.getWidth(), view.getHeight()));
                            return false;
                        }
                    }).sendEmptyMessageDelayed(0, 100);

                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        };
    }


    public static DisplayImageOptions getOptions() {
        return options;
    }

    public static DisplayImageOptions getOptions_avater() {
        return options_avater;
    }

    public static DisplayImageOptions getOptions_avater_doc() {
        return options_avater_doc;
    }

    public static DisplayImageOptions getOptions_avater_doc2() {
        return options_avater_doc2;
    }

    public static DisplayImageOptions getOptions_gallery() {
        return options_gallery;
    }

    public static AbsListView.OnScrollListener getPauseScrollListener() {
        return pauseScrollListener;
    }

    public static DisplayImageOptions getOptions2() {
        return options2;
    }


    /**
     * 根据路径获得图片并压缩，返回bitmap用于显示
     */
    public static Bitmap getSmallBitmap(String filePath) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 640);

        // Decode bitmap withView inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }


    /**
     * 计算图片的缩放值
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }


    /**
     * 压缩图片
     */

    public static File bitmapToPng(String filePath, String filename) {
        File dir;
        File file;
        Bitmap bm = getSmallBitmap(filePath);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            dir = new File(Environment.getExternalStorageDirectory() + "/pxgo/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(Environment.getExternalStorageDirectory() + "/pxgo//" + filename + ".jpg");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            dir = new File("/pxgo/");
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File("/pxgo" + filename + ".jpg");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            if (bm.compress(Bitmap.CompressFormat.JPEG, 50, fout)) {
                try {
                    fout.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fout.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

}

