package com.shuimunianhua.xianglixiangqin.service;

import java.io.File;

import okhttp3.Call;

import android.content.Intent;

import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;
import com.shuimunianhua.xianglixiangqin.util.SystemUtil;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

public class UpdateAppServiceController{

	UpdateAppService mUpdateAppService;
	
	
	public UpdateAppServiceController(UpdateAppService mUpdateAppService) {
		super();
		this.mUpdateAppService = mUpdateAppService;
	}

	public void init(Intent intent) {
		mUpdateAppService.initView(intent);
		beginToUpdateApp(intent);
	}
	
	public void beginToUpdateApp(Intent intent){
		
		//获取安装包下载地址
		final String addressOfApkDownload = intent.getStringExtra(InformationCodeUtil.IntentServiceAddressOfApkDownload);
		//获取安装包名称    并获取下载SD卡位置
		int position = addressOfApkDownload.lastIndexOf("/");
		InformationCodeUtil.NameOfCurrentVersion = addressOfApkDownload.substring(position+1, addressOfApkDownload.length());
		
		//Xutils
//		final String locationForApkDown = SystemUtil.getDownloadFilePath(InformationCodeUtil.NameOfCurrentVersion);
//		HttpUtils httpUtils = new HttpUtils();
//		httpUtils.download(addressOfApkDownload, locationForApkDown, true, new RequestCallBack<File>() {
//			
//			@Override
//			public void onLoading(long total, long current, boolean isUploading) {
//				float progress = current/total;
//				LogUtil.LogShitou("下载进程", "current="+current+",total="+total+"progress="+progress);
//				mUpdateAppService.showLoading(progress);
//			}
//			
//			@Override
//			public void onSuccess(ResponseInfo<File> responseInfo) {
//				LogUtil.LogShitou("下载结果", responseInfo.result.getAbsolutePath());
//				mUpdateAppService.finishRefrushView(locationForApkDown);
//				
//			}
//			@Override
//			public void onFailure(HttpException error, String msg) {
//				if(msg.contains("file has downloaded completely")){
//					mUpdateAppService.finishRefrushView(locationForApkDown);
//				}else{
//					mUpdateAppService.stopSelf();
//				}
//			}
//		});
		
		//OkHttp
		OkHttpUtils.get()
		.url(addressOfApkDownload)
		.build()
		.execute(new FileCallBack(SystemUtil.getDownloadFilePath(), InformationCodeUtil.NameOfCurrentVersion) {
			
			@Override
			public void onResponse(File response) {
//				LogUtil.LogShitou("下载结果", response.getAbsolutePath());
				mUpdateAppService.finishRefrushView(response.getAbsolutePath());			
			}
			
			@Override
			public void onError(Call call, Exception e) {
				String msg = e.getMessage();
//				LogUtil.LogShitou("错误", "错误信息"+msg);
				mUpdateAppService.stopSelf();
			}
			
			@Override
			public void inProgress(long progress, long total) {
//				LogUtil.LogShitou("下载进程", "progress="+progress+",total="+total);
				mUpdateAppService.showLoading(progress,total);	
			}
		});
		
		
	}
}
