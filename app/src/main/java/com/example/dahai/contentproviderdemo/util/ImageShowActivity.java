package com.example.dahai.contentproviderdemo.util;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dahai.contentproviderdemo.R;

import java.io.File;
import java.util.ArrayList;

/**
 * 描述：
 * <p>
 * 作者： 向金海
 * 时间： 2017/9/8 17:51
 */

public class ImageShowActivity extends AppCompatActivity {

    private TextView mSure,mPreView;
    private String TAG="HHH";
    private ImageShowAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagepick);

        FrameLayout mToolbar = (FrameLayout) findViewById(R.id.mToolbar);
        mToolbar.setVisibility(View.VISIBLE);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        mSure = (TextView) findViewById(R.id.mSure);
        mPreView = (TextView) findViewById(R.id.mPreView);
        TextView mToolbarTitle = (TextView) findViewById(R.id.mToolbarTitle);
        TextView mToolbarCancel = (TextView) findViewById(R.id.mToolbarCancel);
        TextView mToolbarNav = (TextView) findViewById(R.id.mToolbarNav);

        mToolbarCancel.setText("取消");
        mToolbarNav.setVisibility(View.VISIBLE);
        mToolbarNav.setText("相册");
        mToolbarCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageSelectUtil.getInstance().clearSelect();
                setResult(120);
                finish();
            }
        });
        mToolbarNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        ArrayList<String> list = extras.getStringArrayList("image");

        if (list==null || list.size()==0) {
            return;
        }
        mToolbarTitle.setText(new File(list.get(0)).getParent().substring(new File(list.get(0)).getParent().lastIndexOf("/")+1));

        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3, LinearLayoutManager.VERTICAL,false));
        adapter = new ImageShowAdapter(list, this);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnSelectImageHandler(new ImageShowAdapter.OnSelectImageHandler() {
            @Override
            public void onHandler(View view, boolean b) {
                int selectNum = ImageSelectUtil.getInstance().getSelectNum();
                if (selectNum==0) {
                    mSure.setText("确定");
                    mSure.setBackground(ContextCompat.getDrawable(ImageShowActivity.this,R.drawable.sure_select_no));
                    mSure.setTextColor(ContextCompat.getColor(ImageShowActivity.this,R.color.preViewColor_no));
                    mPreView.setTextColor(ContextCompat.getColor(ImageShowActivity.this,R.color.preViewColor_no));
                } else {
                    mSure.setText("确定"+"("+String.valueOf(selectNum)+")");
                    mSure.setBackground(ContextCompat.getDrawable(ImageShowActivity.this,R.drawable.sure_select));
                    mSure.setTextColor(Color.WHITE);
                    mPreView.setTextColor(ContextCompat.getColor(ImageShowActivity.this,R.color.preViewColor));
                    if (b) {
                        TextView textView = new TextView(ImageShowActivity.this);
                        ViewGroup decorView = (ViewGroup) getWindow().getDecorView();
                        LinearLayout animLayout = new LinearLayout(ImageShowActivity.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        animLayout.setLayoutParams(lp);
                        animLayout.setBackgroundResource(android.R.color.transparent);
                        decorView.addView(animLayout);
                        textView.setTextColor(Color.WHITE);
                        textView.setText(String.valueOf(selectNum));
                        textView.setBackground(ContextCompat.getDrawable(ImageShowActivity.this,R.drawable.image_select));
                        textView.setGravity(Gravity.CENTER);
                        animLayout.addView(textView);

                        int[] locs=new int[2];
                        mSure.getLocationInWindow(locs);
                        int[] loc2=new int[2];
                        view.getLocationInWindow(loc2);

                        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                        textParams.leftMargin = loc2[0];
                        textParams.topMargin = loc2[1];
                        textView.setLayoutParams(textParams);


                        Animation topTranslateAnimation=new TranslateAnimation(0, (locs[0]-loc2[0])/3, 0, (locs[1]-loc2[1])/3);
                        topTranslateAnimation.setDuration(1000);
                        topTranslateAnimation.setInterpolator(ImageShowActivity.this, android.R.anim.linear_interpolator);
                        topTranslateAnimation.setFillAfter(false);

                        Animation alphaAnimation = new AlphaAnimation(1,0);
                        alphaAnimation.setDuration(1000);

                        AnimationSet animationSet = new AnimationSet(false);
                        animationSet.addAnimation(topTranslateAnimation);
                        animationSet.addAnimation(alphaAnimation);
                        animationSet.setFillAfter(true);
                        textView.startAnimation(animationSet);
                    }
                }
            }
        });
        mPreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ImageSelectUtil.getInstance().getSelectNum()==0) {
                    return;
                }
                Intent intent = new Intent(ImageShowActivity.this,PreviewImageActivity.class);

                startActivityForResult(intent,120);
            }
        });
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImageSelectUtil.getInstance().getSelectNum()==0) {
                    return;
                }
                setResult(200);
                finish();
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
        int selectNum = ImageSelectUtil.getInstance().getSelectNum();
        if (selectNum==0) {
            mSure.setText("确定");
            mSure.setBackground(ContextCompat.getDrawable(ImageShowActivity.this,R.drawable.sure_select_no));
            mSure.setTextColor(ContextCompat.getColor(ImageShowActivity.this,R.color.preViewColor_no));
            mPreView.setTextColor(ContextCompat.getColor(ImageShowActivity.this,R.color.preViewColor_no));
        } else {
            mSure.setText("确定"+"("+String.valueOf(selectNum)+")");
            mSure.setBackground(ContextCompat.getDrawable(ImageShowActivity.this,R.drawable.sure_select));
            mSure.setTextColor(Color.WHITE);
            mPreView.setTextColor(ContextCompat.getColor(ImageShowActivity.this,R.color.preViewColor));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==120 && resultCode==200) {
            setResult(200);
            finish();
            Log.e(TAG, "onActivityResult: " + "表格" + ImageSelectUtil.getInstance().getSelectNum());
        }
    }
}
