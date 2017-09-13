package com.example.dahai.photopicklib.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.dahai.photopicklib.R;
import com.example.dahai.photopicklib.bean.SelectBean;
import com.example.dahai.photopicklib.util.AnimationUtil;
import com.example.dahai.photopicklib.util.ImageSelectUtil;
import com.example.dahai.photopicklib.util.LoadImageUtil;
import com.example.dahai.photopicklib.util.OnLoadImageListener;
import com.example.dahai.photopicklib.util.ResourceLoadUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：
 * <p>
 * 作者： BigSea001
 * 时间： 2017/9/11 11:19
 */

public class PreviewImageActivity extends AppCompatActivity {

    private List<SelectBean> list;
    private ArrayList<String> previewImage;
    private int index=0;
    private RelativeLayout mTop,mBottom;
    private TextView mBack,mShowNum,mSelect,mSend;
    private Handler handler = new Handler();
    private boolean isShow=true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        Bundle extras = getIntent().getExtras();
        if (extras!=null) {
            previewImage = extras.getStringArrayList("previewImage");
            index = extras.getInt("index");
        } else {
            list = ImageSelectUtil.getInstance().getSelectImage();
        }

        mTop = (RelativeLayout) findViewById(R.id.mTop);
        mBottom = (RelativeLayout) findViewById(R.id.mBottom);
        mBack = (TextView) findViewById(R.id.mBreak);
        mShowNum = (TextView) findViewById(R.id.mShowNum);
        mSelect = (TextView) findViewById(R.id.mSelect);
        mSend = (TextView) findViewById(R.id.mSend);

        ViewPager mViewPager = (ViewPager) findViewById(R.id.mViewPager);

        List<View> views = new ArrayList<>();
        if (list==null) {
            for (String ignored : previewImage) {
                View view = LayoutInflater.from(this).inflate(R.layout.item_image, null);
                views.add(view);
            }
        } else {
            for (SelectBean ignored : list) {
                View view = LayoutInflater.from(this).inflate(R.layout.item_image, null);
                views.add(view);
            }
        }

        mViewPager.setAdapter(new MyAdapter(views));

        mViewPager.setCurrentItem(index);
        mViewPager.setOffscreenPageLimit(0);
        final float scale = getResources().getDisplayMetrics().density;
        mViewPager.setPageMargin((int) (45 * scale + 0.5f));
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                mShowNum.setText((position+1)+"/"+(list==null?previewImage.size():list.size()));
                index = position;
                List<SelectBean> image = ImageSelectUtil.getInstance().getSelectImage();
                mSelect.setBackground(ContextCompat.getDrawable(PreviewImageActivity.this,R.drawable.image_select_no));
                mSelect.setText("");

                for (SelectBean bean : image) {
                    if (TextUtils.equals(bean.getPath(),list==null?previewImage.get(position):list.get(position).getPath())) {
                        mSelect.setBackground(ContextCompat.getDrawable(PreviewImageActivity.this,R.drawable.image_select));
                        mSelect.setText(String.valueOf(bean.getOrder()));
                        break;
                    }
                }
            }
        });
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<SelectBean> image = ImageSelectUtil.getInstance().getSelectImage();
                boolean isSelect=false;
                for (SelectBean bean : image) {
                    if (TextUtils.equals(bean.getPath(),list==null?previewImage.get(index):list.get(index).getPath())) {
                        mSelect.setBackground(ContextCompat.getDrawable(PreviewImageActivity.this,R.drawable.image_select_no));
                        mSelect.setText("");
                        ImageSelectUtil.getInstance().removeImage(bean);
                        isSelect = true;
                        break;
                    }
                }
                if (!isSelect) {
                    String path = list==null?previewImage.get(index):list.get(index).getPath();
                    SelectBean bean = new SelectBean();
                    bean.setOrder(image.size()+1);
                    bean.setPath(path);
                    ImageSelectUtil.getInstance().addImage(bean);

                    mSelect.setBackground(ContextCompat.getDrawable(PreviewImageActivity.this,R.drawable.image_select));
                    mSelect.setText(String.valueOf(bean.getOrder()));
                }

                mSend.setText(ResourceLoadUtil.getResourceString(PreviewImageActivity.this,R.string.send)+(ImageSelectUtil.getInstance().getSelectNum()==0?"":("("+ImageSelectUtil.getInstance().getSelectNum()+")")));
            }
        });
        List<SelectBean> image = ImageSelectUtil.getInstance().getSelectImage();
        for (SelectBean bean : image) {
            if (TextUtils.equals(bean.getPath(),list==null?previewImage.get(index):list.get(index).getPath())) {
                mSelect.setBackground(ContextCompat.getDrawable(PreviewImageActivity.this,R.drawable.image_select));
                mSelect.setText(String.valueOf(bean.getOrder()));
                break;
            }
        }
        mSend.setText(ResourceLoadUtil.getResourceString(PreviewImageActivity.this,R.string.send)+(ImageSelectUtil.getInstance().getSelectNum()==0?"":("("+ImageSelectUtil.getInstance().getSelectNum()+")")));

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ImageSelectUtil.getInstance().getSelectNum()==0) {
                    SelectBean bean = new SelectBean();
                    String path = list==null?previewImage.get(index):list.get(index).getPath();
                    bean.setOrder(1);
                    bean.setPath(path);
                    ImageSelectUtil.getInstance().addImage(bean);
                }
                setResult(200);
                finish();
            }
        });
        mShowNum.setText((index+1)+"/"+(list==null?previewImage.size():list.size()));
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isShow) {
                    isShow = false;
                    if (mTop==null) return;
                    AnimationUtil.hideLine(PreviewImageActivity.this,mTop,mBottom);
                }
            }
        },1500);

    }

    private class MyAdapter extends PagerAdapter {
        private List<View> mListViews;

        MyAdapter(List<View> mListViews) {
            this.mListViews = mListViews;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = mListViews.get(position);
            ImageView iv = view.findViewById(R.id.mItemImage);
            Drawable ivDrawable = iv.getDrawable();
            if (ivDrawable instanceof BitmapDrawable) {
                BitmapDrawable drawable=(BitmapDrawable) ivDrawable;
                drawable.getBitmap().recycle();
            }
            iv.setImageBitmap(null);
            iv.setImageDrawable(null);
            recycleBitmap(iv);
            container.removeView(view);//删除页卡
        }


        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View child = mListViews.get(position);
            final ImageView imageView = child.findViewById(R.id.mItemImage);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changeShow();
                }
            });
            final String path;
            if (list!=null) {
                path = list.get(position).getPath();
            } else {
                path = previewImage.get(position);
            }

            LoadImageUtil.loadImage(path,new OnLoadImageListener() {
                @Override
                public void onFinish(Bitmap bitmap,String p) {
                    if (path.equals(p)) {
                        imageView.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onError() {

                }
            });

            container.addView(child); //添加页卡
            return child;
        }

        @Override
        public int getCount() {
            return  mListViews.size(); //返回页卡的数量
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0==arg1;//官方提示这样写
        }

        void recycleBitmap(ImageView imageView) {
            if (imageView == null) return;
            Drawable drawable = imageView.getDrawable();
            if (drawable != null && drawable instanceof BitmapDrawable) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                Bitmap bitmap = bitmapDrawable.getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bitmap=null;
                }
            }
        }

        private void changeShow() {
            if (isShow) {
                isShow = false;
                AnimationUtil.hideLine(PreviewImageActivity.this,mTop,mBottom);
            } else {
                isShow = true;
                AnimationUtil.showLine(PreviewImageActivity.this,mTop,mBottom);
            }
        }
    }

}
