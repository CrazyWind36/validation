package com.conch.validation.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.conch.validation.base.App;

import java.io.File;

/**
 * 图片加载工具类
 */
public class PictureLoadUtil {
    static RequestOptions options = new RequestOptions()
            // 关闭内存缓存
            .skipMemoryCache(true)
            // 关闭磁盘缓存
            .diskCacheStrategy(DiskCacheStrategy.NONE);
    public static void  loadFilePathWithoutCache(String filePath, ImageView imageView) {
        Glide.with(App.getInstance()).load(new File(filePath)).apply(options).into(imageView);
    }
}
