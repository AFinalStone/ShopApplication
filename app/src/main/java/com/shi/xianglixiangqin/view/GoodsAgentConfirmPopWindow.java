package com.shi.xianglixiangqin.view;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import org.ksoap2.serialization.SoapObject;


/**
 * 代理商品
 * SHI
 * 2016年5月19日 15:31:27
 */
public class GoodsAgentConfirmPopWindow extends PopupWindow{

    onAgentConfirmListener onAgentConfirmListener;
    boolean IfGoWXShopAlone;
    public GoodsAgentConfirmPopWindow(final Activity mContext, final onAgentConfirmListener listener) {
        super(mContext);
        onAgentConfirmListener = listener;
        View popView = View.inflate(mContext, R.layout.item_pop_goods_agent_confirm, null);
        ImageView iv_close = (ImageView) popView.findViewById(R.id.iv_close);
        ImageView iv_image = (ImageView) popView.findViewById(R.id.iv_image);
        TextView tv_agentDesc = (TextView) popView.findViewById(R.id.tv_agentDesc);
        final CheckBox cb_agentToMyShop = (CheckBox) popView.findViewById(R.id.cb_agentToMyShop);
        final CheckBox cb_agentToMyWXShop = (CheckBox) popView.findViewById(R.id.cb_agentToMyWXShop);
        Button btn_confirmAgent = (Button) popView.findViewById(R.id.btn_confirmAgent);

        if (MyApplication.getmCustomModel(mContext).getRoleID() == 3) {
            IfGoWXShopAlone = true;
            iv_image.setImageResource(R.mipmap.icon_goods_agent_confirm_header_01);
            cb_agentToMyShop.setVisibility(View.GONE);
            cb_agentToMyWXShop.setVisibility(View.GONE);
            tv_agentDesc.setVisibility(View.VISIBLE);
        } else {
            IfGoWXShopAlone = false;
            iv_image.setImageResource(R.mipmap.icon_goods_agent_confrim_header_02);
            cb_agentToMyShop.setVisibility(View.VISIBLE);
            cb_agentToMyWXShop.setVisibility(View.VISIBLE);
            tv_agentDesc.setVisibility(View.GONE);
        }
        // 设置按钮监听
        iv_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btn_confirmAgent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onAgentConfirmListener != null){
                    if (IfGoWXShopAlone) {
                        listener.agentGoods(2);
                        dismiss();
                        return;
                    }
                    if (cb_agentToMyShop.isChecked() && cb_agentToMyWXShop.isChecked()) {
                        listener.agentGoods(0);
                        dismiss();
                        return;
                    }
                    if (cb_agentToMyShop.isChecked()) {
                        listener.agentGoods(1);
                        dismiss();
                        return;
                    }
                    if (cb_agentToMyWXShop.isChecked()) {
                        listener.agentGoods(2);
                        dismiss();
                        return;
                    }
                    ToastUtil.show(mContext, "请选择代理方式");
                }
            }
        });

        setContentView(popView);
        setWidth(LayoutParams.MATCH_PARENT);
        setHeight(LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        setBackgroundDrawable(dw);
    }

    public interface  onAgentConfirmListener{
        public void agentGoods(int gowhere);
    }

}
