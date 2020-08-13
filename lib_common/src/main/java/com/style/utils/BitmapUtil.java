package com.style.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.collection.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.nio.ByteBuffer;
import java.util.HashMap;

/**
 * ALPHA_8：只有一个alpha通道每个像素占用1byte内存
 * * ARGB_4444：每个像素占用2byte内存，已经被官方嫌弃
 * * ARGB_8888：每个像素ARGB四个通道，每个通道8bit，占用4byte内存
 * * （默认） RGB_565：每个像素占2Byte，其中红色占5bit，绿色占6bit，蓝色占5bit
 * * 深度与色深这两个概念。
 * 1、位深度指的是存储每个像素所用的位数，主要用于存储
 * 2、色深指的是每一个像素点用多少bit存储颜色，属于图片自身的一种属性
 * <p>
 * Android Bitmap中的Config参数其实指的就是色深
 * <p>
 * Bitmap.Config ARGB_4444：每个像素占四位，即A=4，R=4，G=4，B=4，那么一个像素点占4+4+4+4=16位
 * Bitmap.Config ARGB_8888：每个像素占四位，即A=8，R=8，G=8，B=8，那么一个像素点占8+8+8+8=32位
 * Bitmap.Config RGB_565：每个像素占四位，即R=5，G=6，B=5，没有透明度，那么一个像素点占5+6+5=16位
 * Bitmap.Config ALPHA_8：每个像素占四位，只有透明度，没有颜色
 * <p>
 * 举个例子：
 * 100像素*100像素 色深32位(ARGB_8888) 保存时位深度为24位 的图片
 * 在内存中所占大小为：100 * 100 * (32 / 8)Byte
 * 在文件中所占大小为 100 * 100 * ( 24/ 8 ) * 压缩效率 Byte
 * ————————————————
 * <p>
 * * dp/dip  : 与密度无关的象素，一种基于屏幕密度的抽象单位。在每英寸160点的显示器上，1dp = 1px。但dp和px的比例会随着屏幕密度的变化而改变，不同设备有不同的显示效果。
 * * 总结下：上面话就是想表达放在drawable的图片会对不适用真机屏幕密度的资源进行移除，启动图标放大可能会变模糊；放在mipmap依然会保留下各个密度的图片，
 * * 所以为了保证桌面图标的显示质量因此放在mipmap下面，其他的图标建议都放在drawable文件夹下面吧。
 * *
 * 内存是根据图片的像素数量来给图片分配内存大小的：
 * * 如果图片所在目录dpi低于匹配目录，那么该图片被认为是为低密度设备需要的，现在要显示在高密度设备上，图片会被放大。内存会成倍数增长（确实是这样）,而且效果也会模糊。
 * * 如果图片所在目录dpi高于匹配目录，那么该图片被认为是为高密度设备需要的，现在要显示在低密度设备上，图片会被缩小。
 * * 如果图片所在目录为匹配目录，则无论设备dpi为多少，保留原图片大小，不进行缩放。
 * 图片有以下存在形式：
 * 1.文件形式(即以二进制形式存在于硬盘上)
 * 2.流的形式(即以二进制形式存在于内存中)
 * 3.Bitmap形式
 * <p>
 * 这三种形式的区别: 
 * 文件形式和流的形式对图片体积大小并没有影响,也就是说,如果你手机SD卡上的如果是100K,那么通过流的形式读到内存中,也一定是占100K的内存,注意是流的形式,不是Bitmap的形式,
 * 当图片以Bitmap的形式存在时,其占用的内存会瞬间变大,
 * 我试过500K文件形式的图片加载到内存,以Bitmap形式存在时,占用内存将近10M,当然这个增大的倍数并不是固定的（原因在下面提到）。
 * <p>
 * 检测图片三种形式大小的方法:
 * 文件形式: file.length()
 * 流的形式: 讲图片文件读到内存输入流中,看它的byte数
 * Bitmap:    bitmap.getByteCount()
 */
