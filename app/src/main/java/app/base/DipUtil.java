package app.base;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
/***
 * dip px 互转
 * @author Administrator
 *
 */
public class DipUtil {
	
	public static float scale;
	public static Context context;
	public static float origScale;
	
	public static float getDensity(Context context) {
		if(scale==0){
			scale = context.getResources().getDisplayMetrics().density;//density值表示每英寸有多少个显示点
		}
		if(origScale==0){
			origScale = context.getResources().getDisplayMetrics().density;
		}
		return scale;
	}
	/** 
	    * 像素转换密度 
	    * */ 
	public static int px2dip(float pxValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(pxValue / scale + 0.5f); 
} 
	public static float getScale(Activity context) {
		return context.getResources().getDisplayMetrics().scaledDensity;
	}
	 /** 
	    * 密度转换像素 
	   * */  
	public static int fromDip(Context context, float dipValue) {
		return (int) (dipValue * getDensity(context) + 0.5f);
	}

	public static float getDensity() {
		return getDensity(context);
	}

	public static float getScale() {
		return context.getResources().getDisplayMetrics().scaledDensity;
	}
	
	public static int fromDip(float dipValue) {
		return (int) (dipValue * getDensity() + 0.5f);
	}

	
	public static void initCtx(Context context) {
		if (DipUtil.context == null) {
			DipUtil.context = context;
			getDensity();
		}
	}

	/**
	 * ��spֵת��Ϊpxֵ����֤���ִ�С����
	 * 
	 * @param spValue
	 * @param fontScale
	 *            ��DisplayMetrics��������scaledDensity��
	 * @return
	 */
	public static int sp2px(float spValue, float fontScale) {
		return (int) (spValue * fontScale + 0.5f);
	}

	/**
	 * ��pxֵת��Ϊspֵ����֤���ִ�С����
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            ��DisplayMetrics��������scaledDensity��
	 * @return
	 */
	public static int px2sp(float pxValue, float fontScale) {
		return (int) (pxValue / fontScale + 0.5f);
	}
}