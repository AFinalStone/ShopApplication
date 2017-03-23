package com.shi.xianglixiangqin.activity;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import butterknife.ButterKnife;
import me.iwf.photopicker.PhotoPicker;

import com.afinalstone.androidstudy.view.ShapedImageView;
import com.shi.xianglixiangqin.R;
import com.shi.xianglixiangqin.app.MyApplication;
import com.shi.xianglixiangqin.interfaceImpl.OnConnectServerStateListener;
import com.shi.xianglixiangqin.model.CustomModel;
import com.shi.xianglixiangqin.task.ConnectCustomServiceAsyncTask;
import com.shi.xianglixiangqin.task.ConnectServiceUtil;
import com.shi.xianglixiangqin.util.ImagerLoaderUtil;
import com.shi.xianglixiangqin.util.InformationCodeUtil;
import com.shi.xianglixiangqin.util.LogUtil;
import com.shi.xianglixiangqin.util.ImgToBase64Util;
import com.shi.xianglixiangqin.util.ToastUtil;
import com.shi.xianglixiangqin.view.FragmentViewDialog;
import com.shi.xianglixiangqin.view.FragmentViewDialog.OnButtonClickListener;
import com.shi.xianglixiangqin.view.SelectPicturePopWindow;

/**
 * 完善个人信息
 * @author SHI
 * 2016年5月3日 17:41:28
 */
public class CompletePersonalInformationActivity extends MyBaseActivity implements OnClickListener, OnConnectServerStateListener<Integer> {
	/**左侧返回控件**/
	@BindView(R.id.iv_titleLeft)
	 ImageView iv_titleLeft;
	/**标题**/
	@BindView(R.id.tv_title)
	 TextView tv_title;
//GetUserInfoByIDResponse{GetUserInfoByIDResult={"DjLsh":841,"Email":"602392033@qq.com","ImgUrl":"http:\/\/wcf.dainif.com\/UploadFiles\/User\/20160127\/507215811042.jpg","Integral":0.000,"LocCityCode":null,"LocProCode":null,"LocSiteName":null,"OpenKey":"515699653009","PassWord":"","PhoneNum":"18211673059","RealName":"石要磊","RoleID":3,"RongCloudToken":"\/4GxIhFE4t7ehK12lbqiZ7LfFd646bAyQwVEmibNS\/fuv3zZknoq9y9GA+f\/t0nM0hxdXxndIy6XCbB5l8IpuA==","Sex":"男士","ShopID":855,"ShopName":"18211673059的店铺","ShopUserID":0,"UserName":"18211673059","WeChatImgUrl":""}; }

	/**我的头像外围控件**/
	@BindView(R.id.linearLayout_myHeaderImage)
	 LinearLayout linearLayout_myHeaderImage;
	/**我的头像图片**/
	@BindView(R.id.shapedImageView)
	 ShapedImageView shapedImageView;
	
	/**我的真实姓名外围控件**/
	@BindView(R.id.linearLayout_realName)
	 LinearLayout linearLayout_realName;
	/**我的真实姓名**/
	@BindView(R.id.tv_realName)
	 TextView tv_realName;
	
	/**我的店铺名称外围控件**/
	@BindView(R.id.linearLayout_changeShopName)
	 LinearLayout linearLayout_changeShopName;
	/**我的店铺名称**/
	@BindView(R.id.tv_shopName)
	 TextView tv_shopName;
	
	/**当前登录用户对象**/
	 CustomModel mCustomModel;
	 String strRealName;
	 String strShopName;
	 String strHeadImage;
	
	 final String titleChangeRealName = "修改真实姓名";
	 final String titleChangeShopName = "修改店铺名称";
	
	 SelectPicturePopWindow pop;
	 View view;
	/**
	 * 选择图片的返回码
	 */
	public final static int SELECT_CAMERA_RESULT_CODE = 100;
	/**
	 * 当前选择的图片的路径
	 */
	public String mImagePath;
	
	@Override
	public void initView() {
		view = View.inflate(mContext, R.layout.activity_complete_personalinfo, null);
		setContentView(view);
		ButterKnife.bind(this);
		
		tv_title.setText(R.string.completePersonalInfoTitle);
		iv_titleLeft.setVisibility(View.VISIBLE);
		iv_titleLeft.setOnClickListener(this);
		linearLayout_myHeaderImage.setOnClickListener(this);
		linearLayout_realName.setOnClickListener(this);
		linearLayout_changeShopName.setOnClickListener(this);
	}

