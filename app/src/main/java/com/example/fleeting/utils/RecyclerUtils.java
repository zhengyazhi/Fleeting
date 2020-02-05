package com.example.fleeting.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class RecyclerUtils {
    public static int getScreenWidth(Context context) {
        //窗口管理器
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE );
        //显示度量
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels; //返回屏幕宽度像素
    }
}
