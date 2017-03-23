package com.shi.xianglixiangqin.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import butterknife.ButterKnife;
import butterknife.BindView;

import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.adapter.MyBaseAdapter;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.json.JSONResultMsgModel;
import com.shi.xianglixiangqin.model.CustomModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.PreferencesUtilMy;
import com.shi.xianglixiangqin.util.StringUtil;
import com.shi.xianglixiangqin.util.ToastUtil;
import com.shi.xianglixiangqin.view.FragmentViewDialog;

/**
 * 为微商场店铺添加特价商品
 *
 * @author SHI 2016年5月12日 17:11:32
 */
public class WXShopAddGoods extends MyBaseActivity implements OnClickListener,
        OnConnectServerStateListener<Integer> {

    /**
     * 后退控件
     **/
    @BindView(R.id.iv_titleLeft)
    ImageView iv_titleLeft;
    /**
     * 页面标题设置为我的代理商品
     **/
    @BindView(R.id.tv_title)
    TextView tv_title;

    /**
     * 我的店铺名称外围控件
     **/
    @BindView(R.id.linearLayout_changeShopName)
    LinearLayout linearLayout_changeShopName;
    @BindView(R.id.tv_shopName)
    TextView tv_shopName;
    /**
     * 特价商品设置
     **/
    @BindView(R.id.linearLayout_settingGoods)
    LinearLayout linearLayout_settingGoods;
    /**
     * 选择商品分类
     **/
    @BindView(R.id.listView)
    ListView listView;
    private MyAdapter myAdapter;
    private List<WXShopTjClassModel> listData = new ArrayList<WXShopTjClassModel>();
    private final String GoodsClassName = "商品分类";
    private final int RequestCode_GoodsClassType01 = 0;
    private final int RequestCode_GoodsClassType02 = 1;
    /**
     * 确认提交
     **/
    @BindView(R.id.btn_submit)
    Button btn_submit;

    @Override
    public void initView() {
        setContentView(R.layout.activity_wxshop_addgoods);
        ButterKnife.bind(this);
        iv_titleLeft.setVisibility(View.VISIBLE);
        iv_titleLeft.setOnClickListener(this);
        tv_title.setText(R.string.wxShopAddGoodsTitle);
        linearLayout_changeShopName.setOnClickListener(this);
        linearLayout_settingGoods.setOnClickListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (RequestCode_GoodsClassType01 == position) {
                    Intent intent = new Intent(mContext,
                            WXShopTypeSelectActivity.class);
                    intent.putExtra(
                            InformationCodeUtil.IntentWXShopTypeSelectActivityGoodsClassNameType,
                            RequestCode_GoodsClassType01);
                    startActivityForResult(intent, RequestCode_GoodsClassType01);
                    return;
                }
                if (RequestCode_GoodsClassType02 == position) {
                    Intent intent = new Intent(mContext,
                            WXShopTypeSelectActivity.class);
                    intent.putExtra(
                            InformationCodeUtil.IntentWXShopTypeSelectActivityGoodsClassNameType,
                            position);
                    startActivityForResult(intent, RequestCode_GoodsClassType02);
                    return;
                }
            }
        });
        btn_submit.setOnClickListener(this);
    }

    @Override
    public void initData() {
        tv_shopName.setText(MyApplication.getmCustomModel(mContext).getShopName());
        WXShopTjClassModel model01 = new WXShopTjClassModel(-1, false,
                getResources().getString(R.string.selectWXShopGoodsClass));
        listData.add(model01);
        WXShopTjClassModel model02 = new WXShopTjClassModel(-1, false,
                getResources().getString(R.string.selectWXShopGoodsClass));
        listData.add(model02);
        myAdapter = new MyAdapter(mContext, listData);
        listView.setAdapter(myAdapter);
        getData(InformationCodeUtil.methodNameGetEcShopRecClass);
    }

    private void getData(String methodName) {
        if (InformationCodeUtil.methodNameGetEcShopRecClass.equals(methodName)) {
            SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
            requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
            requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
            ConnectCustomServiceAsyncTask connectCustomTask = new ConnectCustomServiceAsyncTask(mContext, this, requestSoapObject, methodName);
            connectCustomTask.execute();
            return;
        }
        if (InformationCodeUtil.methodNameSetEcShopRecClass.equals(methodName)) {

            int firClassID = listData.get(0).getID();
            int secClassID = listData.get(1).getID();
//			if(firClassID == -1){
//				ToastUtil.show(mContext, "请为商品分类1选择商品分类");
//				return;
//			}
//			if(secClassID == -1){
//				ToastUtil.show(mContext, "请为商品分类2选择商品分类");
//				return;
//			}

            SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
            requestSoapObject.addProperty("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
            requestSoapObject.addProperty("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
            requestSoapObject.addProperty("firClassID", firClassID);
            requestSoapObject.addProperty("secClassID", secClassID);
            ConnectCustomServiceAsyncTask connectCustomTask = new ConnectCustomServiceAsyncTask(mContext, this, requestSoapObject, methodName);
            connectCustomTask.execute();
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_titleLeft:
                finish();
                break;
            case R.id.linearLayout_settingGoods:
                startActivity(new Intent(mContext, WXShopGoodsManageActivity.class));
                break;
            case R.id.btn_submit:
                getData(InformationCodeUtil.methodNameSetEcShopRecClass);
                break;
            case R.id.linearLayout_changeShopName:
                showChangeInfoDialog(tv_shopName.getText().toString());
                break;
            default:
                break;
        }
    }

    /**修改用户信息对话框**/
    void showChangeInfoDialog(String message) {

        View view = View.inflate(mContext, R.layout.dialog_userdefine_view, null);
        TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
        final EditText et_message = (EditText)view.findViewById(R.id.et_message);
        tv_title.setText("修改店铺名称");
        et_message.setText(message);
        FragmentViewDialog fragmentViewDialog = new FragmentViewDialog();

        fragmentViewDialog.initView(view, "取消", "修改",  new FragmentViewDialog.OnButtonClickListener() {

            @Override
            public void OnOkClick() {
                    changeShopNameData(et_message.getText().toString().trim());
            }

            @Override
            public void OnCancelClick() {

            }
        });

        fragmentViewDialog.show(getSupportFragmentManager(), "fragmentDialog");
    }

    void changeShopNameData(String newShopName) {
            if(StringUtil.isEmpty(newShopName) || newShopName.equals(MyApplication.getmCustomModel(mContext).getShopName())){
                return;
            }
            String methodName = InformationCodeUtil.methodNameUpdateUserInfo;
            SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
            requestSoapObject.addPropertyIfValue("customID", MyApplication.getmCustomModel(mContext).getDjLsh());
            requestSoapObject.addPropertyIfValue("openKey", MyApplication.getmCustomModel(mContext).getOpenKey());
            requestSoapObject.addPropertyIfValue("realName", MyApplication.getmCustomModel(mContext).getRealName());
            requestSoapObject.addPropertyIfValue("shopName", newShopName);
            requestSoapObject.addPropertyIfValue("headStr", null);
            ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
                    mContext, this, requestSoapObject , methodName);
            connectCustomServiceAsyncTask.setConnectOutTime(10000);
            connectCustomServiceAsyncTask.execute();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        String selectGoodsClassName = data
                .getStringExtra(InformationCodeUtil.IntentWXSelectGoodsClassName);
        int selectGoodsClassID = data.getIntExtra(
                InformationCodeUtil.IntentWXSelectGoodsClassID, -1);
        if (StringUtil.isEmpty(selectGoodsClassName)) {
            selectGoodsClassName = "";
        }
        switch (requestCode) {
            case RequestCode_GoodsClassType01:
                listData.get(RequestCode_GoodsClassType01).setIsSelected(true);
                listData.get(RequestCode_GoodsClassType01).setID(selectGoodsClassID);
                listData.get(RequestCode_GoodsClassType01).setName(selectGoodsClassName);
                myAdapter.notifyDataSetChanged();
                break;

            case RequestCode_GoodsClassType02:
                listData.get(RequestCode_GoodsClassType02).setIsSelected(true);
                listData.get(RequestCode_GoodsClassType02).setID(selectGoodsClassID);
                listData.get(RequestCode_GoodsClassType02).setName(selectGoodsClassName);
                myAdapter.notifyDataSetChanged();
                break;
        }
    }

    // {
    // "DjLsh": -1,
    // "Msg":
    // "{\"FirstRecClass\":[{\"ID\":97,\"IsSelected\":false,\"Name\":\"笔记本\"},{\"ID\":98,\"IsSelected\":false,\"Name\":\"台式机\"},{\"ID\":99,\"IsSelected\":false,\"Name\":\"一体机\"},{\"ID\":100,\"IsSelected\":false,\"Name\":\"平板电脑\"},{\"ID\":107,\"IsSelected\":false,\"Name\":\"CPU\"},{\"ID\":108,\"IsSelected\":false,\"Name\":\"内存\"},{\"ID\":110,\"IsSelected\":false,\"Name\":\"主板\"},{\"ID\":112,\"IsSelected\":false,\"Name\":\"显示器\"},{\"ID\":115,\"IsSelected\":false,\"Name\":\"鼠标键盘\"},{\"ID\":116,\"IsSelected\":false,\"Name\":\"电脑包\"},{\"ID\":134,\"IsSelected\":false,\"Name\":\"U盘\"},{\"ID\":136,\"IsSelected\":false,\"Name\":\"移动硬盘\"},{\"ID\":141,\"IsSelected\":false,\"Name\":\"手机\"},{\"ID\":161,\"IsSelected\":false,\"Name\":\"数码相机\"},{\"ID\":170,\"IsSelected\":false,\"Name\":\"单反相机\"},{\"ID\":178,\"IsSelected\":false,\"Name\":\"摄像机\"},{\"ID\":187,\"IsSelected\":false,\"Name\":\"耳机\\\/耳麦\"},{\"ID\":188,\"IsSelected\":false,\"Name\":\"便携音箱\"},{\"ID\":216,\"IsSelected\":false,\"Name\":\"纸类\"},{\"ID\":217,\"IsSelected\":false,\"Name\":\"组装电脑\"}],\"SecondRecClass\":[{\"ID\":97,\"IsSelected\":false,\"Name\":\"笔记本\"},{\"ID\":98,\"IsSelected\":false,\"Name\":\"台式机\"},{\"ID\":99,\"IsSelected\":false,\"Name\":\"一体机\"},{\"ID\":100,\"IsSelected\":false,\"Name\":\"平板电脑\"},{\"ID\":107,\"IsSelected\":false,\"Name\":\"CPU\"},{\"ID\":108,\"IsSelected\":false,\"Name\":\"内存\"},{\"ID\":110,\"IsSelected\":false,\"Name\":\"主板\"},{\"ID\":112,\"IsSelected\":false,\"Name\":\"显示器\"},{\"ID\":115,\"IsSelected\":false,\"Name\":\"鼠标键盘\"},{\"ID\":116,\"IsSelected\":false,\"Name\":\"电脑包\"},{\"ID\":134,\"IsSelected\":false,\"Name\":\"U盘\"},{\"ID\":136,\"IsSelected\":false,\"Name\":\"移动硬盘\"},{\"ID\":141,\"IsSelected\":false,\"Name\":\"手机\"},{\"ID\":161,\"IsSelected\":false,\"Name\":\"数码相机\"},{\"ID\":170,\"IsSelected\":false,\"Name\":\"单反相机\"},{\"ID\":178,\"IsSelected\":false,\"Name\":\"摄像机\"},{\"ID\":187,\"IsSelected\":false,\"Name\":\"耳机\\\/耳麦\"},{\"ID\":188,\"IsSelected\":false,\"Name\":\"便携音箱\"},{\"ID\":216,\"IsSelected\":false,\"Name\":\"纸类\"},{\"ID\":217,\"IsSelected\":false,\"Name\":\"组装电脑\"}]}",
    // "Sign": 1
    // }
    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, Integer state, boolean whetherRefresh) {
        LogUtil.LogShitou("返回结果", returnString);
        Gson gson = new Gson();
        if (InformationCodeUtil.methodNameGetEcShopRecClass.equals(methodName)) {
            try {
                JSONResultMsgModel msgModel = gson.fromJson(returnString,
                        JSONResultMsgModel.class);
                JSONObject jsonObject = new JSONObject(msgModel.getMsg());
                String jsonGoodsTjClass01 = jsonObject
                        .getString("FirstRecClass");
                String jsonGoodsTjClass02 = jsonObject
                        .getString("SecondRecClass");
                List<WXShopTjClassModel> listGoodsTjClass01 = gson.fromJson(
                        jsonGoodsTjClass01,
                        new TypeToken<List<WXShopTjClassModel>>() {
                        }.getType());

                List<WXShopTjClassModel> listGoodsTjClass02 = gson.fromJson(
                        jsonGoodsTjClass02,
                        new TypeToken<List<WXShopTjClassModel>>() {
                        }.getType());
                updateView(listGoodsTjClass01, listGoodsTjClass02);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (InformationCodeUtil.methodNameSetEcShopRecClass.equals(methodName)) {
            JSONResultMsgModel msgModel = gson.fromJson(returnString,
                    JSONResultMsgModel.class);
            ToastUtil.show(mContext, msgModel.getMsg());
        }

        if(InformationCodeUtil.methodNameUpdateUserInfo.equals(methodName)){
            try {
                JSONObject jsonObject = new JSONObject(returnString);
                int sign = jsonObject.getInt("Sign");
                if(sign == 1){
                    CustomModel mCustomModel= gson.fromJson( jsonObject.getString("Msg"), CustomModel.class);
                    LogUtil.LogShitou("对象数据", mCustomModel.toString());
                    mCustomModel.setLoginShopID(MyApplication.getmCustomModel(mContext).getLoginShopID());
                    mCustomModel.setCurrentBrowsingShopID(MyApplication.getmCustomModel(mContext).getCurrentBrowsingShopID());
//                    PreferencesUtilMy.saveCustomModel(mContext, mCustomModel);
                    MyApplication.setmCustomModel( mContext, mCustomModel);
                    tv_shopName.setText(MyApplication.getmCustomModel(mContext).getShopName());
                }else{
                    ToastUtil.show(mContext, jsonObject.getString("Msg"));
                }
            } catch (Exception e) {
                ToastUtil.show(mContext, "数据异常,修改用户信息失败");
                e.printStackTrace();
            }
        }

    }

    private void updateView(List<WXShopTjClassModel> listGoodsTjClass01,
                            List<WXShopTjClassModel> listGoodsTjClass02) {
        if (listGoodsTjClass01 != null) {
            for (int i = 0; i < listGoodsTjClass01.size(); i++) {
                WXShopTjClassModel model = listGoodsTjClass01.get(i);
                if (model.getIsSelected()) {
                    listData.get(0).setID(model.getID());
                    listData.get(0).setName(model.getName());
                    listData.get(0).setIsSelected(model.getIsSelected());
                    break;
                }
            }
        }
        if (listGoodsTjClass02 != null) {
            for (int i = 0; i < listGoodsTjClass02.size(); i++) {
                WXShopTjClassModel model = listGoodsTjClass02.get(i);
                if (model.getIsSelected()) {
                    listData.get(1).setID(model.getID());
                    listData.get(1).setName(model.getName());
                    listData.get(1).setIsSelected(model.getIsSelected());
                    break;
                }
            }
        }
        myAdapter.notifyDataSetChanged();
    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Integer state,
                                     boolean whetherRefresh) {
        if (InformationCodeUtil.methodNameGetEcShopRecClass.equals(methodName)) {
            ToastUtil.show(mContext, "网络异常，微店推荐栏目数据获取失败");
            return;
        }

        if (InformationCodeUtil.methodNameSetEcShopRecClass.equals(methodName)) {
            ToastUtil.show(mContext, "网络异常，微店推荐栏目设置失败");
            return;
        }

        if(InformationCodeUtil.methodNameUpdateUserInfo.equals(methodName)){
            ToastUtil.show(mContext, "网络异常,修改用户信息失败");
        }
    }

    @Override
    public void connectServiceCancelled(String returnString, String methodName,
                                        Integer state, boolean whetherRefresh) {

    }

    private class MyAdapter extends MyBaseAdapter<WXShopTjClassModel> {

        public MyAdapter(Context mContext, List<WXShopTjClassModel> listData) {
            super(mContext, listData);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(mContext,
                    R.layout.item_adapter_wxshopaddgoods_listview, null);
            TextView tv_GoodsClass = (TextView) view
                    .findViewById(R.id.tv_GoodsClass);
            TextView tv_selectGoodsClass = (TextView) view
                    .findViewById(R.id.tv_selectGoodsClass);
            tv_GoodsClass.setText(GoodsClassName + (position + 1));
            if (listData.get(position).getIsSelected()) {
                tv_selectGoodsClass.setText(listData.get(position).getName());
            } else {
                tv_selectGoodsClass.setHint(listData.get(position).getName());
            }
            return view;
        }

    }

    /**
     * 微店推荐分类标题model
     *
     * @author SHI 2016年5月13日 15:30:23
     */
    private class WXShopTjClassModel {
        private int ID;
        private boolean IsSelected;
        private String Name;

        public WXShopTjClassModel(int iD, boolean isSelected, String name) {
            super();
            ID = iD;
            IsSelected = isSelected;
            Name = name;
        }

        public boolean getIsSelected() {
            return IsSelected;
        }

        public int getID() {
            return ID;
        }

        public void setID(int iD) {
            ID = iD;
        }

        public void setIsSelected(boolean isSelected) {
            IsSelected = isSelected;
        }

        public String getName() {
            return Name;
        }

        public void setName(String name) {
            Name = name;
        }
    }

}
