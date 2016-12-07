package com.example.administrator.learn.Tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * @author harry
 * @version Creat on 2014-9-1上午10:58:47
 */
public class WebViewUtil {
    /**
     * 配置webview
     */
    @SuppressLint("NewApi") public static WebView configWebViewSetting(Context context, WebView webview) {
        webview.setHorizontalScrollBarEnabled(false);// 水平不显示
        webview.setVerticalScrollBarEnabled(false); // 垂直不显示
        // 开启 Application Caches 功能
        webview.setDrawingCacheBackgroundColor(0x00000000);
        webview.setFocusableInTouchMode(true);
        webview.setFocusable(true);
        webview.setAnimationCacheEnabled(false);
        webview.setDrawingCacheEnabled(true);
        webview.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        webview.setWillNotCacheDrawing(false);
        webview.setAlwaysDrawnWithCacheEnabled(true);
        webview.setScrollbarFadingEnabled(true);
        webview.setSaveEnabled(true);
        webview.requestFocus(View.FOCUS_DOWN);
        // 兼容2.3键盘弹出
        webview.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_UP:
                        if (!v.hasFocus())
                        {
                            v.requestFocus();
                        }
                        break;
                }
                return false;
            }
        });
        WebSettings settings = webview.getSettings();
        // 在webview的userAgent中加设备信息和版本号
        
        settings.setJavaScriptEnabled(true);
        // 设置可以使用localStorage
        settings.setDomStorageEnabled(true);
        // 开启 database storage API 功能
        // 应用可以有数据库
        settings.setDatabaseEnabled(true);
        String dbPath = context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        settings.setDatabasePath(dbPath);
        // 应用可以有缓存
        settings.setAppCacheEnabled(true);
        settings.setAppCacheMaxSize(1024 * 1024 * 8);// 设置缓冲大小，我设的是8M
        String appCaceDir = context.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        settings.setAppCachePath(appCaceDir);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setGeolocationDatabasePath(context.getCacheDir().getAbsolutePath());
        // 可以读取文件缓存(manifest生效)
        settings.setAllowFileAccess(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        // 设置加载进来的页面自适应手机屏幕
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT > 11)
        {
            settings.setDisplayZoomControls(false);
        }
        settings.setAllowContentAccess(true);
        settings.setDefaultTextEncodingName("utf-8");
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT < 18)
        {
            settings.setSavePassword(true);
        }
        settings.setSaveFormData(true);
        if (Build.VERSION.SDK_INT > 16)
        {
            settings.setAllowFileAccessFromFileURLs(false);
            settings.setAllowUniversalAccessFromFileURLs(false);
        }
        /*
         * 默认情况html代码下载到WebView后，webkit开始解析网页各个节点，
         * 发现有外部样式文件或者外部脚本文件时，会异步发起网络请求下载文件
         * ，但如果在这之前也有解析到image节点，那势必也会发起网络请求下载相应的图片
         * 。在网络情况较差的情况下，过多的网络请求就会造成带宽紧张，影响到css或js文件加载完成的时间
         * ，造成页面空白loading过久。解决的方法就是告诉WebView先不要自动加载图片，等页面finish后再发起图片加载。
         * 在系统API在19以上的版本作了兼容
         * 因为4.4以上系统在onPageFinished时再恢复图片加载时,如果存在多张图片引用的是相同的src时,
         * 会只有一个image标签得到加载，因而对于这样的系统我们就先直接加载。
         */
        if (Build.VERSION.SDK_INT >= 19)
        {
            settings.setLoadsImagesAutomatically(true);
        }
        else
        {
            settings.setLoadsImagesAutomatically(false);
        }
        CookieSyncManager.createInstance(context);
        CookieManager.getInstance().setAcceptCookie(true);
        CookieSyncManager.getInstance().sync();
        return webview;
    }

    /**
     * 保存cookie到本地
     */
   
}
