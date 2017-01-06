package com.example.administrator.learn.Tool;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;

import java.io.File;

/**
 * Created by Administrator on 2016/2/2.
 */
public class YImagePicker {

    private static final int REQUEST_IMAGE_PICK_FROM_FILE = 0x121;
    private static final int REQUEST_IMAGE_PICK_FROM_CAMERA = 0x122;
    private static final int REQUEST_IMAGE_CROP = 0x123;
    private static final int REQUEST_IMAGE_PICUT = 22;
    protected Uri IMAGE_URI;
    public static final File sTempDir = new File(Environment
            .getExternalStorageDirectory().getAbsolutePath() + "/Android/TEMP");
    private File cameraOutImageFile;
    private Activity activity;
    OnImagePickedListener onImagePickedListener;
    private String tempImgFileName;
    private Fragment fragment;
    private Uri uritempFile;
    BitmapParser bitmapParser;

    public YImagePicker(Activity activity, OnImagePickedListener onImagePickedListener) {
        this(activity, onImagePickedListener, new BitmapParser() {
            @Override
            public Bitmap parse(String path) {
                return YBitmaps.getBitmap(path, 480, 800);
            }
        });
    }

    public YImagePicker(Fragment fragment, OnImagePickedListener onImagePickedListener) {
        this(fragment.getActivity(), onImagePickedListener);
        this.fragment = fragment;
    }

    public YImagePicker(Fragment fragment, OnImagePickedListener onImagePickedListener, BitmapParser bitmapParser) {
        this(fragment.getActivity(), onImagePickedListener, bitmapParser);
        this.fragment = fragment;
    }

    public YImagePicker(Activity activity, OnImagePickedListener onImagePickedListener, BitmapParser bitmapParser) {
        this.activity = activity;
        this.onImagePickedListener = onImagePickedListener;
        this.bitmapParser = bitmapParser;
        if (!sTempDir.exists()) {
            sTempDir.mkdirs();
        }
    }


    public void startImagePickFromFile() {
        tempImgFileName = "image_file_comp.jpg";
        startImagePickFromFile(tempImgFileName);
    }

    public void startImagePickFromFile(String fileName) {

        tempImgFileName = fileName;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        if (fragment == null) {
            activity.startActivityForResult(intent, REQUEST_IMAGE_PICK_FROM_FILE);
        } else {
            fragment.startActivityForResult(intent, REQUEST_IMAGE_PICK_FROM_FILE);
        }
    }

    public void startImagePickFromCamera() {

        startImagePickFromCamera("image.jpg");
    }

