package com.shi.xianglixiangqin.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/3 19:49
 */
public class CreateOrderMethodSelectModel implements Serializable {

    //支付选择界面必须使用的字段
    private int TotalOrderNum;
    private Double TotalPrices;
    private String TotalOrderIds;

    //提交普通商品进入支付方式选择界面
    private String ParamPostShoppingCarts;
    private String ParamRealName;
    private String ParamAddress;
    private String ParamPhoneNum;

    //提交组合商品进入支付方式选择界面
    private int ParamZHGoodsID;
    private String ParamZHBuyGoods;

    //提交活动商品进入支付方式选择界面
//    private String ParamOrderJson;
//    private String ParamItemsJson;
//    private String ParamPlatformActionID;
//    private int ParamPlatformActionType;
//    private String ParamRemark;

    //提交积分商品进入提交积分订单界面
    private int ParamJF_ShopID;
    private int ParamJF_GoodsID;
    private long ParamJF_BuyNum;
    private String ParamJF_TCName;
    private String ParamJF_YSName;
    private int ParamJF_BuyType;
    private String ParamJF_SHAddress;
    private String ParamJF_SHUserName;
    private String ParamJF_SHUserPhone;
    private String ParamJF_SHRemark;


    private int TypeComeFromView;
    /**
     * 普通订单(生成订单，可以账期支付)
     **/
    public static final int TypeConfirmOrderGeneralCanPayCredit = 1;
    /**
     * 普通订单(生成订单，不可以账期支付)
     **/
    public static final int TypeConfirmOrderGeneralCanNotPayCredit = 2;
    /**
     * 普通订单(不生成订单，可以账期支付)
     **/
    public static final int TypeMyOrderCanPayCredit = 4;
    /**
     * 普通订单(不生成订单，不可以账期支付)
     **/
    public static final int TypeMyOrderCanNotPayCredit = 5;
    /**
     * 组合订单(生成订单，可以账期支付)
     **/
    public static final int TypeConfirmOrderZHCanPayCredit = 6;
    /**
     * 组合订单(生成订单，不可以账期支付)
     **/
    public static final int TypeConfirmOrderZHCanNotPayCredit = 7;

    /**
     * 我的订单计入支付选择界面
     **/
    public CreateOrderMethodSelectModel(int totalOrderNum, Double totalPrices, String totalOrderIds, int typeComeFromView) {
        TotalOrderIds = totalOrderIds;
        TotalOrderNum = totalOrderNum;
        TotalPrices = totalPrices;
        TypeComeFromView = typeComeFromView;
    }

    /**
     * 提交普通商品和组合商品进入支付选择界面
     **/
    public CreateOrderMethodSelectModel(int totalOrderNum
            , Double totalPrices, String paramPostShoppingCarts, String paramRealName
            , String paramAddress, String paramPhoneNum, int paramZHGoodsID, String paramZHBuyGoods
            , int typeComeFromView) {
        TotalOrderNum = totalOrderNum;
        TotalPrices = totalPrices;
        ParamPostShoppingCarts = paramPostShoppingCarts;
        ParamRealName = paramRealName;
        ParamAddress = paramAddress;
        ParamPhoneNum = paramPhoneNum;
        ParamZHGoodsID = paramZHGoodsID;
        ParamZHBuyGoods = paramZHBuyGoods;
        TypeComeFromView = typeComeFromView;
    }

//    /**
//     * 提交活动商品进入支付选择界面
//     **/
//    public CreateOrderMethodSelectModel(int totalOrderNum
//            , Double totalPrices, String paramOrderJson
//            , String paramItemsJson, String paramPlatformActionID
//            , int paramPlatformActionType, String paramRemark
//            , int typeComeFromView) {
//        TotalOrderNum = totalOrderNum;
//        TotalPrices = totalPrices;
//        ParamOrderJson = paramOrderJson;
//        ParamItemsJson = paramItemsJson;
//        ParamPlatformActionType = paramPlatformActionType;
//        ParamPlatformActionID = paramPlatformActionID;
//        ParamRemark = paramRemark;
//        TypeComeFromView = typeComeFromView;
//    }

