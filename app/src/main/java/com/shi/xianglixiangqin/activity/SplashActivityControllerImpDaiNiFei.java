package com.shi.xianglixiangqin.activity;



import org.ksoap2.serialization.SoapObject;


import com.google.gson.Gson;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.json.JSONResultMsgModel;
import com.shi.xianglixiangqin.model.CustomModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.ActivityCollectorUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.PreferencesUtilMy;

/****
 * 闪屏页逻辑控制层
 * 
 * @author SHI 2016-2-25 14:28:35
 */
public class SplashActivityControllerImpDaiNiFei extends SplashActivityController {

	/**用户名手机号，密码,令牌**/
  
	public SplashActivityControllerImpDaiNiFei(SplashActivity mSplashActivity) {
		super();
		this.mSplashActivity = mSplashActivity;
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
//				CustomModel mCustomModel = PreferencesUtilMy.getCustomModel(mSplashActivity);
//				MyApplication.setmCustomModel( mSplashActivity, mCustomModel);
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
	public void connectServiceFailed(String returnStrError, String methodName, Integer state,
			boolean whetherRefresh) {
		PreferencesUtilMy.clearCustomModel(mSplashActivity);
		toLoginView();
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
			ActivityCollectorUtil.finishAll();
	}
}
