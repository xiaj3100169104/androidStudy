package com.style.lib.album.loader;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestOptions;
import com.xiajun.libalbumselect.R;

public class ImageLoader {

    public static void normal(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.empty_photo)
                    .error(R.mipmap.image_fail)
                    .priority(Priority.HIGH);
            Glide.with(context).load(url).apply(options).into(imageView);
        }
    }

}
