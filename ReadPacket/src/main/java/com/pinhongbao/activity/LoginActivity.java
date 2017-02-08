package com.pinhongbao.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.pinhongbao.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

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
                break;
            case R.id.btn_login://立即登录
                break;
            case R.id.image_weixinlogin://微信登录
                break;
        }
    }
}
