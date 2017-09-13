package com.example.dahai.photopicklib.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dahai.photopicklib.R;
import com.example.dahai.photopicklib.activity.PreviewImageActivity;

/**
 * 描述：
 * <p>
 * 作者： BigSea001
 * 时间： 2017/9/13 9:19
 */

public class AnimationUtil {

    public static void startAnimation(Activity activity, int selectNum, View startView, View endView) {
        TextView textView = new TextView(activity);
        ViewGroup decorView = (ViewGroup) activity.getWindow().getDecorView();
        LinearLayout animLayout = new LinearLayout(activity);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        animLayout.setLayoutParams(lp);
        animLayout.setBackgroundResource(android.R.color.transparent);
        decorView.addView(animLayout);
        textView.setTextColor(Color.WHITE);
        textView.setText(String.valueOf(selectNum));
        textView.setBackground(ContextCompat.getDrawable(activity, R.drawable.image_select));
        textView.setGravity(Gravity.CENTER);
        animLayout.addView(textView);

        int[] locs = new int[2];
        endView.getLocationInWindow(locs);
        int[] loc2 = new int[2];
        startView.getLocationInWindow(loc2);

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textParams.leftMargin = loc2[0];
        textParams.topMargin = loc2[1];
        textView.setLayoutParams(textParams);


        Animation topTranslateAnimation = new TranslateAnimation(0, (locs[0] - loc2[0]) / 3, 0, (locs[1] - loc2[1]) / 3);
        topTranslateAnimation.setDuration(1000);
        topTranslateAnimation.setInterpolator(activity, android.R.anim.linear_interpolator);
        topTranslateAnimation.setFillAfter(false);

        Animation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(1000);

        AnimationSet animationSet = new AnimationSet(false);
        animationSet.addAnimation(topTranslateAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setFillAfter(true);
        textView.startAnimation(animationSet);
    }

    public static void showLine(Context context, View top, View bottom) {
        Animation topTranslateAnimation=new TranslateAnimation(0, 0, -DensityUtil.dip2px(context, 56f), 0);
        topTranslateAnimation.setDuration(300);
        topTranslateAnimation.setInterpolator(context, android.R.anim.linear_interpolator);
        topTranslateAnimation.setFillAfter(true);
        top.startAnimation(topTranslateAnimation);

        Animation bottomTranslateAnimation=new TranslateAnimation(0, 0, DensityUtil.dip2px(context, 56f), 0);
        bottomTranslateAnimation.setDuration(300);
        bottomTranslateAnimation.setInterpolator(context, android.R.anim.linear_interpolator);
        bottomTranslateAnimation.setFillAfter(true);
        bottom.startAnimation(bottomTranslateAnimation);
        top.setVisibility(View.VISIBLE);
        bottom.setVisibility(View.VISIBLE);
    }

    public static void hideLine(Context context,View top,View bottom) {
        Animation topTranslateAnimation=new TranslateAnimation(0, 0, 0, -DensityUtil.dip2px(context,56f));
        topTranslateAnimation.setDuration(300);
        topTranslateAnimation.setInterpolator(context, android.R.anim.linear_interpolator);
        topTranslateAnimation.setFillAfter(true);
        top.startAnimation(topTranslateAnimation);

        Animation bottomTranslateAnimation=new TranslateAnimation(0, 0, 0, DensityUtil.dip2px(context,56f));
        bottomTranslateAnimation.setDuration(300);
        bottomTranslateAnimation.setInterpolator(context, android.R.anim.linear_interpolator);
        bottomTranslateAnimation.setFillAfter(true);
        bottom.startAnimation(bottomTranslateAnimation);
        top.setVisibility(View.GONE);
        bottom.setVisibility(View.GONE);
    }
}
