package com.shi.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultBaseModel;
import com.shi.xianglixiangqin.json.JSONResultMsgModel;
import com.shi.xianglixiangqin.model.GoodsGeneralModel;
import com.shi.xianglixiangqin.model.GoodsPackageStandardModel;
import com.shi.xianglixiangqin.model.GoodsPackageModel;
import com.shi.xianglixiangqin.model.SelectPackageModel;
import com.shi.xianglixiangqin.model.ShoppingCartChildGoodsModel;
import com.shi.xianglixiangqin.model.ShoppingCartParentGoodsModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.PreferencesUtilMy;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.ToastUtil;
import com.shi.xianglixiangqin.view.FlowTagLayout;
import com.shi.xianglixiangqin.view.FragmentOkDialog;

import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/****
 * 套餐选择界面 加入购物车和立即购买
 * @author SHI
 * 2016-3-15 16:03:03
 */
public class GoodsPackageActivity extends MyBaseTranslucentActivity implements OnClickListener, OnConnectServerStateListener<Integer> {

	/**关闭当前界面控件**/
	@BindView(R.id.iv_closeActivity)
	ImageView iv_closeActivity;
	/**当前商品图标**/
	@BindView(R.id.iv_goodsImages)
	ImageView iv_goodsImages;
	/**当前商品名称**/
	@BindView(R.id.tv_goodsName)
	TextView tv_goodsName;
	/**当前商品价格**/
	@BindView(R.id.tv_goodsPrice)
	TextView tv_goodsPrice;
	/**当前商品选中套餐描述**/
	@BindView(R.id.tv_goodsPackageDesc)
	TextView tv_goodsPackageDesc;
	/**当前商品库存**/
	@BindView(R.id.tv_goodsStock)
	TextView tv_goodsStock;
	/**当前商品购买数量减一**/
	@BindView(R.id.iv_goodsNumberSub)
	ImageView iv_goodsNumberSub;
	/**当前商品购买数量加一**/
	@BindView(R.id.iv_goodsNumberAdd)
	ImageView iv_goodsNumberAdd;
	/**当前商品购买数量**/
	@BindView(R.id.et_goodsNumber)
	EditText et_goodsNumber;
	/**加入购物车**/
	@BindView(R.id.btn_addShoppingCart)
	Button btn_addShoppingCart;
	/**立即购买**/
	@BindView(R.id.btn_buyNow)
	Button btn_buyNow;

	/**套餐描述**/
	@BindView(R.id.tv_goodsPackage)
	TextView tv_goodsPackage;

	/**当前商品套餐**/
	@BindView(R.id.flowTagLayout_goodsPackage)
	FlowTagLayout flowTagLayout_goodsPackage;
	List<GoodsPackageModel> listGoodsPackageData;
	GoodPackageTypeAdapter adapterGoodsPackage;

	/**套餐规格描述**/
	@BindView(R.id.tv_goodsPackageStandard)
	TextView tv_goodsPackageStandard;

	/**当前商品套餐规格**/
	@BindView(R.id.flowTagLayout_goodsPackageStandard)
	FlowTagLayout flowTagLayout_goodsPackageStandard;
	List<GoodsPackageStandardModel> listPackageStandardData;
	GoodPackageStandardAdapter adapterGoodsPackageStandard;


	/**当前商品详细信息**/
	 GoodsGeneralModel mGoodsGeneralModel;
	/**当前被选中套餐数据**/
	 GoodsPackageModel selectProductPackage;
	/**当前被选中套餐颜色数据*/
	 GoodsPackageStandardModel selectGoodsPackageStandard;
	/**当前购买的数量**/
	 int currentPurchaseQuantity;

	/**当前套餐详情对象**/
	 SelectPackageModel currentSelectPackageModel;

