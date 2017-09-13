package com.example.dahai.photopicklib.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dahai.photopicklib.R;
import com.example.dahai.photopicklib.activity.PreviewImageActivity;
import com.example.dahai.photopicklib.bean.SelectBean;
import com.example.dahai.photopicklib.util.ImageSelectUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * <p>
 * 作者： BigSea001
 * 时间： 2017/9/11 9:20
 */

public class ImageShowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> list;
    private Context context;

    public ImageShowAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_showimage, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MyViewHolder myViewHolder = (MyViewHolder) holder;
        final String s = list.get(position);
        if (s!=null) {
            Glide.with(context).load(s).into(myViewHolder.imageView);
            final List<SelectBean> selectImage = ImageSelectUtil.getInstance().getSelectImage();
            myViewHolder.mItemText.setBackground(ContextCompat.getDrawable(context, R.drawable.image_select_no));
            myViewHolder.mItemText.setText("");
            if (selectImage!=null) {
                for (SelectBean path : selectImage) {
                    if (TextUtils.equals(s,path.getPath())) {
                        myViewHolder.mItemText.setBackground(ContextCompat.getDrawable(context,R.drawable.image_select));
                        myViewHolder.mItemText.setText(String.valueOf(path.getOrder()));
                        break;
                    }
                }
            }
            myViewHolder.mItemText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean haveSelect=false;
                    if (selectImage!=null) {
                        for (SelectBean path : selectImage) {
                            if (TextUtils.equals(s,path.getPath())) {
                                myViewHolder.mItemText.setBackground(ContextCompat.getDrawable(context,R.drawable.image_select_no));
                                myViewHolder.mItemText.setText("");
                                ImageSelectUtil.getInstance().removeImage(path);
                                notifyDataSetChanged();
                                haveSelect = true;
                                break;
                            }
                        }
                        if (!haveSelect) {
                            if (ImageSelectUtil.getInstance().getSelectNum()>5) {
                                Toast.makeText(context, "只能选6张图片", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            SelectBean bean = new SelectBean();
                            bean.setOrder(ImageSelectUtil.getInstance().getSelectNum()+1);
                            bean.setPath(s);
                            ImageSelectUtil.getInstance().addImage(bean);
                            myViewHolder.mItemText.setBackground(ContextCompat.getDrawable(context,R.drawable.image_select));
                            myViewHolder.mItemText.setText(String.valueOf(bean.getOrder()));
                        }
                    }
                    if (onSelectImageHandler!=null) {
                        onSelectImageHandler.onHandler(view,!haveSelect);
                    }
                }
            });

            myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(context,PreviewImageActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("previewImage", (ArrayList<String>) list);
                    bundle.putInt("index",position);
                    intent.putExtras(bundle);

                    ((Activity) context).startActivityForResult(intent,120);
                }
            });
        }
    }

    private OnSelectImageHandler onSelectImageHandler;

    public void setOnSelectImageHandler(OnSelectImageHandler onSelectImageHandler) {
        this.onSelectImageHandler = onSelectImageHandler;
    }

    public interface OnSelectImageHandler {
        void onHandler(View view, boolean b);
    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView mItemText;

        MyViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.mItemImage);
            mItemText = itemView.findViewById(R.id.mItemText);
        }
    }
}
