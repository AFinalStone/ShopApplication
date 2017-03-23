package com.shi.xianglixiangqin.frament;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout.LayoutParams;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afinalstone.androidstudy.view.roolpager.RollViewPager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.activity.GoodsDetailGeneralActivity;
import com.shi.xianglixiangqin.activity.GoodsDetailSportActivity;
import com.shi.xianglixiangqin.activity.MainActivity;
import com.shi.xianglixiangqin.activity.MyOrderActivity;
import com.shi.xianglixiangqin.activity.MyShopWebActivity;
import com.shi.xianglixiangqin.activity.SearchGoodsActivity;
import com.shi.xianglixiangqin.activity.SearchGoodsMyAgentActivity;
import com.shi.xianglixiangqin.activity.SettingCenterActivity;
import com.shi.xianglixiangqin.activity.ShopActivity;
import com.shi.xianglixiangqin.activity.SportSaleBuyGroupActivity;
import com.shi.xianglixiangqin.activity.SportSaleBuyTimeLimitedActivity;
import com.shi.xianglixiangqin.activity.ThemeTypeActivity;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultBaseModel;
import com.shi.xianglixiangqin.model.AdvertisementModel;
import com.shi.xianglixiangqin.model.FunctionTypeModel;
import com.shi.xianglixiangqin.model.GoodsGeneralModel;
import com.shi.xianglixiangqin.model.GoodsSportModel;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.DensityUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.TimeUtil;
import com.shi.xianglixiangqin.util.ToastUtil;
import com.shi.xianglixiangqin.view.ScrollListView;
import com.shi.xianglixiangqin.view.SelectCityZhanPopWindow;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/***
 * @action 带你飞首页
 * @author SHI
 * @date 2015-7-18 上午11:24:43
 */

