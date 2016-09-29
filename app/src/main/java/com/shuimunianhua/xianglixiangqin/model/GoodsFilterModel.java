package com.shuimunianhua.xianglixiangqin.model;

/***
 * 商品筛选分类 - 模型类
 * @author ZHU
 */
public class GoodsFilterModel {
	/**自动编号**/
	private int DjLsh ;
	/**筛选名称**/
	private String FilterName;
	// ==== 特殊字段，为了返回结果 ====
	/**所选的条件名称**/
	private String SelectedSubName;
	/**所选的值**/
	private String SelectedSubPid;
	
	public GoodsFilterModel(){}

	public int getDjLsh() {
		return DjLsh;
	}

	public void setDjLsh(int djLsh) {
		DjLsh = djLsh;
	}

	public String getFilterName() {
		return FilterName;
	}

	public void setFilterName(String filterName) {
		FilterName = filterName;
	}
	
	public String getSelectedSubName() {
		return SelectedSubName;
	}

	public void setSelectedSubName(String selectedSubName) {
		SelectedSubName = selectedSubName;
	}

	public String getSelectedSubPid() {
		return SelectedSubPid;
	}

	public void setSelectedSubPid(String selectedSubPid) {
		SelectedSubPid = selectedSubPid;
	}

}
