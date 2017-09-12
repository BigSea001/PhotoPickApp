package com.example.dahai.contentproviderdemo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.dahai.contentproviderdemo.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 描述：
 * <p>
 * 作者： 向金海
 * 时间： 2017/9/8 17:16
 */

public class ImageDirAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<ImageBean> list;
    private HashMap<String, List<String>> mGroupMap;

    public ImageDirAdapter(Context context, List<ImageBean> list, HashMap<String, List<String>> mGroupMap) {
        this.context = context;
        this.list = list;
        this.mGroupMap = mGroupMap;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_imagedir, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder myViewHolder = (MyViewHolder) holder;

        final ImageBean bean = list.get(position);
        if (bean!=null) {

            Glide.with(context).load(bean.getPath()).override(200,200).into(myViewHolder.mIcon);
            myViewHolder.mTitle.setText(bean.getPercentName());
            myViewHolder.mNum.setText("("+bean.getNum()+")");

            myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,ImageShowActivity.class);
                    Bundle bundle = new Bundle();
                    List<String> list = mGroupMap.get(bean.getPercentName());
                    bundle.putStringArrayList("image", (ArrayList<String>) list);
                    intent.putExtras(bundle);
                    ((Activity)context).startActivityForResult(intent,120);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mIcon;
        TextView mTitle;
        TextView mNum;
        ImageView mMore;

        MyViewHolder(View itemView) {
            super(itemView);

            mIcon = itemView.findViewById(R.id.mIcon);
            mTitle = itemView.findViewById(R.id.mTitle);
            mNum = itemView.findViewById(R.id.mNum);
            mMore = itemView.findViewById(R.id.mMore);
        }
    }
}
