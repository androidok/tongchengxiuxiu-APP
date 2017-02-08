package com.pinhongbao.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.pinhongbao.R;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_red_envelope);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        tvTitle.setText("拼手气");
    }

    @OnClick({R.id.image_back,R.id.btn_send})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.image_back:
                finish();
                break;
            case R.id.btn_send:
                break;
        }
    }
}
