package com.conch.validation.view.camera;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Surface;
import android.view.WindowManager;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public enum CameraUtils {
    /**
     *
     */
    INS ;
    /**
     * Check if this device has a camera
     */
     public boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * A safe way to get an instance of the Camera object.
     * 采用后置摄像头，若不存在后置摄像头或后置摄像头被占用则返回null
     * <p>
     * On some devices, this method may
     * take a long time to complete.  It is best to call this method from a
     * worker thread (possibly using {@link android.os.AsyncTask}) to avoid
     * blocking the main application UI thread.
     */
    @Nullable
     public CameraWrapper getCameraInstance(int cameraPosition) {
        try {
//            Caution: Always check for exceptions when using Camera.open().
//                     Failing to check for exceptions if the camera is in use or does
//                     not exist will cause your application to be shut down by the system.
//            c = Camera.open(); // attempt to get a Camera instance

            int numberOfCameras = Camera.getNumberOfCameras();
            Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
            CameraWrapper cameraWrapper =null ;
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.getCameraInfo(i, cameraInfo);
                if (cameraInfo.facing ==cameraPosition) {
                    cameraWrapper = new CameraWrapper(Camera.open(i), i);
                    break ;
                }
            }
            //如果没有前置,就用后置
//            if (null == cameraWrapper){
//                cameraWrapper = new CameraWrapper(Camera.open(0), 0);
//            }
            return cameraWrapper;
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            new RuntimeException("开启相机失败", e).printStackTrace();
        }
        return null; // returns null if camera is unavailable
    }

    @Nullable
    public PreviewSize findBestPreviewSize(Context context, int cameraId, int viewW, int viewH, List<Camera.Size> supportedPreviewSizes) {
        if (supportedPreviewSizes == null || supportedPreviewSizes.isEmpty()) {
            return null;
        }
        Camera.CameraInfo cameraInfo = getCameraInfo(cameraId);
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int rotation = display.getRotation();
        int degrees = 90;
        //暂时先注释,其他设备要解开
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        Collections.sort(supportedPreviewSizes, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size o1, Camera.Size o2) {
                if (o1.width == o2.width) {
                    return o1.height - o2.height;
                }
                return o1.width - o2.width;
            }
        });

        viewW = Math.min(viewW, display.getWidth());
        viewH = Math.min(viewH, display.getHeight());

        Camera.Size size = null;
        for (Camera.Size previewSize : supportedPreviewSizes) {
            float preW, preH;
            if ((cameraInfo.orientation - degrees) % 180 != 0) {
                //宽高对换
                preW = previewSize.height;
                preH = previewSize.width;
            } else {
                //宽高不交换
                preW = previewSize.width;
                preH = previewSize.height;
            }
            if (preW >= viewW && preH >= viewH) {
                size = previewSize;
                break;
            }

        }
        if (size == null) {
            size = supportedPreviewSizes.get(supportedPreviewSizes.size() - 1);
        }
        float preW, preH;
        if ((cameraInfo.orientation - degrees) % 180 != 0) {
            //宽高对换
            preW = size.height;
            preH = size.width;
        } else {
            //宽高不交换
            preW = size.width;
            preH = size.height;
        }
        float preS = preH / preW;
        if (preS * viewW < viewH) {
            PreviewSize previewSize = new PreviewSize(viewH / preS, viewH, size.width, size.height);
            previewSize.setDegree(degrees);
            return previewSize;
        } else {
            PreviewSize previewSize = new PreviewSize(viewW, viewW * preS, size.width, size.height);
            previewSize.setDegree(degrees);
            return previewSize;
        }

    }


    private  Camera.CameraInfo getCameraInfo(int cameraId) {
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, cameraInfo);
        return cameraInfo;
    }

    /**
     * Starting from API level 14, this method can be called when preview is
     * active.
     * 设置预览角度
     * @param context
     * @param camera
     */
    public void   setCameraDisplayOrientation(Context context, Camera camera, int cameraId) {
        if (camera == null || context == null) {
            return;
        }
        Camera.CameraInfo info = getCameraInfo(cameraId);
        int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
//         camera.setDisplayOrientation(90);
    }

    public   int   getOrientation(Context context, Camera camera, int cameraId) {
        if (camera == null || context == null) {
            return 0;
        }
        Camera.CameraInfo info = getCameraInfo(cameraId);
        int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result ;
//        Starting from API level 14, this method can be called when preview is active.
    }
}
