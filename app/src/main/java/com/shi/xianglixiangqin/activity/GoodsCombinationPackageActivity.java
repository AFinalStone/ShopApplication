package com.shi.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultBaseModel;
import com.shi.xianglixiangqin.model.GoodsGeneralModel;
import com.shi.xianglixiangqin.model.GoodsPackageStandardModel;
import com.shi.xianglixiangqin.model.GoodsPackageModel;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.ToastUtil;
import com.shi.xianglixiangqin.view.FlowTagLayout;

import org.ksoap2.serialization.SoapObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/****
 * 套餐选择界面 加入购物车和立即购买
 * @author SHI
 * 2016-3-15 16:03:03
 */
public class GoodsCombinationPackageActivity extends MyBaseTranslucentActivity implements OnConnectServerStateListener<Integer> {

    /**关闭当前界面控件**/
    @BindView(R.id.iv_closeActivity)
    ImageView iv_closeActivity;
    /**当前商品图标**/
    @BindView(R.id.iv_goodsImages)
    ImageView iv_goodsImages;
    /**当前商品名称**/
    @BindView(R.id.tv_goodsName)
    TextView tv_goodsName;
    /**当前商品价格**/
    @BindView(R.id.tv_goodsPrice)
    TextView tv_goodsPrice;
    /**当前商品选中套餐描述**/
    @BindView(R.id.tv_goodsPackageDesc)
    TextView tv_goodsPackageDesc;
    /**当前商品库存**/
    @BindView(R.id.tv_goodsStock)
    TextView tv_goodsStock;

    /**套餐描述**/
    @BindView(R.id.tv_goodsPackage)
    TextView tv_goodsPackage;

    /**当前商品套餐**/
    @BindView(R.id.flowTagLayout_goodsPackage)
    FlowTagLayout flowTagLayout_goodsPackage;
    List<GoodsPackageModel> listGoodsPackageData = new ArrayList<>();
    GoodPackageTypeAdapter adapterGoodsPackage;

    /**套餐规格描述**/
    @BindView(R.id.tv_goodsPackageStandard)
    TextView tv_goodsPackageStandard;

    /**当前商品套餐规格**/
    @BindView(R.id.flowTagLayout_goodsPackageStandard)
    FlowTagLayout flowTagLayout_goodsPackageStandard;
    List<GoodsPackageStandardModel> listPackageStandardData;
    GoodPackageStandardAdapter adapterGoodsPackageStandard;

    /**当前商品详细信息**/
    GoodsGeneralModel mGoodsGeneralModel;
    /**当前被选中套餐数据**/
    GoodsPackageModel selectProductPackage;
    /**当前被选中套餐颜色数据*/
    GoodsPackageStandardModel selectGoodsPackageStandard;

