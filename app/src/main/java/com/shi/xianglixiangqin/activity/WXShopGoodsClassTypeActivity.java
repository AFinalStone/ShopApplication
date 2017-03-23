package com.shi.xianglixiangqin.activity;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.shi.xianglixiangqin.util.ToastUtil;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import butterknife.ButterKnife;
import butterknife.BindView;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.GoodsClassifcationAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultBaseModel;
import com.shi.xianglixiangqin.model.GoodsClassModel;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;

/***
 * 
 * @author SHI 
 * 微店商品管理分类界面  
 * 2016年5月16日 12:35:55
 *
 */
public class WXShopGoodsClassTypeActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer> {

	/**各级分类数据集合**/
	private List<GoodsClassModel> aListData,
								    bListData,
								    cListData;
	
	/**ListView控件**/
	@BindView(R.id.aCategory_listView)
	 ListView aCategoryListView;
	@BindView(R.id.bCategory_listView)
	 ListView bCategoryListView;
	@BindView(R.id.cCategory_listView)
	 ListView cCategoryListView;
	
	/**数据适配器**/
	public GoodsClassifcationAdapter aCategoryAdapter,
						   bCategoryAdapter,
						   cCategoryAdapter;
	/**返回控件**/
	@BindView(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**页面标题**/
	@BindView(R.id.tv_title)
	 TextView tv_title;
	/**条目被选中位置*/
	int aSelectPosition = 0;
	int bSelectPosition = 0;	
	@Override
	public void initView() {
		setContentView(R.layout.fragment_classification_dainifei);
		ButterKnife.bind(this);
	}
	
	/**初始化一级列表**/
	@Override
	public void initData() {

		tv_title.setText("产品分类");
		//初始化AListView相关数据
		aListData = new ArrayList<GoodsClassModel>();
		bListData = new ArrayList<GoodsClassModel>();
		cListData = new ArrayList<GoodsClassModel>();
		
		aCategoryAdapter = new GoodsClassifcationAdapter(mContext, aListData, InformationCodeUtil.flagOfAListView);
		bCategoryAdapter = new GoodsClassifcationAdapter(mContext, bListData, InformationCodeUtil.flagOfBListView);
		cCategoryAdapter = new GoodsClassifcationAdapter(mContext, cListData, InformationCodeUtil.flagOfCListView);
		
		aCategoryListView.setAdapter(aCategoryAdapter);
		bCategoryListView.setAdapter(bCategoryAdapter);
		cCategoryListView.setAdapter(cCategoryAdapter);
		//第一次请求数据，刷新AListView的数据
		getData( -1, InformationCodeUtil.flagOfAListView);
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		aCategoryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				aSelectPosition = position;
				bListData.clear();
				cListData.clear();
				bCategoryAdapter.notifyDataSetChanged();
				cCategoryAdapter.notifyDataSetChanged();
				getData(aListData.get(position).getDjLsh(),InformationCodeUtil.flagOfBListView);				
			}
		});
		//初始化BListView相关数据
		bCategoryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				bSelectPosition = position;
				cListData.clear();
				cCategoryAdapter.notifyDataSetChanged();
				getData(bListData.get(position).getDjLsh(),InformationCodeUtil.flagOfCListView);
			}

		});	
		//初始化CistView相关数据
		cCategoryListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					
				GoodsClassModel productClassModel = cListData.get(position);
				Intent intent = new Intent(WXShopGoodsClassTypeActivity.this,WXShopGoodsManageActivity.class);
				intent.putExtra(InformationCodeUtil.IntentWXShopGoodsManagerActivityFilterClassID, productClassModel.getDjLsh());
				startActivity(intent);
				finish();
			}

		});
	}
	
	
	public void getData(int parentID, int flagOfListView) {
		
		SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE,
				"GetCurrentAgentGoodsClasses");
		soapObject.addProperty("customID", MyApplication
				.getmCustomModel(mContext).getDjLsh());
		soapObject.addProperty("parentID", parentID);
		
		ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask
		(mContext, this, soapObject, InformationCodeUtil.methodNameGetCurrentAgentGoodsClasses, flagOfListView);
		connectGoodsServiceAsyncTask.execute();
	}
	
	
	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		
		Gson gson = new Gson();
		List<GoodsClassModel> listData = null;
		try {
			JSONResultBaseModel<GoodsClassModel> mJSONProductClassModel = gson.fromJson
					(returnString, new TypeToken<JSONResultBaseModel<GoodsClassModel>>(){}.getType());			
			listData = mJSONProductClassModel.getList();
		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
		if(listData == null || listData.size() == 0){
			return;
		}
		if(state == InformationCodeUtil.flagOfAListView){
			aListData.addAll(listData);
			aCategoryAdapter.notifyDataSetChanged();
			getData( aListData.get(aSelectPosition).getDjLsh(), InformationCodeUtil.flagOfBListView);
		}
		
		if(state == InformationCodeUtil.flagOfBListView)
		{
			bListData.addAll(listData);
//			LogUtils.LogShitou("bListData", bListData.get(0).toString());
			bCategoryAdapter.notifyDataSetChanged();
		}
		
		if(state == InformationCodeUtil.flagOfCListView)
		{
			cListData.clear();
			cListData.addAll(listData);
//			LogUtils.LogShitou("cListData", cListData.get(0).toString());
			cCategoryAdapter.notifyDataSetChanged();
		}		
	}

	@Override
	public void connectServiceFailed(String returnStrError, String methodName, Integer state,
			boolean whetherRefresh) {
			ToastUtil.show(mContext, "获取数据失败，请检查网络状况的");
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		
	}

}




