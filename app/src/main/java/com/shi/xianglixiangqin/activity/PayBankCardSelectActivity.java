package com.shi.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.model.BankBindingSelectModel;
import com.shi.xianglixiangqin.model.ParamPayMoneyModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/****
 * 银行卡支付界面
 * @author SHI
 * 2016年4月10日 16:29:33
 */
public class PayBankCardSelectActivity extends MyBaseTranslucentActivity implements OnConnectServerStateListener<Integer>{

	/**总金额**/
	@BindView(R.id.iv_closeActivity)
	ImageView iv_closeActivity;

	/**总金额**/
	@BindView(R.id.tv_totalPrices)
	TextView tv_totalPrices;

	/**关闭当前界面**/
	@BindView(R.id.listView)
	ListView listView;
	ListAdapter listAdapter;
	List<YiZhiFuBankCardModel> listData = new ArrayList<YiZhiFuBankCardModel>();
	List<BankBindingSelectModel> listData_selectBank;
	/**当前订单信息**/
	 String currentOrderIds;
	/**当前支付总金额**/
	 String currentTotalMoney;
	/**是否付款成功**/
	 boolean IfPayMoneySuccess = false;
	/**请求码**/
	private final int RequestCode_PayBankCardSelectActivity = 1;

	private View viewListFoot;
	/**请求数据是否成功**/
	private boolean connectSuccess = false;
	private boolean IfFirstPay = true;

	@Override
	public void initView() {
		setContentView(R.layout.activity_pay_bank_card_select);
		ButterKnife.bind(this);
		iv_closeActivity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				previewToDestroy();
			}
		});
		viewListFoot = View.inflate(mContext,R.layout.item_adapter_pay_bank_card_select_foot,null);
		listView.addFooterView(viewListFoot);
	}

	@Override
	public void initData() {
		Intent intent = getIntent();
		//获取当前需要付款的金额
		Double currentTotalMoney = intent.getDoubleExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalMoney, 0.00);
		this.currentTotalMoney = StringUtil.doubleToString(currentTotalMoney,"0.00")+"元";
		tv_totalPrices.setText(this.currentTotalMoney);

		//获取当前要支付掉的订单号,使用 "," 分开
		currentOrderIds = intent.getStringExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds);

		//获取全国各地银行名称和编号
		try {
			AssetManager am = getAssets();
			Gson gson = new Gson();
			InputStream is = am.open("json_bind_bank");
			String str_select = StringUtil.inputStreamToString(is);
			listData_selectBank = gson.fromJson(str_select, new TypeToken<List<BankBindingSelectModel>>(){}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}

		listAdapter = new ListAdapter(mContext,listData);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position < listData.size()){
					for (int i=0; i<listData.size(); i++){
						listData.get(i).IfSelect = false;
					}
					YiZhiFuBankCardModel bankCardModel = listData.get(position);
					bankCardModel.setIfSelect(true);
					listAdapter.notifyDataSetChanged();
					toPayInputPassword(bankCardModel);
				}else{
					toPayByNewBankCard();
				}
			}
		});

		getTotalMoneyOfToPay(currentOrderIds);
	}

	private void toPayInputPassword(YiZhiFuBankCardModel bankCardModel){
		//付款界面付款成功会返回付款成功信息
		Intent intent = new Intent(mContext,PayMoneyInputPasswordActivity.class);
		ParamPayMoneyModel model = new ParamPayMoneyModel(
				bankCardModel.getBankCardCode(), bankCardModel.getBankCode(), bankCardModel.getAreaCode()
				, bankCardModel.getMobile(), bankCardModel.getBankCardName(), bankCardModel.getCertNo()
				,currentOrderIds,"");
		intent.putExtra(InformationCodeUtil.IntentPayMoneyInputPasswordActivityParamModel,model);
		startActivityForResult(intent,RequestCode_PayBankCardSelectActivity);
	}

	private void toPayByNewBankCard(){

		if(connectSuccess){
			if(IfFirstPay){
				Intent intent = new Intent(mContext,PayPasswordSetActivity.class);
				intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds,currentOrderIds);
				startActivityForResult(intent,RequestCode_PayBankCardSelectActivity);
			}else{
				Intent intent = new Intent(mContext,PayByNewBankCardActivity.class);
				intent.putExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds,currentOrderIds);
				startActivityForResult(intent,RequestCode_PayBankCardSelectActivity);
			}
		}

	}


	private void getTotalMoneyOfToPay(String totalOrderId){
		String methodName = InformationCodeUtil.methodNameGetEPayInfoByUserID;
		SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE,
				methodName);
		requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
		requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
		requestSoapObject.addProperty("orderIds", totalOrderId);
		ConnectCustomServiceAsyncTask ConnectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
				mContext, this, requestSoapObject, methodName);
		ConnectCustomServiceAsyncTask.initProgressDialog(true,"请稍等...");
		ConnectCustomServiceAsyncTask.execute();
	}


	/**启用动画结束当前界面**/
	void previewToDestroy(){
		if(IfPayMoneySuccess){
			setResult(RESULT_OK);
			IfOpenFinishActivityAnim(false);
		}
		finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
			case RESULT_OK:
				//用户付款成功
				IfPayMoneySuccess = true;
				previewToDestroy();
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		previewToDestroy();
	}

