package com.shi.xianglixiangqin.model;

import java.io.Serializable;
import java.util.List;

/***
 * 积分商品套餐模型
 * @author SHI
 * 2016-12-21 15:05:55
 */
public class GoodsIntegralPackageModel implements Serializable {
    private int Gid;
    private List<GoodsIntegralPackageColorModel> Goodstcys;
    private int Shopid;
    private String Tcname;

    public int getGid() {
        return Gid;
    }

    public void setGid(int gid) {
        Gid = gid;
    }

    public List<GoodsIntegralPackageColorModel> getGoodstcys() {
        return Goodstcys;
    }

    public void setGoodstcys(List<GoodsIntegralPackageColorModel> goodstcys) {
        Goodstcys = goodstcys;
    }

    public int getShopid() {
        return Shopid;
    }

    public void setShopid(int shopid) {
        Shopid = shopid;
    }

    public String getTcname() {
        return Tcname;
    }

    public void setTcname(String tcname) {
        Tcname = tcname;
    }
}
