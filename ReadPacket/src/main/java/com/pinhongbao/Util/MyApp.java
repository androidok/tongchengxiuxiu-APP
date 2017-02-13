package com.pinhongbao.Util;

import android.app.Application;
import com.androidnetworking.AndroidNetworking;

import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Administrator on 2017/2/6.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        /*网络框架*/
        AndroidNetworking.initialize(this);
        ShareSDK.initSDK(this);
    }
}
