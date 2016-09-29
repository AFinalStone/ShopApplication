package com.shuimunianhua.xianglixiangqin.model;
/**
 * 店铺首页Model对象
 * @author SHI
 * 2016年3月24日 13:55:10
 */
public class ShopInfoModel {
	
	//店铺首页字段
	/**店铺活动集合**/
	public String Acts;
	/**图片广告 列表**/
	public String AdvList ;
	/**全部商品数量**/
	public int CountAll ;
	/**店铺编号**/
	public int DjLsh ;
	/**Logo图片**/
	public String Logo ;
	/**模块集合**/
	public String Modules;
	/**店铺通知集合**/
	public String Notices;
	/**店铺名称**/
	public String ShopName ;
	/**店铺用户ID**/
	public int ShopUserID ;

	public String getActs() {
		return Acts;
	}

	public void setActs(String acts) {
		Acts = acts;
	}

	public String getAdvList() {
		return AdvList;
	}

	public void setAdvList(String advList) {
		AdvList = advList;
	}

	public int getCountAll() {
		return CountAll;
	}

	public void setCountAll(int countAll) {
		CountAll = countAll;
	}

	public String getLogo() {
		return Logo;
	}

	public void setLogo(String logo) {
		Logo = logo;
	}

	public String getModules() {
		return Modules;
	}

	public void setModules(String modules) {
		Modules = modules;
	}

	public String getNotices() {
		return Notices;
	}

	public void setNotices(String notices) {
		Notices = notices;
	}

	public int getShopUserID() {
		return ShopUserID;
	}

	public void setShopUserID(int shopUserID) {
		ShopUserID = shopUserID;
	}

	public int getDjLsh() {
		return DjLsh;
	}

	public void setDjLsh(int djLsh) {
		DjLsh = djLsh;
	}

	public String getShopName() {
		return ShopName;
	}

	public void setShopName(String shopName) {
		ShopName = shopName;
	}

}





