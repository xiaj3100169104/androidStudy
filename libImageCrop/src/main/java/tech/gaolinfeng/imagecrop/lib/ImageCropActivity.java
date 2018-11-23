package tech.gaolinfeng.imagecrop.lib;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.style.lib.imagecrop.R;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;

/**
 * Created by gaolf on 15/12/21.
 */
public class ImageCropActivity extends Activity {
    private static final String TAG = "ImageCropActivity";
    private static final String REQUEST_IMAGE_PATH = "REQUEST_IMAGE_PATH";
    private static final String REQUEST_TARGET_PATH = "REQUEST_TARGET_PATH";
    private static final String REQUEST_CROP_RECT = "REQUEST_CROP_RECT";
    private static final String REQUEST_CROP_IN_CIRCLE = "REQUEST_CROP_IN_CIRCLE";
    private static final String REQUEST_CROP_MAX_SIZE = "REQUEST_CROP_MAX_SIZE";
    private static final int CROP_DEFAULT_MAX_SIZE = 100;

    /**
     * @param from
     * @param imageFilePath
     * @param targetPath
     * @param cropRect
     * @param cropInCircle
     * @param maxSize       裁剪后图片最大大小 默认100kb，单位kb
     * @return
     */
    public static Intent createIntent(Activity from, String imageFilePath, String targetPath, String cropRect, boolean cropInCircle, int maxSize) {
        Intent intent = new Intent(from, ImageCropActivity.class);
        intent.putExtra(REQUEST_IMAGE_PATH, imageFilePath);
        intent.putExtra(REQUEST_TARGET_PATH, targetPath);
        intent.putExtra(REQUEST_CROP_RECT, cropRect);
        intent.putExtra(REQUEST_CROP_IN_CIRCLE, cropInCircle);
        intent.putExtra(REQUEST_CROP_MAX_SIZE, maxSize);
        return intent;
    }

    private String inPath, outPath;
    private Rect cropRect;
    private boolean isCircle;
    private int maxSize;

