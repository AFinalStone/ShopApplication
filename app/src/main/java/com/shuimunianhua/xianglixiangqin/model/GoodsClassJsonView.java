package com.shuimunianhua.xianglixiangqin.model;

import java.util.List;

/**
 * 商品分类具体信息model
 * Created by SHI on 2016/8/30 16:55
 */
public class GoodsClassJsonView {
    private String ClassName;
    private int DjLsh;
    private List<GoodsClassJsonView> GoodsClassJsonViews;
    private String ImgUrl;

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public int getDjLsh() {
        return DjLsh;
    }

    public void setDjLsh(int djLsh) {
        DjLsh = djLsh;
    }

    public List<GoodsClassJsonView> getGoodsClassJsonViews() {
        return GoodsClassJsonViews;
    }

    public void setGoodsClassJsonViews(List<GoodsClassJsonView> goodsClassJsonViews) {
        GoodsClassJsonViews = goodsClassJsonViews;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }
}
