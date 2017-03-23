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
 * @action 登陆批发系统
 * @author SHI
 * @date 2016-10-10 15:25:02
 */

public class LoginFragment_General extends LoginBaseFragment{

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
		View rootView = inflater.inflate(R.layout.fragment_login_xianglixiangqin, container, false);
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


}









