package com.pinhongbao.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinhongbao.Model.EvenbusInfo;
import com.pinhongbao.R;
import com.pinhongbao.Util.SPUtils;
import com.pinhongbao.Util.UtilTool;
import com.pinhongbao.Util.commonParme;
import com.pinhongbao.serviceTool.ApiService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 发起红包
 */
public class SendRedEnvelopeActivity extends Activity {

    @InjectView(R.id.image_back)
    ImageView imageBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.ed_theme)
    EditText edTheme;
    @InjectView(R.id.ed_mun)
    EditText edMun;
    @InjectView(R.id.ed_money)
    EditText edMoney;
    @InjectView(R.id.btn_send)
    Button btnSend;
    private String tempId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_red_envelope);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        EventBus.getDefault().register(this);
        tvTitle.setText("拼手气");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.image_back, R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_back:
                finish();
                break;
            case R.id.btn_send:
                String money = edMoney.getText().toString().trim();
                String num = edMun.getText().toString().trim();
                String title = tvTitle.getText().toString().trim();
                if (TextUtils.isEmpty(money)) {
                    UtilTool.ShowToast(this, "请填写金额");
                    return;
                }
                if (TextUtils.isEmpty(num)) {
                    UtilTool.ShowToast(this, "请填写总人数");
                    return;
                }
                if (TextUtils.isEmpty(title)) {
                    UtilTool.ShowToast(this, "请填写标题");
                    return;
                }
                String balanced = SPUtils.getBalance(this);
                float balance = Float.parseFloat(balanced);
                float moneyd = Float.parseFloat(money);
                if (balance < moneyd) {
                    UtilTool.ShowToast(this, "余额不足,用微信支付");
                    send_pay(money, num, title, 1);
                } else {
                    send_pay(money, num, title, 0);
                }

                break;
        }
    }

    private void send_pay(String money, String num, String title, final int payStyle) {
        ApiService.send_task(this, SPUtils.getUid(this), num, money, title, payStyle + "", new ApiService.ParsedRequestListener<String>() {
            @Override
            public void onResponseResult(String onpanse) {
                try {
                    JSONObject jsonObject = new JSONObject(onpanse);

                    if (payStyle == 1) {//微信支付
                        JSONObject info = jsonObject.getJSONObject("info");
                        String noncestr = info.getString("noncestr");
                        String prepayid = info.getString("prepayid");
                        String timestamp = info.getString("timestamp");
                        String sign = info.getString("sign");
                         tempId = jsonObject.getString("tempId");
                        UtilTool.WXPay(SendRedEnvelopeActivity.this, prepayid, noncestr, sign, timestamp);

                    } else {
                        if (!jsonObject.isNull("msg")) {
                            UtilTool.ShowToast(SendRedEnvelopeActivity.this, jsonObject.getString("msg"));
                        }
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

    @Subscribe
    public void onEventMainThread(EvenbusInfo info) {
        int respCode = info.getMessage();
        if (respCode == 0) {//成功
            UtilTool.ShowToast(this, "支付成功");
            check_redpack();
        } else if (respCode == -1) {//失败
            UtilTool.ShowToast(this, "支付失败");
        } else if (respCode == -2) {//取消支付
            UtilTool.ShowToast(this, "取消支付");
            check_redpack();
        }
    }

    /**
     * 得到红包id
     */
    private void check_redpack(){
        ApiService.check_redpack(this, tempId, new ApiService.ParsedRequestListener<String>() {
            @Override
            public void onResponseResult(String onpase) {
                try {
                    JSONObject jsonObject=new JSONObject(onpase);
                    String code = jsonObject.getString("code");
                    if (commonParme.API_SERVICE_SUCCESSFUL.equalsIgnoreCase(code)){
                        String rid = jsonObject.getString("rid");
                        Intent intent = new Intent(SendRedEnvelopeActivity.this, DetailActivity.class);
                        intent.putExtra("rid", rid);
                        startActivity(intent);
                        finish();
                    }else {
                        if (!jsonObject.isNull("msg")){
                            UtilTool.ShowToast(SendRedEnvelopeActivity.this,jsonObject.getString("msg"));
                        }
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
