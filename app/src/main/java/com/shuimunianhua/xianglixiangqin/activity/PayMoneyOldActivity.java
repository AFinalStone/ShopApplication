package com.shuimunianhua.xianglixiangqin.activity;

import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultMsgModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.StringUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/****
 * 银行卡支付界面
 * @author SHI
 * 2016年4月10日 16:29:33
 */
public class PayMoneyOldActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer>{

	
	/**整体外围控件**/
	@Bind(R.id.relativeLayout_background)
	RelativeLayout relativeLayout_background;

	/**关闭当前Activity**/
	@Bind(R.id.iv_closeActivity)
	 ImageView iv_closeActivity;
	
	/**用户没有绑定银行卡**/
	@Bind(R.id.linearLayout_bindingBankCard)
	 LinearLayout linearLayout_bindingBankCard;
	/**去绑定银行卡按钮**/
	@Bind(R.id.btn_bindingBankCard)
	 Button btn_bindingBankCard;
	
	/**用户付款界面**/
	@Bind(R.id.linearLayout_payMoney)
	 LinearLayout linearLayout_payMoney;
	/**付款金额**/
	@Bind(R.id.tv_payMoneyNumber)
	 TextView tv_payMoneyNumber;
	/**付款银行卡**/
	@Bind(R.id.tv_payBankCardDesc)
	 TextView tv_payBankCardDesc;
	
	/**交易密码**/
	@Bind(R.id.et_payPassword)
	 EditText et_payPassword;

	/**付款按钮**/
	@Bind(R.id.btn_payMoney)
	 Button btn_payMoney;
	
	/**付款成功界面**/
	@Bind(R.id.linearLayout_paySuccess)
	 LinearLayout linearLayout_paySuccess;
	
	/**当前签约默认银行卡ID**/
	 String currengSignID;
	/**当前签约银行卡编号**/
	 String currentAccountCode;
	/**当前订单信息**/
	 String currentOrderIds;
	/**当前支付总金额**/
	 String currentTotalMoney;
	/**是否付款成功**/
	 boolean IfPayMoneySuccess = false;

	@Override
	public void initView() {
		setContentView(R.layout.activity_pay_money);
		ButterKnife.bind(this);
	}

	@OnClick({R.id.iv_closeActivity, R.id.btn_bindingBankCard, R.id.btn_payMoney})
	public void onClick(View view){
		switch (view.getId()){
			case R.id.iv_closeActivity:
				previewToFinish();
				break;
			case R.id.btn_bindingBankCard:
				ToastUtils.show(mContext,"功能暂未开通,支付请联系店主");
//				startActivity(new Intent(mContext,PayByNewBankCardActivity.class));
				break;
			case R.id.btn_payMoney:
//				getData(InformationCodeUtil.methodNameSignAgentReceiveToAccount);
				break;
		}
	}

	@Override
	public void initData() {
		Intent intent = getIntent();
		//获取当前需要付款的金额
		Double currentTotalMoney = intent.getDoubleExtra(InformationCodeUtil.IntentPayMoneyByBankCardActivityCurrentTotalMoney, 0.00);
		//获取当前要支付掉的订单号,使用 "," 分开
		currentOrderIds = intent.getStringExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds);
		
		if(currentOrderIds == null){
			previewToFinish();
			return;
		}

		tv_payMoneyNumber.setText(StringUtil.doubleToString(currentTotalMoney, "0.00")+"元");
		int total = (int) (currentTotalMoney*100);
		this.currentTotalMoney = ""+total;
		getData(InformationCodeUtil.methodNameCheckESign);
	}

	@Override
	protected void onResume() {
		super.onResume();
		getData(InformationCodeUtil.methodNameCheckESign);
	}

	void getData(String methodName){
		
		if(InformationCodeUtil.methodNameCheckESign.equals(methodName)){
		
			SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
			requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
					mContext, this, requestSoapObject, methodName);
			connectCustomServiceAsyncTask.execute();
			return;
		}

		if(InformationCodeUtil.methodNameSignAgentReceiveToAccount.equals(methodName)){
			
			String strPayPassword = et_payPassword.getText().toString().trim();
			if(StringUtil.isEmpty(strPayPassword)){
				ToastUtils.show(mContext, "请输入交易密码");
				return;
			}
			SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
			requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
			//签约ID
			requestSoapObject.addProperty("signId", currengSignID);
			//签约银行卡号
			requestSoapObject.addProperty("accountCode", currentAccountCode);
			//订单信息
			requestSoapObject.addProperty("orderIds", currentOrderIds);
			//代扣数量
			requestSoapObject.addProperty("amount", currentTotalMoney);
			requestSoapObject.addProperty("pinCode", strPayPassword);
			LogUtil.LogShitou("返回结果", requestSoapObject.toString());
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
					mContext, this, requestSoapObject, methodName);
			connectCustomServiceAsyncTask.execute();			
		}
		
	}
	

	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
