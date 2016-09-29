package com.shuimunianhua.xianglixiangqin.util;

public class InformationCodeUtil {

	public static final boolean whetherIsDaiNiFei = false;

	/**最新app 平台版本(带你飞)信息  文件地址**/
	public static final String urlVersionDesc_DaiNiFei = "http://115.28.9.25/AppVersion.js";

	/**最新app 批发版(聚合)版本信息  文件地址**/
	public static final String urlVersionDesc_JvHe = "http://115.28.9.25/AppVersion_JuHe.js";

	/**最新app 批发版(聚合)版本信息  文件地址**/
	public static final String urlVersionDesc_ShuiMuNianHua = "http://115.28.9.25/AppVersion_ShuiMuNianHua.js";

	/**下载到本地的应用名称**/
	public static String NameOfCurrentVersion;

	//分类页面的  信息标记码
	public static final int flagOfAListView = 2;
	public static final int flagOfBListView = 3;
	public static final int flagOfCListView = 4;

	//Activity   跳转时的辅助键值
	public static final String IntentServiceAddressOfApkDownload = "addressOfApkDownload";
	/**Intent传送时的手机号**/
	public static final String IntentPhoneNum = "IntentPhoneNum";
	/**进入普通商品详情界面时的商品ID**/
	public static final String IntentGoodsID = "goodsID";

	/**成功生成订单**/
	public static final int RESULT_BUILD_ORDER_SUCCESS    = 100;
	/**成功付款**/
	public static final int RESULT_PAY_MONEY_SUCCESS    = 101;

	/**微信店铺商品管理**/
	public static final String IntentWXShopGoodsManagerActivityFilterClassID = "currentFilterClassID";
	public static final String IntentWXShopTypeSelectActivityGoodsClassNameType = "GoodsClassNameType";
	public static final String IntentWXSelectGoodsClassName = "SelectGoodsClassTypeName";
	public static final String IntentWXSelectGoodsClassID = "SelectGoodsClassTypeID";
	/**选择的地址**/
	public static final String IntentAddressLocationSelect = "IntentAddressLocationSelect";	
	/** 当前商品活动编号key **/
	public static final String IntentPlatformActionID = "KeyPlatformActionID";
	/** 当前商品活动类型key **/
	public static final String IntentPlatformActionType = "KeyPlatformActionType";
	/** 整点秒杀类型 **/
	public static final int PlatformActionType_MesKill = 1;
	/** 团购中心类型 **/
	public static final int PlatformActionType_GroupCentre = 2;
	/** 限时抢购类型 **/
	public static final int PlatformActionType_SaleByTimeLitmited = 3;

	/**主题街进入主题街详情界面时使用  当前主题名称**/
	public static final String IntentThemeTypeName = "IntentThemeTypeName";
	/**主题街进入主题街详情界面时使用  当前主题ID**/
	public static final String IntentThemeTypeID = "IntentThemeTypeID";

	/**购物车进入提交订单界面**/
	public static final String IntentConfirmOrderGeneralActivityShopCartList = "ShopCartList";
	/**活动商品进入提交订单界面**/
	public static final String IntentConfirmOrderSportActivityPackageModel = "PackageModel";
	//提交普通或者活动商品订单界面进入支付界面
	public static final String IntentPayMethodSelectActivityParamObject = "ParamObject";
	//订单是否生成成功和付款是否成功的结果说明界面
	public static final String IntentPayConfirmResultActivityOperateIfSuccess = "OperateIfSuccess";
	public static final String IntentPayConfirmResultActivityShowMsg = "ShowMsg";

	/**进入翼支付付款界面**/
	public static final String IntentPayMoneyByBankCardActivityCurrentTotalMoney = "CurrentTotalMoney";
	public static final String IntentPayMoneyCurrentTotalOrderIds = "CurrentOrderIds";
	public static final String IntentPayPasswordSetActivityPayPassword = "PayPassword";
	/**付款方式选择界面**/
	public static final String IntentPayBankCardSelectActivityCurrentTotalMoney = "CurrentTotalMoney";
	public static final String IntentPayBankCardSelectActivityCurrentOrderIds = "CurrentOrderIds";
	/**订单列表进入订单详情界面**/
	public static final String IntentMyOrderDetailActivityOrderViewModel = "OrderViewModel";
	/**店铺商品搜索界面**/
	public static final String IntentSearchGoodsFilterClassID= "currentFilterClassID";
	public static final String IntentSearchGoodsCurrentShopID = "KeyCurrentShopID";
	public static final String IntentSearchGoodsInFromBottom = "FromUserDefinePosition";
	/**筛选界面**/
	public static final  String IntentFilterFirstActivityFilterClassID = "SelectedSubPid";
	/**活动商品分类界面**/
	public static final String IntentSportSaleByActivityCurrentShopID = "CurrentShopID";
	/**店铺推荐界面**/
	public static final String IntentShopRecommendActivityCurrentShopID = "CurrentShopID";
	/**套餐选择界面**/
	public static final String IntentGoodsActivityCurrentGoodsModel = "CurrentGoodsModel";
	/**支付输入密码界面**/
	public static final String IntentPayMoneyInputPasswordActivityParamModel = "ParamModel";

