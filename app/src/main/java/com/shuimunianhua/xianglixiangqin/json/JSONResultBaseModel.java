package com.shuimunianhua.xianglixiangqin.json;

import java.util.List;



public class JSONResultBaseModel<T>{
	private String Count;
	private int ID;
	private List<T> List;
	private int PageCount;
	private int PageIndex;
	private int PageSize;
	private int RecordCount;
	private String Title;
	public String getCount() {
		return Count;
	}
	public void setCount(String count) {
		Count = count;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public List<T> getList() {
		return List;
	}
	public void setList(List<T> list) {
		List = list;
	}
	public int getPageCount() {
		return PageCount;
	}
	public void setPageCount(int pageCount) {
		PageCount = pageCount;
	}
	public int getPageIndex() {
		return PageIndex;
	}
	public void setPageIndex(int pageIndex) {
		PageIndex = pageIndex;
	}
	public int getPageSize() {
		return PageSize;
	}
	public void setPageSize(int pageSize) {
		PageSize = pageSize;
	}
	public int getRecordCount() {
		return RecordCount;
	}
	public void setRecordCount(int recordCount) {
		RecordCount = recordCount;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	
}
