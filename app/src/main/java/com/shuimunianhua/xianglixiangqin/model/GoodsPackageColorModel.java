package com.shuimunianhua.xianglixiangqin.model;

import java.io.Serializable;

/**
 * 套餐颜色对象
 * @author SHI
 * 2016年4月20日 19:03:12
 */

public class GoodsPackageColorModel implements Serializable{
	/**颜色图片**/
	private String ColorImgUrl;
	/**颜色名称**/
	private String ColorName;
	/**核心价格**/
	private double CorePrice;
	/**含税核心价格**/
	private double CoreTaxPrice;
	/**单据流水号**/
	private int DjLsh;
	/**飞币数**/
	private int FlyCoin;
	/**价格**/
	private double Price;
	/**已售数量**/
	private double SaledCount;
	/**库存数量**/
	private long StoneCount;
	/**含税飞币数**/
	private int TaxFlyCoin;
	/**含税价格**/
	private double TaxPrice;
	/**是否选中**/
	private boolean whetherSelect;
	
	public String getColorImgUrl() {
		return ColorImgUrl;
	}
	public void setColorImgUrl(String colorImgUrl) {
		ColorImgUrl = colorImgUrl;
	}
	public String getColorName() {
		return ColorName;
	}
	public void setColorName(String colorName) {
		ColorName = colorName;
	}
	public double getCorePrice() {
		return CorePrice;
	}
	public void setCorePrice(double corePrice) {
		CorePrice = corePrice;
	}
	public double getCoreTaxPrice() {
		return CoreTaxPrice;
	}
	public void setCoreTaxPrice(double coreTaxPrice) {
		CoreTaxPrice = coreTaxPrice;
	}
	public int getDjLsh() {
		return DjLsh;
	}
	public void setDjLsh(int djLsh) {
		DjLsh = djLsh;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public double getSaledCount() {
		return SaledCount;
	}
	public void setSaledCount(double saledCount) {
		SaledCount = saledCount;
	}
	public long getStoneCount() {
		return StoneCount;
	}
	public void setStoneCount(long stoneCount) {
		StoneCount = stoneCount;
	}
	public double getTaxPrice() {
		return TaxPrice;
	}
	public void setTaxPrice(double taxPrice) {
		TaxPrice = taxPrice;
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
	public int getTaxFlyCoin() {
		return TaxFlyCoin;
	}
	public void setTaxFlyCoin(int taxFlyCoin) {
		TaxFlyCoin = taxFlyCoin;
	}

}
