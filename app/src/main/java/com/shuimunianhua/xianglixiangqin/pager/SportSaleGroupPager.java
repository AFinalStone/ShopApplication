package com.shuimunianhua.xianglixiangqin.pager;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.afinalstone.androidstudy.swiperefreshview.OnSwipeRefreshViewListener;
import com.afinalstone.androidstudy.swiperefreshview.SwipeRefreshGridView;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.activity.GoodsDetailSportActivity;
import com.shuimunianhua.xianglixiangqin.activity.SportSaleBuyGroupActivity;
import com.shuimunianhua.xianglixiangqin.adapter.MyBaseAdapter;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.model.GoodsSportModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.ImagerLoaderUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;


/****
 * 活动团购中心Pager
 * @author SHI
 * 2016-3-10 17:06:57
 */
public class SportSaleGroupPager extends BasePager<SportSaleBuyGroupActivity> implements OnConnectServerStateListener<Integer>, OnSwipeRefreshViewListener {

	/**团购中心gridView**/
	private SwipeRefreshGridView swipeRefreshGridView;
	/**gridView数据集合**/
	List<GoodsSportModel> listData_gridView;
	/**gridView适配器**/
	private AdapterGridView adapterGridView;
	/**当前页面索引**/
	private int currentPageIndex;
	
	public SportSaleGroupPager(SportSaleBuyGroupActivity activity) {
		super(activity);
	}
	
    @Override
    public View initView() {
    	if(view == null){
    		currentPageIndex = 1;
	    	view = View.inflate(mActivity, R.layout.pager_sport_sale_group_buy, null);
	    	swipeRefreshGridView = (SwipeRefreshGridView) view.findViewById(R.id.swipeRefreshGridView);
	    	swipeRefreshGridView.getGridView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					//获取活动商品活动类型和活动ID
					int platformActionID = listData_gridView.get(position).getPlatformActionID();	
					int platformActionType =  mActivity.currentPlatformActionType;
					Intent intent = new Intent(mActivity, GoodsDetailSportActivity.class);
					intent.putExtra(InformationCodeUtil.IntentPlatformActionID, platformActionID);
					intent.putExtra(InformationCodeUtil.IntentPlatformActionType, platformActionType);
					mActivity.startActivity(intent);
				}
			});
	    	
	    	swipeRefreshGridView.setOnRefreshListener(this);
	    	listData_gridView = new ArrayList<GoodsSportModel>();
	    	adapterGridView = new AdapterGridView(mActivity, listData_gridView);
	    	swipeRefreshGridView.getGridView().setAdapter(adapterGridView);
    	}
        return view;
    }
    
    @Override
    public void initData() {
    	if(!connectSuccessFlag){
			connectSuccessFlag = true;
			swipeRefreshGridView.openRefreshState();
    	}
    }
	
    
	private void getData(boolean whetherRefresh) {
		String methodName = InformationCodeUtil.methodNameGetGoodsActList;
		if(whetherRefresh){
			currentPageIndex = 1;
		}else{
			currentPageIndex++;
		}
		SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
		requestSoapObject.addProperty("pageSize", 10);
		requestSoapObject.addProperty("pageIndex", currentPageIndex);
		requestSoapObject.addProperty("classID", classID);
		requestSoapObject.addProperty("platformActionType", mActivity.currentPlatformActionType);
		requestSoapObject.addProperty("cityCode", mActivity.currentCityCode);
		requestSoapObject.addProperty("shopID", mActivity.currentShopID);
		ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask
				(mActivity, this, requestSoapObject, methodName,whetherRefresh);
		connectGoodsServiceAsyncTask.initProgressDialog(false, "请稍等");
		connectGoodsServiceAsyncTask.execute();
	}
	
	/**gridView适配器**/
	public class AdapterGridView extends MyBaseAdapter<GoodsSportModel> {

		public AdapterGridView(Context mContext, List<GoodsSportModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = View.inflate(mContext, R.layout.item_adapter_sport_sale_by_group02, null);
				holder = new ViewHolder();
				holder.iv_productImage = (ImageView) convertView.findViewById(R.id.iv_productImage);
				holder.tv_productName = (TextView) convertView.findViewById(R.id.tv_productName);
			
				holder.tv_productPrice_new = (TextView) convertView.findViewById(R.id.tv_productPrice_new);
				holder.tv_productPrice_old = (TextView) convertView.findViewById(R.id.tv_productPrice_old);
				holder.tv_productPrice_old.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
				
				holder.tv_numOfJoinGroup = (TextView) convertView.findViewById(R.id.tv_numOfJoinGroup);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			GoodsSportModel currentProductActivityModel = listData.get(position);
			
			ImagerLoaderUtil.getInstance(mContext).displayMyImage(currentProductActivityModel.getImages().get(0), holder.iv_productImage);
			holder.tv_productName.setText(currentProductActivityModel.getGoodsName());
			holder.tv_productPrice_new.setText("￥"+(int)currentProductActivityModel.getPrice());
			holder.tv_productPrice_old.setText("￥"+(int)currentProductActivityModel.getOriginalPrice());
			holder.tv_numOfJoinGroup.setText(currentProductActivityModel.getJoinNum()+"件已参团");
			return convertView;
		}
		
		private class ViewHolder {
			/**商品图片**/
			ImageView iv_productImage;
			/**商品名称**/
			TextView tv_productName;
			/**商品价格**/
			TextView tv_productPrice_new;
			TextView tv_productPrice_old;
			/**已参团数量**/
			TextView tv_numOfJoinGroup;
		}
	}
	
	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		LogUtil.LogShitou("活动商品",returnString);
		List<GoodsSportModel> listData_result = null;
		try {
			JSONObject jsonObject = new JSONObject(returnString);
			String strArray = jsonObject.getString("List");
			Gson gson = new Gson();
			listData_result = gson.fromJson(strArray, new TypeToken<List<GoodsSportModel>>(){}.getType());
		} catch (JSONException e) {
			connectSuccessFlag = false;
			if(!whetherRefresh){
				currentPageIndex--;
			}
		}
		if(listData_result == null || listData_result.size() == 0){
			if(whetherRefresh){
				ToastUtils.show(mActivity, "暂无该类商品数据");
			}else{
				ToastUtils.show(mActivity, "暂无更多商品数据");
			}
		}else{
			listData_gridView.clear();
			listData_gridView.addAll(listData_result);
			adapterGridView.notifyDataSetChanged();
		}
		swipeRefreshGridView.closeRefreshState();
	}
	
	@Override
	public void connectServiceFailed(String methodName, Integer state, boolean whetherRefresh) {
		connectSuccessFlag = false;
		if(!whetherRefresh){
			currentPageIndex--;
		}
		swipeRefreshGridView.closeRefreshState();
	}
	
	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		connectSuccessFlag = false;
		if(!whetherRefresh){
			currentPageIndex--;
		}
		swipeRefreshGridView.closeRefreshState();
	}

	@Override
	public void onTopRefrushListener() {
    	getData(true);
	}

	@Override
	public void onBottomRefrushListener() {
    	getData(false);
	}
	

}








