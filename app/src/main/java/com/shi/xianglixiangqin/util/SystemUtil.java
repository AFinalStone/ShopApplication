package com.shi.xianglixiangqin.util;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

/***
 * 系统工具类  获取  包管理器信息，SDcard信息，网络状况信息
 * @author SHI
 * 2016-2-25 16:39:55
 */
public class SystemUtil {

	/**获取当前应用版本号**/
	public static int getCurrentAppVersionCode(Context mContext){
        // 获得包管理器，注意，整个android手机，共用一个包管理器
         PackageManager packageManager = mContext.getPackageManager();
		 PackageInfo packageInfo = null;
		try {
			packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), PackageManager.GET_META_DATA);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if(packageInfo != null){
			return packageInfo.versionCode;
		}else{
			return 0;
		}
	}

	/**获取当前应用版本名称**/
	public static String getCurrentAppVersionName(Context mContext){
		// 获得包管理器，注意，整个android手机，共用一个包管理器
		PackageManager packageManager = mContext.getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if(packageInfo != null){
			return packageInfo.versionName;
		}else{
			return "";
		}
	}

	/**获取当前应用版包名**/
	public static String getCurrentAppPackageName(Context mContext){
		// 获得包管理器，注意，整个android手机，共用一个包管理器
		PackageManager packageManager = mContext.getPackageManager();
		PackageInfo packageInfo = null;
		try {
			packageInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		if(packageInfo != null){
			return packageInfo.packageName;
		}else{
			return "";	
		}
	}

	/** SD卡是否存在 **/
	public static boolean IfExistExternalStorage() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}


	/****
	 * 返回SD卡默认Download文件夹路径
	 * @return
	 */
	public static File getDownloadFilePath() {
		return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
	}

	/****
	 * 返回当前应用的缓存文件夹
	 * @return
	 */
	public static File getExtendCachFiles(Context mContext) {
		return mContext.getExternalCacheDir();
	}

	/*****
	 * 在SD卡根目录创建文件夹,成功返回true，失败返回false
	 * @param FolderName 文件夹名称
	 * @return
	 */
	public static boolean createSDCardFolder(String FolderName) {
		
		if(IfExistExternalStorage()){
			File pathSDCard = Environment.getExternalStorageDirectory();
			File file = new File(pathSDCard.toString() + File.separator + FolderName);
			if (!file.exists()) {// 目录不存在
				file.mkdirs();
			}
			return true;
		}
		return false;
	}

	/****
	 * 删除文件夹或者文件
	 * @param file
	 * @return
	 */
	public static boolean deleteDirectory(File file) {
		if (file.isDirectory()) {
			File[] filelist = file.listFiles();
			for (int i = 0; i < filelist.length; i++) {
				deleteDirectory(filelist[i]);
			}
			if (!file.delete()) {
				return false;
			}
		} else {
			if (!file.delete()) {
				return false;
			}
		}
		return true;
	}

	/****
	 * 判断是否有网络连接
	 * @param mContext 设备上下文
	 * @return
	 */
    public boolean isNetworkConnected(Context mContext) {
        if (mContext != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /****
     * 判断WiFi网络是否可用
     * @param mContext
     * @return
     */
    public boolean isWifiConnected(Context mContext) {
        if (mContext != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    
    /****
     * 判断MOBILE网络是否可用
     * @param mContext
     * @return
     */
    public boolean isMobileConnected(Context mContext) {
        if (mContext != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    
    /****
     * 获取当前网络连接类型
     * @param mContext
     * @return
     * 
     */
    public static int getConnectedType(Context mContext) {
        if (mContext != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /***
     * 获取手机时间按照format格式返回
     * @param format
     * @return
     */
	public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}
	
	
	/**
	 * 判断指定名称的服务是否运行
	 * @param act activity
	 * @param name 服务的类的全名
	 * @return
	 */
	public static boolean isServiceRunning(Activity act, String name) {
		
		ActivityManager am = (ActivityManager) act.getSystemService(Context.ACTIVITY_SERVICE);
		// 返回正在运行的服务的信息列表
		List<RunningServiceInfo> infoList = am.getRunningServices(100); // 参数是指，最多返回多少个服务的信息
		for (RunningServiceInfo runningServiceInfo : infoList) {
			String className = runningServiceInfo.service.getClassName();
//			System.out.println("className::"+className);
			if(className.equals(name)){
				return true;
			}
		}
		return false;
	}

	/***
	 * 获取手机时间
	 * @return
	 */
	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
	}


	public static String getTotalCacheSize(Context context) throws Exception {
		long cacheSize = getFolderSize(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			cacheSize += getFolderSize(context.getExternalCacheDir());
		}
		return getFormatSize(cacheSize);
	}

	public static void clearAllCache(Context context) {
		deleteDir(context.getCacheDir());
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			File externalCacheDir = context.getExternalCacheDir();
			if(externalCacheDir != null){
				deleteDir(externalCacheDir);
			}
		}
	}

	private static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		return dir.delete();
	}

	// 获取文件
	// Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/
	// 目录，一般放一些长时间保存的数据
	// Context.getExternalCacheDir() -->
	// SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
	public static long getFolderSize(File file) throws Exception {
		long size = 0;
		try {
			File[] fileList = file.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				// 如果下面还有文件
				if (fileList[i].isDirectory()) {
					size = size + getFolderSize(fileList[i]);
				} else {
					size = size + fileList[i].length();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return size;
	}

	/**
	 * 格式化单位
	 *
	 * @param size
	 * @return
	 */
	public static String getFormatSize(double size) {
		double kiloByte = size / 1024;
		if (kiloByte < 1) {
			// return size + "Byte";
			return "0.00M";
		}

		double megaByte = kiloByte / 1024;
		if (megaByte < 1) {
			BigDecimal result1 = new BigDecimal(Double.toString(kiloByte / 1024));
			return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "M";
		}

		double gigaByte = megaByte / 1024;
		if (gigaByte < 1) {
			BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
			return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "M";
		}

		double teraBytes = gigaByte / 1024;
		if (teraBytes < 1) {
			BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
			return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
					.toPlainString() + "G";
		}
		BigDecimal result4 = new BigDecimal(teraBytes);
		return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
				+ "T";
	}

}













