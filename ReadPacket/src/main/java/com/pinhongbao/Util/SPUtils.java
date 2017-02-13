package com.pinhongbao.Util;

import android.content.Context;

/**
 * Created by Administrator on 2016/12/28.
 */

public class SPUtils {

    /**得到id
     * @param context
     * @return
     */
    public static String getUid(Context context){
        return context.getSharedPreferences(commonParme.spInfo.sp,0).getString(commonParme.spInfo.ID,null);
    }

    /**保存id
     * @param context
     * @param id
     */
    public static void PutUid(Context context,String id){
        context.getSharedPreferences(commonParme.spInfo.sp,0).edit().putString(commonParme.spInfo.ID,id).commit();
    }
    /**得到icon
     * @param context
     * @return
     */
    public static String getIcon(Context context){
        return context.getSharedPreferences(commonParme.spInfo.sp,0).getString(commonParme.spInfo.Icon,null);
    }

    /**保存icon
     * @param context
     * @param icon
     */
    public static void PutIcon(Context context,String icon){
        context.getSharedPreferences(commonParme.spInfo.sp,0).edit().putString(commonParme.spInfo.Icon,icon).commit();
    }
    /**得到icon
     * @param context
     * @return
     */
    public static String getnicname(Context context){
        return context.getSharedPreferences(commonParme.spInfo.sp,0).getString(commonParme.spInfo.Nicname,null);
    }

    /**保存icon
     * @param context
     * @param nicname
     */
    public static void Putnicname(Context context,String nicname){
        context.getSharedPreferences(commonParme.spInfo.sp,0).edit().putString(commonParme.spInfo.Nicname,nicname).commit();
    }
    /**得到余额
     * @param context
     * @return
     */
    public static String getBalance(Context context){
        return context.getSharedPreferences(commonParme.spInfo.sp,0).getString(commonParme.spInfo.Balance,null);
    }

    /**保存余额
     * @param context
     * @param
     */
    public static void PutBalance(Context context,String Balance){
        context.getSharedPreferences(commonParme.spInfo.sp,0).edit().putString(commonParme.spInfo.Balance,Balance).commit();
    }
}
