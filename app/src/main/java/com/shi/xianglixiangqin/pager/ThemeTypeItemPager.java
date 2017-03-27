package com.shi.xianglixiangqin.pager;

import java.util.ArrayList;
import java.util.List;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.activity.ThemeTypeActivity;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.model.GoodsGeneralModel;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.view.ScrollGridView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ThemeTypeItemPager extends BasePager<ThemeTypeActivity> {

	private ScrollGridView scrollGridView;
	List<GoodsGeneralModel> listData_gridView;
	private AdapterThemeTypeItem adapterGridView;
	
	public ThemeTypeItemPager(ThemeTypeActivity context) {
		super(context);
	}

	@Override
	public View initView() {
    	view = View.inflate(mActivity, R.layout.pager_themetype_item, null);
    	scrollGridView = (ScrollGridView) view.findViewById(R.id.scrollGridView);
    	listData_gridView = new ArrayList<GoodsGeneralModel>();
    	GoodsGeneralModel model = new GoodsGeneralModel();
    	model.setMinPrice(1590);
    	model.setGoodsName("戴尔(DELL) S2415H 23.8英寸超窄边框 宽屏IPS 显示器");
//    	model.setImgUrl("drawable://"+R.drawable.imageview_a02);
    	model.setSaledCount(0);
    	listData_gridView.add(model);
    	listData_gridView.add(model);
    	listData_gridView.add(model);
    	listData_gridView.add(model);
    	listData_gridView.add(model);
    	listData_gridView.add(model);
    	listData_gridView.add(model);
    	adapterGridView = new AdapterThemeTypeItem(mActivity, listData_gridView);
    	scrollGridView.setAdapter(adapterGridView);
		return view;
	}

	@Override
	public void initData() {
		
	}
	
	private class AdapterThemeTypeItem extends MyBaseAdapter<GoodsGeneralModel>{

		public AdapterThemeTypeItem(Context mContext,
				List<GoodsGeneralModel> listData) {
			super(mContext, listData);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = View.inflate(mContext, R.layout.item_adapter_themetype_item,null);
				holder = new ViewHolder();
				holder.iv_productImage = (ImageView) convertView.findViewById(R.id.iv_productImage);
				holder.tv_productName = (TextView) convertView.findViewById(R.id.tv_productName);
				holder.tv_productPrice = (TextView) convertView.findViewById(R.id.tv_productPrice);
				holder.tv_haveSaleNum = (TextView) convertView.findViewById(R.id.tv_haveSaleNum);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			GoodsGeneralModel currentGoodsGeneralModel = listData.get(position);
			if(currentGoodsGeneralModel != null){
				ImagerLoaderUtil.getInstance(mContext).displayMyImage(currentGoodsGeneralModel.getImgUrl(), holder.iv_productImage);
				holder.tv_productName.setText(currentGoodsGeneralModel.getGoodsName());
				holder.tv_productPrice.setText("￥"+currentGoodsGeneralModel.getMinPrice());
				holder.tv_haveSaleNum.setText("已售"+currentGoodsGeneralModel.getSaledCount()+"件");
			}
			return convertView;
		}
		
		private class ViewHolder {
			/**商品图片**/
			ImageView iv_productImage;
			/**商品名称**/
			TextView tv_productName;
			/**商品价格**/
			TextView tv_productPrice;
			/**商品已经出售数量**/
			TextView tv_haveSaleNum;
		}
		
	}

}
