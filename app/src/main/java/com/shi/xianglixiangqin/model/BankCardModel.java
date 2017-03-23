package com.shi.xianglixiangqin.model;

/**
 * 银行卡信息对象
 * @author SHI
 * 2016年4月11日 11:50:58
 */
public class BankCardModel {
//	04-11 12:43:47.894: E/返回信息___0(8183): {"DjLsh":-1,"Msg":"{\"AccountCode\":\"6222023602102495280866200\",\"BankName\":\"中国工商银行\",\"ID\":2,\"SignID\":\"20160411101916000000000000012837\"}","Sign":1}

	/**银行卡编号**/
	private String AccountCode;
	
	/**银行名称*/
	private String BankName;
	
	/**银行名称*/
	private int ID;
	
	/**签约ID**/
	private String SignID;
	
	public String getAccountCode() {
		return AccountCode;
	}

	public void setAccountCode(String accountCode) {
		AccountCode = accountCode;
	}

	public String getBankNam() {
		return BankName;
	}

	public void setBankNam(String bankNam) {
		BankName = bankNam;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getSignID() {
		return SignID;
	}

	public void setSignID(String signID) {
		SignID = signID;
	}

	
}
