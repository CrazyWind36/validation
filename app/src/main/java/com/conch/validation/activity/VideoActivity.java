package com.conch.validation.activity;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.conch.validation.R;
import com.conch.validation.base.BaseHttpActivity;
import com.conch.validation.http.basis.exception.base.BaseException;
import com.conch.validation.http.viewmodel.FaceValidationViewModel;
import com.conch.validation.http.viewmodel.base.LViewModelProviders;
import com.conch.validation.util.BitmapUtils;
import com.conch.validation.util.LogUtils;
import com.conch.validation.util.ToastUtil;
import com.conch.validation.util.base64.Base64Util;
import com.conch.validation.view.camera.PreviewCameraCallback;
import com.conch.validation.view.camera.PreviewCameraView;
import com.conch.validation.view.camera.PreviewSize;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class VideoActivity extends BaseHttpActivity implements PreviewCameraCallback {

    private PreviewCameraView mCameraPreview;


    /**
     * 后台人脸比对
     * 网络请求操作
     */
    private FaceValidationViewModel faceValidationViewModel;



    /**
     * 失败计数
     */
    private int failCount = 0;
    private ImageView mIvVideo;

    /**
     * 是否开始录屏
     */
    private boolean isVideo = false;

    @Override
    protected ViewModel initViewModel() {
        faceValidationViewModel = LViewModelProviders.of(this, FaceValidationViewModel.class);
        faceValidationViewModel.getValidationOkMutableLiveData().observe(this, this::validationOk);
        faceValidationViewModel.getValidationErrorMutableLiveData().observe(this, this::validationError);
        return faceValidationViewModel;
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_video;
    }

    @Override
    protected void initView() {
        mCameraPreview = findViewById(R.id.video);
        mCameraPreview.setPreviewCallback(this);

        mIvVideo = findViewById(R.id.iv_video);
        mIvVideo.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        isVideo = true;
                        break;
                    case MotionEvent.ACTION_UP:
                        isVideo = false;
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCameraPreview != null) {
            mCameraPreview.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraPreview != null) {
            mCameraPreview.destroy();
        }
    }

    /**
     * 是否下一帧开始识别
     */
    private boolean isNextFrame = true;
    @Override
    public void onPreviewFrame(byte[] data, Camera camera, PreviewSize previewSize) {
        if (isVideo && isNextFrame) {
            LogUtils.e("video",System.currentTimeMillis() + "时间");
            isNextFrame = false;
            //触发录像后,连续捕捉画面人脸,提交后台处理,1分钟内连续5次失败,则提示失败
            Bitmap bitmap = BitmapUtils.INS.cameraByteToBitmap(data, previewSize.preW, previewSize.preH);
            String param = Base64Util.bitmapToString(bitmap, true);
            faceValidationViewModel.faceValidation(param);
        }

    }


    /**
     * 识别验证失败
     *
     * @param e
     */
    private void validationError(BaseException e) {
        failCount++;
        LogUtils.e("video","失败次数=" + failCount);
        if (failCount == 5) {
            ToastUtil.showShortToast("识别失败,请重试");
            //失败,返回
           setResult(Activity.RESULT_CANCELED);
           finish();
        }else {

            isNextFrame = true;
        }

    }

    /**
     * 识别验证成功
     *
     * @param result
     */
    private void validationOk(String result) {
        LogUtils.e("video","成功" );
        isNextFrame = true;
        setResult(Activity.RESULT_OK);
        finish();
    }

}
