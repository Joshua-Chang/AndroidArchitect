package org.devio.design.pattern.singleton;

public class Singleton4Java {
    /**
     * 推荐静态内部类实现单例
     * 既能保证线程安全，又能保证唯一性，
     * 又能保证单例延迟实例化：外部类加载时不会立即加载内部类
     * Singleton4Java加载并不会实例化SingletonProvider，getInstance才会创建Singleton4Java
     */
    private Singleton4Java() {
    }

    /*静态内部类*/
    private static class SingletonProvider{
        private static Singleton4Java instance=new Singleton4Java();
    }

    public static Singleton4Java getInstance(){
        return SingletonProvider.instance;
    }
}
