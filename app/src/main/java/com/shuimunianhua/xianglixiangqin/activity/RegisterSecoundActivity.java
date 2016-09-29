package com.shuimunianhua.xianglixiangqin.activity;


import org.ksoap2.serialization.SoapObject;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultMsgModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.StringUtil;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * @action 注册第二步
 * @author SHI
 * @date  
 */
public class RegisterSecoundActivity extends MyBaseActivity implements OnClickListener, OnConnectServerStateListener<Integer> {
	
	/**左侧返回控件**/
	@Bind(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**标题**/
	@Bind(R.id.tv_title)
	 TextView tv_title;
	/**注册地**/
	@Bind(R.id.tv_registerLocation)
	 TextView tv_registerLocation;
	/**选择注册地**/
	@Bind(R.id.btn_selectLocation)
	 ImageButton btn_selectLocation;
	/**选择邀请码**/
	@Bind(R.id.et_registerInvitationCode)
	 EditText et_registerInvitationCode;
	/**选择密码**/
	@Bind(R.id.et_registerPassword)
	 EditText et_registerPassword;
	/**再次输入密码**/
	@Bind(R.id.et_registerPasswordConfirm)
	 EditText et_registerPasswordConfirm;
	/**注册**/
	@Bind(R.id.btn_register)
	 Button btn_register;
	//用户注册信息
	private String strPhoneNumber;
	private String strRegisterLocation;
	private String strEmailNumber;
	private String strInvitationCode;
	private String strPassword;
	/**当前选择的城市编号**/
	private String cityCodeSelect;
	/**是否成功获取省、市、区**/
	private boolean selectSuccessFlag;	
	@Override
	public void initView() {
		setContentView(R.layout.activity_register_second);
		ButterKnife.bind(this);
		tv_title.setText("注册");
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(this);
		btn_selectLocation.setOnClickListener(this);
		btn_register.setOnClickListener(this);
	}

	@Override
	public void initData() {
		strPhoneNumber = getIntent().getStringExtra(InformationCodeUtil.IntentPhoneNum);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_titleLeft:
			finish();
			break;
		case R.id.btn_selectLocation:
			 startActivityForResult(new Intent(mContext,AddressPositionSelectActivity.class),1);
			 tv_registerLocation.setText(R.string.registerLocationHint);
			 selectSuccessFlag = false;
			break;
		case R.id.btn_register:
			getData(InformationCodeUtil.methodNameAddCustom);
			break;

		default:
			break;
		}		
	}
	
	public void onActivityResult(int requestCode,int resultCode,Intent data){
		switch (resultCode) {
		case RESULT_OK:
			//获取对应key的map值
			String text = data.getStringExtra(InformationCodeUtil.IntentAddressLocationSelect);
			if(!StringUtil.isEmpty(text)){
				selectSuccessFlag = true;
				tv_registerLocation.setText(text);
				cityCodeSelect = MyApplication.getmAddressModel().getAreaCode();
				LogUtil.LogShitou("cityCodeSelect", ""+cityCodeSelect);
			}
			break;
		}
	}

	private boolean checkUserMsg(){
		
		strRegisterLocation = tv_registerLocation.getText().toString().trim();
		strEmailNumber = "example@dainifei.com";
		strInvitationCode = et_registerInvitationCode.getText().toString().trim();
		strPassword = et_registerPassword.getText().toString().trim();
		String strConfirmPassword = et_registerPasswordConfirm.getText().toString().trim();	
		
		//检验注册地址是否为空
		if(!selectSuccessFlag){
			ToastUtils.show(mContext,"请选择注册地址");
			return false;
		}
		//检验邀请码是否为空
		if(StringUtil.isEmpty(strInvitationCode)){
			strInvitationCode = "0";
		}
		//检验密码是否合法
		if(StringUtil.isEmpty(strPassword)){
			et_registerPassword.setError("密码不能为空");
			et_registerPassword.requestFocus();
			return false;
		}
		if(!strPassword.equals(strConfirmPassword)){
			et_registerPasswordConfirm.setError("两次密码输入不一致,请重新输入");
			et_registerPasswordConfirm.requestFocus();
			return false;
		}
		return true;
	}
	
	public void getData(String methodName){
		
		if(InformationCodeUtil.methodNameAddCustom.equals(methodName)){

			if(checkUserMsg()){
				SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
				requestSoapObject.addProperty("phoneNum", strPhoneNumber);
				requestSoapObject.addProperty("email", strEmailNumber);
				requestSoapObject.addProperty("passWord", strPassword);
				requestSoapObject.addProperty("roleID", 3);
				requestSoapObject.addProperty("cityCode", cityCodeSelect);
				requestSoapObject.addProperty("code", strInvitationCode);
				requestSoapObject.addProperty("shopName", "");
				requestSoapObject.addProperty("realName", "");
				ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
				(mContext, this, requestSoapObject , methodName);
				connectCustomServiceAsyncTask.execute();				
				LogUtil.LogShitou("注册", requestSoapObject.toString());
			}
		}
		
	}

	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
//		String result = returnSoapObject.getPropertyAsString(methodName+"Result");
//		LogUtil.LogShitou("注册结果", result);
		JSONResultMsgModel mJSONBackResultModel = null;
		Gson gson = new Gson();
		try {
			mJSONBackResultModel = gson.fromJson(returnString, JSONResultMsgModel.class);
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}

		if(InformationCodeUtil.methodNameAddCustom.equals(methodName)){
				
			if(mJSONBackResultModel == null){
				ToastUtils.show(this, "注册异常,账号注册失败");
				return;
			}
			if(mJSONBackResultModel != null && mJSONBackResultModel.getSign() == 1){
				ToastUtils.show(this, "账号注册成功");
				setResult(RESULT_OK);
				finish();
				return;
			}
			if(mJSONBackResultModel != null && mJSONBackResultModel.getSign() == 0){
				ToastUtils.show(mContext, mJSONBackResultModel.getMsg());
			}		
		}
	}

	@Override
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {
		
		if(InformationCodeUtil.methodNameAddCustom.equals(methodName)){
			ToastUtils.show(this, "网络异常，账号注册失败");
		}

	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
	}

}
