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
 * Created by xiajun on 2017/12/23.
 */

public class RectTopCornerTransform extends BitmapTransformation {
    private float corner = 0f;

    public RectTopCornerTransform(float corner) {
        this.corner = Resources.getSystem().getDisplayMetrics().density * corner;
    }

    @Override
    protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
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
        //RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
        canvas.drawPath(roundedTopRect(0, 0, source.getWidth(), source.getHeight(), (int) corner), paint);
        return result;
    }

    // 画圆角矩形
    private Path roundedTopRect(int x, int y, int width, int height, int radius) {
        Path ctx = new Path();
        ctx.moveTo(x, y + radius);
        ctx.lineTo(x, y + height);
        ctx.lineTo(x + width, y + height);
        ctx.lineTo(x + width, y + radius);
        //画右上角
        ctx.rQuadTo(x + width - radius / 2, y + radius / 2, x + width - radius, y);
        ctx.lineTo(x + radius, y);
        //画左上角
        ctx.rQuadTo(radius / 2, radius / 2, x, y + radius);
        ctx.close();
        return ctx;
    }

    @Override
    public void updateDiskCacheKey(MessageDigest messageDigest) {

    }
}