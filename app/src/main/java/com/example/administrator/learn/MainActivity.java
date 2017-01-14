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

import com.alipay.sdk.app.PayTask;
import com.example.administrator.learn.Model.PersonalInfo;
import com.example.administrator.learn.Model.putPictureInfo;
import com.example.administrator.learn.ServceTool.ApiService;
import com.example.administrator.learn.Tool.EvenbusInfo;
import com.example.administrator.learn.Tool.SPUtils;
import com.example.administrator.learn.Tool.ShareUtils;
import com.example.administrator.learn.Tool.Sharedparms;
import com.example.administrator.learn.Tool.SignUtils;
import com.example.administrator.learn.Tool.Util;
import com.example.administrator.learn.Tool.UtilTool;
import com.example.administrator.learn.Tool.YImagePicker;
import com.example.administrator.learn.Tool.evenbus_push;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
    private static int SDK_PAY_FLAG = 3;
    private static int WEIXIN_PAY = 4;//
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

    /**
     * 微信回调页面传过来，判断是否支付成功
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EvenbusInfo event) {
        int respCode = event.getRespCode();
        if (respCode == 1) {//成功
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
                progressDialog.dismiss();
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
//            Toast.makeText(MainActivity.this, wxPayInfo, Toast.LENGTH_LONG).show();
            if (TextUtils.isEmpty(wxPayInfo)) {
                Toast.makeText(MainActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                return;
            }
            //开始预支付/
            try {
                final JSONObject jsonObject = new JSONObject(wxPayInfo);
                final String tradeNo = jsonObject.getString("tradeNo");
                final String totalFee = jsonObject.getString("totalFee");
                final String body = jsonObject.getString("body");
                String notifyUrl = jsonObject.getString("notifyUrl");
                final String noncestr = UtilTool.genNonceStr();
                final String ipAddress = UtilTool.getIpAddress();
                List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
                packageParams.add(new BasicNameValuePair("appid", Sharedparms.weixinInfo.WEIXIN_APPID));
                packageParams.add(new BasicNameValuePair("body", body));
                packageParams.add(new BasicNameValuePair("mch_id", Sharedparms.weixinInfo.WINXIN_PARTNERID));
                packageParams.add(new BasicNameValuePair("nonce_str", noncestr));
                packageParams.add(new BasicNameValuePair("notify_url", notifyUrl));
                packageParams.add(new BasicNameValuePair("out_trade_no", tradeNo));
                packageParams.add(new BasicNameValuePair("spbill_create_ip", ipAddress));
                packageParams.add(new BasicNameValuePair("total_fee", totalFee));
                packageParams.add(new BasicNameValuePair("trade_type", "APP"));
                final String sign = UtilTool.genPackageSign(packageParams);
                packageParams.add(new BasicNameValuePair("sign", sign));
                String xmlstring = UtilTool.toXml(packageParams);
                final String parme;

                parme = new String(xmlstring.toString().getBytes(), "ISO8859-1");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = String.format(Sharedparms.weixinInfo.WEIXIN_API);
                        byte[] buf = Util.httpPost(url, parme);
                        String content = new String(buf);
                        Map<String, String> xml = UtilTool.decodeXml(content);
                        if (xml.containsKey("prepay_id")) {
                            String prepay_id = xml.get("prepay_id");
                            Map<String, String> messMap = new HashMap<String, String>();
                            messMap.put("prepay_id", prepay_id);
                            messMap.put("noncestr", noncestr);
                            messMap.put("sign", sign);
                            Message message = new Message();
                            message.what = WEIXIN_PAY;
                            message.obj = messMap;
                            Mhandler.dispatchMessage(message);
                        }

                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        /**
         * 微信支付接口
         *
         * @param prepay_id
         * @param
         * @param
         * @param
         * @param noncestr
         * @param
         * @param sign
         */
        private void WXPay(String prepay_id, String noncestr, String sign) {
            try {
                if (!TextUtils.isEmpty(prepay_id)) {
                    Log.d("微信支付:", "prepay_id" + Sharedparms.weixinInfo.WEIXIN_APPID + ".." + Sharedparms.weixinInfo.WINXIN_PARTNERID + ".." + prepay_id
                            + ".." + noncestr + ".." + UtilTool.genTimeStamp() + ".." + sign);
                    PayReq req = new PayReq();
  /*
        *注册app到微信
        * */
                    IWXAPI api = WXAPIFactory.createWXAPI(MainActivity.this, Sharedparms.weixinInfo.WEIXIN_APPID);
                    api.registerApp(Sharedparms.weixinInfo.WEIXIN_APPID);
                    req.appId = Sharedparms.weixinInfo.WEIXIN_APPID;
                    req.partnerId = Sharedparms.weixinInfo.WINXIN_PARTNERID;
                    req.prepayId = prepay_id;
                    req.nonceStr = UtilTool.getRanDomNum();
                    req.timeStamp = UtilTool.genTimeStamp();
                    req.packageValue = "Sign=WXPay";
                    List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                    signParams.add(new BasicNameValuePair("appid", req.appId));
                    signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                    signParams.add(new BasicNameValuePair("package", req.packageValue));
                    signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                    signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                    signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
                    req.sign = UtilTool.genPackageSign(signParams);
                    // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                    boolean b = api.sendReq(req);
                } else {
                    Log.e("微信支付", "服务器请求错误");

                }
            } catch (Exception e) {
                Log.e("微信支付", "异常：" + e.getMessage());

            }
        }

        @JavascriptInterface
        public void Zhipay(final String payJsInfo) {
            try {
                JSONObject jsonObject = new JSONObject(payJsInfo);
                String seller_id = jsonObject.getString("seller");
                String subject = jsonObject.getString("subject");
                String body = jsonObject.getString("body");
                String price = jsonObject.getString("price");
                String out_trade_no = jsonObject.getString("tradeNo");
                String it_b_pay = jsonObject.getString("timeout");
                String notify_url = jsonObject.getString("notifyUrl");
                String orderInfo = getOrderInfo(subject, body, price, seller_id, out_trade_no, it_b_pay, notify_url);
                /**
                 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
                 */
                String sign = sign(orderInfo);
                try {
                    /**
                     * 仅需对sign 做URL编码
                     */
                    sign = URLEncoder.encode(sign, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();
                Runnable payRunnable = new Runnable() {

                    @Override
                    public void run() {
                        // 构造PayTask 对象
                        PayTask alipay = new PayTask(MainActivity.this);
                        // 调用支付接口，获取支付结果
                        String result = alipay.pay(payInfo, true);

                        Message msg = new Message();
                        msg.what = SDK_PAY_FLAG;
                        msg.obj = result;
                        Mhandler.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            } catch (JSONException e) {
                e.printStackTrace();
            }


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
                ShareUtils.shareweixin(MainActivity.this, title, desc, imgurl, fulllink, false, null);
            } else {
                ShareUtils.shareWechatMoments(MainActivity.this, title, desc, imgurl, fulllink, false, null);
            }
        }

        @JavascriptInterface
        public void shareWeibo(String title, String url, String imageUrl) {
            ShareUtils.shareSinaWei(MainActivity.this, title + url, imageUrl, false, null);
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
                ShareUtils.shareQQ(MainActivity.this, description, url, imageUrl, false, null);
            } else {
                //qq空间
                ShareUtils.shareQZone(MainActivity.this, description, url, imageUrl, appName, false, null);
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
                Log.d("直播播放信息", "" + live_url + ".." + url + ".." + img);
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                intent.putExtra(Sharedparms.IntentInfo.LIVERTMPURL, live_url);
                intent.putExtra(Sharedparms.IntentInfo.IMAGEURL, img);
                intent.putExtra(Sharedparms.IntentInfo.WEBVIEWURL, url);
                startActivity(intent);
            } catch (JSONException e) {

            }

        }

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
                } else if (msg.what == WEIXIN_PAY) {//微信支付
                    Map<String, String> map = (Map<String, String>) msg.obj;
                    String prepay_id = map.get("prepay_id");
                    String noncestr = map.get("noncestr");
                    String sign = map.get("sign");
                    WXPay(prepay_id, noncestr, sign);

                }

            }
        };


        /**
         * @param subject      描述
         * @param body         详情
         * @param price        价格
         * @param seller_id    商家支付宝账号
         * @param out_trade_no 唯一订单号
         * @param it_b_pay     设置未付款交易的超时时间
         *                     notify_url    服务器异步通知页面路径
         * @return
         */
        private String getOrderInfo(String subject, String body, String price, String seller_id, String out_trade_no, String it_b_pay, String notify_url) {

            // 签约合作者身份ID
            String orderInfo = "partner=" + "\"" + Sharedparms.PayInfo.PARTNER + "\"";

            // 签约卖家支付宝账号
            orderInfo += "&seller_id=" + "\"" + seller_id + "\"";

            // 商户网站唯一订单号
            orderInfo += "&out_trade_no=" + "\"" + out_trade_no + "\"";

            // 商品名称
            orderInfo += "&subject=" + "\"" + subject + "\"";

            // 商品详情
            orderInfo += "&body=" + "\"" + body + "\"";

            // 商品金额
            orderInfo += "&total_fee=" + "\"" + price + "\"";

            // 服务器异步通知页面路径
            orderInfo += "&notify_url=" + "\"" + notify_url + "\"";

            // 服务接口名称， 固定值
            orderInfo += "&service=\"mobile.securitypay.pay\"";

            // 支付类型， 固定值
            orderInfo += "&payment_type=\"1\"";

            // 参数编码， 固定值
            orderInfo += "&_input_charset=\"utf-8\"";

            // 设置未付款交易的超时时间
            // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
            // 取值范围：1m～15d。
            // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
            // 该参数数值不接受小数点，如1.5h，可转换为90m。
            orderInfo += "&it_b_pay=" + "\"" + it_b_pay + "\"";

            // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
            // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

            // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//            orderInfo += "&return_url=\"m.alipay.com\"";

            return orderInfo;
        }

        /**
         * sign the order info. 对订单信息进行签名
         *
         * @param content 待签名订单信息
         */
        private String sign(String content) {
            return SignUtils.sign(content, Sharedparms.PayInfo.RSA_PRIVATE);
        }

        /**
         * get the sign type we use. 获取签名方式
         */
        private String getSignType() {
            return "sign_type=\"RSA\"";
        }
    }


}
