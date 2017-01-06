package com.example.administrator.learn;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.alibaba.livecloud.event.AlivcEvent;
import com.alibaba.livecloud.event.AlivcEventResponse;
import com.alibaba.livecloud.event.AlivcEventSubscriber;
import com.alibaba.livecloud.live.AlivcMediaFormat;
import com.alibaba.livecloud.live.AlivcMediaRecorder;
import com.alibaba.livecloud.live.AlivcMediaRecorderFactory;
import com.alibaba.livecloud.live.AlivcRecordReporter;
import com.alibaba.livecloud.live.AlivcStatusCode;
import com.alibaba.livecloud.live.OnLiveRecordErrorListener;
import com.alibaba.livecloud.live.OnNetworkStatusListener;
import com.alibaba.livecloud.live.OnRecordStatusListener;
import com.alibaba.livecloud.model.AlivcWatermark;
import com.duanqu.qupai.logger.DataStatistics;
import com.example.administrator.learn.Model.StartPushInfo;
import com.example.administrator.learn.Model.apiSuccessInfo;
import com.example.administrator.learn.ServceTool.ApiService;
import com.example.administrator.learn.Tool.SPUtils;
import com.example.administrator.learn.Tool.SelecPopuview;
import com.example.administrator.learn.Tool.ShareUtils;
import com.example.administrator.learn.Tool.Sharedparms;
import com.example.administrator.learn.Tool.UtilTool;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 *
 */
public class PushFlowActivity extends Activity {

    @InjectView(R.id.image_xx)
    ImageView imageXx;
    @InjectView(R.id.image_cameraresvise)
    ImageView imageCameraresvise;
    @InjectView(R.id.ed_title)
    EditText edTitle;
    @InjectView(R.id.layout_weibo)
    RelativeLayout layoutWeibo;
    @InjectView(R.id.layout_wexin)
    RelativeLayout layoutWexin;
    @InjectView(R.id.layout_friend)
    RelativeLayout layoutFriend;
    @InjectView(R.id.layout_qq)
    RelativeLayout layoutQq;
    @InjectView(R.id.layout_qqzone)
    RelativeLayout layoutQqzone;
    @InjectView(R.id.btn_startpush)
    Button btnStartpush;
    @InjectView(R.id.pushWebview)
    WebView pushWebview;
    @InjectView(R.id.layout_webview)
    RelativeLayout layoutWebview;
    @InjectView(R.id.image_weibo)
    ImageView imageWeibo;
    @InjectView(R.id.image_weixin)
    ImageView imageWeixin;
    @InjectView(R.id.image_friend)
    ImageView imageFriend;
    @InjectView(R.id.image_qq)
    ImageView imageQq;
    @InjectView(R.id.image_qqzone)
    ImageView imageQqzone;
    private View layout_startpush;//是添加标题那个view
    private ProgressDialog progressDialog;
    private AlertDialog.Builder builder;

