package com.shi.xianglixiangqin.frament;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.shi.xianglixiangqin.BuildConfig;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.interfaceImpl.OnTextChangeListener;
import com.shi.xianglixiangqin.model.CustomModel;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.PreferencesUtilMy;
import com.shi.xianglixiangqin.util.StringUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * @action 登陆
 * @author SHI
 * @date 2016-10-10 15:25:02
 */

public class LoginFragment_DongMiKeJi extends LoginBaseFragment{

	/** 账号或手机号框 **/
	@BindView(R.id.et_userNumber)
	EditText et_userNumber;
	/** 密码框 **/
	@BindView(R.id.et_userPassword)
	EditText et_userPassword;

	@BindView(R.id.iv_clearUserNumber)
	ImageView iv_clearUserNumber;
	@BindView(R.id.iv_clearUserPassword)
	ImageView iv_clearUserPassword;

	/** 登陆按钮 **/
	@BindView(R.id.btn_login)
	Button btn_login;

	private OnTextChangeListener textChangeListener = new OnTextChangeListener() {
		@Override
		public void afterTextChanged(Editable s) {

			if (!StringUtil.isEmpty(et_userNumber.getText().toString().trim())){
				iv_clearUserNumber.setVisibility(View.VISIBLE);
			}else{
				iv_clearUserNumber.setVisibility(View.INVISIBLE);
			}

			if (!StringUtil.isEmpty(et_userPassword.getText().toString().trim())){
				iv_clearUserPassword.setVisibility(View.VISIBLE);
			}else{
				iv_clearUserPassword.setVisibility(View.INVISIBLE);
			}

			if(!StringUtil.isEmpty(et_userNumber.getText().toString().trim())
					&& !StringUtil.isEmpty(et_userPassword.getText().toString().trim()))
			{
				btn_login.setEnabled(true);
			}else{
				btn_login.setEnabled(false);
			}
		}
	};


	@Override
	public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_login_dongmikeji, container, false);
		unbinder = ButterKnife.bind(this,rootView);
		et_userNumber.addTextChangedListener(textChangeListener);
		et_userPassword.addTextChangedListener(textChangeListener);
		return rootView;
	}

	@Override
	public void initData() {
		//清除购物车数据
		PreferencesUtilMy.clearShopCartAllGoods(mActivity);
		CustomModel mCustomModel = PreferencesUtilMy.getCustomModel(mActivity);
		if(mCustomModel != null){
			et_userNumber.setText(mCustomModel.getPhoneNum());
		}
	}

	@OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forgotPassword,R.id.iv_clearUserNumber, R.id.iv_clearUserPassword})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_login:
				login_JvHe(et_userNumber.getText().toString().trim()
						,et_userPassword.getText().toString().trim()
						,BuildConfig.LOGIN_SHOPCODE);
				break;
			case R.id.tv_register:
				toRegisterView();
				break;
			case R.id.tv_forgotPassword:
				toPasswordRetrieveView();
				break;
			case R.id.iv_clearUserNumber:
				et_userNumber.setText("");
				break;
			case R.id.iv_clearUserPassword:
				et_userPassword.setText("");
				break;
			default:
				break;
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == mActivity.RESULT_OK){
			et_userNumber.setText(data.getStringExtra(InformationCodeUtil.IntentPhoneNum));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**登录聚合**/
//	public void login() {
//		String strUserNumber = et_userNumber.getText().toString();
//		String strUserPassword = et_userPassword.getText().toString();
//		String strUserCodeOfEnterShop = BuildConfig.LOGIN_SHOPCODE;
//
//		String methodName = InformationCodeUtil.methodNameDealerLoginExt;
//		SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
//		soapObject.addProperty("phoneNum", strUserNumber);
//		soapObject.addProperty("passWord", strUserPassword);
//		soapObject.addProperty("code", strUserCodeOfEnterShop);
//		ConnectCustomServiceAsyncTask ConnectCustomServiceAsyncTask =
//				new ConnectCustomServiceAsyncTask(
//						mActivity, this, soapObject, methodName, strUserCodeOfEnterShop);
//		ConnectCustomServiceAsyncTask.execute();
//	}



//	@Override
//	public void connectServiceSuccessful(String returnString,
//										 String methodName, String state, boolean whetherRefresh) {
//		LogUtil.LogShitou("登录成功", returnString);
//		Gson gson = new Gson();
//		try {
//			JSONResultMsgModel jSONResult = gson.fromJson(returnString,JSONResultMsgModel.class);
//			if(jSONResult.getSign() == 1){
//				CustomModel mCustomModel= gson.fromJson(jSONResult.getMsg(),CustomModel.class);
//				mCustomModel.setLoginShopID(Integer.parseInt(state));
//				PreferencesUtilMy.saveCustomModel(mActivity, mCustomModel);
//				MyApplication.setmCustomModel(mCustomModel);
//				ToastUtil.show(mActivity,"登录成功");
//				mActivity.toMainView();
//			}else{
//				ToastUtil.show(mActivity,jSONResult.getMsg());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			ToastUtil.show(mActivity,"服务器数据异常，请稍后再试");
//		}
//	}
//
//	@Override
//	public void connectServiceFailed(String returnStrError, String methodName, String state, boolean whetherRefresh) {
//		ToastUtil.show(mActivity,"网络异常，登陆失败");
//	}
//
//
//	@Override
//	public void connectServiceCancelled(String returnString,
//										String methodName, String state, boolean whetherRefresh) {
//
//	}

}