	public static final String IntentMainActivityCheckID = "KeyCheckedID";


	public static final String IntentPayMoneyByQDCodeActivityFlag ="Flag";
	//本地存储文件的Key值
	/**是否第一次登录**/
	public static final String KeyFirstOpenApp = "KeyFirstOpenApp";
	/**购物车商品Json数据key**/
	public static final String KeyShoppingCartData = "KeyShoppingCartData";
	/**用户信息Json数据key**/
	public static final String KeyUserInfo = "KeyUserInfo";



	/**强制用户下线的广播过滤器**/
	public static final String ReceiverFilterPressUserLogout = "com.xianglixiangqin.current.PRESS_USERLOGGOT";
	

 	/**手机号正则表达式**/
	public  final static String regExpPhotoNumber = "^[1][0-9]{10}$";
	/**邮箱号正则表达式**/
	public final static String regExpEmailNumber = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";


	//和服务器交互的函数名称
	/**获取我的代理商品数据**/
	public static final String methodNameGetAgentProducts = "GetAgentProducts";
	
	/**获取我的配送地址**/
	public static final String methodNameGetAddrList = "GetAddrList";
	
	/**获取我的订单列表   (已经废弃)**/
	public static final String methodNameGetOrderList = "GetOrderList";
	
	/**获取当前代理商可使用飞币数    (int customID)**/
	public static final String methodNameGetAcceptFlyCoin = "GetAcceptFlyCoin";
	
	/**使用飞币抵扣订单价格      (已经废弃)**/
	public static final String methodNameFlyCoinHandler = "FlyCoinHandler";
	
	/**修改订单状态(单个操作)目前主要是用来确认收货,取消订单 (int customID, string openKey, int orderID,int state)**/
	public static final String methodNameChangeOrderState = "ChangeOrderState";

	/**获取订单里面所有商品详情   (已经废弃)**/
	public static final String methodNameGetOrderItems = "GetOrderItems";

	/**C
	 * 检查用户登录信息是否过期 (仅带你飞平台使用此接口)
	 * (int customID, string openKey)
	 * **/
	public static final String methodNameCheckSign = "CheckSign";

	/**C 监测用户登陆信息是否过期(  仅聚合批发系统使用此接口，可以检测用户是否被锁定)
	 * (int customID, int enterShopID, string openKey)
	 * **/
	public static  final String methodNameCheckDealerLock = "CheckDealerLock";

	/**用户登录带你飞平台**/
	public static final String methodNameLogin = "Login";

	/**用户登录聚合批发系统 (string phoneNum, string passWord, string code) **/
	public static final String methodNameDealerLoginExt = "DealerLoginExt";

	/**获取平台版首页数据  (已经废弃)**/
	public static final String methodNameGetHomeList = "GetHomeList";
	

	/**一键代理 某个店铺的所有商品或部分商品  (int customID, string openKey, int shopID, string goodsIDS)**/
	public static final String methodNameDelegateShopAllGoods = "DelegateShopAllGoods";
	
	/**获取某个经销商店铺所有商品的分类信息  (int shopUserID, int parentID)**/
	public static final String methodNameGetShopGoodsClasses = "GetShopGoodsClasses";
	
	/**获取经销商商品详情**/
	public static final String methodNameGetShopInfo = "GetShopInfo";
	
	/**G
	 * 批量改价
	 * (int customID, string openKey, string Ids, string AgentAddMoneyStr, 
	 * string ParentIDBackMoney1Str, string ParentIDBackMoney2Str, 
	 * string ParentIDBackMoney3Str)
	 **/
	public static final String methodNameBatchUpdatePrice = "BatchUpdatePrice";
	
