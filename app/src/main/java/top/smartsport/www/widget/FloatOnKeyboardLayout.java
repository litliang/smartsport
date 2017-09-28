package top.smartsport.www.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;

import top.smartsport.www.R;


public class FloatOnKeyboardLayout extends RelativeLayout {

    private static final int ID_CHILD = R.id.id_autolayout;
    private Context mContext;
    private int mOldKeyboardHeight = -1;
    private int mNowKeyboardHeight = -1;
    protected int mScreenHeight = 0;

    private int trueViewY;

    private View view;
    //相差距离
    private int distance;

    public FloatOnKeyboardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        mContext = context;

        setSoftKeyboardListener();
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        int childSum = this.getChildCount();
        if (childSum > 1) {
            throw new IllegalStateException("can host only one direct child");
        } else {
            super.addView(child, index, params);
            LayoutParams paramsChild;
            if (childSum == 0) {
                if (child.getId() < 0) {
                    child.setId(ID_CHILD);
                }

                paramsChild = (LayoutParams) child.getLayoutParams();
                //paramsChild.setMargins(0, 300, 0, 0);
                paramsChild.addRule(12);
                child.setLayoutParams(paramsChild);
            } else if (childSum == 1) {
                paramsChild = (LayoutParams) child.getLayoutParams();
                //paramsChild.setMargins(0, 300, 0, 0);
                paramsChild.addRule(2, ID_CHILD);
                child.setLayoutParams(paramsChild);
            }

        }
    }

    //设置要弹得view
    public void setView(final View view) {
        this.view = view;
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int[] outLocation = new int[2];
                view.getLocationOnScreen(outLocation);
                if (outLocation.length >= 2){
                    trueViewY = outLocation[1];
                }
            }
        });
    }

    private void setSoftKeyboardListener() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                try {
                    Rect r = new Rect();

                    ((Activity) mContext).getWindow().getDecorView().getWindowVisibleDisplayFrame(r);

                    if (mScreenHeight == 0) {
                        mScreenHeight = r.bottom;
                    }

                    mNowKeyboardHeight = mScreenHeight - r.bottom;

                    boolean visible = mNowKeyboardHeight > mScreenHeight / 3;
                    if (mOldKeyboardHeight != -1 && mNowKeyboardHeight != mOldKeyboardHeight) {
                        if (mNowKeyboardHeight > 100) {
                            setChildViewPosition(mNowKeyboardHeight, visible);
                        } else {
                            setChildViewPosition(mNowKeyboardHeight, visible);
                        }
                    }
                    mOldKeyboardHeight = mNowKeyboardHeight;
                }catch (Exception e){
                }
            }
        });

    }


    public void resetLisenter() {
        setSoftKeyboardListener();
    }

    //如果设定view较低,view弹到软键盘位置，键盘高度为0时，弹回原位置
    private void setChildViewPosition(int keyboardHeight, boolean visiable) {
        int[] location = new int[2];
        int height = 0;
        if (view != null) {
            view.getLocationOnScreen(location);
            height = location[1];
        } else {
            height = 0;
        }

        int marginBottom = mScreenHeight - trueViewY - view.getHeight();
        if (marginBottom > keyboardHeight && keyboardHeight > 0) {
            return;
        }

        if (height > keyboardHeight && visiable) {

            distance = height - (mScreenHeight - keyboardHeight) + view.getHeight();
            scrollTo(0, distance);
        } else if (!visiable) {//返回原位置
            scrollTo(0, 0);
            distance = 0;
        }
    }
}
