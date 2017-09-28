package top.smartsport.www.utils;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewHelper {

    private ViewHelper() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void setVisibility(View view, int visibility) {
        view.setVisibility(visibility);
    }

    public static void setVisibility(View view, boolean isShow) {
        view.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public static <T extends TextView> boolean safelySetText(T view, CharSequence text) {
        return safelySetText(view, text, false);
    }

    /**
     * 过滤空字符串进行文本设置
     *
     * @param view  控件
     * @param text  文字
     * @param isHtmlFormat 是否Html格式转换
     * @return 是否显示
     */
    public static <T extends TextView> boolean safelySetText(T view, CharSequence text, boolean isHtmlFormat) {
        if (!TextUtils.isEmpty(text)) {
            setVisibility(view, true);
            view.setText(isHtmlFormat ? Html.fromHtml(text.toString().trim()) : text.toString().trim());
            return true;
        } else {
            setVisibility(view, false);
            return false;
        }
    }

    public static <T extends TextView> boolean safelySetText(T view, View parent, CharSequence text) {
        return safelySetText(view, parent, text, false);
    }

    /**
     * 过滤空字符串进行文本设置
     *
     * @param view
     * @param parent
     * @param text
     * @param isHtmlFormat 是否Html格式转换
     * @return 是否显示
     */
    public static <T extends TextView> boolean safelySetText(T view, View parent, CharSequence text, boolean isHtmlFormat) {
        if (!TextUtils.isEmpty(text)) {
            setVisibility(parent, true);
            view.setText(isHtmlFormat ? Html.fromHtml(text.toString().trim()) : text.toString().trim());
            return true;
        } else {
            setVisibility(parent, false);
            return false;
        }
    }

    /**
     * 使用默认文案替代空字符串
     *
     * @param view
     * @param text
     * @param defVal
     */
    public static <T extends TextView> void safelySetText(T view, CharSequence text, CharSequence defVal) {
        safelySetText(view, text, defVal, false);
    }

    public static <T extends TextView> void safelySetText(T view, CharSequence text, CharSequence defVal, boolean isHtmlFormat) {
        view.setText(!TextUtils.isEmpty(text)
                ? isHtmlFormat ? Html.fromHtml(text.toString().trim()) : text.toString().trim()
                : isHtmlFormat ? Html.fromHtml(defVal.toString().trim()) : defVal.toString().trim());
    }

    /**
     * 带完善重构方法
     *
     * @param view         TargetView
     * @param parent       Target 父布局, 用于数据为空是整块不展示, 传 null 不处理
     * @param text         内容数据
     * @param defVal       默认数据
     * @param nullVisible  内容数据为空是否展示       (def: false)
     * @param isHtmlFormat 是否使用 Html 进行解析    (def: false)
     * @param <T>
     */
    public static <T extends TextView> void safelySetText(T view, @Nullable ViewGroup parent
            , @Nullable CharSequence text, @NonNull CharSequence defVal
            , boolean nullVisible, boolean isHtmlFormat) {
        boolean isEmpty = TextUtils.isEmpty(text);
        boolean isDefEmpty = TextUtils.isEmpty(defVal);
        // TODO
        if (isEmpty) {
            if (null == parent) {
                if (nullVisible) {
                    view.setText(isEmpty
                            ? isHtmlFormat ? Html.fromHtml(text.toString().trim()) : text.toString().trim()
                            : isHtmlFormat ? Html.fromHtml(defVal.toString().trim()) : defVal.toString().trim());
                    view.setVisibility(View.VISIBLE);
                } else {
                    view.setVisibility(View.GONE);
                }
            } else {

            }
        } else {
        }
    }

    public static  <T extends TextView> void safelySetTextColor(Activity activity, T view, int color){
        if (activity == null || activity.getResources() == null)return;
        if (view == null)return;
        view.setTextColor(activity.getResources().getColor(color));
    }

    public static <T extends ImageView> void saveSetImageDrawable(Context context, T view, int drawableId) {
        if (context != null && context.getResources() != null){
            view.setImageDrawable(context.getResources().getDrawable(drawableId));
        }
    }

}
