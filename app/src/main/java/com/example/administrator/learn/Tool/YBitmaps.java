package com.example.administrator.learn.Tool;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


public class YBitmaps {

	public static byte[] Bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	// 我们先看下质量压缩方法
	@SuppressLint("NewApi")
	public static Bitmap compressSize(Bitmap image, int compSize) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		if (image.getByteCount() > 1024*1000) {
			image.recycle();
			Options ops = new Options();
			ops.inSampleSize = 4;
			image = BitmapFactory.decodeStream(isBm, null, ops);// 把ByteArrayInputStream数据生成图片
			baos.reset();
			image.compress(CompressFormat.PNG, 100, baos);
		}

		int options = 89;
		while (baos.toByteArray().length > compSize && options > 0) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 11;// 每次都减少11
		}
		image.recycle();
		ByteArrayInputStream isBm1 = new ByteArrayInputStream(
				baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm1, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}
	/**
	 * 压缩路径所提供的路径 并返回压缩好的路径
	 * 
	 * @param srcPath
	 *            图片保存的路径
	 * @param hh
	 *            压缩至多高
	 * @param ww
	 *            压缩至多宽
	 * @param compSize
	 *            压缩至多大
	 * @return 压缩后的路径
	 */
	@SuppressLint("NewApi")
	public static File compressBitmapAndGetPath(String srcPath, int ww, int hh,
			int compSize) {
		Bitmap bitmap = getBitmapFromPathWidthCompressBounds(srcPath, ww, hh);
		bitmap = compressSize(bitmap, compSize);
		File dir = new File(srcPath).getParentFile();
		if (!dir.exists()) {
			dir.mkdir();
		}
		// // 保存入sdCard
		File compFile = new File(Environment.getExternalStorageDirectory(),
				"comp_" + srcPath.substring(srcPath.lastIndexOf("/") + 1));
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(compFile);
			bitmap.compress(CompressFormat.PNG, 100, out);
			bitmap.recycle();
			return compFile;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private static Bitmap getBitmapFromPathWidthCompressBounds(String srcPath, int ww, int hh) {
		Options newOpts = new Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts); // 此时返回bm为空
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1; // be=1表示不缩放
		if (w > h && w > ww) { // 如果宽度大的话根据宽度固定大小缩放
			be = (int) (w * 1.0f / ww);
		} else if (w < h && h > hh) { // 如果高度高的话根据宽度固定大小缩放
			be = (int) (h * 1.0f / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be; // 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		newOpts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		return bitmap;
	}

	/**
	 * 按照比例 压缩宽高（读进内存后压缩） 宽比高大则根据宽的比例 反之
	 * 
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap compressBounds(Bitmap bm, int newWidth, int newHeight) {
		int w = bm.getWidth();
		int h = bm.getHeight();
		float nw, nh;
		if (w > h && w > newWidth) {
			nw = newWidth;
			nh = newWidth * 1.0f * h / w + 0.5f;
		} else if (w <= h && h > newHeight) {
			nw = newHeight * 1.0f * w / h + 0.5f;
			nh = newHeight;
		} else {
			return bm;
		}
		Bitmap b = zoomImg(bm, nw, nh);
		bm.recycle();
		return b;
	}

	/**
	 * 新建一个bitmap
	 * 
	 * @param bm
	 * @param newWidth
	 * @param newHeight
	 * @return
	 */
	public static Bitmap zoomImg(Bitmap bm, float newWidth, float newHeight) {
		// 获得图片的宽高
		int width = bm.getWidth();
		int height = bm.getHeight();
		// 计算缩放比例
		float scaleWidth = newWidth / width;
		float scaleHeight = newHeight / height;
		// 取得想要缩放的matrix参数
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
				true);
		return newbm;
	}

	/**
	 * 读进内存时进行缩放
	 * 
	 * @param srcPath
	 * @param ww
	 * @param hh
	 * @return
	 */
	public static Bitmap getBitmap(String srcPath, int ww, int hh) {
		Options newOpts = new Options();
		newOpts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(srcPath, newOpts);
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1; // be=1表示不缩放
		if (w >= h && w > ww) { // 如果宽度大的话根据宽度固定大小缩放
			be = (int) (w * 1.0f / ww + 0.5f);
		} else if (w < h && h > hh) { // 如果高度高的话根据宽度固定大小缩放
			be = (int) (h * 1.0f / hh + 0.5f);
		}
		if (be <= 0) {
			be = 1;
		}
		if (be != 1 && be % 2 != 0) {
			be++;
		}
		newOpts.inSampleSize = be; // 设置缩放比例
		newOpts.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(srcPath, newOpts);
	}

	/**
	 * 这种压缩会失真 丢失透明 压缩至指定的路径
	 * 
	 * @param bm
	 * @param compSize
	 * @return
	 */
	@SuppressLint("NewApi")
	public static void compressToSizeAndSaveFile(Bitmap bm, int compSize,
			File file) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 89;
		while (baos.toByteArray().length > compSize && options > 0) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			bm.compress(CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 11;// 每次都减少11
		}
		bm.recycle();
		OutputStream out = null;
		try {
			out = new FileOutputStream(file);
			out.write(baos.toByteArray());
		} catch (Exception e) {
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 画一个圆角图
	 * 
	 * @param bitmap
	 * @param roundPx
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);

		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();

		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(false);

		canvas.drawARGB(0, 0, 0, 0);

		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * bitmap转为base64
	 * 
	 * @param bitmap
	 * @return String
	 */
	@SuppressLint("NewApi")
	public static String bitmapToBase64(Bitmap bitmap) {

		String result = null;

		ByteArrayOutputStream baos = null;

		try {
			if (bitmap != null) {
				baos = new ByteArrayOutputStream();
				bitmap.compress(CompressFormat.JPEG, 100, baos);

				baos.flush();
				baos.close();

				byte[] bitmapBytes = baos.toByteArray();
				result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (baos != null) {
					baos.flush();
					baos.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * base64转为bitmap
	 * 
	 * @param base64Data
	 * @return bitmap
	 */
	@SuppressLint("NewApi")
	public static Bitmap base64ToBitmap(String base64Data) {

		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
	}

	/**
	 * 旋转bitmap
	 */
	public static Bitmap rotate(Bitmap source, float rotation) {

		Matrix matrix = new Matrix();
		matrix.reset();
		matrix.setRotate(rotation);
		Bitmap bm = Bitmap.createBitmap(source, 0, 0, source.getWidth(),
				source.getHeight(), matrix, true);
		source.recycle();
		return bm;
	}

	public static Bitmap convert(Bitmap source, int dir) {
		int w = source.getWidth();
		int h = source.getHeight();
		Bitmap newb = Bitmap.createBitmap(w, h, Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
		Canvas cv = new Canvas(newb);
		Matrix m = new Matrix();

		int x, y;
		switch (dir) {
		case 0:
			x = -1;
			y = 1;
			break;
		case 1:
		default:
			x = 1;
			y = -1;
			break;
		}
		m.postScale(x, y); // 镜像垂直翻转
		Bitmap new2 = Bitmap.createBitmap(source, 0, 0, w, h, m, true);
		cv.drawBitmap(new2, new Rect(0, 0, new2.getWidth(), new2.getHeight()),
				new Rect(0, 0, new2.getWidth(), new2.getHeight()), null);
		source.recycle();
		return newb;
	}

	public static void writeBitmap(Bitmap bm, String path,
			CompressFormat format, int quality) {
		writeBitmapToFile(bm, new File(path), format, quality);
	}

	public static void writeBitmapToFile(Bitmap bm, File file,
			CompressFormat format, int quality) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bm.compress(format, quality, fos);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *
	 * @param sourceImg
	 * @param number 0-255
     * @return
     */
	public static Bitmap newBitmapWithAlpha(Context context,Bitmap sourceImg, int number){
		Bitmap bitmap = Bitmap.createBitmap(sourceImg.getWidth()/3, sourceImg.getHeight()/3, Config.ARGB_4444);
		Canvas canvas = new Canvas(bitmap);
		Paint paint = new Paint();
		paint.setAlpha(50);
		canvas.drawBitmap(sourceImg,0,0,paint);
		return bitmap;
	}
}