package com.example.dahai.contentproviderdemo.util;

import android.support.annotation.NonNull;

/**
 * 描述：
 * <p>
 * 作者： 向金海
 * 时间： 2017/9/11 10:33
 */

public class SelectBean implements Comparable<SelectBean> {
    private int order;
    private String path;
    private String percentName;
    private int size;

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

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

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int compareTo(@NonNull SelectBean bean) {
        return this.getOrder()-bean.getOrder();
    }

    @Override
    public String toString() {
        return "SelectBean{" +
                "order=" + order +
                ", path='" + path + '\'' +
                ", percentName='" + percentName + '\'' +
                ", size=" + size +
                '}';
    }
}
