package com.shi.xianglixiangqin.activity;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afinalstone.androidstudy.view.wheel.widget.OnWheelChangedListener;
import com.afinalstone.androidstudy.view.wheel.widget.WheelView;
import com.afinalstone.androidstudy.view.wheel.widget.adapters.BaseWheelTextAdapter;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.model.AddressSelectModel;
import com.shi.xianglixiangqin.util.ToastUtil;

import com.google.gson.Gson;
import butterknife.ButterKnife;
import butterknife.BindView;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.model.AddressModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.view.FragmentViewDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @action 新增或者编辑收货地址
 * @author SHI
 * @date  2016-2-17 10:09:43
 */
public class ReceiveAddressEditActivityEx extends MyBaseActivity implements OnClickListener, OnWheelChangedListener,OnConnectServerStateListener<Integer>{
	/**返回控件**/
	@BindView(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**标题**/
	@BindView(R.id.tv_title)
	 TextView tv_title;
	/**姓名**/
	@BindView(R.id.et_receiverName)
	 EditText et_receiverName;
	/**手机号码**/
	@BindView(R.id.et_userPhone)
	 EditText et_userPhone;
	/**邮政编码**/
	@BindView(R.id.et_postCode)
	 EditText et_postCode;
	/**选择省、市、区控件**/
	@BindView(R.id.relativeLayout_select)
	 RelativeLayout relativeLayout_select;
	/**省、市、区按钮**/
	@BindView(R.id.tv_selectPosition)
	 TextView tv_selectPosition;
	/**详细收货地址**/
	@BindView(R.id.et_receiverAddress)
	 EditText et_receiverAddress;
	/**确认按钮**/
	@BindView(R.id.btn_confirm)
	 Button btn_confirm;
	/**当前待编辑的收货地址对象**/
	private AddressModel currentAddressModel;
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
		setContentView(R.layout.activity_receiver_address_edit_ex);
		ButterKnife.bind(this);
	}
	@Override
	public void initData() {
		currentAddressModel = (AddressModel) getIntent().getSerializableExtra("currentAddressModel");
		if(currentAddressModel == null){
			tv_title.setText("新增收货地址");
			selectSuccessFlag = false;
			currentAddressModel = new AddressModel();
			currentAddressModel.setDjLsh(-1);
			currentAddressModel.setUserID(MyApplication.getmCustomModel(mContext).getDjLsh());
		}else{
			selectSuccessFlag = true;
			tv_title.setText("修改收货地址");
			et_receiverName.setText(currentAddressModel.getRealName());
			et_userPhone.setText(currentAddressModel.getPhoneNum());
			et_postCode.setText(currentAddressModel.getPostCode());
			et_receiverAddress.setText(currentAddressModel.getAddress());
			String strPosition = currentAddressModel.getProvinceName()+" "+currentAddressModel.getCityName()+" "+currentAddressModel.getAreaName();
			tv_selectPosition.setText(strPosition);
//			MyApplication.setmAddressModel(currentAddressModel);
		}
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		relativeLayout_select.setOnClickListener(this);

		initSelectAddressData();
	}
	private void initSelectAddressData(){
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_titleLeft:
			 finish();
			break;
		case R.id.relativeLayout_select:
			showSelectAddressView();
			break;
		case R.id.btn_confirm:
			if(checkMsg()){
				if("新增收货地址".equals(tv_title.getText())){
					getData(InformationCodeUtil.methodNameAddAddr);
				}else{
					getData(InformationCodeUtil.methodNameEditAddr);
				}
			}
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
				String selectAddress = selectCurrentFirstPosition.getName()+" "
						+selectCurrentSecondPosition.getName()+" "+selectCurrentThirdPosition.getName();
				tv_selectPosition.setText(selectAddress);
				currentAddressModel.setProvinceCode(selectCurrentFirstPosition.getCode());
				currentAddressModel.setCityCode(selectCurrentSecondPosition.getCode());
				currentAddressModel.setAreaCode(selectCurrentThirdPosition.getCode());
				selectSuccessFlag = true;
			}

			@Override
			public void OnCancelClick() {
				tv_selectPosition.setText(R.string.selectPosition);
				selectSuccessFlag = false;
			}
		});
		fragmentViewDialog.show(getSupportFragmentManager(),"fragmentViewDialog");
	}

	private void getData(String methodName) {
		if(methodName.equals(InformationCodeUtil.methodNameAddAddr)){
			Gson gson = new Gson();
			String addressJson = gson.toJson(getAddressModel());
			SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			soapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
			soapObject.addProperty("openKey",  MyApplication.getmCustomModel(mContext).getOpenKey());
			soapObject.addProperty("addrJson", addressJson);
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
					mContext, this, soapObject, methodName);
			connectCustomServiceAsyncTask.execute();
			return;
		}
		if(methodName.equals(InformationCodeUtil.methodNameEditAddr)){
			Gson gson = new Gson();
			String addressJson = gson.toJson(getAddressModel());
			SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			soapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
			soapObject.addProperty("openKey",  MyApplication.getmCustomModel(mContext).getOpenKey());
			soapObject.addProperty("addrJson", addressJson);
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
					mContext, this, soapObject, methodName);
			connectCustomServiceAsyncTask.execute();
			return;
		}
	}
	
	private boolean checkMsg(){
		if(et_receiverName.getText().toString().isEmpty()){
			ToastUtil.show(mContext, "请输入收件人名称");
			return false;
		}
		if(!StringUtil.matchRegex(et_userPhone.getText().toString(), 
				InformationCodeUtil.regExpPhotoNumber)){
			ToastUtil.show(mContext, "请输入合法的手机号码");
			return false;
		}
		if(et_postCode.getText().toString().isEmpty()){
			ToastUtil.show(mContext, "请输入邮政编码");
			return false;
		}
		if(et_receiverAddress.getText().toString().isEmpty()){
			ToastUtil.show(mContext, "请输入详细地址");
			return false;
		}
		if(selectSuccessFlag == false){
			ToastUtil.show(mContext, "请选择所在城市");
			return false;
		}
		return true;
	}

	private AddressModel getAddressModel(){

		currentAddressModel.setRealName(et_receiverName.getText().toString());
		currentAddressModel.setPhoneNum(et_userPhone.getText().toString());
		currentAddressModel.setPostCode(et_postCode.getText().toString());
		currentAddressModel.setAddress(et_receiverAddress.getText().toString());
		return currentAddressModel;
		
	}


//	public void onActivityResult(int requestCode,int resultCode,Intent data){
//		switch (resultCode) {
//		case RESULT_OK:
//			//获取对应key的map值
//			String text = data.getStringExtra(InformationCodeUtil.IntentAddressLocationSelect);
//			if(!StringUtil.isEmpty(text)){
//				tv_selectPosition.setText(text);
//				selectSuccessFlag = true;
//				currentAddressModel.setProvinceCode(MyApplication.getmAddressModel().getProvinceCode());
//				currentAddressModel.setCityCode(MyApplication.getmAddressModel().getCityCode());
//				currentAddressModel.setAreaCode(MyApplication.getmAddressModel().getAreaCode());
//			}
//			break;
//		}
//	}

	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
//		    LogUtil.LogShitou("操作地址成功", returnSoapObject.toString());
			if(returnString.contains("\"Sign\":1")){
				ToastUtil.show(mContext, "操作成功");
				finish();
			}
	}
	@Override
	public void connectServiceFailed(String returnStrError, String methodName, Integer state,
			boolean whetherRefresh) {
			ToastUtil.show(mContext, "操作失败，请检查网络");
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
