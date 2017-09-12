package com.example.dahai.contentproviderdemo.util;

import android.graphics.Bitmap;

/**
 * 描述：
 * <p>
 * 作者： 向金海
 * 时间： 2017/9/11 16:13
 */

public interface OnLoadImageListener {
    void onFinish(Bitmap bitmap,String path);
    void onError();
}
