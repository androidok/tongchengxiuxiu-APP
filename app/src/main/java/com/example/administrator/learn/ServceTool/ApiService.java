package com.example.administrator.learn.ServceTool;

import android.content.Context;

import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.administrator.learn.CameraActivity;
import com.example.administrator.learn.Model.PersonalInfo;
import com.example.administrator.learn.Model.putPictureInfo;
import com.example.administrator.learn.Tool.SPUtils;
import com.example.administrator.learn.Tool.Sharedparms;
import com.example.administrator.learn.Tool.UtilTool;
import com.google.gson.reflect.TypeToken;
import com.rxandroidnetworking.RxAndroidNetworking;

import java.io.File;
import java.util.HashMap;

import static android.R.attr.path;

/**
 * Created by Administrator on 2016/12/28.
 */

public class ApiService {
    public interface ParsedRequestListener<T> {
        void onResponseResult(T t);
        void _OnError(String errormessage);
    }

    /**获取个人信息
     * @param uid
     * @param parsedRequestListener
     */
    public static void getPersonnalInfo( String uid, final ParsedRequestListener<PersonalInfo> parsedRequestListener) {
        HashMap<String, String> parametMap = new HashMap<>();
        parametMap.put("uid", uid);
        RxAndroidNetworking.post(Sharedparms.Http_url.URL_DOMAIN + Sharedparms.Http_url.GETPERSONAL)
                .addBodyParameter(parametMap)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsParsed(new TypeToken<PersonalInfo>() {
                }, new com.androidnetworking.interfaces.ParsedRequestListener<PersonalInfo>() {
                    @Override
                    public void onResponse(PersonalInfo response) {
//
                        parsedRequestListener.onResponseResult(response);
                    }

                    @Override
                    public void onError(ANError anError) {
                    }
                });
    }

    /**
     * 保存封面照片
     * @param path
     * @param parsedRequestListener
     */
    public static void putPicture(String path, final  ParsedRequestListener<putPictureInfo> parsedRequestListener){
        RxAndroidNetworking.upload(Sharedparms.Http_url.URL_DOMAIN + Sharedparms.Http_url.POSTPICTURE)
                .addMultipartFile("file", new File(path))
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsParsed(new TypeToken<putPictureInfo>() {
                }, new com.androidnetworking.interfaces.ParsedRequestListener<putPictureInfo>() {
                    @Override
                    public void onResponse(putPictureInfo response) {
                        parsedRequestListener.onResponseResult(response);
//
                    }

                    @Override
                    public void onError(ANError anError) {
                        parsedRequestListener._OnError(anError.getMessage());

                    }
                });
    }

    /**开始直播
     * @param title
     * @param uid
     * @param liveimage
     * @param parsedRequestListener
     */
    public static void StartPush(String title,String uid,String liveimage,final ParsedRequestListener parsedRequestListener){
        HashMap<String,String> bodyMap=new HashMap<>();
        bodyMap.put("uid",uid);
        bodyMap.put("liveTitle",title);
        bodyMap.put("liveImg",liveimage);
        RxAndroidNetworking.post(Sharedparms.Http_url.STARTPUSH)
                .addBodyParameter(bodyMap)
                .setPriority(Priority.IMMEDIATE)
                .build();

    }
}
