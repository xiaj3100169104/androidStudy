package com.style.data.glide;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Priority;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.style.framework.R;
import com.style.glide.GlideApp;

public class ImageLoader {
    public static void loadNormalAvatar(Context context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            //Glide.with(context).load(url).error(R.mipmap.ic_launcher).into(imageView);
        }
    }

    public static void loadNormalPicture(Activity context, ImageView imageView, String url) {
        if (!TextUtils.isEmpty(url)) {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.mipmap.empty_photo)
                    .error(R.mipmap.image_fail)
                    .priority(Priority.HIGH);
            Glide.with(context).load(url).apply(options).into(imageView);
        }
    }

    public static void load(Fragment fragment, String url, ImageView iv) {
        if (!TextUtils.isEmpty(url)) {
            //Glide.with(fragment).load(url).into(imageView);
            GlideApp.with(fragment).load(url).into(iv);
        }
    }

    public static void load(Activity fragment, String url, ImageView iv) {
        if (!TextUtils.isEmpty(url)) {
            //Glide.with(fragment).load(url).into(imageView);
            GlideApp.with(fragment).load(url).into(iv);
        }
    }

    public void release(Context context) {
        //Glide.get(context).
        /*if (getActivity() != null && !getActivity().isDestroyed() && bd.iv != null) {
            Glide.with(context).clear(bd.iv);
        }*/
    }
}
