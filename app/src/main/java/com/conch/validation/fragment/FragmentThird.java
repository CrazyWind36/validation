package com.conch.validation.fragment;

import android.app.Activity;
import android.arch.lifecycle.ViewModel;
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

import com.baidu.ocr.ui.camera.CameraActivity;
import com.conch.validation.activity.MainActivity;
import com.conch.validation.R;
import com.conch.validation.activity.VideoActivity;
import com.conch.validation.base.BaseHttpFragment;
import com.conch.validation.fragment.listener.FaceValidationCheckListener;
import com.conch.validation.http.basis.exception.base.BaseException;
import com.conch.validation.http.viewmodel.FaceValidationViewModel;
import com.conch.validation.http.viewmodel.base.LViewModelProviders;
import com.conch.validation.util.FileUtil;



/**
 * 第三步:
 * 上传人脸信息,比对
 * @author Oranger
 *         on 2019/4/11/011
 */

public class FragmentThird extends Fragment {
    private MainActivity mActivity;
    private ImageView mIv;
    private ImageView mIvState;
    private TextView mTvState;
    private final int VIDEO = 0x11;


    /**
     * 识别验证结果回调
     */
    private FaceValidationCheckListener faceValidationCheckListener;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_third_step, container, false);
        mIv = inflate.findViewById(R.id.iv_third);
        mIvState = inflate.findViewById(R.id.iv_third_state);
        mTvState = inflate.findViewById(R.id.tv_third_state);
        return inflate;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
        if (context instanceof FaceValidationCheckListener) {
            faceValidationCheckListener = (FaceValidationCheckListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FaceValidationCheckListener");
        }

    }

    /**
     * 创建时使用 setArguments 传入参数
     * 方便在系统内存不足销毁该页面后,重新获取数据
     *
     * @return
     */
    public static FragmentThird newInstance() {
        FragmentThird fragmentThird = new FragmentThird();
        Bundle bundle = new Bundle();
        fragmentThird.setArguments(bundle);
        return fragmentThird;
    }


    public void takePhoto() {
        mTvState.setText("");
        mIvState.setImageDrawable(null);
        Intent intent = new Intent(mActivity, VideoActivity.class);
        startActivityForResult(intent,VIDEO);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == VIDEO ) {
            if (resultCode == Activity.RESULT_OK) {//成功
                faceValidationCheckListener.onFaceValidationCheckListener(true);
                mIvState.setImageResource(R.drawable.ic_success);
                mTvState.setText(getResources().getText(R.string.success));
            }else {//失败
                faceValidationCheckListener.onFaceValidationCheckListener(false);
                mIvState.setImageResource(R.drawable.ic_error);
                mTvState.setText(getResources().getText(R.string.fail));
            }
        }


    }
}
