package com.shuimunianhua.xianglixiangqin.model;

import java.util.List;

/**
 *  活动商品模型
 * @author SHI
 * 2016年5月9日 16:37:28
 */
public class GoodsSportModel{

    /**原来的价格**/
	private double OriginalPrice;//
    /**商家电话**/
    private String ShopPhoneNum;//
    /**商品图片列表，可多张**/
    private List<String> Images;//
    /**商品所属商铺名称**/
    private String ShopName;
    /**团购商品 是否成团**/
    private boolean IsGrouped;
	/**代理ID**/
	private int AgentID;//
	/**开始时间**/
	private String BeginTime;//
	/**结束时间**/
	private String EndTime;//
	/**当前时间**/
	private String PresentTime;//
	/**所属分类ID**/
	private int ClassID;//
	/**商品编号**/
	private int DjLsh;//
	/**允许使用飞币数量**/
	private int FlyCoin;//
	/**商品详情**/
	private String GoodsDesc;//
	/**商品名称**/
	private String GoodsName;//
	/**商品参数**/
	private String GoodsSpec;//
	/**活动是否正在进行**/
	private boolean IsRunning;//
	/**包装售后**/
	private String Package;//
	/**商品价格**/
	private double Price;//

	/**商品所属店铺ID**/
	private int ShopID;//
	/**发布商品者ID**/
	private int UserID;//
	/**用户限购量**/
	private int UserQuantity;//
	/**商品活动ID**/
	private int PlatformActionID;//
	/**商品活动类型**/
	private int PlatformActionType;

	/**限时抢购    当前抢购量**/
	private int PurchaseNum;//
	/**团购商品    当前成团数**/
	private int JoinNum;//
	/**整点秒杀 限时抢购 活动商品参与数量**/
	private int Quantity;//
	/**团购商品 最小成团数**/
	private int MinQuantity;//
	/**团购商品 最大成团数**/
	private int MaxQuantity;
	
	public int getAgentID() {
		return AgentID;
	}
	public void setAgentID(int agentID) {
		AgentID = agentID;
	}
	public String getBeginTime() {
		return BeginTime;
	}
	public void setBeginTime(String beginTime) {
		BeginTime = beginTime;
	}
	public int getClassID() {
		return ClassID;
	}
	public void setClassID(int classID) {
		ClassID = classID;
	}
	public int getDjLsh() {
		return DjLsh;
	}
	public void setDjLsh(int djLsh) {
		DjLsh = djLsh;
	}
	public String getEndTime() {
		return EndTime;
	}
	public void setEndTime(String endTime) {
		EndTime = endTime;
	}
	public int getFlyCoin() {
		return FlyCoin;
	}
	public void setFlyCoin(int flyCoin) {
		FlyCoin = flyCoin;
	}
	public String getGoodsDesc() {
		return GoodsDesc;
	}
	public void setGoodsDesc(String goodsDesc) {
		GoodsDesc = goodsDesc;
	}
	public String getGoodsSpec() {
		return GoodsSpec;
	}
	public void setGoodsSpec(String goodsSpec) {
		GoodsSpec = goodsSpec;
	}
	public boolean isIsRunning() {
		return IsRunning;
	}
	public void setIsRunning(boolean isRunning) {
		IsRunning = isRunning;
	}
	public String getPackage() {
		return Package;
	}
	public void setPackage(String package1) {
		Package = package1;
	}
	public int getPlatformActionID() {
		return PlatformActionID;
	}
	public void setPlatformActionID(int platformActionID) {
		PlatformActionID = platformActionID;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public int getQuantity() {
		return Quantity;
	}
	public void setQuantity(int quantity) {
		Quantity = quantity;
	}
	public int getShopID() {
		return ShopID;
	}
	public void setShopID(int shopID) {
		ShopID = shopID;
	}
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public int getUserQuantity() {
		return UserQuantity;
	}
	public void setUserQuantity(int userQuantity) {
		UserQuantity = userQuantity;
	}
	public String getGoodsName() {
		return GoodsName;
	}
	public void setGoodsName(String goodsName) {
		GoodsName = goodsName;
	}
	
	public double getOriginalPrice() {
		return OriginalPrice;
	}
	public void setOriginalPrice(double originalPrice) {
		OriginalPrice = originalPrice;
	}
	public String getShopPhoneNum() {
		return ShopPhoneNum;
	}
	public void setShopPhoneNum(String shopPhoneNum) {
		ShopPhoneNum = shopPhoneNum;
	}
	public List<String> getImages() {
		return Images;
	}
	public void setImages(List<String> images) {
		Images = images;
	}
	public String getShopName() {
		return ShopName;
	}
	public void setShopName(String shopName) {
		ShopName = shopName;
	}
	public int getMinQuantity() {
		return MinQuantity;
	}
	public void setMinQuantity(int minQuantity) {
		MinQuantity = minQuantity;
	}
	public int getMaxQuantity() {
		return MaxQuantity;
	}
	public void setMaxQuantity(int maxQuantity) {
		MaxQuantity = maxQuantity;
	}
	public boolean isIsGrouped() {
		return IsGrouped;
	}
	public void setIsGrouped(boolean isGrouped) {
		IsGrouped = isGrouped;
	}
	public String getPresentTime() {
		return PresentTime;
	}
	public void setPresentTime(String presentTime) {
		PresentTime = presentTime;
	}
	public int getPlatformActionType() {
		return PlatformActionType;
	}
	public void setPlatformActionType(int platformActionType) {
		PlatformActionType = platformActionType;
	}
	public int getJoinNum() {
		return JoinNum;
	}
	public void setJoinNum(int joinNum) {
		JoinNum = joinNum;
	}
	public int getPurchaseNum() {
		return PurchaseNum;
	}
	public void setPurchaseNum(int purchaseNum) {
		PurchaseNum = purchaseNum;
	}
	
}