	/**取 一级分类过滤 列表**/
	public static final String methodNameGetClassFilterList = "GetClassFilterList";
	
	/**取 二级分类过滤 列表**/
	public static final String methodNameGetSecondFilterList = "GetSecondFilterList";
	
	/**获取 我的 收藏 列表    (暂未使用)**/
	public static final String methodNameGetFavorList = "GetFavorList";
	
	/**根据商品编号获取 普通商品 详情**/
	public static final String methodNameGetGoodsItem = "GetGoodsItem";
	
	/**检查 当前商品是否被 收藏**/
	public static final String methodNameCheckFavor = "CheckFavor";
	
	/**
	 * 取 商品套餐 列表
	 *(int goodsID,int customID = 0)
	 * **/
	public static final String methodNameGetGoodsPackageList = "GetGoodsPackageList";
	
	/**取 套餐颜色 列表    (int packageID, int customID = 0) (已经废弃)**/
//	public static final String methodNameGetPackageColorList = "GetPackageColorList";
	
	/**添加商品到我的代理**/
	public static final String methodNameAddDelegate = "AddDelegate";
	
	/**获取当前代理商代理商品的分类信息**/
	public static final String methodNameGetCurrentAgentGoodsClasses ="GetCurrentAgentGoodsClasses" ;
	
	/**获取短信验证码  (string phoneNum)**/
	public static final String methodNameGetSMSCode = "GetSMSCode";
	
	/**修改密码  (string phoneNum, string newPassWord) **/
	public static final String methodNameChangePwdExt = "ChangePwdExt" ;
	
	/**删除收货地址  (int customID, string openKey, int addrID)**/
	public static final String methodNameDeleteAddr = "DeleteAddr";
	
	/**修改收货地址  (int customID, string openKey, string addrJson)**/
	public static final String methodNameEditAddr = "EditAddr";
	
	/**增加收货地址  (int customID, string openKey, string addrJson)**/
	public static final String methodNameAddAddr = "AddAddr";
	
	/**获取省级分类  ()**/
	public static final String methodNameGetProvinceList = "GetProvinceList";
	
	/**获取市级分类  (string provinceCode)**/
	public static final String methodNameGetCityList = "GetCityList";
	
	/**获取区域分类  (string cityCode)**/
	public static final String methodNameGetAreaList = "GetAreaList";
	
	
	/**获取活动商品详情    (int PlatformActionID, int PlatformActionType)**/
	public static final String methodNameGetSingleActProduct = "GetSingleActProduct";
	
	/**获取用户的签约银行    (int customID,string openKey)**/
	public static final String methodNameCheckESign = "CheckESign";
	
	/**C 和银行签约    (int customID,string openKey, string bankCode
	 * , string bankName, string accountCode, string bankCardName
	 * , string certNo, string areaCode,string pinCode(交易密码))**/
	public static final String methodNameESign = "ESign";
	
	/**银行卡解约    (int customID, string openKey, string signId, string accountCode)**/
	public static final String methodNameCancelESign = "CancelESign";
	
	/**C 代扣    (int customID(客户编号), string openKey, string signId(签约Id)
	 * , string accountCode(签约银行卡号), string orderIds(多个逗号分隔), string amount(扣款(单位:分))
	 * ,string pinCode(交易密码))**/
	public static final String methodNameSignAgentReceiveToAccount = "SignAgentReceiveToAccount";
	
	/**这个接口目前的功能主要是为了把账期订单状态批量改成待发货    (int customID, string openKey, string orderIds)**/
	public static final String methodNameChangeOrderStateExt = "ChangeOrderStateExt";
	
	/**C 获取已开通城市站的省份   (int customID, string openKey)**/
	public static final String methodNameGetSiteProvince = "GetSiteProvince";
	
	/**C 根据省份编码获取已开通城市站    (int customID, string openKey, string ProvinceCode)**/
	public static final String methodNameGetOpenSites = "GetOpenSites";
	
	/**G 取 带你飞平台所有的商品分类 列表 (城市站) (int parentID, String siteCode(城市站编号))**/
	public static final String methodNameGetSiteClassList = "GetSiteClassList";

	/**G 取 带你飞平台所有的商品分类 列表 (int parentID)    (已经废弃)**/
//	public static final String methodNameGetSiteClassList = "GetClassList";

