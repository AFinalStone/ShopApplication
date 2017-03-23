package com.shi.xianglixiangqin.model;

import java.io.Serializable;

/**
 * 套餐颜色对象
 *
 * @author SHI
 *         2016年4月20日 19:03:12
 */

public class GoodsPackageStandardModel implements Serializable {
    /**
     * 颜色图片
     **/
    private String ColorImgUrl;
    private String colorimg;
    /**
     * 颜色名称
     **/
    private String ColorName;
    /**
     * 核心价格
     **/
    private double CorePrice;
    /**
     * 含税核心价格
     **/
    private double CoreTaxPrice;
    /**
     * 单据流水号
     **/
    private int DjLsh;
    /**
     * 价格
     **/
    private double Price;
    /**
     * 已售数量
     **/
    private double SaledCount;
    /**
     * 库存数量
     **/
    private long StoneCount;
    /**
     * 飞币数
     **/
    private int FlyCoin;
    /**
     * 含税飞币数
     **/
    private int TaxFlyCoin;
    /**
     * 含税价格
     **/
    private double TaxPrice;
    //为组合商品添加的额外字段
    private int ColorID;
    private double Yhje;
    public String getColorImgUrl() {
        return ColorImgUrl;
    }

    public void setColorImgUrl(String colorImgUrl) {
        ColorImgUrl = colorImgUrl;
    }

    public String getColorimg() {
        return colorimg;
    }

    public void setColorimg(String colorimg) {
        this.colorimg = colorimg;
    }

    public String getColorName() {
        return ColorName;
    }

    public void setColorName(String colorName) {
        ColorName = colorName;
    }

    public double getCorePrice() {
        return CorePrice;
    }

    public void setCorePrice(double corePrice) {
        CorePrice = corePrice;
    }

    public double getCoreTaxPrice() {
        return CoreTaxPrice;
    }

    public void setCoreTaxPrice(double coreTaxPrice) {
        CoreTaxPrice = coreTaxPrice;
    }

    public int getDjLsh() {
        return DjLsh;
    }

    public void setDjLsh(int djLsh) {
        DjLsh = djLsh;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getSaledCount() {
        return SaledCount;
    }

    public void setSaledCount(double saledCount) {
        SaledCount = saledCount;
    }

    public long getStoneCount() {
        return StoneCount;
    }

    public void setStoneCount(long stoneCount) {
        StoneCount = stoneCount;
    }

    public double getTaxPrice() {
        return TaxPrice;
    }

    public void setTaxPrice(double taxPrice) {
        TaxPrice = taxPrice;
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

    public int getColorID() {
        return ColorID;
    }

    public void setColorID(int colorID) {
        ColorID = colorID;
    }

    public double getYhje() {
        return Yhje;
    }

    public void setYhje(double yhje) {
        Yhje = yhje;
    }
}
