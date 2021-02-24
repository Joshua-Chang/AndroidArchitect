package org.devio.as.proj.libbreakpad;

/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/18 2:17 PM
 * @创建人：常守达
 * @备注：
 */
public class NativeCrashHandler {
    static {
        System.loadLibrary("breakpad-core");
    }
    public static void init(String crashDir){
        initBreakPad(crashDir);
    }

    private static native void initBreakPad(String crashDir);
}
