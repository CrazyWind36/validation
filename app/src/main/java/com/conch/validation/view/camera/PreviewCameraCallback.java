package com.conch.validation.view.camera;

import android.hardware.Camera;

/**
 * Created by wanjian on 2017/11/28.
 *
 */

public interface PreviewCameraCallback {
    /**
     * 正常摄像头预览
     * @param data
     * @param camera
     * @param previewSize
     */
    void onPreviewFrame(byte[] data, Camera camera, PreviewSize previewSize);


}
