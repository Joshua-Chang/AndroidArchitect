package org.devio.as.hi.hi_concurrent_demo.concurrent;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicDemo {
    public static void main(String[] args) throws InterruptedException {
        AtomicTask task=new AtomicTask();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10000; i++) {
                    task.incrementVolatile();
                    task.incrementAtomic();
                }
            }
        };
        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("\n"+task.atomicInteger.get()+"\n"+task.volatileCount);
    }
    static class AtomicTask{
        AtomicInteger atomicInteger=new AtomicInteger();
        volatile int volatileCount=0;
        void incrementAtomic(){
            atomicInteger.getAndIncrement();
        }
        void incrementVolatile(){
            // volatile修饰的成员变量在每次被线程访问时，都从共享内存重新读取该成员的值，
            // 而当成员变量值发生变化时，将变化的值重新写入共享内存
            volatileCount++;
            //不能解决非原子操作的线程安全性。性能不及原子类高
        }
    }
}
