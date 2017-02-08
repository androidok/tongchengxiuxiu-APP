package com.pinhongbao.Util;

import android.app.Application;
import com.androidnetworking.AndroidNetworking;

/**
 * Created by Administrator on 2017/2/6.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        /*网络框架*/
        AndroidNetworking.initialize(this);
    }
}
