package com.example.administrator.learn;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.MediaPlayer;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.learn.Model.apiSuccessInfo;
import com.example.administrator.learn.Model.checkliveInfo;
import com.example.administrator.learn.ServceTool.ApiService;
import com.example.administrator.learn.Tool.EvenbusInfo;
import com.example.administrator.learn.Tool.PayUtil;
import com.example.administrator.learn.Tool.ShareUtils;
import com.example.administrator.learn.Tool.Sharedparms;
import com.example.administrator.learn.Tool.UtilTool;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PlayerActivity extends Activity {

    @InjectView(R.id.image_back)
    ImageView imageBack;
    @InjectView(R.id.layout_image)
    RelativeLayout layoutImage;
    @InjectView(R.id.video_videoview)
    WebView videoVideoview;
    @InjectView(R.id.layout)
    RelativeLayout layout;
    @InjectView(R.id.tv_pause)
    TextView tvPause;
    private String liveurl;
    private String livetmpUrl;
    private String image_url;
    private String webView_url;
    private String live_id;//分享使用
    private ProgressDialog progressDialog;
    private String video_liveid;

    public interface StatusListener {
        int notifyStatus(int status);
    }

    public static final String TAG = "PlayerActivity";

    public static final int STATUS_START = 1;
    public static final int STATUS_STOP = 2;
    public static final int STATUS_PAUSE = 3;
    public static final int STATUS_RESUME = 4;

    public static final int CMD_START = 1;
    public static final int CMD_STOP = 2;
    public static final int CMD_PAUSE = 3;
    public static final int CMD_RESUME = 4;
    public static final int CMD_VOLUME = 5;
    public static final int CMD_SEEK = 6;

    public static final int TEST = 0;


    private AliVcMediaPlayer mPlayer = null;
    private SurfaceHolder mSurfaceHolder = null;
    private SurfaceView mSurfaceView = null;

    private SeekBar mSeekBar = null;
    private TextView mTipView = null;
    private TextView mCurDurationView = null;
    private TextView mErrInfoView = null;
    private TextView mDecoderTypeView = null;
    private LinearLayout mTipLayout = null;

    private boolean mEnableUpdateProgress = true;
    private int mLastPercent = -1;
    private int mPlayingIndex = -1;
    private StringBuilder msURI = new StringBuilder("");
    private StringBuilder msTitle = new StringBuilder("");
    private GestureDetector mGestureDetector;
    private int mPosition = 0;
    private int mVolumn = 50;
    private MediaPlayer.VideoScalingMode mScalingMode = MediaPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING;
    private boolean mMute = false;

    private PlayerControl mPlayerControl = null;

    private PowerManager.WakeLock mWakeLock = null;

    private StatusListener mStatusListener = null;

    private boolean isLastWifiConnected = false;

    // 标记播放器是否已经停止
    private boolean isStopPlayer = false;
    // 标记播放器是否已经暂停
    private boolean isPausePlayer = false;
    private boolean isPausedByUser = false;
    //用来控制应用前后台切换的逻辑
    private boolean isCurrentRunningForeground = true;
    private String playvideo_share_content = "我在看直播，一起来吧！";
    public static PlayerActivity playerActivity;
    // 重点:发生从wifi切换到4g时,提示用户是否需要继续播放,此处有两种做法:
    // 1.从历史位置从新播放
    // 2.暂停播放,因为存在网络切换,续播有时会不成功
    private BroadcastReceiver connectionReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            Log.d(TAG, "mobile " + mobNetInfo.isConnected() + " wifi " + wifiNetInfo.isConnected());

            if (!isLastWifiConnected && wifiNetInfo.isConnected()) {
                isLastWifiConnected = true;
            }
            if (isLastWifiConnected && mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                isLastWifiConnected = false;
                if (mPlayer != null) {
                    mPosition = mPlayer.getCurrentPosition();
                    // 重点:新增接口,此处必须要将之前的surface释放掉
                    mPlayer.releaseVideoSurface();
                    mPlayer.stop();
                    mPlayer.destroy();
                    mPlayer = null;
                }
                dialog();
            }
        }
    };

    protected void dialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PlayerActivity.this);
        builder.setMessage("确认继续播放吗？");

        builder.setTitle("提示");

        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                initSurface();

            }
        });

        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PlayerActivity.this.finish();
            }
        });

        builder.create().show();
    }

    void setStatusListener(StatusListener listener) {
        mStatusListener = listener;
    }

    private PlayerControl.ControllerListener mController = new PlayerControl.ControllerListener() {

        @Override
        public void notifyController(int cmd, int extra) {
            Message msg = Message.obtain();
            switch (cmd) {
                case PlayerControl.CMD_PAUSE:
                    msg.what = CMD_PAUSE;
                    break;
                case PlayerControl.CMD_RESUME:
                    msg.what = CMD_RESUME;
                    break;
                case PlayerControl.CMD_SEEK:
                    msg.what = CMD_SEEK;
                    msg.arg1 = extra;
                    break;
                case PlayerControl.CMD_START:
                    msg.what = CMD_START;
                    break;
                case PlayerControl.CMD_STOP:
                    msg.what = CMD_STOP;
                    break;
                case PlayerControl.CMD_VOLUME:
                    msg.what = CMD_VOLUME;
                    msg.arg1 = extra;

                    break;

                default:
                    break;

            }

            if (TEST != 0) {
                mTimerHandler.sendMessage(msg);
            }
        }
    };

    /**
     * 得到信息
     */
    private void getintentinfo() {
        Bundle extras = getIntent().getExtras();
        livetmpUrl = extras.getString(Sharedparms.IntentInfo.LIVERTMPURL);
        image_url = extras.getString(Sharedparms.IntentInfo.IMAGEURL);
        webView_url = extras.getString(Sharedparms.IntentInfo.WEBVIEWURL);
        video_liveid = extras.getString(Sharedparms.IntentInfo.LIVEID);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e(TAG, "onCreate.");
        super.onCreate(savedInstanceState);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectionReceiver, intentFilter);
        setContentView(R.layout.activity_play);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
        playerActivity=this;
        mPlayingIndex = -1;
        Bundle extras = getIntent().getExtras();
        getintentinfo();

        setVebView();
        //加载背景
        Glide.with(PlayerActivity.this)
                .load(image_url)
                .error(R.mipmap.background_default)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageBack);
        if (TEST == 1) {
            mPlayerControl = new PlayerControl(this);
            mPlayerControl.setControllerListener(mController);
        }
        progressDialog = ProgressDialog.show(PlayerActivity.this, "", "加载中...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        checkVideo(true);
        acquireWakeLock();

        init();
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, 5000);
            isPausePlayer = false;
            mPlayer.play();
            startToPlay();

        }
    };
    private static int SDK_PAY_FLAG = 3;//支付宝支付
    Handler Mhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == SDK_PAY_FLAG) {//支付宝支付
                String result = (String) msg.obj;
                String substring = result.split(";")[0].split("=")[1];
                String resultStatus = substring.substring(1, substring.length() - 1);
                videoVideoview.reload();
                if ("9000".equalsIgnoreCase(resultStatus)) {
                    UtilTool.ShowToast(PlayerActivity.this,"支付成功");
                } else {
                    UtilTool.ShowToast(PlayerActivity.this,"支付失败或者取消");

                }
            }

        }
    };

    private static String status_finish = "2";//直播结束
    private static String status_banned = "3";//被禁播
    private static String status_ing = "4";//直播中

    /**
     * 检测直播是否结束了
     * isstart  开始的话如果直播结束就不用结束页面，如果在播放的时候检测到直播结束了，那就要结束页面
     */
    private void checkVideo(final boolean isstart) {
        ApiService.CheckLive(video_liveid, new ApiService.ParsedRequestListener<checkliveInfo>() {
            @Override
            public void onResponseResult(checkliveInfo checkliveInfo) {
                if (checkliveInfo != null && Sharedparms.statusSuccess == checkliveInfo.getStatus()) {
//                    UtilTool.ShowToast(PlayerActivity.this, checkliveInfo.toString());
                    if (status_finish.equalsIgnoreCase(checkliveInfo.getLive_status())) {
                        if (isstart && progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        } else if (!isstart) {
                            UtilTool.ShowToast(PlayerActivity.this, "直播结束");
                            finish();
                        }

                    } else if (status_banned.equalsIgnoreCase(checkliveInfo.getLive_status())) {
                        UtilTool.ShowToast(PlayerActivity.this, "视频被禁播");
                        finish();
                    } else if (status_ing.equalsIgnoreCase(checkliveInfo.getLive_status())) {
                        //直播中
                        UtilTool.ShowToast(PlayerActivity.this, "主播稍后回来");
                        whetherTiemer(true);
                    }
                }
            }

            @Override
            public void _OnError(String errormessage) {
                UtilTool.ShowToast(PlayerActivity.this, errormessage);

            }
        });

    }
    /**
     * 微信回调页面传过来，判断是否支付成功
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(EvenbusInfo event) {
        int respCode = event.getRespCode();
        if (respCode == 0) {//成功
            UtilTool.ShowToast(PlayerActivity.this,"支付成功");
            videoVideoview.reload();
        } else if (respCode == -1) {//失败
            UtilTool.ShowToast(PlayerActivity.this,"支付失败");
        } else if (respCode == -2) {//取消支付
            UtilTool.ShowToast(PlayerActivity.this,"取消支付");
        }

    }
    /**
     * 定时器是否开启
     *
     * @param isOpen
     */
    private void whetherTiemer(boolean isOpen) {
        if (isOpen) {
            imageBack.setVisibility(View.VISIBLE);
            tvPause.setVisibility(View.VISIBLE);
            videoVideoview.setVisibility(View.GONE);
            handler.postDelayed(runnable, 5000);
        } else {
            imageBack.setVisibility(View.GONE);
            tvPause.setVisibility(View.GONE);
            videoVideoview.setVisibility(View.VISIBLE);
            handler.removeCallbacks(runnable);
        }

    }

    private void setVebView() {
        WebSettings webSettings = videoVideoview.getSettings();
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUserAgentString(Sharedparms.WEBVIEWA_AGENT + UtilTool.getVersionName(this));
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //下面三个方法设置webview透明的
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDefaultTextEncodingName("utf-8");
        videoVideoview.setBackgroundColor(0); // 设置背景色
        videoVideoview.addJavascriptInterface(new javaScriptObjcet(), "jnoo");
        videoVideoview.setWebChromeClient(new WebChromeClient());
        videoVideoview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        videoVideoview.loadUrl(webView_url);
    }

    private class MyGestureListener extends SimpleOnGestureListener {

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {

            final double FLING_MIN_DISTANCE = 0.5;
            final double FLING_MIN_VELOCITY = 0.5;

            if (e1.getY() - e2.getY() > FLING_MIN_DISTANCE
                    && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                onVolumeSlide(1);
            }
            if (e1.getY() - e2.getY() < FLING_MIN_DISTANCE
                    && Math.abs(distanceY) > FLING_MIN_VELOCITY) {
                onVolumeSlide(-1);
            }

            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    }

    /**
     * js
     */
    public class javaScriptObjcet {
        @JavascriptInterface
        public void livestop() {//播放时的xx
            finish();
        }

        @JavascriptInterface
        public void overliveStop() {//当直播结束了，还是有人进去看，那这样网页上就是结束的页面，这个就是返回按钮的js方法
            finish();
        }

        @JavascriptInterface
        public void WXpay(String wxPayInfo) {
            if (TextUtils.isEmpty(wxPayInfo)) {
                Toast.makeText(PlayerActivity.this, "服务器异常", Toast.LENGTH_SHORT).show();
                return;
            }
            PayUtil.getInstance(PlayerActivity.this).wxPay(wxPayInfo);
        }
        @JavascriptInterface
        public void Zhipay(final String payJsInfo) {
            PayUtil.getInstance(PlayerActivity.this).zhiPay(Mhandler,payJsInfo);
        }
        @JavascriptInterface
        public void liveShare(String data) {//播放时的
            //开始分享
            if (TextUtils.isEmpty(data)) {
                UtilTool.ShowToast(PlayerActivity.this, "js出现错误");
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(data);
                String img = jsonObject.getString("img");
                String title = jsonObject.getString("title");
                String share_url = jsonObject.getString("share_url");
                live_id = jsonObject.getString("live_id");
                ShareUtils.showPopuViewDialog(PlayerActivity.this, playvideo_share_content, title, img, share_url, PlayerActivity.this.findViewById(R.id.layout), false, shareListener);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 分享回调
     */
    ShareUtils.setShareListener shareListener = new ShareUtils.setShareListener() {

        @Override
        public void shareSuccess(boolean isSuccessful, boolean iscallback) {
            if (isSuccessful) {
                shareLiveId();
            }
        }
    };

    /**
     * 分享后接口
     */
    private void shareLiveId() {
        ApiService.ShareLive(live_id, new ApiService.ParsedRequestListener<apiSuccessInfo>() {
            @Override
            public void onResponseResult(apiSuccessInfo apiSuccessInfo) {
                if (apiSuccessInfo.getStatus() == Sharedparms.statusSuccess) {
//                    UtilTool.ShowToast(PlayerActivity.this, "接口成功");
                } else {
                    UtilTool.ShowToast(PlayerActivity.this, apiSuccessInfo.getMsg() + apiSuccessInfo.getMsg());
                }

            }

            @Override
            public void _OnError(String errormessage) {
                UtilTool.ShowToast(PlayerActivity.this, errormessage);
            }
        });
    }

    private void onVolumeSlide(int vol) {
        if (mPlayer != null) {
            mVolumn += vol;
            if (mVolumn > 100)
                mVolumn = 100;
            if (mVolumn < 0)
                mVolumn = 0;
            mPlayer.setVolume(mVolumn);
        }
    }

    private void acquireWakeLock() {
        if (mWakeLock == null) {
            PowerManager pMgr = (PowerManager) this.getSystemService(this.POWER_SERVICE);
            mWakeLock = pMgr.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK,
                    "SmsSyncService.sync() wakelock.");

        }
        mWakeLock.acquire();
    }

    private void releaseWakeLock() {
        mWakeLock.release();
        mWakeLock = null;
    }

    private void update_progress(int ms) {
        if (mEnableUpdateProgress) {
            mSeekBar.setProgress(ms);
        }
        return;
    }

    private void update_second_progress(int ms) {
        if (mEnableUpdateProgress) {
            mSeekBar.setSecondaryProgress(ms);
        }
        return;
    }

    private void show_progress_ui(boolean bShowPause) {
        LinearLayout progress_layout = (LinearLayout) findViewById(R.id.progress_layout);
        TextView video_title = (TextView) findViewById(R.id.video_title);

        if (bShowPause) {
            progress_layout.setVisibility(View.VISIBLE);
            video_title.setVisibility(View.VISIBLE);

        } else {
//            progress_layout.setVisibility(View.GONE);
//            video_title.setVisibility(View.GONE);
        }
    }

    private void show_pause_ui(boolean bShowPauseBtn, boolean bShowReplayBtn) {
        LinearLayout layout = (LinearLayout) findViewById(R.id.buttonLayout);
        if (!bShowPauseBtn && !bShowReplayBtn) {
            layout.setVisibility(View.GONE);
        } else {
            layout.setVisibility(View.VISIBLE);
        }
        ImageView pause_view = (ImageView) findViewById(R.id.pause_button);
        pause_view.setVisibility(bShowPauseBtn ? View.VISIBLE : View.GONE);

        Button replay_btn = (Button) findViewById(R.id.replay_button);
        replay_btn.setVisibility(bShowReplayBtn ? View.VISIBLE : View.GONE);

        return;
    }

    private int show_tip_ui(boolean bShowTip, float percent) {

        int vnum = (int) (percent);
        vnum = vnum > 100 ? 100 : vnum;

        mTipLayout.setVisibility(bShowTip ? View.VISIBLE : View.GONE);
        mTipView.setVisibility(bShowTip ? View.VISIBLE : View.GONE);

        if (mLastPercent < 0) {
            mLastPercent = vnum;
        } else if (vnum < mLastPercent) {
            vnum = mLastPercent;
        } else {
            mLastPercent = vnum;
        }

        String strValue = String.format("Buffering(%1$d%%)...", vnum);
        mTipView.setText(strValue);

        if (!bShowTip) { //hide it, then we need reset the percent value here.
            mLastPercent = -1;
        }

        return vnum;
    }

    private void show_buffering_ui(boolean bShowTip) {

        mTipLayout.setVisibility(bShowTip ? View.VISIBLE : View.GONE);
        mTipView.setVisibility(bShowTip ? View.VISIBLE : View.GONE);

        String strValue = "缓存中...";
        mTipView.setText(strValue);
    }

    private void update_total_duration(int ms) {
        int var = (int) (ms / 1000.0f + 0.5f);
        int min = var / 60;
        int sec = var % 60;
        TextView total = (TextView) findViewById(R.id.total_duration);
        total.setText("" + min + ":" + sec);


        SeekBar sb = (SeekBar) findViewById(R.id.progress);
        sb.setMax(ms);
        sb.setKeyProgressIncrement(10000); //5000ms = 5sec.
        sb.setProgress(0);
        sb.setSecondaryProgress(0); //reset progress now.

        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int i, boolean fromuser) {
                int var = (int) (i / 1000.0f + 0.5f);
                int min = var / 60;
                int sec = var % 60;
                String strCur = String.format("%1$d:%2$d", min, sec);
                mCurDurationView.setText(strCur);
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                mEnableUpdateProgress = false;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                int ms = seekBar.getProgress();
                mPlayer.seekTo(ms);
            }
        });

        return;
    }

    private void report_error(String err, boolean bshow) {
        if (mErrInfoView.getVisibility() == View.GONE && !bshow) {
            return;
        }
        mErrInfoView.setVisibility(bshow ? View.VISIBLE : View.GONE);
        mErrInfoView.setText(err);
        mErrInfoView.setTextColor(Color.RED);
        return;
    }


    private SurfaceHolder.Callback mSurfaceHolderCB = new SurfaceHolder.Callback() {
        @SuppressWarnings("deprecation")
        public void surfaceCreated(SurfaceHolder holder) {
            holder.setType(SurfaceHolder.SURFACE_TYPE_GPU);
            holder.setKeepScreenOn(true);
            Log.d(TAG, "AlivcPlayer onSurfaceCreated.");

            // 重点:
            if (mPlayer != null) {
                // 对于从后台切换到前台,需要重设surface;部分手机锁屏也会做前后台切换的处理
                mPlayer.setVideoSurface(mSurfaceView.getHolder().getSurface());
            } else {
                // 创建并启动播放器
                startToPlay();
            }

            if (mPlayerControl != null)
                mPlayerControl.start();
            Log.d(TAG, "AlivcPlayeron SurfaceCreated over.");
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            Log.d(TAG, "onSurfaceChanged is valid ? " + holder.getSurface().isValid());
            if (mPlayer != null)
                mPlayer.setSurfaceChanged();
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            Log.d(TAG, "onSurfaceDestroy.");

            if (mPlayer != null) {
                mPlayer.releaseVideoSurface();
            }
        }
    };

    public void switchScalingMode(View view) {
        if (mPlayer != null) {
            if (mScalingMode == MediaPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING) {
                mPlayer.setVideoScalingMode(MediaPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT);
                mScalingMode = MediaPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT;
            } else {
                mPlayer.setVideoScalingMode(MediaPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
                mScalingMode = MediaPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING;
            }
        }
    }

    public void switchMute(View view) {
        if (mPlayer != null) {
            if (mMute == false) {
                mMute = true;
                mPlayer.setMuteMode(true);
            } else {
                mMute = false;
                mPlayer.setMuteMode(false);
            }
        }
    }

//    public void gotoActivity(View view) {
//        startActivity(new Intent(this, BlankActivity.class));
//    }

    public void switchSurface(View view) {
        if (mPlayer != null) {
            // release old surface;
            mPlayer.releaseVideoSurface();
            mSurfaceHolder.removeCallback(mSurfaceHolderCB);
            FrameLayout frameContainer = (FrameLayout) findViewById(R.id.GLViewContainer);
            frameContainer.removeAllViews();

            // init surface
            LinearLayout linearLayout = (LinearLayout) findViewById(R.id.surface_view_container);
            mSurfaceView = new SurfaceView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            params.gravity = Gravity.CENTER;
            mSurfaceView.setLayoutParams(params);
            linearLayout.addView(mSurfaceView);

            mSurfaceHolder = mSurfaceView.getHolder();
            mSurfaceHolder.addCallback(mSurfaceHolderCB);
        }
    }

    /**
     * 重点:初始化播放器使用的SurfaceView,此处的SurfaceView采用动态添加
     *
     * @return 是否成功
     */
    private boolean initSurface() {
        FrameLayout frameContainer = (FrameLayout) findViewById(R.id.GLViewContainer);
        frameContainer.setBackgroundColor(Color.rgb(0, 0, 0));
        mSurfaceView = new SurfaceView(this);
        mGestureDetector = new GestureDetector(this, new MyGestureListener());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER;
        mSurfaceView.setLayoutParams(params);
        // 为避免重复添加,事先remove子view
        frameContainer.removeAllViews();
        frameContainer.addView(mSurfaceView);

        mSurfaceView.setZOrderOnTop(false);

        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            private long mLastDownTimestamp = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (mGestureDetector.onTouchEvent(event))
                    return true;

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mLastDownTimestamp = System.currentTimeMillis();
                    return true;
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mPlayer != null && !mPlayer.isPlaying() && mPlayer.getDuration() > 0) {
                        start();

                        return false;
                    }

                    //just show the progress bar
                    if ((System.currentTimeMillis() - mLastDownTimestamp) > 200) {
                        show_progress_ui(true);
                        mTimerHandler.postDelayed(mUIRunnable, 3000);
                        return true;
                    } else {
                        if (mPlayer != null && mPlayer.getDuration() > 0)
                            pause();

                    }
                    return false;
                }
                return false;
            }
        });

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(mSurfaceHolderCB);

        getPlayUrl();

        return true;
    }

    private boolean init() {
        mTipLayout = (LinearLayout) findViewById(R.id.LayoutTip);
        mSeekBar = (SeekBar) findViewById(R.id.progress);
        mTipView = (TextView) findViewById(R.id.text_tip);
        mCurDurationView = (TextView) findViewById(R.id.current_duration);
        mErrInfoView = (TextView) findViewById(R.id.error_info);
        mDecoderTypeView = (TextView) findViewById(R.id.decoder_type);

        initSurface();

        return true;
    }

    private int getVideoSourcePath(int curIndex, StringBuilder sURI, StringBuilder sTitle) {
        //clear all now
        sURI.delete(0, sURI.length());
        sTitle.delete(0, sTitle.length());

        Bundle bundle = (Bundle) getIntent().getExtras();
        int selected = -1;
        if (curIndex == -1) { //we play the selected item
            sTitle.append(bundle.getString("TITLE"));
            sURI.append(bundle.getString("URI"));
        }
        Bundle loopBundle = bundle.getBundle("loopList");
        if (loopBundle != null) {
            int count = loopBundle.getInt("ItemCount");
            if (curIndex == -1) {
                selected = loopBundle.getInt("SelectedIndex");
            } else {
                selected = curIndex + 1;
                selected = (selected == count ? 0 : selected);
                sURI.append(loopBundle.getString("URI" + selected));
                sTitle.append(loopBundle.getString("TITLE" + selected));
            }
        }
        return selected;
    }

    ;

    private boolean startToPlay() {
        Log.d(TAG, "start play.");
        resetUI();

        if (mPlayer == null) {
            // 初始化播放器
            mPlayer = new AliVcMediaPlayer(this, mSurfaceView);
            mPlayer.setPreparedListener(new VideoPreparedListener());
            mPlayer.setErrorListener(new VideoErrorListener());
            mPlayer.setInfoListener(new VideoInfolistener());
            mPlayer.setSeekCompleteListener(new VideoSeekCompletelistener());
            mPlayer.setCompletedListener(new VideoCompletelistener());
            mPlayer.setVideoSizeChangeListener(new VideoSizeChangelistener());
            mPlayer.setBufferingUpdateListener(new VideoBufferUpdatelistener());
            mPlayer.setStopedListener(new VideoStoppedListener());
            // 如果同时支持软解和硬解是有用
            Bundle bundle = (Bundle) getIntent().getExtras();
            mPlayer.setMediaType(MediaPlayer.MediaType.Live);
            mPlayer.setDefaultDecoder(1);
            mPlayer.setMaxBufferDuration(60000);
//            mPlayer.setMaxBufferDuration(0);
            // 重点: 在调试阶段可以使用以下方法打开native log
//            mPlayer.enableNativeLog();

            if (mPosition != 0) {
                mPlayer.seekTo(mPosition);
            }
        }

        TextView vt = (TextView) findViewById(R.id.video_title);
        vt.setText(msTitle.toString());
        vt.setVisibility(View.GONE);


        mPlayer.prepareAndPlay(livetmpUrl);
//        mPlayer.prepareAndPlay("http://live.jnoo.com/shanmao1/259440.flv");
        if (mStatusListener != null)
            mStatusListener.notifyStatus(STATUS_START);

//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                mDecoderTypeView.setText(NDKCallback.getDecoderType() == 0 ? "HardDeCoder" : "SoftDecoder");
//            }
//        }, 5000);
        return true;

    }

    private void resetUI() {
        mSeekBar.setProgress(0);
        show_pause_ui(false, false);
        show_progress_ui(false);
        mErrInfoView.setText("");
    }

    //pause the video
    private void pause() {
        if (mPlayer != null) {
            mPlayer.pause();
            isPausePlayer = true;
            isPausedByUser = true;
            if (mStatusListener != null)
                mStatusListener.notifyStatus(STATUS_PAUSE);
            show_pause_ui(true, false);
            show_progress_ui(true);
        }
    }

    //start the video
    private void start() {

        if (mPlayer != null) {
            isPausePlayer = false;
            isPausedByUser = false;
            isStopPlayer = false;
            mPlayer.play();
            if (mStatusListener != null)
                mStatusListener.notifyStatus(STATUS_RESUME);
            show_pause_ui(false, false);
            show_progress_ui(false);
        }
    }

    //stop the video 
    private void stop() {
        Log.d(TAG, "AudioRender: stop play");
        if (mPlayer != null) {
            mPlayer.stop();
            if (mStatusListener != null)
                mStatusListener.notifyStatus(STATUS_STOP);
            mPlayer.destroy();
            mPlayer = null;
        }
    }

    private void getPlayUrl() {
        mPlayingIndex = getVideoSourcePath(mPlayingIndex, msURI, msTitle);
    }

    /**
     * 准备完成监听器:调度更新进度
     */
    private class VideoPreparedListener implements MediaPlayer.MediaPlayerPreparedListener {

        @Override
        public void onPrepared() {
            Log.d(TAG, "onPrepared");
            if (mPlayer != null) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                imageBack.setVisibility(View.GONE);
                mPlayer.setVideoScalingMode(MediaPlayer.VideoScalingMode.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
//                update_total_duration(mPlayer.getDuration());
                mTimerHandler.postDelayed(mRunnable, 1000);
//                show_progress_ui(true);
                mTimerHandler.postDelayed(mUIRunnable, 3000);
            }
        }
    }


    /**
     * 错误处理监听器
     */
    private class VideoErrorListener implements MediaPlayer.MediaPlayerErrorListener {

        public void onError(int what, int extra) {
            int errCode = 0;

            if (mPlayer == null) {
                return;
            }

            errCode = mPlayer.getErrorCode();
            switch (errCode) {
                case MediaPlayer.ALIVC_ERR_LOADING_TIMEOUT:
                    report_error("缓冲超时,请确认网络连接正常后重试", true);
                    UtilTool.ShowToast(PlayerActivity.this, "缓冲超时,请确认网络连接正常后重试");
                    mPlayer.reset();
                    finish();
                    break;
                case MediaPlayer.ALIVC_ERR_NO_INPUTFILE:
                    report_error("no input file", true);
                    UtilTool.ShowToast(PlayerActivity.this, "出现未知错误");
                    mPlayer.reset();
                    finish();
                    break;
                case MediaPlayer.ALIVC_ERR_NO_VIEW:
                    report_error("no surface", true);
                    UtilTool.ShowToast(PlayerActivity.this, "出现未知错误");
                    mPlayer.reset();
                    finish();
                    break;
                case MediaPlayer.ALIVC_ERR_INVALID_INPUTFILE:
                    report_error("视频资源或者网络不可用", true);
                    UtilTool.ShowToast(PlayerActivity.this, "视频资源或者网络不可用");
                    mPlayer.reset();
                    finish();
                    break;
                case MediaPlayer.ALIVC_ERR_NO_SUPPORT_CODEC:
                    UtilTool.ShowToast(PlayerActivity.this, "出现未知错误");
                    mPlayer.reset();
                    finish();
                    break;
                case MediaPlayer.ALIVC_ERR_FUNCTION_DENIED:
                    report_error("no priority", true);
                    UtilTool.ShowToast(PlayerActivity.this, "出现未知错误");
                    mPlayer.reset();
                    finish();
                    break;
                case MediaPlayer.ALIVC_ERR_UNKNOWN:
                    report_error("unknown error", true);
                    UtilTool.ShowToast(PlayerActivity.this, "出现未知错误");
                    mPlayer.reset();
                    finish();
                    break;
                case MediaPlayer.ALIVC_ERR_NO_NETWORK:
                    report_error("视频资源或者网络不可用", true);
                    UtilTool.ShowToast(PlayerActivity.this, "视频资源或者网络不可用");
                    mPlayer.reset();
                    finish();
                    break;
                case MediaPlayer.ALIVC_ERR_ILLEGALSTATUS:
                    report_error("illegal call", true);
                    UtilTool.ShowToast(PlayerActivity.this, "illegal call");
                    finish();
                    break;
                case MediaPlayer.ALIVC_ERR_NOTAUTH:
                    report_error("auth failed", true);
                    UtilTool.ShowToast(PlayerActivity.this, "auth failed");
                    finish();
                    break;
                case MediaPlayer.ALIVC_ERR_READD:
                    report_error("资源访问失败,请重试", true);
                    UtilTool.ShowToast(PlayerActivity.this, "资源访问失败,请重试");
                    mPlayer.reset();
                    finish();
                    break;
                default:
                    break;

            }
        }
    }

    /**
     * 信息通知监听器:重点是缓存开始/结束
     */
    private class VideoInfolistener implements MediaPlayer.MediaPlayerInfoListener {

        public void onInfo(int what, int extra) {
            Log.d(TAG, "onInfo what = " + what + " extra = " + extra);
            switch (what) {
                case MediaPlayer.MEDIA_INFO_UNKNOW:
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_START:
//                    pause();
                    show_buffering_ui(true);
                    checkVideo(false);
                    break;
                case MediaPlayer.MEDIA_INFO_BUFFERING_END:
//                    start();
                    show_buffering_ui(false);
                    whetherTiemer(false);
                    break;
                case MediaPlayer.MEDIA_INFO_TRACKING_LAGGING:
                    break;
                case MediaPlayer.MEDIA_INFO_NETWORK_ERROR:
                    report_error("系统错误", true);
                    UtilTool.ShowToast(PlayerActivity.this, "系统错误");
                    finish();
                    break;
                case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    if (mPlayer != null)
                        Log.d(TAG, "on Info first render start : " + ((long) mPlayer.getPropertyDouble(AliVcMediaPlayer.FFP_PROP_DOUBLE_1st_VFRAME_SHOW_TIME, -1) - (long) mPlayer.getPropertyDouble(AliVcMediaPlayer.FFP_PROP_DOUBLE_OPEN_STREAM_TIME, -1)));

                    break;
            }
        }
    }

    /**
     * 快进完成监听器
     */
    private class VideoSeekCompletelistener implements MediaPlayer.MediaPlayerSeekCompleteListener {

        public void onSeekCompleted() {
            mEnableUpdateProgress = true;
        }
    }

    /**
     * 视频播完监听器
     */
    private class VideoCompletelistener implements MediaPlayer.MediaPlayerCompletedListener {

        public void onCompleted() {
            Log.d(TAG, "onCompleted.");

            AlertDialog.Builder builder = new AlertDialog.Builder(PlayerActivity.this);
            builder.setMessage("播放结束");

            builder.setTitle("提示");


            builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    PlayerActivity.this.finish();
                }
            });

            builder.create().show();
        }
    }

    /**
     * 视频大小变化监听器
     */
    private class VideoSizeChangelistener implements MediaPlayer.MediaPlayerVideoSizeChangeListener {

        public void onVideoSizeChange(int width, int height) {
            Log.d(TAG, "onVideoSizeChange width = " + width + " height = " + height);
        }
    }

    /**
     * 视频缓存变化监听器: percent 为 0~100之间的数字】
     */
    private class VideoBufferUpdatelistener implements MediaPlayer.MediaPlayerBufferingUpdateListener {

        public void onBufferingUpdateListener(int percent) {

        }
    }

    private class VideoStoppedListener implements MediaPlayer.MediaPlayerStopedListener {
        @Override
        public void onStopped() {
            Log.d(TAG, "onVideoStopped.");
        }
    }

    @Override
    protected void onDestroy() {
        Log.e(TAG, "AudioRender: onDestroy.");
        EventBus.getDefault().unregister(this);
        if (mPlayer != null) {
//            stop();
            mTimerHandler.removeCallbacks(mRunnable);
        }

        releaseWakeLock();

        if (connectionReceiver != null) {
            unregisterReceiver(connectionReceiver);
        }
        // 重点:在 activity destroy的时候,要停止播放器并释放播放器
        if (mPlayer != null) {
            mPosition = mPlayer.getCurrentPosition();
            stop();
            if (mPlayerControl != null)
                mPlayerControl.stop();
        }
        handler.removeCallbacks(runnable);

        super.onDestroy();
        return;
    }

    @Override
    protected void onResume() {
        Log.e(TAG, "onResume");
        super.onResume();

        // 重点:如果播放器是从锁屏/后台切换到前台,那么调用player.stat
        if (mPlayer != null && !isStopPlayer && isPausePlayer) {
            if (!isPausedByUser) {
                isPausePlayer = false;
                mPlayer.play();
                // 更新ui
                show_pause_ui(false, false);
                show_progress_ui(false);
            }
        }
    }

    @Override
    protected void onStart() {
        Log.e(TAG, "onStart.");
        super.onStart();
        if (!isCurrentRunningForeground) {
            Log.d(TAG, ">>>>>>>>>>>>>>>>>>>切到前台 activity process");
        }
    }

    @Override
    protected void onPause() {
        Log.e(TAG, "onPause." + isStopPlayer + " " + isPausePlayer + " " + (mPlayer == null));
        super.onPause();
        // 重点:播放器没有停止,也没有暂停的时候,在activity的pause的时候也需要pause
        if (!isStopPlayer && !isPausePlayer && mPlayer != null) {
            Log.e(TAG, "onPause mpayer.");
            mPlayer.pause();
            isPausePlayer = true;
        }
    }

    @Override
    protected void onStop() {
        Log.e(TAG, "onStop.");
        super.onStop();

        isCurrentRunningForeground = isRunningForeground();
        if (!isCurrentRunningForeground) {
            Log.d(TAG, ">>>>>>>>>>>>>>>>>>>切到后台 activity process");
        }
    }

    private Handler mTimerHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case CMD_PAUSE:
                    pause();
                    break;
                case CMD_RESUME:
                    start();
                    break;
                case CMD_SEEK:
                    mPlayer.seekTo(msg.arg1);
                    break;
                case CMD_START:
                    startToPlay();
                    break;
                case CMD_STOP:
                    stop();
                    break;
                case CMD_VOLUME:
                    mPlayer.setVolume(msg.arg1);
                    break;
                default:
                    break;
            }
        }
    };
    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {

            if (mPlayer != null && mPlayer.isPlaying())
                update_progress(mPlayer.getCurrentPosition());

            mTimerHandler.postDelayed(this, 1000);
        }
    };

    Runnable mUIRunnable = new Runnable() {
        @Override
        public void run() {
            show_progress_ui(false);
        }
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isStopPlayer = true;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 重点:判定是否在前台工作
    public boolean isRunningForeground() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(this.getApplicationInfo().processName)) {
                    Log.d(TAG, "EntryActivity isRunningForeGround");
                    return true;
                }
            }
        }
        Log.d(TAG, "EntryActivity isRunningBackGround");
        return false;
    }

}
