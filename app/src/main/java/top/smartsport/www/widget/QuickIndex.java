package top.smartsport.www.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import top.smartsport.www.R;

/**
 * @describe:选择城市时的快速检索
 */
public class QuickIndex extends View {


    private Paint mPaint;
    private float mCellWidth;
    private float mCellHeight;
    private float mTextWidth;
    private float mTextHeight;
    private Rect mBounds;
    private static final String[] LETTERS = new String[] {"A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y",
            "Z" };
    private int mIndex = -1; // 默认不是任何一个字母的索引

    public interface OnLetterChangeListener {
        void onLetterChange(String letter);
    }

    private OnLetterChangeListener mOnLetterChangeListener;

    public OnLetterChangeListener getOnLetterChangeListener() {
        return mOnLetterChangeListener;
    }

    public void setOnLetterChangeListener(OnLetterChangeListener onLetterChangeListener) {
        mOnLetterChangeListener = onLetterChangeListener;
    }

    public QuickIndex(Context context) {
        this(context, null);
    }

    public QuickIndex(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public QuickIndex(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QuickIndex);
        float dimension = typedArray.getDimension(R.styleable.QuickIndex_drawSize, 18);
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#8C8C8C"));
        mPaint.setTextSize(dimension);
        mPaint.setTypeface(Typeface.DEFAULT_BOLD); // 粗体
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG); // 抗锯齿
        mBounds = new Rect();
    }

    private void init() {

    }

    // draw - onDraw
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // canvas.drawText("A", 10, 10, mPaint);
        for (int i = 0; i < LETTERS.length; i++) {
            mTextWidth = mPaint.measureText(LETTERS[i]); // 测量一段文本的宽度
            mPaint.getTextBounds(LETTERS[i], 0, LETTERS[i].length(), mBounds);
            mTextHeight = mBounds.height();
            float x = mCellWidth * 0.5f - mTextWidth * 0.5f;
            float y = mCellHeight * 0.5f + mTextHeight * 0.5f + i * mCellHeight;
            mPaint.setColor(mIndex == i ? Color.parseColor("#8C8C8C") :Color.parseColor("#8C8C8C")); // 设置画笔颜色
            canvas.drawText(LETTERS[i], x, y, mPaint);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        float y;
        int currentIndex;
        switch (event.getAction()) {
//        case MotionEvent.ACTION_DOWN:
//            y = event.getY();
//            currentIndex = (int) (y / mCellHeight);
//            // System.out.println(LETTERS[currentIndex]);
//            if (mOnLetterChangeListener != null && currentIndex != mIndex
//                    && currentIndex < LETTERS.length) {
//                mOnLetterChangeListener.onLetterChange(LETTERS[currentIndex]);
//                mIndex = currentIndex;
//            }
//            break;
//        case MotionEvent.ACTION_MOVE:
//            y = event.getY();
//            currentIndex = (int) (y / mCellHeight);
//            // System.out.println(LETTERS[currentIndex]);
//            if (mOnLetterChangeListener != null && currentIndex != mIndex
//                    && currentIndex < LETTERS.length) {
//                mOnLetterChangeListener.onLetterChange(LETTERS[currentIndex]);
//                mIndex = currentIndex;
//            }
//            break;

        case MotionEvent.ACTION_UP:
            mIndex = -1; // 恢复默认索引
            break;
        default:
            y = event.getY();
            currentIndex = (int) (y / mCellHeight);
            // System.out.println(LETTERS[currentIndex]);
            if (mOnLetterChangeListener != null && currentIndex != mIndex
                    && currentIndex < LETTERS.length) {
                mOnLetterChangeListener.onLetterChange(LETTERS[currentIndex]);
                mIndex = currentIndex;
            }
            break;
        }
        invalidate(); // 重绘
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCellWidth = getMeasuredWidth();
        mCellHeight = getMeasuredHeight() * 1.0f / LETTERS.length;

    }

}