	/**G 获取新版首页内容    (int customID(登录用户id), int secKillCount(秒杀条数), int groupPurchaseCount(团购条数)
	 * , int timePurchaseCount(限时购条数), int advCount(头部广告条数), 
	 * ,int hotShopCount(好店推荐条数), int newGoodsCount(新品推荐条数), string cityCode=""(城市编号))**/
	public static final String methodNameHomeIndex = "HomeIndex";
	
	/**G 获取店铺商品分类信息 列表(int pageSize(页大小), int pageIndex(第几页), int classID(分类编号,-1取全部), string filterIDS(过滤编号)
	 * ,int shopID, string keyWord(关键字), int orderIndex(排序方式:0默认/1销售优先/2价格)
	 * , int customID=0(默认为0), string cityCode=""(城市编码))**/
	public static final String methodNameGetGoodsList = "GetGoodsList";
	
	/**C 注册账号  (string phoneNum (手机号), string email (邮箱), string passWord (密码)
	 * , int roleID=3 (2经销商/3代理商), string cityCode="" (城市编号), int code=0 (邀请码) 
	 * , string shopName="" (店铺名称), string realName="" (真实姓名)**/
	public static final String methodNameAddCustom = "AddCustom";
	
	/**C
	 * 根据id获取用户信息
	 * (string UserId)
	 */
	public static final String methodNameGetUserInfoByID = "GetUserInfoByID";
	
	/**C
	 * 修改用户信息
	 * (int customID(客户编号), string openKey(交互唯一key)
	 * , string realName(用户真实名), string shopName(店铺名)
	 * , string headStr(头像base64字符串))
	 */
	public static final String methodNameUpdateUserInfo = "UpdateUserInfo" ;
	
	/**G
	 * 批量设置微商场商品是否为特价商品
	 * (int customID, string openKey, string Ids, int IfTj(0是))
	 */
	public static final String methodNameBatchSetTjProducts = "BatchSetTjProducts";
	
	/**C
	 * 设置微店推荐栏目
	 * (int customID, string openKey, int firClassID(推荐栏目1), int secClassID(推荐栏目2))
	 */
	public static final String methodNameSetEcShopRecClass = "SetEcShopRecClass";

	/**C
	 * 获取微店推荐栏目
	 * (int customID, string openKey)
	 */
	public static final String methodNameGetEcShopRecClass = "GetEcShopRecClass";

	/**G
	 * 取消某个已经代理的商品
	 * (int customID, string openKey, int goodsID)
	 */
	public static final String methodNameCancelGoodAgent = "CancelGoodAgent";

	/**C
	 * 把单个商品加入购物车
	 * (int customID, string openKey, int ProductID(商品id)
	 * , int PackageID(套餐id), int PackageColorID(套餐颜色id), int Quantity(购买量))
	 */
	public static final String methodNameAddShopCart = "AddShopCart";

	/**C
	 * 批量把商品加入购物车
	 * (int customID, string openKey, string shopCartWappers(商品套餐集合))
	 */
	public static final String methodNameBatchAddShopCart = "BatchAddShopCart";
	/**C
	 * 加载购物车
	 * (int customID, string openKey)
	 */
	public static final String methodNameLoadShopCart = "LoadShopCart";

	/**C
	 * 变更套餐商品数量
	 * (int customID, string openKey, int ID(购物id), int Quantity(购买量))
	 */
	public static final String methodNameChangeProductNum = "ChangeProductNum";

	/**C
	 * 购物车移除套餐商品
	 * (int customID, string openKey, string Ids(购物id,字符串多个可用","号分割))
	 */
	public static final String methodNameBatchDelShopCart = "BatchDelShopCart";

	/**旧版的添加订单 (不扣除飞币)(已经废弃)   (int customID, String openKey, String orderJson, String itemsJson, int PlatformActionID, int PlatformActionType, string remark)**/
//	public static final String methodNameAddOrder = "AddOrder";

	/**新版的添加订单 (自动扣除飞币)(目前活动商品还在使用此接口,普通商品不再使用此接口,改用CreateOrder)
	 *  (int customID, String openKey
	 *  , String orderJson, String itemsJson
	 *  , int PlatformActionID, int PlatformActionType
	 *  , string remark)
	 **/
	public static final String methodNameAddOrderExt = "AddOrderExt";

	/**C
	 * 生成普通订单（走在线支付的会变成待支付，走信用支付的会变成待发货）
	 * (int customID, string openKey
	 * , string postShoppingCarts(这个对象的Json字符串), string address(收货地址)
	 * , string realName(收货人), string phoneNum(收货人电话), payType(支付方式 0:在线支付，1信用支付))
	 */
	public static final String methodNameCreateOrder = "CreateOrder";

