package com.pinhongbao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.pinhongbao.Model.LoginInfo;
import com.pinhongbao.Model.registerInfo;
import com.pinhongbao.Model.weixinInfo;
import com.pinhongbao.R;
import com.pinhongbao.Util.SPUtils;
import com.pinhongbao.Util.UtilTool;
import com.pinhongbao.Util.commonParme;
import com.pinhongbao.serviceTool.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * 登录
 */
public class LoginActivity extends Activity {

    @InjectView(R.id.image_back)
    ImageView imageBack;
    @InjectView(R.id.ed_account)
    EditText edAccount;
    @InjectView(R.id.ed_password)
    EditText edPassword;
    @InjectView(R.id.btn_forgetpassword)
    Button btnForgetpassword;
    @InjectView(R.id.btn_login)
    Button btnLogin;
    @InjectView(R.id.image_weixinlogin)
    ImageView imageWeixinlogin;
    HashMap<String,String> hashMap=new HashMap<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.image_back,R.id.btn_forgetpassword,R.id.btn_login,R.id.image_weixinlogin})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_back:
                finish();
                break;
            case R.id.btn_forgetpassword://忘记密码
                Intent intent = new Intent(this, RegisterActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
                //密码设置成功后返回来需要再次登录，立即登录
                break;
            case R.id.btn_login://立即登录
                String username = edAccount.getText().toString().trim();
                String password = edPassword.getText().toString().trim();
                if (TextUtils.isEmpty(username)){
                    UtilTool.ShowToast(this,"账号不能为空");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    UtilTool.ShowToast(this,"密码不能为空");
                    return;
                }
                Login_phone(username,password);
                break;
            case R.id.image_weixinlogin://微信登录
                authorize(new Wechat(this));
                UtilTool.ShowToast(this,"正在登录");
                break;
        }
    }
    private void authorize(Platform plat) {
        plat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Message msg = new Message();
                msg.what = MSG_WEIXIN_SCUSSE;
                msg.obj = platform;
                handler.sendMessage(msg);

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d("微信登录", "失败" + throwable.getMessage());
                Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                handler.sendEmptyMessage(MSG_WEIXIN_ERROR);

            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toast.makeText(LoginActivity.this, "取消登录", Toast.LENGTH_LONG).show();
            }
        });
        plat.SSOSetting(true);
        plat.removeAccount(true);
        ShareSDK.removeCookieOnAuthorize(true);
        plat.showUser(null);
    }
    private static final int MSG_WEIXIN_SCUSSE = 1;
    private static final int MSG_WEIXIN_ERROR = 2;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WEIXIN_SCUSSE:
                    Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_LONG).show();
                    Platform platform = (Platform) msg.obj;
                    PlatformDb db = platform.getDb();
                    try {
                        JSONObject jsonObject = new JSONObject(db.exportData());
                        weixinInfo weixinInfo = JSON.parseObject(jsonObject.toString(), weixinInfo.class);

                        Log.d("微信登录信息", "" + jsonObject.toString());
                        hashMap.put("nickname",weixinInfo.getNickname());
                        hashMap.put("gender",weixinInfo.getGender());
                        hashMap.put("openid",weixinInfo.getOpenid());
                        hashMap.put("city",weixinInfo.getCity());
                        hashMap.put("province",weixinInfo.getProvince());
                        hashMap.put("unionid",weixinInfo.getUnionid());
                        hashMap.put("icon",weixinInfo.getIcon());
                        Register(hashMap);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
                case MSG_WEIXIN_ERROR:
                    Toast.makeText(LoginActivity.this, "登录失败", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    /**手机登录
     * @param username
     * @param password
     */
    private void Login_phone(String username,String password){
        ApiService.Login_phone(this, username, password, new ApiService.ParsedRequestListener<LoginInfo>() {
            @Override
            public void onResponseResult(LoginInfo loginInfo) {
                SPUtils.PutUid(LoginActivity.this,loginInfo.getId());
                SPUtils.PutBalance(LoginActivity.this,loginInfo.getPaymoney());
                SPUtils.PutIcon(LoginActivity.this,loginInfo.getAvatar());
                SPUtils.Putnicname(LoginActivity.this,loginInfo.getUser_nicename());
                finish();
            }

            @Override
            public void _OnError(String errormessage) {
                UtilTool.ShowToast(LoginActivity.this,errormessage);

            }
        });
    }

    /**注册
     * @param hashMap
     */
    private void Register(final HashMap<String ,String> hashMap){
        ApiService.weixinLogin( hashMap, new ApiService.ParsedRequestListener<String>() {
            @Override
            public void onResponseResult(String response) {
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String code = jsonObject.getString("code");
                    if (commonParme.API_SERVICE_SUCCESSFUL.equalsIgnoreCase(code)){
                        if (!jsonObject.isNull("uid")){
                            String uid = jsonObject.getString("uid");
                            SPUtils.PutUid(LoginActivity.this,uid);
                        }
                        if (!jsonObject.isNull("uinfo")){
                            String mobile = jsonObject.getJSONObject("uinfo").getString("mobile");
                            JSONObject uinfo = jsonObject.getJSONObject("uinfo");
                            registerInfo registerInfo = JSON.parseObject(uinfo.toString(), registerInfo.class);
                            UtilTool.ShowToast(LoginActivity.this,registerInfo.toString());
                            SPUtils.PutUid(LoginActivity.this,registerInfo.getId());
                            SPUtils.PutIcon(LoginActivity.this,registerInfo.getAvatar());
                            SPUtils.Putnicname(LoginActivity.this,registerInfo.getUser_nicename());
                            SPUtils.PutBalance(LoginActivity.this,registerInfo.getPaymoney());
                            if (TextUtils.isEmpty(mobile) || "".equalsIgnoreCase(mobile)){
                                //需要注册
                                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                                intent.putExtra("type",2);
                                startActivity(intent);
                            }
                            finish();
                        }else {
                            //注册完了需要再次登录
                            Register(hashMap);
                        }
                    }else {
                        UtilTool.ShowToast(LoginActivity.this,"注册失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void _OnError(String errormessage) {

            }
        });
    }
}