    @OnClick({R.id.btn_startpush, R.id.image_cameraresvise, R.id.image_xx, R.id.layout_weibo, R.id.layout_wexin, R.id.layout_friend
            , R.id.layout_qq, R.id.layout_qqzone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_startpush://开始直播
                String title = edTitle.getText().toString().trim();
                if (TextUtils.isEmpty(title)) {
                    UtilTool.ShowToast(PushFlowActivity.this, "标题不能为空");
                    return;
                }
                startPush(title,-2,false);
                break;
            case R.id.image_cameraresvise://设置摄像头
                int currFacing = mMediaRecorder.switchCamera();
                if (currFacing == AlivcMediaFormat.CAMERA_FACING_FRONT) {
                    mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_BEAUTY_ON);
                }
                mConfigure.put(AlivcMediaFormat.KEY_CAMERA_FACING, currFacing);
                break;
            case R.id.image_xx://关闭
                builder.show();
                break;
            case R.id.layout_weibo://微博
                setshareimage(WEIBO);
                break;
            case R.id.layout_wexin://微信
                setshareimage(WEIXIN);
                break;
            case R.id.layout_friend://朋友圈
                setshareimage(FRIEND);
                break;
            case R.id.layout_qq://qq
                setshareimage(QQ);
                break;
            case R.id.layout_qqzone://空间
                setshareimage(QQZONE);
                break;
        }
    }

    /**
     * 显示popuview对话框
     */
    private void showPopuViewDialog() {
        SelecPopuview picPopupWindow = new SelecPopuview(this, new SelecPopuview.setonListener() {
            @Override
            public void setonlistfener(View view, int index) {
                switch (index) {
                    case SelecPopuview.WEIBO:
                        ShareUtils.shareSinaWei(PushFlowActivity.this, "", "http://img1.imgtn.bdimg.com/it/u=1222734533,741800870&fm=21&gp=0.jpg", true, setShareListener);
                        break;
                    case SelecPopuview.WEIXIN:
                        ShareUtils.shareweixin(PushFlowActivity.this, "title", "title", "http://img1.imgtn.bdimg.com/it/u=1222734533,741800870&fm=21&gp=0.jpg", "http://blog.csdn.net/liguangzhenghi/article/details/8076361", true, setShareListener);
                        break;
                    case SelecPopuview.FRIEND:
                        ShareUtils.shareWechatMoments(PushFlowActivity.this, "title", "title", "http://img1.imgtn.bdimg.com/it/u=1222734533,741800870&fm=21&gp=0.jpg", "http://blog.csdn.net/liguangzhenghi/article/details/8076361", true, setShareListener);
                        break;
                    case SelecPopuview.QQ:
                        ShareUtils.shareQQ(PushFlowActivity.this, "title", "http://blog.csdn.net/liguangzhenghi/article/details/8076361", "http://img1.imgtn.bdimg.com/it/u=1222734533,741800870&fm=21&gp=0.jpg", true, setShareListener);
                        break;
                    case SelecPopuview.QQZONE:
                        ShareUtils.shareQZone(PushFlowActivity.this, "title", "http://blog.csdn.net/liguangzhenghi/article/details/8076361", "http://img1.imgtn.bdimg.com/it/u=1222734533,741800870&fm=21&gp=0.jpg", "同城秀秀", true, setShareListener);
                        break;
                }
            }
        });
        //设置layout在PopupWindow中显示的位置
        picPopupWindow.showAtLocation(PushFlowActivity.this.findViewById(R.id.btn_startpush), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

    }

    /**开始直播接口
     * @param title
     * @param index
     * @param isShare  是否先要分享再直播
     */
    private void startPush(final String title, final int index, final boolean isShare) {
        progressDialog = ProgressDialog.show(this, null, "处理中");
        progressDialog.show();
        ApiService.StartPush(title, SPUtils.getUid(this), SPUtils.getimage_url(this), new ApiService.ParsedRequestListener<StartPushInfo>() {
            @Override
            public void onResponseResult(StartPushInfo StartPushInfo) {
                progressDialog.dismiss();
                if (StartPushInfo.getStatus() == Sharedparms.statusSuccess) {
                    SPUtils.Putliveid(PushFlowActivity.this, StartPushInfo.getData().getLiveId() + "");
                    SPUtils.Putshare_url(PushFlowActivity.this,StartPushInfo.getData().getShare_url());
                    UtilTool.ShowToast(PushFlowActivity.this,StartPushInfo.getData().getLiveId()+"");
                    pushWebview.loadUrl(Sharedparms.WEBPUSH+StartPushInfo.getData().getLiveId());
                    if (isShare){
                        //分享
                        share(index,title,StartPushInfo.getData().getShare_url());
                    }else {
                        startPushAPI();
                    }
                } else {
                    UtilTool.ShowToast(PushFlowActivity.this, StartPushInfo.getMsg());
                }
            }

            @Override
            public void _OnError(String errormessage) {
                UtilTool.ShowToast(PushFlowActivity.this, errormessage);
                progressDialog.dismiss();

            }
        });

    }

    /**
     * 功能上 -开始直播
     */
    private void startPushAPI(){
        //开始推流
                    layout_startpush.setVisibility(View.GONE);
                    layoutWebview.setVisibility(View.VISIBLE);
                    mMediaRecorder.startRecord(pushUrl);
                    isRecording = true;
    }

    /**分享
     * @param index
     */
    private void share(int index,String title,String share_url) {
        switch (index){
            case WEIBO:
                ShareUtils.shareSinaWei(PushFlowActivity.this, title, SPUtils.getimage_url(PushFlowActivity.this), false, setShareListener);
                break;
            case WEIXIN:
                ShareUtils.shareweixin(PushFlowActivity.this, title, title, SPUtils.getimage_url(PushFlowActivity.this),share_url , false, setShareListener);
                break;
            case FRIEND:
                ShareUtils.shareWechatMoments(PushFlowActivity.this, title, title, SPUtils.getimage_url(PushFlowActivity.this), share_url, false, setShareListener);
                break;
            case QQ:
                ShareUtils.shareQQ(PushFlowActivity.this, title, share_url, SPUtils.getimage_url(PushFlowActivity.this), false, setShareListener);
                break;
            case QQZONE:
                ShareUtils.shareQZone(PushFlowActivity.this, title,share_url, SPUtils.getimage_url(PushFlowActivity.this), "同城秀秀", false, setShareListener);
                break;
        }
    }

    private static final int WEIBO = 1;
    private static final int WEIXIN = 2;
    private static final int FRIEND = 3;
    private static final int QQ = 4;
    private static final int QQZONE = 5;

    /**
     * 设置分享按钮
     *
     * @param index
     */
    private void setshareimage(int index) {
        String title = edTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            UtilTool.ShowToast(PushFlowActivity.this, "标题不能为空");
            return;
        }
        setshareimageDefault();
        switch (index) {
            case WEIBO:
                imageWeibo.setImageResource(R.mipmap.weibo_selector);
                startPush(title,WEIBO,true);//开始直播
                break;
            case WEIXIN:
                imageWeixin.setImageResource(R.mipmap.wexin_selector);
                startPush(title,WEIXIN,true);//开始直播
                break;
            case FRIEND:
                imageFriend.setImageResource(R.mipmap.friend_selector);
                startPush(title,FRIEND,true);//开始直播
                break;
            case QQ:
                imageQq.setImageResource(R.mipmap.qq_selector);
                startPush(title,QQ,true);//开始直播
                break;
            case QQZONE:
                imageQqzone.setImageResource(R.mipmap.zoneqq_selector);
                startPush(title,QQZONE,true);//开始直播
                break;
        }
    }

    ShareUtils.setShareListener setShareListener = new ShareUtils.setShareListener() {

        @Override
        public void shareSuccess(boolean issuccess, boolean iscallback) {
            if (issuccess) {
                startPushAPI();
                if (iscallback) {
                    //在这做分享成功后告诉后端
                    shareLiveId();

                }
            }

        }
    };

    /**
     * 分享后接口
     */
    private void shareLiveId() {
        ApiService.ShareLive(SPUtils.getliveid(this), new ApiService.ParsedRequestListener<apiSuccessInfo>() {
            @Override
            public void onResponseResult(apiSuccessInfo apiSuccessInfo) {
                if (apiSuccessInfo.getStatus() == Sharedparms.statusSuccess) {
                    UtilTool.ShowToast(PushFlowActivity.this, "接口成功");
                } else {
                    UtilTool.ShowToast(PushFlowActivity.this, "" + apiSuccessInfo.getMsg());
                }

            }

            @Override
            public void _OnError(String errormessage) {
                UtilTool.ShowToast(PushFlowActivity.this, errormessage);
            }
        });
    }

    /**
     * 分享图标全部设置为默认
     */
    private void setshareimageDefault() {
        imageFriend.setImageResource(R.mipmap.freiend_default);
        imageQq.setImageResource(R.mipmap.qq_default);
        imageQqzone.setImageResource(R.mipmap.zoneqq_default);
        imageWeibo.setImageResource(R.mipmap.weibo_default);
        imageWeixin.setImageResource(R.mipmap.weixin_default);
    }

    public static class RequestBuilder {
        String rtmpUrl;
        int videoResolution;
        boolean isPortrait;
        int cameraFacing;
        String watermarkUrl;
        int dx;
        int dy;
        int site;
        int bestBitrate;
        int minBitrate;
        int maxBitrate;
        int initBitrate;
        int frameRate;

        public RequestBuilder rtmpUrl(String url) {
            this.rtmpUrl = url;
            return this;
        }

        public RequestBuilder videoResolution(int resolution) {
            this.videoResolution = resolution;
            return this;
        }

        public RequestBuilder portrait(boolean isPortrait) {
            this.isPortrait = isPortrait;
            return this;
        }

        public RequestBuilder cameraFacing(int cameraFacing) {
            this.cameraFacing = cameraFacing;
            return this;
        }

        public RequestBuilder watermarkUrl(String url) {
            this.watermarkUrl = url;
            return this;
        }

        public RequestBuilder dx(int dx) {
            this.dx = dx;
            return this;
        }

        public RequestBuilder dy(int dy) {
            this.dy = dy;
            return this;
        }

        public RequestBuilder site(int site) {
            this.site = site;
            return this;
        }

        public RequestBuilder bestBitrate(int bestBitrate) {
            this.bestBitrate = bestBitrate;
            return this;
        }

        public RequestBuilder minBitrate(int minBitrate) {
            this.minBitrate = minBitrate;
            return this;
        }

        public RequestBuilder maxBitrate(int maxBitrate) {
            this.maxBitrate = maxBitrate;
            return this;
        }

        public RequestBuilder initBitrate(int initBitrate) {
            this.initBitrate = initBitrate;
            return this;
        }

        public RequestBuilder frameRate(int frameRate) {
            this.frameRate = frameRate;
            return this;
        }

        public Intent build(Context context) {
            Intent intent = new Intent(context, PushFlowActivity.class);
            intent.putExtra(URL, rtmpUrl);
            intent.putExtra(VIDEO_RESOLUTION, videoResolution);
            intent.putExtra(SCREENORIENTATION, isPortrait);
            intent.putExtra(FRONT_CAMERA_FACING, cameraFacing);
            intent.putExtra(WATERMARK_PATH, watermarkUrl);
            intent.putExtra(WATERMARK_DX, dx);
            intent.putExtra(WATERMARK_DY, dy);
            intent.putExtra(WATERMARK_SITE, site);
            intent.putExtra(BEST_BITRATE, bestBitrate);
            intent.putExtra(MIN_BITRATE, minBitrate);
            intent.putExtra(MAX_BITRATE, maxBitrate);
            intent.putExtra(INIT_BITRATE, initBitrate);
            intent.putExtra(FRAME_RATE, frameRate);
            return intent;
        }

    }


    private static final String TAG = "PushFlowActivity";

    public final static String URL = "url";
    public final static String VIDEO_RESOLUTION = "video_resolution";
    public final static String SCREENORIENTATION = "screen_orientation";
    public final static String FRONT_CAMERA_FACING = "front_camera_face";

    public final static String WATERMARK_PATH = "watermark_path";
    public final static String WATERMARK_DX = "watermark_dx";
    public final static String WATERMARK_DY = "watermark_dy";
    public final static String WATERMARK_SITE = "watermark_site";
    public final static String BEST_BITRATE = "best-bitrate";
    public final static String MIN_BITRATE = "min-bitrate";
    public final static String MAX_BITRATE = "max-bitrate";
    public final static String INIT_BITRATE = "init-bitrate";
    public final static String FRAME_RATE = "frame-rate";

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String[] permissionManifest = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };
    private final int PERMISSION_DELAY = 100;
    private boolean mHasPermission = true;

    private SurfaceView _CameraSurface;
    private AlivcMediaRecorder mMediaRecorder;
    private AlivcRecordReporter mRecordReporter;
