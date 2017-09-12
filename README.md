# PhotoPickApp
该项目是一款模仿qq的图片选择器，加入了动画效果，使用VIewPager浏览图片，初步处理了加载大图多图时内存占用过大。

![](https://github.com/BigSea001/PhotoPickApp/blob/master/images/GIF.gif)
<br/>
主要技术使用ContentResolver读取本地图片
```
Cursor mCursor = resolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
                    MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[] { "image/jpeg", "image/png" }, "-"+MediaStore.Images.Media.DATE_MODIFIED);
```
![](https://github.com/BigSea001/PhotoPickApp/blob/master/images/home.png)
