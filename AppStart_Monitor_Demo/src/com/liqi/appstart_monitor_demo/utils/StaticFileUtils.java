package com.liqi.appstart_monitor_demo.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;

/**
 * 文件操作工具
 * 
 * @author Administrator
 * 
 */
public class StaticFileUtils {
	/**
	 * 把APP里面的资源项目写到本地
	 * 
	 * @param am
	 * @param fileName
	 *            文件名
	 * @param foundFile
	 *            文件路径
	 * @return
	 */
	public static boolean assetsAppOut(AssetManager am, String fileName,
			File foundFile) {
		boolean fileOK = false;
		try {
			InputStream open = am.open(fileName);
			fileOK = putFileOutputStream(open, foundFile, 1024);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return fileOK;
	}

	/**
	 * 调用系统的软件安装
	 * 
	 * @param file
	 */
	public static void installOpen(Activity activity, File file) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		activity.startActivity(intent);
	}

	/**
	 * 根据标识打开一个其它APPA
	 * 
	 * @param comp
	 */
	public static Intent startActivityNew(ComponentName comp) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setComponent(comp);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction("android.intent.action.VIEW");
		System.out.println("<<<开启一个新的APP>>>");
		return intent;
	}

	/**
	 * 根据路径删除指定的目录或文件，无论存在与否
	 * 
	 * @param sPath
	 *            要删除的目录或文件
	 * @return 删除成功返回 true，否则返回 false。
	 */
	public static boolean DeleteFolder(String sPath) {
		boolean flag = false;
		File file = new File(sPath);
		// 判断目录或文件是否存在
		if (!file.exists()) { // 不存在返回 false
			return flag;
		} else {
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				return deleteFile(file);
			} else { // 为目录时调用删除目录方法
				return deleteDirectory(sPath);
			}
		}
	}

	/**
	 * 删除单个文件
	 * 
	 * @param sPath
	 *            被删除文件的文件名
	 * @return 单个文件删除成功返回true，否则返回false
	 */
	private static boolean deleteFile(File file) {
		boolean flag = false;
		// 路径为文件且不为空则进行删除
		if (file.isFile() && file.exists()) {
			file.delete();
			flag = true;
		}
		return flag;
	}

	/**
	 * 删除目录（文件夹）以及目录下的文件
	 * 
	 * @param sPath
	 *            被删除目录的文件路径
	 * @return 目录删除成功返回true，否则返回false
	 */
	private static boolean deleteDirectory(String sPath) {
		// 如果sPath不以文件分隔符结尾，自动添加文件分隔符
		if (!sPath.endsWith(File.separator)) {
			sPath = sPath + File.separator;
		}
		File dirFile = new File(sPath);
		// 如果dir对应的文件不存在，或者不是一个目录，则退出
		if (!dirFile.exists() || !dirFile.isDirectory()) {
			return false;
		}
		boolean flag = true;
		// 删除文件夹下的所有文件(包括子目录)
		File[] files = dirFile.listFiles();
		for (int i = 0; i < files.length; i++) {
			// 删除子文件
			if (files[i].isFile()) {
				flag = deleteFile(files[i]);
				if (!flag)
					break;
			} // 删除子目录
			else {
				flag = deleteDirectory(files[i].getAbsolutePath());
				if (!flag)
					break;
			}
		}
		if (!flag)
			return false;
		// 删除当前目录
		if (dirFile.delete()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取保存路径
	 * 
	 * @param activity
	 * @param name
	 *            路径名字
	 * @return
	 */
	public static String getPath(Context activity, String name) {
		String path = "";
		// 判断是否安装有SD卡
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			path = Environment.getExternalStorageDirectory() + "/" + name;
		} else {
			path = activity.getCacheDir() + "/" + name;
		}
		return path;
	}

	/**
	 * 通过URL获取路径文件所在路径。（原理是切割URL最后的文件名作为保存在本地的文件名）
	 * 
	 * @param url
	 *            URL
	 * @param activity
	 * @param pathName
	 *            保存的文件夹名字。如果本地没有，本方法会为你创建一个
	 * @return
	 */
	public static File putFilePath(String url, Context activity, String pathName) {
		File file = null;
		String[] split = url.split("/");
		String name = split[split.length - 1];
		// String name = "SampleVideo_1080x720_10mb~4.mp4";
		String path = getPath(activity, pathName);
		// 先创建文件夹file
		file = new File(path);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
		file = null;
		file = new File(path, name);
		return file;
	}

	/**
	 * 判断路径是否存在
	 * 
	 * @param file
	 * @return
	 */
	public static boolean isFilePath(File file) {
		boolean isFile = false;
		if (file.exists() && file.isFile()) {
			isFile = true;
		} else {
			isFile = false;
		}
		return isFile;

	}

	/**
	 * 根据传过来的文件目录和文件名来创建路径
	 * 
	 * @param filePath
	 *            目录路径
	 * @param fileName
	 *            文件名称，记得带扩展名 -->如果没有传值，就只返回目录路径File
	 * @return
	 */
	public static File nonentityFoundFile(String filePath, String fileName) {
		// 先创建文件夹file
		File file = new File(filePath);
		if (!file.exists() && !file.isDirectory()) {
			file.mkdirs();
		}
		if (null != fileName && !"".equals(fileName)) {
			file = new File(filePath, fileName);
		}
		return file;
	}

	/**
	 * 通过包名去判断程序是不是已经安装
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static boolean isAppInstalled(Context context, String packageName) {
		final PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
		List<String> pName = new ArrayList<String>();
		if (pinfo != null) {
			for (int i = 0; i < pinfo.size(); i++) {
				String pn = pinfo.get(i).packageName;
				pName.add(pn);
			}
		}
		return pName.contains(packageName);
	}

	/**
	 * 把输入流写出
	 * 
	 * @param inputContent
	 *            输入流
	 * @param file
	 *            输出文件路径
	 * @param byteCache
	 *            byte数组缓存字节大小设置
	 * @return
	 */
	public static boolean putFileOutputStream(InputStream inputContent,
			File file, int byteCache) {
		boolean sign = false;
		if (null != inputContent) {
			FileOutputStream fileOutputStream = null;
			BufferedOutputStream bufferedOutputStream = null;
			try {
				fileOutputStream = new FileOutputStream(file);
				bufferedOutputStream = new BufferedOutputStream(
						fileOutputStream);
				byte buffer[] = new byte[byteCache];
				int length = -1;
				while ((length = inputContent.read(buffer)) != -1) {
					bufferedOutputStream.write(buffer, 0, length);
					bufferedOutputStream.flush();
				}
				if (file.exists())
					sign = true;
				else
					sign = false;
			} catch (Exception e) {
				sign = false;
				e.printStackTrace();
			} finally {
				try {
					bufferedOutputStream.close();
					fileOutputStream.close();
					inputContent.close();
					bufferedOutputStream = null;
					fileOutputStream = null;
					inputContent = null;
				} catch (IOException e) {
					bufferedOutputStream = null;
					fileOutputStream = null;
					inputContent = null;
					e.printStackTrace();
				}
			}
		}
		return sign;
	}

	/**
	 * 把文本内容用ASE加密写到本地
	 * 
	 * @param password
	 *            密码
	 * @param content
	 *            内容
	 * @param file
	 *            路径
	 * @return
	 */
	public static boolean putFileOutputStreamContent(String password,
			String content, File file) {

		boolean sign = false;
		FileOutputStream fileOutputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(file);
			bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
			bufferedOutputStream.write(AESEncryptor.encrypt(password, content)
					.getBytes());
			bufferedOutputStream.flush();
			if (file.exists())
				sign = true;
			else
				sign = false;
		} catch (Exception e) {
			sign = false;
			e.printStackTrace();
		} finally {
			try {
				bufferedOutputStream.close();
				fileOutputStream.close();
				bufferedOutputStream = null;
				fileOutputStream = null;
			} catch (IOException e) {
				bufferedOutputStream = null;
				fileOutputStream = null;
				e.printStackTrace();
			}
		}

		return sign;
	}

	/**
	 * 通过递归得到某一路径下所有的目录及其文件
	 */
	public static List<File> getFiles(String filePath) {
		List<File> fileList = new ArrayList<File>();
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				/*
				 * 递归调用
				 */
				getFiles(file.getAbsolutePath());
			}
			fileList.add(file);
		}
		return fileList;
	}
}
