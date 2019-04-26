package com.conch.validation.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.conch.validation.activity.MainActivity;
import com.conch.validation.R;

/**
 * 第一步:
 * 输入基本信息
 *
 * @author Oranger
 * on 2019/4/11/011
 */

public class FragmentFirst extends Fragment {
    private static String ARG_PARAM = "param_key";
    private String mParam;
    private MainActivity mActivity;
    /**
     * 用户姓名
     */
    private EditText mEtName;
    /**
     * 用户身份证号
     */
    private EditText mEtNumber;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
        //获取参数
        mParam = getArguments().getString(ARG_PARAM);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_first_step, container, false);
        mEtName = inflate.findViewById(R.id.et_user_name);
        mEtNumber = inflate.findViewById(R.id.et_identity_card);
        return inflate;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

    }

    /**
     * 创建时使用 setArguments 传入参数
     * 方便在系统内存不足销毁该页面后,重新获取数据
     *
     * @return
     */
    public static FragmentFirst newInstance() {
        FragmentFirst fragmentFirst = new FragmentFirst();
        Bundle bundle = new Bundle();
//        bundle.putString(ARG_PARAM, str);
// 设置参数
        fragmentFirst.setArguments(bundle);
        return fragmentFirst;
    }

    public String getIDCard() {
        return mEtNumber.getText().toString().trim();
    }

    /**
     * 恢复初始化
     */
    public void refresh() {
        mEtName.setText("");
        mEtNumber.setText("");
    }

}
