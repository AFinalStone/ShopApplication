package com.shuimunianhua.xianglixiangqin.activity;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.adapter.MyBaseAdapter;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultMsgModel;
import com.shuimunianhua.xianglixiangqin.model.BankCardModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;
import com.shuimunianhua.xianglixiangqin.view.FragmentCommonDialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 我的银行卡界面
 * @author SHI
 * 2016年4月11日 11:51:36
 */
public class MyBankCardActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer> {

	
	/**左侧返回按钮**/
	@Bind(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	
	/**页面标题**/
	@Bind(R.id.tv_title)
	 TextView tv_title;
	
	/**尚未绑定银行卡**/
	@Bind(R.id.linearLayout_nothingBankcard)
	 LinearLayout linearLayout_nothingBankcard;
	
	/**银行卡**/
	@Bind(R.id.listView)
	 ListView listView;
	private MyAdapter myAdapter;
	private List<BankCardModel> listData;
	
	
	@Override
	public void initView() {
	       setContentView(R.layout.activity_my_bankcard);
	       ButterKnife.bind(this);
	       	
	       tv_title.setText(R.string.myBankCardTitle);
			iv_titleLeft.setVisibility(View.VISIBLE);
	       iv_titleLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

	       listData = new ArrayList<BankCardModel>();
	       myAdapter = new MyAdapter(mContext, listData);
	       listView.setAdapter(myAdapter);
			linearLayout_nothingBankcard.setVisibility(View.VISIBLE);
	}

	@Override
	public void initData() {
	}
	
	@Override
	protected void onResume() {
//		getData(InformationCodeUtil.methodNameCheckESign);
		super.onResume();
	}

	private void getData(String methodName){
		
		if(InformationCodeUtil.methodNameCheckESign.equals(methodName)){
			listData.clear();
			myAdapter.notifyDataSetChanged();
			SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
			requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
					mContext, this, requestSoapObject, methodName);
			connectCustomServiceAsyncTask.execute();
		}

	}
	
	/**
	 * 解除签约
	 * @param signId  签约ID
	 * @param accountCode  银行卡号
	 */
	private void cancelESign(String signId, String accountCode, int position ){
		
			SoapObject requestSoapObject = new SoapObject(
					ConnectServiceUtil.NAMESPACE, InformationCodeUtil.methodNameCancelESign);
			requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
			requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
			requestSoapObject.addProperty("signId", signId);
			requestSoapObject.addProperty("accountCode", accountCode);
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
			mContext, this, requestSoapObject, InformationCodeUtil.methodNameCancelESign,position);
			connectCustomServiceAsyncTask.execute();
	}
	
	private class MyAdapter extends MyBaseAdapter<BankCardModel>{


		public MyAdapter(Context mContext, List<BankCardModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
				
			convertView = View.inflate(mContext, R.layout.item_adapter_mybankcard_listview, null);
			
			TextView tv_bankName = (TextView) convertView.findViewById(R.id.tv_bankName);
			TextView tv_bankCardCode = (TextView) convertView.findViewById(R.id.tv_bankCardCode);
			Button btn_cancelBinding = (Button) convertView.findViewById(R.id.btn_cancelBinding);
			
			final BankCardModel mBankCardmodel =  listData.get(position);
			
			tv_bankName.setText(mBankCardmodel.getBankNam());
			tv_bankCardCode.setText(mBankCardmodel.getAccountCode());
			btn_cancelBinding.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showLogoutDialog(mBankCardmodel,position);
				}
			});
			return convertView;
		}
		
	}
	
	
	private void showLogoutDialog(final BankCardModel mBankCardmodel,final int position) {
		
		final FragmentCommonDialog fragmentDailog = new FragmentCommonDialog();
		fragmentDailog.initView("提示","确认解绑?","取消","解绑", new FragmentCommonDialog.OnButtonClickListener() {
			
			@Override
			public void OnOkClick() {
				cancelESign(mBankCardmodel.getSignID(), mBankCardmodel.getAccountCode(), position);
				fragmentDailog.dismiss();
			}
			
			@Override
			public void OnCancelClick() {
				fragmentDailog.dismiss();
			}
		});
		fragmentDailog.show(getSupportFragmentManager(), "fragmentDailog");
	}

	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
//		String result = returnSoapObject.getPropertyAsString(methodName+"Result");
//		LogUtil.LogShitou("返回信息", result);
		Gson gson = new Gson();
		if(InformationCodeUtil.methodNameCancelESign.equals(methodName)){
			JSONResultMsgModel json = gson.fromJson(returnString, JSONResultMsgModel.class);
			if(json.getSign() == 1){
				listData.remove(state.intValue());
				myAdapter.notifyDataSetChanged();
				if(listData.size() == 0)
				linearLayout_nothingBankcard.setVisibility(View.VISIBLE);
			}
			ToastUtils.show(mContext, json.getMsg());
			return;
		}
		
		if(InformationCodeUtil.methodNameCheckESign.equals(methodName)){
			try {
				JSONResultMsgModel json = gson.fromJson(returnString, JSONResultMsgModel.class);
				if(json.getSign() == 1){
					BankCardModel bank = gson.fromJson(json.getMsg(), BankCardModel.class);
					listData.add(bank);
					myAdapter.notifyDataSetChanged();
					linearLayout_nothingBankcard.setVisibility(View.GONE);
				}else{
//					ToastUtils.show(mContext, json.getMsg());
					linearLayout_nothingBankcard.setVisibility(View.VISIBLE);
				}
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
				linearLayout_nothingBankcard.setVisibility(View.VISIBLE);
			}
		}
		
	}

	@Override
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {
		ToastUtils.show(mContext, "网络异常，数据加载失败");
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		
	}
	
	
	

}
