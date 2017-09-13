package com.example.dahai.photopicklib.util;

import android.content.Context;

/**
 * 描述：
 * <p>
 * 作者： 向金海
 * 时间： 2017/9/13 9:36
 */

public class ResourceLoadUtil {

    public static String getResourceString(Context context,int resId) {
        if (context==null) return "";
        return context.getResources().getString(resId);
    }
}
