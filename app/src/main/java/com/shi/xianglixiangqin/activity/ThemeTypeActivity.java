package com.shi.xianglixiangqin.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import butterknife.ButterKnife;
import butterknife.BindView;
import com.afinalstone.androidstudy.view.TabIndicatorView;
import com.afinalstone.androidstudy.view.roolpager.RollViewPager;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultBaseModel;
import com.shi.xianglixiangqin.model.AdvertisementModel;
import com.shi.xianglixiangqin.model.GoodsGeneralModel;
import com.shi.xianglixiangqin.model.GoodsSportModel;
import com.shi.xianglixiangqin.pager.BasePager;
import com.shi.xianglixiangqin.pager.ThemeTypeItemPager;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.TimeUtil;
import com.shi.xianglixiangqin.util.ToastUtil;
import com.shi.xianglixiangqin.view.ScrollListView;

public class ThemeTypeActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer>, OnClickListener, OnRefreshListener {

	/** 左侧返回控件  **/
	@BindView(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/** 标题  **/
	@BindView(R.id.tv_title)
	 TextView tv_title;
	/** 首页广告轮播图  **/
	@BindView(R.id.rollViewpager)
	 RollViewPager rollViewpager;
	private List<String> listData_rollView;
	
	/**swipeRefreshLayout刷新控件**/
	@BindView(R.id.swipeRefreshLayout)
	 SwipeRefreshLayout swipeRefreshLayout;
	/**scrollView**/
	@BindView(R.id.scrollView)
	 ScrollView scrollView;

	/** 团购中心外围的控件**/
	@BindView(R.id.relativeLayout_goodsByGroupBuy)
	 RelativeLayout relativeLayout_goodsByGroupBuy;
	/** 团购中心 **/
	@BindView(R.id.linearLayout_goodsByGroupBuy)
	 LinearLayout linearLayout_goodsByGroupBuy;
	/** 精品团购ListView **/
	@BindView(R.id.listView_goodsByGroupBuy)
	 ScrollListView listView_goodsByGroupBuy;
	/** 精品团购数据 **/
	private List<GoodsSportModel> listData_goodsByGroupBuy;
	/** 精品团购适配器 **/
	private AdapterGoodsByGroupBuy adapterGoodsByGroupBuy;

	/**限时抢购外围的控件**/
	@BindView(R.id.relativeLayout_salesByTimeLimited)
	 RelativeLayout relativeLayout_salesByTimeLimited;
	/** 更多限时抢购商品 **/
	@BindView(R.id.linearLayout_moreSalesByTimeLimited)
	 LinearLayout linearLayout_moreSalesByTimeLimited;
	/** 限时抢购GridView **/
	@BindView(R.id.gridView_saleByTimeLimited)
	 GridView gridView_saleByTimeLimited;
	/** 限时抢购数据 **/
	private List<GoodsSportModel> listData_saleByTimeLimited;
	/** 限时抢购适配器 **/
	private AdapterSalesByTimeLimited adapterSaleByTimeLimited;

	/**精品推荐外围的控件**/
	@BindView(R.id.relativeLayout_morejpRecomment)
	 RelativeLayout relativeLayout_morejpRecomment;
	/** 更多精品推荐 **/
	@BindView(R.id.linearLayout_morejpRecomment)
	 LinearLayout linearLayout_morejpRecomment;
	/** 精品推荐gridView **/
	@BindView(R.id.gridView_jpRecomment)
	 GridView gridView_jpRecomment;
	/** 精品推荐数据 **/
	private List<GoodsGeneralModel> listData_jpRecomment;
	/** 精品推荐适配器 **/
	AdapterJpRecomment adapterjpRecomment;
	
	
	/** 当前主题分类子目录名称   **/
	@BindView(R.id.tabIndicatorView)
	 TabIndicatorView tabIndicatorView;
	/**	当前主题分类子目录内容   **/
	@BindView(R.id.viewPager)
	 ViewPager viewPager;
	private MyAdapterPager myAdapterPager;
	private List<ThemeTypeItemPager> listData_viewPager;
    /**当前服务器时间**/
    private long time_current;
    
    private int MESSAGE_01 = 1;
	//这里很重要，使用Handler的延时效果，每隔一秒刷新一下适配器，以此产生倒计时效果
    private Handler handler_timeCurrent = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	time_current = time_current+1000;
        	adapterGoodsByGroupBuy.notifyDataSetChanged();
        	adapterSaleByTimeLimited.notifyDataSetChanged();
            handler_timeCurrent.sendEmptyMessageDelayed(MESSAGE_01,1000);

        }
    };


	private void getData(String methodName) {
		
		if(InformationCodeUtil.methodNameHomeIndex.equals(methodName)){
			SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
			requestSoapObject.addProperty("secKillCount", 2);
			requestSoapObject.addProperty("groupPurchaseCount", 3);
			requestSoapObject.addProperty("timePurchaseCount", 6);
			requestSoapObject.addProperty("advCount", 3);
			requestSoapObject.addProperty("hotShopCount", 0);
			requestSoapObject.addProperty("newGoodsCount", 6);
			requestSoapObject.addProperty("cityCode", MyApplication.getmCustomModel(mContext).getLocCityCode());
			ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask
					(mContext, this, requestSoapObject, methodName);
			connectGoodsServiceAsyncTask.initProgressDialog(false);
			connectGoodsServiceAsyncTask.execute();
			return;
		}
		
	}

	public void initView() {
		setContentView(R.layout.activity_themetype);
		ButterKnife.bind(this);
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(this);
		String title = getIntent().getStringExtra(InformationCodeUtil.IntentThemeTypeName);
		if(title != null){
			tv_title.setText(title+"街");
		}else{
			tv_title.setText("主题街");
		}
		swipeRefreshLayout.setColorSchemeColors( Color.RED
				,Color.GREEN
				,Color.BLUE
				,Color.YELLOW
				,Color.CYAN
				,0xFFFE5D14
				,Color.MAGENTA);
		swipeRefreshLayout.setOnRefreshListener(this);
		linearLayout_goodsByGroupBuy.setOnClickListener(this);
		linearLayout_moreSalesByTimeLimited.setOnClickListener(this);
		linearLayout_morejpRecomment.setOnClickListener(this);		
		//设置轮播图的宽高
		rollViewpager.setLayoutParams(new LinearLayout.LayoutParams(
				displayDeviceWidth, displayDeviceWidth*31/72));
		
		//初始化团购中心
		listData_goodsByGroupBuy = new ArrayList<GoodsSportModel>();
		adapterGoodsByGroupBuy = new AdapterGoodsByGroupBuy(mContext, listData_goodsByGroupBuy);
		listView_goodsByGroupBuy.setAdapter(adapterGoodsByGroupBuy);
		listView_goodsByGroupBuy.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int PlatformActionID = listData_goodsByGroupBuy.get(position).getPlatformActionID();
				Intent intent = new Intent(mContext, GoodsDetailSportActivity.class);
				intent.putExtra(InformationCodeUtil.IntentPlatformActionID, PlatformActionID);
				intent.putExtra(InformationCodeUtil.IntentPlatformActionType,
								InformationCodeUtil.PlatformActionType_GroupCentre);
				mContext.startActivity(intent);			
			}
		});
		
		//初始化限时抢购
		listData_saleByTimeLimited = new ArrayList<GoodsSportModel>();
		adapterSaleByTimeLimited = new AdapterSalesByTimeLimited(mContext, listData_saleByTimeLimited);
		gridView_saleByTimeLimited.setAdapter(adapterSaleByTimeLimited);
		gridView_saleByTimeLimited.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int PlatformActionID = listData_saleByTimeLimited.get(position).getPlatformActionID();
				Intent intent = new Intent(mContext, GoodsDetailSportActivity.class);
				intent.putExtra(InformationCodeUtil.IntentPlatformActionID, PlatformActionID);
				intent.putExtra(InformationCodeUtil.IntentPlatformActionType,
						InformationCodeUtil.PlatformActionType_SaleByTimeLimited);
				mContext.startActivity(intent);			
			}
		});
		
		//初始化新品推荐数据源
		listData_jpRecomment = new ArrayList<GoodsGeneralModel>();
		adapterjpRecomment = new AdapterJpRecomment(mContext, listData_jpRecomment);
		gridView_jpRecomment.setAdapter(adapterjpRecomment);
		gridView_jpRecomment.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent  = new Intent(mContext,GoodsDetailGeneralActivity.class);
				intent.putExtra(InformationCodeUtil.IntentGoodsID, listData_jpRecomment.get(position).getDjLsh());
				mContext.startActivity(intent);			
			}
		});	
		
		//初始化当前主题子目录内容
		listData_viewPager =  new ArrayList<ThemeTypeItemPager>();
		myAdapterPager = new MyAdapterPager();
		viewPager.setAdapter(myAdapterPager);
		tabIndicatorView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				LogUtil.LogShitou("onCheckedChanged", ""+checkedId);
				viewPager.setCurrentItem(checkedId);
			}
		});
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(final int position) {
//				LogUtil.LogShitou("onPageScrollStateChanged", ""+position);
				tabIndicatorView.setCurrentSelectItem(position);
			}
			
			@Override
			public void onPageScrolled(final int arg0, final float arg1, final int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(final int position) {
			}
		});
	}

	public void initData() {
			swipeRefreshLayout.post(new Runnable() {
				@Override
				public void run() {
					swipeRefreshLayout.setRefreshing(true);
					onRefresh();
				}
			});		

	}

	@Override
	public void onRefresh() {
		getData(InformationCodeUtil.methodNameHomeIndex);			
	}
	

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.iv_titleLeft:
			finish();
			break;
		case R.id.linearLayout_moreSalesByTimeLimited:
			intent = new Intent(mContext,
					SportSaleBuyTimeLimitedActivity.class);
			mContext.startActivity(intent);
			break;
		case R.id.linearLayout_goodsByGroupBuy:
			intent = new Intent(mContext, SportSaleBuyGroupActivity.class);
			mContext.startActivity(intent);
			break;
			//TODO精品推荐
		case R.id.linearLayout_morejpRecomment:
			intent = new Intent(mContext, SportSaleBuyRecommendActivity.class);
			mContext.startActivity(intent);
			break;

		default:
			break;
		}
	}

	/****
	 * 团购中心适配器
	 * 
	 * @author SHI 2016-3-2 16:13:09
	 */
	public class AdapterGoodsByGroupBuy extends MyBaseAdapter<GoodsSportModel> {

		public AdapterGoodsByGroupBuy(Context mContext,
				List<GoodsSportModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.item_adapter_sport_sale_by_group01, null);
				holder = new ViewHolder();
				holder.iv_productImage = (ImageView) convertView
						.findViewById(R.id.iv_productImage);
				holder.tv_productName = (TextView) convertView
						.findViewById(R.id.tv_productName);
				holder.tv_productPrice_new = (TextView) convertView
						.findViewById(R.id.tv_productPrice_new);
				holder.tv_productPrice_old = (TextView) convertView
						.findViewById(R.id.tv_productPrice_old);
				holder.tv_NumOfjoinGroup = (TextView) convertView
						.findViewById(R.id.tv_numOfJoinGroup);
				holder.btn_justToJoinGroup = (Button) convertView
						.findViewById(R.id.btn_justToJoinGroup);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final GoodsSportModel currentGoodsInfoActModel = listData.get(position);
			if(currentGoodsInfoActModel.getImages() != null && currentGoodsInfoActModel.getImages().size()>0) {
			ImagerLoaderUtil.getInstance(mContext).displayMyImage(
					currentGoodsInfoActModel.getImages().get(0), holder.iv_productImage);
			}	
			updateGroupCentreTextView(holder,currentGoodsInfoActModel);
			
			holder.tv_productName.setText(currentGoodsInfoActModel.getGoodsName());
			holder.tv_productPrice_new.setText("￥"
					+ (int)currentGoodsInfoActModel.getPrice());
			holder.tv_productPrice_old.setText("￥"
					+ (int)currentGoodsInfoActModel.getOriginalPrice());
			holder.tv_productPrice_old.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG);
			holder.tv_NumOfjoinGroup.setText(currentGoodsInfoActModel
					.getJoinNum() + "件已参团");
			holder.btn_justToJoinGroup
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							int PlatformActionID = currentGoodsInfoActModel.getPlatformActionID();
							Intent intent = new Intent(mContext, GoodsDetailSportActivity.class);
							intent.putExtra(InformationCodeUtil.IntentPlatformActionID, PlatformActionID);
							intent.putExtra(InformationCodeUtil.IntentPlatformActionType,
									InformationCodeUtil.PlatformActionType_GroupCentre);
							startActivity(intent);							
						}
					});
			return convertView;
		}
		
		/**
		 * 刷新精品团购中心数据
		 */
		private void updateGroupCentreTextView(ViewHolder holder, GoodsSportModel mGoodsInfoActModel) {
			long times_begin = TimeUtil.getTimeDate(mGoodsInfoActModel.getBeginTime()).getTime();
//			LogUtil.LogShitou("精品团购当前时间", ""+time_current);
//			LogUtil.LogShitou("精品团购结束时间", ""+time_end);
		
			if(times_begin - time_current > 0){
				//活动还未开始
				holder.btn_justToJoinGroup.setEnabled(false);
				holder.btn_justToJoinGroup.setText(R.string.justToBegin);
				
			}else{
				
				long time_end = TimeUtil.getTimeDate(mGoodsInfoActModel.getEndTime()).getTime();
				if( time_end > time_current ){
					//团购是否正在进行
					if(mGoodsInfoActModel.isIsRunning()){
						holder.btn_justToJoinGroup.setEnabled(true);
						holder.btn_justToJoinGroup.setText(R.string.justToJoinGroup);
					}else{
						//活动结束，因为服务器后台人为停止了
						holder.btn_justToJoinGroup.setEnabled(false);
						//团购是否成功
						if(mGoodsInfoActModel.isIsGrouped()){
							holder.btn_justToJoinGroup.setText(R.string.joinGroupSuccess);
						}else{
							holder.btn_justToJoinGroup.setText(R.string.joinGroupFailed);
						}				
					}			
				}else{
					//活动结束，因为活动截至时间到了
					holder.btn_justToJoinGroup.setEnabled(false);
					//团购是否成功
					if(mGoodsInfoActModel.isIsGrouped()){
						holder.btn_justToJoinGroup.setText(R.string.joinGroupSuccess);
					}else{
						holder.btn_justToJoinGroup.setText(R.string.joinGroupFailed);
					}			
				}			
			}
		
		}

		private class ViewHolder {
			/** 商品图片 **/
			ImageView iv_productImage;
			/** 商品名称 **/
			TextView tv_productName;
			/** 商品价格 **/
			TextView tv_productPrice_new;
			TextView tv_productPrice_old;
			/** 参团数量 **/
			TextView tv_NumOfjoinGroup;
			/** 马上参团 **/
			Button btn_justToJoinGroup;
		}
	}

	
	
	
	/****
	 * 限时抢购适配器
	 * 
	 * @author SHI 2016-3-2 16:13:35
	 */
	public class AdapterSalesByTimeLimited extends MyBaseAdapter<GoodsSportModel> {

		public AdapterSalesByTimeLimited(Context mContext,
				List<GoodsSportModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.item_adapter_sport_sale_by_timelimited01, null);
				holder = new ViewHolder();
				holder.iv_productImage = (ImageView) convertView
						.findViewById(R.id.iv_productImage);
				holder.tv_productName = (TextView) convertView
						.findViewById(R.id.tv_productName);
				holder.tv_productPrice_new = (TextView) convertView
						.findViewById(R.id.tv_productPrice_new);
				holder.tv_productPrice_old = (TextView) convertView
						.findViewById(R.id.tv_productPrice_old);
//				holder.tv_timeRemain = (TextView) convertView
//						.findViewById(R.id.tv_timeRemain);
				holder.btn_justToBuy = (Button) convertView
						.findViewById(R.id.btn_justToRob);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final GoodsSportModel currentGoodsInfoActModel = listData.get(position);
			
			updateTextView(holder,currentGoodsInfoActModel);
			
			if(currentGoodsInfoActModel.getImages() != null && currentGoodsInfoActModel.getImages().size()>0) {
			ImagerLoaderUtil.getInstance(mContext).displayMyImage(
					currentGoodsInfoActModel.getImages().get(0), holder.iv_productImage);
			}	
			holder.tv_productName.setText(currentGoodsInfoActModel.getGoodsName());
			holder.tv_productPrice_new.setText("￥"
					+ (int)currentGoodsInfoActModel.getPrice());
			holder.tv_productPrice_old.setText("￥"
					+ (int)currentGoodsInfoActModel.getOriginalPrice());
			holder.tv_productPrice_old.getPaint().setFlags(
					Paint.STRIKE_THRU_TEXT_FLAG);

			holder.btn_justToBuy.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int PlatformActionID = currentGoodsInfoActModel.getPlatformActionID();
					Intent intent = new Intent(mContext, GoodsDetailSportActivity.class);
					intent.putExtra(InformationCodeUtil.IntentPlatformActionID, PlatformActionID);
					intent.putExtra(InformationCodeUtil.IntentPlatformActionType,
							InformationCodeUtil.PlatformActionType_SaleByTimeLimited);
					startActivity(intent);						
				}
			});
			
			return convertView;
		}
		

		/****
		 * 刷新限时抢购倒计时控件
		 */
		public void updateTextView(ViewHolder holder,GoodsSportModel mGoodsInfoActModel) {
			
			long time_begin = TimeUtil.getTimeDate(mGoodsInfoActModel.getBeginTime()).getTime();
			long time_end = TimeUtil.getTimeDate(mGoodsInfoActModel.getEndTime()).getTime();
//			LogUtil.LogShitou("限时抢购当前时间", ""+time_current);
//			LogUtil.LogShitou("限时抢购结束时间", ""+time_end);
			if(time_begin - time_current > 0){
				//活动还未开始
				holder.btn_justToBuy.setEnabled(false);
				holder.btn_justToBuy.setText(R.string.justToBegin);
//				holder.tv_timeRemain.setText("00:00:00");
				
			}else{
				//活动开始
				long times_remain = time_end-time_current;
				if (times_remain <= 0) {
					//活动结束,因为限时抢购截至时间到了
					holder.btn_justToBuy.setEnabled(false);
					holder.btn_justToBuy.setText(R.string.haveFinish);
//					holder.tv_timeRemain.setText("00:00:00");
//					holder.tv_timeRemain.setText(mGoodsInfoActModel.getEndTime()+"结束");
					return;
				}
				if(mGoodsInfoActModel.isIsRunning()){
					//活动正在进行，可以秒杀
					holder.btn_justToBuy.setEnabled(true);
					holder.btn_justToBuy.setText(R.string.justToRob);
				}else{
					//活动已经结束,因为商品已被卖光
					holder.btn_justToBuy.setEnabled(false);
					holder.btn_justToBuy.setText(R.string.haveFinishRob);
				}
				
			}		

		}
		
		private class ViewHolder {

			/** 商品图片 **/
			ImageView iv_productImage;
			/** 商品名称 **/
			TextView tv_productName;
			/** 商品价格 **/
			TextView tv_productPrice_new;
			TextView tv_productPrice_old;
			/** 限时抢购剩余时间 **/
//			TextView tv_timeRemain;
			/** 马上开抢按钮 **/
			Button btn_justToBuy;
		}
	}

	/**
	 * @author SHI
	 * 2016-2-17 15:34:28
	 * 精品推荐适配器
	 */
	public class AdapterJpRecomment extends MyBaseAdapter<GoodsGeneralModel> {

		public AdapterJpRecomment(Context mContext, List<GoodsGeneralModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				convertView = View.inflate(mContext, R.layout.item_adapter_jprecomment, null);
				holder = new ViewHolder();
				holder.iv_goodsImages = (ImageView) convertView.findViewById(R.id.iv_goodsImages);
				holder.tv_goodsName = (TextView) convertView.findViewById(R.id.tv_goodsName);
				holder.tv_goodsPrice = (TextView) convertView.findViewById(R.id.tv_goodsPrice);
				holder.tv_goodsOriginalPrices = (TextView) convertView.findViewById(R.id.tv_goodsOriginalPrices);
				holder.btn_justToBuy = (Button) convertView.findViewById(R.id.btn_justToBuy);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			final GoodsGeneralModel currentProductModel = listData.get(position);
			ImagerLoaderUtil.getInstance(mContext).displayMyImage(currentProductModel.getImgUrl(), holder.iv_goodsImages);
			holder.tv_goodsName.setText(currentProductModel.getGoodsName());
			holder.tv_goodsPrice.setText("￥"+(int)currentProductModel.getMinPrice());
			holder.tv_goodsOriginalPrices.setText("￥"+(int)currentProductModel.getMaxPrice());
			holder.tv_goodsOriginalPrices.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
			holder.btn_justToBuy.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent  = new Intent(mContext,GoodsDetailGeneralActivity.class);
					intent.putExtra(InformationCodeUtil.IntentGoodsID, currentProductModel.getDjLsh());
					mContext.startActivity(intent);					
				}
			});
			return convertView;
		}
		
		private class ViewHolder{
			/**商品图标**/
			ImageView iv_goodsImages;
			/**商品名称**/
			TextView tv_goodsName;
			/**商品价格**/
			TextView tv_goodsPrice;
			/**商品原价**/
			TextView tv_goodsOriginalPrices;
			/**立即购买**/
			Button btn_justToBuy;
		}

	}
	
	/** 设置限时抢购和精品推荐GirdView的宽和高,使其能够横向滑动 */
	public void setGridViewWH(List returnModel, GridView gridView) {
		float displayDeviceWidth = mContext.getResources().getDisplayMetrics().widthPixels;
		int size = returnModel.size();
		// 设置GridView子空间宽为屏幕宽度的二分之一
		int itemWidth = (int) (displayDeviceWidth*2/5);
		int gridviewWidth = (int) (size * (itemWidth));
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
		gridView.setColumnWidth(itemWidth); // 设置列表项宽
		gridView.setHorizontalSpacing(1); // 设置列表项水平间距
		gridView.setStretchMode(GridView.NO_STRETCH);// spacingWidthUniform
		gridView.setNumColumns(size); // 设置列数量=列表集合数
	}
	
	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
