package org.devio.hi.library.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Process;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import java.util.ArrayDeque;
import java.util.List;

public class HiViewUtil{
    public static <T> T findTypeView(@Nullable ViewGroup group, Class<T>cls){
        if (group==null) {
            return null;
        }
        ArrayDeque<View> deque = new ArrayDeque<>();//双端队列
        deque.add(group);
        while (!deque.isEmpty()) {
            View node = deque.removeFirst();
            if (cls.isInstance(node)) {
                return cls.cast(node);
            }else if (node instanceof ViewGroup){
                ViewGroup container = (ViewGroup) node;
                int count = container.getChildCount();
                for (int i = 0; i < count; i++) {
                    deque.add(container.getChildAt(i));
                }
            }
        }
        return null;
    }
    public static boolean isActivityDestroyed(Context context) {
        Activity activity = findActivity(context);
        if (activity != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                return activity.isDestroyed() || activity.isFinishing();
            }

            return activity.isFinishing();
        }

        return true;
    }
    public static Activity findActivity(Context context) {
        //怎么判断context 是不是activity 类型的
        if (context instanceof Activity) return (Activity) context;
        /*ContextWrapper context的子类包装类，包装作用，子类有TintContextWrapper，用于appcompat控件加载不同的resource*/
        /**
         * @see androidx.fragment.app.FragmentManager
         */
        else if (context instanceof ContextWrapper) {
            return findActivity(((ContextWrapper) context).getBaseContext());
        }
        return null;
    }
    //检测是否是浅色主题
    public static boolean lightMode() {
        int mode = AppGlobals.INSTANCE.get().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return mode == Configuration.UI_MODE_NIGHT_NO;
    }
    public static boolean inMainProcess(Application application) {

        int myPid = Process.myPid();
        android.app.ActivityManager activityManager = (android.app.ActivityManager) application.getSystemService(Context.ACTIVITY_SERVICE);
        List<android.app.ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (android.app.ActivityManager.RunningAppProcessInfo process : runningAppProcesses) {
            if (process.processName.equals(application.getPackageName())) {
                return true;
            }
        }
        return false;
    }
}
