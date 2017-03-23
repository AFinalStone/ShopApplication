package com.shi.xianglixiangqin.frament;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.activity.ShopActivity;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.task.ConnectGoodsServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.ToastUtil;

import org.ksoap2.serialization.SoapObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopInstructionFragment extends MyBaseFragment<ShopActivity> implements OnConnectServerStateListener<Integer> {

    /**店铺图片**/
    @BindView(R.id.logImageView)
    ImageView logImageView;
    /**店铺名称**/
    @BindView(R.id.tv_shopName)
    TextView tv_shopName;
    /**真实名称**/
    @BindView(R.id.tv_realName)
    TextView tv_realName;
    /**联系电话**/
    @BindView(R.id.tv_phoneNumber)
    TextView tv_phoneNumber;
    /**店铺地址**/
    @BindView(R.id.tv_shopAddress)
    TextView tv_shopAddress;
    /**营业执照**/
    @BindView(R.id.iv_businessLicense)
    ImageView iv_businessLicense;
    /**店铺简介**/
    @BindView(R.id.tv_shopIntroduction)
    TextView tv_shopIntroduction;

    private View rootView;

    public ShopInstructionFragment() {
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView == null){
            rootView = inflater.inflate(R.layout.pager_shop_information, container, false);
            ButterKnife.bind(this,rootView);
        }
        return rootView;
    }


    @Override
    public void initData() {
        if(!connectSuccessFlag){
            connectSuccessFlag = true;
            getData(InformationCodeUtil.methodNameGetShopInfo);
        }
    }

    private void getData(String methodName) {
        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
        requestSoapObject.addProperty("shopID", mActivity.currentShopID);
        ConnectGoodsServiceAsyncTask connectGoodsServiceAsyncTask = new ConnectGoodsServiceAsyncTask(mActivity,
                this, requestSoapObject , methodName);
        connectGoodsServiceAsyncTask.execute();
    }

    @Override
    public void connectServiceSuccessful(String returnString,
                                         String methodName, Integer state, boolean whetherRefresh) {
        LogUtil.LogShitou("店铺详情",returnString);
        Gson gson = new Gson();
        ShopIntroductionModel mShopIntroductionModel = gson.fromJson(returnString, ShopIntroductionModel.class);
        refreshHome(mShopIntroductionModel);
    }

    /**网路数据请求成功，刷新界面数据**/
    private void refreshHome(ShopIntroductionModel mShopIntroductionModel) {

        if(TextUtils.isEmpty(mShopIntroductionModel.getShopName())){
            tv_shopName.setText("店铺名称：还未填写店铺名称");
        }else{
            tv_shopName.setText("店铺名称："+mShopIntroductionModel.getShopName());
        }
        if(TextUtils.isEmpty(mShopIntroductionModel.getRealName())){
            tv_realName.setText("真实姓名：还未填写真实名称");
        }else{
            tv_realName.setText("真实姓名"+mShopIntroductionModel.getRealName());
        }
        if(TextUtils.isEmpty(mShopIntroductionModel.getPhoneNum())){
            tv_phoneNumber.setText("还未填写联系电话");
        }else{
            tv_phoneNumber.setText(mShopIntroductionModel.getPhoneNum());
        }
        if(TextUtils.isEmpty(mShopIntroductionModel.getAddress())){
            tv_shopAddress.setText("店铺地址：还未填写店铺地址");
        }else{
            tv_shopAddress.setText("店铺地址："+mShopIntroductionModel.getAddress());
        }

        ImagerLoaderUtil.getInstance(mActivity).displayMyImage(mShopIntroductionModel.getImgUrl(), logImageView);

        if(TextUtils.isEmpty(mShopIntroductionModel.getBusImgUrl())){
            ImagerLoaderUtil.getInstance(mActivity).displayMyImage(mShopIntroductionModel.getImgUrl(),iv_businessLicense);
        }else{
            ImagerLoaderUtil.getInstance(mActivity).displayMyImage(mShopIntroductionModel.getBusImgUrl(),iv_businessLicense);
        }

        if(TextUtils.isEmpty(mShopIntroductionModel.getRemark())){
            tv_shopIntroduction.setText("还未填写店铺简介");
        }else{
            tv_shopIntroduction.setText(mShopIntroductionModel.getRemark());
        }
    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Integer state, boolean whetherRefresh) {
        connectSuccessFlag = false;
        ToastUtil.show(mActivity, "网络异常，数据请求失败");
    }


    @Override
    public void connectServiceCancelled(String returnString,
                                        String methodName, Integer state, boolean whetherRefresh) {
        connectSuccessFlag = false;
    }

    private class ShopIntroductionModel{

        /**店铺地址**/
        private String Address;
        /**营业执照图片**/
        private String BusImgUrl;
        /**店铺ID**/
        private int ID;
        /**店铺上方图片**/
        private String ImgUrl;
        /**联系电话**/
        private String PhoneNum;
        /**真实名称**/
        private String RealName;
        /**公司简介**/
        private String Remark;
        /**店铺名称**/
        private String ShopName ;
        /**店铺所属用户ID**/
        private int UserID;

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getAddress() {
            return Address;
        }

        public void setAddress(String address) {
            Address = address;
        }

        public String getBusImgUrl() {
            return BusImgUrl;
        }

        public void setBusImgUrl(String busImgUrl) {
            BusImgUrl = busImgUrl;
        }

        public String getImgUrl() {
            return ImgUrl;
        }

        public void setImgUrl(String imgUrl) {
            ImgUrl = imgUrl;
        }

        public String getPhoneNum() {
            return PhoneNum;
        }

        public void setPhoneNum(String phoneNum) {
            PhoneNum = phoneNum;
        }

        public String getRealName() {
            return RealName;
        }

        public void setRealName(String realName) {
            RealName = realName;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public String getShopName() {
            return ShopName;
        }

        public void setShopName(String shopName) {
            ShopName = shopName;
        }

        public int getUserID() {
            return UserID;
        }

        public void setUserID(int userID) {
            UserID = userID;
        }
    }
}
