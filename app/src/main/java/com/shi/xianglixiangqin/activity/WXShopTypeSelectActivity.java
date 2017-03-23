package com.shi.xianglixiangqin.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import butterknife.ButterKnife;
import butterknife.BindView;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultMsgModel;
/***
 * @action 微店推荐商品类型选择界面
 * @author SHI
 * @date  2016年5月13日 16:18:19
 */
public class WXShopTypeSelectActivity extends MyBaseActivity implements OnRefreshListener, OnConnectServerStateListener<Integer>{

	/** 后退控件 **/
	@BindView(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/** 页面标题设置为收货地址 **/
	@BindView(R.id.tv_title)
	 TextView tv_title;
	/** 刷新控件 **/
	@BindView(R.id.SwipeRefreshLayout)
	 SwipeRefreshLayout swipeRefreshLayout;
	/** 当前选择位置数据listView **/
	@BindView(R.id.listView)
	 ListView listView;
	/** 数据源 **/
	private List<WXShopTjClassModel> listData;
	private MyAdapter myAdapter;
	private final int NameGoodsClass01 = 0;
	private final int NameGoodsClass02 = 1;
	/**当前是分类1还是分类2**/
	private int RequestCode_GoodsClassType;
	@Override
	public void initView() {
		setContentView(R.layout.activity_wxshop_tjgoodsclass_select);
		ButterKnife.bind(mContext);
		tv_title.setText("微店推荐栏目类型");
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				previewFinish("",-1);
			}
		});
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				previewFinish(listData.get(position).getName() ,listData.get(position).getID());
			}
		});
	}

	@Override
	public void initData() {
		RequestCode_GoodsClassType = getIntent().getIntExtra(InformationCodeUtil.IntentWXShopTypeSelectActivityGoodsClassNameType,-1);
//		LogUtil.LogShitou("意图数据", GoodsClassNameType);
		listData = new ArrayList<WXShopTjClassModel>();
		myAdapter = new MyAdapter(mContext, listData);
		listView.setAdapter(myAdapter);
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
		getData(InformationCodeUtil.methodNameGetEcShopRecClass);
   	}	
	
	private void getData(String methodName){
		SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
		requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
		requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
		ConnectCustomServiceAsyncTask connectCustomTask = new ConnectCustomServiceAsyncTask(mContext, this, requestSoapObject, methodName);
		connectCustomTask.initProgressDialog(false);
		connectCustomTask.execute();
	}

	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		Gson gson = new Gson();
		swipeRefreshLayout.setRefreshing(false);
		LogUtil.LogShitou("结构数据", returnString);
		try {
			JSONResultMsgModel msgModel = gson.fromJson(returnString, JSONResultMsgModel.class);
			JSONObject jsonObject = new JSONObject(msgModel.getMsg());
			String jsonGoodsTjClass01 = jsonObject.getString("FirstRecClass");
			String jsonGoodsTjClass02 = jsonObject.getString("SecondRecClass");
			if(NameGoodsClass01 == RequestCode_GoodsClassType){
				List<WXShopTjClassModel> result = gson.fromJson(
						jsonGoodsTjClass01, 
						new TypeToken<List<WXShopTjClassModel>>(){}.getType());
				listData.clear();
				listData.addAll(result);
				myAdapter.notifyDataSetChanged();
				return;
			}
			
			if(NameGoodsClass02 == RequestCode_GoodsClassType){
				List<WXShopTjClassModel> result = gson.fromJson(
						jsonGoodsTjClass02, 
						new TypeToken<List<WXShopTjClassModel>>(){}.getType());	
				listData.clear();
				listData.addAll(result);
				myAdapter.notifyDataSetChanged();
				return;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void connectServiceFailed(String returnStrError, String methodName, Integer state,
			boolean whetherRefresh) {
		ToastUtil.show(mContext, "网络异常，数据加载失败");
		swipeRefreshLayout.setRefreshing(false);
	}
	
	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		swipeRefreshLayout.setRefreshing(false);
	}
	
	private void previewFinish(String className, int classID){
		Intent intent = getIntent();
		intent.putExtra(InformationCodeUtil.IntentWXSelectGoodsClassName, className);
		intent.putExtra(InformationCodeUtil.IntentWXSelectGoodsClassID, classID);
		setResult(RESULT_OK,intent);
		finish();
	}

	@Override
	public void onBackPressed() {
		previewFinish("",-1);
	}

	private class MyAdapter extends MyBaseAdapter<WXShopTjClassModel>{

		public MyAdapter(Context mContext, List<WXShopTjClassModel> listData) {
			super(mContext, listData);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(mContext, R.layout.item_adapter_wxshop_typeselect_listview, null);
			TextView tv = (TextView) view.findViewById(R.id.tv_GoodsClassName);
			tv.setText(listData.get(position).getName());
			return view;
		}
		
	}
	
	/**
	 * 微店推荐分类标题model
	 * @author SHI
	 * 2016年5月13日 15:30:23
	 */
	private class WXShopTjClassModel{
		private int ID;
		private boolean IsSelected;
		private String Name;
		public boolean getIsSelected() {
			return IsSelected;
		}
		public int getID() {
			return ID;
		}
		public void setID(int iD) {
			ID = iD;
		}
		public void setIsSelected(boolean isSelected) {
			IsSelected = isSelected;
		}
		public String getName() {
			return Name;
		}
		public void setName(String name) {
			Name = name;
		}
	}
	
}
