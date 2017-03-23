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
import android.widget.TextView;

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
 * @action 登陆带你飞
 * @author SHI
 * @date 2016-10-10 15:25:02
 */

public class LoginFragment_DaiNiFei extends LoginBaseFragment{

	/**标题名称**/
	@BindView(R.id.tv_title)
	TextView tv_title;
	/**右侧注册控件**/
	@BindView(R.id.tv_titleRight)
	TextView tv_titleRight;
	/** 账号或手机号框 **/
	@BindView(R.id.et_userNumber)
	EditText et_userNumber;
	/** 密码框 **/
	@BindView(R.id.et_userPassword)
	EditText et_userPassword;
	/** 登陆按钮 **/
	@BindView(R.id.btn_login)
	Button btn_login;

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
	public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_login_dainifei, container, false);
		unbinder = ButterKnife.bind(this,rootView);
		tv_title.setText("登录");
		tv_titleRight.setVisibility(View.VISIBLE);
		tv_titleRight.setText("注册");
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

	@OnClick({R.id.btn_login, R.id.tv_titleRight, R.id.tv_forgotPassword})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_login:
				login_DaiNiFei(et_userNumber.getText().toString().trim()
						,et_userPassword.getText().toString().trim());
				break;
			case R.id.tv_titleRight:
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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == mActivity.RESULT_OK){
			et_userNumber.setText(data.getStringExtra(InformationCodeUtil.IntentPhoneNum));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

//	public void toMainView() {
//		Intent intent = new Intent(mActivity,MainActivity.class);
//		startActivity(intent);
//		mActivity.finish();
//	}
//
//	public void toRegisterView() {
//		Intent intent = new Intent(mActivity,UserGetSecurityCodeActivity.class);
//		startActivityForResult(intent,RequestCode_RegisterView);
//	}
//


	//登陆成功	{"DjLsh":841,"Email":"602392033@qq.com","ImgUrl":"http:\/\/192.168.0.108:8001\/UploadFiles\/HeadImg\/20160625\/520200924717.Png","Integral":0.000,"LocCityCode":"330100","LocProCode":"330000","LocSiteName":"杭州站","OpenKey":"524023533789","PassWord":"","PhoneNum":"18211673059","RealName":"机器人","RoleID":3,"RongCloudToken":"\/4GxIhFE4t7ehK12lbqiZ7LfFd646bAyQwVEmibNS\/fuv3zZknoq9y9GA+f\/t0nM0hxdXxndIy6XCbB5l8IpuA==","Sex":"男士","ShopID":855,"ShopName":"灯火蓝州","ShopUserID":0,"UserName":"18211673059","WeChatImgUrl":null}
//	@Override
//	public void connectServiceSuccessful(String returnString,
//										 String methodName, Integer state, boolean whetherRefresh) {
//		Gson gson = new Gson();
//		try {
//			CustomModel mCustomModel= gson.fromJson(returnString,CustomModel.class);
//			mCustomModel.setLoginShopID(-1);
//			PreferencesUtilMy.saveCustomModel(mActivity, mCustomModel);
//			MyApplication.setmCustomModel(mCustomModel);
//			ToastUtil.show(mActivity,"登录成功");
//			toMainView();
//		} catch (Exception e) {
//			ToastUtil.show(mActivity,"密码错误或账号冻结，请重新尝试");
//			e.printStackTrace();
//		}
//	}
//
//	@Override
//	public void connectServiceFailed(String returnStrError, String methodName, Integer state, boolean whetherRefresh) {
//		ToastUtil.show(mActivity,"网络异常,登录失败");
//	}
//
//
//	@Override
//	public void connectServiceCancelled(String returnString,
//										String methodName, Integer state, boolean whetherRefresh) {
//
//	}


}









