package com.style.data.glide;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

/**
 * Created by xiajun on 2017/12/23.
 */

public class CornerRectTransform extends BitmapTransformation {
    private float roundWidth = 0f;
    private int roundColor;

    public CornerRectTransform(int roundWidth_dp, int roundColor) {
        this.roundWidth = (Resources.getSystem().getDisplayMetrics().density * roundWidth_dp);
        this.roundColor = roundColor;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null)
            return null;

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        if (result == null) {
            result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRect(rectF, paint);
        if (roundWidth > 0) {
            //绘制边框
            paint.reset();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);  //绘制空心
            paint.setColor(roundColor);
            paint.setStrokeWidth(roundWidth);
            RectF rectR = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRect(rectR, paint);
        }
        return result;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}