    /**
     * 提交积分商品进入提交积分订单界面
     **/
    public CreateOrderMethodSelectModel(long paramJF_BuyNum, int paramJF_ShopID
            , int paramJF_GoodsID, String paramJF_TCName, String paramJF_YSName, int paramJF_BuyType) {
        ParamJF_BuyNum = paramJF_BuyNum;
        ParamJF_ShopID = paramJF_ShopID;
        ParamJF_GoodsID = paramJF_GoodsID;
        ParamJF_TCName = paramJF_TCName;
        ParamJF_YSName = paramJF_YSName;
        ParamJF_BuyType = paramJF_BuyType;
    }

    public String getParamPhoneNum() {
        return ParamPhoneNum;
    }

    public String getTotalOrderIds() {
        return TotalOrderIds;
    }

    public int getTotalOrderNum() {
        return TotalOrderNum;
    }

    public Double getTotalPrices() {
        return TotalPrices;
    }

    public String getParamPostShoppingCarts() {
        return ParamPostShoppingCarts;
    }

    public String getParamRealName() {
        return ParamRealName;
    }

    public String getParamAddress() {
        return ParamAddress;
    }

    public int getTypeComeFromView() {
        return TypeComeFromView;
    }

    public int setTypeComeFromView(int typeComeFromView) {
        return TypeComeFromView = typeComeFromView;
    }

    public int getParamZHGoodsID() {
        return ParamZHGoodsID;
    }

    public void setParamZHGoodsID(int paramZHGoodsID) {
        ParamZHGoodsID = paramZHGoodsID;
    }

    public String getParamZHBuyGoods() {
        return ParamZHBuyGoods;
    }

    public void setParamZHBuyGoods(String paramZHBuyGoods) {
        ParamZHBuyGoods = paramZHBuyGoods;
    }

    public void setParamPhoneNum(String paramPhoneNum) {
        ParamPhoneNum = paramPhoneNum;
    }

    public long getParamJF_BuyNum() {
        return ParamJF_BuyNum;
    }

    public void setParamJF_BuyNum(long paramJF_BuyNum) {
        ParamJF_BuyNum = paramJF_BuyNum;
    }

    public int getParamJF_ShopID() {
        return ParamJF_ShopID;
    }

    public void setParamJF_ShopID(int paramJF_ShopID) {
        ParamJF_ShopID = paramJF_ShopID;
    }

    public int getParamJF_GoodsID() {
        return ParamJF_GoodsID;
    }

    public void setParamJF_GoodsID(int paramJF_GoodsID) {
        ParamJF_GoodsID = paramJF_GoodsID;
    }

    public String getParamJF_TCName() {
        return ParamJF_TCName;
    }

    public void setParamJF_TCName(String paramJF_TCName) {
        ParamJF_TCName = paramJF_TCName;
    }

    public String getParamJF_YSName() {
        return ParamJF_YSName;
    }

    public void setParamJF_YSName(String paramJF_YSName) {
        ParamJF_YSName = paramJF_YSName;
    }

    public String getParamJF_SHAddress() {
        return ParamJF_SHAddress;
    }

    public void setParamJF_SHAddress(String paramJF_SHAddress) {
        ParamJF_SHAddress = paramJF_SHAddress;
    }

    public int getParamJF_BuyType() {
        return ParamJF_BuyType;
    }

    public void setParamJF_BuyType(int paramJF_BuyType) {
        ParamJF_BuyType = paramJF_BuyType;
    }

    public String getParamJF_SHUserName() {
        return ParamJF_SHUserName;
    }

    public void setParamJF_SHUserName(String paramJF_SHUserName) {
        ParamJF_SHUserName = paramJF_SHUserName;
    }

    public String getParamJF_SHUserPhone() {
        return ParamJF_SHUserPhone;
    }

    public void setParamJF_SHUserPhone(String paramJF_SHUserPhone) {
        ParamJF_SHUserPhone = paramJF_SHUserPhone;
    }

    public String getParamJF_SHRemark() {
        return ParamJF_SHRemark;
    }

    public void setParamJF_SHRemark(String paramJF_SHRemark) {
        ParamJF_SHRemark = paramJF_SHRemark;
    }
}
