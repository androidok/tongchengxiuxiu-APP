package com.example.administrator.learn.Tool;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;

/**
 * Created by Administrator on 2016/12/6.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ShareSDK.initSDK(this);
        /*极光推送*/
        JPushInterface.init(this);
    }
}
