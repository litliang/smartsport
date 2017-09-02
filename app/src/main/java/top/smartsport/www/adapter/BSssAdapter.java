package top.smartsport.www.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.activity.BSDetailActivity;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.BSssInfo;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by Aaron on 2017/7/24.
 * 比赛--赛事
 */

public class BSssAdapter extends EntityListAdapter<BSssInfo,BSssViewHolder>{


    public BSssAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_bsss;
    }

    @Override
    protected BSssViewHolder getViewHolder(View root) {
        return new BSssViewHolder(root);
    }

    @Override
    protected void initViewHolder(BSssViewHolder bSssViewHolder, int position) {
        bSssViewHolder.init(getItem(position),mContext);
    }
}

class BSssViewHolder extends ViewHolder{
    @ViewInject(R.id.adapter_bsss_state)
    private TextView adapter_bsss_state;
    @ViewInject(R.id.adapter_bsss_img)
    private ImageView adapter_bsss_img;
    @ViewInject(R.id.adapter_bsss_title)
    private TextView adapter_bsss_title;
    @ViewInject(R.id.adapter_bsss_date)
    private TextView adapter_bsss_date;
    @ViewInject(R.id.adapter_bsss_address)
    private TextView adapter_bsss_address;
    @ViewInject(R.id.adapter_bsss_pay)
    private TextView adapter_bsss_pay;
    @ViewInject(R.id.adapter_bsss_level)
    private TextView adapter_bsss_level;
    @ViewInject(R.id.adapter_bsss_people)
    private TextView adapter_bsss_people;
    @ViewInject(R.id.adapter_bsss_syu)
    private TextView adapter_bsss_syu;
    @ViewInject(R.id.adapter_bsss_rl_detail)
    private RelativeLayout adapter_bsss_rl_detail;
    @ViewInject(R.id.adapter_bsss_rl_pay)
    private RelativeLayout adapter_bsss_rl_pay;

    @ViewInject(R.id.adapter_bsss_wybm)
    private TextView adapter_bsss_wybm;
    @ViewInject(R.id.adapter_bsss_detail)
    private TextView adapter_bsss_detail;



    public BSssViewHolder(View root) {
        super(root);
    }

    public void init(BSssInfo info, final Context context){
        adapter_bsss_state.setText(info.getStatus());
        String state = info.getStatus();
        if(state.equals("报名中")){
            adapter_bsss_state.setBackgroundResource(R.drawable.shape_bg_button);
            adapter_bsss_rl_pay.setVisibility(View.VISIBLE);
            adapter_bsss_rl_detail.setVisibility(View.GONE);
//            adapter_bsss_wybm.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //我要报名（跳转详情）
//                    Intent intent = new Intent(context, BSDetailActivity.class);
//                    context.startActivity(intent);
//                }
//            });
        }
        if(state.equals("进行中")){
            adapter_bsss_state.setBackgroundResource(R.drawable.shape_bg_button_blue);
            adapter_bsss_rl_pay.setVisibility(View.GONE);
            adapter_bsss_rl_detail.setVisibility(View.VISIBLE);
//            adapter_bsss_detail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //查看详情
//                    Intent intent = new Intent(context, BSDetailActivity.class);
//                    context.startActivity(intent);
//                }
//            });
        }
        if(state.equals("已结束")){
            adapter_bsss_state.setBackgroundResource(R.drawable.shape_bg_button_gray);
            adapter_bsss_rl_pay.setVisibility(View.GONE);
            adapter_bsss_rl_detail.setVisibility(View.VISIBLE);
//            adapter_bsss_detail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    //查看详情
//                    Intent intent = new Intent(context, BSDetailActivity.class);
//                    context.startActivity(intent);
//                }
//            });
        }
        ImageLoader.getInstance().displayImage(info.getCover(), adapter_bsss_img, ImageUtil.getOptions());
        adapter_bsss_title.setText(info.getName());
        adapter_bsss_date.setText(info.getStart_time()+"至"+info.getEnd_time());
        adapter_bsss_address.setText(info.getAddress());
        adapter_bsss_pay.setText("￥"+info.getSell_price().replace(".00",""));
        adapter_bsss_level.setText(info.getLevel());
        adapter_bsss_people.setText(info.getType());
        adapter_bsss_syu.setText("还剩"+info.getSurplus()+"个名额");

    }
}
