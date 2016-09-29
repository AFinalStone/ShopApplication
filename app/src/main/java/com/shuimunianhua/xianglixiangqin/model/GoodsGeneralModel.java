package com.shuimunianhua.xianglixiangqin.model;

import java.io.Serializable;
import java.util.List;

/***
 * 普通商品模型
 * @author SHI
 * 2016年5月9日 16:37:41
 */
public class GoodsGeneralModel implements Serializable{
	/**代理加价**/
	private double AgentAddMoney;
	private int AgentID;
	
	private double AvgStars;

	/**分类id**/
	private int ClassID;
	/**创建时间**/
	private String CreateTime;
	/**允许使用的飞币数量**/
	private int FlyCoin;
	/**延迟付款的天数**/
	private int DelayPayDays;
	/**商品ID**/
	private int DjLsh;
	/**代理商ID**/
	private int GoodsAgentID;
	/**商品描述**/
	private String GoodsDesc;
	/**商品名称**/
	private String GoodsName;
	/**商品规格**/
	private String GoodsSpec;
	/**副标题**/
	private String GoodsSubName;
	/**是否热销**/
	private boolean IfHot;
	/**是否显示**/
	private boolean IfShow;
	/**图片列表**/
	private List<String> Images;	
	/**图片名称**/
	private String ImgName;	
	/***图片路径**/
//	private String ImgPath;
	private String ImgUrl;
	private int IsAgented;
	private int MaxCorePrice;
	private int MinCorePrice;
	/**最大价格**/
	private double MaxPrice;
	/**最小价格**/
	private double MinPrice;
	/***包装售后**/
	private String Package;
	/**真实名称**/
	private String RealName;
	/**已售数量**/
	private int SaledCount;
	/***店铺编号**/
	private int ShopID;
	/***店铺名称**/
	private String ShopName;
	/**商家电话**/
	private String ShopPhoneNum;
	/**更新时间**/
	private String UpdateTime;
	/***用户编号**/
	private int UserID;
	/**用户名**/
	private String UserName;
	/**是否是微店特价商品(主要是为了我的代理商品中可以设置微店特价商品)**/
	private boolean IfTj;
	/**热卖**/
	private boolean RmHot;
	private boolean TjHot;
	private boolean JpHot;
	/**是否选中**/
	public boolean whetherSelect;
	
	//为了兼容活动商品而加的三个字段
	/**商品活动ID**/
	private int PlatformActionID;
	/**商品活动类型**/
	private int PlatformActionType;
	/**用户限购量**/
	private int UserQuantity;
	/**活动商品的库存数量**/
	private int StoneCount;
	//为了能够在商品搜索界面直接把商品加入购物车而添加的字段
	/**如果服务端在商品信息中返回默认套餐信息，则返回在这个字段中**/
	private GoodsPackageModel DefaultGoodsPackage;



