package com.shuimunianhua.xianglixiangqin.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Base64;

public class ImgToBase64Util {

	/**
	 * 根据图片路径和格式把图片转化为Base64编码格式
	 * @param imgPath
	 * @param bitmap
	 * @param imgFormat 图片格式
	 * @param quality 压缩的程度
	 * @return
	 */
	public static String imgToBase64(Context context, String imgPath, String imgFormat, int quality) {
		Bitmap bitmap = null;
		if (imgPath !=null && imgPath.length() > 0) {
			bitmap = readBitmap(context,imgPath);
		}
		if(bitmap == null){
			return null;
		}
		ByteArrayOutputStream out = null;
		try {
			out = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, quality, out);
			out.flush();
			out.close();

			byte[] imgBytes = out.toByteArray();
			long length = imgBytes.length;
			LogUtil.LogShitou("字节长度", length+"");
			return Base64.encodeToString(imgBytes, Base64.DEFAULT);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static Bitmap readBitmap(Context context, String imgPath) {
        
		try {
		Options opts = new Options();
        // 如果设置为true,仅仅返回图片的宽和高,宽和高是赋值给opts.outWidth,opts.outHeight;
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, opts);
        // 得到原图的宽和高
        int srcWidth = opts.outWidth;
        int srHeigth = opts.outHeight;
      
        int scale = 1;
        int sx = srcWidth/DensityUtil.dip2px(context, 80); // 3000/320= 9;
        int sy = srHeigth/DensityUtil.dip2px(context, 80); // 2262/480= 5;
        if(sx > sy && sx > 1){
        	scale = sx;
        }
        if(sy > sx && sy > 1){
        	scale = sy;
        }
        
        // 设置为false表示,让decodeFile返回一个bitmap类型的对象
        opts.inJustDecodeBounds = false;
        // 设置缩放的比例,设置后,decodeFile方法就会按照这个比例缩放图片,加载到内存
        opts.inSampleSize= scale; 
        // 缩放图片加载到内存
        Bitmap bitmap =  BitmapFactory.decodeFile(imgPath, opts);
        
		return bitmap;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return null;
		}

	}

	/**
	 * 
	 * @param base64Data
	 * @param imgName
	 * @param imgFormat 图片格式
	 */
	public static void base64ToBitmap(String base64Data,String imgName,String imgFormat) {
		byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
		Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

		File myCaptureFile = new File("/sdcard/", imgName);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(myCaptureFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		boolean isTu = bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
		if (isTu) {
			// fos.notifyAll();
			try {
				fos.flush();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	

	public static String img_android = "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";
	public static String time_img = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEB AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/2wBDAQEBAQEBAQEBAQEBAQEBAQEBAQEB AQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQEBAQH/wAARCAC1AJYDASIA AhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3 ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3 uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwD84ZtF Jdx5SMGKk8El+SBu/cygspbA4k3mQnMonP2ujNo/BzCH3nJBQ/vRwWZmNu5DYkBUEyNJ5m398ZmN 17JcaMpdiUU5IzkZ3nL5YnyZTn5t2Pn3785mE5a7xJNHJDO0QbdtJJC/ON2AxzbuP4xgFpdxf70p lZbz/bz2KvdQtone+ut47qD3aWq96N22mpRb/wCebDcQOST57WSurvd+1S6bPq97uHVXflMmjI5Z GiSXcQSTDnzsE7SQbeTJG7oA5Yup/wBIMge7gXQprmWO1tbGa6urqRIra0tLe4mmvZHZswwQRWr3 DEvJlQPMaTzBkyiYtd/UHwd+AXjf46+OLHwF4H022kuZ4pL7V9b1VmtdB8M6HaMTf+JfEl+trL9h 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...";
	public static String time_img_jpg = "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...";
}