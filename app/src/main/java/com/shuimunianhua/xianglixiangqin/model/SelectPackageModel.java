package com.shuimunianhua.xianglixiangqin.model;

import java.io.Serializable;

/***
 * 选中套餐信息
 * @author SHI
 * 2016-2-18 15:42:24
 */
public class SelectPackageModel implements Serializable{
	/**套餐颜色数据**/
	private GoodsPackageColorModel mPackageColorModel;
	/**套餐数据**/
	private GoodsPackageModel mPackages;
	/**购买数量**/
	private int purchaseQuantity;
	/**商品信息**/
	private GoodsGeneralModel mProductModel;
	/**商品选中状态**/
	public boolean whetherSelect = false;
	public SelectPackageModel(){}
	
	public SelectPackageModel(GoodsPackageColorModel mPackageColorModel,
			GoodsPackageModel mPackages,GoodsGeneralModel mProductModel, int purchaseQuantity) {
		super();
		this.mPackageColorModel = mPackageColorModel;
		this.mPackages = mPackages;
		this.mProductModel = mProductModel;
		this.purchaseQuantity = purchaseQuantity;
	}
	
	public GoodsGeneralModel getmProductModel() {
		return mProductModel;
	}

	public void setmProductModel(GoodsGeneralModel mProductModel) {
		this.mProductModel = mProductModel;
	}

	public GoodsPackageColorModel getmPackageColorModel() {
		return mPackageColorModel;
	}
	public void setmPackageColorModel(GoodsPackageColorModel mPackageColorModel) {
		this.mPackageColorModel = mPackageColorModel;
	}
	public GoodsPackageModel getmPackages() {
		return mPackages;
	}
	public void setmPackages(GoodsPackageModel mPackages) {
		this.mPackages = mPackages;
	}
	public int getPurchaseQuantity() {
		return purchaseQuantity;
	}
	public void setPurchaseQuantity(int purchaseQuantity) {
		this.purchaseQuantity = purchaseQuantity;
	}

	@Override
	public boolean equals(Object o) {
		if( o instanceof SelectPackageModel && o != null){
			
			SelectPackageModel tempPackageModel = (SelectPackageModel) o;
			String name = tempPackageModel.getmProductModel().getGoodsName();
			String packageName = tempPackageModel.getmPackages().getPackageName();
			String color = tempPackageModel.getmPackageColorModel().getColorName();
			
			if(mProductModel.getGoodsName().equals(name) 
				&& mPackages.getPackageName().equals(packageName) 
				&& mPackageColorModel.getColorName().equals(color)){
				return true;
			}
		}
		return false;
	}


	
}
