package com.shuimunianhua.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.adapter.MyBaseAdapter;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultBaseModel;
import com.shuimunianhua.xianglixiangqin.json.JSONResultMsgModel;
import com.shuimunianhua.xianglixiangqin.model.AddressModel;
import com.shuimunianhua.xianglixiangqin.model.CustomModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;
import com.shuimunianhua.xianglixiangqin.view.FragmentCommonDialog;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 收货地址界面
 * 
 * @author SHI 2016-2-16 13:48:39
 */
public class ReceiveAddressManageActivity extends MyBaseActivity implements
		SwipeRefreshLayout.OnRefreshListener, OnConnectServerStateListener<Integer> {

	/** 后退控件 **/
	@Bind(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/** 页面标题设置为收货地址 **/
	@Bind(R.id.tv_title)
	 TextView tv_title;
	/** 右侧新增收货地址 **/
	@Bind(R.id.tv_titleRight)
	 TextView tv_titleRight;
	/** 刷新控件 **/
	@Bind(R.id.SwipeRefreshLayout)
	 SwipeRefreshLayout swipeRefreshLayout;
	/** 我的收藏数据 **/
	@Bind(R.id.listView)
	 ListView listView;
	/** 数据源 **/
	private List<AddressModel> listData = new ArrayList<AddressModel>();
	/** 适配器 **/
	private ShoppingAddressAdapter shoppingAddressAdapter;
	/** 当前登录用户 **/
	private CustomModel mCustomModel;
	/** 当前待删除收货地址对象 **/
	private AddressModel addressModelToDelete;
	private View listViewIsEmpty;
	@Override
	public void initView() {
		setContentView(R.layout.activity_shopping_address_manage);
		ButterKnife.bind(this);
	}

	@Override
	public void initData() {
		mCustomModel = MyApplication.getmCustomModel(mContext);
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				previewFinish(0);
			}
		});
		tv_title.setText("收货地址");
		tv_titleRight.setText("新增");
		tv_titleRight.setVisibility(View.VISIBLE);
		tv_titleRight.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext,ReceiveAddressEditActivity.class));
			}
		});
		// 初始化listView控件和外围的刷新控件
		listViewIsEmpty = View.inflate(mContext,R.layout.item_adapter_list_view_empty, null);
		((ImageView)listViewIsEmpty.findViewById(R.id.iv_empty)).setImageResource(R.drawable.icon_my_address_empty);
		if(listView.getHeaderViewsCount() == 0){
			listView.addHeaderView(listViewIsEmpty);
		}
		shoppingAddressAdapter = new ShoppingAddressAdapter(this, listData);
		listView.setAdapter(shoppingAddressAdapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						previewFinish(position);
					}
		});
		swipeRefreshLayout.setColorSchemeColors( Color.RED
				,Color.GREEN
				,Color.BLUE
				,Color.YELLOW
				,Color.CYAN
				,0xFFFE5D14
				,Color.MAGENTA);	
		swipeRefreshLayout.setOnRefreshListener(this);
	}

	@Override
	protected void onResume() {
		//每次启动页面 自动刷新listView
		swipeRefreshLayout.post(new Runnable() {
			@Override
			public void run() {
				swipeRefreshLayout.setRefreshing(true);
				onRefresh();
			}
		});		
		super.onResume();
	}
	
	@Override
	public void onRefresh() {
		getData(InformationCodeUtil.methodNameGetAddrList);
	}
	

	public void getData(String methodName) {

		if (methodName.equals(InformationCodeUtil.methodNameGetAddrList)) {
			SoapObject soapObject = new SoapObject(
					ConnectServiceUtil.NAMESPACE, methodName);
			soapObject.addProperty("customID", mCustomModel.getDjLsh());
			soapObject.addProperty("openKey", mCustomModel.getOpenKey());
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
					mContext, this, soapObject, methodName);
			connectCustomServiceAsyncTask.initProgressDialog(false);
			connectCustomServiceAsyncTask.execute();
			return;
		}

		if (methodName.equals(InformationCodeUtil.methodNameDeleteAddr)) {
			
			
			SoapObject soapObject = new SoapObject(
					ConnectServiceUtil.NAMESPACE, methodName);
			soapObject.addProperty("customID", mCustomModel.getDjLsh());
			soapObject.addProperty("openKey", mCustomModel.getOpenKey());
			soapObject.addProperty("addrID", addressModelToDelete.getDjLsh());
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
					mContext, this, soapObject, methodName);
			connectCustomServiceAsyncTask.execute();
		}
		
	}

	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		if (methodName.equals(InformationCodeUtil.methodNameGetAddrList)) {
			Gson gson = new Gson();
			JSONResultBaseModel<AddressModel> mJSONAddrModel = null;
			try {
				JSONResultMsgModel mJSONBackResultModel = gson.fromJson(
						returnString, JSONResultMsgModel.class);
				mJSONAddrModel = gson.fromJson(
						mJSONBackResultModel.getMsg(), new TypeToken<JSONResultBaseModel<AddressModel>>(){}.getType());
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			} 
			listData.clear();
			if(mJSONAddrModel != null)
			listData.addAll(mJSONAddrModel.getList());
			if(listData.size() == 0){
				if(listView.getHeaderViewsCount() == 0){
					listView.addHeaderView(listViewIsEmpty);
				}
			}else{
				listView.removeHeaderView(listViewIsEmpty);
			}
			shoppingAddressAdapter.notifyDataSetChanged();
			swipeRefreshLayout.setRefreshing(false);

			return;
		}
		if (methodName.equals(InformationCodeUtil.methodNameDeleteAddr)) {
			if(returnString.contains("删除成功")){
				listData.remove(addressModelToDelete);
				shoppingAddressAdapter.notifyDataSetChanged();
				ToastUtils.show(mContext, "成功删除地址");
				return;
			}
		}

	}

	@Override
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {

		listData.clear();
		if(swipeRefreshLayout != null){
			listView.removeHeaderView(listViewIsEmpty);
			swipeRefreshLayout.setRefreshing(false);
		}
		shoppingAddressAdapter.notifyDataSetChanged();
		ToastUtils.show(mContext, "数据请求失败,请检查网络");
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
	}

	public void showDeleteDialog() {
		
		final FragmentCommonDialog fragmentDailog = new FragmentCommonDialog();
		fragmentDailog.initView("删除","确定删除吗?","取消","确定",
			new FragmentCommonDialog.OnButtonClickListener() {
			
			@Override
			public void OnOkClick() {
				getData(InformationCodeUtil.methodNameDeleteAddr);
				fragmentDailog.dismiss();
			}
			
			@Override
			public void OnCancelClick() {
				fragmentDailog.dismiss();
			}
		});
		fragmentDailog.show(getSupportFragmentManager(), "fragmentDialog");
	}	
	
	/**
	 * 收货地址适配器
	 * @author SHI
	 * 2016-2-17 15:44:17
	 */
	public class ShoppingAddressAdapter extends MyBaseAdapter<AddressModel> {

		public ShoppingAddressAdapter(Context mContext, List<AddressModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = View.inflate(mContext,R.layout.item_shipping_address,null);
				holder = new ViewHolder();
				holder.tv_userName = (TextView) convertView.findViewById(R.id.tv_userName);
				holder.tv_userPhone = (TextView) convertView.findViewById(R.id.tv_userPhone);
				holder.tv_postCode = (TextView) convertView.findViewById(R.id.tv_postCode);
				holder.tv_addressName = (TextView) convertView.findViewById(R.id.tv_addressName);
				holder.iv_editAddress = (ImageView) convertView.findViewById(R.id.iv_editAddress);
				holder.iv_deleteAddress = (ImageView) convertView.findViewById(R.id.iv_deleteAddress);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final AddressModel currentAddressModel = listData.get(position);
			if (currentAddressModel != null) {
				holder.tv_userName.setText("用户名称："+currentAddressModel.getRealName());
				holder.tv_userPhone.setText("电话号码："+currentAddressModel.getPhoneNum());
				holder.tv_postCode.setText("邮政编号："+currentAddressModel.getPostCode());
				holder.tv_addressName.setText("收货地址："+currentAddressModel.getProvinceName()
				+ currentAddressModel.getCityName() + currentAddressModel.getAreaName()+"\n"+currentAddressModel.getAddress());
				holder.iv_editAddress.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
//						ToastUtils.show(mContext, "编辑地址");
						Intent intent = new Intent(mContext,ReceiveAddressEditActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("currentAddressModel", currentAddressModel);
						intent.putExtras(bundle);
						mContext.startActivity(intent);
					}
				});
				holder.iv_deleteAddress.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
//						ToastUtils.show(mContext, "删除地址");
						((ReceiveAddressManageActivity)mContext).addressModelToDelete = currentAddressModel;
						((ReceiveAddressManageActivity)mContext).showDeleteDialog();
					}
				});
			}
			return convertView;
		}

		private class ViewHolder {
			/** 用户名 **/
			private TextView tv_userName;
			/** 手机号码 */
			private TextView tv_userPhone;
			/** 邮箱编号**/
			private TextView tv_postCode;
			/** 收货地址 **/
			private TextView tv_addressName;
			/**编辑地址按钮**/
			private ImageView iv_editAddress;
			/**删除地址按钮**/
			private ImageView iv_deleteAddress;
		}

	}
	private  void previewFinish(int position){
		
		if(listData == null || listData.size() == 0){
			finish();
			return;
		}
		Intent intent = getIntent();
		intent.putExtra("selectAddressModel", listData.get(position));
		setResult(RESULT_OK, intent);
		finish();
	}
	
	@Override
	public void onBackPressed() {
		previewFinish(0);
	}
	
}
