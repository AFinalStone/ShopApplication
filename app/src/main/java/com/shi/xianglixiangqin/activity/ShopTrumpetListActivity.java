package com.shi.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseRecycleAdapter;
import com.shi.xianglixiangqin.model.TrumpetNoticeModel;
import com.shi.xianglixiangqin.util.InformationCodeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ShopTrumpetListActivity extends MyBaseActivity {

    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.recycleView)
    RecyclerView recycleView;
    private List<TrumpetNoticeModel> listData_InforMsg;
    private RecyclerViewAdapter adapterRecycle;

    String htmlHeader = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">"
            + "<html xmlns=\"http://www.w3.org/1999/xhtml\">"
            + "<head>"
            + "<meta name=\"viewport\" content=\"width=device-width,height=device-height,inital-scale=1.0,minimum-scale=1.0,maximum-scale=3.0,user-scalable=yes\" />"
            + "<meta id=\"vp\" name=\"viewport\" content=\"width=device-width, user-scalable=yes,maximum-scale=3.0,initial-scale=1.0,minimum-scale=1.0\" />"
            + "<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />"
            + "<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\" />"
            + "<meta name=\"format-detection\" content=\"telephone=no\" />"
            + "<style type='text/css'>"
            + "img{max-width:100%;height:auto;}"
            + "</style>"
            + "</head>"
            + "<body style='padding:0 0 0 0;margin:0 0 0 0;line-height:25px; font-size:12px;'>";

    String htmlRoot = "</body></html>";

    @Override
    public void initView() {
        setContentView(R.layout.activity_shop_trumpet_list);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        tv_title.setText(R.string.title_ShopTrumpetListActivity);
    }

    @Override
    public void initData() {
        listData_InforMsg = (List<TrumpetNoticeModel>) getIntent().getSerializableExtra(InformationCodeUtil.IntentShopTrumpetListInformMsg);
        adapterRecycle = new RecyclerViewAdapter(mContext, listData_InforMsg);
        recycleView.setLayoutManager(new LinearLayoutManager(mContext));
        recycleView.setAdapter(adapterRecycle);
    }


    @OnClick(R.id.iv_titleLeft)
    public void onClick() {
        finish();
    }

    private class RecyclerViewAdapter extends MyBaseRecycleAdapter<MyViewHolder, TrumpetNoticeModel> {


        public RecyclerViewAdapter(Context context, List<TrumpetNoticeModel> listData) {
            super(context, listData);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(mContext, R.layout.item_adapter_shop_trumpet_recycle_view, null);
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            TrumpetNoticeModel model = listData.get(position);
            final String strTitle = model.getTitle();
            final String strContent = htmlHeader + model.getContent()+ htmlRoot;
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent mIntent = new Intent(mContext,ShopTrumpetActivity.class);
                    mIntent.putExtra(InformationCodeUtil.IntentShopTrumpetTitle, strTitle);
                    mIntent.putExtra(InformationCodeUtil.IntentShopTrumpetContent, strContent);
                    startActivity(mIntent);
                }
            });
            holder.tv_trumpetTitle.setText(model.getTitle());
            holder.webView.loadDataWithBaseURL(null, strContent, "text/html", "utf-8", null);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_trumpetTitle)
        TextView tv_trumpetTitle;
        @BindView(R.id.webView)
        WebView webView;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind( this,itemView);
        }
    }

}
