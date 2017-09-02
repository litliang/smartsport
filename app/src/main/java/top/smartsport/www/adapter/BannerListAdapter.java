package top.smartsport.www.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.banner.BannerIndicatorView;
import top.smartsport.www.banner.BannerView;
import top.smartsport.www.bean.Carousel;
import top.smartsport.www.bean.HDZXInfo;
import top.smartsport.www.bean.PicInfo;
import top.smartsport.www.bean.Players;
import top.smartsport.www.bean.SSXWInfo;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.widget.Banner;

import static com.letv.ads.constant.AdInfoConstant.AdZoneType.BANNER;

/**
 * Created by Aaron on 2017/8/13.
 */

public class BannerListAdapter extends BaseAdapter {

    private static final int BANMER =0;
    private static final int LISTIMAGE =1;

    private Context context;
    private List<SSXWInfo> list;
    private List<Carousel> imgs;

    private LayoutInflater inflater;
    private BannerView mBannerView;

    public BannerListAdapter(Context context, List<SSXWInfo> list,List<Carousel> imgs ){
        this.context = context;
        this.list = list;
        this.imgs =imgs;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        switch (getItemViewType(position)) {
            case BANMER:
                return getItemBanner(convertView, 0);//轮播
            case LISTIMAGE:
                return getone(position, convertView);//
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return BANMER;
        }else {
            return LISTIMAGE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }


    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return super.getDropDownView(position, convertView, parent);
    }

    private View getItemBanner(View convertView, int position) {
        if (convertView == null && position == 0) {
            convertView = inflater.inflate(R.layout.layout_banner, null);
//            Log.i("mBanners" + mBanners, "****************************************************************");
//            mBannerView = (BannerView) convertView.findViewById(R.id.banner_view);
//            mBannerView.init(mBanners);
//            mBannerView.setIndicator((BannerIndicatorView) convertView.findViewById( R.id.banner_indicator_view));
//            Banner banner = new Banner(convertView, context, new Banner.MultiItemClickListener() {
//                @Override
//                public void onMultiItemClick(Banner.BannerData data) {
//
//                }
//            });
//            banner.init(imgs);
        }
        return convertView;
    }
    private View getone(int position, View convertView) {
        ViewHolder viewHolder;
        if(convertView ==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.adapter_news, null);
            viewHolder.name = (TextView)convertView.findViewById(R.id.news_name);
            viewHolder.date = (TextView)convertView.findViewById(R.id.news_date);
            viewHolder.dis = (TextView)convertView.findViewById(R.id.news_dis);
            viewHolder.hint = (TextView)convertView.findViewById(R.id.news_hint);
            viewHolder.img = (ImageView) convertView.findViewById(R.id.news_img);



            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.name.setText(list.get(position).getCate_name());
            viewHolder.date.setText(list.get(position).getCtime());
            viewHolder.dis.setText(list.get(position).getDescription());
            viewHolder.hint.setText("阅读："+list.get(position).getHits());
            ImageLoader.getInstance().displayImage(list.get(position).getCover_url(), viewHolder.img, ImageUtil.getOptions());
        }


        return convertView;
    }

    class ViewHolder
    {
        public TextView name,date,dis,hint;
        public ImageView img;
    }

}
