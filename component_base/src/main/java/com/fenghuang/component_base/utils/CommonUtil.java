package com.fenghuang.component_base.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * 公共工具类
 * @author Angus
 * @company 9zhitx.com
 */
public class CommonUtil {

	private static long lastClickTime;
	/**
	 * 按钮是否短时间内重复点击
	 * @return
	 */
	public static boolean isFastDoubleClick() {   
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;   
		if ( 0 < timeD && timeD < 500) {
			return true;      
		}      
		lastClickTime = time;      
		return false;      
	}

	/**
	 * checkPermissions
	 *
	 * @param context
	 * @param permission
	 * @return true or false
	 */
	public static boolean checkPermissions(Context context, String permission) {
		if (context == null || permission.equals("") || permission.equals("")) {
			return false;
		}
		PackageManager pm = context.getPackageManager();
		return pm.checkPermission(permission, context.getPackageName())
				== PackageManager.PERMISSION_GRANTED;
	}

	/**
	 * 按钮是否短时间内重复点击
	 * @return
	 */
	public static boolean isFastDoubleClick(long timeMill) {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if ( 0 < timeD && timeD < timeMill) {
			return true;
		}
		lastClickTime = time;
		return false;
	}

	private static long viewId;
	public static boolean isFastDoubleClick(int viewId) {
		if (CommonUtil.viewId == viewId) {
			return isFastDoubleClick();
		} else {
			CommonUtil.viewId = viewId;
			return false;
		}
	}

	public static boolean isFastDoubleClick(int viewId,long timeMill) {
		if (CommonUtil.viewId == viewId) {
			return isFastDoubleClick(timeMill);
		} else {
			CommonUtil.viewId = viewId;
			return false;
		}
	}
	
	/**
	 * 拼接参数url
	 * @param baseUrl
	 * @param paramsMap
	 * @return
	 */
	public static String getParamsUrl(String baseUrl, Map<String, String> paramsMap){
		if (paramsMap != null && !paramsMap.isEmpty()) {
			StringBuffer buf = new StringBuffer(baseUrl == null ? "" : baseUrl);
			for (Map.Entry<String, String> entry : paramsMap.entrySet()) {
				if (buf.indexOf("?") > 0 ) {
					buf.append("&");
				}
				else {
					buf.append("?");
				}
				buf.append(entry.getKey()).append("=").append(entry.getValue());
			}
			baseUrl = buf.toString();
		}
		
		return baseUrl;
	}

	/**
	 * 剔除掉url中指定的参数
	 * @param url
	 * @param params
     * @return
     */
	public static String removeParams(String url, String... params) {
		try {
			String reg = null;
			for (int i = 0; i < params.length; i++) {
				reg = "(?<=[\\?&])" + params[i] + "=[^&]*&?";
				url = url.replaceAll(reg, "");
			}
			url = url.replaceAll("&+$", "");
		} catch (Exception e) {

		}
		return url;
	}

	/**
	 * 九宫格图，取缩略图用1
	 * @param imageUrl
	 * @param size
     * @return
     */
	public static String getThumbImageUrlForGrid(String imageUrl, int size) {
		return getThumbImageUrl(imageUrl, size, size, 1);
	}
	/**
	 * 获取缩略图， 针对七牛
	 * @param imageUrl
	 * @param size
	 * @return
	 */
	public static String getThumbImageUrl(String imageUrl, int size) {
		return getThumbImageUrl(imageUrl, size, size);
	}

	public static String getThumbImageUrl(String imageUrl, int width, int height) {
		return getThumbImageUrl(imageUrl, width, height, 2);
	}

	public static String getThumbImageUrl(String imageUrl, int width, int height, int model) {
		StringBuffer buf = new StringBuffer(imageUrl == null ? "" : imageUrl);
		if (buf.indexOf("?") > 0 ) {
			buf.append("&");
		} else {
			buf.append("?");
		}
		buf.append("imageView2/").append(model);
		buf.append("/w/").append(width);
		buf.append("/h/").append(height);
		//buf.append("/interlace/1");// interlace 渐进显示
		buf.append("/ignore-error/1"); // 处理的结果失败，则返回原图

		imageUrl = buf.toString();
		return imageUrl;

		//buf.append("imageMogr2/auto-orient/thumbnail/100x100");
	}



	/**
	 * 获取字符串长度，中英文区分
	 * @param string
	 * @return
	 */
	public static int getStringLength(String string){
		if (string!=null) {
			int length = string.length();
			if (length==2){
				int charLength=0;
				for (int i=0;i<length;i++){
					char c = string.charAt(i);
					if (isChineseByChar(c)){
						charLength+=2;
					}else{
						charLength+=1;
					}
				}
				return charLength;
			}else{
				return length;
			}
		}
		return 0;
	}

	// 根据Unicode编码完美的判断中文汉字和符号
	private static boolean isChineseByChar(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION) {
			return true;
		}
		return false;
	}

//	/**
//	 * 获取sd卡总共内存
//	 * @return
//	 */
//	public static long getEXTTotalSize() {
//		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//			File sdcardDir = Environment.getExternalStorageDirectory();
//			return getTotalSize(sdcardDir.getAbsolutePath());
//		}
//		return 0;
//	}
//
//	/**
//	 * 获取sd卡剩余内存
//	 * @return
//     */
//	public static long getEXTSizeLeft() {
//		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//			File sdcardDir = Environment.getExternalStorageDirectory();
//			return getAvailableSize(sdcardDir.getAbsolutePath());
//		}
//		return 0;
//	}
//
//	private static long getAvailableSize(String dir) {
//		StatFs stat = new StatFs(dir);
//		long blockSize = stat.getBlockSize();
//		long availableBlocks = stat.getAvailableBlocks();
//		return (blockSize * availableBlocks);
//	}
//
//	private static long getTotalSize(String dir) {
//		StatFs stat = new StatFs(dir);
//		long blockSize = stat.getBlockSize();
//		long totalBlocks = stat.getBlockCount();
//		long size = (blockSize * totalBlocks);
//		return size;
//	}

	/**
	 * install apk
	 * @param
	 */
	public static void installApk(Context context, String filePath) {
		if (TextUtils.isEmpty(filePath)) return;
		File apkfile = new File(filePath);
		if (!apkfile.exists()) {
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(apkfile),
				"application/vnd.android.package-archive");
		context.startActivity(intent);
	}

	public String streamToString(InputStream is) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = is.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.close();
			is.close();
			byte[] byteArray = baos.toByteArray();
			return new String(byteArray);
		} catch (Exception e) {
			return null;
		}
	}
	
}
