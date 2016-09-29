package com.shuimunianhua.xianglixiangqin.model;


/**
 * @action 会员模型类
 * @author SHI
 * @date  2015-8-18 下午11:37:30
 */
public class CustomModel {
	/**用户ID**/
	private int DjLsh; 
	/**电子邮箱**/
	private String Email ;
	/**头像**/
	private String ImgUrl ;
	/**积分**/
	private Double Integral ;
	/**默认定位省份编码**/
	private String LocProCode;
	/**默认定位城市站编码**/
	private String LocCityCode;
	/**默认定位城市站名称**/
	private String LocSiteName;
	/**交互唯一码**/
	private String OpenKey ;
	/**密码**/
	private String PassWord ;
	/**手机号码**/
	private String PhoneNum;
	/**真实姓名**/
	private String RealName;
	/**经销商代理商分类标识**/
	private int RoleID;
	/**融云密钥*/
	private String RongCloudToken;
	/**性别**/
	private String Sex;
	/**门店编号**/
	private int ShopID;
	/**店铺名称**/
	private String ShopName;
	/**店铺用户ID**/
	private int ShopUserID;
	/**用户名**/
	private String UserName ;
	/**微信头像Imageurl**/
	private String WeChatImgUrl;
	/**当前登录店铺ID**/
	private int loginShopID;
//	/**当前登录店铺店主用户ID**/
//	private int loginShopUserID;
//	/**当前登录店铺店名称**/
//	private String loginShopName;

	public String getRongCloudToken() {
		return RongCloudToken;
	}
	public void setRongCloudToken(String rongCloudToken) {
		RongCloudToken = rongCloudToken;
	}
	public String getShopName() {
		return ShopName;
	}
	public void setShopName(String shopName) {
		ShopName = shopName;
	}
	public int getDjLsh() {
		return DjLsh;
	}
	public void setDjLsh(int djLsh) {
		DjLsh = djLsh;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getPassWord() {
		return PassWord;
	}
	public void setPassWord(String passWord) {
		PassWord = passWord;
	}
	public String getRealName() {
		return RealName;
	}
	public void setRealName(String realName) {
		RealName = realName;
	}
	public String getPhoneNum() {
		return PhoneNum;
	}
	public void setPhoneNum(String phoneNum) {
		PhoneNum = phoneNum;
	}
	public String getSex() {
		return Sex;
	}
	public void setSex(String sex) {
		Sex = sex;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	public Double getIntegral() {
		return Integral;
	}
	public void setIntegral(Double integral) {
		Integral = integral;
	}
	public String getImgUrl() {
		return ImgUrl;
	}
	public void setImgUrl(String imgUrl) {
		ImgUrl = imgUrl;
	}
	public String getOpenKey() {
		return OpenKey;
	}
	public void setOpenKey(String openKey) {
		OpenKey = openKey;
	}
	public int getShopID() {
		return ShopID;
	}
	public void setShopID(int shopID) {
		ShopID = shopID;
	}
	public int getLoginShopID() {
		return loginShopID;
	}
	public void setLoginShopID(int loginShopID) {
		this.loginShopID = loginShopID;
	}
	public String getLocProCode() {
		return LocProCode;
	}
	public void setLocProCode(String locProCode) {
		LocProCode = locProCode;
	}
	public String getLocCityCode() {
		return LocCityCode;
	}
	public void setLocCityCode(String locCityCode) {
		LocCityCode = locCityCode;
	}
	public String getLocSiteName() {
		return LocSiteName;
	}
	public void setLocSiteName(String locSiteName) {
		LocSiteName = locSiteName;
	}
	public int getRoleID() {
		return RoleID;
	}
	public void setRoleID(int roleID) {
		RoleID = roleID;
	}
	public String getWeChatImgUrl() {
		return WeChatImgUrl;
	}
	public void setWeChatImgUrl(String weChatImgUrl) {
		WeChatImgUrl = weChatImgUrl;
	}
	public int getShopUserID() {
		return ShopUserID;
	}
	public void setShopUserID(int shopUserID) {
		ShopUserID = shopUserID;
	}


}
