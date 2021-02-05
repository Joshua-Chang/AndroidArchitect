package org.devio.design.pattern.singleton;

public class Singleton3Java {
    private volatile static Singleton3Java instance;

    private Singleton3Java() {/*构造方法私有化*/
    }
    /*double check*/
    public static Singleton3Java getInstance() {
        if (instance == null) {
            synchronized (Singleton3Java.class){
                if (instance == null) {
                    instance=new Singleton3Java();
                }
            }
        }
        return instance;
    }
}
