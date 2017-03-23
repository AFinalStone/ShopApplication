package com.shi.xianglixiangqin.activity;


import org.ksoap2.serialization.SoapObject;

import com.afinalstone.androidstudy.view.wheel.widget.OnWheelChangedListener;
import com.afinalstone.androidstudy.view.wheel.widget.WheelView;
import com.afinalstone.androidstudy.view.wheel.widget.adapters.BaseWheelTextAdapter;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.model.AddressSelectModel;
import com.shi.xianglixiangqin.util.ToastUtil;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import butterknife.ButterKnife;
import butterknife.BindView;
import butterknife.OnClick;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultMsgModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.view.FragmentViewDialog;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @action 注册第二步
 * @author SHI
 * @date  
 */
public class UserRegisterActivity extends MyBaseActivity implements OnClickListener,OnWheelChangedListener,OnConnectServerStateListener<Integer> {
	
	/**左侧返回控件**/
	@BindView(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**标题**/
	@BindView(R.id.tv_title)
	 TextView tv_title;
	/**注册地**/
	@BindView(R.id.tv_registerLocation)
	 TextView tv_registerLocation;
	/**姓名**/
	@BindView(R.id.et_registerUserName)
	 EditText et_registerUserName;
	/**公司名称**/
	@BindView(R.id.et_registerCompanyName)
	 EditText et_registerCompanyName;
	/**选择密码**/
	@BindView(R.id.et_registerPassword)
	 EditText et_registerPassword;
	/**再次输入密码**/
	@BindView(R.id.et_registerPasswordConfirm)
	 EditText et_registerPasswordConfirm;
	/**注册**/
	@BindView(R.id.btn_register)
	 Button btn_register;
	//用户注册信息
	private String strPhoneNumber;
	private String strRegisterLocation;
	private String strEmailNumber;
	private String strInvitationCode;
	private String strRegisterUserName;
	private String strRegisterCompanyName;
	private String strPassword;
	/**当前选择的城市编号**/
	private String cityCodeSelect;
	/**是否成功获取省、市、区**/
	private boolean selectSuccessFlag;

	/**
	 * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
	 */
	private List<AddressSelectModel> listData_All;
	private List<AddressSelectModel> listData_Second;
	private List<AddressSelectModel> listData_Third;

	private AddressSelectModel selectCurrentFirstPosition;
	private AddressSelectModel selectCurrentSecondPosition;
	private AddressSelectModel selectCurrentThirdPosition;
	/**
	 * 省的WheelView控件
	 */
	private WheelView mProvince;
	/**
	 * 市的WheelView控件
	 */
	private WheelView mCity;
	/**
	 * 区的WheelView控件
	 */
	private WheelView mArea;


	@Override
	public void initView() {
		setContentView(R.layout.activity_user_register);
		ButterKnife.bind(this);
		iv_titleLeft.setVisibility(View.VISIBLE);
	}


	@Override
	public void initData() {
		strPhoneNumber = getIntent().getStringExtra(InformationCodeUtil.IntentPhoneNum);

		AssetManager am = getAssets();
		Gson gson = new Gson();
		try {
			InputStream is = am.open("json_address_select");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			listData_All = gson.fromJson(sb.toString(), new TypeToken<List<AddressSelectModel>>(){}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	@OnClick({R.id.iv_titleLeft, R.id.linearLayout_selectLocation, R.id.btn_register})
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_titleLeft:
			finish();
			break;
		case R.id.linearLayout_selectLocation:
			showSelectAddressView();
			break;
		case R.id.btn_register:
			getData(InformationCodeUtil.methodNameAddCustom);
			break;
		default:
			break;
		}		
	}


	private void showSelectAddressView(){
		View selectAddressView = View.inflate(mContext, R.layout.dialog_address_position_select, null);
		mProvince = (WheelView) selectAddressView.findViewById(R.id.id_province);
		mCity = (WheelView) selectAddressView.findViewById(R.id.id_city);
		mArea = (WheelView) selectAddressView.findViewById(R.id.id_area);
		mProvince.setViewAdapter(new MyWheelAdapter(this, listData_All));
		// 添加change事件
		mProvince.addChangingListener(this);
		// 添加change事件
		mCity.addChangingListener(this);
		// 添加change事件
		mArea.addChangingListener(this);
		mProvince.setVisibleItems(7);
		mCity.setVisibleItems(7);
		mArea.setVisibleItems(7);
		mProvince.setCurrentItem(0);
		updateCities();
		updateAreas();
		FragmentViewDialog fragmentViewDialog = new FragmentViewDialog();
		fragmentViewDialog.initView(selectAddressView, "取消", "确定", new FragmentViewDialog.OnButtonClickListener() {
			@Override
			public void OnOkClick() {
				String registerAddress = selectCurrentFirstPosition.getName()+" "
						+selectCurrentSecondPosition.getName()+" "+selectCurrentThirdPosition.getName();
				tv_registerLocation.setText(registerAddress);
				cityCodeSelect = selectCurrentThirdPosition.getCode();
				selectSuccessFlag = true;
			}

			@Override
			public void OnCancelClick() {
				tv_registerLocation.setText(R.string.registerLocationHint);
				selectSuccessFlag = false;
			}
		});
		fragmentViewDialog.show(getSupportFragmentManager(),"fragmentViewDialog");
	}
	
	private boolean checkUserMsg(){
		
		strRegisterLocation = tv_registerLocation.getText().toString().trim();
		strEmailNumber = "example@jvhe.com";
		strInvitationCode = "0";
		strRegisterUserName = et_registerUserName.getText().toString().trim();
		strRegisterCompanyName = et_registerCompanyName.getText().toString().trim();
		strPassword = et_registerPassword.getText().toString().trim();
		String strConfirmPassword = et_registerPasswordConfirm.getText().toString().trim();

		if(StringUtil.isEmpty(strRegisterUserName)){
			et_registerUserName.setError(getResources().getString(R.string.et_registerUserNameHint));
			et_registerUserName.requestFocus();
			return false;
		}

		if(!selectSuccessFlag){
			ToastUtil.show(mContext,getResources().getString(R.string.registerLocationHint));
			return false;
		}
		if(StringUtil.isEmpty(strRegisterCompanyName)){
			et_registerCompanyName.setError(getResources().getString(R.string.et_registerCompanyNameHint));
			et_registerCompanyName.requestFocus();
			return false;
		}
		if(StringUtil.isEmpty(strPassword)){
			et_registerPassword.setError(getResources().getString(R.string.et_registerPasswordHint));
			et_registerPassword.requestFocus();
			return false;
		}
		if(!strPassword.equals(strConfirmPassword)){
			et_registerPasswordConfirm.setError("两次密码不一致");
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
				requestSoapObject.addProperty("shopName", strRegisterCompanyName);
				requestSoapObject.addProperty("realName", strRegisterUserName);
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
				ToastUtil.show(this, "注册异常,账号注册失败");
				return;
			}
			if(mJSONBackResultModel != null && mJSONBackResultModel.getSign() == 1){
				ToastUtil.show(this, "账号注册成功");
				setResult(RESULT_OK);
				finish();
				return;
			}
			if(mJSONBackResultModel != null && mJSONBackResultModel.getSign() == 0){
				ToastUtil.show(mContext, mJSONBackResultModel.getMsg());
			}		
		}
	}

	@Override
	public void connectServiceFailed(String returnStrError, String methodName, Integer state,
			boolean whetherRefresh) {
		
		if(InformationCodeUtil.methodNameAddCustom.equals(methodName)){
			ToastUtil.show(this, "网络异常，账号注册失败");
		}

	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
	}

	/**
	 * change事件的处理
	 */
	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue)
	{
		if (wheel == mProvince)
		{
			updateCities();
		} else if (wheel == mCity)
		{
			updateAreas();
		} else if (wheel == mArea)
		{
			selectCurrentThirdPosition = listData_Third.get(newValue);
		}
	}

