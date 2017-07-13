package com.style.lib.album;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.xiajun.libalbumselect.R;

public class ImageLoadManager {

    public static void loadNormalPicture(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url))
        Glide.with(context).load(url).placeholder(R.mipmap.empty_photo).error(R.mipmap.image_fail).into(imageView);
    }

}
