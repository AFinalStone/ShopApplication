package com.shuimunianhua.xianglixiangqin.activity;

import java.util.ArrayList;
import java.util.List;
import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.afinalstone.androidstudy.swiperefreshview.OnSwipeRefreshViewListener;
import com.afinalstone.androidstudy.swiperefreshview.SwipeRefreshListView;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.adapter.MyBaseAdapter;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultBaseModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsFilterModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

/***
 * 二级筛选条件界面
 * @author ZHU
 * 2016-2-1 11:39:50
 */
public class FilterSecondActivity extends MyBaseActivity implements OnClickListener, OnConnectServerStateListener<Integer>, OnSwipeRefreshViewListener {
	/** 返回图片控件 **/
	@Bind(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**标题文本**/
	@Bind(R.id.tv_title)
	 TextView tv_title;
	/**完成文本**/
	@Bind(R.id.tv_titleRight)
	 TextView tv_titleRight;
	/**条件列表 **/
	@Bind(R.id.swipeRefreshListView)
	 SwipeRefreshListView swipeRefreshListView;
	 List<GoodsFilterModel> listData;
	 BFilterListViewAdapter bFilterListViewAdapter;
	/**下方的清除筛选和完成按钮外部布局**/
	@Bind(R.id.relativeLayout_filter)
	 RelativeLayout filter_layout;
	
	/**ListView当前选中行数**/
	 int index;
	/**二级筛选条件ID**/
	 int pid;
	/**数据传递的意图对象**/
	 Intent intent;

	public void initView() {
		super.setContentView(R.layout.activity_filter);
		ButterKnife.bind(this);
	}

	public void initData() {
		
		listData = new ArrayList<GoodsFilterModel>();
		bFilterListViewAdapter = new BFilterListViewAdapter(this,listData);
		swipeRefreshListView.getListView().setAdapter(bFilterListViewAdapter);	
		
		swipeRefreshListView.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
					GoodsFilterModel productFilterModel= listData.get(position);
					intent.putExtra("index",index);	
					intent.putExtra("FilterName",productFilterModel.getFilterName());
					intent.putExtra("DjLsh",productFilterModel.getDjLsh());	
					//设置Intent状态码
					setResult(RESULT_OK,getIntent());
					//结束Intent
					finish();					
				}
		});
		
		iv_titleLeft.setOnClickListener(this);
		iv_titleLeft.setVisibility(View.VISIBLE);
		filter_layout.setVisibility(View.GONE);

		intent = getIntent();
		index = intent.getIntExtra("index", -1);
		pid = intent.getIntExtra("DjLsh", -1);
		tv_title.setText(intent.getStringExtra("FilterName"));
		
		swipeRefreshListView.setOnRefreshListener(this);
		swipeRefreshListView.openRefreshState();
	}

	 void getData() {
		listData.clear();
		bFilterListViewAdapter.notifyDataSetChanged();	
		ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
				mContext, this, getSoapObject(), InformationCodeUtil.methodNameGetSecondFilterList);
		connectGoodsServiceAsyncTask.initProgressDialog(false, "请稍等...");
		connectGoodsServiceAsyncTask.execute();
	}
	
	
	
	 SoapObject getSoapObject() {
		SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE,
				InformationCodeUtil.methodNameGetSecondFilterList);
		soapObject.addProperty("parentID", pid);
		return soapObject;
	}
	
	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.iv_titleLeft:
				finish();
			break;
		default:
			break;
		}
	}
	
	/**
	 * @action 产品定位条件适配器
	 * @author SHI
	 * 2016-2-17 15:36:02
	 */
	public class BFilterListViewAdapter extends MyBaseAdapter<GoodsFilterModel> {

		public BFilterListViewAdapter(Context mContext,
				List<GoodsFilterModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv_filterName;
			if(convertView == null){
				convertView = View.inflate(mContext, R.layout.item_adapter_filter_blistview, null);
				tv_filterName = (TextView) convertView.findViewById(R.id.tv_filterName);
				convertView.setTag(tv_filterName);
			}else{
				tv_filterName = (TextView) convertView.getTag();
			}
			
			final GoodsFilterModel productFilterModel = listData.get(position);
			if(productFilterModel != null){
				tv_filterName.setText(productFilterModel.getFilterName());
			}
			
			return convertView;
		}

	}
	
	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
//		Object  provinceSoapObject = (Object ) returnSoapObject.getProperty(methodName+"Result");
		swipeRefreshListView.closeRefreshState();
		Gson gson  = new Gson();
//		JSONProductFilterModel returnModel = gson.fromJson(provinceSoapObject.toString(), JSONProductFilterModel.class);		
		JSONResultBaseModel<GoodsFilterModel> returnModel = gson.fromJson
				(returnString, new TypeToken<JSONResultBaseModel<GoodsFilterModel>>(){}.getType());		
		listData.addAll(returnModel.getList()) ;
		bFilterListViewAdapter.notifyDataSetChanged();		
	}
	
	@Override
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {
		swipeRefreshListView.closeRefreshState();
		ToastUtils.show(mContext, "获取过滤信息失败，请检查网络状况");
	}
	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		
	}

	@Override
	public void onTopRefrushListener() {
		getData();		
	}

	@Override
	public void onBottomRefrushListener() {
		getData();		
	}

}
