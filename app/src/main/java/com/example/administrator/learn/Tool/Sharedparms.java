package com.example.administrator.learn.Tool;

/**
 * Created by fanhb on 2016/12/6.--公共参数
 */

public class Sharedparms {
    public static int statusSuccess = 1;//接口回调成功

    /*
    网页
    * */
    public static String WEBVIEW_UIL = "http://jy.leejia.cn";
    /*
    * webview的设置useragent，但是后面需要加入版本号
    * */
    public static String WEBVIEWA_AGENT = "app.Jnoo.com/";
    /*
    * 直播那个黑白页
    * */
    public static String WEBPUSH = "http://jy.leejia.cn/index.php?s=/Live/Index/mylive";

    /**
     * 微信appid和appidsecret
     */
    public static class weixinInfo {
        public static String WEIXIN_APPID = "wx7c35ac6495868a48";
        public static String WINXIN_SECRET = "ec9423ddc4c6ec73395f0da8de91afbd";
        //商户号
        public static String WINXIN_PARTNERID = "1359749402";
        //key
        public static String WEIXIN_KEY = "641e3e03a32cfced53af2bacab970da8";
        //统一下单生成订单的接口
        public static String WEIXIN_API = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    }

    /**
     * 支付宝支付信息
     */
    public static class PayInfo {
        // 商户PID
        public static final String PARTNER = "2088911648469578";
        // 商户私钥，pkcs8格式
        public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAK6aWJB0K6OLsOpq K6cxtO37bWHNbslSblvJh2Eo+ifXWce+fNPZpz09Z0P+6BYgSg8eDW2IDVm9KMkW oO3hIcihotu5krv0+SKISVhGp/OZ7sDG5o/YUgBdb/MfvwIHIPGTAOMNL4jJX2ks 3JuAwBzyX2Y0OXEZh5LZSfmAJyJ5AgMBAAECgYEArL2a/V9RhT5Ay8o6YfF1dTUY pHggMSFSeZDKVT+7LguKkWlOCjH9mULUlZrtdmZ/nrE2y7ScsLnKThgLIw1m43tc ojqBpLYmcJ6dVkSYL3w+A3wxeD4U79qluG7GixtdhyHVcraawtLq5teQ3OtFIeME uoA+LMUVpblgyk/jN7kCQQDVP9qUUVh7jCjjTFe+4HCE+7lG68LeL4o5Wt+/5/zf FhAX686AnWvBNANLzOk6eiIJnpfnworTmpFUyBFV1ycXAkEA0Zsejk3wehWesfRm MQiKTjes8NudgyrpPlUSyb7NPa07pZBUC6UdaYcgOKra1r3Uh/ZIlu0/8yz3GRJ2 ngn87wJADqv3AO9b1Bw/j+vnuZU9iJi9FZkQ7jJ9IxcSf+SZLEwbTVoG+ihaio9m jqeJgGF4yAqmTua+oHJo/1lIgAxufwJBAKqO3r5NDXFKRmfnx3/+wwwCoecbzX/+ Wu0trKwdZkTZwb9nQfx3zwcfvUhfPtOehGJeNZMaWv81h5wPOhY/amECQEt8zKvQ mze685yDK/Cc54uUGEV2gH6TGnqtfG27S2iZ3MXFF756v6lFLK65LR7qDJH4p7GR dLp+i2KLc8H36gM=";
        // 支付宝公钥
        public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    }

    /**
     * 接口
     */
    public static class Http_url {
        public static final String URL_DOMAIN = "http://jy.leejia.cn";//域名
        //上传图片
        public static final String POSTPICTURE = "/index.php?s=/Live/Api/uploadImg/key/jnooo/vcode/9441020bd47843e473c78bb4861639cf/";//上传照片
        //获取用户信息
        public static final String GETPERSONAL = "/index.php?s=/Live/Api/getUserInfo/key/jnooo/vcode/9441020bd47843e473c78bb4861639cf/";
        //开始直播
        public static final String STARTPUSH = "/index.php?s=/Live/Api/saveLiveData/key/jnooo/vcode/9441020bd47843e473c78bb4861639cf/";
        //分享完成告诉后台
        public static final String SHAREURL = "/index.php?s=/Live/Api/shareLive/key/jnooo/vcode/9441020bd47843e473c78bb4861639cf/";
        //结束直播
        public static final String STOPPUSH = "/index.php?s=/Live/Api/overLive/key/jnooo/vcode/9441020bd47843e473c78bb4861639cf";
    }

    /*
    * sp保存信息
    * */
    public static class spinfo {
        public static final String APPINFO = "appinfo";
        public static final String UID = "uid";
        public static final String LIVE_RTMP = "live_rtmp";//推流地址
        public static final String NICE_NAME = "nice_name";//用户昵称
        public static final String USER_ACCOUNT = "user_account";//用户登入账号
        public static final String HEADER_URL = "headerurl";//头像url
        public static final String SHARE_URL = "share_url";//分享地址
        public static final String IMAGE_URL = "image_url";//接口返回 封面照片地址
        public static final String LIVEID = "liveid";


    }


}
