package com.example.dahai.photopicklib.activity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.dahai.photopicklib.R;
import com.example.dahai.photopicklib.adapter.ImageDirAdapter;
import com.example.dahai.photopicklib.bean.ImageBean;
import com.example.dahai.photopicklib.util.ImageSelectUtil;
import com.example.dahai.photopicklib.util.ResourceLoadUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述：
 * <p>
 * 作者： BigSea001
 * 时间： 2017/9/8 16:33
 */

public class ImagePickActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imagepick);

        recyclerView = ((RecyclerView) findViewById(R.id.mRecyclerView));
        FrameLayout mToolbar = (FrameLayout) findViewById(R.id.mToolbar);
        TextView mToolbarTitle = (TextView) findViewById(R.id.mToolbarTitle);
        TextView mToolbarCancel = (TextView) findViewById(R.id.mToolbarCancel);
        findViewById(R.id.mBottomLine).setVisibility(View.GONE);
        findViewById(R.id.mBottom).setVisibility(View.GONE);


        mToolbar.setVisibility(View.VISIBLE);

        mToolbarTitle.setText(ResourceLoadUtil.getResourceString(this,R.string.selectPhoto));
        mToolbarCancel.setText(ResourceLoadUtil.getResourceString(this,R.string.cancel));
        mToolbarCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageSelectUtil.getInstance().clearSelect();
                finish();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        dialog = new ProgressDialog(this);
        dialog.setTitle(ResourceLoadUtil.getResourceString(this,R.string.loading));

        //初始化选中的图片
        ImageSelectUtil.getInstance().clearSelect();

        new MyTask().execute();
    }

    private class MyTask extends AsyncTask<Void,Void,HashMap<String, List<String>>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.show();
        }

        @Override
        protected HashMap<String, List<String>> doInBackground(Void... voids) {

            ContentResolver resolver = ImagePickActivity.this.getContentResolver();
            HashMap<String, List<String>> mGroupMap = new HashMap<>();

            Cursor mCursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[] { "image/jpeg", "image/png" }, "-"+MediaStore.Images.Media.DATE_MODIFIED);

            if (mCursor==null) return null;

            while (mCursor.moveToNext()) {
                //获取图片的路径
                String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));
                File file = new File(path);
                String parentName = file.getParentFile().getName();
                if (!mGroupMap.containsKey(parentName)) {
                    List<String> childPath = new ArrayList<>();
                    childPath.add(path);
                    mGroupMap.put(parentName,childPath);
                } else {
                    mGroupMap.get(parentName).add(path);
                }
            }
            mCursor.close();

            return mGroupMap;
        }

        @Override
        protected void onPostExecute(HashMap<String, List<String>> mGroupMap) {
            super.onPostExecute(mGroupMap);

            if (dialog!=null) {
                dialog.dismiss();
            }

            List<ImageBean> list = new ArrayList<>();
            for (Map.Entry<String, List<String>> entry : mGroupMap.entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                if (value == null || value.size()==0) continue;
                ImageBean bean = new ImageBean();
                bean.setPath(value.get(0));
                bean.setPercentName(key);
                bean.setNum(value.size());
                list.add(bean);
            }
            Collections.sort(list);
            ImageDirAdapter adapter = new ImageDirAdapter(ImagePickActivity.this, list, mGroupMap);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==120&&resultCode==120) {
            ImageSelectUtil.getInstance().clearSelect();
            finish();
        }
        if (requestCode==120 && resultCode==200) {
            setResult(200);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        ImageSelectUtil.getInstance().clearSelect();
        super.onBackPressed();
    }
}
