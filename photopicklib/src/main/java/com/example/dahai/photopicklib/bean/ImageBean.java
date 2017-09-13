package com.example.dahai.photopicklib.bean;

import android.support.annotation.NonNull;

/**
 * 描述：
 * <p>
 * 作者： BigSea001
 * 时间： 2017/9/8 16:36
 */

public class ImageBean implements Comparable<ImageBean> {

    private String path;
    private String percentName;
    private int num;
    private boolean isSelect;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPercentName() {
        return percentName;
    }

    public void setPercentName(String percentName) {
        this.percentName = percentName;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public int compareTo(@NonNull ImageBean imageBean) {

        return this.percentName.compareToIgnoreCase(imageBean.getPercentName());
    }
}
