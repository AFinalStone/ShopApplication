package com.shuimunianhua.xianglixiangqin.model;

/***
 * 商品分类 - 模型类
 * @author ZHU
 *
 */
public class GoodsClassModel {
	/**分类名称**/
	private String ClassName;
	/**图标**/
	private String ImgUrl;
	/**自动编号**/
	private int DjLsh;

	public void setClassName(String ClassName){
		this.ClassName = ClassName;
	}
	public String getClassName(){
		return this.ClassName;
	}
	public String getImgUrl() {
		return ImgUrl;
	}
	public void setImgUrl(String imgUrl) {
		ImgUrl = imgUrl;
	}
	public int getDjLsh() {
		return DjLsh;
	}
	public void setDjLsh(int djLsh) {
		DjLsh = djLsh;
	}

}
