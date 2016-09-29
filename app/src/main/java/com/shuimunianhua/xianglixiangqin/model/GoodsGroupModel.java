package com.shuimunianhua.xianglixiangqin.model;

import java.util.List;

public class GoodsGroupModel {

    private int ClassID;
    private String ProductModeType;
    private List<GoodsGeneralModel> listProductModels;

//    private GoodsGeneralModel productModel0;
//    private GoodsGeneralModel productModel1;
//    private GoodsGeneralModel productModel2;
//    private GoodsGeneralModel productModel3;
//    private GoodsGeneralModel productModel4;
//    private GoodsGeneralModel productModel5;

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

//    public GoodsGeneralModel getProductModel0() {
//        return productModel0;
//    }
//
//    public void setProductModel0(GoodsGeneralModel productModel0) {
//        this.productModel0 = productModel0;
//    }
//
//    public GoodsGeneralModel getProductModel1() {
//        return productModel1;
//    }
//
//    public void setProductModel1(GoodsGeneralModel productModel1) {
//        this.productModel1 = productModel1;
//    }
//
//    public GoodsGeneralModel getProductModel2() {
//        return productModel2;
//    }
//
//    public void setProductModel2(GoodsGeneralModel productModel2) {
//        this.productModel2 = productModel2;
//    }
//
//    public GoodsGeneralModel getProductModel3() {
//        return productModel3;
//    }
//
//    public void setProductModel3(GoodsGeneralModel productModel3) {
//        this.productModel3 = productModel3;
//    }
//
//    public GoodsGeneralModel getProductModel4() {
//        return productModel4;
//    }
//
//    public void setProductModel4(GoodsGeneralModel productModel4) {
//        this.productModel4 = productModel4;
//    }
//
//    public GoodsGeneralModel getProductModel5() {
//        return productModel5;
//    }
//
//    public void setProductModel5(GoodsGeneralModel productModel5) {
//        this.productModel5 = productModel5;
//    }
//
//    public void setProductModel(int index, GoodsGeneralModel productModel) {
//        switch (index) {
//            case 0:
//                productModel0 = productModel;
//                break;
//            case 1:
//                productModel1 = productModel;
//                break;
//            case 2:
//                productModel2 = productModel;
//                break;
//            case 3:
//                productModel3 = productModel;
//                break;
//            case 4:
//                productModel4 = productModel;
//                break;
//            case 5:
//                productModel5 = productModel;
//                break;
//            default:
//                break;
//        }
//    }
//
//    public GoodsGeneralModel getProductModel(int index) {
//        GoodsGeneralModel returnProductModel = null;
//        switch (index) {
//            case 0:
//                returnProductModel = productModel0;
//                break;
//            case 1:
//                returnProductModel = productModel1;
//                break;
//            case 2:
//                returnProductModel = productModel2;
//                break;
//            case 3:
//                returnProductModel = productModel3;
//                break;
//            case 4:
//                returnProductModel = productModel4;
//                break;
//            case 5:
//                returnProductModel = productModel5;
//                break;
//            default:
//                break;
//        }
//        return returnProductModel;
//    }

}
