package com.shi.xianglixiangqin.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.util.CreateQRImageUtil;
import com.shi.xianglixiangqin.util.DensityUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 提现页面
 * @author SHI
 * @time 2017/2/28 15:57
 */
public class CheckStandWithdrawalActivity extends MyBaseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_titleRight)
    ImageView iv_titleRight;

    @BindView(R.id.tv_arriveTomorrow)
    TextView tv_arriveTomorrow;

    @BindView(R.id.iv_arriveTomorrow)
    ImageView iv_arriveTomorrow;

    @BindView(R.id.linearLayout_arriveTomorrow)
    LinearLayout linearLayout_arriveTomorrow;

    @BindView(R.id.tv_arriveImmediately)
    TextView tv_arriveImmediately;

    @BindView(R.id.iv_arriveImmediately)
    ImageView iv_arriveImmediately;

    @BindView(R.id.linearLayout_arriveImmediately)
    LinearLayout linearLayout_arriveImmediately;

    @BindView(R.id.iv_QDCode)
    ImageView iv_QDCode;

    /**
     * 二维码生成对象
     **/
    private CreateQRImageUtil createQRImageUtil = new CreateQRImageUtil();

    @Override
    public void initView() {
        setContentView(R.layout.activity_check_stand_withdrawal);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        iv_titleRight.setVisibility(View.VISIBLE);
        iv_titleRight.setImageResource(R.mipmap.icon_check_to_money_record);
    }

    @Override
    public void initData() {
        int length = DensityUtil.dip2px(mContext,225);
        createQRImageUtil.createQRImage("http://www.zcy.gov.cn/", iv_QDCode, length, length);
    }

    @OnClick({R.id.iv_titleLeft, R.id.iv_titleRight, R.id.linearLayout_arriveTomorrow, R.id.linearLayout_arriveImmediately})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.iv_titleRight:
                startActivity(new Intent(mContext, CheckStandMoneyRecordActivity.class));
                break;
            case R.id.linearLayout_arriveTomorrow:
                showArriveTomorrow();
                break;
            case R.id.linearLayout_arriveImmediately:
                showArriveImmediately();
                break;
        }
    }

    //明天到账
    private void showArriveTomorrow() {
        //右侧恢复正常颜色
        linearLayout_arriveImmediately.setBackgroundResource(R.drawable.button_shape_check_stand_rightcircle_normal);
        tv_arriveImmediately.setTextColor(getResources().getColor(R.color.colorRed_FFE83821));
        iv_arriveImmediately.setImageResource(R.mipmap.icon_check_immediately_red);

        linearLayout_arriveTomorrow.setBackgroundResource(R.drawable.button_shape_check_stand_leftcircle_select);
        tv_arriveTomorrow.setTextColor(getResources().getColor(R.color.colorWhite_FFFFFFFF));
        iv_arriveTomorrow.setImageResource(R.mipmap.icon_check_tomorry_white);
    }

    //立即到账
    private void showArriveImmediately() {
        //左侧恢复正常颜色
        linearLayout_arriveTomorrow.setBackgroundResource(R.drawable.button_shape_check_stand_leftcircle_normal);
        tv_arriveTomorrow.setTextColor(getResources().getColor(R.color.colorRed_FFE83821));
        iv_arriveTomorrow.setImageResource(R.mipmap.icon_check_tomorry_red);

        linearLayout_arriveImmediately.setBackgroundResource(R.drawable.button_shape_check_stand_rightcircle_select);
        tv_arriveImmediately.setTextColor(getResources().getColor(R.color.colorWhite_FFFFFFFF));
        iv_arriveImmediately.setImageResource(R.mipmap.icon_check_immediately_white);
    }
}
