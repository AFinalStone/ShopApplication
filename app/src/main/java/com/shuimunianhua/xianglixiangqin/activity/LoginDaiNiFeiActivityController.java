package com.shuimunianhua.xianglixiangqin.activity;

import org.ksoap2.serialization.SoapObject;

import com.google.gson.Gson;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.model.CustomModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.PreferencesUtilMy;
import com.shuimunianhua.xianglixiangqin.util.StringUtil;

/**
 * 登陆带你飞平台逻辑控制对象
 * @author SHI
 * @time 2016/8/8 19:04
 */
public class LoginDaiNiFeiActivityController implements OnConnectServerStateListener<Integer> {

	LoginDaiNiFeiActivity mLoginDaiNiFeiActivity;
	
	public LoginDaiNiFeiActivityController(LoginDaiNiFeiActivity mLoginDaiNiFeiActivity) {
		super();
		this.mLoginDaiNiFeiActivity = mLoginDaiNiFeiActivity;
	}

	public void login() {
		String strUserNumber = mLoginDaiNiFeiActivity.getUserPhoneNum();
		String strUserPassword = mLoginDaiNiFeiActivity.getUserPassword();
		login_DaiNiFei(strUserNumber,strUserPassword);
	}


	public void register() {
		mLoginDaiNiFeiActivity.toRegisterView();
	}


	public void passwordRetrieve() {
		mLoginDaiNiFeiActivity.toPasswordRetrieveView();
	}
	
	
	/**登录带你飞**/
	public void login_DaiNiFei( String strUserNumber, String strUserPassword) {
		
			if(StringUtil.isEmpty(strUserNumber)){
				mLoginDaiNiFeiActivity.setUserNumberError("用户名不能为空");
				return;
			}
			if(StringUtil.isEmpty(strUserPassword)){
				mLoginDaiNiFeiActivity.setUserPasswordError("密码不能为空");
				return;
			}
			String methodName = InformationCodeUtil.methodNameLogin;
			SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			soapObject.addProperty("phoneNum", strUserNumber);
			soapObject.addProperty("passWord", strUserPassword);
			ConnectCustomServiceAsyncTask ConnectCustomServiceAsyncTask = 
					new ConnectCustomServiceAsyncTask(
							mLoginDaiNiFeiActivity, this, soapObject, methodName);
			ConnectCustomServiceAsyncTask.execute();
	}

//登陆成功	{"DjLsh":841,"Email":"602392033@qq.com","ImgUrl":"http:\/\/192.168.0.108:8001\/UploadFiles\/HeadImg\/20160625\/520200924717.Png","Integral":0.000,"LocCityCode":"330100","LocProCode":"330000","LocSiteName":"杭州站","OpenKey":"524023533789","PassWord":"","PhoneNum":"18211673059","RealName":"机器人","RoleID":3,"RongCloudToken":"\/4GxIhFE4t7ehK12lbqiZ7LfFd646bAyQwVEmibNS\/fuv3zZknoq9y9GA+f\/t0nM0hxdXxndIy6XCbB5l8IpuA==","Sex":"男士","ShopID":855,"ShopName":"灯火蓝州","ShopUserID":0,"UserName":"18211673059","WeChatImgUrl":null}
	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
			Gson gson = new Gson();
			try {
				CustomModel mCustomModel= gson.fromJson(returnString,CustomModel.class);
				PreferencesUtilMy.saveCustomModel(mLoginDaiNiFeiActivity, mCustomModel);
				MyApplication.setmCustomModel(mCustomModel);
				mLoginDaiNiFeiActivity.showMessage("登录成功");
				mLoginDaiNiFeiActivity.toMainView();
			} catch (Exception e) {
				mLoginDaiNiFeiActivity.showMessage("密码错误或账号冻结，请重新尝试");
				e.printStackTrace();
			}
	}

	@Override
	public void connectServiceFailed(String methodName, Integer state, boolean whetherRefresh) {
		mLoginDaiNiFeiActivity.showMessage("网络异常,登录失败");
	}
	
	
	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		
	}


}
