package com.pinhongbao.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.StringReader;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fanhengbiao on 16-9-27.工具类
 */

public class UtilTool {

    /**
     * MD加密
     *
     * @param plainText
     * @return
     */
    public String Md5(String plainText) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString().toUpperCase();// 32位的加密（转成大写）
            buf.toString().substring(8, 24);// 16位的加密
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 检测是否安装这个应用
     *
     * @param context
     * @param pagename
     * @return
     */
    //检测系统中是否已经安装了adobe flash player插件，插件的packageName是com.adobe.flashplayer：
    private boolean check(Context context, String pagename) {
        PackageManager pm = context.getPackageManager();
        List<PackageInfo> infoList = pm
                .getInstalledPackages(PackageManager.GET_SERVICES);
        for (PackageInfo info : infoList) {
            if (pagename.equals(info.packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 关闭键盘
     *
     * @param activity
     */
    public static void hideSoftInput(Activity activity) {
        if (activity.getCurrentFocus() != null)
            ((InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(activity.getCurrentFocus()
                            .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        String packageName = context.getPackageName();
        try {
            PackageInfo info = manager.getPackageInfo(packageName, 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "1.0";
        }
    }

    /**
     * 验证是否手机号
     *
     * @param mobiles
     * @return
     */
    public boolean isMobileNO(String mobiles) {
        String expression = "((^(13|15|18)[0-9]{9}$)|(^0[1,2]{1}\\d{1}-?\\d{8}$)|(^0[3-9] {1}\\d{2}-?\\d{7,8}$)|(^0[1,2]{1}\\d{1}-?\\d{8}-(\\d{1,4})$)|(^0[3-9]{1}\\d{2}-? \\d{7,8}-(\\d{1,4})$))";
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(mobiles);
        Log.d("wxl", matcher.matches() + "");
        return matcher.matches();
    }

    /**
     * 正则表达式数字验证
     *
     * @param str
     * @return
     */
    public boolean isNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher match = pattern.matcher(str);
        if (match.matches() == false) {
            return false;
        } else {
            return true;
        }
    }

//    /**
//     * 获取ip
//     * @return
//     */
//    public static String getIpAddress() {
//        String ip = "127.0.0.1";
//        try {
//            for (Enumeration<NetworkInterface> en = NetworkInterface
//                    .getNetworkInterfaces(); en.hasMoreElements();) {
//                NetworkInterface intf = en.nextElement();
//                for (Enumeration<InetAddress> enumIpAddr = intf
//                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
//                    InetAddress inetAddress = enumIpAddr.nextElement();
//                    if (!inetAddress.isLoopbackAddress()
//                            && InetAddressUtils.isIPv4Address(inetAddress
//                            .getHostAddress())) // 这里做了一步IPv4的判定
//                    {
//                        ip = inetAddress.getHostAddress().toString();
//                        return ip;
//                    }
//                }
//            }
//        } catch (SocketException e) {
//            Log.i("getIpAddress--->", ""+e.getLocalizedMessage());
//        }
//        return ip;
//    }

//    /**
//     * 微信支付签名
//     * @param params
//     * @return
//     */
//    public static String genPackageSign(List<NameValuePair> params) {
//        StringBuilder sb = new StringBuilder();
//
//        for (int i = 0; i < params.size(); i++) {
//            sb.append(params.get(i).getName());
//            sb.append('=');
//            sb.append(params.get(i).getValue());
//            sb.append('&');
//        }
//        sb.append("key=");
//        sb.append(Sharedparms.weixinInfo.WEIXIN_KEY);
//
//        String packageSign = MD5.getMessageDigest(sb.toString().getBytes(Charset.forName("utf-8"))).toUpperCase();
//
//        return packageSign;
//    }

    /**
     * 获取随机数
     *
     * @return
     */
    public static String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

//    /**
//     * 微信支付---数据转换为xml
//     * @param params
//     * @return
//     */
//    public static String toXml(List<NameValuePair> params) {
//        StringBuilder sb = new StringBuilder();
//        sb.append("<xml>");
//        for (int i = 0; i < params.size(); i++) {
//            sb.append("<" + params.get(i).getName() + ">");
//
//            sb.append(params.get(i).getValue());
//            sb.append("</" + params.get(i).getName() + ">");
//        }
//        sb.append("</xml>");
//
//        Log.e("orion", sb.toString());
//        return sb.toString();
//    }

    /**
     * string转map
     *
     * @param content
     * @return
     */
    public static Map<String, String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {

                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false) {
                            // 实例化student对象
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
        }
        return null;

    }

    /**
     * 时间戳
     *
     * @return
     */
    public static String genTimeStamp() {
        return System.currentTimeMillis() / 1000 + "";
    }

    /**
     * 获取随机数
     *
     * @return
     */
    public static String getRanDomNum() {
        Random random = new Random();
        return String.valueOf(random.nextInt(30));

    }

    /**
     * toast
     *
     * @param context
     * @param str
     */
    public static void ShowToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

    }

    /**
     * 计算时间差   传入的是毫秒数
     *
     * @param starTime      开始时间
     * @param endTime       结束时间
     * @param ==1----天，时，分。 ==2----时
     * @return 返回时间差
     */
    public static String getTimeDifference(String starTime, String endTime) {
        String timeString = "";

        long longend = Long.parseLong(endTime);
        long longstart = Long.parseLong(starTime);
        if (longend <= longstart || (longend - longstart) < 1000) {
            return "0小时0分0秒";
        }

        long diff = longend - longstart;

        long day = diff / (24 * 60 * 60 * 1000);
        long hour = (diff / (60 * 60 * 1000) - day * 24);
        long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        long hour1 = diff / (60 * 60 * 1000);
        timeString = hour + "小时" + min + "分" + s + "秒";

        return timeString;

    }

    /**
     * 时间转换时间格式
     *
     * @param time
     * @return
     */
    public static String timedate(String time) {
        SimpleDateFormat sdr = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        @SuppressWarnings("unused")
        long lcc = Long.valueOf(time);
        int i = Integer.parseInt(time);
        String times = sdr.format(new Date(i * 1000L));
        return times;

    }

    public static String getTodayDateTimes() {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日",
                Locale.getDefault());
        return format.format(new Date());
    }

    public static String floatzhu(float s) {
        DecimalFormat decimalFormat = new DecimalFormat("0.0");//构造方法的字符格式这里如果小数不足2位,会以0补足.//保留一位小数
        String p = decimalFormat.format(s);
        return p;
    }

    /**
     * 对话框提示
     */
    public static AlertDialog.Builder creatDialog(final Activity activity, String Title, String message, String btnstr, final SetonListener<DialogInterface> setonListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle(Title);
        builder.setMessage(message);
        builder.setPositiveButton(btnstr, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (setonListener != null) {
                    setonListener.setonlistener(dialog, which);
                }
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder;

    }

    public interface SetonListener<T> {
        void setonlistener(T t, int i);
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
    public static void WXPay(Context context, String prepay_id, String noncestr, String sign,String timeStamp) {
        try {
            if (!TextUtils.isEmpty(prepay_id)) {
                PayReq req = new PayReq();
  /*
        *注册app到微信
        * */
                IWXAPI api = WXAPIFactory.createWXAPI(context, commonParme.weixinInfo.WXAppId);
                api.registerApp(commonParme.weixinInfo.WXAppId);
                req.appId = commonParme.weixinInfo.WXAppId;
                req.partnerId = commonParme.weixinInfo.WXMerchant;
                req.prepayId = prepay_id;
                req.nonceStr = noncestr;
                req.timeStamp = timeStamp;
                req.packageValue = "Sign=WXPay";
                List<NameValuePair> signParams = new LinkedList<NameValuePair>();
                signParams.add(new BasicNameValuePair("appid", req.appId));
                signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
                signParams.add(new BasicNameValuePair("package", req.packageValue));
                signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
                signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
                signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
//                req.sign = UtilTool.genPackageSign(signParams);
                req.sign = sign;
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                boolean b = api.sendReq(req);
                Log.d("微信支付", "b" + b);
            } else {
                Log.e("微信支付", "服务器请求错误");

            }
        } catch (Exception e) {
            Log.e("微信支付", "异常：" + e.getMessage());

        }
    }

    /**
     * 微信支付签名
     *
     * @param params
     * @return
     */
    public static String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(commonParme.weixinInfo.WINXIN_SECRET);

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes(Charset.forName("utf-8"))).toUpperCase();

        return packageSign;
    }
}