//		Object provinceSoapObject = (Object) returnSoapObject.getProperty(methodName + "Result");
		if(InformationCodeUtil.methodNameHomeIndex.equals(methodName)){
			refreshHome(returnString);
		}
	}
	
	/**
	 * 刷新数据
	 * @param result  数据字符串
	 */
	private void refreshHome(String result) {
		
		relativeLayout_goodsByGroupBuy.setVisibility(View.GONE);
		relativeLayout_salesByTimeLimited.setVisibility(View.GONE);	
		relativeLayout_morejpRecomment.setVisibility(View.GONE);
		handler_timeCurrent.removeMessages(MESSAGE_01);
		try {
			 Gson gson = new Gson();
			//JSONObject负责解决获取JSON键值对
			 JSONObject jsonObject=new JSONObject(result);
			 JSONArray jsonArray = jsonObject.getJSONArray("ActList");
			//循环遍历活动商品数组内容
			for (int i = 0; i < jsonArray.length(); i++) {
				//使用泛型解析JSON数据，主要用到了TypeToken这个对象 
				JSONResultBaseModel<GoodsSportModel> mJSONResultModel = gson.fromJson
						(jsonArray.get(i).toString(), new TypeToken<JSONResultBaseModel<GoodsSportModel>>(){}.getType());
				if("精品团购".equals(mJSONResultModel.getTitle())){
					relativeLayout_goodsByGroupBuy.setVisibility(View.VISIBLE);
					listData_goodsByGroupBuy.clear();
					listData_goodsByGroupBuy.addAll(mJSONResultModel.getList());
					adapterGoodsByGroupBuy.notifyDataSetChanged();
					time_current = TimeUtil.getTimeDate(listData_goodsByGroupBuy.get(0).getPresentTime()).getTime();
				}
				if("限时抢购".equals(mJSONResultModel.getTitle())){
					relativeLayout_salesByTimeLimited.setVisibility(View.VISIBLE);
					listData_saleByTimeLimited.clear();
					listData_saleByTimeLimited.addAll(mJSONResultModel.getList());
					setGridViewWH(listData_saleByTimeLimited,	gridView_saleByTimeLimited);
					time_current = TimeUtil.getTimeDate(listData_saleByTimeLimited.get(0).getPresentTime()).getTime();
				}
			}
			
            handler_timeCurrent.sendEmptyMessageDelayed(MESSAGE_01,100);
			//刷新首页广告轮播图
            if(listData_rollView == null){
				jsonArray = jsonObject.getJSONArray("AdvList");
				List<AdvertisementModel> listData = gson.fromJson
				(jsonArray.toString(), new TypeToken<List<AdvertisementModel>>(){}.getType());
				listData_rollView = new ArrayList<String>();
					
				for (int i = 0; i < listData.size(); i++) {
					listData_rollView.add(listData.get(i).getImgUrl());
				}
				
				rollViewpager.setImageUris(listData_rollView);
				rollViewpager.startRoll();
            }
			
			//刷新新品推荐
			JSONResultBaseModel<GoodsGeneralModel> mJSONResultModel = gson.fromJson
			(jsonObject.getString("NewGoodsList"), new TypeToken<JSONResultBaseModel<GoodsGeneralModel>>(){}.getType());			
			List<GoodsGeneralModel> listData_tempNewGoodsPush = mJSONResultModel.getList();
			listData_jpRecomment.clear();
			listData_jpRecomment.addAll(listData_tempNewGoodsPush);
			setGridViewWH(listData_jpRecomment,	gridView_jpRecomment);
			adapterjpRecomment.notifyDataSetChanged();
			relativeLayout_morejpRecomment.setVisibility(View.VISIBLE);
			
			
			//刷新主题街
			listData_viewPager.add(new ThemeTypeItemPager(this));
			listData_viewPager.add(new ThemeTypeItemPager(this));
			listData_viewPager.add(new ThemeTypeItemPager(this));
			listData_viewPager.add(new ThemeTypeItemPager(this));
			listData_viewPager.add(new ThemeTypeItemPager(this));
			myAdapterPager.notifyDataSetChanged();
			
			List<String> listData = new ArrayList<String>();
			listData.add("笔记本");
			listData.add("台式机");
			listData.add("一体机");
			listData.add("平板电脑");
			listData.add("服务器");
			tabIndicatorView.refreshRadioGroup(listData);
			
			
			swipeRefreshLayout.setRefreshing(false);
		} catch (JSONException e) {
			e.printStackTrace();
		}		
	}

	@Override
	public void connectServiceFailed(String returnStrError, String methodName, Integer state,
			boolean whetherRefresh) {
		if(InformationCodeUtil.methodNameHomeIndex.equals(methodName)){
			swipeRefreshLayout.setRefreshing(false);
			relativeLayout_goodsByGroupBuy.setVisibility(View.GONE);
			relativeLayout_salesByTimeLimited.setVisibility(View.GONE);	
			relativeLayout_morejpRecomment.setVisibility(View.GONE);
			ToastUtil.show(mContext, "网络异常，数据请求失败");
		}
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		if(InformationCodeUtil.methodNameHomeIndex.equals(methodName)){
			relativeLayout_goodsByGroupBuy.setVisibility(View.GONE);
			relativeLayout_salesByTimeLimited.setVisibility(View.GONE);	
			relativeLayout_morejpRecomment.setVisibility(View.GONE);
			swipeRefreshLayout.setRefreshing(false);
		}
	}
	
	private class MyAdapterPager extends PagerAdapter{

        @Override
        public int getCount() {
        	if(listData_viewPager == null)
        		return 0;
            return listData_viewPager.size();
        }
        /**
         * 1. 根据position获取对应的view，给view添加到container
         * 2. 返回一个view（Viewpaer的每个界面的内容）
         *      采用的是Viewpager+自定义的view（对外提供自己长什么样子：view）
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager currentPager = listData_viewPager.get(position);
            View initView = currentPager.initView();
            currentPager.initData();//***********初始化数据******************
            container.addView(initView);
            return initView;
        }
        

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }
		
		
	}


}
