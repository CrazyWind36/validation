package com.conch.validation.activity;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.conch.validation.R;
import com.conch.validation.base.FragmentTag;
import com.conch.validation.base.StepCode;
import com.conch.validation.fragment.FragmentFirst;
import com.conch.validation.fragment.FragmentReady;
import com.conch.validation.fragment.FragmentSecond;
import com.conch.validation.fragment.FragmentThird;
import com.conch.validation.fragment.listener.FaceValidationCheckListener;
import com.conch.validation.fragment.listener.IDCardCheckListener;
import com.conch.validation.util.IDCardUtil;
import com.conch.validation.util.ToastUtil;


/**
 * 莫名其妙的项目
 * 这个是主干代码 master
 * 2340*1080分辨率
 *
 * @author Administrator
 */
public class MainActivity extends FragmentActivity implements View.OnClickListener, IDCardCheckListener,FaceValidationCheckListener {

    /**
     * 碎片
     */
    private FrameLayout container;
    /**
     * 标题
     */
    private TextView mTvTitle;
    /**
     * 返回按钮
     */
    private ImageView mIvBack;

    /**
     * 底部流程操作按钮
     */
    private Button mBtBottom;

    /**
     * 当前走到第几步流程
     */
    private int currentStep = StepCode.READY;

    /**
     * 控制 身份证识别页面的按钮点击事件
     */
    private boolean isIDCheckSuccess = false;
    /**
     * 控制 后台人脸识别页面的按钮点击事件
     */
    private boolean isFaceValidationSuccess = false;

