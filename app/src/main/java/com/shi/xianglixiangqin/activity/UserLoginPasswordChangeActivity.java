package com.shi.xianglixiangqin.activity;

import org.ksoap2.serialization.SoapObject;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.shi.xianglixiangqin.util.ToastUtil;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import butterknife.ButterKnife;
import butterknife.BindView;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultMsgModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.StringUtil;

public class UserLoginPasswordChangeActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer>{
	
	/**新密码**/
	@BindView(R.id.iv_titleLeft)
	ImageView iv_titleLeft;
	/**新密码**/
	@BindView(R.id.et_newPassword)
	EditText et_newPassword;
	/**确认新密码**/
	@BindView(R.id.et_confirmNewPassword)
	EditText et_confirmNewPassword;
	/**修改密码按钮**/
	@BindView(R.id.btn_changePassword)
	Button btn_changePassword;
	/**手机号**/
	private String strPhoneNumber;
	/**新密码**/
	private String strNewPassword;
	
	
	@Override
	public void initView() {
		setContentView(R.layout.activity_user_login_password_change);
		ButterKnife.bind(mContext);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);
		iv_titleLeft.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btn_changePassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(checkUserMsg()){
					getData(InformationCodeUtil.methodNameChangePwdExt);
				}
			}
		});
	}

	@Override
	public void initData() {
		strPhoneNumber = getIntent().getStringExtra(InformationCodeUtil.IntentPhoneNum);
	}

	private boolean checkUserMsg(){
		
		strNewPassword = et_newPassword.getText().toString().trim();
		String strConfirmNewPassword = et_confirmNewPassword.getText().toString().trim();

		if(StringUtil.isEmpty(strNewPassword)){
			et_newPassword.setError("新密码不能为空");
			et_newPassword.requestFocus();
			return false;
		}
		
		if(!strNewPassword.equals(strConfirmNewPassword)){
			et_confirmNewPassword.setError("两次密码输入不一致");
			et_confirmNewPassword.requestFocus();
			return false;
		}
		
		return true;
	}	
	
	private void getData(String methodName){
		SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
		requestSoapObject.addProperty("phoneNum", strPhoneNumber);
		requestSoapObject.addProperty("newPassWord", strNewPassword);
		ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
				(mContext, this, requestSoapObject, methodName);
		connectCustomServiceAsyncTask.execute();
	}

	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		JSONResultMsgModel mJSONBackResultModel = null;
		Gson gson = new Gson();
		try {
			mJSONBackResultModel = gson.fromJson(returnString, JSONResultMsgModel.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}

		if(mJSONBackResultModel != null && mJSONBackResultModel.getSign() == 1){
			ToastUtil.show(this, "成功修改密码");
			setResult(RESULT_OK);
			finish();
			return;
		}
		
		if(mJSONBackResultModel != null && mJSONBackResultModel.getSign() == 0){
			ToastUtil.show(mContext, mJSONBackResultModel.getMsg());
		}		
	}

	@Override
	public void connectServiceFailed(String returnStrError, String methodName, Integer state,
			boolean whetherRefresh) {
		ToastUtil.show(mContext, "网络异常,修改密码失败");
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		
	}
	
}
