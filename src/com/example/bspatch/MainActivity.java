package com.example.bspatch;

import java.io.File;
import java.io.FileOutputStream;

import com.bspatch.test.BSPatchDemo;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	private boolean LOGD = true;
	private String tag = "MainActivity";

	private TextView tv_info;
	private String str;
	private Handler handler;
	private static final int REFRESH = 100;
	
	private String old_file = "MainActivity.apk";
	private String new_file = "MainActivity_new.apk";
	private String patch = "11.patch";
	private String sdcard_path;
	private int status = -1;
	
	private static String urlNull = "原文件路径不存在";
	private static String isFile = "原文件不是文件";
	private static String canRead = "原文件不能读";
	// private static String notWrite = "备份文件不能写入";
	private static String message = "OK";
	private static String cFromFile = "创建原文件出错:";
	private static String ctoFile = "创建备份文件出错:";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (LOGD) {
			Log.d(tag, "onCreate()");
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv_info = (TextView) findViewById(R.id.tv_info);
		str = "combing...";
		tv_info.setText(str);
		
		sdcard_path = Environment.getExternalStorageDirectory().getPath();
		old_file = sdcard_path + File.separator + old_file;
		new_file = sdcard_path + File.separator + new_file;
		patch = sdcard_path + File.separator + patch;
		
		handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case REFRESH:
					tv_info.setText(str);
					break;

				default:
					break;
				}
				super.handleMessage(msg);
			}
			
		};
		
		new BSPatchDemoThread().start();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	private class BSPatchDemoThread extends Thread {

		@Override
		public void run() {
			String sourcedir = getApplicationInfo().sourceDir;
			// 获取本地apk的sha1校验码
			String sha1 = DigestUtil.getFileSha1(sourcedir);
			// 版本信息
			String verName = getVerName(getApplicationContext());
			int verCode = getVerCode(getApplicationContext());
			if (LOGD) {
				Log.d(tag, "sha1:" + sha1);
				Log.d(tag, "verCode:" + verCode);
				Log.d(tag, "verName:" + verName);
			}
			if (getVerCode(getApplicationContext()) > 2) {
				// 
				// String msg = copyFile(sourcedir, old_file);
				if (LOGD) {
					Log.d(tag, "sourcedir:" + sourcedir);
					// Log.d(tag, "msg:" + msg);
					Log.d(tag, "sha1:" + sha1);
				}
				status = new BSPatchDemo().combine(sourcedir, new_file, patch);
				if (LOGD) {
					Log.d(tag, "status:" + status);
				}
				handler.sendEmptyMessage(REFRESH);
				if (status == 0) {
					installApk(new_file);
				}
			}
			super.run();
		}
		
	}

	private void installApk(String apk_name){
		Intent intent = new Intent(Intent.ACTION_VIEW); 
		intent.setDataAndType(Uri.fromFile(new File(apk_name)), "application/vnd.android.package-archive"); 
		startActivity(intent);
	}
	
	/**
	 * 
	 * @param fromFile
	 *            旧文件地址和名称
	 * @param toFile
	 *            新文件地址和名称
	 * @return 返回备份文件的信息，ok是成功，其它就是错误
	 */
	public static String copyFile(String fromFileUrl, String toFileUrl) {
		File fromFile = null;
		File toFile = null;
		try {
			fromFile = new File(fromFileUrl);
		} catch (Exception e) {
			return cFromFile + e.getMessage();
		}
		try {
			toFile = new File(toFileUrl);
		} catch (Exception e) {
			return ctoFile + e.getMessage();
		}
		if (!fromFile.exists()) {
			return urlNull;
		}
		if (!fromFile.isFile()) {
			return isFile;
		}
		if (!fromFile.canRead()) {
			return canRead;
		}
		// 复制到的路径如果不存在就创建
		if (!toFile.getParentFile().exists()) {
			toFile.getParentFile().mkdirs();
		}
		if (toFile.exists()) {
			toFile.delete();
		}
//		if (!toFile.canWrite()) {
//			return notWrite;
//		}
		try {
			java.io.FileInputStream fosfrom = new java.io.FileInputStream(
					fromFile);
			java.io.FileOutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c); // 将内容写到新文件当中
			}
			// 关闭数据流
			fosfrom.close();
			fosto.close();
		} catch (Exception e) {
			e.printStackTrace();
			message = "备份失败!";

		}
		return message;
	}
	
 
	public int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager()
					.getPackageInfo("com.example.bspatch", 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(tag, e.getMessage());
		}
		return verCode;
	}

	public String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager()
					.getPackageInfo("com.example.bspatch", 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(tag, e.getMessage());
		}
		return verName;
	}
}
