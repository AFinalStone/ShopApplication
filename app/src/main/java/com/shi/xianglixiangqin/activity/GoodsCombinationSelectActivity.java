package com.shi.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.model.GoodsGeneralModel;
import com.shi.xianglixiangqin.model.GoodsPackageModel;
import com.shi.xianglixiangqin.model.ShoppingCartChildGoodsModel;
import com.shi.xianglixiangqin.model.ShoppingCartParentGoodsModel;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 组合商品主商品和子商品选择界面
 * @author SHI
 * @time 2016/11/29 11:36
 */
public class GoodsCombinationSelectActivity extends MyBaseActivity implements OnConnectServerStateListener<Integer> {

    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;

    @BindView(R.id.tv_title)
    TextView tv_title;

    @BindView(R.id.listView)
    ListView listView;

    @BindView(R.id.tv_totalNumber)
    TextView tv_totalNumber;

    /**合计**/
    @BindView(R.id.tv_totalOrdersPrices)
    TextView tv_totalOrdersPrices;
    /**优惠**/
    @BindView(R.id.tv_totalPrivilegePrice)
    TextView tv_totalPrivilegePrice;
    /**提交**/
    @BindView(R.id.btn_submit)
    Button btn_submit;

    /**
     * 组合主商品
     **/
    ImageView iv_goodsImage;
    /**
     * 组合主商品名称
     **/
    TextView tv_goodsName;
    /**
     * 组合商品价格
     **/
//    TextView tv_goodsPrice;
    /**
     * 选择套餐界面
     **/
    LinearLayout linearLayout_selectPackage;
    /**
     * 具体套餐配置描述
     **/
    TextView tv_selectPackageDesc;
    /**
     * 组合商品子条目
     **/
    List<GoodsGeneralModel> listData_ItemGoods = new ArrayList<>();
    MyListViewAdapter myListViewAdapter;
    /**
     * 请求码主商品
     **/
    private int RequestCode_MainGoods = 100;

    /**
     * 当前主商品ID
     **/
    int goodsID;
    int zhGoodID;//当前组合商品ID
    GoodsGeneralModel mGoodsGeneralModel;//当前主商品详细信息
    GoodsGeneralModel currentSelectItemGoodsModel;//当前选中的子商品详细信息
    private int currentTotalNum = 1;

    @Override
    public void initView() {
        setContentView(R.layout.activity_goods_combination_select);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.GoodsCombinationSelect_title);

