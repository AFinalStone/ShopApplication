package com.shuimunianhua.xianglixiangqin.model;

import java.io.Serializable;
import java.util.List;

/***
 * 套餐
 * @author SHI
 * 2016年4月20日 19:04:49
 */
public class GoodsPackageModel implements Serializable{
	/**单据流水号**/
	private int DjLsh;
	/**商品编号**/
	private int GoodsID;
	/**图片路径**/
	private String ImgUrl;
	/**套餐名称**/
	private String PackageName;
	/**当前套餐颜色集合**/
	private List<GoodsPackageColorModel> PackageColorJsonList ;	
	/**默认返回的套餐颜色*/
	private GoodsPackageColorModel DefaultPackageColor ;	
	/**是否被选中**/
	private boolean whetherSelect;
	public int getDjLsh() {
		return DjLsh;
	}
	public void setDjLsh(int djLsh) {
		DjLsh = djLsh;
	}
	public int getGoodsID() {
		return GoodsID;
	}
	public void setGoodsID(int goodsID) {
		GoodsID = goodsID;
	}
	public String getPackageName() {
		return PackageName;
	}
	public void setPackageName(String packageName) {
		PackageName = packageName;
	}
	public String getImgUrl() {
		return ImgUrl;
	}
	public void setImgUrl(String imgUrl) {
		ImgUrl = imgUrl;
	}
	public boolean getWhetherSelect() {
		return whetherSelect;
	}
	public void setWhetherSelect(boolean whetherSelect) {
		this.whetherSelect = whetherSelect;
	}
	public List<GoodsPackageColorModel> getPackageColorJsonList() {
		return PackageColorJsonList;
	}
	public void setPackageColorJsonList(
			List<GoodsPackageColorModel> packageColorJsonList) {
		PackageColorJsonList = packageColorJsonList;
	}
	public GoodsPackageColorModel getDefaultPackageColor() {
		return DefaultPackageColor;
	}
	public void setDefaultPackageColor(GoodsPackageColorModel defaultPackageColor) {
		DefaultPackageColor = defaultPackageColor;
	}
}

