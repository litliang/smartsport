package top.smartsport.www.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.xutils.view.annotation.ViewInject;

import top.smartsport.www.R;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.Courses;
import top.smartsport.www.utils.ImageUtil;
import top.smartsport.www.utils.ViewHolder;

/**
 * Created by Aaron on 2017/8/9.
 */

public class CoursesAdapter extends EntityListAdapter<Courses,CoursesHolder> {
    public CoursesAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getAdapterRes() {
        return R.layout.adapter_courses;
    }

    @Override
    protected CoursesHolder getViewHolder(View root) {
        return new CoursesHolder(root);
    }

    @Override
    protected void initViewHolder(CoursesHolder coursesHolder, int position) {
        coursesHolder.init(getItem(position));
    }
}
class CoursesHolder extends ViewHolder{
    @ViewInject(R.id.course_img)
    private ImageView course_img;
    @ViewInject(R.id.course_name)
    private TextView course_name;
    @ViewInject(R.id.course_money)
    private TextView course_money;
    public CoursesHolder(View root) {
        super(root);
    }

    public void init(Courses info){
        ImageLoader.getInstance().displayImage(info.getCover_url(), course_img, ImageUtil.getOptions());
        course_name.setText(info.getTitle());
        course_money.setText("￥"+info.getSell_price());
    }
}
