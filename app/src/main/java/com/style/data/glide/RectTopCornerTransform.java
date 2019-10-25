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
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
        return roundCrop(pool, toTransform, outWidth, outHeight);
    }

    private Bitmap roundCrop(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
        if (source == null)
            return null;
        //裁剪bitmap使其宽高比与imageview保持一致
        float scale;
        float dx = 0, dy = 0;
        //源bitmap宽高比大于imageview的宽高比,以imageview的宽高比裁剪bitmap宽度中心dx区域，反之裁剪高度中心dy区域。
        if ((float) source.getWidth() / source.getHeight() > (float) outWidth / outHeight) {
            scale = (float) outWidth / (float) outHeight;
            dx = source.getHeight() * scale;
            int x = (int) ((source.getWidth() - dx) / 2);
            int y = (source.getHeight() - size) / 2;
            //以矩形短的一边为边长截取bitmap的居中正方形区域
            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);
        } else if ((float) source.getWidth() / source.getHeight() < (float) outWidth / outHeight) {
            scale = (float) outHeight / (float) outWidth;
            dy = source.getWidth() * scale;
        } else {

        }

        Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
        paint.setAntiAlias(true);
        //RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());

        canvas.drawPath(drawTopRect(0, 0, source.getWidth(), source.getHeight(), (int) corner), paint);
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