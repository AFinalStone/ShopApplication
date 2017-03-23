package com.shi.xianglixiangqin.activity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.frament.MainGoodsClassTypeFragment_DaiNiFei;
import com.shi.xianglixiangqin.frament.MainGoodsClassTypeFragment_JvHe;
import com.shi.xianglixiangqin.frament.MainHomeFragment_DaiNiFei;
import com.shi.xianglixiangqin.frament.MainHomeFragment_JvHe;
import com.shi.xianglixiangqin.frament.MainMessageFragment;
import com.shi.xianglixiangqin.frament.MainPersonalCenterFragment;
import com.shi.xianglixiangqin.frament.MainShoppingCartFragment;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.model.CustomModel;
import com.shi.xianglixiangqin.receiver.JPushReceiver;
import com.shi.xianglixiangqin.receiver.PressLogoutReceiver;
import com.shi.xianglixiangqin.rongyun.CustomizeMessageItemProvider;
import com.shi.xianglixiangqin.rongyun.MyConnectionStatusListener;
import com.shi.xianglixiangqin.rongyun.MyPrivateConversationProvider;
import com.shi.xianglixiangqin.rongyun.RCDTestMessage;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceSyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;

import org.ksoap2.serialization.SoapObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ErrorCode;
import io.rong.imlib.model.Conversation.ConversationType;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/***
 * @author SHI
 * @action 主界面
 * @date 2016年5月6日 11:01:42
 */
