package com.shuimunianhua.xianglixiangqin.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.frament.MainGoodsClassTypeFragmentEx;
import com.shuimunianhua.xianglixiangqin.frament.MainHomeFragment_DaiNiFei;
import com.shuimunianhua.xianglixiangqin.frament.MainHomeFragment_JvHeEx;
import com.shuimunianhua.xianglixiangqin.frament.MainMessageFragment;
import com.shuimunianhua.xianglixiangqin.frament.MainPersonalCenterFragment;
import com.shuimunianhua.xianglixiangqin.frament.MainShoppingCartFragment;
import com.shuimunianhua.xianglixiangqin.model.CustomModel;
import com.shuimunianhua.xianglixiangqin.receiver.PressLogoutReceiver;
import com.shuimunianhua.xianglixiangqin.rongyun.CustomizeMessageItemProvider;
import com.shuimunianhua.xianglixiangqin.rongyun.MyConnectionStatusListener;
import com.shuimunianhua.xianglixiangqin.rongyun.MyPrivateConversationProvider;
import com.shuimunianhua.xianglixiangqin.rongyun.RCDTestMessage;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceSyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;

import org.ksoap2.serialization.SoapObject;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/***
 * @action 主界面
 * @author SHI
 * @date 2016年5月6日 11:01:42
 */
