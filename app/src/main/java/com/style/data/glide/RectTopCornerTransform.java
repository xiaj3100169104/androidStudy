package com.style.data.glide;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import org.jetbrains.annotations.NotNull;

import java.security.MessageDigest;

/**
 * 顶部以圆角矩形
 * 注意：这个只是裁剪的bitmap，最终效果会受imageview的scaleType影响.
 * 此模式目前只兼容centerCrop
 * Created by xiajun on 2017/12/23.
 */

public class RectTopCornerTransform extends BitmapTransformation {
    private float corner = 0f;

    public RectTopCornerTransform(float corner) {
        this.corner = Resources.getSystem().getDisplayMetrics().density * corner;
    }

    @Override
    protected Bitmap transform(@NotNull BitmapPool pool, @NotNull Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform, outWidth, outHeight);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
        if (source == null)
            return null;
        //裁剪bitmap使其宽高比与imageview保持一致
        float scale;
        int x = 0, y = 0;
        int dx = source.getWidth(), dy = source.getHeight();
        //源bitmap宽高比大于imageview的宽高比,以imageview的宽高比裁剪bitmap宽度中心dx区域，反之裁剪高度中心dy区域。
        if ((float) source.getWidth() / source.getHeight() > (float) outWidth / outHeight) {
            scale = (float) outWidth / (float) outHeight;
            dx = (int) (source.getHeight() * scale);
            x = (source.getWidth() - dx) / 2;
        } else if ((float) source.getWidth() / source.getHeight() < (float) outWidth / outHeight) {
            scale = (float) outHeight / (float) outWidth;
            dy = (int) (source.getWidth() * scale);
            y = (source.getHeight() - dy) / 2;
        }
        Bitmap sourceNew = Bitmap.createBitmap(source, x, y, dx, dy);

        Bitmap result = pool.get(sourceNew.getWidth(), sourceNew.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(sourceNew, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        //实际需要根据bitmap大小计算圆角大小或者把bitmap缩放到图片大小,这儿glide已经缩放到了最佳尺寸
        canvas.drawPath(drawTopRect(0, 0, sourceNew.getWidth(), sourceNew.getHeight(), (int) corner), paint);
        return result;
    }

    //画顶部圆角矩形
    private Path drawTopRect(int left, int top, int right, int bottom, int radius) {
        Path ctx = new Path();
        ctx.reset();
        ctx.moveTo(left, top + radius);
        //画左上角
        ctx.quadTo(left, top, left + radius, top);
        ctx.lineTo(right - radius, top);
        //画右上角
        ctx.quadTo(right, top, right, top + radius);
        ctx.lineTo(right, bottom);
        ctx.lineTo(left, bottom);
        ctx.lineTo(left, top + radius);
        ctx.close();
        return ctx;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}