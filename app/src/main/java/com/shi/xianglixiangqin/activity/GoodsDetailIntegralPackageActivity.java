package com.shi.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.model.CreateOrderMethodSelectModel;
import com.shi.xianglixiangqin.model.GoodsIntegralModel;
import com.shi.xianglixiangqin.model.GoodsIntegralPackageColorBuyTypeModel;
import com.shi.xianglixiangqin.model.GoodsIntegralPackageColorModel;
import com.shi.xianglixiangqin.model.GoodsIntegralPackageModel;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.ToastUtil;
import com.shi.xianglixiangqin.view.FlowTagLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/****
 * 套餐选择界面 加入购物车和立即购买
 *
 * @author SHI
 *         2016-3-15 16:03:03
 */
public class GoodsDetailIntegralPackageActivity extends MyBaseTranslucentActivity {

    /**
     * 关闭当前界面控件
     **/
    @BindView(R.id.iv_closeActivity)
    ImageView iv_closeActivity;
    /**
     * 当前商品图标
     **/
    @BindView(R.id.iv_goodsImages)
    ImageView iv_goodsImages;
    /**
     * 当前商品名称
     **/
    @BindView(R.id.tv_goodsName)
    TextView tv_goodsName;
    /**
     * 当前商品价格
     **/
    @BindView(R.id.tv_goodsPrice)
    TextView tv_goodsPrice;
    /**
     * 当前商品选中套餐描述
     **/
    @BindView(R.id.tv_goodsPackageDesc)
    TextView tv_goodsPackageDesc;
    /**
     * 当前商品库存
     **/
    @BindView(R.id.tv_goodsStock)
    TextView tv_goodsStock;

    /**
     * 当前商品购买数量
     **/
    @BindView(R.id.et_goodsNumber)
    EditText et_goodsNumber;

    /**
     * 当前商品套餐
     **/
    @BindView(R.id.flowTagLayout_goodsPackage)
    FlowTagLayout flowTagLayout_goodsPackage;
    List<GoodsIntegralPackageModel> listGoodsPackageData = new ArrayList<>();
    GoodPackageAdapter adapterGoodsPackage;

    /**
     * 套餐规格描述
     **/
    @BindView(R.id.tv_goodsPackageStandard)
    TextView tv_goodsPackageStandard;

    /**
     * 当前商品套餐颜色
     **/
    @BindView(R.id.flowTagLayout_goodsPackageStandard)
    FlowTagLayout flowTagLayout_goodsPackageStandard;
    List<GoodsIntegralPackageColorModel> listPackageColorData = new ArrayList<>();
    GoodPackageColorAdapter adapterGoodsPackageColor;

    /**
     * 当前商品套餐颜色购买方式
     **/
    @BindView(R.id.flowTagLayout_buyType)
    FlowTagLayout flowTagLayout_buyType;
    List<GoodsIntegralPackageColorBuyTypeModel> listPackageColorBuyTypeData = new ArrayList<>();
    GoodPackageColorBuyTypeAdapter adapterGoodPackageColorBuyType;

    /**
     * 当前商品详细信息
     **/
    GoodsIntegralModel mGoodsIntegralModel;
    /**
     * 当前被选中套餐数据
     **/
    GoodsIntegralPackageModel selectPackageModel;
    /**
     * 当前被选中套餐颜色数据
     */
    GoodsIntegralPackageColorModel selectPackageColorModel;
    /**
     * 当前被选中的购买方式
     */
    GoodsIntegralPackageColorBuyTypeModel selectPackageColorBuyTypModel;
    /**
     * 当前购买的数量
     **/
    long currentBuyNum = 1;

