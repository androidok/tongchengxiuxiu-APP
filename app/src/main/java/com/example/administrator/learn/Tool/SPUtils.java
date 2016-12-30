package com.example.administrator.learn.Tool;

import android.content.Context;

/**
 * Created by Administrator on 2016/12/28.
 */

public class SPUtils {

    /**得到uid
     * @param context
     * @return
     */
    public static String getUid(Context context){
        return context.getSharedPreferences(Sharedparms.spinfo.APPINFO,0).getString(Sharedparms.spinfo.UID,null);
    }

    /**保存uid
     * @param context
     * @param uid
     */
    public static void PutUid(Context context,String uid){
        context.getSharedPreferences(Sharedparms.spinfo.APPINFO,0).edit().putString(Sharedparms.spinfo.UID,uid).commit();
    }
    /**得到推流地址
     * @param context
     * @return
     */
    public static String getlivertmpUrl(Context context){
        return context.getSharedPreferences(Sharedparms.spinfo.APPINFO,0).getString(Sharedparms.spinfo.LIVE_RTMP,null);
    }

    /**保存uid
     * @param context
     * @param livertmpUrl
     */
    public static void PutlivertmpUrl(Context context,String livertmpUrl){
        context.getSharedPreferences(Sharedparms.spinfo.APPINFO,0).edit().putString(Sharedparms.spinfo.LIVE_RTMP,livertmpUrl).commit();
    }
    /**得到nicename
     * @param context
     * @return
     */
    public static String getNiceName(Context context){
        return context.getSharedPreferences(Sharedparms.spinfo.APPINFO,0).getString(Sharedparms.spinfo.NICE_NAME,null);
    }

    /**保存nicename
     * @param context
     * @param nicename
     */
    public static void PutNiceName(Context context,String nicename){
        context.getSharedPreferences(Sharedparms.spinfo.APPINFO,0).edit().putString(Sharedparms.spinfo.NICE_NAME,nicename).commit();
    }
    /**得到uid
     * @param context
     * @return
     */
    public static String getUserAccount(Context context){
        return context.getSharedPreferences(Sharedparms.spinfo.APPINFO,0).getString(Sharedparms.spinfo.USER_ACCOUNT,null);
    }

    /**保存UserAccount
     * @param context
     * @param UserAccount
     */
    public static void PutUserAccount(Context context,String UserAccount){
        context.getSharedPreferences(Sharedparms.spinfo.APPINFO,0).edit().putString(Sharedparms.spinfo.USER_ACCOUNT,UserAccount).commit();
    }
    /**得到头像地址
     * @param context
     * @return
     */
    public static String getheader_url(Context context){
        return context.getSharedPreferences(Sharedparms.spinfo.APPINFO,0).getString(Sharedparms.spinfo.HEADER_URL,null);
    }

    /**保存头像地址
     * @param context
     * @param header_url
     */
    public static void Putheader_url(Context context,String header_url){
        context.getSharedPreferences(Sharedparms.spinfo.APPINFO,0).edit().putString(Sharedparms.spinfo.HEADER_URL,header_url).commit();
    }
    /**得到分享地址
     * @param context
     * @return
     */
    public static String getshare_url(Context context){
        return context.getSharedPreferences(Sharedparms.spinfo.APPINFO,0).getString(Sharedparms.spinfo.SHARE_URL,null);
    }

    /**保存分享地址
     * @param context
     * @param share_url
     */
    public static void Putshare_url(Context context,String share_url){
        context.getSharedPreferences(Sharedparms.spinfo.APPINFO,0).edit().putString(Sharedparms.spinfo.SHARE_URL,share_url).commit();
    }
    /**得到封面照片
     * @param context
     * @return
     */
    public static String getimage_url(Context context){
        return context.getSharedPreferences(Sharedparms.spinfo.APPINFO,0).getString(Sharedparms.spinfo.IMAGE_URL,null);
    }

    /**保存封面照片
     * @param context
     * @param image_url
     */
    public static void Putimage_url(Context context,String image_url){
        context.getSharedPreferences(Sharedparms.spinfo.APPINFO,0).edit().putString(Sharedparms.spinfo.IMAGE_URL,image_url).commit();
    }
    /**得到liveid
     * @param context
     * @return
     */
    public static String getliveid(Context context){
        return context.getSharedPreferences(Sharedparms.spinfo.APPINFO,0).getString(Sharedparms.spinfo.LIVEID,null);
    }

    /**保存liveid
     * @param context
     * @param liveid
     */
    public static void Putliveid(Context context,String liveid){
        context.getSharedPreferences(Sharedparms.spinfo.APPINFO,0).edit().putString(Sharedparms.spinfo.LIVEID,liveid).commit();
    }


}
