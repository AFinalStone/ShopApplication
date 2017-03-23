package com.shi.xianglixiangqin.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by SHI on 2016/7/25 16:34
 * 从服务器货取回来的购物车数据是这个格式的json字符串
 */
public class ShoppingCartParentGoodsModel implements Serializable{

    /**店铺ID**/
    private int ShopID;

    /**店铺名称**/
    private String ShopName;

    /**客户账期天数**/
    private int DelayPayDays;

    /**店铺已经加入购物车的商品**/
    private List<ShoppingCartChildGoodsModel> ShoppingCarts = new ArrayList<ShoppingCartChildGoodsModel>();

    /**商品选中状态**/
    public boolean IfSelect = false;

    public int getShopID() {
        return ShopID;
    }

    public void setShopID(int shopID) {
        ShopID = shopID;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public int getDelayPayDays() {
        return DelayPayDays;
    }

    public void setDelayPayDays(int delayPayDays) {
        DelayPayDays = delayPayDays;
    }

    public List<ShoppingCartChildGoodsModel> getShoppingCarts() {
        return ShoppingCarts;
    }

    public void setShoppingCarts(List<ShoppingCartChildGoodsModel> shoppingCarts) {
        ShoppingCarts = shoppingCarts;
    }

    @Override
    public boolean equals(Object o) {

        if( o instanceof ShoppingCartParentGoodsModel && o != null){

            ShoppingCartParentGoodsModel that = (ShoppingCartParentGoodsModel) o;
            if(ShopID == that.getShopID()){
                return true;
            }
        }
        return false;
    }

}
