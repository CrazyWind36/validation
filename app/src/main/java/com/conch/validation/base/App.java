package com.conch.validation.base;

import android.app.Application;
import android.os.Build;
import android.support.annotation.RequiresApi;




/**
 * Created by Administrator on 2017/12/21/021.
 */

public class App extends Application {




    // 定义一个SingletonTest类型的变量（不初始化，注意这里没有使用final关键字）
    private static App Singleton;

    // 定义一个静态的方法（调用时再初始化SingletonTest，使用synchronized 避免多线程访问时，可能造成重的复初始化问题）
    public static  App getInstance() {
        return Singleton;
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate() {
        super.onCreate();
        Singleton = this;


        /**
         * ----------------------------------------------------------
         */
        //调试的时候可以注释掉,生产环境要打开,不然客户那边报错,你不知道
//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(getApplicationContext());
    }

    @Override
    public void onLowMemory() {
        System.gc();
        super.onLowMemory();
    }

}
