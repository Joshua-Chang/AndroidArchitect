package org.devio.as.proj.main;

import com.alibaba.android.arouter.launcher.ARouter;
import com.google.gson.Gson;

import org.devio.as.proj.common.flutter.HiFlutterCacheManager;
import org.devio.as.proj.common.ui.component.HiBaseApplication;
import org.devio.hi.library.log.HiConsolePrinter;
import org.devio.hi.library.log.HiLogConfig;
import org.devio.hi.library.log.HiLogManager;

public class HiApplication extends HiBaseApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.init(this);
        HiFlutterCacheManager.getInstance().preLoad(this);
        HiLogManager.init(new HiLogConfig() {
            @Override
            public JsonParser injectJsonParser() {
                return src -> new Gson().toJson(src);
            }
        }, new HiConsolePrinter());
    }
}
