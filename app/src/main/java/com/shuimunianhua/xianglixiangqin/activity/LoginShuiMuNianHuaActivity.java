package com.shuimunianhua.xianglixiangqin.activity;


import android.content.Intent;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnTextChangeListener;
import com.shuimunianhua.xianglixiangqin.model.CustomModel;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.PreferencesUtilMy;
import com.shuimunianhua.xianglixiangqin.util.StringUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 
 * @author SHI
 *	登录页面 登陆聚合批发系统
 *  2016-2-1 11:40:57
 */
public class LoginShuiMuNianHuaActivity extends MyBaseActivity{

	/** 账号或手机号框 **/
	@Bind(R.id.et_userNumber)
	 EditText et_userNumber;
	/** 密码框 **/
	@Bind(R.id.et_userPassword)
	 EditText et_userPassword;
	/** 登陆按钮 **/
	@Bind(R.id.btn_login)
	Button btn_login;

	private int RequestCode_RegisterView = 1;
	private int RequestCode_PasswordRetrieveView = 2;

	LoginShuiMuNianHuaActivityController mLoginActivityController;

	private OnTextChangeListener textChangeListener = new OnTextChangeListener() {
		@Override
		public void afterTextChanged(Editable s) {
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
	public void initView() {
		setContentView(R.layout.activity_login_shuimunianhua);
		ButterKnife.bind(this);
		setReturnStatu(true);

		et_userNumber.addTextChangedListener(textChangeListener);
		et_userPassword.addTextChangedListener(textChangeListener);
	}

	@Override
	public void initData() {
		mLoginActivityController = new LoginShuiMuNianHuaActivityController(this);
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

	public void toMainView() {
		Intent intent = new Intent(this,MainActivity.class);
		startActivity(intent);	
		finish();
	}

	public void toRegisterView() {
		Intent intent = new Intent(this,RegisterFirstActivity.class);
		startActivityForResult(intent, RequestCode_RegisterView);
	}

	public void toPasswordRetrieveView() {
		Intent intent = new Intent(this,PasswordChangeFirstActivity.class);
		startActivityForResult(intent, RequestCode_PasswordRetrieveView);
	}

	@OnClick({R.id.btn_login, R.id.tv_register, R.id.tv_forgotPassword})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_login:
				mLoginActivityController.login();
				break;
			case R.id.tv_register:
				toRegisterView();
				break;
			case R.id.tv_forgotPassword:
				toPasswordRetrieveView();
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










