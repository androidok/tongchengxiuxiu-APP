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
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by Administrator on 2016/12/28.
 */

public class ShareUtils {
    public interface setShareListener {
        /**
         * @param issuccess  分享是否成功
         * @param iscallback 是否需要告诉后台
         */
        void shareSuccess(boolean issuccess, boolean iscallback);
    }
//
//    /**
//     * 微信分享
//     *
//     * @param platform  类型
//     * @param title
//     * @param
//     * @param text      imageUrl  图片url
//     * @param wechatUrl url仅在微信（包括好友和朋友圈）中使用wechatUrl
//     *                  commentText   comment是我对这条分享的评论，仅在人人网和QQ空间使用
//     */
//    public static void showShare(Context context, String platform, String title, String text, String imageUrl, String wechatUrl) {
//        final OnekeyShare oks = new OnekeyShare();
//        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
//        if (platform != null) {
//            oks.setPlatform(platform);
//        }
//        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
//        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
//        oks.setTitle(title);
//        // text是分享文本，所有平台都需要这个字段
//        oks.setText(text);
//        //分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
//        oks.setImageUrl(imageUrl);
//        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
////        oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
//        // url仅在微信（包括好友和朋友圈）中使用wechatUrl
//        oks.setUrl(wechatUrl);
//        //启动分享
//        oks.show(context);
//
//    }

    /**
     * 微信分享最好绕过审核分享，这样造成分享朋友圈的时分享的url不显示，而且需要设置分享类型
     * 微信朋友分享
     *
     * @param text
     * @param imageUrl setShareListener 接口回调出去
     *                 iscallback 是否要告诉后台使用
     */
    public static void shareweixin(final Context context, String title, String text, String imageUrl, String wechatUrl, final boolean iscallback, final setShareListener setShareListener) {
        Wechat.ShareParams wechat = new Wechat.ShareParams();
        wechat.setShareType(Platform.SHARE_WEBPAGE);
        wechat.setText(text);
        wechat.setTitle(title);
        wechat.setImageUrl(imageUrl);
        wechat.setUrl(wechatUrl);
        Platform weixin = ShareSDK.getPlatform(Wechat.NAME);
        weixin.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                UtilTool.ShowToast(context, "分享成功");
                if (setShareListener != null) {
                    setShareListener.shareSuccess(true, iscallback);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                UtilTool.ShowToast(context, "分享失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                UtilTool.ShowToast(context, "分享取消");
            }
        }); // 设置分享事件回调
        if (!weixin.isClientValid()) {
            UtilTool.ShowToast(context, "没有安装了微信");
        }
        weixin.share(wechat);
    }

    /**
     * 微信分享最好绕过审核分享，这样造成分享朋友圈的时分享的url不显示,而且需要设置分享类型
     * 微信朋友圈分享
     *
     * @param context
     * @param title
     * @param text
     * @param imageUrl
     * @param wechatUrl
     * @param setShareListener
     */
    public static void shareWechatMoments(final Context context, String title, String text, String imageUrl, String wechatUrl, final boolean iscallback, final setShareListener setShareListener) {
        WechatMoments.ShareParams wechatMoments = new WechatMoments.ShareParams();
        wechatMoments.setShareType(Platform.SHARE_WEBPAGE);
        wechatMoments.setText(text);
        wechatMoments.setTitle(title);
        wechatMoments.setImageUrl(imageUrl);
        wechatMoments.setUrl(wechatUrl);
        Platform weixinfriead = ShareSDK.getPlatform(WechatMoments.NAME);
        weixinfriead.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                UtilTool.ShowToast(context, "分享成功");
                if (setShareListener != null) {
                    setShareListener.shareSuccess(true, iscallback);
                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                UtilTool.ShowToast(context, "分享失败");
            }

            @Override
            public void onCancel(Platform platform, int i) {
                UtilTool.ShowToast(context, "分享取消");
            }
        }); // 设置分享事件回调
        if (!weixinfriead.isClientValid()) {
            UtilTool.ShowToast(context, "没有安装了微信");
        }
        weixinfriead.share(wechatMoments);
    }

    /**
     * 新浪微博分享
     *
     * @param text
     * @param imageUrl setShareListener 接口回调出去
     */
    public static void shareSinaWei(final Context context, String text, String imageUrl, final boolean iscallback, final setShareListener setShareListener) {
        SinaWeibo.ShareParams sp = new SinaWeibo.ShareParams();
        sp.setText(text);
        sp.setImageUrl(imageUrl);
        Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
                if (setShareListener != null) {
                    setShareListener.shareSuccess(true, iscallback);
                }
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
     * @param imageUrl setShareListener 接口回调出去
     */
    public static void shareQQ(final Context context, String text, String url, String imageUrl, final boolean iscallback, final setShareListener setShareListener) {
        QQ.ShareParams sp = new QQ.ShareParams();
        sp.setText(text);
        sp.setImageUrl(imageUrl);
        sp.setTitleUrl(url);
        Platform weibo = ShareSDK.getPlatform(QQ.NAME);
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast.makeText(context, "分享成功", Toast.LENGTH_SHORT).show();
                if (setShareListener != null) {
                    setShareListener.shareSuccess(true, iscallback);
                }
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
     * @param imageUrl setShareListener 接口回调出去
     */
    public static void shareQZone(final Context context, String text, String url, String imageUrl, String appName, final boolean iscallback, final setShareListener setShareListener) {
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
                if (setShareListener != null) {
                    setShareListener.shareSuccess(true, iscallback);
                }
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
