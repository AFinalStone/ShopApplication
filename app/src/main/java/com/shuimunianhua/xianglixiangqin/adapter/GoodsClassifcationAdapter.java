package com.shuimunianhua.xianglixiangqin.adapter;

import java.util.List;

import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.model.GoodsClassModel;
import com.shuimunianhua.xianglixiangqin.util.ImagerLoaderUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @action 一级分类适配器
 * @author SHI
 * 2016-2-17 15:36:11
 */
public  class  GoodsClassifcationAdapter extends MyBaseAdapter<GoodsClassModel> {

	int listViewType;
	public GoodsClassifcationAdapter(Context mContext,List<GoodsClassModel> listData,int ListViewType){
		super(mContext, listData);
		this.listViewType = ListViewType;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView == null){
			convertView = View.inflate(mContext,R.layout.item_adapter_goods_classifcation,null);
			holder = new ViewHolder();
			holder.iv_productImage = (ImageView) convertView.findViewById(R.id.iv_productImage);
			holder.tv_productName = (TextView) convertView.findViewById(R.id.tv_productName);
			convertView.setTag(holder);
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		GoodsClassModel productClassModel = listData.get(position);
		if(productClassModel != null){
			
			switch (listViewType) {
			case InformationCodeUtil.flagOfAListView:
				holder.iv_productImage.setVisibility(View.VISIBLE);
				holder.tv_productName.setVisibility(View.GONE);
				holder.tv_productName.setText(productClassModel.getClassName());
				ImagerLoaderUtil.getInstance(mContext).displayMyImage(productClassModel.getImgUrl(), holder.iv_productImage);
				break;
			case InformationCodeUtil.flagOfBListView:
				holder.iv_productImage.setVisibility(View.GONE);
				holder.tv_productName.setVisibility(View.VISIBLE);
				holder.tv_productName.setText(productClassModel.getClassName());
				break;
			case InformationCodeUtil.flagOfCListView:
				holder.iv_productImage.setVisibility(View.GONE);
				holder.tv_productName.setVisibility(View.VISIBLE);
				holder.tv_productName.setText(productClassModel.getClassName());
				break;

			default:
				break;
			}
		}
		return convertView;
	}


	private class ViewHolder {
		ImageView iv_productImage;
		TextView tv_productName;
	}

}
