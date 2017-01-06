package com.example.administrator.learn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.administrator.learn.Model.PersonalInfo;
import com.example.administrator.learn.Model.putPictureInfo;
import com.example.administrator.learn.ServceTool.ApiService;
import com.example.administrator.learn.Tool.CameraGrid;
import com.example.administrator.learn.Tool.CameraPreview;
import com.example.administrator.learn.Tool.SPUtils;
import com.example.administrator.learn.Tool.Sharedparms;
import com.example.administrator.learn.Tool.UtilTool;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static android.R.attr.type;

public class CameraActivity extends Activity implements View.OnTouchListener {
    public static final int CAMERA_TYPE_1 = 1;
    public static final int CAMERA_TYPE_2 = 2;
    @InjectView(R.id.layout_back)
    RelativeLayout layoutBack;
    @InjectView(R.id.tv_title)
    TextView tvTitle;
    @InjectView(R.id.image_camerafan)
    ImageView imageCamerafan;
    @InjectView(R.id.tv_camera)
    TextView tvCamera;
    @InjectView(R.id.image_camerafan)
    ImageView image_camerafan;
    @InjectView(R.id.surfaceView)
    SurfaceView surfaceView;
    @InjectView(R.id.camera_grid)
    CameraGrid cameraGrid;
    @InjectView(R.id.btn_camera)
    ImageView btnCamera;
    @InjectView(R.id.layout_button)
    RelativeLayout layoutButton;
    @InjectView(R.id.image_show)
    ImageView imageShow;
    @InjectView(R.id.layout_right)
    RelativeLayout layoutRight;
    @InjectView(R.id.tv_cancel)
    TextView tvCancel;
    @InjectView(R.id.tv_save)
    TextView tvSave;
    private CameraPreview preview;
    private int mCurrentCameraId = 1; // 1是前置 0是后置
    private Camera camera;
    private ProgressDialog progressDialog;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ButterKnife.inject(this);
        initView();
        InitData();
        getPersonalinfo();
    }

    /**
     * 获取个人信息
     */
    private void getPersonalinfo() {
        String uid = SPUtils.getUid(this);
        if (TextUtils.isEmpty(uid)) {
            UtilTool.ShowToast(this, "个人信息获取失败");
            finish();
            return;
        }
        ApiService.getPersonnalInfo(uid, new ApiService.ParsedRequestListener<PersonalInfo>() {
            @Override
            public void onResponseResult(PersonalInfo response) {
                if (response.getStatus() == Sharedparms.statusSuccess) {
                    SPUtils.PutlivertmpUrl(CameraActivity.this, response.getData().getLive_rtmp());
                    SPUtils.PutNiceName(CameraActivity.this, response.getData().getUser_nicename());
                    SPUtils.PutUserAccount(CameraActivity.this, response.getData().getUser_login());
                    SPUtils.Putheader_url(CameraActivity.this, response.getData().getAvatar());
                } else {
                    UtilTool.ShowToast(CameraActivity.this, response.getMsg());
                    finish();
                    return;
                }
            }

            @Override
            public void _OnError(String errormessage) {
                UtilTool.ShowToast(CameraActivity.this, errormessage);
            }
        });
    }

    private void initView() {
        tvTitle.setText(getResources().getString(R.string.ACTIONBAR_CAMERA));
    }

    private void InitData() {
        preview = new CameraPreview(CameraActivity.this, surfaceView);
        preview.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        ((FrameLayout) findViewById(R.id.layout)).addView(preview);
        preview.setKeepScreenOn(true);
        surfaceView.setOnTouchListener(this);
        ViewGroup.LayoutParams lp = surfaceView.getLayoutParams();
        WindowManager windowManager = this.getWindowManager();
        int height = windowManager.getDefaultDisplay().getHeight();
        int width = windowManager.getDefaultDisplay().getWidth();
        lp.width = width;
        lp.height = height * 2 / 3;
        surfaceView.setLayoutParams(lp);
        cameraGrid.setType(type);
        imageShow.setLayoutParams(lp);
        ShowPic(false, null);
        ViewGroup.LayoutParams layoutButtonLayoutParams = layoutButton.getLayoutParams();
        layoutButtonLayoutParams.height = height * 1 / 3;
        layoutButtonLayoutParams.width = width;
        layoutButton.setLayoutParams(layoutButtonLayoutParams);

    }

    @OnClick({R.id.layout_back, R.id.image_camerafan, R.id.btn_camera, R.id.tv_cancel, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_back:
                finish();
                break;
            case R.id.image_camerafan://摄像头切换
                mCurrentCameraId = (mCurrentCameraId + 1) % Camera.getNumberOfCameras();
                if (camera != null) {
                    camera.stopPreview();
                    preview.setCamera(null);
                    camera.setPreviewCallback(null);
                    camera.release();
                    camera = null;
                }
                try {
                    camera = Camera.open(mCurrentCameraId);
                    camera.setPreviewDisplay(surfaceView.getHolder());
                    UtilTool.ShowToast(this,mCurrentCameraId+"");
                    camera.startPreview();
                    preview.setCamera(camera);
                    preview.reAutoFocus();
                } catch (Exception e) {
                    UtilTool.ShowToast(this, "11未发现相机"+e.getMessage());
                }
                break;
            case R.id.btn_camera://拍照
                try {
                    camera.takePicture(null, null, jpegCallback);

                } catch (Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(getApplication(), "拍照失败，请重试！", Toast.LENGTH_LONG)
                            .show();
                    try {
                        camera.startPreview();
                    } catch (Throwable e) {

                    }
                }
                break;
            case R.id.tv_cancel:
                ShowPic(false, null);
                break;
            case R.id.tv_save:
                putPicture(imagePath);
                break;
        }
    }

    Camera.PictureCallback jpegCallback = new Camera.PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            progressDialog = ProgressDialog.show(CameraActivity.this, null, "处理中");
            progressDialog.setCanceledOnTouchOutside(true);
            new SaveImageTask(data).execute();
            resetCam();
        }
    };

    private void resetCam() {
        camera.startPreview();
        preview.setCamera(camera);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                preview.pointFocus(event);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    //处理拍摄的照片
    private class SaveImageTask extends AsyncTask<Void, Void, String> {
        private byte[] data;

        SaveImageTask(byte[] data) {
            this.data = data;
        }

        @Override
        protected String doInBackground(Void... params) {
            // Write to SD Card
            String path = "";
            try {

                path = saveToSDCard(data);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return path;
        }


        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);

            if (!TextUtils.isEmpty(path)) {

                Log.d("DemoLog", "path=" + path);
                imagePath = path;
                progressDialog.dismiss();
                ShowPic(true, path);
                //在这直接给后端
            } else {
                UtilTool.ShowToast(getApplicationContext(), "拍照失败，请稍后重试！");
            }
        }
    }

    /**
     * 上传照片
     */
    private void putPicture(String path) {
        progressDialog.show();
        ApiService.putPicture(path, new ApiService.ParsedRequestListener<putPictureInfo>() {
            @Override
            public void onResponseResult(putPictureInfo response) {

                progressDialog.dismiss();
                if (response.getStatus() == Sharedparms.statusSuccess) {
                    SPUtils.Putimage_url(CameraActivity.this,response.getData().getUrl());
                    pushFlow();
                } else {
                    UtilTool.ShowToast(CameraActivity.this, response.getMsg());
                    ShowPic(false, null);
                }
            }

            @Override
            public void _OnError(String errormessage) {
                ShowPic(false, null);
                progressDialog.dismiss();
            }
        });


    }

    private void pushFlow() {
        String livertmpUrl = SPUtils.getlivertmpUrl(CameraActivity.this);
         /*开始推流*/
        PushFlowActivity.RequestBuilder builder = new PushFlowActivity.RequestBuilder()
                .bestBitrate(600)
                .cameraFacing(1)//是否前置摄像头  1前面  0后面
                .dx(14)//marginx
                .dy(14)
                .site(1)//水印位置
                .rtmpUrl(livertmpUrl)//rtmp服务器地址
                .videoResolution(360)
                .portrait(false)//是否横屏
                //.watermarkUrl("assets:///spalsh.png")// 水印图片路径
                //.watermarkUrl("assets://qupai-logo.png")
                .minBitrate(500)//帧率
                .maxBitrate(800)
                .frameRate(600)
                .initBitrate(800);
        PushFlowActivity.startActivity(this, builder);
        finish();
    }

    private final int PROCESS = 1;
    public static final String CAMERA_PATH_VALUE1 = "PHOTO_PATH";

    @Override
    protected void onResume() {
        super.onResume();
        int numCams = Camera.getNumberOfCameras();
        if (numCams > 0) {
            try {
                mCurrentCameraId = 0;
                camera = Camera.open(mCurrentCameraId);
                camera.startPreview();
                preview.setCamera(camera);
                preview.reAutoFocus();
            } catch (RuntimeException ex) {
                Toast.makeText(this, "222未发现相机"+ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    protected void onPause() {
        if (camera != null) {
            camera.stopPreview();
            preview.setCamera(null);
            camera.release();
            camera = null;
            preview.setNull();
        }
        super.onPause();

    }

    /**
     * 是否显示照片UI设置
     *
     * @param isShow
     */
    private void ShowPic(boolean isShow, String PicPath) {
        if (isShow) {
            surfaceView.setVisibility(View.GONE);
            imageShow.setVisibility(View.VISIBLE);
            tvCancel.setVisibility(View.VISIBLE);
            tvSave.setVisibility(View.VISIBLE);
            Glide.with(CameraActivity.this)
                    .load(PicPath)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)// 缓存所有版本的图像（默认行为）
                    .into(imageShow);
        } else {
            surfaceView.setVisibility(View.VISIBLE);
            imageShow.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);
            tvSave.setVisibility(View.GONE);
        }

    }

    private int PHOTO_SIZE_W = 2000;
    private int PHOTO_SIZE_H = 2000;

    /**
     * 将拍下来的照片存放在SD卡中
     */
    public String saveToSDCard(byte[] data) throws IOException {
        Bitmap croppedImage;
        // 获得图片大小
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 8;
        options.inInputShareable = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        // PHOTO_SIZE = options.outHeight > options.outWidth ? options.outWidth
        // : options.outHeight;
        PHOTO_SIZE_W = options.outWidth;
        PHOTO_SIZE_H = options.outHeight;
        options.inJustDecodeBounds = false;
        Rect r = new Rect(0, 0, PHOTO_SIZE_W, PHOTO_SIZE_H);
        try {
            croppedImage = decodeRegionCrop(data, r);
        } catch (Exception e) {
            return null;
        }
        String imagePath = "";
        try {
            imagePath = saveToFile(croppedImage);
        } catch (Exception e) {

        }
        croppedImage.recycle();
        return imagePath;
    }

    // 保存图片文件
    public static String saveToFile(Bitmap croppedImage)
            throws FileNotFoundException, IOException {
        File file=null;
        //判断是否有sd卡
//        if (Environment.getExternalStorageState().equalsIgnoreCase(Environment.MEDIA_MOUNTED) && Util.checkSDStatus(1)){
            File sdCard = Environment.getExternalStorageDirectory();
             file = new File(sdCard.getAbsolutePath() + "/DCIM/Camera/meituxiuxiu/");
            if (!file.exists()) {
                file.mkdirs();
            }
//        }
// else {
//            if (!neiFile.exists()){
//                file.mkdirs();
//            }
//        }

        String fileName = getCameraPath();
        File outFile = new File(file, fileName);
        FileOutputStream outputStream = new FileOutputStream(outFile); // 文件输出流
        croppedImage.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
        outputStream.flush();
        outputStream.close();
        return outFile.getAbsolutePath();
    }

    private Bitmap decodeRegionCrop(byte[] data, Rect rect) {
        InputStream is = null;
        System.gc();
        Bitmap croppedImage = null;
        try {
            is = new ByteArrayInputStream(data);
            BitmapRegionDecoder decoder = BitmapRegionDecoder.newInstance(is, false);
            try {
                croppedImage = decoder.decodeRegion(rect,
                        new BitmapFactory.Options());
            } catch (IllegalArgumentException e) {
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {

        }
        Matrix m = new Matrix();
        m.setRotate(90, PHOTO_SIZE_W / 2, PHOTO_SIZE_H / 2);
        if (mCurrentCameraId == 1) {
            m.postScale(1, -1);
        }
        Bitmap rotatedImage = Bitmap.createBitmap(croppedImage, 0, 0,
                PHOTO_SIZE_W, PHOTO_SIZE_H, m, true);
        if (rotatedImage != croppedImage)
            croppedImage.recycle();
        return rotatedImage;
    }

    private static String getCameraPath() {
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append("IMG");
        sb.append(calendar.get(Calendar.YEAR));
        int month = calendar.get(Calendar.MONTH) + 1; // 0~11
        sb.append(month < 10 ? "0" + month : month);
        int day = calendar.get(Calendar.DATE);
        sb.append(day < 10 ? "0" + day : day);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        sb.append(hour < 10 ? "0" + hour : hour);
        int minute = calendar.get(Calendar.MINUTE);
        sb.append(minute < 10 ? "0" + minute : minute);
        int second = calendar.get(Calendar.SECOND);
        sb.append(second < 10 ? "0" + second : second);
        if (!new File(sb.toString() + ".jpg").exists()) {
            return sb.toString() + ".jpg";
        }

        StringBuilder tmpSb = new StringBuilder(sb);
        int indexStart = sb.length();
        for (int i = 1; i < Integer.MAX_VALUE; i++) {
            tmpSb.append('(');
            tmpSb.append(i);
            tmpSb.append(')');
            tmpSb.append(".jpg");
            if (!new File(tmpSb.toString()).exists()) {
                break;
            }

            tmpSb.delete(indexStart, tmpSb.length());
        }
        return tmpSb.toString();
    }

}