    private FragmentReady fragmentReady;
    private FragmentFirst fragmentFirst;
    private Button mBtSecond;
    private FragmentSecond fragmentSecond;
    private FragmentThird fragmentThird;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        fragmentReady = FragmentReady.newInstance();
        fragmentFirst = FragmentFirst.newInstance();
        fragmentSecond = FragmentSecond.newInstance();
        fragmentThird = FragmentThird.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.activity_main_container, fragmentReady, FragmentTag.READY).commit();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        mTvTitle = findViewById(R.id.view_tv_title);
        mIvBack = findViewById(R.id.view_img_back);
        mIvBack.setOnClickListener(this);
        container = findViewById(R.id.activity_main_container);

        mBtBottom = findViewById(R.id.bt_bottom);
        mBtBottom.setOnClickListener(this);

        mBtSecond = findViewById(R.id.bt_second);
        mBtSecond.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_img_back:
                back();
                break;
            case R.id.bt_bottom:
                firstClick();
                break;
            case R.id.bt_second:
                secondClick();
                break;
        }
    }

    private void secondClick() {
        switch (currentStep) {
            case StepCode.SECOND:
                mBtSecond.setVisibility(View.INVISIBLE);
                mBtBottom.setVisibility(View.INVISIBLE);
                FragmentSecond fragment = (FragmentSecond) getSupportFragmentManager().findFragmentByTag(FragmentTag.SECOND);
                fragment.takePhoto();
                break;
                //后台验证失败后,返回按钮的点击事件,=什么逻辑需要讨论
                case StepCode.THIRD:


                    break;
        }

    }


    private void back() {
        switch (currentStep) {
            //退出
            case StepCode.READY:
                finish();
                break;
            //返回准备页面
            case StepCode.FIRST:
                currentStep = StepCode.READY;
                mBtBottom.setText(getResources().getText(R.string.start_validation));
                mTvTitle.setText(getResources().getText(R.string.app_title));
                //清空输入的数据
                FragmentFirst fragment = (FragmentFirst) getSupportFragmentManager().findFragmentByTag(FragmentTag.FIRST);
                fragment.refresh();
                replaceFragment(fragmentFirst, fragmentReady, FragmentTag.READY);
                break;
            case StepCode.SECOND:
                currentStep = StepCode.FIRST;
                mBtBottom.setVisibility(View.VISIBLE);
                mBtBottom.setText(getResources().getText(R.string.next_step));
                mTvTitle.setText(getResources().getText(R.string.first_title));
                replaceFragment(fragmentSecond, fragmentFirst, FragmentTag.FIRST);
                break;
            case StepCode.THIRD:
                currentStep = StepCode.SECOND;
                mBtBottom.setVisibility(View.INVISIBLE);
                mTvTitle.setText(getResources().getText(R.string.second_title));
                replaceFragment(fragmentThird, fragmentSecond, FragmentTag.SECOND);
                break;
        }
    }

    private void firstClick() {
        switch (currentStep) {
            //进入基本信息输入页面
            case StepCode.READY:
                currentStep = StepCode.FIRST;
                mBtBottom.setText(getResources().getText(R.string.next_step));
                mTvTitle.setText(getResources().getText(R.string.first_title));
                replaceFragment(fragmentReady, fragmentFirst, FragmentTag.FIRST);
                break;
            //进入身份证识别验证
            case StepCode.FIRST:
                FragmentFirst fragment = (FragmentFirst) getSupportFragmentManager().findFragmentByTag(FragmentTag.FIRST);
                String idCard = fragment.getIDCard();
                boolean b = IDCardUtil.checkIDNo(idCard);
                if (b) {
                    currentStep = StepCode.SECOND;
                    mBtBottom.setVisibility(View.INVISIBLE);
                    mTvTitle.setText(getResources().getText(R.string.second_title));
                    replaceFragment(fragmentFirst, fragmentSecond, FragmentTag.SECOND);
                }else {
                    ToastUtil.showShortToast(getResources().getText(R.string.please_input_correct_no).toString());
                }

                break;

            case StepCode.SECOND:
                //下一步
                if (isIDCheckSuccess) {
                    currentStep = StepCode.THIRD;
                    mBtSecond.setVisibility(View.INVISIBLE);
                    mBtBottom.setText(getResources().getText(R.string.start_face_validation));
                    mTvTitle.setText(getResources().getText(R.string.third_title));
                    replaceFragment(fragmentSecond, fragmentThird, FragmentTag.Third);
                } else {//重新拍摄
                    mBtSecond.setVisibility(View.INVISIBLE);
                    mBtBottom.setVisibility(View.INVISIBLE);
                    FragmentSecond second = (FragmentSecond) getSupportFragmentManager().findFragmentByTag(FragmentTag.SECOND);
                    second.takePhoto();
                }
                break;
            //人脸检测
            case StepCode.THIRD:
                if (isFaceValidationSuccess) {
                    back();
                }else {
                    //打开相机
                    FragmentThird fragmentThird = (FragmentThird) getSupportFragmentManager().findFragmentByTag(FragmentTag.Third);
                    fragmentThird.takePhoto();
                }
                break;
        }
    }

    private void replaceFragment(Fragment origin, Fragment next, String tag) {
        if (next.isAdded()) {
            if (next.isHidden()) {
                getSupportFragmentManager().beginTransaction().hide(origin).show(next).commit();
            }
        } else {
            getSupportFragmentManager().beginTransaction().hide(origin).add(R.id.activity_main_container, next, tag).commit();
        }
    }

    /**
     * 身份证 验证识别 结果
     *
     * @param isSuccess
     */
    @Override
    public void onIDCardCheckListener(boolean isSuccess) {
        isIDCheckSuccess = isSuccess;
        mBtBottom.setVisibility(View.VISIBLE);
        mBtBottom.setText(isSuccess ? getResources().getText(R.string.next_step) : getResources().getText(R.string.take_photo_again));
        if (isSuccess) {
            mBtSecond.setVisibility(View.VISIBLE);
            mBtSecond.setText(getResources().getText(R.string.take_photo_again));
        } else {
            mBtSecond.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * 后台人脸识别验证结果回调
     * @param isSuccess
     */
    @Override
    public void onFaceValidationCheckListener(boolean isSuccess) {
        isFaceValidationSuccess = isSuccess;
        if (isSuccess) {
            mBtSecond.setVisibility(View.INVISIBLE);
            mBtBottom.setText(getResources().getText(R.string.back));
        }else {
            mBtBottom.setText(getResources().getText(R.string.take_photo_again));
            mBtSecond.setVisibility(View.VISIBLE);
            mBtSecond.setText(getResources().getText(R.string.back));
        }
    }
}
