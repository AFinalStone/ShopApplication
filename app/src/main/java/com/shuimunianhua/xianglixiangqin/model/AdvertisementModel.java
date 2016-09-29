package com.shuimunianhua.xianglixiangqin.model;

/**
 * 广告模型类
 * @author SHI
 * 2016年4月7日 14:01:49
 */
public class AdvertisementModel {
	
	private String DjLsh;
	/**名称**/
	private String Title ;
	/**图片网址**/
	private String ImgUrl ;
	/**链接地址**/
	private String LinkUrl ;
	
	public String getDjLsh() {
		return DjLsh;
	}
	public void setDjLsh(String djLsh) {
		DjLsh = djLsh;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	
	public String getImgUrl() {
		return ImgUrl;
	}
	public void setImgUrl(String imgUrl) {
		ImgUrl = imgUrl;
	}
	public String getLinkUrl() {
		return LinkUrl;
	}
	public void setLinkUrl(String linkUrl) {
		LinkUrl = linkUrl;
	}
	
}