public class MainActivity extends MyBaseActivity implements
		RongIM.UserInfoProvider, OnClickListener {
	/** 带你飛首页Fragment **/
	private MainHomeFragment_DaiNiFei mainHomeFragment_DaiNiFei;
	/** 聚合批发系统首页Fragment **/
//	private MainHomeFragment_JvHe mainHomeFragment_jvHe;
	private MainHomeFragment_JvHeEx mainHomeFragment_jvHeEx;
	/** 带你飞分类Fragment **/
	private MainGoodsClassTypeFragmentEx mainGoodsClassTypeFragmentEx;
	/** 购物车Fragment **/
	private MainShoppingCartFragment mainShoppingCartFragment;
	/** 聊天消息Fragment **/
	private MainMessageFragment mainMessageFragment;
	/** 个人中心Fragment **/
	private MainPersonalCenterFragment mainPersonalCenterFragment;

	/**
	 * 用于对Fragment进行管理
	 */
	private FragmentManager fragmentManager;

	/** 首页 **/
	@Bind(R.id.linearLayout_home)
	public LinearLayout linearLayout_home;
	/** 首页图标 **/
	@Bind(R.id.iv_home)
	public ImageView iv_home;
	/** 首页文字 **/
	@Bind(R.id.tv_home)
	public TextView tv_home;
	/** 分类 **/
	@Bind(R.id.linearLayout_class)
	public LinearLayout linearLayout_class;
	/** 分类图标 **/
	@Bind(R.id.iv_class)
	public ImageView iv_class;
	/** 分类文字 **/
	@Bind(R.id.tv_class)
	public TextView tv_class;
	/** 购物车 **/
	@Bind(R.id.linearLayout_shopcart)
	public LinearLayout linearLayout_shopcart;
	/** 购物车图标 **/
	@Bind(R.id.iv_shopcart)
	public ImageView iv_shopcart;
	/** 购物车文字 **/
	@Bind(R.id.tv_shopcart)
	public TextView tv_shopcart;
	/** 消息 **/
	@Bind(R.id.linearLayout_message)
	public LinearLayout linearLayout_message;
	/** 消息图标 **/
	@Bind(R.id.iv_message)
	public ImageView iv_message;
	/** 消息文字 **/
	@Bind(R.id.tv_message)
	public TextView tv_message;
	/** 个人中心 **/
	@Bind(R.id.linearLayout_person)
	public LinearLayout linearLayout_person;
	/** 个人中心图标 **/
	@Bind(R.id.iv_person)
	public ImageView iv_person;
	/** 个人中心文字 **/
	@Bind(R.id.tv_person)
	public TextView tv_person;
	/** 当前聊天未读消息数量 **/
	@Bind(R.id.tv_messageNumber)
	public TextView tv_messageNumber;

	private PressLogoutReceiver pressLogoutReceiver;

	private IntentFilter intentFilter;

	/**
	 * 当前店铺对象
	 **/
	public int currentShopID = -1;
	private int originalShopID;
//	/**
//	 * 当前店铺店主ID
//	 **/
//	public int currentShopUserID = -1;

	@Override
	public void initView() {
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);
		setReturnStatu(true);
		fragmentManager = getSupportFragmentManager();
		linearLayout_home.setOnClickListener(this);
		linearLayout_class.setOnClickListener(this);
		linearLayout_shopcart.setOnClickListener(this);
		linearLayout_message.setOnClickListener(this);
		linearLayout_person.setOnClickListener(this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
			currentShopID = intent.getIntExtra(InformationCodeUtil.IntentShopRecommendActivityCurrentShopID, -1);
			if (currentShopID == -1) {
				currentShopID = originalShopID;
			} else {
				if (currentShopID != originalShopID) {
					originalShopID = currentShopID;
//					currentShopUserID = -1;
				}
			}
		int viewId = intent.getIntExtra(InformationCodeUtil.IntentMainActivityCheckID, R.id.linearLayout_home);
		showViewByViewId(viewId);
	}

	@Override
	public void initData() {
		currentShopID = MyApplication.getmCustomModel(mContext).getLoginShopID();
		originalShopID = currentShopID;
		showViewByViewId(R.id.linearLayout_home);

		connectRongYun(MyApplication.getmCustomModel(mContext).getRongCloudToken());
		if(intentFilter == null){
			pressLogoutReceiver = new PressLogoutReceiver();
			intentFilter = new IntentFilter(InformationCodeUtil.ReceiverFilterPressUserLogout);
			registerReceiver( pressLogoutReceiver, intentFilter);
		}
	}


	@Override
	protected void onResume() {

		RongIM.getInstance()
		.getRongIMClient()
		.getUnreadCount(
		new RongIMClient.ResultCallback<Integer>() {

			@Override
			public void onSuccess(Integer count) {
				LogUtil.LogShitou("当前未读消息数量",
						"onSuccess:" + count);
				if (count > 0) {
					tv_messageNumber
							.setVisibility(View.VISIBLE);
					tv_messageNumber
							.setText(new StringBuffer()
									.append(count)
									.toString());
				} else {
					tv_messageNumber
							.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void onError(ErrorCode code) {
				LogUtil.LogShitou("当前消息数量",
						"onError:" + code);

			}
		}, ConversationType.PRIVATE);

		super.onResume();
	}

	/** 根据ID切换展示页面 **/
	public void showViewByViewId(int viewId) {
		switch (viewId) {
		case R.id.linearLayout_home:
			linearLayout_home.callOnClick();
			break;
		case R.id.linearLayout_class:
			linearLayout_class.callOnClick();
			break;
		case R.id.linearLayout_shopcart:
			linearLayout_shopcart.callOnClick();
			break;
		case R.id.linearLayout_message:
			linearLayout_message.callOnClick();
			break;
		case R.id.linearLayout_person:
			linearLayout_person.callOnClick();
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		int black = getResources().getColor(R.color.black);
		int red = getResources().getColor(R.color.redTitleBarBackground);
		iv_home.setImageResource(R.drawable.radiobutton_main_home_unselected);
		tv_home.setTextColor(black);
		iv_class.setImageResource(R.drawable.radiobutton_main_class_unselected);
		tv_class.setTextColor(black);
		iv_shopcart
				.setImageResource(R.drawable.radiobutton_main_shopcart_unselected);
		tv_shopcart.setTextColor(black);
		iv_message
				.setImageResource(R.drawable.radiobutton_main_message_unselected);
		tv_message.setTextColor(black);
		iv_person
				.setImageResource(R.drawable.radiobutton_main_person_unselected);
		tv_person.setTextColor(black);
		hideFragments(fragmentManager.beginTransaction());
		switch (v.getId()) {
		case R.id.linearLayout_home:
			iv_home.setImageResource(R.drawable.radiobutton_main_home_selected);
			tv_home.setTextColor(red);
			showHomeView();
			break;
		case R.id.linearLayout_class:
			iv_class.setImageResource(R.drawable.radiobutton_main_class_selected);
			tv_class.setTextColor(red);
			showClassView();
			break;
		case R.id.linearLayout_shopcart:
			iv_shopcart
					.setImageResource(R.drawable.radiobutton_main_shopcart_selected);
			tv_shopcart.setTextColor(red);
			showShopCartView();
			break;
		case R.id.linearLayout_message:
			iv_message
					.setImageResource(R.drawable.radiobutton_main_message_selected);
			tv_message.setTextColor(red);
			showMessageView();
			break;
		case R.id.linearLayout_person:
			iv_person.setImageResource(R.drawable.radiobutton_main_person_selected);
			tv_person.setTextColor(red);
			showPersonCenterView();
			break;
		default:
			break;
		}
	}

	/**
	 * 将所有的Fragment都置为隐藏状态。
	 *
	 * @param transaction
	 *            用于对Fragment执行操作的事务
	 */
	private void hideFragments(FragmentTransaction transaction) {

		if (mainHomeFragment_DaiNiFei != null) {
			transaction.hide(mainHomeFragment_DaiNiFei);
		}

		if (mainHomeFragment_jvHeEx != null) {
			transaction.hide(mainHomeFragment_jvHeEx);
		}

		if (mainGoodsClassTypeFragmentEx != null) {
			transaction.hide(mainGoodsClassTypeFragmentEx);
		}
		if (mainShoppingCartFragment != null) {
			transaction.hide(mainShoppingCartFragment);
		}
		if (mainMessageFragment != null) {
			transaction.hide(mainMessageFragment);
		}
		if (mainPersonalCenterFragment != null) {
			transaction.hide(mainPersonalCenterFragment);
		}
		transaction.commit();
	}

	private  void  showHomeView() {
		FragmentTransaction transaction = fragmentManager
				.beginTransaction();
		if(InformationCodeUtil.whetherIsDaiNiFei){
			if (mainHomeFragment_DaiNiFei == null) {
				mainHomeFragment_DaiNiFei = new MainHomeFragment_DaiNiFei();
				transaction.add(R.id.frameLayout, mainHomeFragment_DaiNiFei,"mainHomeFragment_DaiNiFei");
			}else{
				transaction.show(mainHomeFragment_DaiNiFei);
			}

		}else{
				if (mainHomeFragment_jvHeEx == null) {
					mainHomeFragment_jvHeEx = new MainHomeFragment_JvHeEx();
					transaction.add(R.id.frameLayout, mainHomeFragment_jvHeEx,"mainHomeFragment_jvHeEx");
				}else{
					transaction.show(mainHomeFragment_jvHeEx);
				}
		}
		transaction.commit();

//		if(InformationCodeUtil.whetherIsDaiNiFei){
//
//			if (mainHomeFragment_DaiNiFei == null) {
//				mainHomeFragment_DaiNiFei = new MainHomeFragment_DaiNiFei();
//			}
//			FragmentTransaction fragmentTransaction = fragmentManager
//					.beginTransaction();
//			fragmentTransaction.replace(R.id.frameLayout, mainHomeFragment_DaiNiFei,"mainHomeFragment_DaiNiFei");
//			fragmentTransaction.commit();
//		}else{
//			if (mainHomeFragment_jvHe == null) {
//				mainHomeFragment_jvHe = new MainHomeFragment_JvHe();
//			}
//			FragmentTransaction fragmentTransaction = fragmentManager
//					.beginTransaction();
//			fragmentTransaction.replace(R.id.frameLayout, mainHomeFragment_jvHe,"mainHomeFragment_jvHe");
//			fragmentTransaction.commit();
//		}

	}

	private void showClassView() {
		FragmentTransaction transaction = fragmentManager
				.beginTransaction();
		if (mainGoodsClassTypeFragmentEx == null) {
			mainGoodsClassTypeFragmentEx = new MainGoodsClassTypeFragmentEx();
			transaction.add(R.id.frameLayout, mainGoodsClassTypeFragmentEx,"mainHomeFragment_jvHe");
		}else{
			transaction.show(mainGoodsClassTypeFragmentEx);
		}
		transaction.commit();

//		if (mainGoodsClassTypeFragmentEx == null) {
//			mainGoodsClassTypeFragmentEx = new MainGoodsClassTypeFragmentEx();
//			}
//			FragmentTransaction fragmentTransaction = fragmentManager
//					.beginTransaction();
//			fragmentTransaction.replace(R.id.frameLayout, mainGoodsClassTypeFragmentEx,"mainGoodsClassTypeFragmentEx");
//			fragmentTransaction.commit();
	}

	private void showShopCartView() {
		FragmentTransaction transaction = fragmentManager
				.beginTransaction();
		if (mainShoppingCartFragment == null) {
			mainShoppingCartFragment = new MainShoppingCartFragment();
			transaction.add(R.id.frameLayout, mainShoppingCartFragment,"mainShoppingCartFragment");
		}else{
			transaction.show(mainShoppingCartFragment);
		}
		transaction.commit();

//		if (mainShoppingCartFragment == null) {
//			mainShoppingCartFragment = new MainShoppingCartFragment();
//		}
//		FragmentTransaction fragmentTransaction = fragmentManager
//				.beginTransaction();
//
//		fragmentTransaction.replace(R.id.frameLayout, mainShoppingCartFragment,"mainShoppingCartFragment");
//		fragmentTransaction.commit();
	}

	private void showMessageView() {
		FragmentTransaction transaction = fragmentManager
				.beginTransaction();
		if (mainMessageFragment == null) {
			mainMessageFragment = new MainMessageFragment();
			transaction.add(R.id.frameLayout, mainMessageFragment,"mainMessageFragment");
		}else{
			transaction.show(mainMessageFragment);
		}
		transaction.commit();

//		if (mainMessageFragment == null) {
//			mainMessageFragment = new MainMessageFragment();
//		}
//		FragmentTransaction fragmentTransaction = fragmentManager
//				.beginTransaction();
//		fragmentTransaction.replace(R.id.frameLayout, mainMessageFragment,"mainMessageFragment");
//		fragmentTransaction.commit();
	}

	private void showPersonCenterView() {
		FragmentTransaction transaction =fragmentManager
				.beginTransaction();
		if (mainPersonalCenterFragment == null) {
			mainPersonalCenterFragment = new MainPersonalCenterFragment();
			transaction.add(R.id.frameLayout, mainPersonalCenterFragment,"mainPersonalCenterFragment");
		}else{
			transaction.show(mainPersonalCenterFragment);
		}
		transaction.commit();

//		if (mainPersonalCenterFragment == null) {
//			mainPersonalCenterFragment = new MainPersonalCenterFragment();
//		}
//		FragmentTransaction fragmentTransaction = fragmentManager
//				.beginTransaction();
//		fragmentTransaction.replace(R.id.frameLayout, mainPersonalCenterFragment,"mainPersonalCenterFragment");
//		fragmentTransaction.commit();
	}


	/** 融云连接 **/
	private void connectRongYun(String token) {
		String currentProcessName = ((MyApplication) getApplication())
				.getCurProcessName(getApplicationContext());

		if (getApplicationInfo().packageName.equals(currentProcessName)) {

			RongIM.registerMessageType(RCDTestMessage.class);
			RongIM.getInstance().registerConversationTemplate(new MyPrivateConversationProvider());
			RongIM.getInstance().registerMessageTemplate(new CustomizeMessageItemProvider());
			/**
			 * IMKit SDK调用第二步,建立与服务器的连接
			 */
			RongIM.connect(token, new RongIMClient.ConnectCallback() {

				/**
				 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的
				 * Token
				 */
				@Override
				public void onTokenIncorrect() {
					LogUtil.LogShitou("Activity", "--Token 已经过期");
				}

				/**
				 * 连接融云成功
				 *
				 * @param userid
				 *            当前 token
				 */
				@Override
				public void onSuccess(String userid) {
					LogUtil.LogShitou("MainActivity", "融云连接成功");
					if (RongIM.getInstance() != null) {


						// 设置用户信息提供者。
						RongIM.setUserInfoProvider(MainActivity.this, true);
						//设置融云当前状态监听者
						RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new MyConnectionStatusListener(mContext));
						// 设置收到新消息时的监听者，然后显示当前消息数量
						RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {

							@Override
							public boolean onReceived(Message arg0, int arg1) {
								RongIM.getInstance()
										.getRongIMClient()
										.getUnreadCount(
												new RongIMClient.ResultCallback<Integer>() {

													@Override
													public void onSuccess(
															Integer count) {
														LogUtil.LogShitou(
																"当前未读消息数量",
																"onSuccess:"
																		+ count);
														if (count > 0) {
															tv_messageNumber
																	.setVisibility(View.VISIBLE);
															tv_messageNumber
																	.setText(new StringBuffer()
																			.append(count)
																			.toString());
														} else {
															tv_messageNumber
																	.setVisibility(View.INVISIBLE);
														}
													}

													@Override
													public void onError(
															ErrorCode code) {
														LogUtil.LogShitou(
																"当前消息数量",
																"onError:"
																		+ code);

													}
												}, ConversationType.PRIVATE);
								return false;
							}
						});
					}

				}

				/**
				 * 连接融云失败
				 * 
				 * @param errorCode
				 *            错误码，可到官网 查看错误码对应的注释
				 */
				@Override
				public void onError(ErrorCode errorCode) {
					LogUtil.LogShitou("MainActivity", "连接融云失败");
					Log.d("LoginActivity", "--连接融云失败" + errorCode);
				}
			});
		}
	}

	@Override
	public UserInfo getUserInfo(String userId) {

		SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE,InformationCodeUtil.methodNameGetUserInfoByID);
		requestSoapObject.addProperty("UserId",userId);

		ConnectServiceSyncTask connectCustomServiceSyncTask =
		new ConnectServiceSyncTask(mContext,requestSoapObject
		, InformationCodeUtil.methodNameGetUserInfoByID);

		String strUserInfo = connectCustomServiceSyncTask.connectCustomService();
//		LogUtil.LogShitou("获取    "+userId+"   用户信息被执行:"+strUserInfo);
		UserInfo userInfo = null;
		try {
			Gson gson = new Gson();
			CustomModel customModel = gson.fromJson(strUserInfo,CustomModel.class);
			String DjLsh = new StringBuffer().append(customModel.getDjLsh()).toString();
			String realName = customModel.getRealName();
			if(TextUtils.isEmpty(realName)){
				realName = customModel.getShopName();
			}

			String ImageUrl;
			if (TextUtils.isEmpty(customModel.getImgUrl())) {
				ImageUrl = "http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png";
			} else {
				ImageUrl = customModel.getImgUrl();
			}
			userInfo = new UserInfo(DjLsh,realName, Uri.parse(ImageUrl));
		}catch (Exception e){
			userInfo = null;
		}
		return userInfo;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
//		if(mainHomeFragment_DaiNiFei != null){
//			ButterKnife.unbind(mainHomeFragment_DaiNiFei);
//		}
//		if(mainHomeFragment_jvHe != null){
//			ButterKnife.unbind(mainHomeFragment_jvHe);
//		}
//		if(mainGoodsClassTypeFragmentEx != null){
//			ButterKnife.unbind(mainGoodsClassTypeFragmentEx);
//		}
//		if(mainShoppingCartFragment != null){
//			ButterKnife.unbind(mainShoppingCartFragment);
//		}
//		if(mainMessageFragment != null){
//			ButterKnife.unbind(mainMessageFragment);
//		}
//		if(mainPersonalCenterFragment != null){
//			ButterKnife.unbind(mainPersonalCenterFragment);
//		}
		if (intentFilter != null){
			unregisterReceiver(pressLogoutReceiver);
			intentFilter = null;
		}

	}
}