package com.example.administrator.learn.Tool;

import android.content.Context;
import android.widget.Toast;

import com.example.administrator.learn.MainActivity;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;

/**
 * Created by Administrator on 2016/12/28.
 */

public class ShareUtils {
    /**
     * 微信分享
     *
     * @param platform  类型
     * @param title
     * @param
     * @param text      imageUrl  图片url
     * @param wechatUrl url仅在微信（包括好友和朋友圈）中使用wechatUrl
     *                  commentText   comment是我对这条分享的评论，仅在人人网和QQ空间使用
     */
    public static void showShare(Context context,String platform, String title,  String text, String imageUrl, String wechatUrl) {
        final OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        if (platform != null) {
            oks.setPlatform(platform);
        }
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(title);
        // text是分享文本，所有平台都需要这个字段
        oks.setText(text);
        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        oks.setImageUrl(imageUrl);
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
//        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用wechatUrl
        oks.setUrl(wechatUrl);

        //启动分享
        oks.show(context);
    }

    /**
     * 新浪微博分享
     *
     * @param text
     * @param imageUrl
     *  isCallback 是否需要告诉后端
     */
    public static void shareSinaWei(final Context context, String text, String imageUrl,boolean iscallback) {
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setText(text);
        sp.setImageUrl(imageUrl);
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toast.makeText(context, "分享取消", Toast.LENGTH_SHORT).show();
            }
        }); // 设置分享事件回调
// 执行图文分享
        weibo.share(sp);
    }

    /**
     * qq分享
     *
     * @param text
     * @param url
     * @param imageUrl
     *  isCallback 是否需要告诉后端
     */
    public static  void shareQQ(final Context context, String text, String url, String imageUrl,boolean iscallback) {
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setText(text);
        sp.setImageUrl(imageUrl);
        sp.setTitleUrl(url);
        Platform weibo = ShareSDK.getPlatform(QQ.NAME);
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toast.makeText(context, "分享取消", Toast.LENGTH_SHORT).show();
            }
        }); // 设置分享事件回调
// 执行图文分享
        weibo.share(sp);
    }

    /**
     * qq空间分享
     *
     * @param text
     * @param url
     * @param imageUrl
     * isCallback 是否需要告诉后端
     */
    public static void shareQZone(final Context context, String text, String url, String imageUrl, String appName,boolean isCallback) {
        QZone.ShareParams sp = new QZone.ShareParams();
        sp.setText(text);
        sp.setImageUrl(imageUrl);
        sp.setTitleUrl(url);
        sp.setSite(appName);
        Platform weibo = ShareSDK.getPlatform(QZone.NAME);
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toast.makeText(context, "分享失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toast.makeText(context, "分享取消", Toast.LENGTH_SHORT).show();
            }
        }); // 设置分享事件回调
// 执行图文分享
        weibo.share(sp);
    }
}
