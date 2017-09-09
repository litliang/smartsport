package top.smartsport.www.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;


import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.ScorerInfo;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by bajieaichirou on 17/9/8.
 */
public class ScorerAdapter extends EntityListAdapter<ScorerInfo,ScorerViewHolder> {

    public ScorerAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_scorer_item;
    }

    @Override
    protected ScorerViewHolder getViewHolder(View root) {
        return new ScorerViewHolder(root);
    }

    @Override
    protected void initViewHolder(ScorerViewHolder viewHolder, int position) {
        viewHolder.init(getItem(position));
    }
}
class ScorerViewHolder extends ViewHolder {
    @ViewInject(R.id.scorer_item_number_tv)
    TextView mNumTv;
    @ViewInject(R.id.scorer_item_username_tv)
    TextView mUserNameTv;
    @ViewInject(R.id.scorer_item_iv)
    ImageView mIconIv;
    @ViewInject(R.id.scorer_item_name_tv)
    TextView mTeamNameTv;
    @ViewInject(R.id.scorer_item_score_tv)
    TextView mScoreTv;

    public ScorerViewHolder(View root) {
        super(root);
    }

    public void init(ScorerInfo info){
        mNumTv.setText(info.getPosition());
    }
}