        // 初始化ListView的Header布局
        View listViewHeader = View.inflate(mContext, R.layout.layout_combination_listview_header, null);
        iv_goodsImage = (ImageView) listViewHeader.findViewById(R.id.iv_goodsImage);
        tv_goodsName = (TextView) listViewHeader.findViewById(R.id.tv_goodsName);
//        tv_goodsPrice = (TextView) listViewHeader.findViewById(R.id.tv_goodsPrice);
        tv_selectPackageDesc = (TextView) listViewHeader.findViewById(R.id.tv_selectPackageDesc);
        linearLayout_selectPackage = (LinearLayout) listViewHeader.findViewById(R.id.linearLayout_selectPackage);
        linearLayout_selectPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoodsGeneralModel.setDefaultGoodsPackage(null);
//                tv_goodsPrice.setText("");
                tv_selectPackageDesc.setText(R.string.goodsCombinationSelect_selectGoodsProperty);
                toGoodsCombinationSelectActivity(RequestCode_MainGoods,mGoodsGeneralModel);
            }
        });
        listView.addHeaderView(listViewHeader);
        myListViewAdapter = new MyListViewAdapter(mContext, listData_ItemGoods);
        listView.setAdapter(myListViewAdapter);
    }

    @Override
    public void initData() {
        goodsID = getIntent().getIntExtra(InformationCodeUtil.IntentGoodsID, -1);
        mGoodsGeneralModel = (GoodsGeneralModel) getIntent().getSerializableExtra(InformationCodeUtil.IntentGoodsActivityCurrentGoodsModel);
        ImagerLoaderUtil.getInstance(mContext).displayMyImage(mGoodsGeneralModel.getImages().get(0), iv_goodsImage);
        tv_goodsName.setText(mGoodsGeneralModel.getGoodsName());
//        tv_goodsPrice.setText("");
        getData();
    }

    private void getData() {
        String methodName = InformationCodeUtil.methodNameGetZHTC;
        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        requestSoapObject.addProperty("shopid", MyApplication.getmCustomModel(mContext).getCurrentBrowsingShopID());
        requestSoapObject.addProperty("gid", goodsID);
        ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask
                (mContext, this, requestSoapObject, methodName);
        connectGoodsServiceAsyncTask.execute();
    }


    @OnClick({R.id.iv_titleLeft, R.id.tv_sub, R.id.tv_add ,R.id.btn_submit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_titleLeft:
                previewToDestroy();
                break;
            case R.id.tv_sub:
                changeZHGoodsNum(-1);
                break;
            case R.id.tv_add:
                changeZHGoodsNum(1);
                break;
            case R.id.btn_submit:
                toConfirmOrderActivity();
                break;
        }
    }

    private void changeZHGoodsNum(int addNumber) {
        currentTotalNum += addNumber;
        if(currentTotalNum == 0){
            currentTotalNum = 1;
        }
        tv_totalNumber.setText(""+currentTotalNum);
        getCurrentTotalPrices();
    }

    /**
     * 进入套餐选择界面
     **/
    private void toGoodsCombinationSelectActivity(int requestCode, GoodsGeneralModel parameterGeneralModel) {
        Intent mIntent = new Intent(mContext, GoodsCombinationPackageActivity.class);
        mIntent.putExtra(InformationCodeUtil.IntentGoodsActivityCurrentGoodsModel, parameterGeneralModel);
        startActivityForResult(mIntent, requestCode);
        overridePendingTransition(R.anim.in_from_bottom, R.anim.not_change);
    }

    /**
     * 进入提交订单界面
     **/
    private void toConfirmOrderActivity() {

        if(mGoodsGeneralModel.getDefaultGoodsPackage() == null){
            ToastUtil.show(mContext,"请选择主商品属性");
            return;
        }
        if(currentSelectItemGoodsModel == null){
            ToastUtil.show(mContext,"请选择一个子商品");
            return;
        }
        if(currentTotalNum > mGoodsGeneralModel.getDefaultGoodsPackage().getDefaultPackageColor().getStoneCount()){
            ToastUtil.show(mContext,"购买数量超出主商品存库数量");
            return;
        }
        if(currentTotalNum > currentSelectItemGoodsModel.getDefaultGoodsPackage().getDefaultPackageColor().getStoneCount()){
            ToastUtil.show(mContext,"购买数量超出优惠品存库数量");
            return;
        }

        ShoppingCartChildGoodsModel  childGoodsModelMain = new ShoppingCartChildGoodsModel(
                0, mGoodsGeneralModel.getDjLsh()
                , mGoodsGeneralModel.getGoodsName(), mGoodsGeneralModel.getImages().get(0)
                , mGoodsGeneralModel.getDefaultGoodsPackage().getDjLsh(), mGoodsGeneralModel.getDefaultGoodsPackage().getPackageName()
                , mGoodsGeneralModel.getDefaultGoodsPackage().getDefaultPackageColor().getDjLsh()
                , mGoodsGeneralModel.getDefaultGoodsPackage().getDefaultPackageColor().getColorName(),currentTotalNum
                , mGoodsGeneralModel.getDefaultGoodsPackage().getDefaultPackageColor().getPrice()
                , mGoodsGeneralModel.getDefaultGoodsPackage().getDefaultPackageColor().getTaxPrice()
                , mGoodsGeneralModel.getDefaultGoodsPackage().getDefaultPackageColor().getFlyCoin()
                , mGoodsGeneralModel.getDefaultGoodsPackage().getDefaultPackageColor().getTaxFlyCoin()
                , mGoodsGeneralModel.getDefaultGoodsPackage().getDefaultPackageColor().getYhje()
        );
        ShoppingCartChildGoodsModel  childGoodsModelItem = new ShoppingCartChildGoodsModel(
                0, currentSelectItemGoodsModel.getDjLsh()
                ,  currentSelectItemGoodsModel.getGoodsName(), currentSelectItemGoodsModel.getImgName()
                , currentSelectItemGoodsModel.getDefaultGoodsPackage().getDjLsh()
                , currentSelectItemGoodsModel.getDefaultGoodsPackage().getPackageName()
                , currentSelectItemGoodsModel.getDefaultGoodsPackage().getDefaultPackageColor().getDjLsh()
                , currentSelectItemGoodsModel.getDefaultGoodsPackage().getDefaultPackageColor().getColorName(),currentTotalNum
                , currentSelectItemGoodsModel.getDefaultGoodsPackage().getDefaultPackageColor().getPrice()
                , currentSelectItemGoodsModel.getDefaultGoodsPackage().getDefaultPackageColor().getTaxPrice()
                , currentSelectItemGoodsModel.getDefaultGoodsPackage().getDefaultPackageColor().getFlyCoin()
                , currentSelectItemGoodsModel.getDefaultGoodsPackage().getDefaultPackageColor().getTaxFlyCoin()
                , currentSelectItemGoodsModel.getDefaultGoodsPackage().getDefaultPackageColor().getYhje()
        );

        ShoppingCartParentGoodsModel parentGoodsModel = new ShoppingCartParentGoodsModel();
        parentGoodsModel.setShopID(mGoodsGeneralModel.getShopID());
        parentGoodsModel.setShopName(mGoodsGeneralModel.getShopName());
        parentGoodsModel.setDelayPayDays(0);
        parentGoodsModel.getShoppingCarts().add(childGoodsModelMain);
        parentGoodsModel.getShoppingCarts().add(childGoodsModelItem);
        parentGoodsModel.IfSelect = true;

        List<ShoppingCartParentGoodsModel> list = new ArrayList<ShoppingCartParentGoodsModel>();
        list.add(parentGoodsModel);

        Intent intent = new Intent(mContext, ConfirmOrderGeneralActivity.class);
        intent.putExtra(InformationCodeUtil.IntentConfirmOrderGeneralActivityGoodsList, (Serializable) list);
        intent.putExtra(InformationCodeUtil.IntentConfirmOrderGeneralActivityZHGoodsID, zhGoodID);
        startActivity(intent);
    }

    /**获取当前总价格**/
    private void getCurrentTotalPrices() {
        Double totalPrices = 0.0;
        Double totalPrivilegePrice = 0.0;
        if(mGoodsGeneralModel.getDefaultGoodsPackage() == null){
            tv_totalOrdersPrices.setText("合计：¥ 0.00");
            tv_totalPrivilegePrice.setText("优惠：¥ 0.00");
            return;
        }
        if(currentSelectItemGoodsModel == null){
            tv_totalOrdersPrices.setText("合计：¥ 0.00");
            tv_totalPrivilegePrice.setText("优惠：¥ 0.00");
            return;
        }
        totalPrices += mGoodsGeneralModel.getDefaultGoodsPackage().getDefaultPackageColor().getPrice()*currentTotalNum;
        totalPrices += currentSelectItemGoodsModel.getDefaultGoodsPackage().getDefaultPackageColor()
                .getPrice()*currentTotalNum;
        totalPrivilegePrice +=  currentSelectItemGoodsModel.getDefaultGoodsPackage().getDefaultPackageColor()
                .getYhje()*currentTotalNum;
        tv_totalOrdersPrices.setText("合计：¥ "+StringUtil.doubleToString(totalPrices,"0.00"));
        tv_totalPrivilegePrice.setText("优惠：¥ "+StringUtil.doubleToString(totalPrivilegePrice,"0.00"));
    }


    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, Integer state, boolean whetherRefresh) {
        LogUtil.LogShitou(returnString);
        try {
            JSONObject jsonObject = new JSONObject(returnString);
            zhGoodID = jsonObject.getInt("gid");
//            currentShopID = jsonObject.getInt("shopid");
            String strZHColors = jsonObject.getString("zgoods");
            Gson gson = new Gson();
            List<GoodsGeneralModel> listTemps = gson.fromJson(strZHColors
                    , new TypeToken<List<GoodsGeneralModel>>() {
                    }.getType());
            listData_ItemGoods.clear();
            listData_ItemGoods.addAll(listTemps);
            for (int i = 0; i < listData_ItemGoods.size(); i++) {
                GoodsGeneralModel goodsModel = listData_ItemGoods.get(i);
                GoodsPackageModel packageModel = goodsModel.getZgcolorpackages().get(0);
                packageModel.setDefaultPackageColor(packageModel.getZgcolors().get(0));
                goodsModel.setDefaultGoodsPackage(packageModel);
            }
            myListViewAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Integer state,
                                     boolean whetherRefresh) {

    }

    @Override
    public void connectServiceCancelled(String returnString, String methodName, Integer state, boolean whetherRefresh) {

    }

    /**
     * 即将关闭当前页面时启动关闭动画
     **/
    void previewToDestroy() {
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                //用户成功选择了套餐
                GoodsGeneralModel generalModel = (GoodsGeneralModel) data.getSerializableExtra(InformationCodeUtil.IntentGoodsActivityCurrentGoodsModel);
                if (requestCode == RequestCode_MainGoods) {
                    mGoodsGeneralModel.setDefaultGoodsPackage(generalModel.getDefaultGoodsPackage());
                    String strPrice = StringUtil.doubleToString(mGoodsGeneralModel.getDefaultGoodsPackage().getDefaultPackageColor().getPrice(),"0.00");
                    tv_selectPackageDesc.setText("价格：" +strPrice +"    套餐：" + mGoodsGeneralModel.getDefaultGoodsPackage().getPackageName() +
                            "/" + mGoodsGeneralModel.getDefaultGoodsPackage().getDefaultPackageColor().getColorName());
                } else {
                    listData_ItemGoods.get(requestCode).setDefaultGoodsPackage(generalModel.getDefaultGoodsPackage());
                    myListViewAdapter.notifyDataSetChanged();
                }
                break;
        }
        getCurrentTotalPrices();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 如果是返回键，则关闭当前页面
     **/
    @Override
    public void onBackPressed() {
        previewToDestroy();
    }


    private class MyListViewAdapter extends MyBaseAdapter<GoodsGeneralModel> {

        public MyListViewAdapter(Context mContext, List<GoodsGeneralModel> listData) {
            super(mContext, listData);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_adapter_combination_listview, null);
                viewHolder.iv_select = (ImageView) convertView.findViewById(R.id.iv_select);
                viewHolder.iv_goodsImage = (ImageView) convertView.findViewById(R.id.iv_goodsImage);
                viewHolder.tv_goodsName = (TextView) convertView.findViewById(R.id.tv_goodsName);
                viewHolder.linearLayout_selectPackage = (LinearLayout) convertView.findViewById(R.id.linearLayout_selectPackage);
                viewHolder.tv_selectPackagePrices = (TextView) convertView.findViewById(R.id.tv_selectPackagePrices);
                viewHolder.tv_selectPackageDesc = (TextView) convertView.findViewById(R.id.tv_selectPackageDesc);
                viewHolder.tv_goodsPrivilegePrice = (TextView) convertView.findViewById(R.id.tv_goodsPrivilegePrice);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            final GoodsGeneralModel model = listData.get(position);
            ImagerLoaderUtil.getInstance(mContext).displayMyImage(model.getImgName(), viewHolder.iv_goodsImage);
            viewHolder.iv_select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.isWhetherSelect()) {
                        viewHolder.iv_select.setImageResource(R.drawable.icon_select_off_02);
                        model.setWhetherSelect(false);
                        currentSelectItemGoodsModel = null;
                    } else {
                        for (int i = 0; i < listData.size(); i++) {
                            listData.get(i).setWhetherSelect(false);
                        }
                        model.setWhetherSelect(true);
                        viewHolder.iv_select.setImageResource(R.drawable.icon_select_on_02);
                        myListViewAdapter.notifyDataSetChanged();
                        currentSelectItemGoodsModel = model;
                    }
                    getCurrentTotalPrices();
                }
            });
            if (model.isWhetherSelect()) {
                viewHolder.iv_select.setImageResource(R.drawable.icon_select_on_02);
            } else {
                viewHolder.iv_select.setImageResource(R.drawable.icon_select_off_02);
            }
