package com.shuimunianhua.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.adapter.MyBaseAdapter;
import com.shuimunianhua.xianglixiangqin.model.BankBindingSelectModel;
import com.shuimunianhua.xianglixiangqin.model.ParamPayMoneyModel;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.StringUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import java.io.InputStream;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PayByNewBankCardActivity extends MyBaseActivity implements OnCheckedChangeListener{

	
	/**左侧返回按钮**/
	@Bind(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	
	/**页面标题**/
	@Bind(R.id.tv_title)
	 TextView tv_title;
	
	/**选择银行**/
	@Bind(R.id.tv_selectBank)
	 TextView tv_selectBank;
	/**当前选择的银行ID**/
	 String currentSelectBankID ;
	
	@Bind(R.id.cb_selectBank)
	 CheckBox cb_selectBank;
	
	/**选择省份**/
	@Bind(R.id.tv_selectProvince)
	 TextView tv_selectProvince;
	/**当前选择的省份ID**/
	 String currentSelectProvinceID;
	
	@Bind(R.id.cb_selectPosition)
	 CheckBox cb_selectPosition;
	
	/**用户银行卡号**/
	@Bind(R.id.et_bankCardNumber)
	 EditText et_bankCardNumber;
	/**用户银行卡号**/
	 String str_bankCardNumber;
	
	/**持卡人姓名**/
	@Bind(R.id.et_bankCardUserName)
	 EditText et_bankCardUserName;
	/**持卡人姓名**/
	 String str_bankCardUserName;
	
	/**持卡人身份证号**/
	@Bind(R.id.et_bankCardUserCardID)
	 EditText et_bankCardUserCardID;
	/**持卡人身份证号**/
	 String str_bankCardUserCardID;

	/**预留手机号**/
	@Bind(R.id.et_bankCardBindPhone)
	 EditText et_bankCardBindPhone;
	/**预留手机号**/
	 String str_bankCardBindPhone;
	
	/**下一步**/
	@Bind(R.id.btn_nextStep)
	 Button btn_nextStep;
	
	 List<BankBindingSelectModel> listData_selectBank;
	 List<BankBindingSelectModel> listData_selectPosition;

	/**当前订单信息**/
	String currentOrderIds;
	/**交易密码**/
	String strPayPassword;
	/**请求码**/
	private final int RequestCode_PayByNewBankCardActivity = 1;
	private boolean IfPayMoneySuccess = false;


	@Override
	public void initView() {
	       setContentView(R.layout.activity_bankcard_binding);
	       ButterKnife.bind(this);
	       	
	       tv_title.setText(R.string.title_binkBankCardBinding);
	       cb_selectBank.setOnCheckedChangeListener(this);
	       cb_selectPosition.setOnCheckedChangeListener(this);
			iv_titleLeft.setVisibility(View.VISIBLE);
	       iv_titleLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				previewToDestroy();
			}
		});
	       btn_nextStep.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					toPayMoney();
				}
	       });

	}

	@Override
	public void initData() {
		//获取当前要支付掉的订单号,使用 "," 分开
		currentOrderIds = getIntent().getStringExtra(InformationCodeUtil.IntentPayMoneyCurrentTotalOrderIds);
		strPayPassword = getIntent().getStringExtra(InformationCodeUtil.IntentPayPasswordSetActivityPayPassword);

		AssetManager am = getAssets();
		Gson gson = new Gson();
		try {
			InputStream is = am.open("json_bind_bank");
			String str_select = StringUtil.inputStreamToString(is);
			listData_selectBank = gson.fromJson(str_select, new TypeToken<List<BankBindingSelectModel>>(){}.getType());
			is = am.open("json_bind_position");
			str_select = StringUtil.inputStreamToString(is);
			listData_selectPosition = gson.fromJson(str_select, new TypeToken<List<BankBindingSelectModel>>(){}.getType());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	private void toPayMoney(){
		if(checkMsg()){
			Intent intent = new Intent(mContext,PayMoneyInputPasswordActivity.class);
			ParamPayMoneyModel model = new ParamPayMoneyModel(
					str_bankCardNumber, currentSelectBankID, currentSelectProvinceID
					, str_bankCardBindPhone, str_bankCardUserName, str_bankCardUserCardID
					,currentOrderIds,strPayPassword);
			intent.putExtra(InformationCodeUtil.IntentPayMoneyInputPasswordActivityParamModel,model);
			startActivityForResult(intent,RequestCode_PayByNewBankCardActivity);
		}

	}

	private boolean checkMsg(){
		str_bankCardNumber = et_bankCardNumber.getText().toString();
		str_bankCardUserName = et_bankCardUserName.getText().toString();
		str_bankCardUserCardID = et_bankCardUserCardID.getText().toString();
		str_bankCardBindPhone = et_bankCardBindPhone.getText().toString();


		if(StringUtil.isEmpty(currentSelectBankID)){
			ToastUtils.show(mContext, "请选择银行");
			return false;
		}
		if(StringUtil.isEmpty(currentSelectProvinceID)){
			ToastUtils.show(mContext, "请选择省份");
			return false;
		}
		if(StringUtil.isEmpty(str_bankCardNumber)){
			ToastUtils.show(mContext, "请填写银行卡号");
			return false;
		}
		if(StringUtil.isEmpty(str_bankCardUserName)){
			ToastUtils.show(mContext, "请填写持卡人名称");
			return false;
		}
		if(StringUtil.isEmpty(str_bankCardUserCardID)){
			ToastUtils.show(mContext, "请填写身份证号");
			return false;
		}
		if(StringUtil.isEmpty(str_bankCardBindPhone)){
			ToastUtils.show(mContext, "请输入预留手机号");
			return false;
		}

		return true;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if(isChecked){
			RotateAnimation ra01 = new RotateAnimation(0f, 180f,
			Animation.RELATIVE_TO_SELF, 0.5f,
			Animation.RELATIVE_TO_SELF, 0.5f);
			ra01.setDuration(300);
			ra01.setFillAfter(true);
			buttonView.startAnimation(ra01);
			showPopuWindow(buttonView);
			
		}else{
			RotateAnimation ra02 = new RotateAnimation(180f, 0f,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			ra02.setDuration(300);
			ra02.setFillAfter(true);
			buttonView.startAnimation(ra02);
		}		
	}

	
	 void showPopuWindow(final CompoundButton buttonView) {
		
		View popView = View.inflate(mContext, R.layout.item_pop_bind_bankcard_info_select, null);

		final PopupWindow pop = new PopupWindow(popView, tv_selectBank.getWidth()
				+ buttonView.getWidth()*4/3, LayoutParams.WRAP_CONTENT, true);

		ListView listView = (ListView) popView.findViewById(R.id.listView);
		if (buttonView == cb_selectBank) {
			MyAdapter myadapter = new MyAdapter(mContext, listData_selectBank);
			listView.setAdapter(myadapter);
		}else if(buttonView == cb_selectPosition) {
			MyAdapter myadapter = new MyAdapter(mContext, listData_selectPosition);
			listView.setAdapter(myadapter);
		}
		listView.setOnItemClickListener(
				new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						if (buttonView == cb_selectBank) {
							tv_selectBank.setText(listData_selectBank.get(position).getName());
							currentSelectBankID = listData_selectBank.get(position).getID();
						}else if(buttonView == cb_selectPosition) {
							tv_selectProvince.setText(listData_selectPosition.get(position).getName());
							currentSelectProvinceID = listData_selectPosition.get(position).getID();
						}
						pop.dismiss();
					}
			
		});
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setOutsideTouchable(true);
		pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
			
			@Override
			public void onDismiss() {
				buttonView.setChecked(false);
			}
		});		
		pop.showAsDropDown(buttonView);
	}
	
	 class MyAdapter extends MyBaseAdapter<BankBindingSelectModel>{


		public MyAdapter(Context mContext, List<BankBindingSelectModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = null;
			if(convertView == null){
				
				convertView = View.inflate(mContext, R.layout.item_adapter_bankcardbind_listview, null);
				tv = (TextView) convertView.findViewById(R.id.textView);
				convertView.setTag(tv);
				
			}else{
				tv = (TextView) convertView.getTag();
			}
			tv.setText(listData.get(position).getName());
			
			return convertView;
		}
		
	}
	

	@Override
	public void onBackPressed() {
		previewToDestroy();
	}

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
	

}
