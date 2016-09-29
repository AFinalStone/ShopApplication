package com.shuimunianhua.xianglixiangqin.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/3 19:49
 */
public class ParamPayMoneyModel implements Serializable{

    /**银行卡卡号**/
    private String bankCardCode;
    /**银行编号*/
    private String bankCode;
    /**区域编号*/
    private String areaCode;
    /**银行卡绑定的手机号*/
    private String bankCardBindMobile;
    /**持卡人姓名**/
    private String bankCardUserName;
    /**持卡人身份证号**/
    private String BankCardUserCardID;
    /**订单号**/
    private String orderIds;
    /**支付密码**/
    private String payPassword;

    public ParamPayMoneyModel(String bankCardCode, String bankCode
            , String areaCode, String bankCardBindMobile
            , String bankCardUserName, String bankCardUserCardID
            , String orderIds, String payPassword) {
        this.bankCardCode = bankCardCode;
        this.bankCode = bankCode;
        this.areaCode = areaCode;
        this.bankCardBindMobile = bankCardBindMobile;
        this.bankCardUserName = bankCardUserName;
        BankCardUserCardID = bankCardUserCardID;
        this.orderIds = orderIds;
        this.payPassword = payPassword;
    }

    public String getBankCardCode() {
        return bankCardCode;
    }

    public void setBankCardCode(String bankCardCode) {
        this.bankCardCode = bankCardCode;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getBankCardBindMobile() {
        return bankCardBindMobile;
    }

    public void setBankCardBindMobile(String bankCardBindMobile) {
        this.bankCardBindMobile = bankCardBindMobile;
    }

    public String getBankCardUserName() {
        return bankCardUserName;
    }

    public void setBankCardUserName(String bankCardUserName) {
        this.bankCardUserName = bankCardUserName;
    }

    public String getBankCardUserCardID() {
        return BankCardUserCardID;
    }

    public void setBankCardUserCardID(String bankCardUserCardID) {
        BankCardUserCardID = bankCardUserCardID;
    }

    public String getOrderIds() {
        return orderIds;
    }

    public void setOrderIds(String orderIds) {
        this.orderIds = orderIds;
    }

    public String getPayPassword() {
        return payPassword;
    }

    public void setPayPassword(String payPassword) {
        this.payPassword = payPassword;
    }
}
