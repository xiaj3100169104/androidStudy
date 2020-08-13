package com.style.data.glide;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.annotation.NonNull;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;

/**
 * 圆形裁剪
 * Created by xiajun on 2017/12/23.
 *
 */

public class RoundTransform extends BitmapTransformation {
    private static final int VERSION = 1;
    //这样只有更改VERSION才能刷新缓存数据，重新执行transform方法
    private static final String ID = "com.bumptech.glide.load.resource.bitmap.RoundTransform." + VERSION;
    private static final byte[] ID_BYTES = ID.getBytes(CHARSET);
    private float borderWidth = 0;
    private int borderColor;

    public RoundTransform() {
    }

    public RoundTransform(float borderWidth, int borderColor) {
        this.borderWidth = Resources.getSystem().getDisplayMetrics().density * borderWidth;
        this.borderColor = borderColor;
    }

    @Override
    protected Bitmap transform(@NotNull BitmapPool pool, @NotNull Bitmap toTransform, int outWidth, int outHeight) {
        return circleCrop(pool, toTransform);
    }

    private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
        if (source == null)
            return null;
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        //以矩形短的一边为边长截取bitmap的居中正方形区域
        Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

        Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        float r = size / 2f;
        canvas.drawCircle(r, r, r, paint);
        if (this.borderWidth > 0) {
            //绘制圆环
            paint.reset();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.STROKE);  //绘制空心
            paint.setColor(borderColor);
            paint.setStrokeWidth(borderWidth);
            //圆形边框半径需要减去borderWidth / 2边框才会显示完全
            canvas.drawCircle(r, r, r - borderWidth / 2, paint);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RoundTransform;
    }

    /*@Override
    public int hashCode() {
        return ID.hashCode();
    }*/

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        //messageDigest.update(ID_BYTES);
    }
}
