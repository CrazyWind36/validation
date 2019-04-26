package com.conch.validation.util;

import android.util.Log;

/**
 * Created by FHXJR
 * on 2018/7/6/006.
 */

public class LogUtils {
    /**
     * None 0
     * Verbose 1
     * Debug 2
     * Info 3
     * Warn 4
     * Error 5
     * Assert 6
     * All 7
     */
    public static final int LEVEL = 7;

    public static void e(String tag, String msg) {
        if (LEVEL >= 5 ){
            Log.e(tag,msg);
        }
    }
}
