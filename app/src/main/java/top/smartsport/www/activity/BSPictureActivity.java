package top.smartsport.www.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import cn.android.gallery.GoodsGalleryActivity;
import top.smartsport.www.R;
import top.smartsport.www.adapter.PICAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.bean.PicInfo;
import top.smartsport.www.widget.MyGridView;

import java.io.Serializable;
import java.util.List;

@ContentView(R.layout.activity_bspicture)
public class BSPictureActivity extends BaseActivity {
    @ViewInject(R.id.pic_gridView)
    MyGridView pic_gridView;

    @ViewInject(R.id.action_bar)
    View actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View getTopBar() {
        return actionbar;
    }

    @Override
    protected void initView() {
        actionbar.findViewById(R.id.ivLeft).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((TextView)actionbar.findViewById(R.id.tvTitle)).setText("赛事图片");
//        ((TextView)actionbar.findViewById(R.id.ivRight_text)).setText("上传图片");
        actionbar.findViewById(R.id.ivRight).setVisibility(View.GONE);
        final List<PicInfo> pics = (List<PicInfo>) getIntent().getExtras().getSerializable("pics");
        PICAdapter picAdapter = new PICAdapter(this);
        pic_gridView.setAdapter(picAdapter);
        picAdapter.addAll(pics);
        pic_gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(getBaseContext(), GoodsGalleryActivity.class).putExtra("urls", (Serializable) pics).putExtra("position",i));
            }
        });
    }
}
