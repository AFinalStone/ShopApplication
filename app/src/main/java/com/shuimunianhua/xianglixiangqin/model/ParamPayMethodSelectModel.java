package com.shuimunianhua.xianglixiangqin.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/3 19:49
 */
public class ParamPayMethodSelectModel implements Serializable{

    //支付选择界面必须使用的字段
    private int TotalOrderNum;
    private Double TotalPrices;


    private String TotalOrderIds;

    //提交普通商品进入支付方式选择界面
    private String ParamPostShoppingCarts;
    private String ParamRealName;
    private String ParamAddress;
    private String ParamPhoneNum;

    //提交活动商品进入支付方式选择界面
    private String ParamOrderJson;
    private String ParamItemsJson;
    private String ParamPlatformActionID;
    private int ParamPlatformActionType;
    private String ParamRemark;


    private int TypeComeFromView;
    /**普通订单(生成订单，可以账期支付)**/
    public static final int TypeConfirmOrderGeneralCanPayCredit = 1;
    /**普通订单(生成订单，不可以账期支付)**/
    public static final int TypeConfirmOrderGeneralCanNotPayCredit = 2;
    /**活动订单**/
    public static final int TypeConfirmOrderSport = 3;
    /**普通订单(不生成订单，可以账期支付)**/
    public static final int TypeMyOrderCanPayCredit = 4;
    /**普通订单(不生成订单，不可以账期支付)**/
    public static final int TypeMyOrderCanNotPayCredit = 5;

    /**我的订单计入支付选择界面**/
    public ParamPayMethodSelectModel( int totalOrderNum, Double totalPrices, String totalOrderIds,int typeComeFromView) {
        TotalOrderIds = totalOrderIds;
        TotalOrderNum = totalOrderNum;
        TotalPrices = totalPrices;
        TypeComeFromView = typeComeFromView;
    }

    /**提交普通商品进入支付选择界面**/
    public ParamPayMethodSelectModel( int totalOrderNum
            , Double totalPrices, String paramPostShoppingCarts
            , String paramRealName, String paramAddress, String paramPhoneNum
            ,int typeComeFromView) {
        TotalOrderNum = totalOrderNum;
        TotalPrices = totalPrices;
        ParamPostShoppingCarts = paramPostShoppingCarts;
        ParamRealName = paramRealName;
        ParamAddress = paramAddress;
        ParamPhoneNum = paramPhoneNum;
        TypeComeFromView = typeComeFromView;
    }

    /**提交活动商品进入支付选择界面**/
    public ParamPayMethodSelectModel( int totalOrderNum
            , Double totalPrices, String paramOrderJson
            , String paramItemsJson , String paramPlatformActionID
            , int paramPlatformActionType, String paramRemark
            ,int typeComeFromView) {
        TotalOrderNum = totalOrderNum;
        TotalPrices = totalPrices;
        ParamOrderJson = paramOrderJson;
        ParamItemsJson = paramItemsJson;
        ParamPlatformActionType = paramPlatformActionType;
        ParamPlatformActionID = paramPlatformActionID;
        ParamRemark = paramRemark;
        TypeComeFromView = typeComeFromView;
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

    public String getParamOrderJson() {
        return ParamOrderJson;
    }

    public String getParamItemsJson() {
        return ParamItemsJson;
    }

    public String getParamPlatformActionID() {
        return ParamPlatformActionID;
    }

    public int getParamPlatformActionType() {
        return ParamPlatformActionType;
    }

    public String getParamRemark() {
        return ParamRemark;
    }
    public int getTypeComeFromView() {
        return TypeComeFromView;
    }
}
