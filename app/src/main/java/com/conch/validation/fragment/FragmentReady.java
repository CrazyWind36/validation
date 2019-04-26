package com.conch.validation.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.conch.validation.activity.MainActivity;
import com.conch.validation.R;

/**
 * 准备
 * @author Oranger
 *         on 2019/4/11/011
 */

public class FragmentReady extends Fragment {


    private MainActivity mActivity;
    private ImageView mIvState;
    private TextView mTvState;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_ready, container, false);
        mIvState = inflate.findViewById(R.id.iv_ready_state);
        mTvState = inflate.findViewById(R.id.tv_ready_state);
        return inflate;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;

    }

    /**
     * 创建时使用 setArguments 传入参数
     * 方便在系统内存不足销毁该页面后,重新获取数据
     *
     * @return
     */
    public static FragmentReady newInstance() {
        FragmentReady fragmentReady = new FragmentReady();
        Bundle bundle = new Bundle();
//        bundle.putString(ARG_PARAM, str);
// 设置参数
        fragmentReady.setArguments(bundle);
        return fragmentReady;
    }



}