	/**
	 * 根据当前的市，更新区WheelView的信息
	 */
	private void updateAreas()
	{
		int pCurrent = mCity.getCurrentItem();
		selectCurrentSecondPosition =  listData_Second.get(pCurrent);
		listData_Third = selectCurrentSecondPosition.getSubChinaCity();
		mArea.setViewAdapter(new MyWheelAdapter(this, listData_Third));
		mArea.setCurrentItem(0);
		if(listData_Third != null && listData_Third.size() != 0){
			selectCurrentThirdPosition = listData_Third.get(0);
		}
	}

	/**
	 * 根据当前的省，更新市WheelView的信息
	 */
	private void updateCities()
	{
		int pCurrent = mProvince.getCurrentItem();
		selectCurrentFirstPosition = listData_All.get(pCurrent);
		listData_Second = selectCurrentFirstPosition.getSubChinaCity();
		mCity.setViewAdapter(new MyWheelAdapter(this, listData_Second));
		mCity.setCurrentItem(0);
		updateAreas();
	}

	private class MyWheelAdapter extends BaseWheelTextAdapter<AddressSelectModel> {

		public MyWheelAdapter(Context context, List<AddressSelectModel> listData) {
			super(context, listData);
		}

		@Override
		protected CharSequence getItemText(int index) {
			return listData.get(index).getName();
		}
	}
}
