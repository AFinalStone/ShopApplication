package com.shuimunianhua.xianglixiangqin.pager;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.afinalstone.androidstudy.swiperefreshview.OnSwipeRefreshViewListener;
import com.afinalstone.androidstudy.swiperefreshview.SwipeRefreshGridView;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.adapter.MyBaseAdapter;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultMsgModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

public class SelectCityLocationPager extends BasePager<Activity> implements OnSwipeRefreshViewListener, OnConnectServerStateListener<Integer> {

	private SwipeRefreshGridView swipeRefreshGridView;
	private List<CityLocation> listData_gridView;
	private GridViewAdapter adapter_gridView;
	private PopupWindow popWindow;
	
	public SelectCityLocationPager(Activity context) {
		super(context);
	}
	
	public SelectCityLocationPager(Activity context,PopupWindow popWindow) {
		super(context);
		this.popWindow = popWindow;
	}

	@Override
	public View initView() {
		View view = View.inflate(mActivity, R.layout.pager_select_city_location, null);
		swipeRefreshGridView = (SwipeRefreshGridView) view.findViewById(R.id.swipeRefreshGridView);
		listData_gridView = new ArrayList<CityLocation>();
		adapter_gridView = new GridViewAdapter(mActivity, listData_gridView);
		swipeRefreshGridView.getGridView().setAdapter(adapter_gridView);
		swipeRefreshGridView.setOnRefreshListener(this);
		LogUtil.LogShitou("预加载"+title, "被执行");
		return view;
	}

	@Override
	public void initData() {
		swipeRefreshGridView.openRefreshState();
	}

	private void getData(String methodName){
		if(InformationCodeUtil.methodNameGetOpenSites.equals(methodName)){
			SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mActivity).getDjLsh());
			requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mActivity).getOpenKey());
			requestSoapObject.addProperty("ProvinceCode", classID);
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
					(mActivity, this, requestSoapObject, methodName);
			connectCustomServiceAsyncTask.initProgressDialog(false);
			connectCustomServiceAsyncTask.execute();
			return;
		}	
	}
	
	
	private class GridViewAdapter extends MyBaseAdapter<CityLocation>{


		public GridViewAdapter(Context mContext, List<CityLocation> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			Button view = (Button)View.inflate(mContext, R.layout.item_adapter_select_city_location_gridview, null);
			view.setText(listData.get(position).getName());
			final String cityCode = new StringBuffer().append(listData.get(position).Code).toString();
			view.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					MyApplication.getmCustomModel(mContext).setLocCityCode(cityCode);
					if(popWindow != null)
					popWindow.dismiss();
				}
			});
			return view;
		}

	}


	@Override
	public void onTopRefrushListener() {
		getData(InformationCodeUtil.methodNameGetOpenSites);
	}

	@Override
	public void onBottomRefrushListener() {
		getData(InformationCodeUtil.methodNameGetOpenSites);
	}

	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		connectSuccessFlag = true;
//		String result = returnSoapObject.getPropertyAsString(methodName + "Result");
//		LogUtil.LogShitou("返回结果",result);
		try {
			Gson gson = new Gson();
			JSONResultMsgModel mJSONResultMsgModel = gson.fromJson(returnString, JSONResultMsgModel.class);
//			JSONArray jsonArry = new JSONArray(mJSONResultMsgModel.getMsg());
			List<CityLocation> listData = gson.fromJson(mJSONResultMsgModel.getMsg(), new TypeToken<List<CityLocation>>(){}.getType());
			if(listData == null || listData.size() == 0){
				
			}else{
				listData_gridView.clear();
				listData_gridView.addAll(listData);
				adapter_gridView.notifyDataSetChanged();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		swipeRefreshGridView.closeRefreshState();
	}

	@Override
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {
		connectSuccessFlag = false;
		swipeRefreshGridView.closeRefreshState();
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		connectSuccessFlag = false;
		swipeRefreshGridView.closeRefreshState();
	}
	
	private class CityLocation{
		/**城市站编号**/
		private String Code;
		/**城市站名称**/
		private String Name;
		public String getCode() {
			return Code;
		}
		public void setCode(String code) {
			Code = code;
		}
		public String getName() {
			return Name;
		}
		public void setName(String name) {
			Name = name;
		}
	}

}
