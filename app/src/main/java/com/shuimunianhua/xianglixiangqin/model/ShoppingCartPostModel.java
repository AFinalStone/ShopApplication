package com.shuimunianhua.xianglixiangqin.model;

/**
 * Created by SHI on 2016/7/27 11:53
 * 提交购物车数据生成订单时所需要的格式
 */
public class ShoppingCartPostModel {
    /**
     * 商铺ID
     **/
    private int ShopID;

    /**
     * 购物车记录id字符串(形如:"1,2,3")
     **/
    private String Ids;

    /**
     * 是否开具发票
     **/
    private boolean IfBilling;

    /**
     * 发票抬头
     **/
    private String BillingName;

    /**
     * 配送方式
     **/
    private int PostMethondID;

    /**
     * 备注
     **/
    private String Remark;

    public int getShopID() {
        return ShopID;
    }

    public void setShopID(int shopID) {
        ShopID = shopID;
    }

    public String getIds() {
        return Ids;
    }

    public void setIds(String ids) {
        Ids = ids;
    }

    public boolean isIfBilling() {
        return IfBilling;
    }

    public void setIfBilling(boolean ifBilling) {
        IfBilling = ifBilling;
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

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }


    public ShoppingCartPostModel(int shopID, String ids
            , boolean ifBilling, String billingName
            , int postMethondID, String remark) {
        ShopID = shopID;
        Ids = ids;
        IfBilling = ifBilling;
        BillingName = billingName;
        PostMethondID = postMethondID;
        Remark = remark;
    }
}
