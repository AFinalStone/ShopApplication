package com.shuimunianhua.xianglixiangqin.activity;

import com.google.gson.Gson;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultMsgModel;
import com.shuimunianhua.xianglixiangqin.model.CustomModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.PreferencesUtilMy;

import org.ksoap2.serialization.SoapObject;

/**
 * 登陆聚合批发系统登陆水木年华
 * @author SHI
 * @time 2016/8/8 19:03
 */
public class LoginShuiMuNianHuaActivityController implements OnConnectServerStateListener<String> {

	LoginShuiMuNianHuaActivity mLoginActivity;

	public LoginShuiMuNianHuaActivityController(LoginShuiMuNianHuaActivity mLoginDaiNiFeiActivity) {
		super();
		this.mLoginActivity = mLoginDaiNiFeiActivity;
	}

	
	public void login() {
		String strUserNumber = mLoginActivity.getUserPhoneNum();
		String strUserPassword = mLoginActivity.getUserPassword();
		String strUserCodeOfEnterShop = "1397";
		login_JvHe(strUserNumber, strUserPassword, strUserCodeOfEnterShop);
	}
	
	/**登录聚合**/
	public void login_JvHe(String strUserNumber, String strUserPassword, String strUserCodeOfEnterShop) {

		String methodName = InformationCodeUtil.methodNameDealerLoginExt;
		SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
		soapObject.addProperty("phoneNum", strUserNumber);
		soapObject.addProperty("passWord", strUserPassword);
		soapObject.addProperty("code", strUserCodeOfEnterShop);
		ConnectCustomServiceAsyncTask ConnectCustomServiceAsyncTask = 
				new ConnectCustomServiceAsyncTask(
				mLoginActivity, this, soapObject, methodName, strUserCodeOfEnterShop);
		ConnectCustomServiceAsyncTask.execute();		
	}
	
	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, String state, boolean whetherRefresh) {
			LogUtil.LogShitou("登录成功", returnString);
			Gson gson = new Gson();
			try {
				JSONResultMsgModel jSONResult = gson.fromJson(returnString,JSONResultMsgModel.class);
				if(jSONResult.getSign() == 1){
					CustomModel mCustomModel= gson.fromJson(jSONResult.getMsg(),CustomModel.class);
					mCustomModel.setLoginShopID(Integer.parseInt(state));
					PreferencesUtilMy.saveCustomModel(mLoginActivity, mCustomModel);
					MyApplication.setmCustomModel(mCustomModel);
					mLoginActivity.showMessage("登录成功");
					mLoginActivity.toMainView();
				}else{
					mLoginActivity.showMessage(jSONResult.getMsg());
				}
			} catch (Exception e) {
				e.printStackTrace();
				mLoginActivity.showMessage("服务器数据异常，请稍后再试");
			}
	}

	@Override
	public void connectServiceFailed(String methodName, String state, boolean whetherRefresh) {
		mLoginActivity.showMessage("网络异常，登陆失败");
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, String state, boolean whetherRefresh) {
	}


}
