package com.shuimunianhua.xianglixiangqin.activity;

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

import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.adapter.GoodsClassifcationAdapter;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultBaseModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsClassModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;

/***
 * 
 * @author SHI 
 * 我的代理商品分类界面  
 * 2016-2-1 11:33:53
 *
 */
public class SearchGoodsMyAgentClassTypeActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer> {

	/**各级分类数据集合**/
	private List<GoodsClassModel> aListData,
								    bListData,
								    cListData;
	
	/**ListView控件**/
	@Bind(R.id.aCategory_listView)
	 ListView aCategoryListView;
	@Bind(R.id.bCategory_listView)
	 ListView bCategoryListView;
	@Bind(R.id.cCategory_listView)
	 ListView cCategoryListView;
	
	/**数据适配器**/
	public GoodsClassifcationAdapter aCategoryAdapter,
						   bCategoryAdapter,
						   cCategoryAdapter;
	/**返回控件**/
	@Bind(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**页面标题**/
	@Bind(R.id.tv_title)
	 TextView tv_title;
	/**条目被选中位置*/
	int aSelectPosition = 0;
	int bSelectPosition = 0;	
	@Override
	public void initView() {
		setContentView(R.layout.fragment_classification);
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
				Intent intent = new Intent(SearchGoodsMyAgentClassTypeActivity.this,SearchGoodsMyAgentActivity.class);
				intent.putExtra(InformationCodeUtil.IntentSearchGoodsFilterClassID, productClassModel.getDjLsh());
				startActivity(intent);
				finish();
			}

		});
	}
	
	
	public void getData(int parentID, int flagOfListview) {
		String methodName = InformationCodeUtil.methodNameGetCurrentAgentGoodsClasses;
		SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
		soapObject.addProperty("customID", MyApplication
				.getmCustomModel(mContext).getDjLsh());
		soapObject.addProperty("parentID", parentID);

		ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask
		(mContext, this, soapObject, methodName, flagOfListview);
		connectGoodsServiceAsyncTask.execute();

	}
	
	
	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		
//		Object provinceSoapObject = (Object) returnSoapObject.getProperty(methodName + "Result");
		Gson gson = new Gson();
		List<GoodsClassModel> listData = null;
		try {
//			JSONProductClassModel mJSONProductClassModel = gson.fromJson(provinceSoapObject.toString(),
//					JSONProductClassModel.class);
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
	public void connectServiceFailed(String methodName, Integer state,
			boolean whetherRefresh) {
			ToastUtils.show(mContext, "获取数据失败，请检查网络状况的");
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		// TODO Auto-generated method stub
		
	}

}




