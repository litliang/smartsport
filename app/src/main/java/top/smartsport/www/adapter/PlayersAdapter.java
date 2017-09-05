package top.smartsport.www.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.view.annotation.ViewInject;

import java.io.Serializable;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.Players;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by Aaron on 2017/8/10.
 */

public class PlayersAdapter extends EntityListAdapter<Players,PlayersHolder>  implements Serializable{
    public PlayersAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_players;
    }

    @Override
    protected PlayersHolder getViewHolder(View root) {
        return new PlayersHolder(root);
    }

    @Override
    protected void initViewHolder(PlayersHolder playersHolder, int position) {
        playersHolder.init(getItem(position));
    }
}

class PlayersHolder extends ViewHolder{
    @ViewInject(R.id.players_img)
    private ImageView players_img;
    @ViewInject(R.id.players_name)
    private TextView players_name;
    @ViewInject(R.id.players_num)
    private TextView players_num;
    @ViewInject(R.id.players_dis)
    private TextView players_dis;

    public PlayersHolder(View root) {
        super(root);
    }
    public void init(Players info){
        ImageLoader.getInstance().displayImage(info.getCover_url(), players_img, ImageUtil.getOptions());
        players_num.setText(info.getStage()+"\n"+"期");
        players_name.setText(info.getName());
        players_dis.setText(info.getTeam_name());

    }
}
