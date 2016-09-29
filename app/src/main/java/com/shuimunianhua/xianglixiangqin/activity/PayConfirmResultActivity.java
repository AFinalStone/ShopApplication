package com.shuimunianhua.xianglixiangqin.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PayConfirmResultActivity extends MyBaseActivity {

    @Bind(R.id.iv_confirmOrderResultDesc)
    ImageView iv_confirmOrderResultDesc;

    @Bind(R.id.tv_confirmOrderResultDesc)
    TextView tv_confirmOrderResultDesc;

    @Bind(R.id.tv_continueToShopping)
    TextView tv_continueToShopping;

    @Bind(R.id.linearLayout_continueToShopping)
    LinearLayout linearLayout_continueToShopping;

    @Bind(R.id.iv_continueToMyOrder)
    ImageView iv_continueToMyOrder;

    @Bind(R.id.tv_continueToMyOrder)
    TextView tv_continueToMyOrder;

    @Bind(R.id.linearLayout_continueToMyOrder)
    LinearLayout linearLayout_continueToMyOrder;

    @Bind(R.id.iv_continueToConfirmOrder)
    ImageView iv_continueToConfirmOrder;

    @Bind(R.id.tv_continueToConfirmOrder)
    TextView tv_continueToConfirmOrder;

    @Bind(R.id.linearLayout_continueToConfirmOrder)
    LinearLayout linearLayout_continueToConfirmOrder;

    private boolean operateIfSuccess;
    private String showMsg;

    @Override
    public void initView() {
        setContentView(R.layout.activity_pay_money_result);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        Intent intent = getIntent();
        operateIfSuccess = intent.getBooleanExtra(InformationCodeUtil.IntentPayConfirmResultActivityOperateIfSuccess,false);
        showMsg = intent.getStringExtra(InformationCodeUtil.IntentPayConfirmResultActivityShowMsg);

        if(operateIfSuccess){
            iv_confirmOrderResultDesc.setImageResource(R.drawable.iv_confirm_order_success);
            tv_confirmOrderResultDesc.setText(showMsg);

            linearLayout_continueToShopping.setVisibility(View.VISIBLE);
            linearLayout_continueToMyOrder.setVisibility(View.VISIBLE);
            linearLayout_continueToConfirmOrder.setVisibility(View.GONE);
            linearLayout_continueToShopping.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);
                    finish();
                }
            });
            linearLayout_continueToMyOrder.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MyOrderActivity.class);
                    mContext.startActivity(intent);
                    finish();
                }
            });
        }else{
            iv_confirmOrderResultDesc.setImageResource(R.drawable.iv_confirm_order_false);
            tv_confirmOrderResultDesc.setText(showMsg);
            linearLayout_continueToShopping.setVisibility(View.GONE);
            linearLayout_continueToMyOrder.setVisibility(View.GONE);
            linearLayout_continueToConfirmOrder.setVisibility(View.VISIBLE);
            linearLayout_continueToConfirmOrder.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                }
            });
        }
    }

    //提交订单返回结果之后提醒
    void showPopuWindowOfConfirmResult(final boolean confirmIsSuccess, String msg) {
        View view = View.inflate(mContext, R.layout.item_pop_result_of_confirm_order, null);
        final PopupWindow pop = new PopupWindow(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, true);
        //订单是否提交成功
        ImageView iv_confirmOrderResultDesc = (ImageView) view.findViewById(R.id.iv_confirmOrderResultDesc);
        TextView tv_confirmOrderResultDesc = (TextView) view.findViewById(R.id.tv_confirmOrderResultDesc);

        //收货人信息，继续购物，去我的订单，重复提交控件
        LinearLayout linearLayout_continueToShopping = (LinearLayout) view.findViewById(R.id.linearLayout_continueToShopping);
        LinearLayout linearLayout_continueToMyOrder = (LinearLayout) view.findViewById(R.id.linearLayout_continueToMyOrder);
        LinearLayout linearLayout_continueToConfirmOrder = (LinearLayout) view.findViewById(R.id.linearLayout_continueToConfirmOrder);

        if (confirmIsSuccess) {
            iv_confirmOrderResultDesc.setImageResource(R.drawable.iv_confirm_order_success);
            tv_confirmOrderResultDesc.setText(msg);

            linearLayout_continueToShopping.setVisibility(View.VISIBLE);
            linearLayout_continueToMyOrder.setVisibility(View.VISIBLE);
            linearLayout_continueToConfirmOrder.setVisibility(View.GONE);
            linearLayout_continueToShopping.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MainActivity.class);
                    mContext.startActivity(intent);
                    finish();
                }
            });
            linearLayout_continueToMyOrder.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MyOrderActivity.class);
                    mContext.startActivity(intent);
                    finish();
                }
            });


        } else {
            iv_confirmOrderResultDesc.setImageResource(R.drawable.iv_confirm_order_false);
            tv_confirmOrderResultDesc.setText(msg);
            linearLayout_continueToShopping.setVisibility(View.GONE);
            linearLayout_continueToMyOrder.setVisibility(View.GONE);
            linearLayout_continueToConfirmOrder.setVisibility(View.VISIBLE);
            linearLayout_continueToConfirmOrder.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    pop.dismiss();
                }
            });
        }


        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                if (confirmIsSuccess) {
                    finish();
                }
            }
        });
    }
}
