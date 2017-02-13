package com.pinhongbao.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.pinhongbao.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 玩法规则
 */
public class RuleActivity extends Activity {

    @InjectView(R.id.btn_back)
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.btn_back)
    public void onClick() {
        finish();
    }
}