public class MainHomeFragment_DaiNiFei extends MyBaseFragment<MainActivity> implements
		OnClickListener,OnConnectServerStateListener<Integer>, OnRefreshListener {

	
	/**当前城市站**/
	@BindView(R.id.cb_location)
	 CheckBox cb_location;
	/**选择城市站动画控件**/
	@BindView(R.id.iv_location)
	 ImageView iv_location;
	/**选择城市战的界面**/
	private SelectCityZhanPopWindow pop;
	/** 搜索内容 **/
	@BindView(R.id.et_searchContext)
	 EditText et_searchContext;
	/** 搜索按钮 **/
	@BindView(R.id.btn_search)
	 Button btn_search;
	/**标题栏**/
	@BindView(R.id.relativeLayout_title)
	 RelativeLayout relativeLayout_title;
	
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
	/** 功能模块GridView **/
	@BindView(R.id.gridView_functionModel)
	 GridView gridView_functionModel;
	/** 功能模块数据 **/
	private List<FunctionTypeModel> listData_FunctionType;
	/** 功能模块适配器 **/
	private AdapterFunctionType adapterFunctionType;
	/**疯狂秒杀外围的控件**/
	@BindView(R.id.relativeLayout_buyCrazy)
	RelativeLayout relativeLayout_buyCrazy;
	/** 疯狂秒杀GridView **/
	@BindView(R.id.gridView_buyCrazy)
	GridView gridView_buyCrazy;
	/** 疯狂秒杀数据 **/
	private List<GoodsSportModel> listData_buyCrazy;
	/** 疯狂秒杀适配器 **/
	private AdapterBuyCrazy adapterBuyCrazy;

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

	
	/**主题街外围的控件**/
	@BindView(R.id.tv_themeType)
	 TextView tv_themeType;
	/** 主题街gridview_border01 **/
	@BindView(R.id.gridView_themeType01)
	 GridView gridView_themeType01;
	/** 主题街gridview_border01数据 **/
	private List<ThemeTypeModel> listData_themeType01;
	/** 主题街gridview_border01适配器 **/
	private AdapterThemeType01 adapterThemeType01;

	/** 主题街gridView02 **/
	@BindView(R.id.gridView_themeType02)
	 GridView gridView_themeType02;
	/** 主题街gridView02数据 **/
	private List<ThemeTypeModel> listData_themeType02;
	/** 主题街gridView02适配器 **/
	private AdapterThemeType02 adapterThemeType02;

	/**好店推荐**/
	@BindView(R.id.tv_niceShopPush)
	 TextView tv_niceShopPush;
	/** 好店推荐gridView **/
	@BindView(R.id.gridView_niceShopPush)
	 GridView gridView_niceShopPush;
	/** 好店推荐数据 **/
	private List<NiceShopPushModel> listData_niceShopPush;
	/** 好店推荐适配器 **/
	private AdapterNiceShopPush adapterNiceShopPush;

	/**新品推荐外围的控件**/
	@BindView(R.id.relativeLayout_moreNewGoodsData)
	 RelativeLayout relativeLayout_moreNewGoodsData;
	/** 更多新品推荐 **/
	@BindView(R.id.tv_moreNewGoodsData)
	 TextView tv_moreNewGoodsData;
	/** 新品推荐gridView **/
	@BindView(R.id.gridView_newGoodsPush)
	 GridView gridView_newGoodsPush;
	/** 新品推荐数据 **/
	private List<GoodsGeneralModel> listData_newGoodsPush;
	/**新品推荐适配器**/
	AdapterNewGoodsPush adapterNewGoodsPush;

	@BindView(R.id.iv_advertisement_01)
	ImageView iv_advertisement_01;

	@BindView(R.id.iv_advertisement_02)
	ImageView iv_advertisement_02;

	/** 当前界面内容 **/
	private View rootView;
    /**当前服务器时间**/
    private long time_current;
    private int MESSAGE_01 = 1;
	/**是否包含活动商品**/
	private boolean IfHaveSportGoods = false;
	//这里很重要，使用Handler的延时效果，每隔一秒刷新一下适配器，以此产生倒计时效果
    private Handler handler_timeCurrent = new Handler(){
        @Override
        public void handleMessage(Message msg) {
        	time_current = time_current+1000;
        	adapterBuyCrazy.notifyDataSetChanged();
//        	adapterGoodsByGroupBuy.notifyDataSetChanged();
//        	adapterSaleByTimeLimited.notifyDataSetChanged();
//        	LogUtil.LogShitou("商品秒杀当前时间"+System.currentTimeMillis()/1000);
            handler_timeCurrent.sendEmptyMessageDelayed(MESSAGE_01,1000);

        }
    };


	@Override
	public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(rootView == null){
			rootView = inflater.inflate(R.layout.fragment_home_dainifei, container, false);
			unbinder = ButterKnife.bind(this, rootView);
			String cityName = MyApplication.getmCustomModel(mActivity).getLocSiteName();
			if(StringUtil.isEmpty(cityName)){
				cb_location.setText("杭州站");
			}else{
				cb_location.setText(cityName);
			}
			swipeRefreshLayout.setColorSchemeColors( Color.RED
					,Color.GREEN
					,Color.BLUE
					,Color.YELLOW
					,Color.CYAN
					,0xFFFE5D14
					,Color.MAGENTA);
			swipeRefreshLayout.setOnRefreshListener(this);
			//设置城市战选择和搜索
//		cb_location.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//			@Override
//			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//				if(isChecked){
//					RotateAnimation ra01 = new RotateAnimation(0f, 180f,
//					Animation.RELATIVE_TO_SELF, 0.5f,
//					Animation.RELATIVE_TO_SELF, 0.5f);
//					ra01.setDuration(300);
//					ra01.setFillAfter(true);
//					iv_location.startAnimation(ra01);
//					//TODO
//					showPopuWindow();
//				}else{
//					RotateAnimation ra02 = new RotateAnimation(180f, 0f,
//							Animation.RELATIVE_TO_SELF, 0.5f,
//							Animation.RELATIVE_TO_SELF, 0.5f);
//					ra02.setDuration(300);
//					ra02.setFillAfter(true);
//					iv_location.startAnimation(ra02);
//				}
//			}
//		});
			et_searchContext.setInputType(InputType.TYPE_NULL);
			et_searchContext.setOnClickListener(this);
			btn_search.setOnClickListener(this);
			linearLayout_goodsByGroupBuy.setOnClickListener(this);
			linearLayout_moreSalesByTimeLimited.setOnClickListener(this);
			tv_moreNewGoodsData.setOnClickListener(this);
			//设置轮播图的宽高
			rollViewpager.setLayoutParams(new LinearLayout.LayoutParams(
					mActivity.displayDeviceWidth, mActivity.displayDeviceWidth*31/72));

			// 功能模块数据
			listData_FunctionType = new ArrayList<FunctionTypeModel>();
			listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_buy_group, "精品团购"));
			listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_buy_time_limit, "限时抢购"));
			listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_setting_center, "设置中心"));
			listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_my_agent_goods, "我的代理"));
			listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_my_order, "我的订单"));
			listData_FunctionType.add(new FunctionTypeModel(R.drawable.function_type_my_shop, "我的店铺"));
			adapterFunctionType = new AdapterFunctionType(mActivity,
					listData_FunctionType);
			gridView_functionModel.setAdapter(adapterFunctionType);

			//初始化整点秒杀
			listData_buyCrazy = new ArrayList<GoodsSportModel>();
			adapterBuyCrazy = new AdapterBuyCrazy(mActivity, listData_buyCrazy);
			gridView_buyCrazy.setAdapter(adapterBuyCrazy);
			gridView_buyCrazy.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					int PlatformActionID = listData_buyCrazy.get(position).getPlatformActionID();
					Intent intent = new Intent(mActivity, GoodsDetailSportActivity.class);
					intent.putExtra(InformationCodeUtil.IntentPlatformActionID, PlatformActionID);
					intent.putExtra(InformationCodeUtil.IntentPlatformActionType,
							InformationCodeUtil.PlatformActionType_MesKill);
					mActivity.startActivity(intent);
				}
			});

			//初始化团购中心
			listData_goodsByGroupBuy = new ArrayList<GoodsSportModel>();
			adapterGoodsByGroupBuy = new AdapterGoodsByGroupBuy(mActivity, listData_goodsByGroupBuy);
			listView_goodsByGroupBuy.setAdapter(adapterGoodsByGroupBuy);
			listView_goodsByGroupBuy.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					int PlatformActionID = listData_goodsByGroupBuy.get(position).getPlatformActionID();
					Intent intent = new Intent(mActivity, GoodsDetailSportActivity.class);
					intent.putExtra(InformationCodeUtil.IntentPlatformActionID, PlatformActionID);
					intent.putExtra(InformationCodeUtil.IntentPlatformActionType,
							InformationCodeUtil.PlatformActionType_GroupCentre);
					mActivity.startActivity(intent);
				}
			});

			//初始化限时抢购
			listData_saleByTimeLimited = new ArrayList<GoodsSportModel>();
			adapterSaleByTimeLimited = new AdapterSalesByTimeLimited(mActivity, listData_saleByTimeLimited);
			gridView_saleByTimeLimited.setAdapter(adapterSaleByTimeLimited);
			gridView_saleByTimeLimited.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					int PlatformActionID = listData_saleByTimeLimited.get(position).getPlatformActionID();
					Intent intent = new Intent(mActivity, GoodsDetailSportActivity.class);
					intent.putExtra(InformationCodeUtil.IntentPlatformActionID, PlatformActionID);
					intent.putExtra(InformationCodeUtil.IntentPlatformActionType,
							InformationCodeUtil.PlatformActionType_SaleByTimeLimited);
					mActivity.startActivity(intent);
				}
			});

			//初始化好店推荐
			listData_niceShopPush = new ArrayList<NiceShopPushModel>();
			adapterNiceShopPush = new AdapterNiceShopPush(mActivity, listData_niceShopPush);
			gridView_niceShopPush.setAdapter(adapterNiceShopPush);
			gridView_niceShopPush.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					Intent intent = new Intent(mActivity,ShopActivity.class);
					intent.putExtra(InformationCodeUtil.IntentSearchGoodsCurrentShopID, listData_niceShopPush.get(position).getDjLsh());
					mActivity.startActivity(intent);
				}
			});


			//初始化新品推荐数据源
			listData_newGoodsPush = new ArrayList<GoodsGeneralModel>();
			adapterNewGoodsPush = new AdapterNewGoodsPush(mActivity, listData_newGoodsPush);
			gridView_newGoodsPush.setAdapter(adapterNewGoodsPush);
			gridView_newGoodsPush.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					Intent intent  = new Intent(mActivity,GoodsDetailGeneralActivity.class);
					intent.putExtra(InformationCodeUtil.IntentGoodsID, listData_newGoodsPush.get(position).getDjLsh());
					mActivity.startActivity(intent);
				}
			});

			//初始化主题街
			listData_themeType01 = new ArrayList<ThemeTypeModel>();
			adapterThemeType01 = new AdapterThemeType01(mActivity, listData_themeType01);
			gridView_themeType01.setAdapter(adapterThemeType01);
			gridView_themeType01.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					Intent intent = new Intent(mActivity, ThemeTypeActivity.class);
					intent.putExtra(InformationCodeUtil.IntentThemeTypeName, listData_themeType01.get(position).getThemeName());
					intent.putExtra(InformationCodeUtil.IntentThemeTypeID, position);
					mActivity.startActivity(intent );
				}
			});

			listData_themeType02 = new ArrayList<ThemeTypeModel>();
			adapterThemeType02 = new AdapterThemeType02(mActivity, listData_themeType02);
			gridView_themeType02.setAdapter(adapterThemeType02);
			gridView_themeType02.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
										int position, long id) {
					Intent intent = new Intent(mActivity, ThemeTypeActivity.class);
					intent.putExtra(InformationCodeUtil.IntentThemeTypeName, listData_themeType02.get(position).getThemeName());
					intent.putExtra(InformationCodeUtil.IntentThemeTypeID, position);
					mActivity.startActivity(intent );
				}
			});
		}
		return rootView;
	}

	public void initData() {

	}


	@Override
	public void onResume() {
		super.onResume();
		swipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(true);
				onRefresh();
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		handler_timeCurrent.removeCallbacksAndMessages(null);
	}

	@Override
	public void onRefresh() {
		getData(InformationCodeUtil.methodNameHomeIndex);
	}

	private void getData(String methodName) {

		if(InformationCodeUtil.methodNameHomeIndex.equals(methodName)){
			SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mActivity).getDjLsh());
			requestSoapObject.addProperty("secKillCount", 4);
			requestSoapObject.addProperty("groupPurchaseCount", 3);
			requestSoapObject.addProperty("timePurchaseCount", 8);
			requestSoapObject.addProperty("advCount", 3);
			requestSoapObject.addProperty("hotShopCount", 8);
			requestSoapObject.addProperty("newGoodsCount", 8);
			requestSoapObject.addProperty("cityCode", MyApplication.getmCustomModel(mActivity).getLocCityCode());
			ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask
					(mActivity, this, requestSoapObject, methodName);
			connectGoodsServiceAsyncTask.initProgressDialog(false);
			connectGoodsServiceAsyncTask.execute();
			return;
		}

	}


	
	private void showPopuWindow(){
		int popHeight = mActivity.displayDeviceHeight-DensityUtil.dip2px(mActivity, 105);
//		pop = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT, popHeight, true);
		pop = new SelectCityZhanPopWindow(mActivity, LayoutParams.MATCH_PARENT, popHeight, true);
//		pop = new SelectCityLocationPop(mActivity,LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true, null);
		pop.setBackgroundDrawable(new BitmapDrawable());
		pop.setOutsideTouchable(true);
		pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
			
			@Override
			public void onDismiss() {
				cb_location.setChecked(false);
			}
		});
		pop.showAsDropDown(relativeLayout_title);
		pop.initData();
	}
	

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.linearLayout_moreSalesByTimeLimited:
			intent = new Intent(mActivity,
					SportSaleBuyTimeLimitedActivity.class);
			mActivity.startActivity(intent);
			break;
		case R.id.linearLayout_goodsByGroupBuy:
			intent = new Intent(mActivity, SportSaleBuyGroupActivity.class);
			mActivity.startActivity(intent);
			break;
		case R.id.et_searchContext:
		case R.id.btn_search:
			intent = new Intent(mActivity,
					SearchGoodsActivity.class);
			mActivity.startActivity(intent);
			break;

		default:
			break;
		}
	}

	/** 功能模块适配器 **/
	public class AdapterFunctionType extends MyBaseAdapter<FunctionTypeModel> {

		public AdapterFunctionType(Context mContext, List listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.item_adapter_function_type, null);
				holder = new ViewHolder();
				holder.iv_functionType = (ImageView) convertView
						.findViewById(R.id.iv_functionType);
				holder.tv_functionType = (TextView) convertView
						.findViewById(R.id.tv_functionType);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			FunctionTypeModel currentFunctionTypeModel = listData.get(position);
			holder.iv_functionType.setImageResource(currentFunctionTypeModel.getImageUrl());
			holder.iv_functionType.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					toOtherView(position);
				}
			});
			holder.tv_functionType.setText(currentFunctionTypeModel.getName());
