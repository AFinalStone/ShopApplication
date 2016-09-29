package com.shuimunianhua.xianglixiangqin.frament;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.activity.SearchGoodsActivity;
import com.shuimunianhua.xianglixiangqin.activity.ShopActivity;
import com.shuimunianhua.xianglixiangqin.adapter.GoodsClassifcationAdapter;
import com.shuimunianhua.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shuimunianhua.xianglixiangqin.json.JSONResultBaseModel;
import com.shuimunianhua.xianglixiangqin.model.GoodsClassModel;
import com.shuimunianhua.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shuimunianhua.xianglixiangqin.task.ConnectServiceUtil;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.LogUtil;
import com.shuimunianhua.xianglixiangqin.util.ToastUtils;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopGoodsClassTypeFragment extends MyBaseFragment<ShopActivity> implements OnConnectServerStateListener<Integer> {

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
    /**条目被选中位置*/
    int  aSelectPosition = 0;
    int bSelectPosition = 0;

    private View rootView;

    public ShopGoodsClassTypeFragment() {
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.pager_shop_class, container, false);
            ButterKnife.bind(this,rootView);
            //初始化AListView相关数据
            aListData = new ArrayList<GoodsClassModel>();
            bListData = new ArrayList<GoodsClassModel>();
            cListData = new ArrayList<GoodsClassModel>();

            aCategoryAdapter = new GoodsClassifcationAdapter(mActivity, aListData, InformationCodeUtil.flagOfAListView);
            bCategoryAdapter = new GoodsClassifcationAdapter(mActivity, bListData, InformationCodeUtil.flagOfBListView);
            cCategoryAdapter = new GoodsClassifcationAdapter(mActivity, cListData, InformationCodeUtil.flagOfCListView);

            aCategoryListView.setAdapter(aCategoryAdapter);
            bCategoryListView.setAdapter(bCategoryAdapter);
            cCategoryListView.setAdapter(cCategoryAdapter);
            //第一次请求数据，刷新AListView的数据
            aCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
            bCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

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
            cCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {

                    GoodsClassModel productClassModel = cListData.get(position);
                    Intent intent = new Intent(mActivity,SearchGoodsActivity.class);
                    intent.putExtra(InformationCodeUtil.IntentSearchGoodsCurrentShopID, mActivity.currentShopID);
                    intent.putExtra(InformationCodeUtil.IntentSearchGoodsFilterClassID, productClassModel.getDjLsh());
                    mActivity.startActivity(intent);

                }
            });
        }
        return rootView;
    }

    /**初始化一级列表**/
    @Override
    public void initData() {
        if(mActivity.currentShopUserID != -1){
            if(!connectSuccessFlag){
                connectSuccessFlag = true;
                getData( -1, InformationCodeUtil.flagOfAListView);
            }
        }
    }

    public void getData(int parentID, int flagOfListView) {
        String methodName = InformationCodeUtil.methodNameGetShopGoodsClasses;
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE,methodName );
        soapObject.addProperty("shopUserID", mActivity.currentShopUserID);
        soapObject.addProperty("parentID", parentID);
        ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
                mActivity, this, soapObject, methodName , flagOfListView);
        connectGoodsServiceAsyncTask.execute();

    }


    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, Integer state, boolean whetherRefresh) {

        LogUtil.LogShitou("商品分类",returnString);
        try {
            Gson gson = new Gson();
            JSONResultBaseModel<GoodsClassModel> status = gson.fromJson
                    (returnString, new TypeToken<JSONResultBaseModel<GoodsClassModel>>(){}.getType());
            if(state == InformationCodeUtil.flagOfAListView){

                aListData.addAll(status.getList());
                aCategoryAdapter.notifyDataSetChanged();
                getData( aListData.get(aSelectPosition).getDjLsh(), InformationCodeUtil.flagOfBListView);
            }

            if(state == InformationCodeUtil.flagOfBListView)
            {
                bListData.addAll(status.getList());
                bCategoryAdapter.notifyDataSetChanged();
            }

            if(state == InformationCodeUtil.flagOfCListView)
            {
                cListData.clear();
                cListData.addAll(status.getList());
                cCategoryAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            connectSuccessFlag = false;
            e.printStackTrace();
        }
    }

    @Override
    public void connectServiceFailed(String methodName, Integer state, boolean whetherRefresh) {
        ToastUtils.show(mActivity, "网络异常，数据请求失败");
        connectSuccessFlag = false;

    }

    @Override
    public void connectServiceCancelled(String returnString,
                                        String methodName, Integer state, boolean whetherRefresh) {
        connectSuccessFlag = false;
    }
}
