/*
 *@Project: GZJK
 *@Author: BMR
 *@Date: 2015年7月16日
 *@Copyright: 2000-2015 CMCC . All rights reserved.
 */
package com.ran.tools;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * @ClassName: DiaplayUtil
 * @Description: TODO
 * @author BMR
 * @date 2015年7月16日 下午10:04:41
 */
public class DisplayUtil
{
	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue)
	{
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取手机的屏幕尺寸
	 */
	public static int[] getScreenSize(Context context)
	{
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;
		return new int[]
		{ screenWidth, screenHeight };
	}
}