//	{"DjLsh":-1,"Msg":"[{\"AreaCode\":\"330000\",\"BankCardCode\":\"6212261202029256667\",\"BankCardName\":\"石要磊\",\"BankCode\":\"866200\",\"CertNo\":\"410421199004034576\",\"ID\":3,\"Mobile\":\"18211673059\",\"UserID\":841}]","Sign":1,"Tags":"4270.000","orderId":0}
	@Override
	public void connectServiceSuccessful(String returnString, String methodName, Integer state, boolean whetherRefresh) {
		LogUtil.LogShitou(returnString);
		try {
			Gson gson = new Gson();
			JSONObject jsonObject = new JSONObject(returnString);
			currentTotalMoney = jsonObject.getString("Tags");
			tv_totalPrices.setText(currentTotalMoney+"元");
			String recordOfBankCard = jsonObject.getString("Msg");
			List<YiZhiFuBankCardModel> listResult = gson.fromJson(recordOfBankCard, new TypeToken<List<YiZhiFuBankCardModel>>(){}.getType());
			listData.addAll(listResult);
			listAdapter.notifyDataSetChanged();
			if(listData.size() > 0){
				IfFirstPay = false;
			}else{
				IfFirstPay = true;
			}
			connectSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void connectServiceFailed(String returnStrError, String methodName, Integer state, boolean whetherRefresh) {
		ToastUtil.show(mContext,"网络异常，数据加载失败");
	}

	@Override
	public void connectServiceCancelled(String returnString, String methodName, Integer state, boolean whetherRefresh) {

	}

	private class ListAdapter extends MyBaseAdapter<YiZhiFuBankCardModel> {

		public ListAdapter(Context mContext, List<YiZhiFuBankCardModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder;
			if(convertView == null){
				viewHolder = new ViewHolder();
				convertView= View.inflate(mContext,R.layout.item_adapter_pay_bank_card_select,null);
				viewHolder.tv_bankCardName = (TextView) convertView.findViewById(R.id.tv_bankCardName);
				viewHolder.tv_bankCardDesc = (TextView) convertView.findViewById(R.id.tv_bankCardDesc);
				viewHolder.cb_ifSelect = (CheckBox) convertView.findViewById(R.id.cb_ifSelect);
				convertView.setTag(viewHolder);
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			YiZhiFuBankCardModel bankCardModel  = listData.get(position);

			//获取银行卡名称
			BankBindingSelectModel beanTemp = new BankBindingSelectModel();
			beanTemp.setID(bankCardModel.getBankCode());
			int selectPosition = listData_selectBank.indexOf(beanTemp);
			String bankName = listData_selectBank.get(selectPosition).getName();
			//获取尾号
			int length = bankCardModel.getBankCardCode().length();
			String bankCardLastNumber = bankCardModel.getBankCardCode().substring(length-4, length);

			viewHolder.tv_bankCardName.setText(bankName+" 尾号"+bankCardLastNumber);
			viewHolder.tv_bankCardDesc.setText(R.string.tv_PayBankCardSelectBankCardDesc);
			viewHolder.cb_ifSelect.setChecked(bankCardModel.getIfSelect());
			return convertView;
		}

		private class ViewHolder{
			TextView tv_bankCardName;
			TextView tv_bankCardDesc;
			CheckBox cb_ifSelect;
		}
	}

	/**翼支付交易银行卡记录**/
	private class YiZhiFuBankCardModel{
		private int ID ;
		//用户id
		private int UserID;
		//银行账号
		private String BankCardCode;
		//银行账户名
		private String BankCardName;
		//银行编码
		private String BankCode;
		//银行区域编码
		private String AreaCode;
		//预留手机号
		private String Mobile;
		//身份证号码
		private String CertNo;

		private boolean IfSelect = false;

		public int getID() {
			return ID;
		}

		public void setID(int ID) {
			this.ID = ID;
		}

		public int getUserID() {
			return UserID;
		}

		public void setUserID(int userID) {
			UserID = userID;
		}

		public String getBankCardCode() {
			return BankCardCode;
		}

		public void setBankCardCode(String bankCardCode) {
			BankCardCode = bankCardCode;
		}

		public String getBankCardName() {
			return BankCardName;
		}

		public void setBankCardName(String bankCardName) {
			BankCardName = bankCardName;
		}

		public String getBankCode() {
			return BankCode;
		}

		public void setBankCode(String bankCode) {
			BankCode = bankCode;
		}

		public String getAreaCode() {
			return AreaCode;
		}

		public void setAreaCode(String areaCode) {
			AreaCode = areaCode;
		}

		public String getMobile() {
			return Mobile;
		}

		public void setMobile(String mobile) {
			Mobile = mobile;
		}

		public String getCertNo() {
			return CertNo;
		}

		public void setCertNo(String certNo) {
			CertNo = certNo;
		}

		public boolean getIfSelect() {
			return IfSelect;
		}

		public void setIfSelect(boolean ifSelect) {
			IfSelect = ifSelect;
		}
	}
}