//    private ToggleButton mTbtnMute;

    private Surface mPreviewSurface;
    private Map<String, Object> mConfigure = new HashMap<>();
    private boolean isRecording = false;
    private int mPreviewWidth = 0;
    private int mPreviewHeight = 0;
    private DataStatistics mDataStatistics = new DataStatistics(1000);

    public static void startActivity(Context context,
                                     RequestBuilder builder) {
        Intent intent = builder.build(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        RecordLoggerManager.createLoggerFile();
        setContentView(R.layout.activity_push_flow);
        ButterKnife.inject(this);
        if (Build.VERSION.SDK_INT >= 23) {
//            permissionCheck();
        } else {
            mHasPermission = true;
        }

        getExtraData();


        setRequestedOrientation(screenOrientation ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //采集
        _CameraSurface = (SurfaceView) findViewById(R.id.camera_surface);
        _CameraSurface.getHolder().addCallback(_CameraSurfaceCallback);
        _CameraSurface.setOnTouchListener(mOnTouchListener);
        layout_startpush = findViewById(R.id.layout_startpush);
        ///
        layoutWebview.setVisibility(View.GONE);
        layout_startpush.setVisibility(View.VISIBLE);
        setVebView();
        //对焦，缩放
        mDetector = new GestureDetector(_CameraSurface.getContext(), mGestureDetector);
        mScaleDetector = new ScaleGestureDetector(_CameraSurface.getContext(), mScaleGestureListener);

        mMediaRecorder = AlivcMediaRecorderFactory.createMediaRecorder();
        mMediaRecorder.init(this);
        mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_BEAUTY_ON);
        mDataStatistics.setReportListener(mReportListener);

        /**
         * this method only can be called after mMediaRecorder.init(),
         * else will return null;
         */
        mRecordReporter = mMediaRecorder.getRecordReporter();

        mDataStatistics.start();
        mMediaRecorder.setOnRecordStatusListener(mRecordStatusListener);
        mMediaRecorder.setOnNetworkStatusListener(mOnNetworkStatusListener);
        mMediaRecorder.setOnRecordErrorListener(mOnErrorListener);

        mConfigure.put(AlivcMediaFormat.KEY_CAMERA_FACING, cameraFrontFacing);
        mConfigure.put(AlivcMediaFormat.KEY_MAX_ZOOM_LEVEL, 3);
        mConfigure.put(AlivcMediaFormat.KEY_OUTPUT_RESOLUTION, resolution);
        mConfigure.put(AlivcMediaFormat.KEY_MAX_VIDEO_BITRATE, maxBitrate * 1000);
        mConfigure.put(AlivcMediaFormat.KEY_BEST_VIDEO_BITRATE, bestBitrate * 1000);
        mConfigure.put(AlivcMediaFormat.KEY_MIN_VIDEO_BITRATE, minBitrate * 1000);
        mConfigure.put(AlivcMediaFormat.KEY_INITIAL_VIDEO_BITRATE, initBitrate * 1000);
        mConfigure.put(AlivcMediaFormat.KEY_DISPLAY_ROTATION, screenOrientation ? AlivcMediaFormat.DISPLAY_ROTATION_90 : AlivcMediaFormat.DISPLAY_ROTATION_0);
        mConfigure.put(AlivcMediaFormat.KEY_EXPOSURE_COMPENSATION, -1);//曝光度
        mConfigure.put(AlivcMediaFormat.KEY_WATERMARK, mWatermark);
        mConfigure.put(AlivcMediaFormat.KEY_FRAME_RATE, frameRate);
        mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_BEAUTY_ON);
        builder = new AlertDialog.Builder(this);
        builder.setMessage("                  确定退出直播");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isRecording){
                     //如果已经开始直播了就要到结束直播那
                    startActivity(new Intent(PushFlowActivity.this, StopPushActivity.class));
                }
                finish();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }

    private void setVebView() {
        WebSettings webSettings = pushWebview.getSettings();
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
        pushWebview.setBackgroundColor(0); // 设置背景色
        pushWebview.addJavascriptInterface(new javaScriptObjcet(), "jnoo");
        pushWebview.setWebChromeClient(new WebChromeClient());
    }


    /**
     * js
     */
    public class javaScriptObjcet {
        @JavascriptInterface
        public void livelist11() {
            //开始直播
        }
    }

    private String pushUrl;
    private int resolution;
    private boolean screenOrientation;
    private int cameraFrontFacing;
    private AlivcWatermark mWatermark;
    private int bestBitrate;
    private int minBitrate;
    private int maxBitrate;
    private int initBitrate;
    private int frameRate;

    private void getExtraData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pushUrl = bundle.getString(URL);
            resolution = bundle.getInt(VIDEO_RESOLUTION);
            screenOrientation = bundle.getBoolean(SCREENORIENTATION);
            cameraFrontFacing = bundle.getInt(FRONT_CAMERA_FACING);
            mWatermark = new AlivcWatermark.Builder()
                    .watermarkUrl(bundle.getString(WATERMARK_PATH))
                    .paddingX(bundle.getInt(WATERMARK_DX))
                    .paddingY(bundle.getInt(WATERMARK_DY))
                    .site(bundle.getInt(WATERMARK_SITE))
                    .build();
            minBitrate = bundle.getInt(MIN_BITRATE);
            maxBitrate = bundle.getInt(MAX_BITRATE);
            bestBitrate = bundle.getInt(BEST_BITRATE);
            initBitrate = bundle.getInt(INIT_BITRATE);
            frameRate = bundle.getInt(FRAME_RATE);
        }
    }

