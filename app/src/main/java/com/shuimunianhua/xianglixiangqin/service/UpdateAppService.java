package com.shuimunianhua.xianglixiangqin.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.shuimunianhua.xianglixiangqin.R;
import com.shuimunianhua.xianglixiangqin.util.InformationCodeUtil;

/***
 * 更新下載服务
 * @author ZHU
 */
public class UpdateAppService extends Service {
	//和下载过程状态栏的显示有关
//	private RemoteViews contentView;
//	private NotificationManager notiManage;
//	private Notification note;
	/**设备上下文**/
	private Context mContext;
	
    private int Notification_ID = 110;
	private PendingIntent pd;
	private int AppLogoResID;
	
	UpdateAppServiceController updateAppServiceControllerImp;
    
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(mContext == null){
			mContext = this;
			if(InformationCodeUtil.whetherIsDaiNiFei){
				AppLogoResID = R.mipmap.ic_launcher_dainifei;
			}else{
				AppLogoResID = R.mipmap.ic_launcher_jvhe;
			}
			updateAppServiceControllerImp = new UpdateAppServiceController(this);
			updateAppServiceControllerImp.init(intent);
		}
		return super.onStartCommand(intent, flags, startId);
	}

	
	public void initView(Intent intent) {

		NotificationManager notiManage=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
			
		Notification note=new Notification();

			note.icon = AppLogoResID;

			note.tickerText = "软件更新包下载";

			note.flags=Notification.FLAG_AUTO_CANCEL;

			RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.layout_progress);

			contentView.setImageViewResource(R.id.notificationImage, AppLogoResID);
			contentView.setProgressBar(R.id.notificationProgress, 100, 0, false);

			note.contentView = contentView;

			notiManage.notify(Notification_ID, note);
	}
	
	/***
	 * 显示下载进度
	 * @param currentSize
	 */
	public void showLoading(long currentSize, long totalSize) {
		int progress = (int) (currentSize*100/totalSize);
		
		NotificationManager notiManage=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification note=new Notification();

		note.icon = AppLogoResID;
		
		note.tickerText = "软件更新包下载";

		note.flags=Notification.FLAG_AUTO_CANCEL;

		RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.layout_progress);

		contentView.setImageViewResource(R.id.notificationImage, AppLogoResID);
		contentView.setProgressBar(R.id.notificationProgress, 100, progress, false); 
		contentView.setTextViewText(R.id.notificationPercent, "已下载"+progress+"%"); 	

		note.contentView = contentView;
		notiManage.notify(Notification_ID, note);
	}
	
	/***
	 * 显示错误信息
	 * @param msg
	 */
	public void showFialedMsg(String msg){
		
	}
	
	/***
	 * 下载成功
	 */
	public void finishRefrushView(String locationForApkDown){
		
		NotificationManager notiManage=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		
		Notification note=new Notification();

		note.icon = AppLogoResID;

		note.tickerText = "软件更新包下载";

		note.flags=Notification.FLAG_AUTO_CANCEL;

		RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.layout_progress);

		contentView.setImageViewResource(R.id.notificationImage, AppLogoResID);
		contentView.setProgressBar(R.id.notificationProgress, 100, 100, false);
		contentView.setTextViewText(R.id.notificationPercent, "已下载完成");

		note.contentView = contentView;
		
		//点击安装
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.parse("file://" + locationForApkDown),"application/vnd.android.package-archive");
//		intent.setDataAndType(Uri.fromFile(responseInfo.result), "application/vnd.android.package-archive");

		pd = PendingIntent.getActivity(mContext, 0, intent, 0);//这个非要不可。
		
		note.contentIntent = pd;
		
		notiManage.notify(Notification_ID, note);
		startActivity(intent);	
		stopSelf();
  	}
	
	
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onDestroy() {
		NotificationManager notiManage=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		notiManage.cancel(Notification_ID);
		super.onDestroy();
	}

}
