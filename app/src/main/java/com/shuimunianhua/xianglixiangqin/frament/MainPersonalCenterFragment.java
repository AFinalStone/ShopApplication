package com.shuimunianhua.xianglixiangqin.frament;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.afinalstone.androidstudy.view.ShapedImageView;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.activity.CompletePersonalInformationActivity;
import com.shuimunianhua.xianglixiangqin.activity.MainActivity;
import com.shuimunianhua.xianglixiangqin.activity.OtherOrderActivity;
import com.shuimunianhua.xianglixiangqin.activity.PayPasswordChangeActivity;
import com.shuimunianhua.xianglixiangqin.activity.SearchGoodsMyAgentActivity;
import com.shuimunianhua.xianglixiangqin.activity.MyOrderActivity;
import com.shuimunianhua.xianglixiangqin.activity.MyShopWebActivity;
import com.shuimunianhua.xianglixiangqin.activity.WXShopAddGoods;
import com.shuimunianhua.xianglixiangqin.activity.SettingCenterActivity;
import com.shuimunianhua.xianglixiangqin.activity.ReceiveAddressManageActivity;
import com.shuimunianhua.xianglixiangqin.adapter.MyBaseAdapter;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultMsgModel;
import com.shuimunianhua.xianglixiangqin.model.CustomModel;
import com.shuimunianhua.xianglixiangqin.model.FunctionTypeModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.GaussianBlurUtil;
import com.shuimunianhua.xianglixiangqin.util.ImagerLoaderUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.StringUtil;

/**
 * @action 个人中心
 * @author SHI
 * @date 2016年4月22日 16:25:31
 */
public class MainPersonalCenterFragment extends MyBaseFragment<MainActivity> implements OnConnectServerStateListener<Integer> {

	/** 当前界面内容 **/
	private View rootView;
	/** 主页标题 **/
	@Bind(R.id.tv_title)
	 TextView tv_title;
	/** 右侧控件设置 **/
	@Bind(R.id.iv_titleRight)
	 ImageView iv_titleRight;

	/** 用户头像背景 **/
	@Bind(R.id.iv_GaussianBlurBackground)
	 ImageView iv_GaussianBlurBackground;
	/** 用户头像高斯模糊遮罩* **/
	@Bind(R.id.view_GaussianBlur)
	 View view_GaussianBlur;
	/** 用户头像 **/
	@Bind(R.id.shapedImageView)
	 ShapedImageView shapedImageView;
	/** 用户电话号码 **/
	@Bind(R.id.tv_phoneNum)
	 TextView tv_phoneNum;
	/** 用户飞币数量 **/
	@Bind(R.id.tv_flyCoinNumber)
	 TextView tv_flyCoinNumber;

	@Bind(R.id.listView)
	 ListView listView;
	private List<FunctionTypeModel> listData = new ArrayList<FunctionTypeModel>();
	private MyAdapter adapter;

	private final String TypeNameMyOrder = "我的订单";
	private final String TypeNameCustomerOrder = "客户订单";
	private final String TypeNameAgent = "代理商品";
	private final String TypeNameReceiverAddress = "地址管理";
	private final String TypeNameMyShop = "我的店铺";
	private final String TypeNamePayPassword = "交易密码";
	//	private final String TypeNameMyBankCard = "我的银行卡";
	private final String TypeNameWXShopMange = "微信店铺管理";

	/**网络请求是否成功**/
	public boolean connectSuccessFlag = false;


	@Override
	public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(rootView == null){
			rootView = inflater.inflate(R.layout.fragment_personal, container, false);
			ButterKnife.bind(this, rootView);
			initView();
		}
		return rootView;
	}

	public void initView() {
		tv_title.setText("个人中心");
		iv_titleRight.setVisibility(View.VISIBLE);
		iv_titleRight.setImageResource(R.drawable.iv_personal_setting);
		iv_titleRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mActivity.startActivity(new Intent(mActivity,SettingCenterActivity.class));
			}
		});
		shapedImageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mActivity.startActivity(new Intent(mActivity,CompletePersonalInformationActivity.class));
			}
		});

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				toOtherView(listData.get(position).getName());
			}
		});

		listData.add(new FunctionTypeModel(R.drawable.iv_personal_my_order,
				TypeNameMyOrder));
		listData.add(new FunctionTypeModel(R.drawable.iv_personal_other_order,
				TypeNameCustomerOrder));
		listData.add(new FunctionTypeModel(R.drawable.iv_personal_agent,
				TypeNameAgent));
		listData.add(new FunctionTypeModel(
				R.drawable.iv_personal_receiver_address,
				TypeNameReceiverAddress));
		listData.add(new FunctionTypeModel(R.drawable.icon_shop_sign_01,
				TypeNameMyShop));
		listData.add(new FunctionTypeModel(R.drawable.iv_personal_pay_password,
				TypeNamePayPassword));
