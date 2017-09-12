package com.example.dahai.contentproviderdemo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

import cat.ereza.customactivityoncrash.CustomActivityOnCrash;

/**
 * 描述：
 * <p>
 * 作者： 向金海
 * 时间： 2017/9/11 14:34
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        CustomActivityOnCrash.install(this);

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
