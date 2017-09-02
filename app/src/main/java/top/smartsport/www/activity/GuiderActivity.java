package top.smartsport.www.activity;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import top.smartsport.www.R;
import top.smartsport.www.base.BaseActivity;

/**
 * Created by Aaron on 2017/7/11.
 * 引导页
 */
@ContentView(R.layout.activity_guider)
public class GuiderActivity extends BaseActivity {
    @ViewInject(R.id.viewpager_guide)
    private ViewPager viewpager_guide;
    @ViewInject(R.id.viewpager_button)
    private Button viewpager_button;


    @Override
    public void featureNoTitle() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    @Override
    protected void initView() {
        initViewPager();
        viewpager_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuiderActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    @Event(value = {R.id.viewpager_button})
    private void getEvent(View v){
        switch (v.getId()){
            case R.id.viewpager_button:
                goActivity(LoginActivity.class);
                finish();
                break;
        }
    }

    /**
     * 初始化viewPager
     */

    private List<View> list;
    public void initViewPager(){
        list = new ArrayList<>();

        ImageView iv1 = new ImageView(this);
        iv1.setImageResource(R.mipmap.p1);
        iv1.setScaleType(ImageView.ScaleType.FIT_XY);
        list.add(iv1);

        ImageView iv2 = new ImageView(this);
        iv2.setImageResource(R.mipmap.p2);
        iv2.setScaleType(ImageView.ScaleType.FIT_XY);
        list.add(iv2);

        ImageView iv3 = new ImageView(this);
        iv3.setImageResource(R.mipmap.p3);
        iv3.setScaleType(ImageView.ScaleType.FIT_XY);
        list.add(iv3);

        viewpager_guide.setAdapter(new MyAdapter());
        viewpager_guide.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                if(position==2){
                    viewpager_button.setVisibility(View.VISIBLE);
                }else{
                    viewpager_button.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

        });


    }

    /**
     * 自定义pagerAdaper
     */
    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }
    }


}
