package com.style.glide;

import android.content.Context;
import android.util.Log;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;

/**
 * Created by xiajun on 2018/1/7.
 */
@GlideModule
public class MyGlideModule extends AppGlideModule {
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }
   /* @Override
    public void registerComponents(Context context, Registry registry) {
        registry.append(Photo.class, InputStream.class, new FlickrModelLoader.Factory());
    }*/
    /**
     * MemorySizeCalculator类通过考虑设备给定的可用内存和屏幕大小想出合理的默认大小.
     * 通过LruResourceCache进行缓存。
     *
     * @param context
     * @param builder
     */
    /*@Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));
    }*/

    /**
     * 自定义缓存大小.
     *
     * @param context
     * @param builder
     */
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        Log.e("MyGlideModule", "applyOptions");
        // 默认内存和图片池大小
        /*MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize(); // 默认内存大小
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize(); // 默认图片池大小
        Log.e("defaultMemoryCacheSize", defaultMemoryCacheSize / 1024 / 1024 + "");
        Log.e("defaultBitmapPoolSize", defaultBitmapPoolSize / 1024 / 1024 + "");*/

        int memoryCacheSizeBytes = 1024 * 1024 * 20;
        int defaultBitmapPoolSize = 1024 * 1024 * 20;

        builder.setMemoryCache(new LruResourceCache(memoryCacheSizeBytes));
        builder.setBitmapPool(new LruBitmapPool(defaultBitmapPoolSize));
/*          //定义图片的本地磁盘缓存
        File cacheDir = context.getExternalCacheDir();//指定的是数据的缓存地址
        int diskCacheSize = 1024 * 1024 * 30;//最多可以缓存多少字节的数据
        //设置磁盘缓存大小
        builder.setDiskCache(new DiskLruCacheFactory(cacheDir.getPath(), "glide", diskCacheSize));
        // 定义缓存大小和位置
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, diskSize));  //内存中
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "cache", diskSize)); //sd卡中
        // 定义图片格式
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565); // 默认
        // 自定义内存和图片池大小
        builder.setMemoryCache(new LruResourceCache(memorySize));
        builder.setBitmapPool(new LruBitmapPool(memorySize));
        //builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);*/

    }

}
