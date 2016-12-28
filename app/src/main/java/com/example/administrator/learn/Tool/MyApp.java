package com.example.administrator.learn.Tool;

import android.app.Application;

import com.androidnetworking.AndroidNetworking;
import com.duanqu.qupai.jni.ApplicationGlue;

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
        /*阿里推流*/
        System.loadLibrary("gnustl_shared");
//        System.loadLibrary("ijkffmpeg");//目前使用微博的ijkffmpeg会出现1K再换wifi不重连的情况
        System.loadLibrary("qupai-media-thirdparty");
//        System.loadLibrary("alivc-media-jni");
        System.loadLibrary("qupai-media-jni");
        /*加载图片*/
        ApplicationGlue.initialize(this);
        /*网络框架*/
        AndroidNetworking.initialize(this);
    }
}
