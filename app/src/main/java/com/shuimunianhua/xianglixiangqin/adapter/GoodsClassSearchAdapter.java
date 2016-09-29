package com.shuimunianhua.xianglixiangqin.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultMsgModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsGeneralModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsPackageColorModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsPackageModel;
import com.shuimunianhua.xianglixiangqin.model.ShoppingCartChildGoodsModel;
import com.shuimunianhua.xianglixiangqin.model.ShoppingCartParentGoodsModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.ImagerLoaderUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.PreferencesUtilMy;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

/****
 * 
 * @author SHI
 * 2016-2-17 15:44:03
 *
 */
public class GoodsClassSearchAdapter extends MyBaseAdapter<GoodsGeneralModel> implements OnConnectServerStateListener<GoodsGeneralModel> {

	public GoodsClassSearchAdapter(Context mContext,
			List<GoodsGeneralModel> listData) {
		super(mContext, listData);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if(convertView == null){
			convertView = View.inflate(mContext,R.layout.item_adapter_goods_search, null);
			holder = new ViewHolder();
			holder.cb_whetherSelect = (CheckBox) convertView.findViewById(R.id.cb_whetherSelect);
			holder.iv_productImage = (ImageView) convertView.findViewById(R.id.shoping_goods_image);
			holder.tv_productTitle = (TextView) convertView.findViewById(R.id.shoping_goods_title);
			holder.tv_productPrice = (TextView) convertView.findViewById(R.id.shoping_goods_price);
			holder.tv_saleCount = (TextView) convertView.findViewById(R.id.tv_saleCount);
			holder.iv_addToShopCart = (ImageView) convertView.findViewById(R.id.iv_addToShopCart);
			holder.tv_notStoneCount = (TextView) convertView.findViewById(R.id.tv_notStoneCount);

			convertView.setTag(holder);
		}else{
			holder = (ViewHolder)convertView.getTag();
		}
		final GoodsGeneralModel productModel = listData.get(position);	
		if(productModel != null){
			holder.cb_whetherSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					productModel.whetherSelect = isChecked;
				}
			});
			holder.cb_whetherSelect.setChecked(productModel.whetherSelect);
			ImagerLoaderUtil.getInstance(mContext).displayMyImage(productModel.getImgUrl(), holder.iv_productImage); 
			holder.tv_productTitle.setText(productModel.getGoodsName());
			holder.tv_productPrice.setText("￥"+productModel.getMinPrice());
			holder.tv_saleCount.setText(productModel.getSaledCount() + "人已付款");
			try {
				if(0 == productModel.getDefaultGoodsPackage().getDefaultPackageColor().getStoneCount()){
                    holder.tv_notStoneCount.setVisibility(View.VISIBLE);
                    holder.iv_addToShopCart.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            ToastUtils.show(mContext,"操作失败,该商品库存数量不足");
                        }
                    });
                }else {
                    holder.tv_notStoneCount.setVisibility(View.INVISIBLE);
                    holder.iv_addToShopCart.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            addGoodsToShoppingCart(productModel);
                        }
                    });
                }
			} catch (Exception e) {
				holder.tv_notStoneCount.setVisibility(View.VISIBLE);
				holder.iv_addToShopCart.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						ToastUtils.show(mContext,"操作失败,该商品库存数量不足");
					}
				});
			}

		}
	
		return convertView;
	}

	/** 添加JSON数据 到购物车中 **/
	public void addGoodsToShoppingCart(GoodsGeneralModel productModel) {
			String methodName = InformationCodeUtil.methodNameAddShopCart;
			SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
			requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
			requestSoapObject.addProperty("ProductID", productModel.getDjLsh());
			requestSoapObject.addProperty("PackageID",  productModel.getDefaultGoodsPackage().getDjLsh());
			requestSoapObject.addProperty("PackageColorID", productModel.getDefaultGoodsPackage().getDefaultPackageColor().getDjLsh());
			requestSoapObject.addProperty("Quantity", 1);
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
					((Activity) mContext, this, requestSoapObject , methodName,productModel);
			connectCustomServiceAsyncTask.execute();
	}


	/** 添加JSON数据 到购物本地缓存中 **/
	public void addGoodsToCacheData(GoodsGeneralModel productModel) {
		//把当前选中的套餐添加到购物车数据中
		GoodsGeneralModel tempGoods = productModel;
		GoodsPackageModel tempPackage = productModel.getDefaultGoodsPackage();
		GoodsPackageColorModel tempPackageColor = productModel.getDefaultGoodsPackage().getDefaultPackageColor();
		ShoppingCartChildGoodsModel currentChildGoodsModel = new ShoppingCartChildGoodsModel
				( 0, tempGoods.getDjLsh(),tempGoods.getGoodsName(), tempGoods.getImgUrl()
						,tempPackage.getDjLsh(),tempPackage.getPackageName()
						,tempPackageColor.getDjLsh(),tempPackageColor.getColorName()
						,1
						,tempPackageColor.getPrice(),tempPackageColor.getTaxPrice()
						,tempPackageColor.getFlyCoin(),tempPackageColor.getTaxFlyCoin());
		List<ShoppingCartChildGoodsModel> tempList = new ArrayList<ShoppingCartChildGoodsModel>();
		tempList.add(currentChildGoodsModel);

		ShoppingCartParentGoodsModel currentParentGoodsModel = new ShoppingCartParentGoodsModel();
		currentParentGoodsModel.setShopName(productModel.getShopName());
		currentParentGoodsModel.setShopID(productModel.getShopID());
		currentParentGoodsModel.setShoppingCarts(tempList);

		PreferencesUtilMy.addGoodsToShopCart(mContext,currentParentGoodsModel);
	}


	@Override
	public void connectServiceSuccessful(String returnString, String methodName, GoodsGeneralModel state, boolean whetherRefresh) {
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
					addGoodsToCacheData(state);
				}else{
					ToastUtils.show(mContext,mJSONResultMsgModel.getMsg());
				}
			}
		}
	}

	@Override
	public void connectServiceFailed(String methodName, GoodsGeneralModel state, boolean whetherRefresh) {
		ToastUtils.show(mContext,"网络异常，加入购物车失败");
	}

	@Override
	public void connectServiceCancelled(String returnString, String methodName, GoodsGeneralModel state, boolean whetherRefresh) {
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
					addGoodsToCacheData(state);
				}else{
					ToastUtils.show(mContext,mJSONResultMsgModel.getMsg());
				}
			}
		}
	}

	private class ViewHolder {
		/**商品是否选中*/
		private CheckBox cb_whetherSelect;
		/**商品图片*/
		private ImageView iv_productImage;
		/**商品标题**/
		private TextView tv_productTitle;
		/**商品价格**/
		private TextView tv_productPrice;
		/**销售数量**/
		private TextView tv_saleCount;
		/**当前套餐无货**/
		private TextView tv_notStoneCount;
		/**添加商品到购物车**/
		private ImageView iv_addToShopCart;
	}

}
