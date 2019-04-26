package com.conch.validation.util;

import android.widget.Toast;

import com.conch.validation.base.App;


/**
 * Created by FHXJR
 * on 2018/7/16/016.
 */

public class ToastUtil {
    private static Toast toast;

    public static void showShortToast(String content) {
        if (toast == null) {
            toast = Toast.makeText(App.getInstance(),
                    content,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(content);
        }
        toast.show();
    }
}
