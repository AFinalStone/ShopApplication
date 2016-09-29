package com.shuimunianhua.xianglixiangqin.frament;

import java.util.ArrayList;
import java.util.List;

import org.ksoap2.serialization.SoapObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.shuimunianhua.xianglixiangqin.activity.SearchGoodsActivity;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import butterknife.ButterKnife;
import butterknife.Bind;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.activity.MainActivity;
import com.shuimunianhua.xianglixiangqin.adapter.GoodsClassifcationAdapter;
import com.shuimunianhua.xianglixiangqin.app.MyApplication;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultBaseModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsClassModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;

/***
 * @action 分类
 * @author SHI
 * @date  2015-7-18 上午11:29:00
 */
public class MainGoodsClassTypeFragment extends MyBaseFragment<MainActivity> implements OnConnectServerStateListener<Integer>{
	/**各级分类数据集合**/
	private List<GoodsClassModel> aListData = new ArrayList<GoodsClassModel>();
	private List<GoodsClassModel> bListData = new ArrayList<GoodsClassModel>();
	private List<GoodsClassModel> cListData = new ArrayList<GoodsClassModel>();
	
	/**ListView控件**/
	@Bind(R.id.aCategory_listView)
	 ListView aCategoryListView;
	@Bind(R.id.bCategory_listView)
	 ListView bCategoryListView;
	@Bind(R.id.cCategory_listView)
	 ListView cCategoryListView;
	
	/**数据适配器**/
	private GoodsClassifcationAdapter aCategoryAdapter,
						   bCategoryAdapter,
						   cCategoryAdapter;
	/**页面标题**/
	@Bind(R.id.tv_title)
	 TextView tv_title;
	/**网络请求是否成功**/
	public boolean connectSuccessFlag;
	/**条目被选中位置*/
	int aSelectPosition;
	int bSelectPosition;
	/**当前界面内容**/
	private View rootView;

	@Override
	public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if(rootView == null){
			rootView = inflater.inflate(R.layout.fragment_classification, container, false);
			ButterKnife.bind(this,rootView);
			initView();
		}
		return rootView;
	}

	public void initView() {

		tv_title.setText("产品分类");

		aCategoryAdapter = new GoodsClassifcationAdapter(mActivity, aListData, InformationCodeUtil.flagOfAListView);
		bCategoryAdapter = new GoodsClassifcationAdapter(mActivity, bListData, InformationCodeUtil.flagOfBListView);
		cCategoryAdapter = new GoodsClassifcationAdapter(mActivity, cListData, InformationCodeUtil.flagOfCListView);
		
		aCategoryListView.setAdapter(aCategoryAdapter);
		bCategoryListView.setAdapter(bCategoryAdapter);
		cCategoryListView.setAdapter(cCategoryAdapter);

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
				Intent intent = new Intent(mActivity, SearchGoodsActivity.class);
				intent.putExtra(InformationCodeUtil.IntentSearchGoodsFilterClassID, productClassModel.getDjLsh());
				if(!InformationCodeUtil.whetherIsDaiNiFei){
					//如果是聚合批发系统
					intent.putExtra(InformationCodeUtil.IntentSearchGoodsCurrentShopID, mActivity.currentShopID);
				}
				mActivity.startActivity(intent);
			}

		});		
	}


	/**初始化一级列表**/
	public void initData() {

				//第一次请求数据，刷新AListView的数据
				connectSuccessFlag = true;
				aSelectPosition = 0;
				bSelectPosition = 0;
				aListData.clear();
				bListData.clear();
				cListData.clear();
				getData(-1, InformationCodeUtil.flagOfAListView);

	}
	
	
	public void getData(int parentID, int flagOfListview) {

		if(InformationCodeUtil.whetherIsDaiNiFei){
			//获取平台所有商品的分类信息,使用城市站
			String methodName = InformationCodeUtil.methodNameGetSiteClassList;
			SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE,methodName);
			soapObject.addProperty("parentID", parentID);
			soapObject.addProperty("siteCode", MyApplication.getmCustomModel(mActivity).getLocCityCode());
			ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
					mActivity, this, soapObject, methodName , flagOfListview, true);
			connectGoodsServiceAsyncTask.execute();
			return;
		}

		//获取某个特定店铺的商品分类信息
		String methodName = InformationCodeUtil.methodNameGetShopGoodsClasses;
		SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE,methodName);
//		soapObject.addProperty("shopUserID", mActivity.currentShopUserID);
		soapObject.addProperty("parentID", parentID);
		ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
				mActivity, this, soapObject, methodName, flagOfListview);
		connectGoodsServiceAsyncTask.execute();
	}
	
	
	@Override
	public void connectServiceSuccessful(String returnString,String methodName,
			Integer state, boolean whetherRefresh) {
		Gson gson = new Gson();

		try {
			JSONResultBaseModel<GoodsClassModel> results = gson.fromJson
                    (returnString, new TypeToken<JSONResultBaseModel<GoodsClassModel>>(){}.getType());

			if(state == InformationCodeUtil.flagOfAListView){
				aListData.addAll(results.getList());
				aCategoryAdapter.notifyDataSetChanged();
				getData( aListData.get(aSelectPosition).getDjLsh(), InformationCodeUtil.flagOfBListView);
			}

			if(state == InformationCodeUtil.flagOfBListView){
				bListData.addAll(results.getList());
				bCategoryAdapter.notifyDataSetChanged();
			}

			if(state == InformationCodeUtil.flagOfCListView){
				cListData.clear();
				cListData.addAll(results.getList());
				cCategoryAdapter.notifyDataSetChanged();
			}
		} catch (Exception e) {
			e.printStackTrace();
			connectSuccessFlag = false;
		}


	}

	@Override
	public void connectServiceFailed(String methodName, Integer state, boolean whetherRefresh) {
		connectSuccessFlag = false;
		ToastUtils.show(mActivity, "分类信息请求失败,请检查网络状况");
		
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		connectSuccessFlag = false;
	}


}
