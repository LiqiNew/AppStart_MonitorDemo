package com.liqi.appstart_monitor_demo.obj;

import java.io.File;

import android.app.Activity;
import android.content.res.AssetManager;

import com.liqi.appstart_monitor_demo.acitivity.TestMainActivity;
import com.liqi.appstart_monitor_demo.utils.StaticFileUtils;

public class RunnableObj implements Runnable {
	// 辅助配置文件名(必须是此文件名)
	private final String TEXT_NAME = "appStart.txt";
	// 辅助配置文件路径(必须是此文件路径)
	private final String TEXT_NAMEPATH = "AppStart/Monitor/Pag";

	// 要启动的辅助APP包名(必须是此包名)
	public static final String OPENAPPPAG = "com.liqi.appstart";
	// 要打开的辅助APP页面(必须是此跳转页面名)
	public static final String OPENAPPVIEW = "com.liqi.appstart.acitivity.MainActivity";

	// 辅助APP解锁数据密码，必须是此密码才可以和辅助APP交互
	private final String PASSWORD = "Liqi543945827";
	// app存储的目录
	private String appPath;
	private AssetManager am;
	private Activity activity;
	// app名字(必须是此APP名字)
	private final String APPNAME = "AppStart_Monitor.apk";

	public RunnableObj(Activity activity) {
		am = activity.getResources().getAssets();
		appPath = StaticFileUtils.getPath(activity, "AppStart/Monitor");
		this.activity = activity;
	}

	@Override
	public void run() {
		File foundFile = StaticFileUtils.nonentityFoundFile(appPath, APPNAME);
		// 安装包存在
		if (StaticFileUtils.isFilePath(foundFile)) {
			// 没有安装
			if (!StaticFileUtils.isAppInstalled(activity, OPENAPPPAG)) {
				StaticFileUtils.installOpen(activity, foundFile);
				// 如果此辅助APP重新安装了，就删除掉它以前的配置文件
				StaticFileUtils.DeleteFolder(StaticFileUtils.getPath(activity,
						TEXT_NAMEPATH + "/" + TEXT_NAME));
			}
		} else {
			if (StaticFileUtils.assetsAppOut(am, APPNAME, foundFile)) {
				StaticFileUtils.installOpen(activity, foundFile);
				// 如果此辅助APP重新安装了，就删除掉它以前的配置文件
				StaticFileUtils.DeleteFolder(StaticFileUtils.getPath(activity,
						TEXT_NAMEPATH + "/" + TEXT_NAME));
			}
		}
		foundFile = StaticFileUtils.nonentityFoundFile(
				StaticFileUtils.getPath(activity, TEXT_NAMEPATH), TEXT_NAME);

		// 如果此辅助APP没有存在配置文件，就写入它的配置文件
		if (!foundFile.exists())
			// 写入辅助APPP需要的信息
			StaticFileUtils.putFileOutputStreamContent(
					PASSWORD,
					activity.getPackageName() + "-"
							+ TestMainActivity.class.getName(), foundFile);
	}

}
