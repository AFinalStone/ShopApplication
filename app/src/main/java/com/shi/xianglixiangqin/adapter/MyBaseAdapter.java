package com.shi.xianglixiangqin.adapter;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

/**
 * @action 一级分类适配器
 * @author SHI
 * @date  2016-2-17 15:34:13
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {

	/**上下文对象**/ 
	protected Context mContext; 
	/**数据源**/
	protected List<T> listData;
	
	public MyBaseAdapter(Context mContext,List<T> listData){
		this.mContext = mContext;
		this.listData = listData;
	}

	@Override
	public int getCount() {
		if(listData == null){
			return 0;
		}
		return listData.size();
	}

	@Override
	public Object getItem(int position) {
		return listData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}
