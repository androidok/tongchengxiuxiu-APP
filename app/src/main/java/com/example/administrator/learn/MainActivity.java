package com.example.administrator.learn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.administrator.learn.Model.PersonalInfo;
import com.example.administrator.learn.Model.putPictureInfo;
import com.example.administrator.learn.ServceTool.ApiService;
import com.example.administrator.learn.Tool.EvenbusInfo;
import com.example.administrator.learn.Tool.PayUtil;
import com.example.administrator.learn.Tool.SPUtils;
import com.example.administrator.learn.Tool.ShareUtils;
import com.example.administrator.learn.Tool.Sharedparms;
import com.example.administrator.learn.Tool.UtilTool;
import com.example.administrator.learn.Tool.YImagePicker;
import com.example.administrator.learn.Tool.evenbus_push;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;


public class MainActivity extends Activity implements YImagePicker.OnImagePickedListener {

    private WebView mwebview;
    private static final int MSG_WEIXIN_SCUSSE = 1;
    private static final int MSG_WEIXIN_ERROR = 2;


    public static MainActivity mainActivity;
    private YImagePicker imagePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mwebview = (WebView) findViewById(R.id.webView);
        EventBus.getDefault().register(this);
        imagePicker = new YImagePicker(this, this);
        this.mainActivity = this;
        mwebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        WebSettings webSettings = mwebview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUserAgentString(Sharedparms.WEBVIEWA_AGENT + UtilTool.getVersionName(this));
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mwebview.addJavascriptInterface(new javaScriptObjcet(), "jnoo");
        mwebview.loadUrl(Sharedparms.WEBVIEW_UIL);
        mwebview.setWebChromeClient(new WebChromeClient());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Bundle extras = getIntent().getExtras();
                try {
                    if (getIntent().hasExtra("extra")) {
                        String extra = extras.getString("extra");
                        if (!TextUtils.isEmpty(extra)) {
                            JSONObject jsonObject = new JSONObject(extra);
                            //push推送出来  打开主页面，然后调用js方法,这个时候要等会再加载，不然主页面还没出来，加载这个就加载不出来
                            mwebview.loadUrl("javascript:openNotification(" + jsonObject + ")");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, 3000);

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mwebview.canGoBack()) {
            mwebview.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void authorize(Platform plat) {
        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Message msg = new Message();
                msg.what = MSG_WEIXIN_SCUSSE;
                msg.obj = platform;
                handler.sendMessage(msg);

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d("微信登录", "失败" + throwable.getMessage());
                Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                handler.sendEmptyMessage(MSG_WEIXIN_ERROR);

            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toast.makeText(MainActivity.this, "取消登录", Toast.LENGTH_LONG).show();
            }
        });
        plat.SSOSetting(true);
        plat.removeAccount(true);
        ShareSDK.removeCookieOnAuthorize(true);
        plat.showUser(null);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WEIXIN_SCUSSE:
                    Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    Platform platform = (Platform) msg.obj;
                    PlatformDb db = platform.getDb();
                    try {
                        JSONObject jsonObject = new JSONObject(db.exportData());
                        Log.e("微信登录信息", "" + jsonObject.toString());
                        mwebview.loadUrl("javascript:success(" + jsonObject + ")");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_WEIXIN_ERROR:
                    Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("login_error", "登入失败");
                        /*
                        * 获取到信息返回给js
                        * */
                        mwebview.loadUrl("javascript:error(" + jsonObject + ")");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShareSDK.stopSDK(this);
        EventBus.getDefault().unregister(this);
        mainActivity = null;
    }
    private static int SDK_PAY_FLAG = 3;//支付宝支付
    Handler Mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SDK_PAY_FLAG) {//支付宝支付
                String result = (String) msg.obj;
                String substring = result.split(";")[0].split("=")[1];
                String resultStatus = substring.substring(1, substring.length() - 1);
                if ("9000".equalsIgnoreCase(resultStatus)) {
                        mwebview.loadUrl("javascript:success(" + resultStatus + ")");
                } else {
                    mwebview.loadUrl("javascript:error(" + resultStatus + ")");

                }
            }

        }
    };
    /**
     * 微信回调页面传过来，判断是否支付成功
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EvenbusInfo event) {
        int respCode = event.getRespCode();
        UtilTool.ShowToast(MainActivity.this,""+respCode);
        if (respCode == 0) {//成功

            mwebview.loadUrl("javascript:successCallback(" + respCode + ")");
        } else if (respCode == -1) {//失败
            mwebview.loadUrl("javascript:failCallback(" + respCode + ")");
        } else if (respCode == -2) {//取消支付
            mwebview.loadUrl("javascript:cancelCallback(" + respCode + ")");
        }

    }

    /**
     * 推送过来   当打开app页面时
     *
     * @param push
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(evenbus_push push) {
        String pushInfo = push.getPushInfo();
        try {
            JSONObject jsonObject = new JSONObject(pushInfo);
            mwebview.loadUrl("javascript:receiveNotification(" + jsonObject + ")");
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 上传照片
     */
    private void putPicture(String path) {
        final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this, "", "开始直播，稍等...");
        ApiService.putPicture(path, new ApiService.ParsedRequestListener<putPictureInfo>() {
            @Override
            public void onResponseResult(putPictureInfo response) {
                progressDialog.dismiss();
                if (response.getStatus() == Sharedparms.statusSuccess) {
                    SPUtils.Putimage_url(MainActivity.this, response.getData().getUrl());
                    pushFlow();
                } else {
                    UtilTool.ShowToast(MainActivity.this, response.getMsg());
                }
            }

            @Override
            public void _OnError(String errormessage) {
                UtilTool.ShowToast(MainActivity.this, errormessage);
//                progressDialog.dismiss();
            }
        });


    }


    private void pushFlow() {
        String livertmpUrl = SPUtils.getlivertmpUrl(MainActivity.this);
         /*开始推流*/
        PushFlowActivity.RequestBuilder builder = new PushFlowActivity.RequestBuilder()
                .bestBitrate(1000)//600
                .cameraFacing(1)//是否前置摄像头  1前面  0后面
                .dx(14)//marginx
                .dy(14)
                .site(1)//水印位置
                .rtmpUrl(livertmpUrl)//rtmp服务器地址
                .videoResolution(540)//360
                .portrait(false)//是否横屏
                //.watermarkUrl("assets:///spalsh.png")// 水印图片路径
                //.watermarkUrl("assets://qupai-logo.png")
                .minBitrate(400)//500帧率
                .maxBitrate(1200)//600
                .frameRate(1000)//600
                .initBitrate(1000);//800
        PushFlowActivity.startActivity(this, builder);
    }

    /**
     * 获取个人信息
     */
    private void getPersonalinfo() {
        String uid = SPUtils.getUid(this);
        if (TextUtils.isEmpty(uid)) {
            UtilTool.ShowToast(this, "个人信息获取失败");
            return;
        }
        ApiService.getPersonnalInfo(uid, new ApiService.ParsedRequestListener<PersonalInfo>() {
            @Override
            public void onResponseResult(PersonalInfo response) {
                if (response.getStatus() == Sharedparms.statusSuccess) {
                    SPUtils.PutlivertmpUrl(MainActivity.this, response.getData().getLive_rtmp());
                    SPUtils.PutNiceName(MainActivity.this, response.getData().getUser_nicename());
                    SPUtils.PutUserAccount(MainActivity.this, response.getData().getUser_login());
                    SPUtils.Putheader_url(MainActivity.this, response.getData().getAvatar());
                    Log.e("推流地址", "" + response.getData().getLive_rtmp());
                    //去拍照
                    imagePicker.startImagePickFromCamera();
                } else {
                    //status -4;//您已经在直播了
                    //status -3;//您已经被禁播
                    UtilTool.ShowToast(MainActivity.this, response.getMsg());
                }
            }

            @Override
            public void _OnError(String errormessage) {
                UtilTool.ShowToast(MainActivity.this, errormessage);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imagePicker.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onImagePicked(Bitmap bitmap, int requestCode, Intent data, Uri originalUri, String path) {
//        UtilTool.ShowToast(this, path);
        putPicture(path);
    }

    @Override
    public void onImageCroped(Bitmap bm) {

    }

    @Override
    public void onPickFail(Exception e) {

    }


    public class javaScriptObjcet {


        /*
                * 方法由js调用
                * */
        @JavascriptInterface
        public void wxlogin() {
            authorize(new Wechat(MainActivity.this));
            Toast.makeText(MainActivity.this, "正在登录", Toast.LENGTH_LONG).show();
        }

        @JavascriptInterface
        public void Alias(String uid) {
            //传id过来，设置别名
            JPushInterface.setAlias(MainActivity.this, uid, null);
            SPUtils.PutUid(MainActivity.this, uid);
//            UtilTool.ShowToast(MainActivity.this, uid);
        }

        /*
        * 微信支付
        * */
        @JavascriptInterface
        public void WXpay(String wxPayInfo) {

            PayUtil.getInstance(MainActivity.this).wxPay(wxPayInfo);

        }


        @JavascriptInterface
        public void Zhipay(final String payJsInfo) {
            PayUtil.getInstance(MainActivity.this).zhiPay(Mhandler,payJsInfo);


        }

        /**
         * @param type     分享类型 对应值：  1：微信朋友，2：微信朋友圈
         * @param title
         * @param desc     分享描述
         * @param imgurl   分享图片
         * @param fulllink 分享链接
         */
        @JavascriptInterface
        public void shareWechat(String type, String title, String desc, String imgurl, String fulllink) {
            if (type.equalsIgnoreCase("1")) {
                ShareUtils.shareweixin(MainActivity.this,desc , title, imgurl, fulllink, false, null);
            } else {
                ShareUtils.shareWechatMoments(MainActivity.this, desc, title, imgurl, fulllink, false, null);
            }
        }

        @JavascriptInterface
        public void shareWeibo(String title, String url, String imageUrl) {
            ShareUtils.shareSinaWei(MainActivity.this,title, title + url, imageUrl, false, null);
        }

        /**
         * @param type        1 qq  2 QQ空间
         * @param url
         * @param title
         * @param appName
         * @param description
         */
        @JavascriptInterface
        public void shareQQ(String type, String url, String title, String imageUrl, String appName, String description) {
            if (type.equalsIgnoreCase("1")) {
                //qq
                ShareUtils.shareQQ(MainActivity.this, description,title, url, imageUrl, false, null);
            } else {
                //qq空间
                ShareUtils.shareQZone(MainActivity.this, description,title, url, imageUrl, appName, false, null);
            }
        }

        /**
         * 我要直播
         */
        @JavascriptInterface
        public void livelist() {
            UtilTool.creatDialog(MainActivity.this, "提示！", "               先拍摄直播封面图", "确定", new UtilTool.SetonListener<DialogInterface>() {
                @Override
                public void setonlistener(DialogInterface dialogInterface, int i) {
                    getPersonalinfo();
                }
            }).show();


        }

        @JavascriptInterface
        public void VideoPlay(String data) {
//            UtilTool.ShowToast(MainActivity.this, data.toString());
            if (TextUtils.isEmpty(data)) {
                UtilTool.ShowToast(MainActivity.this, "出现问题");
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(data);
                String live_url = jsonObject.getString("live_url");
                String img = jsonObject.getString("img");
                String url = jsonObject.getString("url");
                String liveId = jsonObject.getString("liveId");
                Log.d("直播播放信息", "" + live_url + ".." + url + ".." + img);
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                intent.putExtra(Sharedparms.IntentInfo.LIVERTMPURL, live_url);
                intent.putExtra(Sharedparms.IntentInfo.IMAGEURL, img);
                intent.putExtra(Sharedparms.IntentInfo.WEBVIEWURL, url);
                intent.putExtra(Sharedparms.IntentInfo.LIVEID,liveId);
                startActivity(intent);
            } catch (JSONException e) {

            }

        }




    }


}
