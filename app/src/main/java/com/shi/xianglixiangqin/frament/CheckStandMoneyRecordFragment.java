package com.shi.xianglixiangqin.frament;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afinalstone.androidstudy.swiperefreshview.SwipeRefreshRecycleView;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.activity.CheckStandMoneyRecordActivity;
import com.shi.xianglixiangqin.adapter.MyBaseRecycleAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SHI on 2017/2/28 14:00
 * 收银台记录页面
 */
public class CheckStandMoneyRecordFragment extends MyBaseFragment<CheckStandMoneyRecordActivity> {

    private String title;

    /**
     * 交易记录
     **/
    SwipeRefreshRecycleView swipeRefreshRecycleView;

    List<RecordModel> listData = new ArrayList<>();
    RecycleAdapter myAdapter;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(mActivity, R.layout.fragment_check_stand_money_record, null);
        swipeRefreshRecycleView = (SwipeRefreshRecycleView) view.findViewById(R.id.swipeRefreshRecycleView);
        return view;
    }

    @Override
    public void initData() {
        listData.add(new RecordModel());
        listData.add(new RecordModel());
        listData.add(new RecordModel());
        listData.add(new RecordModel());
        myAdapter = new RecycleAdapter(mActivity,listData);
        swipeRefreshRecycleView.getItemView().setLayoutManager(new LinearLayoutManager(mActivity));
        swipeRefreshRecycleView.getItemView().setAdapter(myAdapter);
    }

    public class RecycleAdapter extends MyBaseRecycleAdapter<RecycleAdapter.MyViewHolder, RecordModel> {

        public RecycleAdapter(Context context, List<RecordModel> listData) {
            super(context, listData);
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = View.inflate(mActivity, R.layout.item_adapter_check_stand_money_record, null);
            view.setLayoutParams(new RecyclerView.LayoutParams(mActivity.displayDeviceWidth,RecyclerView.LayoutParams.WRAP_CONTENT));
            MyViewHolder myViewHolder = new MyViewHolder(view);
            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {

        }

        protected class MyViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_recordType)
            TextView tv_recordType;
            @BindView(R.id.tv_date)
            TextView tv_date;
            @BindView(R.id.tv_charge)
            TextView tv_charge;
            @BindView(R.id.tv_recordNumber)
            TextView tv_recordNumber;

            public MyViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public class RecordModel {

    }
}