//		listData.add(new FunctionTypeModel(R.drawable.iv_personal_bankcard,
//				TypeNameMyBankCard));
		listData.add(new FunctionTypeModel(R.drawable.iv_personal_wxshopmange,
				TypeNameWXShopMange));
		adapter = new MyAdapter(mActivity, listData);
		listView.setAdapter(adapter);

	}


	public void initData() {

		CustomModel mCustomModel = MyApplication.getmCustomModel(mActivity);
		if(!StringUtil.isEmpty(mCustomModel.getRealName())){
			tv_phoneNum.setText(mCustomModel.getRealName());
		}else{
			tv_phoneNum.setText(mCustomModel.getPhoneNum());
		}

		ImagerLoaderUtil.getInstance(mActivity).displayMyImage(mCustomModel.getImgUrl(), shapedImageView,R.drawable.iv_personal_my_header);
		ImagerLoaderUtil.getInstance(mActivity)
				.displayMyImage(mCustomModel.getImgUrl(), iv_GaussianBlurBackground,R.drawable.background_personnal,new SimpleImageLoadingListener(){

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						super.onLoadingComplete(imageUri, view, loadedImage);
						GaussianBlurUtil.applyBlur(iv_GaussianBlurBackground,view_GaussianBlur);
					}
				});
		getData(InformationCodeUtil.methodNameGetAcceptFlyCoin);
	}

	@Override
	public void onResume() {
		super.onResume();
		initData();
	}

	private void getData(String methodName){
		if (methodName == InformationCodeUtil.methodNameGetAcceptFlyCoin) {
			SoapObject requestSoapObject = new SoapObject(
					ConnectServiceUtil.NAMESPACE, methodName);
			requestSoapObject.addProperty("customID", MyApplication
					.getmCustomModel(mActivity).getDjLsh());
			ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
					mActivity, this, requestSoapObject, methodName);
			connectGoodsServiceAsyncTask.initProgressDialog(false, "请稍等...");
			connectGoodsServiceAsyncTask.execute();
			return;
		}
	}


	private void toOtherView(String name) {
		if (TypeNameMyOrder.equals(name)) {
			Intent intent = new Intent(mActivity, MyOrderActivity.class);
			mActivity.startActivity(intent);
			return;
		}

		if (TypeNameCustomerOrder.equals(name)) {
			Intent intent = new Intent(mActivity, OtherOrderActivity.class);
			mActivity.startActivity(intent);
			return;
		}
		if (TypeNameAgent.equals(name)) {
			mActivity.startActivity(new Intent(mActivity,
					SearchGoodsMyAgentActivity.class));
			return;
		}
		if (TypeNameReceiverAddress.equals(name)) {
			mActivity.startActivity(new Intent(mActivity,
					ReceiveAddressManageActivity.class));
			return;
		}
		if (TypeNameMyShop.equals(name)) {
			mActivity.startActivity(new Intent(mActivity,
					MyShopWebActivity.class));
			return;
		}
//		if (TypeNameMyBankCard.equals(name)) {
//			mActivity.startActivity(new Intent(mActivity,
//					MyBankCardActivity.class));
//			return;
//		}
 		if (TypeNamePayPassword.equals(name)) {
			mActivity.startActivity(new Intent(mActivity,
					PayPasswordChangeActivity.class));
			return;
		}
		if (TypeNameWXShopMange.equals(name)) {
			mActivity.startActivity(new Intent(mActivity,
					WXShopAddGoods.class));
			return;
		}
	}

	private class MyAdapter extends MyBaseAdapter<FunctionTypeModel> {

		public MyAdapter(Context mContext, List<FunctionTypeModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = View.inflate(mContext,
						R.layout.item_adapter_personal_listview, null);
				holder = new ViewHolder();
				holder.iv_functionType = (ImageView) convertView
						.findViewById(R.id.imageview);
				holder.tv_functionType = (TextView) convertView
						.findViewById(R.id.tv_name);
				holder.view = (View) convertView.findViewById(R.id.view);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if (position == 0) {
				holder.view.setVisibility(View.VISIBLE);
			} else {
				holder.view.setVisibility(View.GONE);
			}
			FunctionTypeModel currentFunctionTypeModel = listData.get(position);
			holder.iv_functionType.setImageResource(currentFunctionTypeModel
					.getImageUrl());
			holder.tv_functionType.setText(currentFunctionTypeModel.getName());
			return convertView;
		}

		private class ViewHolder {
			/** 功能图标 **/
			ImageView iv_functionType;
			/** 功能名称 **/
			TextView tv_functionType;
			/** 分割线 **/
			View view;
		}

	}

	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		connectSuccessFlag = true;
//		String returnString = returnSoapObject.getProperty(
//				methodName + "Result").toString();
//		LogUtil.LogShitou("飞币数量", returnString);
		Gson gson = new Gson();
		if (methodName == InformationCodeUtil.methodNameGetAcceptFlyCoin) {
			try {
				JSONResultMsgModel result = gson.fromJson(returnString, JSONResultMsgModel.class);
				int totalFlyCoin_user = Integer.parseInt(result.getMsg());
				tv_flyCoinNumber.setText(new StringBuffer().append(totalFlyCoin_user).append("飞币").toString());
			} catch (Exception e) {
				e.printStackTrace();
				connectSuccessFlag = false;
			} 
		}		
	}

	@Override
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {
		connectSuccessFlag = false;
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		
	}

}
