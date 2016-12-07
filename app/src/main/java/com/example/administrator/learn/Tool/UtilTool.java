package com.example.administrator.learn.Tool;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by fanhengbiao on 16-9-27.工具类
 */

public class UtilTool {

    /**
     * MD加密
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

    /**关闭键盘
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
     * @param str
     * @return
     */
    public boolean isNumber(String str)
    {
        java.util.regex.Pattern pattern=java.util.regex.Pattern.compile("[0-9]*");
        java.util.regex.Matcher match=pattern.matcher(str);
        if(match.matches()==false)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
}
