package com.shi.xianglixiangqin.model;

import java.io.Serializable;
import java.util.List;

/***
 * 积分商品模型
 * @author SHI
 * 2016-12-21 15:05:55
 */
public class GoodsIntegralModel implements Serializable {
    
    private String etime;
    /**商品ID**/
    private int gid;
    /**商品名称**/
    private String goodsname;
    
    private String goodsbz;
    private String goodsdesc;
    private String goodsspec;
    /**套餐**/
    private List<GoodsIntegralPackageModel> goodstc;
    /**图片地址**/
    private List<String> imgs;
    
    private String isrun;
    private String isshow;
    /**店铺ID**/
    private int shopid;
    private String stime;

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getGoodsname() {
        return goodsname;
    }

    public void setGoodsname(String goodsname) {
        this.goodsname = goodsname;
    }

    public String getGoodsbz() {
        return goodsbz;
    }

    public void setGoodsbz(String goodsbz) {
        this.goodsbz = goodsbz;
    }

    public String getGoodsdesc() {
        return goodsdesc;
    }

    public void setGoodsdesc(String goodsdesc) {
        this.goodsdesc = goodsdesc;
    }

    public List<GoodsIntegralPackageModel> getGoodstc() {
        return goodstc;
    }

    public void setGoodstc(List<GoodsIntegralPackageModel> goodstc) {
        this.goodstc = goodstc;
    }

    public String getGoodsspec() {
        return goodsspec;
    }

    public void setGoodsspec(String goodsspec) {
        this.goodsspec = goodsspec;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }

    public String getIsrun() {
        return isrun;
    }

    public void setIsrun(String isrun) {
        this.isrun = isrun;
    }

    public String getIsshow() {
        return isshow;
    }

    public void setIsshow(String isshow) {
        this.isshow = isshow;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }
}