public class BitmapUtil {
    public final static String TAG = "BitmapCache";
    private static HashMap<String, SoftReference<Bitmap>> imageCache = new HashMap<String, SoftReference<Bitmap>>();
    private static LruCache<String, Bitmap> mMemoryCache;
    public static int cacheSize;

    static void initConfig() {
        // 获取虚拟机可用内存（内存占用超过该值的时候，将报OOM异常导致程序崩溃）。最后除以1024是为了以kb为单位
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        Log.e(TAG, "maxMemory-----" + String.valueOf(maxMemory / 1024) + "Mib");
        // 使用可用内存的1/8来作为Memory Cache
        cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // 重写sizeOf()方法，使用Bitmap占用内存的kb数作为LruCache的size
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public static void getMemoryCacheSize(String path, Bitmap bmp) {
        if (!TextUtils.isEmpty(path) && bmp != null) {
            imageCache.put(path, new SoftReference<Bitmap>(bmp));
        }
    }

    public static void put(String path, Bitmap bmp) {
        if (!TextUtils.isEmpty(path) && bmp != null) {
            imageCache.put(path, new SoftReference<Bitmap>(bmp));
        }
    }

    public static void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public static void displayBmp(final Activity act, final ImageView imageView, final String path, final int reqWidth, final int reqHeight) {
        if (null != imageView) {
            if (imageCache.containsKey(path)) {
                SoftReference<Bitmap> reference = imageCache.get(path);
                Bitmap bmp = reference.get();
                if (bmp != null) {
                    imageView.setImageBitmap(bmp);
                    return;
                }
            }
            Bitmap cachebitmap = getBitmapFromMemCache(path);
            if (cachebitmap != null) {
                imageView.setImageBitmap(cachebitmap);
                return;
            }
            new Thread() {
                Bitmap thumb;

                public void run() {
                    try {
                        thumb = BitmapUtil.getThumbnail(path, reqHeight, reqHeight);
                    } catch (Exception e) {
                    }
                    if (null != thumb) {
                        act.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                imageView.setImageBitmap(thumb);
                                put(path, thumb);
                                addBitmapToMemoryCache(path, thumb);
                            }
                        });
                    }
                }
            }.start();
        }
    }

    public static Bitmap getThumbnail(String path, int vWidth, int vHeight) {
        try {
            FileInputStream in = new FileInputStream(new File(path));
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(in, null, options);
            in.close();
            Bitmap bitmap = null;
            in = new FileInputStream(new File(path));
            // 计算 inSampleSize 的值，解析器使用的inSampleSize都是2的指数倍，如果inSampleSize是其他值，则找一个离这个值最近的2的指数值。
            // 解码后的宽高页变成原来的1/inSampleSize
            options.inSampleSize = calculateInSampleSize(options, vWidth, vHeight);
            options.inPreferredConfig = null;    // 让解码器基于屏幕以最佳方式解码
            options.inJustDecodeBounds = false;  // 是否只读取边界
            options.inDither = false;            // 不进行图片抖动处理
            bitmap = BitmapFactory.decodeStream(in, null, options);
            return bitmap;
        } catch (IOException e) {
            return null;
        }
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 原始图片的宽高
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // 在保证解析出的bitmap宽高分别大于目标尺寸宽高的前提下，取可能的inSampleSize的最大值
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 根据显示图片比例裁剪源bitmap
     *
     * @param source
     * @param outWidth
     * @param outHeight
     * @return
     */
    public static Bitmap centerCrop(Bitmap source, int outWidth, int outHeight) {
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
        return sourceNew;
    }

    /**
     * 旋转图片一定角度
     *
     * @return Bitmap
     * @throws
     */
    public static Bitmap rotateImageView(Bitmap bitmap, int angle) {
        if (bitmap != null && angle != 0) {
            // 旋转图片 动作
            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            // 创建新的图片
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

    /**
     * @param path    --文件路径
     * @param bitmap
     * @param quality 图片质量：30 表示压缩70%; 100表示压缩率为0
     * @return void
     * @throws
     */
    public static void saveBitmap(String path, Bitmap bitmap, int quality) throws IOException {
        FileOutputStream out;
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        out = new FileOutputStream(f);//JPEG:以什么格式压缩
        if (bitmap.compress(Bitmap.CompressFormat.JPEG, quality, out)) {
            out.flush();
        }
        out.close();
        recycle(bitmap);
    }

    public static void saveByte(String path, byte[] bitmap) throws IOException {
        FileOutputStream out;
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        out = new FileOutputStream(f);
        if (bitmap.length > 0) {
            out.write(bitmap);
            out.flush();
        }
        out.close();
    }

    public static void recycle(Bitmap bitmap) {
        if (bitmap != null && bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public static byte[] bitmap2byte(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        return baos.toByteArray();
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    public static InputStream bitmapToInputStream(Bitmap bitmap) {
        int size = bitmap.getHeight() * bitmap.getRowBytes();
        ByteBuffer buffer = ByteBuffer.allocate(size);
        bitmap.copyPixelsToBuffer(buffer);
        return new ByteArrayInputStream(buffer.array());
    }

    /**
     * note：处理decode后的小图片压缩并直接存储、传输，因为大图片Bitmap已经oom了。
     * CompressFormat.PNG处理图片无效；如果outMimeType图片类型不是image/jpeg可以跳过压缩步骤
     * Bitmap.CompressFormat.JPEG压缩png图片会自动忽略quality属性，bytes.length减少。图片会失去透明度，透明处变黑.
     * 如果图片太大最好先缩小图片再质量压缩效果更好。
     * 参考：
     * 1.主流屏幕尺寸1080x1920   此尺寸图片以ARGB_8888解码时占内存为：1080*1920*4/1024=8100kb=8M；
     * 2.quality=60以上,如果quality到了60图片大小还是超过了maxSize（说明图片色彩比较丰富），最好不要再压缩了；
     * 3.如果原图片大小小于maxSize不管尺寸大小：不压缩且不缩放；
     * 4.如果原图片大小大于maxSize但尺寸小于1080x1920(说明图片色彩比较丰富)：只压缩不缩放(这种处理处理的图片占用空间最大)。
     * 实际测试:微信就是以1080p，quality=60压缩的，png也用jpg处理，
     *
     * @param path    图片路径
     * @param maxSize 单位kb
     * @return 压缩后的字节数据
     */
    public static byte[] compress(String path, int maxSize) {
        try {
            FileInputStream in = new FileInputStream(new File(path));
            int size = in.available();
            byte[] b = new byte[size];
            in.read(b);
            in.close();
            //小于maxSize直接返回（会跳过旋转：但小尺寸一般不是拍照的照片，不存在照片角度问题）
            if (size <= maxSize * 1024)
                return b;
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(b, 0, b.length, options);
            in = new FileInputStream(new File(path));
            // 计算 inSampleSize 的值，解析器使用的inSampleSize都是2的指数倍，如果inSampleSize是其他值，则找一个离这个值最近的2的指数值。
            // 解码后的宽高变成原来的1/inSampleSize
            options.inSampleSize = getCompressInSampleSize(options.outWidth, options.outHeight);
            options.inPreferredConfig = null;    // 让解码器基于屏幕以最佳方式解码
            options.inJustDecodeBounds = false;  // 是否只读取边界
            options.inDither = false;            // 不进行图片抖动处理
            Bitmap bm = BitmapFactory.decodeStream(in, null, options);
            int degree = PictureUtil.readPictureDegree(path);
            // 旋转图片
            if (degree != 0) {
                bm = BitmapUtil.rotateImageView(bm, degree);
            }
            //当尺寸大于1080且空间存储大小大于maxSize时才缩放到1080p
            final int width = bm.getWidth();
            final int height = bm.getHeight();
            if (width * height > 1080 * 1920 && size > maxSize * 1024) {
                Matrix m = new Matrix();
                if (width >= height) {
                    int newh = 1080;
                    float sx = (float) newh / height;
                    m.setScale(sx, sx);
                } else {
                    int neww = 1080;
                    float sy = (float) neww / width;
                    m.setScale(sy, sy);
                }
                bm = Bitmap.createBitmap(bm, 0, 0, width, height, m, true);
            }
            //质量压缩
            int quality = 100;//必须大于0
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);//把压缩后的数据存放到baos中
            Log.e("compress", "quality=" + quality + "  压缩后  " + baos.toByteArray().length / 1024 + " kb" + " size " + baos.size());
            //png同样处理：因为有些png图片其实并没有透明度像素
            //if ("image/jpeg".equals(options.outMimeType)) {
            quality -= 10;//必须大于0
            //大于max继续压缩到quality=60
            while (quality >= 60 && baos.toByteArray().length > 1024 * maxSize) {
                baos.reset();//重置baos即清空baos
                bm.compress(Bitmap.CompressFormat.JPEG, quality, baos);
                Log.e("compress", "quality=" + quality + "  压缩后  " + baos.toByteArray().length / 1024 + " kb" + " size " + baos.size());
                quality -= 10;
            }
            //}
            return baos.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 获取用于图片压缩的最佳inSampleSize值，以1080x1920的乘积为基数计算2的指数值：
     * 尺寸1080x1920以ARGB_8888解码时占内存为：1080*1920*4/1024=8100kb=8M
     * 目前小米最高摄像头像素为12032*9024是1080*1920的52倍接近2的6次方
     * 1080*1920=1440*1440=2072600
     *
     * @param outWidth  图片原始宽度
     * @param outHeight 图片原始高度
     * @return
     */
    private static int getCompressInSampleSize(int outWidth, int outHeight) {
        final int reqWidth = 1080;
        final int reqHeight = 1920;
        int inSampleSize = 1;
        float scale = (float) outWidth * outHeight / (reqWidth * reqHeight);
        //计算以2为底scale的对数
        double k = Math.log(scale) / Math.log(2);
        if (k > 8)
            inSampleSize = 8;
        else if (k > 4)
            inSampleSize = 4;
        else if (k > 2)
            inSampleSize = 2;
        Log.e("inSampleSize", "scale=" + scale + "  k=" + k + "  inSampleSize=" + inSampleSize);
        return inSampleSize;
    }

    /**
     * 将图片变为圆角
     *
     * @param bitmap 原Bitmap图片
     * @param corner 图片圆角的弧度(单位:像素(px))
     * @return 带有圆角的图片(Bitmap 类型)
     */
    public static Bitmap toRoundCorner(Bitmap bitmap, int corner) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect dst = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(src);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, corner, corner, paint);
        //SRC_IN:后画的
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 将图片转化为圆形头像
     *
     * @throws
     * @Title: toRoundBitmap
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;

            left = 0;
            top = 0;
            right = width;
            bottom = width;

            height = width;

            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;

            float clip = (width - height) / 2;

            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;

            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);// 设置画笔无锯齿

        canvas.drawARGB(0, 0, 0, 0); // 填充整个Canvas

        // 以下有两种方法画圆,drawRounRect和drawCircle
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 画圆角矩形，第一个参数为图形显示区域，第二个参数和第三个参数分别是水平圆角半径和垂直圆角半径。
        // canvas.drawCircle(roundPx, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置两张图片相交时的模式,参考http://trylovecatch.iteye.com/blog/1189452
        canvas.drawBitmap(bitmap, src, dst, paint); // 以Mode.SRC_IN模式合并bitmap和已经draw了的Circle
        return output;
    }
}
