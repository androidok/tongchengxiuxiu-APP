package com.example.administrator.learn;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.example.administrator.learn.Tool.Sharedparms;
import com.example.administrator.learn.Tool.SignUtils;
import com.example.administrator.learn.Tool.UtilTool;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

import static com.example.administrator.learn.Tool.Sharedparms.PayInfo.RSA_PRIVATE;


public class MainActivity extends Activity {

    private WebView mwebview;
    private static final int MSG_WEIXIN_SCUSSE = 1;
    private static final int MSG_WEIXIN_ERROR = 2;
    private static int SDK_PAY_FLAG=3;
    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mwebview = (WebView) findViewById(R.id.webView);

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
        mwebview.addJavascriptInterface(new javaScriptObjcet(), "android");
        mwebview.loadUrl(Sharedparms.WEBVIEW_UIL);
        mwebview.setWebChromeClient(new WebChromeClient());
        /*
        *注册app到微信
        * */
        api = WXAPIFactory.createWXAPI(this, Sharedparms.weixinInfo.WEIXIN_APPID);
        api.registerApp(Sharedparms.weixinInfo.WEIXIN_APPID);
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
                    Platform platform = (cn.sharesdk.framework.Platform) msg.obj;
                    PlatformDb db = platform.getDb();
                try {
                    JSONObject jsonObject=new JSONObject(db.exportData());
                    mwebview.loadUrl("javascript:success("+jsonObject+")");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    break;
                case MSG_WEIXIN_ERROR:
                    Toast.makeText(MainActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                    try {
                        JSONObject jsonObject=new JSONObject();
                        jsonObject.put("login_error","登入失败");
                        /*
                        * 获取到信息返回给js
                        * */
                        mwebview.loadUrl("javascript:error("+jsonObject+")");
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
        /*
        * 微信支付
        * */
        @JavascriptInterface
        public void  WXplay(){
//            String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
//            Toast.makeText(MainActivity.this, "获取订单中...", Toast.LENGTH_SHORT).show();
//            try{
//                byte[] buf = .httpGet(url);
//                if (buf != null && buf.length > 0) {
//                    String content = new String(buf);
//                    Log.e("get server pay params:",content);
//                    JSONObject json = new JSONObject(content);
//                    if(null != json && !json.has("retcode") ){
//                        PayReq req = new PayReq();
//                        //req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
//                        req.appId			= json.getString("appid");
//                        req.partnerId		= json.getString("partnerid");
//                        req.prepayId		= json.getString("prepayid");
//                        req.nonceStr		= json.getString("noncestr");
//                        req.timeStamp		= json.getString("timestamp");
//                        req.packageValue	= json.getString("package");
//                        req.sign			= json.getString("sign");
//                        req.extData			= "app data"; // optional
//                        Toast.makeText(PayActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
//                        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
//                        api.sendReq(req);
//                    }else{
//                        Log.d("PAY_GET", "返回错误"+json.getString("retmsg"));
//                        Toast.makeText(PayActivity.this, "返回错误"+json.getString("retmsg"), Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Log.d("PAY_GET", "服务器请求错误");
//                    Toast.makeText(PayActivity.this, "服务器请求错误", Toast.LENGTH_SHORT).show();
//                }
//            }catch(Exception e){
//                Log.e("PAY_GET", "异常："+e.getMessage());
//                Toast.makeText(PayActivity.this, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
//            }
        }
        @JavascriptInterface
        public void Zhipay(final  String payJsInfo){
            try {
                JSONObject jsonObject=new JSONObject(payJsInfo);
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
                Toast.makeText(MainActivity.this,payInfo,Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
        Handler Mhandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.what==SDK_PAY_FLAG){
                    String result = (String) msg.obj;
                        String substring = result.split(";")[0].split("=")[1];
                        String resultStatus = substring.substring(1, substring.length() - 1);
                        if ("9000".equalsIgnoreCase(resultStatus)){
                            mwebview.loadUrl("javascript:success("+resultStatus+")");
                            Toast.makeText(MainActivity.this,"支付成功",Toast.LENGTH_SHORT).show();
                        }else {
                            mwebview.loadUrl("javascript:error("+resultStatus+")");
                        }
//                        Toast.makeText(MainActivity.this,""+resultStatus,Toast.LENGTH_SHORT).show();
                }

            }
        };

        /**
         * @param subject 描述
         * @param body 详情
         * @param price 价格
         * @param seller_id 商家支付宝账号
         * @param out_trade_no 唯一订单号
         * @param it_b_pay 设置未付款交易的超时时间
         *                 notify_url    服务器异步通知页面路径
         * @return
         */
        private String getOrderInfo(String subject, String body, String price,String seller_id,String out_trade_no,String it_b_pay,String notify_url) {

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
            orderInfo += "&it_b_pay="+"\""+it_b_pay+"\"";

            // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
            // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

            // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
//            orderInfo += "&return_url=\"m.alipay.com\"";

            return orderInfo;
        }
        /**
         * sign the order info. 对订单信息进行签名
         *
         * @param content
         *            待签名订单信息
         */
        private String sign(String content) {
            return SignUtils.sign(content,Sharedparms.PayInfo.RSA_PRIVATE);
        }

        /**
         * get the sign type we use. 获取签名方式
         *
         */
        private String getSignType() {
            return "sign_type=\"RSA\"";
        }
    }

}
