package main.demo.mobads.baidu.com.androidutils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.BatteryManager;
import android.os.Build;
import android.text.TextUtils;
import android.view.WindowManager;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author jiangrenming
 * @date 2018/8/22
 * android 常用工具类
 */

public class AndroidUtils {


    private static ConnectivityManager connectivityManager;
    private static NetworkInfo getNetworkInfo(Context context) {
        if (connectivityManager == null) {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        }
        return connectivityManager.getActiveNetworkInfo();
    }

    /**
     * 判断是否联网，无视联网方式
     * @param context
     * @return
     */
    public static boolean isHaveInternet(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是wifi方式联网
     * @param context
     * @return
     */
    public static boolean isWifiConnect(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否为手机的联网方式（GPRS,UMTS等）
     * @param context
     * @return
     */
    public static boolean isMobileConnect(Context context) {
        NetworkInfo info = getNetworkInfo(context);
        if (info != null && info.getType() == ConnectivityManager.TYPE_MOBILE && info.isConnected()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 是否安装了指定的应用。
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isInstalled(Context context, String packageName) {
        if (context != null && !TextUtils.isEmpty(packageName)) {
            PackageManager packageManager = context.getPackageManager();
            List<PackageInfo> infoList = packageManager.getInstalledPackages(0);
            for (PackageInfo packageInfo : infoList) {
                if (TextUtils.equals(packageInfo.packageName, packageName)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 获取当前应用版本名。
     * @param context
     * @return 当前应用版本号，在androidMainfest.xml中配置的版本号
     */
    public static String getApplicationVersionName(Context context) {
        if (context != null) {
            return getAppVersionName(context, context.getPackageName());
        }
        return null;
    }


    /**
     * 获取应用版本名。
     * @param context
     * @param packageName
     * @return
     */
    public static String getAppVersionName(Context context, String packageName) {
        if (context == null || TextUtils.isEmpty(packageName))
            return null;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 是否是新版本（相对于指定版本）。
     * @param context
     * @param packageName
     * @param versionCode
     * @return
     */
    public static boolean isNewVersion(Context context, String packageName, int versionCode) {
        return getAppVersionCode(context, packageName) >= versionCode;
    }
    /**
     * 获取应用版本号。
     * @param packageName
     * @return
     */
    public static int getAppVersionCode(Context context, String packageName) {
        if (context == null || TextUtils.isEmpty(packageName))
            return -1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }


    /**
     * 判断是否低电量且处于非充电状态
     */
    public static boolean isLowEleAndNotChargin(Context context, final int lowEleLevel) {
        final ResultLock lock = new ResultLock();
        IntentFilter mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mIntentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        mIntentFilter.addAction(Intent.ACTION_PROVIDER_CHANGED);
        BroadcastReceiver batteryBroadcast = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.equals(Intent.ACTION_BATTERY_CHANGED)) {
                    context.unregisterReceiver(this);
                    // 电池状态，返回是一个数字
                    // BatteryManager.BATTERY_STATUS_UNKNOWN 1 未知
                    // BatteryManager.BATTERY_STATUS_CHARGING 2 表示是充电状态
                    // BatteryManager.BATTERY_STATUS_DISCHARGING 3 放电中
                    // BatteryManager.BATTERY_STATUS_NOT_CHARGING 4 未充电
                    // BatteryManager.BATTERY_STATUS_FULL 5 电池满
                    int batteryStatus = intent.getIntExtra("status", 0); // 电池状态
                    int scale = intent.getIntExtra("scale", 0); // 电池最大容量
                    int level = intent.getIntExtra("level", 0); // 电池的电量，数字
                    double batteryPercent = 1.0 * level / scale;
                    if (batteryPercent < lowEleLevel * 0.01 && batteryStatus != BatteryManager.BATTERY_STATUS_CHARGING) {
                        lock.setResult(true);
                    }
                    lock.getLatch().countDown();
                }
            }
        };
        context.registerReceiver(batteryBroadcast, mIntentFilter);
        try {
            lock.getLatch().await();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lock.getResult();
    }

    /**
     * 将隐式启动转换为显示启动
     * @param context
     * @param implicitIntent
     * @return
     */
    public static Intent getExplicitIntent(Context context, Intent implicitIntent) {
        PackageManager pm = context.getPackageManager();
        List<ResolveInfo> resolveInfo = pm.queryIntentServices(implicitIntent, 0);
        if (resolveInfo == null || resolveInfo.size() == 0) {
            return null;
        }
        ResolveInfo serviceInfo = resolveInfo.get(0);
        String packageName = serviceInfo.serviceInfo.packageName;
        String className = serviceInfo.serviceInfo.name;
        ComponentName component = new ComponentName(packageName, className);
        Intent explicitIntent = new Intent(implicitIntent);
        explicitIntent.setComponent(component);
        return explicitIntent;
    }

    /**
     * 是否切换到 后台了
     * @param context
     * @return false 当前应用在前台，true 当前应用在后头
     */
    public static boolean isInBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * 应用立即重启
     *
     * @param context
     */
    public static void reStartApp(Context context) {
        reStartApp(context, 0);
    }

    /**
     * 应用延时重启
     * @param context
     * @param startDelay 启动延迟(单位毫秒)
     */
    public static void reStartApp(Context context, long startDelay) {
        //用本应用的包名获取本应用的启动Intent
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        PendingIntent restartIntent = PendingIntent.getActivity(context.getApplicationContext(), -1, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + startDelay, restartIntent);
    }

    /**
     * 判断某个服务是否还在运行
     * @return
     */
    public static boolean isRunningService(Context context, String className) {
        boolean isRunning = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(Integer.MAX_VALUE);
        if (runningServices != null && runningServices.size() > 0) {
            for (ActivityManager.RunningServiceInfo service : runningServices) {
                if (className.equals(service.service.getClassName())) {
                    isRunning = true;
                    break;
                }
            }
        }
        return isRunning;
    }

    /**
     * 设置activity界面上的状态栏是否显示
     * @param activity
     * @param isVisiable
     */
    public static void setStatusBarVisiable(Activity activity, boolean isVisiable) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        if (isVisiable) {
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            activity.getWindow().setAttributes(lp);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } else {
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().setAttributes(lp);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    /**
     * API >5.0 获取运行在栈顶的应用包名
     * @param context
     * @return
     */
    public String getTopActivtyFromLolipopOnwards(Context context){
        String topPackageName = null ;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager)context.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000*10, time);
            if(stats != null) {
                SortedMap<Long,UsageStats> mySortedMap = new TreeMap<Long,UsageStats>();
                for (UsageStats usageStats : stats) {
                    mySortedMap.put(usageStats.getLastTimeUsed(),usageStats);
                }
                if(mySortedMap != null && !mySortedMap.isEmpty()) {
                    topPackageName =  mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        }
        return topPackageName;
    }



}