	private String url_Image;
	@Override
	public void initView() {
		setContentView(R.layout.activity_goods_package);
		ButterKnife.bind(mContext);

		mGoodsGeneralModel = (GoodsGeneralModel) getIntent().getSerializableExtra(InformationCodeUtil.IntentGoodsActivityCurrentGoodsModel);
		if(mGoodsGeneralModel == null){
			finish();
			return;
		}
		if(mGoodsGeneralModel.getPlatformActionID() != 0){
			btn_addShoppingCart.setVisibility(View.GONE);
			if(mGoodsGeneralModel.getPlatformActionType() == InformationCodeUtil.PlatformActionType_MesKill){
				btn_buyNow.setText(R.string.clickMesKill);
			}else if(mGoodsGeneralModel.getPlatformActionType() == InformationCodeUtil.PlatformActionType_SaleByTimeLimited){
				btn_buyNow.setText(R.string.justToRob);
			}else if(mGoodsGeneralModel.getPlatformActionType() == InformationCodeUtil.PlatformActionType_GroupCentre){
				btn_buyNow.setText(R.string.justToJoinGroup);
			}
		}else{
			btn_buyNow.setVisibility(View.GONE);
		}
		btn_buyNow.setOnClickListener(this);
		iv_closeActivity.setOnClickListener(this);
		iv_goodsNumberSub.setOnClickListener(this);
		iv_goodsNumberAdd.setOnClickListener(this);
		btn_addShoppingCart.setOnClickListener(this);
		url_Image = mGoodsGeneralModel.getImages().get(0);
		ImagerLoaderUtil.getInstance(mContext).displayMyImage(url_Image, iv_goodsImages);
		tv_goodsName.setText(mGoodsGeneralModel.getGoodsName());

		//初始化商品套餐数据
		listGoodsPackageData = new ArrayList<GoodsPackageModel>();
		adapterGoodsPackage = new GoodPackageTypeAdapter(mContext,listGoodsPackageData);
		flowTagLayout_goodsPackage.setOnItemSelectedListener(new FlowTagLayout.OnItemSelectedListener() {
			@Override
			public void onItemSelected(View view, int position, long id) {
					selectProductPackage = listGoodsPackageData.get(position);
					listPackageStandardData.clear();
					listPackageStandardData.addAll(selectProductPackage.getPackageColorJsonList());
					if(mGoodsGeneralModel.getPlatformActionID() != 0){
						for (int i=0; i<listPackageStandardData.size();i++){
							listPackageStandardData.get(i).setStoneCount(mGoodsGeneralModel.getStoneCount());
						}
					}

					flowTagLayout_goodsPackageStandard.setCurrentSelectView(-1);
					selectGoodsPackageStandard = null;
					for(int i=0; i<listPackageStandardData.size(); i++){
						if(listPackageStandardData.get(i).getStoneCount() > 0 ){
							flowTagLayout_goodsPackageStandard.setCurrentSelectView(i);
							selectGoodsPackageStandard = listPackageStandardData.get(i);
							break;
						}
					}
					adapterGoodsPackageStandard.notifyDataSetChanged();
					refreshPackageData();
			}

			@Override
			public void onNothingSelected(View view, int position, long id) {
					flowTagLayout_goodsPackage.setCurrentSelectView(-1);
					flowTagLayout_goodsPackageStandard.setCurrentSelectView(-1);
					adapterGoodsPackageStandard.notifyDataSetChanged();
					selectProductPackage = null;
					selectGoodsPackageStandard = null;
					refreshPackageData();
			}
		});

		flowTagLayout_goodsPackage.setAdapter(adapterGoodsPackage);

		listPackageStandardData = new ArrayList<GoodsPackageStandardModel>();
		adapterGoodsPackageStandard = new GoodPackageStandardAdapter(mContext,listPackageStandardData);
		flowTagLayout_goodsPackageStandard.setOnItemSelectedListener(new FlowTagLayout.OnItemSelectedListener() {
			@Override
			public void onItemSelected(View view, int position, long id) {
				TextView textView = (TextView) view.findViewById(R.id.tv_tag);
				if(textView != null && textView.isEnabled()){
					selectGoodsPackageStandard = listPackageStandardData.get(position);
				}else{
					selectGoodsPackageStandard = null;
				}
				refreshPackageData();
			}

			@Override
			public void onNothingSelected(View view, int position, long id) {
				TextView textView = (TextView) view.findViewById(R.id.tv_tag);
				if(textView != null && textView.isEnabled()){
					selectGoodsPackageStandard = null;
				}else{
					selectGoodsPackageStandard = null;
				}
				refreshPackageData();
			}
		});

		flowTagLayout_goodsPackageStandard.setAdapter(adapterGoodsPackageStandard);
	}

