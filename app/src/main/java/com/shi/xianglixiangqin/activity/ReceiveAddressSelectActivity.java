package com.shi.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultBaseModel;
import com.shi.xianglixiangqin.json.JSONResultMsgModel;
import com.shi.xianglixiangqin.model.AddressModel;
import com.shi.xianglixiangqin.model.CustomModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 收货地址选择界面
 * @author SHI 2016-2-16 13:48:39
 */
public class ReceiveAddressSelectActivity extends MyBaseActivity implements
		SwipeRefreshLayout.OnRefreshListener, OnConnectServerStateListener<Integer> {

	/** 后退控件 **/
	@BindView(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/** 页面标题设置为收货地址 **/
	@BindView(R.id.tv_title)
	 TextView tv_title;
	/** 右侧新增收货地址 **/
	@BindView(R.id.iv_titleRight)
	 ImageView iv_titleRight;
	/** 刷新控件 **/
	@BindView(R.id.SwipeRefreshLayout)
	 SwipeRefreshLayout swipeRefreshLayout;
	/** 我的收藏数据 **/
	@BindView(R.id.listView)
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
		setContentView(R.layout.activity_receiver_address_select);
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
		tv_title.setText("地址列表");
		iv_titleRight.setImageResource(R.drawable.icon_receiver_address_add);
		iv_titleRight.setVisibility(View.VISIBLE);
		iv_titleRight.setOnClickListener(new OnClickListener() {

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

	}

	@Override
	public void connectServiceFailed(String returnStrError, String methodName, Integer state,
			boolean whetherRefresh) {

		listData.clear();
		if(swipeRefreshLayout != null){
			listView.removeHeaderView(listViewIsEmpty);
			swipeRefreshLayout.setRefreshing(false);
		}
		shoppingAddressAdapter.notifyDataSetChanged();
		ToastUtil.show(mContext, "数据请求失败,请检查网络");
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
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
				convertView = View.inflate(mContext,R.layout.item_adapter_receiver_address_select,null);
				holder = new ViewHolder();
				holder.tv_userName = (TextView) convertView.findViewById(R.id.tv_userName);
				holder.tv_userPhone = (TextView) convertView.findViewById(R.id.tv_userPhone);
				holder.tv_addressName = (TextView) convertView.findViewById(R.id.tv_addressName);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			AddressModel currentAddressModel = listData.get(position);
			if (currentAddressModel != null) {
				holder.tv_userName.setText(currentAddressModel.getRealName());
				holder.tv_userPhone.setText(currentAddressModel.getPhoneNum());
				holder.tv_addressName.setText(currentAddressModel.getProvinceName()
				+ currentAddressModel.getCityName() + currentAddressModel.getAreaName()+currentAddressModel.getAddress());
			}
			return convertView;
		}

		private class ViewHolder {
			/** 用户名 **/
			private TextView tv_userName;
			/** 手机号码 */
			private TextView tv_userPhone;
			/** 收货地址 **/
			private TextView tv_addressName;
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
