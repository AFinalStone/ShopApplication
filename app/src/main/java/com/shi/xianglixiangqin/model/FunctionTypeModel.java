package com.shi.xianglixiangqin.model;

public class FunctionTypeModel {

	/** 功能模块资源ID **/
	private int imageUrl;
	/** 功能模块名称 **/
	private String name;

	public FunctionTypeModel(int imageUrl, String name) {
		super();
		this.imageUrl = imageUrl;
		this.name = name;
	}

	public int getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(int imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
