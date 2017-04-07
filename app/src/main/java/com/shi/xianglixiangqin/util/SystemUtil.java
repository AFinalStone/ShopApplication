package com.shi.xianglixiangqin.util;

import java.io.File;
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

}













