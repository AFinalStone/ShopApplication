package com.shuimunianhua.xianglixiangqin.activity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.adapter.MyBaseAdapter;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnStateChangeListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultBaseModel;
import com.shuimunianhua.xianglixiangqin.json.JSONResultMsgModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsGeneralModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsPackageModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsPackageColorModel;
import com.shuimunianhua.xianglixiangqin.model.SelectPackageModel;
import com.shuimunianhua.xianglixiangqin.model.ShoppingCartChildGoodsModel;
import com.shuimunianhua.xianglixiangqin.model.ShoppingCartParentGoodsModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.ImagerLoaderUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.PreferencesUtilMy;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

/****
 * 套餐选择界面 加入购物车和立即购买
 * @author SHI
 * 2016-3-15 16:03:03
 */
public class GoodsPackageActivity extends MyBaseActivity implements OnClickListener, OnConnectServerStateListener<Integer> {

	/**关闭当前界面控件**/
	@Bind(R.id.iv_closeActivity)
	ImageView iv_closeActivity;
	/**当前商品图标**/
	@Bind(R.id.iv_goodsImages)
	ImageView iv_goodsImages;
	/**当前商品名称**/
	@Bind(R.id.tv_goodsName)
	TextView tv_goodsName;
	/**当前商品价格**/
	@Bind(R.id.tv_goodsPrice)
	TextView tv_goodsPrice;
	/**当前商品选中套餐描述**/
	@Bind(R.id.tv_goodsPackageDesc)
	TextView tv_goodsPackageDesc;
	/**当前商品库存**/
	@Bind(R.id.tv_goodsStock)
	TextView tv_goodsStock;
	/**当前商品购买数量减一**/
	@Bind(R.id.iv_goodsNumberSub)
	ImageView iv_goodsNumberSub;
	/**当前商品购买数量加一**/
	@Bind(R.id.iv_goodsNumberAdd)
	ImageView iv_goodsNumberAdd;
	/**当前商品购买数量**/
	@Bind(R.id.et_goodsNumber)
	EditText et_goodsNumber;
	/**加入购物车**/
	@Bind(R.id.btn_addShoppingCart)
	Button btn_addShoppingCart;
	/**立即购买**/
	@Bind(R.id.btn_buyNow)
	Button btn_buyNow;
	/**当前商品套餐**/
	@Bind(R.id.gridview_goodsPackageType)
	GridView gridview_goodsPackageType;
	List<GoodsPackageModel> listPackageData;
	GridPackageAdapter gridPackageAdapter;
	/**当前商品 颜色套餐配置**/
	@Bind(R.id.gridview_goodsSpecTyp)
	GridView gridview_goodsSpecTyp;
	List<GoodsPackageColorModel> listPackageColorData;
	GridPackageColorAdapter gridPackageColorAdapter;

	/**当前商品详细信息**/
	 GoodsGeneralModel mGoodsGeneralModel;
	/**当前被选中套餐数据**/
	 GoodsPackageModel selectProductPackage;
	/**当前被选中套餐颜色数据*/
	 GoodsPackageColorModel selectProductPackageColor;
	/**当前购买的数量**/
	 int currentPurchaseQuantity;

