package org.devio.design.pattern.singleton;

public class Singleton2Java {
    private static Singleton2Java instance;

    private Singleton2Java() {/*构造方法私有化*/
    }

    public static synchronized Singleton2Java getInstance() {
        if (instance == null) {
            instance=new Singleton2Java();
        }
        return instance;
    }
}