    @Override
    public void initView() {
        setContentView(R.layout.activity_combination_package);
        ButterKnife.bind(mContext);

        //初始化商品套餐数据
        adapterGoodsPackage = new GoodPackageTypeAdapter(mContext, listGoodsPackageData);
        flowTagLayout_goodsPackage.setOnItemSelectedListener(new FlowTagLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                selectProductPackage = listGoodsPackageData.get(position);
                listPackageStandardData.clear();
                listPackageStandardData.addAll(selectProductPackage.getPackageColorJsonList());
                if(mGoodsGeneralModel.getPlatformActionID() != 0){
                    for (int i=0; i<listPackageStandardData.size();i++){
                        listPackageStandardData.get(i).setStoneCount(mGoodsGeneralModel.getStoneCount());
                    }
                }

                flowTagLayout_goodsPackageStandard.setCurrentSelectView(-1);
                selectGoodsPackageStandard = null;
                for(int i=0; i<listPackageStandardData.size(); i++){
                    if(listPackageStandardData.get(i).getStoneCount() > 0 ){
                        flowTagLayout_goodsPackageStandard.setCurrentSelectView(i);
                        selectGoodsPackageStandard = listPackageStandardData.get(i);
                        break;
                    }
                }
                adapterGoodsPackageStandard.notifyDataSetChanged();
                refreshPackageView();
            }

            @Override
            public void onNothingSelected(View view, int position, long id) {
                flowTagLayout_goodsPackage.setCurrentSelectView(-1);
                flowTagLayout_goodsPackageStandard.setCurrentSelectView(-1);
                adapterGoodsPackageStandard.notifyDataSetChanged();
                selectProductPackage = null;
                selectGoodsPackageStandard = null;
                refreshPackageView();
            }
        });

        flowTagLayout_goodsPackage.setAdapter(adapterGoodsPackage);
        listPackageStandardData = new ArrayList<GoodsPackageStandardModel>();
        adapterGoodsPackageStandard = new GoodPackageStandardAdapter(mContext,listPackageStandardData);
        flowTagLayout_goodsPackageStandard.setOnItemSelectedListener(new FlowTagLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.tv_tag);
                if(textView != null && textView.isEnabled()){
                    selectGoodsPackageStandard = listPackageStandardData.get(position);
                }else{
                    selectGoodsPackageStandard = null;
                }
                refreshPackageView();
            }

            @Override
            public void onNothingSelected(View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.tv_tag);
                if(textView != null && textView.isEnabled()){
                    selectGoodsPackageStandard = null;
                }else{
                    selectGoodsPackageStandard = null;
                }
                refreshPackageView();
            }
        });

        flowTagLayout_goodsPackageStandard.setAdapter(adapterGoodsPackageStandard);
    }

    @Override
    public void initData() {
        mGoodsGeneralModel = (GoodsGeneralModel) getIntent().getSerializableExtra(InformationCodeUtil.IntentGoodsActivityCurrentGoodsModel);
        if(mGoodsGeneralModel.getDefaultGoodsPackage() == null){
            ImagerLoaderUtil.getInstance(mContext).displayMyImage(mGoodsGeneralModel.getImages().get(0), iv_goodsImages);
            tv_goodsName.setText(mGoodsGeneralModel.getGoodsName());
            getData();
        }else{
            ImagerLoaderUtil.getInstance(mContext).displayMyImage(mGoodsGeneralModel.getImgName(), iv_goodsImages);
            tv_goodsName.setText(mGoodsGeneralModel.getGoodsName());
            for(int i=0; i<mGoodsGeneralModel.getZgcolorpackages().size(); i++){
                GoodsPackageModel mGoodsPackageModel = mGoodsGeneralModel.getZgcolorpackages().get(i);
                mGoodsPackageModel.setPackageColorJsonList(mGoodsPackageModel.getZgcolors());
            }
            refreshPackageData(mGoodsGeneralModel.getZgcolorpackages());
            refreshPackageView();
        }
    }


    private void getData(){
        String methodName = InformationCodeUtil.methodNameGetGoodsPackageList;
        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        requestSoapObject.addProperty("goodsID", mGoodsGeneralModel.getDjLsh());
        requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
        ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask
                (mContext, this, requestSoapObject , methodName);
        connectGoodsServiceAsyncTask.execute();
    }


    //响应套餐颜色点击事件之后 刷新界面显示数据
    private void refreshPackageData(List<GoodsPackageModel> list) {

        listGoodsPackageData.clear();
        listGoodsPackageData.addAll(list);
        adapterGoodsPackage.notifyDataSetChanged();

        listPackageStandardData.clear();
        selectProductPackage = listGoodsPackageData.get(0);
        listPackageStandardData.addAll(selectProductPackage.getPackageColorJsonList());

        flowTagLayout_goodsPackageStandard.setCurrentSelectView(-1);
        selectGoodsPackageStandard = null;
        for(int i=0; i<listPackageStandardData.size(); i++){
            if(listPackageStandardData.get(i).getStoneCount() > 0 ){
                flowTagLayout_goodsPackageStandard.setCurrentSelectView(i);
                selectGoodsPackageStandard = listPackageStandardData.get(i);
                break;
            }
        }
        adapterGoodsPackageStandard.notifyDataSetChanged();
    }
    //响应套餐颜色点击事件之后 刷新界面显示数据
    private void refreshPackageView() {
        double productPackagePrice = 0;
        long productPackageStockNumber = 0;
        String ProductPackageDesc = "套餐:未选套餐";
        String ProductPackageStandardDesc = "未选规格";
        if(selectProductPackage != null){
            ProductPackageDesc = "套餐:"+selectProductPackage.getPackageName();
        }
        if(selectGoodsPackageStandard != null){
            productPackagePrice = selectGoodsPackageStandard.getPrice();
            ProductPackageStandardDesc = selectGoodsPackageStandard.getColorName();
            productPackageStockNumber = selectGoodsPackageStandard.getStoneCount();
        }
        DecimalFormat dcm = new DecimalFormat("0.00");
        tv_goodsPrice.setText(dcm.format(productPackagePrice));
        tv_goodsPackageDesc.setText(ProductPackageDesc+"/"+ProductPackageStandardDesc);
        tv_goodsStock.setText("库存:"+productPackageStockNumber);
    }

    @OnClick({R.id.iv_closeActivity, R.id.btn_query})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_closeActivity:
                previewToDestroy();
                break;
            case R.id.btn_query:
                if(selectProductPackage == null || selectGoodsPackageStandard == null){
                    ToastUtil.show(mContext, "请先选择套餐");
                }else{
                    selectProductPackage.setDefaultPackageColor(selectGoodsPackageStandard);
                    mGoodsGeneralModel.setDefaultGoodsPackage(selectProductPackage);
                    Intent intent = new Intent();
                    intent.putExtra(InformationCodeUtil.IntentGoodsActivityCurrentGoodsModel,mGoodsGeneralModel);
                    setResult(RESULT_OK,intent);
                    previewToDestroy();
                }
                break;

            default:
                break;
        }
    }


    /**
     * 即将关闭当前页面时启动关闭动画
     **/
    void previewToDestroy() {
        finish();
        overridePendingTransition(R.anim.not_change, R.anim.out_to_bottom);
    }


    @Override
    public void onBackPressed() {
        previewToDestroy();
    }


    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, Integer state, boolean whetherRefresh) {
        LogUtil.LogShitou("returnSoapObject", returnString);
        if(InformationCodeUtil.methodNameGetGoodsPackageList == methodName){
            JSONResultBaseModel<GoodsPackageModel> result = null;
            try {
                Gson gson = new Gson();
                result = gson.fromJson(returnString, new TypeToken<JSONResultBaseModel<GoodsPackageModel>>(){}.getType());

                if(!StringUtil.isEmpty(result.getTitle())){
                    tv_goodsPackage.setText(result.getTitle());
                }
                if(!StringUtil.isEmpty(result.getSubTitle())){
                    tv_goodsPackageStandard.setText(result.getSubTitle());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(result != null || result.getList() != null){
                refreshPackageData(result.getList());
            }
            refreshPackageView();
            return;
        }
    }


    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Integer state,
                                     boolean whetherRefresh) {

        if(InformationCodeUtil.methodNameGetGoodsPackageList == methodName){
            ToastUtil.show(mContext,"网络异常，获取商品套餐数据失败");
        }

    }
    @Override
    public void connectServiceCancelled(String returnString,
                                        String methodName, Integer state, boolean whetherRefresh) {
    }


    public class GoodPackageTypeAdapter extends MyBaseAdapter<GoodsPackageModel>{


        public GoodPackageTypeAdapter(Context mContext, List<GoodsPackageModel> listData) {
            super(mContext, listData);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.item_tag_button_background, null);

            TextView textView = (TextView) view.findViewById(R.id.tv_tag);
            GoodsPackageModel t = listData.get(position);

            textView.setText(t.getPackageName());
            return view;
        }
    }


    public class GoodPackageStandardAdapter extends MyBaseAdapter<GoodsPackageStandardModel>{

        public GoodPackageStandardAdapter(Context mContext, List<GoodsPackageStandardModel> listData) {
            super(mContext, listData);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if(position < 0 || position >= listData.size()){
                return null;
            }else{
                View view = View.inflate(mContext ,R.layout.item_tag_button_background, null);
                TextView textView = (TextView) view.findViewById(R.id.tv_tag);
                GoodsPackageStandardModel model = listData.get(position);
                if(model.getStoneCount() == 0){
                    textView.setEnabled(false);
                }else{
                    textView.setEnabled(true);
                }
                textView.setText(model.getColorName());

                return view;
            }
        }

    }

}