	/**C
	 * 获取订单列表
	 *(int customID(客户编号), string openKey(识别码)
	 * , int pageSize(页大小), int pageIndex(页码,1.2.3.4...)
	 * , int orderState(0(全部)/1(代支付)/3(代发货)/4(待收货)/16(信用支付订单)/32(待支付-在线支付)/64(待支付-信用支付)))
	 */
	public static final String methodNameGetOrders = "GetOrders";

	/**获取店铺主页数据
	 * (int shopID(门店编号),int userID(用户编号),int advReadCount(图片广告)
	 * ,int newGoodsReadCount(新品推荐),int priceGoodsReadCount(特价商品)
	 * ,int allGoodsReadCount(全部商品), String keyWord(全部商品 关键字)， int customeID(登陆用户ID))
	 * **/
	public static final String methodNameGetStoreList = "GetStoreList";

	/**G
	 * 获取店铺主页数据
	 * (int shopID(门店编号))
	 * **/
	public static final String methodNameStoreHomeIndex = "StoreHomeIndex";

	/**获取当前店铺的资源店铺(资源店铺的特价商品仅用于展示，不做二次点击)
	 * (int customID, int enterShopID(当前店铺编号))
	 * **/
	public static final String methodNameGetAttentionShops = "GetAttentionShops";


	/**G
	 * 获取某个特定店铺的活动商品列表
	 * (int pageSize(页大小), int pageIndex(页索引), int classID(分类编号,-1取全部)
	 * , int platformActionType(1:秒杀2:团购3:限时购),string cityCode(城市编码), int shopID=0(店铺ID,0获取平台活动商品))
	 * **/
	public static final String methodNameGetGoodsActList = "GetGoodsActList";

	/**G 获取平台活动商品分类  (int platformActionType(1秒杀,2团购,3限时购), string cityCode=""(城市编码))**/
	public static final String methodNameGetActGoodsClass = "GetActGoodsClass";

	/**G
	 *	获取某个特定店铺的所有商品分类(int shopID)
	 * **/
	public static final String methodNameGetUserCategories = "GetUserCategories";

	/**G
	 *	获取某个特定用户代理的所有商品的分类(int customID)
	 * **/
	public static final String methodNameAgentCategories = "AgentCategories";

	/**C
	 *	获取用户e支付交易卡信息(msg中返回json集合，Tags中返回实际支付金额)
	 *	(int customID, string openKey, string orderIds
	 * **/
	public static final String methodNameGetEPayInfoByUserID = "GetEPayInfoByUserID";

	/**C
	 *	短信支付验证码申请
	 *	(int customID, string openKey, string bankCardCode(银行卡号)
	 *	, string bankCardName(银行卡持卡人姓名), string bankCode(银行编码)
	 *	, string areaCode(银行区域编码), string mobile(预留手机号), string certNo(身份证号))
	 * **/
	public static final String methodNameSmsPayApply = "SmsPayApply";

	/**C
	 *	e支付交易密码设置
	 *	(int customID, string openKey, string ePayPinCode)
	 * **/
	public static final String methodNameSetEPayPinCode = "SetEPayPinCode";

	/**C
	 *	翼支付进行支付
	 *(int customID, string openKey
	 * , string bankCardCode(银行账号), string bankCardName(银行账户名)
	 * , string bankCode(银行编码), string areaCode(银行区域编码)
	 * , string mobile(预留手机号), string certNo(身份证号)
	 * , string orderIds(订单id字符串，多个用逗号分隔)
	 * , string pinCode(支付密码), string smsCode(短信验证码))
	 * **/
	public static final String methodNameSmsPay = "SmsPay";

	/**G
	 * (int shopID)
	 * **/
	public static final String methodNameStoreHomeIndexExt = "StoreHomeIndexExt";

	/**C  支付宝,微信支付二维码生成
	 * (int customID, string openKey
	 * , string orderTag, int payMethod(付款方式,0是支付宝，1是微信))
	 * **/
	public static final String methodNameSaoMaHandler = "SaoMaHandler";

	/**C  扫码支付处理结果
	 * (int customID, string openKey, int orderId)
	 * **/
	public static final String methodNameGetSaoMaPayResult = "GetSaoMaPayResult";

}










