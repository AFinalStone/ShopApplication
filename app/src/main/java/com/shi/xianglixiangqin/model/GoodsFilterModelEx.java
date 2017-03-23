package com.shi.xianglixiangqin.model;

import java.util.List;

/***
 * 商品筛选分类 - 模型类
 * @author ZHU
 */
public class GoodsFilterModelEx {

	private String attr_dw;
	private String attr_name;
	private List<String> attr_val;
	private int checkPosition = -1;
	public String getAttr_dw() {
		return attr_dw;
	}

	public void setAttr_dw(String attr_dw) {
		this.attr_dw = attr_dw;
	}

	public String getAttr_name() {
		return attr_name;
	}

	public void setAttr_name(String attr_name) {
		this.attr_name = attr_name;
	}

	public List<String> getAttr_val() {
		return attr_val;
	}

	public void setAttr_val(List<String> attr_val) {
		this.attr_val = attr_val;
	}

	public int getCheckPosition() {
		return checkPosition;
	}

	public void setCheckPosition(int checkPosition) {
		this.checkPosition = checkPosition;
	}
}
