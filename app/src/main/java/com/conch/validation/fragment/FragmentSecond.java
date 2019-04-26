package com.conch.validation.fragment;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.conch.validation.activity.MainActivity;
import com.conch.validation.R;
import com.conch.validation.fragment.listener.IDCardCheckListener;
import com.conch.validation.util.FileUtil;
import com.conch.validation.util.LogUtils;
import com.conch.validation.util.PictureLoadUtil;

import java.io.File;

import static com.conch.validation.base.IDCardCode.REQUEST_CODE_CAMERA;

/**
 * 第二步:
 * 上传身份证,验证
 *
 * @author Oranger
 * on 2019/4/11/011
 */

public class FragmentSecond extends Fragment {
    private static String ARG_PARAM = "param_key";

    private MainActivity mActivity;

    /**
     * 相机 || 身份证
     */
    private ImageView mIv;

    /**
     * 是否得到身份证检测结果
     */
    private boolean isResult = false;

    /**
     * 是否获取到百度OCR token
     */
    private boolean hasGotToken = false;

    /**
     * 身份证验证结果
     */
    private TextView mTvState;
    /**
     * 身份证验证结果
     */
    private ImageView mIvState;

    private IDCardCheckListener idCardCheckListener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LogUtils.e("life", "onAttack");
        mActivity = (MainActivity) context;
        if (context instanceof IDCardCheckListener) {
            idCardCheckListener = (IDCardCheckListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement IDCardCheckListener");
        }

        initAccessTokenWithAkSk();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.e("life", "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        LogUtils.e("life", "onCreateView");
        View inflate = inflater.inflate(R.layout.fragment_second_step, container, false);
        mTvState = inflate.findViewById(R.id.tv_second_state);
        mIvState = inflate.findViewById(R.id.iv_second_state);
        mIv = inflate.findViewById(R.id.iv_second);
        mIv.setOnClickListener(v -> {
            if (!isResult) {
               takePhoto();
            }
        });
        return inflate;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtils.e("life", "onActivityCreated");
    }

    @Override
    public void onStart() {
        LogUtils.e("life", "onStart");
        super.onStart();
    }
    @Override
    public void onResume() {
        super.onResume();
        LogUtils.e("life", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtils.e("life", "onPause");
    }
    @Override
    public void onStop() {
        LogUtils.e("life", "onStop");
        super.onStop();
    }




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtils.e("life", "onDestroyView");
        // 释放内存资源
        OCR.getInstance(mActivity).release();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtils.e("life", "onDetach");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    /**
     * 在 fragment hide 时 调用
     * @param hidden
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        refresh();
    }

    /**
     * 回到最初状态
     */
    private void refresh() {
        mIv.setImageResource(R.drawable.ic_camera);
        mIvState.setVisibility(View.INVISIBLE);
        mTvState.setText("");
    }


    /**
     * 创建时使用 setArguments 传入参数
     * 方便在系统内存不足销毁该页面后,重新获取数据
     *
     * @return
     */
    public static FragmentSecond newInstance() {
        FragmentSecond fragmentSecond = new FragmentSecond();
        Bundle bundle = new Bundle();
//        bundle.putString(ARG_PARAM, idCardNumber);
        // 设置参数
        fragmentSecond.setArguments(bundle);
        return fragmentSecond;
    }



    /**
     * 处理 百度OCR 身份证识别结果
     *
     * @param idCardSide
     * @param filePath
     */
    private void recIDCard(String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        // 设置身份证正反面
        param.setIdCardSide(idCardSide);
        // 设置方向检测
        param.setDetectDirection(true);
        // 设置图像参数压缩质量0-100, 越大图像质量越好但是请求时间越长。 不设置则默认值为20
        param.setImageQuality(20);

        OCR.getInstance(mActivity).recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null) {
                    isResult = false;
                    FragmentFirst first = (FragmentFirst) mActivity.getSupportFragmentManager().findFragmentByTag("First");
                    String idCard = first.getIDCard();
                    boolean b = idCard.equalsIgnoreCase(result.getIdNumber().getWords());
                    //文字说明
                    mTvState.setText(b? getResources().getString(R.string.success ): getResources().getString(R.string.fail));
                    mIvState.setImageResource(b?R.drawable.ic_success:R.drawable.ic_error);
                    //图标说明
                    //结果回调
                    idCardCheckListener.onIDCardCheckListener(b);
                    LogUtils.e("idCard", result.toString());
                }
            }

            @Override
            public void onError(OCRError error) {
                LogUtils.e("idCard", error.toString());
            }
        });
    }


    /**
     * 用明文ak，sk初始化
     */
    private void initAccessTokenWithAkSk() {
        OCR.getInstance(mActivity).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {

            }
        }, mActivity, "EPsa56XaLrPBlCxAvREyiWDM", "GZgXWxLHs4yq52lQluSyH3s2W1p6Yald");
    }

    /**
     * 获取 token授权状态
     *
     * @return
     */
    private boolean checkTokenStatus() {
        if (!hasGotToken) {
//            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //身份证照片
        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = FileUtil.getSaveFile(mActivity).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                        PictureLoadUtil.loadFilePathWithoutCache(filePath,mIv);
                    }
                }
            }
        }
    }


    public void takePhoto() {
        mTvState.setText("");
        mIvState.setImageDrawable(null);
        mIv.setImageDrawable(null);
        Intent intent = new Intent(mActivity, CameraActivity.class);
        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(mActivity.getApplication()).getAbsolutePath());
        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }
    

}
