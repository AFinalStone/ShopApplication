package com.shuimunianhua.xianglixiangqin.pager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afinalstone.androidstudy.swiperefreshview.OnSwipeRefreshViewListener;
import com.afinalstone.androidstudy.swiperefreshview.SwipeRefreshGridView;
import com.google.gson.Gson;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.activity.GoodsDetailSportActivity;
import com.shuimunianhua.xianglixiangqin.activity.SportSaleBuyCrazyActivity;
import com.shuimunianhua.xianglixiangqin.adapter.MyBaseAdapter;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.model.GoodsSportModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.ImagerLoaderUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.StringUtil;
import com.shuimunianhua.xianglixiangqin.util.TimeUtils;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 整点秒杀
 * @author SHI
 * @time 2016/9/12 15:37
 */
public class SportSaleBuyCrazyPager extends BasePager<SportSaleBuyCrazyActivity> implements OnConnectServerStateListener<Integer>, OnSwipeRefreshViewListener {

	/**限时抢购九宫格**/
	private SwipeRefreshGridView swipeRefreshGridView;
	/**数据集合**/
	List<GoodsSportModel> listData_gridView;
	/**适配器**/
	private AdapterSaleByTimeLimited adapterGridView;
    /**当前服务器时间**/
    private long time_current;
	/**当前页面索引**/
	private int currentPageIndex;

	private ImageView iv_empty;

