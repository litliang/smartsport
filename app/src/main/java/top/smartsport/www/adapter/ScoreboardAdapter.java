package top.smartsport.www.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;


import org.xutils.view.annotation.ViewInject;

import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.ScoreboardInfo;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by bajieaichirou on 17/9/8.
 */
public class ScoreboardAdapter extends EntityListAdapter<ScoreboardInfo,ScoreboardViewHolder> {
    private List<ScoreboardInfo> list;
    public ScoreboardAdapter(Context context) {
        super(context);
//        this.list = list;
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_scoreboard_item;
    }

    @Override
    protected ScoreboardViewHolder getViewHolder(View root) {
        return new ScoreboardViewHolder(root);
    }

    @Override
    protected void initViewHolder(ScoreboardViewHolder viewHolder, int position) {
        viewHolder.init(getItem(position));
    }
}
class ScoreboardViewHolder extends ViewHolder {
    @ViewInject(R.id.item_number_tv)
    TextView mNumTv;
    @ViewInject(R.id.item_name_tv)
    TextView mNameTv;
    @ViewInject(R.id.item_result_tv)
    TextView mResultTv;
    @ViewInject(R.id.item_scored_tv)
    TextView mScoredTv;
    @ViewInject(R.id.item_card_tv)
    TextView mCardTv;
    @ViewInject(R.id.item_score_tv)
    TextView mScoreTv;

    public ScoreboardViewHolder(View root) {
        super(root);
    }

    public void init(ScoreboardInfo info){
//        mNumTv.setText(info.getPosition());
//        String temp = info.getCard();
//        SpannableStringBuilder builder = new SpannableStringBuilder(temp);
//        int start_index = temp.indexOf("红");
//        int start_index1 = temp.indexOf("黄");
//        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF3B30")), start_index, start_index + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        builder.setSpan(new ForegroundColorSpan(Color.parseColor("#FFD700")), start_index1, start_index1 + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        mCardTv.setText(builder);
//        mResultTv.setText(info.getResult());
    }
}
