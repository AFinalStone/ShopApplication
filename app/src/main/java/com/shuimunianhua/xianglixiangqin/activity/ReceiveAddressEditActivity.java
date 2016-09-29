package com.shuimunianhua.xianglixiangqin.activity;

import org.ksoap2.serialization.SoapObject;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import com.google.gson.Gson;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.model.AddressModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.StringUtil;

/**
 * @action 收货地址
 * @author SHI
 * @date  2016-2-17 10:09:43
 */
public class ReceiveAddressEditActivity extends MyBaseActivity implements OnClickListener, OnConnectServerStateListener<Integer>{
	/**返回控件**/
	@Bind(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**标题**/
	@Bind(R.id.tv_title)
	 TextView tv_title;
	/**姓名**/
	@Bind(R.id.et_receiverName)
	 EditText et_receiverName;
	/**手机号码**/
	@Bind(R.id.et_userPhone)
	 EditText et_userPhone;
	/**邮政编码**/
	@Bind(R.id.et_postCode)
	 EditText et_postCode;
	/**选择省、市、区控件**/
	@Bind(R.id.relativeLayout_select)
	 RelativeLayout relativeLayout_select;
	/**省、市、区按钮**/
	@Bind(R.id.tv_selectPosition)
	 TextView tv_selectPosition;
	/**详细收货地址**/
	@Bind(R.id.et_receiverAddress)
	 EditText et_receiverAddress;
	/**确认按钮**/
	@Bind(R.id.btn_confirm)
	 Button btn_confirm;
	/**当前待编辑的收货地址对象**/
	private AddressModel currentAddressModel;
	/**是否成功获取省、市、区**/
	private boolean selectSuccessFlag;
	@Override
	public void initView() {
		setContentView(R.layout.activity_shopping_address_edit);
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
			MyApplication.setmAddressModel(currentAddressModel);
		}
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		relativeLayout_select.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_titleLeft:
			 finish();
			break;
		case R.id.relativeLayout_select:
			 startActivityForResult(new Intent(mContext,AddressPositionSelectActivity.class),1);
			 selectSuccessFlag = false;
			 tv_selectPosition.setText(R.string.selectPosition);
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
	
	private void getData(String methodName) {
		if(methodName.equals(InformationCodeUtil.methodNameAddAddr)){
			Gson gson = new Gson();
			String addrJson = gson.toJson(getAddressModel());
			SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			soapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
			soapObject.addProperty("openKey",  MyApplication.getmCustomModel(mContext).getOpenKey());
			soapObject.addProperty("addrJson", addrJson);
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
					mContext, this, soapObject, methodName);
			connectCustomServiceAsyncTask.execute();
			return;
		}
		if(methodName.equals(InformationCodeUtil.methodNameEditAddr)){
			Gson gson = new Gson();
			String addrJson = gson.toJson(getAddressModel());
			SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			soapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
			soapObject.addProperty("openKey",  MyApplication.getmCustomModel(mContext).getOpenKey());
			soapObject.addProperty("addrJson", addrJson);
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
					mContext, this, soapObject, methodName);
			connectCustomServiceAsyncTask.execute();
			return;
		}
	}
	
	private boolean checkMsg(){
		if(et_receiverName.getText().toString().isEmpty()){
			ToastUtils.show(mContext, "请输入收件人名称");
			return false;
		}
		if(!StringUtil.matchRegex(et_userPhone.getText().toString(), 
				InformationCodeUtil.regExpPhotoNumber)){
			ToastUtils.show(mContext, "请输入合法的手机号码");
			return false;
		}
		if(et_postCode.getText().toString().isEmpty()){
			ToastUtils.show(mContext, "请输入邮政编码");
			return false;
		}
		if(et_receiverAddress.getText().toString().isEmpty()){
			ToastUtils.show(mContext, "请输入详细地址");
			return false;
		}
		if(selectSuccessFlag == false){
			Toast.makeText(this, "请选择所在城市", Toast.LENGTH_SHORT).show();
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


	public void onActivityResult(int requestCode,int resultCode,Intent data){
		switch (resultCode) {
		case RESULT_OK:
			//获取对应key的map值
			String text = data.getStringExtra(InformationCodeUtil.IntentAddressLocationSelect);
			if(!StringUtil.isEmpty(text)){
				tv_selectPosition.setText(text);
				selectSuccessFlag = true;
				currentAddressModel.setProvinceCode(MyApplication.getmAddressModel().getProvinceCode());
				currentAddressModel.setCityCode(MyApplication.getmAddressModel().getCityCode());
				currentAddressModel.setAreaCode(MyApplication.getmAddressModel().getAreaCode());
			}
			break;
		}
	}
	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
//		    LogUtil.LogShitou("操作地址成功", returnSoapObject.toString());
			if(returnString.contains("\"Sign\":1")){
				ToastUtils.show(mContext, "操作成功");
				finish();
			}
	}
	@Override
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {
			ToastUtils.show(mContext, "操作失败，请检查网络");
	}
	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		
	}
}
