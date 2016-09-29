package com.shuimunianhua.xianglixiangqin.model;

import java.io.Serializable;

/***
 * 地址信息转JSON
 * @author ZHU
 *
 */
@SuppressWarnings("serial")
public class AddressModel  implements Serializable{	
	
	/**单据流水号**/
	private int DjLsh ;
	/**用户编号**/
	private int UserID; 
	/**用户名**/
	private String RealName ;
	/**手机号码**/
	private String PhoneNum ;
	/**地址**/
	private String Address ;
	/**邮政编码**/
	private String PostCode ;
	/**省份编号**/
	private String ProvinceCode ;
	/**城市编号**/
	private String CityCode ;
	/**区域编号**/
	private String AreaCode ;
	/**辖区编号**/
	private String TownCode;
	/**省份名称**/
	private String ProvinceName;
	/**城市名称**/
	private String CityName;
	/**辖区名称**/
	private String AreaName;
	/**区块名称**/
	private String TownName;
	public int getDjLsh() {
		return DjLsh;
	}
	public void setDjLsh(int djLsh) {
		DjLsh = djLsh;
	}
	public int getUserID() {
		return UserID;
	}
	public void setUserID(int userID) {
		UserID = userID;
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
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public String getPostCode() {
		return PostCode;
	}
	public void setPostCode(String postCode) {
		PostCode = postCode;
	}
	public String getProvinceCode() {
		return ProvinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		ProvinceCode = provinceCode;
	}
	public String getCityCode() {
		return CityCode;
	}
	public void setCityCode(String cityCode) {
		CityCode = cityCode;
	}
	public String getAreaCode() {
		return AreaCode;
	}
	public void setAreaCode(String areaCode) {
		AreaCode = areaCode;
	}
	public String getTownCode() {
		return TownCode;
	}
	public void setTownCode(String townCode) {
		TownCode = townCode;
	}
	public String getProvinceName() {
		return ProvinceName;
	}
	public void setProvinceName(String provinceName) {
		ProvinceName = provinceName;
	}
	public String getCityName() {
		return CityName;
	}
	public void setCityName(String cityName) {
		CityName = cityName;
	}
	public String getAreaName() {
		return AreaName;
	}
	public void setAreaName(String areaName) {
		AreaName = areaName;
	}
	public String getTownName() {
		return TownName;
	}
	public void setTownName(String townName) {
		TownName = townName;
	}
}
