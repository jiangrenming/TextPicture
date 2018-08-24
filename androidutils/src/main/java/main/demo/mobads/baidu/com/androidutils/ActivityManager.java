package main.demo.mobads.baidu.com.androidutils;

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity管理器
 *
 * @author CB
 * @time 2014年10月27日 上午9:46:06
 */
public class ActivityManager {

    /**
     * 保存所有的activity，在程序退出的时候集中销毁，完全退出程序
     */
    private static List<Activity> activitys = new ArrayList<>();

    /**
     * 添加Activity到容器中
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        activitys.add(activity);
    }

    /**
     * 把Activity从容器中移除
     *
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        activitys.remove(activity);
    }

    /**
     * 结束(finish)所有的activity
     */
    public static void finishAllActivity() {
        for (Activity activity : activitys) {
            if (activity != null) {//退出的时候，需要销毁之前所有的界面，这样做主要是为了清理缓存的界面，更时将数据更新为新登录的用户,并不是程序完全退出
                activity.finish();
            }
        }
    }

    /**
     * 关闭除栈底的其他Activity。
     */
    public static void finishAllActivityExcludeBottomActivity() {
        for (int i = 1; i < activitys.size(); i++) {
            Activity activity = activitys.get(i);
            if (activity != null) {
                activity.finish();
            }
        }
    }

    /**
     * 返回当前activity的数量
     */
    public static int size() {
        return activitys.size();
    }
}