//    private void permissionCheck() {
//        int permissionCheck = PackageManager.PERMISSION_GRANTED;
//        for (String permission : permissionManifest) {
//            if (PermissionChecker.checkSelfPermission(this, permission)
//                    != PackageManager.PERMISSION_GRANTED) {
//                permissionCheck = PackageManager.PERMISSION_DENIED;
//            }
//        }
//        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
//        }else {
//            mHasPermission = true;
//        }
//    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPreviewSurface != null) {
            mMediaRecorder.prepare(mConfigure, mPreviewSurface);
            Log.d("AlivcMediaRecorder", " onResume==== isRecording =" + isRecording + "=====");
        }
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_BITRATE_DOWN, mBitrateDownRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_BITRATE_RAISE, mBitrateUpRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_AUDIO_CAPTURE_OPEN_SUCC, mAudioCaptureSuccRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_DATA_DISCARD, mDataDiscardRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_INIT_DONE, mInitDoneRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_VIDEO_ENCODER_OPEN_SUCC, mVideoEncoderSuccRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_VIDEO_ENCODER_OPEN_FAILED, mVideoEncoderFailedRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_VIDEO_ENCODED_FRAMES_FAILED, mVideoEncodeFrameFailedRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_AUDIO_ENCODED_FRAMES_FAILED, mAudioEncodeFrameFailedRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_AUDIO_CAPTURE_OPEN_FAILED, mAudioCaptureOpenFailedRes));
    }

    @Override
    protected void onPause() {
        if (isRecording) {
            mMediaRecorder.stopRecord();
        }
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_BITRATE_DOWN);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_BITRATE_RAISE);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_AUDIO_CAPTURE_OPEN_SUCC);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_DATA_DISCARD);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_INIT_DONE);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_VIDEO_ENCODER_OPEN_SUCC);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_VIDEO_ENCODER_OPEN_FAILED);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_VIDEO_ENCODED_FRAMES_FAILED);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_AUDIO_ENCODED_FRAMES_FAILED);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_AUDIO_CAPTURE_OPEN_FAILED);
        /**
         * 如果要调用stopRecord和reset()方法，则stopRecord（）必须在reset之前调用，否则将会抛出IllegalStateException
         */
        mMediaRecorder.reset();
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        RecordLoggerManager.closeLoggerFile();
        mDataStatistics.stop();
        mMediaRecorder.release();
        mMediaRecorder.stopRecord();
        mMediaRecorder.reset();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            builder.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void testPublish(boolean isPublish, final String url) {
        if (isPublish) {
            mMediaRecorder.startRecord(url);
            Log.d(TAG, "Start Record Time:" + System.currentTimeMillis());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    testPublish(false, url);
                }
            }, 10000);
        } else {
            mMediaRecorder.stopRecord();
            Log.d(TAG, "Stop Record Time:" + System.currentTimeMillis());
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    testPublish(true, url);
                }
            }, 500);
        }
    }

    private Handler mHandler = new Handler();
    private GestureDetector mDetector;
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector.OnGestureListener mGestureDetector = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            if (mPreviewWidth > 0 && mPreviewHeight > 0) {
                float x = motionEvent.getX() / mPreviewWidth;
                float y = motionEvent.getY() / mPreviewHeight;
                mMediaRecorder.focusing(x, y);
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }
    };

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mDetector.onTouchEvent(motionEvent);
            mScaleDetector.onTouchEvent(motionEvent);
            return true;
        }
    };

    private ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            mMediaRecorder.setZoom(scaleGestureDetector.getScaleFactor());
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }
    };

    private void startPreview(final SurfaceHolder holder) {
        if (!mHasPermission) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startPreview(holder);
                }
            }, PERMISSION_DELAY);
            return;
        }
        mMediaRecorder.prepare(mConfigure, mPreviewSurface);
        mMediaRecorder.setPreviewSize(_CameraSurface.getMeasuredWidth(), _CameraSurface.getMeasuredHeight());
        if ((int) mConfigure.get(AlivcMediaFormat.KEY_CAMERA_FACING) == AlivcMediaFormat.CAMERA_FACING_FRONT) {
            mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_BEAUTY_ON);
        }
    }

    private final SurfaceHolder.Callback _CameraSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            holder.setKeepScreenOn(true);
            mPreviewSurface = holder.getSurface();
            startPreview(holder);
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            mMediaRecorder.setPreviewSize(width, height);
            mPreviewWidth = width;
            mPreviewHeight = height;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            mPreviewSurface = null;
            mMediaRecorder.stopRecord();
            mMediaRecorder.reset();
        }
    };


    private OnRecordStatusListener mRecordStatusListener = new OnRecordStatusListener() {
        @Override
        public void onDeviceAttach() {
//            mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_AUTO_FOCUS_ON);
        }

        @Override
        public void onDeviceAttachFailed(int facing) {

        }

        @Override
        public void onSessionAttach() {
            if (isRecording && !TextUtils.isEmpty(pushUrl)) {
                mMediaRecorder.startRecord(pushUrl);
            }
            mMediaRecorder.focusing(0.5f, 0.5f);
        }

        @Override
        public void onSessionDetach() {

        }

        @Override
        public void onDeviceDetach() {

        }

        @Override
        public void onIllegalOutputResolution() {
            Log.d(TAG, "selected illegal output resolution");
            Toast.makeText(PushFlowActivity.this, R.string.illegal_output_resolution, Toast.LENGTH_SHORT).show();
        }
    };


    private OnNetworkStatusListener mOnNetworkStatusListener = new OnNetworkStatusListener() {
        @Override
        public void onNetworkBusy() {
            Log.d("network_status", "==== on network busy ====");
            Toast.makeText(PushFlowActivity.this, "当前网络状态极差，已无法正常流畅直播，确认要继续直播吗？", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNetworkFree() {
            Toast.makeText(PushFlowActivity.this, "network free", Toast.LENGTH_SHORT).show();
            Log.d("network_status", "===== on network free ====");
        }

        @Override
        public void onConnectionStatusChange(int status) {
            Log.d(TAG, "ffmpeg Live stream connection status-->" + status);

            switch (status) {
                case AlivcStatusCode.STATUS_CONNECTION_START:
                    Toast.makeText(PushFlowActivity.this, "Start live stream connection!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Start live stream connection!");
                    break;
                case AlivcStatusCode.STATUS_CONNECTION_ESTABLISHED:
                    Log.d(TAG, "Live stream connection is established!");
//                    showIllegalArgumentDialog("链接成功");
                    Toast.makeText(PushFlowActivity.this, "Live stream connection is established!", Toast.LENGTH_SHORT).show();
                    break;
                case AlivcStatusCode.STATUS_CONNECTION_CLOSED:
                    Log.d(TAG, "Live stream connection is closed!");
                    Toast.makeText(PushFlowActivity.this, "Live stream connection is closed!", Toast.LENGTH_SHORT).show();
//                    mLiveRecorder.stop();
//                    mLiveRecorder.release();
//                    mLiveRecorder = null;
//                    mMediaRecorder.stopRecord();
                    break;
            }
        }

//        @Override
//        public void onFirstReconnect() {
//            ToastUtils.showToast(LiveCameraActivity.this, "首次重连");
//        }


        @Override
        public boolean onNetworkReconnectFailed() {
            Log.d(TAG, "Reconnect timeout, not adapt to living");
            Toast.makeText(PushFlowActivity.this, "长时间重连失败，已不适合直播，请退出", Toast.LENGTH_SHORT).show();
            mMediaRecorder.stopRecord();
            showIllegalArgumentDialog("网络重连失败");
            return false;
        }
    };

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSION_REQUEST_CODE:
//                boolean hasPermission = true;
//                for (int i = 0; i < permissions.length; i++) {
//                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                        int toastTip = 0;
//                        if (android.Manifest.permission.CAMERA.equals(permissions[i])) {
//                            toastTip = R.string.no_camera_permission;
//                        } else if (android.Manifest.permission.RECORD_AUDIO.equals(permissions[i])) {
//                            toastTip = R.string.no_record_audio_permission;
//                        }
//                        if (toastTip != 0) {
//                            Toast.makeText(PushFlowActivity.this,toastTip,Toast.LENGTH_SHORT).show();
//                            hasPermission = false;
//                        }
//                    }
//                }
//                mHasPermission = hasPermission;
//                break;
//        }
//    }


    public void showIllegalArgumentDialog(String message) {
        if (illegalArgumentDialog == null) {
            illegalArgumentDialog = new AlertDialog.Builder(this)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            illegalArgumentDialog.dismiss();
                        }
                    })
                    .setTitle("提示")
                    .create();
        }
        illegalArgumentDialog.dismiss();
        illegalArgumentDialog.setMessage(message);
        illegalArgumentDialog.show();
    }

    AlertDialog illegalArgumentDialog = null;

    private OnLiveRecordErrorListener mOnErrorListener = new OnLiveRecordErrorListener() {
        @Override
        public void onError(int errorCode) {
            Log.d(TAG, "Live stream connection error-->" + errorCode);

            switch (errorCode) {
                case AlivcStatusCode.ERROR_ILLEGAL_ARGUMENT:
                    showIllegalArgumentDialog("-22错误产生");
                case AlivcStatusCode.ERROR_SERVER_CLOSED_CONNECTION:
                case AlivcStatusCode.ERORR_OUT_OF_MEMORY:
                case AlivcStatusCode.ERROR_CONNECTION_TIMEOUT:
                case AlivcStatusCode.ERROR_BROKEN_PIPE:
                case AlivcStatusCode.ERROR_IO:
                case AlivcStatusCode.ERROR_NETWORK_UNREACHABLE:
                    Toast.makeText(PushFlowActivity.this, "Live stream connection error-->" + errorCode, Toast.LENGTH_SHORT).show();

                    break;

                default:
            }
        }
    };

    DataStatistics.ReportListener mReportListener = new DataStatistics.ReportListener() {
        @Override
        public void onInfoReport() {
            runOnUiThread(mLoggerReportRunnable);
        }
    };
    private Runnable mLoggerReportRunnable = new Runnable() {
        @Override
        public void run() {
            if (mRecordReporter != null) {
               //  UtilTool.ShowToast(PushFlowActivity.this, mRecordReporter.getInt(AlivcRecordReporter.VIDEO_OUTPUT_FPS) + "fps");
            }
        }
    };

    private AlivcEventResponse mBitrateUpRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Bundle bundle = event.getBundle();
            int preBitrate = bundle.getInt(AlivcEvent.EventBundleKey.KEY_PRE_BITRATE);
            int currBitrate = bundle.getInt(AlivcEvent.EventBundleKey.KEY_CURR_BITRATE);
            Log.d(TAG, "event->up bitrate, previous bitrate is " + preBitrate +
                    "current bitrate is " + currBitrate);
            Toast.makeText(PushFlowActivity.this, "event->up bitrate, previous bitrate is " + preBitrate +
                    "current bitrate is " + currBitrate, Toast.LENGTH_SHORT).show();
        }
    };
    private AlivcEventResponse mBitrateDownRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Bundle bundle = event.getBundle();
            int preBitrate = bundle.getInt(AlivcEvent.EventBundleKey.KEY_PRE_BITRATE);
            int currBitrate = bundle.getInt(AlivcEvent.EventBundleKey.KEY_CURR_BITRATE);
            Log.d(TAG, "event->down bitrate, previous bitrate is " + preBitrate +
                    "current bitrate is " + currBitrate);
            Toast.makeText(PushFlowActivity.this, "event->down bitrate, previous bitrate is " + preBitrate +
                    "current bitrate is " + currBitrate, Toast.LENGTH_SHORT).show();
        }
    };
    private AlivcEventResponse mAudioCaptureSuccRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Log.d(TAG, "event->audio recorder start success");
            Toast.makeText(PushFlowActivity.this, "event->audio recorder start success", Toast.LENGTH_SHORT).show();
        }
    };

    private AlivcEventResponse mVideoEncoderSuccRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Log.d(TAG, "event->video encoder start success");
            Toast.makeText(PushFlowActivity.this, "event->video encoder start success", Toast.LENGTH_SHORT).show();
        }
    };
    private AlivcEventResponse mVideoEncoderFailedRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Log.d(TAG, "event->video encoder start failed");
            Toast.makeText(PushFlowActivity.this, "event->video encoder start failed", Toast.LENGTH_SHORT).show();
        }
    };
    private AlivcEventResponse mVideoEncodeFrameFailedRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Log.d(TAG, "event->video encode frame failed");
            Toast.makeText(PushFlowActivity.this, "event->video encode frame failed", Toast.LENGTH_SHORT).show();
        }
    };


    private AlivcEventResponse mInitDoneRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Log.d(TAG, "event->live recorder initialize completely");
            Toast.makeText(PushFlowActivity.this, "event->live recorder initialize completely", Toast.LENGTH_SHORT).show();
        }
    };

    private AlivcEventResponse mDataDiscardRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Bundle bundle = event.getBundle();
            int discardFrames = 0;
            if (bundle != null) {
                discardFrames = bundle.getInt(AlivcEvent.EventBundleKey.KEY_DISCARD_FRAMES);
            }
            Log.d(TAG, "event->data discard, the frames num is " + discardFrames);
            Toast.makeText(PushFlowActivity.this, "event->data discard, the frames num is ", Toast.LENGTH_SHORT).show();
        }
    };

    private AlivcEventResponse mAudioCaptureOpenFailedRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Log.d(TAG, "event-> audio capture device open failed");
            Toast.makeText(PushFlowActivity.this, "event-> audio capture device open failed", Toast.LENGTH_SHORT).show();
        }
    };

    private AlivcEventResponse mAudioEncodeFrameFailedRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Log.d(TAG, "event-> audio encode frame failed");
            Toast.makeText(PushFlowActivity.this, "event-> audio encode frame failed", Toast.LENGTH_SHORT).show();
        }
    };
}
