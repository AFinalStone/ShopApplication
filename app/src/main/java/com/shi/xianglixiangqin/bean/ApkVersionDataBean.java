package com.shi.xianglixiangqin.bean;

/**
 * Created by SHI on 2017/4/1 14:05
 */

public class ApkVersionDataBean {


    /**
     * apkMd5 : D083E87808CD0385662F37F8F9A3AF88
     * apkUrl : http://imtt.dd.qq.com/16891/D083E87808CD0385662F37F8F9A3AF88.apk?fsname=com.xianglixiangqin.xianglixiangqin_1.6.8_15.apk&hsr=4d5s
     * desc : 12.37MB
     * newFeature : 1.增加积分商城模块，积分送送送，商品免费拿。2.新增组合商品，组合购买更实惠。3.新增飞币，积分流水模块
     * packageName : com.xianglixiangqin.xianglixiangqin
     * publishTime : 2017-03-29 04:52:23
     * versionCode : 15
     * versionName : 1.6.8
     */

    private String apkMd5;
    private String apkUrl;
    private String newFeature;
    private String packageName;
    private String publishTime;
    private int versionCode;
    private String versionName;

    private FileSize fileSize;


    public void getApkInfo() {

    }

    public String getApkMd5() {
        return apkMd5;
    }

    public void setApkMd5(String apkMd5) {
        this.apkMd5 = apkMd5;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public void setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
    }

    public String getNewFeature() {
        return newFeature;
    }

    public void setNewFeature(String newFeature) {
        this.newFeature = newFeature;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public FileSize getFileSize() {
        return fileSize;
    }

    public void setFileSize(FileSize fileSize) {
        this.fileSize = fileSize;
    }

//  {"appId":  开始位置
    //  ,"isNew"   结束位置
    //  {"appId":42377589,"packageName":"com.xianglixiangqin.xianglixiangqin","appName":"乡里乡亲","categoryId":122,"categoryName":"购物","authorId":4366090,"author":"杭州巨合网络科技有限公司","downCount":{"bytes":84,"desc":"1,000次"},"rating":{"ratingDistribution":{"1":0,"2":0,"3":0,"4":0,"5":1},"averageRating":5.0,"ratingCount":1},"apkId":65374087,"minSdkVersion":{"apiLevel":16,"verDesc":"1.6"},"iconUrl":"http://shp.qpic.cn/ma_icon/0/icon_42377589_1490598506/96","signatureMd5":"9F656F7B8E8A7A45AC373EE22112E813","newFeature":"1.增加积分商城模块，积分送送送，商品免费拿。\r\n2.新增组合商品，组合购买更实惠。\r\n3.新增飞币，积分流水模块","description":"乡里乡亲是为IT及手机行业量身打造的一个批发商城，旨在为传统批发商解决报价不及时、订单接受的低效率、产品信息沟通不顺畅、催款不及时等老问题，彻底解放业务员的时间及手脚，去做业务真正该做得事——拓展业务、拓展业务、拓展业务！","versionCode":15,"versionName":"1.6.8","apkUrl":"http://imtt.dd.qq.com/16891/D083E87808CD0385662F37F8F9A3AF88.apk?fsname=com.xianglixiangqin.xianglixiangqin_1.6.8_15.apk&hsr=4d5s","lang":"中文","showType":0,"fileSize":{"bytes":12968630,"desc":"12.37MB"},"apkMd5":"D083E87808CD0385662F37F8F9A3AF88","publishTime":"2017-03-29 04:52:23","flag":{"value":153,"userSafe":0,"virus":1,"adver":1,"offical":2},"apkDownCount":{"bytes":84,"desc":"1,000次"},"apkRating":{"ratingDistribution":{"1":0,"2":0,"3":0,"4":0,"5":1},"averageRating":5.0,"ratingCount":1},"permissions":null,"snapshotsUrl":["http://pp.myapp.com/ma_pic2/0/shot_42377589_1_1490598504/550","http://pp.myapp.com/ma_pic2/0/shot_42377589_2_1490598504/550","http://pp.myapp.com/ma_pic2/0/shot_42377589_3_1490598504/550","http://pp.myapp.com/ma_pic2/0/shot_42377589_4_1490598504/550","http://pp.myapp.com/ma_pic2/0/shot_42377589_5_1490598504/550"],"channel":"","commentCount":0}

    public class FileSize {
        public String desc;

    }
}



