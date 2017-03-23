package com.shi.xianglixiangqin.util;

import java.util.ArrayList;
import java.util.List;

import com.shi.xianglixiangqin.activity.MyBaseActivity;

/***
 * activity管理工具类
 * @author SHI
 * 2016-3-3 17:04:01
 */
public class ActivityCollectorUtil {

	public static List<MyBaseActivity> activities = new ArrayList<MyBaseActivity>();
	public static void addActivity(MyBaseActivity activity) {
		activities.add(activity);
	}
	public static void removeActivity(MyBaseActivity activity) {
		activities.remove(activity);
	}
	public static void finishAll() {
		for (MyBaseActivity activity : activities) {
			if (!activity.isFinishing()) {
				activity.IfOpenFinishActivityAnim(false);
				activity.finish();
			}
		}
	}
}
