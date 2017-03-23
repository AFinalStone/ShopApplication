package com.shi.xianglixiangqin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.model.AddressModel;
import com.shi.xianglixiangqin.view.SelectAddressPopWindow;
import com.shi.xianglixiangqin.view.SelectBankPopWindow;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckStandBankBindingActivity extends MyBaseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView ivTitleLeft;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    SelectAddressPopWindow pop;
    SelectBankPopWindow popSelectBank;
    View rootView;

    /**
     * 选择银行
     **/
    @BindView(R.id.tv_selectBankCard)
    TextView tvSelectBankCard;

    /**
     * 选择地区
     **/
    @BindView(R.id.tv_selectProvince)
    TextView tvSelectProvince;
    /**
     * 当前待编辑的收货地址对象
     **/
    private AddressModel currentAddressModel = new AddressModel();
    /**
     * 是否成功获取省、市、区
     **/
    private boolean selectSuccessFlag;

    @Override
    public void initView() {
        rootView = View.inflate(mContext, R.layout.activity_check_stand_bank_binding, null);
        setContentView(rootView);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.checkStandBankBinding_title);
        ivTitleLeft.setVisibility(View.VISIBLE);
    }

    @Override
    public void initData() {
        selectSuccessFlag = false;
    }

    @OnClick({R.id.iv_titleLeft, R.id.linearLayout_selectBankCard, R.id.linearLayout_selectProvince, R.id.btn_queryBinding})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.linearLayout_selectBankCard:
                showSelectBankView();
                break;
            case R.id.linearLayout_selectProvince:
                showSelectAddressView();
                break;
            case R.id.btn_queryBinding:
                startActivity(new Intent(mContext, CheckStandApplyMDActivity.class));
                break;

        }
    }

    private void showSelectAddressView() {

        pop = new SelectAddressPopWindow(this, new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // 隐藏弹出窗口
                pop.dismiss();
                switch (v.getId()) {
                    case R.id.tv_cancel:
                        tvSelectProvince.setText(R.string.CheckStandBankBindingActivity_tv_selectProvince);
                        selectSuccessFlag = false;
                        break;
                    case R.id.tv_ok:
                        String selectAddress = pop.selectCurrentFirstPosition.getName() + " "
                                + pop.selectCurrentSecondPosition.getName() + " " + pop.selectCurrentThirdPosition.getName();
                        tvSelectProvince.setText(selectAddress);
                        currentAddressModel.setProvinceCode(pop.selectCurrentFirstPosition.getCode());
                        currentAddressModel.setCityCode(pop.selectCurrentSecondPosition.getCode());
                        currentAddressModel.setAreaCode(pop.selectCurrentThirdPosition.getCode());
                        selectSuccessFlag = true;
                        break;
                    default:
                        break;
                }
            }
        });
        pop.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private void showSelectBankView() {

        popSelectBank = new SelectBankPopWindow(mContext, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popSelectBank.dismiss();
                switch (v.getId()) {
                    case R.id.tv_cancel:
                        tvSelectBankCard.setText(R.string.CheckStandBankBindingActivity_tv_selectBankCard);
                        selectSuccessFlag = false;
                        break;
                    case R.id.tv_ok:
                        tvSelectBankCard.setText(popSelectBank.currentSelectBank);
                        selectSuccessFlag = true;
                        break;
                    default:
                        break;
                }
            }
        });
        popSelectBank.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

}
