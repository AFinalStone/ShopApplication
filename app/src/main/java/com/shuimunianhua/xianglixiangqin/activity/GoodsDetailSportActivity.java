package com.shuimunianhua.xianglixiangqin.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.model.GoodsGeneralModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsSportModel;
import com.shuimunianhua.xianglixiangqin.rongyun.ConversationGeneralActivity;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.ImagerLoaderUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.TimeUtils;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.rong.imlib.model.Conversation;

/**
 * @action 活动商品详情页面
 * @author SHI
 * @date 2015-7-29 下午8:35:55
 */
public class GoodsDetailSportActivity extends MyBaseActivity implements
		OnConnectServerStateListener<Integer>, OnClickListener {

	/** 返回控件 **/
	@Bind(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/** 标题 **/
	@Bind(R.id.tv_title)
	 TextView tv_title;
	@Bind(R.id.scrollView)
	 ScrollView scrollView;
	/** 商品图片 **/
	@Bind(R.id.iv_goodsImages)
	 ImageView iv_goodsImages;
	/** 聊天控件 **/
	@Bind(R.id.iv_openConverSation)
	 ImageView iv_openConverSation;
	/** 商品名称 **/
	@Bind(R.id.tv_goodsName)
	 TextView tv_goodsName;
	/** 商品价格 **/
	@Bind(R.id.tv_goodsNewPrices)
	 TextView tv_goodsNewPrices;
	/** 商品原来价格 **/
	@Bind(R.id.tv_goodsOriginalPrice)
	 TextView tv_goodsOriginalPrice;
	/** 可使用的飞币 **/
	@Bind(R.id.tv_flyCoinCanUsed)
	 TextView tv_flyCoinCanUsed;
	/** 秒杀开始时间 **/
	@Bind(R.id.tv_timeToBegin)
	 TextView tv_timeToBegin;

	/** 疯狂秒杀 **/
	@Bind(R.id.linearLayout_sportMesKill)
	 LinearLayout linearLayout_sportMesKill;
	/** 倒计时小时 **/
	@Bind(R.id.tv_hour)
	 TextView tv_hour;
	/** 倒计时分钟 **/
	@Bind(R.id.tv_minute)
	 TextView tv_minute;
	/** 倒计时秒钟 **/
	@Bind(R.id.tv_second)
	 TextView tv_second;

	/** 团购中心 **/
	@Bind(R.id.linearLayout_sportGroup)
	 LinearLayout linearLayout_sportGroup;
	/** 已参团数量和成团最低数量 **/
	@Bind(R.id.tv_numHaveJoinGroup)
	 TextView tv_numHaveJoinGroup;
	/** 活动结束时间 **/
	@Bind(R.id.tv_timeToFinish)
	 TextView tv_timeToFinish;

	/** 限时抢购 **/
	@Bind(R.id.linearLayout_sportTimeLimited)
	 LinearLayout linearLayout_sportTimeLimited;
	/** 已抢数量 **/
	@Bind(R.id.tv_numHaveRob)
	 TextView tv_numHaveRob;
	/** 活动距离结束的时间 **/
	@Bind(R.id.tv_timeRemain)
	 TextView tv_timeRemain;

	/** 选择套餐 **/
	@Bind(R.id.relativeLayout_selectPackageType)
	 RelativeLayout relativeLayout_selectPackageType;
	/** 商品介绍 商品参数 包装售后 **/
	@Bind(R.id.rg_goodsGroup)
	 RadioGroup rg_goodsGroup;
	/** 商品介绍 商品参数 包装售后 **/
	@Bind(R.id.webview_goodsDetail)
	 WebView webview_goodsDetail;
	/** 疯狂秒杀按钮（疯狂秒杀） **/
	@Bind(R.id.btn_mesKill)
	 Button btn_mesKill;
	/** 团购中心按钮（马上参团） **/
	@Bind(R.id.btn_justToJoinGroup)
	 Button btn_justToJoinGroup;
	/** 限时抢购按钮（马上抢） **/
	@Bind(R.id.btn_justToRob)
	 Button btn_justToRob;

	/** 当前商品活动ID **/
	 int GoodsPlatformActionID;
	/** 当前商品活动类型ID **/
	 int GoodsPlatformActionType;
	/** 当前商品详细信息 **/
	public GoodsSportModel mGoodsInfoActModel = new GoodsSportModel();
	public GoodsGeneralModel mGoodsGeneralModel;
	/** 当倒计时剩余时间 **/
	 long time_current;

	// 这里很重要，使用Handler的延时效果，每隔一秒刷新一下适配器，以此产生倒计时效果
	 Handler handler_timeCurrent = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			time_current = time_current + 1000;
			LogUtil.LogShitou("商品秒杀当前时间" + System.currentTimeMillis() / 1000);
			// 当前界面为疯狂秒杀
			if (InformationCodeUtil.PlatformActionType_MesKill == msg.what) {
				updateMesKillTextView(time_current);
				handler_timeCurrent.sendEmptyMessageDelayed(
						InformationCodeUtil.PlatformActionType_MesKill, 1000);
				return;
			}
			// 当前界面为精品团购
			if (InformationCodeUtil.PlatformActionType_GroupCentre == msg.what) {
				updateGroupCentreTextView(time_current);
				handler_timeCurrent.sendEmptyMessageDelayed(
						InformationCodeUtil.PlatformActionType_GroupCentre, 1000);
				return;
			}
			// 当前界面为限时抢购
			if (InformationCodeUtil.PlatformActionType_SaleByTimeLitmited == msg.what) {
				updateTimeLimitedTextView(time_current);
				handler_timeCurrent.sendEmptyMessageDelayed(
						InformationCodeUtil.PlatformActionType_SaleByTimeLitmited, 1000);
				return;
			}
		}
	};
	/** webview控件 **/
	 String htmlHeader = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
			+ "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
			+ "<head>"
			+ "<meta name=\"viewport\" content=\"width=device-width,height=device-height,inital-scale=1.0,minimum-scale=1.0,maximum-scale=3.0,user-scalable=yes\" />"
			+ "<meta id=\"vp\" name=\"viewport\" content=\"width=device-width, user-scalable=yes,maximum-scale=3.0,initial-scale=1.0,minimum-scale=1.0\" />"
			+ "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
			+ "<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\" />"
			+ "<meta name=\"format-detection\" content=\"telephone=no\" />"
			+ "<style type='text/css'>"
			+ "img{max-width:100%%;height:auto;}"
			+ "</style>"
			+ "</head>"
			+ "<body style='padding:35px 10px 0 10px;margin:0 0 0 0;line-height:25px;'>";

	 String htmlRoot = "</body></html>";

	@Override
	public void initView() {
		setContentView(R.layout.activity_goods_detail_sport);
		ButterKnife.bind(mContext);
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(this);
		tv_title.setText("商品详情");
//		iv_goodsImages.setLayoutParams(new RelativeLayout.LayoutParams(
//				RelativeLayout.LayoutParams.MATCH_PARENT, displayDeviceWidth));		
		relativeLayout_selectPackageType.setOnClickListener(this);
		btn_mesKill.setOnClickListener(this);
		btn_justToJoinGroup.setOnClickListener(this);
		btn_justToRob.setOnClickListener(this);
		iv_openConverSation.setOnClickListener(this);
		rg_goodsGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// String str = "style=/"width:100%;height:auto/"";
						// String str =
						// "<style>img{ max-width:80%; height:auto;	}</style>";
						switch (checkedId) {
						// 获取商品详情描述
						case R.id.rb_goodsDesc:
							webview_goodsDetail.loadDataWithBaseURL(
									null,
									htmlHeader
											+ mGoodsInfoActModel.getGoodsDesc()
											+ htmlRoot, "text/html", "utf-8",
									null);
							break;
						// 获取商品参数
						case R.id.rb_goodsParameter:
							webview_goodsDetail.loadDataWithBaseURL(
									null,
									htmlHeader
											+ mGoodsInfoActModel.getGoodsSpec()
											+ htmlRoot, "text/html", "utf-8",
									null);
							break;
						// 获取包装售后数据
						case R.id.rb_goodsPackage:
							webview_goodsDetail.loadDataWithBaseURL(
									null,
									htmlHeader
											+ mGoodsInfoActModel.getPackage()
											+ htmlRoot, "text/html", "utf-8",
									null);
							break;

						default:
							break;
						}
					}
				});


		WebSettings webSetting = webview_goodsDetail.getSettings();
		webSetting.setLoadWithOverviewMode(true);
		webSetting.setUseWideViewPort(true);

		webSetting.setDefaultZoom(ZoomDensity.FAR);
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webSetting.setPluginState(PluginState.ON);
		// 设置 缓存模式
		webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
		// 开启 DOM storage API 功能
		webSetting.setDomStorageEnabled(true);
		// 设置是否支持缩放
		webSetting.setBuiltInZoomControls(true);
		// 设置是否支持JavaScript
		webSetting.setJavaScriptEnabled(true);
		// 允许访问文件
		webSetting.setAllowFileAccess(true);
		// 支持缩放
		webSetting.setSupportZoom(true);
		// 隐藏WebView缩放按钮
		webSetting.setDisplayZoomControls(false);

	}

	@Override
	public void initData() {
		mGoodsGeneralModel = null;
		Intent intent = getIntent();
		GoodsPlatformActionID = intent.getIntExtra(InformationCodeUtil.IntentPlatformActionID, -1);
		GoodsPlatformActionType = intent.getIntExtra(InformationCodeUtil.IntentPlatformActionType, -1);
		getData(InformationCodeUtil.methodNameGetSingleActProduct);
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		mGoodsGeneralModel = null;
		GoodsPlatformActionID = intent.getIntExtra(InformationCodeUtil.IntentPlatformActionID, -1);
		GoodsPlatformActionType = intent.getIntExtra(InformationCodeUtil.IntentPlatformActionType, -1);
//		LogUtil.LogShitou("商品编号" + GoodsPlatformActionID);
		getData(InformationCodeUtil.methodNameGetSingleActProduct);	
		super.onNewIntent(intent);
	}

	 void getData(String methodName) {
		// 获取活动商品 详情
		if (InformationCodeUtil.methodNameGetSingleActProduct
				.equals(methodName)) {
			SoapObject requestSoapObject = new SoapObject(
					ConnectServiceUtil.NAMESPACE, methodName);
			// int PlatformActionID, int PlatformActionType
			requestSoapObject.addProperty("platformActionID",
					GoodsPlatformActionID);
			requestSoapObject.addProperty("platformActionType ",
					GoodsPlatformActionType);
			ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
					mContext, this, requestSoapObject, methodName);
			connectGoodsServiceAsyncTask.execute();
		}
	}

	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		 LogUtil.LogShitou("活动商品", returnString);
		// 获取活动商品 详情成功
		if (InformationCodeUtil.methodNameGetSingleActProduct
				.equals(methodName)) {
			try {
				Gson gson = new Gson();
				mGoodsInfoActModel = null;
				mGoodsInfoActModel = gson.fromJson(
						returnString, GoodsSportModel.class);
				mGoodsInfoActModel
						.setPlatformActionType(GoodsPlatformActionType);

				mGoodsGeneralModel = getOriginalGoodsModel(mGoodsInfoActModel);
				RefrushGoodsData();
			} catch (JsonSyntaxException e) {
				ToastUtils.show(mContext, "获取宝贝详情失败");
				finish();
			}
		}
	}

	// 活动商品对象转普通商品对象没有用到的活动商品对象字段
	// /**原来的价格**/
	//  double OriginalPrice;
	// /**最小成团数**/
	//  int MinQuantity;
	// /**最大成团数**/
	//  int MaxQuantity;
	// /**是否成团**/
	//  boolean IsGrouped;
	// /**开始时间**/
	//  String BeginTime;
	// /**结束时间**/
	//  String EndTime;
	// /**当前时间**/
	//  String PresentTime;
	// /**活动类型**/
	//  int PlatformActionID;
	// /**商品价格**/
	//  double Price;
	// /**活动商品参与数量**/
	//  int Quantity;
	// /**用户限购量**/
	//  int UserQuantity;
	/**
	 * 类型转换函数
	 * 
	 * @param mGoodsInfoActModel
	 * @return
	 */
	 GoodsGeneralModel getOriginalGoodsModel(
			GoodsSportModel mGoodsInfoActModel) {
		if (mGoodsInfoActModel == null) {
			return null;
		}
		GoodsGeneralModel mProductModel = new GoodsGeneralModel();
		mProductModel.setShopPhoneNum(mGoodsInfoActModel.getShopPhoneNum());
		mProductModel.setImages(mGoodsInfoActModel.getImages());
		mProductModel.setShopName(mGoodsInfoActModel.getShopName());
		mProductModel.setAgentID(mGoodsInfoActModel.getAgentID());
		mProductModel.setClassID(mGoodsInfoActModel.getClassID());
		mProductModel.setDjLsh(mGoodsInfoActModel.getDjLsh());
		mProductModel.setMinPrice(mGoodsInfoActModel.getPrice());
		mProductModel.setMaxPrice(mGoodsInfoActModel.getPrice());
		mProductModel.setFlyCoin(mGoodsInfoActModel.getFlyCoin());
		mProductModel.setGoodsName(mGoodsInfoActModel.getGoodsName());
		mProductModel.setGoodsDesc(mGoodsInfoActModel.getGoodsDesc());
		mProductModel.setGoodsSpec(mGoodsInfoActModel.getGoodsSpec());
		mProductModel.setPackage(mGoodsInfoActModel.getPackage());
		mProductModel.setShopID(mGoodsInfoActModel.getShopID());
		mProductModel.setUserID(mGoodsInfoActModel.getUserID());
		mProductModel.setPlatformActionID(mGoodsInfoActModel
				.getPlatformActionID());
		mProductModel.setPlatformActionType(mGoodsInfoActModel
				.getPlatformActionType());
		mProductModel.setUserQuantity(mGoodsInfoActModel.getUserQuantity());
		 if(GoodsPlatformActionType == InformationCodeUtil.PlatformActionType_GroupCentre){
			 mProductModel.setStoneCount(mGoodsInfoActModel.getMaxQuantity());
		 }else{
			 mProductModel.setStoneCount(mGoodsInfoActModel.getQuantity());
		 }
		return mProductModel;
	}

	/** 收到网络数据，刷新商品详情界面 **/
	 void RefrushGoodsData() {
		if (mGoodsInfoActModel.getImages() != null
				&& mGoodsInfoActModel.getImages().size() > 0) {
			ImagerLoaderUtil.getInstance(mContext).displayMyImage(
					mGoodsInfoActModel.getImages().get(0), iv_goodsImages);
		}
		tv_goodsName.setText(mGoodsInfoActModel.getGoodsName());
		DecimalFormat dcmFmt = new DecimalFormat("0.00");
		String goodsNewPrice = dcmFmt.format(mGoodsInfoActModel.getPrice());
		String goodsOriginalPrice = dcmFmt.format(mGoodsInfoActModel
				.getOriginalPrice());
		tv_goodsNewPrices.setText("￥" + goodsNewPrice);
		tv_goodsOriginalPrice.setText("￥" + goodsOriginalPrice);
		tv_goodsOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		tv_flyCoinCanUsed
				.setText("可用" + mGoodsInfoActModel.getFlyCoin() + "飞币");
		rg_goodsGroup.check(R.id.rb_goodsDesc);

		// 开始时间
		Date date_begin = TimeUtils.getTimeDate(mGoodsInfoActModel
				.getBeginTime());
		// 结束时间
		Date date_end = TimeUtils.getTimeDate(mGoodsInfoActModel.getEndTime());
		// 当前时间
		Date date_current = TimeUtils.getTimeDate(mGoodsInfoActModel
				.getPresentTime());

		LogUtil.LogShitou("", mGoodsInfoActModel.toString());
		switch (GoodsPlatformActionType) {

		case InformationCodeUtil.PlatformActionType_MesKill:
			tv_title.setText("整点秒杀");
			linearLayout_sportMesKill.setVisibility(View.VISIBLE);
			btn_mesKill.setVisibility(View.VISIBLE);
			tv_timeToBegin.setText(TimeUtils.getTimeString(date_begin,
					new SimpleDateFormat("HH:mm")) + " 开始");
			time_current = date_current.getTime();
			updateMesKillTextView(time_current);
			handler_timeCurrent.sendEmptyMessageDelayed(
					InformationCodeUtil.PlatformActionType_MesKill, 500);
			break;
		case InformationCodeUtil.PlatformActionType_GroupCentre:
			tv_title.setText("精品团购");
			linearLayout_sportGroup.setVisibility(View.VISIBLE);
			btn_justToJoinGroup.setVisibility(View.VISIBLE);
			tv_numHaveJoinGroup.setText(mGoodsInfoActModel.getJoinNum()
					+ "件已参团{满" + mGoodsInfoActModel.getMinQuantity() + "成团}");
			if (date_end != null) {
				tv_timeToFinish.setText(TimeUtils.getTimeString(date_end,
						new SimpleDateFormat("M月dd日 HH:mm")) + "结束");
			}
			time_current = date_current.getTime();
			updateGroupCentreTextView(time_current);
			handler_timeCurrent.sendEmptyMessageDelayed(
					InformationCodeUtil.PlatformActionType_GroupCentre, 500);
			break;
		case InformationCodeUtil.PlatformActionType_SaleByTimeLitmited:
			tv_title.setText("限时抢购");
			linearLayout_sportTimeLimited.setVisibility(View.VISIBLE);
			btn_justToRob.setVisibility(View.VISIBLE);
			tv_numHaveRob
					.setText("已抢数量：" + mGoodsInfoActModel.getPurchaseNum());

			time_current = date_current.getTime();
			updateTimeLimitedTextView(time_current);
			handler_timeCurrent.sendEmptyMessageDelayed(
					InformationCodeUtil.PlatformActionType_SaleByTimeLitmited, 500);
			break;

		default:
			finish();
			break;
		}
	}

	/****
	 * 刷新整点秒杀倒计时控件
	 */
	public void updateMesKillTextView(long time_current) {

		long time_begin = TimeUtils.getTimeDate(mGoodsInfoActModel.getBeginTime()).getTime();
		long time_end = TimeUtils.getTimeDate(mGoodsInfoActModel.getEndTime())
				.getTime();
		// 活动截至时间还未到
		if (time_end > time_current) {
			long time_remains = time_begin - time_current;
			if (time_remains <= 0) {

				if (mGoodsInfoActModel.isIsRunning()) {
					// 活动开始，可以秒杀
					relativeLayout_selectPackageType.setEnabled(true);
					btn_mesKill.setEnabled(true);
					relativeLayout_selectPackageType.setEnabled(true);
					btn_mesKill.setText(R.string.clickMesKill);
				} else {
					// 活动已经结束,商品被抢光
					relativeLayout_selectPackageType.setEnabled(false);
					btn_mesKill.setEnabled(false);
					relativeLayout_selectPackageType.setEnabled(false);
					btn_mesKill.setText(R.string.haveFinishRob);
				}
				tv_hour.setText("00");
				tv_minute.setText("00");
				tv_second.setText("00");
				return;

			}

			// 活动即将开始
			relativeLayout_selectPackageType.setEnabled(false);
			btn_mesKill.setEnabled(false);
			btn_mesKill.setText(R.string.justToBegin);
			// 秒钟
			long time_second = (time_remains / 1000) % 60;
			String str_second;
			if (time_second < 10) {
				str_second = "0" + time_second;
			} else {
				str_second = "" + time_second;
			}

			long time_temp = ((time_remains / 1000) - time_second) / 60;
			// 分钟
			long time_minute = time_temp % 60;
			String str_minute;
			if (time_minute < 10) {
				str_minute = "0" + time_minute;
			} else {
				str_minute = "" + time_minute;
			}

			time_temp = (time_temp - time_minute) / 60;
			// 小时
			long time_hour = time_temp;
			String str_hour;
			if (time_hour < 10) {
				str_hour = "0" + time_hour;
			} else {
				str_hour = "" + time_hour;
			}
			tv_minute.setText(str_minute);
			tv_second.setText(str_second);
			tv_hour.setText(str_hour);

		} else {
			// 活动截至时间已经到了
			btn_mesKill.setEnabled(false);
			relativeLayout_selectPackageType.setEnabled(false);
			btn_mesKill.setText(R.string.haveFinish);
		}

	}

	/**
	 * 刷新精品团购中心数据
	 * 
	 * @param time_current
	 */
	 void updateGroupCentreTextView(long time_current) {
		LogUtil.LogShitou("结束时间", mGoodsInfoActModel.getEndTime());
		long time_begin = TimeUtils.getTimeDate(
				mGoodsInfoActModel.getBeginTime()).getTime();
		long time_end = TimeUtils.getTimeDate(mGoodsInfoActModel.getEndTime())
				.getTime();

		if (time_begin > time_current) {// 活动开始时间还未到
			btn_justToJoinGroup.setEnabled(false);
			btn_justToJoinGroup.setText(R.string.justToBegin);
			relativeLayout_selectPackageType.setEnabled(false);
		} else {
			if (time_end > time_current) {
				// 团购是否正在进行
				if (mGoodsInfoActModel.isIsRunning()) {
					btn_justToJoinGroup.setEnabled(true);
					relativeLayout_selectPackageType.setEnabled(true);
					btn_justToJoinGroup.setText(R.string.justToJoinGroup);
				} else {
					btn_justToJoinGroup.setEnabled(false);
					relativeLayout_selectPackageType.setEnabled(false);
					// 团购是否成功
					if (mGoodsInfoActModel.isIsGrouped()) {
						btn_justToJoinGroup.setText(R.string.joinGroupSuccess);
					} else {
						btn_justToJoinGroup.setText(R.string.joinGroupFailed);
					}
				}
			} else {
				btn_justToJoinGroup.setEnabled(false);
				relativeLayout_selectPackageType.setEnabled(false);
				// 团购是否成功
				if (mGoodsInfoActModel.isIsGrouped()) {
					btn_justToJoinGroup.setText(R.string.joinGroupSuccess);
				} else {
					btn_justToJoinGroup.setText(R.string.joinGroupFailed);
				}
			}
		}

	}

	/****
	 * 刷新限时抢购倒计时控件
	 */
	public void updateTimeLimitedTextView(long time_current) {
		long time_begin = TimeUtils.getTimeDate(
				mGoodsInfoActModel.getBeginTime()).getTime();
		long time_end = TimeUtils.getTimeDate(mGoodsInfoActModel.getEndTime())
				.getTime();

		if (time_begin > time_current) {// 活动开始时间还未到
			relativeLayout_selectPackageType.setEnabled(false);
			btn_justToRob.setEnabled(false);
			btn_justToRob.setText(R.string.justToBegin);
			tv_timeRemain.setText(TimeUtils.getTimeString(time_end,
					new SimpleDateFormat("M月dd日 HH:mm")) + "结束");

		} else {// 活动开始时间已经到了
			long time_remain = time_end - time_current;
			if (time_remain <= 0) {// 活动已经结束，截止时间到了
				relativeLayout_selectPackageType.setEnabled(false);
				btn_justToRob.setEnabled(false);
				btn_justToRob.setText(R.string.haveFinish);
				// tv_timeRemain.setText("剩余: 00:00:00");
				tv_timeRemain.setText(TimeUtils.getTimeString(time_end,
						new SimpleDateFormat("M月dd日 HH:mm")) + "结束");
				return;
			}

			if (mGoodsInfoActModel.isIsRunning()) {
				// 活动开始，可以秒杀
				relativeLayout_selectPackageType.setEnabled(true);
				btn_justToRob.setEnabled(true);
				btn_justToRob.setText(R.string.justToRob);
			} else {
				// 活动已经结束,商品被抢光
				relativeLayout_selectPackageType.setEnabled(false);
				btn_justToRob.setEnabled(false);
				btn_justToRob.setText(R.string.haveFinishRob);
			}
			tv_timeRemain.setText(TimeUtils.getTimeString(time_end,
					new SimpleDateFormat("M月dd日 HH:mm")) + "结束");
		}

	}

	@Override
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {
		// 获取商品 详情
		if (InformationCodeUtil.methodNameGetSingleActProduct.equals(methodName)) {
			ToastUtils.show(mContext, "获取宝贝详情失败,请检查网络状况");
			finish();
		}
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.iv_titleLeft:
			finish();
			break;
		case R.id.relativeLayout_selectPackageType:
		case R.id.btn_mesKill:
		case R.id.btn_justToJoinGroup:
		case R.id.btn_justToRob:
			if(mGoodsGeneralModel != null){
				intent = new Intent(mContext, GoodsPackageActivity.class);
				intent.putExtra(InformationCodeUtil.IntentGoodsActivityCurrentGoodsModel,mGoodsGeneralModel);
				startActivity(intent);
				overridePendingTransition(R.anim.in_from_bottom, R.anim.not_change);
			}
			break;
		case R.id.iv_openConverSation:
			toConversationView();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 进入会话界面
	 */
	 void toConversationView(){

		if(mGoodsGeneralModel == null){
			ToastUtils.show(mContext, "请先获取商品内容");
			return;
		}
		Conversation.ConversationType conversationType = Conversation.ConversationType.PRIVATE;
		String targetId = new StringBuffer().append(mGoodsInfoActModel.getUserID()).toString();
		String title =  mGoodsInfoActModel.getShopName();

		Uri data = Uri.parse("rong://" + mContext.getApplicationInfo().processName)
		.buildUpon()
		.appendPath("conversation").appendPath(conversationType.getName().toLowerCase())
		.appendQueryParameter(ConversationGeneralActivity.INTENT_TARGETID, targetId)
		.appendQueryParameter(ConversationGeneralActivity.INTENT_TITLE, title)
		.appendQueryParameter(
		 ConversationGeneralActivity.INTENT_GOODSTYPE, ConversationGeneralActivity.FLAGSPORTTYPEGOODS)
		.build();
		Intent intent = new Intent(this,ConversationGeneralActivity.class);
		intent.setData(data);
		Gson gson = new Gson();
		String jsonString = gson.toJson(mGoodsInfoActModel);
		intent.putExtra(ConversationGeneralActivity.INTENT_JSONGOODSDETAIL, jsonString);
		startActivity(intent);
	}

	@Override
	protected void onDestroy() {
		handler_timeCurrent.removeCallbacksAndMessages(null);
		super.onDestroy();
	}

}
