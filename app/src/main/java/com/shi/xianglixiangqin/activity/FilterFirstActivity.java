package com.shi.xianglixiangqin.activity;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afinalstone.androidstudy.swiperefreshview.OnSwipeRefreshViewListener;
import com.afinalstone.androidstudy.swiperefreshview.SwipeRefreshListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.model.GoodsFilterModelEx;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.DensityUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * @author Zhu
 * @action 搜索筛选界面
 * @date 2016-2-1 11:39:38
 */
public class FilterFirstActivity extends MyBaseActivity implements OnClickListener, OnConnectServerStateListener<Integer>, OnSwipeRefreshViewListener {
    /**
     * 返回图片控件
     **/
    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    /**
     * 标题文本
     **/
    @BindView(R.id.tv_title)
    TextView tv_title;

    /**
     * 清空筛选
     **/
    @BindView(R.id.btn_clearFilter)
    Button btn_clearFilter;
    /**
     * 确认筛选
     **/
    @BindView(R.id.btn_confirmFilter)
    Button btn_confirmFilter;

    /***
     * 筛选条件列表
     **/
    @BindView(R.id.swipeRefreshListView)
    SwipeRefreshListView swipeRefreshListView;
    /**
     * ListView 对应的数据
     **/
    List<GoodsFilterModelEx> listData = new ArrayList<GoodsFilterModelEx>();
    /**
     * 适配器
     **/
    FilterListViewParentAdapter FilterListViewParentAdapter;
    int currentFilterClassID;

    public void initView() {
        super.setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        tv_title.setText(R.string.title_GoodsFilter);
        iv_titleLeft.setVisibility(View.VISIBLE);
        swipeRefreshListView.setOnRefreshListener(this);
        FilterListViewParentAdapter = new FilterListViewParentAdapter(mContext, listData);
        swipeRefreshListView.getListView().setAdapter(FilterListViewParentAdapter);
    }

    public void initData() {
        currentFilterClassID = getIntent().getIntExtra(InformationCodeUtil.IntentFilterFirstActivityFilterClassID, 0);
        swipeRefreshListView.openRefreshState();
    }

    @Override
    public void onTopRefrushListener() {
        getData();
    }

    @Override
    public void onBottomRefrushListener() {
        getData();
    }


    private void getData() {
        listData.clear();
        FilterListViewParentAdapter.notifyDataSetChanged();
        String methodName = InformationCodeUtil.methodNameGetSPWhere;
        SoapObject soapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        soapObject.addProperty("shopid", MyApplication.getmCustomModel(mContext).getCurrentBrowsingShopID());
        soapObject.addProperty("classid", currentFilterClassID);
        ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(
                mContext, this, soapObject, methodName);
        connectGoodsServiceAsyncTask.initProgressDialog(false);
        connectGoodsServiceAsyncTask.execute();
        LogUtil.LogShitou(soapObject.toString());
    }

    @OnClick({R.id.iv_titleLeft, R.id.btn_confirmFilter, R.id.btn_clearFilter})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.btn_confirmFilter:
                confirmFilterResult();
                break;
            case R.id.btn_clearFilter:
                clearFilter();
                break;
            default:
                break;
        }
    }

    /**
     * 提交筛选结果
     **/
    private void confirmFilterResult() {

        List<HashMap> selectValue = new ArrayList<>();
        for (int i = 0; i < listData.size(); i++) {
            int checkPosition = listData.get(i).getCheckPosition();
            if (checkPosition != -1) {
                HashMap hashMap = new HashMap();
                String attr_key = listData.get(i).getAttr_name();
                String attr_value = listData.get(i).getAttr_val().get(checkPosition);
                hashMap.put(attr_key, attr_value);
                selectValue.add(hashMap);
                LogUtil.LogShitou(hashMap.toString());
            }
        }
        Gson gson = new Gson();
        Intent intent = getIntent();
        intent.putExtra(InformationCodeUtil.IntentFilterActivitySelectFilter, gson.toJson(selectValue));
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 清空筛选
     **/
    private void clearFilter() {
        for (int i = 0; i < listData.size(); i++) {
            listData.get(i).setCheckPosition(-1);
        }
        FilterListViewParentAdapter.notifyDataSetChanged();
    }

    @Override
    public void connectServiceSuccessful(String returnString, String methodName, Integer state, boolean whetherRefresh) {

        LogUtil.LogShitou(returnString);
        try {
            Gson gson = new Gson();
            List<GoodsFilterModelEx> list = gson.fromJson(returnString, new TypeToken<List<GoodsFilterModelEx>>() {
            }.getType());
            listData.addAll(list);
            FilterListViewParentAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
        swipeRefreshListView.closeRefreshState();
    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Integer state, boolean whetherRefresh) {
        swipeRefreshListView.closeRefreshState();
        ToastUtil.show(mContext, returnStrError);
    }

    @Override
    public void connectServiceCancelled(String returnString, String methodName, Integer state, boolean whetherRefresh) {

    }

    /**
     * @author SHI
     *         2016-2-17 15:36:36
     * @action 筛选条件适配器
     */
    public class FilterListViewParentAdapter extends MyBaseAdapter<GoodsFilterModelEx> {


        public FilterListViewParentAdapter(Context mContext, List<GoodsFilterModelEx> listData) {
            super(mContext, listData);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_adapter_filter_listview_parent, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final GoodsFilterModelEx model = listData.get(position);
            holder.tv_filterType.setText(model.getAttr_name());
            holder.gridView_filterNames.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (model.getCheckPosition() == position) {
                        model.setCheckPosition(-1);
                    } else {
                        model.setCheckPosition(position);
                    }
                    notifyDataSetChanged();
                    LogUtil.LogShitou(position + "被点击");
                }
            });
            holder.gridView_filterNames.setAdapter(new FilterListViewChildAdapter(mContext, model.getAttr_val(), model));
            return convertView;
        }


        public class ViewHolder {

            @BindView(R.id.tv_filterType)
            TextView tv_filterType;

            @BindView(R.id.gridView_filterNames)
            GridView gridView_filterNames;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }

    public class FilterListViewChildAdapter extends MyBaseAdapter<String> {

        private GoodsFilterModelEx model;

        public FilterListViewChildAdapter(Context mContext, List<String> listData, GoodsFilterModelEx model) {
            super(mContext, listData);
            this.model = model;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(mContext, R.layout.item_adapter_filter_listview_child, null);
            CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.cb_filterItemName);
            checkBox.setText(listData.get(position) + model.getAttr_dw());
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        model.setCheckPosition(position);
                        notifyDataSetChanged();
                    } else {
                        model.setCheckPosition(-1);
                    }
                }
            });
            if (model.getCheckPosition() == position) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }
            return convertView;
        }

    }
}
