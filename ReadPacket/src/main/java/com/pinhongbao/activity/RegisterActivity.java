package com.pinhongbao.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.pinhongbao.R;
import com.pinhongbao.Util.SPUtils;
import com.pinhongbao.Util.TimeCount;
import com.pinhongbao.Util.UtilTool;
import com.pinhongbao.Util.commonParme;
import com.pinhongbao.serviceTool.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 注册
 */
public class RegisterActivity extends Activity {

    @InjectView(R.id.image_back)
    ImageView imageBack;
    @InjectView(R.id.ed_phonenum)
    EditText edPhonenum;
    @InjectView(R.id.ed_password)
    EditText edPassword;
    @InjectView(R.id.ed_passworded)
    EditText edPassworded;
    @InjectView(R.id.tv_code)
    EditText tvCode;
    @InjectView(R.id.btn_getcode)
    Button btnGetcode;
    @InjectView(R.id.btn_ok)
    Button btnOk;
    private int type;//1忘记密码   2 绑定

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        initview();
    }

    private void initview() {
        Bundle extras = getIntent().getExtras();
        type = extras.getInt("type");
    }

    @OnClick({R.id.btn_ok, R.id.btn_getcode, R.id.image_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.btn_ok:
                Login();
                break;
            case R.id.btn_getcode:
                getcode();
                break;
        }
    }

    /**
     * 登录或者重置密码
     */
    private void Login() {
        String phone = edPhonenum.getText().toString().trim();
        String password = edPassword.getText().toString().trim();
        String passworded = edPassworded.getText().toString().trim();
        String code = tvCode.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            UtilTool.ShowToast(this, "手机号码不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            UtilTool.ShowToast(this, "密码不能为空");
            return;
        }
        if (!password.equalsIgnoreCase(passworded)) {
            UtilTool.ShowToast(this, "密码不一致");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            UtilTool.ShowToast(this, "请填写验证码");
            return;
        }
        if (type == 2) {
            Login(phone, password, code);
        } else if (type == 1) {
            ReSetPassword(phone, password, code);
        }


    }

    /**
     * 绑定手机号码
     *
     * @param phone
     * @param password
     * @param code
     */
    private void Login(String phone, String password, String code) {
        ApiService.login_weixin(this, phone, SPUtils.getUid(this), password, code, new ApiService.ParsedRequestListener<String>() {
            @Override
            public void onResponseResult(String onpase) {
                if (TextUtils.isEmpty(onpase)) {
                    UtilTool.ShowToast(RegisterActivity.this, "绑定失败");
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(onpase);
                    String code = jsonObject.getString("code");
                    if (commonParme.API_SERVICE_SUCCESSFUL.equalsIgnoreCase(code)) {
                        UtilTool.ShowToast(RegisterActivity.this, "绑定成功");
                        finish();
                    } else {
                        UtilTool.ShowToast(RegisterActivity.this, jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void _OnError(String errormessage) {
                UtilTool.ShowToast(RegisterActivity.this, "登入失败");
            }
        });
    }

    /**
     * 重置密码
     *
     * @param phone
     * @param password
     * @param code
     */
    private void ReSetPassword(String phone, String password, String code) {
        ApiService.ResetPassword(phone, password, code, new ApiService.ParsedRequestListener<String>() {
            @Override
            public void onResponseResult(String onpase) {
                if (TextUtils.isEmpty(onpase)) {
                    UtilTool.ShowToast(RegisterActivity.this, "重置失败");
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(onpase);
                    String code = jsonObject.getString("code");
                    if (commonParme.API_SERVICE_SUCCESSFUL.equalsIgnoreCase(code)) {
                        UtilTool.ShowToast(RegisterActivity.this, "重置成功");
                        finish();
                    } else {
                        UtilTool.ShowToast(RegisterActivity.this, jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void _OnError(String errormessage) {
                UtilTool.ShowToast(RegisterActivity.this, "重置失败");
            }
        });
    }


    /**
     * 获取验证码
     */
    private void getcode() {
        String phone = edPhonenum.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            UtilTool.ShowToast(this, "手机号码不能为空");
            return;
        }
        ApiService.getcode( phone, new ApiService.ParsedRequestListener<String>() {
            @Override
            public void onResponseResult(String respone) {
                if (TextUtils.isEmpty(respone)) {
                    UtilTool.ShowToast(RegisterActivity.this, "请重新获取");
                    return;
                }
                try {
                    JSONObject jsonObject = new JSONObject(respone);
                    if (jsonObject.getString("code").equalsIgnoreCase(commonParme.API_SERVICE_SUCCESSFUL)) {
                        UtilTool.ShowToast(RegisterActivity.this, "发送成功");
                        new TimeCount(RegisterActivity.this, 60000, 1000, btnGetcode).start();
                    }else {
                        UtilTool.ShowToast(RegisterActivity.this, jsonObject.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void _OnError(String errormessage) {
                UtilTool.ShowToast(RegisterActivity.this, "请重新获取");
            }
        });
    }
}
