package com.conch.validation.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;



/**
 * Created by FHXJR
 * on 2018/6/27/027.
 */

public abstract class BaseActivity extends AppCompatActivity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initBeforeSetContentView();
        super.onCreate(savedInstanceState);
        setContentView(setContentView());

        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }





    /**
     * 在设置布局前进行一些初始化设置
     */
    protected void initBeforeSetContentView() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }



    /**
     * 设置布局id
     *
     * @return
     */
    protected abstract int setContentView();

    protected abstract void initView();

    protected abstract void initData();

    /**
     * 打开新页面,不关闭当前页面
     *
     * @param aClass
     */
    protected void startActivity(Class aClass) {
        startActivity(aClass, false);
    }

    /**
     * 打开新页面
     *
     * @param aClass
     * @param isCloseCurrentActivity true:关闭当前页面 false:不关闭当前页面
     */
    protected void startActivity(Class aClass, boolean isCloseCurrentActivity) {
        Intent intent = new Intent(this, aClass);
        startActivity(intent);
        if (isCloseCurrentActivity) {
            finish();
        }
    }

    protected String getTextFromView(View view) {
        if (view != null && view instanceof TextView) {
            TextView textview = (TextView) view;
            return textview.getText().toString().trim();
        }
        if (view != null && view instanceof EditText) {
            EditText editText = (EditText) view;
            return editText.getText().toString().trim();
        }
        return "";
    }





}
