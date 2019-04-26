package com.conch.validation.activity;

import android.arch.lifecycle.ViewModel;
import android.widget.Button;
import android.widget.EditText;

import com.conch.validation.R;
import com.conch.validation.base.BaseHttpActivity;


public class LoginActivity extends BaseHttpActivity {

    private Button mBtLogin;
    private EditText mEtName;
    private EditText mEtPhone;


    @Override
    protected int setContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        mBtLogin = findViewById(R.id.bt_login);
        mBtLogin.setOnClickListener(v -> login());

        mEtName = findViewById(R.id.et_login_user_name);
        mEtPhone = findViewById(R.id.et_login_user_phone);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected ViewModel initViewModel() {
        return null;
    }

    private void login() {
        //登录验证



        //最终登录
        startActivity(MainActivity.class,true);
    }
}
