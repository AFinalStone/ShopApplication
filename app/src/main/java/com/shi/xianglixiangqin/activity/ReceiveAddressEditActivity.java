package com.shi.xianglixiangqin.activity;

import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.model.AddressModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.ToastUtil;
import com.shi.xianglixiangqin.view.SelectAddressPopWindow;

import org.ksoap2.serialization.SoapObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @action 新增或者编辑收货地址
 * @author SHI
 * @date  2016-2-17 10:09:43
 */
public class ReceiveAddressEditActivity extends MyBaseActivity implements OnClickListener,OnConnectServerStateListener<Integer>{
	/**返回控件**/
	@BindView(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**标题**/
	@BindView(R.id.tv_title)
	 TextView tv_title;
	/**保存**/
	@BindView(R.id.tv_titleRight)
	TextView tv_titleRight;

	/**姓名**/
	@BindView(R.id.et_userNumber)
	 EditText et_userNumber;
	/**联系电话**/
	@BindView(R.id.et_userPhone)
	 EditText et_userPhone;


	/**选择省、市、区控件**/
	@BindView(R.id.linearLayout_selectAddress)
	LinearLayout linearLayout_selectAddress;
	/**所在地区**/
	@BindView(R.id.tv_selectAddress)
	 TextView tv_selectAddress;
	/**详细收货地址**/
	@BindView(R.id.et_receiverAddress)
	 EditText et_receiverAddress;
	/**当前待编辑的收货地址对象**/
	private AddressModel currentAddressModel;
	/**是否成功获取省、市、区**/
	private boolean selectSuccessFlag;



	SelectAddressPopWindow pop;
	View rootView;

	@Override
	public void initView() {
		rootView = View.inflate(mContext, R.layout.activity_receiver_address_edit, null);
		setContentView(rootView);
		ButterKnife.bind(this);
	}
	@Override
	public void initData() {
		currentAddressModel = (AddressModel) getIntent().getSerializableExtra("currentAddressModel");
		if(currentAddressModel == null){
			tv_title.setText("新增地址");
			selectSuccessFlag = false;
			currentAddressModel = new AddressModel();
			currentAddressModel.setDjLsh(-1);
			currentAddressModel.setUserID(MyApplication.getmCustomModel(mContext).getDjLsh());
		}else{
			selectSuccessFlag = true;
			tv_title.setText("修改地址");
			et_userNumber.setText(currentAddressModel.getRealName());
			et_userPhone.setText(currentAddressModel.getPhoneNum());
			et_receiverAddress.setText(currentAddressModel.getAddress());
			String strPosition = currentAddressModel.getProvinceName()+" "+currentAddressModel.getCityName()+" "+currentAddressModel.getAreaName();
			tv_selectAddress.setText(strPosition);
		}
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(this);
		tv_titleRight.setVisibility(View.VISIBLE);
		tv_titleRight.setText("保存");
		tv_titleRight.setOnClickListener(this);
		linearLayout_selectAddress.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_titleLeft:
			 finish();
			break;
		case R.id.linearLayout_selectAddress:
			showSelectAddressView();
			break;
		case R.id.tv_titleRight:
			if(checkMsg()){
				if("新增地址".equals(tv_title.getText())){
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

		pop = new SelectAddressPopWindow(this, new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 隐藏弹出窗口
				pop.dismiss();
				switch (v.getId()) {
					case R.id.tv_cancel:
						tv_selectAddress.setText("所在地区");
						selectSuccessFlag = false;
						break;
					case R.id.tv_ok:
						String selectAddress = pop.selectCurrentFirstPosition.getName()+" "
								+pop.selectCurrentSecondPosition.getName()+" "+pop.selectCurrentThirdPosition.getName();
						tv_selectAddress.setText(selectAddress);
						currentAddressModel.setProvinceCode(pop.selectCurrentFirstPosition.getCode());
						currentAddressModel.setCityCode(pop.selectCurrentSecondPosition.getCode());
						currentAddressModel.setAreaCode(pop.selectCurrentThirdPosition.getCode());
						selectSuccessFlag = true;
						break;
					default:
						break;
				}
			}
		});
		pop.showAtLocation(rootView, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
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
		if(et_userNumber.getText().toString().isEmpty()){
			ToastUtil.show(mContext, "请输入收货人名称");
			return false;
		}
		if(!StringUtil.matchRegex(et_userPhone.getText().toString(), 
				InformationCodeUtil.regExpPhotoNumber)){
			ToastUtil.show(mContext, "请输入合法的手机号码");
			return false;
		}
		if(et_receiverAddress.getText().toString().isEmpty()){
			ToastUtil.show(mContext, "请输入详细地址");
			return false;
		}
		if(selectSuccessFlag == false){
			ToastUtil.show(mContext, "请选择所在地区");
			return false;
		}
		return true;
	}

	private AddressModel getAddressModel(){

		currentAddressModel.setRealName(et_userNumber.getText().toString());
		currentAddressModel.setPhoneNum(et_userPhone.getText().toString());
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
}
