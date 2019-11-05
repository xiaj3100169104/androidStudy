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

    /**
     *BitmapShader：
     * 重复：就是横向、纵向不断重复这个bitmap
     * 镜像：横向不断翻转重复，纵向不断翻转重复；
     * 拉伸：这个和电脑屏保的模式应该有些不同，这个拉伸的是图片最后的那一个像素；横向的最后一个横行像素，不断的重复，纵项的那一列像素，不断的重复；
     * @param pool
     * @param source
     * @return
     */
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