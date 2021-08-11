package example.activity;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.media.FaceDetector;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.camera2.utils.CameraUtils;
import com.camera2.utils.ColorConvertUtil;
import com.camera2.view.AutoFitTextureView;
import com.camera2.view.TestView;
import com.style.config.FileDirConfig;
import com.style.framework.R;
import com.style.utils.BitmapUtil;
import com.style.utils.FileUtil;
import com.style.utils.PictureUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PreviewActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();

    AutoFitTextureView previewView;//自适应相机预览view
    CameraManager cameraManager;//相机管理类
    CameraDevice cameraDevice;//相机设备类
    CameraCaptureSession cameraCaptureSession;//相机会话类
    String cameraId;//相机id
    List<Size> outputSizes;//相机输出尺寸

    Size previewSize;//预览尺寸
    ImageReader previewReader;
    private HandlerThread mCameraWorkThread;
    private Handler mCameraThreadHandler;
    private boolean isSurfaceTextureAvailable;
    private TestView testView;
    private ImageView takeView;
    private Button mViewSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera2_preview);
        previewView = findViewById(R.id.afttv_camera);
        testView = findViewById(R.id.test);
        takeView = findViewById(R.id.view_take_picture);
        mViewSwitch = findViewById(R.id.view_switch);
        mViewSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsShutter = true;
            }
        });
        takeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIsShutter = true;
            }
        });
        initCamera();
    }

    //很多过程都变成了异步，所以这里需要一个子线程的looper
    private void initCamera() {
        CameraUtils.init(this);
        cameraManager = CameraUtils.getInstance().getCameraManager();
        cameraId = CameraUtils.getInstance().getCameraId(false);//默认使用后置相机
        //获取指定相机的输出尺寸列表，降序排序
        outputSizes = CameraUtils.getInstance().getCameraOutputSizes(cameraId, SurfaceTexture.class);
        //计算预览尺寸
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Point p = new Point();
        windowManager.getDefaultDisplay().getSize(p);
        previewSize = CameraUtils.getInstance().getBestPreviewSize(outputSizes, 800, 800);
        //竖屏预览交换宽高
        previewView.setAspectRation(previewSize.getHeight(), previewSize.getWidth());
        testView.setAspectRation(previewSize.getHeight(), previewSize.getWidth());
        Log.e("previewView", previewSize.getWidth() + "x" + previewSize.getHeight());
        //设置 TextureView 的状态监听
        previewView.setSurfaceTextureListener(surfaceTextureListener);
    }

    /**
     * 拍照模式，选择最大输出尺寸
     */
    private void updateCameraPreviewWithImageMode() {
        previewSize = outputSizes.get(0);
        previewView.setAspectRation(previewSize.getWidth(), previewSize.getHeight());
        createPreviewSession();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isSurfaceTextureAvailable)
            openCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (fw != null)
                fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void releaseCamera() {
        stopBackgroundThread();
        CameraUtils.getInstance().releaseImageReader(previewReader);
        CameraUtils.getInstance().releaseCameraSession(cameraCaptureSession);
        CameraUtils.getInstance().releaseCameraDevice(cameraDevice);
    }

    private void stopBackgroundThread() {
        Log.v(TAG, "stopBackgroundThread");
        try {
            //优先取消回调，再退出线程
            if (mCameraThreadHandler != null) {
                mCameraThreadHandler.removeCallbacksAndMessages(null);
                mCameraThreadHandler = null;
            }
            if (mCameraWorkThread != null) {
                mCameraWorkThread.quitSafely();
                mCameraWorkThread.join();
                mCameraWorkThread = null;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("MissingPermission")
    private void openCamera() {
        try {
            mCameraWorkThread = new HandlerThread("CAMERA2");
            mCameraWorkThread.start();
            mCameraThreadHandler = new Handler(mCameraWorkThread.getLooper());

            //打开相机
            cameraManager.openCamera(cameraId, new CameraDevice.StateCallback() {
                        @Override
                        public void onOpened(CameraDevice camera) {
                            if (camera == null) {
                                return;
                            }
                            cameraDevice = camera;
                            //创建相机预览 session
                            createPreviewSession();
                        }

                        @Override
                        public void onDisconnected(CameraDevice camera) {
                            //释放相机资源
                            releaseCamera();
                        }

                        @Override
                        public void onError(CameraDevice camera, int error) {
                            //释放相机资源
                            releaseCamera();
                        }
                    },
                    null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void createPreviewSession() {
        //根据TextureView 和 选定的 previewSize 创建用于显示预览数据的Surface;SurfaceTexture接收来自camera的图像流
        SurfaceTexture surfaceTexture = previewView.getSurfaceTexture();
        surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());//设置SurfaceTexture缓冲区大小
        final Surface previewSurface = new Surface(surfaceTexture);
        //获取 ImageReader 和 surface
        previewReader = ImageReader.newInstance(previewSize.getWidth(), previewSize.getHeight(), ImageFormat.YUV_420_888, 2);
        previewReader.setOnImageAvailableListener(
                new ImageReader.OnImageAvailableListener() {
                    @Override
                    public void onImageAvailable(ImageReader reader) {
                        Image image = reader.acquireLatestImage();
                        if (image != null) {
                            //yuvToRGB(image);
                            yuvToBitmap(image);
                            //一定需要close，否则不会收到新的Image回调。
                            image.close();
                        }
                    }
                },
                mCameraThreadHandler);
        final Surface readerSurface = previewReader.getSurface();

        try {
            //创建预览session
            cameraDevice.createCaptureSession(Arrays.asList(previewSurface, readerSurface), new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(CameraCaptureSession session) {

                            cameraCaptureSession = session;
                            try {
                                //构建预览模板捕获请求
                                CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                                // 设置连续自动对焦和自动曝光
                                builder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                builder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                                builder.addTarget(previewSurface);//设置 previewSurface 作为预览数据的显示界面
                                builder.addTarget(readerSurface);
                                builder.set(CaptureRequest.FLASH_MODE, CaptureRequest.FLASH_MODE_TORCH);
                                CaptureRequest captureRequest = builder.build();
                                //设置重复请求，以获取连续预览数据
                                session.setRepeatingRequest(captureRequest, new CameraCaptureSession.CaptureCallback() {
                                            @Override
                                            public void onCaptureProgressed(CameraCaptureSession session, CaptureRequest request, CaptureResult partialResult) {
                                                super.onCaptureProgressed(session, request, partialResult);
                                            }

                                            @Override
                                            public void onCaptureCompleted(CameraCaptureSession session, CaptureRequest request, TotalCaptureResult result) {
                                                super.onCaptureCompleted(session, request, result);
                                            }
                                        },
                                        mCameraThreadHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(CameraCaptureSession session) {

                        }
                    },
                    mCameraThreadHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() {

        //TextureView 可用时调用改回调方法
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            Log.d("Available", width + " x " + height);
            isSurfaceTextureAvailable = true;
            //TextureView 可用，打开相机
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
            Log.d("SizeChanged", width + " x " + height);

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            Log.d("Destroyed", " x ");
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    private byte[] mYuvBytes;
    private boolean mIsShutter;

    private void yuvToBitmap(Image image) {
        int width = image.getWidth();
        int height = image.getHeight();
        Log.e("image", width + "x" + height);
        if (mYuvBytes == null) {
            // YUV420 大小总是 width * height * 3 / 2
            mYuvBytes = new byte[width * height * 3 / 2];
        }
        // YUV_420_888
        Image.Plane[] planes = image.getPlanes();
        Log.e("image", "u PixelStride = " + planes[0].getPixelStride() + "  RowStride = " + planes[0].getRowStride());
        Log.e("image", "u PixelStride = " + planes[1].getPixelStride() + "  RowStride = " + planes[1].getRowStride());
        Log.e("image", "u PixelStride = " + planes[2].getPixelStride() + "  RowStride = " + planes[2].getRowStride());
        //某些预览尺寸行数据会比实际图片行数据多一些，可能是占位数据为了内存对齐提高效率
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - width;
        // Y通道，对应planes[0]
        // Y size = width * height
        // yBuffer.remaining() = width * height;
        // pixelStride = 1
        ByteBuffer yBuffer = planes[0].getBuffer();
        int yLen = width * height;
        int pos = 0;
        if (rowPadding == 0) {
            pos = yBuffer.remaining();
            yBuffer.get(mYuvBytes, 0, pos);
        } else {
            int yBufferPos = 0;
            for (int i = 0; i < height; i++) {
                yBuffer.position(yBufferPos);
                yBuffer.get(mYuvBytes, pos, width);
                yBufferPos += rowStride;
                pos += width;
            }
        }
        // U通道，对应planes[1]:uvuvuvuvuvuv
        // U size = width * height / 4;
        // uBuffer.remaining() = width * height / 2;
        // pixelStride = 2
        ByteBuffer uBuffer = planes[1].getBuffer();
        int pixelStride = planes[1].getPixelStride(); // pixelStride = 2
        int i = 0;
        int uRemaining = uBuffer.remaining();
        while (i < uRemaining) {
            mYuvBytes[pos++] = uBuffer.get(i);
            i += pixelStride;
            if (rowPadding == 0) continue;
            int rowLen = i % rowStride;
            if (rowLen >= width) {
                i += rowPadding;
            }
        }
        // V通道，对应planes[2]:vuvuvuvuvuvu，就是把U通道平面uv交换
        // V size = width * height / 4;
        // vBuffer.remaining() = width * height / 2;
        // pixelStride = 2
        ByteBuffer vBuffer = planes[2].getBuffer();
        pixelStride = planes[2].getPixelStride(); // pixelStride = 2
        i = 0;
        int vRemaining = vBuffer.remaining();
        while (i < vRemaining) {
            mYuvBytes[pos++] = vBuffer.get(i);
            i += pixelStride;
            if (rowPadding == 0) continue;
            int rowLen = i % rowStride;
            if (rowLen >= width) {
                i += rowPadding;
            }
        }

        if (mIsShutter) {
            mIsShutter = false;
            try {
                // save yuv data
                String yuvPath = FileDirConfig.DIR_CACHE + "/" + System.currentTimeMillis() + ".yuv";
                FileUtil.saveBytes(mYuvBytes, yuvPath);
                // save bitmap data
                String jpgPath = yuvPath.replace(".yuv", ".jpg");
                Bitmap bitmap = ColorConvertUtil.yuv420pToBitmap(mYuvBytes, width, height);
                //Bitmap bitmap = ColorConvertUtil.rgb8888toRGB565(b);
                BitmapUtil.saveBitmap(jpgPath, bitmap, 100);
                PictureUtil.setPictureDegree90(jpgPath);
                int degree = PictureUtil.readPictureDegree(jpgPath);
                Log.e("getTAG()", "拍照后的角度：" + degree);
                FaceDetector faceDet = new FaceDetector(bitmap.getWidth(), bitmap.getHeight(), 1);
                FaceDetector.Face[] faceList = new FaceDetector.Face[1];
                int n = faceDet.findFaces(bitmap, faceList);
                Log.e("人脸数", n + "");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    long startTime = 0;
    int count = 0;

    private void yuvToRGB(Image image) {
        if (startTime == 0) {
            startTime = System.currentTimeMillis();
            count++;
        } else {
            if (System.currentTimeMillis() - startTime >= 1000) {
                startTime = 0;
                count = 0;
            } else {
                count++;
            }
        }
        //目前测试到的回调帧率为30次每秒
        Log.e("frames", "yuv = " + count);
        Image.Plane yp = image.getPlanes()[0];//Y通道
        Image.Plane up = image.getPlanes()[1];//U通道
        Image.Plane vp = image.getPlanes()[2];//V通道

        ByteBuffer ybb = yp.getBuffer();
        byte[] dataY = new byte[ybb.remaining()];
        ybb.get(dataY);
        ByteBuffer ubb = up.getBuffer();
        byte[] dataU = new byte[ubb.remaining()];
        ubb.get(dataU);
        ByteBuffer vbb = vp.getBuffer();
        byte[] dataV = new byte[vbb.remaining()];
        vbb.get(dataV);
        //Log.d("onImageAvailable", "pla-size = " + image.getPlanes().length);
        Log.d("onImageAvailable", "img-size = " + image.getWidth() + " x " + image.getHeight() + " = " + image.getWidth() * image.getHeight());
        Log.d("onImageAvailable", "yuv-size = " + dataY.length + "  " + dataU.length + "  " + dataV.length);
        Log.d("onImageAvailable", "yuv-rows = " + yp.getRowStride() + "  " + up.getRowStride() + "  " + vp.getRowStride());
        Log.d("onImageAvailable", "yuv-pixs = " + yp.getPixelStride() + "  " + up.getPixelStride() + "  " + vp.getPixelStride());
        //取uv平面中心点的数据行列索引(4:2:2);//(uvDataIndex+1)*2 = yDataIndex+1
        int YdataXP = image.getWidth() / 2 - 1;
        int YdataYP = image.getHeight() / 2 - 1;
        int yDataIndex = (image.getWidth() * (YdataYP + 1 - 1) + YdataXP + 1) - 1;
        int uvDataIndex = yDataIndex / 4;
        Log.d("onImageAvailable", "index y = " + yDataIndex + "  uv = " + uvDataIndex);
        int y = dataY[yDataIndex];
        int v = dataV[uvDataIndex];
        int u = dataU[uvDataIndex];

        int r = (1000 * y + 1402 * v) / 1000;
        int g = (1000 * y - 344 * u - 714 * v) / 1000;
        int b = (1000 * y + 1772 * u) / 1000;
        r = r < 0 ? 0 : r;
        r = r > 255 ? 255 : r;
        g = g < 0 ? 0 : g;
        g = g > 255 ? 255 : g;
        b = b < 0 ? 0 : b;
        b = b > 255 ? 255 : b;
        Log.e("onImageAvailable", "yvu->rgb = (" + y + "," + v + "," + u + ")" + "   (" + r + "," + g + "," + b + ")");

        writeToFile(r + "," + g + "," + b + "\n");
    }

    FileWriter fw;//SD卡中的路径

    {
        try {
            fw = new FileWriter(Environment.getExternalStorageDirectory() + "/aaaa" + "/rgb.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(String str) {
        try {
            fw.write(str);
            fw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
