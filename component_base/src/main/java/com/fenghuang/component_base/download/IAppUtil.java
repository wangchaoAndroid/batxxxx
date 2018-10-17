package com.fenghuang.component_base.download;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.PowerManager;
import android.os.SystemClock;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.fenghuang.component_base.net.ILog;
import com.fenghuang.component_base.utils.ContextManager;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

import static android.content.Context.POWER_SERVICE;

/**
 * app 工具类
 * @author Angus
 * @company 9zhitx.com
 */
public class IAppUtil {

    private static final String TAG = "IAppUtil";

    /**
	 * 获取应用程序包名
	 * @param context
	 * @return
	 */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
	 * 获取版本
	 * @param context
	 * @return
	 */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode + "";
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取应用程序名称 
     */
     public static String getAppName(Context context) {
         try {
             PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
             int labelRes = packageInfo.applicationInfo.labelRes;
             return context.getResources().getString(labelRes);
         } catch (NameNotFoundException e) {
             e.printStackTrace();
         }
         return null;
     }


    /**
     * whether application is in background
     * <ul>
     * <li>need use permission android.permission.GET_TASKS in Manifest.xml</li>
     * </ul>
     * @param context
     * @return if application is in background return true, otherwise return false
     */
    public static boolean isApplicationInBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName topActivity = taskList.get(0).topActivity;
            if (topActivity != null && !topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }

    
    /**
     * 获取 topActivity ComponentName
     * @param context
     * @return
     */
    public static ComponentName getTopComponentName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List< RunningTaskInfo > taskInfo = am.getRunningTasks(1);
        ComponentName componentName = taskInfo.get(0).topActivity;
        return componentName;
    }

    /**
     * 获取AndroidId
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
    	ContentResolver cr = context.getContentResolver();
		String androidid = Settings.System.getString(cr, Settings.Secure.ANDROID_ID);
		if (TextUtils.isEmpty(androidid)) {
			androidid = "unknow";
		}
		return androidid;
    }

    /**
     * activity 是否在最上面
     * @param context
     * @param c
     * @return
     */
    public static boolean isTopActivity(Context context, Class c) {
        boolean isTop = false;
        try {
            ActivityManager am = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
            ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
            if (cn.getClassName().contains(c.getCanonicalName())) {
                isTop = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return isTop;
    }

    /**
     * need < uses-permission android:name =“android.permission.GET_TASKS” />
     * 判断是否前台运行
     */
    public static boolean isRunningForeground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> taskList = am.getRunningTasks(1);
        if (taskList != null && !taskList.isEmpty()) {
            ComponentName componentName = taskList.get(0).topActivity;
            if (componentName != null && componentName.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取正在运行的任务这里一定要注意，这里我们获取的时候，
     * 你的任务或者其中的activity可能没结束，但是当你在后边使用的时候，很有可能已经被kill了哦。
     * 意思很简单，系统返给你的正在运行的task，是暂态的，仅仅代表你调用该方法时系统中的状态，
     * 至于后边是否发生了该变，系统概不负责、
     * 返回true表示在前台运行，
     * 返回false表示activity没有运行
     */
    public static boolean isActivityRunning(Context context, String activityName) {
        ActivityManager activityManager = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager == null) {
            return false;
        }
        List<RunningTaskInfo> runningTasks = activityManager.getRunningTasks(50);
        if (runningTasks != null) {
            for (RunningTaskInfo taskInfo : runningTasks) {
                String info = taskInfo.baseActivity.getClassName();
                if (info.equals(activityName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 判断服务是否正在运行
     * @param mContext
     * @param serviceName
     * @return
     */
    public static boolean isServiceRunning(Context mContext, String serviceName) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningServiceInfo service : list) {
            String mName = service.service.getClassName().toString();
            if (serviceName.equals(mName)) {
                isRunning = true;
                return isRunning;
            }
        }

        return isRunning;
    }

    /**
     * 系统是否处在锁屏状态：
     * @param mContext
     * @return
     */
    public static boolean isLocked(Context mContext){
        KeyguardManager km = (KeyguardManager) mContext.getSystemService(Context.KEYGUARD_SERVICE);
        return km.inKeyguardRestrictedInputMode();
    }




    /**
     * 判断应用是否安装
     * @param context
     * @param pkgName
     * @return
     */
    public static boolean isAppInstalled(Context context, String pkgName) {
        if (TextUtils.isEmpty(pkgName)) {
            return false;
        }
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (Exception e) {
//            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }





    public static String getInstall(Context context){
        PackageManager pm = context.getPackageManager();
        if(pm != null){
            try {
                PackageInfo pkgInfo = pm.getPackageInfo(context.getPackageName(), 0);
                return pkgInfo.applicationInfo.sourceDir;
            } catch (NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return  "";
    }


    /**
     * 客户端目录：
         1.system目录,
         2.data目录,
         3.sd卡目录,
         4.vendor目录
         5.system priv目录
     * @return
     */
    public static int getInstallPath(Context context) {
        int localtion = 0;
        try {
            PackageManager pm = context.getPackageManager();
            if (pm != null) {
                PackageInfo pkgInfo = pm.getPackageInfo(context.getPackageName(), 0);
                if (pkgInfo != null) {
                    if ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0 ||
                            ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0)) {
                        if (pkgInfo.applicationInfo.sourceDir.indexOf("priv-app") != -1) {
                            localtion = 5;
                        } else if (pkgInfo.applicationInfo.sourceDir.indexOf("data") != -1){
                            localtion = 2;
                        } else {
                            localtion = 1;
                        }
                    } else if (((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0)
                            && ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0)) {
                        if (pkgInfo.applicationInfo.sourceDir.indexOf("vendor") == -1) {
                            localtion = 2;
                        }
                    } else if (((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0) &&
                            ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0)) {
                        localtion = 3;
                    } else if (((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) <= 0)
                            && ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == 0)) {
                        if (pkgInfo.applicationInfo.sourceDir.indexOf("vendor") != -1) {
                            localtion = 4;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return localtion;
    }

    /**
     * 判断手机是否root，不弹出root请求框<br/>
     */
    public static boolean isRoot() {
        try {
            String binPath = "/system/bin/su";
            String xBinPath = "/system/xbin/su";
            if (new File(binPath).exists() && isExecutable(binPath))
                return true;
            if (new File(xBinPath).exists() && isExecutable(xBinPath))
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
	private static Process process;
	public static boolean haveRoot() {
		try {
			process = Runtime.getRuntime().exec("su");
			process.getOutputStream().write("exit\n".getBytes());
			process.getOutputStream().flush();
			int i = process.waitFor();
			if (0 == i) {
				process = Runtime.getRuntime().exec("su");
				return true;
			}

		} catch (Exception e) {
			return false;
		}
		return false;

	}

    private static boolean isExecutable(String filePath) {
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ls -l " + filePath);
            // 获取返回内容
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String str = in.readLine();
            ILog.wr("AppUtil, isExecutable str = " + str);
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x')
                    return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if (p != null) {
                    // use exitValue() to determine if process is still running.
                    p.exitValue();
                }
            } catch (IllegalThreadStateException e) {
                // process is still running, kill it.
//                try {
//                    p.destroy();
//                }
//                catch (Exception ex) {
//                }
//                catch (Throwable er) {
//                }
            }
        }
        return false;
    }

    public static boolean isScreenOn(Context context){
        PowerManager powerManager = (PowerManager) context.getSystemService(POWER_SERVICE);
        boolean isScreenOn;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            isScreenOn = powerManager.isInteractive();
        } else {
            isScreenOn = powerManager.isScreenOn();
        }
        return isScreenOn;
    }

    /**
     * 判断Service是否重启
     *
     * @param serviceName Service所在的进程名
     * @return true:重启 false:非重启
     */
    public static boolean isServiceRestart(Context context, String serviceName) {
        try {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningServiceInfo> list = activityManager.getRunningServices(200);
            if (list.size() <= 0) {
                return false;
            }
            for (ActivityManager.RunningServiceInfo service : list) {
                String mName = service.service.getClassName().toString();
                if (serviceName.equals(mName)) {
                    long mSleepTime = SystemClock.elapsedRealtime() - SystemClock.uptimeMillis(); //计算当前系统休眠的时间
                    long mRestartTime = service.activeSince - service.lastActivityTime; //计算Service已经启动的时间
                    /** 通过时间差判断是否是重新启动的Service */
                    if (Math.abs(mRestartTime - mSleepTime) < 100) {
                        return true;
                    }
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    /**
     * 判断应用是否有root权限
     * @return
     */
    public static boolean appIsHasRootPermission() {
        Process process = null;
        DataOutputStream os = null;
        try {
            String cmd = "touch /data/roottest.txt";
            process = Runtime.getRuntime().exec("su"); //切换到root帐号
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(cmd + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        return true;
    }


    /**
     * 获取系统总内存 mb (取整)
     * @param context
     * @return
     */
    public static int getTotalMemory(Context context) {
        String str1 = "/proc/meminfo"; // 系统内存信息文件
        String str2;
        String[] arrayOfString;
        int memorySize = 0;

        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小

            arrayOfString = str2.split("\\s+");

            memorySize = Integer.valueOf(arrayOfString[1]).intValue();// 获得系统总内存，单位是KB
            localBufferedReader.close();

            if (memorySize > 1024) {
                memorySize = memorySize / 1024;
            } else if (memorySize > 0) {
                memorySize = 1;
            }
        } catch (IOException e) {

        }
        return memorySize;
    }

	// You may uncomment next line if using Android Annotations library,
	// otherwise just be sure to run it in on the UI thread
	// @UiThread
	public static String getDefaultUserAgentString(Context context) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            ILog.e(TAG,"获取默认设置");
			return WebSettings.getDefaultUserAgent(context);
		}

		try {
			Constructor<WebSettings> constructor = WebSettings.class.getDeclaredConstructor(Context.class, WebView.class);
			constructor.setAccessible(true);
			try {
				WebSettings settings = constructor.newInstance(context, null);
				return settings.getUserAgentString();
			} finally {
				constructor.setAccessible(false);
			}
		} catch (Exception e) {
			return new WebView(context).getSettings().getUserAgentString();
		}
	}

	@TargetApi(17)
	static class NewApiWrapper {
		static String getDefaultUserAgent(Context context) {
			return WebSettings.getDefaultUserAgent(context);
		}
	}



}

