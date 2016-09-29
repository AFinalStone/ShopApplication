package com.shuimunianhua.xianglixiangqin.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultBaseModel;
import com.shuimunianhua.xianglixiangqin.json.JSONResultMsgModel;
import com.shuimunianhua.xianglixiangqin.model.AddressModel;
import com.shuimunianhua.xianglixiangqin.model.ParamPayMethodSelectModel;
import com.shuimunianhua.xianglixiangqin.model.SelectPackageModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.ImagerLoaderUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.StringUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;
import com.shuimunianhua.xianglixiangqin.view.FragmentCommonDialog;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * @action 确认订单界面
 * @author ZHU
 * @date 2015-8-9 上午9:42:11
 */
public class ConfirmOrderSportActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer> {
	/** 返回控件 */
	@Bind(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/** 页面标题 */
	@Bind(R.id.tv_title)
	 TextView tv_title;
	/** 确认订单按钮 **/
	@Bind(R.id.btn_submit)
	Button btn_submit;
	/** 价格合计 */
	@Bind(R.id.tv_totalPrices)
	TextView tv_totalPrices;

	/** 收件人 **/
	@Bind(R.id.tv_customerName)
	 TextView tv_customerName;
	/** 联系方式 **/
	@Bind(R.id.tv_customerPhone)
	TextView tv_customerPhone;
	/** 收货地址 **/
	@Bind(R.id.tv_customerReceiverAddress)
	TextView tv_customerReceiverAddress;
	/** 进入编辑收货地址控件 */
	@Bind(R.id.iv_managerReceiverAddress)
	ImageView iv_managerReceiverAddress;


	/**商品图片**/
	@Bind(R.id.iv_goodsImages)
	ImageView iv_goodsImages;
	/**商品名称**/
	@Bind(R.id.tv_goodsName)
	TextView tv_goodsName;
	/**商品套餐和颜色**/
	@Bind(R.id.tv_goodsPackageAndColor)
	TextView tv_goodsPackageAndColor;
	/**商品数量**/
	@Bind(R.id.tv_goodsNum)
	TextView tv_goodsNum;
	/**商品价格**/
	@Bind(R.id.tv_goodsPrice)
	TextView tv_goodsPrice;


	/**订单备注**/
	@Bind(R.id.et_orderRemark)
	EditText et_orderRemark;
	/**是否开发票**/
	@Bind(R.id.cb_confirmOrderIfOpenBilling)
	CheckBox cb_confirmOrderIfOpenBilling;
	/**发票号码**/
	@Bind(R.id.et_InvoiceNumberCode)
	EditText et_InvoiceNumberCode;
	/**飞币数量**/
	@Bind(R.id.tv_privilegeFlyCoin)
	TextView tv_privilegeFlyCoin;

	private SelectPackageModel currentSelectPackageMode;
	/** 当前商品总价格 **/
	private Double totalOrdersPrices = 0.00;
	/**当前服务器飞币数量**/
	private int totalFlyCoin_web = 0;
	/**
	 * 获取收货地址界面
	 **/
	private final int RequestCode_PayMethodSelectActivity = 2;
	/**
	 * 是否进行付款操作界面
	 **/
	private final int RequestCode_ShoppingAddressManageActivity = 1;

	public void initView() {
		// 初始化主页布局
		setContentView( R.layout.activity_confirm_order_activity);
		ButterKnife.bind(this);
		iv_titleLeft.setVisibility(View.VISIBLE);
		tv_title.setText(R.string.title_confirmOrder);
		et_InvoiceNumberCode.setEnabled(false);
		cb_confirmOrderIfOpenBilling.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				et_InvoiceNumberCode.setEnabled(isChecked);
				getTotalPrice();
			}
		});
	}


	@OnClick({R.id.iv_titleLeft,R.id.btn_submit})
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.iv_titleLeft:
				previewToDestroy();
				break;
			case R.id.iv_managerReceiverAddress:
				Intent mIntent = new Intent(mContext,
						ReceiveAddressManageActivity.class);
				startActivityForResult(mIntent, RequestCode_ShoppingAddressManageActivity);
				break;
			case R.id.btn_submit:
				toPayMethodSelectActivity();
			default:
				break;
		}
	}

	public void initData() {

		currentSelectPackageMode = (SelectPackageModel) getIntent().getSerializableExtra(InformationCodeUtil.IntentConfirmOrderSportActivityPackageModel);
		if(currentSelectPackageMode == null){
			previewToDestroy();
			return;
		}

		ImagerLoaderUtil.getInstance(mContext).displayMyImage(
				currentSelectPackageMode.getmProductModel().getImages()
						.get(0), iv_goodsImages);
		tv_goodsName.setText(""
				+ currentSelectPackageMode.getmProductModel()
				.getGoodsName());
		// 设置商品套餐颜色和价格
		if (currentSelectPackageMode.getmPackageColorModel() != null) {
			tv_goodsPackageAndColor.setText(currentSelectPackageMode
					.getmPackages().getPackageName()
					+ "/"
					+ currentSelectPackageMode.getmPackageColorModel()
					.getColorName());
			tv_goodsPrice.setText("￥"+StringUtil.doubleToString(currentSelectPackageMode
					.getmPackageColorModel().getPrice (),"0.00"));
		}
		tv_goodsNum.setText("×"	+ currentSelectPackageMode.getPurchaseQuantity());
		tv_privilegeFlyCoin.setText(""+currentSelectPackageMode.getmPackageColorModel().getFlyCoin());

		getData(InformationCodeUtil.methodNameGetAcceptFlyCoin, true);
		getData(InformationCodeUtil.methodNameGetAddrList, true);
	}

	private void toPayMethodSelectActivity() {

		String strCustomerName = tv_customerName.getText().toString();
		String strCustomerReceiverAddress = tv_customerReceiverAddress.getText().toString();
		String strCustomerPhone = tv_customerPhone.getText().toString();

		if (StringUtil.isEmpty(strCustomerName)) {
			ToastUtils.show(this, "请输入收货人姓名");
			return;
		}
		if (StringUtil.isEmpty(strCustomerReceiverAddress)) {
			ToastUtils.show(this, "收货地址不能为空");
			return;
		}
		if (StringUtil.isEmpty(strCustomerPhone)) {
			ToastUtils.show(this, "联系方式不能为空");
			return;
		}
		String sportPlatformActionID = ""+currentSelectPackageMode.getmProductModel().getPlatformActionID();
		int sportPlatformActionType = currentSelectPackageMode.getmProductModel().getPlatformActionType();
		String sportRemark = et_orderRemark.getText().toString();
		if(StringUtil.isEmpty(sportRemark)){
			sportRemark = "";
		}
		ParamPayMethodSelectModel paramSport = new ParamPayMethodSelectModel(1,totalOrdersPrices,
				getOrderInfoJsonString(),getItemJsonString(),sportPlatformActionID
				,sportPlatformActionType,sportRemark,ParamPayMethodSelectModel.TypeConfirmOrderSport);

		Intent mIntent = new Intent(mContext, PayMethodSelectActivityEx.class);
		mIntent.putExtra(InformationCodeUtil.IntentPayMethodSelectActivityParamObject, paramSport);
		startActivityForResult(mIntent, RequestCode_PayMethodSelectActivity);
		overridePendingTransition(R.anim.in_from_bottom,R.anim.not_change);
	}


	public void getData(String methodName, boolean whetherRefresh) {

		// 获取 我的收货地址
		if (methodName == InformationCodeUtil.methodNameGetAddrList) {
			SoapObject requestSoapObject = new SoapObject(
					ConnectServiceUtil.NAMESPACE, methodName);
			requestSoapObject.addProperty("customID", MyApplication
					.getmCustomModel(mContext).getDjLsh());
			requestSoapObject.addProperty("openKey", MyApplication
					.getmCustomModel(mContext).getOpenKey());
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
					mContext, this, requestSoapObject, methodName,
					whetherRefresh);
			connectCustomServiceAsyncTask.execute();
			return;
		}
		//获取我的飞币数量
		if (methodName == InformationCodeUtil.methodNameGetAcceptFlyCoin) {
			SoapObject requestSoapObject = new SoapObject(
					ConnectServiceUtil.NAMESPACE, methodName);
			requestSoapObject.addProperty("customID", MyApplication
					.getmCustomModel(mContext).getDjLsh());
			ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
					mContext, this, requestSoapObject, methodName);
			connectGoodsServiceAsyncTask.initProgressDialog(false, "请稍等...");
			connectGoodsServiceAsyncTask.execute();
			return;
		}

	}


	 void initViewHeaderData(AddressModel mAddressModel) {
		tv_customerName.setText("" + mAddressModel.getRealName());
		tv_customerPhone.setText("" + mAddressModel.getPhoneNum());
		tv_customerReceiverAddress.setText("" + mAddressModel.getProvinceName()
				+ mAddressModel.getCityName() + mAddressModel.getAreaName()
				+ "\n" + mAddressModel.getAddress());
	}

	 String getOrderInfoJsonString() {
		Gson gson = new Gson();
		OrderInfoModel mOrderInfoModel = new OrderInfoModel();
		mOrderInfoModel.setDjLsh(0);
		mOrderInfoModel.setUserID(MyApplication.getmCustomModel(mContext).getDjLsh());
		String strTotalPriceTextView = tv_totalPrices.getText()
				.toString();
		LogUtil.LogShitou(strTotalPriceTextView);
		mOrderInfoModel.setTotalMoney(totalOrdersPrices);
		mOrderInfoModel.setAddress(tv_customerReceiverAddress.getText().toString());
		mOrderInfoModel.setRealName(tv_customerName.getText().toString());
		mOrderInfoModel.setPhoneNum(tv_customerPhone.getText().toString());
		mOrderInfoModel.setIfBilling(cb_confirmOrderIfOpenBilling.isChecked());
		mOrderInfoModel.setBillingName(et_InvoiceNumberCode.getText()
				.toString());
		mOrderInfoModel.setPostMethondID(2);
		mOrderInfoModel.setPostMethondName("普通快递");
//		mOrderInfoModel.setFlyCoin(currentTotalFlyCoin);
		mOrderInfoModel.setOrderSign(1);
		return gson.toJson(mOrderInfoModel);
	}

	 String getItemJsonString() {
		Gson gson = new Gson();
		List<OrderItemModel> list = new ArrayList<OrderItemModel>();
		OrderItemModel mOrderItemModel = new OrderItemModel();
		mOrderItemModel.setDjLsh(-1);
		mOrderItemModel.setOrderID(0);
		mOrderItemModel.setGoodsID(currentSelectPackageMode.getmProductModel()
				.getDjLsh());
		mOrderItemModel.setGoodsName(currentSelectPackageMode.getmProductModel()
				.getGoodsName());
		mOrderItemModel.setPackageID(currentSelectPackageMode.getmPackages()
				.getGoodsID());
		mOrderItemModel.setPackageName(currentSelectPackageMode.getmPackages()
				.getPackageName());
		mOrderItemModel.setColorID(currentSelectPackageMode.getmPackageColorModel()
				.getDjLsh());
		mOrderItemModel.setColorName(currentSelectPackageMode
				.getmPackageColorModel().getColorName());
		mOrderItemModel.setUnitPrice(currentSelectPackageMode
				.getmPackageColorModel().getPrice());
		mOrderItemModel.setUnitTaxPrice(currentSelectPackageMode
				.getmPackageColorModel().getTaxPrice());
		mOrderItemModel.setBuyCount(currentSelectPackageMode.getPurchaseQuantity());
		mOrderItemModel.setImgUrl(currentSelectPackageMode.getmProductModel()
				.getImgUrl());
		list.add(mOrderItemModel);

		return gson.toJson(list);
	}


	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		Gson gson = new Gson();
		if (methodName == InformationCodeUtil.methodNameGetAddrList) {
			List<AddressModel> returnList = null;
			try {
				JSONObject jsonObject = new JSONObject(returnString);
				String strMsg = jsonObject.getString("Msg");
				int Sign = jsonObject.getInt("Sign");
			// 获取 我的收货地址 成功
				JSONResultBaseModel<AddressModel> mJSONAddrModel = gson.fromJson(
						strMsg, new TypeToken<JSONResultBaseModel<AddressModel>>() {
						}.getType());
				returnList = mJSONAddrModel.getList();

			} catch (Exception e) {
				e.printStackTrace();
			}
			if (returnList != null && returnList.size() > 0) {
				initViewHeaderData(returnList.get(0));
			} else {
				showAddReceiverAddressDialog();
			}
		}
		if (methodName == InformationCodeUtil.methodNameGetAcceptFlyCoin) {
			try {
				JSONResultMsgModel result = gson.fromJson(returnString, JSONResultMsgModel.class);
				totalFlyCoin_web = Integer.parseInt(result.getMsg());
			} catch (Exception e) {
				e.printStackTrace();
			}
			getTotalPrice();
		}
	}

	/** 获取当前商品总价格 */
	 void getTotalPrice() {
		//首先获取可抵用的最大飞币数量
		 int totalPrivilegeFlyCoin = getCurrentPrivilegeTotalFlyCoin();
		 //计算商品总价格
		 Double orderPrices = 0.00;
		 if(cb_confirmOrderIfOpenBilling.isChecked()){
			 orderPrices += currentSelectPackageMode.getmPackageColorModel()
				 .getTaxPrice() * currentSelectPackageMode.getPurchaseQuantity();
		 }else{
			 orderPrices += currentSelectPackageMode.getmPackageColorModel().getPrice()
				 * currentSelectPackageMode.getPurchaseQuantity();
		 }

		 totalOrdersPrices = orderPrices;
		 if(cb_confirmOrderIfOpenBilling.isChecked()){
			 tv_totalPrices.setText(StringUtil.doubleToString(
					 totalOrdersPrices - totalPrivilegeFlyCoin, "0.00") + "(含税)");
		 }else{
			 tv_totalPrices.setText(StringUtil.doubleToString(
					 totalOrdersPrices - totalPrivilegeFlyCoin, "0.00"));
		 }

	}

	/** 首先获取可抵用的最大飞币数量 */
	int getCurrentPrivilegeTotalFlyCoin() {
		int totalPrivilegeFlyCoin = 0;
		if(cb_confirmOrderIfOpenBilling.isChecked()){
				totalPrivilegeFlyCoin += currentSelectPackageMode.getmPackageColorModel().getTaxFlyCoin()
						* currentSelectPackageMode.getPurchaseQuantity();
		}else{
				totalPrivilegeFlyCoin += currentSelectPackageMode.getmPackageColorModel().getFlyCoin()
							* currentSelectPackageMode.getPurchaseQuantity();
		}
		tv_privilegeFlyCoin.setText(""+totalPrivilegeFlyCoin);
		if(totalPrivilegeFlyCoin > totalFlyCoin_web){
			totalPrivilegeFlyCoin = totalFlyCoin_web;
		}
		return totalPrivilegeFlyCoin;
	}

	@Override
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {
		if (methodName == InformationCodeUtil.methodNameGetAddrList) {
			showAddReceiverAddressDialog();
		} else if (methodName == InformationCodeUtil.methodNameAddOrderExt) {
			ToastUtils.show(mContext, "网络异常,订单提交失败");
		}else if(methodName == InformationCodeUtil.methodNameGetAcceptFlyCoin){
			ToastUtils.show(mContext, "获取抵扣飞币数量失败，请检查网络");
			getTotalPrice();
		}
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
	}


	//添加收货地址
	void showAddReceiverAddressDialog() {

		final FragmentCommonDialog fragmentDailog = new FragmentCommonDialog();
		fragmentDailog.initView("提示","没有收货地址，请添加收货地址","取消","添加",
				new FragmentCommonDialog.OnButtonClickListener() {

					@Override
					public void OnOkClick() {
						Intent mIntent = new Intent(ConfirmOrderSportActivity.this,
								ReceiveAddressManageActivity.class);
						mIntent.putExtra("status", 1);
						startActivityForResult(mIntent, 1);
						fragmentDailog.dismiss();
					}

					@Override
					public void OnCancelClick() {
						fragmentDailog.dismiss();
					}

				});
		fragmentDailog.show(getSupportFragmentManager(), "fragmentDailog");
	}


	/** 即将关闭当前页面时启动关闭动画 **/
	void previewToDestroy() {
		finish();
		overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
	}

	/** 如果是返回键，则关闭当前页面 **/
	@Override
	public void onBackPressed() {
		previewToDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
			case RESULT_OK:
				//用户成功生成了订单
				if (requestCode == RequestCode_PayMethodSelectActivity) {
					IfOpenFinishActivityAnim(false);
					finish();
				}
				//用户选择了新的收货地址
				if (requestCode == RequestCode_ShoppingAddressManageActivity) {
					AddressModel mAddressModel = (AddressModel) data
							.getSerializableExtra("selectAddressModel");
					initViewHeaderData(mAddressModel);
				}
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}



	/***
	 * 订单详细
	 * @author ZHU
	 */
	private class OrderItemModel {
		/**单据流水号**/
		private int DjLsh;
		/**订单号*/
		private int OrderID;
		/**商品编号**/
		private int GoodsID;
		/**商品名称**/
		private String GoodsName;
		/**套餐编号**/
		private int PackageID;
		/**套餐名称**/
		private String PackageName;
		/**颜色编号**/
		private int ColorID;
		/**颜色名称**/
		private String ColorName;
		/**单价**/
		private double UnitPrice;
		/**含税单价**/
		private double UnitTaxPrice;
		/**购买数量**/
		private long BuyCount;
		/**延迟付款的天数**/
		private int DelayPayDays;
		/**图片路径**/
		private String ImgUrl;
		/**订单备注**/
		private String Remark;

		public int getDjLsh() {
			return DjLsh;
		}
		public void setDjLsh(int djLsh) {
			DjLsh = djLsh;
		}
		public int getOrderID() {
			return OrderID;
		}
		public void setOrderID(int orderID) {
			OrderID = orderID;
		}
		public int getGoodsID() {
			return GoodsID;
		}
		public void setGoodsID(int goodsID) {
			GoodsID = goodsID;
		}
		public String getGoodsName() {
			return GoodsName;
		}
		public void setGoodsName(String goodsName) {
			GoodsName = goodsName;
		}
		public int getPackageID() {
			return PackageID;
		}
		public void setPackageID(int packageID) {
			PackageID = packageID;
		}
		public String getPackageName() {
			return PackageName;
		}
		public void setPackageName(String packageName) {
			PackageName = packageName;
		}
		public int getColorID() {
			return ColorID;
		}
		public void setColorID(int colorID) {
			ColorID = colorID;
		}
		public String getColorName() {
			return ColorName;
		}
		public void setColorName(String colorName) {
			ColorName = colorName;
		}
		public double getUnitPrice() {
			return UnitPrice;
		}
		public void setUnitPrice(double unitPrice) {
			UnitPrice = unitPrice;
		}
		public double getUnitTaxPrice() {
			return UnitTaxPrice;
		}
		public void setUnitTaxPrice(double unitTaxPrice) {
			UnitTaxPrice = unitTaxPrice;
		}
		public long getBuyCount() {
			return BuyCount;
		}
		public void setBuyCount(long buyCount) {
			BuyCount = buyCount;
		}
		public int getDelayPayDays() {
			return DelayPayDays;
		}
		public void setDelayPayDays(int delayPayDays) {
			DelayPayDays = delayPayDays;
		}
		public String getImgUrl() {
			return ImgUrl;
		}
		public void setImgUrl(String imgUrl) {
			ImgUrl = imgUrl;
		}
		public String getRemark() {
			return Remark;
		}
		public void setRemark(String remark) {
			Remark = remark;
		}
	}

	/***
	 * 用户订单模型类
	 * @author ZHU
	 *
	 */
	public class OrderInfoModel {
		/**自动编号**/
		private int DjLsh;
		/**用户编号**/
		private int UserID;
		/**总金额**/
		private double TotalMoney;
		/**收货地址**/
		private String Address;
		/**真实姓名**/
		private String RealName;
		/**电话号码**/
		private String PhoneNum;
		/**是否开票**/
		private boolean IfBilling;
		/**发票抬头**/
		private String BillingName;
		/**支付方式编号**/
		private int PostMethondID;
		/**支付方式名称**/
		private String PostMethondName;
		/**订单状态**/
		private int OrderSign;
		/**订单号**/
		private String OrderNo;
		/**下单时间**/
		private String OrderTime;
		/**首个商品编号**/
		private int FirstGoodsID;
		/**首个商品名称**/
		private String FirstGoodsName;
		/**首个商品图片**/
		private String FirstImgUrl;
		/**首个套餐名称**/
		private String FirstPackageName;
		/**首个颜色名称**/
		private String FirstColorName;
		/**首个商品单价**/
		private double FirstGoodsUnitMoney;
		/**首个商品数量**/
		private int FirstGoodsBuyCount;
		/**延迟付款的天数**/
		private int DelayPayDays;
		/**订单备注**/
		private String Remark;

		/**快递名称**/
		private String PostName;
		/**快递编号**/
		private String PostNameNum;

		/**凭证图片**/
		private String Certifi;
		/**凭证状态(0未打款， 1已通过)**/
		private int CertifiState;

		/**使用飞币的数量**/
		private int FlyCoin;
		/**订单商品允许抵扣的最大飞币数**/
		private int AcceptFlyCoin;
		/**信用支付倒计时**/
		private int CountDown;
		/**账期是否付款**/
		private boolean IsAccountLimitPayed;

		/**订单是否被选中**/
		private boolean whetherSelect = false;

		public int getDjLsh() {
			return DjLsh;
		}
		public void setDjLsh(int djLsh) {
			DjLsh = djLsh;
		}
		public int getUserID() {
			return UserID;
		}
		public void setUserID(int userID) {
			UserID = userID;
		}
		public double getTotalMoney() {
			return TotalMoney;
		}
		public void setTotalMoney(double totalMoney) {
			TotalMoney = totalMoney;
		}
		public String getAddress() {
			return Address;
		}
		public void setAddress(String address) {
			Address = address;
		}
		public String getRealName() {
			return RealName;
		}
		public void setRealName(String realName) {
			RealName = realName;
		}
		public String getPhoneNum() {
			return PhoneNum;
		}
		public void setPhoneNum(String phoneNum) {
			PhoneNum = phoneNum;
		}
		public boolean isIfBilling() {
			return IfBilling;
		}
		public void setIfBilling(boolean ifBilling) {
			IfBilling = ifBilling;
		}
		public String getBillingName() {
			return BillingName;
		}
		public void setBillingName(String billingName) {
			BillingName = billingName;
		}
		public int getPostMethondID() {
			return PostMethondID;
		}
		public void setPostMethondID(int postMethondID) {
			PostMethondID = postMethondID;
		}
		public String getPostMethondName() {
			return PostMethondName;
		}
		public void setPostMethondName(String postMethondName) {
			PostMethondName = postMethondName;
		}
		public int getOrderSign() {
			return OrderSign;
		}
		public void setOrderSign(int orderSign) {
			OrderSign = orderSign;
		}
		public String getOrderNo() {
			return OrderNo;
		}
		public void setOrderNo(String orderNo) {
			OrderNo = orderNo;
		}
		public String getOrderTime() {
			return OrderTime;
		}
		public void setOrderTime(String OrderTime) {
			this.OrderTime = OrderTime;
		}
		public String getFirstGoodsName() {
			return FirstGoodsName;
		}
		public void setFirstGoodsName(String firstGoodsName) {
			FirstGoodsName = firstGoodsName;
		}
		public String getFirstImgUrl() {
			return FirstImgUrl;
		}
		public void setFirstImgUrl(String firstImgUrl) {
			FirstImgUrl = firstImgUrl;
		}
		public String getFirstPackageName() {
			return FirstPackageName;
		}
		public void setFirstPackageName(String firstPackageName) {
			FirstPackageName = firstPackageName;
		}
		public String getFirstColorName() {
			return FirstColorName;
		}
		public void setFirstColorName(String firstColorName) {
			FirstColorName = firstColorName;
		}

		public double getFirstGoodsUnitMoney() {
			return FirstGoodsUnitMoney;
		}
		public void setFirstGoodsUnitMoney(double firstGoodsUnitMoney) {
			FirstGoodsUnitMoney = firstGoodsUnitMoney;
		}

		public int getFirstGoodsBuyCount() {
			return FirstGoodsBuyCount;
		}
		public void setFirstGoodsBuyCount(int firstGoodsBuyCount) {
			FirstGoodsBuyCount = firstGoodsBuyCount;
		}
		public int getDelayPayDays() {
			return DelayPayDays;
		}
		public void setDelayPayDays(int delayPayDays) {
			DelayPayDays = delayPayDays;
		}
		public boolean getWhetherSelect() {
			return whetherSelect;
		}
		public void setWhetherSelect(boolean whetherSelect) {
			this.whetherSelect = whetherSelect;
		}
		public int getFlyCoin() {
			return FlyCoin;
		}
		public void setFlyCoin(int flyCoin) {
			FlyCoin = flyCoin;
		}
		public int getAcceptFlyCoin() {
			return AcceptFlyCoin;
		}
		public void setAcceptFlyCoin(int acceptFlyCoin) {
			AcceptFlyCoin = acceptFlyCoin;
		}
		public int getCountDown() {
			return CountDown;
		}
		public void setCountDown(int countDown) {
			CountDown = countDown;
		}
		public int getFirstGoodsID() {
			return FirstGoodsID;
		}
		public void setFirstGoodsID(int firstGoodsID) {
			FirstGoodsID = firstGoodsID;
		}
		public String getPostName() {
			return PostName;
		}
		public void setPostName(String postName) {
			PostName = postName;
		}
		public String getPostNameNum() {
			return PostNameNum;
		}
		public void setPostNameNum(String postNameNum) {
			PostNameNum = postNameNum;
		}
		public String getCertifi() {
			return Certifi;
		}
		public void setCertifi(String certifi) {
			Certifi = certifi;
		}
		public int getCertifiState() {
			return CertifiState;
		}
		public void setCertifiState(int certifiState) {
			CertifiState = certifiState;
		}
		public boolean isIsAccountLimitPayed() {
			return IsAccountLimitPayed;
		}
		public void setIsAccountLimitPayed(boolean isAccountLimitPayed) {
			IsAccountLimitPayed = isAccountLimitPayed;
		}
		public String getRemark() {
			return Remark;
		}
		public void setRemark(String remark) {
			Remark = remark;
		}

	}

}









