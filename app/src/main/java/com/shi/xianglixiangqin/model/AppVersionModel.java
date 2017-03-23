package com.shi.xianglixiangqin.model;

public class AppVersionModel {

	/**状态**/
	private boolean Sign;
	/**版本号**/
	private int versionCode;
	/**版本名称**/
	private String versionName;
	/**版本大小**/
	private String versionSize;
	/**版本特性描述**/
	private String versionDesc;
	/**App版本下载地址**/
	private String AddressOfDown;
	
	public boolean getSign() {
		return Sign;
	}
	public void setSign(boolean sign) {
		Sign = sign;
	}

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	public String getVersionSize() {
		return versionSize;
	}

	public void setVersionSize(String versionSize) {
		this.versionSize = versionSize;
	}

	public String getVersionDesc() {
		return versionDesc;
	}

	public void setVersionDesc(String versionDesc) {
		this.versionDesc = versionDesc;
	}

	public String getAddressOfDown() {
		return AddressOfDown;
	}

	public void setAddressOfDown(String addressOfDown) {
		AddressOfDown = addressOfDown;
	}
}