	//响应套餐颜色点击事件之后 刷新界面显示数据
	 private void refreshPackageData() {
		 url_Image = mGoodsGeneralModel.getImages().get(0);
		 double productPackagePrice = 0;
		 long productPackageStockNumber = 0;
		 String ProductPackageDesc = "套餐:未选套餐";
		 String ProductPackageStandardDesc = "未选规格";
		 if(selectProductPackage != null){
			 ProductPackageDesc = "套餐:"+selectProductPackage.getPackageName();
		 }
		 if(selectGoodsPackageStandard != null){
			 productPackagePrice = selectGoodsPackageStandard.getPrice();
			 ProductPackageStandardDesc = selectGoodsPackageStandard.getColorName();
			 productPackageStockNumber = selectGoodsPackageStandard.getStoneCount();
			 url_Image = selectGoodsPackageStandard.getColorimg();
			 if(StringUtil.isEmpty(url_Image)){
				 url_Image = mGoodsGeneralModel.getImages().get(0);
			 }
		 }
		ImagerLoaderUtil.getInstance(mContext).displayMyImage(url_Image, iv_goodsImages);
		DecimalFormat dcm = new DecimalFormat("0.00");
		tv_goodsPrice.setText(dcm.format(productPackagePrice));
		tv_goodsPackageDesc.setText(ProductPackageDesc+"/"+ProductPackageStandardDesc);
		tv_goodsStock.setText("库存:"+productPackageStockNumber);
	}

	@Override
	public void initData() {
		getData(InformationCodeUtil.methodNameGetGoodsPackageList);
	}

