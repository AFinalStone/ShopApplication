package com.shuimunianhua.xianglixiangqin.util;

import java.io.File;
import java.util.ArrayList;

import android.os.Environment;
import android.text.TextUtils;

/**
 * 目录管理类
 * 
 * @author ZHU
 * 
 */
public class FileUtil {
	private static File sdDir;

	/** SD卡是否存在 **/
	public static boolean whetherExistSD() {
		// 判断sd卡是否存在
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
			return true;
		}
		return false;
	}

	public static File getSdDir() {
		return sdDir;
	}

	/*****
	 * 返回文件路径
	 * 
	 * @param FolderName
	 * @param fileName
	 * @return
	 */
	public static String getFilePath(String FolderName, String fileName) {
		String downDirectory = null;
		if (whetherExistSD()) {
			//判断文件夹是否为空
			if (TextUtils.isEmpty(FolderName)) {
				FolderName = "";
			}
			//创建文件夹
			if(createFolder(FolderName)){
				downDirectory = sdDir.toString() + File.separator + FolderName
						+ File.separator + fileName;
			}
		}
		return downDirectory;
	}
	
	public static String getDownFilePath(String fileName) {
		String downDirectory = null;
		String FolderName = "Download";
		if (whetherExistSD()) {
			//创建文件夹
			if(createFolder(FolderName)){
				downDirectory = sdDir.toString() + File.separator + FolderName
						+ File.separator + fileName;
			}
		}
		return downDirectory;
	}

	/*****
	 * 创建文件夹
	 * @param FolderName 文件夹名称
	 * @return
	 */
	public static boolean createFolder(String FolderName) {
		
		if (whetherExistSD()) {
			File file = new File(sdDir.toString() + File.separator + FolderName);
			if (!file.exists()) {// 目录不存在
				file.mkdirs();
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 删除目录
	 * 
	 * @param file
	 *            目录路径
	 */
	public static boolean removeDirectory(File file) {
		if (file.isDirectory()) {
			File[] filelist = file.listFiles();
			for (int i = 0; i < filelist.length; i++) {
				removeDirectory(filelist[i]);
			}
			if (!file.delete()) {
				return false;
			}
		} else {
			if (!file.delete()) {
				return false;
			}
		}
		return true;
	}

	/** 列出src中的所有文件，返回一个List **/
	static ArrayList<File> filename = new ArrayList<File>();

	public static ArrayList<File> getAllFiles(File root) {
		File files[] = root.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory()) {
					getAllFiles(f);
				} else {
					System.out.println(f);
					filename.add(f);
				}
			}
		}
		return filename;
	}

	public static long getFileSize(){
		return 0;
	}
	
}
