package com.conch.validation.view.camera;

import android.content.Context;
import android.hardware.Camera;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import com.conch.validation.R;

import java.util.List;

public class PreviewCameraView extends FrameLayout {
    private SurfaceView mPreviewView;

    private Camera mCamera;

    private int mCameraId;
    private PreviewSize mPreviewSize;
    private PreviewCameraCallback mPreviewCallback;


    /**
     * 监听接口
     */
    private OnCameraStatusListener listener;

    public PreviewCameraView(@NonNull Context context) {
        this(context,null);
        init();
    }

    public PreviewCameraView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
        init();
    }

    public PreviewCameraView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_camera, this);
        mPreviewView = findViewById(R.id.preview);

        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        SurfaceHolder holder = mPreviewView.getHolder();
        holder.addCallback(new SurfacePreviewCallback());
        // deprecated setting, but required on Android versions prior to 3.0
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    private void initCamera() {
        //如果已经设置过相机,就不用再设置
        if (mCamera != null) {
            return;
        }

        if (CameraUtils.INS.checkCameraHardware(getContext())) {
//            CameraWrapper cameraWrapper = CameraUtils.INS.getCameraInstance(Camera.CameraInfo.CAMERA_FACING_FRONT);//陳風的
            CameraWrapper cameraWrapper = CameraUtils.INS.getCameraInstance(Camera.CameraInfo.CAMERA_FACING_FRONT);
            if (cameraWrapper == null) {
                return;
            }
            mCamera = cameraWrapper.camera;
            mCameraId = cameraWrapper.id;
            if (mCamera == null) {
                return;
            }
            CameraUtils.INS.setCameraDisplayOrientation(getContext(), mCamera, mCameraId);
        }
    }

    private void startPreview(SurfaceHolder holder) {
        if (holder.getSurface() == null || mPreviewSize == null) {
            // preview surface does not exist
            return;
        }
        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            e.printStackTrace();
            // ignore: tried to stop a non-existent preview
        }
        try {
            mCamera.setPreviewDisplay(holder);
            Camera.Parameters parameters = mCamera.getParameters();
//            parameters.setPreviewSize(480, 800);
            parameters.setPreviewSize(mPreviewSize.preW, mPreviewSize.preH);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
            autoFocus();
            mCamera.setPreviewCallback(new Camera.PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera) {
                    if (mPreviewCallback != null) {
                        mPreviewCallback.onPreviewFrame(data, camera, mPreviewSize/*,getRat()*/);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @return 以Home键为基准
     * 90度是向左的   270度是向右的   180度是向下   0度是向上
     * <p>
     * 摄像机方向, 向上 0 ； 向下 2 ；   向左 1； 向右 3
     */
    private int getRat() {
        int degree = CameraUtils.INS.getOrientation(getContext(), mCamera, mCameraId);
        int rat = 0;
        switch (degree) {
            case 0:
                rat = 0;
                break;
            case 90:
                rat = 3;
                break;
            case 180:
                rat = 2;
                break;
            case 270:
                rat = 1;
                break;
        }
        return rat;
    }

    /**
     * This method is only valid when preview is active
     * (between {@link Camera#startPreview()} and before {@link Camera#stopPreview()}).
     */
    private void autoFocus() {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters cameraParam = mCamera.getParameters();
        List<String> focusModes = cameraParam.getSupportedFocusModes();
        if (focusModes == null) {
            return;
        }
        // Autofocus mode is supported
        if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
            // get Camera parameters

            // set the focus mode
            cameraParam.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            // set Camera parameters
            mCamera.setParameters(cameraParam);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setSurfaceViewSize(w, h);
    }

    private void setSurfaceViewSize(int w, int h) {
        if (w == 0 || h == 0 || mCamera == null) {
            return;
        }
        Camera.Parameters cameraParam = mCamera.getParameters();
        mPreviewSize = CameraUtils.INS.findBestPreviewSize(getContext(), mCameraId, w, h, cameraParam.getSupportedPreviewSizes());
        if (mPreviewSize == null) {
            return;
        }
        post(new Runnable() {
            @Override
            public void run() {
                final ViewGroup.LayoutParams params = mPreviewView.getLayoutParams();
                params.width = (int) mPreviewSize.viewW;
                params.height = (int) mPreviewSize.viewH;
                mPreviewView.setLayoutParams(params);
            }
        });
    }

    public void destroy() {
        stop();
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    public void setPreviewCallback(PreviewCameraCallback previewCallback) {
        this.mPreviewCallback = previewCallback;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    public void takePic() {
        if (null != mCamera) {
            mCamera.startPreview();
            mCamera.takePicture(null, null, pictureCallback);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        super.onDetachedFromWindow();
    }

    public void stop() {
        Log.d("stop", "stop camera");
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
        }
//        releaseCamera();
    }

    /**
     * view显示和影藏时自动切换
     */
//    @Override
//    public void setVisibility(int visibility) {
//        super.setVisibility(visibility);
//        if (visibility == VISIBLE) {
//            start();
//            Log.e(TAG,"-->VISIBLE");
//        } else {
//            stop();
//            Log.e(TAG,"-->UNVISIBLE");
//        }
//
//    }
    public void start() {
        long time = System.currentTimeMillis();
        initCamera();
        int w = getWidth();
        int h = getHeight();
        if (w == 0 || h == 0) {
            return;
        }
        setSurfaceViewSize(w, h);
        startPreview(mPreviewView.getHolder());
        Log.e("打开相机耗时->", (System.currentTimeMillis() - time) + "");
    }

    /**
     * 窗口发生改变时会自动调用,但是这个没有旋转屏幕,不需要
     */
//    @Override
//    public void onWindowFocusChanged(boolean hasWindowFocus) {
//        Log.e("-->", "onWindowFocusChanged: " + hasWindowFocus);
//        super.onWindowFocusChanged(hasWindowFocus);
//        if (hasWindowFocus) {
//            start();
//        } else {
//            stop();
//        }
//    }

    private class SurfacePreviewCallback implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            start();
        }

        //        changing the size of the camera preview requires that you first stop the preview,
//        change the preview size, and then restart the preview
        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.
            startPreview(holder);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            stop();
            // empty. Take care of releasing the Camera preview in your activity.
        }
    }

    public int getCameraDisplayOritation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_FRONT, info);
        int degree = 0;
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degree) % 360;
            //处理前置的镜像问题
            result = (360 - result) % 360;
        } else {
            result = (info.orientation - degree + 360) % 360;
        }
        return result;
    }

    /**
     * 相机拍照监听接口
     */
    public interface OnCameraStatusListener {
        /**
         * 相机拍照结束事件
         */
        void onCameraStopped(byte[] data, Camera camera);
    }

    /**
     * 设置监听事件
     */
    public void setOnCameraStatusListener(OnCameraStatusListener listener) {
        this.listener = listener;
    }
    /**
     * 拍照回调
     * 创建一个PictureCallback对象，并实现其中的onPictureTaken方法
     */
    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {

        // 该方法用于处理拍摄后的照片数据
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // 停止照片拍摄
            try {
                camera.stopPreview();
            } catch (Exception e) {
            }
            // 调用结束事件
            if (null != listener) {
                listener.onCameraStopped(data,camera);
            }
        }
    };
}
