package com.voisd.sun.wxapi;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.util.Calendar;
import java.util.Date;

public class PhotoUtil {

	public static String rootDir = Environment.getExternalStorageDirectory()
			+ File.separator + "baibu_camera" + File.separator;

	public static String fileName = "";

	// 调用系统照相机的方法
	public static File camera(Activity ac) {
		/*
		 * Intent it = new Intent("android.media.action.IMAGE_CAPTURE");
		 * startActivityForResult(it, Activity.DEFAULT_KEYS_DIALER);
		 */
		File f = null;
		String name = "";
		String status = Environment.getExternalStorageState();
		if (status.equals(Environment.MEDIA_MOUNTED)) {
			try {
				name = callTime();
				File dir = new File(rootDir);
				if (!dir.exists())
					dir.mkdirs();

				Intent intent = new Intent(
						MediaStore.ACTION_IMAGE_CAPTURE);
				f = new File(dir, name + ".jpeg");// localTempImgDir和localTempImageFileName是自己定义的名字
				Uri u = Uri.fromFile(f);
				intent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, u);
				fileName = f.getAbsolutePath();
				ac.startActivityForResult(intent, 2);
			} catch (ActivityNotFoundException e) {
				// TODO Auto-generated catch block
				Toast.makeText(ac, "没有找到储存目录", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(ac, "没有储存卡", Toast.LENGTH_SHORT).show();
		}
		return f;
	}

	public static String callTime() {

		long backTime = new Date().getTime();

		Calendar cal = Calendar.getInstance();

		cal.setTime(new Date(backTime));

		int year = cal.get(Calendar.YEAR);

		int month = cal.get(Calendar.MONTH) + 1;

		int date = cal.get(Calendar.DAY_OF_MONTH);

		int hour = cal.get(Calendar.HOUR_OF_DAY);

		int minute = cal.get(Calendar.MINUTE);

		int second = cal.get(Calendar.SECOND);

		String time = "" + year + month + date + hour + minute + second;

		return time;

	}
}
