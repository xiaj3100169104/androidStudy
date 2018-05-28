package com.style.data.glide;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by xiajun on 2017/12/23.
 */

public class GlideCircleTransform extends BitmapTransformation {
    private float roundWidth = 0;
    private int roundColor;

    public GlideCircleTransform(int roundWidth_dp, int roundColor) {
        this.roundWidth = (Resources.getSystem().getDisplayMetrics().density * roundWidth_dp);
        this.roundColor = roundColor;
    }

    public GlideCircleTransform() {

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
            canvas.drawCircle(r, r, r - 5, paint);
        }
        return result;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}
