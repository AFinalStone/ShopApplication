package com.shuimunianhua.xianglixiangqin.activity;



import java.io.File;

import okhttp3.Call;

import org.ksoap2.serialization.SoapObject;

import android.content.Intent;
import android.os.SystemClock;


import com.google.gson.Gson;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.json.JSONResultMsgModel;
import com.shuimunianhua.xianglixiangqin.model.AppVersionModel;
import com.shuimunianhua.xianglixiangqin.model.CustomModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.ActivityCollectorUtils;
import com.shuimunianhua.xianglixiangqin.util.FileUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.PreferencesUtil;
import com.shuimunianhua.xianglixiangqin.util.PreferencesUtilMy;
import com.shuimunianhua.xianglixiangqin.util.SystemUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/****
 * 闪屏页逻辑控制层
 * 
 * @author SHI 2016-2-25 14:28:35
 */
public class SplashActivityControllerImpDaiNiFei extends SplashActivityController {

	/**用户名手机号，密码,令牌**/
  
	/** 当前应用版本号 **/
	float currentAppVersionCode;
	/** 应用信息bean**/
	private AppVersionModel appVersionBean_New;
	/**闪屏页面**/
	private SplashActivity mSplashActivity;
	/**开始时间**/
	private long startTime;
	/**休眠时间**/
	private final long SLEEPTIME = 5000;
	/**当前登录用户对象**/
	private CustomModel mCustomModel;

	public SplashActivityControllerImpDaiNiFei(SplashActivity mSplashActivity) {
		super();
		this.mSplashActivity = mSplashActivity;
	}

	public void init(Intent intent) {
		startTime =  System.currentTimeMillis();
		currentAppVersionCode = SystemUtil
		.getCurrentAppVersionCode(mSplashActivity);
		getServerAppVersionData();
	}

	public void getServerAppVersionData() {

		String urlVersionDesc = InformationCodeUtil.urlVersionDesc_DaiNiFei;
        OkHttpUtils.get()
		   .url(urlVersionDesc)
		   .build()
		   .connTimeOut(3000)
		   .execute(new StringCallback() {
			@Override
			public void onResponse(String result) {
//				if (result != null) {
//					IfUpdateApp(result);
//				} else {
//					cancelToDownApp();
//				}
				cancelToDownApp();
			}
			
			@Override
			public void onError(Call arg0, Exception arg1) {
				cancelToDownApp();
			}
		});

	}

	/** 是否更新应用 **/
	public void IfUpdateApp(String appVersionDesc) {
		Gson gson = new Gson();
		try {
			appVersionBean_New = gson.fromJson(appVersionDesc,
					AppVersionModel.class);

			if(appVersionBean_New.getSign()){
				if (appVersionBean_New.getVersionCode() > currentAppVersionCode) {
					mSplashActivity.showUpdateDialog(appVersionBean_New.getVersionName()
							,appVersionBean_New.getVersionSize(),appVersionBean_New.getVersionDesc(),true);
				}else{
					cancelToDownApp();
				}
			}else{
				mSplashActivity.showUpdateDialog(appVersionBean_New.getVersionName()
						,appVersionBean_New.getVersionSize(),appVersionBean_New.getVersionDesc(),false);
			}

		} catch (Exception e) {
			mSplashActivity.showUpdateDialog(appVersionBean_New.getVersionName()
					,appVersionBean_New.getVersionSize(),appVersionBean_New.getVersionDesc(),false);
		}

	}
	
	public void queryToDownApp(){
		mSplashActivity.startServiceForAppUpdate(appVersionBean_New.getAddressOfDown());
		mSplashActivity.finish();
	}
	
	public void cancelToDownApp() {
		
		boolean flagIsFirstLogin = PreferencesUtil.getBoolean(mSplashActivity,
				InformationCodeUtil.KeyFirstOpenApp, true);

		if(flagIsFirstLogin)
		{
			toGuideView();
		}else{
			expiredVerificationLogin();
		}
	}
	
	
	/**跳转到登录界面**/
	public void toLoginView() {
		//如果开始登录，则把本地下载的app删除掉
		try {
			String filePath = FileUtil.getDownFilePath(InformationCodeUtil.NameOfCurrentVersion);
			File file = new File(filePath);
			FileUtil.removeDirectory(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		final long lengTime = endTime - startTime;
		if(lengTime < SLEEPTIME){
	    	new Thread(){
	    		public void run() {
	    			SystemClock.sleep(SLEEPTIME-lengTime);
					mSplashActivity.toLoginView_DaiNiFei();
	    		};
	    	}.start();			
		}else{
			 	mSplashActivity.toLoginView_DaiNiFei();
		}
	}
	
	/**跳转到登录界面**/
	public void toGuideView() {
		//如果开始登录，则把本地下载的app删除掉
		try {
			String filePath = FileUtil.getDownFilePath(InformationCodeUtil.NameOfCurrentVersion);
			File file = new File(filePath);
			FileUtil.removeDirectory(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		final long lengTime = endTime - startTime;
		if(lengTime < SLEEPTIME){
			new Thread(){
				public void run() {
					SystemClock.sleep(SLEEPTIME-lengTime);
					mSplashActivity.toGuideView();
				};
			}.start();
		}else{
			mSplashActivity.toGuideView();
		}
	}
	
	public void toMainView(){
		//如果开始登录，则把本地下载的app删除掉
		try {
			String filePath = FileUtil.getDownFilePath(InformationCodeUtil.NameOfCurrentVersion);
			File file = new File(filePath);
			FileUtil.removeDirectory(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		long endTime = System.currentTimeMillis();
		final long lengTime = endTime - startTime;
		if(lengTime < SLEEPTIME){
			new Thread(){
				public void run() {
					SystemClock.sleep(SLEEPTIME-lengTime);
					mSplashActivity.toMainView();
				};
			}.start();
		}else{
			mSplashActivity.toMainView();
		}		
	}

	public void expiredVerificationLogin(){
		
		mCustomModel = PreferencesUtilMy.getCustomModel(mSplashActivity);
		if(mCustomModel == null){
			PreferencesUtilMy.clearCustomModel(mSplashActivity);
			toLoginView();
			return;
		}
		String methodName = InformationCodeUtil.methodNameCheckSign;
		SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
		requestSoapObject.addProperty("customID",mCustomModel.getDjLsh());
		requestSoapObject.addProperty("openKey",mCustomModel.getOpenKey());
		//请求网络获取用户信息
		ConnectCustomServiceAsyncTask ConnectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
				(mSplashActivity, this, requestSoapObject, methodName);
		ConnectCustomServiceAsyncTask.initProgressDialog(false);
		ConnectCustomServiceAsyncTask.setConnectOutTime(3000);
		ConnectCustomServiceAsyncTask.execute();
	}

	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		//检查用户登录信息是否超时
		try {
			Gson gson = new Gson();
			JSONResultMsgModel ResultModel = gson.fromJson(returnString, JSONResultMsgModel.class);
			if(1 == ResultModel.getSign()){
				CustomModel mCustomModel = PreferencesUtilMy.getCustomModel(mSplashActivity);
				MyApplication.setmCustomModel(mCustomModel);
				toMainView();
			}else{
				PreferencesUtilMy.clearCustomModel(mSplashActivity);
				toLoginView();
			}
		} catch (Exception e) {
			toLoginView();
		}			
	}
	
	@Override
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {
		PreferencesUtilMy.clearCustomModel(mSplashActivity);
		toLoginView();
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
			ActivityCollectorUtils.finishAll();		
	}
}
