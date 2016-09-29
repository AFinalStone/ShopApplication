package com.shuimunianhua.xianglixiangqin.model;

import java.io.Serializable;

/**
 * 商品视图类
 * Created by Administrator on 2016/7/28 18:04
 */
public class OrderViewProductModel implements Serializable
{
    /// <summary>商品编号</summary>
    private int ProductID;
    /// <summary>商品名称</summary>
    private String ProductName;
    /// <summary>商品图片</summary>
    private String ImgUrl;
    /// <summary>套餐名称</summary>
    private String PackageName;
    /// <summary>颜色名称</summary>
    private String ColorName;
    /// <summary>商品单价</summary>
    private Double UnitMoney;
    /// <summary>商品数量</summary>
    private int BuyCount;

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public String getColorName() {
        return ColorName;
    }

    public void setColorName(String colorName) {
        ColorName = colorName;
    }

    public Double getUnitMoney() {
        return UnitMoney;
    }

    public void setUnitMoney(Double unitMoney) {
        UnitMoney = unitMoney;
    }

    public int getBuyCount() {
        return BuyCount;
    }

    public void setBuyCount(int buyCount) {
        BuyCount = buyCount;
    }
}
