package com.style.data.glide;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import java.security.MessageDigest;

/**
 * Created by xiajun on 2017/12/23.
 * 更改VERSION可刷新缓存数据
 */

public class GlideCircleTransform extends BitmapTransformation {
    private static final int VERSION = 2;
    private static final String ID = "com.bumptech.glide.load.resource.bitmap.GlideCircleTransform." + VERSION;
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private float roundWidth = 0;
    private int roundColor;

    public GlideCircleTransform(int roundWidthPx, int roundColor) {
        this.roundWidth = roundWidthPx;
        this.roundColor = roundColor;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null) return null;

        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;

        //以矩形短的一边为边长截取bitmap的居中正方形区域
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        if (this.roundWidth > 0) {
            //绘制圆环
            paint.reset();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);  //绘制空心
            paint.setColor(roundColor);
            paint.setStrokeWidth(roundWidth);
            canvas.drawCircle(r, r, r - roundWidth / 2, paint);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GlideCircleTransform;
    }

    @Override
    public int hashCode() {
        return ID.hashCode();
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update(ID_BYTES);
    }
}