//            viewHolder.iv_goodsImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, GoodsDetailGeneralActivity.class);
//                    intent.putExtra(InformationCodeUtil.IntentGoodsID, model.getDjLsh());
//                    startActivity(intent);
//                }
//            });
            viewHolder.tv_goodsName.setText(model.getGoodsName());
            String strPrices = StringUtil.doubleToString(model.getDefaultGoodsPackage().getDefaultPackageColor().getPrice(),"0.00");
            String strPrivilegePrice = StringUtil.doubleToString(model.getDefaultGoodsPackage().getDefaultPackageColor().getYhje(), "0.00");
            viewHolder.tv_selectPackagePrices.setText("价格：" + strPrices);
            viewHolder.tv_selectPackageDesc.setText("套餐：" +
                    model.getDefaultGoodsPackage().getPackageName() + "/"
                    + model.getDefaultGoodsPackage().getDefaultPackageColor().getColorName());
            viewHolder.tv_goodsPrivilegePrice.setText("¥ " + strPrivilegePrice);
            viewHolder.linearLayout_selectPackage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toGoodsCombinationSelectActivity(position,model);
                }
            });
            return convertView;
        }

        private class ViewHolder {
            /**
             * 选择按钮
             */
            private ImageView iv_select;
            /**
             * 商品图片
             */
            private ImageView iv_goodsImage;
            /**
             * 商品名称
             */
            private TextView tv_goodsName;
            /**
             * 修改套餐
             **/
            private LinearLayout linearLayout_selectPackage;
            /**
             * 套餐价格
             **/
            private TextView tv_selectPackagePrices;
            /**
             * 套餐名称
             **/
            private TextView tv_selectPackageDesc;
            /**
             * 套餐优惠价格
             **/
            private TextView tv_goodsPrivilegePrice;

        }

    }


}




