package com.example.administrator.learn;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.learn.Model.stoppushInfo;
import com.example.administrator.learn.ServceTool.ApiService;
import com.example.administrator.learn.Tool.BlurTransformation;
import com.example.administrator.learn.Tool.GlideCircleTransform;
import com.example.administrator.learn.Tool.SPUtils;
import com.example.administrator.learn.Tool.UtilTool;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 推流结束
 */
public class StopPushActivity extends Activity {

    @InjectView(R.id.image_head)
    ImageView imageHead;
    @InjectView(R.id.tv_niName)
    TextView tvNiName;
    @InjectView(R.id.tv_time)
    TextView tvTime;
    @InjectView(R.id.tv_gold)
    TextView tvGold;
    @InjectView(R.id.tv_watchNum)
    TextView tvWatchNum;
    @InjectView(R.id.tv_shareNum)
    TextView tvShareNum;
    @InjectView(R.id.btn_stop)
    Button btnStop;
    @InjectView(R.id.image_ground)
    ImageView imageGround;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stop_push);
        ButterKnife.inject(this);
        stopPush();
    }

    @OnClick(R.id.btn_stop)
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_stop:
                finish();
                break;
        }

    }
    /**
     * 退出直播
     */
    private void stopPush() {
        ApiService.stopPush(SPUtils.getUid(this), SPUtils.getliveid(this), new ApiService.ParsedRequestListener<stoppushInfo>() {
            @Override
            public void onResponseResult(stoppushInfo stoppushInfo) {
                tvWatchNum.setText(stoppushInfo.getData().getLive_num());
                tvShareNum.setText(stoppushInfo.getData().getFx_num());
                tvNiName.setText(SPUtils.getNiceName(StopPushActivity.this));
                tvTime.setText("直播时长:"+stoppushInfo.getData().getLive_time());
                tvGold.setText(stoppushInfo.getData().getLive_money());
                setimage(stoppushInfo.getData().getLive_img());
//                UtilTool.ShowToast(StopPushActivity.this, stoppushInfo.toString());
            }


            @Override
            public void _OnError(String errormessage) {
                setimage(null);
                UtilTool.ShowToast(StopPushActivity.this, errormessage);
            }
        });
    }

    /**设置头像跟背景
     * @param
     */
    private void setimage(String liveimage) {
        Glide.with(StopPushActivity.this)
                .load(SPUtils.getheader_url(this))
                .placeholder(R.mipmap.hande_default)
                .error(R.mipmap.hande_default)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .bitmapTransform(new GlideCircleTransform(this))
                .into(imageHead);
        Glide.with(StopPushActivity.this)
                .load(liveimage)
                .crossFade(1000)
                .placeholder(R.mipmap.background_default)
                .error(R.mipmap.background_default)
                .bitmapTransform(new BlurTransformation(this,10,1)) // “23”：设置模糊度(在0.0到25.0之间)，默认”25";"4":图片缩放比例,默认“1”。
                .into(imageGround);

    }
}