	@Override
	public void initData() {
		mCustomModel = MyApplication.getmCustomModel(mContext);
		ImagerLoaderUtil.getInstance(mContext).displayMyImage(mCustomModel.getImgUrl(), shapedImageView, R.drawable.iv_personal_my_header);
		tv_realName.setText(mCustomModel.getRealName());
		tv_shopName.setText(mCustomModel.getShopName());
		String ImageUrl;
		if (TextUtils.isEmpty(mCustomModel.getImgUrl())) {
			ImageUrl = "android.resource://"+getPackageName()+"/"+R.drawable.iv_personal_my_header;
		} else {
			ImageUrl = mCustomModel.getImgUrl();
		}
		RongIM.getInstance().refreshUserInfoCache(new UserInfo(mCustomModel.getDjLsh()+"", mCustomModel.getRealName(), Uri.parse(ImageUrl)));		
		
	}

		
	 void getData(String methodName) {
		if(InformationCodeUtil.methodNameUpdateUserInfo.equals(methodName)){
			
			SoapObject requestSoapObject = new SoapObject(ConnectServiceUtil.NAMESPACE, methodName);
			requestSoapObject.addPropertyIfValue("customID", mCustomModel.getDjLsh());
			requestSoapObject.addPropertyIfValue("openKey", mCustomModel.getOpenKey());
			requestSoapObject.addPropertyIfValue("realName", strRealName);
			requestSoapObject.addPropertyIfValue("shopName", strShopName);
			requestSoapObject.addPropertyIfValue("headStr", strHeadImage);
			ConnectCustomServiceAsyncTask connectCustomServiceAsyncTask = new ConnectCustomServiceAsyncTask(
					mContext, this, requestSoapObject , methodName);
			connectCustomServiceAsyncTask.setConnectOutTime(30000);
			connectCustomServiceAsyncTask.execute();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_titleLeft:
			 finish();
			break;
		case R.id.linearLayout_myHeaderImage:
			showPicturePopupWindow();
			break;
		case R.id.linearLayout_realName:
			showChangeInfoDialog(titleChangeRealName,tv_realName.getText().toString());
			break;
		case R.id.linearLayout_changeShopName:
			showChangeInfoDialog(titleChangeShopName, tv_shopName.getText().toString());
			break;
		default:
			break;
		}
	}

	/**修改用户信息对话框**/
	 void showChangeInfoDialog(final String title, String message) {

		View view = View.inflate(mContext, R.layout.dialog_userdefine_view, null);
		TextView tv_title = (TextView)view.findViewById(R.id.tv_title);
		final EditText et_message = (EditText)view.findViewById(R.id.et_message);
		tv_title.setText(title);
		et_message.setText(message);
		FragmentViewDialog fragmentViewDialog = new FragmentViewDialog();

		 fragmentViewDialog.initView(view, "取消", "修改",  new OnButtonClickListener() {

			@Override
			public void OnOkClick() {
				if(titleChangeRealName.equals(title)){
					strHeadImage = null;
					strShopName = tv_shopName.getText().toString();
					strRealName = et_message.getText().toString();
					getData(InformationCodeUtil.methodNameUpdateUserInfo);
					return;
				}
				if(titleChangeShopName.equals(title)){
					strHeadImage = null;
					strRealName = tv_realName.getText().toString();
					strShopName = et_message.getText().toString();
					getData(InformationCodeUtil.methodNameUpdateUserInfo);
					return;
				}
			}

			@Override
			public void OnCancelClick() {

			}
		});

		fragmentViewDialog.show(getSupportFragmentManager(), "fragmentDailog");
	}
	
