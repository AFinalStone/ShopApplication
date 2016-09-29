package com.shuimunianhua.xianglixiangqin.model;

import java.io.Serializable;
import java.util.List;

/**
 * 订单视图类
 * Created by SHI on 2016/7/28 18:02
 */
public class OrderViewModel implements Serializable
{
    /// <summary>单据流水号</summary>
    private int DjLsh;
    /// <summary>用户编号</summary>
    private int UserID;
    /**店铺名称**/
    private String ProductShopName;
    /// <summary>总金额</summary>
    private Double TotalMoney;
    /// <summary>地址</summary>
    private String Address;
    /// <summary>真实姓名</summary>
    private String RealName;
    /// <summary>电话号码</summary>
    private String PhoneNum;
    /// <summary>是否开票</summary>
    private boolean IfBilling;
    /// <summary>开票抬头</summary>
    private String BillingName;
    /// <summary>支付方式编号</summary>
    private int PostMethondID;
    /// <summary>支付方式名称</summary>
    private String PostMethondName;
    /// <summary>订单状态(状态1:待支付,3:待发货,4:待收货,9:已完成,-1:已关闭,16:信用支付)</summary>
    private int OrderSign;
    /// <summary>订单号</summary>
    private String OrderNo;
    /// <summary>下单时间</summary>
    private String OrderTime;

    /// <summary>延迟付款的天数</summary>
    private int DelayPayDays;
    /// <summary>快递</summary>
    private String PostName;
    /// <summary>快递编号</summary>
    private String PostNameNum;

    /**本地字段，方便区分账期订单付款和未付款两种状态**/
    private boolean IsAccountLimitPayed;

    /// <summary> 抵扣飞币</summary>
    private int FlyCoin;

    /// <summary>备注</summary>
    private String Remark;

    ///<summary>订单商品集合</summary>
    private List<OrderViewProductModel> Products;

    /// <summary> 订单商品可抵扣飞币数</summary>
    private int AcceptFlyCoin;

    ///<summary>信用支付倒计时</summary>
    private int CountDown;

    public boolean IfSelect = false;

    public boolean getIfBilling() {
        return IfBilling;
    }

    public void setIfBilling(boolean ifBilling) {
        IfBilling = ifBilling;
    }

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

    public Double getTotalMoney() {
        return TotalMoney;
    }

    public void setTotalMoney(Double totalMoney) {
        TotalMoney = totalMoney;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
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

    public String getBillingName() {
        return BillingName;
    }

    public void setBillingName(String billingName) {
        BillingName = billingName;
    }

    public int getPostMethondID() {
        return PostMethondID;
    }

    public void setPostMethondID(int postMethondID) {
        PostMethondID = postMethondID;
    }

    public String getPostMethondName() {
        return PostMethondName;
    }

    public void setPostMethondName(String postMethondName) {
        PostMethondName = postMethondName;
    }

    public int getOrderSign() {
        return OrderSign;
    }

    public void setOrderSign(int orderSign) {
        OrderSign = orderSign;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(String orderTime) {
        OrderTime = orderTime;
    }

    public int getDelayPayDays() {
        return DelayPayDays;
    }

    public void setDelayPayDays(int delayPayDays) {
        DelayPayDays = delayPayDays;
    }

    public String getPostName() {
        return PostName;
    }

    public void setPostName(String postName) {
        PostName = postName;
    }

    public String getPostNameNum() {
        return PostNameNum;
    }

    public void setPostNameNum(String postNameNum) {
        PostNameNum = postNameNum;
    }

    public boolean getIsAccountLimitPayed() {
        return IsAccountLimitPayed;
    }

    public void setIsAccountLimitPayed(boolean accountLimitPayed) {
        IsAccountLimitPayed = accountLimitPayed;
    }

    public int getFlyCoin() {
        return FlyCoin;
    }

    public void setFlyCoin(int flyCoin) {
        FlyCoin = flyCoin;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public List<OrderViewProductModel> getProducts() {
        return Products;
    }

    public void setProducts(List<OrderViewProductModel> products) {
        Products = products;
    }

    public int getAcceptFlyCoin() {
        return AcceptFlyCoin;
    }

    public void setAcceptFlyCoin(int acceptFlyCoin) {
        AcceptFlyCoin = acceptFlyCoin;
    }

    public int getCountDown() {
        return CountDown;
    }

    public void setCountDown(int countDown) {
        CountDown = countDown;
    }

    public String getProductShopName() {
        return ProductShopName;
    }

    public void setProductShopName(String productShopName) {
        ProductShopName = productShopName;
    }
}
