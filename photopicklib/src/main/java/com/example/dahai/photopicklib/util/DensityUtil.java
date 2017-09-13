package com.example.dahai.photopicklib.util;

import android.content.Context;

/**
 * 描述：
 * <p>
 * 作者： BigSea001
 * 时间： 2017/9/13 9:27
 */

public class DensityUtil {

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
