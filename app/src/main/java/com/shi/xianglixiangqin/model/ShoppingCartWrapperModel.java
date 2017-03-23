package com.shi.xianglixiangqin.model;

/**
 * Created by SHI on 2016/8/5 14:01
 * 批量加入购物车的时候用到这个对象
 */
public class ShoppingCartWrapperModel {

    private int ProductID;

    private int PackageID;

    private int PackageColorID;

    private int Quantity;


    public ShoppingCartWrapperModel(int productID, int packageID, int packageColorID, int quantity) {
        ProductID = productID;
        PackageID = packageID;
        PackageColorID = packageColorID;
        Quantity = quantity;
    }

    public int getProductID() {
        return ProductID;
    }

    public void setProductID(int productID) {
        ProductID = productID;
    }

    public int getPackageID() {
        return PackageID;
    }

    public void setPackageID(int packageID) {
        PackageID = packageID;
    }

    public int getPackageColorID() {
        return PackageColorID;
    }

    public void setPackageColorID(int packageColorID) {
        PackageColorID = packageColorID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
