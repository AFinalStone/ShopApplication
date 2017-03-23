package com.shi.xianglixiangqin.activity;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.util.InformationCodeUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopTrumpetActivity extends MyBaseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.webView)
    WebView webView;

    @Override
    public void initView() {
        setContentView(R.layout.activity_shop_trumpet);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);

    }

    @Override
    public void initData() {
        String strContent = getIntent().getStringExtra(InformationCodeUtil.IntentShopTrumpetContent);
        String strTitle = getIntent().getStringExtra(InformationCodeUtil.IntentShopTrumpetTitle);
        tv_title.setText(strTitle);
        webView.loadDataWithBaseURL(null, strContent, "text/html", "utf-8", null);
    }


    @OnClick(R.id.iv_titleLeft)
    public void onClick() {
        finish();
    }
}
