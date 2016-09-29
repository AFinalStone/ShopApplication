package com.shuimunianhua.xianglixiangqin.activity;

import java.util.ArrayList;
import java.util.List;
import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
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
import com.shuimunianhua.xianglixiangqin.util.StringUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

/***
 * @action 搜索筛选界面
 * @author SHI
 * @date 2016-2-1 11:39:38
 */
public class FilterFirstActivity extends MyBaseActivity implements OnClickListener, OnConnectServerStateListener<Integer>, OnSwipeRefreshViewListener {
	/** 返回图片控件 **/
	@Bind(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**标题文本 **/
	@Bind(R.id.tv_title)
	 TextView tv_title;
	/**完成文本**/
	@Bind(R.id.tv_titleRight)
	 TextView tv_titleRight;
	
	/** 清空筛选 **/
	@Bind(R.id.btn_clearFilter)
	 Button btn_clearFilter;
	/** 确认筛选 **/
	@Bind(R.id.btn_confirmFilter)
	 Button btn_confirmFilter;
	
	/*** 筛选条件列表 **/
	@Bind(R.id.swipeRefreshListView)
	 SwipeRefreshListView swipeRefreshListView;
	/**ListView 对应的数据**/
	 List<GoodsFilterModel> listData = new ArrayList<GoodsFilterModel>();
	/** 适配器 **/
	 FilterListViewAdapter filterListViewAdapter;
	 int currentFilterClassID;
//	 Intent intent;
	
	public void initView() {
		super.setContentView(R.layout.activity_filter);
		ButterKnife.bind(this);
		tv_title.setText("一级筛选");
		tv_titleRight.setOnClickListener(this);
		btn_clearFilter.setOnClickListener(this);
		btn_confirmFilter.setOnClickListener(this);
		iv_titleLeft.setOnClickListener(this);
		iv_titleLeft.setVisibility(View.VISIBLE);
		swipeRefreshListView.getListView().setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
				GoodsFilterModel mProductModel = listData.get(position);
				Intent intent = new Intent(FilterFirstActivity.this,
						FilterSecondActivity.class);
				intent.putExtra("index", position);
				intent.putExtra("FilterName",mProductModel.getFilterName());
				intent.putExtra("DjLsh", mProductModel.getDjLsh());
				FilterFirstActivity.this.startActivityForResult(intent, 1);
			}
		});
		swipeRefreshListView.setOnRefreshListener(this);
		filterListViewAdapter = new FilterListViewAdapter(mContext,listData);
		swipeRefreshListView.getListView().setAdapter(filterListViewAdapter);
	}
	public void initData() {

		currentFilterClassID = getIntent().getIntExtra(InformationCodeUtil.IntentFilterFirstActivityFilterClassID, 0);

		swipeRefreshListView.openRefreshState();
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (resultCode) {
		case RESULT_OK:
			int inde = data.getIntExtra("index", -1);
			if (inde != -1) {
				listData.get(inde).setSelectedSubName(
						data.getStringExtra("FilterName"));
				listData.get(inde).setSelectedSubPid(
						data.getIntExtra("DjLsh", -1)+"");
//				LogUtils.LogShitou("FilterName",data.getStringExtra("FilterName"));
//				LogUtils.LogShitou("DjLsh",data.getIntExtra("DjLsh", -1)+"");
				filterListViewAdapter.notifyDataSetChanged();
			}
			break;
		default:
			break;
		}
	}

	 private void getData() {
		listData.clear();
		filterListViewAdapter.notifyDataSetChanged();
		 String methodName = InformationCodeUtil.methodNameGetClassFilterList;
		SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
		soapObject.addProperty("classID", currentFilterClassID);		
		soapObject.addProperty("classID", 1);
		ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
				mContext, this, soapObject, methodName);
		connectGoodsServiceAsyncTask.initProgressDialog(false);
		connectGoodsServiceAsyncTask.execute();		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_titleLeft:
				finish();
				break;
		case R.id.tv_titleRight:
		case R.id.btn_confirmFilter:
				confirmFilterResult();
				break;
		case R.id.btn_clearFilter:
				clearFilter();
				break;
		default:
				break;
		}
	}

	/**提交筛选结果**/
	 private void confirmFilterResult(){
		 Intent intent = getIntent();
		 StringBuffer strFilter = new StringBuffer("");
		 if(listData != null){
			 int index = -1;
			 for(int i=0; i<listData.size(); i++){
				 if(!"-1".equals(listData.get(i).getSelectedSubPid())){
					 if(index != -1){
						 strFilter.append(",");
					 }
					 index = i;
					 strFilter.append(listData.get(i).getSelectedSubPid());
				 }
			 }
		 }
		 if(StringUtil.isEmpty(strFilter.toString())){
			 intent.putExtra(InformationCodeUtil.IntentFilterFirstActivityFilterClassID,"");
		 }else{
			 intent.putExtra(InformationCodeUtil.IntentFilterFirstActivityFilterClassID,strFilter.toString());
		 }
		 setResult( RESULT_OK, intent);
		 finish();
	}

	/**清空筛选**/
	private void clearFilter(){
		if(listData != null ) {
			for (int i = 0; i < listData.size(); i++) {
				listData.get(i).setSelectedSubName(null);
				listData.get(i).setSelectedSubPid(null);
			}
			filterListViewAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void connectServiceSuccessful(String returnString, String methodName, Integer state, boolean whetherRefresh) {
		try {
			Gson gson  = new Gson();
			JSONResultBaseModel<GoodsFilterModel> returnModel = gson.fromJson
                    (returnString, new TypeToken<JSONResultBaseModel<GoodsFilterModel>>(){}.getType());
			listData.addAll(returnModel.getList());
			filterListViewAdapter.notifyDataSetChanged();
		} catch (Exception e) {
			e.printStackTrace();
		}
		swipeRefreshListView.closeRefreshState();
	}
	@Override
	public void connectServiceFailed(String methodName, Integer state, boolean whetherRefresh) {
		swipeRefreshListView.closeRefreshState();
		ToastUtils.show(mContext, "数据请求失败,请检查网络或重新请求");	
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
	
	/**
	 * @action 筛选条件适配器
	 * @author SHI
	 * 2016-2-17 15:36:36
	 */
	public class FilterListViewAdapter extends MyBaseAdapter<GoodsFilterModel>{
		
		
		public FilterListViewAdapter(Context mContext,
				List<GoodsFilterModel> listData) {
			super(mContext, listData);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent){
			ViewHolder holder;
			if(convertView == null){
				convertView = View.inflate( mContext, R.layout.item_adapter_filter_alistview, null);
				holder = new ViewHolder();
				holder.conditionTitleTextView  = (TextView)convertView.findViewById(R.id.conditionTitle_textView);
				holder.conditionTextView  = (TextView)convertView.findViewById(R.id.condition_textView);
				convertView.setTag(holder);
			}else{
				holder=(ViewHolder)convertView.getTag();
			}
			GoodsFilterModel mFilterConditionModel = listData.get(position);
			if(mFilterConditionModel != null){
				holder.conditionTitleTextView.setText(mFilterConditionModel.getFilterName());
				
				holder.conditionTextView.setTextColor(Color.RED);
				
				if(mFilterConditionModel.getSelectedSubName()!=null){
					holder.conditionTextView.setText(mFilterConditionModel.getSelectedSubName());
				}else{
					holder.conditionTextView.setText("全部");
				}
			}
			return convertView; 
		}
		 class ViewHolder{
			/**条件选项**/
			TextView conditionTitleTextView;
			/**条件**/
			TextView conditionTextView;
		}

	}
}
