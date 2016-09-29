package com.shuimunianhua.xianglixiangqin.activity;

import org.ksoap2.serialization.SoapObject;

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

/**
 * 登陆聚合批发系统逻辑对象
 * @author SHI
 * @time 2016/8/8 19:03
 */
public class LoginJvHeActivityController implements OnConnectServerStateListener<String> {

	LoginJvHeActivity mLoginJvHeActivity;
	
	public LoginJvHeActivityController(LoginJvHeActivity mLoginDaiNiFeiActivity) {
		super();
		this.mLoginJvHeActivity = mLoginDaiNiFeiActivity;
	}

	
	public void login() {
		String strUserNumber = mLoginJvHeActivity.getUserPhoneNum();
		String strUserPassword = mLoginJvHeActivity.getUserPassword();
		String strUserCodeOfEnterShop = mLoginJvHeActivity.getUserCodeOfEnterShop();
		login_JvHe(strUserNumber, strUserPassword, strUserCodeOfEnterShop);
	}
	
	/**登录聚合**/
	public void login_JvHe(String strUserNumber, String strUserPassword, String strUserCodeOfEnterShop) {

//		if(StringUtil.isEmpty(strUserNumber)){
//			mLoginJvHeActivity.showMessage("用户名不能为空");
//			return;
//		}
//		if(StringUtil.isEmpty(strUserPassword)){
//			mLoginJvHeActivity.showMessage("密码不能为空");
//			return;
//		}
//		if(StringUtil.isEmpty(strUserCodeOfEnterShop) ){
//			mLoginJvHeActivity.showMessage("请输入店铺编号");
//			return;
//		}
		String methodName = InformationCodeUtil.methodNameDealerLoginExt;
		SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
		soapObject.addProperty("phoneNum", strUserNumber);
		soapObject.addProperty("passWord", strUserPassword);
		soapObject.addProperty("code", strUserCodeOfEnterShop);
		ConnectCustomServiceAsyncTask ConnectCustomServiceAsyncTask = 
				new ConnectCustomServiceAsyncTask(
				mLoginJvHeActivity, this, soapObject, methodName, strUserCodeOfEnterShop);
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
					PreferencesUtilMy.saveCustomModel(mLoginJvHeActivity, mCustomModel);
					MyApplication.setmCustomModel(mCustomModel);
					mLoginJvHeActivity.showMessage("登录成功");
					mLoginJvHeActivity.toMainView(mCustomModel.getLoginShopID());
				}else{
					mLoginJvHeActivity.showMessage(jSONResult.getMsg());
				}
			} catch (Exception e) {
				e.printStackTrace();
				mLoginJvHeActivity.showMessage("服务器数据异常，请稍后再试");
			}
	}

	@Override
	public void connectServiceFailed(String methodName, String state, boolean whetherRefresh) {
		mLoginJvHeActivity.showMessage("网络异常，登陆失败");
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, String state, boolean whetherRefresh) {
		
	}


}