	/**当前套餐详情对象**/
	 SelectPackageModel currentSelectPackageModel;
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
		}else{
			btn_buyNow.setVisibility(View.GONE);
		}
		btn_buyNow.setOnClickListener(this);
		iv_closeActivity.setOnClickListener(this);
		iv_goodsNumberSub.setOnClickListener(this);
		iv_goodsNumberAdd.setOnClickListener(this);
		btn_addShoppingCart.setOnClickListener(this);
		ImagerLoaderUtil.getInstance(mContext).displayMyImage(mGoodsGeneralModel.getImages().get(0), iv_goodsImages);
		tv_goodsName.setText(mGoodsGeneralModel.getGoodsName());
		//初始化商品套餐数据
		listPackageData = new ArrayList<GoodsPackageModel>();
		gridPackageAdapter = new GridPackageAdapter( mContext, listPackageData);
		gridPackageAdapter.setOnCheckStateChangeListener(new OnStateChangeListener() {

			@Override
			public void OnStateChange(boolean isChecked) {
				selectProductPackage = null;
				//清空颜色套餐已经选中的状态
				for (int i = 0; i < listPackageColorData.size(); i++) {
					listPackageColorData.get(i).setWhetherSelect(false);
				}
				//检测套餐是否被选中，被选中则刷新颜色套餐集合数据
				for(int i=0; i<listPackageData.size(); i++){
					if(listPackageData.get(i).getWhetherSelect()){
						listPackageColorData.clear();
						selectProductPackage = listPackageData.get(i);
						listPackageColorData.addAll(listPackageData.get(i).getPackageColorJsonList());
						if(listPackageColorData != null && listPackageColorData.size() > 0 ){
							listPackageColorData.get(0).setWhetherSelect(true);
						}
						break;
					}
				}
				gridPackageColorAdapter.notifyDataSetChanged();
				RerushPackageData();
			}

		});
		gridview_goodsPackageType.setAdapter(gridPackageAdapter);
		//初始化商品套餐颜色数据
		listPackageColorData = new ArrayList<GoodsPackageColorModel>();
		gridPackageColorAdapter = new GridPackageColorAdapter( mContext,listPackageColorData);
		gridview_goodsSpecTyp.setAdapter(gridPackageColorAdapter);
		gridPackageColorAdapter.setOnCheckStateChangeListener(new OnStateChangeListener() {

			@Override
			public void OnStateChange(boolean isChecked) {
				if(selectProductPackage == null){
					return;
				}
				RerushPackageData();
			}
		});
	}

	//响应套餐颜色点击事件之后 刷新界面显示数据
	 private void RerushPackageData() {

		GoodsPackageColorModel productPackageColor;
		double productPackagePrice = 0;
		String ProductPackageDesc = "套餐:尚未选择套餐";
		long productPackageStockNumber = 0;
		selectProductPackageColor = null;
		for(int i=0; i<listPackageColorData.size(); i++){
			productPackageColor = listPackageColorData.get(i);
			if(productPackageColor.getWhetherSelect()){
				selectProductPackageColor = productPackageColor;
				productPackagePrice = productPackageColor.getPrice();
				ProductPackageDesc = "套餐:"+selectProductPackage.getPackageName()
						+"/"+selectProductPackageColor.getColorName();
				productPackageStockNumber = productPackageColor.getStoneCount();
				break;
			}
		}
		DecimalFormat dcm = new DecimalFormat("0.00");
		tv_goodsPrice.setText(dcm.format(productPackagePrice));
		tv_goodsPackageDesc.setText(ProductPackageDesc);
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
				currentPurchaseQuantity = 1;
				et_goodsNumber.setText(""+currentPurchaseQuantity);
			}
			if(currentPurchaseQuantity > 1){
				currentPurchaseQuantity--;
				et_goodsNumber.setText(currentPurchaseQuantity+"");
			}
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
				ToastUtils.show(mContext, "购买数量已达到单个用户购买上限");
			}
			et_goodsNumber.setText(currentPurchaseQuantity+"");
			break;
		case R.id.btn_addShoppingCart:
			if(selectProductPackage == null || selectProductPackageColor == null){
				ToastUtils.show(mContext, "请先选择套餐");
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
				if(currentPurchaseQuantity > selectProductPackageColor.getStoneCount()){
					ToastUtils.show(mContext, "操作失败,该商品库存数量不足");
					return;
				}
				currentSelectPackageModel = new SelectPackageModel(
				selectProductPackageColor, selectProductPackage, mGoodsGeneralModel, currentPurchaseQuantity);
				getData(InformationCodeUtil.methodNameAddShopCart);
			}
			break;
		case R.id.btn_buyNow:
			if(selectProductPackage == null || selectProductPackageColor == null){
				ToastUtils.show(mContext, "请先选择套餐");
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
						ToastUtils.show(mContext, "购买数量已达到单个用户购买上限");
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
						selectProductPackageColor, selectProductPackage, mGoodsGeneralModel, currentPurchaseQuantity);
				Intent intent = new Intent(mContext,ConfirmOrderSportActivity.class);
				intent.putExtra(InformationCodeUtil.IntentConfirmOrderSportActivityPackageModel,currentSelectPackageModel);
				startActivity(intent);
				finish();
				overridePendingTransition(R.anim.in_from_top, R.anim.out_to_bottom);
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
		GoodsPackageColorModel tempPackageColor = currentSelectPackageModel.getmPackageColorModel();
		ShoppingCartChildGoodsModel currentChildGoodsModel = new ShoppingCartChildGoodsModel
				( 0, tempGoods.getDjLsh(),tempGoods.getGoodsName(), tempGoods.getImgUrl()
				,tempPackage.getDjLsh(),tempPackage.getPackageName()
				,tempPackageColor.getDjLsh(),tempPackageColor.getColorName()
				,currentSelectPackageModel.getPurchaseQuantity()
				,tempPackageColor.getPrice(),tempPackageColor.getTaxPrice()
				,tempPackageColor.getFlyCoin(),tempPackageColor.getTaxFlyCoin());
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
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(result != null || result.getList() != null){
				listPackageData.clear();
				listPackageData.addAll(result.getList());
				selectProductPackage = listPackageData.get(0);
				selectProductPackage.setWhetherSelect(true);
				listPackageColorData.clear();
				listPackageColorData.addAll(listPackageData.get(0).getPackageColorJsonList());
				if(mGoodsGeneralModel.getPlatformActionID() != 0){
					for (int i=0; i<listPackageColorData.size();i++){
						listPackageColorData.get(i).setStoneCount(mGoodsGeneralModel.getStoneCount());
					}
				}

				selectProductPackageColor = listPackageColorData.get(0);
				selectProductPackageColor.setWhetherSelect(true);
				gridPackageAdapter.notifyDataSetChanged();
				gridPackageColorAdapter.notifyDataSetChanged();
			}
			RerushPackageData();
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
					ToastUtils.show(mContext,"加入购物车成功");
					addGoodsToCacheData();
					finish();
					overridePendingTransition(R.anim.not_change, R.anim.out_to_bottom);
				}else{
					ToastUtils.show(mContext,mJSONResultMsgModel.getMsg());
				}
			}

		}
	}


	@Override
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {

			if(InformationCodeUtil.methodNameGetGoodsPackageList == methodName){
				ToastUtils.show(mContext,"网络异常，获取商品套餐数据失败");
			}
			if(InformationCodeUtil.methodNameAddShopCart == methodName){
				ToastUtils.show(mContext,"网络异常，商品加入购物车失败");
			}

		}
	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
	}

	/**
	 * 商品套餐
	 * @author SHI
	 * 2016-2-17 15:33:47
	 *
	 */
	public class GridPackageAdapter extends MyBaseAdapter<GoodsPackageModel> {


		public OnStateChangeListener onCheckStateChangeListener;

		public GridPackageAdapter(Context mContext, List<GoodsPackageModel> listData) {
			super(mContext, listData);
		}

		public void setOnCheckStateChangeListener(OnStateChangeListener onCheckStateChangeListener){
			this.onCheckStateChangeListener = onCheckStateChangeListener;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mContext, R.layout.item_gridview_productpackage, null);
			CheckBox cb_productPackage = (CheckBox) view.findViewById(R.id.cb_productPackage);
			final GoodsPackageModel currentPackages = listData.get(position);
			cb_productPackage.setText(currentPackages.getPackageName());
			cb_productPackage.setChecked(currentPackages.getWhetherSelect());
			cb_productPackage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					for (int i = 0; i < listData.size(); i++) {
						listData.get(i).setWhetherSelect(false);
					}
					currentPackages.setWhetherSelect(isChecked);
					if(onCheckStateChangeListener != null){
						onCheckStateChangeListener.OnStateChange(isChecked);
					}
					notifyDataSetChanged();
				}
			});
			return view;
		}
	}

	/**
	 * 商品套餐颜色
	 * @author SHI
	 *	2016-2-17 15:38:58
	 */
	public class GridPackageColorAdapter extends MyBaseAdapter<GoodsPackageColorModel> {


			public GridPackageColorAdapter(Context mContext,
				List<GoodsPackageColorModel> listData) {
			super(mContext, listData);
		}

			public OnStateChangeListener onCheckStateChangeListener;

			public void setOnCheckStateChangeListener(OnStateChangeListener onCheckStateChangeListener){
				this.onCheckStateChangeListener = onCheckStateChangeListener;
			}

			@Override
			public View getView(final int position, View convertView, ViewGroup parent) {
				View view = View.inflate(mContext, R.layout.item_gridview_productpackage, null);
				CheckBox cb_productPackage = (CheckBox) view.findViewById(R.id.cb_productPackage);
				final GoodsPackageColorModel currentPackageColorJsonList = listData.get(position);
				cb_productPackage.setText(currentPackageColorJsonList.getColorName());
				cb_productPackage.setChecked(currentPackageColorJsonList.getWhetherSelect());
				cb_productPackage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						for (int i = 0; i < listData.size(); i++) {
							listData.get(i).setWhetherSelect(false);
						}
						currentPackageColorJsonList.setWhetherSelect(isChecked);
						if(onCheckStateChangeListener != null){
							onCheckStateChangeListener.OnStateChange(isChecked);
						}
						notifyDataSetChanged();
					}
				});			
				return view;
			}

	}

}
