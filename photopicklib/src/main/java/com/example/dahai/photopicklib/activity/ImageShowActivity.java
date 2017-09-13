package com.example.dahai.photopicklib.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.dahai.photopicklib.R;
import com.example.dahai.photopicklib.adapter.ImageShowAdapter;
import com.example.dahai.photopicklib.util.AnimationUtil;
import com.example.dahai.photopicklib.util.ImageSelectUtil;
import com.example.dahai.photopicklib.util.ResourceLoadUtil;

import java.io.File;
import java.util.ArrayList;

/**
 * 描述：
 * <p>
 * 作者： BigSea001
 * 时间： 2017/9/8 17:51
 */

public class ImageShowActivity extends AppCompatActivity {

    private TextView mSure,mPreView;
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

        mToolbarCancel.setText(ResourceLoadUtil.getResourceString(this,R.string.cancel));
        mToolbarNav.setVisibility(View.VISIBLE);
        mToolbarNav.setText(ResourceLoadUtil.getResourceString(this,R.string.photo));
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

        int selectNum = ImageSelectUtil.getInstance().getSelectNum();

        setTextState(null,false,selectNum);


        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3, LinearLayoutManager.VERTICAL,false));
        adapter = new ImageShowAdapter(list, this);
        mRecyclerView.setAdapter(adapter);

        adapter.setOnSelectImageHandler(new ImageShowAdapter.OnSelectImageHandler() {
            @Override
            public void onHandler(View view, boolean b) {
                int selectNum = ImageSelectUtil.getInstance().getSelectNum();
                setTextState(view, b, selectNum);
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

    private void setTextState(View view, boolean b, int selectNum) {
        if (selectNum==0) {
            mSure.setText(ResourceLoadUtil.getResourceString(ImageShowActivity.this, R.string.sure));
            mSure.setBackground(ContextCompat.getDrawable(ImageShowActivity.this,R.drawable.sure_select_no));
            mSure.setTextColor(ContextCompat.getColor(ImageShowActivity.this,R.color.preViewColor_no));
            mPreView.setTextColor(ContextCompat.getColor(ImageShowActivity.this,R.color.preViewColor_no));
        } else {
            mSure.setText(ResourceLoadUtil.getResourceString(ImageShowActivity.this,R.string.sure)+"("+String.valueOf(selectNum)+")");
            mSure.setBackground(ContextCompat.getDrawable(ImageShowActivity.this,R.drawable.sure_select));
            mSure.setTextColor(Color.WHITE);
            mPreView.setTextColor(ContextCompat.getColor(ImageShowActivity.this,R.color.preViewColor));
            if (b) {
                AnimationUtil.startAnimation(ImageShowActivity.this,selectNum,view,mSure);
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        adapter.notifyDataSetChanged();
        int selectNum = ImageSelectUtil.getInstance().getSelectNum();
        setTextState(null,false,selectNum);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==120 && resultCode==200) {
            setResult(200);
            finish();
        }
    }
}