//		Object  provinceSoapObject = (Object ) returnSoapObject.getProperty(methodName+"Result");
//		LogUtil.LogShitou(provinceSoapObject.toString());
		Gson gson = new Gson();
//	{"DjLsh":-1,"Msg":"{\"AccountCode\":\"6222023602102495280866200\",\"BankName\":\"中国工商银行\",\"ID\":14,\"SignID\":\"20160411163941000000000000012862\"}","Sign":1}
		if(InformationCodeUtil.methodNameCheckESign == methodName){
			relativeLayout_background.setVisibility(View.VISIBLE);
			try {
				JSONResultMsgModel mJSONResultMsgModel = gson.fromJson(returnString, JSONResultMsgModel.class);
				if(mJSONResultMsgModel.getSign() == 1){
					JSONObject jsonObject = new JSONObject(mJSONResultMsgModel.getMsg());
					currengSignID = jsonObject.getString("SignID");
					currentAccountCode = jsonObject.getString("AccountCode");
					String bankName = jsonObject.getString("BankName");
					String bankLastNum = currentAccountCode.substring(currentAccountCode.length()-4, currentAccountCode.length());
					tv_payBankCardDesc.setText(bankName+"(尾号"+bankLastNum+")");
					linearLayout_payMoney.setVisibility(View.VISIBLE);
				}else{
					linearLayout_bindingBankCard.setVisibility(View.VISIBLE);
				}	
			} catch (Exception e) {
				e.printStackTrace();
				linearLayout_bindingBankCard.setVisibility(View.VISIBLE);
				previewToFinish();
			}
			return;
		}
//		{"DjLsh":-1,"Msg":"成功","Sign":1}
		
		if(InformationCodeUtil.methodNameSignAgentReceiveToAccount.equals(methodName)){
			JSONResultMsgModel mJSONResultMsgModel = gson.fromJson(returnString, JSONResultMsgModel.class);
			if(mJSONResultMsgModel.getSign() == 1){
    			linearLayout_paySuccess.setVisibility(View.VISIBLE);				
    			linearLayout_payMoney.setVisibility(View.GONE);
				IfPayMoneySuccess = true;
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						SystemClock.sleep(3000);
						previewToFinish();
					}
				}).start();
			}else{
				ToastUtils.show(mContext, mJSONResultMsgModel.getMsg());
			}
		}
		
	}
	

	@Override
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {
		if(InformationCodeUtil.methodNameCheckESign == methodName){
			ToastUtils.show(mContext, "网络异常，获取用户银行卡信息失败");
			previewToFinish();
		}
		if(InformationCodeUtil.methodNameSignAgentReceiveToAccount.equals(methodName)){
			ToastUtils.show(mContext, "网络异常，付款失败,请重新提交");
		}
		if(InformationCodeUtil.methodNameGetSMSCode == methodName){
			ToastUtils.show(mContext, "网络异常，获取验证码失败");
		}

	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
	}

	/**启用动画结束当前界面**/
	void previewToFinish(){
		if(IfPayMoneySuccess){
			setResult(RESULT_OK);
		}else{
			setResult(RESULT_CANCELED);
		}
		finish();
		overridePendingTransition(R.anim.not_change,R.anim.out_to_bottom);
	}

	@Override
	public void onBackPressed() {
		previewToFinish();
	}


}

