package com.example.dahai.photopicklib.util;

import com.example.dahai.photopicklib.bean.SelectBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 描述：
 * <p>
 * 作者： 向金海
 * 时间： 2017/9/11 10:12
 */

public class ImageSelectUtil {

    private static ImageSelectUtil instance;
    private List<SelectBean> list;

    private ImageSelectUtil() {
        list = new ArrayList<>();
    }

    public static ImageSelectUtil getInstance() {
        if (instance==null) {
            synchronized (ImageSelectUtil.class) {
                if (instance==null) {
                    instance = new ImageSelectUtil();
                }
            }
        }
        return instance;
    }

    public void addImage(SelectBean path) {
        if (list==null) {
            list = new ArrayList<>();
        }
        list.add(path);
    }

    public void removeImage(SelectBean bean) {
        list.remove(bean);
        // 升序排列
        Collections.sort(list);
        int i=0;
        for (SelectBean selectBean : list) {
            selectBean.setOrder(++i);
        }
    }

    public List<SelectBean> getSelectImage() {
        return list;
    }

    public int getSelectNum() {
        return list.size();
    }

    public void clearSelect() {
        list.clear();
    }
}
