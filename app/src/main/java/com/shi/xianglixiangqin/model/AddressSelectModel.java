package com.shi.xianglixiangqin.model;

import java.util.List;

/**
 * Created by SHI on 2016/10/26 14:13
 */
public class AddressSelectModel {

    /**
     * 地区编号
     **/
    private String Code;
    /**
     * 地区名称
     **/
    private String Name;
    /**
     * 下属地区对象
     **/
    private List<AddressSelectModel> SubChinaCity;

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<AddressSelectModel> getSubChinaCity() {
        return SubChinaCity;
    }

    public void setSubChinaCity(List<AddressSelectModel> subChinaCity) {
        SubChinaCity = subChinaCity;
    }
}
