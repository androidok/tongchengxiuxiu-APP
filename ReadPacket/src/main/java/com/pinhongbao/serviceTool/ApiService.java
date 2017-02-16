package com.pinhongbao.serviceTool;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.pinhongbao.Model.LoginInfo;
import com.pinhongbao.Model.homeInfo;
import com.pinhongbao.Model.lockListInfo;
import com.pinhongbao.Util.UtilTool;
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
    public static void gethomeInfo(final Context context,String uid,String type, final String sort, int page, final ParsedRequestListener<List<homeInfo>> parsedRequestListener) {
        HashMap<String, String> parametMap = new HashMap<>();
        parametMap.put("type", type);
        parametMap.put("sort", sort);
        parametMap.put("page", page+"");
        if (type.equalsIgnoreCase("2") ){
            parametMap.put("uid",uid);
        }
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
    }

    /**注册
     * @param
     * @param Map
     * @param parsedRequestListener
     */
    public static void weixinLogin( HashMap<String ,String> Map, final ParsedRequestListener<String> parsedRequestListener){
        RxAndroidNetworking.post(commonParme.apiservice.URL + commonParme.apiservice.url_Register)
                .addBodyParameter(Map)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("注册信息",response.toString());
                            if (TextUtils.isEmpty(response.toString())){
                                return;
                            }
                            parsedRequestListener.onResponseResult(response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
    /**获取验证码
     * @param
     * @param
     * @param parsedRequestListener
     */
    public static void getcode(String mobile , final ParsedRequestListener<String> parsedRequestListener){
        HashMap<String ,String> Map=new HashMap<>();
        Map.put("mob",mobile);
        RxAndroidNetworking.post(commonParme.apiservice.URL + commonParme.apiservice.url_getcode)
                .addBodyParameter(Map)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("获取验证码",response.toString());
                        if (TextUtils.isEmpty(response.toString())){
                            return;
                        }
                        parsedRequestListener.onResponseResult(response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        parsedRequestListener.onResponseResult(anError.toString());
                    }
                });
    }
    /**绑定手机号
     * @param context
     * @param
     * @param parsedRequestListener
     */
    public static void login_weixin(final Context context,String mobile ,String uid,String password,String code, final ParsedRequestListener<String> parsedRequestListener){
        HashMap<String ,String> Map=new HashMap<>();
        Map.put("uid",uid);
        Map.put("mobile",mobile);
        Map.put("password",password);
        Map.put("code",code);
        RxAndroidNetworking.post(commonParme.apiservice.URL + commonParme.apiservice.url_bind_login)
                .addBodyParameter(Map)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("微信登录",response.toString());
                        if (TextUtils.isEmpty(response.toString())){
                            return;
                        }
                        parsedRequestListener.onResponseResult(response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        parsedRequestListener.onResponseResult(anError.toString());
                    }
                });
    }
    /**重置密码
     * @param
     * @param
     * @param parsedRequestListener
     */
    public static void ResetPassword(String mobile ,String password,String code, final ParsedRequestListener<String> parsedRequestListener){
        HashMap<String ,String> Map=new HashMap<>();
        Map.put("mobile",mobile);
        Map.put("password",password);
        Map.put("code",code);
        RxAndroidNetworking.post(commonParme.apiservice.URL + commonParme.apiservice.url_ResetPassword)
                .addBodyParameter(Map)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("密码重置",response.toString());
                        if (TextUtils.isEmpty(response.toString())){
                            return;
                        }
                        parsedRequestListener.onResponseResult(response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        parsedRequestListener.onResponseResult(anError.toString());
                    }
                });
    }
    /**手气榜
     * @param
     * @param
     * @param parsedRequestListener
     */
    public static void getLockListInfo(final  Context context, final ParsedRequestListener<List<lockListInfo>> parsedRequestListener){
        RxAndroidNetworking.post(commonParme.apiservice.URL + commonParme.apiservice.url_shouqi)
                .setPriority(Priority.IMMEDIATE)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("手气榜",response.toString());
                        if (TextUtils.isEmpty(response.toString())){
                            return;
                        }
                        try {
                            if (response.getString("code").equalsIgnoreCase(commonParme.API_SERVICE_SUCCESSFUL))
                            {
                                if (!response.isNull("data")){
                                    JSONArray data = response.getJSONArray("data");
                                    List<lockListInfo> lockListInfos = JSON.parseArray(data.toString(), lockListInfo.class);
                                    parsedRequestListener.onResponseResult(lockListInfos);
                                }

                            }else {
                                UtilTool.ShowToast(context,response.getString("msg"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        parsedRequestListener._OnError(anError.toString());
                    }
                });
    }
    /**获取推广明细
     * @param
     * @param
     * @param parsedRequestListener
     */
    public static void getPromotedetailInfo(final  Context context,String uid, final ParsedRequestListener<String> parsedRequestListener){
       HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("uid",uid);
        RxAndroidNetworking.post(commonParme.apiservice.URL + commonParme.apiservice.url_promote_detail)
                .setPriority(Priority.IMMEDIATE)
                .addBodyParameter(hashMap)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("获取推广明细",response.toString());
                        if (TextUtils.isEmpty(response.toString())){
                            UtilTool.ShowToast(context,"获取失败");
                            return;
                        }
                        parsedRequestListener.onResponseResult(response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        parsedRequestListener._OnError(anError.toString());
                    }
                });
    }
    /**获取下线明细
     * @param
     * @param
     * @param parsedRequestListener
     */
    public static void getApprenticeInfo(final  Context context,String uid, final ParsedRequestListener<String> parsedRequestListener){
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("uid",uid);
        RxAndroidNetworking.post(commonParme.apiservice.URL + commonParme.apiservice.url_apprentice_detail)
                .setPriority(Priority.IMMEDIATE)
                .addBodyParameter(hashMap)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("获取下线明细",response.toString());
                        if (TextUtils.isEmpty(response.toString())){
                            UtilTool.ShowToast(context,"获取失败");
                            return;
                        }
                        parsedRequestListener.onResponseResult(response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        parsedRequestListener._OnError(anError.toString());
                    }
                });
    }
    /**获取红包记录
     * @param
     * @param
     * @param parsedRequestListener
     */
    public static void getRedPackcoredInfo(final  Context context,String uid,String type,String page, final ParsedRequestListener<String> parsedRequestListener){
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("uid",uid);
        hashMap.put("type",type);
        hashMap.put("page",page);
        RxAndroidNetworking.post(commonParme.apiservice.URL + commonParme.apiservice.url_redpackrecod_detail)
                .setPriority(Priority.IMMEDIATE)
                .addBodyParameter(hashMap)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("获取红包记录",response.toString());
                        if (TextUtils.isEmpty(response.toString())){
                            UtilTool.ShowToast(context,"获取失败");
                            return;
                        }
                        parsedRequestListener.onResponseResult(response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        parsedRequestListener._OnError(anError.toString());
                    }
                });
    }
    /**获取提现记录
     * @param
     * @param
     * @param parsedRequestListener
     */
    public static void getwithdrawalInfo(final  Context context,String uid, final ParsedRequestListener<String> parsedRequestListener){
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("uid",uid);
        RxAndroidNetworking.post(commonParme.apiservice.URL + commonParme.apiservice.url_wihtdrawal_detail)
                .setPriority(Priority.IMMEDIATE)
                .addBodyParameter(hashMap)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("获取提现记录",response.toString());
                        if (TextUtils.isEmpty(response.toString())){
                            UtilTool.ShowToast(context,"获取失败");
                            return;
                        }
                        parsedRequestListener.onResponseResult(response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        parsedRequestListener._OnError(anError.toString());
                    }
                });
    }
    /**获取红包信息
     * @param
     * @param
     * @param parsedRequestListener
     */
    public static void getRedPackInfo(final  Context context,String uid,String rid, final ParsedRequestListener<String> parsedRequestListener){
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("uid",uid);
        hashMap.put("rid",rid);
        RxAndroidNetworking.post(commonParme.apiservice.URL + commonParme.apiservice.url_redpack_detail)
                .setPriority(Priority.IMMEDIATE)
                .addBodyParameter(hashMap)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("获取红包信息",response.toString());
                        if (TextUtils.isEmpty(response.toString())){
                            UtilTool.ShowToast(context,"获取失败");
                            return;
                        }
                        parsedRequestListener.onResponseResult(response.toString());
                    }

                    @Override
                    public void onError(ANError anError) {
                        parsedRequestListener._OnError(anError.toString());
                    }
                });
    }
    /**手机登录
     * @param
     * @param
     * @param parsedRequestListener
     */
    public static void Login_phone(final  Context context,String username,String password, final ParsedRequestListener<LoginInfo> parsedRequestListener){
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("username",username);
        hashMap.put("password",password);
        RxAndroidNetworking.post(commonParme.apiservice.URL + commonParme.apiservice.url_phonelogin_detail)
                .setPriority(Priority.IMMEDIATE)
                .addBodyParameter(hashMap)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("手机号码登录",response.toString());
                        if (TextUtils.isEmpty(response.toString())){
                            UtilTool.ShowToast(context,"获取失败");
                            return;
                        }
                        try {
                            if (commonParme.API_SERVICE_SUCCESSFUL.equalsIgnoreCase(response.getString("code"))){
                                JSONObject uinfo = response.getJSONObject("uinfo");
                                LoginInfo loginInfo = JSON.parseObject(uinfo.toString(), LoginInfo.class);
                                parsedRequestListener.onResponseResult(loginInfo);
                            }else {
                                UtilTool.ShowToast(context,response.getString("msg"));
                            }
                        } catch (JSONException e) {


                        }
//
                    }

                    @Override
                    public void onError(ANError anError) {
                        parsedRequestListener._OnError(anError.toString());
                    }
                });
    }
    /**发起支付
     * @param
     * @param
     * @param parsedRequestListener
     */
    public static void send_task(final  Context context,String uid,String num,String pay,String title,String payStyle, final ParsedRequestListener<String> parsedRequestListener){
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("uid",uid);
        hashMap.put("num",num);
        hashMap.put("pay",pay);
        hashMap.put("title",title);
        hashMap.put("payStyle",payStyle);
        RxAndroidNetworking.post(commonParme.apiservice.URL + commonParme.apiservice.url_task_detail)
                .setPriority(Priority.IMMEDIATE)
                .addBodyParameter(hashMap)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("发起任务",response.toString());
                        if (TextUtils.isEmpty(response.toString())){
                            UtilTool.ShowToast(context,"获取失败");
                            return;
                        }
                        parsedRequestListener.onResponseResult(response.toString());
//
                    }

                    @Override
                    public void onError(ANError anError) {
                        parsedRequestListener._OnError(anError.toString());
                    }
                });
    }
    /**得到红包id
     * @param
     * @param
     * @param parsedRequestListener
     */
    public static void check_redpack(final  Context context,String cid, final ParsedRequestListener<String> parsedRequestListener){
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("cid",cid);
        RxAndroidNetworking.post(commonParme.apiservice.URL + commonParme.apiservice.url_check_detail)
                .setPriority(Priority.IMMEDIATE)
                .addBodyParameter(hashMap)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("检查红包详情",response.toString());
                        if (TextUtils.isEmpty(response.toString())){
                            UtilTool.ShowToast(context,"获取失败");
                            return;
                        }
                        parsedRequestListener.onResponseResult(response.toString());
//
                    }

                    @Override
                    public void onError(ANError anError) {
                        parsedRequestListener._OnError(anError.toString());
                    }
                });
    }
    /**拆红包
     * {
     "back": "1.11",
     "topback": 1,
     "status": 200,
     "msg": "1.11"
     }
     * @param
     * @param
     * @param parsedRequestListener
     */
    public static void chai_redpack(final  Context context,String rid,String uid, final ParsedRequestListener<String> parsedRequestListener){
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("rid",rid);
        hashMap.put("uid",uid);
        RxAndroidNetworking.post(commonParme.apiservice.URL + commonParme.apiservice.uri_chai_rel_detail)
                .setPriority(Priority.IMMEDIATE)
                .addBodyParameter(hashMap)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("拆红包",response.toString());
                        if (TextUtils.isEmpty(response.toString())){
                            UtilTool.ShowToast(context,"获取失败");
                            return;
                        }
                        parsedRequestListener.onResponseResult(response.toString());
//
                    }

                    @Override
                    public void onError(ANError anError) {
                        parsedRequestListener._OnError(anError.toString());
                    }
                });
    }
}
