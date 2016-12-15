package com.example.administrator.learn.Tool;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.learn.MainActivity;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2016/12/13.
 */

public class MyReceiverPush extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())){
            //点击
            String extra = intent.getExtras().getString(JPushInterface.EXTRA_EXTRA);
            Intent mintent=new Intent(context, MainActivity.class);
            mintent.putExtra("extra",extra);
                mintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mintent);
            Log.e("点击附加字段",intent.getExtras().getString(JPushInterface.EXTRA_EXTRA));



        }else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())){
            //收到push时得到消息，extra——extra是这样的  {"r":"11","e":"222"}
            Log.d("附加字段",intent.getExtras().getString(JPushInterface.EXTRA_EXTRA));
            Log.d("内容",intent.getExtras().getString(JPushInterface.EXTRA_ALERT));
            if (MainActivity.mainActivity !=null){
                String extra = intent.getExtras().getString(JPushInterface.EXTRA_EXTRA);
                evenbus_push push=new evenbus_push();
                push.setPushInfo(extra);
                EventBus.getDefault().post(push);
            }
        }
    }
}
