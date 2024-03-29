package org.devio.as.proj.main;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;

import org.devio.as.proj.ability.HiAbility;
import org.devio.as.proj.common.flutter.HiFlutterCacheManager;
import org.devio.as.proj.common.ui.component.HiBaseApplication;
import org.devio.hi.library.crash.CrashMgr;
import org.devio.hi.library.log.HiConsolePrinter;
import org.devio.hi.library.log.HiLogConfig;
import org.devio.hi.library.log.HiLogManager;
import org.devio.hi.library.util.ActivityManager;
import org.devio.hi.library.crash.CrashHandler;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class HiApplication extends HiBaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        HiLogManager.init(new HiLogConfig() {
            @Override
            public JsonParser injectJsonParser() {
                return src -> new Gson().toJson(src);
            }
        }, new HiConsolePrinter());
        ActivityManager.getInstance().init(this);
        ARouter.init(this);
//        HiFlutterCacheManager.getInstance().preLoad(this);
        HiFlutterCacheManager.getInstance().preLoadDartVM(this);
//        CrashMgr.INSTANCE.init();
//        HiAbility.INSTANCE.init(this,"Umeng",null);
    }
}