public class MainActivity extends MyBaseActivity implements
        RongIM.UserInfoProvider, OnClickListener, OnConnectServerStateListener {
    /**
     * 首页Fragment
     **/
    private MainHomeFragment_DaiNiFei mainHomeFragment_DaiNiFei;
    private MainHomeFragment_JvHe mainHomeFragment_jvHe;
    /**
     * 商品分类Fragment
     **/
    private MainGoodsClassTypeFragment_DaiNiFei mainGoodsClassTypeFragment_DaiNiFei;
    private MainGoodsClassTypeFragment_JvHe mainGoodsClassTypeFragment_JvHe;
    private Bundle bundleMainGoodsClassTypeFragment;
    /**
     * 购物车Fragment
     **/
    private MainShoppingCartFragment mainShoppingCartFragment;

    /**
     * 聊天消息Fragment
     **/
    private MainMessageFragment mainMessageFragment;
    /**
     * 个人中心Fragment
     **/
    private MainPersonalCenterFragment mainPersonalCenterFragment;

    /**
     * 用于对Fragment进行管理
     */
    private FragmentManager fragmentManager;

    /**
     * 首页
     **/
    @BindView(R.id.linearLayout_home)
    public LinearLayout linearLayout_home;
    /**
     * 首页图标
     **/
    @BindView(R.id.iv_home)
    public ImageView iv_home;
    /**
     * 首页文字
     **/
    @BindView(R.id.tv_home)
    public TextView tv_home;
    /**
     * 分类
     **/
    @BindView(R.id.linearLayout_class)
    public LinearLayout linearLayout_class;
    /**
     * 分类图标
     **/
    @BindView(R.id.iv_class)
    public ImageView iv_class;
    /**
     * 分类文字
     **/
    @BindView(R.id.tv_class)
    public TextView tv_class;
    /**
     * 购物车
     **/
    @BindView(R.id.linearLayout_shopcart)
    public LinearLayout linearLayout_shopcart;
    /**
     * 购物车图标
     **/
    @BindView(R.id.iv_shopcart)
    public ImageView iv_shopcart;
    /**
     * 购物车文字
     **/
    @BindView(R.id.tv_shopcart)
    public TextView tv_shopcart;
    /**
     * 消息
     **/
    @BindView(R.id.linearLayout_message)
    public LinearLayout linearLayout_message;
    /**
     * 消息图标
     **/
    @BindView(R.id.iv_message)
    public ImageView iv_message;
    /**
     * 消息文字
     **/
    @BindView(R.id.tv_message)
    public TextView tv_message;
    /**
     * 个人中心
     **/
    @BindView(R.id.linearLayout_person)
    public LinearLayout linearLayout_person;
    /**
     * 个人中心图标
     **/
    @BindView(R.id.iv_person)
    public ImageView iv_person;
    /**
     * 个人中心文字
     **/
    @BindView(R.id.tv_person)
    public TextView tv_person;
    /**
     * 当前聊天未读消息数量
     **/
    @BindView(R.id.tv_messageNumber)
    public TextView tv_messageNumber;

    private PressLogoutReceiver pressLogoutReceiver;

    private IntentFilter intentFilter;

    private JPushReceiver jPushReceiver;

    /**
     * 当前店铺对象
     **/
//    public int currentShopID = -1;
//    private int originalShopID;

    @Override
    public void initView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setReturnStatus(true);
        fragmentManager = getSupportFragmentManager();
        linearLayout_home.setOnClickListener(this);
        linearLayout_class.setOnClickListener(this);
        linearLayout_shopcart.setOnClickListener(this);
        linearLayout_message.setOnClickListener(this);
        linearLayout_person.setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int currentShopID = intent.getIntExtra(InformationCodeUtil.IntentShopRecommendActivityCurrentShopID, -1);
        if (currentShopID == -1) {
            currentShopID = MyApplication.getmCustomModel(mContext).getLoginShopID();
        }
        MyApplication.getmCustomModel(mContext).setCurrentBrowsingShopID(currentShopID);
        int viewId = intent.getIntExtra(InformationCodeUtil.IntentMainActivityCheckID, R.id.linearLayout_home);
        showViewByViewId(viewId);
    }

    @Override
    public void initData() {
        int currentShopID =  MyApplication.getmCustomModel(mContext).getLoginShopID();
        MyApplication.getmCustomModel(mContext).setCurrentBrowsingShopID(currentShopID);
        showViewByViewId(R.id.linearLayout_home);
        if (intentFilter == null) {
            pressLogoutReceiver = new PressLogoutReceiver();
            intentFilter = new IntentFilter(InformationCodeUtil.ReceiverFilterPressUserLogout);
            registerReceiver(pressLogoutReceiver, intentFilter);
            registerJPushReceiver();
            getData(InformationCodeUtil.methodNameSPCount);
        }
        connectRongYun(MyApplication.getmCustomModel(mContext).getRongCloudToken());
        JPushInterface.init(this);
    }

    public void registerJPushReceiver() {
        jPushReceiver = new JPushReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(JPushReceiver.MESSAGE_RECEIVED_ACTION);
        registerReceiver(jPushReceiver, filter);
    }


    @Override
    protected void onResume() {

        RongIM.getInstance()
                .getRongIMClient()
                .getUnreadCount(
                        new RongIMClient.ResultCallback<Integer>() {

                            @Override
                            public void onSuccess(Integer count) {
                                LogUtil.LogShitou("当前未读消息数量",
                                        "onSuccess:" + count);
                                if (count > 0) {
                                    tv_messageNumber
                                            .setVisibility(View.VISIBLE);
                                    tv_messageNumber
                                            .setText(new StringBuffer()
                                                    .append(count)
                                                    .toString());
                                } else {
                                    tv_messageNumber
                                            .setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void onError(ErrorCode code) {
                                LogUtil.LogShitou("当前消息数量",
                                        "onError:" + code);

                            }
                        }, ConversationType.PRIVATE);
        isForeground = true;
        JPushInterface.onResume(mContext);
        super.onResume();
    }

    //极光推送相关数据
    public static boolean isForeground = false;
    @Override
    protected void onPause() {
        isForeground = false;
        JPushInterface.onPause(mContext);
        super.onPause();
    }

    /**
     * 根据ID切换展示页面
     **/
    public void showViewByViewId(int viewId) {
        switch (viewId) {
            case R.id.linearLayout_home:
                linearLayout_home.callOnClick();
                break;
            case R.id.linearLayout_class:
                linearLayout_class.callOnClick();
                break;
            case R.id.linearLayout_shopcart:
                linearLayout_shopcart.callOnClick();
                break;
            case R.id.linearLayout_message:
                linearLayout_message.callOnClick();
                break;
            case R.id.linearLayout_person:
                linearLayout_person.callOnClick();
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int black = getResources().getColor(R.color.colorBlack_FF999999);
        int red = getResources().getColor(R.color.redTitleBarBackground);
        iv_home.setImageResource(R.drawable.radiobutton_main_home_unselected);
        tv_home.setTextColor(black);
        iv_class.setImageResource(R.drawable.radiobutton_main_class_unselected);
        tv_class.setTextColor(black);
        iv_shopcart
                .setImageResource(R.drawable.radiobutton_main_shopcart_unselected);
        tv_shopcart.setTextColor(black);
        iv_message
                .setImageResource(R.drawable.radiobutton_main_message_unselected);
        tv_message.setTextColor(black);
        iv_person
                .setImageResource(R.drawable.radiobutton_main_person_unselected);
        tv_person.setTextColor(black);
        hideFragments(fragmentManager.beginTransaction());
        switch (v.getId()) {
            case R.id.linearLayout_home:
                iv_home.setImageResource(R.drawable.radiobutton_main_home_selected);
                tv_home.setTextColor(red);
                showHomeView();
                break;
            case R.id.linearLayout_class:
                iv_class.setImageResource(R.drawable.radiobutton_main_class_selected);
                tv_class.setTextColor(red);
                showClassView();
                break;
            case R.id.linearLayout_shopcart:
                iv_shopcart
                        .setImageResource(R.drawable.radiobutton_main_shopcart_selected);
                tv_shopcart.setTextColor(red);
                showShopCartView();
                break;
            case R.id.linearLayout_message:
                iv_message
                        .setImageResource(R.drawable.radiobutton_main_message_selected);
                tv_message.setTextColor(red);
                showMessageView();
                break;
            case R.id.linearLayout_person:
                iv_person.setImageResource(R.drawable.radiobutton_main_person_selected);
                tv_person.setTextColor(red);
                showPersonCenterView();
                break;
            default:
                break;
        }
    }

    /**
     * 将所有的Fragment都置为隐藏状态。
     *
     * @param transaction 用于对Fragment执行操作的事务
     */
    private void hideFragments(FragmentTransaction transaction) {

        if (mainHomeFragment_DaiNiFei != null) {
            transaction.hide(mainHomeFragment_DaiNiFei);
        }

        if (mainHomeFragment_jvHe != null) {
            transaction.hide(mainHomeFragment_jvHe);
        }

        if (mainGoodsClassTypeFragment_DaiNiFei != null) {
            transaction.hide(mainGoodsClassTypeFragment_DaiNiFei);
        }

        if (mainGoodsClassTypeFragment_JvHe != null) {
            transaction.hide(mainGoodsClassTypeFragment_JvHe);
        }
        if (mainShoppingCartFragment != null) {
            transaction.hide(mainShoppingCartFragment);
        }
        if (mainMessageFragment != null) {
            transaction.hide(mainMessageFragment);
        }
        if (mainPersonalCenterFragment != null) {
            transaction.hide(mainPersonalCenterFragment);
        }
        transaction.commit();
    }

    private void showHomeView() {
        FragmentTransaction transaction = fragmentManager
                .beginTransaction();
        if (InformationCodeUtil.AppName_DaiNiFei.equals(getResources().getText(R.string.app_name))) {
            if (mainHomeFragment_DaiNiFei == null) {
                mainHomeFragment_DaiNiFei = new MainHomeFragment_DaiNiFei();
                transaction.add(R.id.frameLayout, mainHomeFragment_DaiNiFei, "mainHomeFragment");
            } else {
                transaction.show(mainHomeFragment_DaiNiFei);
            }

        } else {
            if (mainHomeFragment_jvHe == null) {
                mainHomeFragment_jvHe = new MainHomeFragment_JvHe();
                transaction.add(R.id.frameLayout, mainHomeFragment_jvHe, "mainHomeFragment");
            } else {
                transaction.show(mainHomeFragment_jvHe);
                if(MyApplication.getmCustomModel(mContext).getCurrentBrowsingShopID() != MyApplication.getmCustomModel(mContext).getLoginShopID()){
                    mainHomeFragment_jvHe.initData();
                }
            }
        }
        transaction.commit();
    }

    private void showClassView() {
        FragmentTransaction transaction = fragmentManager
                .beginTransaction();

        if (InformationCodeUtil.AppName_DaiNiFei.equals(getResources().getText(R.string.app_name))) {
            if (mainGoodsClassTypeFragment_DaiNiFei == null) {
                mainGoodsClassTypeFragment_DaiNiFei = new MainGoodsClassTypeFragment_DaiNiFei();
                transaction.add(R.id.frameLayout, mainGoodsClassTypeFragment_DaiNiFei, "mainGoodsClassTypeFragment");
            } else {
                transaction.show(mainGoodsClassTypeFragment_DaiNiFei);
            }
        } else {
            if (mainGoodsClassTypeFragment_JvHe == null) {
                mainGoodsClassTypeFragment_JvHe = new MainGoodsClassTypeFragment_JvHe();
                transaction.add(R.id.frameLayout, mainGoodsClassTypeFragment_JvHe, "mainGoodsClassTypeFragment");
            } else {
                transaction.show(mainGoodsClassTypeFragment_JvHe);
            }
        }
        transaction.commit();

    }

    private void showShopCartView() {
        FragmentTransaction transaction = fragmentManager
                .beginTransaction();
        if (mainShoppingCartFragment == null) {
            mainShoppingCartFragment = new MainShoppingCartFragment();
            transaction.add(R.id.frameLayout, mainShoppingCartFragment, "mainShoppingCartFragment");
        } else {
            transaction.show(mainShoppingCartFragment);
        }
        transaction.commit();

    }

    private void showMessageView() {
        FragmentTransaction transaction = fragmentManager
                .beginTransaction();
        if (mainMessageFragment == null) {
            mainMessageFragment = new MainMessageFragment();
            transaction.add(R.id.frameLayout, mainMessageFragment, "mainMessageFragment");
        } else {
            transaction.show(mainMessageFragment);
        }
        transaction.commit();

//		if (mainMessageFragment == null) {
//			mainMessageFragment = new MainMessageFragment();
//		}
//		FragmentTransaction fragmentTransaction = fragmentManager
//				.beginTransaction();
//		fragmentTransaction.replace(R.id.frameLayout, mainMessageFragment,"mainMessageFragment");
//		fragmentTransaction.commit();
    }

    private void showPersonCenterView() {
        FragmentTransaction transaction = fragmentManager
                .beginTransaction();
        if (mainPersonalCenterFragment == null) {
            mainPersonalCenterFragment = new MainPersonalCenterFragment();
            transaction.add(R.id.frameLayout, mainPersonalCenterFragment, "mainPersonalCenterFragment");
        } else {
            transaction.show(mainPersonalCenterFragment);
        }
        transaction.commit();

//		if (mainPersonalCenterFragment == null) {
//			mainPersonalCenterFragment = new MainPersonalCenterFragment();
//		}
//		FragmentTransaction fragmentTransaction = fragmentManager
//				.beginTransaction();
//		fragmentTransaction.replace(R.id.frameLayout, mainPersonalCenterFragment,"mainPersonalCenterFragment");
//		fragmentTransaction.commit();
    }


    /**
     * 融云连接
     **/
    private void connectRongYun(String token) {
        String currentProcessName = ((MyApplication) getApplication())
                .getCurProcessName(getApplicationContext());

        if (getApplicationInfo().packageName.equals(currentProcessName)) {

            RongIM.registerMessageType(RCDTestMessage.class);
            RongIM.getInstance().registerConversationTemplate(new MyPrivateConversationProvider());
            RongIM.getInstance().registerMessageTemplate(new CustomizeMessageItemProvider());
            /**
             * IMKit SDK调用第二步,建立与服务器的连接
             */
            RongIM.connect(token, new RongIMClient.ConnectCallback() {

                /**
                 * Token 错误，在线上环境下主要是因为 Token 已经过期，您需要向 App Server 重新请求一个新的
                 * Token
                 */
                @Override
                public void onTokenIncorrect() {
                    LogUtil.LogShitou("Activity", "--Token 已经过期");
                }

                /**
                 * 连接融云成功
                 *
                 * @param userid
                 *            当前 token
                 */
                @Override
                public void onSuccess(String userid) {
                    LogUtil.LogShitou("MainActivity", "融云连接成功");
                    if (RongIM.getInstance() != null) {


                        // 设置用户信息提供者。
                        RongIM.setUserInfoProvider(MainActivity.this, true);
                        //设置融云当前状态监听者
                        RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new MyConnectionStatusListener(mContext));
                        // 设置收到新消息时的监听者，然后显示当前消息数量
                        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageListener() {

                            @Override
                            public boolean onReceived(Message arg0, int arg1) {
                                RongIM.getInstance()
                                        .getRongIMClient()
                                        .getUnreadCount(
                                                new RongIMClient.ResultCallback<Integer>() {

                                                    @Override
                                                    public void onSuccess(
                                                            Integer count) {
                                                        LogUtil.LogShitou(
                                                                "当前未读消息数量",
                                                                "onSuccess:"
                                                                        + count);
                                                        if (count > 0) {
                                                            tv_messageNumber
                                                                    .setVisibility(View.VISIBLE);
                                                            tv_messageNumber
                                                                    .setText(new StringBuffer()
                                                                            .append(count)
                                                                            .toString());
                                                        } else {
                                                            tv_messageNumber
                                                                    .setVisibility(View.INVISIBLE);
                                                        }
                                                    }

                                                    @Override
                                                    public void onError(
                                                            ErrorCode code) {
                                                        LogUtil.LogShitou(
                                                                "当前消息数量",
                                                                "onError:"
                                                                        + code);

                                                    }
                                                }, ConversationType.PRIVATE);
                                return false;
                            }
                        });
                    }

                }

                /**
                 * 连接融云失败
                 *
                 * @param errorCode
                 *            错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(ErrorCode errorCode) {
                    LogUtil.LogShitou("MainActivity", "连接融云失败");
                    Log.d("LoginActivity", "--连接融云失败" + errorCode);
                }
            });
        }
    }

    @Override
    public UserInfo getUserInfo(String userId) {

        SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, InformationCodeUtil.methodNameGetUserInfoByID);
        requestSoapObject.addProperty("UserId", userId);

        ConnectServiceSyncTask connectCustomServiceSyncTask =
                new ConnectServiceSyncTask(mContext, requestSoapObject
                        , InformationCodeUtil.methodNameGetUserInfoByID);

        String strUserInfo = connectCustomServiceSyncTask.connectCustomService();
//		LogUtil.LogShitou("获取    "+userId+"   用户信息被执行:"+strUserInfo);
        UserInfo userInfo = null;
        try {
            Gson gson = new Gson();
            CustomModel customModel = gson.fromJson(strUserInfo, CustomModel.class);
            String DjLsh = new StringBuffer().append(customModel.getDjLsh()).toString();
            String realName = customModel.getRealName();
            if (TextUtils.isEmpty(realName)) {
                realName = customModel.getShopName();
            }

            String ImageUrl;
            if (TextUtils.isEmpty(customModel.getImgUrl())) {
                ImageUrl = "http://rongcloud-web.qiniudn.com/docs_demo_rongcloud_logo.png";
            } else {
                ImageUrl = customModel.getImgUrl();
            }
            userInfo = new UserInfo(DjLsh, realName, Uri.parse(ImageUrl));
        } catch (Exception e) {
            userInfo = null;
        }
        return userInfo;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (intentFilter != null) {
            unregisterReceiver(pressLogoutReceiver);
            intentFilter = null;
        }

    }

    public void getData(String methodName ) {

        if(InformationCodeUtil.methodNameSPCount.equals(methodName)){
            SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
            requestSoapObject.addProperty("spid", -1);
            requestSoapObject.addProperty("dpid", MyApplication.getmCustomModel(mContext).getLoginShopID());
            requestSoapObject.addProperty("fuid", MyApplication.getmCustomModel(mContext).getDjLsh());
            requestSoapObject.addProperty("ftool", 2);
            ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask
                    (mContext, this, requestSoapObject, methodName);
            connectCustomServiceAsyncTask.initProgressDialog(false);
            connectCustomServiceAsyncTask.execute();
            return;
        }

    }

    @Override
    public void connectServiceSuccessful(String returnString, String methodName, Object state, boolean whetherRefresh) {
        LogUtil.LogShitou("埋点操作", returnString);
    }

    @Override
    public void connectServiceFailed(String returnStrError, String methodName, Object state, boolean whetherRefresh) {

    }

    @Override
    public void connectServiceCancelled(String returnString, String methodName, Object state, boolean whetherRefresh) {

    }

}