package com.example.administrator.learn;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;


public class ShlashActivity extends Activity {
Handler handler=new Handler();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shlash);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(ShlashActivity.this,MainActivity.class));
                finish();
            }
        },3000);
    }
}
