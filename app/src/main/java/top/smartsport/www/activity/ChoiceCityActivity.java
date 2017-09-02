package top.smartsport.www.activity;

import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.zhy.autolayout.utils.AutoUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import top.smartsport.www.O;
import top.smartsport.www.R;
import top.smartsport.www.adapter.HotCityAdapter;
import top.smartsport.www.adapter.NameAdapter;
import top.smartsport.www.base.BaseActivity;
import top.smartsport.www.base.EntityListAdapter;
import top.smartsport.www.bean.ComCity;
import top.smartsport.www.bean.HotCity;
import top.smartsport.www.bean.PlaceItemInfo;
import top.smartsport.www.utils.GaoDeUtils;
import top.smartsport.www.utils.SPUtils;
import top.smartsport.www.widget.QuickIndex;

/**
 * Created by Aaron on 2017/7/12.
 */
@ContentView(R.layout.activity_choicecity)
public class ChoiceCityActivity extends BaseActivity {
    @ViewInject( R.id.index_bar)
    private QuickIndex mIndexBar;
    @ViewInject( R.id.lv)
    private ListView mLv;
    @ViewInject( R.id.tv_index)
    private TextView mTv;
    @ViewInject(R.id.gv_hotcity)
    private GridView gv_hotcity;
    @ViewInject(R.id.citychoose_et_searchtext)
    private EditText  et_searchtext;

    @ViewInject(R.id.tv1)
    private TextView tv1;
    @ViewInject(R.id.rl1)
    private RelativeLayout rl1;
    private   NameAdapter2 nameAdapter;

    @ViewInject(R.id.cc_text_location)
    private TextView cc_text_location;


