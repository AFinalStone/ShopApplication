package com.shuimunianhua.xianglixiangqin.activity;


import com.shuimunianhua.xianglixiangqin.model.CustomModel;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.PreferencesUtilMy;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/***
 * 
 * @author SHI
 *	登录页面 登陆带你飞
 *  2016-2-1 11:40:57
 */
public class LoginDaiNiFeiActivity extends MyBaseActivity implements OnClickListener{

	/**左侧返回控件**/
	@Bind(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**标题名称**/
	@Bind(R.id.tv_title)
	 TextView tv_title;
	/**右侧注册控件**/
	@Bind(R.id.tv_titleRight)
	 TextView tv_titleRight;
	/** 账号或手机号框 **/
	@Bind(R.id.et_userNumber)
	 EditText et_userNumber;
	/** 密码框 **/
	@Bind(R.id.et_userPassword)
	 EditText et_userPassword;

	private int RequestCode_RegisterView = 1;
	private int RequestCode_PasswordRetrieveView = 2;
	LoginDaiNiFeiActivityController mLoginActivityController;
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_login_dainifei);
		ButterKnife.bind(this);
		setReturnStatu(true);
		tv_title.setText("登录");
		tv_titleRight.setVisibility(View.VISIBLE);
		tv_titleRight.setText("注册");
	}
	
	@Override
	public void initData() {
		//清除购物车数据
		PreferencesUtilMy.clearShopCartAllGoods(mContext);
		mLoginActivityController = new LoginDaiNiFeiActivityController(this);

		CustomModel mCustomModel = PreferencesUtilMy.getCustomModel(mContext);
		if(mCustomModel != null){
			et_userNumber.setText(mCustomModel.getPhoneNum());
		}
	}

	public String getUserPhoneNum() {
		return et_userNumber.getText().toString();
	}

	public String getUserPassword() {
		return et_userPassword.getText().toString();
	}

	public void showMessage(String msg) {
		ToastUtils.show(mContext, msg);
	}

	public void setUserNumberError(String strError){
		et_userNumber.setError(strError);
		et_userNumber.requestFocus();
	}

	public void setUserPasswordError(String strError){
		et_userPassword.setError(strError);
		et_userPassword.requestFocus();
	}


	public void toMainView() {
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);
		finish();
	}

	public void toRegisterView() {
		Intent intent = new Intent(this,RegisterFirstActivity.class);
		startActivityForResult(intent,RequestCode_RegisterView);
	}

	public void toPasswordRetrieveView() {
		Intent intent = new Intent(this,PasswordChangeFirstActivity.class);
		startActivityForResult(intent,RequestCode_PasswordRetrieveView);
	}

	@OnClick({R.id.btn_login, R.id.tv_titleRight, R.id.tv_forgotPassword})
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			mLoginActivityController.login();
			break;
		case R.id.tv_titleRight:
			mLoginActivityController.register();
			break;
		case R.id.tv_forgotPassword:
			mLoginActivityController.passwordRetrieve();
			break;
		default:
			break;
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK){
			et_userNumber.setText(data.getStringExtra(InformationCodeUtil.IntentPhoneNum));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}










