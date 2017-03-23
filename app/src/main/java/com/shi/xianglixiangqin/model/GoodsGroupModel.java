package com.shi.xianglixiangqin.model;

import java.util.List;

public class GoodsGroupModel {

    private int ClassID;
    private String ProductModeType;
    private List<GoodsGeneralModel> listProductModels;

    public int getClassID() {
        return ClassID;
    }

    public void setClassID(int classID) {
        ClassID = classID;
    }

    public String getProductModeType() {
        return ProductModeType;
    }

    public void setProductModeType(String productModeType) {
        ProductModeType = productModeType;
    }

    public List<GoodsGeneralModel> getListProductModels() {
        return listProductModels;
    }

    public void setListProductModels(List<GoodsGeneralModel> listProductModels) {
        this.listProductModels = listProductModels;
    }
}
