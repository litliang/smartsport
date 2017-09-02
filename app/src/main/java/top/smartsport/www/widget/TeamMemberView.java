package top.smartsport.www.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.ButterKnife;
import top.smartsport.www.R;

/**
 * Created by zl on 17/8/3.
 * 自定义设置布局
 */
public class TeamMemberView extends RelativeLayout {

    TextView mLaber;
    TextView mName;
    TextView mLocation;
    TextView mNumber;

    public TeamMemberView(Context context) {
        super(context);
    }

    public TeamMemberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TeamMemberView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_team_member, this);
        setBackgroundColor(Color.parseColor("#ffffff"));
        ButterKnife.bind(this, view);
        mLaber = (TextView) view.findViewById(R.id.tv_laber);
        mName = (TextView) view.findViewById(R.id.tv_name);
        mLocation = (TextView) view.findViewById(R.id.tv_location);
        mNumber = (TextView) view.findViewById(R.id.tv_number);


        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TeamMemberView);


        String label = typedArray.getString(R.styleable.TeamMemberView_laber);
        int colorLaber = typedArray.getColor(R.styleable.TeamMemberView_laber_color, 0);
        mLaber.setTextColor(colorLaber);
        mLaber.setText(label);

        String hintName = typedArray.getString(R.styleable.TeamMemberView_content_hint_name);
        String hintLocation = typedArray.getString(R.styleable.TeamMemberView_content_hint_location);
        String hintNumber = typedArray.getString(R.styleable.TeamMemberView_content_hint_number);
        mName.setHint(hintName);
        mLocation.setHint(hintLocation);
        mNumber.setHint(hintNumber);

        int colorContent = typedArray.getColor(R.styleable.TeamMemberView_content_color, 0);
        mName.setTextColor(colorContent);
        mLocation.setTextColor(colorContent);
        mNumber.setTextColor(colorContent);

        String name = typedArray.getString(R.styleable.TeamMemberView_content_name);
        String location = typedArray.getString(R.styleable.TeamMemberView_content_location);
        String number = typedArray.getString(R.styleable.TeamMemberView_content_number);
        mName.setText(name);
        mLocation.setText(location);
        mNumber.setText(number);
        typedArray.recycle();
    }

    public void setLaber(String laber) {
        if (!TextUtils.isEmpty(laber)) {
            mLaber.setText(laber);
        }
    }

    public void setName(String name) {
        mName.setText(name);
    }
    public void setLocation(String location) {
        mLocation.setText(location);
    }
    public void setNumber(String number) {
        mNumber.setText(number);
    }

    public String getName(){
        return mName.getText().toString();
    }
    public String getLocation(){
        return mLocation.getText().toString();
    }
    public String getNumber(){
        return mNumber.getText().toString();
    }

}