    @Override
    public void initView() {
        setContentView(R.layout.activity_goods_detail_integral_package);
        ButterKnife.bind(mContext);

        //商品套餐数据
        adapterGoodsPackage = new GoodPackageAdapter(mContext, listGoodsPackageData);
        flowTagLayout_goodsPackage.setOnItemSelectedListener(new FlowTagLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                selectPackageModel = listGoodsPackageData.get(position);
                //主要是为了选择不同的套餐时候，套餐颜色，购买方式产生联动
                flowTagLayout_goodsPackageStandard.setCurrentSelectView(-1);
                flowTagLayout_buyType.setCurrentSelectView(-1);
                selectPackageColorModel = null;
                selectPackageColorBuyTypModel = null;
                listPackageColorData.clear();
                listPackageColorBuyTypeData.clear();
                listPackageColorData.addAll(selectPackageModel.getGoodstcys());
                for (int i = 0; i < listPackageColorData.size(); i++) {
                    if (listPackageColorData.get(i).getTotalnum() > 0) {
                        flowTagLayout_goodsPackageStandard.setCurrentSelectView(i);
                        selectPackageColorModel = listPackageColorData.get(i);

                        listPackageColorBuyTypeData.addAll(selectPackageColorModel.getJfbuytype());
                        flowTagLayout_buyType.setCurrentSelectView(0);
                        selectPackageColorBuyTypModel = listPackageColorBuyTypeData.get(0);
                        break;
                    }
                }
                adapterGoodsPackageColor.notifyDataSetChanged();
                adapterGoodPackageColorBuyType.notifyDataSetChanged();
                refreshView();
            }

            @Override
            public void onNothingSelected(View view, int position, long id) {
                flowTagLayout_goodsPackage.setCurrentSelectView(-1);
                flowTagLayout_goodsPackageStandard.setCurrentSelectView(-1);
                flowTagLayout_buyType.setCurrentSelectView(-1);
                selectPackageModel = null;
                selectPackageColorModel = null;
                selectPackageColorBuyTypModel = null;
                adapterGoodsPackageColor.notifyDataSetChanged();
                adapterGoodPackageColorBuyType.notifyDataSetChanged();
                refreshView();
            }
        });
        flowTagLayout_goodsPackage.setAdapter(adapterGoodsPackage);

        //商品颜色数据
        adapterGoodsPackageColor = new GoodPackageColorAdapter(mContext, listPackageColorData);
        flowTagLayout_goodsPackageStandard.setOnItemSelectedListener(new FlowTagLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(R.id.tv_tag);
                if (textView != null && textView.isEnabled()) {
                    selectPackageColorModel = listPackageColorData.get(position);

                    listPackageColorBuyTypeData.clear();
                    listPackageColorBuyTypeData.addAll(selectPackageColorModel.getJfbuytype());
                    flowTagLayout_buyType.setCurrentSelectView(0);
                    selectPackageColorBuyTypModel = listPackageColorBuyTypeData.get(0);
                    adapterGoodPackageColorBuyType.notifyDataSetChanged();
                } else {
                    selectPackageColorModel = null;
                }
                refreshView();
            }

            @Override
            public void onNothingSelected(View view, int position, long id) {
                selectPackageColorModel = null;
                refreshView();
            }
        });

        flowTagLayout_goodsPackageStandard.setAdapter(adapterGoodsPackageColor);

        //商品购买方式
        adapterGoodPackageColorBuyType = new GoodPackageColorBuyTypeAdapter(mContext, listPackageColorBuyTypeData);
        flowTagLayout_buyType.setOnItemSelectedListener(new FlowTagLayout.OnItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position, long id) {
                selectPackageColorBuyTypModel = listPackageColorBuyTypeData.get(position);
                refreshView();
            }

            @Override
            public void onNothingSelected(View view, int position, long id) {
                selectPackageColorBuyTypModel = null;
                refreshView();
            }
        });
        flowTagLayout_buyType.setAdapter(adapterGoodPackageColorBuyType);
    }

    @Override
    public void initData() {
        mGoodsIntegralModel = (GoodsIntegralModel) getIntent().getSerializableExtra(InformationCodeUtil.IntentGoodsActivityCurrentGoodsModel);
        ImagerLoaderUtil.getInstance(mContext).displayMyImage(mGoodsIntegralModel.getImgs().get(0), iv_goodsImages);
        tv_goodsName.setText(mGoodsIntegralModel.getGoodsname());
        initPackageData(mGoodsIntegralModel.getGoodstc());
        refreshView();
    }


    //初始化界面数据
    private void initPackageData(List<GoodsIntegralPackageModel> list) {

        //刷新套餐列表
        listGoodsPackageData.clear();
        listGoodsPackageData.addAll(list);
        selectPackageModel = listGoodsPackageData.get(0);
        adapterGoodsPackage.notifyDataSetChanged();

        //刷新颜色列表和购买方式
        listPackageColorData.clear();
        listPackageColorBuyTypeData.clear();
        listPackageColorData.addAll(selectPackageModel.getGoodstcys());
        flowTagLayout_goodsPackageStandard.setCurrentSelectView(-1);
        selectPackageColorModel = null;
        for (int i = 0; i < listPackageColorData.size(); i++) {
            if (listPackageColorData.get(i).getTotalnum() > 0) {
                flowTagLayout_goodsPackageStandard.setCurrentSelectView(i);
                selectPackageColorModel = listPackageColorData.get(i);
                listPackageColorBuyTypeData.addAll(selectPackageColorModel.getJfbuytype());
                selectPackageColorBuyTypModel = listPackageColorBuyTypeData.get(0);
                break;
            }
        }
        adapterGoodsPackageColor.notifyDataSetChanged();
        adapterGoodPackageColorBuyType.notifyDataSetChanged();
    }

    //响应套餐颜色点击事件之后 刷新界面显示数据
    private void refreshView() {
        String productPackagePrice = "兑换条件:";
        long productPackageStockNumber = 0;
        String ProductPackageDesc = "套餐:未选套餐";
        String ProductPackageStandardDesc = "未选规格";
        if (selectPackageModel != null) {
            ProductPackageDesc = "套餐:" + selectPackageModel.getTcname();
        }
        if (selectPackageColorModel != null) {
            ProductPackageStandardDesc = selectPackageColorModel.getYsname();
            productPackageStockNumber = selectPackageColorModel.getTotalnum();
        }
        if(selectPackageColorBuyTypModel != null){
            if(selectPackageColorBuyTypModel.getBuytype() == 1){
                productPackagePrice += selectPackageColorBuyTypModel.getUsejf()+"积分";
            }else{
                productPackagePrice += StringUtil.doubleToString( selectPackageColorBuyTypModel.getUseje(), "0.00")+"元+"
                        + selectPackageColorBuyTypModel.getUsejf()+"积分";
            }
        }
        tv_goodsPrice.setText(productPackagePrice);
        tv_goodsPackageDesc.setText(ProductPackageDesc + "/" + ProductPackageStandardDesc);
        tv_goodsStock.setText("库存:" + productPackageStockNumber);
    }

    @OnClick({R.id.iv_closeActivity, R.id.iv_goodsNumberSub, R.id.iv_goodsNumberAdd,R.id.btn_query})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_closeActivity:
                previewToDestroy();
                break;
            case R.id.iv_goodsNumberSub:
                changeCurrentBuyNum(-1);
                break;
            case R.id.iv_goodsNumberAdd:
                changeCurrentBuyNum(1);
                break;
            case R.id.btn_query:
                toConfirmActivity();
                break;

            default:
                break;
        }
    }

    private void changeCurrentBuyNum(int addNumber) {
        try {
            currentBuyNum = Integer.parseInt(et_goodsNumber.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        currentBuyNum += addNumber;
        if(currentBuyNum <= 0){
            currentBuyNum = 1;
        }
//        if(currentBuyNum > selectPackageColorModel.getXzbuynum()){
//            ToastUtil.show(mContext, "操作失败,购买数量超过限购数量");
//            currentBuyNum = selectPackageColorModel.getXzbuynum();
//        }
        et_goodsNumber.setText(""+currentBuyNum);
    }

    private void toConfirmActivity() {
        if (selectPackageModel == null || selectPackageColorModel == null) {
            ToastUtil.show(mContext, "请选择套餐");
            return;
        }
        if (selectPackageColorBuyTypModel == null) {
            ToastUtil.show(mContext, "请选择购买方式");
            return;
        }
        try {
            currentBuyNum = Integer.parseInt(et_goodsNumber.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if(currentBuyNum > selectPackageColorModel.getTotalnum()){
            ToastUtil.show(mContext, "操作失败,购买数量超过商品库存");
            return;
        }
        if(currentBuyNum > selectPackageColorModel.getXzbuynum()){
            ToastUtil.show(mContext, "操作失败,购买数量超过限购数量");
            return;
        }
        //提交积分商品进入支付方式选择界面
        int ParamJF_ShopID = mGoodsIntegralModel.getShopid();
        int ParamJF_GoodsID = mGoodsIntegralModel.getGid();
        String ParamJF_TCName = selectPackageModel.getTcname();
        String ParamJF_YSName = selectPackageColorModel.getYsname();
        int ParamJF_BuyType = selectPackageColorBuyTypModel.getBuytype();

        CreateOrderMethodSelectModel param = new CreateOrderMethodSelectModel(
                currentBuyNum, ParamJF_ShopID, ParamJF_GoodsID
                , ParamJF_TCName, ParamJF_YSName, ParamJF_BuyType);
        Intent mIntent = new Intent(mContext, ConfirmOrderIntegralActivity.class);
        mIntent.putExtra(InformationCodeUtil.IntentPayMethodSelectActivityParamObject, param);
        mIntent.putExtra(InformationCodeUtil.IntentConfirmOrderIntegralActivityGoodsImageUrl, mGoodsIntegralModel.getImgs().get(0));
        mIntent.putExtra(InformationCodeUtil.IntentConfirmOrderIntegralActivityGoodsName, mGoodsIntegralModel.getGoodsname());
        mIntent.putExtra(InformationCodeUtil.IntentConfirmOrderIntegralActivityGoodsPrices, selectPackageColorBuyTypModel.getUseje());
        mIntent.putExtra(InformationCodeUtil.IntentConfirmOrderIntegralActivityGoodsJf, selectPackageColorBuyTypModel.getUsejf());
        startActivity(mIntent);
        finish();
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


    public class GoodPackageAdapter extends MyBaseAdapter<GoodsIntegralPackageModel> {


        public GoodPackageAdapter(Context mContext, List<GoodsIntegralPackageModel> listData) {
            super(mContext, listData);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View view = LayoutInflater.from(mContext).inflate(R.layout.item_tag_button_background, null);

            TextView textView = (TextView) view.findViewById(R.id.tv_tag);
            GoodsIntegralPackageModel packageModel = listData.get(position);

            textView.setText(packageModel.getTcname());
            return view;
        }
    }


    public class GoodPackageColorAdapter extends MyBaseAdapter<GoodsIntegralPackageColorModel> {

        public GoodPackageColorAdapter(Context mContext, List<GoodsIntegralPackageColorModel> listData) {
            super(mContext, listData);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (position < 0 || position >= listData.size()) {
                return null;
            } else {
                View view = View.inflate(mContext, R.layout.item_tag_button_background, null);
                TextView textView = (TextView) view.findViewById(R.id.tv_tag);
                GoodsIntegralPackageColorModel packageColorModel = listData.get(position);
                if (packageColorModel.getTotalnum() == 0) {
                    textView.setEnabled(false);
                } else {
                    textView.setEnabled(true);
                }
                textView.setText(packageColorModel.getYsname());

                return view;
            }
        }

    }

    public class GoodPackageColorBuyTypeAdapter extends MyBaseAdapter<GoodsIntegralPackageColorBuyTypeModel> {

        public GoodPackageColorBuyTypeAdapter(Context mContext, List<GoodsIntegralPackageColorBuyTypeModel> listData) {
            super(mContext, listData);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (position < 0 || position >= listData.size()) {
                return null;
            }
            View view = View.inflate(mContext, R.layout.item_tag_button_background, null);
            TextView textView = (TextView) view.findViewById(R.id.tv_tag);
            GoodsIntegralPackageColorBuyTypeModel packageColorJFModel = listData.get(position);

            if (packageColorJFModel.getBuytype() == 1) {
                textView.setText(packageColorJFModel.getUsejf() + "积分");
            } else {
                textView.setText( StringUtil.doubleToString(packageColorJFModel.getUseje()) + "元+" + packageColorJFModel.getUsejf() + "积分" );
            }
            return view;
        }

    }

}
