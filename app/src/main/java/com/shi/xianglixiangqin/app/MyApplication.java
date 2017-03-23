package com.shi.xianglixiangqin.app;


import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import com.shi.xianglixiangqin.model.AddressModel;
import com.shi.xianglixiangqin.model.CustomModel;
import com.shi.xianglixiangqin.util.PreferencesUtilMy;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;


/**
 * 全局管理
 * @author SHI
 */
public class MyApplication extends Application{
	/**当前登录用户对象**/
	private static CustomModel customModel;
	public static CustomModel getmCustomModel(Context mContext) {
		if(customModel == null){
			customModel = PreferencesUtilMy.getCustomModel(mContext);
		}
		return customModel;
	}
	public static void setmCustomModel(Context mContext, CustomModel mCustomModel) {
		customModel = mCustomModel;
		PreferencesUtilMy.saveCustomModel( mContext, customModel);
//		MyApplication.mCustomModel = mCustomModel;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		//初始化极光推送
		 JPushInterface.init(this);
		 JPushInterface.setDebugMode(false);
		//初始化融云
//		/**
//		 * OnCreate 会被多个进程重入，这段保护代码，确保只有您需要使用 RongIM 的进程和 Push 进程执行了 init。
//		 * io.rong.push 为融云 push 进程名称，不可修改。
//		 */
		if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext())) ||
				"io.rong.push".equals(getCurProcessName(getApplicationContext()))) {
			/**
			 * IMKit SDK调用第一步 初始化
			 */
			RongIM.init(this);
		}
	}

	/**
	 * 获得当前进程的名字
	 *
	 * @param context
	 * @return 进程号
	 */
	public static  String getCurProcessName(Context context) {

		int pid = android.os.Process.myPid();

		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
				.getRunningAppProcesses()) {

			if (appProcess.pid == pid) {
				return appProcess.processName;
			}
		}
		return null;
	}
	
}
