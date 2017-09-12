package com.example.dahai.contentproviderdemo.util;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.dahai.contentproviderdemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * <p>
 * 作者： 向金海
 * 时间： 2017/9/11 11:19
 */

public class PreviewImageActivity2 extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private String TAG="HHH";
    private List<SelectBean> list;
    private ArrayList<String> previewImage;
    private int index=0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview2);

        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            previewImage = extras.getStringArrayList("previewImage");
            index = extras.getInt("index");
        } else {
            list = ImageSelectUtil.getInstance().getSelectImage();
        }


        mRecyclerView = (RecyclerView) findViewById(R.id.mPreviewRecyclerView);
        LinearLayoutManager layout = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layout);
        mRecyclerView.setAdapter(new MyAdapter());
        layout.scrollToPosition(index);
    }

    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(PreviewImageActivity2.this).inflate(R.layout.item_image, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            String path;
            if (list!=null) {
                path = list.get(position).getPath();
            } else {
                path = previewImage.get(position);
            }
            Glide.with(PreviewImageActivity2.this).load(path).into(((MyHolder) holder).imageView);
        }

        @Override
        public int getItemCount() {
            if (list!=null) {
                return list.size();
            } else {
                return previewImage.size();
            }
        }

        class MyHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            MyHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.mItemImage);
            }
        }
    }

}
