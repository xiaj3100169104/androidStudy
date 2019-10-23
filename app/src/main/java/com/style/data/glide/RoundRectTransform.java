package com.style.data.glide;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;

/**
 * 圆角矩形裁剪
 * Created by xiajun on 2017/12/23.
 */

public class RoundRectTransform extends BitmapTransformation {
    private float corner = 0f;
    private float border = 0f;
    private int borderColor;

    public RoundRectTransform(float corner) {
        this.corner = Resources.getSystem().getDisplayMetrics().density * corner;
    }

    public RoundRectTransform(float corner, float border, int borderColor) {
        this(corner);
        this.border = Resources.getSystem().getDisplayMetrics().density * border;
        this.borderColor = borderColor;
    }

    @Override
    protected Bitmap transform(@NotNull BitmapPool pool, @NotNull Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
        if (source == null)
            return null;
        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawRoundRect(rectF, corner, corner, paint);
        if (border > 0) {
            //绘制边框
            paint.reset();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);  //绘制空心
            paint.setColor(borderColor);
            paint.setStrokeWidth(border);
            //矩形区域需要减去borderWidth / 2边框才会显示完全
            RectF rectR = new RectF(border / 2, border / 2, source.getWidth() - border / 2, source.getHeight() - border / 2);
            canvas.drawRoundRect(rectR, corner, corner, paint);
        }
        return result;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}