	public Handler  handler_timeCurrent = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			time_current = time_current+1000;
			adapterGridView.notifyDataSetChanged();
			LogUtil.LogShitou("限时抢购当前时间"+System.currentTimeMillis()/1000);
			LogUtil.LogShitou("time_Current = "+time_current);
			sendEmptyMessageDelayed(0,1000);
		}
	};;

	public SportSaleBuyCrazyPager(SportSaleBuyCrazyActivity activity) {
		super(activity);
	}
	
    @Override
    public View initView() {
    	if(view == null){
    		currentPageIndex = 1;

	    	view = View.inflate(mActivity, R.layout.pager_sport_sale_buy_crazy, null);
			iv_empty = (ImageView) view.findViewById(R.id.iv_empty);
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
	    	adapterGridView = new AdapterSaleByTimeLimited(mActivity, listData_gridView);
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
			adapterGridView.notifyDataSetChanged();
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
		connectGoodsServiceAsyncTask.initProgressDialog(false);
		connectGoodsServiceAsyncTask.execute();		
	}
	
	
	public class AdapterSaleByTimeLimited extends MyBaseAdapter<GoodsSportModel> {

		public AdapterSaleByTimeLimited(Context mContext, List<GoodsSportModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = View.inflate(mContext, R.layout.item_adapter_sport_sale_by_crazy02, null);
				holder = new ViewHolder();
				holder.linearLayout_timeRemain = (LinearLayout) convertView.findViewById(R.id.linearLayout_timeRemain);
				holder.tv_timeRemain = (TextView) convertView.findViewById(R.id.tv_timeRemain);
				holder.iv_goodsImage = (ImageView) convertView.findViewById(R.id.iv_goodsImage);
				holder.iv_saleOnGoing = (ImageView) convertView.findViewById(R.id.iv_saleOnGoing);
				holder.tv_goodsName = (TextView) convertView.findViewById(R.id.tv_goodsName);
				holder.tv_goodsPrice_new = (TextView) convertView.findViewById(R.id.tv_goodsPrice_new);
				holder.tv_goodsPrice_old = (TextView) convertView.findViewById(R.id.tv_goodsPrice_old);
				holder.tv_saleHaveFinish = (TextView) convertView.findViewById(R.id.tv_saleHaveFinish);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			GoodsSportModel currentProductActivityModel = listData.get(position);

			ImagerLoaderUtil.getInstance(mContext).displayMyImage(currentProductActivityModel.getImages().get(0), holder.iv_goodsImage);
			holder.tv_goodsName.setText(currentProductActivityModel.getGoodsName());
			holder.tv_goodsPrice_new.setText("￥"+ StringUtil.doubleToString(currentProductActivityModel.getPrice(),"0.00"));
			holder.tv_goodsPrice_old.setText("￥"+StringUtil.doubleToString(currentProductActivityModel.getOriginalPrice(),"0.00"));
			holder.tv_goodsPrice_old.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			updateTextView(holder, currentProductActivityModel);
			return convertView;
		}
		
		/****
		 * 刷新倒计时控件
		 */
		public void updateTextView(ViewHolder hoder,GoodsSportModel currentProductActivityModel) {
			long time_remainToFinish = (TimeUtils.getTimeDate(currentProductActivityModel.getEndTime()).getTime()-time_current)/1000;
			if (time_remainToFinish <= 0) {
				hoder.iv_saleOnGoing.setVisibility(View.INVISIBLE);
				hoder.linearLayout_timeRemain.setVisibility(View.INVISIBLE);
				hoder.tv_saleHaveFinish.setVisibility(View.VISIBLE);
				return;
			}

			long time_remainToBegin = (TimeUtils.getTimeDate(currentProductActivityModel.getBeginTime()).getTime()-time_current)/1000;
			if(time_remainToBegin <= 0){
				hoder.iv_saleOnGoing.setVisibility(View.VISIBLE);
				hoder.linearLayout_timeRemain.setVisibility(View.INVISIBLE);
				hoder.tv_saleHaveFinish.setVisibility(View.INVISIBLE);
				return;
			}

			hoder.iv_saleOnGoing.setVisibility(View.INVISIBLE);
			hoder.linearLayout_timeRemain.setVisibility(View.VISIBLE);
			hoder.tv_saleHaveFinish.setVisibility(View.INVISIBLE);

			String str_day, str_hour, str_minute, str_second;
			long time_day, time_hour, time_minute, time_second;
			//秒
			time_second = time_remainToBegin%60;
			//分钟
			time_minute = (time_remainToBegin/60)%60;
			//小时
			time_hour = (time_remainToBegin/3600)%60;
			//天数
			time_day = (time_remainToBegin/86400)%24;
			//秒
			if (time_second < 10) {
				str_second = "0" + time_second;
			} else {
				str_second = "" + time_second;
			}
			//分钟
			if (time_minute < 10) {
				str_minute = "0" + time_minute;
			} else {
				str_minute = "" + time_minute;
			}
			//小时
			if (time_hour < 10) {
				str_hour = "0" + time_hour;
			} else {
				str_hour = "" + time_hour;
			}
			//天数
			if (time_hour < 10) {
				str_day = "0" + time_day;
			} else {
				str_day = "" + time_day;
			}
			hoder.tv_timeRemain.setText(str_day + " : " + str_hour + " : " + str_minute + " : " + str_second);
		}
		
		private class ViewHolder {
			/**剩余时间**/
			TextView tv_timeRemain;
			/**商品图片**/
			ImageView iv_goodsImage;
			ImageView iv_saleOnGoing;
			LinearLayout linearLayout_timeRemain;
			/**商品名称**/
			TextView tv_goodsName;
			/**商品价格**/
			TextView tv_goodsPrice_new;
			TextView tv_goodsPrice_old;
			/**活动已经结束**/
			TextView tv_saleHaveFinish;
		}
	}
	
	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		LogUtil.LogShitou("活动商品",returnString);
		List<GoodsSportModel> listData_result = null;
		try {
			JSONObject jsonObject = new JSONObject(returnString);
			JSONArray jsonArray = jsonObject.getJSONArray("List");
			Gson gson = new Gson();
			listData_result = new ArrayList<GoodsSportModel>();
			for (int i = 0; i < jsonArray.length(); i++) {
				String strObject = jsonArray.getString(i);
				GoodsSportModel mGoodsModelSport = gson.fromJson(strObject, GoodsSportModel.class);
				listData_result.add(mGoodsModelSport);
			}

		} catch (JSONException e) {
			connectSuccessFlag = false;
			if(!whetherRefresh){
				currentPageIndex--;
			}
		}
		if(listData_result == null || listData_result.size() == 0){
			if(whetherRefresh){
				iv_empty.setVisibility(View.VISIBLE);
			}else{
				iv_empty.setVisibility(View.INVISIBLE);
				ToastUtils.show(mActivity, "没有更多商品数据了");
			}
		}else{
			iv_empty.setVisibility(View.INVISIBLE);
			handler_timeCurrent.removeCallbacksAndMessages(null);
			listData_gridView.clear();
			listData_gridView.addAll(listData_result);
			time_current = TimeUtils.getTimeDate(listData_gridView.get(0).getPresentTime()).getTime();
			adapterGridView.notifyDataSetChanged();
			handler_timeCurrent.sendEmptyMessageDelayed(0, 500);
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
	
    /**
     * 结束函数
     */
    public void onDestroy(){
    	if(handler_timeCurrent != null){
    		handler_timeCurrent.removeCallbacksAndMessages(null);
    	}
    }
	
}