	public double getAgentAddMoney() {
		return AgentAddMoney;
	}
	public void setAgentAddMoney(double agentAddMoney) {
		AgentAddMoney = agentAddMoney;
	}
	public int getAgentID() {
		return AgentID;
	}
	public void setAgentID(int agentID) {
		AgentID = agentID;
	}
	public double getAvgStars() {
		return AvgStars;
	}
	public void setAvgStars(double avgStars) {
		AvgStars = avgStars;
	}
	public int getClassID() {
		return ClassID;
	}
	public void setClassID(int classID) {
		ClassID = classID;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public int getFlyCoin() {
		return FlyCoin;
	}
	public void setFlyCoin(int flyCoin) {
		FlyCoin = flyCoin;
	}
	public int getDelayPayDays() {
		return DelayPayDays;
	}
	public void setDelayPayDays(int delayPayDays) {
		DelayPayDays = delayPayDays;
	}
	public int getDjLsh() {
		return DjLsh;
	}
	public void setDjLsh(int djLsh) {
		DjLsh = djLsh;
	}
	public int getGoodsAgentID() {
		return GoodsAgentID;
	}
	public void setGoodsAgentID(int goodsAgentID) {
		GoodsAgentID = goodsAgentID;
	}
	public String getGoodsDesc() {
		return GoodsDesc;
	}
	public void setGoodsDesc(String goodsDesc) {
		GoodsDesc = goodsDesc;
	}
	public String getGoodsName() {
		return GoodsName;
	}
	public void setGoodsName(String goodsName) {
		GoodsName = goodsName;
	}
	public String getGoodsSpec() {
		return GoodsSpec;
	}
	public void setGoodsSpec(String goodsSpec) {
		GoodsSpec = goodsSpec;
	}
	public String getGoodsSubName() {
		return GoodsSubName;
	}
	public void setGoodsSubName(String goodsSubName) {
		GoodsSubName = goodsSubName;
	}
	public boolean isIfHot() {
		return IfHot;
	}
	public void setIfHot(boolean ifHot) {
		IfHot = ifHot;
	}
	public boolean isIfShow() {
		return IfShow;
	}
	public void setIfShow(boolean ifShow) {
		IfShow = ifShow;
	}
	public List<String> getImages() {
		return Images;
	}
	public void setImages(List<String> images) {
		Images = images;
	}
	public String getImgName() {
		return ImgName;
	}
	public void setImgName(String imgName) {
		ImgName = imgName;
	}
//	public String getImgPath() {
//		return ImgPath;
//	}
//	public void setImgPath(String imgPath) {
//		ImgPath = imgPath;
//	}
	public String getImgUrl() {
		return ImgUrl;
	}
	public void setImgUrl(String imgUrl) {
		ImgUrl = imgUrl;
	}
	public int getIsAgented() {
		return IsAgented;
	}
	public void setIsAgented(int isAgented) {
		IsAgented = isAgented;
	}
	public int getMaxCorePrice() {
		return MaxCorePrice;
	}
	public void setMaxCorePrice(int maxCorePrice) {
		MaxCorePrice = maxCorePrice;
	}
	public int getMinCorePrice() {
		return MinCorePrice;
	}
	public void setMinCorePrice(int minCorePrice) {
		MinCorePrice = minCorePrice;
	}
	public double getMaxPrice() {
		return MaxPrice;
	}
	public void setMaxPrice(double maxPrice) {
		MaxPrice = maxPrice;
	}
	public double getMinPrice() {
		return MinPrice;
	}
	public void setMinPrice(double minPrice) {
		MinPrice = minPrice;
	}
	public String getPackage() {
		return Package;
	}
	public void setPackage(String package1) {
		Package = package1;
	}
	public String getRealName() {
		return RealName;
	}
	public void setRealName(String realName) {
		RealName = realName;
	}
	public int getSaledCount() {
		return SaledCount;
	}
	public void setSaledCount(int saledCount) {
		SaledCount = saledCount;
	}
	public int getShopID() {
		return ShopID;
	}
	public void setShopID(int shopID) {
		ShopID = shopID;
	}
	public String getShopName() {
		return ShopName;
	}
	public void setShopName(String shopName) {
		ShopName = shopName;
	}
	public String getShopPhoneNum() {
		return ShopPhoneNum;
	}
	public void setShopPhoneNum(String shopPhoneNum) {
		ShopPhoneNum = shopPhoneNum;
	}
	public String getUpdateTime() {
		return UpdateTime;
	}
	public void setUpdateTime(String updateTime) {
		UpdateTime = updateTime;
	}
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public boolean isRmHot() {
		return RmHot;
	}
	public void setRmHot(boolean rmHot) {
		RmHot = rmHot;
	}
	public boolean isTjHot() {
		return TjHot;
	}
	public void setTjHot(boolean tjHot) {
		TjHot = tjHot;
	}
	public boolean isJpHot() {
		return JpHot;
	}
	public void setJpHot(boolean jpHot) {
		JpHot = jpHot;
	}
	public boolean isWhetherSelect() {
		return whetherSelect;
	}
	public void setWhetherSelect(boolean whetherSelect) {
		this.whetherSelect = whetherSelect;
	}
	public int getPlatformActionID() {
		return PlatformActionID;
	}
	public void setPlatformActionID(int platformActionID) {
		PlatformActionID = platformActionID;
	}
	public int getPlatformActionType() {
		return PlatformActionType;
	}
	public void setPlatformActionType(int platformActionType) {
		PlatformActionType = platformActionType;
	}
	public int getUserQuantity() {
		return UserQuantity;
	}
	public void setUserQuantity(int userQuantity) {
		UserQuantity = userQuantity;
	}
	public GoodsPackageModel getDefaultGoodsPackage() {
		return DefaultGoodsPackage;
	}
	public void setDefaultGoodsPackage(GoodsPackageModel defaultGoodsPackage) {
		DefaultGoodsPackage = defaultGoodsPackage;
	}
	public boolean isIfTj() {
		return IfTj;
	}
	public void setIfTj(boolean ifTj) {
		IfTj = ifTj;
	}

	public int getStoneCount() {
		return StoneCount;
	}

	public void setStoneCount(int stoneCount) {
		StoneCount = stoneCount;
	}
}