    public void startImagePickFromCamera(String imgFileName) {
        String fileName = null;
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileName = imgFileName;
        cameraOutImageFile = new File(sTempDir, fileName);
        if (!cameraOutImageFile.getParentFile().exists()) {
            cameraOutImageFile.getParentFile().mkdirs();
        }
        IMAGE_URI = Uri.fromFile(cameraOutImageFile);
        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, IMAGE_URI);
        if (fragment == null) {
            activity.startActivityForResult(openCameraIntent, REQUEST_IMAGE_PICK_FROM_CAMERA);
        } else {
            fragment.startActivityForResult(openCameraIntent, REQUEST_IMAGE_PICK_FROM_CAMERA);
        }
    }

    public void startImagePickFromPicture() {
//        String fileName = null;
//        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        fileName = imgFileName;
//        cameraOutImageFile = new File(sTempDir, fileName);
//        if (!cameraOutImageFile.getParentFile().exists()) {
//            cameraOutImageFile.getParentFile().mkdirs();
//        }
//        IMAGE_URI = Uri.fromFile(cameraOutImageFile);
//        // 指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
//        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, IMAGE_URI);
        Intent i = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (fragment == null) {
            activity.startActivityForResult(i, REQUEST_IMAGE_PICUT);
        } else {
            fragment.startActivityForResult(i, REQUEST_IMAGE_PICUT);
        }
    }

    /*
         * 裁剪图片
         */
    public void startImageCrop(Uri uri, String tempName, int aspectX, int aspectY, int outputX, int outputY) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        if ("content".equals(uri.getScheme())) {
            String absoluteImagePath = getAbsoluteImagePath(uri);
            uri = Uri.parse("file://" + absoluteImagePath);
        }
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        /**
         * 此方法返回的图片只能是小图片（sumsang测试为高宽160px的图片）
         * 故将图片保存在Uri中，调用时将Uri转换为Bitmap，此方法还可解决miui系统不能return data的问题
         */
        //intent.putExtra("return-data", true);

        //uritempFile为Uri类变量，实例化uritempFile
        uritempFile = Uri.parse("file://" + sTempDir + "/" + tempName);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uritempFile);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        if (fragment == null) {
            activity.startActivityForResult(intent, REQUEST_IMAGE_CROP);
        } else {
            fragment.startActivityForResult(intent, REQUEST_IMAGE_CROP);
        }
    }

    /*
         * 裁剪图片
         */
    public void startImageCrop(String path) {
        startImageCrop(Uri.fromFile(new File(path)));
    }

    public void startImageCrop(Uri uri) {
        startImageCrop(uri, "crop_temp_img.jpg", 1, 1, 200, 200);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_IMAGE_PICK_FROM_CAMERA: {//拍照拿去裁剪
                    // 将保存在本地的图片取出并缩小后显示在界面上
                    Bitmap bitmap = bitmapParser.parse(cameraOutImageFile.getAbsolutePath());
                    onImagePickedListener.onImagePicked(bitmap, requestCode, data, IMAGE_URI,
                            cameraOutImageFile.getAbsolutePath());
                    break;
                }
                case REQUEST_IMAGE_PICUT: {//选择到的照片拿去裁剪
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};
                    Cursor cursor = activity.getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    onImagePickedListener.onImagePicked(bitmapParser.parse(picturePath), requestCode, data, null,
                            picturePath);
                    break;
                }
                case REQUEST_IMAGE_PICK_FROM_FILE: {
                    ContentResolver resolver = activity.getContentResolver();
                    // 照片的原始资源地址
                    if (data == null) {
                        return;
                    }
                    Uri originalUri = data.getData();

                    String path = getAbsoluteImagePath(originalUri);
                    if (path == null) {
                        path = getPathByUri4kitkat(activity, originalUri);
                    }
                    // 使用ContentProvider通过URI获取原始图片
//                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver,
//                                originalUri);
                    if (path == null) {
                        onImagePickedListener.onPickFail(new Exception("path get fail"));
                        return;
                    }
                    Bitmap bitmap = bitmapParser.parse(path);
                    if (bitmap == null) {
                        return;
                    }
                    onImagePickedListener.onImagePicked(bitmap, requestCode, data, originalUri, path);
                    break;
                }
                case REQUEST_IMAGE_CROP: {
                    try {

                        String path = uritempFile.getPath();
//                        Bitmap bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream
//                                (uritempFile));
                        Bitmap bitmap = YBitmaps.getBitmap(path, 200, 200);
                        onImagePickedListener.onImageCroped(bitmap);
                    } catch (Exception e) {
                        onImagePickedListener.onPickFail(e);
                    }
                }
            }
        }
    }

    private String getAbsoluteImagePath(Uri uri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            // Do not call Cursor.close() on a cursor obtained using this method,
            // because the activity will do that for you at the appropriate time
            Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return uri.getPath();
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPathByUri4kitkat(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {// ExternalStorageProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore
            // (and
            // general)
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static interface OnImagePickedListener {

        void onImagePicked(Bitmap bitmap, int requestCode, Intent data, Uri originalUri, String path);

        void onImageCroped(Bitmap bm);

        void onPickFail(Exception e);
    }

    public static interface BitmapParser {

        Bitmap parse(String path);
    }
}
