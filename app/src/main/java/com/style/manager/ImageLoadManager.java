package com.style.manager;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.style.framework.R;

public class ImageLoadManager {
    public static void loadNormalAvatar(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            //Glide.with(context).load(url).error(R.mipmap.ic_launcher).into(imageView);
        }
    }

    public static void loadNormalPicture(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(com.xiajun.libalbumselect.R.mipmap.empty_photo)
                    .error(com.xiajun.libalbumselect.R.mipmap.image_fail)
                    .priority(Priority.HIGH);
            Glide.with(context).load(url).apply(options).into(imageView);
        }   }

    public static void loadBigPicture(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            //Glide.with(context).load(url).error(R.mipmap.image_fail).into(imageView);
        }
    }
}
