package com.shuimunianhua.xianglixiangqin.model;

import java.io.Serializable;

/**
 * 购物车视图类
 * Created by SHI on 2016/7/25 09:50
 */
public class ShoppingCartChildGoodsModel  implements Serializable
{
    private int Id;
    /// <summary>商品id </summary>
    private int ProductId;

    /// <summary>商品名称 </summary>
    private String ProductName;

    /// <summary>套餐id </summary>
    private int PackageID;

    /// <summary>套餐名称 </summary>
    private String PackageName;

    /// <summary>套餐颜色id(而非颜色id) </summary>
    private int PackageColorID;

    /// <summary>颜色名称 </summary>
    private String ColorName;

    /// <summary>购买数量</summary>
    private int Quantity;

    /// <summary>含税单价</summary>
    private Double UnitTaxPrice;

    /// <summary>未税单价</summary>
    private Double UnitPrice;

    /// <summary>商品图片地址</summary>
    private String ImgUrl;

    /// <summary>单价未税飞币</summary>
    private int FlyCoin;

    /// <summary>单价含税飞币</summary>
    private int TaxFlyCoin;

    /**商品选中状态**/
    public boolean IfSelect = false;

    ShoppingCartChildGoodsModel(){super();}

    public ShoppingCartChildGoodsModel(
            int id,
            int productId, String productName
            , String imgUrl
            , int packageID, String packageName
            , int packageColorID, String colorName
            , int quantity
            , Double unitPrice, Double unitTaxPrice
            , int flyCoin, int taxFlyCoin) {
        Id = id;
        ColorName = colorName;
        ProductId = productId;
        ProductName = productName;
        PackageID = packageID;
        PackageName = packageName;
        PackageColorID = packageColorID;
        Quantity = quantity;
        UnitTaxPrice = unitTaxPrice;
        UnitPrice = unitPrice;
        ImgUrl = imgUrl;
        FlyCoin = flyCoin;
        TaxFlyCoin = taxFlyCoin;
    }


    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getPackageID() {
        return PackageID;
    }

    public void setPackageID(int packageID) {
        PackageID = packageID;
    }

    public String getPackageName() {
        return PackageName;
    }

    public void setPackageName(String packageName) {
        PackageName = packageName;
    }

    public int getPackageColorID() {
        return PackageColorID;
    }

    public void setPackageColorID(int packageColorID) {
        PackageColorID = packageColorID;
    }

    public String getColorName() {
        return ColorName;
    }

    public void setColorName(String colorName) {
        ColorName = colorName;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public Double getUnitTaxPrice() {
        return UnitTaxPrice;
    }

    public void setUnitTaxPrice(Double unitTaxPrice) {
        UnitTaxPrice = unitTaxPrice;
    }

    public Double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        UnitPrice = unitPrice;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }

    public int getFlyCoin() {
        return FlyCoin;
    }

    public void setFlyCoin(int flyCoin) {
        FlyCoin = flyCoin;
    }

    public int getTaxFlyCoin() {
        return TaxFlyCoin;
    }

    public void setTaxFlyCoin(int taxFlyCoin) {
        TaxFlyCoin = taxFlyCoin;
    }


    @Override
    public boolean equals(Object o) {

        if( o instanceof ShoppingCartChildGoodsModel && o != null){

            ShoppingCartChildGoodsModel that = (ShoppingCartChildGoodsModel) o;

            if(  ProductId == that.ProductId && PackageID == that.PackageID
                    && PackageColorID == that.PackageColorID && ProductName.equals(that.ProductName)
                    && PackageName.equals(that.PackageName) && ColorName.equals(that.ColorName)
                    && UnitTaxPrice.equals(that.UnitTaxPrice) && UnitPrice.equals(that.UnitPrice))
            {
                return true;
            }
        }
        return false;

    }

}