//			toOtherView(position);
			return convertView;
		}

		private class ViewHolder {
			/** 功能图标 **/
			ImageView iv_functionType;
			/** 功能名称 **/
			TextView tv_functionType;
		}
	}
	
	/**功能模块  快捷跳转到别的界面**/
	private void toOtherView(int position) {
		Intent intent = null;
		switch (position) {
			//精品团购
		case 0:
			intent = new Intent(mActivity, SportSaleBuyGroupActivity.class);
			mActivity.startActivity(intent);	
			break;
			//限时抢购
		case 1:
			intent = new Intent(mActivity,SportSaleBuyTimeLimitedActivity.class);
			mActivity.startActivity(intent);			
			break;
			//设置中心
		case 2:
			intent = new Intent(mActivity,SettingCenterActivity.class);
			mActivity.startActivity(intent);			
			break;
			//我的代理
		case 3:
			intent = new Intent(mActivity,SearchGoodsMyAgentActivity.class);
			mActivity.startActivity(intent);			
			break;
			//我的订单
		case 4:
			intent = new Intent(mActivity,MyOrderActivity.class);
			mActivity.startActivity(intent);			
			break;
			//我的店铺
		case 5:
			intent = new Intent(mActivity,MyShopWebActivity.class);
			mActivity.startActivity(intent);			
			break;

		default:
			break;
		}
	}

	/****
	 * 整点秒杀适配器
	 * 
	 * @author SHI 2016-3-2 16:12:21
	 */
	public class AdapterBuyCrazy extends MyBaseAdapter<GoodsSportModel> {

		public AdapterBuyCrazy(Context mContext, List<GoodsSportModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.item_adapter_sport_sale_by_crazy01, null);
				holder = new ViewHolder();
				holder.tv_hour = (TextView) convertView.findViewById(R.id.tv_hour);
				holder.tv_minute = (TextView) convertView.findViewById(R.id.tv_minute);
				holder.tv_second = (TextView) convertView.findViewById(R.id.tv_second);
				holder.linearLayout_timeToBeginBuyCray = (LinearLayout)
						convertView.findViewById(R.id.linearLayout_timeToBeginBuyCray);
				holder.iv_productImage = (ImageView) convertView
						.findViewById(R.id.iv_productImage);
				holder.tv_productName = (TextView) convertView
						.findViewById(R.id.tv_productName);
				holder.tv_productPrice = (TextView) convertView
						.findViewById(R.id.tv_productPrice);
				holder.tv_productOriginalPrice = (TextView) convertView
						.findViewById(R.id.tv_productOriginalPrice);
				holder.btn_buyCrazy = (Button) convertView
						.findViewById(R.id.btn_buyCrazy);
				holder.tv_crazyBuying = (TextView) convertView
						.findViewById(R.id.tv_crazyBuying);
				holder.iv_IfBuyCrazyState = (ImageView) convertView
						.findViewById(R.id.iv_IfBuyCrazyState);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final GoodsSportModel currentGoodsInfoActModel = listData.get(position);
//			LogUtil.LogShitou("整点秒杀", currentGoodsInfoActModel.toString());
			//更新倒计时控件times_current
			updateTextView(holder,currentGoodsInfoActModel);

			if(currentGoodsInfoActModel.getImages() != null && currentGoodsInfoActModel.getImages().size()>0) {
			ImagerLoaderUtil.getInstance(mContext).displayMyImage(
					currentGoodsInfoActModel.getImages().get(0), holder.iv_productImage);
			}

			holder.tv_productName.setText(currentGoodsInfoActModel.getGoodsName());
			holder.tv_productPrice.setText("￥"
					+ (int)currentGoodsInfoActModel.getPrice());
			holder.tv_productOriginalPrice.setText("￥"+(int)currentGoodsInfoActModel.getOriginalPrice());
		    holder.tv_productOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		    
			holder.btn_buyCrazy.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					int PlatformActionID = currentGoodsInfoActModel.getPlatformActionID();
					Intent intent = new Intent(mActivity, GoodsDetailSportActivity.class);
					intent.putExtra(InformationCodeUtil.IntentPlatformActionID, PlatformActionID);
					intent.putExtra(InformationCodeUtil.IntentPlatformActionType,
							InformationCodeUtil.PlatformActionType_MesKill);
					startActivity(intent);
				}
			});

			return convertView;
		}
		
		/****
		 * 刷新整点秒杀倒计时控件,这个时候活动截至时间还未到
		 */
		public void updateTextView(ViewHolder holder,GoodsSportModel mGoodsInfoActModel) {
			long time_begin = TimeUtil.getTimeDate(mGoodsInfoActModel.getBeginTime()).getTime();
			long time_end = TimeUtil.getTimeDate(mGoodsInfoActModel.getEndTime()).getTime();
//			LogUtil.LogShitou("整点秒杀当前时间", ""+time_current);
//			LogUtil.LogShitou("整点秒杀结束时间", ""+time_end);		
			//活动截至时间还未到
			if(time_end > time_current ){
				long time_remains = time_begin - time_current;
				if (time_remains <= 0) {
					if(mGoodsInfoActModel.isIsRunning()){
						//活动正在进行，可以秒杀
						holder.btn_buyCrazy.setEnabled(true);
						holder.btn_buyCrazy.setText(R.string.clickMesKill);
						holder.tv_crazyBuying.setVisibility(View.VISIBLE);
						holder.linearLayout_timeToBeginBuyCray.setVisibility(View.INVISIBLE);
						holder.iv_IfBuyCrazyState.setVisibility(View.INVISIBLE);
					}else{
						//活动已经结束,因为商品被抢光
						holder.btn_buyCrazy.setEnabled(false);
						holder.btn_buyCrazy.setText(R.string.haveFinishRob);
						holder.tv_crazyBuying.setVisibility(View.INVISIBLE);
						holder.linearLayout_timeToBeginBuyCray.setVisibility(View.VISIBLE);
						holder.iv_IfBuyCrazyState.setVisibility(View.VISIBLE);
						holder.iv_IfBuyCrazyState.setImageResource(R.drawable.icon_buy_crazy_clear);
					}
					holder.tv_hour.setText("00");
					holder.tv_minute.setText("00");
					holder.tv_second.setText("00");
					return;
				}
				//活动即将开始
				holder.btn_buyCrazy.setEnabled(false);
				holder.btn_buyCrazy.setText(R.string.justToBegin);
				holder.tv_crazyBuying.setVisibility(View.INVISIBLE);
				holder.iv_IfBuyCrazyState.setVisibility(View.INVISIBLE);
				holder.linearLayout_timeToBeginBuyCray.setVisibility(View.VISIBLE);
				//秒钟
				long time_second = (time_remains/1000)%60;
				String str_second;
				if (time_second < 10) {
					str_second = "0" + time_second;
				} else {
					str_second = "" + time_second;
				}
				long time_temp = ((time_remains / 1000) - time_second) / 60;
				//分钟
				long time_minute = time_temp % 60;
				String str_minute;
				if (time_minute < 10) {
					str_minute = "0" + time_minute;
				} else {
					str_minute = "" + time_minute;
				}
				time_temp = (time_temp - time_minute) / 60;
				//小时
				long time_hour = time_temp;
				String str_hour;
				if (time_hour < 10) {
					str_hour = "0" + time_hour;
				} else {
					str_hour = "" + time_hour;
				}	
				holder.tv_hour.setText(str_hour);
				holder.tv_minute.setText(str_minute);
				holder.tv_second.setText(str_second);				
			}else{
				//活动已经结束,因为活动截至时间到了
				holder.btn_buyCrazy.setEnabled(false);
				holder.btn_buyCrazy.setText(R.string.haveFinish);
				holder.tv_crazyBuying.setVisibility(View.INVISIBLE);
				holder.linearLayout_timeToBeginBuyCray.setVisibility(View.VISIBLE);
				holder.iv_IfBuyCrazyState.setVisibility(View.VISIBLE);
				holder.iv_IfBuyCrazyState.setImageResource(R.drawable.icon_buy_crazy_finished);
			}
			

		}

		private class ViewHolder {
			/** 剩余开抢小时 **/
			TextView tv_hour;
			/** 剩余开抢分钟 **/
			TextView tv_minute;
			/** 剩余开抢秒钟**/
			TextView tv_second;
			/**剩余时间外围布局控件**/
			LinearLayout linearLayout_timeToBeginBuyCray;
			/** 商品图片 **/
			ImageView iv_productImage;
			/** 商品名称 **/
			TextView tv_productName;
			/** 商品价格 **/
			TextView tv_productPrice;
			TextView tv_productOriginalPrice;
			/** 秒杀按钮 **/
			Button btn_buyCrazy;
			/**是否正在疯狂秒杀图标**/
			TextView tv_crazyBuying;
			/** 商品是否已抢光或已结束 **/
			ImageView iv_IfBuyCrazyState;
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
							Intent intent = new Intent(mActivity, GoodsDetailSportActivity.class);
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
					Intent intent = new Intent(mActivity, GoodsDetailSportActivity.class);
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
	 * 主题街内容对象
	 * @author SHI
	 *
	 */
	public class ThemeTypeModel {

		/**主题街类型名称**/
		private String ThemeName;
		
		/**主题街类型代表商品名称**/
		private String ProductName;
		
		/**商品图片Url**/
		private String ProductImageUrl;
		
		public ThemeTypeModel(String themeName, String productName,
			 String productImageUrl) {
			super();
			ThemeName = themeName;
			ProductName = productName;
			ProductImageUrl = productImageUrl;
		}

		public String getThemeName() {
			return ThemeName;
		}

		public void setThemeName(String themeName) {
			ThemeName = themeName;
		}

		public String getProductName() {
			return ProductName;
		}

		public void setProductName(String productName) {
			ProductName = productName;
		}

		public String getProductImageUrl() {
			return ProductImageUrl;
		}

		public void setProductImageUrl(String productImageUrl) {
			ProductImageUrl = productImageUrl;
		}
		
		
		
	}
	/****
	 * 主题街GridView适配器01
	 * 
	 * @author SHI 2016-3-3 11:07:14
	 */
	public class AdapterThemeType01 extends MyBaseAdapter<ThemeTypeModel> {

		public AdapterThemeType01(Context mContext,
				List<ThemeTypeModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			if (convertView == null) {

				convertView = View.inflate(mContext,
						R.layout.item_adapter_theme_type01, null);
				holder = new ViewHolder();

				holder.iv_productImage = (ImageView) convertView
						.findViewById(R.id.iv_productImage);
				holder.tv_themeTypeName = (TextView) convertView
						.findViewById(R.id.tv_themeTypeName);
				holder.tv_productName = (TextView) convertView
						.findViewById(R.id.tv_productName);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ThemeTypeModel currentThemeTypeModel = listData.get(position);
			ImagerLoaderUtil.getInstance(mContext).displayMyImage(
					currentThemeTypeModel.getProductImageUrl(),
					holder.iv_productImage);
			holder.tv_themeTypeName.setText(currentThemeTypeModel
					.getThemeName());
			holder.tv_productName.setText(currentThemeTypeModel
					.getProductName());

			return convertView;
		}

		private class ViewHolder {

			/** 主图街类型 **/
			TextView tv_themeTypeName;
			/** 商品图片 **/
			ImageView iv_productImage;
			/** 商品名称 **/
			TextView tv_productName;
		}
	}

	/****
	 * 主题街GridView适配器01
	 * 
	 * @author SHI 2016-3-3 11:07:14
	 */
	public class AdapterThemeType02 extends MyBaseAdapter<ThemeTypeModel> {

		public AdapterThemeType02(Context mContext,
				List<ThemeTypeModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {

				convertView = View.inflate(mContext,
						R.layout.item_adapter_theme_type02, null);
				holder = new ViewHolder();
				holder.iv_productImage = (ImageView) convertView
						.findViewById(R.id.iv_productImage);
				holder.tv_themeTypeName = (TextView) convertView
						.findViewById(R.id.tv_themeTypeName);
				holder.tv_productName = (TextView) convertView
						.findViewById(R.id.tv_productName);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			ThemeTypeModel currentThemeTypeModel = listData.get(position);
			ImagerLoaderUtil.getInstance(mContext).displayMyImage(
					currentThemeTypeModel.getProductImageUrl(),
					holder.iv_productImage);
			String typeName = currentThemeTypeModel.getThemeName();
			holder.tv_themeTypeName.setText(typeName);
			holder.tv_productName.setText(currentThemeTypeModel
					.getProductName());

			return convertView;
		}

		private class ViewHolder {
			/** 主图街类型 **/
			TextView tv_themeTypeName;
			/** 商品图片 **/
			ImageView iv_productImage;
			/** 商品名称 **/
			TextView tv_productName;
		}
	}

	/****
	 * 好店推荐适配器
	 * 
	 * @author SHI 2016-3-3 15:08:46
	 */
	public class AdapterNiceShopPush extends MyBaseAdapter<NiceShopPushModel> {


		public AdapterNiceShopPush(Context mContext,
				List<NiceShopPushModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView imageView;
			if (convertView == null) {
				convertView = View.inflate(mContext, R.layout.item_gridview,null);
				imageView = (ImageView) convertView
						.findViewById(R.id.ico_imageView);
				convertView.setTag(imageView);
			} else {
				imageView = (ImageView) convertView.getTag();
			}
			ImagerLoaderUtil.getInstance(mContext).displayMyImage(
					listData.get(position).getLogoUrl(), imageView);
			return convertView;
		}
	}

	/**好点推荐Model对象**/
	private class NiceShopPushModel{
		private int DjLsh;
		private String LogoUrl;
		private String PhoneNum;
		private String ShopName;
		private int UserID;

		public int getDjLsh() {
			return DjLsh;
		}

		public void setDjLsh(int djLsh) {
			DjLsh = djLsh;
		}

		public String getLogoUrl() {
			return LogoUrl;
		}

		public void setLogoUrl(String logoUrl) {
			LogoUrl = logoUrl;
		}

		public String getPhoneNum() {
			return PhoneNum;
		}

		public void setPhoneNum(String phoneNum) {
			PhoneNum = phoneNum;
		}

		public String getShopName() {
			return ShopName;
		}

		public void setShopName(String shopName) {
			ShopName = shopName;
		}

		public int getUserID() {
			return UserID;
		}

		public void setUserID(int userID) {
			UserID = userID;
		}
	}
	
	/**
	 * @author SHI
	 * 2016-2-17 15:34:28
	 * 新品推荐适配器
	 */
	public class AdapterNewGoodsPush extends MyBaseAdapter<GoodsGeneralModel> {

		public AdapterNewGoodsPush(Context mContext, List<GoodsGeneralModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView == null){
				convertView = View.inflate(mContext, R.layout.item_adapter_newgoods_push, null);
				holder = new ViewHolder();
				holder.iv_specialPricesGoods = (ImageView) convertView.findViewById(R.id.iv_specialPricesGoods);
				holder.tv_specialPricesGoodsName = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsName);
				holder.tv_specialPricesGoodsPrices = (TextView) convertView.findViewById(R.id.tv_specialPricesGoodsPrices);
				holder.btn_seeDataDetail = (Button) convertView.findViewById(R.id.btn_seeDataDetail);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			final GoodsGeneralModel currentProductModel = listData.get(position);
			ImagerLoaderUtil.getInstance(mContext).displayMyImage(currentProductModel.getImgUrl(), holder.iv_specialPricesGoods);
			holder.tv_specialPricesGoodsName.setText(currentProductModel.getGoodsName());
			holder.tv_specialPricesGoodsPrices.setText("￥"+(int)currentProductModel.getMinPrice());
			holder.btn_seeDataDetail.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent  = new Intent(mActivity,GoodsDetailGeneralActivity.class);
					intent.putExtra(InformationCodeUtil.IntentGoodsID, currentProductModel.getDjLsh());
					mActivity.startActivity(intent);					
				}
			});
			return convertView;
		}
		
		private class ViewHolder{
			/**特价商品图标**/
			ImageView iv_specialPricesGoods;
			/**特价商品名称**/
			TextView tv_specialPricesGoodsName;
			/**特价商品价格**/
			TextView tv_specialPricesGoodsPrices;
			/**查看详情**/
			Button btn_seeDataDetail;
		}

	}
	
	
	/** 设置整点秒杀GirdView的宽和高,使其能够横向滑动 */
	public void setBuyCrazyGridView(List returnModel, GridView gridView) {
		int size = returnModel.size();
		if(size == 0){
			return;
		}
		// 设置GridView子空间宽为屏幕宽度的二分之一
		int itemWidth = mActivity.displayDeviceWidth/2;
		int gridViewWidth = size * itemWidth;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridViewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
		gridView.setColumnWidth(itemWidth); // 设置列表项宽
		gridView.setHorizontalSpacing(1); // 设置列表项水平间距
		gridView.setStretchMode(GridView.NO_STRETCH);// spacingWidthUniform
		gridView.setNumColumns(size); // 设置列数量=列表集合数
	}
	
	/** 设置限时抢购GirdView的宽和高,使其能够横向滑动 */
	public void setSalesByTimeLimitedGridView(List returnModel, GridView gridView) {

		int size = returnModel.size();
		if(size == 0){
			return;
		}
		int itemWidth = mActivity.displayDeviceWidth*2/5;
		int gridViewWidth = size * itemWidth;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridViewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		gridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
		gridView.setColumnWidth(itemWidth); // 设置列表项宽
		gridView.setHorizontalSpacing(1); // 设置列表项水平间距
		gridView.setStretchMode(GridView.NO_STRETCH);// spacingWidthUniform
		gridView.setNumColumns(size); // 设置列数量=列表集合数
	}
	
	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		if(InformationCodeUtil.methodNameHomeIndex.equals(methodName)){
			try {
				iv_advertisement_01.setVisibility(View.GONE);
				iv_advertisement_02.setVisibility(View.GONE);
				returnString = getResources().getString(R.string.String_HomeIndex);
				JSONObject jsonObject= new JSONObject(returnString);
				refreshHomeSportGoods(jsonObject.getString("ActList"));
				refreshRollView(jsonObject.getString("AdvList"));
				refreshNewProductsRecommendation(jsonObject.getString("NewGoodsList"));
				iv_advertisement_01.setVisibility(View.VISIBLE);
				iv_advertisement_02.setVisibility(View.VISIBLE);
				//刷新主题街
//			tv_themeType.setVisibility(View.VISIBLE);
//			listData_themeType01.clear();
//			listData_themeType01.add(new ThemeTypeModel(
//					"电脑整机", "微软电脑 Surface Pro", "drawable://"+R.drawable.imageview_a01));
//			listData_themeType01.add(new ThemeTypeModel(
//					"手机通讯", "华为p8 4G 手机", "drawable://"+R.drawable.imageview_a02));
//			adapterThemeType01.notifyDataSetChanged();
//
//			listData_themeType02.clear();
//			listData_themeType02.add(new ThemeTypeModel(
//					"办公设备", "吉米(XG) z3", "drawable://"+R.drawable.imageview_b01));
//			listData_themeType02.add(new ThemeTypeModel(
//					"DIY 耗材/配件", "inter 酷睿 i3 CPU", "drawable://"+R.drawable.imageview_b02));
//			listData_themeType02.add(new ThemeTypeModel(
//					"手机配件", "倍斯特 10400 毫安 移动电源", "drawable://"+R.drawable.imageview_b03));
//			listData_themeType02.add(new ThemeTypeModel(
//					"网络设备", "TP_Link TL_WDR&400 路由器", "drawable://"+R.drawable.imageview_b04));
//			listData_themeType02.add(new ThemeTypeModel(
//					"二手市场", "办公设备", "drawable://"+R.drawable.imageview_b05));
//			listData_themeType02.add(new ThemeTypeModel(
//					"尾货清仓", "闪迪酷悠 64GB USB3.0 U盘", "drawable://"+R.drawable.imageview_b06));
//			adapterThemeType02.notifyDataSetChanged();
//
//			//刷新好店推荐
//			Gson gson = new Gson();
//			tv_niceShopPush.setVisibility(View.GONE);
//			JSONArray jsonArray = jsonObject.getJSONArray("HotShopList");
//			List<NiceShopPushModel> listData_tempNiceShopPush = gson.fromJson(jsonArray.toString(), new TypeToken<List<NiceShopPushModel>>(){}.getType());
//			listData_niceShopPush.clear();
//			listData_niceShopPush.addAll(listData_tempNiceShopPush);
//			adapterNiceShopPush.notifyDataSetChanged();
//			tv_niceShopPush.setVisibility(View.VISIBLE);


			} catch (Exception e) {
				e.printStackTrace();
			}
			swipeRefreshLayout.setRefreshing(false);
		}
	}

	/**刷新活动商品**/
	private void refreshHomeSportGoods(String strActs) {
		try {
			Gson gson = new Gson();
			relativeLayout_buyCrazy.setVisibility(View.GONE);
			relativeLayout_goodsByGroupBuy.setVisibility(View.GONE);
			relativeLayout_salesByTimeLimited.setVisibility(View.GONE);
			handler_timeCurrent.removeCallbacksAndMessages(null);
			IfHaveSportGoods = false;
			listData_buyCrazy.clear();
			listData_goodsByGroupBuy.clear();
			listData_saleByTimeLimited.clear();
			//JSONObject负责解决获取JSON键值对
			JSONArray jsonArray = new JSONArray(strActs);
			//循环遍历活动商品数组内容
			for (int i = 0; i < jsonArray.length(); i++) {
				//使用泛型解析JSON数据，主要用到了TypeToken这个对象
				JSONResultBaseModel<GoodsSportModel> mJSONResultModel = gson.fromJson
						(jsonArray.get(i).toString(), new TypeToken<JSONResultBaseModel<GoodsSportModel>>() {
						}.getType());
				if ("整点秒杀".equals(mJSONResultModel.getTitle())) {
					listData_buyCrazy.addAll(mJSONResultModel.getList());
					relativeLayout_buyCrazy.setVisibility(View.VISIBLE);
					setBuyCrazyGridView(listData_buyCrazy, gridView_buyCrazy);
					IfHaveSportGoods = true;
					time_current = TimeUtil.getTimeDate(listData_buyCrazy.get(0).getPresentTime()).getTime();
				}
				if ("精品团购".equals(mJSONResultModel.getTitle())) {
					listData_goodsByGroupBuy.addAll(mJSONResultModel.getList());
					relativeLayout_goodsByGroupBuy.setVisibility(View.VISIBLE);
					time_current = TimeUtil.getTimeDate(listData_goodsByGroupBuy.get(0).getPresentTime()).getTime();
				}
				if ("限时抢购".equals(mJSONResultModel.getTitle())) {
					listData_saleByTimeLimited.addAll(mJSONResultModel.getList());
					relativeLayout_salesByTimeLimited.setVisibility(View.VISIBLE);
					setSalesByTimeLimitedGridView(listData_saleByTimeLimited, gridView_saleByTimeLimited);
					time_current = TimeUtil.getTimeDate(listData_saleByTimeLimited.get(0).getPresentTime()).getTime();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		adapterBuyCrazy.notifyDataSetChanged();
		adapterGoodsByGroupBuy.notifyDataSetChanged();
		adapterSaleByTimeLimited.notifyDataSetChanged();
		if(IfHaveSportGoods){
			handler_timeCurrent.sendEmptyMessageDelayed(MESSAGE_01,100);
		}
	}

	private void refreshRollView(String strRollData){
		Gson gson = new Gson();
		//刷新首页广告轮播图
		if(listData_rollView == null){
			List<AdvertisementModel> listData = gson.fromJson
					(strRollData, new TypeToken<List<AdvertisementModel>>(){}.getType());
			listData_rollView = new ArrayList<String>();

			for (int i = 0; i < listData.size(); i++) {
				listData_rollView.add(listData.get(i).getImgUrl());
			}

			rollViewpager.setImageUris(listData_rollView);
			rollViewpager.startRoll();
		}
	}
	

	/**
	 * 刷新新品推荐内容
	 **/
	private void refreshNewProductsRecommendation(String strNewGoodsList) {
		try {
			relativeLayout_moreNewGoodsData.setVisibility(View.GONE);
			listData_newGoodsPush.clear();
			Gson gson = new Gson();
			JSONResultBaseModel<GoodsGeneralModel> mJSONResultModel = gson.fromJson
                    (strNewGoodsList, new TypeToken<JSONResultBaseModel<GoodsGeneralModel>>(){}.getType());
			List<GoodsGeneralModel> listData_tempNewGoodsPush = mJSONResultModel.getList();
			listData_newGoodsPush.addAll(listData_tempNewGoodsPush);
			relativeLayout_moreNewGoodsData.setVisibility(View.VISIBLE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		adapterNewGoodsPush.notifyDataSetChanged();
	}



	@Override
	public void connectServiceFailed(String returnStrError, String methodName, Integer state,
			boolean whetherRefresh) {
		if(InformationCodeUtil.methodNameHomeIndex.equals(methodName)){
			iv_advertisement_01.setVisibility(View.GONE);
			iv_advertisement_02.setVisibility(View.GONE);
			relativeLayout_buyCrazy.setVisibility(View.GONE);
			relativeLayout_goodsByGroupBuy.setVisibility(View.GONE);
			relativeLayout_salesByTimeLimited.setVisibility(View.GONE);	
			relativeLayout_moreNewGoodsData.setVisibility(View.GONE);
			tv_niceShopPush.setVisibility(View.GONE);
			swipeRefreshLayout.setRefreshing(false);
			ToastUtil.show(mActivity, "网络异常，数据请求失败");
		}
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		if(InformationCodeUtil.methodNameHomeIndex.equals(methodName)){
			iv_advertisement_01.setVisibility(View.GONE);
			iv_advertisement_02.setVisibility(View.GONE);
			relativeLayout_buyCrazy.setVisibility(View.GONE);
			relativeLayout_goodsByGroupBuy.setVisibility(View.GONE);
			relativeLayout_salesByTimeLimited.setVisibility(View.GONE);	
			relativeLayout_moreNewGoodsData.setVisibility(View.GONE);
			tv_niceShopPush.setVisibility(View.GONE);		
			swipeRefreshLayout.setRefreshing(false);
		}
	}


}