    private CropImageView cropImageView;
    private View loadingView;
    private int inSampleSize;                       // 读取时，如果图片过大，会被缩小后读取，因此裁剪时需要补上该值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_crop);
        loadingView = findViewById(R.id.crop_image_progress_bar);
        cropImageView = findViewById(R.id.crop_image);
        findViewById(R.id.title_option).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingView.setVisibility(View.VISIBLE);
                RectF imgRect = cropImageView.getCurrentRect();
                CropTask task = new CropTask(ImageCropActivity.this, inPath, outPath, isCircle, maxSize, cropRect, imgRect,
                        (int) cropImageView.getRawWidth(), (int) cropImageView.getRawHeight(), inSampleSize);
                task.execute();
            }
        });
        findViewById(R.id.iv_base_toolbar_Return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        if (getIntent() != null) {
            inPath = getIntent().getStringExtra(REQUEST_IMAGE_PATH);
            outPath = getIntent().getStringExtra(REQUEST_TARGET_PATH);
            isCircle = getIntent().getBooleanExtra(REQUEST_CROP_IN_CIRCLE, true);
            maxSize = getIntent().getIntExtra(REQUEST_CROP_MAX_SIZE, CROP_DEFAULT_MAX_SIZE);

            String cropRectStr = getIntent().getStringExtra(REQUEST_CROP_RECT);
            if (cropRectStr.matches("\\s*\\d+\\s*,\\s*\\d+\\s*,\\s*\\d+\\s*,\\s*\\d+\\s*")) {
                String[] cropRectStrArray = cropRectStr.split(",");
                int[] rectData = new int[4];
                for (int i = 0; i < rectData.length; i++) {
                    rectData[i] = Integer.parseInt(cropRectStrArray[i].trim());
                }
                cropRect = new Rect(rectData[0], rectData[1], rectData[2], rectData[3]);
            } else {
                throw new RuntimeException(
                        "imageCropActivity only accepts cropRect with format [left, top, right, bottom]");
            }
        }
        cropImageView.setCropCircle(isCircle);
        // 根据手机屏幕大小找出合适的inSampleSize
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(inPath, options);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        int imgWidth = options.outWidth;
        int imgHeight = options.outHeight;
        int inSampleSizeWidth = 1, inSampleSizeHeight = 1;
        while (screenWidth * inSampleSizeWidth * 2 < imgWidth) {
            inSampleSizeWidth *= 2;
        }
        while (screenHeight * inSampleSizeHeight * 2 < imgHeight) {
            inSampleSizeHeight *= 2;
        }
        inSampleSize = Math.max(inSampleSizeWidth, inSampleSizeHeight);
        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap bitmap = BitmapFactory.decodeFile(inPath, options);
        cropImageView.setImageBitmap(bitmap);
        cropImageView.setEdge(cropRect);
        cropImageView.startCrop();


    }

    private static class CropTask extends AsyncTask<Void, Void, Integer> {
        private static final int SUCCESS = 0;
        private static final int FAIL_CROP = 1;
        private static final int FAIL_OUT_PATH = 2;

        private String inPath;
        private String outPath;
        private Rect cropRect;
        private RectF imgRect;
        private int rawImgWidth;
        private int rawImgHeight;
        private int rawInSampleSize;
        private boolean cropCircle;
        private int maxSize;
        private Activity context;

        public CropTask(Activity context, String inPath, String outPath, boolean cropCircle, int maxSize, Rect cropRect, RectF imgRect, int rawImgWidth, int rawImgHeight, int rawInSampleSize) {
            this.inPath = inPath;               // 要裁剪的原图路径
            this.outPath = outPath;             // 要输出的裁剪路径
            this.cropCircle = cropCircle;       // 是否裁剪成圆形
            this.maxSize = maxSize;             // 最大裁剪大小
            this.cropRect = cropRect;           // CropImageView指定的裁剪区域
            this.imgRect = imgRect;             // CropImageView中图片被移动、缩放后的区域
            this.rawImgWidth = rawImgWidth;     // CropImageView中图片原大小
            this.rawImgHeight = rawImgHeight;   // CropImageView中图片原大小
            this.rawInSampleSize = rawInSampleSize; // 图片文件如果过大，在解码时就会指定一个inSampleSize解出一个较小的bitmap，避免爆内存；在裁剪时，计算裁剪区域在原图中的区域也需要考虑这个inSampleSize
            this.context = context;
        }

        @Override
        protected Integer doInBackground(Void... params) {

            // 计算这个rect在原图中的区域
            Rect mappedCropRect = new Rect(
                    (int) ((cropRect.left - imgRect.left) * rawImgWidth * rawInSampleSize / imgRect.width()),
                    (int) ((cropRect.top - imgRect.top) * rawImgHeight * rawInSampleSize / imgRect.height()),
                    (int) ((cropRect.right - imgRect.left) * rawImgWidth * rawInSampleSize / imgRect.width()),
                    (int) ((cropRect.bottom - imgRect.top) * rawImgHeight * rawInSampleSize / imgRect.height())
            );

            // 1, 检查这个rect内的像素数
            int rawCropArea = mappedCropRect.width() * mappedCropRect.height();     // 原图中裁剪区域点数
            int cropArea = cropRect.width() * cropRect.height();                    // 实际需要的点数
            int inSampleSize = 1;
            while (cropArea * inSampleSize * 2 < rawCropArea) {
                inSampleSize *= 2;
            }

            Bitmap bmp;
            try {
                if (Build.VERSION.SDK_INT > 9) {
                    BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(inPath, true);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = inSampleSize;
                    bmp = decoder.decodeRegion(mappedCropRect, options);
                } else {
                    // sdk < 10的系统不支持BitmapRegionDecoder方法
                    // 把原图从系统中读取进来，进行内存裁剪
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = inSampleSize;
                    Bitmap originBitmap = BitmapFactory.decodeFile(inPath, options);
                    bmp = Bitmap.createBitmap(originBitmap,
                            mappedCropRect.left, mappedCropRect.top,
                            mappedCropRect.width(), mappedCropRect.height());
                    if (originBitmap != bmp) {
                        originBitmap.recycle();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return FAIL_CROP;
            }
            Bitmap b;
            if (cropCircle) {
                b = ImageUtil.toRoundBitmap(bmp);
                bmp.recycle();
            } else {
                b = bmp;
            }
            //Bitmap b2 = ImageUtil.compressImage(b, 100);
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(outPath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
                Log.e(TAG, "压缩前大小  " + baos.toByteArray().length / 1024);
                int quality = 95;//必须大于0小于100
                while (baos.toByteArray().length > 1024 * maxSize && quality >= 10) {  //循环判断如果压缩后图片是否大于2048kb,大于继续压缩
                    baos.reset();//重置baos即清空baos
                    //PNG为无损压缩相比JPEG大很多，JPEG为无损压缩,而且PNG格式压缩不了
                    b.compress(Bitmap.CompressFormat.JPEG, quality, baos);//这里压缩options%，把压缩后的数据存放到baos中
                    Log.e(TAG, "压缩后大小  ByteArray=" + baos.toByteArray().length / 1024 + "kb bytes= " + baos.size());
                    quality -= 5;
                }
                fos.write(baos.toByteArray());
                fos.flush();
                //ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
                //Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
            } catch (java.io.IOException e) {
                e.printStackTrace();
                return FAIL_OUT_PATH;
            } finally {
                IOUtil.closeQuietly(fos);
            }
            return SUCCESS;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            switch (integer) {
                case SUCCESS:
                    context.setResult(RESULT_OK);
                    context.finish();
                    break;
                case FAIL_CROP:
                    Toast.makeText(context, "图片裁剪失败", Toast.LENGTH_SHORT).show();
                    break;
                case FAIL_OUT_PATH:
                    Toast.makeText(context, "输出路径无效", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

}
