package com.shi.xianglixiangqin.frament;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.activity.MyBaseActivity;
import com.shi.xianglixiangqin.activity.SearchGoodsActivity;
import com.shi.xianglixiangqin.adapter.GoodsClassifcationAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultBaseModel;
import com.shi.xianglixiangqin.model.GoodsClassModel;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/***
 * @author SHI
 * @action 分类
 * @date 2015-7-18 上午11:29:00
 */
public class MainGoodsClassTypeFragment_DaiNiFei extends MyBaseFragment<MyBaseActivity> implements OnConnectServerStateListener<Integer> {

    /**
     * 各级分类数据集合
     **/
    private List<GoodsClassModel> aListData = new ArrayList<GoodsClassModel>();
    private List<GoodsClassModel> bListData = new ArrayList<GoodsClassModel>();
    private List<GoodsClassModel> cListData = new ArrayList<GoodsClassModel>();

    /**
     * ListView控件
     **/
    @BindView(R.id.aCategory_listView)
    ListView aCategoryListView;
    @BindView(R.id.bCategory_listView)
    ListView bCategoryListView;
    @BindView(R.id.cCategory_listView)
    ListView cCategoryListView;

    /**
     * 数据适配器
     **/
    private GoodsClassifcationAdapter aCategoryAdapter,
            bCategoryAdapter,
            cCategoryAdapter;
    /**
     * 页面标题
     **/
    @BindView(R.id.tv_title)
    TextView tv_title;
    /**
     * 网络请求是否成功
     **/
    public boolean connectSuccessFlag;
    /**
     * 条目被选中位置
     */
    int aSelectPosition;
    int bSelectPosition;
    /**
     * 当前界面内容
     **/
    private View rootView;

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_classification_dainifei, container, false);
        unbinder =  ButterKnife.bind(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {

        tv_title.setText("产品分类");
    }


    /**
     * 初始化一级列表
     **/
    @Override
    public void initData() {

        //初始化AListView相关数据
        aListData = new ArrayList<GoodsClassModel>();
        bListData = new ArrayList<GoodsClassModel>();
        cListData = new ArrayList<GoodsClassModel>();

        aCategoryAdapter = new GoodsClassifcationAdapter(mActivity, aListData, InformationCodeUtil.flagOfAListView);
        bCategoryAdapter = new GoodsClassifcationAdapter(mActivity, bListData, InformationCodeUtil.flagOfBListView);
        cCategoryAdapter = new GoodsClassifcationAdapter(mActivity, cListData, InformationCodeUtil.flagOfCListView);

        aCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                aSelectPosition = position;
                bListData.clear();
                cListData.clear();
                bCategoryAdapter.notifyDataSetChanged();
                cCategoryAdapter.notifyDataSetChanged();
                getData(aListData.get(position).getDjLsh(), InformationCodeUtil.flagOfBListView);
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
                getData(bListData.get(position).getDjLsh(), InformationCodeUtil.flagOfCListView);
            }

        });


        //初始化CistView相关数据
        cCategoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                //带你飞版
                GoodsClassModel productClassModel = cListData.get(position);
                Intent intent = new Intent(mActivity, SearchGoodsActivity.class);
                intent.putExtra("currentFilterClassID", productClassModel.getDjLsh());
                intent.putExtra("currentShopID", -1);
                mActivity.startActivity(intent);
            }

        });

        aCategoryListView.setAdapter(aCategoryAdapter);
        bCategoryListView.setAdapter(bCategoryAdapter);
        cCategoryListView.setAdapter(cCategoryAdapter);
        getData(-1, InformationCodeUtil.flagOfAListView);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            //第一次请求数据，刷新AListView的数据
            getData(-1, InformationCodeUtil.flagOfAListView);
        }
    }

    public void getData(int parentID, int flagOfListview) {
        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE,
                InformationCodeUtil.methodNameGetSiteClassList);
        requestSoapObject.addProperty("parentID", parentID);
        requestSoapObject.addProperty("siteCode", MyApplication.getmCustomModel(mActivity).getLocCityCode());
        ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
                mActivity, this, requestSoapObject, InformationCodeUtil.methodNameGetSiteClassList, flagOfListview);
        connectGoodsServiceAsyncTask.execute();

    }


    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, Integer state, boolean whetherRefresh) {

        connectSuccessFlag = true;

        Gson gson = new Gson();

        JSONResultBaseModel<GoodsClassModel> results = gson.fromJson
                (returnString, new TypeToken<JSONResultBaseModel<GoodsClassModel>>() {
                }.getType());

        if (state == InformationCodeUtil.flagOfAListView) {
            aListData.addAll(results.getList());
            aCategoryAdapter.notifyDataSetChanged();
            if(aListData.size() > 0)
            getData(aListData.get(aSelectPosition).getDjLsh(), InformationCodeUtil.flagOfBListView);
        }

        if (state == InformationCodeUtil.flagOfBListView) {
            bListData.addAll(results.getList());
            bCategoryAdapter.notifyDataSetChanged();
        }

        if (state == InformationCodeUtil.flagOfCListView) {
            cListData.clear();
            cListData.addAll(results.getList());
            cCategoryAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Integer state,
                                     boolean whetherRefresh) {
        connectSuccessFlag = false;
        ToastUtil.show(mActivity, "分类信息请求失败,请检查网络状况");
    }

    @Override
    public void connectServiceCancelled(String returnString,
                                        String methodName, Integer state, boolean whetherRefresh) {
        connectSuccessFlag = false;
    }


}