	/**
	 * 拍照或从图库选择图片(PopupWindow形式)
	 */
	public void showPicturePopupWindow(){
		pop = new SelectPicturePopWindow(this, new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// 隐藏弹出窗口
				pop.dismiss();
				switch (v.getId()) {
				case R.id.takePhotoBtn:// 拍照
					takePhoto();
					break;
				case R.id.pickPhotoBtn:// 相册选择图片
					pickPhoto();
					break;
				case R.id.cancelBtn:// 取消
					break;
				default:
					break;
				}
			}
		});  
		pop.showAtLocation(view, Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
	}
	
	/**
	 * 拍照获取图片
	 */
	 void takePhoto() {
		// 执行拍照前，应该先判断SD卡是否存在
		String SDState = Environment.getExternalStorageState();
		if (SDState.equals(Environment.MEDIA_MOUNTED)) {
			/**
			 * 通过指定图片存储路径，解决部分机型onActivityResult回调 data返回为null的情况
			 */
			//获取与应用相关联的路径
			String imageFilePath = getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA); 
			//根据当前时间生成图片的名称
			String timestamp = "/"+formatter.format(new Date())+".png"; 
			File imageFile = new File(imageFilePath,timestamp);// 通过路径创建保存文件
			mImagePath = imageFile.getAbsolutePath();
			Uri imageFileUri = Uri.fromFile(imageFile);// 获取文件的Uri
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);// 告诉相机拍摄完毕输出图片到指定的Uri
			startActivityForResult(intent, SELECT_CAMERA_RESULT_CODE);
		} else {
			Toast.makeText(this, "内存卡不存在!", Toast.LENGTH_LONG).show();
		}
	}
	/***
	 * 从相册中取图片
	 */
	 void pickPhoto() {
		 PhotoPicker.builder()
				 .setPhotoCount(1)
				 .setShowCamera(true)
				 .setShowGif(true)
				 .setPreviewEnabled(false)
				 .start(this, PhotoPicker.REQUEST_CODE);

	 }
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		LogUtil.LogShitou("头像上传");
		//从照相机获取
		if(requestCode == SELECT_CAMERA_RESULT_CODE && resultCode == RESULT_OK){
			String imagePath = "";
			if(data != null && data.getData() != null){//有数据返回直接使用返回的图片地址
				imagePath = getFilePathByFileUri(this, data.getData( ));
			}else{//无数据使用指定的图片路径
				imagePath = mImagePath;
			}
			Log.e("图片路径", imagePath);
//			ToastUtil.show(mContext, "图片路径"+imagePath,Toast.LENGTH_LONG);
			try {
				File file = new File(imagePath);
				if(file.exists() && file.isFile()){
					long size = new File(imagePath).length()/1024;
					Log.e("图片大小", size+"");
//					ToastUtil.show(mContext, size+"",Toast.LENGTH_LONG);
					strHeadImage = ImgToBase64Util.imgToBase64(mContext, imagePath, ".png", 100);
					strShopName = tv_shopName.getText().toString();
					strRealName = tv_realName.getText().toString();
					getData(InformationCodeUtil.methodNameUpdateUserInfo);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		//选择相册
		}else if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
			if (data != null) {
				ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
				long size = new File(photos.get(0)).length()/1024;
				Log.e("图片大小", size+"");
				File file = new File(photos.get(0));
				strHeadImage = ImgToBase64Util.imgToBase64(mContext, photos.get(0), ".png", 100);
				strShopName = tv_shopName.getText().toString();
				strRealName = tv_realName.getText().toString();
				getData(InformationCodeUtil.methodNameUpdateUserInfo);
			}
		}
	}
	
	/**
	 * 根据文件Uri获取路径
	 */
	public static String getFilePathByFileUri(Context context, Uri uri) {
		String filePath = null;
		Cursor cursor = context.getContentResolver().query(uri, null, null,
				null, null);
		if (cursor.moveToFirst()) {
			filePath = cursor.getString(cursor
					.getColumnIndex(MediaStore.Images.Media.DATA));
		}
		cursor.close();
		return filePath;
	}
//	"Email":"602392033@qq.com","ImgUrl":"http:\/\/wcf.dainif.com\/UploadFiles\/HeadImg\/20160511\/516307450969.Jpeg","Integral":0.000,"LocCityCode":"330100","LocProCode":"330000","LocSiteName":"杭州站","OpenKey":"516307201474","PassWord":"","PhoneNum":"18211673059","RealName":"要磊","RoleID":3,"RongCloudToken":"\/4GxIhFE4t7ehK12lbqiZ7LfFd646bAyQwVEmibNS\/fuv3zZknoq9y9GA+f\/t0nM0hxdXxndIy6XCbB5l8IpuA==","Sex":"男士","ShopID":855,"ShopName":"夏河雨","ShopUserID":0,"UserName":"18211673059","WeChatImgUrl":null}; }

	@Override
	public void connectServiceSuccessful(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
//		LogUtil.LogShitou("returnSoapObject", returnString);
//		String  result = returnSoapObject.getPropertyAsString(methodName+"Result");
		LogUtil.LogShitou("修改后的用户信息", returnString);
		Gson gson = new Gson();
		try {
			JSONObject jsonObject = new JSONObject(returnString);
			int sign = jsonObject.getInt("Sign");
			if(sign == 1){
				CustomModel mCustomModel= gson.fromJson( jsonObject.getString("Msg"), CustomModel.class);
				LogUtil.LogShitou("对象数据", mCustomModel.toString());
				mCustomModel.setLoginShopID(MyApplication.getmCustomModel(mContext).getLoginShopID());
				mCustomModel.setCurrentBrowsingShopID(MyApplication.getmCustomModel(mContext).getLoginShopID());
//				PreferencesUtilMy.saveCustomModel(mContext, mCustomModel);
				MyApplication.setmCustomModel(mContext, mCustomModel);
				initData();
			}else{
				ToastUtil.show(mContext, jsonObject.getString("Msg"));
			}
		} catch (Exception e) {
			ToastUtil.show(mContext, "数据异常");
			e.printStackTrace();
		}
	}

	@Override
	public void connectServiceFailed(String returnStrError, String methodName, Integer state,
			boolean whetherRefresh) {
		ToastUtil.show(mContext, returnStrError);
	}

	@Override
	public void connectServiceCancelled(String returnString,
			String methodName, Integer state, boolean whetherRefresh) {
		
	}

}
