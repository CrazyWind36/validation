package com.conch.validation.view.camera;

import android.hardware.Camera;

/**
 * Created by wanjian on 2017/11/8.
 */

public class CameraWrapper {
   public final Camera camera;
    public final int id;

    CameraWrapper(Camera camera, int id) {
        this.camera = camera;
        this.id = id;
    }
}
