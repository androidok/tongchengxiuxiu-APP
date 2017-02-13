package com.pinhongbao.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;


@SuppressLint("ResourceAsColor")
public class TimeCount extends CountDownTimer
{
	private Button mBtnGetcode;
	private Context context;

	public TimeCount(Context context,long millisInFuture, long countDownInterval, Button btnGetcode) {
		super(millisInFuture, countDownInterval);
		mBtnGetcode = btnGetcode;
		this.context=context;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public void onTick(long millisUntilFinished) {
		mBtnGetcode.setClickable(false);
		mBtnGetcode.setText(millisUntilFinished / 1000 + "s");
	}

	@Override
	public void onFinish() {
		mBtnGetcode.setText("获取验证码");
		mBtnGetcode.setClickable(true);

	}
}
