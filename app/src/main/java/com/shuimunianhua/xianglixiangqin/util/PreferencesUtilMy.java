package com.shuimunianhua.xianglixiangqin.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuimunianhua.xianglixiangqin.model.CustomModel;
import com.shuimunianhua.xianglixiangqin.model.ShoppingCartChildGoodsModel;
import com.shuimunianhua.xianglixiangqin.model.ShoppingCartParentGoodsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 工具类 负责从SP中操作用户和购物车信息
 * @author Administrator
 *
 */
public class PreferencesUtilMy {

	/**当前登录用户**/
	private final static String KeyUserInfo = InformationCodeUtil.KeyUserInfo;

	/**当前购物车内商品数据**/
	private final static String KeyShoppingCartData = InformationCodeUtil.KeyShoppingCartData;

	/**保存当前用户信息到SP文件中**/
	public static void saveCustomModel(Context mContext,CustomModel mCustomModel){
		Gson gson = new Gson();
		String jsonString = gson.toJson(mCustomModel);
		PreferencesUtil.putString(mContext, KeyUserInfo, jsonString);
	}
	
	/**从SP文件中获取当前登录用户信息**/
	public static CustomModel getCustomModel(Context mContext){
		CustomModel mCustomModel = null;
		try {
			Gson gson = new Gson();
			String jsonString = PreferencesUtil.getString(mContext, KeyUserInfo);
			mCustomModel = gson.fromJson(jsonString,CustomModel.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mCustomModel;
	}



	/**从SP文件中清空当前登录用户信息**/
	public static void clearCustomModel(Context mContext){
		try {
			CustomModel mCustomModel = getCustomModel(mContext);
			CustomModel mCustomModelSp = new CustomModel();
			mCustomModelSp.setPhoneNum(mCustomModel.getPhoneNum());
			saveCustomModel(mContext, mCustomModelSp);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**从SP文件中清空当前购物车数据**/
	public static void clearShopCartAllGoods(Context mContext){
		PreferencesUtil.putString(mContext, KeyShoppingCartData, null);
	}

	/**从SP文件中获取当前购物车数据**/
	public static List<ShoppingCartParentGoodsModel> getShopCartAllGoods(Context mContext){

		List<ShoppingCartParentGoodsModel> listShoppintCartData = null;
		Gson gson = new Gson();
		try {
			String jsonString = PreferencesUtil.getString(mContext, KeyShoppingCartData);
			listShoppintCartData = gson.fromJson(jsonString,new TypeToken<List<ShoppingCartParentGoodsModel>>(){}.getType());
		} catch (Exception e) {
		}
		if(listShoppintCartData == null){
			listShoppintCartData = new ArrayList<ShoppingCartParentGoodsModel>();
		}
		return listShoppintCartData;
	}

	/**保存当前购物车数据到SP文件中**/
	public static void saveShopCartAllGoods(Context mContext,List<ShoppingCartParentGoodsModel> listData){
		Gson gson = new Gson();
		String jsonString = gson.toJson(listData);
		PreferencesUtil.putString(mContext, KeyShoppingCartData, jsonString);
	}

	/**添加单个商品到购物车Sp文件中**/
	public static void addGoodsToShopCart(Context mContext,ShoppingCartParentGoodsModel parentGoodsModelWillToShopCart ){

		//获取准备加入到购物车的商品详情
		ShoppingCartChildGoodsModel childGoodsModelWillToShopCart = parentGoodsModelWillToShopCart.getShoppingCarts().get(0);
		//获取当前购物车数据
		List<ShoppingCartParentGoodsModel> listShoppintCartData = getShopCartAllGoods(mContext);
		//当前购物车是否包含这个店铺的商品
		int position = listShoppintCartData.indexOf(parentGoodsModelWillToShopCart);
		if(position == -1){
			//如果不包含
			listShoppintCartData.add(parentGoodsModelWillToShopCart);
		}else{
			//如果包含
			ShoppingCartParentGoodsModel parentGoods = listShoppintCartData.get(position);
			//继续查看是否包含当前商品
			position = parentGoods.getShoppingCarts().indexOf(childGoodsModelWillToShopCart);
			if(position == -1){
				parentGoods.getShoppingCarts().add(childGoodsModelWillToShopCart);
			}else{
				ShoppingCartChildGoodsModel selectChildGoodsModel = parentGoods.getShoppingCarts().get(position);
				int newQuantity = selectChildGoodsModel.getQuantity()+childGoodsModelWillToShopCart.getQuantity();
				selectChildGoodsModel.setQuantity(newQuantity);
			}
		}
		saveShopCartAllGoods(mContext,listShoppintCartData);
	}
	/**批量添加商品到购物车Sp文件中**/
	public static void addGoodsToShopCart(Context mContext,List<ShoppingCartParentGoodsModel> listData){

		//获取当前购物车数据
		List<ShoppingCartParentGoodsModel> listShoppintCartData = getShopCartAllGoods(mContext);

		for (int i=0; i<listData.size(); i++){
			ShoppingCartParentGoodsModel parentGoodsModelWillToShopCart = listData.get(i);
			//获取准备加入到购物车的商品详情
			ShoppingCartChildGoodsModel childGoodsModelWillToShopCart = parentGoodsModelWillToShopCart.getShoppingCarts().get(0);
			//当前购物车是否包含这个店铺的商品
			int position = listShoppintCartData.indexOf(parentGoodsModelWillToShopCart);
			if(position == -1){
				//如果不包含
				listShoppintCartData.add(parentGoodsModelWillToShopCart);
			}else{
				//如果包含
				ShoppingCartParentGoodsModel parentGoods = listShoppintCartData.get(position);
				//继续查看是否包含当前商品
				position = parentGoods.getShoppingCarts().indexOf(childGoodsModelWillToShopCart);
				if(position == -1){
					parentGoods.getShoppingCarts().add(childGoodsModelWillToShopCart);
				}else{
					ShoppingCartChildGoodsModel selectChildGoodsModel = parentGoods.getShoppingCarts().get(position);
					int newQuantity = selectChildGoodsModel.getQuantity()+childGoodsModelWillToShopCart.getQuantity();
					selectChildGoodsModel.setQuantity(newQuantity);
				}
			}
		}

		saveShopCartAllGoods(mContext,listShoppintCartData);
	}

	/**
	 * 获取购物车中当前商品数量
	 **/
	public static int getShopCartAllGoodsNum(Context mContext) {

		Gson gson = new Gson();
		// 当前购物车内套餐数据
		List<ShoppingCartParentGoodsModel> listShoppintCartData = getShopCartAllGoods(mContext);

		int totoalShopCartAllGoodsNumNum = 0;
		for (int i=0; i<listShoppintCartData.size(); i++){
			totoalShopCartAllGoodsNumNum += listShoppintCartData.get(i).getShoppingCarts().size();
		}
		return totoalShopCartAllGoodsNumNum;
	}


//	/**获取SP文件中购物车内所有商品信息**/
//	public static List<SelectPackageModel> getShopCartAllGoods(Context mContext){
//		List<SelectPackageModel> listShoppintCartData = null;
//		try {
//			Gson gson = new Gson();
//			String jsonString = PreferencesUtil.getString(mContext, KeyShoppingCartData);
//			listShoppintCartData = gson.fromJson(jsonString,new TypeToken<List<SelectPackageModel>>(){}.getType());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return listShoppintCartData;
//	}
//
//	/**更新SP文件中购物车内商品信息**/
//	public static void updateShopCartAllGoods(Context mContext,List<SelectPackageModel> listShoppintCartData){
//		Gson gson = new Gson();
//		String jsonString =  gson.toJson(listShoppintCartData);
//		PreferencesUtil.putString(mContext, InformationCodeUtil.KeyShoppingCartData, jsonString);
//	}

}