	 void getData(String methodName) {
		if(InformationCodeUtil.methodNameGetGoodsPackageList == methodName){
			SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			requestSoapObject.addProperty("goodsID", mGoodsGeneralModel.getDjLsh());
			requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
		    ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask
					(mContext, this, requestSoapObject , methodName);
			connectGoodsServiceAsyncTask.execute();
			return;
		}
		 /**C
		  * 把商品加入购物车
		  * (int customID, string openKey, int ProductID(商品id)
		  * , int PackageID(套餐id), int PackageColorID(套餐颜色id), int Quantity(购买量))
		  */
		 if(InformationCodeUtil.methodNameAddShopCart == methodName){
			 SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			 requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
			 requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
			 requestSoapObject.addProperty("ProductID", currentSelectPackageModel.getmProductModel().getDjLsh());
			 requestSoapObject.addProperty("PackageID",  currentSelectPackageModel.getmPackages().getDjLsh());
			 requestSoapObject.addProperty("PackageColorID", currentSelectPackageModel.getmPackageColorModel().getDjLsh());
			 requestSoapObject.addProperty("Quantity", currentSelectPackageModel.getPurchaseQuantity());
			 ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
					 (mContext, this, requestSoapObject , methodName);
			 connectCustomServiceAsyncTask.execute();
			 return;
		 }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_closeActivity:
			finish();
			overridePendingTransition(R.anim.not_change, R.anim.out_to_bottom);
			break;
		case R.id.iv_goodsNumberSub:
			try {
				currentPurchaseQuantity = Integer.parseInt(et_goodsNumber.getText().toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			if(currentPurchaseQuantity > 1){
				currentPurchaseQuantity--;
			}
			et_goodsNumber.setText(currentPurchaseQuantity+"");
			break;
		case R.id.iv_goodsNumberAdd:
			try {
				currentPurchaseQuantity = Integer.parseInt(et_goodsNumber.getText().toString());
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			if(mGoodsGeneralModel.getPlatformActionType() == 0){
				currentPurchaseQuantity++;
				et_goodsNumber.setText(currentPurchaseQuantity+"");
				return;
			}
			if(currentPurchaseQuantity<mGoodsGeneralModel.getUserQuantity()){
				currentPurchaseQuantity++;
			}else{
				ToastUtil.show(mContext, "购买数量已达到单个用户购买上限");
			}
			et_goodsNumber.setText(currentPurchaseQuantity+"");
			break;
		case R.id.btn_addShoppingCart:
			if(selectProductPackage == null || selectGoodsPackageStandard == null){
				ToastUtil.show(mContext, "请先选择具体套餐");
			}else{
				try {
					currentPurchaseQuantity = Integer.parseInt(et_goodsNumber.getText().toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
					currentPurchaseQuantity = 1;
					et_goodsNumber.setText(""+currentPurchaseQuantity);
				}
				if(currentPurchaseQuantity < 1){
					et_goodsNumber.setText(""+currentPurchaseQuantity);
					currentPurchaseQuantity = 1;
					return ;
				}
				if(currentPurchaseQuantity > selectGoodsPackageStandard.getStoneCount()){
					ToastUtil.show(mContext, "操作失败,该商品库存数量不足");
					return;
				}
				currentSelectPackageModel = new SelectPackageModel(
				selectGoodsPackageStandard, selectProductPackage, mGoodsGeneralModel, currentPurchaseQuantity);
				getData(InformationCodeUtil.methodNameAddShopCart);
			}
			break;
		case R.id.btn_buyNow:
			if(selectProductPackage == null || selectGoodsPackageStandard == null){
				ToastUtil.show(mContext, "请先选择套餐");
			}else{
				try {
					currentPurchaseQuantity = Integer.parseInt(et_goodsNumber.getText().toString());
				} catch (NumberFormatException e) {
					e.printStackTrace();
					currentPurchaseQuantity = 1;
					et_goodsNumber.setText(""+currentPurchaseQuantity);
				}

				if(mGoodsGeneralModel.getPlatformActionType() != 0){

					if(currentPurchaseQuantity > mGoodsGeneralModel.getUserQuantity()){
						ToastUtil.show(mContext, "购买数量已达到单个用户购买上限");
						currentPurchaseQuantity = mGoodsGeneralModel.getUserQuantity();
						et_goodsNumber.setText(""+currentPurchaseQuantity);
						return ;
					}
				}
				if(currentPurchaseQuantity < 1){
					currentPurchaseQuantity = 1;
					et_goodsNumber.setText(""+currentPurchaseQuantity);
					return ;
				}

				currentSelectPackageModel = new SelectPackageModel(
						selectGoodsPackageStandard, selectProductPackage, mGoodsGeneralModel, currentPurchaseQuantity);
				Intent intent = new Intent(mContext,ConfirmOrderSportActivity.class);
				intent.putExtra(InformationCodeUtil.IntentConfirmOrderSportActivityPackageModel,currentSelectPackageModel);
				startActivity(intent);
				finish();
			}
			break;

		default:
			break;
		}
	}


	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(R.anim.not_change, R.anim.out_to_bottom);
	}


	/** 添加JSON数据 到购物本地缓存中 **/
	public void addGoodsToCacheData() {
		GoodsGeneralModel tempGoods = currentSelectPackageModel.getmProductModel();
		GoodsPackageModel tempPackage = currentSelectPackageModel.getmPackages();
		GoodsPackageStandardModel tempPackageColor = currentSelectPackageModel.getmPackageColorModel();
		ShoppingCartChildGoodsModel currentChildGoodsModel = new ShoppingCartChildGoodsModel
				( 0, tempGoods.getDjLsh(),tempGoods.getGoodsName(), tempGoods.getImgUrl()
				,tempPackage.getDjLsh(),tempPackage.getPackageName()
				,tempPackageColor.getDjLsh(),tempPackageColor.getColorName()
				,currentSelectPackageModel.getPurchaseQuantity()
				,tempPackageColor.getPrice(),tempPackageColor.getTaxPrice()
				,tempPackageColor.getFlyCoin(),tempPackageColor.getTaxFlyCoin(),0.00);
		List<ShoppingCartChildGoodsModel> tempList = new ArrayList<ShoppingCartChildGoodsModel>();
		tempList.add(currentChildGoodsModel);

		ShoppingCartParentGoodsModel currentParentGoodsModel = new ShoppingCartParentGoodsModel();
		currentParentGoodsModel.setShopName(currentSelectPackageModel.getmProductModel().getShopName());
		currentParentGoodsModel.setShopID(currentSelectPackageModel.getmProductModel().getShopID());
		currentParentGoodsModel.setShoppingCarts(tempList);

		PreferencesUtilMy.addGoodsToShopCart(mContext,currentParentGoodsModel);
	}


	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		LogUtil.LogShitou("returnSoapObject", returnString);
		if(InformationCodeUtil.methodNameGetGoodsPackageList == methodName){
			JSONResultBaseModel<GoodsPackageModel> result = null;
			try {
				Gson gson = new Gson();
				result = gson.fromJson
				(returnString, new TypeToken<JSONResultBaseModel<GoodsPackageModel>>(){}.getType());

				if(!StringUtil.isEmpty(result.getTitle())){
					tv_goodsPackage.setText(result.getTitle());
				}
				if(!StringUtil.isEmpty(result.getSubTitle())){
					tv_goodsPackageStandard.setText(result.getSubTitle());
				}
				} catch (Exception e) {
				e.printStackTrace();
			}
			if(result != null || result.getList() != null){

				listGoodsPackageData.clear();
				listGoodsPackageData.addAll(result.getList());
				if(listGoodsPackageData.size() == 0){
				FragmentOkDialog fragmentOkDialog = new FragmentOkDialog();
				fragmentOkDialog.initView("温馨提示", "该商品还未配置套餐", "确定", new FragmentOkDialog.OnButtonClickListener() {
					@Override
					public void OnOkClick() {
						finish();
						overridePendingTransition(R.anim.not_change, R.anim.out_to_bottom);
					}

					@Override
					public void OnCancelClick() {
						finish();
						overridePendingTransition(R.anim.not_change, R.anim.out_to_bottom);
					}
				});
				fragmentOkDialog.show(getSupportFragmentManager(),"FragmentOkDialog");
//				finish();
				return;
				}
				adapterGoodsPackage.notifyDataSetChanged();

				listPackageStandardData.clear();
				selectProductPackage = listGoodsPackageData.get(0);
				listPackageStandardData.addAll(selectProductPackage.getPackageColorJsonList());
				if(mGoodsGeneralModel.getPlatformActionID() != 0){
					for (int i=0; i<listPackageStandardData.size();i++){
						listPackageStandardData.get(i).setStoneCount(mGoodsGeneralModel.getStoneCount());
					}
				}
				flowTagLayout_goodsPackageStandard.setCurrentSelectView(-1);
				selectGoodsPackageStandard = null;
				for(int i=0; i<listPackageStandardData.size(); i++){
					if(listPackageStandardData.get(i).getStoneCount() > 0 ){
						flowTagLayout_goodsPackageStandard.setCurrentSelectView(i);
						selectGoodsPackageStandard = listPackageStandardData.get(i);
						break;
					}
				}
				adapterGoodsPackageStandard.notifyDataSetChanged();
			}
			refreshPackageData();
			return;
		}

		if(InformationCodeUtil.methodNameAddShopCart == methodName){
			JSONResultMsgModel mJSONResultMsgModel = null;
			try {
				Gson gson = new Gson();
				mJSONResultMsgModel = gson.fromJson(returnString, JSONResultMsgModel.class);
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			}
			if(mJSONResultMsgModel != null){
				if(mJSONResultMsgModel.getSign() == 1){
					ToastUtil.show(mContext,"加入购物车成功");
					addGoodsToCacheData();
					finish();
					overridePendingTransition(R.anim.not_change, R.anim.out_to_bottom);
				}else{
					ToastUtil.show(mContext,mJSONResultMsgModel.getMsg());
				}
			}

		}
	}


	@Override
	public void connectServiceFailed(String returnStrError, String methodName, Integer state,
			boolean whetherRefresh) {

			if(InformationCodeUtil.methodNameGetGoodsPackageList == methodName){
				ToastUtil.show(mContext, returnStrError);
			}
			if(InformationCodeUtil.methodNameAddShopCart == methodName){
				ToastUtil.show(mContext, returnStrError);
			}

		}
	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
	}


	public class GoodPackageTypeAdapter extends MyBaseAdapter<GoodsPackageModel>{


		public GoodPackageTypeAdapter(Context mContext, List<GoodsPackageModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			View view = LayoutInflater.from(mContext).inflate(R.layout.item_tag_button_background, null);

			TextView textView = (TextView) view.findViewById(R.id.tv_tag);
			GoodsPackageModel t = listData.get(position);

			textView.setText(t.getPackageName());
			return view;
		}
	}


	public class GoodPackageStandardAdapter extends MyBaseAdapter<GoodsPackageStandardModel>{

		public GoodPackageStandardAdapter(Context mContext, List<GoodsPackageStandardModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {

			if(position < 0 || position >= listData.size()){
				return null;
			}else{
				View view = View.inflate(mContext ,R.layout.item_tag_button_background, null);
				TextView textView = (TextView) view.findViewById(R.id.tv_tag);
				GoodsPackageStandardModel model = listData.get(position);
				if(model.getStoneCount() == 0){
					textView.setEnabled(false);
				}else{
					textView.setEnabled(true);
				}
				textView.setText(model.getColorName());

				return view;
			}
		}

	}

}
