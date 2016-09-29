package com.shuimunianhua.xianglixiangqin.activity;

import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultBaseModel;
import com.shuimunianhua.xianglixiangqin.model.AddressModel;
import com.shuimunianhua.xianglixiangqin.model.ChinaPositionModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
/***
 * @action 城市选择界面
 * @author ZHU
 * @date  2015-8-16 下午10:48:33
 */
public class AddressPositionSelectActivity extends MyBaseActivity implements OnRefreshListener, OnConnectServerStateListener<Integer>{

	/** 后退控件 **/
	@Bind(R.id.iv_titleLeft)
	ImageView iv_titleLeft;
	/** 页面标题设置为收货地址 **/
	@Bind(R.id.tv_title)
	TextView tv_title;
	/** 刷新控件 **/
	@Bind(R.id.SwipeRefreshLayout)
	 SwipeRefreshLayout swipeRefreshLayout;
	/** 当前选择位置数据listView **/
	@Bind(R.id.listView)
	  ListView listView;
	/** 数据源 **/
	List<String> listData;
	List<ChinaPositionModel> listDataProvince;
	List<ChinaPositionModel> listDataCity ;
	List<ChinaPositionModel> listDataArea ;
	/** 适配器 **/
	  ArrayAdapter<String> arrayAdapter;
	/** 地址对象 **/
	  AddressModel addressModel;	
	/**当前请求位置函数对象名称**/
	  String currentMethodName;

	@Override
	public void initView() {
		setContentView(R.layout.activity_address_position_select);
		ButterKnife.bind(mContext);
	}

	@Override
	public void initData() {
		tv_title.setText("中国");
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				previewToDestroy();
			}
		});
		
		addressModel = new AddressModel();
		currentMethodName = InformationCodeUtil.methodNameGetProvinceList;
		listData = new ArrayList<String>();
		listDataProvince = new ArrayList<ChinaPositionModel>();
		listDataCity = new ArrayList<ChinaPositionModel>();
		listDataArea = new ArrayList<ChinaPositionModel>();
		
		arrayAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, listData);
		listView.setAdapter(arrayAdapter);
		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
				public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						String positionName = listData.get(position);
						tv_title.setText(positionName);
						
						if(InformationCodeUtil.methodNameGetProvinceList.equals(currentMethodName)){
							currentMethodName = InformationCodeUtil.methodNameGetCityList;
							addressModel.setProvinceCode(listDataProvince.get(position).getCityCode());
							addressModel.setProvinceName(listDataProvince.get(position).getCityName());
						}else if(InformationCodeUtil.methodNameGetCityList.equals(currentMethodName)){
							currentMethodName = InformationCodeUtil.methodNameGetAreaList;
							addressModel.setCityCode(listDataCity.get(position).getCityCode());
							addressModel.setCityName(listDataCity.get(position).getCityName());
						}else if(InformationCodeUtil.methodNameGetAreaList.equals(currentMethodName)){
							addressModel.setAreaCode(listDataArea.get(position).getCityCode());
							addressModel.setAreaName(listDataArea.get(position).getCityName());
							MyApplication.setmAddressModel(addressModel);
							String str = addressModel.getProvinceName()+" "+addressModel.getCityName()+" "+addressModel.getAreaName();
							//设置返回信息
							getIntent().putExtra(InformationCodeUtil.IntentAddressLocationSelect, str);
							//设置Intent状态码
							setResult(RESULT_OK, AddressPositionSelectActivity.this.getIntent());
							//结束Intent
							finish();
							return;
						}			
						swipeRefreshLayout.post(new Runnable() {
							@Override
							public void run() {
								swipeRefreshLayout.setRefreshing(true);
								onRefresh();
							}
						});	
						
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
		getData(currentMethodName);
   	}	
	
	
	  void getData(String methodName) {
		
		listData.clear();
		arrayAdapter.notifyDataSetChanged();
		
		if(InformationCodeUtil.methodNameGetProvinceList.equals(methodName)){
			listDataProvince.clear();
			SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
					(mContext, this, requestSoapObject, methodName);
			connectCustomServiceAsyncTask.initProgressDialog(false);
			connectCustomServiceAsyncTask.execute();
			return;
		}
		if(InformationCodeUtil.methodNameGetCityList.equals(methodName)){
			listDataCity.clear();
			SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			requestSoapObject.addProperty("provinceCode", addressModel.getProvinceCode());
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
					(mContext, this, requestSoapObject, methodName);
			connectCustomServiceAsyncTask.initProgressDialog(false);
			connectCustomServiceAsyncTask.execute();
			return;
		}
		if(InformationCodeUtil.methodNameGetAreaList.equals(methodName)){
			listDataArea.clear();
			SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			requestSoapObject.addProperty("cityCode", addressModel.getCityCode());
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
					(mContext, this, requestSoapObject, methodName);
			connectCustomServiceAsyncTask.initProgressDialog(false);
			connectCustomServiceAsyncTask.execute();
			return;
		}
	}

	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
