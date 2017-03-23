package com.shi.xianglixiangqin.model;

import java.io.Serializable;
import java.util.List;

/***
 * 积分商品套餐颜色模型
 * @author SHI
 * 2016-12-21 15:05:55
 */
public class GoodsIntegralPackageColorModel implements Serializable {
    
    private long Gid;
    private List<GoodsIntegralPackageColorBuyTypeModel> Jfbuytype;
    private long Shopid;
    private int Soldnum;
    private String Tcname;
    private long Totalnum;
    private long Xzbuynum;
    private String Ysimg;
    private String Ysname;


    public long getGid() {
        return Gid;
    }

    public void setGid(long gid) {
        Gid = gid;
    }

    public List<GoodsIntegralPackageColorBuyTypeModel> getJfbuytype() {
        return Jfbuytype;
    }

    public void setJfbuytype(List<GoodsIntegralPackageColorBuyTypeModel> jfbuytype) {
        Jfbuytype = jfbuytype;
    }

    public long getShopid() {
        return Shopid;
    }

    public void setShopid(long shopid) {
        Shopid = shopid;
    }

    public int getSoldnum() {
        return Soldnum;
    }

    public void setSoldnum(int soldnum) {
        Soldnum = soldnum;
    }
    public String getTcname() {
        return Tcname;
    }

    public void setTcname(String tcname) {
        Tcname = tcname;
    }

    public long getTotalnum() {
        return Totalnum;
    }

    public void setTotalnum(long totalnum) {
        Totalnum = totalnum;
    }

    public long getXzbuynum() {
        return Xzbuynum;
    }

    public void setXzbuynum(long xzbuynum) {
        Xzbuynum = xzbuynum;
    }

    public String getYsimg() {
        return Ysimg;
    }

    public void setYsimg(String ysimg) {
        Ysimg = ysimg;
    }

    public String getYsname() {
        return Ysname;
    }

    public void setYsname(String ysname) {
        Ysname = ysname;
    }
}