    @Override
    protected void initView() {
        initLocation();//获取地理位置
        mIndexBar = (QuickIndex) findViewById(R.id.index_bar);
        mLv = (ListView) findViewById(R.id.lv);
        mTv = (TextView) findViewById(R.id.tv_index);
        getCity();
        mIndexBar.setOnLetterChangeListener(new QuickIndex.OnLetterChangeListener() {

            @Override
            public void onLetterChange(String letter) {
                showTextView(letter);
                for (int i = 0; i < mDatas.size(); i++) {
                    String firstLetter = String.valueOf(mDatas.get(i).mPinyin.charAt(0));
                    if (TextUtils.equals(firstLetter, letter)) {
                        mLv.setSelection(i); // 让ListView移动到某个位置
                        break;
                    }
                }
            }
        });
        // 填充数据集合
        nameAdapter=new NameAdapter2(ChoiceCityActivity.this);
        fillDataAndSort();
        mLv.setAdapter(nameAdapter);
        mLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlaceItemInfo placeItemInfo = mDatas.get(position);
                Intent intent =new Intent();
                intent.putExtra("placeItemInfo",placeItemInfo);
                SPUtils.put(getApplicationContext(),"city",placeItemInfo.mName);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        gv_hotcity.setAdapter(hotCityAdapter);
        gv_hotcity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HotCity item = hotCityAdapter.getItem(position);
                PlaceItemInfo info=new PlaceItemInfo(item.getTitle(),item.getArea_id());
                Intent intent =new Intent();
                intent.putExtra("placeItemInfo",info);
                SPUtils.put(getApplicationContext(),"city",info.mName);
                setResult(RESULT_OK,intent);
                finish();

            }
        });


        et_searchtext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    if(!et_searchtext.getText().toString().equals("")){
                        String city=et_searchtext.getText().toString();
                        fillDataAndSort(city);
                        nameAdapter.notifyDataSetChanged();

                    }else{
                        fillDataAndSort();
                        nameAdapter.notifyDataSetChanged();
                    }
                }

                return false;
            }
        });
    }

    /**
     * 初始化定位
     *
     */
    private AMapLocationClient locationClient = null;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;

    private void initLocation(){
        //初始化client
        locationClient = new AMapLocationClient(this.getApplicationContext());
        //初始化定位参数
        mLocationOption = new AMapLocationClientOption();

        //设置定位监听
        locationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //定位成功回调信息，设置相关消息
                        String city = amapLocation.getCity();
                        cc_text_location.setText(city+"");


                    } else {
                        //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError","location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        });

        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);

        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(200000);
        //设置定位参数
        locationClient.setLocationOption(mLocationOption);
        locationClient.startLocation();

    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != locationClient) {
            locationClient.onDestroy();
            locationClient = null;
        }
    }
    private ArrayList<PlaceItemInfo> mDatas = new ArrayList<PlaceItemInfo>();

    private Handler mHandler = new Handler();

    protected void showTextView(String letter) {
        mTv.setVisibility(View.VISIBLE);
        mTv.setText(letter);
        mHandler.removeCallbacksAndMessages(null); // 移除掉之前没执行的任务
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mTv.setVisibility(View.GONE);
            }
        }, 1000);
    }

    private void fillDataAndSort() {
        mDatas.clear();
        for (int i = 0; i < comCityList.length; i++) {
            PlaceItemInfo PlaceItemInfo = new PlaceItemInfo(comCityList[i].getTitle(),
                    comCityList[i].getArea_id()
            );
            mDatas.add(PlaceItemInfo);
        }
        Collections.sort(mDatas);
        nameAdapter.clear();
        nameAdapter.addAll(mDatas);
        if(mDatas.size()==0){
            rl1.setVisibility(View.INVISIBLE);
            tv1.setVisibility(View.VISIBLE);
        }else{
            rl1.setVisibility(View.VISIBLE);
            tv1.setVisibility(View.INVISIBLE);
        }
    }


    private void fillDataAndSort(String name){
        mDatas.clear();
        for (int i = 0; i < comCityList.length; i++) {
            PlaceItemInfo placeItemInfo = new PlaceItemInfo(comCityList[i].getTitle(),
                    comCityList[i].getArea_id()
            );
            if(placeItemInfo.mName.contains(name)){
                mDatas.add(placeItemInfo);
            }
        }
        Collections.sort(mDatas);
        nameAdapter.clear();
        nameAdapter.addAll(mDatas);
        if(mDatas.size()==0){
            rl1.setVisibility(View.INVISIBLE);
            tv1.setVisibility(View.VISIBLE);
        }else{
            rl1.setVisibility(View.VISIBLE);
            tv1.setVisibility(View.INVISIBLE);
        }
    }


    private ComCity[] comCityList;
    private HotCity[] hotCityList;


    private HotCityAdapter hotCityAdapter;
    public void getCity() {

        List<ComCity> comCity = O.getComAreas();
        List<HotCity> hotCity = O.getHotAreas();
        comCityList = (ComCity[]) comCity.toArray(new ComCity[comCity.size()]);
        hotCityList= (HotCity[]) hotCity.toArray(new HotCity[hotCity.size()]);
        hotCityAdapter=new HotCityAdapter(this);
        hotCityAdapter.addAll(hotCity);

    }



    private class NameAdapter extends BaseAdapter implements ListAdapter {

        private String mFirstLetter;
        private String mPreFirstLetter;
        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(getApplicationContext(), R.layout.layout_placeitem, null);
                holder = new ViewHolder();
                holder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tvPinyin = (TextView) convertView.findViewById(R.id.tv_pinyin);
                convertView.setTag(holder);
                AutoUtils.autoSize(convertView);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            PlaceItemInfo PlaceItemInfo = (PlaceItemInfo) getItem(position);
            mFirstLetter = String.valueOf(PlaceItemInfo.mPinyin.charAt(0));
            // 对于0号索引, 特殊处理
            if (position == 0) {
                mPreFirstLetter = "-"; // 只要和 A 不一样就可以
            } else {
                PlaceItemInfo prePlaceItemInfo = (PlaceItemInfo) getItem(position - 1);
                mPreFirstLetter = String.valueOf(prePlaceItemInfo.mPinyin.charAt(0));
            }
            // 根据当前首字母和之前条目的首字母作对比, 决定是否显示字母条
            if (mFirstLetter.equals(mPreFirstLetter)) {
                holder.tvPinyin.setVisibility(View.GONE);
            } else {
                holder.tvPinyin.setVisibility(View.VISIBLE);
            }
            holder.tvName.setText(PlaceItemInfo.mName);
            holder.tvPinyin.setText(String.valueOf(PlaceItemInfo.mPinyin.charAt(0)));
            return convertView;
        }

    }

    static class ViewHolder {
        TextView tvName;
        TextView tvPinyin;
    }



    private class NameAdapter2 extends EntityListAdapter<PlaceItemInfo,ViewHolder2> {

        public NameAdapter2(Context context) {
            super(context);
        }

        public NameAdapter2(Context context, List<PlaceItemInfo> list) {
            super(context, list);
        }

        @Override
        protected int getAdapterRes() {
            return R.layout.layout_placeitem;
        }

        @Override
        protected ViewHolder2 getViewHolder(View root) {
            return new ViewHolder2(root);
        }

        @Override
        protected void initViewHolder(ViewHolder2 viewHolder2, int position) {
            if(position==0){
                viewHolder2.initView(getItem(position),position,getItem(0));
            }else {
                viewHolder2.initView(getItem(position), position, getItem(position - 1));
            }
        }
    }
    static class ViewHolder2 extends top.smartsport.www.utils.ViewHolder {
        @ViewInject(R.id.tv_name)
        private TextView tvName;
        @ViewInject(R.id.tv_pinyin)
        private TextView tvPinyin;

        private String mFirstLetter;
        private String mPreFirstLetter;

        public ViewHolder2(View root) {
            super(root);
        }

        public void initView(PlaceItemInfo item,int position,PlaceItemInfo preItem) {
            mFirstLetter = String.valueOf(item.mPinyin.charAt(0));
            // 对于0号索引, 特殊处理
            if (position == 0) {
                mPreFirstLetter = "-"; // 只要和 A 不一样就可以
            } else {
                mPreFirstLetter = String.valueOf(preItem.mPinyin.charAt(0));
            }
            // 根据当前首字母和之前条目的首字母作对比, 决定是否显示字母条
            if (mFirstLetter.equals(mPreFirstLetter)) {
                tvPinyin.setVisibility(View.GONE);
            } else {
                tvPinyin.setVisibility(View.VISIBLE);
            }
            tvName.setText(item.mName);
            tvPinyin.setText(String.valueOf(item.mPinyin.charAt(0)));
        }


    }

}