//		LogUtil.LogShitou("返回结果", returnSoapObject.toString());
//		Object  provinceSoapObject = (Object ) returnSoapObject.getProperty(methodName+"Result");
		Gson gson  = new Gson();
//		JSONShoppingAddressPositionSelectModel result = gson.fromJson(provinceSoapObject.toString(), JSONShoppingAddressPositionSelectModel.class);  
		JSONResultBaseModel<ChinaPositionModel> result = gson.fromJson
		(returnString, new TypeToken<JSONResultBaseModel<ChinaPositionModel>>(){}.getType());  
	
		if(InformationCodeUtil.methodNameGetProvinceList.equals(methodName)){

			listDataProvince.addAll(result.getList());
			for(int i=0; i<listDataProvince.size(); i++){
				listData.add(listDataProvince.get(i).getCityName());
			}
			swipeRefreshLayout.setRefreshing(false);
			arrayAdapter.notifyDataSetChanged();	
			return;
		}
		
		if(InformationCodeUtil.methodNameGetCityList.equals(methodName)){
			listDataCity.addAll(result.getList());
			for(int i=0; i<listDataCity.size(); i++){
				listData.add(listDataCity.get(i).getCityName());
			}
			swipeRefreshLayout.setRefreshing(false);
			arrayAdapter.notifyDataSetChanged();
			return;
		}
		if(InformationCodeUtil.methodNameGetAreaList.equals(methodName)){
			listDataArea.addAll(result.getList());
			for(int i=0; i<listDataArea.size(); i++){
				listData.add(listDataArea.get(i).getCityName());
			}
			swipeRefreshLayout.setRefreshing(false);
			arrayAdapter.notifyDataSetChanged();
			return;
		}
	}
	
	@Override
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {
		ToastUtils.show(mContext, "数据请求失败，请检查网络状况");
		swipeRefreshLayout.setRefreshing(false);
	}
	
	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		swipeRefreshLayout.setRefreshing(false);
	}
	
	
	/**即将销毁当前Activity**/
	  void previewToDestroy(){
		
		if(InformationCodeUtil.methodNameGetProvinceList.equals(currentMethodName)){
			finish();
			return;
		}
		
		if(InformationCodeUtil.methodNameGetCityList.equals(currentMethodName)){
			tv_title.setText("中国");
			currentMethodName = InformationCodeUtil.methodNameGetProvinceList;
			listDataCity.clear();
			listDataArea.clear();
			listData.clear();
			for(int i=0; i<listDataProvince.size(); i++){
				listData.add(listDataProvince.get(i).getCityName());
			}
			arrayAdapter.notifyDataSetChanged();
			return;
		}
		
		if(InformationCodeUtil.methodNameGetAreaList.equals(currentMethodName)){
			tv_title.setText(addressModel.getProvinceName());
			currentMethodName = InformationCodeUtil.methodNameGetCityList;
			listDataArea.clear();
			listData.clear();
			for(int i=0; i<listDataCity.size(); i++){
				listData.add(listDataCity.get(i).getCityName());
			}
			arrayAdapter.notifyDataSetChanged();
			return;
		}
		
	}

	@Override
	public void onBackPressed() {
		previewToDestroy();
	}
	
	

}
