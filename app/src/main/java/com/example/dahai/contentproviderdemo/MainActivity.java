package com.example.dahai.contentproviderdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dahai.photopicklib.activity.ImagePickActivity;
import com.example.dahai.photopicklib.bean.SelectBean;
import com.example.dahai.photopicklib.util.ImageSelectUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<SelectBean> list;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.mRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        list = new ArrayList<>();
        adapter = new MyAdapter(this, list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        if (requestCode==120&&resultCode==200) {
            List<SelectBean> selectImage = ImageSelectUtil.getInstance().getSelectImage();
            list.addAll(selectImage);
            adapter.notifyDataSetChanged();
        }
    }

    public void open(View view) {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);

        }else{
            Intent intent = new Intent(this, ImagePickActivity.class);
            startActivityForResult(intent,120);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case 100: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, ImagePickActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(this, "你已禁止权限", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private Context context;
        private List<SelectBean> list;

        MyAdapter(Context context, List<SelectBean> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_main, parent, false);
            return new MyHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((MyHolder) holder).textView.setText(list.get(position).getPath());
            Glide.with(context).load(list.get(position).getPath()).into(((MyHolder) holder).imageView);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyHolder extends RecyclerView.ViewHolder{

            ImageView imageView;
            TextView textView;
            MyHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.mItem);
                textView = itemView.findViewById(R.id.mPath);
            }
        }
    }
}
