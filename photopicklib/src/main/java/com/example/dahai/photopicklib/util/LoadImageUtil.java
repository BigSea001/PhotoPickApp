package com.example.dahai.photopicklib.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;

/**
 * 描述：
 * <p>
 * 作者： BigSea001
 * 时间： 2017/9/11 16:13
 */

public class LoadImageUtil {

    public static void loadImage(final String path, final OnLoadImageListener listener) {
        new AsyncTask<String, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(String... strings) {
                String string = strings[0];

                return decodeSampledBitmapFromResource(string);
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);

                if (bitmap != null) {
                    listener.onFinish(bitmap, path);
                } else {
                    listener.onError();
                }
            }
        }.execute(path);
    }

    /**
     * 获取压缩后的图片
     *
     */
    private static Bitmap decodeSampledBitmapFromResource(String path) {

        if (path == null || path.equals("")) {
            // 文件不存在
            return null;
        }
        File file = new File(path);
        if (file.exists()
                && (file.getName().indexOf(".jpg") > 0
                || file.getName().indexOf(".JPG") > 0
                || file.getName().indexOf(".png") > 0
                || file.getName().indexOf(".PNG") > 0
                || file.getName().indexOf(".JPEG") > 0 || file
                .getName().indexOf(".jpeg") > 0)) {
            if (file.length() / 1024 < 200) {
                return BitmapFactory.decodeFile(path);
            }
        } else {
            return null;
        }

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        int inSampleSize;
        inSampleSize=outWidth/1080>outHeight/1920?outWidth/1080:outHeight/1920;  //取大的缩放比
        if (inSampleSize<1) {
            inSampleSize =1;
        }
        options.inSampleSize = inSampleSize;
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }
}
