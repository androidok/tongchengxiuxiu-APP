package com.pinhongbao.serviceTool;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.pinhongbao.Model.homeInfo;
import com.pinhongbao.Util.commonParme;
import com.rxandroidnetworking.RxAndroidNetworking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2017/2/6.
 */

public class ApiService {
    public interface ParsedRequestListener<T> {
        void onResponseResult(T t);

        void _OnError(String errormessage);
    }
    /**
     * 获取首页信息
     *
     * @param type  1未参与2已参与
     * @param parsedRequestListener
     * sort   11时间12人数13金额 21进行中22未拆开23已拆开，默认11

     */
    public static void gethomeInfo(final Context context,String type, final String sort, int page, final ParsedRequestListener<List<homeInfo>> parsedRequestListener) {
        HashMap<String, String> parametMap = new HashMap<>();
        parametMap.put("type", type);
        parametMap.put("sort", sort);
        parametMap.put("page", page+"");
        RxAndroidNetworking.post(commonParme.apiservice.URL + commonParme.apiservice.url_home)
                .addBodyParameter(parametMap)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("获取信息",response.toString());
                        try {
                            if (TextUtils.isEmpty(response.toString())){
                                return;
                            }
                            JSONArray data = response.getJSONArray("data");
                            List<homeInfo> homeInfoList = JSON.parseArray(data.toString(), homeInfo.class);
                            parsedRequestListener.onResponseResult(homeInfoList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
//                .getAsParsed(new TypeToken<String>() {
//                }
//                        , new com.androidnetworking.interfaces.ParsedRequestListener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.e("获取信息",response.toString());
//                        try {
//                            if (TextUtils.isEmpty(response.toString())){
//                                return;
//                            }
//                            JSONObject jsonObject=new JSONObject(response);
//                            JSONArray data = jsonObject.getJSONArray("data");
//                            UtilTool.ShowToast(context,"111111111111111"+data.toString());
//                            List<homeInfo> homeInfoList = JSON.parseArray(data.toString(), homeInfo.class);
//                            parsedRequestListener.onResponseResult(homeInfoList);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
////
//                    }
//
//                    @Override
//                    public void onError(ANError anError) {
//                        Log.d("获取"+sort+"信息",anError.toString());
//                        UtilTool.ShowToast(context,"222"+anError.getMessage());
//                    }
//                });
    }
}
