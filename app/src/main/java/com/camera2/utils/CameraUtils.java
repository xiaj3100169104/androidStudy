package com.camera2.utils;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.util.Log;
import android.util.Range;
import android.util.Size;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by wangyt on 2019/4/28
 */
public class CameraUtils {
    private static CameraUtils ourInstance = new CameraUtils();

    private static Context appContext;
    private static CameraManager cameraManager;

    public static void init(Context context) {
        if (appContext == null) {
            appContext = context.getApplicationContext();
            cameraManager = (CameraManager) appContext.getSystemService(Context.CAMERA_SERVICE);
        }
    }

    public static CameraUtils getInstance() {
        return ourInstance;
    }

    private CameraUtils() {

    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public String getFrontCameraId() {
        return getCameraId(true);
    }

    public String getBackCameraId() {
        return getCameraId(false);
    }

    /**
     * 获取相机id
     *
     * @param useFront 是否使用前置相机
     * @return
     */
    public String getCameraId(boolean useFront) {
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                int cameraFacing = characteristics.get(CameraCharacteristics.LENS_FACING);
                Range<Integer>[] fpsRanges;
                if (useFront) {
                    if (cameraFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                        // 该相机的FPS范围
                        fpsRanges = characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
                        Log.d("getCameraId", "FPS: " + Arrays.toString(fpsRanges));
                        return cameraId;
                    }
                } else {
                    if (cameraFacing == CameraCharacteristics.LENS_FACING_BACK) {
                        // 该相机的FPS范围
                        fpsRanges = characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
                        Log.d("getCameraId", "FPS: " + Arrays.toString(fpsRanges));
                        return cameraId;
                    }
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据输出类获取指定相机的输出尺寸列表，降序排序
     *
     * @param cameraId 相机id
     * @param clz      输出类
     * @return
     */
    public List<Size> getCameraOutputSizes(String cameraId, Class clz) {
        try {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap configs = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            List<Size> sizes = Arrays.asList(configs.getOutputSizes(clz));
            Collections.sort(sizes, new Comparator<Size>() {
                @Override
                public int compare(Size o1, Size o2) {
                    return o1.getWidth() * o1.getHeight() - o2.getWidth() * o2.getHeight();
                }
            });
            Collections.reverse(sizes);
            return sizes;
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据给定的尺寸计算最佳匹配分辨率,返回横屏状态下的宽高;
     * 如果是竖屏预览:输入为高宽，预览控件也需要设置宽高颠倒
     * diff最小的就是最佳预览分辨率
     *
     * @param expectW 期望宽度
     * @param expectH 期望高度
     * @return
     */
    public Size getBestPreviewSize(List<Size> availableSizes, int expectW, int expectH) {
        Log.v("PreviewSize", "expect = " + expectW + "x" + expectH);
        int bestPreviewWidth = expectW;
        int bestPreviewHeight = expectH;
        int diffs = Integer.MAX_VALUE;
        for (Size s : availableSizes) {
            int newDiffs = Math.abs(s.getWidth() - expectW) + Math.abs(s.getHeight() - expectH);
            Log.v("PreviewSize", " size = " + s + "  Diffs = " + newDiffs);

            if (newDiffs == 0) {
                bestPreviewWidth = s.getWidth();
                bestPreviewHeight = s.getHeight();
                break;
            }
            if (diffs > newDiffs) {
                bestPreviewWidth = s.getWidth();
                bestPreviewHeight = s.getHeight();
                diffs = newDiffs;
            }
        }
        Log.v("PreviewSize", "best = " + bestPreviewWidth + "x" + bestPreviewHeight);
        return new Size(bestPreviewWidth, bestPreviewHeight);
    }

    /**
     * 根据输出格式获取指定相机的输出尺寸列表
     *
     * @param cameraId 相机id
     * @param format   输出格式
     * @return
     */
    public List<Size> getCameraOutputSizes(String cameraId, int format) {
        try {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap configs = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            return Arrays.asList(configs.getOutputSizes(format));
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 释放相机资源
     *
     * @param cameraDevice
     */
    public void releaseCameraDevice(CameraDevice cameraDevice) {
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    /**
     * 关闭相机会话
     *
     * @param session
     */
    public void releaseCameraSession(CameraCaptureSession session) {
        if (session != null) {
            session.close();
            session = null;
        }
    }

    /**
     * 关闭 ImageReader
     *
     * @param reader
     */
    public void releaseImageReader(ImageReader reader) {
        if (reader != null) {
            reader.close();
            reader = null;
        }
    }

    public static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }
    }
}
